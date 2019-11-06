package net.dolpen.mcmod.ext.handlers;


import net.dolpen.mcmod.ext.network.ToggleMessage;
import net.dolpen.mcmod.ext.settings.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.Objects;

/**
 * クライアントサイドのイベントを吸い上げて各種アクションを行うdispatcher
 */
public class ClientEventHandler extends UniversalEventHandler {

    private static final KeyBinding KEY_BINDING_TOGGLE = new KeyBinding(
            I18n.format(Constants.KEY_LABEL_TOGGLE),
            Keyboard.KEY_K,
            Constants.MOD_NAME
    );

    public ClientEventHandler() {
        super();
        ClientRegistry.registerKeyBinding(KEY_BINDING_TOGGLE);
    }

    private boolean isHandlerEnabled() {
        Minecraft mc = Minecraft.getMinecraft();
        return mc.inGameHasFocus && !mc.isGamePaused() && Objects.nonNull(mc.player);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!isHandlerEnabled()) return;
        if (event.phase != TickEvent.Phase.END) return;
        handleKeyPressEvent();
    }

    private void handleKeyPressEvent() {
        if (KEY_BINDING_TOGGLE.isPressed()) {
            networkManager.sendToServer(new ToggleMessage());
        }
    }

}
