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
    private MenuItem runGame, runStepByStep, loadGame, saveGame, addPlayer, removePlayer, runAlgo;
   // private Menu runAlgo;
    private JLabel textLabel;

    public MainFrame(Board board) {
        this.board = board;

        loadImages();
        initGUI();
    }

    private void loadImages() {
        try {
            icon = ImageIO.read(new File("pacmanIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initGUI() {
        //setContentPane(board);
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

        runGame = new MenuItem("Run Game");
        run.add(runGame);
        runGame.setEnabled(false);

        runStepByStep = new MenuItem("Run Step By Step");
        run.add(runStepByStep);
        runStepByStep.setEnabled(false);

        runAlgo = new MenuItem("Run Algorithm");
        run.add(runAlgo);
        runAlgo.setEnabled(false);

        loadGame = new MenuItem("Load Game");
        file.add(loadGame);

        saveGame = new MenuItem("Save Game");
        file.add(saveGame);

        addPlayer = new MenuItem("Add Player");
        game.add(addPlayer);

        removePlayer = new MenuItem("Remove Player");
        game.add(removePlayer);

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

    public MenuItem getSaveGame() {
        return saveGame;
    }

    public MenuItem getAddPlayer() {
        return addPlayer;
    }

    public MenuItem getRemovePlayer() {
        return removePlayer;
    }

    public MenuItem getRunAlgo() { return runAlgo; }

    public JLabel getTextLabel() {
        return textLabel;
    }

    public void updateTextLabel(String msg) {
        textLabel.setText("");
        textLabel.setText(msg);
    }
}
