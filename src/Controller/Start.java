package Controller;
import Model.*;
import view.*;

import java.util.Scanner;

public class Start {
    public Game game;
    public View view;

    public static void main(String[] args) {
        new Start();
    }

    public Start() {
        this.view = new TUI();
        view.showMessage("Starting game!");
        game = new Model.Game();
        fillGame();
        this.update();
    }

    private void fillGame() {
        int numPlayers = view.getInt("With how many players do you wish to play? -");

        for (int i = 0; i < numPlayers; i++) {
            String name = view.getString("Type the name of the next player: ");
            game.addPlayer(new Player(name, game.getTilebag()));
        }
    }

    public void update() {
        System.out.println(game.getTilebag().toString());
        System.out.println("Total length tilebag: " + game.getTilebag().getSize());

        for (Player player : game.getPlayers()) {
            System.out.println(player.toString());
            view.updateBoard(game.getBoard());
            String[] move = view.getMove();
        }
    }
}
