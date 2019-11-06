package net.dolpen.mcmod.lib.block.filter;


import com.google.common.collect.Sets;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;

import java.util.Set;

public class PropertyMatchRule<T extends Comparable<T>> implements IMatchRule {


    public final IProperty<T> property;
    public final Set<T> values;

    private PropertyMatchRule(IProperty<T> property, T... values) {
        this.property = property;
        this.values = Sets.newHashSet(values);
    }

    public static <T extends Comparable<T>> PropertyMatchRule<T> of(IProperty<T> property, T... values) {
        return new PropertyMatchRule<>(property, values);
    }

    @Override
    public boolean test(IBlockState blockState) {
        return blockState.getPropertyKeys().contains(property)
                && values.contains(blockState.getValue(property));
    }
}
