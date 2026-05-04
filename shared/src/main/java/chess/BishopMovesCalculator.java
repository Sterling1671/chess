package chess;

import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // These are the valid directions the bishop can move
        ChessPosition[] validDirections = {
                new ChessPosition(1, 1),
                new ChessPosition(-1, -1),
                new ChessPosition(1, -1),
                new ChessPosition(-1, 1)
        };
        return checkLineDirection(board, myPosition, validDirections);
    }
}
