package controller;

import Elements.Game;
import Elements.NextPoint;
import Elements.Pacman;
import GUI.Board;
import GUI.MainFrame;
import Geom.Point3D;
import Robot.Play;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {

    private Board board;
    private Game game;
    private Play play;
    private MainFrame frame;
    private boolean firstTimeRun = true, serverInitiated = false;

    private Point3D nextStepPoint;
    private double azimuth;

    public Controller() {
        initView();
        initListeners();
    }

    private void initView() {
        board = new Board();
        frame = new MainFrame(board);
    }

    private void initListeners() {
        frame.getLoadGame().addActionListener(e -> {
            loadGame();
            frame.getAddPlayer().setEnabled(true);
        });

        frame.getSaveGame().addActionListener(e -> {
            saveGame();
        });

        frame.getRunGame().addActionListener(e -> {
            runGame();
        });

        frame.getRunStepByStep().addActionListener(e -> {
            runStepByStep();
        });

        frame.getAddPlayer().addActionListener(e -> {
            addPlayer();
            frame.getAddPlayer().setEnabled(false);
            frame.getRunStepByStep().setEnabled(true);
            frame.getRunGame().setEnabled(true);
        });

        frame.getRemovePlayer().addActionListener(e -> {
            removePlayer();
            frame.getAddPlayer().setEnabled(true);
        });

        // Observe the NextPoint object from the board
        observe(board.getNextPoint());
    }

    private void initServer() {
        play.setIDs(308566611, 312522329);
        game.getPlayer().setPoint(nextStepPoint);
        play.setInitLocation(game.getPlayer().getPoint().get_y(), game.getPlayer().getPoint().get_x());
        play.start();

        serverInitiated = true;
    }

    private void loadGame(){
        String path = chooseFilePath();

        if (path != null) {
            play = new Play(path);
            game = new Game(play);

            board.setLoaded(true);
            board.setGame(game);

            board.updateGUI();
        }
    }

    private void runGame() {
        board.setRunAutoGame(true);
    }

    // TODO: Fix, need to send the game data.
    private void saveGame() {

    }

    private void addPlayer() {
        board.setAddPlayer(true);
    }

    private void removePlayer() {
        board.clearPlayer();
        board.setAddPlayer(true);
    }

    private void runStepByStep() {
        board.setStepByStep(true);
    }

    private void runNextStep() {
        if(!serverInitiated) initServer();

        play.rotate(azimuth);
        game.update(play);

        board.updateGUI();
    }

    private void startThread() {
        if(!serverInitiated) initServer();

        Thread movement = new PlayerMovement();
        movement.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        NextPoint nextPoint = ((NextPoint) o).getNextPoint();

        nextStepPoint = nextPoint.getPoint();
        azimuth = nextPoint.getAzimuth();

        if (firstTimeRun) {
            initServer();
            firstTimeRun = false;
        }

        if (board.isAddPlayer()) {
            Pacman newPlayer = new Pacman(nextStepPoint.get_x(), nextStepPoint.get_y());
            game.addPlayer(newPlayer);

            board.setAddPlayer(false);
            board.updateGUI();
        }

        // Step by step mode
        else if (board.isRunStepByStep()) {
            runNextStep();
        }

        // Auto game mode
        else if (board.isRunAutoGame()) {
            if (board.isFirstClick()) {
                board.setFirstClick(false);
                startThread();
            }
        }
    }

    private void observe(Observable o) {
        o.addObserver(this);
    }

    private String chooseFilePath() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files", "csv");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            return selectedFile.getAbsolutePath();
        }
        return null;
    }

    public class PlayerMovement extends Thread {

        @Override
        public void run() {

            frame.getRunGame().setEnabled(false);
            frame.getRunStepByStep().setEnabled(false);

            while(play.isRuning()) {

                play.rotate(azimuth);
                game.update(play);
                board.updateGUI();

                frame.updateTextLabel(play.getStatistics());

                try {
                    sleep(60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            frame.updateTextLabel(play.getStatistics() + " | Game has ended");
            frame.getRunGame().setEnabled(true);
            frame.getRunStepByStep().setEnabled(true);

            board.clearGame();
            board.clearPlayer();
        }
    }
}
