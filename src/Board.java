import java.sql.Array;

public class Board {
    private Square[][] board;
    private final int length;
    private final int mid;

    public Board() {
        fill();
        this.length = 15;
        this.mid = length / 2;
    }

    private Square[][] fill(){
        this.board = new Square[length][length];
        for (int x = 1; x <= length; x++){
            for (int y = 1; y <= length; y++){
                if (x == mid && y == mid){
                    board[x][y] = Square.START;
                } else if ((x < 1 && x < 6 && y == x) || (x < 10 && x < 15 && x == y)) {
                    board[x][y] = Square.DOUBLE_WORD;
                } else if (((x == 1 || x == length) && (y == 1 || y == length || y == mid)) || (x == mid && (y == 1 || y == length))){
                    board[x][y] = Square.TRIPLE_WORD;
                } else if (((y == 2 || y == 10) && (x == 6 || x == 10)) || ((y == 6 || y == 10) && x % 4 == 2)){
                    board[x][y] = Square.TRIPLE_LETTER;
                } else if (((x == 1 || x == length) && (y == 4 || y == 12)) || ((x == 3 || x == 13) && (y == length - 1 || y == length + 1)) || ((x == mid - 4 || x == mid + 4) && (y == 1 || y == length || y == mid)) || ((x == mid - 1 || x == mid + 1) && (y == 3 || y == length - 3 || y == mid + 1 || y == mid - 1)) || (x == mid && (y == mid - (mid / 2) || y == mid + (mid / 2)))){
                    board[x][y] = Square.DOUBLE_LETTER;
                } else {
                    board[x][y] = Square.BLANK;
                }
            }
        }
        return board;
    }
}
