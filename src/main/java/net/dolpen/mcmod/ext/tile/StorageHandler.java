package net.dolpen.mcmod.ext.tile;

import net.dolpen.mcmod.lib.item.ItemStackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Objects;

public class StorageHandler extends ItemStackHandler {
    public static final int POOL = 0;
    public static final int IN = 1;
    public static final int OUT = 2;

    public StorageHandler() {
        super(3);
    }

    @Override
    protected int getStackLimit(int slot, ItemStack stack) {
        return slot == POOL ? Integer.MAX_VALUE : 64;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagList tags = new NBTTagList();
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack seeing = stacks.get(i);
            if (Objects.isNull(seeing)) continue;
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("Slot", i);
            tag.setInteger("Quantity", seeing.getCount());
            stacks.get(i).writeToNBT(tag);
            tags.appendTag(tag);
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Items", tags);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        NBTTagList tags = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tags.tagCount(); i++) {
            NBTTagCompound tag = tags.getCompoundTagAt(i);
            int slot = tag.getInteger("Slot");
            if (slot < 0 || slot >= stacks.size()) continue;
            stacks.set(slot, new ItemStack(tag));
            stacks.get(slot).setCount(tag.getInteger("Quantity"));
        }
        onLoad();
    }

    public ItemStack insertItem(int slot, ItemStack inStack, boolean simulate) {
        ItemStack outStack = getStackInSlot(OUT);
        boolean canInsert = outStack.isEmpty() || ItemStackUtil.canMergeStrict(outStack, inStack);
        return canInsert ? super.insertItem(slot, inStack, simulate) : inStack;
    }
}