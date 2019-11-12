package net.dolpen.mcmod.ext.gui;

import net.dolpen.mcmod.ext.tile.TileStorage;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;

import java.awt.*;

public class GuiStorageContainer extends GuiContainer {
    public GuiStorageContainer(IInventory playerInventory, TileStorage tile) {
        super(new StorageContainer(playerInventory, tile));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f, 1.0f);
        int color = Color.GRAY.getRGB();
        this.drawGradientRect(guiLeft, guiTop, guiLeft + xSize, guiTop + ySize, color, color);
    }
}