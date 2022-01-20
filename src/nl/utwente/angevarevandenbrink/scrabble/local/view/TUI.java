package nl.utwente.angevarevandenbrink.scrabble.local.view;

import nl.utwente.angevarevandenbrink.scrabble.model.Board;

import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class TUI implements View {
    BoardDraw boardDraw;
    Scanner sc;

    private static final String[] YES = {"yes", "y", "true", "1"};
    private static final String[] NO = {"no", "n", "false", "0"};

    public TUI(){
        this.boardDraw = new BoardDraw();
        this.sc = new Scanner(System.in);
    }
    public void updateBoard(Board board){
        boardDraw.drawBoard(board);
    }

    public void showMessage(String msg) {
        System.out.println(msg);
    }

    public void showError(String msg) { System.out.println(ANSI.RED_BOLD_BRIGHT + msg + ANSI.RESET);}

    private String getLine() {
        return sc.nextLine();
    }

    @Override
    public String getString(String msg) {
        showMessage(msg);
        return getLine();
    }

    @Override
    public int getInt(String msg) {
        showMessage(msg);
        return Integer.parseInt(getLine());
    }

    @Override
    public boolean getBoolean(String msg) {
        while (true) {
            showMessage(msg);
            String input = getLine();
            if (Arrays.asList(YES).contains(input.toLowerCase())) {
                return true;
            } else if (Arrays.asList(NO).contains(input.toLowerCase())) {
                return true;
            } else {
                showMessage("That is not a valid input, try again.");
            }
        }
    }

    @Override
    public String[] getMove() {
        do {
            String move = getString("Type start square (12A) followed by (H)orizontal or (V)ertical and finally the word you want to place\nTo pass and replace all tiles type: -");
            String[] moveArr = move.split(" ");

            if (moveArr.length == 3 && (moveArr[1].equalsIgnoreCase("V") || moveArr[1].equalsIgnoreCase("H"))) {
                for (int let = 65; let < 80; let++){
                    for (int row = 1; row < 16; row++){
                        if(moveArr[0].contains(Character.toString((char) let)) && moveArr[0].contains(Integer.toString(row))){
                            return moveArr;
                        }
                    }
                }
            } else if (moveArr[0].equals("-")){
                return moveArr;
            }

            showMessage("Invalid syntax, try again!");
        } while (true);
    }
}
