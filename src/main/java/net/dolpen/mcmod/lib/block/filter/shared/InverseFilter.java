package net.dolpen.mcmod.lib.block.filter.shared;

public class InverseFilter<T> implements IFilter<T> {

    public final IFilter<T> filter;

    private InverseFilter(IFilter<T> filter) {
        this.filter = filter;
    }


    public static <T, U extends IFilter<T>> InverseFilter<T> not(U filter) {
        return new InverseFilter<>(filter);
    }

    @Override
    public boolean test(final T obj) {
        return !filter.test(obj);
    }

}
