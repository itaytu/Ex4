package Elements;

import Geom.Point3D;

import java.util.ArrayList;
/**
 * This class represents a simple Block Object holding the necessary information.
 */
public class Block {

	private int ID;
	private Point3D MinLeft, MaxLeft, MinRight, MaxRight;
	private double weight;
	private double x1, y1, z1;
	private double x2, y2, z2;

	private ArrayList<Point3D> points;

	/**
	 * Constructor from a given CSV file.
	 * @param data string data
	 */
	public Block(String[] data) {
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


	/**
	 * Get Top Left corner.
	 * @return max left point
	 */
	public Point3D getMaxLeft() {
		return MaxLeft;
	}

	/**
	 * Get Top Right corner.
	 * @return max right point
	 */
	public Point3D getMaxRight() {
		return MaxRight;
	}

	/**
	 * Get Bottom Right corner.
	 * @return min right point
	 */
	public Point3D getMinRight() {
		return MinRight;
	}

	/**
	 * Get Bottom Left corner.
	 * @return min left point
	 */
	public Point3D getMinLeft() {
		return MinLeft;
	}
}
