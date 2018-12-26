package Elements;

import Coords.MyCoords;
import GIS.GIS_element;
import GIS.Meta_data;
import Geom.GeomElement;
import Geom.Geom_element;
import Geom.Point3D;

public class Ghost implements GIS_element {
	
	private int _ID;
	private char _type;
	private Point3D _point;
	private double _speed;
	private double _eatRadius;
	private double _x, _y, _z;
	private MyCoords coords;
	private static int _IDGUI = 0;
	
	
	public Ghost(String[] data, int x, int y, int z) {
		_type = 'G';
		_ID = Integer.parseInt(data[1]);

		_x = Double.parseDouble(data[x]);
		_y = Double.parseDouble(data[y]);
		_z = Double.parseDouble(data[z]);
		_point = new Point3D(_x, _y, _z);

		_speed = Double.parseDouble(data[5]);
		_eatRadius = Double.parseDouble(data[6]);
	}

	public Ghost(double x, double y) {
		_ID = _IDGUI++;
		_type = 'G';
		_x = x; _y = y; _z = 0;
		_point = new Point3D(_x, _y, _z);
		_speed = (int) (Math.random()*(5)+1);
		_eatRadius = (int) (Math.random()*(5)+1);
	}

	public Ghost(Ghost g) {
		_ID = g._ID;
		_type = g._type;
		_x = g._x; _y = g._y; _z = g._z;
		_point = new Point3D(g._point);
		_speed = g._speed;
		_eatRadius = g._eatRadius;
	}

	
	public void setPoint(Point3D point) {
		_point = point;
	}
	
	public Point3D getPoint() {
		return _point;
	}


	@Override
	public Geom_element getGeom() {
		GeomElement ghostGeom = new GeomElement(_point);
		return ghostGeom;
	}

	@Override
	public Meta_data getData() {
		return null;
	}

	@Override
	public void translate(Point3D vec) {
		_point = coords.add(_point, vec);
	}


}

