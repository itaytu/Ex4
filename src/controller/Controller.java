package controller;

import Algorithms.Calculations;
import Coords.MyCoords;
import Elements.*;
import GUI.Board;
import GUI.MainFrame;
import Geom.Point3D;
import Robot.Play;
import Utils.GraphObject;
import Utils.NextPoint;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import observer.Observable;
import observer.Observer;

/**
 * This class represents the Controller of the project, this class is in charge of the communication
 * between the UI and the logical classes. The class uses the Observer design pattern in order to create
 * updates and changes happening both from logical and UI interaction.
 *
 * The main methods of this class are the update for the observer, The initiation of the action listeners
 * and the Thread class for running the game.
 *
 * @author Itay Tuson and Sagi Oshri.
 */

public class Controller extends Observer {

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

    /**
     * The constructor of this class initiates the UI part and the logic functions used by the UI.
     */
    public Controller() {
        initView();
        initListeners();
    }

    /**
     * UI initiation. the Main frame with the menu and the board holding the map game.
     */
    private void initView() {
        board = new Board();
        frame = new MainFrame(board);
    }

    /**
     * Action listeners for the menu items.
     */
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

        frame.getRunAlgoWithout().addActionListener(e ->{
            runAlgoWithout();
        });
        frame.getAddPlayer().addActionListener(e -> {
            addPlayer();
            frame.getAddPlayer().setEnabled(false);
            frame.getRunStepByStep().setEnabled(true);
            frame.getRunGame().setEnabled(true);
            frame.getRunAlgo().setEnabled(true);
            frame.getRunAlgoWithout().setEnabled(false);
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

    /**
     * Server initiation for running the game.
     * @param isAlgo - boolean flag to check if run algorithm was clicked
     */
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

    /**
     * The tab opened to load a game.
     */
    private void loadGame(){
        String path = chooseFilePath();
        System.out.println(path);

        if (path != null) {
            play = new Play(path);
            game = new Game(play);

            board.setLoaded(true);
            frame.getRunAlgoWithout().setEnabled(true);
            board.setGame(game);

            board.updateGUI();
        }
    }

    /**
     * Setting the autoRun mode.
     */
    private void runGame() {
        board.setRunAutoGame(true);
        if(!serverInitiated) initServer(false);
    }

    /**
     * Enable adding a player to the board.
     */
    private void addPlayer() {
        board.setAddPlayer(true);
    }

    /**
     * Removing the player from the board.
     */
    private void removePlayer() {
        board.clearPlayer();
        board.setAddPlayer(true);
    }

    /**
     * Setting the run StepByStep mode.
     */
    private void runStepByStep() {
        board.setStepByStep(true);
        if(!serverInitiated) initServer(false);
    }

    /**
     * When in autoRun mode, moves the player in the desired direction and updates the game.
     */
    private void runNextStep() {
        play.rotate(azimuth);
        game.update(play);
        frame.updateTextLabel(play.getStatistics());
        board.updateGUI();
    }

    /**
     * Setting the run Algorithm mode.
     */
    private void runAlgo() {
        if(!serverInitiated) initServer(true);
        board.setRunAlgo(true);

        startThread();
    }

    /**
     * Run game in algoryhtm mode, without choosing a starting point for the Player.
     */
    private void runAlgoWithout() {
        int randomFruit = (int)(Math.random()*game.getFruitArrayList().size());
        nextStepPoint = new Point3D(game.getFruitArrayList().get(randomFruit).getPoint().get_x()-0.00001,game.getFruitArrayList().get(0).getPoint().get_y()-0.00001);
        Pacman newPlayer = new Pacman(nextStepPoint.get_x(), nextStepPoint.get_y());
        game.addPlayer(newPlayer);

        if(!serverInitiated) initServer(true);
        board.setRunAlgoWithout(true);

        startThread();
    }

    /**
     * Starting the thread for the game.
     */
    private void startThread() {
        Thread movement = new PlayerMovement();
        movement.start();
    }

    /**
     * This function is in charge of checking and making updates when needed, by observing the NextPoint object, who is Observable.
     * @param o observable
     */
    @Override
    public void update(Observable o) {
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

    public void observe(Observable o) {
        o.addObserver(this);
    }

    /**
     * Chosing a game file.
     * @return String for file path
     */
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


    /**
     * This inner class is in charge of the thread running the game.
     * The thread runs on two different modes.
     * 1 - Auto run. 2 - Algorithm run.
     *
     * The Auto run mode gets the Azimuth by observing the clicks on the board and calculating them,
     * and then moves the player in the desired direction.
     *
     * The Algorithm mode calculates the shortest path from the player to one fruit
     * out of all the fruits left on the board by using the Calculations class.
     * The calculation happens while the player moves and checks in real time what path is the best
     * option at the time.
     */
    public class PlayerMovement extends Thread {

        @Override
        public void run() {
            frame.getRunGame().setEnabled(false);
            frame.getRunStepByStep().setEnabled(false);
            frame.getRunAlgo().setEnabled(false);

            //Auto run mode
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

            //Algorithm run mode
            else if (board.isRunAlgo() || board.isRunAlgoWithout()) {

                while ((play.isRuning() && !game.getFruitArrayList().isEmpty())) {
                    calculations = new Calculations(game, board.getWidth(), board.getHeight());
                    calculations.INIT(board.getWidth(), board.getHeight());
                    ArrayList<GraphObject> path = calculations.getFinalPath();
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
                                sleep(40);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                // Make some rotation to end the game, update Play
                play.rotate(0);
                game.update(play);
                board.updateGUI();
                frame.updateTextLabel(play.getStatistics() + " | Game has ended");
            }

        }

        /**
         * This function checks if the fruit target is still on board and hasn't been eaten by others.
         * @param fruitPoint fruit GPS point
         * @return true or false
         */
        private boolean isIN(Point3D fruitPoint) {
            for(Fruit f : game.getFruitArrayList()) {
                if((fruitPoint.get_x() == f.getPoint().get_x()) && (fruitPoint.get_y() == f.getPoint().get_y())) {
                    return true;
                }
            }
            return false;
        }

        /**
         * This function checks if the player is close enough to the target.
         * @param source Point from
         * @param target Point to
         * @return true or false if in range
         */
        private boolean closeDistance(Point3D source, Point3D target) {
            double range= 1;
            if(coords.distance3d(source, target) <= range) return true;
            return false;
        }
    }
}
