package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainFrame extends JFrame {

    private Board board;
    private BufferedImage icon;
    private MenuItem runGame, runStepByStep, loadGame, showStats, addPlayer, removePlayer, runAlgoWith, runAlgoWithout;
    private JLabel textLabel;

    /**
     * The main View for the game. It hosts the board panel, where all the paint work done.
     * @param board board
     */
    public MainFrame(Board board) {
        this.board = board;

        loadImages();
        initGUI();
    }

    /**
     * Load pacman image icon.
     */
    private void loadImages() {
        try {
            File icon1 = new File(getClass().getClassLoader().getResource("pacmanIcon.png").getFile());
            icon = ImageIO.read(icon1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Init menu items of the frame. Setting the properties of the frame.
     */
    private void initGUI() {
        setLayout(new BorderLayout());

        JPanel boardPanel = board;
        JPanel panel = new JPanel();
        boardPanel.setSize(1433, 642);
        panel.setSize(1433, 1600);

        boardPanel.setVisible(true);
        panel.setVisible(true);

        textLabel = new JLabel();
        textLabel.setText("Waiting for a game to start...");

        panel.add(textLabel);

        getContentPane().add(boardPanel, BorderLayout.CENTER);
        getContentPane().add(panel, BorderLayout.NORTH);

        pack();

        setSize(1433,642);
        setIconImage(icon);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MenuBar menuBar = new MenuBar();
        setMenuBar(menuBar);

        Menu file = new Menu("File");
        menuBar.add(file);

        Menu game = new Menu("Game");
        menuBar.add(game);

        Menu run = new Menu("Run");
        menuBar.add(run);

        Menu algorithm = new Menu("Algorithm");
        menuBar.add(algorithm);

        Menu stats = new Menu("Statistics");
        menuBar.add(stats);

        runGame = new MenuItem("Run Game");
        run.add(runGame);
        runGame.setEnabled(false);

        runStepByStep = new MenuItem("Run Step By Step");
        run.add(runStepByStep);
        runStepByStep.setEnabled(false);

        runAlgoWith = new MenuItem("With Player");
        algorithm.add(runAlgoWith);
        runAlgoWith.setEnabled(false);

        runAlgoWithout = new MenuItem("Without Player");
        algorithm.add(runAlgoWithout);
        runAlgoWithout.setEnabled(false);

        loadGame = new MenuItem("Load Game");
        file.add(loadGame);

        addPlayer = new MenuItem("Add Player");
        game.add(addPlayer);

        removePlayer = new MenuItem("Remove Player");
        game.add(removePlayer);

        showStats = new MenuItem("Show Statistics");
        stats.add(showStats);
    }

    public MenuItem getRunGame() {
        return runGame;
    }

    public MenuItem getRunStepByStep() {
        return runStepByStep;
    }

    public MenuItem getLoadGame() {
        return loadGame;
    }

    public MenuItem getAddPlayer() {
        return addPlayer;
    }

    public MenuItem getRemovePlayer() {
        return removePlayer;
    }

    public MenuItem getRunAlgo() { return runAlgoWith; }

    public MenuItem getRunAlgoWithout() { return runAlgoWithout; }

    public MenuItem getShowStats() { return showStats; }

    /**
     * Updates the text label on the status bar.
     * @param msg string message
     */
    public void updateTextLabel(String msg) {
        textLabel.setText("");
        textLabel.setText(msg);
    }
}
