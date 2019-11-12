package net.dolpen.mcmod.lib.capability;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityProvider<T> implements ICapabilityProvider {

    final Capability<T> cap;
    final T instance;

    public CapabilityProvider(Capability<T> cap) {
        this.cap = cap;
        this.instance = cap.getDefaultInstance();
    }

    @Override

    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == cap;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return hasCapability(capability, facing) ? cap.cast(this.instance) : null;
    }
}