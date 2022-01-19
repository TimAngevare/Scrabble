package nl.utwente.angevarevandenbrink.scrabble.model;

import nl.utwente.angevarevandenbrink.scrabble.WordChecker.InMemoryScrabbleWordChecker;
import nl.utwente.angevarevandenbrink.scrabble.WordChecker.ScrabbleWordChecker;

public class Scrabble {
    private static final InMemoryScrabbleWordChecker CHECKER = new InMemoryScrabbleWordChecker();

    /**
     * Checks if a word is valid using the given word checker
     * @param word the word to be tested
     * @return whether the word is a valid scrabble word
     */
    public static boolean checkWord(String word) {
        ScrabbleWordChecker.WordResponse response = CHECKER.isValidWord(word);
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
            case ' ':
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

    public static int getLetterMultiplier(SquareType st) {
        switch(st) {
            case DOUBLE_LETTER:
            case START:
                return 2;
            case TRIPLE_LETTER:
                return 3;
            default:
                return 1;
        }
    }

    public static int getWordMultiplier(SquareType st) {
        switch(st) {
            case DOUBLE_WORD:
                return 2;
            case TRIPLE_WORD:
                return 3;
            default:
                return 1;
        }
    }
}
