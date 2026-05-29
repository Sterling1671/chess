package service;

import dataaccess.*;

import java.util.UUID;

public class AuthService {
    private static final AuthDAO AUTH_DAO;
    static {
        try{
             AUTH_DAO = new SQLAuthDAO();
        }
        catch(DataAccessException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates a new unique authToken
     * @return the new unique authToken
     */
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * Determines if an authToken is authorized to access the database
     * @param authToken the token to check
     * @throws UnauthorizedException if the token isn't authorized
     */
    public static void checkIfAuthorized(String authToken) throws UnauthorizedException, DataAccessException {
        if(AUTH_DAO.getAuth(authToken) == null){
            throw new UnauthorizedException("unauthorized");
        }
    }
    public void clear() throws DataAccessException {
        AUTH_DAO.clear();
    }
}
