package chess;

import java.util.Collection;

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

    // TODO: implement getTeamColor()
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;

        // throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    // TODO: implement getPieceType()
    public PieceType getPieceType() {
        return type;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Calculates all the positions a pawn can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        if((myPosition.getRow() >= 1 || myPosition.getRow() <= 8) && (myPosition.getColumn() >= 1 || myPosition.getColumn() <= 8)) {
            if(board.isEmpty(myPosition.getRow() + 1)) {
                return new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()));
            }
        }
        if (getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (myPosition.getRow() == 2) {
                return new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn()));
            }
            return new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()));
        } else {
            if (myPosition.getRow() == 7) {
                return new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn()));
            }
            return new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()));
        }
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }



    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (getPieceType() == PieceType.PAWN) {
            return pawnMoves(board, myPosition);
        } else if (getPieceType() == PieceType.KNIGHT) {
            return knightMoves(board, myPosition);
        } else if (getPieceType() == PieceType.BISHOP) {
            return bishopMoves(board, myPosition);
        } else if (getPieceType() == PieceType.ROOK) {
            return rookMoves(board, myPosition);
        } else if (getPieceType() == PieceType.QUEEN) {
            return queenMoves(board, myPosition);
        } else if (getPieceType() == PieceType.KING) {
            return kingMoves(board, myPosition);
        } else {
            throw new RuntimeException("Not implemented");
        }
    }
}
