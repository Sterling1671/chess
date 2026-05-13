package chess;

import java.util.Objects;

public class EnPassant {
    public static void setEnPassantTile(ChessMove move, ChessBoard board, ChessGame game){
        ChessPiece pieceToMove = board.getPiece(move.getStartPosition());

        if(pieceToMove.getPieceType() == ChessPiece.PieceType.PAWN){
            int startRow = game.currentTeamTurn == ChessGame.TeamColor.WHITE ? 2 : 7;
            int endRow = game.currentTeamTurn == ChessGame.TeamColor.WHITE ? 4 : 5;
            int enPassantRow = game.currentTeamTurn == ChessGame.TeamColor.WHITE ? 3 : 6;
            if(move.getStartPosition().getRow() == startRow && move.getEndPosition().getRow() == endRow){
                board.enPassantTile = new ChessPosition(enPassantRow, move.getStartPosition().getColumn());
            }
            else{
                board.enPassantTile = null;
            }
        }
        else{
            board.enPassantTile = null;
        }
    }

    public static void setPawnPositions(ChessMove move, ChessBoard board, ChessGame game){
        int rowDir = game.currentTeamTurn == ChessGame.TeamColor.WHITE ? -1 : 1;
        ChessPosition pieceToRemove = move.getEndPosition().add(new ChessPosition(rowDir, 0));
        board.addPiece(pieceToRemove, null);
    }

    /**
     * @param move If move is an enPassant move, set its flag
     */
    public static void checkIfEnPassant(ChessMove move, ChessBoard board){
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if(piece.getPieceType() == ChessPiece.PieceType.PAWN){
            if(Objects.equals(move.getEndPosition(), board.enPassantTile)){
                move.setEnPassantMove(true);
            }
        }
    }
}
