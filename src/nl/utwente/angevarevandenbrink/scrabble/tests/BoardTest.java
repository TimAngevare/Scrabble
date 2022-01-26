package nl.utwente.angevarevandenbrink.scrabble.tests;

import nl.utwente.angevarevandenbrink.scrabble.model.*;
import nl.utwente.angevarevandenbrink.scrabble.model.exception.InvalidWordException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;
    private ArrayList<Tile> tiles = new ArrayList<>();

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        this.board = new Board();
        String word = "hello";
        for (int i = 0; i < word.length(); i++){
            Tile tile = new Tile(word.charAt(i));
            tiles.add(tile);
            board.getPosition(i + 7, 7).placeTile(tile);
        }
    }

    @org.junit.jupiter.api.Test
    void getPosition() {
        assertEquals(board.getPosition(7,7).getTile(), tiles.get(0));
        assertEquals(board.getPosition(8,7).getTile(), tiles.get(1));
    }

    @org.junit.jupiter.api.Test
    void isInBounds() {
        assertTrue(Board.isInBounds(5));
        assertFalse(Board.isInBounds(16));
    }

    @org.junit.jupiter.api.Test
    void cloneBoard() {
        Board clone = board.cloneBoard();
        for (int row = 0; row < Board.LENGTH; row++){
            for (int col = 0; col < Board.LENGTH; col++){
                assertEquals(board.getPosition(row, col).getTile(), clone.getPosition(row, col).getTile());
            }
        }
    }

    @org.junit.jupiter.api.Test
    void isEmpty() {
        assertFalse(board.isEmpty());
        assertTrue((new Board()).isEmpty());
    }

    @org.junit.jupiter.api.Test
    void checkSquareType() {
        assertEquals(board.checkSquareType(7,7), SquareType.START);
        assertEquals(board.checkSquareType(12, 2), SquareType.DOUBLE_WORD);
        assertEquals(board.checkSquareType(8, 7), SquareType.BLANK);
    }

    @org.junit.jupiter.api.Test
    void getNotEmptyPositions() {
        ArrayList<Position> positions = board.getNotEmptyPositions();
        ArrayList<Tile> tilesNew = new ArrayList<>();
        for (Position position : positions){
            for (Tile tile : tiles){
                if (position.getTile().equals(tile)){
                    tilesNew.add(tile);
                    break;
                }
            }

        }
        assertEquals(tiles.size(), tilesNew.size());
    }

    @org.junit.jupiter.api.Test
    void isEmptyField() {
        assertTrue(board.isEmptyField(4, 7));
        assertFalse(board.isEmptyField(8,7));
    }

    @org.junit.jupiter.api.Test
    void checkFullWordsValid() {
        String word = "bee";
        ArrayList<TilePlacement> placements = new ArrayList<>();
        for (int i = 0; i < word.length(); i++){
            TilePlacement placement = new TilePlacement(board.getPosition(3,  i), new Tile(word.charAt(i)));
            placements.add(placement);
        }
        try {
            assertTrue(board.checkFullWordsValid(placements, "H"));
            assertFalse(board.checkFullWordsValid(placements, "V"));
        } catch (InvalidWordException e) {
            e.printStackTrace();
        }
    }

}