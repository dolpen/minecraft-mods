package net.dolpen.mcmod.lib.search.filter.state;


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class BlockClassFilter<T extends Block> implements IBlockStateFilter {


    public final Class<T> parent;

    private BlockClassFilter(Class<T> parent) {
        this.parent = parent;
    }

    public static <T extends Block> BlockClassFilter<T> of(Class<T> parent) {
        return new BlockClassFilter<>(parent);
    }

    @Override
    public boolean test(IBlockState blockState) {
        return parent.isAssignableFrom(blockState.getBlock().getClass());
    }
}
