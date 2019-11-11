package net.dolpen.mcmod.ext.entities.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockSingleSlotStorage extends Block {

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


}