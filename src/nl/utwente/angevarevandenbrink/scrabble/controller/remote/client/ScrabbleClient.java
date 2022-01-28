package nl.utwente.angevarevandenbrink.scrabble.controller.remote.client;

import nl.utwente.angevarevandenbrink.scrabble.controller.remote.protocol.ProtocolMessages;
import nl.utwente.angevarevandenbrink.scrabble.controller.remote.exception.ExitProgram;
import nl.utwente.angevarevandenbrink.scrabble.controller.remote.exception.ProtocolException;
import nl.utwente.angevarevandenbrink.scrabble.controller.remote.exception.ServerUnavailableException;
import nl.utwente.angevarevandenbrink.scrabble.controller.remote.protocol.ClientProtocol;
import nl.utwente.angevarevandenbrink.scrabble.view.remote.client.ClientTUI;
import nl.utwente.angevarevandenbrink.scrabble.view.remote.client.ClientView;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

public class ScrabbleClient implements ClientProtocol, Runnable {
    private Socket serverSock;
    private BufferedReader in;
    private BufferedWriter out;

    private ClientView view;

    private String name;
    private String[] letters;
    private HashMap<String, Integer> players = new HashMap<>();

    public String[] getLetters() {
        return letters;
    }

    private boolean serverReady = false;

    public ScrabbleClient() {
        view = new ClientTUI(this);
    }

    @Override
    public void run() {
        while (true) {
            try {
                createConnection();
                sendHello();
                new Thread(view).start();

                if (in != null) {

                    while (true) {
                        try {
                            String msg = in.readLine();
                            if (msg == null) {
                                throw new ServerUnavailableException("Could not read from server");
                            }
                            //view.showMessage("Received from server: " + msg);
                            handleServerInput(msg);
                        } catch (IOException e) {
                            throw new ServerUnavailableException("Could not read from server");
                        }
                    }
                } else {
                    throw new ServerUnavailableException("Could not read from server");
                }

            } catch (ExitProgram | ServerUnavailableException e) {
                boolean toContinue = view.getBoolean("Do you want to make a new connection?");
                if (!toContinue) {
                    break;
                }
            }
        }
    }

    public void createConnection() throws ExitProgram {
        clearConnection();
        while (serverSock == null) {
            String host = "127.0.0.1";
            int port = 8888;

            try {
                InetAddress addr = InetAddress.getByName(host);
                view.showMessage("Attempting to connect to " + host + ":" + port + "...");
                serverSock = new Socket(addr, port);
                in = new BufferedReader(new InputStreamReader(serverSock.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(serverSock.getOutputStream()));
                view.showMessage("Successfully connected.");
            } catch (IOException e) {
                view.showMessage("Could not connect to " + host + " on port " + port);

                boolean toContinue = view.getBoolean("Do you want to try again?");
                if (!toContinue) {
                    throw new ExitProgram("User wants to exit.");
                }
            }
        }
    }

    public void clearConnection() {
        serverSock = null;
        in = null;
        out = null;
    }

    public synchronized void sendMessage(String msg) throws ServerUnavailableException {
        if (out != null) {
            try {
                out.write(msg);
                out.newLine();
                out.flush();
            } catch (IOException e) {
                view.showMessage(e.getMessage());
                throw new ServerUnavailableException("Could not write to server");
            }
        } else {
            throw new ServerUnavailableException("Could not write to server");
        }
    }

    public void closeConnection() {
        view.showMessage("Closing connection to server...");
        try {
            in.close();
            out.close();
            serverSock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleServerInput(String input) throws ServerUnavailableException {
        String[] split = input.split(ProtocolMessages.SEPARATOR);

        switch (split[0]) {
            case ProtocolMessages.HELLO:
                view.showMessage("Other players (including you):");
                for (int i = 1; i < split.length; i++) {
                    view.showMessage(i + ". " + split[i]);
                }

                view.showMessage("Waiting for the server to be ready...");
                break;
            case ProtocolMessages.SERVERREADY:
                serverReady = true;
                view.showMessage("Server is ready, type 'ready' if you are also ready!");
                break;
            case ProtocolMessages.START:
                String toShow = "Starting game with: ";
                for (int i = 1; i < split.length; i++) {
                    toShow +=  "<" + split[i] + "> ";
                    players.put(split[i], 0);
                }
                view.showMessage(toShow);
                break;
            case ProtocolMessages.WELCOME:
                view.showMessage("New player connected! Call them " + split[1]);
                break;
            case ProtocolMessages.TILES:
                letters = split[1].split(ProtocolMessages.AS);
                //view.showTileRack();
                break;
            case ProtocolMessages.BOARD:
                view.showMessage(split[1]);
                break;
            case ProtocolMessages.TURN:
                view.showTurnSep();
                view.showPlayerSummary(players);
                if (split[1].equals(name)) {
                    view.showMessage("Current turn: YOU");
                    view.showTileRack();
                    view.showMessage("type 'm' to start making a turn, type 'p' to pass this turn.");
                } else {
                    view.showMessage("Current turn: " + split[1]);
                }
                break;
            case ProtocolMessages.MOVE:
                players.replace(split[1], Integer.valueOf(split[2]));

                if (split[1].equals(name)) {
                    view.showMessage("You received " + split[2] + " points for your word '" + split[3] + "'.");
                } else {
                    view.showMessage(name + " lays the word '" + split[3] + "' for " + split[2] + " points.");
                }
                break;
            case ProtocolMessages.GAMEOVER:
                view.showTurnSep();
                view.showMessage("---------- GAME OVER ----------");
                view.showEndGame(split[1], Integer.parseInt(split[2]));
                break;
            case ProtocolMessages.ERROR:
                view.showError(split[1]);

                if (split[1].equals(ProtocolMessages.INVALID_MOVE)) {
                    view.showError("Reason: " + split[2]);
                } else if (split[1].equals(ProtocolMessages.DUPLICATE_NAME)) {
                    sendHello();
                }
                break;
            default:
                view.showMessage("Received unrecognized: " + input);
                break;
        }
    }

    //@Override
    public void sendHello() throws ServerUnavailableException {
        name = view.getString("What is your name?");
        String messageToSend = ProtocolMessages.HELLO + ProtocolMessages.SEPARATOR + name;
        sendMessage(messageToSend);
    }

    public void sendReady() throws ServerUnavailableException {
        String messageToSend = ProtocolMessages.CLIENTREADY + ProtocolMessages.SEPARATOR + name;
        sendMessage(messageToSend);
    }

    public void sendMove(String[] move) throws ServerUnavailableException {
        String messageToSend = ProtocolMessages.MOVE + ProtocolMessages.SEPARATOR + name + ProtocolMessages.SEPARATOR + move[0];
        for (int i = 1; i < move.length; i++) {
            messageToSend += ProtocolMessages.AS + move[i];
        }

        sendMessage(messageToSend);
    }

    public void sendPass() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.PASS + ProtocolMessages.SEPARATOR + name);
    }

    //@Override
    public void sendExit() throws ServerUnavailableException {
        String messagetoSend = ProtocolMessages.ABORT + ProtocolMessages.SEPARATOR + name;
        sendMessage(messagetoSend);

        closeConnection();
    }

    public String getName() {
        return name;
    }

    public boolean isServerReady() {
        return serverReady;
    }

    public static void main(String[] args) {
        ScrabbleClient SClient = new ScrabbleClient();
        //new Thread(SClient).start();
        SClient.run();
    }
}
