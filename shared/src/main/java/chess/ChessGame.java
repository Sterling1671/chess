package chess;

import java.util.*;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard board = new ChessBoard();
    TeamColor currentTeamTurn = TeamColor.WHITE;
    public ChessGame() {
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeamTurn = team;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && currentTeamTurn == chessGame.currentTeamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, currentTeamTurn);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);

        // Makes sure there's a piece here
        if(piece == null) return null;

        // unchecked moves is filled with all the possible moves from pieceMoves
        List<ChessMove> uncheckedMoves = new ArrayList<>(piece.pieceMoves(board, startPosition));
        List<ChessMove> validMoves = new ArrayList<>();
        // Each move is checked for validity and only then moved to validMoves
        for(ChessMove move : uncheckedMoves){
            if(this.TestMoveValidity(move)){
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param move the move to check
     * @return true if the move is valid, false if not
     */
    public boolean TestMoveValidity(ChessMove move){
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = board.getPiece(start);
        ChessPiece takenPiece = board.getPiece(end);

        // Make the move
        board.addPiece(end, piece);
        board.addPiece(start, null);

        // Check the move
        boolean invalid = this.isInCheck(piece.getTeamColor());

        // Puts the board back
        board.addPiece(start, piece);
        board.addPiece(end, takenPiece);

        return !invalid;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece pieceToMove = board.getPiece(start);

        // Checks if piece exists
        if(pieceToMove == null) throw new InvalidMoveException("Invalid Move");
        // Gets all valid moves that start position could make
        List<ChessMove> validMoves = new ArrayList<>(this.validMoves(start));
        // Checks if piece is the right turn
        if(pieceToMove.getTeamColor() != currentTeamTurn) throw new InvalidMoveException("Invalid Move");
        // Makes sure the move given is in the validMoves array
        if(!validMoves.contains(move)) throw new InvalidMoveException("Invalid Move");
        // If none of these throw anything then make the move
        // Also do pawn promotion thingy
        ChessPiece.PieceType pieceToPlaceType = move.getPromotionPiece();
        // This gets the new piece. If it's a promotion do that if not just make a copy of old piece
        ChessPiece pieceToPlace = pieceToPlaceType == null ?
                new ChessPiece(pieceToMove) :
                new ChessPiece(pieceToMove.getTeamColor(),pieceToPlaceType);
        // Actually add the piece and change turn
        board.addPiece(end, pieceToPlace);
        board.addPiece(start, null);
        this.changeTurn();

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor otherColor;
        if(teamColor == TeamColor.WHITE)
            otherColor = TeamColor.BLACK;
        else{
            otherColor = TeamColor.WHITE;
        }
        // Gets all the moves of the opposing team
        Set<ChessMove> otherTeamMoves = new HashSet<>(board.getTeamMoves(otherColor));
        Set<ChessPosition> finalPositions = new HashSet<>();
        // Gets the final position
        for(ChessMove move : otherTeamMoves){
            finalPositions.add(move.getEndPosition());
        }
        // If any of the final positions are the kings position, it's in check
        return finalPositions.contains(board.getKingPosition(teamColor));
    }

    public void changeTurn(){
        this.currentTeamTurn = this.currentTeamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // I'm thinking I check every move the team can make and as long
        // as even one is valid this returns false

        // Make sure the king is actually in check
        if(!this.isInCheck(teamColor)) return false;

        // This is all the moves the team can make
        List<ChessMove> teamMoves = new ArrayList<>(board.getTeamMoves(teamColor));
        List<ChessMove> validMoves = new ArrayList<>();

        for(ChessMove move : teamMoves){
            if(this.TestMoveValidity(move)){
                validMoves.add(move);
            }
        }
        return validMoves.isEmpty();
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // Make sure the king is actually in check
        if(this.isInCheck(teamColor)) return false;

        // This is all the moves the team can make
        List<ChessMove> teamMoves = new ArrayList<>(board.getTeamMoves(teamColor));
        List<ChessMove> validMoves = new ArrayList<>();

        for(ChessMove move : teamMoves){
            if(this.TestMoveValidity(move)){
                validMoves.add(move);
            }
        }
        return validMoves.isEmpty();
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
