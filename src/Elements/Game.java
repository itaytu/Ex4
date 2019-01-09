package Elements;

import Geom.Point3D;
import Robot.Play;

import java.util.ArrayList;

/**
 * This class represents the game holding all the information about the objects in the game.
 *
 * Such objects are: Player, Fruits, Other Pacmans, Ghosts, Blocks.
 */
public class Game {

    private ArrayList<Ghost> ghostArrayList;
    private ArrayList<Fruit> fruitArrayList;
    private ArrayList<Pacman> pacmanArrayList;
    private ArrayList<Block> blockArrayList;
    private Pacman player;


    /**
     * Constructor for a game from a play.
     * @param play
     */
    public Game(Play play) {
        this.player = new Pacman(-1,-1);
        init(play);
    }

    /**
     * Empty Constructor.
     */
    public Game() {
        pacmanArrayList = new ArrayList<>();
        fruitArrayList = new ArrayList<>();
        ghostArrayList = new ArrayList<>();
        blockArrayList = new ArrayList<>();
    }


    /**
     * Reading the information from the play and sorting it by the different objects.
     *
     * @param play
     */
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
    /**
     * Simple get method to get the Pacmans.
     * @return
     */
    public ArrayList<Pacman> getPacmanArrayList() {
        return pacmanArrayList;
    }

    /**
     * Simple get method to get the Fruits.
     * @return
     */
    public ArrayList<Fruit> getFruitArrayList() {
        return fruitArrayList;
    }

    /**
     * Simple get method to get the Ghosts.
     * @return
     */
    public ArrayList<Ghost> getGhostArrayList() {
        return ghostArrayList;
    }

    /**
     * Simple get method to get the Blocks.
     * @return
     */
    public ArrayList<Block> getBlockArrayList() {
        return blockArrayList;
    }

    /**
     * Add a player to the game.
     * @param p
     */
    public void addPlayer(Pacman p) {
        player = new Pacman(p);
    }

    /**
     * Simple get method to get the Player.
     * @return
     */
    public Pacman getPlayer () {
        return player;
    }

    /**
     * Simple set method for the Player.
     * @param p
     */
    public void setPlayer(Point3D p) {
        this.player = new Pacman(p.get_x(), p.get_y());
    }

    /**
     * Updating the game.
     * @param play
     */
    public void update(Play play) {
        pacmanArrayList.clear();
        fruitArrayList.clear();
        ghostArrayList.clear();
        blockArrayList.clear();

        init(play);
    }
}
