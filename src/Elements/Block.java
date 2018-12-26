package Elements;

import Geom.Point3D;

public class Block {

	private int _ID;
	private char _type;
	private Point3D _pointMin, _pointMax;
	private double _weight;
	private double _x1, _y1, _z1;
	private double _x2, _y2, _z2;
	
	private static int _IDGUI = 0;


	public Block(String[] data, int x1, int y1, int z1, int x2, int y2, int z2) {
		_type = 'B';
		_ID = Integer.parseInt(data[1]);

		_x1 = Double.parseDouble(data[x1]);
		_y1 = Double.parseDouble(data[y1]);
		_z1 = Double.parseDouble(data[z1]);

		_x2 = Double.parseDouble(data[x2]);
		_y2= Double.parseDouble(data[y2]);
		_z2 = Double.parseDouble(data[z2]);

		_pointMin = new Point3D(Math.min(_x1, _x2), Math.min(_y1, _y2), Math.min(_z1, _z2));
		_pointMax = new Point3D(Math.max(_x1, _x2), Math.max(_y1, _y2), Math.max(_z1, _z2));

		_weight = Double.parseDouble(data[8]);
	}


	public Point3D get_pointMax() {
		return _pointMax;
	}

	public Point3D get_pointMin() {
		return _pointMin;
	}

	public void set_pointMax(Point3D _pointMax) {
		this._pointMax = _pointMax;
	}

	public void set_pointMin(Point3D _pointMin) {
		this._pointMin = _pointMin;
	}


}
