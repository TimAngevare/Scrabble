package nl.utwente.angevarevandenbrink.scrabble.controller.remote.protocol;

import nl.utwente.angevarevandenbrink.scrabble.exception.ProtocolException;
import nl.utwente.angevarevandenbrink.scrabble.exception.ServerUnavailableException;

public interface ClientProtocol {
    /**
     * Handles the hello to the server -> this method sends HELLO to server
     * @throws ServerUnavailableException if IO error occurs
     * @throws ProtocolException if the received message does not comply to HELLO + DELIMETER + name
     */
    //public String sendHello() throws ServerUnavailableException, ProtocolException;

    /**
     * Sends an EXIT to the server after which it closes the connection
     * @throws ServerUnavailableException if IO error occurs
     */
    //public void sendExit() throws ServerUnavailableException;
}
