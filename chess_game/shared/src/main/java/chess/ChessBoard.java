package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] squares = new ChessPiece[8][8];


    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        // validatePosition(position);
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        // force index back
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                squares[row][column] = null;
            }
        }
        // pawns
        for (int col = 1; col < 9; col++) {
            addPiece(new ChessPosition(2, col),
                    new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7, col),
                    new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));

        }

        // other pieces
        ChessPiece.PieceType[] back_rows = {
                ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK,
        };
        for (int col = 0; col < 8; col++) {
            addPiece(new ChessPosition(1, col + 1), new ChessPiece(ChessGame.TeamColor.WHITE, back_rows[col]));
            addPiece(new ChessPosition(8, col + 1), new ChessPiece(ChessGame.TeamColor.BLACK, back_rows[col]));
        }
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int row = 1; row < 9; row++) {
            for (int column = 1; column < 9; column++) {
                ChessPiece piece = squares[row-1][column-1];
                result = 31 * result + (piece == null ? 0 : piece.hashCode());

            }
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ChessBoard other = (ChessBoard) obj;

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece thisPiece = this.getPiece(position);
                ChessPiece otherPiece = other.getPiece(position);

                if (thisPiece == null) {
                    if (otherPiece != null) return false;
                } else if (!thisPiece.equals(otherPiece)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int row = 8; row >= 1; row--) { // Start from top (rank 8)
            for (int col = 1; col <= 8; col++) {
                ChessPiece piece = getPiece(new ChessPosition(row, col));
                sb.append(piece == null ? "." : piece.toString()).append(" ");
            }
            sb.append("\n"); // Newline after each row
        }

        return sb.toString();
    }

}
