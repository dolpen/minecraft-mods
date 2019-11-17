package net.dolpen.mcmod.lib.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemStackUtil {

    public static boolean canMergeStrict(ItemStack a, ItemStack b) {
        if (!ItemStack.areItemsEqual(a, b))
            return false;
        if (!ItemStack.areItemStackTagsEqual(a, b))
            return false;
        int damageA = a.getItemDamage();
        int damageB = b.getItemDamage();
        if (damageA == -1 || damageA == OreDictionary.WILDCARD_VALUE) return true;
        if (damageB == -1 || damageB == OreDictionary.WILDCARD_VALUE) return true;
        return damageA == damageB;
    }
}
