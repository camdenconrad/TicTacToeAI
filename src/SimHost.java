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
            board.set(i, 0);
        }

        // clone board
        for (int i = 0; i < 9; i++) {
//
//            if(cloneFrom.getBoard().get(i) == 1) {
//                this.setPos(i, 2);
//            }
//            else if(cloneFrom.getBoard().get(i) == 2) {
//                this.setPos(i, 1);
//            } else
            this.setPos(i, cloneFrom.getBoard().get(i));
        }
    }


}
