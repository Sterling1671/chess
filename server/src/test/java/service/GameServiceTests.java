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
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class GameServiceTests {
    private static GameDAO gameDAO;
    private static AuthDAO authDAO;
    private static GameData gameHasTwoPlayers;
    private static GameData gameHasWhitePlayer;
    private static GameData gameHasBlackPlayer;
    private static GameData gameHasNoPlayers;
    private static AuthData authorizedWhitePlayer;
    private static AuthData authorizedBlackPlayer;
    private static AuthData authorizedPlayer;
    private static AuthData unauthorizedPlayer;
    private static GameService service;


    @BeforeAll
    public static void init() {
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        service = new GameService();
        gameHasNoPlayers = new GameData(
                GameService.generateGameID(),
                null,
                null,
                "gameHasNoPlayers",
                new ChessGame());
        gameHasBlackPlayer = new GameData(
                GameService.generateGameID(),
                null,
                "blackUsername",
                "gameHasNoPlayers",
                new ChessGame());
        gameHasWhitePlayer = new GameData(
                GameService.generateGameID(),
                "whiteUsername",
                null,
                "gameHasNoPlayers",
                new ChessGame());
        gameHasTwoPlayers = new GameData(
                GameService.generateGameID(),
                "whiteUsername",
                "blackUsername",
                "gameHasNoPlayers",
                new ChessGame());
        authorizedWhitePlayer = new AuthData(
          "authorizedWhitePlayerToken",
          "whiteUsername"
        );
        authorizedBlackPlayer = new AuthData(
                "authorizedBlackPlayerToken",
                "blackUsername"
        );
        authorizedPlayer = new AuthData(
                "authorizedPlayerToken",
                "genericUsername"
        );
        unauthorizedPlayer = new AuthData(
                "unauthorizedPlayerToken",
                "genericUnauthorizedUsername"
        );
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDAO.clear();
        authDAO.clear();
        authDAO.createAuth(authorizedPlayer);
        authDAO.createAuth(authorizedWhitePlayer);
        authDAO.createAuth(authorizedBlackPlayer);
        gameDAO.createGame(gameHasNoPlayers);
        gameDAO.createGame(gameHasBlackPlayer);
        gameDAO.createGame(gameHasWhitePlayer);
        gameDAO.createGame(gameHasTwoPlayers);
    }

    @Test
    @DisplayName("List all games authorized")
    public void listGamesAuthorized() throws DataAccessException {
        ListGamesRequest request = new ListGamesRequest(authorizedPlayer.authToken());
        ListGamesResult result = service.listGames(request);
        Collection<GameData> correctList = new ArrayList<>(List.of(
                gameHasNoPlayers,
                gameHasBlackPlayer,
                gameHasWhitePlayer,
                gameHasTwoPlayers));
        Assertions.assertEquals(correctList, result.games(),
                "ListGames didn't show all the games");
    }

    @Test
    @DisplayName("List all games unauthorized")
    public void listGamesUnauthorized(){
        ListGamesRequest request = new ListGamesRequest(unauthorizedPlayer.authToken());
        Assertions.assertThrows(UnauthorizedException.class, () -> service.listGames(request));
    }

    @Test
    @DisplayName("Create game authorized")
    void createGameAuthorized() throws DataAccessException {
        CreateGameRequest request = new CreateGameRequest(authorizedPlayer.authToken(),"Authorized Game");
        CreateGameResult result = service.createGame(request);
        Assertions.assertEquals(gameDAO.getGame(result.gameID()).gameName(),request.gameName());
    }

    @Test
    @DisplayName("Create game unauthorized")
    void createGameUnauthorized() {
        CreateGameRequest request = new CreateGameRequest(unauthorizedPlayer.authToken(),"Unauthorized Game");
        Assertions.assertThrows(UnauthorizedException.class, () -> service.createGame(request));
    }

    @Test
    @DisplayName("Join game unauthorized")
    public void joinGameUnauthorized() {
        JoinGameRequest request = new JoinGameRequest(
                unauthorizedPlayer.authToken(),
                ChessGame.TeamColor.WHITE,
                gameHasNoPlayers.gameID());
        Assertions.assertThrows(UnauthorizedException.class, () -> service.joinGame(request));
    }

    @Test
    @DisplayName("Join game badID")
    public void joinGameBadID() {
        JoinGameRequest request = new JoinGameRequest(
                authorizedPlayer.authToken(),
                ChessGame.TeamColor.WHITE,
                100);
        Assertions.assertThrows(BadRequestException.class, () -> service.joinGame(request));
    }

    @Test
    @DisplayName("Join game with no players")
    public void joinGameNoPlayers() throws DataAccessException {
        JoinGameRequest request = new JoinGameRequest(
                authorizedPlayer.authToken(),
                ChessGame.TeamColor.WHITE,
                gameHasNoPlayers.gameID());
        service.joinGame(request);
        Assertions.assertEquals(gameDAO.getGame(gameHasNoPlayers.gameID()).whiteUsername(),authorizedPlayer.username(),
                "White username did not match given username");
    }

    @Test
    @DisplayName("Join game with two players")
    public void joinGameTwoPlayers() {
        JoinGameRequest request = new JoinGameRequest(
                authorizedPlayer.authToken(),
                ChessGame.TeamColor.WHITE,
                gameHasTwoPlayers.gameID());
        Assertions.assertThrows(AlreadyTakenException.class, () -> service.joinGame(request));
    }
}