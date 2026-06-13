package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

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
        GameData game = gameDAO.getGame(command.getGameID());
        AuthData auth = authDAO.getAuth(command.getAuthToken());
        // Check if authorized
        String authError = checkIfAuthorized(game, auth, null);
        if(authError != null){
            connections.sendServerMsg(session, new ErrorMessage(authError));
            return;
        }

        // add the connection to the connection manager
        connections.add(game.gameID(), session);

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



    public void makeMove(MakeMoveCommand command, Session session) throws DataAccessException, IOException {

        ChessMove move = command.getMove();
        GameData game = gameDAO.getGame(command.getGameID());
        AuthData auth = authDAO.getAuth(command.getAuthToken());
        // Check if authorized
        String authError = checkIfAuthorized(game, auth, session);
        if(authError != null){
            connections.sendServerMsg(session, new ErrorMessage(authError));
            return;
        }

        // Check that the game isn't over
        if(game.game().isGameIsOver()){
            connections.sendServerMsg(session, new ErrorMessage("That game is over"));
            return;
        }

        // Check that the player is in the game
        ChessGame.TeamColor color = getPlayerColor(game, auth);
        if(color == null){
            connections.sendServerMsg(session, new ErrorMessage("You're not a player in this game"));
            return;
        }

        // Check that it's the players turn
        if(game.game().getTeamTurn() != color){
            connections.sendServerMsg(session, new ErrorMessage("It isn't your turn"));
            return;
        }

        // Try to make the move
        try{
            game.game().makeMove(move);
        } catch (InvalidMoveException e) {
            connections.sendServerMsg(session, new ErrorMessage("That isn't a valid move"));
            return;
        }

        // If everything else passes, update the database
        gameDAO.updateGame(game);

        // then broadcast the updated game to all parties
        connections.broadcast(game.gameID(), null, new LoadGameMessage(game.game()));

        // Send a notification with what move was made
        connections.broadcast(game.gameID(), session, new NotificationMessage(
                String.format("%s moved %s to %s",
                        color.toString().substring(0, 1).toUpperCase() +
                                color.toString().substring(1).toLowerCase(),
                        move.getStartPosition().toString(),
                        move.getEndPosition().toString()
        )));

        // Checks check conditions
        ChessGame.TeamColor oppositeColor = color == ChessGame.TeamColor.WHITE ?
                ChessGame.TeamColor.BLACK :
                ChessGame.TeamColor.WHITE;

        String oppositeColorString = oppositeColor.toString().substring(0, 1).toUpperCase() +
                oppositeColor.toString().substring(1).toLowerCase();

        if(game.game().isInCheckmate(oppositeColor)){
            connections.broadcast(game.gameID(), null, new NotificationMessage(
                    String.format("%s is in checkmate", oppositeColorString)
            ));
            game.game().setGameIsOver(true);
        }
        else if(game.game().isInStalemate(oppositeColor)){
            connections.broadcast(game.gameID(), null, new NotificationMessage(
                    "The game ends in a stalemate"
            ));
            game.game().setGameIsOver(true);
        }
        else if(game.game().isInCheck(oppositeColor)){
            connections.broadcast(game.gameID(), null, new NotificationMessage(
                    String.format("%s is in check", oppositeColorString)
            ));
        }

    }
    public void leave(UserGameCommand command, Session session) throws DataAccessException, IOException {
        GameData game = gameDAO.getGame(command.getGameID());
        AuthData auth = authDAO.getAuth(command.getAuthToken());
        // Check if authorized
        String authError = checkIfAuthorized(game, auth, session);
        if(authError != null){
            connections.sendServerMsg(session, new ErrorMessage(authError));
            return;
        }

        // leave the game if you are a player
        ChessGame.TeamColor color = getPlayerColor(game, auth);
        if(color != null && color.equals(ChessGame.TeamColor.WHITE)){
            GameData gameToSave = new GameData(game, null, game.blackUsername());
            gameDAO.updateGame(gameToSave);
        }
        else if(color != null && color.equals(ChessGame.TeamColor.BLACK)){
            GameData gameToSave = new GameData(game, game.whiteUsername(), null);
            gameDAO.updateGame(gameToSave);
        }


        // remove your session from the connection manager
        connections.remove(game.gameID(), session);

        // broadcast that you left
        connections.broadcast(game.gameID(), null, new NotificationMessage(
                String.format("%s has left the game", auth.username())
        ));
    }
    public void resign(UserGameCommand command, Session session) throws DataAccessException, IOException {
        GameData game = gameDAO.getGame(command.getGameID());
        AuthData auth = authDAO.getAuth(command.getAuthToken());
        // Check if authorized
        String authError = checkIfAuthorized(game, auth, session);
        if(authError != null){
            connections.sendServerMsg(session, new ErrorMessage(authError));
            return;
        }

        // Check if the session is an active player
        ChessGame.TeamColor color = getPlayerColor(game, auth);
        if(color == null){
            connections.sendServerMsg(session, new ErrorMessage("You aren't a player in this game"));
            return;
        }

        // Set the game to over and save it
        game.game().setGameIsOver(true);
        gameDAO.updateGame(game);

        // broadcast the message
        connections.broadcast(game.gameID(), null, new NotificationMessage(
                String.format("%s has resigned. The game is over", auth.username())
        ));
    }

    /**
     * @param gameData the game data object returned by the SQL
     * @param authData the auth data object returned by the SQL
     * @param session the session that should be in the associated game connections(can be null)
     * @return null if no errors, or a string containing the error message to be broadcast
     */
    private static String checkIfAuthorized(GameData gameData,  AuthData authData, Session session){
        if(authData == null){
            return "You aren't authorized";
        }

        // Check if gameID exists
        if(gameData == null){
            return "That gameID doesn't exist";
        }

        // Check that the player is joined
        connections.connections.computeIfAbsent(
                gameData.gameID(),
                k -> new ConcurrentHashMap<Session, Session>());
        if(session != null && connections.connections.get(gameData.gameID()).get(session) == null){
            return "You haven't joined that game";
        }
        return null;
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
