package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();
        List<ChessMove> newMoves = new ArrayList<>();
        ChessPiece originalPiece = board.getPiece(myPosition);

        // First check what team the pawn is on to determine movement
        if(originalPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            ChessPosition tileToCheck = myPosition.add(new ChessPosition(1,0));
            // If the tile we're checking is in bounds, get the piece there
            if (checkInBounds(tileToCheck)) {
                ChessPiece piece = board.getPiece(tileToCheck);
                // If there is no piece, go forward
                if (piece == null) {
                    // Adds the move to the list if the square is empty
                    validMoves.add(new ChessMove(myPosition, tileToCheck, null));
                    // Hardcoded checker to see if 2 movement is possible, only works on row 2 for white
                    if(myPosition.getRow() == 2){
                        // advance tileToCheck by 1, checking 2 spaces
                        tileToCheck = tileToCheck.add(new ChessPosition(1,0));
                        piece = board.getPiece((tileToCheck));
                        if (piece == null) {
                            // Adds the move to the list if the square is empty
                            validMoves.add(new ChessMove(myPosition, tileToCheck, null));
                        }
                    }
                }
            }
            // Now we check if it can take
            tileToCheck = myPosition.add(new ChessPosition(1,1));
            if (checkInBounds(tileToCheck)) {
                ChessPiece piece = board.getPiece(tileToCheck);
                // If what's there is an opposing piece, add option to take
                if(piece != null && piece.getTeamColor() != originalPiece.getTeamColor()){
                    validMoves.add(new ChessMove(myPosition, tileToCheck, null));
                }
            }
            tileToCheck = myPosition.add(new ChessPosition(1,-1));
            if (checkInBounds(tileToCheck)) {
                ChessPiece piece = board.getPiece(tileToCheck);
                // If what's there is an opposing piece, add option to take
                if(piece != null && piece.getTeamColor() != originalPiece.getTeamColor()){
                    validMoves.add(new ChessMove(myPosition, tileToCheck, null));
                }
            }
            // Promotion checker for white
            for(ChessMove move : validMoves){
                if(move.getEndPosition().getRow() == 8 && move.getPromotionPiece() == null){
                    newMoves.addAll(
                            List.of(
                                new ChessMove(move, ChessPiece.PieceType.QUEEN),
                                new ChessMove(move, ChessPiece.PieceType.BISHOP),
                                new ChessMove(move, ChessPiece.PieceType.ROOK),
                                new ChessMove(move, ChessPiece.PieceType.KNIGHT)
                            )
                    );
                }
                else{
                    newMoves.add(move);
                }
            }
        }else{
            ChessPosition tileToCheck = myPosition.add(new ChessPosition(-1,0));
            // If the tile we're checking is in bounds, get the piece there
            if (checkInBounds(tileToCheck)) {
                ChessPiece piece = board.getPiece(tileToCheck);
                // If there is no piece, go forward
                if (piece == null) {
                    // Adds the move to the list if the square is empty
                    validMoves.add(new ChessMove(myPosition, tileToCheck, null));
                    // Hardcoded checker to see if 2 movement is possible, only works on row 7 for black
                    if(myPosition.getRow() == 7){
                        // advance tileToCheck by 1, checking 2 spaces
                        tileToCheck = tileToCheck.add(new ChessPosition(-1,0));
                        piece = board.getPiece((tileToCheck));
                        if (piece == null) {
                            // Adds the move to the list if the square is empty
                            validMoves.add(new ChessMove(myPosition, tileToCheck, null));
                        }
                    }
                }
            }
            // Now we check if it can take
            tileToCheck = myPosition.add(new ChessPosition(-1,1));
            if (checkInBounds(tileToCheck)) {
                ChessPiece piece = board.getPiece(tileToCheck);
                // If what's there is an opposing piece, add option to take
                if(piece != null && piece.getTeamColor() != originalPiece.getTeamColor()){
                    validMoves.add(new ChessMove(myPosition, tileToCheck, null));
                }
            }
            tileToCheck = myPosition.add(new ChessPosition(-1,-1));
            if (checkInBounds(tileToCheck)) {
                ChessPiece piece = board.getPiece(tileToCheck);
                // If what's there is an opposing piece, add option to take
                if(piece != null && piece.getTeamColor() != originalPiece.getTeamColor()){
                    validMoves.add(new ChessMove(myPosition, tileToCheck, null));
                }
            }
            // Promotion checker for black
            for(ChessMove move : validMoves){
                if(move.getEndPosition().getRow() == 1 && move.getPromotionPiece() == null){
                    newMoves.addAll(
                            List.of(
                                    new ChessMove(move, ChessPiece.PieceType.QUEEN),
                                    new ChessMove(move, ChessPiece.PieceType.BISHOP),
                                    new ChessMove(move, ChessPiece.PieceType.ROOK),
                                    new ChessMove(move, ChessPiece.PieceType.KNIGHT)
                            )
                    );
                }
                else{
                    newMoves.add(move);
                }
            }
        }
        return newMoves;
    }
}
