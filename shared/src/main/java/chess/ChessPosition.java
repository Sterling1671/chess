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
}
