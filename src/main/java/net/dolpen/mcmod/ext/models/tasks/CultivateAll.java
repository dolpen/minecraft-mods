package net.dolpen.mcmod.ext.models.tasks;

import net.dolpen.mcmod.ext.models.matcher.BlockStateGroup;
import net.dolpen.mcmod.ext.models.matcher.MatcherCollection;
import net.dolpen.mcmod.ext.utils.BlockUtil;
import net.dolpen.mcmod.ext.utils.Positions;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

public class CultivateAll extends BlockTask {

    public CultivateAll(World world, EntityPlayer player, BlockPos pos, IBlockState baseBlockState) {
        super(world, player, pos, baseBlockState);
    }

    @Override
    public void execute() {
        LinkedBlockingDeque<BlockPos> pool = Positions.neighbors2D(basePosition).stream().filter(
                pos -> MatcherCollection.POS_CULTIVATE.applyAsBoolean(pos, basePosition)
                        && MatcherCollection.BLOCK_CULTIVATE.applyAsBoolean(world.getBlockState(pos), baseBlockState)
        ).filter(
                pos -> BlockStateGroup.HOE_REMOVABLE.contains(world.getBlockState(pos.up()))
        ).distinct().collect(Collectors.toCollection(LinkedBlockingDeque::new));
        while (!pool.isEmpty()) {
            BlockPos seeingPos = pool.poll();
            if (world.isAirBlock(seeingPos)) continue;
            // break ores
            if (!BlockUtil.cultivateOnce(world, player, seeingPos)) break; // fail(no tools left)
            // add neighbors3D
            pool.addAll(Positions.neighbors2D(seeingPos).stream().filter(
                    pos -> MatcherCollection.POS_CULTIVATE.applyAsBoolean(pos, basePosition)
                            && MatcherCollection.BLOCK_CULTIVATE.applyAsBoolean(world.getBlockState(pos), baseBlockState)
            ).filter(
                    pos -> BlockStateGroup.HOE_REMOVABLE.contains(world.getBlockState(pos.up()))
            ).collect(Collectors.toSet()));
        }
    }

}
