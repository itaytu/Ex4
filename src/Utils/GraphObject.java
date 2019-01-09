package Utils;

import Geom.Point3D;

import java.util.ArrayList;

/**
 * This class represents the Graph Object needed in order to calculate the Algorithm.
 * The Graph Object is a generic object containing an ID, Point3D in GPS & Pixels, Neighbors, and a boolean
 * flag to check if the object has finished calculations in the BFS algorithm.
 */
public class GraphObject {

    Point3D pointPixels, pointGPS;
    private int ID;
    private static int IDCounter = 1;
    ArrayList<GraphObject> neighbors;
    private boolean isDone;

    /**
     * The constructor initiates the object Point3D in GPS & Pixels, the ID and the neighbors ArrayList.
     * @param pointPixels
     * @param pointGPS
     */
    public GraphObject(Point3D pointPixels, Point3D pointGPS) {
        this.pointPixels = pointPixels;
        this.pointGPS = pointGPS;

        this.ID = IDCounter++;
        neighbors = new ArrayList<>();
        isDone = false;
    }

    /**
     * Simple get Method to return the ArrayList containing the neighbors of this object.
     * @return neighbors
     */

    public ArrayList<GraphObject> getNeighbors() {
        return neighbors;
    }


    /**
     * Simple get Method to return the Point3D in pixels and GPS.
     * @return pixel & GPS point
     */
    public Point3D getPointPixels() {
        return pointPixels;
    }

    public Point3D getPointGPS() { return pointGPS; }

    /**
     * Simple set & get Method for the objects ID.
     * @return
     */
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Simple set & get method to check if the object is done calculating.
     * @return
     */
    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    /**
     * Resets the static ID counter.
     */
    public static void resetID() {
        IDCounter = 1;
    }

    /**
     * Decreases the static ID counter by 1.
     */
    public static void decreaseID() { IDCounter = IDCounter -1; }

    /**
     * Clears the object neighbors.
     */
    public void clearNeighbors() {
        neighbors.clear();
    }

}
