package service;

import dataaccess.*;
import model.UserData;
import model.requests.RegisterRequest;
import model.results.RegisterResult;
import org.junit.jupiter.api.*;


public class UserServiceTests {

    private static UserData existingUser;
    private static UserData newUser;
    private static UserDAO userDAO;
    private static AuthDAO authDAO;
    private static UserService service;


    @BeforeAll
    public static void init(){
        existingUser = new UserData("ExistingUser", "existingUserPassword", "eu@mail.com");
        newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        service = new UserService();
    }

    @BeforeEach
    public void setup() {
        userDAO.clear();
        userDAO.createUser(existingUser);
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
}
