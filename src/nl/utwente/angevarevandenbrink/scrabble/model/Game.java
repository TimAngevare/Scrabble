package nl.utwente.angevarevandenbrink.scrabble.model;

import nl.utwente.angevarevandenbrink.scrabble.model.exception.IllegalMoveException;
import nl.utwente.angevarevandenbrink.scrabble.model.exception.InvalidWordException;

import java.util.*;

public class Game {
    private TileBag tilebag;
    private ArrayList<Player> players = new ArrayList<>();
    private Board board;
    private ArrayList<Board> previousBoards = new ArrayList<>();


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

    private Game(TileBag tileBag, Board board) {
        this.tilebag = tileBag;
        this.board = board;
    }

    public Game cloneGame() {
        return new Game(tilebag.cloneTileBag(), board.cloneBoard());
    }

    /**
     * Checks if the tile bag is empty
     * @return if the tile bag is empty
     */
    public boolean isFinished(){
        ArrayList<Integer> tileRackSizes = new ArrayList<>();
        for (Player player : players){
            tileRackSizes.add(player.getTileRack().size());
        }
        return (tilebag.getSize() == 0 && tileRackSizes.contains(0)) || board.equals(previousBoards.get(0));
    }


    /**
     * makes a copy of the board and places the players word if all checks are valid.
     * @param player
     * @param start the row and col of the first letter
     * @param direction either (V)ertical or (H)orizontal
     * @param word the word the player wants to play
     * @throws IllegalMoveException
     */
    public void placeWord(Player player, List<Integer> start, String direction, String word) throws IllegalMoveException, InvalidWordException {
        boolean first = board.isEmpty();
        Board boardCopy = board.cloneBoard();

        if (Scrabble.checkWord(word)) {
            String[] wordarr = word.split("");

            int col = start.get(0);
            int row = start.get(1);

            HashMap<String, ArrayList<TilePlacement>> placedTiles = placeTilesDir(col, row, wordarr, direction, boardCopy, first);

            boolean checkFullWord = boardCopy.checkFullWordsValid(placedTiles.get("new"), direction);
            if (!checkFullWord) {
                throw new InvalidWordException("That placement makes an invalid word on the board");
            }

            boolean pass = player.checkWord(placedTiles, first);

            if (pass){
                player.fillTileRack(this.tilebag);

                int score = calculateScore(board, placedTiles);
                player.addScore(score);

                setCopyBoard(boardCopy);
            }
        } else {
            throw new InvalidWordException("That is not a valid word");
        }

    }

    public void setCopyBoard (Board boardCopy){
        if (this.previousBoards.size() > 2){
            previousBoards.remove(0);
            previousBoards.add(board);
        } else {
            previousBoards.add(board);
        }
        this.board = boardCopy;
    }

    public void placeWord(Player player, Move move) throws IllegalMoveException, InvalidWordException {
        placeWord(player, move.getColRow(), move.getDirection(), move.getWord());
    }

