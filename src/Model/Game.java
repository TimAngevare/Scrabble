package Model;

import java.util.*;

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

    public void placeWord(Player player, List<Integer> start, String direction, String word){
        Boolean first = board.isEmpty();
        HashMap<String, ArrayList<Tile>> placedTiles;
        Board boardCopy = board.cloneBoard();
        boolean inDict = Scrabble.checkWord(word);
        String[] wordarr = word.split("");
        if (!inDict){
            return;
        }
        int col = start.get(0);
        int row = start.get(1);
        placedTiles = placeTilesDir(col, row, wordarr, direction, boardCopy);
        Boolean pass = player.checkWord(placedTiles, first);
        if (pass){
            player.fillTileRack(this.tilebag);
            this.board = boardCopy;
        }
    }

    private HashMap<String, ArrayList<Tile>> placeTilesDir(int col, int row, String[] wordarr, String direction, Board boardCopy) {
        HashMap<String, ArrayList<Tile>> tilesPlaced = new HashMap<>();
        tilesPlaced.put("old", new ArrayList<Tile>());
        tilesPlaced.put("new", new ArrayList<Tile>());
        if (direction.toUpperCase(Locale.ROOT).equals("V")){
            for (int i = 0; i < wordarr.length; i++){
                placeTile(col, row - 1 + i, wordarr[i].charAt(0), boardCopy, tilesPlaced);

            }
        } else if (direction.toUpperCase(Locale.ROOT).equals("H")){
            for (int i = 0; i < wordarr.length; i++){
                placeTile(col + i, row - 1, wordarr[i].charAt(0), boardCopy, tilesPlaced);
            }
        }
        return tilesPlaced;
    }

    private void placeTile(int col, int row, char letter, Board boardCopy, HashMap<String, ArrayList<Tile>> tilesPlaced){
        Tile tile = new Tile(letter);
        Position position = boardCopy.getPosition(row, col);
        if (boardCopy.getPosition(col, row).isEmpty()) {
            position.placeTile(tile);
            tilesPlaced.get("new").add(tile);
        } else if (boardCopy.getPosition(col, row).getTile().getLetter() == letter){
            Tile old = boardCopy.getPosition(col, row).getTile();
            tilesPlaced.get("old").add(old);
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
