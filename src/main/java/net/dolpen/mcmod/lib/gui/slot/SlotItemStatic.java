package net.dolpen.mcmod.lib.gui.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotItemStatic extends SlotItemHandler {

    public SlotItemStatic(ItemStackHandler inventory, int index, int x, int y) {

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