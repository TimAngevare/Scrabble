package Model;

public class TilePlacement {
    private Position position;
    private Tile tile;

    public TilePlacement(Position pos, Tile tile) {
        this.position = pos;
        this.tile = tile;
    }

    public Position getPosition() {
        return position;
    }

    public Tile getTile() {
        return tile;
    }
}
