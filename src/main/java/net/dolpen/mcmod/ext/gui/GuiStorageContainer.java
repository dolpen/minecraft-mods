package net.dolpen.mcmod.ext.gui;

import net.dolpen.mcmod.ext.setting.Constants;
import net.dolpen.mcmod.ext.tile.StorageHandler;
import net.dolpen.mcmod.ext.tile.TileStorage;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import java.text.NumberFormat;

public class GuiStorageContainer extends GuiContainer {

    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/gui/infinity_storage.png");
    private static final NumberFormat FORMAT = NumberFormat.getNumberInstance();
    private final StorageHandler storageHandler;

    public GuiStorageContainer(IInventory playerInventory, TileStorage tile) {
        super(new StorageContainer(playerInventory, tile));
        this.storageHandler = tile.getHandler();
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
        int pool = storageHandler.getStackInSlot(StorageHandler.POOL).getCount();
        if (pool <= 0) return;
        this.drawCenteredString(
                this.mc.fontRenderer,
                FORMAT.format(
                        storageHandler.getStackInSlot(StorageHandler.POOL).getCount()
                ) + " + ",
                this.xSize / 2,
                36 + 4,
                0
        );
    }

}