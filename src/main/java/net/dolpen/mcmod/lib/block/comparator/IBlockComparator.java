package net.dolpen.mcmod.lib.block.comparator;

import net.minecraft.block.state.IBlockState;

@FunctionalInterface
public interface IBlockComparator {

    boolean applyAsBoolean(IBlockState seeingBlockState, IBlockState baseBlockState);

}
