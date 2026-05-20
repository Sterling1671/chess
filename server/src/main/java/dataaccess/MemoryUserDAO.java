package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO{
    private static final List<UserData> allUsers = new ArrayList<>();

    @Override
    public void clear() {
        allUsers.clear();
    }

    @Override
    public void createUser(UserData userData) {
        allUsers.add(userData);
    }

    @Override
    public UserData getUser(String username) {
        for(UserData data : allUsers){
            if(Objects.equals(username, data.username())){
                return data;
            }
        }
        return null;
    }
}
