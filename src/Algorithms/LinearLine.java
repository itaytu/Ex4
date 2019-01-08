package Algorithms;

import Geom.Point3D;

import java.awt.geom.Line2D;

public class LinearLine {

    private Point3D min;
    private Point3D max;

    private Line2D line;

    public LinearLine(Point3D x1, Point3D x2) {
        this.min = new Point3D(Math.min(x1.get_x(), x2.get_x()), Math.min(x1.get_y(), x2.get_y()));
        this.max = new Point3D(Math.max(x1.get_x(), x2.get_x()), Math.max(x1.get_y(), x2.get_y()));

        line = new Line2D.Double(min.get_x(), min.get_y(), max.get_x(), max.get_y());
    }

    public Point3D getMin() {
        return min;
    }

    public void setMin(Point3D x1) {
        this.min = x1;
    }


    public Point3D getMax() {
        return max;
    }

    public void setMax(Point3D x2) {
        this.max = x2;
    }

    public Line2D getLine() {
        return  line;
    }

    public void setLine(Line2D line) {
        this.line = line;
    }


}
