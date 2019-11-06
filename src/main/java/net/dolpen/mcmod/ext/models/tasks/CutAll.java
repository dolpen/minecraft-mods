package net.dolpen.mcmod.ext.models.tasks;

import net.dolpen.mcmod.ext.DolpenMain;
import net.dolpen.mcmod.ext.models.matcher.MatcherCollection;
import net.dolpen.mcmod.ext.utils.BlockUtil;
import net.dolpen.mcmod.ext.utils.Positions;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

public class CutAll extends BlockTask {

    public CutAll(World world, EntityPlayer player, BlockPos pos, IBlockState baseBlockState) {
        super(world, player, pos, baseBlockState);
    }

    @Override
    public void execute() {
        LinkedBlockingDeque<BlockPos> pool = Positions.getAllInRange(basePosition, 1).stream().filter(
                pos -> MatcherCollection.POS_CUT.applyAsBoolean(pos, basePosition)
                        && MatcherCollection.CUT_CHAIN_RULE.applyAsBoolean(world.getBlockState(pos), baseBlockState)
        ).distinct().collect(Collectors.toCollection(LinkedBlockingDeque::new));
        while (!pool.isEmpty()) {
            BlockPos seeingPos = pool.poll();
            if (world.isAirBlock(seeingPos)) continue;
            // break log
            if (!BlockUtil.breakOnce(world, player, seeingPos, false)) break; // fail(no tools left)
            // cut leaf
            Positions.getAllInRange(seeingPos, DolpenMain.getInstance().getConfiguration().leafRange).stream().filter(
                    pos -> MatcherCollection.BLOCK_LEAF.applyAsBoolean(world.getBlockState(pos), baseBlockState)
            ).forEach(
                    leafPos -> BlockUtil.breakOnce(world, player, leafPos, true)
            );
            // add neighbors3D
            pool.addAll(Positions.getAllInRange(seeingPos, 1).stream().filter(
                    pos -> MatcherCollection.POS_CUT.applyAsBoolean(pos, basePosition)
                            && MatcherCollection.CUT_CHAIN_RULE.applyAsBoolean(world.getBlockState(pos), baseBlockState)
            ).collect(Collectors.toSet()));
        }
    }
}
