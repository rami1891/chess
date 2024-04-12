package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;


public class ChessBoardClient {
    private PrintStream out;

    public ChessBoardClient() {
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }


    public void setup(String playerColor, ChessGame game) {
        if (playerColor.equals("WHITE")) {
            printChessBoardWhite(game);
        } else if (playerColor.equals("BLACK")){
            printChessBoardBlack(game);
        }
        else{
            printChessBoardWhite(game);
            printChessBoardBlack(game);
        }
    }





    private void rowWhite(){
        out.print("   ");
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        String spacing = "\u2001\u2005";
        System.out.print(SET_BG_COLOR_LIGHT_GREY + EMPTY + spacing + " ");
        for (int i = 0; i < 8; i++) {
            out.print(" " + (char)('a' + i) + spacing);
        }
        out.print(spacing);
        out.print(SET_BG_COLOR_BLACK);
        out.print("\n");
    }


    private void rowBlack(){
        out.print("   ");
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        String spacing = "\u2001\u2005";
        System.out.print(SET_BG_COLOR_LIGHT_GREY + EMPTY + spacing + " ");
        for (int i = 0; i < 8; i++) {
            out.print(" " + (char)('h' - i) + spacing);
        }
        out.print(spacing);
        out.print(SET_BG_COLOR_BLACK);
        out.print("\n");
    }

    public void printChessBoardWhite(ChessGame game) {
        System.out.println("White Board");

        out.print(ERASE_SCREEN);
        ChessBoard gameBoard = game.getBoard();
        String[][] board = new String[8][8];
        String afterSpacing = "\u2001\u2005";
        String beforeSpacing = "\u2006";


        rowWhite();
        String spacing = "\u2001\u2005\u2006";
        String[] numbers = {"1" + spacing, "2" + spacing, "3" + spacing, "4" + spacing, "5" + spacing, "6" + spacing, "7" + spacing, "8" + spacing};
        for (int i = 0; i < 8; i++) {
            // Print row number and background color
            out.print(SET_BG_COLOR_BLACK + EMPTY);
            out.print(SET_BG_COLOR_LIGHT_GREY + numbers[i]);

            for (int j = 0; j < 8; j++) {
                // Set cell background color
                if ((i + j) % 2 == 0) {
                    out.print(SET_BG_COLOR_WHITE);
                } else {
                    out.print(SET_BG_COLOR_BLACK);
                }

                // Get piece information and print it
                ChessPosition position = new ChessPosition(i+1, j+1);
                var piece = gameBoard.getPiece(position);
                if (piece == null) {
                    board[i][j] = beforeSpacing + " " + afterSpacing;
                } else {
                    var pieceType = piece.getPieceType();
                    var pieceColor = piece.getTeamColor();
                    var pieceString = "";
                    if (pieceColor == ChessGame.TeamColor.WHITE) {
                        pieceString += SET_TEXT_COLOR_GREEN;
                    } else {
                        pieceString += SET_TEXT_COLOR_RED;
                    }
                    switch (pieceType) {
                        case KING -> pieceString += "K";
                        case QUEEN -> pieceString += "Q";
                        case BISHOP -> pieceString += "B";
                        case KNIGHT -> pieceString += "N";
                        case ROOK -> pieceString += "R";
                        case PAWN -> pieceString += "P";
                    }
                    board[i][j] = beforeSpacing + pieceString + afterSpacing;
                }

                // Print the piece representation
                out.print(board[i][j]);
            }

            // Reset text and background colors
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(SET_BG_COLOR_LIGHT_GREY + numbers[i]);
            out.print(SET_BG_COLOR_BLACK + EMPTY);
            out.print("\n");
        }


        rowWhite();
            out.print(SET_BG_COLOR_DARK_GREY);
            out.print(SET_TEXT_COLOR_WHITE);

            out.print("\n");


    }

        public void printChessBoardBlack (ChessGame game){
            System.out.println("Black Board");

            out.print(ERASE_SCREEN);
            ChessBoard gameBoard = game.getBoard();
            String[][] board = new String[8][8];
            String afterSpacing = "\u2001\u2005";
            String beforeSpacing = "\u2006";


            rowBlack();
            String spacing = "\u2001\u2005\u2006";
            String[] numbers = {"8" + spacing, "7" + spacing, "6" + spacing, "5" + spacing, "4" + spacing, "3" + spacing, "2" + spacing, "1" + spacing};
            for (int i = 7; i >= 0; i--) {
                // Print row number and background color
                out.print(SET_BG_COLOR_BLACK + EMPTY);
                out.print(SET_BG_COLOR_LIGHT_GREY + numbers[i]);

                for (int j = 7; j >= 0; j--) {
                    // Set cell background color
                    if ((i + j) % 2 == 0) {
                        out.print(SET_BG_COLOR_WHITE);
                    } else {
                        out.print(SET_BG_COLOR_BLACK);
                    }

                    // Get piece information and print it
                    ChessPosition position = new ChessPosition(i+1, j+1);
                    var piece = gameBoard.getPiece(position);
                    if (piece == null) {
                        board[i][j] = beforeSpacing + " " + afterSpacing;
                    } else {
                        var pieceType = piece.getPieceType();
                        var pieceColor = piece.getTeamColor();
                        var pieceString = "";
                        if (pieceColor == ChessGame.TeamColor.BLACK) {
                            pieceString += SET_TEXT_COLOR_RED;
                        } else {
                            pieceString += SET_TEXT_COLOR_GREEN;
                        }
                        switch (pieceType) {
                            case KING -> pieceString += "K";
                            case QUEEN -> pieceString += "Q";
                            case BISHOP -> pieceString += "B";
                            case KNIGHT -> pieceString += "N";
                            case ROOK -> pieceString += "R";
                            case PAWN -> pieceString += "P";
                        }
                        board[i][j] = beforeSpacing + pieceString + afterSpacing;
                    }

                    // Print the piece representation
                    out.print(board[i][j]);
                }

                out.print(SET_TEXT_COLOR_BLACK);
                out.print(SET_BG_COLOR_LIGHT_GREY + numbers[i]);
                out.print(SET_BG_COLOR_BLACK + EMPTY);
                out.print("\n");
            }

            rowBlack();
            out.print(SET_BG_COLOR_DARK_GREY);
            out.print(SET_TEXT_COLOR_WHITE);
            out.print("\n");

        }
    }


