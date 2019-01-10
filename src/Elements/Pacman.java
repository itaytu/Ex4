package Elements;

import Geom.Point3D;

/**
 * This class represents a simple Pacman Object holding the necessary information.
 */
public class Pacman {
	
	private int _ID;
	private char _type;
	private Point3D _point;
	private int _weight;
	private double _speed;
	private double _eatRadius;
	private double _x, _y, _z;

	private static int _IDGUI = 0;

	/**
	 * Constructor from a given CSV file.
	 * @param data string data
	 * @param x index for x
	 * @param y index for y
	 * @param z index for z
	 */
	public Pacman(String [] data, int x, int y, int z) {
		_type = 'P';
		_ID = Integer.parseInt(data[1]);

		_x = Double.parseDouble(data[x]);
		_y = Double.parseDouble(data[y]);
		_z = Double.parseDouble(data[z]);
		_point = new Point3D(_x, _y, _z);

		_weight = (int) (Math.random()*(5)+1);
		_speed = Double.parseDouble(data[5]);
		_eatRadius = Double.parseDouble(data[6]);
	}

	/**
	 * GUI Constructor from clicks on a board.
	 * @param x pixels x
	 * @param y pixels y
	 */
	public Pacman(double x, double y) {
		_ID = _IDGUI++;
		_type = 'M';
		_x = x; _y = y; _z = 0;
		_point = new Point3D(_x, _y, _z);
		_weight = (int) (Math.random()*(5)+1);
		_speed = (int) (Math.random()*(5)+1);
		_eatRadius = (int) (Math.random()*(5)+1);
	}

	/**
	 * Copy Constructor.
	 * @param p pacman
	 */
	public Pacman(Pacman p) {
		_ID = p._ID;
		_type = p._type;
		_x = p._x; _y = p._y; _z = p._z;
		_point = new Point3D(p._point);
		_weight = p._weight;
		_speed = p._speed;
		_eatRadius = p._eatRadius;
	}

	
	public void setPoint(Point3D point) {
		_point = point;
	}
	
	public Point3D getPoint() {
		return _point;
	}


}
