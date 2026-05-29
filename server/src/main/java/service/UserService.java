package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import model.requests.LoginRequest;
import model.requests.LogoutRequest;
import model.requests.RegisterRequest;
import model.results.LoginResult;
import model.results.RegisterResult;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

public class UserService {

    private static final UserDAO USER_DAO;
    private static final AuthDAO AUTH_DAO;
    static {
        try {
            USER_DAO = new SQLUserDAO();
            AUTH_DAO = new SQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * Registers a user in the database with the provided username, password and email.
     * Throws an exception if the username is already taken
     * @param registerRequest A request object passed from the handler that has fields with
     *                        username, password and email.
     * @return a RegisterResult object containing the username and authToken saved to the DB
     * @throws AlreadyTakenException if user is already taken, an exception is thrown
     */
    public RegisterResult register(RegisterRequest registerRequest) throws AlreadyTakenException, DataAccessException {
        // Get the username from the request
        String username = registerRequest.username();

        // Check if there's already a user by that name
        UserData checkUsername = USER_DAO.getUser(username);

        // If there is throw an exception
        if(checkUsername != null){throw new AlreadyTakenException("username already taken");}

        // If not, fill the UserData and send it to the DAO
        UserData userReceived = new UserData(registerRequest);
        // Hash the password
        UserData userToSave = new UserData(userReceived, BCrypt.hashpw(userReceived.password(), BCrypt.gensalt()));
        // Save the user
        USER_DAO.createUser(userToSave);

        // Then fill out the auth data
        String authToken = AuthService.generateToken();
        AuthData authToSave = new AuthData(authToken, username);
        AUTH_DAO.createAuth(authToSave);

        return new RegisterResult(authToSave);
    }

    /**
     * Logs an existing user in to the database, returning an authToken for them to use
     * @param loginRequest A request object passed from the handler that has fields with
     *                     username and password.
     * @return a LoginResult object containing the username and authToken saved to the DB
     * @throws UnauthorizedException if the user doesn't have correct credentials
     */
    public LoginResult login(LoginRequest loginRequest) throws UnauthorizedException, DataAccessException {
        // Get the username and password from the request
        String username = loginRequest.username();
        String password = loginRequest.password();

        // Check if there's already a UserData by that name
        UserData checkData = USER_DAO.getUser(username);

        // If there isn't throw an exception
        if(checkData == null){throw new UnauthorizedException("unauthorized");}

        // Now check the password
        try {
            if (!BCrypt.checkpw(password, checkData.password())) {
                throw new UnauthorizedException("unauthorized");
            }
        }
        catch(IllegalArgumentException e){
            if (!Objects.equals(checkData.password(),password)){
                throw new UnauthorizedException("unauthorized");
            }
        }

        // Generate a new authToken and make a new AuthData object
        String authToken = AuthService.generateToken();
        AuthData authToSave = new AuthData(authToken, username);
        AUTH_DAO.createAuth(authToSave);
        return new LoginResult(authToSave);
    }

    /**
     * Logs a user out of the database, removing their auth token but keeping username
     * @param logoutRequest a LogoutRequest object containing authToken to be removed from DB
     * @throws UnauthorizedException if the user doesn't have correct credentials
     */
    public void logout(LogoutRequest logoutRequest) throws UnauthorizedException, DataAccessException {
        // Get the auth token from the request
        String authToken = logoutRequest.authToken();

        // Check if it exists
        AuthData checkAuth = AUTH_DAO.getAuth(authToken);
        if(checkAuth == null){
            throw new UnauthorizedException("unauthorized");
        }

        // If it does exist, remove it
        AUTH_DAO.deleteAuth(checkAuth);
    }

    /**
     * Clears the UserDAO associated with UserService
     */
    public void clear() throws DataAccessException {
        USER_DAO.clear();
    }
}
