package nl.utwente.angevarevandenbrink.scrabble.exception;

public class IllegalBotMoveException extends IllegalMoveException{
    public IllegalBotMoveException(String msg) {
        super(msg);
    }
}
