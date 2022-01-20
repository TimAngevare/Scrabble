package nl.utwente.angevarevandenbrink.scrabble.local.view;

import nl.utwente.angevarevandenbrink.scrabble.model.Board;
import nl.utwente.angevarevandenbrink.scrabble.model.Position;
import nl.utwente.angevarevandenbrink.scrabble.model.SquareType;

import java.util.Locale;

public class BoardDraw implements ANSI{
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
    private void drawReference(){
        for (SquareType square : SquareType.values()){
            if (square != SquareType.BLANK){
                System.out.print(checkColor(square) + "  " + RESET + " : " + square.toString() + "  ");
            }
        }
        System.out.print("\n");
    }

    private void drawLetters(){
        int a = 65;
        System.out.print(" ".repeat(INDENT + MAGNIF / 2));
        for (int i = a; i < a + BOARD_SIZE; i++){
            System.out.print((char) i + " ".repeat(INDENT + 1));
        }
        System.out.print("\n");
    }
}
