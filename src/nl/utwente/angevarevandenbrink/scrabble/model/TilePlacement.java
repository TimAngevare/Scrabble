package nl.utwente.angevarevandenbrink.scrabble.model;

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

    public String toString() {
        return position.toString() + " - " + tile.getLetter();
    }
}
