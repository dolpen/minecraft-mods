package net.dolpen.mcmod.ext;

import net.dolpen.mcmod.ext.models.BlockStateGroup;
import net.dolpen.mcmod.ext.settings.Configuration;
import net.dolpen.mcmod.ext.settings.Constants;
import net.dolpen.mcmod.lib.logger.LogWrapper;
import net.dolpen.mcmod.lib.proxy.IModProxy;
import net.dolpen.mcmod.lib.settings.ConfigurationLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;

import java.util.Arrays;

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
        final LogWrapper logger = getLogger();
        logger.info("cut-trigger:" + BlockStateGroup.CUT_TRIGGER.toString());
        logger.info("cut-leaves:" + BlockStateGroup.CUT_LEAVES.toString());
        logger.info("mine-trigger:" + BlockStateGroup.MINE_TRIGGER.toString());
        logger.info("dig-trigger:" + BlockStateGroup.DIG_TRIGGER.toString());
        Arrays.stream(BlockStateGroup.DIG_CHAIN).forEach(g ->
                logger.info("dig-chain:" + g.toString())
        );
        logger.info("hoe-trigger:" + BlockStateGroup.HOE_TRIGGER.toString());
        logger.info("hoe-removable:" + BlockStateGroup.HOE_REMOVABLE.toString());
    }
}
