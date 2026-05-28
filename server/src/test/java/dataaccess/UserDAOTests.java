package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;

public class UserDAOTests {
    private static UserData existingUser;
    private static UserData newUser;
    private static UserDAO userDAO;

    @BeforeAll
    public static void init(){
        existingUser = new UserData("ExistingUser", "existingUserPassword", "eu@mail.com");
        newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
        try {
            userDAO = new SQLUserDAO();
        }
        catch (Exception e){
            System.err.println("Setup Failed");
        }
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        userDAO.clear();
        userDAO.createUser(existingUser);
    }

    @Test
    @DisplayName("Check existing user exists")
    public void checkUserExists() throws DataAccessException {
        Assertions.assertEquals(userDAO.getUser(existingUser.username()), existingUser);
    }

    @Test
    @DisplayName("Check new user does not exist")
    public void checkUserDoesNotExist() throws DataAccessException {
        Assertions.assertNull(userDAO.getUser(newUser.username()));
    }

    @Test
    @DisplayName("Try to add existing user")
    public void addExistingUser(){
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.createUser(existingUser));
    }

    @Test
    @DisplayName("Add new user")
    public void addNewUser(){
        Assertions.assertDoesNotThrow(() -> userDAO.createUser(newUser));
    }

    @Test
    @DisplayName("Clear DB")
    public void clearDB() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> userDAO.clear());
        Assertions.assertNull(userDAO.getUser(existingUser.username()));
    }

}
