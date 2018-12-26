package Elements;

import Geom.Point3D;

public class Fruit {

	private int _ID;
	private char _type;
	private Point3D _point;
	private double _weight;
	private double _x, _y, _z;

	private boolean _isEaten;
	
	public Fruit(String[] data, int x, int y, int z) {
		_type = 'F';
		_ID = Integer.parseInt(data[1]);

		_x = Double.parseDouble(data[x]);
		_y = Double.parseDouble(data[y]);
		_z = Double.parseDouble(data[z]);
		_point = new Point3D(_x, _y, _z);

		_isEaten = false;
		_weight = Double.parseDouble(data[5]);
	}
	
	public Fruit(Fruit f) {
		_ID = f._ID;
		_type = f._type;
		_x = f._x; _y = f._y; _z = f._z;
		_point = new Point3D(f._point);
		_isEaten = f._isEaten;
		_weight = f._weight;
	}

	public void setPoint(Point3D point) {
		_point = point;
	}
	
	public Point3D getPoint() {
		return _point;
	}
	
	public void setIsEaten(boolean flag) {
		_isEaten = flag;
	}
	
	public boolean getIsEaten() {
		return _isEaten;
	}

	
}
