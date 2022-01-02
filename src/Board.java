import java.sql.Array;

public class Board {
    private Square[][] board;

    public Board() {
        this.board = this.fill();
    }

    private Square[][] fill(){
        Square[][] board = new Square[15][15];
        for (int x = 0; x < board.length; x++){
            for (int y = 0; y < board[x].length; y++){
                if (x == 7 && y == 7){
                    board[x][y] = Square.START;
                }
            }
        }
        return board;
    }
}
