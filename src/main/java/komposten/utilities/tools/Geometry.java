/*
 * Copyright (c) 2014 Jakob Hjelm 
 */
package komposten.utilities.tools;

import com.badlogic.gdx.math.Polygon;

import komposten.utilities.data.ObjectPair;


/**
 * This class holds methods for different geometrical operations.
 * <br />
 * <br />
 * <b>Note:</b> Some methods on this class requires LibGDX.
 * @version
 * <b>1.1.0</b> <br />
 * <ul>
 * <li>Added <code>clampVector(float, float, float, float, float, float, float, float)</code>.
 * </ul>
 * <b>Older</b> <br />
 * 1.0.0 <br />
 * <ul>
 * <li>Added <code>createCircle(float, int)</code>.
 * <li>Added <code>intersects(Polygon, Polygon)</code>.
 * </ul>
 * @author Jakob Hjelm
 */
public class Geometry
{
	private static ObjectPair<Float, Float> vector = new ObjectPair<Float, Float>(0f, 0f);


	private Geometry() {}


	/**
	 * Creates an array with the points of a circular polygon.
	 * @param radius The radius of the circle.
	 * @param segments The amount of segments.
	 * @return An array containing the points for a circular polygon.
	 */
	public static float[] createCircle(float radius, int segments)
	{
		float[] polygon = new float[segments*2];
		float   angle   = (float) (2*Math.PI / segments);

		for (int i = 0; i < segments; i++)
		{
			polygon[i*2  ] = (float) (radius*Math.cos(angle*i));
			polygon[i*2+1] = (float) (radius*Math.sin(angle*i));
		}

		return polygon;
	}



	/**
	 * Checks if the two LibGDX polygons intersect.
	 * @param poly1 The first polygon.
	 * @param poly2 The second polygon.
	 * @return True if the two polygons intersect, false otherwise.
	 */
	public static boolean intersects(Polygon poly1, Polygon poly2)
	{
		boolean intersection = false;

		float[] points1 = poly1.getTransformedVertices();
		float[] points2 = poly2.getTransformedVertices();

		for (int i = 0; i < points1.length; i+=2)
			if (poly2.contains(points1[i], points1[i+1]))
			{
				intersection = true;
				break;
			}
		for (int i = 0; i < points2.length; i+=2)
			if (poly1.contains(points2[i], points2[i+1]))
			{
				intersection = true;
				break;
			}

		return intersection;
	}


	/**
	 * Clamps a vector inside the specified rectangle by shortening it if
	 * necessary.<br />
	 * <b>Note</b>: Only the end of the vector (<code>x2; y2</code>) is clamped.
	 * 
	 * @param x1 The vector's origin point.
	 * @param y1 The vector's origin point.
	 * @param x2 The vector's end-point.
	 * @param y2 The vector's end-point.
	 * @param minX The minimum allowed X-value.
	 * @param minY The minimum allowed Y-value.
	 * @param maxX The maximum allowed X-value.
	 * @param maxY The maximum allowed Y-value.
	 * @return An {@link ObjectPair ObjectPair<Float, Float>} containing the
	 *         clamped vector's end-point. The <code>ObjectPair</code> is cached
	 *         by <code>Geometry</code> and will ge reused for future calls to
	 *         this method.
	 * @throws IllegalArgumentException If <code>minX >= maxX</code>,
	 *           <code>minY >= maxY</code>, or if <code>(x1, y1)</code> is outside
	 *           the rectangle.
	 */
	public static ObjectPair<Float, Float> clampVector(float x1, float y1,
			float x2, float y2, float minX, float minY, float maxX, float maxY) throws IllegalArgumentException
	{
		if (minX >= maxX)
			throw new IllegalArgumentException("minX must be < maxX (" + minX + " >= " + maxX + ")");
		if (minY >= maxY)
			throw new IllegalArgumentException("minY must be < maxY (" + minY + " >= " + maxY + ")");
		if (!MathOps.isInInterval(x1, minX, maxX, true) ||
				!MathOps.isInInterval(y1, minY, maxY, true))
		{
			String xError = "";
			String yError = "";
			if (x1 < minX) xError = x1 + " < " + minX;
			else if (x1 > maxX) xError = x1 + " > " + maxX;
			if (y1 < minY) yError = y1 + " < " + minY;
			else if (y1 > maxY) yError = y1 + " > " + maxY;

			String msg = "The vector origin (x1, y1) must fall within the rectangle (";

			if (!xError.isEmpty() && !yError.isEmpty())
				msg += xError + " and " + yError;
			else if (!xError.isEmpty())
				msg += xError;
			else
				msg += yError;

			msg += ")";

			throw new IllegalArgumentException(msg);
		}

		float dX = x2 - x1;
		float dY = y2 - y1;
		float ratio = dX/dY;

		if (MathOps.equals(dX, 0, 0.00001f))
		{
			vector.set(x2, MathOps.clamp(minY, maxY, y2));
		}
		else if (MathOps.equals(dY, 0, 0.00001f))
		{
			vector.set(MathOps.clamp(minX, maxX, x2), y2);
		}
		else
		{
			float xOverflow = (x2 > maxX ? x2-maxX : (x2 < minX ? minX-x2 : 0));
			float yOverflow = (y2 > maxY ? y2-maxY : (y2 < minY ? minY-y2 : 0));
			float x = 0;
			float y = 0;
			float newDX = 0;
			float newDY = 0;

			if (xOverflow == yOverflow && yOverflow == 0)
			{
				x = x2;
				y = y2;
			}
			else if (xOverflow > yOverflow)
			{
				if (x2 > maxX)
				{
					x = maxX;
					newDX = dX - xOverflow;
				}
				else if (x2 < minX)
				{
					x = minX;
					newDX = dX + xOverflow;
				}
				
				newDY = newDX/ratio;
				y = y1 + newDY;
			}
			else
			{
				if (y2 > maxY)
				{
					y = maxY;
					newDY = dY - yOverflow;
				}
				else if (y2 < minY)
				{
					y = minY;
					newDY = dY + yOverflow;
				}
				
				newDX = newDY*ratio;
				x = x1 + newDX;
			}
			
			vector.set(x, y);
		}

		return vector;
	}
}
