package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SQLAuthDAO implements AuthDAO{
    public SQLAuthDAO() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  auth (
              `authToken` varchar(256) NOT NULL,
              `name` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(name)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        DatabaseManager.configureDatabase(createStatements);
    }

    @Override
    public void clear() throws DataAccessException {
        DatabaseManager.clearTable("auth");
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "INSERT INTO auth (authToken, name) VALUES (?, ?)";
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
