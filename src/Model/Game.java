package Model;

import java.util.ArrayList;

public class Game {
    private TileBag tilebag;
    private ArrayList<Player> players = new ArrayList<>();
    private Board board;

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Board getBoard() {
        return board;
    }

    public Game() {
        this.tilebag = new TileBag();
        this.board = new Board();
    }

    public void addPlayer(Player player) {
        if (player == null && player.getName().equals("")) {
            System.out.println("speler is nul!! :(");
        }
        players.add(player);
        System.out.println("Model.Player " + player.getName() + " has been added to the game!");
    }

    public TileBag getTilebag() {
        return tilebag;
    }

}
