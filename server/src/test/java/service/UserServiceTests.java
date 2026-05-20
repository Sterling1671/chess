package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import model.requests.LoginRequest;
import model.requests.LogoutRequest;
import model.requests.RegisterRequest;
import model.results.LoginResult;
import model.results.RegisterResult;
import org.junit.jupiter.api.*;


public class UserServiceTests {

    private static UserData loggedInUser;
    private static AuthData loggedInUserAuth;
    private static UserData existingUser;
    private static UserData newUser;
    private static UserDAO userDAO;
    private static AuthDAO authDAO;
    private static UserService service;


    @BeforeAll
    public static void init(){
        loggedInUser = new UserData("loggedInUser", "loggedInUserPassword", "lu@mail.com");
        loggedInUserAuth = new AuthData("loggedInUserKey","loggedInUser");
        existingUser = new UserData("ExistingUser", "existingUserPassword", "eu@mail.com");
        newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        service = new UserService();
    }

    @BeforeEach
    public void setup() {
        userDAO.clear();
        authDAO.clear();
        userDAO.createUser(existingUser);
        userDAO.createUser(loggedInUser);
        authDAO.createAuth(loggedInUserAuth);

    }

    @Test
    @DisplayName("Register a new User")
    public void registerNewUser(){
        RegisterRequest request = new RegisterRequest(newUser);
        RegisterResult result = service.register(request);

        Assertions.assertEquals(result.username(),request.username(),
                "RegisterResult did not have the same username as the request");
        Assertions.assertNotNull(result.authToken(),"authToken was null");
        Assertions.assertEquals(userDAO.getUser(request.username()), newUser,
                "User in database doesn't match what was registered");
        Assertions.assertEquals(authDAO.getAuth(result.authToken()).username(), request.username(),
                "The username associated with the authToken doesn't match the request");
    }

    @Test
    @DisplayName("Try to register an existing user")
    public void registerExistingUser(){
        RegisterRequest request = new RegisterRequest(existingUser);
        Assertions.assertThrows(AlreadyTakenException.class, () -> service.register(request),
                "No exception was thrown for registry of existing user");
    }

    @Test
    @DisplayName("Login with existing user")
    public void loginExistingUser(){
        String username = existingUser.username();
        String password = existingUser.password();
        LoginRequest request = new LoginRequest(username, password);
        LoginResult result = service.login(request);
        String authToken = result.authToken();

        Assertions.assertEquals(new LoginResult(authDAO.getAuth(authToken)), result,
                "Result didn't match what was in the database");
        Assertions.assertEquals(result.username(),username,
                "The username in the DB doesn't match the requested username");
    }

    @Test
    @DisplayName("Login with new user")
    public void loginNewUser(){
        String username = newUser.username();
        String password = newUser.password();
        LoginRequest request = new LoginRequest(username, password);
        Assertions.assertThrows(UnauthorizedException.class, () -> service.login(request),
                "No exception was thrown for trying to log in as a new user");
    }

    @Test
    @DisplayName("Login with wrong password")
    public void loginWrongPassword(){
        String username = existingUser.username();
        String password = "WrongPassword";
        LoginRequest request = new LoginRequest(username, password);
        Assertions.assertThrows(UnauthorizedException.class, () -> service.login(request),
                "No exception was thrown for trying to log in with wrong password");
    }

    @Test
    @DisplayName("Logout logged in user")
    public void logoutLoggedInUser(){
        String authToken = loggedInUserAuth.authToken();
        Assertions.assertNotNull(authDAO.getAuth(authToken));

        LogoutRequest request = new LogoutRequest(authToken);
        service.logout(request);

        Assertions.assertNull(authDAO.getAuth(authToken));
    }
}
