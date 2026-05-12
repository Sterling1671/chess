package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;
    private boolean enPassantMove;
    private boolean castleMove;
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(startPosition, chessMove.startPosition) && Objects.equals(endPosition, chessMove.endPosition) && promotionPiece == chessMove.promotionPiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
        this.castleMove = false;
        this.enPassantMove = false;
    }

    public ChessMove(ChessMove copy,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = copy.startPosition;
        this.endPosition = copy.endPosition;
        this.promotionPiece = promotionPiece;
        this.castleMove = copy.castleMove;
        this.enPassantMove = copy.enPassantMove;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * @return True if move is a castle
     */
    public boolean getCastleMove() {
        return castleMove;
    }

    /**
     * @return True if move is enPassant
     */
    public boolean getEnPassantMove() {
        return enPassantMove;
    }

    /**
     * @param castleMove True if this move is a castle
     */
    public void setCastleMove(boolean castleMove){this.castleMove = castleMove;}

    /**
     * @param enPassantMove True if this move is enPassant
     */
    public void setEnPassantMove(boolean enPassantMove){this.enPassantMove = enPassantMove;}

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    @Override
    public String toString(){
        return String.format("%s%s", startPosition, endPosition);
    }
}
