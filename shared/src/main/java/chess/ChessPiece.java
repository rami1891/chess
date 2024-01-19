package chess;

import java.util.Collection;
import java.util.HashSet;

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
     * Calculates all the positions a king can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        //throw new RuntimeException("Not implemented");
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new HashSet<>();
        ChessPosition[] kingMoves = new ChessPosition[8];
        kingMoves[0] = new ChessPosition(row + 1, col);
        kingMoves[1] = new ChessPosition(row + 1, col + 1);
        kingMoves[2] = new ChessPosition(row, col + 1);
        kingMoves[3] = new ChessPosition(row - 1, col + 1);
        kingMoves[4] = new ChessPosition(row - 1, col);
        kingMoves[5] = new ChessPosition(row - 1, col - 1);
        kingMoves[6] = new ChessPosition(row, col - 1);
        kingMoves[7] = new ChessPosition(row + 1, col - 1);
        for (int i = 0; i < 8; i++) {
            if (kingMoves[i].getRow() < 1 || kingMoves[i].getRow() > 8 || kingMoves[i].getColumn() < 1 || kingMoves[i].getColumn() > 8) {
                kingMoves[i] = null;
                continue;
            }
            else if(board.getPiece(kingMoves[i]) != null && board.getPiece(kingMoves[i]).getTeamColor() == this.pieceColor){
                kingMoves[i] = null;
                continue;
            }
            else{
                moves.add(new ChessMove(myPosition, kingMoves[i], null));
            }

        }

        return moves;
    }



    private ChessPosition[] isValidMove(ChessBoard board, ChessPosition[] typeMoves){
        for (int i = 0; i < typeMoves.length; i++) {
            if (typeMoves[i].getRow() < 1 || typeMoves[i].getRow() > 8 || typeMoves[i].getColumn() < 1 || typeMoves[i].getColumn() > 8) {
                typeMoves[i] = null;
            } else if (board.getPiece(typeMoves[i]) != null && board.getPiece(typeMoves[i]).getTeamColor() == this.pieceColor) {
                typeMoves[i] = null;
                for(int j = i; j < typeMoves.length; j++) {
                    typeMoves[j] = null;
                }
                break;
            } else if (board.getPiece(typeMoves[i]) != null && board.getPiece(typeMoves[i]).getTeamColor() != this.pieceColor) {
                for(int j = i + 1; j < typeMoves.length; j++) {
                    typeMoves[j] = null;
                }
                break;
            }

        }
        return typeMoves;
    }

    /**
     * Calculates all the positions a queen can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        //throw new RuntimeException("Not implemented");
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new HashSet<>();
        ChessPosition[] queenMoves = new ChessPosition[8];

        // down lane
        for (int i = 0; i < 8; i++) {
            queenMoves[i] = new ChessPosition(row + i + 1, col);
        }
        queenMoves = isValidMove(board, queenMoves);
        for(ChessPosition queen : queenMoves){
            if (queen == null){
                continue;
            }
            moves.add(new ChessMove(myPosition, queen, null));
        }

        // up lane
        for (int i = 0; i < 8; i++) {
            queenMoves[i] = new ChessPosition(row - i - 1, col);
        }
        queenMoves = isValidMove(board, queenMoves);
        for(ChessPosition queen : queenMoves){
            if (queen == null){
                continue;
            }
            moves.add(new ChessMove(myPosition, queen, null));
        }

        // right lane
        for (int i = 0; i < 8; i++) {
            queenMoves[i] = new ChessPosition(row, col + i+ 1);
        }
        queenMoves = isValidMove(board, queenMoves);
        for(ChessPosition queen : queenMoves){
            if (queen == null){
                continue;
            }
            moves.add(new ChessMove(myPosition, queen, null));
        }

        // left lane
        for (int i = 0; i < 8; i++) {
            queenMoves[i] = new ChessPosition(row, col - i - 1);
        }
        queenMoves = isValidMove(board, queenMoves);
        for(ChessPosition queen : queenMoves){
            if (queen == null){
                continue;
            }
            moves.add(new ChessMove(myPosition, queen, null));
        }

        // down right lane
        for (int i = 0; i < 8; i++) {
            queenMoves[i] = new ChessPosition(row + i + 1, col + i+ 1);
        }
        queenMoves = isValidMove(board, queenMoves);
        for(ChessPosition queen : queenMoves){
            if (queen == null){
                continue;
            }
            moves.add(new ChessMove(myPosition, queen, null));
        }

        // up left lane
        for (int i = 0; i < 8; i++) {
            queenMoves[i] = new ChessPosition(row - i - 1, col - i -1);
        }
        queenMoves = isValidMove(board, queenMoves);
        for(ChessPosition queen : queenMoves){
            if (queen == null){
                continue;
            }
            moves.add(new ChessMove(myPosition, queen, null));
        }

        // up right lane
        for (int i = 0; i < 8; i++) {
            queenMoves[i] = new ChessPosition(row + i+ 1, col - i - 1);
        }
        queenMoves = isValidMove(board, queenMoves);
        for(ChessPosition queen : queenMoves){
            if (queen == null){
                continue;
            }
            moves.add(new ChessMove(myPosition, queen, null));
        }

        // down left lane
        for (int i = 0; i < 8; i++) {
            queenMoves[i] = new ChessPosition(row - i - 1, col + i + 1);
        }
        queenMoves = isValidMove(board, queenMoves);
        for(ChessPosition queen : queenMoves){
            if (queen == null){
                continue;
            }
            moves.add(new ChessMove(myPosition, queen, null));
        }

        return moves;
    }



    /**
     * Calculates all the positions a bishop can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        //throw new RuntimeException("Not implemented");
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new HashSet<>();
        ChessPosition[] bishopMoves = new ChessPosition[8];
        // down right lane
        for (int i = 0; i < 8; i++) {
            bishopMoves[i] = new ChessPosition(row + i + 1, col + i+ 1);
        }
        bishopMoves = isValidMove(board, bishopMoves);
        for(ChessPosition bishop : bishopMoves){
            if (bishop == null){
                continue;
            }
            moves.add(new ChessMove(myPosition, bishop, null));
        }

        // up left lane
        for (int i = 0; i < 8; i++) {
            bishopMoves[i] = new ChessPosition(row - i - 1, col - i -1);
        }
        bishopMoves = isValidMove(board, bishopMoves);
        for(ChessPosition bishop : bishopMoves){
            if (bishop == null){
                continue;
            }
            moves.add(new ChessMove(myPosition, bishop, null));
        }

        // up right lane
        for (int i = 0; i < 8; i++) {
            bishopMoves[i] = new ChessPosition(row + i+ 1, col - i - 1);
        }
        bishopMoves = isValidMove(board, bishopMoves);
        for(ChessPosition bishop : bishopMoves){
            if (bishop == null){
                continue;
            }
            moves.add(new ChessMove(myPosition, bishop, null));
        }

        // down left lane
        for (int i = 0; i < 8; i++) {
            bishopMoves[i] = new ChessPosition(row - i - 1, col + i + 1);
        }
        bishopMoves = isValidMove(board, bishopMoves);
        for(ChessPosition bishop : bishopMoves){
            if (bishop == null){
                continue;
            }
            moves.add(new ChessMove(myPosition, bishop, null));
        }

        return moves;
    }

    /**
     * Calculates all the positions a knight can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        //throw new RuntimeException("Not implemented");
        int row = myPosition.getRow() ;
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new HashSet<>();
        ChessPosition[] knightMoves = new ChessPosition[8];
        knightMoves[0] = new ChessPosition(row + 2, col + 1);
        knightMoves[1] = new ChessPosition(row + 2, col - 1);
        knightMoves[2] = new ChessPosition(row - 2, col + 1);
        knightMoves[3] = new ChessPosition(row - 2, col - 1);
        knightMoves[4] = new ChessPosition(row + 1, col + 2);
        knightMoves[5] = new ChessPosition(row + 1, col - 2);
        knightMoves[6] = new ChessPosition(row - 1, col + 2);
        knightMoves[7] = new ChessPosition(row - 1, col - 2);

        for (int i = 0; i < 8; i++) {
            if (knightMoves[i].getRow() < 1 || knightMoves[i].getRow() > 8 || knightMoves[i].getColumn() < 1 || knightMoves[i].getColumn() > 8) {
                knightMoves[i] = null;
            }
            else if (board.getPiece(knightMoves[i]) != null && board.getPiece(knightMoves[i]).getTeamColor() == this.pieceColor) {
                knightMoves[i] = null;
            }
            else{
                moves.add(new ChessMove(myPosition, knightMoves[i], null));
            }
        }
        return moves;
    }

    /**
     * Calculates all the positions a rook can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        //throw new RuntimeException("Not implemented");
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new HashSet<>();
        ChessPosition[] rookMoves = new ChessPosition[8];
        // down lane
        for (int i = 0; i < 8; i++) {
            rookMoves[i] = new ChessPosition(row + i + 1, col);
        }
        rookMoves = isValidMove(board, rookMoves);
        for(ChessPosition rook : rookMoves){
            if (rook == null){
                continue;
            }
            moves.add(new ChessMove(myPosition, rook, null));
        }

        // up lane
        for (int i = 0; i < 8; i++) {
            rookMoves[i] = new ChessPosition(row - i - 1, col);
        }
        rookMoves = isValidMove(board, rookMoves);
        for(ChessPosition rook : rookMoves){
            if (rook == null){
                continue;
            }
            moves.add(new ChessMove(myPosition, rook, null));
        }

        // right lane
        for (int i = 0; i < 8; i++) {
            rookMoves[i] = new ChessPosition(row, col + i+ 1);
        }
        rookMoves = isValidMove(board, rookMoves);
        for(ChessPosition rook : rookMoves){
            if (rook == null){
                continue;
            }
            moves.add(new ChessMove(myPosition, rook, null));
        }

        // left lane
        for (int i = 0; i < 8; i++) {
            rookMoves[i] = new ChessPosition(row, col - i - 1);
        }
        rookMoves = isValidMove(board, rookMoves);
        for(ChessPosition rook : rookMoves){
            if (rook == null){
                continue;
            }
            moves.add(new ChessMove(myPosition, rook, null));
        }

        return moves;
    }

    /**
     * Calculates all the positions a pawn can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        //throw new RuntimeException("Not implemented");
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new HashSet<>();
        ChessPosition[] pawnMoves = new ChessPosition[4];
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            pawnMoves[0] = new ChessPosition(row + 1, col);
            pawnMoves[1] = new ChessPosition(row + 1, col + 1);
            pawnMoves[2] = new ChessPosition(row + 1, col - 1);
            pawnMoves[3] = new ChessPosition(row + 2, col);
        } else {
            pawnMoves[0] = new ChessPosition(row - 1, col);
            pawnMoves[1] = new ChessPosition(row - 1, col + 1);
            pawnMoves[2] = new ChessPosition(row - 1, col - 1);
            pawnMoves[3] = new ChessPosition(row - 2, col);
        }
        for (int i = 0; i < 4; i++) {
            if (pawnMoves[i].getRow() < 0 || pawnMoves[i].getRow() > 7 || pawnMoves[i].getColumn() < 0 || pawnMoves[i].getColumn() > 7) {
                pawnMoves[i] = null;
            }
            else{
                moves.add(new ChessMove(myPosition, pawnMoves[i], null));
            }
        }
        return moves;
    }



    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (type) {
            case KING:
                return kingMoves(board, myPosition);
            case QUEEN:
                return queenMoves(board, myPosition);
            case BISHOP:
                return bishopMoves(board, myPosition);
            case KNIGHT:
                return knightMoves(board, myPosition);
            case ROOK:
                return rookMoves(board, myPosition);
            case PAWN:
                return pawnMoves(board, myPosition);
            default:
                throw new RuntimeException("Not implemented");
        }

        //throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessPiece) {
            ChessPiece other = (ChessPiece) obj;
            return other.pieceColor == this.pieceColor && other.type == this.type;
        }
           return false;
    }

    @Override
    public String toString() {
        return pieceColor + " " + type;
    }
}
