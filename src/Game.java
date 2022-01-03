import java.util.ArrayList;

public class Game {
    private TileBag tilebag;
    private ArrayList<Player> players = new ArrayList<>();

    public Game() {
        this.tilebag = new TileBag();
    }

    public void addPlayer(Player player) {
        if (player == null) {
            System.out.println("speler is nul!! :(");
        }
        players.add(player);
        System.out.println("Player " + player.getName() + " has been added to the game!");
    }

    public TileBag getTilebag() {
        return tilebag;
    }

    /**
     * Starts the game
     */
    public void start() {
        System.out.println(tilebag.toString());
        System.out.println("Total length tilebag: " + tilebag.getSize());

        for (Player player : players) {
            System.out.println(player.toString());
        }
    }
}
