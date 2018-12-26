package Elements;

import Geom.Point3D;
import Robot.Play;

import java.util.ArrayList;

public class Game {

    private Play play;

    private ArrayList<Ghost> ghostArrayList;
    private ArrayList<Fruit> fruitArrayList;
    private ArrayList<Pacman> pacmanArrayList;
    private ArrayList<Block> blockArrayList;
    private Pacman player;

    private ArrayList<String> myData;


    public Game(Play play) {
        this.player = new Pacman(-1,-1);
        INIT(play);
    }

    public Game() {
        pacmanArrayList = new ArrayList<>();
        fruitArrayList = new ArrayList<>();
        ghostArrayList = new ArrayList<>();
        blockArrayList = new ArrayList<>();
    }

    public Game(Game g) {
        pacmanArrayList = new ArrayList<>(g.getPacmanArrayList());
        fruitArrayList = new ArrayList<>(g.getFruitArrayList());
        ghostArrayList = new ArrayList<>(g.getGhostArrayList());
        blockArrayList = new ArrayList<>(g.getBlockArrayList());
        player = new Pacman(g.getPlayer());
    }

    public void INIT(Play play) {
        this.play = play;

        ghostArrayList = new ArrayList<>();
        pacmanArrayList = new ArrayList<>();
        fruitArrayList = new ArrayList<>();
        blockArrayList = new ArrayList<>();

        myData = play.getBoard();

        for (int i = 0; i < myData.size(); i++) {
            String tmp = myData.get(i);
            String [] tmp2 = myData.get(i).split(",");

            if (tmp2[0].equals("P") || tmp2[0].equals("p")) {
                Pacman newPacman = new Pacman(tmp2, 3, 2, 4);
                pacmanArrayList.add(newPacman);
            }

            else if (tmp2[0].equals("F") || tmp2[0].equals("f")) {
                Fruit newFruit = new Fruit(tmp2, 3, 2, 4);
                fruitArrayList.add(newFruit);
            }

            else if (tmp2[0].equals("G") || tmp2[0].equals("g")) {
                Ghost newGhost = new Ghost(tmp2, 3 , 2 , 4);
                ghostArrayList.add(newGhost);
            }

            else if (tmp2[0].equals("B") || tmp2[0].equals("b")) {
                Block newBlock = new Block(tmp2, 3, 2, 4, 6 ,5 , 7);
                blockArrayList.add(newBlock);
            }

            else if (tmp2[0].equals("M") || tmp2[0].equals("m")) {
                player = new Pacman(tmp2, 3, 2, 1);
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

        INIT(play);
    }

}
