package client;

import websocket.messages.ServerMessage;

public interface WsServerMsgHandler {
    void handle(ServerMessage msg);
}
