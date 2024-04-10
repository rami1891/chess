package ui;

import org.junit.jupiter.api.DisplayNameGenerator;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;


public class ChessBoard {
    private PrintStream out;
    public ChessBoard() {
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }

    public void setup(String playerColor){
        out.print(ERASE_SCREEN);
        String[][] board = new String[8][8];
        String afterSpacing = "\u2001\u2005";
        String beforeSpacing = "\u2006";
        board[0] = new String[]{beforeSpacing+"R"+afterSpacing, beforeSpacing+"N"+afterSpacing, beforeSpacing+"B"+afterSpacing, beforeSpacing+"Q"+afterSpacing, beforeSpacing+"K"+afterSpacing, beforeSpacing+"B"+afterSpacing, beforeSpacing+"N"+afterSpacing, beforeSpacing+"R"+afterSpacing};
        board[1] = new String[]{beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing};
        for (int i = 2; i < 6; i++) {
            board[i] = new String[]{beforeSpacing+" "+afterSpacing, beforeSpacing+" "+afterSpacing, beforeSpacing+" "+afterSpacing, beforeSpacing+" "+afterSpacing, beforeSpacing+" "+afterSpacing, beforeSpacing+" "+afterSpacing, beforeSpacing+" "+afterSpacing, beforeSpacing+" "+afterSpacing};
        }
        board[6] = new String[]{beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing};
        board[7] = new String[]{beforeSpacing+"R"+afterSpacing, beforeSpacing+"N"+afterSpacing, beforeSpacing+"B"+afterSpacing, beforeSpacing+"Q"+afterSpacing, beforeSpacing+"K"+afterSpacing, beforeSpacing+"B"+afterSpacing, beforeSpacing+"N"+afterSpacing, beforeSpacing+"R"+afterSpacing};

        if (playerColor.equals("WHITE")){
            printChessBoardWhite(board);

        }
        else if (playerColor.equals("BLACK")){
            printChessBoardBlack(board);
        }

        else {
            printChessBoardWhite(board);
            printChessBoardBlack(board);
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

    public void printChessBoardWhite(String[][] board) {
        rowWhite();
        String spacing = "\u2001\u2005\u2006";
        String[] numbers = {"1" + spacing, "2" + spacing, "3" + spacing, "4" + spacing, "5" + spacing, "6" + spacing, "7" + spacing, "8" + spacing};
        for (int i = 0; i < 8; i++) {
            out.print(SET_BG_COLOR_BLACK + EMPTY);
            out.print(SET_BG_COLOR_LIGHT_GREY + numbers[i]);
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    out.print(SET_BG_COLOR_WHITE);
                } else {
                    out.print(SET_BG_COLOR_BLACK);
                }
                if(i >= 6) out.print(SET_TEXT_COLOR_RED);
                else out.print(SET_TEXT_COLOR_GREEN);


                out.print(board[i][j]);
            }
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

    public void printChessBoardBlack(String[][] board) {
        rowBlack();
        String spacing = "\u2001\u2005\u2006";
        String[] numbers = {"8" + spacing, "7" + spacing, "6" + spacing, "5" + spacing, "4" + spacing, "3" + spacing, "2" + spacing, "1" + spacing};
        for (int i = 0; i < 8; i++) {
            out.print(SET_BG_COLOR_BLACK + EMPTY);
            out.print(SET_BG_COLOR_LIGHT_GREY + numbers[i]);
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    out.print(SET_BG_COLOR_WHITE);
                } else {
                    out.print(SET_BG_COLOR_BLACK);
                }
                if(i >= 6) out.print(SET_TEXT_COLOR_GREEN);
                else out.print(SET_TEXT_COLOR_RED);


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