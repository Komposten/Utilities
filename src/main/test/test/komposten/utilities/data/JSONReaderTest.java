package test.komposten.utilities.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import komposten.utilities.data.JSONObject;
import komposten.utilities.data.JSONReader;

public class JSONReaderTest
{
	private JSONObject expected;
	
	@Before
	public void createExpected()
	{
		expected = new JSONObject();
		expected.addMember("aString", "string");
		expected.addMember("aLong", 123l);
		expected.addMember("aDouble", 123.456d);
		expected.addMember("aBoolean", true);
		expected.addMember("anObject", new JSONObject());
		expected.addMember("anArray", new Object[] {"string", 123l, 123.456d, true, new JSONObject(), new Object[] {0}, null});
		expected.addNullMember("aNull");
	}
	
	
	@Test
	public void testReadMinifiedFile()
	{
		JSONReader reader = new JSONReader();
		JSONObject actual = reader.readFile("test files/json/jsonReaderTestMinified.json");
		
		assertNotNull("The file could not be read or did not exist!", actual);
		assertEquals(expected.toString(false), actual.toString(false));
	}
	
	
	@Test
	public void testReadFormattedFile()
	{
		JSONReader reader = new JSONReader();
		JSONObject actual = reader.readFile("test files/json/jsonReaderTestMinified.json");
		
		assertNotNull("The file could not be read or did not exist!", actual);
		assertEquals(expected.toString(false), actual.toString(false));
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testReadBrokenFile()
	{
		JSONReader reader = new JSONReader();
		reader.readFile("test files/json/jsonReaderTestBroken.json");
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testReadFileWithInvalidValue()
	{
		JSONReader reader = new JSONReader();
		reader.readFile("test files/json/jsonReaderTestInvalidValue.json");
	}
}
