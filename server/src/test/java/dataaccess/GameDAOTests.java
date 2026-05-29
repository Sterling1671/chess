package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

public class GameDAOTests {
    private static GameDAO gameDAO;
    private static GameData existingGame;
    private static GameData gameHasBlackPlayer;
    private static GameData newGame;

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
        newGame = new GameData(
                2,
                null,
                null,
                "newGame",
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

    @Test
    @DisplayName("Check new game")
    public void checkNewGame() throws DataAccessException {
        Assertions.assertNull(gameDAO.getGame(newGame.gameID()));
    }

    @Test
    @DisplayName("Update game")
    public void updateGame() throws DataAccessException {
        Assertions.assertEquals(gameDAO.getGame(existingGame.gameID()), existingGame);
        Assertions.assertDoesNotThrow(() -> gameDAO.updateGame(gameHasBlackPlayer));
        Assertions.assertNotEquals(gameDAO.getGame(existingGame.gameID()), existingGame);
        Assertions.assertEquals(gameDAO.getGame(existingGame.gameID()), gameHasBlackPlayer);
    }

    @Test
    @DisplayName("Fail to update game")
    public void failUpdateGame() throws DataAccessException{
        Assertions.assertDoesNotThrow(() -> gameDAO.updateGame(newGame));
        Assertions.assertNull(gameDAO.getGame(newGame.gameID()));
    }

    @Test
    @DisplayName("List Games")
    public void listGames() throws DataAccessException {
        List<GameData> list = new ArrayList<>(List.of(
                existingGame,
                newGame
        ));
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame(newGame));
        Assertions.assertEquals(gameDAO.listGames(), list);
    }

    @Test
    @DisplayName("clear db")
    public void clear() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> gameDAO.clear());
        Assertions.assertNull(gameDAO.getGame(existingGame.gameID()));
    }

}
