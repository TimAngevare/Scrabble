package nl.utwente.angevarevandenbrink.scrabble.model.exception;

public class IllegalBotMoveException extends IllegalMoveException{
    public IllegalBotMoveException(String msg) {
        super(msg);
    }
}
