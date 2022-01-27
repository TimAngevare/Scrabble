package nl.utwente.angevarevandenbrink.scrabble.controller.remote.server;

import nl.utwente.angevarevandenbrink.scrabble.model.Game;
import nl.utwente.angevarevandenbrink.scrabble.controller.remote.exception.ExitProgram;
import nl.utwente.angevarevandenbrink.scrabble.model.HumanPlayer;
import nl.utwente.angevarevandenbrink.scrabble.model.Player;
import nl.utwente.angevarevandenbrink.scrabble.view.remote.server.ScrabbleServerView;
import nl.utwente.angevarevandenbrink.scrabble.view.remote.server.ScrabbleServerTUI;
import nl.utwente.angevarevandenbrink.scrabble.controller.remote.protocol.ProtocolMessages;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScrabbleServer implements Runnable {
    private ServerSocket ssock;
    private List<ScrabbleClientHandler> clients;
    private Map<ScrabbleClientHandler, Boolean> clientsReady = new HashMap<>();

    private int next_client_no;
    private ScrabbleServerView view;

    private Game game;
    private static final int MINPLAYERS = 1;
    private boolean gameStarted;

    public ScrabbleServer() {
        clients = new ArrayList<>();
        view = new ScrabbleServerTUI();
        next_client_no = 1;
    }

    @Override
    public void run() {
        boolean openNewSocket = true;
        while (openNewSocket) {
            try {
                setup();

                while (true) {
                    Socket sock = ssock.accept();
                    //String name = "Player " + String.format("%02d", next_client_no++);
                    String name = "No name assigned";
                    view.showMessage("New client [" + name + "] connected!");
                    ScrabbleClientHandler handler = new ScrabbleClientHandler(sock, this);
                    new Thread(handler).start();
                    clients.add(handler);
                    clientsReady.put(handler, false);

                    if (clients.size() >= MINPLAYERS) {
                        boolean toContinue = view.getBoolean("Allow another player?");
                        if (!toContinue) {
                            break;
                        }
                    }
                }

                String toSend = ProtocolMessages.SERVERREADY;
                for (Player player : game.getPlayers()) {
                    toSend += ProtocolMessages.SEPARATOR + player.getName();
                }
                sendToAll(toSend);

                while(true) {
                    continue;
                }

            } catch (ExitProgram e1) {
                // If setup() throws an ExitProgram exception,
                // stop the program.
                openNewSocket = false;
            } catch (IOException e) {
                view.showMessage("A server IO error occurred: " + e.getMessage());

                if (!view.getBoolean("Do you want to open a new socket?")) {
                    openNewSocket = false;
                }
            }
        }
        view.showMessage("See you later!");
    }

    public void setup() throws ExitProgram {
        // First, initialize the Hotel.
        setupScrabble();

        ssock = null;
        while (ssock == null) {
            //int port = view.getInt("Please enter the server port.");
            int port = 8888;

            // try to open a new ServerSocket
            try {
                view.showMessage("Attempting to open a socket at 127.0.0.1 " + "on port " + port + "...");
                ssock = new ServerSocket(port, 0, InetAddress.getByName("127.0.0.1"));
                view.showMessage("Server started at port " + port);
            } catch (IOException e) {
                view.showMessage("ERROR: could not create a socket on " + "127.0.0.1" + " and port " + port + ".");

                if (!view.getBoolean("Do you want to try again?")) {
                    throw new ExitProgram("User indicated to exit the program.");
                }
            }
        }
    }

    public void setupScrabble() {
        game = new Game();
    }

    public void removeClient(ScrabbleClientHandler client) {
        this.clients.remove(client);
    }

    public boolean gameStarted() {
        return gameStarted;
    }

    private void sendToAll(String msg) {
        view.showMessage("Sending to all: " + msg);
        for (ScrabbleClientHandler handler : clients) {
            try {
                handler.sendMessage(msg);
            } catch (IOException e) {
                handler.shutdown();
            }
        }
    }

    private void startGame() {
        for (ScrabbleClientHandler handler : clients) {
            game.addPlayer(new HumanPlayer(handler.getName(), game.getTilebag()));
        }
        sendToAll(ProtocolMessages.START);
    }

    // ------------- Server methods -------------------------------

    public String handleHello(String name) {
        String toSend = ProtocolMessages.HELLO;

        for (ScrabbleClientHandler c : clients) {
            toSend += ProtocolMessages.SEPARATOR + c.getName();
        }

        return toSend;
    }

    public void handleClientReady(ScrabbleClientHandler og) {
        clientsReady.replace(og, true);

        if (!clientsReady.containsValue(false) && clients.size() >= MINPLAYERS) {
            startGame();
        }
    }

    // -------------- Main ------------------

    public static void main(String[] args) {
        ScrabbleServer server = new ScrabbleServer();
        System.out.println("Welcome to the scrabble server, starting...");
        new Thread(server).start();
    }
}
