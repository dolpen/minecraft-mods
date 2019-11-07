package net.dolpen.mcmod.lib.block.comparator;

@FunctionalInterface
public interface IComparator<T> {

    boolean applyAsBoolean(T thisObject, T thatObject);

}
