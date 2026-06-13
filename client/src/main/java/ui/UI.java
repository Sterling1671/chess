package ui;

import chess.*;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public interface UI {
    String receiveInput();
    void displayHelp();
    void displayError(String error);
    void displayMessage(String message);
    void displayGames(Collection<GameData> games);

    default void displayGameBoard(ChessGame game, ChessPosition positionToCheck, ChessGame.TeamColor color){
        if(ChessGame.TeamColor.BLACK.equals(color)){
            displayGameBoardBlack(game, positionToCheck);
        }
        else{
            displayGameBoardWhite(game, positionToCheck);
        }
    }

    default void displayGameBoardWhite(ChessGame game, ChessPosition positionToCheck){
        ChessBoard board = game.getBoard();
        List<ChessMove> allValidMoves = (positionToCheck != null && game.validMoves(positionToCheck) != null)
                ? new ArrayList<>(game.validMoves(positionToCheck))
                : new ArrayList<>();

        for (int row = 9; row >= 0; row--) {
            for (int col = 0; col < 10; col++) {
                displayBoardWithHighlight(allValidMoves, row, col, board);
            }
            System.out.println(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
        }
    }

    default void displayGameBoardBlack(ChessGame game, ChessPosition positionToCheck){
        ChessBoard board = game.getBoard();
        List<ChessMove> allValidMoves = (positionToCheck != null && game.validMoves(positionToCheck) != null)
                ? new ArrayList<>(game.validMoves(positionToCheck))
                : new ArrayList<>();

        for (int row = 0; row < 10; row++) {
            for (int col = 9; col >= 0; col--) {
                displayBoardWithHighlight(allValidMoves, row, col, board);
            }
            System.out.println(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
        }
    }

    private void displayBoardWithHighlight(List<ChessMove> allValidMoves, int row, int col, ChessBoard board) {
        boolean highlight = false;
        for(ChessMove move : allValidMoves) {
            if(move.getEndPosition().getRow() == row && move.getEndPosition().getColumn() == col) {
                highlight = true;
                break;
            }
        }
        displayBoardFromPos(board, row, col, highlight);
    }

    default void displayBoardFromPos(ChessBoard board, int row, int col, boolean highlight) {
        if(row == 0 || row == 9 || col == 0 || col == 9){
            String borderToString = getChessBorder(row, col);
            System.out.print(borderToString);
        }
        else{
            String pieceToString = getChessPiece(board.getPiece(new ChessPosition(row, col)));

            String backgroundColor = (row + col) % 2 == 0 ?
                    (highlight ?
                     EscapeSequences.SET_BG_COLOR_DARK_GREEN :
                     EscapeSequences.SET_BG_COLOR_DARK_GREY) :
                    (highlight ?
                     EscapeSequences.SET_BG_COLOR_GREEN :
                     EscapeSequences.SET_BG_COLOR_LIGHT_GREY);

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
            return String.format(returnVal + String.format("\u2002%d\u2002\u2002", row));
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
