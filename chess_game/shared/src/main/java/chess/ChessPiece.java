package chess;

import java.util.ArrayList;
import java.util.Collection;

/* not sure where to put this
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private  final ChessGame.TeamColor pieceColor;
    private  final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
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

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // implement bishop moves (diagonal)
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] directions = {
                { -1, 1 },{1,-1},{1,1},{-1,-1}
        };

        for (int[] direction : directions) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();

            while (true) {
                row += direction[0];
                col += direction[1];
                if (row < 0 || row > 8 || col < 0 || col > 8) {
                    break;
                }

                ChessPosition newPosition = new ChessPosition(row, col);
                ChessPiece pieceAtTarget = board.getPiece(newPosition);

                if (pieceAtTarget == null) {
                    moves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));

                } else {
                    if (!pieceAtTarget.getTeamColor().equals(pieceColor)) {
                        moves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                    }
                    break;
                }
            }
        }
        return moves;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
