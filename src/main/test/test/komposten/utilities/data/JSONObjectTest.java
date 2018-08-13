package test.komposten.utilities.data;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import komposten.utilities.data.JSONObject;

public class JSONObjectTest
{
	JSONObject jsonObject;
	JSONObject jsonObject2;
	Object[] anArray;


	private void fillObject(JSONObject jsonObject)
	{
		jsonObject.addMember("aString", "string");
		jsonObject.addMember("aLong", 123l);
		jsonObject.addMember("aDouble", 123.456d);
		jsonObject.addMember("aBoolean", true);
		jsonObject.addMember("anObject", new JSONObject());
		jsonObject.addMember("anArray", anArray);
		jsonObject.addNullMember("aNull");
	}
	
	
	@Before
	public void setUp()
	{
		jsonObject = new JSONObject();
		jsonObject2 = new JSONObject();
		anArray = new Object[] {"string", 123l, 123.456d, true, new JSONObject(), new Object[] { 0 }, null};
	}
	

	@Test
	public void testGetMembers()
	{
		Map<String, Object> expected = new LinkedHashMap<>();
		assertEquals(expected.entrySet(), jsonObject.getMembers());
	}
	
	
	@Test
	public void testGetMemberByName()
	{
		jsonObject.addMember("test", "123");
		assertEquals("123", jsonObject.getMemberByName("test"));
	}
	
	
	@Test
	public void testAddMember()
	{
		fillObject(jsonObject);

		assertEquals("string", jsonObject.getMemberByName("aString"));
		assertEquals(123l, jsonObject.getMemberByName("aLong"));
		assertEquals(123.456d, jsonObject.getMemberByName("aDouble"));
		assertEquals(true, jsonObject.getMemberByName("aBoolean"));
		assertEquals(jsonObject2, jsonObject.getMemberByName("anObject"));
		assertEquals(anArray, jsonObject.getMemberByName("anArray"));
		assertNull(jsonObject.getMemberByName("aNull"));
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddItself()
	{
		jsonObject.addMember("itself", jsonObject);
	}
	
	
	@Test
	public void testRemoveMember()
	{
		jsonObject.addMember("member", "value");
		assertEquals("value", jsonObject.getMemberByName("member"));
		jsonObject.removeMember("member");
		assertNull(jsonObject.getMemberByName("member"));
	}
	
	
	@Test
	public void testHasMember()
	{
		assertFalse(jsonObject.hasMember("member"));
		jsonObject.addMember("member", "value");
		assertTrue(jsonObject.hasMember("member"));
		assertFalse(jsonObject.hasMember("member2"));
		jsonObject.removeMember("member");
		assertFalse(jsonObject.hasMember("member"));
	}
	
	
	@Test
	public void testIsMemberNull()
	{
		assertFalse(jsonObject.isMemberNull("member"));
		assertFalse(jsonObject.isMemberNull("member2"));
		jsonObject.addMember("member", "value");
		jsonObject.addNullMember("member2");
		assertFalse(jsonObject.isMemberNull("member"));
		assertTrue(jsonObject.isMemberNull("member2"));
	}
	
	
	@Test
	public void testToStringMinified()
	{
		fillObject(jsonObject);
		
		String expected = "{"
				+ "\"aString\":\"string\","
				+ "\"aLong\":123,"
				+ "\"aDouble\":123.456,"
				+ "\"aBoolean\":true,"
				+ "\"anObject\":{},"
				+ "\"anArray\":"
				+ "["
				+ "\"string\",123,123.456,true,{},[0],null"
				+ "],"
				+ "\"aNull\":null"
				+ "}";
		
		assertEquals(expected, jsonObject.toString(true));
	}
	
	
	@Test
	public void testToStringFormatted()
	{
		fillObject(jsonObject);
		
		String expected = "{\n"
				+ "  \"aString\":\"string\",\n"
				+ "  \"aLong\":123,\n"
				+ "  \"aDouble\":123.456,\n"
				+ "  \"aBoolean\":true,\n"
				+ "  \"anObject\":\n"
				+ "  {\n"
				+ "  },\n"
				+ "  \"anArray\":\n"
				+ "  [\n"
				+ "    \"string\",\n"
				+ "    123,\n"
				+ "    123.456,\n"
				+ "    true,\n"
				+ "    {\n"
				+ "    },\n"
				+ "    [\n"
				+ "      0\n"
				+ "    ],\n"
				+ "    null\n"
				+ "  ],\n"
				+ "  \"aNull\":null\n"
				+ "}";
		
		assertEquals(expected, jsonObject.toString(false));
	}
	
	
	@Test
	public void testHashCode()
	{
		assertEquals(jsonObject.hashCode(), jsonObject2.hashCode());
		fillObject(jsonObject);
		assertNotEquals(jsonObject.hashCode(), jsonObject2.hashCode());
		fillObject(jsonObject2);
		assertEquals(jsonObject.hashCode(), jsonObject2.hashCode());
	}
	
	
	@Test
	public void testEquals()
	{
		assertEquals(jsonObject, jsonObject);
		assertNotEquals(jsonObject, null);
		assertNotEquals(jsonObject, "NotAnObject");
		
		assertEquals(jsonObject, jsonObject2);
		fillObject(jsonObject);
		assertNotEquals(jsonObject, jsonObject2);
		fillObject(jsonObject2);
		assertEquals(jsonObject, jsonObject2);
		
		jsonObject.addMember("NewMember", 5);
		jsonObject2.addMember("OtherNewMember", 5);
		assertNotEquals(jsonObject, jsonObject2);
		
		jsonObject = new JSONObject();
		jsonObject2 = new JSONObject();
		jsonObject.addMember("Member", 5);
		jsonObject2.addNullMember("Member");
		assertNotEquals(jsonObject, jsonObject2);
		assertNotEquals(jsonObject2, jsonObject);

		jsonObject.addMember("Member", 5);
		jsonObject2.addMember("Member", "5");
		assertNotEquals(jsonObject, jsonObject2);
		assertNotEquals(jsonObject2, jsonObject);

		jsonObject.addMember("Member", 5);
		jsonObject2.addMember("Member", 4);
		assertNotEquals(jsonObject, jsonObject2);
		assertNotEquals(jsonObject2, jsonObject);
		
		//Test equals with arrays containing primitives.
		Object[] array1 = new Object[] {"string", 123l, 123.456d};
		Object[] array2 = Arrays.copyOf(array1, array1.length);
		Object[] array3 = Arrays.copyOf(array1, array1.length);
		Object[] array4;
		array3[1] = 124l;
		
		jsonObject.addMember("Member", array1);
		jsonObject2.addMember("Member", array2);
		assertEquals(jsonObject, jsonObject2);
		jsonObject2.addMember("Member", array3);
		assertNotEquals(jsonObject, jsonObject2);

		//Test equals with arrays containing JSONObjects
		array1 = new Object[] { new JSONObject() };
		array2 = new Object[] { new JSONObject() };
		array3 = new Object[] { new JSONObject() };
		array4 = new Object[] { new JSONObject(), new JSONObject() };
		((JSONObject)array3[0]).addMember("Member", 5);
		
		jsonObject.addMember("Member", array1);
		jsonObject2.addMember("Member", array2);
		assertEquals(jsonObject, jsonObject2);
		jsonObject2.addMember("Member", array3);
		assertNotEquals(jsonObject, jsonObject2);
		jsonObject2.addMember("Member", array4);
		assertNotEquals(jsonObject, jsonObject2);
		
		//Test equals with nested arrays
		array1 = new Object[] { new Object[] { 1, 2, new JSONObject() } };
		array2 = new Object[] { new Object[] { 1, 2, new JSONObject() } };
		array3 = new Object[] { new Object[] { 1, 3, new JSONObject() } };
		
		jsonObject.addMember("Member", array1);
		jsonObject2.addMember("Member", array2);
		assertEquals(jsonObject, jsonObject2);
		jsonObject2.addMember("Member", array3);
		assertNotEquals(jsonObject, jsonObject2);
	}
}
