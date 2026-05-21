package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public GameData(GameData gameData, String whiteUsername, String blackUsername){
        this(gameData.gameID(), whiteUsername, blackUsername, gameData.gameName(), gameData.game);
    }
}
