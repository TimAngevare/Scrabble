package view;

import Model.Board;

public class TUI {
    BoardDraw boardDraw;

    public TUI(){
        this.boardDraw = new BoardDraw();
    }
    public void start() {
        System.out.println("Starting game!");
        System.out.println("With how many players do you wish to play? -");
    }
    public void drawBoard(Board board){
        boardDraw.drawBoard(board);
    }



}
