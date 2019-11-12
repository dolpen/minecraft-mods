package net.dolpen.mcmod.lib.search.filter.state;


import com.google.common.collect.Sets;
import net.dolpen.mcmod.lib.search.filter.shared.CompositeFilter;
import net.minecraft.block.state.IBlockState;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class CompositeBlockStateFilter extends CompositeFilter<IBlockState> implements IBlockStateFilter {

    private CompositeBlockStateFilter(Type type, Collection<IBlockStateFilter> filters) {
        super(type, filters);
    }


    public static CompositeBlockStateFilter any(IBlockStateFilter... filters) {
        return of(Type.OR, filters);
    }

    public static CompositeBlockStateFilter all(IBlockStateFilter... filters) {
        return of(Type.AND, filters);
    }


    public static CompositeBlockStateFilter of(Type type, IBlockStateFilter... filters) {
        return new CompositeBlockStateFilter(
                type, Sets.newHashSet(filters)
        );
    }

    public static CompositeBlockStateFilter anyName(String... names) {
        return new CompositeBlockStateFilter(
                Type.OR, Arrays.stream(names).map(BlockStateFilter::ofName).collect(Collectors.toSet())
        );
    }


}
