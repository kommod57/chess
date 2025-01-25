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
    private final int col;

    public ChessPosition(int row, int col) {
        if (row < 1 || col < 1 || row > 8 || col > 8) {
            throw new IllegalArgumentException("Invalid row or column" + row + "," + col);
        }
        this.row = row;
        this.col = col;
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
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    @Override
    public int hashCode() {
        return 31 * row + col;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ChessPosition)) return false;
        ChessPosition other = (ChessPosition) obj;
        return this.row == other.row && this.col == other.col;
    }

    @Override
    public String toString() {
        return "Pos [row=" + row + ", col=" + col + "]";
    }
}
