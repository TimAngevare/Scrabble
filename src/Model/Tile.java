package Model;

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
}
