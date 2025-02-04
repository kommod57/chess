package chess;

import java.time.Period;
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
    private ChessBoard board;
    private TeamColor currentColor;
    private ChessPiece currentPiece;
//    private ChessPosition startingPosition;
//    private ChessPosition endingPosition;

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", currentColor=" + currentColor +
                '}';
    }

    public ChessGame() {
        this.board = new ChessBoard(); ;
        this.board.resetBoard();
        this.currentColor = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.currentColor = team;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && currentColor == chessGame.currentColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, currentColor);
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
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();

        ChessPiece moving_piece = board.getPiece(startPosition);


        if (isInCheck(getTeamTurn())) {
            throw new InvalidMoveException("Checked");
        }
        if (moving_piece == null || moving_piece.getTeamColor() != currentColor) {
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
        board.addPiece(endPosition, moving_piece);
        // delete old piece
        board.addPiece(startPosition, null);

        // Pawn promotion
        if (move.getPromotionPiece() != null && moving_piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            moving_piece = new ChessPiece(currentColor, move.getPromotionPiece());
            board.addPiece(endPosition, moving_piece);
        }

        // switch to other team
        currentColor = (currentColor == TeamColor.BLACK) ? TeamColor.WHITE : TeamColor.BLACK;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // Returns true if the specified team’s King could be captured by an opposing piece.
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
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //Returns true if the given team has no legal moves but their king is not in immediate danger.
        if (!isInCheckmate(teamColor))
        {
            // check if it can make a legal move
            ChessPosition kingPosition = null;

            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    ChessPosition position = new ChessPosition(row, col);
                    ChessPiece piece = board.getPiece(position);

                    if (piece != null && piece.getTeamColor() == teamColor &&
                            piece.getPieceType() == ChessPiece.PieceType.KING) {
                        kingPosition = position;
                        break;
                    }
                }
            }

            List<ChessMove> enemy_moves = new ArrayList<>();

            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    ChessPosition position = new ChessPosition(row, col);
                    ChessPiece piece = board.getPiece(position);

                    if (piece != null) {
                        if (piece.getTeamColor() != teamColor) {
                            enemy_moves = (List<ChessMove>) piece.pieceMoves(board, position);
                        }

                    }
                }
            }
            assert kingPosition != null;
            ChessPiece king_piece = board.getPiece(kingPosition);
            List<ChessMove> king_moves = (List<ChessMove>) king_piece.pieceMoves(board, kingPosition);
            for (ChessMove k_move : king_moves) {
                for (ChessMove e_move : enemy_moves) {
                    if (k_move.equals(e_move)) {
                        return true;
                    }
                }
            }
            // check to see if king is pinned
            int validMoves = king_moves.size();
            int kingHitHere = 0;
            boolean nextMove = true;
            for (ChessMove k_move : king_moves) {
                nextMove = true;
                for (int row = 1; row <= 8; row++) {
                    for (int col = 1; col <= 8; col++) {
                        ChessPosition position = new ChessPosition(row, col);

                        ChessPiece piece = board.getPiece(position);
                        List<ChessMove> moves = new ArrayList<>();
                        if (piece != null) {
                            if (piece.getTeamColor()!=teamColor){
                                moves = (List<ChessMove>) piece.pieceMoves(board, position);
                            }
                            for (ChessMove move : moves) {
                                if (move.getEndPosition().equals(k_move.getEndPosition())) {
                                    if (nextMove){
                                        kingHitHere += 1;
                                        nextMove = false;
                                    }


                                }
                            }
                        }
                    }
                }
            }
            System.out.println(kingHitHere + " " + validMoves);
            return kingHitHere >= validMoves && kingHitHere != 0;

//            assert kingPosition != null;
//            ChessPiece king_piece = board.getPiece(kingPosition);
//
//            List<ChessMove> king_moves = (List<ChessMove>) king_piece.pieceMoves(board, kingPosition);
//            return king_moves.isEmpty();
        }
            return false;
    }
//    public boolean castle(TeamColor teamColor, ChessPiece kingPiece, ChessPosition kingPosition,
//                          ChessPiece rookPiece, ChessPosition rookPosition) {
//
//        boolean hasntMovedKing = false;
//        if (kingPosition != null) {
//            if ((kingPiece.getTeamColor()==TeamColor.WHITE && kingPosition.getRow() == 1)||
//                    (kingPiece.getTeamColor()==TeamColor.BLACK && kingPosition.getRow() == 8)) {
//                if (kingPosition.getColumn() == 5) {
//                    hasntMovedKing = true;
//                }
//            }
//        }
//        boolean hasntMovedRook = false;
//        if (rookPosition != null) {
//            if ((rookPiece.getTeamColor()==TeamColor.WHITE && rookPosition.getRow() == 1)||
//                    (rookPiece.getTeamColor()==TeamColor.BLACK && rookPosition.getRow() == 8)) {
//                        hasntMovedRook = true;
//            }
//        }
//        boolean squaresEmpty = false;
//        if (rookPosition.getColumn() == 8) {
//            if (rookPiece.getTeamColor()==TeamColor.WHITE) {
//                ChessPosition pos =
//                if (board.getPiece([1][7])!=null)
//            }
//        }
//
//        return false;
//    }
    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
