package net.dolpen.mcmod.ext.models.matcher;

import net.dolpen.mcmod.lib.block.filter.*;
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
     ここが何とかなれば BlockMatchRule.ofName("minecraft:dirt") はいらなくなるはずです
     dirt + grass は繋がります。
     gravel は個別です、 sand は色違いが一気に取れます
     MODでこれらに追加してるものは対応するでしょう
     */
    public static final BlockStateGroup[] DIG_CHAIN = new BlockStateGroup[]{
            of(
                    CompositeRule.all(
                            CompositeRule.any(
                                    BlockMatchRule.ofName("minecraft:dirt"),
                                    BlockMatchRule.ofDict("dirt"),
                                    BlockMatchRule.ofDict("grass")
                            ),
                            ToolMatchRule.DIG
                    )

            ),
            of(
                    CompositeRule.all(
                            BlockMatchRule.ofDict("sand"),
                            ToolMatchRule.DIG
                    )
            ),
            of(
                    CompositeRule.all(
                            BlockMatchRule.ofDict("gravel"),
                            ToolMatchRule.DIG
                    )
            )
    };

    public static final BlockStateGroup DIG_TRIGGER = union(DIG_CHAIN);


    public static final BlockStateGroup MINE_RS_CHAIN = BlockStateGroup.of(
            CompositeRule.anyName(
                    "minecraft:lit_redstone_ore",
                    "minecraft:redstone_ore"
            )
    );
    public static final BlockStateGroup MINE_BLOCK_CHAIN = of(
            BlockMatchRule.ofName("minecraft:obsidian")
    );

    /*
    鉱石のトリガブロックです
    Forge鉱石辞書で ore 系なら取れるし、同じ鉱石なら別ブロックでも取れるようになってます
    レッドストーンは光ってるやつと光ってないやつは同じグループとして一括破壊できます
    黒曜石も一応入れときます
     */
    public static final BlockStateGroup MINE_TRIGGER = union(
            of(
                    CompositeRule.all(
                            BlockMatchRule.ofDict("ore"),
                            ToolMatchRule.MINE
                    )
            ),
            MINE_RS_CHAIN,
            MINE_BLOCK_CHAIN
    );

    /*
    鉱石辞書依存です、切って欲しければ入れてください
     */
    public static final BlockStateGroup CUT_TRIGGER = of(
            CompositeRule.all(
                    BlockMatchRule.ofDict("log"),
                    ToolMatchRule.CUT
            )
    );

    /*
    葉っぱは殴って取った扱いになります、殴れない葉っぱとかは想定してないです
     */
    public static final BlockStateGroup CUT_LEAVES = of(
            BlockMatchRule.ofDict("treeLeaves")
    );


    /*
     基本的には指名（荒れてない土と草なら耕地化できる）
     ブロック破壊とは違うのでツールでフィルタする方法はなさそう
     これも鉱石辞書の dirt カテゴリが雑だからこうなってる
     MOD間で耕地化可能ブロック指定したいなら
     鉱石辞書上で grass に入れるか DirtType 指定した上で dirt に入れてください
     */
    public static final BlockStateGroup HOE_TRIGGER = BlockStateGroup.of(
            CompositeRule.any(
                    BlockMatchRule.ofDict("grass"),
                    CompositeRule.all(
                            CompositeRule.any(
                                    BlockMatchRule.ofName("minecraft:dirt"),
                                    BlockMatchRule.ofDict("dirt")
                            ),
                            PropertyMatchRule.of(
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
            CompositeRule.any(
                    ClassMatchRule.of(BlockBush.class),
                    BlockMatchRule.ofName("minecraft:air")
            )
    );
    //// ------------------------

    private Set<IBlockState> states;

    private BlockStateGroup(Set<IBlockState> states) {
        this.states = states;
    }

    static BlockStateGroup of(IMatchRule rule) {
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
