package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    // For castling
    private boolean whitKingMoved = false;
    private boolean blackKingMoved = false;
    private boolean whiteLeftRookMoved = false;
    private boolean whiteRightRookMoved = false;
    private boolean blackLeftRookMoved = false;
    private boolean blackRightRookMoved = false;

    // En Passant
    private ChessPosition enPassantVulnerablePawn = null;
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
        this.color = team;
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
        List<ChessMove> pieceMoves = (List<ChessMove>) piece.pieceMoves(board,startPosition);
        List<ChessMove> validPieceMoves = new ArrayList<>();

        for (ChessMove move : pieceMoves) {
            ChessPosition endPosition = move.getEndPosition();

            ChessPiece capturedPiece = board.getPiece(endPosition);

            //castle check later

            // reg move check
            board.addPiece(endPosition, piece);
            board.addPiece(startPosition, null);

            if (!isInCheck(piece.getTeamColor())) {
                validPieceMoves.add(move);
            }

            // en passant here

            // reset the board
            board.addPiece(startPosition,piece);
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

        if (movingPiece == null) {
            throw new InvalidMoveException("No piece at starting pos");
        }
        if (movingPiece.getTeamColor() != color) {
            throw new InvalidMoveException("Wrong team's turn");
        }

        // Castling

        if (! isInCheck(getTeamTurn())) {
            throw new InvalidMoveException("Checked");
        }

        if (movingPiece == null || movingPiece.getTeamColor() != color) {
            throw new InvalidMoveException("Invalid move");
        }

        Collection<ChessMove> validMoves = validMoves(startPosition);
        if (validMoves == null) {
            throw new InvalidMoveException("Invalid move");

        }

        // movement logic

        // En passant

        // move to new position
        board.addPiece(endPosition, movingPiece);
        //delete
        board.addPiece(startPosition, null);

        // pawn promotion
        if (move.getPromotionPiece() != null && movingPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
            movingPiece = new ChessPiece(color, move.getPromotionPiece());
            board.addPiece(endPosition, movingPiece);
        }

        // king and rook movement flags
        if (movingPiece.getPieceType() == ChessPiece.PieceType.KING) {
            if (color == TeamColor.WHITE) {
                whitKingMoved = true;
            }
            else {
                blackKingMoved = true;
            }
        } else if (movingPiece.getPieceType() == ChessPiece.PieceType.ROOK) {
            int row = startPosition.getRow();
            int col = startPosition.getColumn();
            if (color == TeamColor.WHITE) {
                if (row == 1 && c)
            }
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
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
