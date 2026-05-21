package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class PawnMove implements PieceMove{
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();
        ChessPiece originalPiece = board.getPiece(myPosition);
        ChessGame.TeamColor myColor = originalPiece.getTeamColor();
        // For white team
        if(myColor == ChessGame.TeamColor.WHITE){
            ChessPosition direction = new ChessPosition(1,0);
            int startRow = 2;
            checkForward(board, myPosition, direction, validMoves, myColor, startRow);
            // Now check sides
            List<ChessPosition> validDirections = List.of(
                    new ChessPosition(1,-1),
                    new ChessPosition(1,1)
            );
            checkDiagonals(board, myPosition, validMoves, myColor, validDirections);
        }
        else{
            ChessPosition direction = new ChessPosition(-1,0);
            int startRow = 7;
            checkForward(board, myPosition, direction, validMoves, myColor, startRow);
            // Now check sides
            List<ChessPosition> validDirections = List.of(
                    new ChessPosition(-1,-1),
                    new ChessPosition(-1,1)
            );
            checkDiagonals(board, myPosition, validMoves, myColor, validDirections);
        }
        return validMoves;
    }

    private void checkForward(ChessBoard board,
                              ChessPosition myPosition,
                              ChessPosition direction,
                              List<ChessMove> validMoves,
                              ChessGame.TeamColor myColor,
                              int startRow) {
        ChessPosition positionToCheck = myPosition.add(direction);
        if(positionToCheck.isInBounds()){
            // If space in front of white pawn is empty
            if(board.getPiece(positionToCheck) == null){
                validMoves.addAll(checkPromotion(myPosition,positionToCheck, myColor));
                // If second space in front of white pawn is empty bc its in second row
                if(myPosition.getRow() == startRow){
                    positionToCheck = positionToCheck.add(direction);
                    if(board.getPiece(positionToCheck) == null) {
                        validMoves.addAll(checkPromotion(myPosition, positionToCheck, myColor));
                    }
                }
            }
        }
    }

    private void checkDiagonals(ChessBoard board,
                                ChessPosition myPosition,
                                List<ChessMove> validMoves,
                                ChessGame.TeamColor myColor,
                                List<ChessPosition> validDirections) {
        ChessPosition positionToCheck;
        for(ChessPosition direction : validDirections){
            positionToCheck = myPosition.add(direction);
            if(positionToCheck.isInBounds()){
                ChessPiece piece = board.getPiece(positionToCheck);
                if(piece != null) {
                    if (piece.getTeamColor() != myColor) {
                        validMoves.addAll(checkPromotion(myPosition, positionToCheck, myColor));
                    }
                }
                else if(Objects.equals(positionToCheck,board.enPassantTile)){
                    ChessMove enPassantMove = new ChessMove(myPosition, positionToCheck, null);
                    enPassantMove.setEnPassantMove(true);
                    validMoves.add(enPassantMove);
                }
            }
        }
    }

    public Collection<ChessMove> checkPromotion(ChessPosition myStartPosition,
                                                ChessPosition myEndPosition,
                                                ChessGame.TeamColor myColor){
        List<ChessMove> validMoves = new ArrayList<>();
        if(myColor == ChessGame.TeamColor.WHITE && myEndPosition.getRow() == 8){
            addPromotionMoves(myStartPosition, myEndPosition, validMoves);
        }
        else if(myColor == ChessGame.TeamColor.BLACK && myEndPosition.getRow() == 1){
            addPromotionMoves(myStartPosition, myEndPosition, validMoves);
        }
        else{
            validMoves.add(new ChessMove(myStartPosition, myEndPosition, null));
        }
        return validMoves;
    }

    private static void addPromotionMoves(ChessPosition myStartPosition,
                                          ChessPosition myEndPosition,
                                          List<ChessMove> validMoves) {
        validMoves.addAll(List.of(
           new ChessMove(myStartPosition, myEndPosition, ChessPiece.PieceType.KNIGHT),
           new ChessMove(myStartPosition, myEndPosition, ChessPiece.PieceType.QUEEN),
           new ChessMove(myStartPosition, myEndPosition, ChessPiece.PieceType.BISHOP),
           new ChessMove(myStartPosition, myEndPosition, ChessPiece.PieceType.ROOK)
        ));
    }

}
