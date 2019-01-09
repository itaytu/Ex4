package controller;

import Algorithms.Calculations;
import Coords.MyCoords;
import Elements.*;
import GUI.Board;
import GUI.MainFrame;
import Geom.Point3D;
import Robot.Play;
import Utils.GraphObject;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


public class Controller implements Observer {

    private MyCoords coords = new MyCoords();

    private Board board;
    private Game game;
    private Play play;
    private Calculations calculations;
    private MainFrame frame;
    private boolean serverInitiated = false;
    private final int MACHINE_PLAY_ID = 1234;

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

        frame.getRunGame().addActionListener(e -> {
            runGame();
        });

        frame.getRunStepByStep().addActionListener(e -> {
            runStepByStep();
        });

        frame.getRunAlgo().addActionListener(e -> {
            runAlgo();
        });

        frame.getAddPlayer().addActionListener(e -> {
            addPlayer();
            frame.getAddPlayer().setEnabled(false);
            frame.getRunStepByStep().setEnabled(true);
            frame.getRunGame().setEnabled(true);
            frame.getRunAlgo().setEnabled(true);
        });

        frame.getRemovePlayer().addActionListener(e -> {
            removePlayer();
            frame.getAddPlayer().setEnabled(true);
        });

        frame.getShowStats().addActionListener(e -> {
            try {
                new StatisticsController();
            } catch (ClassNotFoundException | SQLException e1) {
                e1.printStackTrace();
            }
        });

        // Observe the NextPoint object from the board
        observe(board.getNextPoint());
    }

    private void initServer(boolean isAlgo) {
        if (isAlgo) {
            play.setIDs(308566611, MACHINE_PLAY_ID);
        }
        else play.setIDs(308566611, 312522329);
        game.getPlayer().setPoint(nextStepPoint);
        play.setInitLocation(game.getPlayer().getPoint().get_y(), game.getPlayer().getPoint().get_x());
        play.start();

        serverInitiated = true;
    }

    private void loadGame(){
        String path = chooseFilePath();
        System.out.println(path);

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
        if(!serverInitiated) initServer(false);
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
        if(!serverInitiated) initServer(false);
    }

    private void runNextStep() {
        play.rotate(azimuth);
        game.update(play);
        frame.updateTextLabel(play.getStatistics());
        board.updateGUI();
    }

    private void runAlgo() {
        if(!serverInitiated) initServer(true);
        board.setRunAlgo(true);
        startThread();
    }

    private void startThread() {
        Thread movement = new PlayerMovement();
        movement.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        NextPoint nextPoint = ((NextPoint) o).getNextPoint();

        nextStepPoint = nextPoint.getPoint();
        azimuth = nextPoint.getAzimuth();

        // Adding player click
        if (board.isAddPlayer()) {
            Pacman newPlayer = new Pacman(nextStepPoint.get_x(), nextStepPoint.get_y());
            game.addPlayer(newPlayer);

            board.setAddPlayer(false);
            board.updateGUI();
        }

        // Step by step mode click
        else if (board.isRunStepByStep()) {
            runNextStep();
        }

        // Auto game mode click
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
            frame.getRunAlgo().setEnabled(false);

            if (board.isRunAutoGame()) {
                while (play.isRuning()) {
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
                frame.getRunAlgo().setEnabled(true);
                board.setLoaded(false);
            }

            else if (board.isRunAlgo()) {
                while ((play.isRuning() && !game.getFruitArrayList().isEmpty())) {
                    calculations = new Calculations(game, board.getWidth(), board.getHeight());
                    calculations.INIT();
                    ArrayList<GraphObject> path = calculations.getFinalPath();


                    System.out.println(path.size());
                    for (int i = 1; i < path.size(); i++) {
                        Point3D target = path.get(i).getPointGPS();
                        if(!isIN(calculations.getTargetFruit().getPointGPS())) break;
                        while(play.isRuning() && isIN(calculations.getTargetFruit().getPointGPS())) {
                            if(closeDistance(game.getPlayer().getPoint(), target)) break;
                            double[] azimut = coords.azimuth_elevation_dist(game.getPlayer().getPoint(), target);
                            play.rotate(azimut[0]);
                            game.update(play);
                            board.updateGUI();
                            frame.updateTextLabel(play.getStatistics());
                            try {
                                sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                // Make some rotation to end the game, update Play
                play.rotate(0);
                game.update(play);
                System.out.println(play.isRuning());
                frame.updateTextLabel(play.getStatistics() + " | Game has ended");
            }

        }

        private boolean isIN(Point3D fruitPoint) {
            for(Fruit f : game.getFruitArrayList()) {
                if((fruitPoint.get_x() == f.getPoint().get_x()) && (fruitPoint.get_y() == f.getPoint().get_y())) {
                    return true;
                }
            }
            return false;
        }

        private boolean closeDistance(Point3D source, Point3D target) {
            double range= 1;
            if(coords.distance3d(source, target) <= range) return true;
            return false;
        }
    }
}
