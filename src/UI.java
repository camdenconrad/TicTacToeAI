import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class UI {

    private final ArrayList<Status> buttons = new ArrayList<>();
    private final JDialog frame = new JDialog();
    private final AtomicInteger totalMoves = new AtomicInteger(0);
    public Thread updateUI;
    private int botSelection;
    private int latestSelection;
    private boolean isBot;
    private boolean isSmartBot;
    private AtomicBoolean isTurn;
    private Host host;

    public UI(String seed) {

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
            buttons.setBorderPainted(false);
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
        frame.setTitle("X");
        this.isTurn = host.getXsTurn();
    }

    public void setO() {
        frame.setTitle("O");
        this.isTurn = host.getOsTurn();
    }

    public void linkTo(String seed) {
        this.host = Linker.linkTo(seed, this);
    }

    @SuppressWarnings("BusyWait")
    private void updateUI() {
        updateUI = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(0, 1);
                } catch (InterruptedException ignored) {
                }
                for (Status button : buttons) {
                    if (host.check(button.getPosition()) == 1) {
                        button.setBackground(new Color(105, 170, 205));
                    }
                    if (host.check(button.getPosition()) == 2) {
                        button.setBackground(new Color(25, 120, 105));
                    }
                    if (host.check(button.getPosition()) == 0) {
                        button.setBackground(new Color(255, 255, 255));
                    }

                }
                if (host.doCheck()) {
                    if (host.getWinner() != 1) {
                        frame.setTitle("O Wins");
                        if ((host instanceof SimHost) && (getOpponent().totalMoves.get() == 1)) {
                            Simulation.doDefense();
                            break;
                        }
                    } else if (host.getWinner() == 1) {
                        frame.setTitle("X Wins");
                        if (host instanceof SimHost) {
                            Simulation.update();
                            break;
                        }

                    } else
                        host.resetBoard();
                    if (totalMoves.get() > 8) {
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
                    chooseSimulatedButton();
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

    private void chooseSimulatedButton() {
        int simulationResult = new Simulation().run(host);
        if (simulationResult != -1) {
            if (totalMoves.get() == 0) {
                botSelection = simulationResult;
            }
            latestSelection = simulationResult;
            buttons.get(simulationResult).doClick();
        } else chooseRandomButton();
    }

    private void chooseRandomButton() {

        int localSelection;
        localSelection = ThreadLocalRandom.current().nextInt(0, 9);

        while (host.check(localSelection) != 0) {
            localSelection = new Random().nextInt(9);
        }

        //System.err.printf("\033[10m chose random %d\033[0m\n", localSelection);
        //System.out.println(host.check(localSelection));


        botSelection = localSelection;
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
}
