package chess;

import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // These are the valid directions the queen can move
        ChessPosition[] validDirections = {
                new ChessPosition(1,1),
                new ChessPosition(-1,-1),
                new ChessPosition(1,-1),
                new ChessPosition(-1,1),
                new ChessPosition(1,0),
                new ChessPosition(-1,0),
                new ChessPosition(0,1),
                new ChessPosition(0,-1)
        };
        return checkLineDirection(board, myPosition, validDirections);
    }
}
