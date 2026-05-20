package model.requests;

import model.UserData;

public record RegisterRequest(String username, String password, String email) {
    public RegisterRequest(UserData d){
        this(d.username(), d.password(), d.email());
    }
}
