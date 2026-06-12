package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public class WebSocketService {
    private static final WsConnectionManager connections = new WsConnectionManager();
    private static AuthDAO authDAO;
    private static GameDAO gameDAO;

    public WebSocketService(){
        try{
            authDAO = new SQLAuthDAO();
            gameDAO = new SQLGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void connect(UserGameCommand command, Session session) throws DataAccessException, IOException {
        String authToken = command.getAuthToken();
        int gameID = command.getGameID();

        // Check if auth token exists
        AuthData auth = authDAO.getAuth(authToken);
        if(auth == null){
            connections.sendServerMsg(session, new ErrorMessage("You aren't authorized"));
            return;
        }
        // Check if gameID exists
        GameData game = gameDAO.getGame(gameID);
        if(game == null){
            connections.sendServerMsg(session, new ErrorMessage("That gameID doesn't exist"));
            return;
        }
        // add the connection to the connection manager
        connections.add(gameID, session);

        // Loads the game on the users machine
        LoadGameMessage loadGameMessage = new LoadGameMessage(game.game());
        connections.sendServerMsg(session, loadGameMessage);

        // Gets the color the user is playing as(or null)
        ChessGame.TeamColor color = getPlayerColor(game, auth);

        String msgToSend = color == null ?
                String.format("%s has joined the game as an observer", auth.username()) :
                String.format("%s has joined the game as %s",
                        auth.username(),
                        color.toString().toLowerCase(Locale.ROOT));

        connections.broadcast(game.gameID(),session, new NotificationMessage(msgToSend));
    }



    public void makeMove(MakeMoveCommand command, Session session){

    }
    public void leave(UserGameCommand command, Session session){

    }
    public void resign(UserGameCommand command, Session session){

    }

    /**
     * @param game the game data requested from the gameID
     * @param auth the auth data requested from the authToken
     * @return the color of the team the user with that authToken belongs to in that game (or null)
     */
    private static ChessGame.TeamColor getPlayerColor(GameData game, AuthData auth) {
        // check if the provided username is already in the game
        ChessGame.TeamColor userColor = null;
        if(Objects.equals(game.whiteUsername(), auth.username())){
            userColor = ChessGame.TeamColor.WHITE;
        }
        else if(Objects.equals(game.blackUsername(), auth.username())){
            userColor = ChessGame.TeamColor.BLACK;
        }
        return userColor;
    }
}
