import java.util.ArrayList;

public class Player {
    private String name;
    private int score;
    private ArrayList<Tile> tileRack = new ArrayList<>();

    public Player(String name, TileBag tileBag) {
        this.name = name;
        this.score = 0;

        for (int i = 0; i < 7; i++) {
            tileRack.add(tileBag.takeOutTile());
        }
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String toString() {
        String result = name + " -";
        for (Tile tile : tileRack) {
            result += tile.getLetter();
        }
        return result;
    }

    public void addScore(int newScore) {
        this.score += newScore;
    }
}
