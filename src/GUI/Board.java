package GUI;

import Converters.Map;
import Elements.*;
import Geom.Point3D;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;


public class Board extends JPanel implements MouseListener {

    private BufferedImage map, pacman, player, fruit, ghost;
    private Game game;
    private Map mapProperties;
    private NextPoint nextPoint;

    private boolean addPlayer, stepByStep, runAutoGame, loaded, firstClick = true;

    public Board() {
        init();
    }

    private void init() {
        game = new Game();
        nextPoint = new NextPoint();

        loadImages();
        addMouseListener(this);
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

        if (loaded) {
                int[] arr = mapProperties.gps2Pixels(game.getPlayer().getPoint());
                g.drawImage(player, arr[0], arr[1], this);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (addPlayer || stepByStep || runAutoGame) {
            int x = e.getX();
            int y = e.getY();

            mapProperties = new Map(map, getWidth(), getHeight(), 35.20238, 35.21236, 32.10190, 32.10569);
            Point3D newPoint = mapProperties.toCoords(x, y);

            if (addPlayer) {

                nextPoint.setPoints(newPoint, newPoint);
            }

            else {
                // stepByStep || runAutoGame Option
                // Update observers with new points
                nextPoint.setPoints(newPoint, game.getPlayer().getPoint());
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

        repaint();
    }

    public void setAddPlayer(boolean flag) {
        addPlayer = flag;
    }

    public void setStepByStep(boolean flag) { stepByStep = flag; }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setLoaded(boolean isLoaded) {
        loaded = isLoaded;
    }

    public void setRunAutoGame(boolean flag) {
        runAutoGame = flag;
    }

    public NextPoint getNextPoint() {
        return nextPoint;
    }

    public boolean isFirstClick() {
        return firstClick;
    }

    public void setFirstClick(boolean isClicked) {
        firstClick = isClicked;
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

    public void updateGUI() {
        repaint();
    }

    public boolean isRunStepByStep() {
        return stepByStep;
    }

    public boolean isRunAutoGame() {
        return runAutoGame;
    }

    public boolean isAddPlayer() {
        return addPlayer;
    }

    public void showMessagePrompt(String msg) {
        JOptionPane.showMessageDialog(null, "Game ended.\n" + msg);

    }
}