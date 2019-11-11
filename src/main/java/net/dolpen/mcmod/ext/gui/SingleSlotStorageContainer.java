package net.dolpen.mcmod.ext.gui;

import net.dolpen.mcmod.ext.entities.tiles.TileSingleSlotStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SingleSlotStorageContainer extends Container {


    private TileSingleSlotStorage tile;

    public SingleSlotStorageContainer(IInventory playerInv, final TileSingleSlotStorage tile) {
        this.tile = tile;
        addSlotToContainer(new Slot(tile, 0, 8, 15));
        bindPlayerInventory(playerInv);
    }

    protected int getPlayerInventoryVerticalOffset() {
        return 84;
    }

    protected void bindPlayerInventory(IInventory inventoryPlayer) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, getPlayerInventoryVerticalOffset() + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, getPlayerInventoryVerticalOffset() + 58));
        }
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();
            if (index < containerSlots) {
                if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, itemstack);
        }
        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return !playerIn.isSpectator();
    }
}