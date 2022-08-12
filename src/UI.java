import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class UI {

    private final ArrayList<Status> buttons = new ArrayList<>();
    private final JDialog frame = new JDialog();
    private final AtomicInteger totalMoves = new AtomicInteger(0);
    private final Simulation simulation;
    public Thread updateUI;
    private int botSelection;
    private int latestSelection;
    private boolean isBot;
    private boolean isSmartBot;
    private AtomicBoolean isTurn;
    private Host host;

    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    public boolean doesWin() {
        return doesWin;
    }

    private boolean doesWin;

    public boolean isDefensive() {
        return isDefensive;
    }

    private boolean isDefensive;


    public UI(String seed) {

        simulation = new Simulation();

        linkTo(seed);

        frame.setLayout(new GridLayout(3, 3));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.height - 100, screenSize.height - 100);
        //frame.setResizable(false);

        for (int i = 0; i < 9; i++) {
            buttons.add(new Status(i));
        }

        for (Status buttons : buttons) {
            frame.add(buttons);
            buttons.setBackground(new Color(255, 255, 255));
            //buttons.setBorderPainted(false);
            //buttons.setText(String.valueOf(j));
        }

        for (Status buttons : buttons) {
            Listener(buttons);
        }

        updateUI();

    }

    public UI getOpponent() {
        return host.findOpponent(this);
    }

    public AtomicInteger getTotalMoves() {
        return totalMoves;
    }

    public int getBotSelection() {
        return botSelection;
    }

    public void setVisible() {
        frame.setVisible(true);
    }

    public void setBot() {
        isBot = true;
        frame.setTitle("Simulation");
    }

    public void setSmartBot() {
        isSmartBot = true;
    }

    public void setX() {
        frame.setTitle("TicTacToe - Player X");
        this.isTurn = host.getXsTurn();
    }

    public void setO() {
        frame.setTitle("TicTacToe - Player O");
        this.isTurn = host.getOsTurn();
    }

    public void linkTo(String seed) {
        this.host = Linker.linkTo(seed, this);
    }

    @SuppressWarnings("BusyWait")
    synchronized private void updateUI() {
        updateUI = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(0, 1);
                } catch (InterruptedException ignored) {
                }
                for (Status button : buttons) {
                    if (host.check(button.getPosition()) == 1) {
                        button.setBackground(new Color(189, 147, 226));
                    }
                    if (host.check(button.getPosition()) == 2) {
                        button.setBackground(new Color(227, 147, 74));
                    }
                    if (host.check(button.getPosition()) == 0) {
                        button.setBackground(new Color(255, 255, 255));
                    }

                }
                if (host.doCheck()) {
                    if (host.getWinner() != 1) {
                        frame.setTitle("O Wins");
                        if (host instanceof SimHost) {
                            if(this.totalMoves.get() == 1) {
                                botSelection = this.getLatestSelection();
                                this.doesWin = true;
                                this.isDefensive = true;
                            }

                            this.isRunning.set(false);
                            frame.dispose();
                            break;
                        }
                    } else if (host.getWinner() <= 3) {
                        frame.setTitle("X Wins");
                        if ((host instanceof SimHost) && (getOpponent().totalMoves.get() == 1)) {
                            System.out.println("Defense triggered");
                            botSelection = getOpponent().getBotSelection();
                            this.isDefensive = true;

                            this.isRunning.set(false);
                            frame.dispose();
                            break;
                        }

                        this.isRunning.set(false);
//                        synchronized(simulation) {
//                            simulation.notifyAll();
//                        }

                    } else {
                        host.resetBoard();
                        totalMoves.set(0);
                    }

                    host.resetBoard();
                    for (Status status : buttons) {
                        status.setEnabled(true);
                    }
                }

                for (Status status : buttons) {
                    status.setEnabled(isTurn.get());
                }

                if (isTurn.get() && isSmartBot) {
                    try {
                        chooseSimulatedButton();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (isTurn.get() && isBot) {
                    chooseRandomButton();
                }
            }
        });
        updateUI.start();
    }

    public int getLatestSelection() {
        return latestSelection;
    }

    private void chooseSimulatedButton() throws InterruptedException, IOException {
        int simulationResult = this.simulation.run(host);
        if (simulationResult != -1) {
            buttons.get(simulationResult).doClick();
        } else chooseRandomButton();
        //System.out.println(botSelection);
        //new IO(host);
    }

    private void chooseRandomButton() {

        int localSelection;
        localSelection = ThreadLocalRandom.current().nextInt(0, 9);

        while (host.check(localSelection) != 0) {
            localSelection = new Random().nextInt(9);
        }
        //System.out.println("Random selection: " + localSelection);

        //System.err.printf("\033[10m chose random %d\033[0m\n", localSelection);
        //System.out.println(host.check(localSelection));


        if (totalMoves.get() == 1) {
            //System.out.println("0 move selection: " + localSelection);
            botSelection = localSelection;
        }
        latestSelection = localSelection;
        buttons.get(localSelection).doClick();
        totalMoves.addAndGet(1);


    }


    private void Listener(Status button) {
        button.addActionListener(e -> {
            if (host.isXsTurn().get()) {
                button.setX();
                host.setPos(button.getPosition());
            } else if (host.isOsTurn().get()) {
                button.setO();
                host.setPos(button.getPosition());
            }
            host.print();
        });
    }

    public void setTitle(String tic_tac_toe) {
        frame.setTitle(tic_tac_toe);
    }

    public void setVisible(boolean b) {
        this.frame.setVisible(b);
    }

    public AtomicBoolean isRunning() {
        return isRunning;
    }
}
