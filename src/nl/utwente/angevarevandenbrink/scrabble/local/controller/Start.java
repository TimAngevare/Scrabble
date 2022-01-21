package nl.utwente.angevarevandenbrink.scrabble.local.controller;
import nl.utwente.angevarevandenbrink.scrabble.model.*;
import nl.utwente.angevarevandenbrink.scrabble.local.view.*;
import nl.utwente.angevarevandenbrink.scrabble.model.exception.IllegalMoveException;

import java.util.ArrayList;
import java.util.List;

public class Start {
    public Game game;
    public View view;

    private static final String TURNSEPERATOR = "---------------------------------------------------------------------";

    public static void main(String[] args) {
        new Start();
    }

    public Start() {
        this.view = new TUI();
        view.showMessage("Starting game!");
        game = new nl.utwente.angevarevandenbrink.scrabble.model.Game();
        this.fillGame();
        this.update();
    }

    private void fillGame() {
        while (true) {
            int numPlayers = view.getInt("With how many players do you wish to play? -");

            if (numPlayers >= 1 && numPlayers <= 4) {
                for (int i = 0; i < numPlayers; i++) {
                    String name = view.getString("Type the name of the next player: ");
                    game.addPlayer(new Player(name, game.getTilebag()));
                }
                break;
            } else {
                view.showMessage("That is not a valid amount of players: min. 2 max. 4");
            }
        }
    }

    public void update() {
        System.out.println(game.getTilebag().toString());

        while (!game.isFinished()){
            for (Player player : game.getPlayers()) {
                view.showMessage(TURNSEPERATOR);
                view.showMessage(player.toString());
                view.updateBoard(game.getBoard());

                String[] move = view.getMove();
                List<Integer> split = splitRowCol(move[0]);

                try {
                    game.placeWord(player, split, move[1], move[2]);
                } catch (IllegalMoveException e) {
                    view.showMessage(e.toString());
                }
            }
        }
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
