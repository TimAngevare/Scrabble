package nl.utwente.angevarevandenbrink.scrabble.view.remote.client;

import nl.utwente.angevarevandenbrink.scrabble.controller.remote.exception.ExitProgram;
import nl.utwente.angevarevandenbrink.scrabble.controller.remote.exception.ServerUnavailableException;

import java.net.InetAddress;
import java.util.HashMap;

public interface ClientView extends Runnable {
    void showMessage(String msg);


    void showError(String msg);

    String getString(String msg);
    int getInt(String msg);
    boolean getBoolean(String msg);
    InetAddress getIp();

    void showTileRack();
    void showPlayerSummary(HashMap<String, Integer> players);

    void showEndGame(String winner, int score);

    void showTurnSep();

    String[] getMove();

    void handleUserInput(String input) throws ExitProgram, ServerUnavailableException;
}
