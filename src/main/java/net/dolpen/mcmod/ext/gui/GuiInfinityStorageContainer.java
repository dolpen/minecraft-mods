package net.dolpen.mcmod.ext.gui;

import net.dolpen.mcmod.ext.setting.Constants;
import net.dolpen.mcmod.ext.tile.TileInfinityStorage;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import java.text.NumberFormat;

public class GuiInfinityStorageContainer extends GuiContainer {

    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(
            Constants.MOD_ID, "textures/gui/infinity_storage.png"
    );
    private static final NumberFormat FORMAT = NumberFormat.getNumberInstance();
    private final TileInfinityStorage tile;

    public GuiInfinityStorageContainer(IInventory playerInventory, TileInfinityStorage tile) {
        super(new InfinityStorageContainer(playerInventory, tile));
        this.tile = tile;
    }


    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawWorldBackground(0);
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        int stored = tile.getField(3); // all stored count!
        this.drawCenteredString(
                this.fontRenderer,
                FORMAT.format(stored),
                this.xSize / 2,
                36 + 4,
                0
        );
    }
}