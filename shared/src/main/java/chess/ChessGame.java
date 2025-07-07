package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
//    private ChessPiece piece;
    private TeamColor color;
    private ChessBoard board;
    private ChessPiece currentPiece;
    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.color = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return color;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
//        this.color = team;
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return color == chessGame.color && Objects.equals(board, chessGame.board) && Objects.equals(currentPiece, chessGame.currentPiece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, board, currentPiece);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "color=" + color +
                ", board=" + board +
                ", currentPiece=" + currentPiece +
                '}';
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        List<ChessMove> pieceMoves = (List<ChessMove>) piece.pieceMoves(board, startPosition);
        List<ChessMove> validPieceMoves = new ArrayList<>();

        for (ChessMove move : pieceMoves) {
            ChessPosition endPosition = move.getEndPosition();

            // save captured pieces
            ChessPiece capturedPiece = board.getPiece(endPosition);

            board.addPiece(endPosition, piece);
            board.addPiece(startPosition, null);

            if (!isInCheck(piece.getTeamColor())) {
                validPieceMoves.add(move);
            }

            // reset the board
            board.addPiece(startPosition, piece);
            board.addPiece(endPosition, capturedPiece);
        }

        return validPieceMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();

        ChessPiece movingPiece = board.getPiece(startPosition);

        if (isInCheck(getTeamTurn())) {
            throw new InvalidMoveException("Checked");
        }
//        if (isInCheckmate(getTeamTurn())) {
//            throw new InvalidMoveException("Checkmate");
//        }
//        if (isInStalemate(getTeamTurn())) {
//            throw new InvalidMoveException("Stalemate");
//        }

        if (movingPiece == null || movingPiece.getTeamColor() != color) {
            throw new InvalidMoveException("Invalid move");
        }

        Collection<ChessMove> validMoves = validMoves(startPosition);
        if (validMoves == null) {
            throw new InvalidMoveException("Valid moves is null");
        }
        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("Invalid move");
        }

        // movement logic
        // move to new position
        board.addPiece(endPosition, movingPiece);
        // delete old piece
        board.addPiece(startPosition, null);

        // Pawn promotion
        if (move.getPromotionPiece() != null && movingPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
            movingPiece = new ChessPiece(color, move.getPromotionPiece());
            board.addPiece(endPosition, movingPiece);
        }

        // switch to other team
        color = (color == TeamColor.BLACK) ? TeamColor.WHITE : TeamColor.BLACK;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPiece king = null;
        ChessPosition kingPosition = null;
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() == teamColor &&
                        piece.getPieceType() == ChessPiece.PieceType.KING) {
                    king = piece;
                    kingPosition = position;
                    break;
                }
            }
        }

        if (king == null) {
            return false; // this should never happen
        }

        // See if any piece can attack the king
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() != teamColor) {
                    List<ChessMove> moves = (List<ChessMove>) piece.pieceMoves(board, position);
                    for (ChessMove move : moves) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // Returns true if the given team has no way to protect their king from being captured.
        ChessPiece king = null;
        ChessPosition kingPosition = null;
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() == teamColor &&
                        piece.getPieceType() == ChessPiece.PieceType.KING) {
                    king = piece;
                    kingPosition = position;
                    break;
                }
            }
        }

        if (king == null) {
            return false; // this should never happen
        }


        // see if any other pieces can save the day before king gets shrecked
        List<ChessMove> helperMoves = new ArrayList<>();
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);

                if (piece != null) {
                    if (piece.getTeamColor()==teamColor) {
                        if (piece.getPieceType() != ChessPiece.PieceType.KING) {
                            helperMoves = (List<ChessMove>) piece.pieceMoves(board, position);
                        }
                    }
                }
            }
        }
        for (ChessMove move : helperMoves) {

            ChessPiece PieceAtTarget =  board.getPiece(move.getEndPosition());
            if (PieceAtTarget != null) {
                board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
            }

        }
        // see if king can capture
        List<ChessMove> king_moves = (List<ChessMove>) king.pieceMoves(board, kingPosition);
        for (ChessMove k_move : king_moves) {
            ChessPosition end_move = k_move.getEndPosition();
            ChessPiece PieceAtTarget =  board.getPiece(end_move);
            if (PieceAtTarget != null) {
                if (PieceAtTarget.getTeamColor() != teamColor) {
                    // check to see if enemy can still capture

                    for (int row = 1; row <= 8; row++) {
                        for (int col = 1; col <= 8; col++) {
                            ChessPosition position = new ChessPosition(row, col);
                            ChessPiece piece = board.getPiece(position);
                            List<ChessMove> moves = new ArrayList<>();
                            if (piece != null) {
                                if (piece.getTeamColor()!=teamColor) {
                                    ChessBoard upBoard = board;
                                    upBoard.addPiece(end_move, king);
                                    moves = (List<ChessMove>) piece.pieceMoves(upBoard, position);



                                    System.out.println(moves);
                                }
                                for (ChessMove move : moves) {
                                    if (move.getEndPosition().equals(end_move)) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }

                    return false;
                }
            }
        }
        // See if any piece can attack the king
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);

                if (piece != null) {
                    List<ChessMove> moves = (List<ChessMove>) piece.pieceMoves(board, position);

                    for (ChessMove move : moves) {

                        if (move.getEndPosition().equals(kingPosition)) {

                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        throw new RuntimeException("Not implemented");
    }
}
