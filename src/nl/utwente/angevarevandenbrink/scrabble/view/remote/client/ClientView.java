package nl.utwente.angevarevandenbrink.scrabble.view.remote.client;

import nl.utwente.angevarevandenbrink.scrabble.model.Board;
import nl.utwente.angevarevandenbrink.scrabble.model.Player;

import java.net.InetAddress;
import java.util.ArrayList;

public interface ClientView {
    void showMessage(String msg);

    void showError(String msg);

    String getString(String msg);
    int getInt(String msg);
    boolean getBoolean(String msg);
    InetAddress getIp();

    void showTileRack(Player player);
    void showPlayerSummary(ArrayList<Player> players);

    String[] getMove();

    void updateBoard(Board board);

    void start() throws nl.utwente.angevarevandenbrink.scrabble.remote.exception.ServerUnavailableException;

    void HandleUserInput(String input) throws nl.utwente.angevarevandenbrink.scrabble.remote.exception.ExitProgram, nl.utwente.angevarevandenbrink.scrabble.remote.exception.ServerUnavailableException;
}
