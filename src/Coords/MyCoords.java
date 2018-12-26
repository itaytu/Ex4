package Coords;

import Geom.Point3D;

/**
 * This class represents an implementation of the coords_converter interface.
 * This class has simple functions to calculate various things.
 * 
 * Some of the functions included are: add, distance3D, vector3D, isValid_GPS_point etc.
 * 
 * @author Itay Tuson and Sagi Oshri
 *
 */
public class MyCoords implements coords_converter {
	
	final double earthR = 6371000;
	
	/** computes a new point which is the gps point transformed by a 3D vector (in meters)*/
	@Override
	public Point3D add(Point3D gps, Point3D local_vector_in_meter) {
		if(!isValid_GPS_Point(gps)) return null;
		double lonNorm = getLon(gps);
		double p1_x = gps.x() + (180/Math.PI)*(Math.asin(local_vector_in_meter.x()/earthR));
		double p1_y = gps.y() + (180/Math.PI)*(Math.asin(local_vector_in_meter.y()/(earthR*lonNorm)));
		double p1_z = gps.z() + local_vector_in_meter.z();
		Point3D newPoint = new Point3D(p1_x, p1_y, p1_z);
		
		if(p1_x<-180||180<p1_x) {
			newPoint.set_x(((p1_x+180)%360) -180);
		}
		if(!isValid_GPS_Point(newPoint)) return null;
		return newPoint;	
	}
	
	/** computes the 3D distance (in meters) between the two gps like points */
	@Override
	public double distance3d(Point3D gps0, Point3D gps1) {
		if(!(isValid_GPS_Point(gps0))||!(isValid_GPS_Point(gps1))) return Double.MAX_VALUE;
		
		double lonNorm = getLon(gps0);
		double Dis_x = Math.sin((gps1.x()-gps0.x())*(Math.PI/180))*earthR;
		double Dis_y = Math.sin((gps1.y()-gps0.y())*(Math.PI/180))*lonNorm*earthR;
		double Dis_z = gps1.z()-gps0.z();
		double distance = Math.sqrt((Dis_x*Dis_x) + (Dis_y*Dis_y));
		return distance;
	}

	/** computes the 3D vector (in meters) between two gps like points  */
	@Override
	public Point3D vector3D(Point3D gps0, Point3D gps1) {
		if(!(isValid_GPS_Point(gps0))||!(isValid_GPS_Point(gps1))) return null;
		
		double lonNorm = getLon(gps0);
		double vec_X = Math.sin((gps1.x()-gps0.x())*(Math.PI/180))*earthR;
		double vec_Y = Math.sin((gps1.y()-gps0.y())*(Math.PI/180))*earthR*lonNorm;
		double vec_Z = gps1.z()-gps0.z();
		Point3D newVector = new Point3D(vec_X, vec_Y, vec_Z);
		return newVector;
	}

	/** computes the polar representation of the 3D vector be gps0-->gps1 
	 *  Note: this method should return an azimuth (aka yaw), elevation (pitch), and distance
	 */
	@Override
	public double[] azimuth_elevation_dist(Point3D gps0, Point3D gps1) {
		if(!(isValid_GPS_Point(gps0))||!(isValid_GPS_Point(gps1))) return null;
		Point3D vec =vector3D(gps0, gps1);
		double azimut=Math.toDegrees(Math.atan(Math.abs(vec.x()/vec.y())));
		if (vec.y()<0) {
			if (vec.x()>0) {
				azimut=180-azimut;
			}else
				azimut=180+azimut;
		}else {
			if(vec.x()<0) {
				azimut=360-azimut;
			}
		}
		//*distance*//
		double distance = distance3d(gps0,gps1);
		//*elevation*//
		double high = gps1.z() - gps0.z();
		double elevation = Math.toDegrees(Math.asin(high/distance));
		double arr[] = {azimut,elevation,distance};
		return arr;
	}

	/**
	 * return true iff this point is a valid lat, lon , lat coordinate: [-180,+180],[-90,+90],[-450, +inf]
	 * @param p
	 * @return boolean true or false.
	 */
	@Override
	public boolean isValid_GPS_Point(Point3D p) {
		return ((-180<=p.x() && p.x()<=180) &&
				(-90<=p.y() && p.y()<=90) && 
				(-450<=p.z() && p.z()<=9000));
	}
	
	/** return the LonNorm calculated for a given point. */
	private double getLon(Point3D point) {
		return Math.cos(point.x()*(Math.PI/180));
	}
}
