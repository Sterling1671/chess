package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import model.requests.ClearRequest;
import model.requests.LoginRequest;
import model.requests.LogoutRequest;
import model.requests.RegisterRequest;
import model.results.LoginResult;
import model.results.RegisterResult;

public class UserService {

    /**
     * Registers a user in the database with the provided username, password and email.
     * Throws an exception if the username is already taken
     * @param registerRequest A request object passed from the handler that has fields with
     *                        username, password and email.
     * @return a RegisterResult object containing the username and authToken saved to the DB
     * @throws AlreadyTakenException if user is already taken, an exception is thrown
     */
    public RegisterResult register(RegisterRequest registerRequest) throws AlreadyTakenException {
        // Get the username from the request
        String username = registerRequest.username();

        // Get a DAO instance
        UserDAO myUserDAO = new MemoryUserDAO();

        // Check if there's already a user by that name
        UserData checkUsername = myUserDAO.getUser(username);

        // If there is throw an exception
        if(checkUsername != null){throw new AlreadyTakenException("username already taken");}

        // If not, fill the UserData and send it to the DAO
        UserData userToSave = new UserData(registerRequest);
        myUserDAO.createUser(userToSave);

        // Then fill out the auth data
        String authToken = AuthService.generateToken();
        AuthData authToSave = new AuthData(authToken, username);
        AuthDAO myAuthDAO = new MemoryAuthDAO();
        myAuthDAO.createAuth(authToSave);

        return new RegisterResult(username, authToken);
    }

    /**
     *
     * @param loginRequest A request object passed from the handler that has fields with
     *                     username and password.
     * @return a LoginResult object containing the username and authToken saved to the DB
     */
    public LoginResult login(LoginRequest loginRequest){
        return null;
    }
    public void logout(LogoutRequest logoutRequest){}
    public void clear(ClearRequest clearRequest){}
}
