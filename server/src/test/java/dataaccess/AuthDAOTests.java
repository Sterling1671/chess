package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.*;

public class AuthDAOTests {
    private static AuthData existingAuth;
    private static AuthData newAuth;
    private static AuthDAO authDAO;

    @BeforeAll
    public static void init(){
        existingAuth = new AuthData("authorizedToken", "authorizedUser");
        newAuth = new AuthData("unauthorizedToken", "unauthorizedUser");
        try {
            authDAO = new SQLAuthDAO();
        }
        catch (Exception e){
            System.err.println("Setup Failed");
        }
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        authDAO.clear();
        authDAO.createAuth(existingAuth);
    }

    @Test
    @DisplayName("Check existing auth")
    public void checkExistingAuth() throws DataAccessException {
        Assertions.assertEquals(authDAO.getAuth(existingAuth.authToken()), existingAuth);
    }

    @Test
    @DisplayName("Check new auth")
    public void checkNewAuth() throws DataAccessException {
        Assertions.assertNull(authDAO.getAuth(newAuth.authToken()));
    }

    @Test
    @DisplayName("Check adding auth")
    public void addAuth(){
        Assertions.assertDoesNotThrow(() -> authDAO.createAuth(newAuth));
    }

    @Test
    @DisplayName("Check adding existing auth")
    public void addExistingAuth(){
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.createAuth(existingAuth));
    }

    @Test
    @DisplayName("Check clear")
    public void checkClear() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> authDAO.clear());
        Assertions.assertNull(authDAO.getAuth(existingAuth.authToken()));
    }
}
