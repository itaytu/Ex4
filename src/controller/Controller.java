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

    private void runAlgo() {
        if(!serverInitiated) initServer();
        board.setRunAlgo(true);

        System.out.println("in CLICK RUNALGO");
        startThread();

        // board.setRunAlgo(true);
    }

    private void startThread() {
        //if(!serverInitiated) initServer();

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

/*        //Algorithm mode
        else if(board.isRunAlgo()) {
            runAlgo();
        }*/

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
           // System.out.println("IN RUN THREAD");
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
                    frame.updateTextLabel(play.getStatistics() + " | Game has ended");
                    // frame.getRunGame().setEnabled(true);
                    // frame.getRunStepByStep().setEnabled(true);
                    // frame.getRunAlgo().setEnabled(true);

                    board.clearGame();
                    board.clearPlayer();
                }
            }

            else if (board.isRunAlgo()) {

                while ((play.isRuning()) && (!game.getFruitArrayList().isEmpty())) {
                    calculations = new Calculations(game, board.getWidth(), board.getHeight());
                    calculations.INIT();

                    ArrayList<GraphObject> path = calculations.getFinalPath();
                    System.out.println("PACMAN PATH: ");
                    for (int i = 0; i < path.size(); i++) {
                        System.out.println(path.get(i).getID());
                    }
                    System.out.println();
                    //int targetIndex = Integer.parseInt(path.get(path.size()-1));
                    GraphObject fruitTarget = path.get(path.size()-1);
                    Point3D fruit = fruitTarget.getPointGPS();
                    System.out.println("TARGET: " + fruit.get_x() + ", " + fruit.get_y());
                    for (int i = 1; i < path.size() && isIN(fruit) && play.isRuning(); i++) {
                        GraphObject NextMove = path.get(i);
                        Point3D target = NextMove.getPointGPS();

                       System.out.println("SOURCE: " + game.getPlayer().getPoint().get_x() + ", " + game.getPlayer().getPoint().get_y());
                       System.out.println("TARGET: " + target.get_x() + ", " + target.get_y());
                        double[] azimut = coords.azimuth_elevation_dist(game.getPlayer().getPoint(), target);
                        System.out.println("AZIMUT : " + azimut[0]);
                        while((isIN(fruit)) && (play.isRuning()) & (!game.getFruitArrayList().isEmpty())) {
                            play.rotate(azimut[0]);
                            game.update(play);
                            board.updateGUI();
                            try {
                                sleep(60);
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        game.update(play);
                        board.updateGUI();
                    }
                    game.update(play);
                    board.updateGUI();
                }
            }
        }
    }



    private boolean isIN(Point3D fruitPoint) {
        for(Fruit f : game.getFruitArrayList()) {
            if((fruitPoint.get_x() == f.getPoint().get_x()) && (fruitPoint.get_y() == f.getPoint().get_y())) {
               // System.out.println("POINTS EQUAL");
                return true;
            }
        }
       // System.out.println("POINTS DON'T EQUAL");
        return false;
    }
}
