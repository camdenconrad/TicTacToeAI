import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Simulation {
    private final AtomicBoolean keepRunning = new AtomicBoolean();
    private final AtomicBoolean doDefense = new AtomicBoolean();

    public Simulation() {
        keepRunning.set(false);
    }

    public void update() {
        keepRunning.set(true);
        //this.notify();
    }

    public void doDefense() {
        keepRunning.set(true);
        doDefense.set(true);

    }

    synchronized public int run(Host board) throws InterruptedException {

        ArrayList<SimulationResults> simulationResults = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            System.out.println("Running simulation " + (i + 1));
            simulationResults.add(simulation(board));
        }

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
        int timesRan = 0;
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
        //games.get(1).setVisible();

        simulationHost.addPlayer(games.get(0));
        simulationHost.addPlayer(games.get(1));

        this.wait(3000);
//        while (!keepRunning.get()) {
//            System.out.println(keepRunning.get());
//            if (timesRan > 50) {
//                return new SimulationResults(-1, new AtomicInteger(5000));
//            }
//            try {
//                Thread.sleep(100, 0);
//            } catch (InterruptedException ignored) {
//            }
//            timesRan++;
//            if (doDefense.get()) {
//                System.out.println("Did defensive play.");
//                doDefense.set(false);
//                int selection = games.get(1).getLatestSelection();
//                games.clear();
//
//                return new SimulationResults(selection, new AtomicInteger(-2));
//            }
//        }
        neededMoves = games.get(1).getTotalMoves();
        //System.out.println(neededMoves);
        keepRunning.set(false);

        int selection = games.get(1).getLatestSelection();
        System.out.println("From Simulation: " + selection);
        System.out.println(games.get(1));
        doDefense.set(false);

        games.clear();

        return new SimulationResults(selection, neededMoves);
    }
}