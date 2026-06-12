package dataaccess;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class WsConnectionManager {
    public final ConcurrentHashMap<Integer, ConcurrentHashMap<Session, Session>> connections
            = new ConcurrentHashMap<>();

    public void add(int gameID, Session session) {
        connections.computeIfAbsent(gameID, k -> new ConcurrentHashMap<>());
        connections.get(gameID).put(session, session);
    }

    public void remove(int gameID, Session session) {
        connections.get(gameID).remove(session);
    }

    public void broadcast(int gameID, Session excludeSession, ServerMessage message) throws IOException {
        String msg = message.toJson();
        for (Session c : connections.get(gameID).values()) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }

    public void sendServerMsg(Session session, ServerMessage message) throws IOException {
        switch(message) {
            case NotificationMessage msg -> session.getRemote().sendString(msg.toJson());
            case LoadGameMessage msg -> session.getRemote().sendString(msg.toJson());
            case ErrorMessage msg -> session.getRemote().sendString(msg.toJson());
            default -> throw new IOException();
        }
    }
}
