package nl.utwente.angevarevandenbrink.scrabble.local.controller;
import nl.utwente.angevarevandenbrink.scrabble.model.*;
import nl.utwente.angevarevandenbrink.scrabble.local.view.*;
import nl.utwente.angevarevandenbrink.scrabble.model.exception.IllegalMoveException;
import nl.utwente.angevarevandenbrink.scrabble.model.exception.InvalidWordException;

import java.util.ArrayList;
import java.util.List;

public class Start {
    private Game game;
    private View view;
    private ArrayList<Bot> bots;

    private static final String TURNSEPERATOR = "---------------------------------------------------------------------";

    public static void main(String[] args) {
        new Start();
    }

    public Start() {
        this.view = new TUI();
        view.showMessage("Starting game!");
        game = new Game();
        this.bots = new ArrayList<>();
        this.fillGame();
        this.update();
    }

    private void fillGame() {
        while (true) {
            int numPlayers = view.getInt("With how many players do you wish to play? -");
            int numBots = view.getInt("How many of those players are bots? -");
            numPlayers = numPlayers - numBots;
            genBots(numBots);
            if (numPlayers >= 1 && numPlayers <= 4) {
                for (int i = 0; i < numPlayers; i++) {
                    String name = view.getString("Type the name of the next player: ");
                    game.addPlayer(new Player(name, game.getTilebag()));
                }
                break;
            } else {
                view.showError("That is not a valid amount of players: min. 2 max. 4");
            }
        }
    }

    private void genBots(int numBots){
        for (int i = 0; i < numBots; i++){
            this.bots.add(new Bot(game.getTilebag()));
        }
    }

    public void update() {
        System.out.println(game.getTilebag().toString());

        while (!game.isFinished()){
            for (Player player : game.getPlayers()) {
                view.showMessage(TURNSEPERATOR);
                view.showPlayerSummary(game.getPlayers());
                view.showTileRack(player);
                view.updateBoard(game.getBoard());

                String[] move = view.getMove();
                List<Integer> split = splitRowCol(move[0]);

                if (move[0].equals("-")){
                    player.newTiles(game.getTilebag());
                } else {
                    try {
                        game.placeWord(player, split, move[1], move[2]);
                    } catch (IllegalMoveException e) {
                        view.showError(e.toString());
                    } catch (InvalidWordException e){
                        view.showError(e.toString());
                    }
                }
            }
            for (Bot bot : bots){
                bot.makeMove(game.getBoard());
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
