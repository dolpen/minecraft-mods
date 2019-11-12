package net.dolpen.mcmod.ext.task.block;

import net.dolpen.mcmod.ext.DolpenMain;
import net.dolpen.mcmod.ext.mod.BlockStateGroup;
import net.dolpen.mcmod.lib.block.BlockStates;
import net.dolpen.mcmod.lib.block.BlockUtil;
import net.dolpen.mcmod.lib.search.CompareSearcher;
import net.dolpen.mcmod.lib.search.comparator.IBlockComparator;
import net.dolpen.mcmod.lib.search.walker.SixNeighborWalker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class CultivateAll extends BlockTask {


    // 異なるブロックでも一括耕地化
    public static final IBlockComparator CHAIN_RULE = (seeing, base) ->
            BlockStates.matchBlockGroups(seeing, base, BlockStateGroup.HOE_TRIGGER);

    public CultivateAll(World world, EntityPlayer player, BlockPos pos, IBlockState baseBlockState) {
        super(world, player, pos, baseBlockState);
    }

    public void run() {
        Set<BlockPos> targets = new CompareSearcher(world, basePosition,
                net.dolpen.mcmod.lib.search.position.Positions.getHorizontalAABB(
                        basePosition,
                        DolpenMain.getInstance().getConfiguration().maxRange
                ),
                new SixNeighborWalker(),
                CHAIN_RULE,
                // 上に自動破壊しないブロックがあるときは対象外
                pos -> BlockStateGroup.HOE_REMOVABLE.contains(world.getBlockState(pos.up()))
        ).search();
        for (BlockPos seeingPos : targets) {
            if (world.isAirBlock(seeingPos)) continue;
            if (!BlockUtil.cultivateOnce(world, player, seeingPos)) break;
        }
    }
}
