package nl.utwente.angevarevandenbrink.scrabble.exception;

public class ServerUnavailableException extends Exception{
    public ServerUnavailableException(String msg) {
        super(msg);
    }
}
