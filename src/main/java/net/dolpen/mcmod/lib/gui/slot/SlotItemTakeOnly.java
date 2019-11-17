package net.dolpen.mcmod.lib.gui.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotItemTakeOnly extends SlotItemHandler {

    public SlotItemTakeOnly(ItemStackHandler inventory, int index, int x, int y) {

        super(inventory, index, x, y);
    }


    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

}