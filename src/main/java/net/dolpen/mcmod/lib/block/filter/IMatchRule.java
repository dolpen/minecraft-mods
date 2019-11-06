package net.dolpen.mcmod.lib.block.filter;


import net.minecraft.block.state.IBlockState;

import java.util.function.Predicate;

public interface IMatchRule extends Predicate<IBlockState> {


}
