import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class UI {

    public void setBot(boolean bot) {
        isBot = bot;
    }

    private boolean isBot;

    public void setX() {
        frame.setTitle("X");
        this.isTurn = host.getXsTurn();
    }

    public void setO() {
        frame.setTitle("O");
        this.isTurn = host.getOsTurn();
    }

    private AtomicBoolean isTurn;



    private Host host;
    private final ArrayList<Status> buttons = new ArrayList<>();
    private final JDialog frame = new JDialog();
    private final JInternalFrame internalFrame = new JInternalFrame();

    public UI linkTo(String seed) {
        frame.setTitle(seed);
        this.host = new Linker().linkTo(seed);
        return this;
    }

    public UI(String seed) {

        linkTo(seed);

        frame.setVisible(true);
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

    private void updateUI() {
        Thread updateUI = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(1,0);
                    } catch (InterruptedException ignored) {
                    }
                    for (Status button : buttons) {
                        if(host.check(button.getPosition()) == 1){
                            button.setBackground(new Color(25, 170, 205));
                        }
                        if(host.check(button.getPosition()) == 2){
                            button.setBackground(new Color(25, 170, 105));
                        }
                        if(host.check(button.getPosition()) == 0){
                            button.setBackground(new Color(255, 255, 255));
                        }

                    }
                    if(host.doCheck()) {
                        if(host.getWinner() == 1) {
                            frame.setTitle("X Wins");
                        } else
                            frame.setTitle("O Wins");


                        host.resetBoard();
                        for (Status status : buttons) {
                            status.setEnabled(true);
                        }
                    }

                    for (Status status : buttons) {
                        status.setEnabled(isTurn.get());
                    }

                    if(isTurn.get() && isBot) {
                        chooseRandomButton();
                    }
                }
            }
        });
        updateUI.start();
    }

    private void chooseRandomButton() {
        //chooseRandomButton
        int localSelection = ThreadLocalRandom.current().nextInt(0, 9);
        System.err.printf("\023896m chose random %d\023[0m",localSelection);
        if(host.check(localSelection) == 0){
            buttons.get(localSelection).doClick();
        }
        else chooseRandomButton();
    }


    private void Listener(Status button) {
        button.addActionListener(e -> {
            if (host.isXsTurn().get()) {
                button.setX();
                host.setPos(button.getPosition());
            }
            else if (host.isOsTurn().get()) {
                button.setO();
                host.setPos(button.getPosition());
            }
            host.print();
        });
    }
}
