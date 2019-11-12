package net.dolpen.mcmod.lib.block;

import net.dolpen.mcmod.ext.mod.BlockStateGroup;
import net.dolpen.mcmod.lib.lang.ArrayUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.util.Strings;

import java.util.Arrays;

public class BlockStates {

    private static String getName(IBlockState blockState) {
        return Block.REGISTRY.getNameForObject(blockState.getBlock()).toString();
    }

    public static boolean matchBlockName(IBlockState seeingBlockState, IBlockState baseBlockState) {
        String baseName = getName(baseBlockState);
        String seeingName = getName(seeingBlockState);
        if (Strings.isBlank(baseName) || Strings.isBlank(seeingName)) return false;
        return seeingName.equals(baseName);
    }


    public static boolean matchBlockMeta(IBlockState seeingBlockState, IBlockState baseBlockState) {
        Block baseBlock = baseBlockState.getBlock();
        Block seeingBlock = seeingBlockState.getBlock();
        if (!matchBlockName(seeingBlockState, baseBlockState)) return false;
        return seeingBlock.getMetaFromState(seeingBlockState) == baseBlock.getMetaFromState(baseBlockState);
    }

    public static boolean matchOreDict(IBlockState seeingBlockState, IBlockState baseBlockState) {
        Block baseBlock = baseBlockState.getBlock();
        Block seeingBlock = seeingBlockState.getBlock();
        ItemStack a = new ItemStack(seeingBlock, 1, seeingBlock.getMetaFromState(seeingBlockState));
        ItemStack b = new ItemStack(baseBlock, 1, baseBlock.getMetaFromState(baseBlockState));
        if (a.isEmpty() || b.isEmpty()) return false;
        int[] joinIds = ArrayUtils.concat(OreDictionary.getOreIDs(a), OreDictionary.getOreIDs(b));
        Arrays.sort(joinIds);
        for (int i = 1; i < joinIds.length; i++) {
            if (joinIds[i] == joinIds[i - 1]) return true;
        }
        return false;
    }

    public static boolean matchBlockGroups(IBlockState seeingBlockState, BlockStateGroup... groups) {
        return Arrays.stream(groups).anyMatch(group -> group.contains(seeingBlockState));
    }

    public static boolean matchBlockGroups(IBlockState seeingBlockState, IBlockState baseBlockState, BlockStateGroup... groups) {
        return Arrays.stream(groups).anyMatch(group -> group.contains(seeingBlockState) && group.contains(baseBlockState));
    }


}
