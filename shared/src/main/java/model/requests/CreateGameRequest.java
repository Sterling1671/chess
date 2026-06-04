package model.requests;

import com.google.gson.annotations.Expose;

public record CreateGameRequest(
        String authToken,
        @Expose String gameName) {
}
