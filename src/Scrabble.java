import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scrabble {
    private HashMap tileBag;
    public static void main(String[] args) {
        System.out.println("Nothing happens but you did try playing!");
    }

    public Scrabble(){
        generateTiles();
    }

    public HashMap getTileBag(){
        return tileBag;
    }
    public void deleteTile(Tile tile ){
        // Wrm werkt dit niet?
        Integer quanitity = this.tileBag.get(tile.getLetter());
        tileBag.replace(tile.getLetter(), quanitity - 1);

    }
    private void generateTiles(){
        this.tileBag = new HashMap<Character, Integer>();
        tileBag.put('a', 9);
        tileBag.put('b', 2);
        tileBag.put('c', 2);
        tileBag.put('d', 4);
        tileBag.put('e', 12);
        tileBag.put('f', 2);
        tileBag.put('g', 2);
        tileBag.put('h', 2);
        tileBag.put('i', 8);
        tileBag.put('j', 2);
        tileBag.put('k', 2);
        tileBag.put('l', 4);
        tileBag.put('m', 2);
        tileBag.put('n', 6);
        tileBag.put('o', 8);
        tileBag.put('p', 2);
        tileBag.put('q', 1);
        tileBag.put('r', 6);
        tileBag.put('s', 4);
        tileBag.put('t', 6);
        tileBag.put('u', 4);
        tileBag.put('v', 2);
        tileBag.put('w', 2);
        tileBag.put('x', 1);
        tileBag.put('y', 2);
        tileBag.put('z', 1);
        tileBag.put(' ', 2);
    }
    /**
     * Returns the correct letter value according to official scrabble rules
     * @param letter the letter you want to know the value of
     * @return the numeric value of the given letter
     */
    public static int getLetterValue(char letter) {
        switch (letter) {
            case 'd':
            case 'g':
                return 2;
            case 'b':
            case 'c':
            case 'm':
            case 'p':
                return 3;
            case 'f':
            case 'h':
            case 'v':
            case 'w':
            case 'y' :
                return 4;
            case 'k':
                return 5;
            case 'x':
            case 'j':
                return 8;
            case 'q':
            case 'z':
                return 10;
            case ' ':
                return 0;
            default:
                return 1;
        }
    }
}
