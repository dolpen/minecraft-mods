package net.dolpen.mcmod.ext.gui;

import net.dolpen.mcmod.ext.block.BlockInfinityStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    public static final int INFINITY_STORAGE = 1;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (id) {
            case INFINITY_STORAGE:
                return BlockInfinityStorage.getContainer(player, world, x, y, z);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (id) {
            case INFINITY_STORAGE:
                return BlockInfinityStorage.getGuiContainer(player, world, x, y, z);
        }
        return null;
    }
}