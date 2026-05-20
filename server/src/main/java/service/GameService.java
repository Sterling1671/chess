package service;

import chess.ChessGame;
import dataaccess.*;
import model.GameData;
import model.requests.CreateGameRequest;
import model.requests.JoinGameRequest;
import model.requests.ListGamesRequest;
import model.results.CreateGameResult;
import model.results.ListGamesResult;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;


public class GameService {

    private static final UserDAO myUserDAO = new MemoryUserDAO();
    private static final GameDAO myGameDAO = new MemoryGameDAO();
    private static final AuthDAO myAuthDAO = new MemoryAuthDAO();
    // The internet says I need to be thread safe for servers I guess
    private static final AtomicInteger gameIDCount = new AtomicInteger(1);
    private static final ConcurrentLinkedQueue<Integer> recycledID = new ConcurrentLinkedQueue<>();

    /**
     * Generates a unique game ID. Uses a queue to recycle old game IDs that aren't in use
     * @return the new gameID to be used
     */
    public static int generateGameID(){
        Integer ID = recycledID.poll();
        if(ID != null){
            return ID;
        }
        return gameIDCount.getAndIncrement();
    }

    /**
     * Lists all current games in the Database
     * @param listGamesRequest a request object passed from the handler that contains a
     *                         valid authToken
     * @return a list of all games in the DB
     * @throws UnauthorizedException if the authToken isn't valid
     */
    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws UnauthorizedException{
        String authToken = listGamesRequest.authToken();

        AuthService.checkIfAuthorized(authToken);
        return new ListGamesResult(new ArrayList<>(myGameDAO.listGames()));
    }


    public CreateGameResult createGame(CreateGameRequest createGameRequest){
        String authToken = createGameRequest.authToken();
        String gameName = createGameRequest.gameName();

        // Check if the operation is authorized
        AuthService.checkIfAuthorized(authToken);

        // If it is, make a new game with the requested game name
        int gameID = GameService.generateGameID();
        GameData gameToSave = new GameData(
          gameID,
          "",
          "",
          gameName,
          new ChessGame()
        );

        // Save the game
        myGameDAO.createGame(gameToSave);

        return new CreateGameResult(gameID);
    }
    public void joinGame(JoinGameRequest joinGameRequest){}
    public void clear(){
        gameIDCount.set(1);
        recycledID.clear();
    }
}
