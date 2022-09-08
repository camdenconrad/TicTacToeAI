import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Host {

    protected final ArrayList<UI> players = new ArrayList<>();
    private final AtomicBoolean xsTurn = new AtomicBoolean(true);
    private final AtomicBoolean osTurn = new AtomicBoolean(false);
    private final String seed;
    private final ThreadLocal<Boolean> isResetting = ThreadLocal.withInitial(() -> false);
    ArrayList<Integer> board;
    private int winner = 0;
    private int wins = 0;
    private int ties = 0;
    private int losses = 0;

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
                    Thread.sleep(0, 1);
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

    public int getWins() {
        return wins;
    }

    public int getTies() {
        return ties;
    }

    public int getLosses() {
        return losses;
    }

    public UI findOpponent(UI ui) {
        for (UI player : players) {
            if (!ui.equals(player)) {
                return player;
            }
        }
        return null;
    }

    public void addPlayer(UI player) {
        players.add(player);
    }

    public String getSeed() {
        return seed;
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
        this.updateScores();
        this.isResetting.set(true);

        //System.err.println("\033[31m Board was reset.\033[0m");
        for (int i = 0; i < 9; i++) {
            board.set(i, 0);
        }
        this.isResetting.set(false);
    }

    public String printBoard() {
        StringBuilder localString = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            localString.append(board.get(i));
        }

        return localString.toString();

    }

    public void clearPlayers() {
        this.players.clear();
    }

    public void removePlayers(ArrayList<UI> players) {
        for (UI player : players) {
            this.players.remove(player);
        }

    }

    public void printPlayers() {
        for (UI players : players) {
            System.out.println(players);
        }
    }

    public void updateScores() {

        if (winner == 2) {
            this.losses++;
        } else if (winner == 1) {
            this.wins++;
        } else {
            this.ties++;
        }
    }


    public boolean getIsResetting() {
        return this.isResetting.get();
    }
}
