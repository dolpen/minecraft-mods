package net.dolpen.mcmod.ext.gui;

import net.dolpen.mcmod.ext.entities.tiles.TileSingleSlotStorage;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;

import java.awt.*;

public class GuiSingleSlotStorageContainer extends GuiContainer {
    public GuiSingleSlotStorageContainer(IInventory playerinv, TileSingleSlotStorage te) {
        super(new SingleSlotStorageContainer(playerinv, te));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f, 0.0f);
        this.drawGradientRect(0,0,0,0, Color.GRAY.getRGB(), Color.GRAY.getRGB());
    }
}