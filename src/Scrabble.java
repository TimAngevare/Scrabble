public class Scrabble {
    public static void main(String[] args) {
        System.out.println("Nothing happens but you did try playing!");
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
            default:
                return 1;
        }
    }
}
