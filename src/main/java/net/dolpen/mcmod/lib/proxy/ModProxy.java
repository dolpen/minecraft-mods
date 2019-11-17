package net.dolpen.mcmod.lib.proxy;

import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public abstract class ModProxy implements IModProxy {

    public void construct(FMLConstructionEvent event) {
    }

    public void preInit(FMLPreInitializationEvent event) {
    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {
    }
}
