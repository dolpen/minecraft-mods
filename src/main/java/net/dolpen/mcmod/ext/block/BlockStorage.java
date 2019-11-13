package net.dolpen.mcmod.ext.block;

import net.dolpen.mcmod.ext.DolpenMain;
import net.dolpen.mcmod.ext.gui.GuiHandler;
import net.dolpen.mcmod.ext.tile.StorageHandler;
import net.dolpen.mcmod.ext.tile.TileStorage;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class BlockStorage extends Block implements ITileEntityProvider {

    public BlockStorage() {
        super(new Material(MapColor.STONE));
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setHardness(2.0F);
        setResistance(30.0F);
        setDefaultState(this.blockState.getBaseState());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
        if (tab == this.getCreativeTabToDisplayOn()) {
            list.add(new ItemStack(this, 1, 0));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(TextFormatting.LIGHT_PURPLE + displayNBTItemStack(stack));
    }

    public String displayNBTItemStack(ItemStack itemStack) {
        if (itemStack.isEmpty() || !itemStack.hasTagCompound()) return I18n.format("inventory.item.empty");
        NBTTagCompound tileInfo = itemStack.getTagCompound().getCompoundTag("tileInfo");
        NBTTagList tags = tileInfo.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        NBTTagCompound pool = tags.getCompoundTagAt(StorageHandler.POOL);
        NBTTagCompound out = tags.getCompoundTagAt(StorageHandler.OUT);
        int count = pool.getInteger("Quantity") + out.getInteger("Quantity");
        ItemStack dummy = new ItemStack(pool);
        dummy.setCount(count);
        return dummy.isEmpty()
                ? I18n.format("inventory.item.empty")
                : I18n.format("inventory.item.stack", dummy.getCount(), dummy.getDisplayName());
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) return true;
        getTileStorage(world, pos).ifPresent(tileStorage -> {
            if (!player.isSneaking()) {
                player.openGui(
                        DolpenMain.getInstance(),
                        GuiHandler.SSS,
                        world, pos.getX(), pos.getY(), pos.getZ()
                );
            }
        });
        return true;
    }


    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity tileEntity, ItemStack stack) {
        Optional<TileStorage> tileStorageOptional = getTileStorage(world, pos);
        if (!tileStorageOptional.isPresent()) {
            super.harvestBlock(world, player, pos, state, tileEntity, stack);
            return;
        }
        tileStorageOptional.ifPresent(tileStorage -> {
            float xOffset = world.rand.nextFloat() * 0.8f + 0.1f;
            float yOffset = world.rand.nextFloat() * 0.8f + 0.1f;
            float zOffset = world.rand.nextFloat() * 0.8f + 0.1f;
            ItemStack nbtStack = tileStorage.getDropWithNBT(this);
            EntityItem entityitem = new EntityItem(world, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, nbtStack.splitStack(1));
            world.spawnEntity(entityitem);
        });
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        getTileStorage(world, pos).ifPresent(tileStorage -> {
            tileStorage.readFromNBTItemStack(stack);
            world.notifyBlockUpdate(pos, state, state, 3);
        });
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        worldIn.notifyBlockUpdate(pos, state, state, 3);
    }


    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileStorage();
    }

    private Optional<TileStorage> getTileStorage(World world, BlockPos pos) {
        return Optional.ofNullable(world.getTileEntity(pos))
                .filter(t -> t instanceof TileStorage)
                .map(t -> (TileStorage) t);
    }


}