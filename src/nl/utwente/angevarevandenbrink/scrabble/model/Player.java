package nl.utwente.angevarevandenbrink.scrabble.model;

import nl.utwente.angevarevandenbrink.scrabble.controller.remote.protocol.ProtocolMessages;
import nl.utwente.angevarevandenbrink.scrabble.exception.IllegalBotMoveException;
import nl.utwente.angevarevandenbrink.scrabble.exception.IllegalMoveException;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Player {
    private String name;
    private int score;
    private ArrayList<Tile> tileRack = new ArrayList<>();

    public ArrayList<Tile> cloneTileRack(){
        ArrayList<Tile> tileRackCopy = new ArrayList<>();
        for (Tile tile : this.tileRack){
            tileRackCopy.add(new Tile(tile.getLetter()));
        }
        return tileRackCopy;
    }

    public void setTileRack(ArrayList<Tile> tileRack) {
        this.tileRack = tileRack;
    }

    public void newTiles(TileBag tileBag){
        for (Tile tile : tileRack){
            tileBag.addTile(tile);
        }
        tileRack.clear();

        tileBag.shuffleTileBag();
        fillTileRack(tileBag);
    }

    public void setName(String name) {
        this.name = name;
    }

    public int amountTileLetter(char letter){
        int counter = 0;
        for (Tile tile : tileRack){
            if (tile.getLetter() == letter){
                counter++;
            }
        }
        return counter;
    }

    public Player(String name, TileBag tileBag) {
        this.name = name;
        this.score = 0;

        this.fillTileRack(tileBag);
    }

    /**
     * Fills tile rack to 7 tiles.
     * @param tileBag tile bag of game
     * @requires tileBag != null
     */
    public void fillTileRack(TileBag tileBag) {
        int toAdd = 7 - tileRack.size();
        for (int i = 0; i < toAdd; i++) {
            tileRack.add(tileBag.takeOutTile());
        }
    }

    /**
     * Removes tiles from tile rack.
     * @param removedTiles list of tiles to be removed
     * @requires removedTiles != null && removedTiles ! contains tiles not in tile rack
     * @ensures tiles in removedTiles are removed from tile rack
     */
    public void removeTiles(ArrayList<Tile> removedTiles){
        for (Tile tile: removedTiles){
            boolean removed = tileRack.remove(tile);

            if (!removed) {
                tileRack.remove(new Tile(' '));
            }
        }
    }

    public void removeTilePlacements(ArrayList<TilePlacement> remove){
        ArrayList<Tile> toRemoveTiles = new ArrayList<>();
        for (TilePlacement tp: remove){
            toRemoveTiles.add(tp.getTile());
        }
        removeTiles(toRemoveTiles);
    }

    public ArrayList<Tile> getTileRack() {
        return tileRack;
    }

    public String getStringTileRack() {
        String result = "";
        for (Tile tile : tileRack) {
            if (!result.equals("")) {
                result += ProtocolMessages.AS;
            }
            result += tile.getLetter();
        }

        return result;
    }

    public int getTileRackScore() {
        int result = 0;
        for (Tile tile : tileRack) {
            result += tile.getValue();
        }

        return result;
    }

    /**
     * Checks if word is in tilerack.
     * @param placedTiles Hashmap of used and players placed tiles
     * @param first true if first move on board
     * @return boolean word is legal
     * @throws IllegalMoveException
     * @requires placedTiles != null && first != null
     */
    public boolean checkWord(HashMap<String, ArrayList<TilePlacement>> placedTiles, boolean first) throws IllegalMoveException {
        int countBlanks = this.amountTileLetter(' ');
        int blanksUsed = 0;
        ArrayList<Integer> indexUsed = new ArrayList<>();

        for (TilePlacement plac : placedTiles.get("new")){
            Tile placed = plac.getTile();
            boolean found = false;

            for (int i = 0; i < tileRack.size(); i++) {
                if (tileRack.get(i).equals(placed) && !indexUsed.contains(i)) {
                    indexUsed.add(i);
                    found = true;
                    break;
                }
            }

            if (!found){
                if (countBlanks > 0) {
                    blanksUsed++;
                    countBlanks--;
                } else {
                    throw new IllegalMoveException("Tile not in tile rack");
                }
            }
        }

        if ((first || placedTiles.get("old").size() >= 1) && (indexUsed.size() + blanksUsed) == placedTiles.get("new").size()){
            //System.out.println("Successfully placed");
            this.removeTilePlacements(placedTiles.get("new"));
            return true;
        } else {
            //System.out.println("Placement failed");
            return false;

        }
    }

    /**
     * Checks if word is in tilerack.
     * @param word String word to place
     * @param used int index of letter used on board
     * @return boolean true if word is legal
     * @throws IllegalBotMoveException
     * requires used != null && word != null
     */
    public boolean checkWord(String word, int used) throws IllegalBotMoveException {
        int countBlanks = this.amountTileLetter(' ');
        ArrayList<Integer> indexUsed = new ArrayList<>();

        for (int i = 0; i < word.length(); i++){
            if (i == used){
                continue;
            }

            char letter = word.toLowerCase().charAt(i);
            boolean found = false;

            for (int b = 0; b < tileRack.size(); b++) {
                if (tileRack.get(b).getLetter() == letter && !indexUsed.contains(b)) {
                    indexUsed.add(b);
                    found = true;
                    break;
                }
            }

            if (!found){
                if (countBlanks > 0) {
                    countBlanks--;
                } else {
                    return false;
                }
            }
        }

        return true;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        String result = name + " (" + getScore() + " pt.) - ";
        for (Tile tile : tileRack) {
            result += tile.getLetter();
        }
        return result;
    }

    /**
     * Adds an integer to the players score.
     * @param newScore integer score to be added
     * @requires newScore != null && newScore >= 0
     */
    public void addScore(int newScore) {
        this.score += newScore;
    }

    /**
     * Substracts an integer of the players score.
     * @param amount integer score to be substracted
     * @requires amount != null && amount >= 0
     */
    public void removeScore(int amount) {
        this.score -= amount;
    }

    public abstract Move getMove();
}
