import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final JFrame frame = new JFrame("Main");

    public static void main(String[] args) {
        frame.setVisible(false);
        frame.setLayout(new GridLayout(1, 2));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((screenSize.height * 2) - 400, screenSize.height - 100);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ArrayList<UI> games = new ArrayList<UI>();

        games.add(new UI("seed"));
        games.get(0).setO();
        games.add(new UI("seed"));
        games.get(1).setBot(true);
        games.get(1).setX();

    }
}
