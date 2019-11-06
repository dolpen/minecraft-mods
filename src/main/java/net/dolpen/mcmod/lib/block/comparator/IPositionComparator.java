package net.dolpen.mcmod.lib.block.comparator;

import net.minecraft.util.math.BlockPos;

@FunctionalInterface
public interface IPositionComparator {

    boolean applyAsBoolean(BlockPos seeingPos, BlockPos basePos);

}
