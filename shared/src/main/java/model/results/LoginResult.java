package model.results;

import model.AuthData;

public record LoginResult(String username, String authToken) {
    public LoginResult(AuthData d){
        this(d.username(),d.authToken());
    }
}
