package net.dolpen.mcmod.lib.search.filter.position;

import net.minecraft.util.math.BlockPos;

public class PositionFilter {
    public static final IPositionFilter ANY = new IPositionFilter() {
        @Override
        public boolean test(BlockPos blockPos) {
            return true;
        }
    };
}
