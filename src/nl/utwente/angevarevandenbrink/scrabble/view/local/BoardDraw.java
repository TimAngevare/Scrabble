package nl.utwente.angevarevandenbrink.scrabble.view.local;

import nl.utwente.angevarevandenbrink.scrabble.model.Board;
import nl.utwente.angevarevandenbrink.scrabble.model.Position;
import nl.utwente.angevarevandenbrink.scrabble.model.SquareType;
import nl.utwente.angevarevandenbrink.scrabble.view.ANSI;

import java.util.Locale;

public class BoardDraw implements ANSI {
    static final int MAGNIF = 4;
    static final int BOARD_SIZE = 15;
    static final int INDENT = 3;

    public void drawBoard(Board board) {
        this.drawLetters();
        this.drawTop();
        for (int i = 1; i <= BOARD_SIZE; i++){
            drawRowBoard(i, board);
        }
        drawReference();
    }

    public String drawStringBoard(Board board) {
        String result = "";

        result += drawStringLetters();
        result += drawStringTop();
        for (int i = 1; i <= BOARD_SIZE; i++){
            result += drawStringRowBoard(i, board);
        }
        drawStringReference();

        return result;
    }

    private void drawRowBoard(int row, Board board){
        if (row >= 10){
            System.out.print(row + " ".repeat(INDENT -2));
        } else {
            System.out.print(row + " ".repeat(INDENT -1));
        }

        for (int square = 0; square < BOARD_SIZE; square++){
            this.drawTile(board, square, row - 1);
        }
        System.out.print("│\n");

        if (row != BOARD_SIZE){
            drawTop();
        } else {
            drawBottom();
        }
    }

    private String drawStringRowBoard(int row, Board board){
        String result = "";

        if (row >= 10){
            result += (row + " ".repeat(INDENT -2));
        } else {
            result += (row + " ".repeat(INDENT -1));
        }

        for (int square = 0; square < BOARD_SIZE; square++){
            result += drawStringTile(board, square, row - 1);
        }
        result += ("│\n");

        if (row != BOARD_SIZE){
            result += drawStringTop();
        } else {
            result += drawStringBottom();
        }

        return result;
    }

    private void drawTile(Board board, int col, int row){
        Position position = board.getPosition(row, col);
        String color = checkColor(position.getType());
        System.out.print("│");
        if (position.isEmpty()){
            System.out.print(color + " ".repeat(MAGNIF) + RESET);
        } else {
            System.out.print(color + " " + String.valueOf(position.getTile().getLetter()).toUpperCase(Locale.ROOT) + " ".repeat(MAGNIF - 2) + RESET);
        }
    }

    private String drawStringTile(Board board, int col, int row){
        String result = "";

        Position position = board.getPosition(row, col);
        String color = checkColor(position.getType());
        result += ("│");
        if (position.isEmpty()){
            result += (color + " ".repeat(MAGNIF) + RESET);
        } else {
            result += (color + " " + String.valueOf(position.getTile().getLetter()).toUpperCase() + " ".repeat(MAGNIF - 2) + RESET);
        }

        return result;
    }

    private String checkColor(SquareType square){
        switch (square){
            case DOUBLE_LETTER:
                return CYAN_BACKGROUND;
            case DOUBLE_WORD:
                return PURPLE_BACKGROUND;
            case TRIPLE_LETTER:
                return BLUE_BACKGROUND;
            case TRIPLE_WORD:
                return RED_BACKGROUND;
            case START:
                return YELLOW_BACKGROUND;
            default:
                return "";
        }
    }

    private void drawTop(){
        System.out.print(" ".repeat(INDENT) + "┌");
        for (int i = 1; i <= BOARD_SIZE * MAGNIF; i++){
            System.out.print("─");
            if (i % MAGNIF == 0){
                System.out.print("┐");
            }
        }
        System.out.print("\n");

    }

    private String drawStringTop(){
        String result = "";

        result += (" ".repeat(INDENT) + "┌");
        for (int i = 1; i <= BOARD_SIZE * MAGNIF; i++){
            result += ("─");
            if (i % MAGNIF == 0){
                result += ("┐");
            }
        }
        result += ("\n");

        return result;
    }

    private void drawBottom(){
        System.out.print(" ".repeat(INDENT) +"└");
        for (int i = 1; i <= BOARD_SIZE * MAGNIF; i++){
            System.out.print("─");
            if (i % MAGNIF == 0){
                System.out.print("┘");
            }
        }
        System.out.print("\n");
    }

    private String drawStringBottom(){
        String result = "";

        result += (" ".repeat(INDENT) +"└");
        for (int i = 1; i <= BOARD_SIZE * MAGNIF; i++){
            result += ("─");
            if (i % MAGNIF == 0){
                result += ("┘");
            }
        }
        result += ("\n");

        return result;
    }

    private void drawReference(){
        for (SquareType square : SquareType.values()){
            if (square != SquareType.BLANK){
                System.out.print(checkColor(square) + "  " + RESET + " : " + square.toString() + "  ");
            }
        }
        System.out.print("\n");
    }

    private String drawStringReference(){
        String result = "";

        for (SquareType square : SquareType.values()){
            if (square != SquareType.BLANK){
                result += (checkColor(square) + "  " + RESET + " : " + square.toString() + "  ");
            }
        }
        result += ("\n");

        return result;
    }

    private void drawLetters(){
        int a = 65;
        System.out.print(" ".repeat(INDENT + MAGNIF / 2));
        for (int i = a; i < a + BOARD_SIZE; i++){
            System.out.print((char) i + " ".repeat(INDENT + 1));
        }
        System.out.print("\n");
    }

    private String drawStringLetters(){
        String result = "";

        int a = 65;
        result += (" ".repeat(INDENT + MAGNIF / 2));
        for (int i = a; i < a + BOARD_SIZE; i++){
            result += ((char) i + " ".repeat(INDENT + 1));
        }
        result += ("\n");

        return result;
    }

}
