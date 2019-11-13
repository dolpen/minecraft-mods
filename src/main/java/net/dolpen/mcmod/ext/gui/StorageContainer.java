package net.dolpen.mcmod.ext.gui;

import net.dolpen.mcmod.ext.tile.StorageHandler;
import net.dolpen.mcmod.ext.tile.TileStorage;
import net.dolpen.mcmod.lib.gui.slot.SlotPutOnly;
import net.dolpen.mcmod.lib.gui.slot.SlotTakeOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.stream.IntStream;

public class StorageContainer extends Container {


    private TileStorage tile;

    public StorageContainer(IInventory playerInventory, final TileStorage tile) {
        this.tile = tile;
        ItemStackHandler handler = tile.getHandler();
        // 搬入
        addSlotToContainer(new SlotPutOnly(handler, StorageHandler.IN, 8 + 18, 36));
        // 搬出
        addSlotToContainer(new SlotTakeOnly(handler, StorageHandler.OUT, 8 + 7 * 18, 36));
        // これ下のやつ
        bindPlayerInventory(playerInventory);
    }

    protected void bindPlayerInventory(final IInventory inventoryPlayer) {
        // inventory
        IntStream.range(0, 3).forEach(i -> {
            IntStream.range(0, 9).forEach(j -> {
                addSlotToContainer(
                        new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18)
                );
            });
        });
        // quick bar
        IntStream.range(0, 9).forEach(j -> {
            addSlotToContainer(new Slot(inventoryPlayer, j, 8 + j * 18, 84 +58));
        });
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        Slot slot = inventorySlots.get(index);
        if (slot == null || !slot.getHasStack()) return ItemStack.EMPTY;
        ItemStack inSlot = slot.getStack();
        ItemStack copyOfSlot = inSlot.copy();
        int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();
        if (index < containerSlots) {
            if (!this.mergeItemStack(inSlot, containerSlots, inventorySlots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.mergeItemStack(inSlot, 0, containerSlots, false)) {
            return ItemStack.EMPTY;
        }
        if (inSlot.getCount() == 0) {
            slot.putStack(ItemStack.EMPTY);
        } else {
            slot.onSlotChanged();
        }
        if (inSlot.getCount() == copyOfSlot.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, copyOfSlot);
        tile.markDirty();
        return copyOfSlot;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return !playerIn.isSpectator();
    }
}