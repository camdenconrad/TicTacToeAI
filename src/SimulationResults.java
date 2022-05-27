import java.util.concurrent.atomic.AtomicInteger;

public class SimulationResults {
    private int index;
    private final AtomicInteger moves;

    public SimulationResults(int index, AtomicInteger moves) {
        this.index = index;
        this.moves = moves;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public AtomicInteger getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves.set(moves);
    }

}
