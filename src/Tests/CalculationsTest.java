package Tests;

import Algorithms.Calculations;
import Elements.Game;
import Elements.Pacman;
import Geom.Point3D;
import Robot.Play;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * This class represents a simple JUnit test for the Calculations class.
 * The class has 3 tests that cover the class purpose.
 */
class CalculationsTest {

    private static String path;

    private static Play play1;
    private static Play play2;

    private static Game game1;
    private static Game game2;

    private static Calculations calculations1;
    private static Calculations calculations2;

    private static Pacman playerPoint1;
    private static Pacman playerPoint2;

    private Point3D expectedFruit1 = new Point3D(35.20699129989315, 32.10302552377673);
    private Point3D expectedFruit2 = new Point3D(35.209463750615576, 32.10312982526325);

    /**
     * Setting up 2 different games with 2 different players in order to check 2 different paths.
     */
    @BeforeAll
    static void setUp() {
        path = "C:\\Users\\Itayt\\IdeaProjects\\Ex4\\data\\Ex4_OOP_example5.csv";

        play1 = new Play(path);
        play2 = new Play(path);

        game1 = new Game(play1);
        game2 = new Game(play2);

        playerPoint1 = new Pacman(35.205273655494935, 32.103879749552775);
        playerPoint2 = new Pacman(35.210065300077936, 32.102740715563506);

        game1.addPlayer(playerPoint1);
        game2.addPlayer(playerPoint2);

        calculations1 = new Calculations(game1, 1283, 559);
        calculations2 = new Calculations(game2, 1283, 559);

    }

    /**
     * This test checks that the initiation of the class algorithm works and that it fills up the
     * necessary objects.
     */

    @Test
    void INIT() {
        calculations1.INIT();
        calculations2.INIT();

        boolean flag1 = true;
        boolean flag2 = true;

        if(calculations1.getShortestPath().isEmpty()) flag1 = false;
        if(calculations1.getFinalPath().isEmpty()) flag1 = false;
        if(calculations1.getPathsDistance().isEmpty()) flag1 = false;
        if(calculations1.getTargetFruit()== null) flag1 = false;

        if(calculations2.getShortestPath().isEmpty()) flag2 = false;
        if(calculations2.getFinalPath().isEmpty()) flag2 = false;
        if(calculations2.getPathsDistance().isEmpty()) flag2 = false;
        if(calculations2.getTargetFruit()== null) flag2 = false;

        assertEquals(true, flag1, "Function is wrong");
        assertEquals(true, flag2, "Function is wrong");
    }

    /**
     * This test checks that the algorithm calculates the right path for the player.
     */
    @Test
    void getFinalPath() {
        boolean flag1 = true;
        boolean flag2 = true;

        ArrayList<Integer> path1 = new ArrayList<>();
        ArrayList<Integer> path2 = new ArrayList<>();

        path1.add(0);     path2.add(0);
        path1.add(8);     path2.add(9);
        path1.add(9);

        for(int i = 0; i < calculations1.getFinalPath().size(); i++) {
            if(calculations1.getFinalPath().get(i).getID()!= path1.get(i)) flag1 = false;
        }

        for(int i = 0; i < calculations2.getFinalPath().size(); i++) {
            if(calculations2.getFinalPath().get(i).getID()!= path2.get(i)) flag2 = false;
        }

        assertEquals(true, flag1, "Function is wrong");
        assertEquals(true, flag2, "Function is wrong");
    }

    /**
     * This test checks that the fruit target for the player is correct.
     */
    @Test
    void getTargetFruit() {

        boolean flag1 = false;
        Point3D actual1 = calculations1.getTargetFruit().getPointGPS();
        if(expectedFruit1.get_x()== actual1.get_x() && expectedFruit1.get_y() == actual1.get_y()) flag1 = true;
        assertEquals(true, flag1, "Function is wrong");

        boolean flag2 = false;
        Point3D actual2 = calculations2.getTargetFruit().getPointGPS();
        if(expectedFruit2.get_x() == actual2.get_x() && expectedFruit2.get_y() == actual2.get_y()) flag2 = true;
        assertEquals(true, flag2, "Function is wrong");
    }
}