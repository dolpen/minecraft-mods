package net.dolpen.mcmod.ext.entities.blocks;

import net.dolpen.mcmod.ext.DolpenMain;
import net.dolpen.mcmod.ext.entities.tiles.TileSingleSlotStorage;
import net.dolpen.mcmod.ext.gui.GuiHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class BlockSingleSlotStorage extends BlockInventory {

    public BlockSingleSlotStorage() {
        super(Material.IRON);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setLightLevel(1.0F);
        this.setHardness(3.0F);
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
        TileSingleSlotStorage tile = (TileSingleSlotStorage) world.getTileEntity(pos);
        if (Objects.isNull(tile)) return true;
        if (!player.isSneaking()) {
            player.openGui(DolpenMain.getInstance(), GuiHandler.SSS, world, pos.getX(), pos.getY(), pos.getZ());
        } else {
            player.sendMessage(new TextComponentString(
                    tile.getStackInSlot(0).toString()
            ));
        }
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileSingleSlotStorage();
    }
}