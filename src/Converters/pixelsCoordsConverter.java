package Converters;

import Geom.Point3D;

/**
 * Class that converts pixels to coordinates, and vice versa, in a given ranges
 */
public class pixelsCoordsConverter {

	private Range imageXrange, imageYrange, // Frame range
			xRange, yRange; // Coords range

	private int maxPixelY;
	private int minPixelY;

	/**
	 * Constructs the ranges from the frame height and width, and X/Y coordinates
	 * 
	 * @param width board width
	 * @param height board height
	 * @param leftBotX left bottom corner x
	 * @param rightTopX right top corner x
	 * @param leftBotY left bottom corner y
	 * @param rightTopY right top corner y
	 */
	public pixelsCoordsConverter(int width, int height, double leftBotX, double rightTopX, double leftBotY,
			double rightTopY) {
		this.imageXrange = new Range(0, width);
		this.imageYrange = new Range(0, height);

		this.xRange = new Range(leftBotX, rightTopX);
		this.yRange = new Range(leftBotY, rightTopY);

		this.minPixelY = 0;
		this.maxPixelY = height;
	}

	/**
	 * Convert pixel point to a coordinate
	 * 
	 * @param x
	 *            pixel
	 * @param y
	 *            pixel
	 * @return Point of coordinate
	 */
	public Point3D toCoords(int x, int y) {
		double XRelation = imageXrange.relation(x);
		double YRelation = imageYrange.relation(y);

		double coordsXValue = xRange.getValueFromRelation(XRelation);
		double coordsYValue = yRange.getValueFromRelation(YRelation);

		double epsilon, r;

		if (YRelation >= 0.5) {
			epsilon = yRange.getMax() - coordsYValue;

			r = yRange.getMin() + epsilon;

		} else {
			epsilon = coordsYValue - yRange.getMin();

			r = yRange.getMax() - epsilon;
		}

		return new Point3D(coordsXValue, r, 0);
	}

	/**
	 * Converts coordinate point to a pixel
	 * 
	 * @param p
	 *            gps point
	 * @return array with x and y
	 */
	public int[] gps2Pixels(Point3D p) {
		// getting the relations
		double relX = xRange.relation(p.get_x());
		double relY = yRange.relation(p.get_y());

		// gettings the vals
		double x = imageXrange.getValueFromRelation(relX);
		double y = imageYrange.getValueFromRelation(relY);

		if (relY >= 0.5) {
			double eps = maxPixelY - y;
			y = minPixelY + eps;
		} else {
			double eps = y - minPixelY;
			y = maxPixelY - eps;
		}

		return new int[]{ (int) x, (int) y };
	}

}
