public class SimHost extends Host {


    private final Host cloneFrom;

    public SimHost(String seed, Host board) {
        super(seed);
        this.cloneFrom = board;
        this.resetBoard();
    }

    public void bumpTurn() {
        super.getOsTurn().set(!getOsTurn().get());
        super.getXsTurn().set(!getXsTurn().get());
    }

    public void resetBoard() {
        for (int i = 0; i < 9; i++) {
            this.setPos(i, cloneFrom.getBoard().get(i));
        }
        //printPlayers();
    }


}
