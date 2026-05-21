package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO{
    private static final List<AuthData> AUTHS = new ArrayList<>();

    @Override
    public void clear() {
        AUTHS.clear();
    }

    @Override
    public void createAuth(AuthData authData) {
        AUTHS.add(authData);
    }

    @Override
    public AuthData getAuth(String authToken) {
        for(AuthData data : AUTHS){
            if(Objects.equals(authToken, data.authToken())){
                return data;
            }
        }
        return null;
    }

    @Override
    public AuthData getAuthByUser(String username){
        for(AuthData data : AUTHS){
            if(Objects.equals(username, data.username())){
                return data;
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(AuthData authData) {
        AUTHS.remove(authData);
    }
}
