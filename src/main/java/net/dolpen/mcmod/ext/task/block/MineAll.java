package net.dolpen.mcmod.ext.task.block;

import net.dolpen.mcmod.ext.DolpenMain;
import net.dolpen.mcmod.ext.mod.BlockStateGroup;
import net.dolpen.mcmod.lib.search.CompareSearcher;
import net.dolpen.mcmod.lib.search.comparator.IBlockComparator;
import net.dolpen.mcmod.lib.search.filter.position.PositionFilter;
import net.dolpen.mcmod.lib.search.position.Positions;
import net.dolpen.mcmod.lib.search.walker.SixNeighborWalker;
import net.dolpen.mcmod.lib.util.BlockStateUtil;
import net.dolpen.mcmod.lib.util.BlockUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class MineAll extends BlockTask {

    // 鉱石辞書的に同じなら1グループとして扱う、ただしレッドストーンだけは独自グルーピング、黒曜石はBlockIDでやる
    public static final IBlockComparator CHAIN_RULE = (seeing, base) -> {
        if (BlockStateUtil.matchBlockGroups(seeing, base, BlockStateGroup.MINE_BLOCK_CHAIN))
            return BlockStateUtil.matchBlockName(seeing, base);
        if (BlockStateUtil.matchBlockGroups(seeing, base, BlockStateGroup.MINE_RS_CHAIN))
            return true;
        return BlockStateUtil.matchOreDict(seeing, base);
    };

    public MineAll(World world, EntityPlayer player, BlockPos pos, IBlockState baseBlockState) {
        super(world, player, pos, baseBlockState);
    }

    @SuppressWarnings("Duplicates")
    public void run() {
        Set<BlockPos> targets = new CompareSearcher(world, basePosition,
                Positions.getAABB(
                        basePosition,
                        DolpenMain.getInstance().getConfiguration().maxRange,
                        false
                ),
                new SixNeighborWalker(),
                CHAIN_RULE,
                PositionFilter.ANY
        ).search();
        for (BlockPos seeingPos : targets) {
            if (world.isAirBlock(seeingPos)) continue;
            if (!BlockUtil.breakOnce(world, player, seeingPos, false)) break;
        }
    }
}
