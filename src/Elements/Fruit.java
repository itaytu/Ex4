package Elements;

import Geom.Point3D;

/**
 * This class represents a simple Fruit Object holding the necessary information.
 */
public class Fruit {

	private int _ID;
	private Point3D _point;
	private double _weight;
	private double _x, _y, _z;

	/**
	 * Constructor from a given CSV file.
	 * @param data string
	 * @param x index for x
	 * @param y index for y
	 * @param z index for z
	 */
	public Fruit(String[] data, int x, int y, int z) {
		_ID = Integer.parseInt(data[1]);

		_x = Double.parseDouble(data[x]);
		_y = Double.parseDouble(data[y]);
		_z = Double.parseDouble(data[z]);
		_point = new Point3D(_x, _y, _z);

		_weight = Double.parseDouble(data[5]);
	}

	public void setPoint(Point3D point) {
		_point = point;
	}
	
	public Point3D getPoint() {
		return _point;
	}

}
