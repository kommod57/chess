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
        ArrayList<ChessMove> moves = new ArrayList<>();
        int[][] dirs = new int[0][0];
        boolean inf = false;
        boolean specialP = false;
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
            // Attempt to castle
            int row = myPosition.getRow();
            if(getTeamColor() == ChessGame.TeamColor.WHITE && row == 1) {
                // kingSide castling (white): col5 to col7, rook at col8
                if (canCastle(board, 1, 5, 6, 7, 8)) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(1,7), null));
                }
                // queenSide castling(white): col5 to col3, rook at col1
                if (canCastle(board, 1, 5, 4, 3, 2, 1)) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(1,3), null));
                }

            }
            if(getTeamColor() == ChessGame.TeamColor.BLACK && row == 8) {
                // kingSide castling (white): col5 to col7, rook at col8
                if (canCastle(board, 8, 5, 6, 7, 8)) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(8,7), null));
                }
                // queenSide castling(white): col5 to col3, rook at col1
                if (canCastle(board, 8, 5, 4, 3, 2, 1)) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(8,3), null));
                }

            }
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
            specialP = true;
            dirs = new int[][] {
                    {1,0},{2,0},{1,1},{1,-1}
            };
            if (getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
                dirs = new int[][] {
                        {-1,0},{-2,0},{-1,1},{-1,-1}
                };

            }
        }
        for (int[] direction : dirs) {
            int oldRow = myPosition.getRow();
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
                    if (specialP) {
                        if (((oldRow==7||oldRow==2)&&(board.getPiece(new ChessPosition(3, col)) != null||board.getPiece(new ChessPosition
                                (6, col)) != null))||direction[1]!=0||((direction[0]==2||
                                direction[0]==-2)&&oldRow!=7&&oldRow!=2)) {
                            break;
                        }
                        if (row==8||row==1) {
                            ChessPiece.addPromotionMoves(moves,myPosition, newPosition);
                            break;
                        }
                    }
                    moves.add(new ChessMove(myPosition, newPosition, null));

                }
                else {
                    if (!pieceAtTarget.getTeamColor().equals(pieceColor)) {
                        if (specialP) {
                            if (direction[1]==0) {
                                break;
                            }
                            if (row==8||row==1) {
                                ChessPiece.addPromotionMoves(moves,myPosition, newPosition);
                                break;
                            }
                        }
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
