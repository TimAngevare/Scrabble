package nl.utwente.angevarevandenbrink.scrabble.model.exception;

public class InvalidUserInputException extends Exception{
    public InvalidUserInputException(String msg){
        super(msg);
    }
}
