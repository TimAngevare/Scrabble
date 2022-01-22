package nl.utwente.angevarevandenbrink.scrabble.model;

import nl.utwente.angevarevandenbrink.scrabble.WordChecker.ScrabbleWordChecker;
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
        HashMap<Position, Direction> directions = getDirection(board);
        Map<String, ScrabbleWordChecker.WordResponse> words = getWords();
        for (Position position : directions.keySet()){
            ArrayList<String> compatibleWords = getCompatibleWords(position, directions.get(position), words.keySet());
            for (String word : compatibleWords){
                int usedIndex = 0;
                if (directions.get(position) == Direction.UP || directions.get(position) == Direction.LEFT){
                    usedIndex = word.length() - 1;
                }
                try{
                    boolean check = checkWord(word, usedIndex);
                    System.out.println(check);
                    String direction = "V";
                    if (directions.get(position) == Direction.RIGHT || directions.get(position) == Direction.LEFT){
                        direction = "H";
                    }
                    List<Integer> colRow = new ArrayList<>();
                    colRow.add(position.getCol());
                    colRow.add(position.getRow());
                    System.out.println("trying to wordplaced");
                    game.placeWord(this, colRow, direction, word);
                    System.out.println("wordplaced");
                    return;
                } catch (IllegalMoveException e){
                    continue;
                } catch (InvalidWordException e) {
                    continue;
                }
            }

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
