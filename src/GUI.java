import javax.swing.*;
import java.awt.event.ContainerAdapter;
import java.awt.event.MouseAdapter;

public class GUI {
    private JPanel frame;
    private JTextField console;
    private JPanel info;
    private JLabel simsRun;
    private JProgressBar progressBar;

    public GUI(UI ui) {

        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> scores = new JList<>(model);

        model.addElement("Player Score: ");
        model.addElement("AI Score: ");
        model.addElement("Ties: ");

        info.add(scores);


        Thread startUpdates = new Thread(() -> {
            while (true) {
                model.set(0, "\rPlayer Score: " + Linker.getHost(0).getWins());
                model.set(1, "\rAI Score: " + Linker.getHost(0).getLosses());
                model.set(2, "\rTies: " + Linker.getHost(0).getTies());
                simsRun.setText("\rSimulations " + "(" + Main.getGames().get(1).getSimulation().getSimsRun() + "/" + Main.runs + ")");
                progressBar.setMaximum(Main.runs);
                progressBar.setValue(Main.getGames().get(1).getSimulation().getSimsRun());

                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {
                }
            }
        });
        startUpdates.start();

        console.addActionListener(e -> {
                    if (console.getText().contains("setSeed")) {
                        ui.linkTo(console.getText().trim().replace("setSeed", ""));
                        console.setText("");
                    }
                    if (console.getText().contains("setRuns")) {
                        try {
                            Main.runs = Integer.parseInt(console.getText().trim().replace("setRuns", ""));
                            console.setText("");
                        } catch (NumberFormatException ignored) {
                            console.setText("Error. %FormatEx");
                        }

                    }

                }
        );
        frame.addContainerListener(new ContainerAdapter() {
        });
        frame.addMouseListener(new MouseAdapter() {
        });
    }

    public JPanel get() {
        return this.frame;
    }
}
