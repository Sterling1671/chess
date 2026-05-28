package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO{
    public SQLGameDAO() throws DataAccessException {
        configureDatabase();
    }
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  games (
              `id` INT NOT NULL,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    /**
     * Adds the users table to the current database if it doesn't exist
     * @throws DataAccessException if the SQL fails
     */
    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE TABLE games";
            try (PreparedStatement deleteTableStatement = conn.prepareStatement(statement)) {
                deleteTableStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Failed");
        }
    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "INSERT INTO games (id, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement insertGameStatement = conn.prepareStatement(statement)) {
                String serialized = new Gson().toJson(gameData.game());

                insertGameStatement.setInt(1,gameData.gameID());
                insertGameStatement.setString(2,gameData.whiteUsername());
                insertGameStatement.setString(3,gameData.blackUsername());
                insertGameStatement.setString(4,gameData.gameName());
                insertGameStatement.setString(5,serialized);
                insertGameStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Failed");
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT id, whiteUsername, blackUsername, gameName, game FROM games WHERE id=?";
            try (PreparedStatement getGameStatement = conn.prepareStatement(statement)) {
                getGameStatement.setInt(1,gameID);
                try (ResultSet rs = getGameStatement.executeQuery()) {
                    if (rs.next()) {
                        return new GameData(
                                rs.getInt("id"),
                                rs.getString("whiteUsername"),
                                rs.getString("blackUsername"),
                                rs.getString("gameName"),
                                new Gson().fromJson(rs.getString("game"), ChessGame.class)
                        );
                    }
                }
            }
        }
        catch (Exception e) {
            throw new DataAccessException("Failed");
        }
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        List<GameData> result = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT id, whiteUsername, blackUsername, gameName, game FROM games";
            try (PreparedStatement listGamesStatement = conn.prepareStatement(statement)) {
                try (ResultSet rs = listGamesStatement.executeQuery()) {
                    while (rs.next()) {
                        result.add(new GameData(
                                rs.getInt("id"),
                                rs.getString("whiteUsername"),
                                rs.getString("blackUsername"),
                                rs.getString("gameName"),
                                new Gson().fromJson(rs.getString("game"), ChessGame.class)
                        ));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Failed");
        }
        return result;
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "UPDATE games SET whiteUsername=?, blackUsername=?, gameName=?, game=? WHERE id=?";
            try (PreparedStatement updateGamesStatement = conn.prepareStatement(statement)) {
                String serialized = new Gson().toJson(gameData.game());
                updateGamesStatement.setString(1,gameData.whiteUsername());
                updateGamesStatement.setString(2, gameData.blackUsername());
                updateGamesStatement.setString(3, gameData.gameName());
                updateGamesStatement.setString(4,serialized);
                updateGamesStatement.setInt(5, gameData.gameID());
                updateGamesStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Failed");
        }
    }


}
