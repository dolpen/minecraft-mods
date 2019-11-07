package net.dolpen.mcmod.ext.models.tasks.block;

import net.dolpen.mcmod.ext.models.tasks.ITask;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockTask implements ITask {

    protected final World world;
    protected final EntityPlayer player;
    protected final IBlockState baseBlockState;
    protected final BlockPos basePosition;

    public BlockTask(World world, EntityPlayer player, BlockPos pos, IBlockState baseBlockState) {
        this.world = world;
        this.player = player;
        this.basePosition = pos;
        this.baseBlockState = baseBlockState;
    }
}