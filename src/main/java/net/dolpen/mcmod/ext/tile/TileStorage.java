package net.dolpen.mcmod.ext.tile;

import net.dolpen.mcmod.lib.item.ItemStackUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileStorage extends TileEntity implements ITickable, ICapabilitySerializable<NBTTagCompound> {

    private StorageHandler handler;

    public TileStorage() {
        handler = new StorageHandler();
    }

    @Override
    public void update() {
        ItemStack inStack = handler.getStackInSlot(StorageHandler.IN);
        ItemStack poolStack = handler.getStackInSlot(StorageHandler.POOL);
        ItemStack outStack = handler.getStackInSlot(StorageHandler.OUT);
        try {
            // process input
            if (!inStack.isEmpty()) {
                if (outStack.isEmpty()) {
                    outStack = inStack.copy();
                    handler.setStackInSlot(StorageHandler.OUT, outStack);
                    handler.setStackInSlot(StorageHandler.IN, ItemStack.EMPTY);
                } else if (ItemStackUtil.canMergeStrict(inStack, outStack)) {
                    if (poolStack.isEmpty()) {
                        poolStack = inStack.copy();
                        handler.setStackInSlot(StorageHandler.POOL, poolStack);
                    } else {
                        poolStack.grow(inStack.getCount());
                    }
                    handler.setStackInSlot(StorageHandler.IN, ItemStack.EMPTY);
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
                        handler.setStackInSlot(StorageHandler.OUT, newOut);
                    } else {
                        outStack.grow(flow);
                    }
                    // out partial
                    poolStack.shrink(flow);
                }
            }
            if (poolStack.isEmpty() || poolStack.getCount() <= 0) {
                handler.setStackInSlot(StorageHandler.POOL, ItemStack.EMPTY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        handler.deserializeNBT(compound);
        markDirty();
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.merge(handler.serializeNBT());
        return compound;
    }

    public ItemStack getDropWithNBT(Block block) {
        NBTTagCompound tileInfo = new NBTTagCompound();
        ItemStack dropStack = new ItemStack(block, 1);
        writeToNBT(tileInfo);
        dropStack.setTagCompound(new NBTTagCompound());
        dropStack.getTagCompound().setTag("tileInfo", tileInfo);
        return dropStack;
    }


    public void readFromNBTItemStack(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) return;
        NBTTagCompound tileInfo = itemStack.getTagCompound().getCompoundTag("tileInfo");
        handler.deserializeNBT(tileInfo);
        markDirty();
    }


    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return hasCapability(capability, facing)
                ? (T) handler
                : super.getCapability(capability, facing);
    }

    public StorageHandler getHandler() {
        return handler;
    }

    public String getName() {
        return "tile_slot_storage";
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

}
