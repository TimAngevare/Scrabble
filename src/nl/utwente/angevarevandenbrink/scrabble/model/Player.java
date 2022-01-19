package nl.utwente.angevarevandenbrink.scrabble.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    private String name;
    private int score;
    private ArrayList<Tile> tileRack = new ArrayList<>();

    public ArrayList<Tile> copyTileRack(){
        ArrayList<Tile> tileRackCopy = new ArrayList<>();
        for (Tile tile : this.tileRack){
            tileRackCopy.add(tile);
        }
        return tileRackCopy;
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

    public void fillTileRack(TileBag tileBag) {
        int toAdd = 7 - tileRack.size();
        for (int i = 0; i < toAdd; i++) {
            tileRack.add(tileBag.takeOutTile());
        }
    }

    public void removeTiles(ArrayList<Tile> remove){
        for (Tile tile: remove){
            tileRack.remove(tile);
        }
    }

    public ArrayList<Tile> getTileRack() {
        return tileRack;
    }

    public boolean checkWord(HashMap<String, ArrayList<Tile>> placedTiles, Boolean first) throws IllegalMoveException {
        int countBlanks = this.amountTileLetter(' ');
        int blanksUsed = 0;
        ArrayList indexUsed = new ArrayList<Integer>();
        for (Tile placed : placedTiles.get("new")){
            Boolean found = false;
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
        if ((placedTiles.get("old").size() >= 1 || first) && (indexUsed.size() + blanksUsed) == placedTiles.get("new").size()){
            System.out.println("Succes");
            this.removeTiles(placedTiles.get("new"));
            return true;
        } else {
            System.out.println("fail");
            return false;

        }
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String toString() {
        String result = name + " -";
        for (Tile tile : tileRack) {
            result += tile.getLetter();
        }
        return result;
    }

    public void addScore(int newScore) {
        this.score += newScore;
    }
}