package nl.utwente.angevarevandenbrink.scrabble.controller.remote.server;

import nl.utwente.angevarevandenbrink.scrabble.remote.exception.ExitProgram;
import nl.utwente.angevarevandenbrink.scrabble.remote.serverview.ScrabbleServerView;
import nl.utwente.angevarevandenbrink.scrabble.view.remote.server.ScrabbleServerTUI;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ScrabbleServer implements Runnable {
    private ServerSocket ssock;
    private List<ScrabbleClientHandler> clients;
    private int next_client_no;
    private ScrabbleServerView view;

    //private Scrabble scrabble;

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
                // Sets up the hotel application
                setup();

                while (true) {
                    Socket sock = ssock.accept();
                    String name = "Client " + String.format("%02d", next_client_no++);
                    view.showMessage("New client [" + name + "] connected!");
                    ScrabbleClientHandler handler = new ScrabbleClientHandler(sock, this, name);
                    new Thread(handler).start();
                    clients.add(handler);
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
            int port = view.getInt("Please enter the server port.");

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
        String hotelName = view.getString("Hotel name: ");
        //scrabble = new Scrabble(hotelName);
    }

    public void removeClient(ScrabbleClientHandler client) {
        this.clients.remove(client);
    }
}
