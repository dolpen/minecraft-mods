package net.dolpen.mcmod.lib.gui.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotPutOnly extends Slot {

    public SlotPutOnly(IInventory inventory, int index, int x, int y) {

        super(inventory, index, x, y);
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        return false;
    }

}