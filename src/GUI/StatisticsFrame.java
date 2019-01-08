package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * The class defines the View for the statistics frame
 */
public class StatisticsFrame extends JFrame {
    private JTextArea textArea;
    private JLabel loading;

    /**
     * Constructs new GUI frame
     */
    public StatisticsFrame() {
        initGUI();
    }

    /**
     * Inits the panel, layout, text area and label
     */
    private void initGUI() {
        setLayout(new BorderLayout());
        setTitle("Statistics");
        setVisible(true);

        JPanel panel = new JPanel();
        JScrollPane pane = new JScrollPane(panel);

        textArea = new JTextArea();
        loading = new JLabel("Loading and calculating the statistics...");

        panel.add(textArea, BorderLayout.CENTER);
        panel.add(loading, BorderLayout.CENTER);
        add(pane);

        pack();

        setSize(1160, 230);
    }

    /**
     * Simple getter for textArea
     * @return the main text area
     */
    public JTextArea getTextArea() {
        return textArea;
    }

    /**
     * Simple getter for the loadingLabel
     * @return the loading label
     */
    public JLabel getLoadingLabel() {
        return loading;
    }
}
