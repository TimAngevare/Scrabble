package nl.utwente.angevarevandenbrink.scrabble.model;

import java.util.ArrayList;

public class Bot extends Player {

    public Bot(String name, TileBag tileBag) {
        super("", tileBag);
        setName(getName());
    }

    public String getName(){
        return "Bot" + (int)Math.random()*150;
    }

    public void makeMove(Board board){

    }

}
