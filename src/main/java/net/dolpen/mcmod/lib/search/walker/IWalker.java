package net.dolpen.mcmod.lib.search.walker;

import net.minecraft.util.math.BlockPos;

import java.util.Set;
import java.util.function.Function;

@FunctionalInterface
public interface IWalker extends Function<BlockPos, Set<BlockPos>> {

}
