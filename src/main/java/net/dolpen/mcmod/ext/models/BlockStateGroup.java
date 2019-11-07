package net.dolpen.mcmod.ext.models;

import net.dolpen.mcmod.lib.block.filter.state.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BlockStateGroup {


    /*
     鉱石辞書 の dirt カテゴリは荒れた土やポドゾルのバリアントを含まないので OR 検索しています
     ここが何とかなれば BlockStateFilter.ofName("minecraft:dirt") はいらなくなるはずです
     dirt + grass は繋がります。
     gravel は個別です、 sand は色違いが一気に取れます
     MODでこれらに追加してるものは対応するでしょう
     */
    public static final BlockStateGroup[] DIG_CHAIN = new BlockStateGroup[]{
            of(
                    CompositeBlockStateFilter.all(
                            CompositeBlockStateFilter.any(
                                    BlockStateFilter.ofName("minecraft:dirt"),
                                    BlockStateFilter.ofDict("dirt"),
                                    BlockStateFilter.ofDict("grass")
                            ),
                            BlockBreakToolFilter.DIG
                    )

            ),
            of(
                    CompositeBlockStateFilter.all(
                            BlockStateFilter.ofDict("sand"),
                            BlockBreakToolFilter.DIG
                    )
            ),
            of(
                    CompositeBlockStateFilter.all(
                            BlockStateFilter.ofDict("gravel"),
                            BlockBreakToolFilter.DIG
                    )
            )
    };

    public static final BlockStateGroup DIG_TRIGGER = union(DIG_CHAIN);


    public static final BlockStateGroup MINE_RS_CHAIN = BlockStateGroup.of(
            CompositeBlockStateFilter.anyName(
                    "minecraft:lit_redstone_ore",
                    "minecraft:redstone_ore"
            )
    );
    public static final BlockStateGroup MINE_BLOCK_CHAIN = of(
            BlockStateFilter.ofName("minecraft:obsidian")
    );

    /*
    鉱石のトリガブロックです
    Forge鉱石辞書で ore 系なら取れるし、同じ鉱石なら別ブロックでも取れるようになってます
    レッドストーンは光ってるやつと光ってないやつは同じグループとして一括破壊できます
    黒曜石も一応入れときます
     */
    public static final BlockStateGroup MINE_TRIGGER = union(
            of(
                    CompositeBlockStateFilter.all(
                            BlockStateFilter.ofDict("ore"),
                            BlockBreakToolFilter.MINE
                    )
            ),
            MINE_RS_CHAIN,
            MINE_BLOCK_CHAIN
    );

    /*
    鉱石辞書依存です、切って欲しければ入れてください
     */
    public static final BlockStateGroup CUT_TRIGGER = of(
            CompositeBlockStateFilter.all(
                    BlockStateFilter.ofDict("log"),
                    BlockBreakToolFilter.CUT
            )
    );

    /*
    葉っぱは殴って取った扱いになります、殴れない葉っぱとかは想定してないです
     */
    public static final BlockStateGroup CUT_LEAVES = of(
            BlockStateFilter.ofDict("treeLeaves")
    );


    /*
     基本的には指名（荒れてない土と草なら耕地化できる）
     ブロック破壊とは違うのでツールでフィルタする方法はなさそう
     これも鉱石辞書の dirt カテゴリが雑だからこうなってる
     MOD間で耕地化可能ブロック指定したいなら
     鉱石辞書上で grass に入れるか DirtType 指定した上で dirt に入れてください
     */
    public static final BlockStateGroup HOE_TRIGGER = BlockStateGroup.of(
            CompositeBlockStateFilter.any(
                    BlockStateFilter.ofDict("grass"),
                    CompositeBlockStateFilter.all(
                            CompositeBlockStateFilter.any(
                                    BlockStateFilter.ofName("minecraft:dirt"),
                                    BlockStateFilter.ofDict("dirt")
                            ),
                            BlockPropertyFilter.of(
                                    BlockDirt.VARIANT,
                                    BlockDirt.DirtType.DIRT,
                                    BlockDirt.DirtType.PODZOL
                            )
                    )
            )
    );

    /*
    取り除き対象です
    土や草の上にこれらが乗っていた場合、空気でなければそれらを（殴って）取り除いて耕地化します
    木の苗なども対象内です。かなり豪快に破壊します。畑と植林場が土壌で連結されていると危険です。
    土や草の上にそれ以外が乗っていた場合、下の土や草は耕地化されません。
    事前に殴って取り除くなどしなければならなくなります。
    MODで草花を追加したい場合は BlockBush を継承してください
    鉱石辞書に適切なカテゴリがあればもう少し簡単に連携やグルーピングができるけれど……
    */
    public static final BlockStateGroup HOE_REMOVABLE = of(
            CompositeBlockStateFilter.any(
                    BlockClassFilter.of(BlockBush.class),
                    BlockStateFilter.ofName("minecraft:air")
            )
    );
    //// ------------------------

    private Set<IBlockState> states;

    private BlockStateGroup(Set<IBlockState> states) {
        this.states = states;
    }

    static BlockStateGroup of(IBlockStateFilter rule) {
        Set<IBlockState> matchStates = StreamSupport.stream(Block.REGISTRY.spliterator(), false)
                .flatMap(b -> b.getBlockState().getValidStates().stream())
                .filter(rule::test)
                .collect(Collectors.toSet());
        return new BlockStateGroup(matchStates);
    }

    static BlockStateGroup union(BlockStateGroup... blockStateGroups) {
        Set<IBlockState> unionStates = Arrays.stream(blockStateGroups)
                .flatMap(g -> g.states.stream())
                .collect(Collectors.toSet());
        return new BlockStateGroup(unionStates);
    }

    public boolean contains(IBlockState blockState) {
        return states.contains(blockState);
    }

    public String toString() {
        return "[BlockStateGroup>]\n" + states.stream().map(
                state -> "" + state.getBlock().getClass().getName() + " " + state.getBlock().toString() + " " + state.toString()
        ).collect(Collectors.joining("\n")) + "\n[/BlockStateGroup]";
    }


}
