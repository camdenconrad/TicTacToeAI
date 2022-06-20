import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Simulation {
    private final AtomicBoolean doDefense = new AtomicBoolean(false);
    private final AtomicBoolean doesWin = new AtomicBoolean(false);

    private SimulationResults DEFENSIVE_RESULTS;

    public Simulation() {
    }

    synchronized public int run(Host board) throws InterruptedException {

        ArrayList<SimulationResults> simulationResults = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            System.out.println("Running simulation " + (i + 1));
            simulationResults.add(simulation(board));
            try {
                if (simulationResults.get(i).equals(this.DEFENSIVE_RESULTS)) {
                    if (this.doDefense.get()) {
                        this.doDefense.set(false);
                        this.doesWin.set(false);
                        System.err.println("Defensive Result: " + DEFENSIVE_RESULTS.index());
                        return DEFENSIVE_RESULTS.index();
                    }
                }
            } catch (NullPointerException ignored){}
        }

//        System.out.println(this.doDefense.get());
//        if(this.doDefense.get()) {
//            this.doDefense.set(false);
//            this.doesWin.set(false);
//            return DEFENSIVE_RESULTS.index();
//        }

        ArrayList<Occurrences> uniqueMoves = new ArrayList<>();
        ArrayList<Integer> uniques = new ArrayList<>();

        for (SimulationResults result : simulationResults) {
            if (!uniques.contains(result.index())) {
                uniques.add(result.index());
                uniqueMoves.add(new Occurrences(result, 1));
            } else {
                for (Occurrences occurrence : uniqueMoves) {
                    if (occurrence.result().index() == result.index()) {
                        occurrence.increaseCount();
                    }
                }
            }
        }
        for (Integer result : uniques) {
            System.out.println(result);
        }
        System.out.println("/////");

        for (Occurrences occurrence : uniqueMoves) {
            System.out.println(occurrence);
        }

        int highestOccurrence = 0;
        int highestOccIndex = 0;
        for (Occurrences occurrence : uniqueMoves) {
            if (occurrence.getCount() > highestOccurrence) {
                highestOccurrence = occurrence.getCount();
                highestOccIndex = uniqueMoves.indexOf(occurrence);
            }
        }

        System.out.println("HIGHEST OCCURRENCE: " + highestOccurrence);
        System.out.println(uniqueMoves.get(highestOccIndex));


        return uniqueMoves.get(highestOccIndex).result().index();
    }

    synchronized private SimulationResults simulation(Host board) throws InterruptedException {
        AtomicInteger neededMoves;
        String localSeed = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
        ArrayList<UI> games = new ArrayList<>();

        SimHost simulationHost = new SimHost(localSeed, board);

        //simulationHost.bumpTurn();
        games.add(new UI(localSeed));
        games.get(0).setBot();
        games.get(0).setX();
        games.add(new UI(localSeed));
        games.get(1).setBot();
        games.get(1).setO();
        games.get(1).setVisible();

        simulationHost.addPlayer(games.get(0));
        simulationHost.addPlayer(games.get(1));

        this.wait(5000);

        neededMoves = games.get(1).getTotalMoves();
        //System.out.println(neededMoves);

        int selection = games.get(1).getBotSelection();
        System.out.println("From Simulation: " + selection);

        if(games.get(1).doesWin()) {
            selection = games.get(1).getLatestSelection();
            this.DEFENSIVE_RESULTS = new SimulationResults(selection, neededMoves);
            this.doDefense.set(true);
            this.doesWin.set(true);
            games.get(1).setVisible(false);
            games.clear();
            return this.DEFENSIVE_RESULTS;
        }

        if(games.get(1).isDefensive() && !doesWin.get()) {
            selection = games.get(0).getLatestSelection();
            this.DEFENSIVE_RESULTS = new SimulationResults(selection, neededMoves);
            this.doDefense.set(true);
            System.out.println(this.doDefense.get());
            games.get(1).setVisible(false);
            games.clear();
            return this.DEFENSIVE_RESULTS;
        }
        games.clear();


        return new SimulationResults(selection, neededMoves);
    }

}