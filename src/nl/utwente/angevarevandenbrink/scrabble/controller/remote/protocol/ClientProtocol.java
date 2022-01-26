package nl.utwente.angevarevandenbrink.scrabble.remote.protocol;

import nl.utwente.angevarevandenbrink.scrabble.remote.exception.ProtocolException;
import nl.utwente.angevarevandenbrink.scrabble.remote.exception.ServerUnavailableException;

public interface ClientProtocol {
    /**
     * Handles the hello to the server -> this method sends HELLO to server
     * @throws ServerUnavailableException if IO error occurs
     * @throws ProtocolException if the received message does not comply to HELLO + DELIMETER + name
     */
    public void handleHello() throws ServerUnavailableException, ProtocolException;

    /**
     * Sends an EXIT to the server after which it closes the connection
     * @throws ServerUnavailableException if IO error occurs
     */
    public void sendExit() throws ServerUnavailableException;
}
