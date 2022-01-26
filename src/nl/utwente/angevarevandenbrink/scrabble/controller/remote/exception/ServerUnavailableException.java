package nl.utwente.angevarevandenbrink.scrabble.remote.exception;

public class ServerUnavailableException extends Exception{
    public ServerUnavailableException(String msg) {
        super(msg);
    }
}
