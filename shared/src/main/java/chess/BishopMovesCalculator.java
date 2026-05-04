package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator implements PieceMovesCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // This stores the finished list
        List<ChessMove> validMoves = new ArrayList<>();

        // These are the valid directions the bishop can move
        ChessPosition[] validDirections = {
                new ChessPosition(1,1),
                new ChessPosition(-1,-1),
                new ChessPosition(1,-1),
                new ChessPosition(-1,1)
        };

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
