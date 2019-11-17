package net.dolpen.mcmod.lib.proxy;

import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IModProxy {

    void construct(FMLConstructionEvent event);

    void preInit(FMLPreInitializationEvent event);

    void init(FMLInitializationEvent event);

    void postInit(FMLPostInitializationEvent event);
}
