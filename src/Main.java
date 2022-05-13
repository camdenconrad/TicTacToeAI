import javax.swing.*;
import java.awt.*;

public class Main {

    private static final JFrame frame = new JFrame("Main");

    public static void main(String[] args) {
        frame.setVisible(false);
        frame.setLayout(new GridLayout(1, 2));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((screenSize.height * 2) - 400, screenSize.height - 100);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        new UI("seed");
        new UI("seed");
    }
}
