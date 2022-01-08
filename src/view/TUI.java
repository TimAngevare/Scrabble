package view;

import Model.Board;
import Model.Position;
import Model.SquareType;

public class TUI implements ANSI{
    static final int magnif = 4;
    static final int boardSize = 15;
    static final int indent = 3;

    public void start() {
        System.out.println("Starting game!");
        System.out.println("With how many players do you wish to play? -");
    }
    public void drawBoard(Board board) {
        this.drawLetters();
        this.drawTop();
        for (int i = 1; i <= boardSize; i++){
            drawRowBoard(i, board);
        }
    }
    private void drawRowBoard(int row, Board board){
        if (row >= 10){
            System.out.print(row + " ".repeat(indent-2));
        } else {
            System.out.print(row + " ".repeat(indent-1));
        }
        for (int square = 0; square < boardSize; square++){
            Position position = board.getPosition(square, row - 1);
            String color = checkColor(position.getType());
            System.out.print("│");
            if (position.isEmpty()){
                System.out.print(color + " ".repeat(magnif) + RESET);
            } else {
                System.out.print(color + position.getTile().getLetter() + " ".repeat(magnif - 1) + RESET);
            }
        }
        System.out.print("│\n");

        if (row != boardSize){
            drawTop();
        } else {
            drawBottom();
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
        System.out.print(" ".repeat(indent) + "┌");
        for (int i = 1; i <= boardSize * magnif; i++){
            System.out.print("─");
            if (i % magnif == 0){
                System.out.print("┐");
            }
        }
        System.out.print("\n");

    }
    private void drawBottom(){

        System.out.print(" ".repeat(indent) +"└");
        for (int i = 1; i <= boardSize * magnif; i++){
            System.out.print("─");
            if (i % magnif == 0){
                System.out.print("┘");
            }
        }
        System.out.print("\n");
    }
    private void drawLetters(){
        int a = 65;
        System.out.print(" ".repeat(indent + magnif / 2));
        for (int i = a; i < a + boardSize; i++){
            System.out.print((char) i + " ".repeat(indent + 1));
        }
        System.out.print("\n");
    }
}
