package net.dolpen.mcmod.ext.utils;

import net.dolpen.mcmod.ext.models.BlockStateGroup;
import net.dolpen.mcmod.lib.block.position.Positions;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.BlockEvent;

import java.util.Objects;

public class BlockUtil {
    /**
     * クワの耐久を消費します
     */
    private static void consumeHoeHealth(EntityPlayer player, ItemStack mainHandStack) {
        if (Objects.isNull(mainHandStack)) return;
        mainHandStack.damageItem(1, player);
        if (mainHandStack.getItemDamage() >= mainHandStack.getMaxDamage()) {
            ForgeEventFactory.onPlayerDestroyItem(player, mainHandStack, EnumHand.MAIN_HAND);
            player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
        }
    }

    /**
     * ツールの耐久を消費します
     *
     * @return 今回使用可能なら true
     */
    private static boolean consumeToolHealth(World world, EntityPlayer player, BlockPos seeingPos, IBlockState seeingBlockState, ItemStack mainHandStack) {
        if (Objects.isNull(mainHandStack)) return false;
        mainHandStack.onBlockDestroyed(world, seeingBlockState, seeingPos, player);
        if (mainHandStack.getCount() <= 0) {
            ForgeEventFactory.onPlayerDestroyItem(player, mainHandStack, EnumHand.MAIN_HAND);
            player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
        }
        return true;
    }

    /**
     * ドロップアイテムを収集します
     */
    private static void pickUpEntities(World world, BlockPos seeingPos, BlockPos myFoot) {
        world.getEntitiesWithinAABB(
                EntityItem.class,
                new AxisAlignedBB(seeingPos, seeingPos.add(1, 1, 1)).grow(1, 1, 1)
        ).forEach(entity -> {
            entity.setNoPickupDelay();
            entity.setPosition(myFoot.getX(), myFoot.getY(), myFoot.getZ());
        });
    }

    /**
     * ブロック破壊消去のコア部分（効果音再生なし）
     */
    private static boolean processDestroy(World world, BlockPos seeingPos, IBlockState seeingBlockState, boolean dropBlock) {
        Block block = seeingBlockState.getBlock();
        if (block.isAir(seeingBlockState, world, seeingPos)) {
            // double_plants の上部対策
            world.notifyBlockUpdate(seeingPos, seeingBlockState, Blocks.AIR.getDefaultState(), 3);
            world.markBlockRangeForRenderUpdate(seeingPos, seeingPos);
            return false;
        }
        // ここで音を再生しない
        // ここで Fortune を適用しないのは確定取得ブロックだから(適用すると dupe になる)
        if (dropBlock)
            block.dropBlockAsItem(world, seeingPos, seeingBlockState, 0);
        if (!world.setBlockState(seeingPos, Blocks.AIR.getDefaultState(), 3)) return false;
        // 本当にこうしないと、任意のブロックからAIRへの差分はクライアントサイド/スレッドに通知されない!!!
        world.notifyBlockUpdate(seeingPos, seeingBlockState, Blocks.AIR.getDefaultState(), 3);
        world.markBlockRangeForRenderUpdate(seeingPos, seeingPos);
        return true;

    }

    /**
     * ドロップアイテムの処理
     */
    private static void processDrop(World world, EntityPlayer player, BlockPos seeingPos, IBlockState seeingBlockState, ItemStack mainHandStack) {
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, seeingPos, seeingBlockState, player);
        Block seeingBlock = seeingBlockState.getBlock();
        BlockPos myFoot = Positions.foot(player);
        seeingBlock.onBlockHarvested(world, seeingPos, seeingBlockState, player);
        seeingBlock.onBlockDestroyedByPlayer(world, seeingPos, seeingBlockState);
        seeingBlock.harvestBlock(world, player, myFoot, seeingBlockState, null, mainHandStack);
        seeingBlock.dropXpOnBlockBreak(world, myFoot, event.getExpToDrop());
    }


    /**
     * ブロックを破壊します
     *
     * @return 今回使用可能なら true
     */
    private static boolean breakBlock(World world, EntityPlayer player, BlockPos seeingPos, IBlockState seeingBlockState, ItemStack mainHandStack, BlockPos myFoot) {
        if (!processDestroy(world, seeingPos, seeingBlockState, true)) return false;
        processDrop(world, player, seeingPos, seeingBlockState, mainHandStack);
        return true;
    }

    /**
     * ブロックを破壊して空気にします
     *
     * @return 基本的に true
     */
    private static boolean eraseBrock(World world, EntityPlayer player, BlockPos seeingPos, IBlockState seeingBlockState, ItemStack mainHandStack, BlockPos myFoot) {
        if (!processDestroy(world, seeingPos, seeingBlockState, false)) return false;
        processDrop(world, player, seeingPos, seeingBlockState, mainHandStack);
        return true;
    }

    /**
     * ブロックを耕します
     *
     * @return 今回使用可能なら true
     */
    private static boolean cultivateBlock(World world, BlockPos seeingPos) {
        if (!world.setBlockState(seeingPos, Blocks.FARMLAND.getDefaultState(), 3)) return false;
        world.scheduleBlockUpdate(seeingPos, Blocks.FARMLAND, 0, 1);
        return true;
    }


    public static boolean cultivateOnce(World world, EntityPlayer player, BlockPos seeingPos) {
        ItemStack mainHandStack = player.getHeldItemMainhand();
        consumeHoeHealth(player, mainHandStack);
        BlockPos myFoot = Positions.foot(player);
        cultivateBlock(world, seeingPos);
        BlockPos upperPos = seeingPos.up();
        IBlockState upperBlockState = world.getBlockState(upperPos);
        while (
                BlockStateGroup.HOE_REMOVABLE.contains(upperBlockState)
                        && eraseBrock(world, player, upperPos, upperBlockState, mainHandStack, myFoot)
        ) {
            upperPos = seeingPos.up();
            upperBlockState = world.getBlockState(upperPos);
        }
        return true;
    }


    public static boolean breakOnce(World world, EntityPlayer player, BlockPos seeingPos, boolean hand) {
        IBlockState seeingBlockState = world.getBlockState(seeingPos);
        ItemStack mainHandStack = hand ? ItemStack.EMPTY : player.getHeldItemMainhand();
        BlockPos myFoot = Positions.foot(player);
        if (!hand && !consumeToolHealth(world, player, seeingPos, seeingBlockState, mainHandStack)) return false;
        breakBlock(world, player, seeingPos, seeingBlockState, mainHandStack, myFoot);
        pickUpEntities(world, seeingPos, myFoot);
        return true;
    }


}
