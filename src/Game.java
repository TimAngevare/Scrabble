public class Game {
    private TileBag tilebag;
    private Player[] players;

    public Game(Player[] players) {
        this.players = players;
        this.tilebag = new TileBag();
    }
}
