package chess;

import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPosition[] validDirections = {
            new ChessPosition(1,0),
            new ChessPosition(-1,0),
            new ChessPosition(0,1),
            new ChessPosition(0,-1)
        };

        return checkLineDirection(board, myPosition, validDirections);
    }
}
