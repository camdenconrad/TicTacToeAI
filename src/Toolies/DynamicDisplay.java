package Toolies;


import javax.swing.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class DynamicDisplay<T> {

    private final JList<T> list;
    private final JPanel panel = new JPanel();

    @SuppressWarnings("BusyWait")
    public DynamicDisplay(ArrayList<T> watch) {

        JDialog display = new JDialog();
        display.setVisible(true);

        DefaultListModel<T> model = new DefaultListModel<>();

        list = new JList<>(model);

        Thread updateInventoryList = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(0, 1); // limits thread
                } catch (InterruptedException ignored) {
                }

                panel.updateUI(); // updates main UI

                try {
                    for (T t : watch) {
                        if (!model.contains(t)) {
                            model.addElement(t);
                        }
                    }
                    for (int i = 0; i < model.getSize(); i++) {
                        if (!watch.contains(model.elementAt(i))) {
                            model.remove(i);
                        }

                    }
                    try {
                        for (int i = 0; i < model.getSize(); i++) {
                            // if list is not in the same order as the inventory, clear list and redo
                            if (model.elementAt(i) != watch.get(i)) {
                                model.clear();
                            }

                        }
                    } catch (IndexOutOfBoundsException ignored) {
                        model.clear();
                    }

                } catch (ConcurrentModificationException ignored) {
                    model.clear();
                }

                display.setTitle("List Watch | Total Elements: " + model.getSize());

            }

        });
        updateInventoryList.start();

        display.add(this.getWatch());


    }

    private JList<T> getWatch() {
        return this.list;
    }
}
