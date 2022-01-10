package view;

import Model.*;
import Model.Player;
import Model.tools.TextIO;

import java.util.Locale;
import java.util.Scanner;
public class Input {
    Scanner sc;

    public Input(Scanner sc){
        this.sc = sc;
    }

    public void startGame(Game game){
        int numPlayers = sc.nextInt();
        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Type the name of the next player:\n");
            String name = sc.nextLine();
            game.addPlayer(new Player(name, game.getTilebag()));
        }
    }

    public String[] getMove(){
        System.out.println("Type start square (12A) followed by (H)orizontal or (V)ertical and finally the word you want to place");
        do {
            String move = sc.nextLine();
            String[] moveArr = move.split(" ");
            if (moveArr.length == 3 && (moveArr[1].toUpperCase(Locale.ROOT).equals("V") || moveArr[1].toUpperCase(Locale.ROOT).equals("V"))) {
                for (int let = 65; let < 80; let++){
                    for (int row = 1; row < 16; row++){
                        if(moveArr[0].contains(Character.toString((char) let)) && moveArr[0].contains(Integer.toString(row))){
                            return moveArr;
                        }
                    }
                }
            }
            System.out.println("Invalid syntax try again!");
        } while (true);
    }


}
