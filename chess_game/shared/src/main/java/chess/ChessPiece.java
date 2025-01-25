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
            int[][] directions = {
                    {1, 0},{2,0},{1,-1},{1,1}
            };
            int rowDir = pieceColor == ChessGame.TeamColor.WHITE ? 1 : -1;
            for (int[] direction : directions) {
                int row = myPosition.getRow();
                int col = myPosition.getColumn();
                int og_row = row;
                row += direction[0] * rowDir;
                col += direction[1];

                if (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
                    ChessPosition newPosition = new ChessPosition(row, col);
                    ChessPiece pieceAtTarget = board.getPiece(newPosition);
                    ChessPiece pieceAtTargetBehind = board.getPiece(new ChessPosition(row - rowDir, col));
                    // for changing the pawns clothes
                    if (((pieceColor == ChessGame.TeamColor.WHITE && row == 8) ||
                            (pieceColor == ChessGame.TeamColor.BLACK && row == 1)) && direction[0] != 2 && direction[0] != -2 && ((pieceAtTarget != null && !pieceAtTarget.getTeamColor().equals(pieceColor))||direction[1] == 0)) {
                        moves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                    }
                    // case where there's no enemy piece
                    else if (pieceAtTarget == null) {
                        if (direction[1] == 0 && direction[0] != 2 && direction[0] != -2) {
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        }
                    }
                    // case where there's an enemy piece
                    else {
                        if (!pieceAtTarget.getTeamColor().equals(pieceColor)) {
                            if (direction[1]!=0) {
                                moves.add(new ChessMove(myPosition, newPosition, null));
                            }
                        }
                    }
                    // case where you can move two spaces
                    if ((pieceColor == ChessGame.TeamColor.WHITE && og_row == 2) ||
                            (pieceColor == ChessGame.TeamColor.BLACK && og_row == 7)) {
                        if (direction[1] == 0) {
                            if (direction[0] == 2 && pieceAtTarget == null && pieceAtTargetBehind == null) {
                                moves.add(new ChessMove(myPosition, newPosition, null));
                            }
                        }
                    }
                }
            }
        }
        // THE QUEEN
        if (this.type == PieceType.QUEEN) {
            int[][] directions = {
                    {-1,0},{1,0},{0,1},{0,-1},
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
        // THE ROOK
        if (this.type == PieceType.ROOK) {
            int[][] directions = {
                    {-1,0},{1,0},{0,1},{0,-1}
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
