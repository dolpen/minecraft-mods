package net.dolpen.mcmod.ext.capabilities;

public interface IPlayerStatus {

    boolean isChainBlockActionEnabled();

    boolean toggleChainBlockAction();

    String toStatusMessageKey();
}

