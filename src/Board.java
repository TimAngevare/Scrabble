public class Board {
    private SquareType[][] board;
    private static final int LENGTH = 15;

    public Board() {
        board = fill();
    }

    private SquareType[][] fill(){
        this.board = new SquareType[LENGTH][LENGTH];
        for (int x = 0; x < LENGTH; x++){
            for (int y = 0; y < LENGTH; y++){
                board[x][y] = checkSquareType(x, y);
            }
        }
        return board;
    }

    public SquareType checkSquareType(int x, int y) {
        int mid = Math.round(LENGTH / 2);
        if (x == mid && y == mid){
            return SquareType.START;
        } else if (y == x && ((x > 0 && x < mid - 2) || (x > mid + 2 && x < LENGTH - 2))) {
            return SquareType.DOUBLE_WORD;
        } else if ((x == 0 || x == LENGTH - 1 || x == mid) && (y == 0 || y == LENGTH - 1 || y == mid)){
            return SquareType.TRIPLE_WORD;
        } else if ((x == mid + 2 || x == mid - 2) && (y == 1 || y == LENGTH - 2) || ((y == mid + 2 || y == mid - 2) && x % 4 == 1)) {
            return SquareType.TRIPLE_LETTER;
        } else if (((x == mid + 1 || x == mid - 1) && (y == mid - 1 || y == mid + 1)) || ((x == mid - 4 || y == mid - 4 || x == mid + 4 || y == mid + 4) && (y == 0 || x == 0)) || ((y == mid - 1 || y == mid + 1) && (x == 2 || x == LENGTH - 3)) || ((x == mid - 1 || x == mid + 1) && (y == 2 || y == LENGTH - 3)) || (y == mid && (x == 3 || x == LENGTH - 4))|| (x == mid && (y == 3 || y == LENGTH - 4)) ) {
            return SquareType.DOUBLE_LETTER;
        } else {
            return SquareType.BLANK;
        }
    }
}