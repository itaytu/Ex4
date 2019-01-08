package Elements;

import Geom.Point3D;
import Robot.Play;

import java.util.ArrayList;

public class Game {

    private ArrayList<Ghost> ghostArrayList;
    private ArrayList<Fruit> fruitArrayList;
    private ArrayList<Pacman> pacmanArrayList;
    private ArrayList<Block> blockArrayList;
    private Pacman player;

    public Game(Play play) {
        this.player = new Pacman(-1,-1);
        init(play);
    }

    public Game(Game g) {
        pacmanArrayList = new ArrayList<>(g.getPacmanArrayList());
        fruitArrayList = new ArrayList<>(g.getFruitArrayList());
        ghostArrayList = new ArrayList<>(g.getGhostArrayList());
        blockArrayList = new ArrayList<>(g.getBlockArrayList());
        player = new Pacman(g.getPlayer());
    }

    public Game() {
        pacmanArrayList = new ArrayList<>();
        fruitArrayList = new ArrayList<>();
        ghostArrayList = new ArrayList<>();
        blockArrayList = new ArrayList<>();
    }

    private void init(Play play) {
        ghostArrayList = new ArrayList<>();
        pacmanArrayList = new ArrayList<>();
        fruitArrayList = new ArrayList<>();
        blockArrayList = new ArrayList<>();

        ArrayList<String> myData = play.getBoard();

        for (String aMyData : myData) {
            String[] tmp2 = aMyData.split(",");

            switch (tmp2[0]) {
                case "P":
                case "p":
                    Pacman newPacman = new Pacman(tmp2, 3, 2, 4);
                    pacmanArrayList.add(newPacman);
                    break;
                case "F":
                case "f":
                    Fruit newFruit = new Fruit(tmp2, 3, 2, 4);
                    fruitArrayList.add(newFruit);
                    break;
                case "G":
                case "g":
                    Ghost newGhost = new Ghost(tmp2, 3, 2, 4);
                    ghostArrayList.add(newGhost);
                    break;
                case "B":
                case "b":
                    Block newBlock = new Block(tmp2);
                    blockArrayList.add(newBlock);
                    break;
                case "M":
                case "m":
                    player = new Pacman(tmp2, 3, 2, 1);
                    break;
            }
        }
    }

    public ArrayList<Pacman> getPacmanArrayList() {
        return pacmanArrayList;
    }

    public ArrayList<Fruit> getFruitArrayList() {
        return fruitArrayList;
    }

    public ArrayList<Ghost> getGhostArrayList() {
        return ghostArrayList;
    }

    public ArrayList<Block> getBlockArrayList() {
        return blockArrayList;
    }

    public void addPlayer(Pacman p) {
        player = new Pacman(p);
    }

    public Pacman getPlayer () {
        return player;
    }

    public void setPlayer(Point3D p) {
        this.player = new Pacman(p.get_x(), p.get_y());
    }

    public void update(Play play) {
        pacmanArrayList.clear();
        fruitArrayList.clear();
        ghostArrayList.clear();
        blockArrayList.clear();

        init(play);
    }
}
