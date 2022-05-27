import javax.swing.*;

public class Status extends JButton {

    private final int position;
    // X = 1
    // O = 2
    // can be 0, 1, 2

    public Status(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setX() {
        //setBackground(new Color(25, 170, 205));
        setEnabled(false);
    }

    public void setO() {
        //setBackground(new Color(25, 170, 105));
        setEnabled(false);
    }


}
