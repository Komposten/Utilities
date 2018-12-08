package test.komposten.utilities.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import komposten.utilities.data.ObjectPair;

public class ObjectPairTest
{
	private ObjectPair<String, Integer> pair;
	
	@Before
	public void setup()
	{
		pair = new ObjectPair<>("5", 10);
	}
	

	@Test
	public void testGetters()
	{
		assertEquals("5", pair.getFirst());
		assertEquals(new Integer(10), pair.getSecond());
	}
	

	@Test
	public void testSetters()
	{
		pair.setFirst("9");
		pair.setSecond(3);
		
		assertEquals("9", pair.getFirst());
		assertEquals(new Integer(3), pair.getSecond());
	}

	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEquals()
	{
		ObjectPair<String, Integer> pair2 = new ObjectPair<>("5", 10);
		ObjectPair<String, Integer> pair3 = new ObjectPair<>("10", 5);
		ObjectPair<Integer, String> pair4 = new ObjectPair<>(10, "5");
		ObjectPair<String, Integer> pair5 = new ObjectPair<>("5", 11);
		
		ObjectPair<String, String> pair6 = new ObjectPair<>(null, null);
		ObjectPair<String, String> pair7 = new ObjectPair<>(null, null);
		ObjectPair<String, String> pair8 = new ObjectPair<>("String", null);
		ObjectPair<String, String> pair9 = new ObjectPair<>(null, "String");

		assertEquals(pair, pair);
		assertEquals(pair2, pair);
		assertNotEquals(pair3, pair);
		assertNotEquals(pair4, pair);
		assertNotEquals(pair5, pair);
		
		assertEquals(pair6, pair7);
		assertNotEquals(pair6, pair8);
		assertNotEquals(pair6, pair9);
		
		assertFalse(pair.equals(null));
		assertFalse(pair.equals("string"));
	}
}
