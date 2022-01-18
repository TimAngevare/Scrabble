package Model;

public class Position {
    private SquareType type;
    private Tile tile;
    private int row;
    private int col;

    public Position(SquareType type, int row, int col) {
        this.type = type;
        this.row = row;
        this.col = col;
    }

    /**
     * Checks if the tile's character has been assigned a value, and returning this boolean
     * @return if the tile is empty
     */
    public boolean isEmpty() {
        return tile == null;
    }

    public Tile getTile(){
        return tile;
    }

    public SquareType getType() {
        return type;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    /**
     * Places a tile on the position
     * @param tile the tile to be placed
     */
    public void placeTile(Tile tile) {
        this.tile = tile;
    }
}