    private HashMap<String, ArrayList<TilePlacement>> placeTilesDir(int col, int row, String[] wordarr, String direction, Board boardCopy, boolean first) throws IllegalMoveException {
        HashMap<String, ArrayList<TilePlacement>> tilesPlaced = new HashMap<>();
        boolean checkOnCenter = false;

        tilesPlaced.put("old", new ArrayList<>());
        tilesPlaced.put("new", new ArrayList<>());

        if (direction.equalsIgnoreCase("V")){
            for (int i = 0; i < wordarr.length; i++){
                if (!Board.isInBounds(col) || !Board.isInBounds(row + i)) {
                    throw new IllegalMoveException("Word comes out of bounds");
                }

                placeTile(col, row + i, wordarr[i].charAt(0), boardCopy, tilesPlaced);

                if(col == 7 && row + i == 7){
                    checkOnCenter = true;
                }
            }
        } else if (direction.equalsIgnoreCase("H")){
            for (int i = 0; i < wordarr.length; i++){
                if (!Board.isInBounds(col + i) || !Board.isInBounds(row)) {
                    throw new IllegalMoveException("Word comes out of bounds");
                }

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

    private void placeTile(int col, int row, char letter, Board boardCopy, HashMap<String, ArrayList<TilePlacement>> tilesPlaced) throws IllegalMoveException {
        Tile tile = new Tile(letter);
        Position position = boardCopy.getPosition(row, col);

        if (position.isEmpty()) {
            position.placeTile(tile);
            tilesPlaced.get("new").add(new TilePlacement(position, tile));

        } else if (position.getTile().getLetter() == letter){
            Tile old = position.getTile();
            tilesPlaced.get("old").add(new TilePlacement(position, old));

        } else {
            throw new IllegalMoveException("Word blocked by letter on board");
        }
    }


    /**
     * Adds a player to the game
     * @param player the player to be added
     */
    public void addPlayer(Player player) {
        if (player == null || player.getName().equals("")) {
            System.out.println("Player is null!! :(");
        }
        players.add(player);

        if (!player.getName().equals("tester")) {
            System.out.println("Player " + player.getName() + " has been added to the game!");
        }

    }

    public TileBag getTilebag() {
        return tilebag;
    }

    private int checkHorizontalScore(Board oldBoard, TilePlacement tp, ArrayList<TilePlacement> oldTiles) {
        int row = tp.getPosition().getRow();
        int col = tp.getPosition().getCol();

        int score = 0;

        for (int inL = col - 1; inL >= 0 && !oldBoard.isEmptyField(row, inL) && !checkRowColInTPAL(row, inL, oldTiles); inL--) {
            score += oldBoard.getPosition(row, inL).getTile().getValue();
        }


        for (int inR = col + 1; inR < Board.LENGTH && !oldBoard.isEmptyField(row, inR) && !checkRowColInTPAL(row, inR, oldTiles); inR++) {
            score += oldBoard.getPosition(row, inR).getTile().getValue();
        }

        //Add value of the letter
        if (score > 0) {
            score += (tp.getTile().getValue() * Scrabble.getLetterMultiplier(tp.getPosition().getType()));
        }

        return score;
    }

    private int checkVerticalScore(Board oldBoard, TilePlacement tp, ArrayList<TilePlacement> oldTiles) {
        int row = tp.getPosition().getRow();
        int col = tp.getPosition().getCol();

        int score = 0;

        for (int inT = row - 1; inT >= 0 && !oldBoard.isEmptyField(inT, col) && !checkRowColInTPAL(inT, col, oldTiles); inT--) {
            score += oldBoard.getPosition(inT, col).getTile().getValue();
        }


        for (int inB = row + 1; inB < Board.LENGTH && !oldBoard.isEmptyField(inB, col) && !checkRowColInTPAL(inB, col, oldTiles); inB++) {
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
    private int checkSurroundings(Board oldBoard, TilePlacement tp, ArrayList<TilePlacement> oldTiles) {
        int wMulti = Scrabble.getWordMultiplier(tp.getPosition().getType());

        int scoreToAdd = checkHorizontalScore(oldBoard, tp, oldTiles) * wMulti;
        scoreToAdd += checkVerticalScore(oldBoard, tp, oldTiles) * wMulti;

        return scoreToAdd;
    }

    /**
     * Checks the score of a turn
     * @param oldBoard the board without the new word being placed
     * @param tilesPlaced the moves that are going to be made
     * @return the score of the move
     */
    public int calculateScore(Board oldBoard, HashMap<String, ArrayList<TilePlacement>> tilesPlaced) {
        int score = 0;
        int wordMultiplier = 1;

        ArrayList<TilePlacement> newTiles = tilesPlaced.get("new");
        ArrayList<TilePlacement> oldTiles = tilesPlaced.get("old");

        for (TilePlacement plac: newTiles) {
            wordMultiplier *= Scrabble.getWordMultiplier(plac.getPosition().getType());

            score += (plac.getTile().getValue() * Scrabble.getLetterMultiplier(plac.getPosition().getType()));
        }

        for (TilePlacement oldTile : oldTiles) {
            score += oldTile.getTile().getValue();
        }

        score *= wordMultiplier;

        //Check surroundings
        for (TilePlacement tp : newTiles) {
            score += checkSurroundings(oldBoard, tp, oldTiles);
        }

        if (newTiles.size() == 7) {
            score += 50;
        }

        return score;
    }

    private boolean checkRowColInTPAL(int row, int col, ArrayList<TilePlacement> tpal) {
        for (TilePlacement tp : tpal) {
            int tpRow = tp.getPosition().getRow();
            int tpCol = tp.getPosition().getCol();

            if (row == tpRow && col == tpCol) {
                return true;
            }
        }
        return false;
    }

    public void setFinalScores() {
        return;
    }

}