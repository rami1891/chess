package chess;

import java.util.Collection;
import java.util.HashSet;

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
        //return ChessPiece.peiceMoves(board, startPosition);
        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> moves = new HashSet<>();
        moves = piece.pieceMoves(board, startPosition);
        if(piece == null) {
            return null;
        }
        if (piece.getTeamColor() != teamTurn) {
            return null;
        }

        if (piece.getPieceType() == ChessPiece.PieceType.KING){
            if(isInCheck(piece.getTeamColor())){
                for (ChessMove move: moves){
                    ChessGame tempGame = new ChessGame();
                    tempGame.setBoard(board);
                    tempGame.setTeamTurn(teamTurn);
                    try {
                        tempGame.makeMove(move);
                    } catch (InvalidMoveException e) {
                        continue;
                    }
                    if(!tempGame.isInCheck(piece.getTeamColor())){
                        moves.remove(move);
                    }
                }
            }


        }

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        if(teamColor == TeamColor.WHITE) {
            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) {
                    if(board.getPiece(new ChessPosition(i, j)) != null) {
                        if(board.getPiece(new ChessPosition(i, j)).getTeamColor() == TeamColor.BLACK) {
                            for(ChessMove move : validMoves(new ChessPosition(i, j))) {
                                if(board.getPiece(move.getEndPosition()) != null) {
                                    if(board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
        else {
            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) {
                    if(board.getPiece(new ChessPosition(i, j)) != null) {
                        if(board.getPiece(new ChessPosition(i, j)).getTeamColor() == TeamColor.WHITE) {
                            for(ChessMove move : validMoves(new ChessPosition(i, j))) {
                                if(board.getPiece(move.getEndPosition()) != null) {
                                    if(board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(teamColor == TeamColor.WHITE) {
            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) {
                    if(board.getPiece(new ChessPosition(i, j)) != null) {
                        if(board.getPiece(new ChessPosition(i, j)).getTeamColor() == TeamColor.WHITE) {
                            for(ChessMove move : validMoves(new ChessPosition(i, j))) {
                                ChessGame tempGame = new ChessGame();
                                tempGame.setBoard(board);
                                tempGame.setTeamTurn(TeamColor.WHITE);
                                try {
                                    tempGame.makeMove(move);
                                } catch (InvalidMoveException e) {
                                    continue;
                                }
                                if(!tempGame.isInCheck(TeamColor.WHITE)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }
        else {
            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) {
                    if(board.getPiece(new ChessPosition(i, j)) != null) {
                        if(board.getPiece(new ChessPosition(i, j)).getTeamColor() == TeamColor.BLACK) {
                            for(ChessMove move : validMoves(new ChessPosition(i, j))) {
                                ChessGame tempGame = new ChessGame();
                                tempGame.setBoard(board);
                                tempGame.setTeamTurn(TeamColor.BLACK);
                                try {
                                    tempGame.makeMove(move);
                                } catch (InvalidMoveException e) {
                                    continue;
                                }
                                if(!tempGame.isInCheck(TeamColor.BLACK)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
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
