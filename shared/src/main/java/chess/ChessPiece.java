package chess;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    public ChessPiece(ChessPiece other){
        this.pieceColor = other.pieceColor;
        this.type = other.type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece piece = (ChessPiece) o;
        return pieceColor == piece.pieceColor && type == piece.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        PieceMovesCalculator calculator;
        // maybe switch is better
        if(piece.getPieceType() == PieceType.BISHOP) {
            calculator = new BishopMovesCalculator();
        }
        else if(piece.getPieceType() == PieceType.QUEEN){
            calculator = new QueenMovesCalculator();
        }
        else if(piece.getPieceType() == PieceType.KNIGHT){
            calculator = new KnightMovesCalculator();
        }
        else if(piece.getPieceType() == PieceType.PAWN){
            calculator = new PawnMovesCalculator();
        }
        else if(piece.getPieceType() == PieceType.ROOK){
            calculator = new RookMovesCalculator();
        }
        else if(piece.getPieceType() == PieceType.KING){
            calculator = new KingMovesCalculator();
        }
        else{
            return List.of();
        }
        return calculator.pieceMoves(board, myPosition);
    }
}
