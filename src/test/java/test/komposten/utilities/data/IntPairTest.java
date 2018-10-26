package test.komposten.utilities.data;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import komposten.utilities.data.IntPair;

public class IntPairTest
{
	private IntPair pair;
	
	@Before
	public void setup()
	{
		pair = new IntPair(5, 10);
	}
	

	@Test
	public void testGetters()
	{
		assertEquals(5, pair.getFirst());
		assertEquals(10, pair.getSecond());
	}
	

	@Test
	public void testSetters()
	{
		pair.setFirst(9);
		pair.setSecond(3);
		
		assertEquals(9, pair.getFirst());
		assertEquals(3, pair.getSecond());
	}

	
	@Test
	public void testEquals()
	{
		IntPair pair2 = new IntPair(5, 10);
		IntPair pair3 = new IntPair(10, 5);
		IntPair pair4 = new IntPair(5, 11);
		
		assertEquals(pair2, pair);
		assertNotEquals(pair3, pair);
		assertNotEquals(pair4, pair);
		
		assertFalse(pair.equals(null));
		assertFalse(pair.equals("string"));
	}
}
