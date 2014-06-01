/*
 * Copyright (c) 2014 Jakob Hjelm 
 */
package komposten.utilities.tools;

import com.badlogic.gdx.math.Polygon;


/**
 * @version
 * <b>1.0.0</b> <br />
 * <ul>
 * <li>Added <code>createCircle(radius, segments)</code>.
 * <li>Added <code>intersects(Polygon, Polygon)</code>.
 * </ul>
 * <b>Older</b> <br />
 * @author Jakob Hjelm
 */
public class Geometry
{

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
   * Checks if the two polygons intersect.
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
}
