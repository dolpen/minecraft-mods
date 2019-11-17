package net.dolpen.mcmod.lib.gui.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotStatic extends Slot {

    public SlotStatic(IInventory inventory, int index, int x, int y) {

        super(inventory, index, x, y);
    }


    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        return false;
    }

}