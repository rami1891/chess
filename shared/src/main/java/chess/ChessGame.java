package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor teamTurn;


    public ChessGame() {
        this.board = new ChessBoard();
        this.teamTurn = TeamColor.WHITE;

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
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
        if(board.getPiece(startPosition) == null) {
            return null;
        }

        else{
            Collection<ChessMove> moves = board.getPiece(startPosition).pieceMoves(board, startPosition);

            moves.removeIf(move -> moveWillCauseCheck(move));
            return moves;
        }
    }

    /**
     * Return a boolean indicating if a move will cause a check for the current team
     * or it will move out of check
     */
    public boolean moveWillCauseCheck(ChessMove move) {
        ChessGame copy = new ChessGame();
        ChessBoard newBoard = new ChessBoard();

        for(int i = 1; i < 9; i++) {
            for(int j = 1; j < 9; j++) {
                newBoard.addPiece(new ChessPosition(i, j), board.getPiece(new ChessPosition(i, j)));
            }
        }

        copy.setBoard(newBoard);
        copy.setTeamTurn(board.getPiece(move.getStartPosition()).getTeamColor());
            copy.board.addPiece(move.getEndPosition(), copy.board.getPiece(move.getStartPosition()));
            copy.board.addPiece(move.getStartPosition(), null);

        return copy.isInCheck(copy.teamTurn);

    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // Check if move is null
        if (move == null) {
            throw new InvalidMoveException("Move is null");
        }

        // Check if there is a piece at the start position
        if (board.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException("No piece at start position");
        }

        // Check if move will cause check
        if(isInCheck(board.getPiece(move.getStartPosition()).getTeamColor())) {
            if(!tryMove(move)) {
                throw new InvalidMoveException("Move will cause check");
            }
        }

        // Check if it's the right team's turn
        if (board.getPiece(move.getStartPosition()).getTeamColor() != teamTurn) {
            throw new InvalidMoveException("Not this team's turn");
        }

        // Check if the move is valid
        if (board.getPiece(move.getStartPosition()).pieceMoves(board, move.getStartPosition()).contains(move)) {
            if(board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.PAWN) {
                // Check if the move is a pawn promotion
                if(move.getEndPosition().getRow() == 1 || move.getEndPosition().getRow() == 8) {
                    board.addPiece(move.getEndPosition(), new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece()));
                }
                else {
                    board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
                }
                board.addPiece(move.getStartPosition(), null);
                teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
            }
            else {
                board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
                board.addPiece(move.getStartPosition(), null);
                teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
            }
        }
        else {
            throw new InvalidMoveException("Invalid move");
        }
    }


    /**
     * Try to make a move in a chess game
     *
     * @param move chess move to preform
     * @return true if move is valid, false if move is invalid
     */

    public boolean tryMove(ChessMove move){
        ChessGame copy = new ChessGame();
        ChessBoard newBoard = new ChessBoard();

        for(int i = 1; i < 9; i++) {
            for(int j = 1; j < 9; j++) {
                newBoard.addPiece(new ChessPosition(i, j), board.getPiece(new ChessPosition(i, j)));
            }
        }
        copy.setBoard(newBoard);
        copy.setTeamTurn(teamTurn);


        //
        if(isInCheck(teamTurn)) {
            copy.board.addPiece(move.getEndPosition(), copy.board.getPiece(move.getStartPosition()));
            copy.board.addPiece(move.getStartPosition(), null);
            return !copy.isInCheck(teamTurn);
        }
        return true;
    }


    /**
     * Gets the position of the king of the given team
     *
     * @param teamColor which team to get the king position for
     * @return the position of the king, or null if the king is not found
     */
    public ChessPosition getKingPosition(TeamColor teamColor) {
        for(int i = 1; i < 9; i++) {
            for(int j = 1; j < 9; j++) {
                if(board.getPiece(new ChessPosition(i, j)) != null) {
                    if(board.getPiece(new ChessPosition(i, j)).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(new ChessPosition(i, j)).getTeamColor() == teamColor) {
                        return new ChessPosition(i, j);
                    }
                }
            }
        }
        return null;
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = getKingPosition(teamColor);
        if (kingPos == null) {
            // King not found, not in check
            return false;
        }

        TeamColor opposingTeam = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;

        return isUnderAttack(kingPos, opposingTeam);
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isUnderAttack(ChessPosition position, TeamColor teamColor) {
        // Check if any opposing team pieces can move to the given position
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(pos);

                // Check if the piece is of the opposing team
                if (piece != null && piece.getTeamColor() == teamColor) {

                    Collection<ChessMove> moves = board.getPiece(pos).pieceMoves(board, pos);

                    if (moves != null) {
                        for (ChessMove move : moves) {
                            if (move.getEndPosition().equals(position)) {
                                return true;
                            }
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
    public Collection<ChessMove> kingValid(ChessPosition startPosition, Collection<ChessMove> moves) {
        moves.removeIf(move -> isUnderAttack(move.getEndPosition(), board.getPiece(startPosition).getTeamColor()));

        return moves;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition kingPos = getKingPosition(teamColor);
        if (kingPos == null) {
            // King not found, not in checkmate
            return false;
        }
        if(!isInCheck(teamColor)) {
            return false;
        }

        Collection<ChessMove> moves = kingValid(kingPos, validMoves(kingPos));
        // If the king can move out of check
        for(ChessMove move : moves) {
            ChessGame copy = new ChessGame();
            copy.setBoard(board);
            copy.setTeamTurn(teamColor);
            try {
                copy.makeMove(move);
            } catch (InvalidMoveException e) {
                continue;
            }
            if(!copy.isInCheck(teamColor)) {
                return false;
            }
        }


        return true;

    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (teamColor == TeamColor.WHITE) {
            for (int i = 1; i < 9; i++) {
                for (int j = 1; j < 9; j++) {
                    ChessPosition pos = new ChessPosition(i, j);
                    if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == TeamColor.WHITE) {
                        Collection<ChessMove> moves = board.getPiece(pos).pieceMoves(board, pos);
                        if (moves != null && !moves.isEmpty()) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        // TeamColor.BLACK
        else {
            for (int i = 1; i < 9; i++) {
                for (int j = 1; j < 9; j++) {
                    ChessPosition pos = new ChessPosition(i, j);
                    if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == TeamColor.BLACK) {
                        Collection<ChessMove> moves = board.getPiece(pos).pieceMoves(board, pos);
                        if (moves != null && !moves.isEmpty()) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }



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


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ChessGame otherGame = (ChessGame) obj;
        return this.board.equals(otherGame.board) && this.teamTurn == otherGame.teamTurn;
    }



    @Override
    public String toString() {
        return "ChessGame [board=" + board + ", teamTurn=" + teamTurn + "]";
    }

    @Override
    public int hashCode() {
        return board.hashCode() * 31 + teamTurn.hashCode();
    }
}
