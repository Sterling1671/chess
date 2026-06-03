package model.requests;

import chess.ChessGame;
import com.google.gson.annotations.Expose;

public record JoinGameRequest(
        @Expose String authToken,
        @Expose ChessGame.TeamColor playerColor,
        int gameID) {
}


