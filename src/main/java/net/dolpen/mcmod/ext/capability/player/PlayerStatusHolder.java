package net.dolpen.mcmod.ext.capability.player;

import net.dolpen.mcmod.ext.setting.Constants;
import net.dolpen.mcmod.lib.capability.CapabilityProvider;
import net.dolpen.mcmod.lib.capability.storage.VoidStorage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class PlayerStatusHolder {

    public static final ResourceLocation LOCATION = new ResourceLocation(
            Constants.MOD_ID,
            String.join(".", "capability", "player")
    );
    @CapabilityInject(IPlayerStatus.class)
    public static Capability<IPlayerStatus> PLAYER_STATUS_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IPlayerStatus.class, new VoidStorage<>(), PlayerStatus::new);
    }

    // これ実行するときには CapabilityInject されてるよね...
    public static CapabilityProvider<IPlayerStatus> getProvider() {
        return new CapabilityProvider<>(PLAYER_STATUS_CAPABILITY);
    }

}

