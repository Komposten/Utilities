/*
 * Copyright (c) 2014 Jakob Hjelm 
 */
package komposten.utilities.tools;

import java.text.DecimalFormat;

/**
 * @version
 * <b>1.3.0</b><br />
 * <ul>
 * <li>Added isInInterval(int, int, int, boolean).</li>
 * <li>Added isInInterval(float, float, float, boolean).</li>
 * </ul>
 * <b>Older</b><br />
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
   * Calculates the distance between two coordinates.
   * @param x1 The first coordinate.
   * @param y1 The first coordinate.
   * @param x2 The second coordinate.
   * @param y2 The second coordinate.
   * @return The distance between (x1, y1) and (x2, y2).
   */
  public static float distance(float x1, float y1, float x2, float y2)
  {
    float dX = x2 - x1;
    float dY = y2 - y1;
    
    return (float)Math.sqrt(dX*dX+dY*dY);
  }
  
  
  
  /**
   * Calculates the angle between the line through (x1, y1) and (x2, y2) and the x-axis.<br />
   * @param x1 The first coordinate.
   * @param y1 The first coordinate.
   * @param x2 The second coordinate.
   * @param y2 The second coordinate.
   * @return The angle between the line through the two points and the x-axis in radians.
   */
  public static float angle(float x1, float y1, float x2, float y2)
  {
    float dX = x2 - x1;
    float dY = y2 - y1;
    
    float angle = (float) Math.atan2(dY, dX);
//    float angle = (float) Math.atan(dY/dX);
    
//    if (x2 < x1 && y2 > y1)
//      angle =  (float) (Math.PI + angle);
//    else if (x2 < x1 && y2 < y1)
//      angle += Math.PI;
//    else if (x2 > x1 && y2 < y1)
//      angle =  (float) (Math.PI * 2 + angle);
    
    return angle;
  }
  
  
  
  /**
   * Returns whether <code>value</code> is a power of two or not.
   * @param value A value
   * @return True if <code>value</code> is a power of two, false otherwise.
   */
  public static boolean isPOT(int value)
  {
    return Integer.toBinaryString(value).matches("0*10*");
  }
  
  
  
  /**
   * Returns whether <code>value</code> is a power of two or not.
   * @param value A value
   * @return True if <code>value</code> is a power of two, false otherwise.
   */
  public static boolean isPOT(long value)
  {
    return Long.toBinaryString(value).matches("0*10*");
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