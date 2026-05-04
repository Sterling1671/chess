package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface PieceMovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    /**
     * @return true if the position is in bounds
     */
    default boolean checkInBounds(ChessPosition myPosition){
        return myPosition.getRow() <= 8 && myPosition.getRow() >= 1 && myPosition.getColumn() <= 8 && myPosition.getColumn() >= 1;
    }

    /**
     * @return A list of valid moves continuing in each direction provided until edge or another piece is reached
     */
    default Collection<ChessMove> checkLineDirection(ChessBoard board, ChessPosition myPosition, ChessPosition[] validDirections){
        List<ChessMove> validMoves = new ArrayList<>();

        // This is the piece that is being passed in
        ChessPiece originalPiece = board.getPiece(myPosition);

        // Loop that goes through directions... Fancy
        for(ChessPosition direction : validDirections){
            // I made a copy constructor for ChessPosition bc Java uses copy by reference
            ChessPosition tileToCheck = myPosition.add(direction);
            while(checkInBounds(tileToCheck)){
                ChessPiece piece = board.getPiece(tileToCheck);
                if(piece == null){
                    // Adds the move to the list if the square is empty
                    validMoves.add(new ChessMove(myPosition, tileToCheck, null));
                }
                else if(piece.getTeamColor() != originalPiece.getTeamColor()){
                    validMoves.add(new ChessMove(myPosition, tileToCheck, null));
                    break;
                }
                else{
                    break;
                }
                tileToCheck = tileToCheck.add(direction);
            }
        }
        return validMoves;
    }
}
