package view;

import Model.*;
import Model.Player;
import Model.tools.TextIO;

public class Input {
    public void startGame(Game game){
        int numPlayers = TextIO.getInt();
        for (int i = 0; i < numPlayers; i++) {
            System.out.println("Type the name of the next player:");
            String name = TextIO.getWord();
            game.addPlayer(new Player(name, game.getTilebag()));
        }
    }
}
