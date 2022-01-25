package nl.utwente.angevarevandenbrink.scrabble.server.controller;

import java.io.*;
import java.net.Socket;

public class ScrabbleClientHandler implements Runnable {
    private BufferedReader in;
    private BufferedWriter out;

    private Socket sock;
    private ScrabbleServer server;

    private String name;

    public ScrabbleClientHandler(Socket sock, ScrabbleServer server, String name) {
        try {
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

            this.sock = sock;
            this.server = server;
            this.name = name;
        } catch(IOException e) {
            shutdown();
        }
    }

    public void run() {
        String msg;
        try {
            msg = in.readLine();
            while (msg != null) {
                System.out.println("> [" + name + "] Incoming: " + msg);
                handleCommand(msg);
                out.newLine();
                out.flush();
                msg = in.readLine();
            }
            shutdown();
        } catch (IOException e) {
            shutdown();
        }
    }

    private void handleCommand(String msg) throws IOException {
        String[] split = msg.split(";");

        String toSend = "Unknown Command: <" + msg + ">";


        System.out.println("gonna send: " + toSend);
        out.write(toSend);
        //out.newLine();
        out.flush();
    }

    private void shutdown() {
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
