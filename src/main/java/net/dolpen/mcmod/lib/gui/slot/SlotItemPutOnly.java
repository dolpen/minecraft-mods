package net.dolpen.mcmod.lib.gui.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotItemPutOnly extends SlotItemHandler {

    public SlotItemPutOnly(ItemStackHandler inventory, int index, int x, int y) {

        super(inventory, index, x, y);
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        return false;
    }

}