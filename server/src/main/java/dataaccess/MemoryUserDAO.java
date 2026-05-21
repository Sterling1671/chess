package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO{
    private static final List<UserData> USERS = new ArrayList<>();

    @Override
    public void clear() {
        USERS.clear();
    }

    @Override
    public void createUser(UserData userData) {
        USERS.add(userData);
    }

    @Override
    public UserData getUser(String username) {
        for(UserData data : USERS){
            if(Objects.equals(username, data.username())){
                return data;
            }
        }
        return null;
    }
}
