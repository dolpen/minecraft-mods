package net.dolpen.mcmod.ext.models.matcher;

import net.dolpen.mcmod.ext.DolpenMain;
import net.dolpen.mcmod.ext.utils.BlockStates;
import net.dolpen.mcmod.ext.utils.Positions;
import net.dolpen.mcmod.lib.block.comparator.IBlockComparator;
import net.dolpen.mcmod.lib.block.comparator.IPositionComparator;

public class MatcherCollection {
    /**
     * BlockStatesによる分類
     */

    // 木は完全一致
    public static final IBlockComparator CUT_CHAIN_RULE = BlockStates::matchBlockName;
    // 土系は別種でも一括
    public static final IBlockComparator DIG_CHAIN_RULE = (seeing, base) -> // trick
            BlockStates.matchBlockGroups(seeing, base, BlockStateGroup.DIG_CHAIN);
    // 鉱石辞書的に同じなら1グループとして扱う、ただしレッドストーンだけは独自グルーピング、黒曜石はBlockIDでやる
    public static final IBlockComparator MINE_CHAIN_RULE = (seeing, base) -> {
        if (BlockStates.matchBlockGroups(seeing, base, BlockStateGroup.MINE_BLOCK_CHAIN))
            return BlockStates.matchBlockName(seeing, base);
        if (BlockStates.matchBlockGroups(seeing, base, BlockStateGroup.MINE_RS_CHAIN))
            return true;
        return BlockStates.matchOreDict(seeing, base);

    };
    // 参照先が葉なら対象に
    public static final IBlockComparator BLOCK_LEAF = (seeing, base) ->
            BlockStates.matchBlockGroups(seeing, BlockStateGroup.CUT_LEAVES);
    // 異なるブロックでも一括耕地化
    public static final IBlockComparator BLOCK_CULTIVATE = (seeing, base) ->
            BlockStates.matchBlockGroups(seeing, base, BlockStateGroup.HOE_TRIGGER);

    /**
     * 位置による分類
     */
    // 木の一括対象は上方向無制限
    public static final IPositionComparator POS_CUT = (seeing, base) ->
            !Positions.isUnder(seeing, base)
                    && Positions.axisDistance2D(seeing, base) < DolpenMain.getInstance().getConfiguration().maxRange;
    // 下を掘らない
    public static final IPositionComparator POS_DIG = (seeing, base) ->
            !Positions.isUnder(seeing, base)
                    && Positions.axisDistance3D(seeing, base) < DolpenMain.getInstance().getConfiguration().maxRange;
    // 下を掘らない
    public static final IPositionComparator POS_MINE = (seeing, base) ->
            !Positions.isUnder(seeing, base)
                    && Positions.axisDistance3D(seeing, base) < DolpenMain.getInstance().getConfiguration().maxRange;
    // 耕地化は同一高の平面のみ
    public static final IPositionComparator POS_CULTIVATE = (seeing, base) ->
            Positions.isSameHeight(seeing, base)
                    && Positions.axisDistance2D(seeing, base) < DolpenMain.getInstance().getConfiguration().maxRange;

}
