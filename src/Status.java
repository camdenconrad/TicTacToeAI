import javax.swing.*;
import java.awt.*;

public class Status extends JButton {

    private final int position;
    // X = 1
    // O = 2
    private int status; // can be 0, 1, 2

    public Status(int position) {
        this.position = position;
        this.status = 0;
    }

    public int getPosition() {
        return position;
    }

    public int getStatus() {
        return status;
    }

    public Color getStatus(String color) {
        if (status == 0) {
            return new Color(255, 255, 255);
        }
        if (status == 1) {
            return new Color(25, 170, 205);
        }
        return new Color(25, 170, 105);

    }

    public void setX() {
        //setBackground(new Color(25, 170, 205));
        setEnabled(false);
        this.status = 1;
    }

    public void setO() {
        //setBackground(new Color(25, 170, 105));
        setEnabled(false);
        this.status = 2;
    }


}
