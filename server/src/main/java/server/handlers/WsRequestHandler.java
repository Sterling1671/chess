package server.handlers;

import io.javalin.websocket.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class WsRequestHandler implements Consumer<WsConfig> {
    @Override
    public void accept(WsConfig wsConfig) {
        wsConfig.onConnect(this::handleConnect);
        wsConfig.onMessage(this::handleMessage);
        wsConfig.onClose(this::handleClose);
    }

    public void handleConnect(@NotNull WsConnectContext ctx) {
        ctx.enableAutomaticPings();
        System.out.println("Websocket connected");
    }

    public void handleMessage(@NotNull WsMessageContext ctx) {
        ctx.send("WebSocket response:" + ctx.message());
    }

    public void handleClose(@NotNull WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }
}
