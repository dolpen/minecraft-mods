package net.dolpen.mcmod.ext.settings;

import net.minecraft.util.ResourceLocation;

public class Constants {

    public static final String MOD_ID = "dolpenext";
    public static final String MOD_NAME = "Dolpen Extras";
    // ツール定義
    public static final String TOOL_CUT = "axe";
    public static final String TOOL_DIG = "shovel";
    public static final String TOOL_MINE = "pickaxe";
    public static final ResourceLocation CAPABILITY_PLAYER = new ResourceLocation(
            MOD_ID,
            String.join(".", "capability", "player")
    );
    public static final String MOD_VERSION = "0.1.0";
    private static final String KEY_LABEL_PREFIX = MOD_ID + "." + "key";
    public static final String KEY_LABEL_TOGGLE = KEY_LABEL_PREFIX + "." + "toggle";
    private static final String MESSAGE_LABEL_PREFIX = MOD_ID + "." + "message";
    public static final String MESSAGE_LABEL_TOGGLE_ON = MESSAGE_LABEL_PREFIX + "." + "toggle.on";
    public static final String MESSAGE_LABEL_TOGGLE_OFF = MESSAGE_LABEL_PREFIX + "." + "toggle.off";
    private static final String PROXY_BASE = "net.dolpen.mcmod.ext.proxy";
    public static final String CLIENT_PROXY = PROXY_BASE + "." + "ClientProxy";
    public static final String SERVER_PROXY = PROXY_BASE + "." + "ServerProxy";

}
