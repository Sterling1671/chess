package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.requests.CreateGameRequest;
import model.requests.JoinGameRequest;
import model.requests.ListGamesRequest;
import model.results.CreateGameResult;
import model.results.ListGamesResult;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;


public class GameService {

    private static final GameDAO GAME_DAO = new MemoryGameDAO();
    private static final AuthDAO AUTH_DAO = new MemoryAuthDAO();
    // The internet says I need to be thread safe for servers I guess
    private static final AtomicInteger GAME_ID_COUNT = new AtomicInteger(1);
    private static final ConcurrentLinkedQueue<Integer> RECYCLED_ID = new ConcurrentLinkedQueue<>();

    /**
     * Generates a unique game ID. Uses a queue to recycle old game IDs that aren't in use
     * @return the new gameID to be used
     */
    public static int generateGameID(){
        Integer id = RECYCLED_ID.poll();
        if(id != null){
            return id;
        }
        return GAME_ID_COUNT.getAndIncrement();
    }

    /**
     * Lists all current games in the Database
     * @param listGamesRequest a request object passed from the handler that contains a
     *                         valid authToken
     * @return a list of all games in the DB
     * @throws UnauthorizedException if the authToken isn't valid
     */
    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws UnauthorizedException, DataAccessException {
        String authToken = listGamesRequest.authToken();

        AuthService.checkIfAuthorized(authToken);
        return new ListGamesResult(new ArrayList<>(GAME_DAO.listGames()));
    }


    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws UnauthorizedException, DataAccessException {
        String authToken = createGameRequest.authToken();
        String gameName = createGameRequest.gameName();

        // Check if the operation is authorized
        AuthService.checkIfAuthorized(authToken);

        // If it is, make a new game with the requested game name
        int gameID = GameService.generateGameID();
        GameData gameToSave = new GameData(
          gameID,
          null,
          null,
          gameName,
          new ChessGame()
        );

        // Save the game
        GAME_DAO.createGame(gameToSave);

        return new CreateGameResult(gameID);
    }

    /**
     * Joins an already created game of chess
     * @param joinGameRequest a request object from the handler that includes an authToken,
     *                        a TeamColor, and a gameID
     * @throws UnauthorizedException If the given authorization token doesn't have permission,
     *                               or the gameID doesn't exist
     * @throws AlreadyTakenException If the game already has a player of the selected color
     */
    public void joinGame(JoinGameRequest joinGameRequest) throws UnauthorizedException, AlreadyTakenException, BadRequestException, DataAccessException {
        String authToken = joinGameRequest.authToken();
        ChessGame.TeamColor playerColor = joinGameRequest.playerColor();
        int gameId = joinGameRequest.gameID();

        // Check if user is authorized
        AuthService.checkIfAuthorized(authToken);

        // If authorized, get auth data
        AuthData authData = AUTH_DAO.getAuth(authToken);
        String username = authData.username();

        // Get game id if exists
        GameData gameData = GAME_DAO.getGame(gameId);
        if(gameData == null){
            throw new BadRequestException("bad request");
        }

        // Check to see if there is already player at color
        GameData dataToSave;
        if(playerColor == ChessGame.TeamColor.WHITE){
            if(Objects.equals(gameData.whiteUsername(), null)){
                dataToSave = new GameData(gameData, username, gameData.blackUsername());
            }
            else if(Objects.equals(gameData.whiteUsername(), username)){
                return;
            }
            else{
                throw new AlreadyTakenException("already taken");
            }
        }
        else{
            if(Objects.equals(gameData.blackUsername(), null)){
                dataToSave = new GameData(gameData, gameData.whiteUsername(), username);
            }
            else if(Objects.equals(gameData.blackUsername(), username)){
                return;
            }
            else{
                throw new AlreadyTakenException("already taken");
            }
        }

        // Remove the old game from file and add the new one
        GAME_DAO.updateGame(dataToSave);
    }

    /**
     * Clears the GameDAO associated with GameService
     */
    public void clear() throws DataAccessException {
        GAME_ID_COUNT.set(1);
        RECYCLED_ID.clear();
        GAME_DAO.clear();
    }
}
