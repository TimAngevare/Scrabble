package nl.utwente.angevarevandenbrink.scrabble.model;

import nl.utwente.angevarevandenbrink.scrabble.WordChecker.ScrabbleWordChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static nl.utwente.angevarevandenbrink.scrabble.model.Scrabble.getWords;

public class Bot extends Player {

    public enum Direction {DOWN, UP, LEFT , RIGHT}

    public Bot(String name, TileBag tileBag) {
        super("", tileBag);
        setName(getName());
    }

    public String getName(){
        return "Bot" + (int)Math.random()*150;
    }

    public void makeMove(Board board){
        HashMap<Position, Direction> directions = getDirection(board);
        Map<String, ScrabbleWordChecker.WordResponse> words = getWords();
        for (Position position : directions.keySet()){
            ArrayList<String> compatibleWords = getCompatibleWords(position, directions.get(position), words.keySet());

        }
    }

    public ArrayList<String> getCompatibleWords(Position position, Direction direction, Set<String> words){
        ArrayList<String> compatibleWords = new ArrayList<>();
        for (String word : words){
            if (direction == Direction.RIGHT || direction == Direction.DOWN){
                if(word.charAt(0) == position.getTile().getLetter()){
                    compatibleWords.add(word);
                }
            } else {
                if(word.charAt(word.length() - 1) == position.getTile().getLetter()){
                    compatibleWords.add(word);
                }
            }
        }
        return compatibleWords;
    }

    public boolean wordInRack(String word, int used){
        for (int i = 0; i < word.length(); i++){
            if (i == used) {
                continue;
            }
            // implement wordchecker in player
        }
        return false;
    }

    public HashMap<Position, Direction> getDirection(Board board){
        ArrayList<Position> positions = board.getPositions();
        HashMap<Position, Direction> directions = new HashMap<>();
        for (Position position : positions){
            char letter = position.getTile().getLetter();
            if (board.getPosition(position.getRow(), position.getCol() + 1).isEmpty()){
                directions.put(position, Direction.RIGHT);
            } else if (board.getPosition(position.getRow(), position.getCol() - 1).isEmpty()){
                directions.put(position, Direction.LEFT);
            } else if (board.getPosition(position.getRow() + 1, position.getCol()).isEmpty()){
                directions.put(position, Direction.UP);
            } else if (board.getPosition(position.getRow() - 1, position.getCol()).isEmpty()){
                directions.put(position, Direction.DOWN);
            }
        }
        return directions;
    }

}
