package nl.utwente.angevarevandenbrink.scrabble.model;

import nl.utwente.angevarevandenbrink.scrabble.model.exception.InvalidWordException;

import java.util.ArrayList;
import java.util.HashMap;

public class Board {
    private Position[][] board;
    public static final int LENGTH = 15;

    public Board() {
        this.board = newBoard();
    }

    public Position getPosition (int row, int col){
        return board[row][col];
    }

    public static boolean isInBounds(int num) {
        return num >= 0 && num < LENGTH;
    }

    /**
     * Returns a filled board with tiles with correct square types
     * @return an array with an array of tiles
     */
    private Position[][] newBoard(){
        Position[][] newBoard = new Position[LENGTH][LENGTH];
        for (int row = 0; row < LENGTH; row++){
            for (int col = 0; col < LENGTH; col++){
                newBoard[row][col] = new Position(checkSquareType(row, col), row, col);
            }
        }
        return newBoard;
    }

    /**
     * Returns a copy of the board it was called on
     * @return a copy of the current board
     */
    public Board cloneBoard(){
        Board boardCopy = new Board();
        for (int row = 0; row < LENGTH; row++){
            for (int col = 0; col < LENGTH; col++){
                Tile tile = this.getPosition(row, col).getTile();
                if (tile != null){
                    boardCopy.getPosition(row, col).placeTile(tile);
                }

            }
        }
        return boardCopy;
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

    public ArrayList<Position> getNotEmptyPositions(){
        ArrayList<Position> result = new ArrayList<>();

        for (int row = 0; row < LENGTH; row++){
            for (int col = 0; col < LENGTH; col++){
                Position position = getPosition(row, col);
                if (!position.isEmpty()){
                    result.add(position);
                }
            }
        }

        return result;
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

    private String getHorizontalWord(int row, int col) throws InvalidWordException {
        String result1 = "";

        for (int i = 0; i < LENGTH; i++) {
            Position pos1 = board[row][i];

            if (pos1.isEmpty() && i < col) {
                result1 = "";
            } else if (pos1.isEmpty() && i > col) {
                break;
            } else {
                result1 += pos1.getTile().getLetter();
            }
        }

        return result1;
    }

    private String getVerticalWord(int row, int col) throws InvalidWordException {
        String result2 = "";

        for (int i = 0; i < LENGTH; i++) {
            Position pos2 = board[i][col];

            if (pos2.isEmpty() && i < row) {
                result2 = "";
            } else if (pos2.isEmpty() && i > row) {
                break;
            } else {
                result2 += pos2.getTile().getLetter();
            }
        }

        return result2;
    }

    /**
     * Checks if all words formed by a placement are valid
     * @param tpToCheck all TilePlacements to check
     * @param direction the direction of the new word, needed for optimization purposes
     * @return if it returns and does not throw, true
     * @throws InvalidWordException
     */
    public boolean checkFullWordsValid(ArrayList<TilePlacement> tpToCheck, String direction) throws InvalidWordException {
        if (tpToCheck.size() == 0) {
            throw new InvalidWordException("No placement of new letters?!?!");
        }

        int i_row = tpToCheck.get(0).getPosition().getRow();
        int i_col = tpToCheck.get(0).getPosition().getCol();


        if (direction.equals("V")) {
            boolean ver = Scrabble.checkWord(getVerticalWord(i_row, i_col));

            for (TilePlacement tp : tpToCheck) {
                String word_hor = getHorizontalWord(tp.getPosition().getRow(), tp.getPosition().getCol());

                if (word_hor.length() > 1) {
                    ver = Scrabble.checkWord(word_hor);
                }
            }
        } else {
            boolean hor = Scrabble.checkWord(getHorizontalWord(i_row, i_col));

            for (TilePlacement tp : tpToCheck) {
                String word_ver = getVerticalWord(tp.getPosition().getRow(), tp.getPosition().getCol());

                if (word_ver.length() > 1) {
                    hor = Scrabble.checkWord(word_ver);
                }
            }
        }

        return true;
    }

    /**
     * Checks if one board is equal to other.
     * @requires obj is of instance board
     * @param obj other board
     * @return if other board is equal to this board
     */
    @Override
    public boolean equals(Object obj) {
        Board other = (Board) obj;
        for (int row = 0; row < LENGTH; row++){
            for (int col = 0; col < LENGTH; col++){
                if(!(this.getPosition(row, col).getTile().getLetter() == other.getPosition(row, col).getTile().getLetter())){
                    return false;
                }
            }
        }
        return true;
    }
}
