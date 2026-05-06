package chess;

import java.lang.reflect.Array;
import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
    }
    public ChessBoard(ChessBoard other){
        for(int i = 0; i < this.squares.length; i++){
            for(int j = 0; j < this.squares[i].length; j++) {
                // Added null checker bc memory is scary
                if(other.squares[i][j] == null){
                    this.squares[i][j] = null;
                }
                else {
                    this.squares[i][j] = new ChessPiece(other.squares[i][j]);
                }

            }
       }
    }
    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Gets the position of the king on the chessboard
     * @param teamColor the team of the king you want to find
     * @return the position of the king
     */
    public ChessPosition getKingPosition(ChessGame.TeamColor teamColor) {
        for(int i = 0; i < squares.length; i++){
            for(int j = 0; j < squares[i].length; j++){
                ChessPiece piece = squares[i][j];
                if(piece != null){
                    if(piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor){
                        return new ChessPosition(i+1,j+1);
                    }
                }
            }
        }
        return null;
    }


    /**
     * Gets all valid moves a team can currently make. Doesn't check for extra rules
     * @param teamColor the team you want to get moves for
     * @return the collection of all moves
     */
    public Collection<ChessMove> getTeamMoves(ChessGame.TeamColor teamColor) {
        Set<ChessMove> teamMoves = new HashSet<>();
        for(int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                if(squares[i][j] != null){
                    if(squares[i][j].getTeamColor() == teamColor){
                        ChessPiece piece = squares[i][j];
                        ChessPosition position = new ChessPosition(i+1,j+1);
                        Collection<ChessMove> pieceMoves = piece.pieceMoves(this, position);
                        teamMoves.addAll(pieceMoves);
                    }
                }
            }
        }
        return teamMoves;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for(ChessPiece[] row : squares){
            Arrays.fill(row, null);
        }
        // Hardcode the configuration for white
        Array.set(squares[0],0,new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        Array.set(squares[0],1,new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        Array.set(squares[0],2,new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        Array.set(squares[0],3,new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        Array.set(squares[0],4,new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        Array.set(squares[0],5,new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        Array.set(squares[0],6,new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        Array.set(squares[0],7,new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

        for(int i = 0; i < squares[1].length;i++){
            Array.set(squares[1],i,new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }

        // Hardcode the configuration for black
        Array.set(squares[7],0,new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        Array.set(squares[7],1,new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        Array.set(squares[7],2,new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        Array.set(squares[7],3,new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        Array.set(squares[7],4,new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        Array.set(squares[7],5,new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        Array.set(squares[7],6,new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        Array.set(squares[7],7,new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));

        for(int i = 0;i < squares[6].length;i++) {
            Array.set(squares[6], i, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }



    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }
}
