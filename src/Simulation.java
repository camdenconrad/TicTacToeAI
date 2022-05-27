import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Simulation {

    private static final AtomicBoolean keepRunning = new AtomicBoolean();
    private static final AtomicBoolean doDefense = new AtomicBoolean();

    public Simulation() {
        keepRunning.set(false);
    }

    public static void update() {
        keepRunning.set(true);
    }

    public static void doDefense() {
        keepRunning.set(true);
        doDefense.set(true);

    }

    public int run(Host board) {
        int lowestIndex = 0;

        ArrayList<SimulationResults> simulationResults = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            simulationResults.add(simulation(board));
        }
        AtomicInteger lowestMoves = simulationResults.get(0).moves();
        for (int i = 0; i < 1; i++) {
            System.out.println(simulationResults.get(i).moves());
            if (simulationResults.get(i).moves().get() < lowestMoves.get() && simulationResults.get(i).moves().get() != 0) {
                lowestMoves = simulationResults.get(i).moves();
                lowestIndex = i;
            }

        }
        System.err.printf("\033[96m Lowest moves: %d\033[0m\n", lowestMoves.get());
        System.err.printf("\033[96m Chosen index: %d\033[0m\n", lowestIndex);
        System.err.println("\033[96m Simulation Finished\033[0m " + simulationResults.get(lowestIndex).index());

        return simulationResults.get(lowestIndex).index();
    }

    private SimulationResults simulation(Host board) {
        int timesRan = 0;
        AtomicInteger neededMoves;
        String localSeed = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
        ArrayList<UI> games = new ArrayList<>();

        SimHost simulationHost = new SimHost(localSeed, board);

        games.add(new UI(localSeed));
        games.get(0).setBot();
        games.get(0).setX();
        //games.get(0).setVisible();
        games.add(new UI(localSeed));
        games.get(1).setBot();
        games.get(1).setO();

        simulationHost.addPlayer(games.get(0));
        simulationHost.addPlayer(games.get(1));


        while (!keepRunning.get()) {
            if (timesRan > 5000) {
                return new SimulationResults(-1, new AtomicInteger(5000));
            }
            try {
                Thread.sleep(1, 0);
            } catch (InterruptedException ignored) {
            }
            timesRan++;
            if (doDefense.get()) {
                System.out.println("Did defensive play.");
                doDefense.set(false);
                int selection = games.get(0).getLatestSelection();
                games.clear();

                return new SimulationResults(selection, new AtomicInteger(-1));
            }
        }
        neededMoves = games.get(0).getTotalMoves();
        //System.out.println(neededMoves);
        keepRunning.set(false);

        int selection = games.get(0).getBotSelection();
        doDefense.set(false);

        games.clear();

        return new SimulationResults(selection, neededMoves);
    }
}