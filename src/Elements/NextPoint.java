package Elements;

import Coords.MyCoords;
import Geom.Point3D;

import java.util.Observable;

/**
 * This class defines the next point, which created when user clicked on the Board.
 * This class extends Observable, and will notify the Controller when next point of Pacman is changed,
 * The controller will update the Game model with the new point, hence update Board with the new placements.
 * This class also calculates the azimuth between next point and current point.
 */
public class NextPoint extends Observable {

    private Point3D nextPoint;
    private double[] azimuth;
    private MyCoords coords = new MyCoords();

    public void setPoints(Point3D nextPoint, Point3D currentPoint) {
        synchronized (this) {
            this.nextPoint = nextPoint;
            azimuth = coords.azimuth_elevation_dist(currentPoint, nextPoint);
        }
        setChanged();
        notifyObservers();
    }

    public synchronized Point3D getPoint() {
        return nextPoint;
    }

    public synchronized double getAzimuth() {
        return azimuth[0];
    }

    public NextPoint getNextPoint() {
        return this;
    }
}