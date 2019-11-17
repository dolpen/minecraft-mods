package net.dolpen.mcmod.ext.setting;

public class Constants {

    public static final String MOD_ID = "dolpenext";
    public static final String MOD_NAME = "Dolpen Extras";
    public static final String MOD_VERSION = "0.0.2";
    private static final String KEY_LABEL_PREFIX = MOD_ID + "." + "key";
    public static final String KEY_LABEL_TOGGLE = KEY_LABEL_PREFIX + "." + "toggle";
    private static final String MESSAGE_LABEL_PREFIX = MOD_ID + "." + "message";
    public static final String MESSAGE_LABEL_TOGGLE_ON = MESSAGE_LABEL_PREFIX + "." + "toggle.on";
    public static final String MESSAGE_LABEL_TOGGLE_OFF = MESSAGE_LABEL_PREFIX + "." + "toggle.off";
    private static final String PROXY_BASE = "net.dolpen.mcmod.ext.proxy";
    public static final String CLIENT_PROXY = PROXY_BASE + "." + "ClientProxy";
    public static final String SERVER_PROXY = PROXY_BASE + "." + "ServerProxy";

}
