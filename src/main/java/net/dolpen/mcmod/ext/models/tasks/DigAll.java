package net.dolpen.mcmod.ext.models.tasks;

import net.dolpen.mcmod.ext.models.matcher.MatcherCollection;
import net.dolpen.mcmod.ext.utils.BlockUtil;
import net.dolpen.mcmod.ext.utils.Positions;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

public class DigAll extends BlockTask {

    public DigAll(World world, EntityPlayer player, BlockPos pos, IBlockState baseBlockState) {
        super(world, player, pos, baseBlockState);
    }

    @Override
    public void execute() {
        LinkedBlockingDeque<BlockPos> pool = Positions.neighbors3D(basePosition).stream().filter(
                pos -> MatcherCollection.POS_DIG.applyAsBoolean(pos, basePosition)
                        && MatcherCollection.DIG_CHAIN_RULE.applyAsBoolean(world.getBlockState(pos), baseBlockState)
        ).distinct().collect(Collectors.toCollection(LinkedBlockingDeque::new));
        while (!pool.isEmpty()) {
            BlockPos seeingPos = pool.poll();
            if (world.isAirBlock(seeingPos)) continue;
            // break soil
            if (!BlockUtil.breakOnce(world, player, seeingPos, false)) break; // fail(no tools left)
            // add neighbors3D
            pool.addAll(Positions.neighbors3D(seeingPos).stream().filter(
                    pos -> MatcherCollection.POS_DIG.applyAsBoolean(pos, basePosition)
                            && MatcherCollection.DIG_CHAIN_RULE.applyAsBoolean(world.getBlockState(pos), baseBlockState)
            ).collect(Collectors.toSet()));
        }
    }
}
