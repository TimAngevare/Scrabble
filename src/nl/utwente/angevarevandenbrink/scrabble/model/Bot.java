package nl.utwente.angevarevandenbrink.scrabble.model;

public class Bot extends Player {
    public Bot(String name, TileBag tileBag) {
        super("", tileBag);
        setName(genName());
    }

    public String genName(){
        return "Bot" + (int)Math.random()*150;
    }

    public void makeMove(Board board){

    }
}
