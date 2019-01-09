package Tests;

import Converters.pixelsCoordsConverter;
import Elements.Fruit;
import Elements.Game;
import Geom.Point3D;
import Robot.Play;
import Utils.LineOfSight;

import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
    private Point3D p4_1 = new Point3D(552, 451);
    private Point3D p5_0 = new Point3D(414, 286);
    private Point3D p6_0 = new Point3D(1060, 281);



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

/*    @Test
    void checkRectangles() {
        for (Rectangle2D rectangle : lineOfSight.getBlockRectangles()) {
            System.out.println("RECTANGLE: ");
            System.out.println("MinX: " + rectangle.getMinX() + " MaxX: " + rectangle.getMaxX());
            System.out.println("MinY: " + rectangle.getMinY() + " MaxY: " + rectangle.getMaxY());
            System.out.println("------------------------------------------------------");
        }
    }*/

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

    @Test
    void checkIntersectionforp4() {
        System.out.println("Only 1 expected");
        int actual = 0;
        int expected = 1;
        for (Point3D p : fruitPixels) {
            boolean flag = lineOfSight.checkIntersection(p4_1, p);
            if(!flag) actual++;
        }
        assertEquals(expected, actual, "Function is wrong");
        if(expected==actual) System.out.println("True");
        System.out.println("\n----------------------------------\n");
    }

    @Test
    void checkIntersectionforp5() {
        System.out.println("Zero expected");
        int actual = 0;
        int expected = 0;
        for (Point3D p : fruitPixels) {
            boolean flag = lineOfSight.checkIntersection(p5_0, p);
            if(!flag) actual++;
        }
        assertEquals(expected, actual, "Function is wrong");
        if(expected==actual) System.out.println("True");
        System.out.println("\n----------------------------------\n");
    }

    @Test
    void checkIntersectionforp6() {
        System.out.println("Zero expected");
        int actual = 0;
        int expected = 0;
        for (Point3D p : fruitPixels) {
            boolean flag = lineOfSight.checkIntersection(p6_0, p);
            if(!flag) actual++;
        }
        assertEquals(expected, actual, "Function is wrong");
        if(expected==actual) System.out.println("True");
        System.out.println("\n----------------------------------\n");
    }
}