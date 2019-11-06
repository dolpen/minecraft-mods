package net.dolpen.mcmod.ext.proxy;

import net.dolpen.mcmod.ext.handlers.ServerEventHandler;
import net.dolpen.mcmod.lib.proxy.ModProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.SERVER)
public class ServerProxy extends ModProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
    }

}
