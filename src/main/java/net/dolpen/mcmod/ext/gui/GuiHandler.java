package net.dolpen.mcmod.ext.gui;

import net.dolpen.mcmod.ext.entities.tiles.TileSingleSlotStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    public static final int SSS = 1;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (id) {
            case SSS:
                return new SingleSlotStorageContainer(player.inventory, (TileSingleSlotStorage) world.getTileEntity(new BlockPos(x, y, z)));
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (id) {
            case SSS:
                return new GuiSingleSlotStorageContainer(player.inventory, (TileSingleSlotStorage) world.getTileEntity(new BlockPos(x, y, z)));
        }
        return null;
    }
}