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

    public Bot(TileBag tileBag) {
        super("", tileBag);
        setName(getName());
        this.game = new Game();
    }

    public String getName(){
        return "Bot" + (int)Math.random()*150;
    }

    public void makeMove(Board board){
        int checkTrue = 0;
        int checkFalse = 0;
        HashMap<Position, ArrayList<Direction>> directionsForPosition = getDirection(board);
        Map<String, ScrabbleWordChecker.WordResponse> words = getWords();
        for (Position position : directionsForPosition.keySet()){
            for (Direction direction : directionsForPosition.get(position)){
                ArrayList<String> compatibleWords = getCompatibleWords(position, direction, words.keySet());
                for (String word : compatibleWords){
                    int usedIndex = 0;
                    if (direction == Direction.UP || direction == Direction.LEFT){
                        usedIndex = word.length() - 1;
                    }
                    try {
                        boolean wordCheck = checkWord(word, usedIndex);
                        if (wordCheck){
                            checkTrue++;
                            String directionWord = "V";
                            if (direction == Direction.RIGHT || direction == Direction.LEFT) {
                                directionWord = "H";
                            }
                            List<Integer> colRow = new ArrayList<>();
                            colRow.add(position.getCol());
                            colRow.add(position.getRow());
                            System.out.println("trying to place word");
                            game.placeWord(this, colRow, directionWord, word.toLowerCase());
                            System.out.println("wordplaced");
                            return;
                        } else {
                            checkFalse++;
                        }
                    } catch (IllegalMoveException e2){
                        e2.printStackTrace();
                        continue;
                    } catch (InvalidWordException e3) {
                        e3.printStackTrace();
                        continue;
                    }
                }
            }
        }
        System.out.println("true: " + checkTrue + "\nfalse: " + checkFalse);
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
        ArrayList<Position> positions = board.getPositions();
        HashMap<Position, ArrayList<Direction>> directionsForPosition = new HashMap<>();
        for (Position position : positions){
            ArrayList<Direction> directions = new ArrayList<>();
            char letter = position.getTile().getLetter();
            while (true){
                if (board.getPosition(position.getRow(), position.getCol() + 1).isEmpty() && !directions.contains(Direction.RIGHT)){
                    directions.add(Direction.RIGHT);
                } else if (board.getPosition(position.getRow(), position.getCol() - 1).isEmpty() && !directions.contains(Direction.LEFT)){
                    directions.add(Direction.LEFT);
                } else if (board.getPosition(position.getRow() + 1, position.getCol()).isEmpty() && !directions.contains(Direction.UP)){
                    directions.add(Direction.UP);
                } else if (board.getPosition(position.getRow() - 1, position.getCol()).isEmpty() && !directions.contains(Direction.DOWN)){
                    directions.add(Direction.DOWN);
                } else {
                    break;
                }
            }
            directionsForPosition.put(position, directions);
        }
        return directionsForPosition;
    }

}
