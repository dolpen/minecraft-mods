package net.dolpen.mcmod.ext.capabilities.providers;

import net.dolpen.mcmod.ext.capabilities.IPlayerStatus;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerStatusProvider implements ICapabilityProvider {

    // インスタンス生成前に都度上書きDIされる（のでほぼNULLにならないしpublic）
    // これを DI するために CapabilityManager に登録する
    @CapabilityInject(IPlayerStatus.class)
    public static Capability<IPlayerStatus> INJECTED = null;

    // DIされたものから内部保持される、外部に参照しか渡らない
    private IPlayerStatus instance = INJECTED.getDefaultInstance();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == INJECTED;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return hasCapability(capability, facing) ? INJECTED.cast(this.instance) : null;
    }


}

