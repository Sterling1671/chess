package client;

import chess.ChessGame;
import model.requests.*;
import org.junit.jupiter.api.*;
import server.Server;

import java.util.Random;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    private static final Random random = new Random();
    private static final String existingUserName = String.format("TestUser_%d",random.nextInt(100000));
    private static final String newUserName = String.format("TestNewUser_%d",random.nextInt(100000));
    private static final String newGameName = String.format("TestNewGame_%d",random.nextInt(10000));
    private static final RegisterRequest existingUser = new RegisterRequest(existingUserName,"ePassword","eEmail");
    private static final LoginRequest existingLogin = new LoginRequest(existingUserName,"ePassword");
    private static int yeet = 1;
    private static String authToken;
    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + String.format("%d",port) + "/");
        authToken = facade.register(existingUser).authToken();
    }

    @BeforeEach
    public void setup(){

        authToken = facade.login(existingLogin).authToken();

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void loginPos() {
        Assertions.assertDoesNotThrow(() -> facade.login(existingLogin));
    }
    @Test
    public void loginNeg() {
        Assertions.assertThrows(ResponseException.class, () -> facade.login(new LoginRequest("a","a")));
    }
    @Test
    public void regPos() {
        Assertions.assertDoesNotThrow(() -> facade.register(new RegisterRequest(newUserName,"newTestPassword", "newTestEmail")));
    }
    @Test
    public void regNeg() {
        Assertions.assertThrows(ResponseException.class, () -> facade.register(existingUser));
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
        Assertions.assertThrows(ResponseException.class, () -> yeet = facade.createGame(new CreateGameRequest(authToken, newGameName)).gameID());
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
        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(new JoinGameRequest(authToken, ChessGame.TeamColor.BLACK, random.nextInt(10000))));
    }
    @Test
    public void getGamePos() {
        Assertions.assertDoesNotThrow(() -> facade.getGame(new ListGamesRequest(authToken), yeet));
    }
    @Test
    public void getGameNeg() {
        Assertions.assertThrows(ResponseException.class, () -> facade.getGame(new ListGamesRequest(authToken), random.nextInt(10000)));
    }

}
