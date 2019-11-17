package net.dolpen.mcmod.lib.search.filter.shared;


import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Set;

public class CompositeFilter<T> implements IFilter<T> {

    public final Type type;
    public final Set<IFilter<T>> filters;


    protected CompositeFilter(Type type, Collection<? extends IFilter<T>> filters) {
        this.filters = Sets.newHashSet(filters);
        this.type = type;
    }

    @Override
    public boolean test(final T obj) {
        if (this.type.equals(Type.AND)) {
            return filters.stream().allMatch(r -> r.test(obj));
        } else {
            return filters.stream().anyMatch(r -> r.test(obj));
        }
    }

    public enum Type {
        OR,
        AND
    }

}
