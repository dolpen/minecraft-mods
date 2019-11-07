package net.dolpen.mcmod.lib.block.walker;

import com.google.common.collect.Sets;
import net.minecraft.util.math.BlockPos;

import java.util.Set;

public class SixNeighborWalker implements IWalker {

    @Override
    public Set<BlockPos> apply(BlockPos blockPos) {
        return Sets.newHashSet(
                blockPos.north(),
                blockPos.south(),
                blockPos.east(),
                blockPos.west(),
                blockPos.up(),
                blockPos.down()
        );
    }
}
