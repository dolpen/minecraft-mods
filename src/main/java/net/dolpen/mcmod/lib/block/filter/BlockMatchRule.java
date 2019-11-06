package net.dolpen.mcmod.lib.block.filter;


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;
import java.util.Optional;

public class BlockMatchRule implements IMatchRule {

    public final Type type;
    public final String prefix;

    private BlockMatchRule(Type type, String prefix) {
        this.prefix = prefix;
        this.type = type;
    }

    public static BlockMatchRule ofName(String prefix) {
        return new BlockMatchRule(Type.NAME, prefix);
    }

    public static BlockMatchRule ofDict(String prefix) {
        return new BlockMatchRule(Type.DICT, prefix);
    }

    @Override
    public boolean test(IBlockState blockState) {
        Block block = blockState.getBlock();
        if (this.type.equals(Type.DICT)) {
            ItemStack a = new ItemStack(block, 1, block.getMetaFromState(blockState));
            if (a.isEmpty()) return false;
            return Arrays.stream(OreDictionary.getOreIDs(a))
                    .mapToObj(OreDictionary::getOreName)
                    .anyMatch(name -> name.startsWith(prefix));
        } else {
            return Optional.of(Block.REGISTRY.getNameForObject(block))
                    .map(ResourceLocation::toString)
                    .filter(name -> name.startsWith(prefix))
                    .isPresent();
        }
    }

    public enum Type {
        NAME,
        DICT
    }
}
