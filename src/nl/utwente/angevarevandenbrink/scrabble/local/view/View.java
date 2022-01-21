package nl.utwente.angevarevandenbrink.scrabble.local.view;

import nl.utwente.angevarevandenbrink.scrabble.model.Board;
import nl.utwente.angevarevandenbrink.scrabble.model.Player;

public interface View {
    void showMessage(String msg);

    void showError(String msg);

    String getString(String msg);
    int getInt(String msg);
    boolean getBoolean(String msg);

    void showTileRack(Player player);

    String[] getMove();

    void updateBoard(Board board);
}
