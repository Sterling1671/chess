package model.results;

import model.AuthData;

public record RegisterResult(String username, String authToken) {
    public RegisterResult(AuthData d){
        this(d.username(),d.authToken());
    }
}
