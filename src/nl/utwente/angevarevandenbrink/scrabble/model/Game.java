package nl.utwente.angevarevandenbrink.scrabble.model;

import nl.utwente.angevarevandenbrink.scrabble.model.exception.IllegalMoveException;

import java.util.*;

public class Game {
    private final String VERTICAL = "V";
    private final String HORIZONTAL = "H";
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

    /**
     * Checks if the tile bag is empty
     * @return if the tile bag is empty
     */
    public boolean isFinished(){
        return tilebag.getSize() == 0;
    }


    /**
     * makes a copy of the board and places the players word if all checks are valid.
     * @param player
     * @param start the row and col of the first letter
     * @param direction either (V)ertical or (H)orizontal
     * @param word the word the player wants to play
     * @throws IllegalMoveException
     */
    public void placeWord(Player player, List<Integer> start, String direction, String word) throws IllegalMoveException {
        Boolean first = board.isEmpty();
        HashMap<String, ArrayList<Tile>> placedTiles;
        Board boardCopy = board.cloneBoard();
        boolean inDict = Scrabble.checkWord(word);
        String[] wordarr = word.split("");
        if (!inDict){
            throw new IllegalMoveException("Word not in dictionary");
        }
        int col = start.get(0);
        int row = start.get(1);
        placedTiles = placeTilesDir(col, row, wordarr, direction, boardCopy, first);
        Boolean pass = player.checkWord(placedTiles, first);
        if (pass){
            player.fillTileRack(this.tilebag);
            this.board = boardCopy;
        }
    }

    private HashMap<String, ArrayList<Tile>> placeTilesDir(int col, int row, String[] wordarr, String direction, Board boardCopy, Boolean first) throws IllegalMoveException {
        HashMap<String, ArrayList<Tile>> tilesPlaced = new HashMap<>();
        Boolean checkOnCenter = false;
        tilesPlaced.put("old", new ArrayList<Tile>());
        tilesPlaced.put("new", new ArrayList<Tile>());
        if (direction.toUpperCase().equals(VERTICAL)){
            for (int i = 0; i < wordarr.length; i++){
                placeTile(col, row + i, wordarr[i].charAt(0), boardCopy, tilesPlaced);
                if(col == 7 && row + i == 7){
                    checkOnCenter = true;
                }
            }
        } else if (direction.toUpperCase().equals(HORIZONTAL)){
            for (int i = 0; i < wordarr.length; i++){
                placeTile(col + i, row, wordarr[i].charAt(0), boardCopy, tilesPlaced);
                if(col + i == 7 && row == 7){
                    checkOnCenter = true;
                }
            }
        }
        if (first && !checkOnCenter){
            throw new IllegalMoveException("First move not on center board.");
        }
        return tilesPlaced;
    }

    private void placeTile(int col, int row, char letter, Board boardCopy, HashMap<String, ArrayList<Tile>> tilesPlaced) throws IllegalMoveException {
        Tile tile = new Tile(letter);
        Position position = boardCopy.getPosition(row, col);
        if (position.isEmpty()) {
            position.placeTile(tile);
            tilesPlaced.get("new").add(tile);
        } else if (position.getTile().getLetter() == letter){
            Tile old = boardCopy.getPosition(row, col).getTile();
            tilesPlaced.get("old").add(old);
        } else {
            System.out.println(letter + " : " + position.getTile().getLetter());
            throw new IllegalMoveException("Word blocked by letter on board");
        }
    }


    /**
     * Adds a player to the game
     * @param player the player to be added
     */
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

    private int checkHorizontalScore(Board oldBoard, TilePlacement tp) {
        int row = tp.getPosition().getRow();
        int col = tp.getPosition().getCol();

        int score = 0;

        for (int inL = col - 1; inL >= 0 && !oldBoard.isEmptyField(row, inL); inL--) {
            score += oldBoard.getPosition(row, inL).getTile().getValue();
        }


        for (int inR = col + 1; inR < Board.LENGTH && !oldBoard.isEmptyField(row, inR); inR++) {
            score += oldBoard.getPosition(row, inR).getTile().getValue();
        }

        //Add value of the letter
        if (score > 0) {
            score += (tp.getTile().getValue() * Scrabble.getLetterMultiplier(tp.getPosition().getType()));
        }

        return score;
    }

    private int checkVerticalScore(Board oldBoard, TilePlacement tp) {
        int row = tp.getPosition().getRow();
        int col = tp.getPosition().getCol();

        int score = 0;

        for (int inT = row - 1; inT >= 0 && !oldBoard.isEmptyField(inT, col); inT--) {
            score += oldBoard.getPosition(inT, col).getTile().getValue();
        }


        for (int inB = row + 1; inB < Board.LENGTH && !oldBoard.isEmptyField(inB, col); inB++) {
            score += oldBoard.getPosition(inB, col).getTile().getValue();
        }

        //Add value of the letter
        if (score > 0) {
            score += (tp.getTile().getValue() * Scrabble.getLetterMultiplier(tp.getPosition().getType()));
        }

        return score;
    }

    /**
     * Checks the score of the words surrounding a new position
     * @param oldBoard the board without the new word being placed
     * @param tp the tile and position of the new move
     * @return the score of the words surrounding
     */
    private int checkSurroundings(Board oldBoard, TilePlacement tp) {
        int wMulti = Scrabble.getWordMultiplier(tp.getPosition().getType());

        int scoreToAdd = checkHorizontalScore(oldBoard, tp) * wMulti;
        scoreToAdd += checkVerticalScore(oldBoard, tp) * wMulti;

        return scoreToAdd;
    }

    /**
     * Checks the score of a turn
     * @param oldBoard the board without the new word being placed
     * @param newTiles the moves that are going to be made
     * @param oldTiles the letters which partake in the new word but which are already on the board
     * @return the score of the move
     */
    public int calculateScore(Board oldBoard, TilePlacement[] newTiles, Position[] oldTiles) {
        int score = 0;
        int wordMultiplier = 1;

        for (TilePlacement plac: newTiles) {
            wordMultiplier *= Scrabble.getWordMultiplier(plac.getPosition().getType());

            score += (plac.getTile().getValue() * Scrabble.getLetterMultiplier(plac.getPosition().getType()));
        }

        for (Position oldTile : oldTiles) {
            score += oldTile.getTile().getValue();
        }

        score *= wordMultiplier;

        //Check surroundings
        for (TilePlacement tp : newTiles) {
            score += checkSurroundings(oldBoard, tp);
        }

        return score;
    }


}
