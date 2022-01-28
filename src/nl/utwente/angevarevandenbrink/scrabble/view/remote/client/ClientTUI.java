package nl.utwente.angevarevandenbrink.scrabble.view.remote.client;

import nl.utwente.angevarevandenbrink.scrabble.controller.remote.client.ScrabbleClient;
import nl.utwente.angevarevandenbrink.scrabble.controller.remote.exception.ExitProgram;
import nl.utwente.angevarevandenbrink.scrabble.controller.remote.exception.ServerUnavailableException;

import nl.utwente.angevarevandenbrink.scrabble.view.ANSI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.*;

public class ClientTUI implements Runnable, ClientView {
    //Scanner sc;
    BufferedReader br;
    private ScrabbleClient client;

    private String name;

    private static final String[] YES = {"yes", "y", "true", "1"};
    private static final String[] NO = {"no", "n", "false", "0"};
    private static final String TURNSEPERATOR = "---------------------------------------------------------------------";

    public ClientTUI(ScrabbleClient client){
        //this.sc = new Scanner(System.in);
        this.br = new BufferedReader(new InputStreamReader(System.in));
        this.client = client;
    }

    @Override
    public void run() {

        try {
            while (true) {
                //String input = sc.nextLine();
                String input = br.readLine();
                handleUserInput(input);
            }
        } catch (ExitProgram | ServerUnavailableException | IOException e) {
            showMessage("Something went wrong");
            client.closeConnection();
        }
    }

    @Override
    public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException {
        String[] split = input.split(" ");

        switch (split[0].toLowerCase()) {
            case "ready":
                if (client.isServerReady()) {
                    client.sendReady();
                } else {
                    showMessage("Server is not ready yet.");
                }
                break;
            case "m":
                String[] move = getMove();
                showMessage("Sending move...");
                client.sendMove(move);
                break;
            case "p":
                showMessage("Passing move...");
                client.sendPass();
                break;
            case "exit":
                showMessage("Exiting game...");
                client.sendExit();
                break;
            default:
                showMessage("That is not a valid input, please try again.");
                break;
        }
    }

    public void showMessage(String msg) {
        System.out.println(msg);
    }

    public void showError(String msg) { System.out.println(ANSI.RED_BOLD_BRIGHT + msg + ANSI.RESET);}

    private String getLine() {
        try {
            return br.readLine();
        } catch (IOException e) {
            showMessage(e.getMessage());
            return "";
        }
    }

    @Override
    public String getString(String msg) {
        showMessage(msg);
        return getLine();
    }

    @Override
    public int getInt(String msg) {
        showMessage(msg);
        return Integer.parseInt(getLine());
    }

    @Override
    public boolean getBoolean(String msg) {
        while (true) {
            showMessage(msg);
            String input = getLine();
            if (Arrays.asList(YES).contains(input.toLowerCase())) {
                return true;
            } else if (Arrays.asList(NO).contains(input.toLowerCase())) {
                return true;
            } else {
                showMessage("That is not a valid input, try again.");
            }
        }
    }

    @Override
    public InetAddress getIp() {
        return null;
    }

    @Override
    public void showTurnSep() {
        showMessage(TURNSEPERATOR);
    }

    @Override
    public void showTileRack() {
        System.out.print(ANSI.PURPLE + "Your letters - |");
        for (String let : client.getLetters()){
            System.out.print(" " + let + " |");
        }
        System.out.print(ANSI.RESET + "\n");
    }

    @Override
    public void showPlayerSummary(HashMap<String, Integer> players) {
        for (String playerName : new ArrayList<>(players.keySet())) {
            System.out.print(ANSI.YELLOW + ( "- " + playerName + " (" + players.get(playerName) + ") "));
        }
        System.out.print(ANSI.RESET + "\n");

    }

    @Override
    public void showEndGame(String winner, int score) {
        showMessage("The winner is " + winner + " with a score of " + score + "!");
    }

    @Override
    public String[] getMove() {
        do {
            String move = getString("Type start square (12A) followed by (H)orizontal or (V)ertical and finally the word you want to place, to pass and replace all tiles type: -");
            String[] moveArr = move.split(" ");

            if (moveArr.length == 3 && (moveArr[1].equalsIgnoreCase("V") || moveArr[1].equalsIgnoreCase("H"))) {
                for (int let = 65; let < 80; let++){
                    for (int row = 1; row < 16; row++){
                        if(moveArr[0].contains(Character.toString((char) let)) && moveArr[0].contains(Integer.toString(row))){
                            return moveArr;
                        }
                    }
                }
            } else if (moveArr[0].equals("-")){
                return moveArr;
            }

            showMessage("Invalid syntax, try again!");
        } while (true);
    }
}
