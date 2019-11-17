package net.dolpen.mcmod.lib.gui.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotTakeOnly extends Slot {

    public SlotTakeOnly(IInventory inventory, int index, int x, int y) {

        super(inventory, index, x, y);
    }


    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

}