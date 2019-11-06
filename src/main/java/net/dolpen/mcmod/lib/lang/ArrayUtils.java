package net.dolpen.mcmod.lib.lang;

public class ArrayUtils {
    public static int[] concat(final int[] array1, final int... array2) {
        final int[] joinedArray = new int[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }
}
