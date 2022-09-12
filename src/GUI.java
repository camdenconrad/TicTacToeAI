import javax.swing.*;
import java.awt.event.ContainerAdapter;
import java.awt.event.MouseAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;

public class GUI {
    public static void stopTime() {
        timeNotStopped = false;
    }

    public static void startTime() {
        timeNotStopped = true;
    }

    private static boolean timeNotStopped = false;
    private String playerName = "Player";
    private JPanel frame;
    private JTextField console;
    private JPanel info;
    private JLabel simsRun;
    private JProgressBar progressBar;
    private JLabel simTime;
    private static long startTime;

    public GUI(UI ui) {

        DateTimeFormatter dateAndTime = DateTimeFormatter.ofPattern("mm:ss");

        startTime = System.currentTimeMillis()/1000;

        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> scores = new JList<>(model);

        model.addElement("Player Score: ");
        model.addElement("AI Score: ");
        model.addElement("Ties: ");

        info.add(scores);
        simTime.setText("Time: ");


        Thread startUpdates = new Thread(() -> {
            while (true) {
                model.set(0, playerName + "\r's Score: " + Linker.getHost(0).getWins());
                model.set(1, "\rAI Score: " + Linker.getHost(0).getLosses());
                model.set(2, "\rTies: " + Linker.getHost(0).getTies());
                simsRun.setText("\rSimulations " + "(" + Main.getGames().get(1).getSimulation().getSimsRun() + "/" + Main.runs + ")");
                progressBar.setMaximum(Main.runs);
                progressBar.setValue(Main.getGames().get(1).getSimulation().getSimsRun());
                if(timeNotStopped)
                    simTime.setText("Time: " + ((System.currentTimeMillis()/1000) - startTime) + "s");

                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {
                }
            }
        });
        startUpdates.start();

        console.addActionListener( e -> {

            if(console.getText().toLowerCase().contains("setseed ")) {
                ui.linkTo(console.getText().trim().replace("setSeed ",""));
                console.setText("");
            }
            if(console.getText().contains("setRuns ")) {
                try {
                    Main.runs = Integer.parseInt(console.getText().trim().replace("setRuns ", ""));
                    console.setText("");
                } catch (NumberFormatException ignored) {
                    console.setText("Error. %FormatEx");
                }

            }
            if(console.getText().contains("setName")) {
                playerName = console.getText().trim().replace("setName ","");
                console.setText("");
            }
            if(console.getText().equals("gc")) {
                System.gc();
                console.setText("");
            }
            if(console.getText().contains("setPlayer")) {
                Main.getGames().get(1).setPlayer();
                console.setText("");
            }
            if(console.getText().contains("setAI")) {
                Main.getGames().get(1).setSmartBot();
                console.setText("");
            }
            if(console.getText().contains("trainingmode")) {
                Main.getGames().get(0).setSmartBot();
            }


        }
        );
        frame.addContainerListener(new ContainerAdapter() {
        });
        frame.addMouseListener(new MouseAdapter() {
        });
    }

    public static void setStartTime() {
        startTime = System.currentTimeMillis()/1000;
    }

    public JPanel get() {
        return this.frame;
    }
}
