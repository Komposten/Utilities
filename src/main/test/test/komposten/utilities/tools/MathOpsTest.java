package test.komposten.utilities.tools;

import static org.junit.Assert.*;

import org.junit.Test;

import komposten.utilities.tools.MathOps;

public class MathOpsTest
{

	@Test
	public void testRound()
	{
		assertEquals(MathOps.round(9.49d), 9L);
		assertEquals(MathOps.round(9.50d), 10L);
		assertEquals(MathOps.round(9.49f), 9L);
		assertEquals(MathOps.round(9.50f), 10L);

		assertEquals(MathOps.round(9.6789d, 4).replace(',', '.'), "9.6789");
		assertEquals(MathOps.round(9.6789d, 3).replace(',', '.'), "9.679");
		assertEquals(MathOps.round(9.6789d, 2).replace(',', '.'), "9.68");
		assertEquals(MathOps.round(9.6789d, 1).replace(',', '.'), "9.7");
		assertEquals(MathOps.round(9.6789d, 0).replace(',', '.'), "10");
	}
	
	
	@Test
	public void testEquals()
	{
		//Doubles
		assertTrue(MathOps.equals(1, 1.1, 0.2));
		assertFalse(MathOps.equals(1, 1.1, 0.05));

		assertTrue(MathOps.equals(-0.01, 0.09, 0.2));
		assertFalse(MathOps.equals(-0.01, 0.09, 0.05));

		assertTrue(MathOps.equals(-1, -1.1, 0.2));
		assertFalse(MathOps.equals(-1, -1.1, 0.05));

		//Floats
		assertTrue(MathOps.equals(1f, 1.1f, 0.2f));
		assertFalse(MathOps.equals(1f, 1.1f, 0.05f));

		assertTrue(MathOps.equals(-0.01f, 0.09f, 0.2f));
		assertFalse(MathOps.equals(-0.01f, 0.09f, 0.05f));

		assertTrue(MathOps.equals(-1f, -1.1f, 0.2f));
		assertFalse(MathOps.equals(-1f, -1.1f, 0.05f));
	}
	
	
	@Test
	public void testClamp()
	{
		assertEquals(1f, MathOps.clamp(1f, 2f, 0f), 0.001f);
		assertEquals(1.5f, MathOps.clamp(1f, 2f, 1.5f), 0.001f);
		assertEquals(2f, MathOps.clamp(1f, 2f, 3f), 0.001f);

		assertEquals(1d, MathOps.clamp(1d, 2d, 0d), 0.001d);
		assertEquals(1.5d, MathOps.clamp(1d, 2d, 1.5d), 0.001d);
		assertEquals(2d, MathOps.clamp(1d, 2d, 3d), 0.001d);
	}
	
	
	@Test
	public void testClamp01()
	{
		assertEquals(0f, MathOps.clamp01(-1f), 0.001f);
		assertEquals(0.5f, MathOps.clamp01(0.5f), 0.001f);
		assertEquals(1f, MathOps.clamp01(1.5f), 0.001f);

		assertEquals(0d, MathOps.clamp01(-1d), 0.001d);
		assertEquals(0.5d, MathOps.clamp01(0.5d), 0.001d);
		assertEquals(1d, MathOps.clamp01(1.5d), 0.001d);
	}
	
	
	@Test
	public void testDistance()
	{
		assertEquals(5f, MathOps.distance(2, 4, 5, 8), 0.001f);
		assertEquals(5f, MathOps.distance(4, 2, 8, 5), 0.001f);
	}
	
	
	@Test
	public void testDistanceSqr()
	{
		assertEquals(25f, MathOps.distanceSqr(2, 4, 5, 8), 0.001f);
		assertEquals(25f, MathOps.distanceSqr(4, 2, 8, 5), 0.001f);
	}
	
	
	@Test
	public void testAngle()
	{
		assertEquals( 1*Math.PI/4, MathOps.angle(0, 0, 1, 1), 0.001f);
		assertEquals( 3*Math.PI/4, MathOps.angle(0, 0, -1, 1), 0.001f);
		assertEquals(-3*Math.PI/4, MathOps.angle(0, 0, -1, -1), 0.001f);
		assertEquals(-1*Math.PI/4, MathOps.angle(0, 0, 1, -1), 0.001f);
	}
	
	
	@Test
	public void testIsPOT()
	{
		
		assertIsPOT("1000");
		assertIsPOT("0100");
		assertIsPOT("0010");
		assertIsPOT("0001");

		assertIsNotPOT("0000");
		assertIsNotPOT("1100");
		assertIsNotPOT("0110");
		assertIsNotPOT("0011");
		assertIsNotPOT("1001");
		assertIsNotPOT("1010");
		assertIsNotPOT("0101");
		assertIsNotPOT("1110");
		assertIsNotPOT("1101");
		assertIsNotPOT("1011");
		assertIsNotPOT("0111");
		assertIsNotPOT("1111");
	}

	private void assertIsPOT(String bitSequence)
	{
		assertTrue(MathOps.isPOT(Integer.parseInt(bitSequence, 2)));
		assertTrue(MathOps.isPOT(Long.parseLong(bitSequence, 2)));
	}

	private void assertIsNotPOT(String bitSequence)
	{
		assertFalse(MathOps.isPOT(Integer.parseInt(bitSequence, 2)));
		assertFalse(MathOps.isPOT(Long.parseLong(bitSequence, 2)));
	}
	
	
	@Test
	public void testIsInInterval()
	{
		//int
		assertTrue(MathOps.isInInterval(-3, -3, 4, true));
		assertTrue(MathOps.isInInterval( 0, -3, 4, true));
		assertTrue(MathOps.isInInterval( 4, -3, 4, true));
		assertFalse(MathOps.isInInterval(-4, -3, 4, true));
		assertFalse(MathOps.isInInterval(5, -3, 4, true));
		
		assertFalse(MathOps.isInInterval(-3, -3, 4, false));
		assertTrue(MathOps.isInInterval( 0, -3, 4, false));
		assertFalse(MathOps.isInInterval( 4, -3, 4, false));
		assertFalse(MathOps.isInInterval(-4, -3, 4, false));
		assertFalse(MathOps.isInInterval(5, -3, 4, false));
		
		//float
		assertTrue(MathOps.isInInterval(-3f, -3f, 4f, true));
		assertTrue(MathOps.isInInterval( 0f, -3f, 4f, true));
		assertTrue(MathOps.isInInterval( 4f, -3f, 4f, true));
		assertFalse(MathOps.isInInterval(-4f, -3f, 4f, true));
		assertFalse(MathOps.isInInterval(5f, -3f, 4f, true));
		
		assertFalse(MathOps.isInInterval(-3f, -3f, 4f, false));
		assertTrue(MathOps.isInInterval( 0f, -3f, 4f, false));
		assertFalse(MathOps.isInInterval( 4f, -3f, 4f, false));
		assertFalse(MathOps.isInInterval(-4f, -3f, 4f, false));
		assertFalse(MathOps.isInInterval(5f, -3f, 4f, false));
	}
}
