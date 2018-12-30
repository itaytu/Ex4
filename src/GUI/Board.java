package GUI;

import Converters.Map;
import Coords.MyCoords;
import Elements.*;
import Geom.Point3D;
import Robot.Play;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;


public class Board extends JPanel implements MouseListener {

    private double movementX, movementY;

    private Play play;
    private BufferedImage map, pacman, player, fruit, ghost;
    private Game game;
    private Map mapProperties;
    private MyCoords coords;
    private Point3D stepPoint = new Point3D(0,0,0);

    private boolean addPlayer, stepByStep, run = false;
    private boolean loaded = false;
    private boolean serverInitiated = false;

    private boolean firstClick = true;

    private int numOfclicks=0;


    public Board() {
        this.addMouseListener(this);
        game = new Game();
        INIT();
    }

    public void INIT() {
        loadImages();
        coords = new MyCoords();
    }

    private void loadImages() {
        try {
            map = ImageIO.read(new File("Ariel1.png"));
            pacman = ImageIO.read(new File("badPacman.png"));
            fruit = ImageIO.read(new File("fruit.png"));
            ghost = ImageIO.read(new File ("ghost.png"));
            player = ImageIO.read(new File ("pacman.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        repaint();
    }


    @Override
    public void paint(Graphics g) {
        int h = this.getHeight();
        int w = this.getWidth();

        mapProperties = new Map(map, w, h, 35.20238, 35.21236, 32.10190, 32.10569);

        g.drawImage(map, 0, 0, w, h, this);

        if (!game.getPacmanArrayList().isEmpty()) {
            for (int i = 0; i < game.getPacmanArrayList().size(); i++) {
                Pacman p = game.getPacmanArrayList().get(i);
                int[] arr = mapProperties.gps2Pixels(p.getPoint());
                g.drawImage(pacman, arr[0], arr[1], this);
            }
        }

        if (!game.getFruitArrayList().isEmpty()) {
            for (int i = 0; i < game.getFruitArrayList().size(); i++) {
                Fruit f = game.getFruitArrayList().get(i);
                int[] arr = mapProperties.gps2Pixels(f.getPoint());
                g.drawImage(fruit, arr[0], arr[1], this);
            }
        }

        if (!game.getGhostArrayList().isEmpty()) {
            for (int i = 0; i < game.getGhostArrayList().size(); i++) {
                Ghost gh = game.getGhostArrayList().get(i);
                int[] arr = mapProperties.gps2Pixels(gh.getPoint());
                g.drawImage(ghost, arr[0], arr[1], this);
            }
        }

        if (!game.getBlockArrayList().isEmpty()) {
            for(int i = 0; i < game.getBlockArrayList().size(); i ++) {
                Block b = game.getBlockArrayList().get(i);

                Point3D startP = new Point3D(b.get_pointMin().get_x(), b.get_pointMax().get_y(), 0);
                int[] startArr = mapProperties.gps2Pixels(startP);
                int[] minArr = mapProperties.gps2Pixels(b.get_pointMin());
                int[] maxArr = mapProperties.gps2Pixels(b.get_pointMax());

                int width = maxArr[0] - startArr[0];
                int height = minArr[1] - startArr[1];

                g.fillRect(startArr[0], startArr[1], width, height);

            }
        }

        if (loaded == true) {
                int[] arr = mapProperties.gps2Pixels(game.getPlayer().getPoint());
                g.drawImage(player, arr[0], arr[1], this);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        mapProperties = new Map(map, getWidth(), getHeight(), 35.20238, 35.21236, 32.10190, 32.10569);

        if(addPlayer) {
            int x = e.getX();
            int y = e.getY();

            Point3D playerPoint = mapProperties.toCoords(x, y);
            Pacman newPlayer = new Pacman(playerPoint.get_x(), playerPoint.get_y());
            game.addPlayer(newPlayer);

            addPlayer = false;

            movementX = playerPoint.get_x();
            movementY = playerPoint.get_y();

            repaint();
        }

        else if(stepByStep) {
            int x = e.getX();
            int y = e.getY();

            stepPoint = mapProperties.toCoords(x, y);

            runStepByStep();
        }

        else if (run) {
            int x = e.getX();
            int y = e.getY();

            stepPoint = mapProperties.toCoords(x, y);

            movementX = stepPoint.get_x();
            movementY = stepPoint.get_y();

            numOfclicks++;

            if (firstClick) {
                startThread();
                firstClick = false;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }



    public void loadGame(String path) {
        clearGame();
        play = new Play(path);
        this.game = new Game(play);
        loaded = true;
        repaint();
    }

    public void runGame() {
        if(!serverInitiated || !play.isRuning()) {
            INITserver();
        }
    }

    private void startThread() {
        Thread movement = new PlayerMovement(this);

        movement.start();
    }

    public void runStepByStep() {
        if(!serverInitiated) INITserver();

        double [] azimut = coords.azimuth_elevation_dist(game.getPlayer().getPoint() , stepPoint);

        play.rotate(azimut[0]);
        game.update(play);

        this.throwMessage();
        repaint();
    }



    public void clearPlayer() {
        game.getPlayer().setPoint(null);
        repaint();
    }

    public void clearGame() {

        game.setPlayer(new Point3D(0,0,0));
        game.getBlockArrayList().clear();
        game.getGhostArrayList().clear();
        game.getFruitArrayList().clear();
        game.getPacmanArrayList().clear();

        numOfclicks=0;

        repaint();
    }


    public void setAddPlayer(boolean flag) {
        addPlayer = flag;
    }

    public void setStepByStep(boolean flag) { stepByStep = flag; }

    public void setRun(boolean flag) {
        run = flag;
    }

    private void INITserver(){
        play.setIDs(308566611, 312522329);
        play.setInitLocation(game.getPlayer().getPoint().get_y(), game.getPlayer().getPoint().get_x());
        play.start();
        serverInitiated = true;
    }


    public double getMovementX() {
        return movementX;
    }

    public double getMovementY() {
        return movementY;
    }

    public Pacman getPlayer() {
        return game.getPlayer();
    }

    public MyCoords getCoords() {
        return coords;
    }

    public Game getGame() {
        return game;
    }

    public Play getPlay() { return play; }

    public int getNumOfclicks() {
        return numOfclicks;
    }


    synchronized void updateGUI() {
        repaint();
    }

    public void throwMessage() {
        JOptionPane.showMessageDialog(this,"The game has ended.\n" +
                play.getStatistics());
    }

}