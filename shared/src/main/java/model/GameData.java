package model;

import chess.ChessGame;

public record GameData(int gameId, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public GameData(GameData gameData, String whiteUsername, String blackUsername){
        this(gameData.gameId(), whiteUsername, blackUsername, gameData.gameName(), gameData.game);
    }
}
