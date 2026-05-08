package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMove implements PieceMove{
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
            validMoves.addAll(calculateStraightLine(board,myPosition,direction));
        }
        return validMoves;
    }
}
