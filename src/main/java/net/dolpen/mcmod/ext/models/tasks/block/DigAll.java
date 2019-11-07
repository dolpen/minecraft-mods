package net.dolpen.mcmod.ext.models.tasks.block;

import net.dolpen.mcmod.ext.DolpenMain;
import net.dolpen.mcmod.ext.models.BlockStateGroup;
import net.dolpen.mcmod.ext.utils.BlockStates;
import net.dolpen.mcmod.ext.utils.BlockUtil;
import net.dolpen.mcmod.lib.block.CompareSearcher;
import net.dolpen.mcmod.lib.block.comparator.IBlockComparator;
import net.dolpen.mcmod.lib.block.filter.position.PositionFilter;
import net.dolpen.mcmod.lib.block.position.Positions;
import net.dolpen.mcmod.lib.block.walker.SixNeighborWalker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class DigAll extends BlockTask {


    // 土系は別種でも一括
    public static final IBlockComparator CHAIN_RULE = (seeing, base) -> // trick
            BlockStates.matchBlockGroups(seeing, base, BlockStateGroup.DIG_CHAIN);

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
