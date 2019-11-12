package net.dolpen.mcmod.ext.capability.player;

public interface IPlayerStatus {

    boolean isChainBlockActionEnabled();

    boolean toggleChainBlockAction();

    String toStatusMessageKey();
}

