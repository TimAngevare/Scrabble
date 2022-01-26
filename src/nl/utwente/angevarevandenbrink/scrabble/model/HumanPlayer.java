package nl.utwente.angevarevandenbrink.scrabble.model;

import nl.utwente.angevarevandenbrink.scrabble.view.local.LocalTUI;
import nl.utwente.angevarevandenbrink.scrabble.view.local.LocalView;

import java.util.ArrayList;
import java.util.List;

public class HumanPlayer extends Player {
    private LocalView view;

    public HumanPlayer(String name, TileBag tileBag) {
        super(name, tileBag);
        this.view = new LocalTUI();
    }

    @Override
    public Move getMove() {
        do {
            String move = view.getString("Type start square (12A) followed by (H)orizontal or (V)ertical and finally the word you want to place\nTo pass and replace all tiles type: -");
            String[] moveArr = move.split(" ");

            if (moveArr[0].equals("-")){
                return new Move(0, 0, "H", "-");
            } else if (moveArr.length == 3 && (moveArr[1].equalsIgnoreCase("V") || moveArr[1].equalsIgnoreCase("H"))) {
                for (int let = 65; let < 80; let++){
                    for (int row = 1; row < 16; row++){
                        if(moveArr[0].contains(Character.toString((char) let)) && moveArr[0].contains(Integer.toString(row))){
                            return new Move(splitRowCol(moveArr[0]), moveArr[1], moveArr[2]);
                        }
                    }
                }
            }

            view.showMessage("Invalid syntax, try again!");
        } while (true);
    }

    public List<Integer> splitRowCol(String start) {
        List<Integer> startSplit = new ArrayList<>();

        try {
            char letter = start.charAt(start.length() - 1);

            int col = ((int) letter - 65);
            int row = Integer.parseInt(start.substring(0, start.length() - 1));

            startSplit.add(col);
            startSplit.add(row - 1);
        } catch (NumberFormatException e) {
            char letter = start.charAt(0);

            int col = ((int) letter - 65);
            start = start.replace(String.valueOf(letter), "");
            int row = Integer.parseInt(start.substring(0, start.length()));

            startSplit.add(col);
            startSplit.add(row - 1);
        } finally {
            return startSplit;
        }
    }
}
