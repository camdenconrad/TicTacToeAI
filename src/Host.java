import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Host {

    private final AtomicBoolean xsTurn = new AtomicBoolean(true);
    private final AtomicBoolean osTurn = new AtomicBoolean(false);
    ArrayList<Integer> board;
    private String seed;
    private int winner = 0;
    private Thread checker;

    @SuppressWarnings("BusyWait")
    public Host(String seed) {


        this.seed = seed;
        Linker.addHost(this);

        board = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            board.add(i, 0);
        }

        Thread checker = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1, 0);
                } catch (InterruptedException ignored) {
                }
                // both sides lose
                boolean allTurnsTaken = true;
                for (Integer num : board) {
                    if (num == 0) {
                        allTurnsTaken = false;
                        break;
                    }
                }
                if (allTurnsTaken) {
                    winner = 0;
                    resetBoard();
                }
            }
        });
        checker.start();

    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public ArrayList<Integer> getBoard() {
        return board;
    }

    public int getWinner() {
        return winner;
    }

    public AtomicBoolean getXsTurn() {
        return this.xsTurn;
    }

    public AtomicBoolean getOsTurn() {
        return this.osTurn;
    }

    public AtomicBoolean isXsTurn() {
        //System.err.println("\033[31m Xs Turn\033[0m");
        return xsTurn;
    }

    public AtomicBoolean isOsTurn() {
        //System.err.println("\033[96m Os Turn\033[0m");
        return osTurn;
    }

    public void setPos(int z) {
        if (board.get(z) == 0) {
            if (xsTurn.get()) {
                xsTurn.set(false);
                osTurn.set(true);
                board.set(z, 1);
            } else if (osTurn.get()) {
                osTurn.set(false);
                xsTurn.set(true);
                board.set(z, 2);
            }
        }
    }

    public void setPos(int z, int set) {
        board.set(z, set);
    }

    public int check(int pos) {
        return board.get(pos);
    }


    public boolean doCheck() {

        for (int i = 0; i < 3; i++) {
            // ROWS
            if (areEqual(board.get((i * 3)), board.get(1 + (i * 3)), board.get(2 + (i * 3))))
                return true;
            // COLUMNS
            if (areEqual(board.get(i), board.get(3 + i), board.get(6 + i)))
                return true;
        }

        //Diagonal
        if (areEqual(board.get(0), board.get(4), board.get(8)))
            return true;
        return areEqual(board.get(2), board.get(4), board.get(6));

        /*

        [0][1][2]
        [3][4][5]
        [6][7][8]
     */
    }

    private boolean areEqual(int x, int y, int z) {
        if ((x == y) && (y == z)) {
            if (x != 0) {
                winner = x;
                return true;
            }
        }
        return false;
    }

    public void print() {
        //System.out.println("[" + board.get(0) + "]" + "[" + board.get(1) + "]" + "[" + board.get(2) + "]");
        //System.out.println("[" + board.get(3) + "]" + "[" + board.get(4) + "]" + "[" + board.get(5) + "]");
        //System.out.println("[" + board.get(6) + "]" + "[" + board.get(7) + "]" + "[" + board.get(8) + "]\n");
    }

    public void resetBoard() {
        //System.err.println("\033[31m Board was reset.\033[0m");
        for (int i = 0; i < 9; i++) {
            board.set(i, 0);
        }
    }


}
