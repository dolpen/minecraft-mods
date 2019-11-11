package net.dolpen.mcmod.ext.entities.tiles;

import net.minecraft.util.text.ITextComponent;

public class TileSingleSlotStorage extends TileInventory {

    public TileSingleSlotStorage() {
        super(1);
    }

    @Override
    public String getName() {
        return "tile_slot_storage";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }
}
