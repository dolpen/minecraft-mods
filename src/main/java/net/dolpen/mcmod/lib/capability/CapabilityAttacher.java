package net.dolpen.mcmod.lib.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;

public class CapabilityAttacher {

    public final CapabilityHolder<Entity> player;
    public final CapabilityHolder<TileEntity> tile;

    public CapabilityAttacher() {
        player = new CapabilityHolder<>();
        tile = new CapabilityHolder<>();

    }

    public void handleTile(AttachCapabilitiesEvent<TileEntity> event) {
        tile.handle(event);
    }

    public void handleEntity(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof EntityPlayer)
            player.handle(event);
    }
}
