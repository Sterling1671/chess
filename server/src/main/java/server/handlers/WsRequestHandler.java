package server.handlers;

import com.google.gson.Gson;
import io.javalin.websocket.*;
import org.jetbrains.annotations.NotNull;
import service.WebSocketService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.util.function.Consumer;

public class WsRequestHandler implements Consumer<WsConfig> {
    private static final WebSocketService service = new WebSocketService();

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
        Gson serializer = new Gson();
        UserGameCommand command = serializer.fromJson(ctx.message(), UserGameCommand.class);
        switch(command.getCommandType()){
            case CONNECT -> service.connect(command, ctx.session);
            case LEAVE -> service.leave(command, ctx.session);
            case RESIGN -> service.resign(command, ctx.session);
            case MAKE_MOVE -> service.makeMove(
                    serializer.fromJson(ctx.message(), MakeMoveCommand.class),
                    ctx.session
            );
        }
    }

    public void handleClose(@NotNull WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }
}
