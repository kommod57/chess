package chess;

import java.util.ArrayList;
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

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] dirs = new int[0][0];
        boolean inf = false;
        boolean special_p = false;
        if (this.type.equals(PieceType.BISHOP)) {
            dirs = new int[][] {{-1, 1}, {1, 1}, {1, -1}, {-1, -1}
            };
            inf = true;
        }
        else if (this.type.equals(PieceType.KING)) {
            dirs = new int[][] {
                    {-1, 1}, {1, 1}, {1, -1}, {-1, -1},
                    {0,1},{1,0},{-1,0},{0,-1}
            };
        }
        else if (this.type.equals(PieceType.KNIGHT)) {
            dirs = new int[][] {
                    {-1, 2}, {-1, -2}, {-2, 1}, {-2, -1},
                    {1,2},{1,-2},{2,1},{2,-1}
            };
        }
        else if (this.type.equals(PieceType.QUEEN)) {
            dirs = new int[][] {
                    {-1, 1}, {1, 1}, {1, -1}, {-1, -1},
                    {0,1},{1,0},{-1,0},{0,-1}
            };
            inf = true;
        }
        else if (this.type.equals(PieceType.ROOK)) {
            dirs = new int[][] {
                    {0,1},{1,0},{-1,0},{0,-1}
            };
            inf = true;
        }
        else if (this.type.equals(PieceType.PAWN)) {
            special_p = true;
            dirs = new int[][] {
                    {1,0},{2,0},{1,1},{1,-1}
            };
        }
        for (int[] direction : dirs) {
            int old_row = myPosition.getRow();
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
                while (true) {
                    row += direction[0];
                    col += direction[1];
                    if (row < 1 || row > 8 || col < 1 || col > 8) {
                        break;
                    }

                    ChessPosition newPosition = new ChessPosition(row, col);
                    ChessPiece pieceAtTarget = board.getPiece(newPosition);

                    if (pieceAtTarget == null) {
                        if (special_p) {
                            if (direction[1]!=0||(direction[0]==2&&old_row!=7&&old_row!=2)) {
                                break;
                            }
                        };
                        moves.add(new ChessMove(myPosition, newPosition, null));

                    } else {
                        if (!pieceAtTarget.getTeamColor().equals(pieceColor)) {
                            if (special_p) {
                                if (direction[1]==0) {
                                    break;
                                }
                            };
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        }
                        break;
                    }
                    if (!inf) {
                        break;
                    }
                }
            }
        return moves;
    }
}
