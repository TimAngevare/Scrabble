package nl.utwente.angevarevandenbrink.scrabble.controller.remote.server;

import nl.utwente.angevarevandenbrink.scrabble.controller.remote.protocol.ProtocolMessages;

import java.io.*;
import java.net.Socket;

public class ScrabbleClientHandler implements Runnable {
    private BufferedReader in;
    private BufferedWriter out;

    private Socket sock;
    private ScrabbleServer server;

    private String name = null;

    public ScrabbleClientHandler(Socket sock, ScrabbleServer server) {
        try {
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

            this.sock = sock;
            this.server = server;
        } catch(IOException e) {
            shutdown();
        }
    }

    public void run() {
        String msg;
        try {
            msg = in.readLine();
            while (msg != null) {
                System.out.println("> [" + getName() + "] Incoming: " + msg);
                handleCommand(msg);
                msg = in.readLine();
            }
            shutdown();
        } catch (IOException e) {
            shutdown();
        }
    }

    private void handleCommand(String msg) throws IOException {
        String[] split = msg.split(ProtocolMessages.SEPARATOR);

        switch (split[0]) {
            case ProtocolMessages.HELLO:
                this.name = split[1];
                sendMessage(server.handleHello(split[1]));
                break;
            case ProtocolMessages.CLIENTREADY:
                server.handleClientReady(this);
                break;
            case ProtocolMessages.MOVE:
                if (server.isTurn(this)) {
                    server.nextTurn(split[2].split(ProtocolMessages.AS));
                } else {
                    sendMessage(ProtocolMessages.ERROR + ProtocolMessages.SEPARATOR + ProtocolMessages.OUT_OF_TURN);
                }
                break;
            case ProtocolMessages.PASS:
                if (server.isTurn(this)) {
                    server.nextTurn(new String[]{"-"});
                }
                break;
            case ProtocolMessages.ABORT:
                server.removeClient(this);
                shutdown();
                break;
            default:
                //sendMessage("Unknown Command: <" + msg + ">");
                sendMessage(ProtocolMessages.ERROR + ProtocolMessages.SEPARATOR + ProtocolMessages.UNRECOGNIZED);
                break;
        }

        //System.out.println("gonna send: " + toSend);
        //sendMessage(toSend);
    }

    public String getName() {
        if (name == null) {
            return "Unready player";
        }
        return name;
    }

    public void sendMessage(String msg) throws IOException {
        out.write(msg);
        out.newLine();
        out.flush();
    }

    public void shutdown() {
        System.out.println("> [" + name + "] Shutting down.");
        try {
            in.close();
            out.close();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.removeClient(this);
    }
}
