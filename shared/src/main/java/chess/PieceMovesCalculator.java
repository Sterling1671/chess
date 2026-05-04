package chess;

import java.util.Collection;

public interface PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    /**
     * @return Returns true if the position is in bounds
     */
    default public boolean checkInBounds(ChessPosition myPosition){
        return myPosition.getRow() <= 8 && myPosition.getRow() >= 1 && myPosition.getColumn() <= 8 && myPosition.getColumn() >= 1;
    }
}
