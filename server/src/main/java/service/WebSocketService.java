package service;

import dataaccess.WsConnectionManager;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

public class WebSocketService {
    private final WsConnectionManager connections = new WsConnectionManager();
        public void connect(UserGameCommand command, Session session){

        }
        public void makeMove(MakeMoveCommand command, Session session){

        }
        public void leave(UserGameCommand command, Session session){

        }
        public void resign(UserGameCommand command, Session session){

        }
}
