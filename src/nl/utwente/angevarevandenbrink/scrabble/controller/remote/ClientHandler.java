package nl.utwente.angevarevandenbrink.scrabble.controller.remote;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private BufferedReader in;
    private BufferedWriter out;

    public ClientHandler(Socket sock){
        try {
            this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }
}
