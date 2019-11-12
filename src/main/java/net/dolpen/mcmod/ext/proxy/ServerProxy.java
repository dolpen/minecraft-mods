package net.dolpen.mcmod.ext.proxy;

import net.dolpen.mcmod.ext.handler.ServerEventHandler;
import net.dolpen.mcmod.ext.handler.UniversalEventHandler;
import net.dolpen.mcmod.lib.proxy.ModProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.SERVER)
public class ServerProxy extends ModProxy {

    private final UniversalEventHandler handler = new ServerEventHandler();

    @Mod.EventHandler
    public void construct(FMLConstructionEvent event) {
        MinecraftForge.EVENT_BUS.register(handler);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        handler.initNetwork();
    }
}
