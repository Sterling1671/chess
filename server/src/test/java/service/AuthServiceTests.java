package service;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.UnauthorizedException;
import model.AuthData;
import org.junit.jupiter.api.*;

public class AuthServiceTests {

    private static AuthDAO authDAO;
    private static AuthData authorized;
    private static AuthData unauthorized;

    @BeforeAll
    public static void init() {
        authDAO = new MemoryAuthDAO();
        authorized = new AuthData("authorizedToken", "authorizedUser");
        unauthorized = new AuthData("unauthorizedToken", "unauthorizedUser");

    }

    @BeforeEach
    public void setup() {
        authDAO.clear();
        authDAO.createAuth(authorized);
    }

    @Test
    @DisplayName("Check authorized")
    public void checkAuthorized() {
        Assertions.assertDoesNotThrow(() -> AuthService.checkIfAuthorized(authorized.authToken()));
    }

    @Test
    @DisplayName("Check unauthorized")
    public void checkUnauthorized() {
        Assertions.assertThrows(UnauthorizedException.class,() -> AuthService.checkIfAuthorized(unauthorized.authToken()));
    }
}
