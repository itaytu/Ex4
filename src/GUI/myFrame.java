package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class myFrame extends JFrame {

    private Board board;
    private BufferedImage icon;

    public myFrame() {
        loadImages();
        initGUI();
    }

    public void loadImages() {
        try {
            icon = ImageIO.read(new File("pacmanIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initGUI() {
        board = new Board();
        this.setContentPane(board);
        this.setSize(1433, 642);
        this.setIconImage(icon);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MenuBar menuBar = new MenuBar();
        setMenuBar(menuBar);

        Menu file = new Menu("File");
        menuBar.add(file);

        Menu game = new Menu("Game");
        menuBar.add(game);

        Menu run = new Menu("Run");
        menuBar.add(run);

        MenuItem runGame = new MenuItem("Run Game");
        run.add(runGame);
        runGame.setEnabled(false);

        MenuItem runStepByStep = new MenuItem("Run Step By Step");
        run.add(runStepByStep);
        runStepByStep.setEnabled(false);

        MenuItem loadGame = new MenuItem("Load Game");
        file.add(loadGame);

        MenuItem saveGame = new MenuItem("Save Game");
        file.add(saveGame);

        MenuItem addPlayer = new MenuItem("Add Player");
        game.add(addPlayer);

        MenuItem removePlayer = new MenuItem("Remove Player");
        game.add(removePlayer);

        initListeners(loadGame, saveGame, runGame, addPlayer, removePlayer, runStepByStep);
    }

    public void initListeners(MenuItem loadGame, MenuItem saveGame, MenuItem runGame,
                              MenuItem addPlayer, MenuItem removePlayer, MenuItem runStepByStep) {
        loadGame.addActionListener(e -> {
            loadGame();
            addPlayer.setEnabled(true);
        });

        saveGame.addActionListener(e -> saveGame());

        addPlayer.addActionListener(e -> {
            addPlayer();
            addPlayer.setEnabled(false);
            runStepByStep.setEnabled(true);
            runGame.setEnabled(true);
        } );

        removePlayer.addActionListener(e -> {
            removePlayer();
            addPlayer.setEnabled(true);
        });

        runGame.addActionListener(e -> runGame());

        runStepByStep.addActionListener(e -> runStepByStep());
    }

    public void loadGame(){
        System.out.println("> Load Game");

        String path = chooseFilePath();

        if (path != null) {
            board.loadGame(path);
        } else
            System.out.println("Path not entered");
    }

    public void saveGame() {

    }

    public void addPlayer() {
        board.setAddPlayer(true);
    }

    public void removePlayer() {
        board.clearPlayer();
        board.setAddPlayer(true);
    }

    public void runGame() {
        board.setRun(true);
        board.runGame();
    }

    public void runStepByStep() {
        board.setStepByStep(true);
    }

    private String chooseFilePath() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files", "csv");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            return selectedFile.getAbsolutePath();
        }
        return null;
    }

}
