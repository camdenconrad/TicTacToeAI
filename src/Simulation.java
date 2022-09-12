import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Simulation {
    private final AtomicBoolean doesWin = new AtomicBoolean(false);
    private final AtomicInteger simsRun = new AtomicInteger();
    ArrayList<UI> simGames = new ArrayList<>();
    private ThreadLocal<Boolean> isRunning = ThreadLocal.withInitial(() -> false);

    private final ThreadLocal<Long> simTime = new ThreadLocal<>();

    public Simulation() {

    }

    public int getSimsRun() {
        return simsRun.get();
    }

    public boolean getIsRunning() {
        return isRunning.get();
    }

    public int run(Host board) throws IOException {
        simGames.clear();

        Main.setStartTime();
        Main.startTime();

        simTime.set(0L);
        simsRun.set(0);

        ArrayList<SimulationResults> simulationResults = new ArrayList<>();
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < Main.runs; i++) {
            //System.out.println("Running simulation " + (i + 1));
            threads.add(new Thread(() -> simulationResults.add(simulation(board))));
            threads.get(threads.size()-1).start();

        }
        while (threads.size() > 0) {
            int localSize = threads.size();
            threads.removeIf(local -> !local.isAlive());
            if(threads.size() < localSize) {
                simsRun.addAndGet(localSize-threads.size());
            }
            //System.out.println(threads.size());
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

        ArrayList<Occurrences> forRotations = new ArrayList<>();
        for(int i = 0; i < 9; i++) {
            forRotations.add(null);
        }
        for (Occurrences occurrence : uniqueMoves) {
            try {
                forRotations.set(occurrence.index(), occurrence);
            } catch (IndexOutOfBoundsException ignored){}
        }



        IO results = new IO(board, forRotations);

        // rotations
        forRotations = rotate(forRotations);
        board.rotateBoard();
        new IO(board, forRotations);

        forRotations = rotate(forRotations);
        board.rotateBoard();
        new IO(board, forRotations);

        forRotations = rotate(forRotations);
        board.rotateBoard();
        new IO(board, forRotations);

        //rotate back in place
        board.rotateBoard();

        board.removePlayers(this.simGames);
        //Linker.clearSims();

        simsRun.set(0);
        Main.stopTime();

        return results.getHighestOccurrence();
    }

    private SimulationResults simulation(Host board) {

        isRunning = ThreadLocal.withInitial(() -> true);

        AtomicInteger neededMoves;
        String localSeed = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
        ArrayList<UI> games = new ArrayList<>();

        SimHost simulationHost = new SimHost(localSeed, board);

        games.add(new UI(localSeed));
        games.get(0).setBot();
        games.get(0).setX();

        simGames.add(games.get(0));

        games.add(new UI(localSeed));
        games.get(1).setBot();
        games.get(1).setO();
        //games.get(1).setVisible();

        simGames.add(games.get(1));

        simulationHost.addPlayer(games.get(0));
        simulationHost.addPlayer(games.get(1));

        long time = System.currentTimeMillis();

        // int localSelection = -1;
        while (games.get(0).isRunning().get()) {

            // timer, if simulation takes to long, break
            if (System.currentTimeMillis() > (time + 5000)) {
                break;
            }

        }

        neededMoves = games.get(1).getTotalMoves();

        int selection = games.get(1).getBotSelection();
        //System.out.println("From Simulation: " + selection);

        SimulationResults DEFENSIVE_RESULTS;
        if (games.get(1).doesWin()) {
            selection = games.get(1).getLatestSelection();
            DEFENSIVE_RESULTS = new SimulationResults(selection, neededMoves);
            //this.doDefense.set(true);
            //this.doesWin.set(true);
            games.get(1).setVisible(false);

            //Linker.seeHosts();

            isRunning.set(false);

            Linker.removeHost(simulationHost);
            //simulationHost.removePlayers(games);
            games.clear();

            return DEFENSIVE_RESULTS;
        }

        if (games.get(1).isDefensive() && !doesWin.get()) {
            selection = games.get(0).getLatestSelection();
            DEFENSIVE_RESULTS = new SimulationResults(selection, neededMoves);
            //this.doDefense.set(true);
            //System.out.println(this.doDefense.get());
            //games.get(1).setVisible(false);


            //Linker.seeHosts();
            isRunning.set(false);

            Linker.removeHost(simulationHost);
            //simulationHost.removePlayers(games);
            games.clear();

            return DEFENSIVE_RESULTS;
        }

        //games.get(1).setVisible(false);

        //Linker.seeHosts();

        isRunning.set(false);

        Linker.removeHost(simulationHost);
        //simulationHost.removePlayers(games);
        games.clear();

        return new SimulationResults(selection, neededMoves);
    }

    public ArrayList<Occurrences> rotate(ArrayList<Occurrences> toRotate) {
        int length = 0;
        ArrayList<Occurrences> result = null;
        try {
            length = (int) Math.sqrt(toRotate.size());
            result = new ArrayList<>();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        Occurrences[][] rotated = new Occurrences[length][length];
        int index = (length * length) - 1;

        for(int i = length; i > 0; i--) {
            for(int j = 0; j < length; j++) {
                rotated[i-1][j] = toRotate.get(index);
                try {
                    rotated[i - 1][j].setCount(toRotate.get(index).getCount());
                } catch (NullPointerException ignored) {
                    //rotated[i - 1][j].setCount(0);
                }
                index -= 3;
                if(index < 0) {
                    index += (length * length)-1;
                }

            }

        }

        for(int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                assert result != null;
                result.add(rotated[i][j]);
            }
        }


        return result;
    }


}