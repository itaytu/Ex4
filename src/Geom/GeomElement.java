package Geom;

public class GeomElement implements Geom_element {

	Point3D Geom;

	public GeomElement(Point3D Geompoint) {
		this.Geom = Geompoint;
	}

	@Override
	public double distance3D(Point3D p) {
		double distance = p.distance3D(Geom);
		return distance;
	}

	@Override
	public double distance2D(Point3D p) {
		double distance = p.distance2D(Geom);
		return distance;
	}

}
