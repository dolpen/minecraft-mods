package net.dolpen.mcmod.ext;

import net.dolpen.mcmod.ext.setting.Configuration;
import net.dolpen.mcmod.ext.setting.Constants;
import net.dolpen.mcmod.lib.logger.LogWrapper;
import net.dolpen.mcmod.lib.proxy.IModProxy;
import net.dolpen.mcmod.lib.setting.ConfigurationLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;

@Mod(
        modid = Constants.MOD_ID,
        name = Constants.MOD_NAME,
        version = Constants.MOD_VERSION
)
public class DolpenMain {

    // ちょっと Singleton パターンにしておこう...
    private static DolpenMain INSTANCE = null;

    @SidedProxy(clientSide = Constants.CLIENT_PROXY, serverSide = Constants.SERVER_PROXY)
    private static IModProxy proxy;
    private static LogWrapper logger = null;
    private Configuration configuration = null;

    public DolpenMain() {
        INSTANCE = this;
    }

    public static DolpenMain getInstance() {
        return INSTANCE;
    }

    public static LogWrapper getLogger() {
        return logger;
    }

    public Configuration getConfiguration() {
        return configuration;
    }


    @Mod.EventHandler
    //この関数でMODファイル自体をイベントの発火先にする。
    public void construct(FMLConstructionEvent event) {
        proxy.construct(event);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = new LogWrapper(event.getModLog());
        logger.setLevel(Level.WARN);
        ConfigurationLoader loader = new ConfigurationLoader(event.getSuggestedConfigurationFile());
        configuration = loader.load("general", Configuration::new);
        loader.saveIfNeeded();
        proxy.preInit(event);

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
