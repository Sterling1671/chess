package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMove implements PieceMove{
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();
        List<ChessPosition> validDirections = List.of(
                new ChessPosition(1,1),
                new ChessPosition(1,-1),
                new ChessPosition(-1,1),
                new ChessPosition(-1,-1),
                new ChessPosition(1,0),
                new ChessPosition(-1,0),
                new ChessPosition(0,1),
                new ChessPosition(0,-1)
        );

        for(ChessPosition direction : validDirections){
            validMoves.addAll(calculateSingleDirection(board,myPosition,direction));
        }
        // Castle check
        ChessPiece piece = board.getPiece(myPosition);
        ChessGame.TeamColor color = piece.getTeamColor();
        int row = color == ChessGame.TeamColor.WHITE ? 1 : 8;
        // Checks it's still on it's starting tile
        if(myPosition.equals(new ChessPosition(row, 5))) {
            List<ChessMove> castleMoves = new ArrayList<>(List.of(
                    new ChessMove(new ChessPosition(row, 5), new ChessPosition(row, 3), null),
                    new ChessMove(new ChessPosition(row, 5), new ChessPosition(row, 7), null)
            ));
            for (ChessMove castleMove : castleMoves) {
                castleMove.setCastleMove(true);
            }
            validMoves.addAll(castleMoves);
        }
        return validMoves;
    }
}
