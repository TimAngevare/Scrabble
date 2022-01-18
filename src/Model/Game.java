package Model;

import java.util.ArrayList;
import WordChecker.InMemoryScrabbleWordChecker;

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

    public boolean isFinished(){
        return tilebag.getSize() == 0;
    }

    public void placeWord(Player player, String start, String direction, String word){
        if (board.isEmpty()){
            start = "8H";
        }
        boolean inDict = Scrabble.checkWord(word);
        String[] wordarr = word.split("");
        if (!inDict || !checkword(player, wordarr)){
            return;
        } else {
            try {
                char letter = start.charAt(start.length() - 1);
                int row = Integer.parseInt(start.substring(0, start.length() - 1));
                try {
                    placeTiles(letter, row, wordarr, direction);
                } catch (RuntimeException b){
                    b.printStackTrace();
                    return;
                }
            } catch (NumberFormatException e) {
                char letter = start.charAt(0);
                start = start.replace(Character.toString(letter), "");
                int row = Integer.parseInt(start.substring(0, start.length()));
                try {
                    placeTiles(letter, row, wordarr, direction);
                } catch (RuntimeException b){
                    b.printStackTrace();
                    return;
                }
            }
        }

        player.removeTiles(wordarr);
        player.fillTileRack(this.tilebag);
    }

    private void placeTiles(char letter, int row, String[] wordarr, String direction) throws RuntimeException{
        if (direction.equals("V")){
            for (int i = 0; i < wordarr.length; i++){
                Tile tile = new Tile(wordarr[i].charAt(0));
                Position position = board.getPosition((int)(letter) - 65, row - 1 + i);
                if (position.isEmpty()) {
                    position.placeTile(tile);
                } else {
                    throw new RuntimeException("Trying to place tile on tile");
                }
            }
        } else if (direction.equals("H")){
            for (int i = 0; i < wordarr.length; i++){
                Tile tile = new Tile(wordarr[i].charAt(0));
                Position position = board.getPosition((int)(letter) - 65 + i , row - 1);
                if (position.isEmpty()) {
                    position.placeTile(tile);
                } else {
                    throw new RuntimeException("Trying to Place tile on tile");
                }
            }
        }

    }

    private boolean checkword(Player player, String[] tiles){
        int counter = 0;
        ArrayList<Tile> tileRackCopy = player.copyTileRack();
        for (String letter : tiles){
            for (Tile tile : tileRackCopy){
                if (tile.getLetter() == letter.charAt(0)){
                   tileRackCopy.remove(tile);
                   counter++;
                   break;
                }
            }
        }
        return counter == tiles.length;
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
