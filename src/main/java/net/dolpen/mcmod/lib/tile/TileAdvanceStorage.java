package net.dolpen.mcmod.lib.tile;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.Objects;


public abstract class TileAdvanceStorage extends TileEntity implements IInventory, ICapabilitySerializable<NBTTagCompound> {


    protected AdvanceStorageHandler storageHandler = null;
    protected NonNullList<ItemStack> inventory;
    protected int accessPlayerCount = 0;

    public static String KEY_QUANTITY = "Quantity";
    public static String KEY_SLOT = "Slot";
    public static String KEY_ITEMS = "Items";
    public static String KEY_TILE_INFO = "TileInfo";

    public TileAdvanceStorage(int slots) {
        inventory = NonNullList.withSize(slots, ItemStack.EMPTY);
    }

    public NBTTagCompound serializeNBT() {
        NBTTagList tags = new NBTTagList();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack seeing = inventory.get(i);
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger(KEY_SLOT, i);
            tag.setInteger(KEY_QUANTITY, seeing.getCount());
            inventory.get(i).writeToNBT(tag);
            tags.appendTag(tag);
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag(KEY_ITEMS, tags);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        NBTTagList tags = nbt.getTagList(KEY_ITEMS, net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tags.tagCount(); i++) {
            NBTTagCompound tag = tags.getCompoundTagAt(i);
            int slot = tag.getInteger(KEY_SLOT);
            if (slot < 0 || slot >= inventory.size()) continue;
            inventory.set(slot, new ItemStack(tag));
            inventory.get(slot).setCount(tag.getInteger(KEY_QUANTITY));
        }
        onLoad();
    }

    // called from game engine
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        deserializeNBT(compound);
    }

    // called from game engine
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.merge(serializeNBT());
        return compound;
    }

    // called from block.harvestBlock(serialize for ItemStack)
    public ItemStack toItemStackWithNBT(Block block) {
        NBTTagCompound outer = new NBTTagCompound();
        outer.setTag(KEY_TILE_INFO, writeToNBT(new NBTTagCompound()));
        ItemStack dropStack = new ItemStack(block, 1);
        dropStack.setTagCompound(outer);
        return dropStack;
    }

    // called from onBlockPlacedBy(deserialize from ItemStack)
    public void fromItemStackWithNBT(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) return;
        NBTTagCompound outer = itemStack.getTagCompound();
        if (Objects.isNull(outer) || !outer.hasKey(KEY_TILE_INFO)) return;
        deserializeNBT(outer.getCompoundTag(KEY_TILE_INFO));
    }

    //from server -> client accessPlayerCount
    public void openInventory(EntityPlayer player) {
        if (player.isSpectator()) return;
        accessPlayerCount = Math.max(0, accessPlayerCount) + 1;
        Block thisBlock = getBlockType();
        world.addBlockEvent(pos, thisBlock, 1, accessPlayerCount);
        world.notifyNeighborsOfStateChange(pos, thisBlock, false);
    }

    //from server -> client accessPlayerCount
    public void closeInventory(EntityPlayer player) {
        if (player.isSpectator()) return;
        Block thisBlock = getBlockType();
        accessPlayerCount = Math.max(0, accessPlayerCount - 1);
        world.addBlockEvent(this.pos, thisBlock, 1, accessPlayerCount);
        world.notifyNeighborsOfStateChange(this.pos, thisBlock, false);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        this.readFromNBT(packet.getNbtCompound());
    }

    public void sync(EntityPlayer player) {
        if (!(player instanceof EntityPlayerMP)) return;
        EntityPlayerMP playerMP = (EntityPlayerMP) player;
        playerMP.connection.sendPacket(getUpdatePacket());
    }


    protected AdvanceStorageHandler createStorageHandler() {
        return new AdvanceStorageHandler(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    @javax.annotation.Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) (storageHandler == null ? (storageHandler = createStorageHandler()) : storageHandler);
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    public abstract int getGuiId();


}
