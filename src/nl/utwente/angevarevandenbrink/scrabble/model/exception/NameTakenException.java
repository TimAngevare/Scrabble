package nl.utwente.angevarevandenbrink.scrabble.model.exception;

public class NameTakenException extends Exception{
    public NameTakenException(String msg){
        super(msg);
    }
}
