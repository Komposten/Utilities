/*
 * Copyright (c) 2014 Jakob Hjelm 
 */
package komposten.utilities.tools;

import java.text.DecimalFormat;

/**
 * @version
 * <b>1.4.0</b><br />
 * <ul>
 * <li>isPOT() now use a (100x) faster approach based on bitwise operations.
 * <li>Added equals(float, float, float).</li>
 * <li>Added equals(double, double, double).</li>
 * </ul>
 * <b>Older</b><br />
 * 1.3.0<br />
 * <ul>
 * <li>Added isInInterval(int, int, int, boolean).</li>
 * <li>Added isInInterval(float, float, float, boolean).</li>
 * </ul>
 * 1.2.0
 * <ul>
 * <li>Added isPOT(int) and isPOT(long).</li>
 * </ul>
 * 1.1.0
 * <ul>
 * <li>Changed <code>twoDecimals(double)</code> to <code>round(double, int)</code></li>
 * </ul>
 * <b>Older</b> <br />
 * @author Jakob Hjelm
 */
public class MathOps
{
  private static DecimalFormat format_;
  
  static
  {
    format_ = new DecimalFormat("#.##");
  }
  
  public static int round(double d)
  {
    return (int) Math.round(d);
  }
  
  public static int round(float f)
  {
    return Math.round(f);
  }
  
  public static String round(double d, int decimals)
  {
    format_.setMaximumFractionDigits(decimals);
    format_.setMinimumFractionDigits(decimals);
    return format_.format(d);
  }
  
  
  
  /**
   * Compares two floating-point values to see if they are similar enough to be considered equal.
   * @param value1
   * @param value2
   * @param threshold The maximum deviation allowed between two "equal" values.
   * @return <code>true</code> if the difference between the values is lower than <code>threshold</code>, false otherwise.
   */
  public static boolean equals(float value1, float value2, float threshold)
  {
  	return Math.abs(value1 - value2) < threshold;
  }
  
  /**
   * Compares two floating-point values to see if they are similar enough to be considered equal.
   * @param value1
   * @param value2
   * @param threshold The maximum deviation allowed between two "equal" values.
   * @return <code>true</code> if the difference between the values is lower than <code>threshold</code>, false otherwise.
   */
  public static boolean equals(double value1, double value2, double threshold)
  {
  	return Math.abs(value1 - value2) < threshold;
  }
  
  
  
  public static float clamp(float min, float max, float value)
  {
  	return Math.min(Math.max(min, value), max);
  }
  
  public static double clamp(double min, double max, double value)
  {
  	return Math.min(Math.max(min, value), max);
  }
  
  public static float clamp01(float value)
  {
  	return clamp(0, 1, value);
  }
  
  public static double clamp01(double value)
  {
  	return clamp(0, 1, value);
  }
  
  
  
  /**
   * Calculates the distance between two coordinates.
   * @param x1 The first coordinate.
   * @param y1 The first coordinate.
   * @param x2 The second coordinate.
   * @param y2 The second coordinate.
   * @return The distance between (x1, y1) and (x2, y2).
   */
  public static float distance(float x1, float y1, float x2, float y2)
  {
    return (float)Math.sqrt(distanceSqr(x1, y1, x2, y2));
  }
  
  

	/**
	 * Calculates the square of the distance between two coordinates. This is
	 * slightly more efficient than {@link #distance(float, float, float, float)}
	 * when simply comparing distances since it doesn't use
	 * {@link Math#sqrt(double)}. Just remember that the value that
	 * <code>distanceSqr</code> is compared with must also be squared!
	 * 
	 * @param x1 The first coordinate.
	 * @param y1 The first coordinate.
	 * @param x2 The second coordinate.
	 * @param y2 The second coordinate.
	 * @return The squared distance between (x1, y1) and (x2, y2).
	 */
  public static float distanceSqr(float x1, float y1, float x2, float y2)
  {
    float dX = x2 - x1;
    float dY = y2 - y1;
    
    return dX*dX+dY*dY;
  }
  
  
  
	/**
	 * Calculates the angle between the line through (x1, y1) and (x2, y2) and the
	 * x-axis.<br />
	 * 
	 * @param x1 The first coordinate.
	 * @param y1 The first coordinate.
	 * @param x2 The second coordinate.
	 * @param y2 The second coordinate.
	 * @return The angle between the line through the two points and the x-axis in
	 *         radians (in the range <i>-pi</i> to <i>pi</i>).
	 */
  public static float angle(float x1, float y1, float x2, float y2)
  {
    float dX = x2 - x1;
    float dY = y2 - y1;
    
    float angle = (float) Math.atan2(dY, dX);
    
    return angle;
  }
  
  
  
  /**
   * Returns whether <code>value</code> is a power of two or not.
   * </br> Implementation from <a href="http://stackoverflow.com/a/19383296">http://stackoverflow.com/a/19383296</a>
   * @param value A value
   * @return True if <code>value</code> is a power of two, false otherwise.
   */
  public static boolean isPOT(int value)
  {
    return (value > 0) && ((value & (value - 1)) == 0);
  }
  
  
  
  /**
   * Returns whether <code>value</code> is a power of two or not.
   * </br> Implementation from <a href="http://stackoverflow.com/a/19383296">http://stackoverflow.com/a/19383296</a>
   * @param value A value
   * @return True if <code>value</code> is a power of two, false otherwise.
   */
  public static boolean isPOT(long value)
  {
    return (value > 0) && ((value & (value - 1)) == 0);
  }
  
  
  
  /**
   * Checks if the a value lies within an interval.
   * @param value
   * @param min The interval's minimum value.
   * @param max The interval's maximum value.
   * @param inclusive
   * @return If the provided value lies within the interval or not.
   */
	public static boolean isInInterval(int value, int min, int max, boolean inclusive)
  {
		if (inclusive)
			return value >= min && value <= max;
		else
			return value > min && value < max;
  }
  
  

  /**
   * Checks if the a value lies within an interval.
   * @param value
   * @param min The interval's minimum value.
   * @param max The interval's maximum value.
   * @param inclusive
   * @return If the provided value lies within the interval or not.
   */
	public static boolean isInInterval(float value, float min, float max, boolean inclusive)
  {
		if (inclusive)
			return value >= min && value <= max;
		else
			return value > min && value < max;
  }
}