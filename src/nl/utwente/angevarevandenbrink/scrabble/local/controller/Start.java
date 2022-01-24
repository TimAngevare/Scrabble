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

    private static final String TURNSEPERATOR = "---------------------------------------------------------------------";

    public static void main(String[] args) {
        new Start();
    }

    public Start() {
        this.view = new TUI();
        view.showMessage("Starting game!");
        game = new Game();
        this.fillGame();
        this.update();
    }

    private void fillGame() {
        while (true) {
            int numPlayers = view.getInt("With how many players do you wish to play? -");
            int numBots = view.getInt("How many of those players are bots? -");

            numPlayers = numPlayers - numBots;

            if (numPlayers >= 1 && numPlayers <= 4) {
                for (int i = 0; i < numPlayers; i++) {
                    String name = view.getString("Type the name of the next player: ");
                    game.addPlayer(new HumanPlayer(name, game.getTilebag()));
                }

                for (int i = 1; i <= numBots; i++){
                    Bot newBot = new Bot(game, i);
                    game.addPlayer(newBot);
                }

                break;
            } else {
                view.showError("That is not a valid amount of players: min. 2 max. 4");
            }


        }
    }

    public void update() {
        System.out.println(game.getTilebag().toString());

        while (!game.isFinished()){

            for (Player player : game.getPlayers()) {
                view.showMessage(TURNSEPERATOR);
                view.showPlayerSummary(game.getPlayers());

                if (!(player instanceof Bot)) {
                    view.showTileRack(player);
                    view.updateBoard(game.getBoard());
                }

                Move move = player.getMove();

                if (move.getWord().equals("-")){
                    player.newTiles(game.getTilebag());
                } else {
                    try {
                        game.placeWord(player, move.getColRow(), move.getDirection(), move.getWord());
                    } catch (IllegalMoveException e) {
                        view.showError(e.toString());
                    } catch (InvalidWordException e){
                        view.showError(e.toString());
                    }
                }
            }
        }
    }
}
