import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Simulation {

    private static final AtomicBoolean keepRunning = new AtomicBoolean();


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

    private static final AtomicBoolean doDefense = new AtomicBoolean();

    public int run(Host board) {
        int lowestIndex = 0;

        ArrayList<SimulationResults> simulationResults = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            simulationResults.add(simulation(board));
        }
        AtomicInteger lowestMoves = simulationResults.get(0).getMoves();
        for (int i = 0; i < 1; i++) {
            System.out.println(simulationResults.get(i).getMoves());
            if (simulationResults.get(i).getMoves().get() < lowestMoves.get() && simulationResults.get(i).getMoves().get() != 0) {
                lowestMoves = simulationResults.get(i).getMoves();
                lowestIndex = i;
            }

        }
        System.err.printf("\033[96m Lowest moves: %d\033[0m\n", lowestMoves.get());
        System.err.printf("\033[96m Chosen index: %d\033[0m\n", lowestIndex);
        System.err.println("\033[96m Simulation Finished\033[0m");

        return simulationResults.get(lowestIndex).getIndex();
    }

    private SimulationResults simulation(Host board) {
        int timesRan = 0;
        AtomicInteger neededMoves;
        String localSeed = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
        ArrayList<UI> games = new ArrayList<>();

        new SimHost(localSeed, board);

        games.add(new UI(localSeed));
        games.get(0).setBot(true);
        games.get(0).setX();
        //games.get(0).setVisible();
        games.add(new UI(localSeed));
        games.get(1).setBot(true);
        games.get(1).setO();

        while (true) {
            if (keepRunning.get()) break;
//            if(timesRan > 1000000000) {
//                return new SimulationResults(-1, 1000000000);
//            }
//            timesRan++;
        }
        neededMoves = games.get(0).getTotalMoves();
        //System.out.println(neededMoves);
        keepRunning.set(false);

        int selection;
        if (doDefense.get()) {
            selection = games.get(1).getBotSelection();
            System.out.println("Did defensive play.");
        } else
            selection = games.get(0).getBotSelection();
        doDefense.set(false);

        games.clear();

        return new SimulationResults(selection, neededMoves);
    }
}