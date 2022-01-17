package Model;

import java.util.ArrayList;

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

    public void removeTiles(String[] tiles){
        ArrayList<Tile> remove = new ArrayList<>();
        for (String letter : tiles){
            for (Tile tile : tileRack){
                if (tile.getLetter() == letter.charAt(0)){
                    remove.add(tile);
                }
            }
        }
        for (Tile tile: remove){
            tileRack.remove(tile);
        }
    }

    public ArrayList<Tile> getTileRack() {
        return tileRack;
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
