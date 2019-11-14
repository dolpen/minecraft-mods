package net.dolpen.mcmod.ext.tile;

import net.dolpen.mcmod.ext.gui.GuiHandler;
import net.dolpen.mcmod.lib.item.ItemStackUtil;
import net.dolpen.mcmod.lib.tile.TileAdvanceStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;

import javax.annotation.Nullable;
import java.util.Objects;


public class TileInfinityStorage extends TileAdvanceStorage implements ITickable {

    public static final int IN = 0;
    public static final int OUT = 1;
    public static final int POOL = 2;
    private static final int VISIBLE = 2;
    private static final int SLOTS = 3;

    public TileInfinityStorage() {
        super(SLOTS);
    }

    @Override
    public void update() {
        ItemStack inStack = inventory.get(IN);
        ItemStack poolStack = inventory.get(POOL);
        ItemStack outStack = inventory.get(OUT);
        boolean needCommit = false;
        // process input
        if (!inStack.isEmpty()) {
            if (outStack.isEmpty()) {
                outStack = inStack.copy();
                inventory.set(OUT, outStack);
                inventory.set(IN, ItemStack.EMPTY);
                needCommit = true;
            } else if (ItemStackUtil.canMergeStrict(inStack, outStack)) {
                if (poolStack.isEmpty()) {
                    poolStack = inStack.copy();
                    inventory.set(POOL, poolStack);
                } else {
                    poolStack.grow(inStack.getCount()); //?
                }
                inventory.set(IN, ItemStack.EMPTY);
                needCommit = true;
            }
        }
        if (!poolStack.isEmpty()) {
            int flow = Math.min(poolStack.getCount(),
                    outStack.getMaxStackSize() - outStack.getCount()
            );
            if (flow > 0) {
                if (outStack.isEmpty() || outStack.getCount() == 0) {
                    ItemStack newOut = poolStack.copy();
                    newOut.setCount(flow);
                    inventory.set(OUT, newOut);
                } else {
                    outStack.grow(flow);
                }
                // out partial
                poolStack.shrink(flow);
                needCommit = true;
            }
            if (poolStack.isEmpty() || poolStack.getCount() <= 0) {
                inventory.set(POOL, ItemStack.EMPTY);
                // すでに needCommit = true;
            }
        }
        if (needCommit) markDirty();

    }


    @Override
    public int getSizeInventory() {
        return VISIBLE;
    }

    @Override
    public boolean isEmpty() {
        return inventory.stream().allMatch(
                itemStack -> itemStack.isEmpty() || itemStack.getCount() <= 0
        );
    }

    private boolean validateSlot(int slot) {
        return slot >= 0 && slot < VISIBLE;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return validateSlot(slot) ? inventory.get(slot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        if (!validateSlot(slot)) return ItemStack.EMPTY;
        ItemStack picked = ItemStackHelper.getAndSplit(inventory, slot, count);
        if (!picked.isEmpty()) markDirty();
        return picked;
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        return ItemStackHelper.getAndRemove(inventory, slot);
    }

    @Override
    public void setInventorySlotContents(int slot, @Nullable ItemStack stack) {
        if (Objects.isNull(stack)) return;
        inventory.set(slot, stack);
        if (stack.getCount() > getInventoryStackLimit()) {
            stack.setCount(getInventoryStackLimit());
        }
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (slot != IN) return false;
        ItemStack outStack = getStackInSlot(OUT);
        return outStack.isEmpty() || ItemStackUtil.canMergeStrict(outStack, stack);
    }

    @Override
    public int getField(int id) {
        if (id < SLOTS) return inventory.get(id).getCount();
        if (id == 3) return inventory.get(OUT).getCount() + inventory.get(POOL).getCount();
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return SLOTS + 1;
    }

    @Override
    public void clear() {

    }

    @Override
    public String getName() {
        return "infinity_storage";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int getGuiId() {
        return GuiHandler.INFINITY_STORAGE;
    }
}
