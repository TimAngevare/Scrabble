package nl.utwente.angevarevandenbrink.scrabble.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TileBag {
    private ArrayList<Tile> tileBag = new ArrayList<>();

    private HashMap<Character, Integer> AMOUNTS = new HashMap<>();

    public TileBag() {
        AMOUNTS.put('a', 9);
        AMOUNTS.put('b', 2);
        AMOUNTS.put('c', 2);
        AMOUNTS.put('d', 4);
        AMOUNTS.put('e', 12);
        AMOUNTS.put('f', 2);
        AMOUNTS.put('g', 2);
        AMOUNTS.put('h', 2);
        AMOUNTS.put('i', 8);
        AMOUNTS.put('j', 2);
        AMOUNTS.put('k', 2);
        AMOUNTS.put('l', 4);
        AMOUNTS.put('m', 2);
        AMOUNTS.put('n', 6);
        AMOUNTS.put('o', 8);
        AMOUNTS.put('p', 2);
        AMOUNTS.put('q', 1);
        AMOUNTS.put('r', 6);
        AMOUNTS.put('s', 4);
        AMOUNTS.put('t', 6);
        AMOUNTS.put('u', 4);
        AMOUNTS.put('v', 2);
        AMOUNTS.put('w', 2);
        AMOUNTS.put('x', 1);
        AMOUNTS.put('y', 2);
        AMOUNTS.put('z', 1);
        AMOUNTS.put(' ', 2);

        for(Map.Entry<Character, Integer> entry : AMOUNTS.entrySet()) {
            for (int i = 0; i < entry.getValue(); i ++) {
                tileBag.add(new Tile(entry.getKey()));
            }
        }

        shuffleTileBag();
    }

    public void shuffleTileBag() {
        Collections.shuffle(tileBag);
    }

    public int getSize() {
        return tileBag.size();
    }

    /**
     * Retuns a visual representation of all letters inside the tile bag
     * @return a string of letters inside the bag
     */
    public String toString() {
        String result = "Tile bag: ";
        for (Tile tile : tileBag) {
            result += tile.getLetter();
        }
        result += " - " + String.valueOf(getSize()) + " tiles";
        return result;
    }

    /**
     * Takes out first tile, removes that tile from bag, returns that tile
     * @return the tile that has been taken out
     */
    public Tile takeOutTile() {
        Tile removedTile = tileBag.get(0);
        tileBag.remove(removedTile);
        return removedTile;
    }
}
