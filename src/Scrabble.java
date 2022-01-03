public class Scrabble {
    public static void main(String[] args) {
        boolean ding = checkWord("hell");
    }

    private static InMemoryScrabbleWordChecker checker = new InMemoryScrabbleWordChecker();

    /**
     * Checks if a word is valid using the given word checker
     * @param word the word to be tested
     * @return whether the word is a valid scrabble word
     */
    public static boolean checkWord(String word) {
        ScrabbleWordChecker.WordResponse response = checker.isValidWord(word);
        try {
            System.out.println(response.toString());
            return true;
        } catch (NullPointerException e)  {
            System.out.println(word + " is not a valid word.");
            return false;
        }
    }

    /**
     * Returns the correct letter value according to official scrabble rules
     * @param letter the letter you want to know the value of
     * @return the numeric value of the given letter
     */
    public static int getLetterValue(char letter) {
        switch (letter) {
            case 0:
                return 0;
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
            case 'y':
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
