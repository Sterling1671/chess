package dataaccess;

import model.UserData;
import java.sql.SQLException;
import java.sql.*;


public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        configureDatabase();
    }
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  users (
              `name` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`name`),
              INDEX(email)
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

    /**
     * Clears the users table with a TRUNCATE TABLE statement
     * @throws DataAccessException if the SQL failed
     */
    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE TABLE users";
            try (PreparedStatement deleteTableStatement = conn.prepareStatement(statement)) {
                deleteTableStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Failed");
        }
    }

    /**
     * Adds a userData object to the data structure
     * @param userData a userData object to be saved, password SHOULD ALREADY BE HASHED
     * @throws DataAccessException if SQL fails
     */
    @Override
    public void createUser(UserData userData) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "INSERT INTO users (name, password, email) VALUES (?, ?, ?)";
            try (PreparedStatement insertUserStatement = conn.prepareStatement(statement)) {
                insertUserStatement.setString(1,userData.username());
                insertUserStatement.setString(2, userData.password());
                insertUserStatement.setString(3,userData.email());
                insertUserStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Failed");
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT name, password, email FROM users WHERE name=?";
            try (PreparedStatement getUserStatement = conn.prepareStatement(statement)) {
                getUserStatement.setString(1,username);
                try (ResultSet rs = getUserStatement.executeQuery()) {
                    if (rs.next()) {
                        return new UserData(
                                rs.getString("name"),
                                rs.getString("password"),
                                rs.getString("email")
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
}
