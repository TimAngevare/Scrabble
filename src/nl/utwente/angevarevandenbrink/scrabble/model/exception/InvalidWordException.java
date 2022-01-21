package nl.utwente.angevarevandenbrink.scrabble.model.exception;

public class InvalidWordException extends Exception {

    public InvalidWordException(String msg){
        super(msg);
    }

    @Override
    public String toString() {
        return super.getMessage() + " is not a valid word";
    }
}
