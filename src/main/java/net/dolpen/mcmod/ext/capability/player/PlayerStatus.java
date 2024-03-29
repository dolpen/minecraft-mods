package net.dolpen.mcmod.ext.capability.player;

import net.dolpen.mcmod.ext.setting.Constants;

public class PlayerStatus implements IPlayerStatus {

    private boolean chainActionEnabled = false;

    public boolean isChainBlockActionEnabled() {
        return chainActionEnabled;
    }

    public boolean toggleChainBlockAction() {
        chainActionEnabled ^= true;
        return chainActionEnabled;
    }

    @Override
    public String toStatusMessageKey() {
        return chainActionEnabled
                ? Constants.MESSAGE_LABEL_TOGGLE_ON
                : Constants.MESSAGE_LABEL_TOGGLE_OFF;
    }


}

