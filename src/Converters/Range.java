package Converters;

/**
 * Class that defines a range between minimum and maximum values
 */
public class Range {

	private double min, max, relation = 0;

	/**
	 * Constructs a range between 2 doubles
	 * 
	 * @param min minimum point
	 * @param max maximum point
	 */
	// Min x and Max x, Or Min y and Max y
	public Range(double min, double max) {
		this.min = min;
		this.max = max;
	}

	/**
	 * Constructs a range between 2 integers
	 * 
	 * @param min minimum point
	 * @param max maximum point
	 */
	public Range(int min, int max) {
		this.min = min;
		this.max = max;
	}

	/**
	 * Checks the distance of this range
	 * 
	 * @return distance
	 */
	public double distance() {
		return max - min;
	}

	/**
	 * Calculates the relation between the value and the range
	 * 
	 * @param x
	 *            to check
	 * @return relation between 0.0 and 1.0
	 */
	public double relation(double x) {
		relation = (x - min) / distance();
		return relation;
	}

	/**
	 * Calculate the value of a given relation, in this range
	 * 
	 * @param relation
	 *            to check
	 * @return the value
	 */
	public double getValueFromRelation(double relation) {
		return min + relation * distance();
	}

	/**
	 * Check if specific value found inside range
	 * 
	 * @param x
	 *            to check
	 * @return true or not
	 */
	public boolean isIn(double x) {
		if (x >= min && x <= max) {
			return true;
		}
		return false;
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	public double getRelation() {
		return relation;
	}

}
