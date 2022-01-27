package nl.utwente.angevarevandenbrink.scrabble.view.remote.server;

public interface ScrabbleServerView {
    void showMessage(String msg);
    String getString(String msg);
    int getInt(String msg);
    boolean getBoolean(String msg);
}
