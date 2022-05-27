public class SimHost extends Host {


    private final Host cloneFrom;

    public SimHost(String seed, Host board) {
        super(seed);
        this.cloneFrom = board;
    }

    public void resetBoard() {
        // clone board
        for (int i = 0; i < 9; i++) {
            //assert false;
            this.setPos(i, cloneFrom.getBoard().get(i));
        }
    }


}
