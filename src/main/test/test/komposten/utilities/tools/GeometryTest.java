package test.komposten.utilities.tools;

import static org.junit.Assert.*;

import org.junit.Test;

import komposten.utilities.data.FloatPair;
import komposten.utilities.tools.Geometry;

public class GeometryTest
{

	@Test
	public void testClampVector()
	{
		FloatPair expected = new FloatPair(0f, 0f);
		FloatPair actual = null;
		
		//Test no clamp needed
		expected.set(5f, 5f);
		actual = Geometry.clampVector(0, 0, 5, 5, 0, 0, 10, 10);
		assertEquals(expected, actual);
		
		expected.set(-5f, -5f);
		actual = Geometry.clampVector(0, 0, -5, -5, -10, -10, 0, 0);
		assertEquals(expected, actual);

		//Test clamp needed
		for (int y = -10; y <= 10; y+=20)
		{
			for (int x = -10; x <= 10; x++)
			{
				expected.set(x/2f, y/2f);
				actual = Geometry.clampVector(0, 0, x, y, -5, -5, 5, 5);
				assertEquals(expected, actual);
			}
		}
		
		for (int x = -10; x <= 10; x+=20)
		{
			for (int y = -10; y <= 10; y++)
			{
				expected.set(x/2f, y/2f);
				actual = Geometry.clampVector(0, 0, x, y, -5, -5, 5, 5);
				assertEquals(expected, actual);
			}
		}
	}
	

	@Test(expected = IllegalArgumentException.class)
	public void testClampVectorTooHighMinX()
	{
		Geometry.clampVector(0, 0, 1, 1, 2, 0, 1, 1);
	}
	

	@Test(expected = IllegalArgumentException.class)
	public void testClampVectorTooHighMinY()
	{
		Geometry.clampVector(0, 0, 1, 1, 0, 2, 1, 1);
	}
	

	@Test(expected = IllegalArgumentException.class)
	public void testClampVectorOriginOutside()
	{
		Geometry.clampVector(-1, 0, 1, 1, 0, 0, 1, 1);
	}
}
