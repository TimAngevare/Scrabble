package nl.utwente.angevarevandenbrink.scrabble.tests;

import nl.utwente.angevarevandenbrink.scrabble.controller.remote.protocol.ProtocolMessages;
import nl.utwente.angevarevandenbrink.scrabble.model.*;
import nl.utwente.angevarevandenbrink.scrabble.model.exception.IllegalBotMoveException;
import nl.utwente.angevarevandenbrink.scrabble.model.exception.IllegalMoveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player;
    private Game game;

    @BeforeEach
    void setUp(){
        game = new Game();
        player = new HumanPlayer("random", game.getTilebag());
    }

    @Test
    void testCloneTileRack(){
        ArrayList<Tile> original = player.getTileRack();
        ArrayList<Tile> copy = player.cloneTileRack();

        for (int i = 0; i < original.size(); i++){
            assertTrue(original.get(i).equals(copy.get(i)));
        }
    }

    @Test
    void testSetTileRack(){
        ArrayList<Tile> original = player.getTileRack();
        ArrayList<Tile> newTileRack= new ArrayList<>();
        newTileRack.add(new Tile('h'));
        newTileRack.add(new Tile('b'));
        player.setTileRack(newTileRack);
        for (int i = 0; i < newTileRack.size(); i++){
            assertTrue(newTileRack.get(i).equals(player.getTileRack().get(i)));
        }
        player.setTileRack(original);
    }

    @Test
    void testNewTiles(){
        ArrayList<Tile> original = player.getTileRack();
        player.newTiles(game.getTilebag());
        for (int i = 0; i < original.size(); i++){
            if (!original.get(i).equals(player.getTileRack().get(i))){
                assertTrue(true);
            }
        }
    }

    @Test
    void testSetName(){
        String name = "tim";
        player.setName(name);
        assertTrue(name.equals(player.getName()));
    }

    @Test
    void testGetName(){
        assertTrue(player.getName().equals("random"));
    }

    @Test
    void testAmountTileLetter(){
        HashMap<Character, Integer> count = new HashMap<>();
        for (Tile tile : player.getTileRack()){
            if (count.keySet().contains(tile.getLetter())){
                int amount = count.get(tile.getLetter()) + 1;
                count.remove(tile.getLetter());
                count.put(tile.getLetter(), amount);
            }
        }
        for (char letter : count.keySet()){
            assertEquals(count.get(letter), player.amountTileLetter(letter));
        }
    }

    @Test
    void testFillTileRack(){
        ArrayList<Tile> tilesToRemove = new ArrayList<>();
        tilesToRemove.add(new Tile(player.getTileRack().get(0).getLetter()));
        tilesToRemove.add(new Tile(player.getTileRack().get(1).getLetter()));
        player.removeTiles(tilesToRemove);
        player.fillTileRack(game.getTilebag());
        System.out.println(player.getTileRack().size());
        assertTrue(player.getTileRack().size() == 7);
    }

    @Test
    void testRemoveTiles(){
        ArrayList<Tile> tilesToRemove = new ArrayList<>();
        tilesToRemove.add(new Tile(player.getTileRack().get(0).getLetter()));
        tilesToRemove.add(new Tile(player.getTileRack().get(1).getLetter()));
        player.removeTiles(tilesToRemove);
        assertTrue(player.getTileRack().size() == 5);
    }

    @Test
    void testRemoveTilePlacements(){
        ArrayList<TilePlacement> tilePlacementsRemove = new ArrayList<>();
        Tile tile1 = player.getTileRack().get(0);
        Tile tile2 = player.getTileRack().get(1);
        tilePlacementsRemove.add(new TilePlacement(new Position(SquareType.BLANK, 0, 0), tile1));
        tilePlacementsRemove.add(new TilePlacement(new Position(SquareType.DOUBLE_WORD, 5, 5), tile2));
        player.removeTilePlacements(tilePlacementsRemove);
        assertTrue(player.getTileRack().size() == 5);
    }

    @Test
    void testGetScore(){
        assertEquals(player.getScore(), 0);
    }

    @Test
    void testAddScore(){
        int oldscore = player.getScore();
        player.addScore(9);
        assertEquals(oldscore + 9, player.getScore());
    }

    @Test
    void testRemoveScore(){
        int oldscore = player.getScore();
        player.removeScore(9);
        assertEquals(oldscore - 9, player.getScore());
    }

    @Test
    void testGetTileRack(){
        ArrayList<Tile> newTileRack = new ArrayList<>();
        newTileRack.add(new Tile('h'));
        newTileRack.add(new Tile('b'));
        player.setTileRack(newTileRack);
        ArrayList<Tile> tileRack = player.getTileRack();
        for (int i = 0; i < newTileRack.size(); i++){
            if(!newTileRack.get(i).equals(tileRack.get(i))){
                assertTrue(false);
            }
        }
        assertTrue(true);
    }

    @Test
    void testGetStringTileRack(){
        String correct = "";
        for (Tile tile : player.getTileRack()) {
            if (!correct.equals("")) {
                correct += ProtocolMessages.AS;
            }
            correct += tile.getLetter();
        }
        assertEquals(player.getStringTileRack(), correct);
    }

    @Test
    void testGetTileRackScore(){
        int correct = 0;
        for (Tile tile : player.getTileRack()) {
            correct += tile.getValue();
        }
        assertEquals(player.getTileRackScore(), correct);
    }

    @Test
    void testToString(){
        String correct = player.getName() + " (" + player.getScore() + " pt.) - ";
        for (Tile tile : player.getTileRack()) {
            correct += tile.getLetter();
        }
        assertEquals(player.toString(), correct);
    }

    @Test
    void testCheckWord(){
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile('b'));
        tiles.add(new Tile('y'));
        tiles.add(new Tile('e'));
        player.setTileRack(tiles);
        try {
            player.checkWord("bye", 0);
            assertTrue(true);
        } catch (IllegalBotMoveException e) {
            assertTrue(false);
        }
        player.setTileRack(tiles);
        ArrayList<TilePlacement> tilePlacements = new ArrayList<>();
        HashMap<String, ArrayList<TilePlacement>> placedTiles = new HashMap<>();
        for (int i = 0; i < tiles.size(); i++){
            tilePlacements.add(new TilePlacement(new Position(SquareType.BLANK, 7, i + 6), tiles.get(i)));
        }
        placedTiles.put("new", tilePlacements);
        try {
            player.checkWord(placedTiles, true);
            assertTrue(true);
        } catch (IllegalMoveException e) {
            assertTrue(false);
        }
    }

}
