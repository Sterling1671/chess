package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Castling {

    // ****************************************************************
    // * MOVE TRACKER CLASS(needs Javadoc)
    // ****************************************************************
    public static class MoveTracker {
        private boolean kingMoved = false;
        private boolean queensideRookMoved = false;
        private boolean kingsideRookMoved = false;
        private final int homeRow;
        private final ChessGame.TeamColor color;

        public MoveTracker(int homeRow){
            this.homeRow = homeRow;
            this.color = homeRow == 1 ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        }

        public void markKingMoved() { this.kingMoved = true; }
        public void markQueensideRookMoved() { this.queensideRookMoved = true; }
        public void markKingsideRookMoved() { this.kingsideRookMoved = true; }

        public boolean canCastleKingside() {
            return !kingMoved && !kingsideRookMoved;
        }

        public boolean canCastleQueenside() {
            return !kingMoved && !queensideRookMoved;
        }

        public boolean canNotCastleGeneral(ChessMove move){
            if(move.getEndPosition().getColumn() == 7){
                return !canCastleKingside();
            }
            else{
                return !canCastleQueenside();
            }
        }
        public void setMoves(ChessMove move, ChessBoard board){
            if(Objects.equals(move.getStartPosition(), new ChessPosition(this.homeRow, 5))){markKingMoved();}
            if(Objects.equals(move.getStartPosition(), new ChessPosition(this.homeRow, 8))){markKingsideRookMoved();}
            if(Objects.equals(move.getStartPosition(), new ChessPosition(this.homeRow, 1))){markQueensideRookMoved();}
            // This menace should check the piece at queenside rook space and make sure it's actually the rook
            if(!Objects.equals(board.getPiece(new ChessPosition(this.homeRow,1)),new ChessPiece(color, ChessPiece.PieceType.ROOK))){
                markQueensideRookMoved();
            }
            if(!Objects.equals(board.getPiece(new ChessPosition(this.homeRow,8)),new ChessPiece(color, ChessPiece.PieceType.ROOK))){
                markKingsideRookMoved();
            }
        }
    }

    public static final Map<ChessPosition, ChessPosition> ROOK_MAP = Map.of(
            new ChessPosition(1, 3), new ChessPosition(1, 1),
            new ChessPosition(1, 7), new ChessPosition(1, 8),
            new ChessPosition(8, 3), new ChessPosition(8, 1),
            new ChessPosition(8, 7), new ChessPosition(8, 8),
            new ChessPosition(8, 8), new ChessPosition(8, 6),
            new ChessPosition(8, 1), new ChessPosition(8, 4),
            new ChessPosition(1, 1), new ChessPosition(1, 4),
            new ChessPosition(1, 8), new ChessPosition(1, 6)
    );
    /**
     * @param move If move is a castle move, sets it's castle flag. NO CHECKS
     */
    public static void checkIfCastle(ChessMove move, ChessBoard board){
        ChessPiece piece = board.getPiece(move.getStartPosition());
        // Make sure it exists again
        if(piece == null){return;}

        // Gets the color
        ChessGame.TeamColor color = piece.getTeamColor();
        // Makes sure it's a king
        if(piece.getPieceType() == ChessPiece.PieceType.KING){
            // Get the color and check the moves
            int row = color == ChessGame.TeamColor.WHITE ? 1 : 8;
            List<ChessMove> castleMoves = new ArrayList<>(List.of(
                    new ChessMove(new ChessPosition(row,5), new ChessPosition(row,3),null),
                    new ChessMove(new ChessPosition(row,5), new ChessPosition(row,7),null)
            ));
            for(ChessMove castleMove : castleMoves){
                if(move.equals(castleMove)){
                    move.setCastleMove(true);
                }
            }
        }
    }
    /**
     * Checks if a castle move is allowed. Assumes Prior checks
     * have confirmed this is a king, and it is in the correct position.
     *
     * @param move the castle move to be checked
     * @return True if castling is possible
     */
    public static boolean checkCastleValidity(ChessMove move, ChessBoard board, MoveTracker tracker, ChessGame game){
        // Makes sure I'm not just working with a normal move
        if(!move.getCastleMove()){return false;}

        // First check if the king and rook have moved
        if(tracker.canNotCastleGeneral(move)){return false;}

        // Second checks to make sure the pieces are empty
        int colDiff = move.getEndPosition().getColumn() - move.getStartPosition().getColumn();
        int step = (colDiff > 0) ? 1 : -1;
        ChessPosition current = move.getStartPosition().add(new ChessPosition(0, step));

        // This monstrosity checks current until the square on the same row as it at 1 or 8
        while (!current.equals(new ChessPosition(current.getRow(),step == 1 ? 8:1))) {
            if (board.getPiece(current) != null) {
                return false;
            }
            current = current.add(new ChessPosition(0, step));
        }

        // Third check the moves against check
        List<ChessMove> movesToBeChecked = new ArrayList<>();


        // This section takes a counter equal to start position then increments it
        // until it reaches the required end position.
        ChessPosition counter = new ChessPosition(move.getStartPosition());
        while(!counter.equals(move.getEndPosition())){
            counter = counter.add(new ChessPosition(0,step));
            ChessPosition end = new ChessPosition(counter);
            movesToBeChecked.add(new ChessMove(move.getStartPosition(),end,null));
        }
        for(ChessMove move1 : movesToBeChecked){
            boolean check = game.testMoveValidity(move1);
            if(!check){return false;}
        }

        // Forth makes sure the piece isn't in check already
        return !game.isInCheck(board.getPiece(move.getStartPosition()).getTeamColor());
    }

    public static void setRookPosition(ChessMove move, ChessBoard board){
        ChessPosition rookPosition = Castling.ROOK_MAP.get(move.getEndPosition());
        ChessPosition newRookPosition = Castling.ROOK_MAP.get(rookPosition);
        ChessPiece rookToPlace = board.getPiece(rookPosition);
        board.addPiece(newRookPosition, rookToPlace);
        board.addPiece(rookPosition, null);
    }
}
