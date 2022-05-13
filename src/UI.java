import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class UI {
    private final Host host;
    private final ArrayList<Status> buttons = new ArrayList<>();
    private final JDialog frame = new JDialog();

    public UI(String seed) {
        Linker linker = new Linker();
        this.host = linker.linkTo(seed);

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
                        host.resetBoard();
                        for (Status status : buttons) {
                            status.setEnabled(true);
                        }
                    }
                }
            }
        });
        updateUI.start();
    }


    private void Listener(Status button) {
        button.addActionListener(e -> {
            if (host.isXsTurn()) {
                button.setX();
                host.setPos(button.getPosition());
            }
            else if (host.isOsTurn()) {
                button.setO();
                host.setPos(button.getPosition());
            }
            host.print();
        });
    }
}
