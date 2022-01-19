package Model;

public class IllegalMoveException extends Exception {
    private String msg;

    public IllegalMoveException(String msg){
        super(msg);
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Illegal move: " + msg;
    }
}
