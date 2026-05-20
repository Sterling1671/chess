package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void clear();
    void createAuth(AuthData authData);
    AuthData getAuth(String authToken);
    AuthData getAuthByUser(String username);
    void deleteAuth(AuthData authData);
}
