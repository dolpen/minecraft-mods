package net.dolpen.mcmod.ext.handler;

import net.dolpen.mcmod.ext.DolpenMain;
import net.dolpen.mcmod.ext.capability.player.IPlayerStatus;
import net.dolpen.mcmod.ext.capability.player.PlayerStatusHolder;
import net.dolpen.mcmod.ext.gui.GuiHandler;
import net.dolpen.mcmod.ext.mod.BlockStateGroup;
import net.dolpen.mcmod.ext.mod.CustomBlocks;
import net.dolpen.mcmod.ext.mod.CustomTiles;
import net.dolpen.mcmod.ext.network.ToggleMessage;
import net.dolpen.mcmod.ext.setting.Constants;
import net.dolpen.mcmod.ext.task.block.CultivateAll;
import net.dolpen.mcmod.ext.task.block.CutAll;
import net.dolpen.mcmod.ext.task.block.DigAll;
import net.dolpen.mcmod.ext.task.block.MineAll;
import net.dolpen.mcmod.ext.tile.TileStorage;
import net.dolpen.mcmod.lib.capability.CapabilityAttacher;
import net.dolpen.mcmod.lib.network.NetworkManager;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;


/**
 * サーバー or ローカルのワールド所有者側で処理して欲しいイベントハンドリング
 * 性質上、キー処理などのクライアントにしか存在しない処理はさせてはいけない
 * 一方で、EntityPlayerはEntityPlayerMPであることが保証されるかどうかは注意深く確認する
 */
public abstract class UniversalEventHandler {

    final NetworkManager networkManager;
    final CapabilityAttacher capabilityAttacher;

    protected UniversalEventHandler() {
        // 通信
        networkManager = new NetworkManager(Constants.MOD_ID);
        capabilityAttacher = new CapabilityAttacher();
        capabilityAttacher.player.register(
                e -> true,
                e -> e.addCapability(PlayerStatusHolder.LOCATION, PlayerStatusHolder.getProvider())
        );
    }

    public void initNetwork() {
        networkManager.registerServerRequest(ToggleHandler.class, ToggleMessage.class);
        NetworkRegistry.INSTANCE.registerGuiHandler(DolpenMain.getInstance(), new GuiHandler());
        PlayerStatusHolder.register();
        CustomTiles.registerAllTile();
    }

    private void doTask(World world, Runnable runnable) {
        if (Objects.isNull(world) || Objects.isNull(world.getMinecraftServer())) {
            runnable.run();
        } else {
            world.getMinecraftServer().addScheduledTask(runnable);
        }
    }


    private boolean checkCapability(EntityPlayer player, Predicate<IPlayerStatus> query) {
        return Optional.ofNullable(
                player.getCapability(PlayerStatusHolder.PLAYER_STATUS_CAPABILITY, null)
        ).filter(
                query
        ).isPresent();
    }


    @SubscribeEvent
    public void registerItem(RegistryEvent.Register<Item> event) {
        CustomBlocks.registerAllItemBlock(event.getRegistry());
    }

    @SubscribeEvent
    public void registerBlock(RegistryEvent.Register<Block> event) {
        CustomBlocks.registerAllBlock(event.getRegistry());
    }


    @SubscribeEvent
    public void onAttachingEntity(AttachCapabilitiesEvent<Entity> event) {
        capabilityAttacher.handleEntity(event);
    }

    /**
     * 一括灌漑
     *
     * @param event 耕すアクション
     */
    @SubscribeEvent
    public void onCultivate(UseHoeEvent event) {
        World world = event.getWorld();
        if (world.isRemote) return;
        EntityPlayer player = event.getEntityPlayer();
        if (!checkCapability(player, IPlayerStatus::isChainBlockActionEnabled)) return;
        final BlockPos pos = event.getPos();
        final IBlockState blockState = world.getBlockState(pos);
        if (BlockStateGroup.HOE_TRIGGER.contains(blockState)) {
            doTask(world, new CultivateAll(world, player, pos, blockState));
        }
    }

    /**
     * 一括破壊
     *
     * @param event 破壊アクション
     */
    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        World world = event.getWorld();
        if (world.isRemote) return;
        EntityPlayer player = event.getPlayer();
        if (!checkCapability(player, IPlayerStatus::isChainBlockActionEnabled)) {
            return;
        }
        final IBlockState blockState = event.getState();
        ItemStack held = player.getHeldItemMainhand();
        if (held.isEmpty()) {
            return;
        }
        boolean usedSuitableTool = held.getItem().getToolClasses(held).stream().anyMatch(
                toolClass -> toolClass.equals(blockState.getBlock().getHarvestTool(blockState))
        );
        if (!usedSuitableTool) {
            //player.sendMessage(new TextComponentString("not suitable ..."));
            return;
        }
        if (BlockStateGroup.CUT_TRIGGER.contains(blockState)) {
            doTask(world, new CutAll(world, player, event.getPos(), blockState));
        } else if (BlockStateGroup.DIG_TRIGGER.contains(blockState)) {
            doTask(world, new DigAll(world, player, event.getPos(), blockState));
        } else if (BlockStateGroup.MINE_TRIGGER.contains(blockState)) {
            doTask(world, new MineAll(world, player, event.getPos(), blockState));
        }
    }

    // メッセージを受けてCapabilityの一括破壊フラグを反転する
    public static class ToggleHandler implements IMessageHandler<ToggleMessage, IMessage> {

        public ToggleHandler() {
        }

        @Override
        public IMessage onMessage(ToggleMessage message, MessageContext ctx) {
            EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
            IPlayerStatus playerStatus = serverPlayer
                    .getCapability(PlayerStatusHolder.PLAYER_STATUS_CAPABILITY, null);
            if (playerStatus == null) return null;
            playerStatus.toggleChainBlockAction();
            serverPlayer.sendMessage(new TextComponentTranslation(playerStatus.toStatusMessageKey()));
            return null;
        }
    }
}
