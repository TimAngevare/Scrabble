package Model;

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

    public boolean checkWord(HashMap<String, ArrayList<Tile>> placedTiles, Boolean first){
        int countBlanks = this.amountTileLetter(' ');
        int blanksUsed = 0;
        ArrayList indexUsed = new ArrayList<Integer>();
        for (Tile placed : placedTiles.get("new")){
            for (int i = 0; i < tileRack.size(); i++){
                if (tileRack.get(i).equals(placed) && !indexUsed.contains(i)){
                    indexUsed.add(i);
                    break;
                }
                if (countBlanks > 0){
                    blanksUsed++;
                    countBlanks--;
                }
            }
        }
        if ((placedTiles.get("old").size() >= 1 || first) && (indexUsed.size() + blanksUsed) == placedTiles.size()){
            this.removeTiles(placedTiles.get("new"));
            return true;
        } else {
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
