package server.handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.websocket.*;
import org.jetbrains.annotations.NotNull;
import service.WebSocketService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.function.Consumer;

public class WsRequestHandler implements Consumer<WsConfig> {
    private static final WebSocketService SERVICE = new WebSocketService();

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

    public void handleMessage(@NotNull WsMessageContext ctx) throws DataAccessException, IOException {
        Gson serializer = new Gson();
        UserGameCommand command = serializer.fromJson(ctx.message(), UserGameCommand.class);
        switch(command.getCommandType()){
            case CONNECT -> SERVICE.connect(command, ctx.session);
            case LEAVE -> SERVICE.leave(command, ctx.session);
            case RESIGN -> SERVICE.resign(command, ctx.session);
            case MAKE_MOVE -> {
                MakeMoveCommand moveCmd = serializer.fromJson(ctx.message(), MakeMoveCommand.class);
                SERVICE.makeMove(moveCmd, ctx.session);
            }
        }
    }

    public void handleClose(@NotNull WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }
}
