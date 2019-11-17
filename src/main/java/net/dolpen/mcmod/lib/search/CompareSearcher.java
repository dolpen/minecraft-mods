package net.dolpen.mcmod.lib.search;

import com.google.common.collect.Sets;
import net.dolpen.mcmod.lib.search.comparator.IBlockComparator;
import net.dolpen.mcmod.lib.search.filter.position.IPositionFilter;
import net.dolpen.mcmod.lib.search.walker.IWalker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 起点との比較条件に合う BlockPos を BFS します
 */
public class CompareSearcher {

    private final AxisAlignedBB searchArea;
    private final BlockPos basePos;
    private final IWalker walker;
    private final IBlockComparator blockComparator;
    private final IPositionFilter positionFilter;
    private final World world;

    public CompareSearcher(
            World world, BlockPos basePos, AxisAlignedBB searchArea,
            IWalker walker,
            IBlockComparator blockComparator,
            IPositionFilter positionFilter
    ) {
        this.world = world;
        this.basePos = basePos;
        this.searchArea = searchArea;
        this.walker = walker;
        this.blockComparator = blockComparator;
        this.positionFilter = positionFilter;
    }

    public Set<BlockPos> search() {
        Set<BlockPos> pool = Sets.newHashSet(BlockPos.getAllInBox(
                new BlockPos(searchArea.minX, searchArea.minY, searchArea.minZ),
                new BlockPos(searchArea.maxX, searchArea.maxY, searchArea.maxZ)
        ));
        ConcurrentLinkedDeque<BlockPos> queue = new ConcurrentLinkedDeque<>();
        Set<BlockPos> hitPositions = Sets.newLinkedHashSet(); // 挿入順保持
        pool.remove(basePos);
        queue.addFirst(basePos);
        IBlockState baseState = world.getBlockState(basePos);
        while (!queue.isEmpty()) {
            BlockPos seeingPos = queue.pollLast();
            assert seeingPos != null;
            if (!positionFilter.test(seeingPos)) continue;
            if (!blockComparator.applyAsBoolean(world.getBlockState(seeingPos), baseState)) continue;
            hitPositions.add(seeingPos);
            walker.apply(seeingPos).stream().filter(pool::contains).forEach(nextPos -> {
                pool.remove(nextPos);
                queue.addFirst(nextPos);
            });
        }
        return hitPositions;
    }
}
