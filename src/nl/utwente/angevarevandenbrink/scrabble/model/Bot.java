package nl.utwente.angevarevandenbrink.scrabble.model;

import nl.utwente.angevarevandenbrink.scrabble.WordChecker.ScrabbleWordChecker;
import nl.utwente.angevarevandenbrink.scrabble.model.exception.IllegalBotMoveException;
import nl.utwente.angevarevandenbrink.scrabble.model.exception.IllegalMoveException;
import nl.utwente.angevarevandenbrink.scrabble.model.exception.InvalidWordException;

import java.util.*;

import static nl.utwente.angevarevandenbrink.scrabble.model.Scrabble.getWords;

public class Bot extends Player {

    public enum Direction {DOWN, UP, LEFT , RIGHT}
    private Game game;

    public Bot(Game game, int botNum) {
        super("Bot " + botNum, game.getTilebag());

        this.game = game;
    }

    @Override
    public Move getMove(){
        int checkTrue = 0;
        int checkFalse = 0;

        HashMap<Position, ArrayList<Direction>> directionsForPosition = getDirection(game.getBoard());
        Map<String, ScrabbleWordChecker.WordResponse> words = getWords();

        HashMap<Move, Integer> options = new HashMap<>();

        for (Position position : directionsForPosition.keySet()){
            for (Direction direction : directionsForPosition.get(position)){
                ArrayList<String> compatibleWords = getCompatibleWords(position, direction, words.keySet());

                for (String word : compatibleWords){
                    int usedIndex = 0;
                    if (direction == Direction.UP || direction == Direction.LEFT){
                        usedIndex = word.length() - 1;
                    }

                    word = word.toLowerCase(); //Gewoon zekerheid

                    try {
                        boolean wordCheck = checkWord(word, usedIndex);

                        if (wordCheck){
                            checkTrue++;
                            String directionWord = "V";
                            if (direction == Direction.RIGHT || direction == Direction.LEFT) {
                                directionWord = "H";
                            }

                            int theCol = position.getCol();
                            int theRow = position.getRow();

                            if (direction == Direction.LEFT) {
                                theCol -= word.length() - 1;
                            } else if (direction == Direction.UP) {
                                theRow -= word.length() - 1;
                            }

                            int score = 1;
                            options.put(new Move(theRow, theCol, directionWord, word.toLowerCase()), score);
                        } else {
                            checkFalse++;
                        }
                    } catch (IllegalMoveException e){
                        e.printStackTrace();
                    }
                }
            }
        }

        if (options.size() == 0) {
            System.out.println("initial: true: " + checkTrue + "\nfalse: " + checkFalse);
            return new Move(0, 0, "H", "-");
        } else {
            Map.Entry<Move, Integer> maxEntry = null;
            for (Map.Entry<Move, Integer> entry : options.entrySet()) {
                if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                    maxEntry = entry;
                }
            }

            System.out.println(getName() + " lays the word " + maxEntry.getKey().getWord() + "!");
            return maxEntry.getKey();
        }
    }

    public ArrayList<String> getCompatibleWords(Position position, Direction direction, Set<String> words){
        ArrayList<String> compatibleWords = new ArrayList<>();
        for (String word : words){
            if (direction == Direction.RIGHT || direction == Direction.DOWN){
                if(word.toLowerCase().charAt(0) == position.getTile().getLetter() && word.length() < 6){
                    compatibleWords.add(word);
                }
            } else {
                if(word.toLowerCase().charAt(word.length() - 1) == position.getTile().getLetter() && word.length() < 6){
                    compatibleWords.add(word);
                }
            }
        }
        return compatibleWords;
    }


    public HashMap<Position, ArrayList<Direction>> getDirection(Board board){
        ArrayList<Position> positions = board.getNotEmptyPositions();
        HashMap<Position, ArrayList<Direction>> directionsForPosition = new HashMap<>();

        for (Position position : positions){
            ArrayList<Direction> directions = new ArrayList<>();

            if (board.getPosition(position.getRow(), position.getCol() + 1).isEmpty()) { directions.add(Direction.RIGHT); }
            if (board.getPosition(position.getRow(), position.getCol() - 1).isEmpty()) { directions.add(Direction.LEFT);}
            if (board.getPosition(position.getRow() + 1, position.getCol()).isEmpty()) { directions.add(Direction.UP);}
            if (board.getPosition(position.getRow() - 1, position.getCol()).isEmpty()) { directions.add(Direction.DOWN);}

            directionsForPosition.put(position, directions);
        }

        return directionsForPosition;
    }
}
