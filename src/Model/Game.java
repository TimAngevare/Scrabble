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

    public void placeWord(Player player, String start, String direction, String word){
        String[] wordarr = word.split("");
        try {
            char letter = start.charAt(0);
            int row = Integer.parseInt(start.substring(1, start.length()-1));
        } catch (UnsupportedOperationException e) {
            char letter = start.charAt(start.length()-1);
            int row = Integer.parseInt(start.substring(0, start.length()-2));
        }
        if (direction.equals("V")){
            for (int i = 0; i < wordarr.length; i++){

            }

        } else if (direction.equals("H")){

        }
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
