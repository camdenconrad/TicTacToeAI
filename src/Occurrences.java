public class Occurrences {
    private final SimulationResults simulationResults;
    private int count = 0;
    private int index;

    public Occurrences(SimulationResults simulationResults, int count) {
        this.simulationResults = simulationResults;
        this.count = count;
        this.index = result().index();

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

    public int index() {
        return index;
    }

    public void addCount(int add) {
        this.count += add;
    }
}