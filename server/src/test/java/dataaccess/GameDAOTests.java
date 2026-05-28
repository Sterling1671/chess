package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.*;

public class GameDAOTests {
    private static GameDAO gameDAO;
    private static GameData existingGame;
    private static GameData gameHasBlackPlayer;
    private static GameData gameHasTwoPlayers;

    @BeforeAll
    public static void init(){
        try{
            gameDAO = new SQLGameDAO();
        } catch (DataAccessException e) {
            System.err.println("Failed setup");
        }
        existingGame = new GameData(
                1,
                null,
                null,
                "gameHasNoPlayers",
                new ChessGame());
        gameHasBlackPlayer = new GameData(
                1,
                null,
                "blackUsername",
                "gameHasOnePlayer",
                new ChessGame());
        gameHasTwoPlayers = new GameData(
                1,
                "whiteUsername",
                "blackUsername",
                "gameHasTwoPlayers",
                new ChessGame());
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDAO.clear();
        gameDAO.createGame(existingGame);
    }

    @Test
    @DisplayName("Check existing game")
    public void checkExistingGame() throws DataAccessException {
        Assertions.assertEquals(gameDAO.getGame(existingGame.gameID()),existingGame);
    }

}
