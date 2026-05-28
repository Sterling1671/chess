package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO{
    public SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `authToken` varchar(256) NOT NULL,
              `name` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(name)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
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
            String statement = "TRUNCATE TABLE auth";
            try (PreparedStatement deleteTableStatement = conn.prepareStatement(statement)) {
                deleteTableStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Failed");
        }
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "INSERT INTO auth (authKey, name) VALUES (?, ?)";
            try (PreparedStatement createAuthStatement = conn.prepareStatement(statement)) {
                createAuthStatement.setString(1,authData.authToken());
                createAuthStatement.setString(2, authData.username());
                createAuthStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Failed");
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT authToken, name FROM auth WHERE authToken=?";
            try (PreparedStatement getAuthStatement = conn.prepareStatement(statement)) {
                getAuthStatement.setString(1,authToken);
                try (ResultSet rs = getAuthStatement.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(
                                rs.getString("authToken"),
                                rs.getString("name")
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
    public AuthData getAuthByUser(String username) throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT authToken, name FROM auth WHERE name=?";
            try (PreparedStatement getAuthStatement = conn.prepareStatement(statement)) {
                getAuthStatement.setString(1,username);
                try (ResultSet rs = getAuthStatement.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(
                                rs.getString("authToken"),
                                rs.getString("name")
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
    public void deleteAuth(AuthData authData) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "DELETE FROM auth WHERE authToken=?";
            try (PreparedStatement deleteAuthStatement = conn.prepareStatement(statement)) {
                deleteAuthStatement.setString(1, authData.authToken());
                deleteAuthStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Failed");
        }
    }
}
