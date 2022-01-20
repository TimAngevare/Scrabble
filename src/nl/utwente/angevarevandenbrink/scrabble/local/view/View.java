package nl.utwente.angevarevandenbrink.scrabble.local.view;

import nl.utwente.angevarevandenbrink.scrabble.model.Board;

public interface View {
    void showMessage(String msg);

    void showError(String msg);

    String getString(String msg);
    int getInt(String msg);
    boolean getBoolean(String msg);

    String[] getMove();

    void updateBoard(Board board);
}
