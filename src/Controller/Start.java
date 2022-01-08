package Controller;
import Model.*;
import view.*;

public class Start {

    public static void main(String[] args) {
        new Start();
    }
    public Game game;
    public TUI tui;
    public Input input;

    public Start(){
        game = new Model.Game();
        tui = new view.TUI();
        input = new view.Input();
        tui.start();
        input.startGame(game);
        game.start();
    }
}
