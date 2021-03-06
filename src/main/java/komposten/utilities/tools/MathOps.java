/*
 * Copyright 2014, 2015, 2017, 2018 Jakob Hjelm
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package komposten.utilities.tools;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * @version
 * <b>1.6.0</b><br />
 * <ul>
 * <li>Added isDouble(String).</li>
 * </ul>
 * <b>Older</b><br />
 * 1.5.0<br />
 * <ul>
 * <li>Added dotProduct(double[], double[])</li>
 * </ul>
 * 1.4.0<br />
 * <ul>
 * <li>isPOT() now use a (100x) faster approach based on bitwise operations.
 * <li>Added equals(float, float, float).</li>
 * <li>Added equals(double, double, double).</li>
 * </ul>
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
 * @author Jakob Hjelm
 */
public class MathOps
{
	public static final String doubleRegex = "[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*";
	
  private static DecimalFormat format_;
  private static final Pattern DOUBLE_PATTERN;
  
  static
  {
    format_ = new DecimalFormat("#.##");
    DOUBLE_PATTERN = Pattern.compile(doubleRegex);
  }
  
  
	/**
	 * Checks if the specified string contains a valid double. See
	 * {@link Double#valueOf(String)} for the regular expression that is used.
	 * 
	 * @param string The string to check.
	 * @return <code>true</code> if and only if the specified string contains a
	 *         double in a valid format, false otherwise. Note:
	 *         <code>Infinity</code> and <code>NaN</code> are treated as valid
	 *         doubles!
	 */
  public static boolean isDouble(String string)
  {
  	return DOUBLE_PATTERN.matcher(string).matches();
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
	
	
	
	/**
	 * Calculates the dot (or scalar) product of two vectors/arrays.
	 * @throws IllegalArgumentException If the two arrays are not of the same length.
	 */
	public static double dotProduct(double[] vector1, double[] vector2)
	{
		if (vector1.length != vector2.length)
			throw new IllegalArgumentException("The vectors must have the same length!");
		
		double sum = 0;
		for (int i = 0; i < vector1.length; i++)
		{
			sum += (vector1[i] * vector2[i]);
		}
		
		return sum;
	}
}