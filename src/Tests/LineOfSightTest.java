package Tests;

import Converters.pixelsCoordsConverter;
import Elements.Fruit;
import Elements.Game;
import Geom.Point3D;
import Robot.Play;
import Utils.LineOfSight;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * This class represents a simple Junit test for the LineOfSight class.
 * The purpose of this class is to test if the function that checks if there is a straight line
 * between two points, considering all the obstacles in the way.
 */
class LineOfSightTest {

    private static String path;
    private static Play play;
    private static Game game;
    private static ArrayList<Point3D> fruitPixels;

    private static pixelsCoordsConverter converter;
    private static LineOfSight lineOfSight;

    private Point3D p1_2 = new Point3D(540, 153);
    private Point3D p2_3 = new Point3D(959, 105);
    private Point3D p3_1 = new Point3D(997, 435);
    private Point3D p4_0 = new Point3D(414, 286);


    /**
     * This section sets up all the necessary objects needed for the calculation.
     * Including a converter to convert from pixels to GPS coordinates and vice versa,
     * Rectangles that represent the blocks.
     */
    @BeforeAll
    static void setUp() {
        path = "C:\\Users\\Itayt\\IdeaProjects\\Ex4\\data\\Ex4_OOP_example5.csv";
        play = new Play(path);
        game = new Game(play);
        lineOfSight = new LineOfSight(game.getBlockArrayList(), 1283, 585);
        converter = new pixelsCoordsConverter( 1283, 585, 35.20238, 35.21236, 32.10190, 32.10569);
        fruitPixels = new ArrayList<>();
        for (Fruit f : game.getFruitArrayList()) {
            int [] point = converter.gps2Pixels(f.getPoint());
            Point3D tmp = new Point3D(point[0], point[1]);
            fruitPixels.add(tmp);
        }
    }

    /**
     * This test checks a line of sight from player 1 to all the fruits on the map.
     */
    @Test
    void checkIntersectionforp1() {
        System.out.println("Only 2 expected");
        int actual = 0;
        int expected = 2;
        for (Point3D p : fruitPixels) {
            boolean flag = lineOfSight.checkIntersection(p1_2, p);
            if(!flag) actual++;
        }
        assertEquals(expected, actual, "Function is wrong");
        if(expected==actual) System.out.println("True");
        System.out.println("\n----------------------------------\n");
    }

    /**
     * This test checks a line of sight from player 2 to all the fruits on the map.
     */
    @Test
    void checkIntersectionforp2() {
        System.out.println("Only 3 expected");
        int actual = 0;
        int expected = 3;
        for (Point3D p : fruitPixels) {
            boolean flag = lineOfSight.checkIntersection(p2_3, p);
            if(!flag) actual++;
        }
        assertEquals(expected, actual, "Function is wrong");
        if(expected==actual) System.out.println("True");
        System.out.println("\n----------------------------------\n");
    }

    /**
     * This test checks a line of sight from player 3 to all the fruits on the map.
     */
    @Test
    void checkIntersectionforp3() {
        System.out.println("Only 1 expected");
        int actual = 0;
        int expected = 1;
        for (Point3D p : fruitPixels) {
            boolean flag = lineOfSight.checkIntersection(p3_1, p);
            if(!flag) actual++;
        }
        assertEquals(expected, actual, "Function is wrong");
        if(expected==actual) System.out.println("True");
        System.out.println("\n----------------------------------\n");
    }

    /**
     * This test checks a line of sight from player 4 to all the fruits on the map.
     */
    @Test
    void checkIntersectionforp4() {
        System.out.println("Zero expected");
        int actual = 0;
        int expected = 0;
        for (Point3D p : fruitPixels) {
            boolean flag = lineOfSight.checkIntersection(p4_0, p);
            if(!flag) actual++;
        }
        assertEquals(expected, actual, "Function is wrong");
        if(expected==actual) System.out.println("True");
        System.out.println("\n----------------------------------\n");
    }

}