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
        System.out.println(this.type);
        Collection<ChessMove> moves = new ArrayList<>();
        // implement bishop moves (diagonal)
        if (this.type == PieceType.BISHOP) {
            int[][] directions = {
                    { -1, 1 },{1,-1},{1,1},{-1,-1}
            };

            for (int[] direction : directions) {
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
                        moves.add(new ChessMove(myPosition, newPosition, null));

                    } else {
                        if (!pieceAtTarget.getTeamColor().equals(pieceColor)) {
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        }
                        break;
                    }
                }
            }
        }
        // King moves
        if (this.type == PieceType.KING) {
            System.out.println("inside");
            int[][] directions = {
                    { -1, 1 },{1,-1},{1,1},{-1,-1},
                    {0, 1},{1,0},{-1,0},{0,-1}
            };

            for (int[] direction : directions) {
                int row = myPosition.getRow();
                int col = myPosition.getColumn();

                row += direction[0];
                col += direction[1];

                if (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
                    ChessPosition newPosition = new ChessPosition(row, col);
                    ChessPiece pieceAtTarget = board.getPiece(newPosition);

                    if (pieceAtTarget == null) {
                        moves.add(new ChessMove(myPosition, newPosition, null));

                    } else {
                        if (!pieceAtTarget.getTeamColor().equals(pieceColor)) {
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        }
                    }
                }
            }
        }
        // Knight moves
        if (this.type == PieceType.KNIGHT) {
            int[][] directions = {
                    {-2, 1},{-1,2},{2,1},{1,2},
                    {-2, -1},{-1,-2},{2,-1},{1,-2}
            };

            for (int[] direction : directions) {
                int row = myPosition.getRow();
                int col = myPosition.getColumn();

                row += direction[0];
                col += direction[1];

                if (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
                    ChessPosition newPosition = new ChessPosition(row, col);
                    ChessPiece pieceAtTarget = board.getPiece(newPosition);

                    if (pieceAtTarget == null) {
                        moves.add(new ChessMove(myPosition, newPosition, null));

                    } else {
                        if (!pieceAtTarget.getTeamColor().equals(pieceColor)) {
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        }
                    }
                }
            }
        }
        // Pawn moves
        if (this.type == PieceType.PAWN) {
            int rowDir = pieceColor == ChessGame.TeamColor.WHITE ? 1 : -1;
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            int[][] directions = {
                    {1, 0},
                    {2,0}, //only at first move
                    {1,1},{1,-1} // captures
            };

            for (int[] direction : directions) {
                //ChessPosition newPosition = new ChessPosition(row, col);
                //ChessPiece pieceAtTarget = board.getPiece(newPosition);

                int newRow = row;
                int newCol = col;

                newRow += direction[0] * rowDir;
                newCol += direction[1];
                if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    ChessPiece pieceAtTarget = board.getPiece(newPosition);
                    if (direction[1] == 0) {
                        if (pieceAtTarget == null) {
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        }
                    } else {
                        if (pieceAtTarget == null) {
                            if (direction[0] == 0) {
                                moves.add(new ChessMove(myPosition, newPosition, null));
                            }

                        } else {
                            if (!pieceAtTarget.getTeamColor().equals(pieceColor)) {
                                moves.add(new ChessMove(myPosition, newPosition, null));
                            }
                        }
                    }
                }
                System.out.println(direction[0] + "," + direction[1] + row);
                if (direction[1] != 2 && direction[1] != -2 && row != 2 && row != 7) {
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
