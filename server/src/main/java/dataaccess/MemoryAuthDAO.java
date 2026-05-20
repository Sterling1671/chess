package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO{
    private static final List<AuthData> allAuth = new ArrayList<>();

    @Override
    public void clear() {
        allAuth.clear();
    }

    @Override
    public void createAuth(AuthData authData) {
        allAuth.add(authData);
    }

    @Override
    public AuthData getAuth(String authToken) {
        for(AuthData data : allAuth){
            if(Objects.equals(authToken, data.authToken())){
                return data;
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(AuthData authData) {
        allAuth.remove(authData);
    }
}
