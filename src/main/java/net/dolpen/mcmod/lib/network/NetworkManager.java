package net.dolpen.mcmod.lib.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkManager {

    private final SimpleNetworkWrapper nw;
    private int discriminator;

    public NetworkManager(String channelName) {
        discriminator = 0;
        nw = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
    }

    public void sendToServer(IMessage message) {
        nw.sendToServer(message);
    }

    private <REQ extends IMessage, RES extends IMessage> void registerRequest(
            Class<? extends IMessageHandler<REQ, RES>> handler,
            Class<REQ> type, Side side
    ) {
        nw.registerMessage(handler, type, discriminator++, side);
    }

    public <REQ extends IMessage, RES extends IMessage> void registerServerRequest(
            Class<? extends IMessageHandler<REQ, RES>> handler, Class<REQ> type
    ) {
        registerRequest(handler, type, Side.SERVER);
    }

    public <REQ extends IMessage, RES extends IMessage> void registerClientRequest(
            Class<? extends IMessageHandler<REQ, RES>> handler, Class<REQ> type
    ) {
        registerRequest(handler, type, Side.CLIENT);
    }

}
