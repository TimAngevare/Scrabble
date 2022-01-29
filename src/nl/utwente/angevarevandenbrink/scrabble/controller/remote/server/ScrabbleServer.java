package nl.utwente.angevarevandenbrink.scrabble.controller.remote.server;

import nl.utwente.angevarevandenbrink.scrabble.model.*;
import nl.utwente.angevarevandenbrink.scrabble.controller.remote.exception.ExitProgram;
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
    private List<Bot> allBots;
    private Map<ScrabbleClientHandler, Boolean> clientsReady;
    private Map<ScrabbleClientHandler, Player> playerHandlerLink;

    private ScrabbleServerView view;
    private BoardDraw bd;

    private boolean openNewSocket = true;

    private Game game;
    private int amountBots = 0;
    private static final int MINPLAYERS = 2;
    private static final int MAXPLAYERS = 4;
    private ScrabbleClientHandler turn = null;

    private boolean continueGame = true;

    public ScrabbleServer() {
        clients = new ArrayList<>();
        allBots = new ArrayList<>();
        clientsReady = new HashMap<>();
        playerHandlerLink = new HashMap<>();

        view = new ScrabbleServerTUI();
    }

    private void addPlayer() {
        try {
            Socket sock = ssock.accept();
            //String name = "Player " + String.format("%02d", next_client_no++);
            String name = "No name assigned";
            view.showMessage("New client [" + name + "] connected!");
            ScrabbleClientHandler handler = new ScrabbleClientHandler(sock, this);
            new Thread(handler).start();
            clients.add(handler);
            clientsReady.put(handler, false);
        }  catch (IOException e) {
            view.showMessage("A server IO error occurred: " + e.getMessage());

            if (!view.getBoolean("Do you want to open a new socket?")) {
                openNewSocket = false;
            }
        }

    }

    @Override
    public void run() {
        while (openNewSocket) {
            try {
                setup();

                while (true) {
                    addPlayer();

                    if (clients.size() + amountBots >= MAXPLAYERS) {
                        view.showMessage("Maximum amount of players reached, sending server ready.");
                        break;
                    } else if (clients.size() + amountBots >= MINPLAYERS) {
                        int chosen = view.getInt("Choose one: 1. allow another player 2. add bot 3. send server ready");
                        if (chosen == 2) {
                            amountBots++;
                            int difficulty = view.getInt("What difficulty?\n(1) beginner\n(2) intermediate\n(3) hard\n(4) expert");
                            Bot bot = new Bot(game, amountBots, difficulty);

                            allBots.add(bot);
                            game.addPlayer(bot);
                            break;
                        } else if (chosen == 3) {
                            break;
                        } else if (chosen == 1) {
                            int amountNewPlayers = view.getInt("How many new players do you want to allow?");
                            for (int i = 0; i < amountNewPlayers; i++) {
                                addPlayer();
                            }
                        }
                    }
                }

                sendToAll(ProtocolMessages.SERVERREADY);
                view.showMessage("Waiting for all players to be ready...");
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

    private void botsMakeAMove(){
        for (int i = 0; i < amountBots; i++) {
            Bot bot = allBots.get(i);
            sendToAll(ProtocolMessages.BOARD + ProtocolMessages.SEPARATOR + bd.drawStringBoard(game.getBoard()));
            sendToAll(ProtocolMessages.TURN + ProtocolMessages.SEPARATOR + bot.getName());
            view.showMessage("Turn: " + bot.getName());

            try {
                Move move = bot.getMove();
                game.placeWord(bot, move);

                sendToAll(ProtocolMessages.MOVE + ProtocolMessages.SEPARATOR + bot.getName() + ProtocolMessages.SEPARATOR + bot.getScore() + ProtocolMessages.SEPARATOR + move.getWord());
            } catch (IllegalMoveException | InvalidWordException ignored) {}
        }
    }

    private ScrabbleClientHandler getNextHandler() {
        if (turn == null) {
            return clients.get(0);
        }

        int index = clients.indexOf(turn) + 1;
        if (index >= clients.size()) {
            botsMakeAMove();
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
        view.showMessage("Turn: " + player.getName());
    }

    public void nextTurn(String[] move) throws IOException {
        Player player = playerHandlerLink.get(turn);

        if (move[0].equals("-")) {
            player.newTiles(game.getTilebag());
            turn.sendMessage(ProtocolMessages.TILES + ProtocolMessages.SEPARATOR + player.getStringTileRack());
        } else {
            try {
                game.placeWord(player, splitRowCol(move[0]), move[1], move[2]);
                turn.sendMessage(ProtocolMessages.TILES + ProtocolMessages.SEPARATOR + player.getStringTileRack());
                sendToAll(ProtocolMessages.MOVE + ProtocolMessages.SEPARATOR + player.getName() + ProtocolMessages.SEPARATOR + player.getScore() + ProtocolMessages.SEPARATOR + move[2]);
            } catch (InvalidWordException | IllegalMoveException e) {
                turn.sendMessage(ProtocolMessages.ERROR + ProtocolMessages.SEPARATOR + ProtocolMessages.INVALID_MOVE + ProtocolMessages.SEPARATOR + e.getMessage());
            }
        }

        if (!game.isFinished()) {
            startTurn();
        } else {
            game.setFinalScores();
            Player winner = game.getTopPlayer();
            sendToAll(ProtocolMessages.GAMEOVER + ProtocolMessages.SEPARATOR + winner.getName() + ProtocolMessages.SEPARATOR + winner.getScore());

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

    public boolean isTurn(ScrabbleClientHandler handler) {
        return turn == handler;
    }

    private void sendToAll(String msg) {
        //view.showMessage("Sending to all: " + msg);
        for (ScrabbleClientHandler handler : clients) {
            try {
                handler.sendMessage(msg);
            } catch (IOException e) {
                handler.shutdown();
            }
        }
    }

    private void sendToAll(String msg, ScrabbleClientHandler exception) {
        for (ScrabbleClientHandler handler : clients) {
            if (handler != exception) {
                try {
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    handler.shutdown();
                }
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

    public String handleHello(String name, ScrabbleClientHandler og) {
        for (ScrabbleClientHandler handler : clients) {
            if (name.equals(handler.getName()) && handler != og) {
                return ProtocolMessages.ERROR + ProtocolMessages.SEPARATOR + ProtocolMessages.DUPLICATE_NAME;
            }
        }

        String toSend = ProtocolMessages.HELLO;
        for (ScrabbleClientHandler c : clients) {
            toSend += ProtocolMessages.SEPARATOR + c.getName();
        }

        sendToAll(ProtocolMessages.WELCOME + ProtocolMessages.SEPARATOR + name, og);

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
