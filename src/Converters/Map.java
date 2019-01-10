package Converters;

import java.awt.image.BufferedImage;
import Coords.MyCoords;
import Geom.Point3D;

/**
 * Class that defines a map, with coordiante ranges, and pixel ranges. Also it
 * supports the conversation of pixels to coordiantes and vice versa
 */
public class Map {

	private int width, height;
	private MyCoords coords;
	private Range coordsRangeX, coordsRangeY;
	private BufferedImage mapImage;
	private pixelsCoordsConverter converter;

	// Range coordsRangeX, Range coordsRangeY
	public Map(BufferedImage mapImage, int width, int height, double leftbotX, double righttopX, double leftbotY,
			double righttopY) {
		this.mapImage = mapImage;
		this.width = width;
		this.height = height;
		this.coordsRangeX = new Range(leftbotX, righttopX);
		this.coordsRangeY = new Range(leftbotY, righttopY);

		this.coords = new MyCoords();

		initConverter();
	}

	/**
	 * Initializes the pixel/coords converter object with given ranges
	 */
	private void initConverter() {
		converter = new pixelsCoordsConverter(width, height, coordsRangeX.getMin(), coordsRangeX.getMax(),
				coordsRangeY.getMin(), coordsRangeY.getMax());
	}

	public pixelsCoordsConverter getConverter() {
		return converter;
	}

	public BufferedImage getMapImage() {
		return mapImage;
	}

	/**
	 * Convert gps coordinates to pixels
	 * 
	 * @param p point
	 * @return array with x/y pixels
	 */
	public int[] gps2Pixels(Point3D p) {
		return converter.gps2Pixels(p);
	}

	/**
	 * Convert pixels to coords
	 * 
	 * @param x x pixel
	 * @param y y pixel
	 * @return Point in pixels
	 */
	public Point3D toCoords(int x, int y) {
		return converter.toCoords(x, y);
	}

	/**
	 * Calculates the distance between to pixels on the board
	 * 
	 * @param x1 pixel
	 * @param y1 pixel
	 * @param x2 pixel
	 * @param y2 pixel
	 * @return the distance
	 */
	public double distanceBetweenPixels(int x1, int y1, int x2, int y2) {

		// Coordinates
		Point3D from = toCoords(x1, y1);
		Point3D to = toCoords(x2, y2);

		return coords.distance3d(from, to);
	}

	/**
	 * Calculates the angle between to pixels on the board
	 * 
	 * @param x1 x pixel point1
	 * @param y1 y pixel point1
	 * @param x2 x pixel point2
	 * @param y2 y pixel point2
	 * @return angle between pixels
	 */
	public double angleBetweenPixels(int x1, int y1, int x2, int y2) {

		// Coordinates
		Point3D from = toCoords(x1, y1);
		Point3D to = toCoords(x2, y2);

		double angle = coords.azimuth_elevation_dist(from, to)[0];

		return angle;
	}
}
