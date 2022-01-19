package nl.utwente.angevarevandenbrink.scrabble.server.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;;

public class Start {
    private ServerSocket ss;
    private ArrayList<Socket> sockets;


    public void setUp(int players){
        sockets = new ArrayList<>();
        try {
            this.ss = new ServerSocket(0);
            System.out.println("Listening on:\nport: " + ss.getLocalPort() + "\nip: " + ss.getInetAddress());
            int counter = 0;
            while (counter <= players){
                sockets.add(ss.accept());
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Socket> getSockets() {
        return sockets;
    }
}
