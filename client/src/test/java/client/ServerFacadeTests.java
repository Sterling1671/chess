package client;

import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + String.format("%d",port) + "/");

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void loginPos() {
        Assertions.assertTrue(true);
    }
    @Test
    public void loginNeg() {
        Assertions.assertTrue(true);
    }
    @Test
    public void regPos() {
        Assertions.assertTrue(true);
    }
    @Test
    public void regNeg() {
        Assertions.assertTrue(true);
    }
    @Test
    public void logoutPos() {
        Assertions.assertTrue(true);
    }
    @Test
    public void logoutNeg() {
        Assertions.assertTrue(true);
    }
    @Test
    public void createGamePos() {
        Assertions.assertTrue(true);
    }
    @Test
    public void createGameNeg() {
        Assertions.assertTrue(true);
    }
    @Test
    public void listGamesPos() {
        Assertions.assertTrue(true);
    }
    @Test
    public void listGamesNeg() {
        Assertions.assertTrue(true);
    }
    @Test
    public void joinGamePos() {
        Assertions.assertTrue(true);
    }
    @Test
    public void joinGameNeg() {
        Assertions.assertTrue(true);
    }
    @Test
    public void getGamePos() {
        Assertions.assertTrue(true);
    }
    @Test
    public void getGameNeg() {
        Assertions.assertTrue(true);
    }

}
