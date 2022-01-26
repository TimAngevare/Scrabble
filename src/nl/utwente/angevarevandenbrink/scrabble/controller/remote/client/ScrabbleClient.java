package nl.utwente.angevarevandenbrink.scrabble.controller.remote.client;

import nl.utwente.angevarevandenbrink.scrabble.remote.exception.ExitProgram;
import nl.utwente.angevarevandenbrink.scrabble.remote.exception.ProtocolException;
import nl.utwente.angevarevandenbrink.scrabble.remote.exception.ServerUnavailableException;
import nl.utwente.angevarevandenbrink.scrabble.remote.protocol.ClientProtocol;
import nl.utwente.angevarevandenbrink.scrabble.remote.protocol.ProtocolMessages;
import nl.utwente.angevarevandenbrink.scrabble.view.local.LocalTUI;
import nl.utwente.angevarevandenbrink.scrabble.view.local.LocalView;
import nl.utwente.angevarevandenbrink.scrabble.view.remote.client.ClientTUI;
import nl.utwente.angevarevandenbrink.scrabble.view.remote.client.ClientView;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ScrabbleClient implements ClientProtocol {
    private Socket serverSock;
    private BufferedReader in;
    private BufferedWriter out;

    private ClientView view;

    public ScrabbleClient() {
        view = new ClientTUI(this);
    }

    public void start() {
        while (true) {
            try {
                createConnection();
                view.start();
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

    public String readLineFromServer() throws ServerUnavailableException {
        if (in != null) {
            try {
                String msg = in.readLine();
                if (msg == null) {
                    throw new ServerUnavailableException("Could not read from server");
                }
                return msg;
            } catch (IOException e) {
                throw new ServerUnavailableException("Could not read from server");
            }
        } else {
            throw new ServerUnavailableException("Could not read from server");
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

    @Override
    public void handleHello() throws ServerUnavailableException, ProtocolException {
        String messagetoSend = ProtocolMessages.HELLO;
        sendMessage(messagetoSend);

        String received = readLineFromServer();
        if (received.startsWith(ProtocolMessages.HELLO + ProtocolMessages.SEPARATOR)) {
            view.showMessage("Succesfull connection as " + received.substring(3));
        }
    }

    @Override
    public void sendExit() throws ServerUnavailableException {
        String messagetoSend = ProtocolMessages.ABORT;
        sendMessage(messagetoSend);

        closeConnection();
    }

    public static void main(String[] args) {
        (new ScrabbleClient()).start();
    }
}
