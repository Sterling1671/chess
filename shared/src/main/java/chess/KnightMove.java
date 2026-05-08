package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMove implements PieceMove{
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();
        List<ChessPosition> validDirections = List.of(
                new ChessPosition(2,1),
                new ChessPosition(2,-1),
                new ChessPosition(-2,1),
                new ChessPosition(-2,-1),
                new ChessPosition(1,2),
                new ChessPosition(-1,2),
                new ChessPosition(1,-2),
                new ChessPosition(-1,-2)
        );

        for(ChessPosition direction : validDirections){
            validMoves.addAll(calculateSingleDirection(board,myPosition,direction));
        }
        return validMoves;
    }
}
