package net.dolpen.mcmod.ext.models.tasks.block;

import com.google.common.collect.Sets;
import net.dolpen.mcmod.ext.DolpenMain;
import net.dolpen.mcmod.ext.models.BlockStateGroup;
import net.dolpen.mcmod.ext.utils.BlockStates;
import net.dolpen.mcmod.ext.utils.BlockUtil;
import net.dolpen.mcmod.lib.block.CompareSearcher;
import net.dolpen.mcmod.lib.block.ExpandMatchSearcher;
import net.dolpen.mcmod.lib.block.comparator.IBlockComparator;
import net.dolpen.mcmod.lib.block.filter.position.PositionFilter;
import net.dolpen.mcmod.lib.block.filter.state.IBlockStateFilter;
import net.dolpen.mcmod.lib.block.position.Positions;
import net.dolpen.mcmod.lib.block.walker.BoxWalker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;
import java.util.stream.StreamSupport;

public class CutAll extends BlockTask {
    // 木は完全一致
    public static final IBlockComparator CHAIN_RULE = BlockStates::matchBlockName;
    // 参照先が葉なら対象に
    public static final IBlockStateFilter BLOCK_LEAF = (seeing) ->
            BlockStates.matchBlockGroups(seeing, BlockStateGroup.CUT_LEAVES);

    public CutAll(World world, EntityPlayer player, BlockPos pos, IBlockState baseBlockState) {
        super(world, player, pos, baseBlockState);
    }

    public void run() {
        int range = DolpenMain.getInstance().getConfiguration().maxRange;
        // non-air ブロックの最高点を割り出す
        BlockPos highestBase = basePosition.add(0, range, 0);
        while (
                StreamSupport.stream(BlockPos.getAllInBox(
                        highestBase.add(range, 0, -range),
                        highestBase.add(-range, 0, range)
                ).spliterator(), false).anyMatch(pos -> !world.isAirBlock(pos))
        ) {
            highestBase = highestBase.up();
        }
        // 最高点まで検索する
        AxisAlignedBB targetArea = Positions.getUpperWithHeight(
                basePosition,
                range,
                highestBase.getY()
        );
        // 検索実行
        Set<BlockPos> targets = new CompareSearcher(world, basePosition,
                targetArea,
                new BoxWalker(1),
                CHAIN_RULE,
                PositionFilter.ANY
        ).search();

        // ブロック破壊/実績出す
        Set<BlockPos> dones = Sets.newHashSet();
        for (BlockPos seeingPos : targets) {
            if (world.isAirBlock(seeingPos)) continue;
            if (!BlockUtil.breakOnce(world, player, seeingPos, false)) break;
            dones.add(seeingPos);
        }

        // 実際に破壊できたブロックの周辺の葉を探索し除去
        new ExpandMatchSearcher(world, dones,
                targetArea,
                new BoxWalker(DolpenMain.getInstance().getConfiguration().leafRange),
                BLOCK_LEAF
        ).search().forEach(
                leafPos -> BlockUtil.breakOnce(world, player, leafPos, true)
        );
    }
}
