package Model;

public class Board {
    private Position[][] board;
    private static final int LENGTH = 15;

    public Board() {
        this.board = newBoard();
    }

    public Position getPosition (int x, int y){
        return board[y][x];
    }
    /**
     * Returns a filled board with tiles with correct square types
     * @return an array with an array of tiles
     */
    private Position[][] newBoard(){
        Position[][] newBoard = new Position[LENGTH][LENGTH];
        for (int x = 0; x < LENGTH; x++){
            for (int y = 0; y < LENGTH; y++){
                newBoard[y][x] = new Position(checkSquareType(x, y));
            }
        }
        return newBoard;
    }

    /**
     * Returns the corresponding square type at a certain location
     * @param x the x value of the desired location
     * @param y the y value of the desired location
     * @return the square type at the given location
     */
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

    /**
     * Checks if the tile at the given coordinates is empty
     * @param x the desired x coordinate
     * @param y the desired y coordinate
     * @return whether the tile at the coordinate is empty
     */
    public boolean isEmptyField(int x, int y) {
        return board[y][x].isEmpty();
    }
}
