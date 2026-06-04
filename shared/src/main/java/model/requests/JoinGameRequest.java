package model.requests;

import chess.ChessGame;
import com.google.gson.annotations.Expose;

public record JoinGameRequest(
        String authToken,
        @Expose ChessGame.TeamColor playerColor,
        @Expose int gameID) {
}


