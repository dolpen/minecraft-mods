package net.dolpen.mcmod.ext.block;

import net.dolpen.mcmod.ext.DolpenMain;
import net.dolpen.mcmod.ext.gui.GuiHandler;
import net.dolpen.mcmod.ext.tile.TileStorage;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class BlockStorage extends Block implements ITileEntityProvider {

    public BlockStorage() {
        super(new Material(MapColor.STONE));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setResistance(30.0F);
        this.setDefaultState(this.blockState.getBaseState());
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
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) return true;
        TileStorage tile = (TileStorage) world.getTileEntity(pos);
        if (Objects.isNull(tile)) return true;
        if (!player.isSneaking()) {
            tile.sync(player);
            player.openGui(DolpenMain.getInstance(), GuiHandler.SSS, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }


    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity tileEntity, ItemStack stack) {
        if(world.isRemote)return;
        if (!(tileEntity instanceof TileStorage)) {
            super.harvestBlock(world, player, pos, state, tileEntity, stack);
        } else {
            TileStorage tileStorage = (TileStorage) tileEntity;
            float xOffset = world.rand.nextFloat() * 0.8F + 0.1F;
            float yOffset = world.rand.nextFloat() * 0.8F + 0.1F;
            float zOffset = world.rand.nextFloat() * 0.8F + 0.1F;
            ItemStack nbtStack = tileStorage.getDropWithNBT(this);
            EntityItem entityitem = new EntityItem(world, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, nbtStack.splitStack(1));
            world.spawnEntity(entityitem);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if(world.isRemote)return;
        TileStorage tileStorage = (TileStorage) world.getTileEntity(pos);
        if (Objects.isNull(tileStorage)) return;
        tileStorage.readFromNBTItemStack(stack);
        world.notifyBlockUpdate(pos, state, state, 3);
    }


    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileStorage();
    }


}