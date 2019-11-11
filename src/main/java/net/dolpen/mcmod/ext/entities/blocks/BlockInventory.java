package net.dolpen.mcmod.ext.entities.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class BlockInventory extends Block implements ITileEntityProvider {

    public BlockInventory(Material blockMaterialIn) {
        super(blockMaterialIn);
    }

    @Nullable
    @Override
    public abstract TileEntity createNewTileEntity(World worldIn, int meta);
}
