package net.dolpen.mcmod.lib.block.filter;


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class ClassMatchRule<T extends Block> implements IMatchRule {


    public final Class<T> parent;

    private ClassMatchRule(Class<T> parent) {
        this.parent = parent;
    }

    public static <T extends Block> ClassMatchRule<T> of(Class<T> parent) {
        return new ClassMatchRule<>(parent);
    }

    @Override
    public boolean test(IBlockState blockState) {
        return parent.isAssignableFrom(blockState.getBlock().getClass());
    }
}
