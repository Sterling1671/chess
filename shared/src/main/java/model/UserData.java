package model;

import model.requests.RegisterRequest;

public record UserData(String username, String password, String email) {
    public UserData(RegisterRequest r){
        this(r.username(),r.password(),r.email());
    }
    public UserData(UserData u, String password){
        this(u.username(),password,u.email());
    }
}
