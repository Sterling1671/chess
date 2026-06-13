package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int column;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return row == that.row && column == that.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    public ChessPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public ChessPosition(ChessPosition other){
        this.row = other.getRow();
        this.column = other.getColumn();
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
        return column;
    }

    /**
     * @return a new ChessPosition that has rows and columns added with the current one
     */
    public ChessPosition add(ChessPosition other){
        return new ChessPosition(row + other.getRow(), column + other.getColumn());
    }

    public boolean isInBounds(){
        return (1 <= this.getRow() && this.getRow() <= 8) && (1 <= this.getColumn() && this.getColumn() <= 8);
    }

    @Override
    public String toString(){
        return String.format("%c%d", 'a' + column - 1, row);
    }

    public static ChessPosition fromString(String positionStr) throws IllegalArgumentException{
        if (positionStr == null || positionStr.length() != 2) {
            throw new IllegalArgumentException("Invalid chess position format. Expected exactly 2 characters (e.g., 'a1')");
        }

        // Extract the characters
        char colChar = positionStr.charAt(0); // 'a' through 'h'
        char rowChar = positionStr.charAt(1); // '1' through '8'

        // Convert char to 1-indexed integers using char arithmetic
        int col = colChar - 'a' + 1;
        int row = rowChar - '1' + 1;

        if (col < 1 || col > 8 || row < 1 || row > 8) {
            throw new IllegalArgumentException("Position out of chess board bounds");
        }

        return new ChessPosition(row, col);
    }
}
