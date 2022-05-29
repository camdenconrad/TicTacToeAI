public class Occurrences {
    private final SimulationResults simulationResults;
    private int count = 0;

    public Occurrences(SimulationResults simulationResults, int count) {
        this.simulationResults = simulationResults;
        this.count = count;

    }

    public void increaseCount() {
        count++;
    }

    public SimulationResults result() {
        return simulationResults;
    }

    public int getCount() {
        return count;
    }

    public String toString() {
        return (simulationResults.index() + " " + count);
    }
}