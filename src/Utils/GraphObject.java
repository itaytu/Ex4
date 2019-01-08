package Utils;

import Geom.Point3D;

import java.util.ArrayList;

public class GraphObject {

    Point3D pointPixels, pointGPS;
    private int ID;
    private static int IDCounter = 1;
    ArrayList<GraphObject> neighbors;
    private boolean isDone;

    public GraphObject(Point3D pointPixels, Point3D pointGPS) {
        this.pointPixels = pointPixels;
        this.pointGPS = pointGPS;

        this.ID = IDCounter++;
        neighbors = new ArrayList<>();
        isDone = false;
    }


    public ArrayList<GraphObject> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(ArrayList<GraphObject> neighbors) {
        this.neighbors = neighbors;
    }


    public Point3D getPointPixels() {
        return pointPixels;
    }

    public void setPointPixels(Point3D point) {
        this.pointPixels = point;
    }


    public Point3D getPointGPS() { return pointGPS; }

    public void setPointGPS(Point3D point) { this.pointGPS = point; }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }


    public static int getIDCounter() {
        return IDCounter;
    }

    public static void setIDCounter(int IDCounter) {
        GraphObject.IDCounter = IDCounter;
    }

    public static void resetID() {
        IDCounter = 1;
    }

    public static void decreaseID() { IDCounter = IDCounter -1; }

    public void clearNeighbors() {
        neighbors.clear();
    }

}
