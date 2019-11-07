package net.dolpen.mcmod.lib.block.walker;

import net.minecraft.util.math.BlockPos;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BoxWalker implements IWalker {

    private final int range;

    public BoxWalker(int range) {
        this.range = Math.max(range, 0);
    }

    @Override
    public Set<BlockPos> apply(BlockPos blockPos) {
        return StreamSupport.stream(
                BlockPos.getAllInBox(
                        blockPos.add(range, range, range),
                        blockPos.add(-range, -range, -range)
                ).spliterator(),
                false
        ).filter(
                pos -> !pos.equals(blockPos)
        ).collect(
                Collectors.toSet()
        );
    }
}
