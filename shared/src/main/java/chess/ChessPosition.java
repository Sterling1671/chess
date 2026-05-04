package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public ChessPosition(ChessPosition other){
        this.row = other.getRow();
        this.col = other.getColumn();
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left column
     */
    public int getColumn() {
        return col;
    }

    /**
     * @return a new ChessPosition that has rows and columns added with the current one
     */
    public ChessPosition add(ChessPosition other){
        return new ChessPosition(row + other.getRow(), col + other.getColumn());
    }

    @Override
    public String toString(){
        return String.format("[%d,%d]", row, col);
    }
}
