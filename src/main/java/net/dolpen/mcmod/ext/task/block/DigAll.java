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

public class DigAll extends BlockTask {


    // 土系は別種でも一括
    public static final IBlockComparator CHAIN_RULE = (seeing, base) -> // trick
            BlockStateUtil.matchBlockGroups(seeing, base, BlockStateGroup.DIG_CHAIN);

    public DigAll(World world, EntityPlayer player, BlockPos pos, IBlockState baseBlockState) {
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
