package nl.utwente.angevarevandenbrink.scrabble.tests;

import nl.utwente.angevarevandenbrink.scrabble.model.*;
import nl.utwente.angevarevandenbrink.scrabble.model.exception.IllegalMoveException;
import nl.utwente.angevarevandenbrink.scrabble.model.exception.InvalidWordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTest {
    private Game game;
    private Player bot;
    private Player human;

    private ArrayList<Tile> exampleTileRack = new ArrayList<>();
    private static final char[] exampleLetters = {'a', 'a', 'b', 'd', 'e', 'k', 'c'};

    @BeforeEach
    void setUp() {
        this.game = new Game();
        this.bot = new Bot(game, 1, 1);
        this.human = new HumanPlayer("Human", game.getTilebag());

        for (char let : exampleLetters) {
            exampleTileRack.add(new Tile(let));
        }
    }

    @Test
    void TestAddPlayer() {
        game.addPlayer(bot);
        assertTrue(game.getPlayers().contains(bot));
    }

    @Test
    void testTopPlayer() {
        game.addPlayer(human);
        game.addPlayer(bot);
        human.addScore(20);
        bot.addScore(10);

        assertEquals(game.getTopPlayer(), human);
    }

    @Test
    void testTurn() throws InvalidWordException, IllegalMoveException {
        game.addPlayer(human);
        human.setTileRack(exampleTileRack);
        Move move = new Move(7, 5, "H", "baked");

        game.placeWord(human, move);

        assertEquals(24, human.getScore());
        assertEquals(game.getBoard().getPosition(7, 5).getTile().getLetter(), 'b');
    }

    @Test
    void testIsFinished() {
        game.addPlayer(human);
        human.setTileRack(new ArrayList<>());
        game.getTilebag().emptyTileBag();

        game.setCopyBoard(game.getBoard().cloneBoard());
        game.setCopyBoard(game.getBoard().cloneBoard());

        assertTrue(game.isFinished());
    }

}
