package test.komposten.utilities.tools;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import komposten.utilities.tools.KeyMap;

public class KeyMapTest
{
	
	@Test(expected=FileNotFoundException.class)
	public void testFileNotFound() throws FileNotFoundException
	{
		new KeyMap("I DONT EXIST.NULL");
	}
	

	@Test
	public void testValue()
	{
		try
		{
			KeyMap map = new KeyMap("test files/keymap.txt");
			assertEquals("value1", map.value("key1"));
			assertEquals("value2", map.value("key2"));
			assertNull(map.value("key3"));
		}
		catch (FileNotFoundException e) { }
	}
	

	@Test
	public void testKey()
	{
		try
		{
			KeyMap map = new KeyMap("test files/keymap.txt");
			assertEquals("key1", map.key("value1"));
			assertEquals("key2", map.key("value2"));
			assertNull(map.key("value3"));
		}
		catch (FileNotFoundException e) { }
	}
}
