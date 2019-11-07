package net.dolpen.mcmod.lib.block;

import com.google.common.collect.Sets;
import net.dolpen.mcmod.lib.block.filter.state.IBlockStateFilter;
import net.dolpen.mcmod.lib.block.walker.IWalker;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 複数の BlockPos を起点に検索条件に合う BlockPos を BFS します
 */
public class ExpandMatchSearcher {

    private final AxisAlignedBB searchArea;
    private final Set<BlockPos> basePos;
    private final IWalker walker;
    private final IBlockStateFilter rule;
    private final World world;

    public ExpandMatchSearcher(World world, Set<BlockPos> basePos, AxisAlignedBB searchArea, IWalker walker, IBlockStateFilter rule) {
        this.world = world;
        this.basePos = basePos;
        this.searchArea = searchArea;
        this.walker = walker;
        this.rule = rule;
    }

    public Set<BlockPos> search() {
        Set<BlockPos> pool = Sets.newHashSet(BlockPos.getAllInBox(
                new BlockPos(searchArea.minX, searchArea.minY, searchArea.minZ),
                new BlockPos(searchArea.maxX, searchArea.maxY, searchArea.maxZ)
        ));
        Set<BlockPos> hitPositions = Sets.newHashSet();
        pool.removeAll(basePos);
        ConcurrentLinkedDeque<BlockPos> queue = new ConcurrentLinkedDeque<>(basePos);
        while (!queue.isEmpty()) {
            BlockPos seeingPos = queue.pollLast();
            assert seeingPos != null;
            walker.apply(seeingPos).stream().filter(pool::contains).forEach(nextPos -> {
                pool.remove(nextPos);
                if (rule.test(world.getBlockState(nextPos))) hitPositions.add(nextPos);
            });

        }
        return hitPositions;
    }
}
