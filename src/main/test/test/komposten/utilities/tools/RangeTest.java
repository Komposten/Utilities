package test.komposten.utilities.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import komposten.utilities.data.Range;


public class RangeTest
{
	Range range;
	
	@Before
	public void setUp()
	{
		range = new Range(2, 9);
	}
	

	@Test
	public void testGetters()
	{
		assertEquals(2, range.getLower());
		assertEquals(9, range.getUpper());
	}


	@Test
	public void testSetter()
	{
		assertEquals(2, range.getLower());
		assertEquals(9, range.getUpper());
		
		range.setRange(-4, 3);

		assertEquals(-4, range.getLower());
		assertEquals(3, range.getUpper());
		
		range.setRange(3, -4);

		assertEquals(-4, range.getLower());
		assertEquals(3, range.getUpper());
	}
	
	
	@Test
	public void testContains()
	{
		assertTrue(range.contains(2, true));
		assertTrue(range.contains(4, true));
		assertTrue(range.contains(9, true));
		assertFalse(range.contains(1, true));
		assertFalse(range.contains(10, true));

		assertFalse(range.contains(2, false));
		assertTrue(range.contains(4, false));
		assertFalse(range.contains(9, false));
		assertFalse(range.contains(1, false));
		assertFalse(range.contains(10, false));
	}
}
