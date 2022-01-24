package nl.utwente.angevarevandenbrink.scrabble.model;

import java.util.ArrayList;
import java.util.List;

public class Move {
    private int col;
    private int row;
    private String direction;
    private String word;

    public Move(int row, int col, String direction, String word) {
        this.row = row;
        this.col = col;
        this.direction = direction;
        this.word = word;
    }

    public Move(List<Integer> colRow, String direction, String word) {
        this.col = colRow.get(0);
        this.row = colRow.get(1);
        this.direction = direction;
        this.word = word;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public String getDirection() {
        return direction;
    }

    public String getWord() {
        return word;
    }

    public List<Integer> getColRow() {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(this.col);
        result.add(this.row);
        return result;
    }
}
