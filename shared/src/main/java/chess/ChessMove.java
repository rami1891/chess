package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;

    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessMove) {
            ChessMove other = (ChessMove) obj;
            return this.startPosition.equals(other.startPosition) && this.endPosition.equals(other.endPosition) && this.promotionPiece == other.promotionPiece;
        }
        return false;
    }

    @Override
    public String toString() {
        return "ChessMove [startPosition=" + startPosition + ", endPosition=" + endPosition + ", promotionPiece="
                + promotionPiece + "]";
    }

    @Override
    public int hashCode() {
        int result = 17; // Choose a prime number as an initial value
        result = 31 * result + startPosition.hashCode();
        result = 31 * result + endPosition.hashCode();
        result = 31 * result + (promotionPiece != null ? promotionPiece.hashCode() : 0);
        return result;    }

}
