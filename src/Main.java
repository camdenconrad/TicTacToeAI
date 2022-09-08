import Toolies.DynamicDisplay;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Main {


    private static final JFrame frame = new JFrame();
    public static FlatLightLaf laf = new FlatLightLaf(); // external library
    public static int runs = 100;
    public static ArrayList<UI> games;

    public static void main(String[] args) throws UnsupportedLookAndFeelException {

        FlatLaf.setGlobalExtraDefaults(Collections.singletonMap("@accentColor", "#F37A10")); // changes accent color

        UIManager.setLookAndFeel(laf); //new FlatDarkLaf()


        frame.setVisible(true);
        //frame.setLayout(new GridLayout(1, 2));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((screenSize.height * 2) - 900, screenSize.height - 100);
        frame.setMaximumSize(new Dimension((screenSize.height * 2) - 900, screenSize.height - 100));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setTitle("Tic Tac Toe");

        games = new ArrayList<>();

        System.out.println("Running...");

        //new Simulation();
        games.add(new UI("seed"));
        games.get(0).setX();
        //games.get(0).setSmartBot();
        games.add(new UI("seed"));
        games.get(1).setSmartBot();
        games.get(1).setO();
        games.get(0).setVisible();

        GUI gui = new GUI(games.get(0));
        frame.add(gui.get());

        gui.get().add(games.get(0).getFrame());

        //games.get(0).setSmartBot();

        for (int j = 2; j < 00; j += 1) {
            games.add(new UI("seed"));
            games.get(j).setSmartBot();
            games.get(j).setO();

            //train(games, j);
        }

        new DynamicDisplay<>(Linker.getHosts());

    }

    private static void train(ArrayList<UI> games, int i) {
        games.add(new UI(String.valueOf(i)));
        games.get(i).setX();
        games.get(i).setBot();
        //games.get(i).setVisible();
        games.add(new UI(String.valueOf(i)));
        games.get(i + 1).setSmartBot();
        games.get(i + 1).setO();
        games.get(i).setSmartBot();
    }

    public static ArrayList<UI> getGames() {
        return games;
    }
}
