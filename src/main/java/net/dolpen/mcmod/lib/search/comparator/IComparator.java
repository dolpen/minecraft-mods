package net.dolpen.mcmod.lib.search.comparator;

@FunctionalInterface
public interface IComparator<T> {

    boolean applyAsBoolean(T thisObject, T thatObject);

}
