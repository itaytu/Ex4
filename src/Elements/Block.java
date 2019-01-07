package Elements;

import Geom.Point3D;

import java.util.ArrayList;

public class Block {

	private int ID;
	private char type;
	private Point3D MinLeft, MaxLeft, MinRight, MaxRight;
	private double weight;
	private double x1, y1, z1;
	private double x2, y2, z2;

	private ArrayList<Point3D> points;

	public Block(String[] data) {
		type = 'B';
		ID = Integer.parseInt(data[1]);

		this.x1 = Double.parseDouble(data[3]);
		this.y1 = Double.parseDouble(data[2]);
		this.z1 = Double.parseDouble(data[4]);

		this.x2 = Double.parseDouble(data[6]);
		this.y2 = Double.parseDouble(data[5]);
		this.z2 = Double.parseDouble(data[7]);

		weight = Double.parseDouble(data[8]);

		MinLeft = new Point3D(Math.min(this.x1, this.x2), Math.min(this.y1, this.y2), 0);
		MaxLeft = new Point3D(Math.min(this.x1, this.x2), Math.max(this.y1, this.y2), 0);

		MinRight = new Point3D(Math.max(this.x1,this.x2), Math.min(this.y1, this.y2),0);
		MaxRight = new Point3D(Math.max(this.x1, this.x2), Math.max(this.y1, this.y2), 0);

		points = new ArrayList<>();

		points.add(MaxLeft);
		points.add(MaxRight);
		points.add(MinRight);
		points.add(MinLeft);

	}


	public Point3D getMaxRight() {
		return MaxRight;
	}

	public void setMaxRight(Point3D maxRight) {
		this.MaxRight = maxRight;
	}


	public Point3D getMinLeft() {
		return MinLeft;
	}

	public void setMinLeft(Point3D minLeft) {
		this.MinLeft = minLeft;
	}


	public Point3D getMinRight() {
		return MinRight;
	}

	public void setMinRight(Point3D minRight) {
		MinRight = minRight;
	}


	public Point3D getMaxLeft() {
		return MaxLeft;
	}

	public void setMaxLeft(Point3D maxLeft) {
		MaxLeft = maxLeft;
	}


	public ArrayList<Point3D> getPoints() {
		return points;
	}

}
