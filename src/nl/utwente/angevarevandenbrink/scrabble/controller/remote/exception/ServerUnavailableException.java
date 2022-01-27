package nl.utwente.angevarevandenbrink.scrabble.controller.remote.exception;

public class ServerUnavailableException extends Exception{
    public ServerUnavailableException(String msg) {
        super(msg);
    }
}
