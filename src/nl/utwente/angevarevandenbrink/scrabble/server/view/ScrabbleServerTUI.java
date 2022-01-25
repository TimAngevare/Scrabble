package nl.utwente.angevarevandenbrink.scrabble.server.view;

import java.util.Arrays;
import java.util.Scanner;

public class ScrabbleServerTUI implements ScrabbleServerView {
    private Scanner sc;

    private static final String[] YES = {"yes", "y", "true", "1"};
    private static final String[] NO = {"no", "n", "false", "0"};

    public ScrabbleServerTUI() {
        this.sc = new Scanner(System.in);
    }

    @Override
    public void showMessage(String msg) {
        System.out.println(msg);
    }

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
}
