package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface PieceMove {
    Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition);

    default Collection<ChessMove> calculateStraightLine(ChessBoard board, ChessPosition myPosition, ChessPosition line){
        List<ChessMove> validMoves = new ArrayList<>();
        ChessPosition positionToCheck = myPosition.add(line);
        ChessPiece originalPiece = board.getPiece(myPosition);
        while(positionToCheck.isInBounds()){
            ChessPiece piece = board.getPiece(positionToCheck);
            if(piece == null){
                validMoves.add(new ChessMove(myPosition, positionToCheck, null));
            }
            else{
                if(piece.getTeamColor() != originalPiece.getTeamColor()){
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                }
                break;
            }
            positionToCheck = positionToCheck.add(line);
        }



        return validMoves;
    }

    default Collection<ChessMove> calculateSingleDirection(ChessBoard board, ChessPosition myPosition, ChessPosition line) {
        List<ChessMove> validMoves = new ArrayList<>();
        ChessPosition positionToCheck = myPosition.add(line);
        ChessPiece originalPiece = board.getPiece(myPosition);
        if(positionToCheck.isInBounds()){
            ChessPiece piece = board.getPiece(positionToCheck);
            if (piece == null) {
                validMoves.add(new ChessMove(myPosition, positionToCheck, null));
            } else if(piece.getTeamColor() != originalPiece.getTeamColor()) {
                validMoves.add(new ChessMove(myPosition, positionToCheck, null));
            }
        }
        return validMoves;
    }
}
