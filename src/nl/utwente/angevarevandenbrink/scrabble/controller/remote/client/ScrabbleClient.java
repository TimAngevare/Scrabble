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

public class ScrabbleClient implements ClientProtocol, Runnable {
    private Socket serverSock;
    private BufferedReader in;
    private BufferedWriter out;

    private ClientView view;

    private String name;

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

            } catch (ExitProgram | ServerUnavailableException | ProtocolException e) {
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
                }
                view.showMessage(toShow);
                break;
            case ProtocolMessages.BOARD:
                view.showMessage(split[1]);
                break;
            default:
                view.showMessage("Received unrecognized: " + input);
                break;
        }
    }

    //@Override
    public void sendHello() throws ServerUnavailableException, ProtocolException {
        name = view.getString("What is your name?");
        String messageToSend = ProtocolMessages.HELLO + ProtocolMessages.SEPARATOR + name;
        sendMessage(messageToSend);
    }

    public void sendReady() throws ServerUnavailableException {
        String messageToSend = ProtocolMessages.CLIENTREADY + ProtocolMessages.SEPARATOR + name;
        sendMessage(messageToSend);
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
