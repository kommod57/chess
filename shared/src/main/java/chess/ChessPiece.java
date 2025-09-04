package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
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
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    //For Promotion
    public static void addPromotionMoves(ArrayList<ChessMove> moves, ChessPosition from, ChessPosition to) {
        for (PieceType type : Arrays.asList(
                PieceType.QUEEN,
                PieceType.BISHOP,
                PieceType.KNIGHT,
                PieceType.ROOK)) {
            moves.add(new ChessMove(from, to, type));
        }
    }

    // Castling
    private boolean canCastle(ChessBoard board, int row, int... cols) {
        for (int i=1; i < cols.length - 1; i++) {
            ChessPiece piece = board.getPiece(new ChessPosition(row, cols[i]));
            if (piece != null) return false; //Pathway must be empty
        }
        ChessPiece rook = board.getPiece(new ChessPosition(row, cols[cols.length - 1]));
        return rook != null &&
                rook.getPieceType() == PieceType.ROOK &&
                rook.getTeamColor() == this.getTeamColor();
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }
}
