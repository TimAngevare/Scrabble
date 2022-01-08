package Controller;
import Model.*;
import view.*;

public class Start {
    public Game game;
    public TUI tui;
    public Input input;

    public static void main(String[] args) {
        new Start();
    }


    public Start(){
        game = new Model.Game();
        tui = new view.TUI();
        input = new view.Input();
        tui.start();
        input.startGame(game);
        this.update();
    }

    public void update() {
        System.out.println(game.getTilebag().toString());
        System.out.println("Total length tilebag: " + game.getTilebag().getSize());

        for (Player player : game.getPlayers()) {
            System.out.println(player.toString());
        }

        tui.drawBoard(game.getBoard());
    }
}
