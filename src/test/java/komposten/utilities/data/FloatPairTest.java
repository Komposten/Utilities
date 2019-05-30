package komposten.utilities.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import komposten.utilities.data.FloatPair;

public class FloatPairTest
{
	private FloatPair pair;
	
	@Before
	public void setup()
	{
		pair = new FloatPair(5.3f, 10.1f);
	}
	

	@Test
	public void testGetters()
	{
		assertEquals(5.3f, pair.getFirst(), 0.001f);
		assertEquals(10.1f, pair.getSecond(), 0.001f);
		assertEquals(5.3f, pair.getX(), 0.001f);
		assertEquals(10.1f, pair.getY(), 0.001f);
	}
	

	@Test
	public void testSetters()
	{
		pair.setFirst(9.8f);
		pair.setSecond(3.1f);
		
		assertEquals(9.8f, pair.getFirst(), 0.001f);
		assertEquals(3.1f, pair.getSecond(), 0.001f);
		
		pair.setX(5.9f);
		pair.setY(2.6f);
		
		assertEquals(5.9f, pair.getFirst(), 0.001f);
		assertEquals(2.6f, pair.getSecond(), 0.001f);
	}

	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEquals()
	{
		FloatPair pair2 = new FloatPair(5.3f, 10.1f);
		FloatPair pair3 = new FloatPair(10.1f, 5.3f);
		FloatPair pair4 = new FloatPair(5.3f, 11.1f);
		
		assertEquals(pair, pair);
		assertEquals(pair2, pair);
		assertNotEquals(pair3, pair);
		assertNotEquals(pair4, pair);
		
		assertFalse(pair.equals(null));
		assertFalse(pair.equals("string"));
	}
}
