package nl.utwente.angevarevandenbrink.scrabble.controller.remote.server;

import nl.utwente.angevarevandenbrink.scrabble.model.Game;
import nl.utwente.angevarevandenbrink.scrabble.controller.remote.exception.ExitProgram;
import nl.utwente.angevarevandenbrink.scrabble.model.HumanPlayer;
import nl.utwente.angevarevandenbrink.scrabble.model.Player;
import nl.utwente.angevarevandenbrink.scrabble.model.exception.IllegalMoveException;
import nl.utwente.angevarevandenbrink.scrabble.model.exception.InvalidWordException;
import nl.utwente.angevarevandenbrink.scrabble.view.local.BoardDraw;
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
    private Map<ScrabbleClientHandler, Player> playerHandlerLink = new HashMap<>();

    private int next_client_no;
    private ScrabbleServerView view;
    private BoardDraw bd;

    private Game game;
    private static final int MINPLAYERS = 2;
    private boolean gameStarted;
    private ScrabbleClientHandler turn = null;

    private boolean continueGame = true;

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

                sendToAll(ProtocolMessages.SERVERREADY);
                while (clientsReady.containsValue(false)) {
                    continue;
                }

                sendStartGame();
                startTurn();

                while (continueGame) {
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

    private ScrabbleClientHandler getNextHandler() {
        if (turn == null) {
            return clients.get(0);
        }

        int index = clients.indexOf(turn) + 1;
        if (index >= clients.size()) {
            index = 0;
        }
        return clients.get(index);
    }

    private void startTurn() {
        ScrabbleClientHandler handler = getNextHandler();
        turn = handler;
        Player player = playerHandlerLink.get(handler);

        sendToAll(ProtocolMessages.BOARD + ProtocolMessages.SEPARATOR + bd.drawStringBoard(game.getBoard()));
        sendToAll(ProtocolMessages.TURN + ProtocolMessages.SEPARATOR + player.getName());
    }

    public void nextTurn(String[] move) throws IOException {
        Player player = playerHandlerLink.get(turn);

        if (move[0].equals("-")) {
            player.newTiles(game.getTilebag());
        } else {
            try {
                game.placeWord(player, splitRowCol(move[0]), move[1], move[2]);
                turn.sendMessage(ProtocolMessages.TILES + ProtocolMessages.SEPARATOR + player.getStringTileRack());
                sendToAll(ProtocolMessages.MOVE + ProtocolMessages.SEPARATOR + player.getName() + ProtocolMessages.SEPARATOR + player.getScore());
            } catch (InvalidWordException | IllegalMoveException e) {
                turn.sendMessage(ProtocolMessages.ERROR + ProtocolMessages.SEPARATOR + ProtocolMessages.INVALID_MOVE);
            }
        }

        if (!game.isFinished()) {
            startTurn();
        } else {
            sendToAll(ProtocolMessages.GAMEOVER);
            game.setFinalScores();

            continueGame = false;
        }
    }

    public List<Integer> splitRowCol(String start) {
        List<Integer> startSplit = new ArrayList<>();

        try {
            char letter = start.charAt(start.length() - 1);

            int col = ((int) letter - 65);
            int row = Integer.parseInt(start.substring(0, start.length() - 1));

            startSplit.add(col);
            startSplit.add(row - 1);
        } catch (NumberFormatException e) {
            char letter = start.charAt(0);

            int col = ((int) letter - 65);
            start = start.replace(String.valueOf(letter), "");
            int row = Integer.parseInt(start.substring(0, start.length()));

            startSplit.add(col);
            startSplit.add(row - 1);
        } finally {
            return startSplit;
        }
    }

    public void setupScrabble() {
        game = new Game();
        bd = new BoardDraw();
    }

    public void removeClient(ScrabbleClientHandler client) {
        this.clients.remove(client);
    }

    public boolean gameStarted() {
        return gameStarted;
    }

    public boolean isTurn(ScrabbleClientHandler handler) {
        return turn == handler;
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

    private void sendStartGame(){
        for (ScrabbleClientHandler handler : clients) {
            Player newPlayer = new HumanPlayer(handler.getName(), game.getTilebag());
            game.addPlayer(newPlayer);
            playerHandlerLink.put(handler, newPlayer);

        }

        String toSend = ProtocolMessages.START;
        for (Player player : game.getPlayers()) {
            toSend += ProtocolMessages.SEPARATOR + player.getName();
        }
        sendToAll(toSend);

        view.showMessage("Giving everyone their tiles...");
        for (ScrabbleClientHandler handler : clients) {
            Player player = playerHandlerLink.get(handler);
            try {
                handler.sendMessage(ProtocolMessages.TILES + ProtocolMessages.SEPARATOR + player.getStringTileRack());
            } catch (IOException e) {
                handler.shutdown();
            }
        }


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
    }

    // -------------- Main ------------------

    public static void main(String[] args) {
        ScrabbleServer server = new ScrabbleServer();
        System.out.println("Welcome to the scrabble server, starting...");
        new Thread(server).start();
    }
}
