package client;

import chess.ChessGame;
import model.requests.*;
import org.junit.jupiter.api.*;
import server.Server;

import java.util.Random;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    private static final Random RANDOM = new Random();
    private static final String EXISTING_USER_NAME = String.format("TestUser_%d", RANDOM.nextInt(100000));
    private static final String NEW_USER_NAME = String.format("TestNewUser_%d", RANDOM.nextInt(100000));
    private static final String NEW_GAME_NAME = String.format("TestNewGame_%d", RANDOM.nextInt(10000));
    private static final RegisterRequest EXISTING_USER = new RegisterRequest(EXISTING_USER_NAME,"ePassword","eEmail");
    private static final LoginRequest EXISTING_LOGIN = new LoginRequest(EXISTING_USER_NAME,"ePassword");
    private static int yeet = 1;
    private static String authToken;
    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + String.format("%d",port) + "/");
        authToken = facade.register(EXISTING_USER).authToken();
    }

    @BeforeEach
    public void setup(){

        authToken = facade.login(EXISTING_LOGIN).authToken();

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void loginPos() {
        Assertions.assertDoesNotThrow(() -> facade.login(EXISTING_LOGIN));
    }
    @Test
    public void loginNeg() {
        Assertions.assertThrows(ResponseException.class, () -> facade.login(new LoginRequest("a","a")));
    }
    @Test
    public void regPos() {
        Assertions.assertDoesNotThrow(() -> facade.register(new RegisterRequest(NEW_USER_NAME,"newTestPassword", "newTestEmail")));
    }
    @Test
    public void regNeg() {
        Assertions.assertThrows(ResponseException.class, () -> facade.register(EXISTING_USER));
    }
    @Test
    public void logoutPos() {
        Assertions.assertDoesNotThrow(() -> facade.logout(new LogoutRequest(authToken)));
    }
    @Test
    public void logoutNeg() {
        Assertions.assertThrows(ResponseException.class, () -> facade.logout(new LogoutRequest("yeet")));
    }
    @Test
    public void createGamePos() {
        Assertions.assertThrows(ResponseException.class, () -> yeet = facade.createGame(new CreateGameRequest(authToken, NEW_GAME_NAME)).gameID());
    }
    @Test
    public void createGameNeg() {
        Assertions.assertThrows(ResponseException.class, () -> facade.createGame(new CreateGameRequest("yeet", "yote")));
    }
    @Test
    public void listGamesPos() {
        Assertions.assertDoesNotThrow(() -> facade.listGames(new ListGamesRequest(authToken)));
    }
    @Test
    public void listGamesNeg() {
        Assertions.assertThrows(ResponseException.class, () -> facade.listGames(new ListGamesRequest("badAuth")));
    }
    @Test
    public void joinGamePos() {
        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(new JoinGameRequest(authToken, ChessGame.TeamColor.WHITE, yeet)));
    }
    @Test
    public void joinGameNeg() {
        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(
                new JoinGameRequest(authToken, ChessGame.TeamColor.BLACK, RANDOM.nextInt(10000))));
    }
    @Test
    public void getGamePos() {
        Assertions.assertDoesNotThrow(() -> facade.getGame(new ListGamesRequest(authToken), yeet));
    }
    @Test
    public void getGameNeg() {
        Assertions.assertThrows(ResponseException.class, () -> facade.getGame(new ListGamesRequest(authToken), RANDOM.nextInt(10000)));
    }

}
