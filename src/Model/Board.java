package Model;

public class Board {
    private Position[][] board;
    private static final int LENGTH = 15;

    public Board() {
        this.board = newBoard();
    }

    public Position getPosition (int row, int col){
        return board[row][col];
    }
    /**
     * Returns a filled board with tiles with correct square types
     * @return an array with an array of tiles
     */
    private Position[][] newBoard(){
        Position[][] newBoard = new Position[LENGTH][LENGTH];
        for (int row = 0; row < LENGTH; row++){
            for (int col = 0; col < LENGTH; col++){
                newBoard[row][col] = new Position(checkSquareType(row, col));
            }
        }
        return newBoard;
    }

    /**
     * Returns a copy of the board it was called on
     * @return a copy of the current board
     */
    public Position[][] cloneBoard(){
        Position[][] newBoard = new Position[LENGTH][LENGTH];
        for (int row = 0; row < LENGTH; row++){
            for (int col = 0; col < LENGTH; col++){
                newBoard[row][col] = this.board[row][col];
            }
        }
        return newBoard;
    }

    public boolean isEmpty(){
        for (int row = 0; row < LENGTH; row++){
            for (int col = 0; col < LENGTH; col++){
                if (!this.isEmptyField(row, col)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns the corresponding square type at a certain location
     * @param x the x value of the desired location
     * @param y the y value of the desired location
     * @return the square type at the given location
     */
    public SquareType checkSquareType(int y, int x) {
        int mid = Math.round(LENGTH / 2);
        if (x == mid && y == mid){
            return SquareType.START;
        } else if ((y == x || ((y == LENGTH - x - 1))) && ((x > 0 && x < mid - 2) || (x > mid + 2 && x < LENGTH - 1))) {
            return SquareType.DOUBLE_WORD;
        } else if ((x == 0 || x == LENGTH - 1 || x == mid) && (y == 0 || y == LENGTH - 1 || y == mid)){
            return SquareType.TRIPLE_WORD;
        } else if ((x == mid + 2 || x == mid - 2) && (y == 1 || y == LENGTH - 2) || ((y == mid + 2 || y == mid - 2) && x % 4 == 1)) {
            return SquareType.TRIPLE_LETTER;
        } else if (((x == mid + 1 || x == mid - 1) && (y == mid - 1 || y == mid + 1)) || ((x == mid - 4 || x == mid + 4) && (y == 0 || y == LENGTH - 1) ) || ((y == mid - 4 || y == mid + 4) && (x == 0 || x == LENGTH - 1) ) || ((y == mid - 1 || y == mid + 1) && (x == 2 || x == LENGTH - 3)) || ((x == mid - 1 || x == mid + 1) && (y == 2 || y == LENGTH - 3)) || (y == mid && (x == 3 || x == LENGTH - 4))|| (x == mid && (y == 3 || y == LENGTH - 4)) ) {
            return SquareType.DOUBLE_LETTER;
        } else {
            return SquareType.BLANK;
        }
    }

    /**
     * Checks if the tile at the given coordinates is empty
     * @param row the desired row
     * @param col the desired col
     * @return whether the tile at the coordinate is empty
     */
    public boolean isEmptyField(int row, int col) {
        return board[row][col].isEmpty();
    }

    public boolean isValidPlacement(int row, int col, char letter) {
        return (isEmptyField(row, col) || getPosition(row, col).getTile().getLetter() == letter);
    }


}
