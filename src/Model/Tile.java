package Model;

import java.util.Objects;

public class Tile {
    private char letter;
    private int value;

    public Tile(char letter) {
        this.letter = letter;
        this.value = Scrabble.getLetterValue(this.letter);
    }

    public char getLetter() {
        return letter;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return letter == tile.letter && value == tile.value;
    }
}
