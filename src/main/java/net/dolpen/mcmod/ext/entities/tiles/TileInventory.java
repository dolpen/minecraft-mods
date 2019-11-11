package net.dolpen.mcmod.ext.entities.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

import java.util.stream.IntStream;

public abstract class TileInventory extends TileEntity implements ISidedInventory {

    private int slots;
    private NonNullList<ItemStack> inventories;

    public TileInventory(int slots) {
        this.slots = Math.max(1, slots);
        this.inventories = NonNullList.withSize(slots, ItemStack.EMPTY);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return IntStream.range(0, slots).toArray();
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return !(index < 0 || index >= slots);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return !inventories.get(index).isEmpty();
    }

    @Override
    public int getSizeInventory() {
        return slots;
    }

    @Override
    public boolean isEmpty() {
        return inventories.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inventories.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack target = inventories.get(index);
        if (target.isEmpty()) return ItemStack.EMPTY;
        if (target.getCount() <= count) {
            inventories.set(index, ItemStack.EMPTY);
            return target;
        }
        ItemStack out = target.splitStack(count);
        return out;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        inventories.set(index, ItemStack.EMPTY);
        return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {

        inventories.set(index, stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }


    public abstract String getName();

    public abstract boolean hasCustomName();

    public abstract ITextComponent getDisplayName();

}
