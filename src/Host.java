import java.util.ArrayList;

public class Host {

    private static Linker linker = new Linker();

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    private String seed;
    private final ArrayList<Integer> board;
    private int winner;
    private boolean xsTurn = true;
    private boolean osTurn = false;

    public Host() {
        this.seed = null;
        linker.addHost(this);

        board = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            board.add(i, 0);
        }
    }

    public Host(String seed) {
        this.seed = seed;
        linker.addHost(this);

        board = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            board.add(i, 0);
        }
    }

    public boolean isXsTurn() {
        return xsTurn;
    }

    public boolean isOsTurn() {
        return osTurn;
    }

    public void setPos(int z) {
        if(board.get(z) == 0) {
            if (xsTurn) {
                xsTurn = false;
                osTurn = true;
                board.set(z, 1);
            } else if (osTurn) {
                osTurn = false;
                xsTurn = true;
                board.set(z, 2);
            }
        }
    }

    public int check(int pos) {
        return board.get(pos);
    }


    public boolean doCheck() {

        // both sides lose
        boolean allTurnsTaken = true;
        for (int i = 0; i < 9; i++) {
            if (board.get(i) != 0) {
                allTurnsTaken = false;
                break;
            }
        }
        if(allTurnsTaken) {
            resetBoard();
        }

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
        System.out.println("[" + board.get(0) + "]" + "[" + board.get(1) + "]" + "[" + board.get(2) + "]");
        System.out.println("[" + board.get(3) + "]" + "[" + board.get(4) + "]" + "[" + board.get(5) + "]");
        System.out.println("[" + board.get(6)+ "]" + "[" + board.get(7) + "]" + "[" + board.get(8)+ "]\n");
    }

    public void resetBoard() {
        for (int i = 0; i < 9; i++) {
            board.set(i, 0);
        }
    }


}
