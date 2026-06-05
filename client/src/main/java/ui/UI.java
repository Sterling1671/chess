package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;

import java.util.Collection;
import java.util.Objects;

public interface UI {
    String receiveInput();
    void displayHelp();
    void displayError(String error);
    void displayMessage(String message);
    void displayGames(Collection<GameData> games);

    default void displayGameBoardWhite(GameData game){
        ChessBoard board = game.game().getBoard();
        for(int row = 0; row < 10; row++){
            for(int col = 0; col < 10; col++){
                displayBoardFromPos(board, row, col);
            }
            System.out.println(EscapeSequences.RESET_BG_COLOR);
        }
    }
    default void displayGameBoardBlack(GameData game){
        ChessBoard board = game.game().getBoard();
        for(int row = 9; row >= 0; row--){
            for(int col = 9; col >= 0; col--){
                displayBoardFromPos(board, row, col);
            }
            System.out.println(EscapeSequences.RESET_BG_COLOR);
        }
    }

    default void displayBoardFromPos(ChessBoard board, int row, int col) {
        if(row == 0 | row == 9 | col == 0 | col == 9){
            String borderToString = getChessBorder(row, col);
            System.out.print(borderToString);
        }
        else{
            String pieceToString = getChessPiece(board.getPiece(new ChessPosition(row, col)));
            String backgroundColor = (row + col) % 2 == 0 ?
                    EscapeSequences.SET_BG_COLOR_LIGHT_GREY :
                    EscapeSequences.SET_BG_COLOR_DARK_GREY;
            System.out.print(backgroundColor + pieceToString);
        }
    }

    default String getChessBorder(int row, int col){
        String returnVal = EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.SET_TEXT_COLOR_WHITE;
        if((row == 0 || row == 9) && (col == 0 || col == 9)){
            return returnVal + EscapeSequences.EMPTY;
        }
        else if(row == 0 || row == 9){
            return returnVal + String.format("\u2002%c\u2002\u2002", col + 96);
        }
        else{
            return String.format(returnVal + String.format("\u2002%d\u2002\u2002",9 - row));
        }
    }
    default String getChessPiece(ChessPiece piece){
        if(piece ==  null){
            return EscapeSequences.EMPTY;
        }
        if(Objects.equals(piece.getTeamColor(), ChessGame.TeamColor.WHITE)){
            switch(piece.getPieceType()) {
                case KING -> {
                    return EscapeSequences.WHITE_KING;
                }
                case QUEEN -> {
                    return EscapeSequences.WHITE_QUEEN;
                }
                case BISHOP -> {
                    return EscapeSequences.WHITE_BISHOP;
                }
                case KNIGHT -> {
                    return EscapeSequences.WHITE_KNIGHT;
                }
                case ROOK -> {
                    return EscapeSequences.WHITE_ROOK;
                }
                case PAWN -> {
                    return EscapeSequences.WHITE_PAWN;
                }
                default -> {
                    return EscapeSequences.EMPTY;
                }
            }
        }
        else{
            switch (piece.getPieceType()) {
                case KING -> {
                    return EscapeSequences.BLACK_KING;
                }
                case QUEEN -> {
                    return EscapeSequences.BLACK_QUEEN;
                }
                case BISHOP -> {
                    return EscapeSequences.BLACK_BISHOP;
                }
                case KNIGHT -> {
                    return EscapeSequences.BLACK_KNIGHT;
                }
                case ROOK -> {
                    return EscapeSequences.BLACK_ROOK;
                }
                case PAWN -> {
                    return EscapeSequences.BLACK_PAWN;
                }
                default -> {
                    return EscapeSequences.EMPTY;
                }
            }
        }
    }
}
