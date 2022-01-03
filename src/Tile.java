public class Tile {
    private SquareType type;
    private char letter;
    private int value;

    public Tile(SquareType type) {
        this.type = type;
        this.value = 0;
    }

    /**
     * Sets the correct letter and correct value to the properties
     * @param letter the letter to be filled in
     */
    public void setLetter(char letter) {
        this.letter = letter;
        int letterValue = Scrabble.getLetterValue(letter);

        if (type == SquareType.DOUBLE_LETTER) {
            value = letterValue * 2;
        } else if (type == SquareType.TRIPLE_LETTER) {
            value = letterValue * 3;
        } else {
            value = letterValue;
        }
    }

    public char getLetter() {
        return letter;
    }

    public int getValue() {
        return value;
    }

    public SquareType getType() {
        return type;
    }
}
