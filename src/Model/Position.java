package Model;

public class Position {
    private SquareType type;
    private Tile tile;

    public Position(SquareType type) {
        this.type = type;
    }

    /**
     * Checks if the tile's character has been assigned a value, and returning this boolean
     * @return if the tile is empty
     */
    public boolean isEmpty() {
        return tile == null;
    }

    public SquareType getType() {
        return type;
    }

    /**
     * Places a tile on the position
     * @param tile the tile to be placed
     */
    public void placeTile(Tile tile) {
        this.tile = tile;
    }
}