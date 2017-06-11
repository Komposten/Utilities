package test.komposten.utilities.tools;

import static org.junit.Assert.*;

import org.junit.Test;

import komposten.utilities.tools.Text;

public class TextTest
{

	@Test
	public void testEditDistance()
	{
		//Test single-char strings.
		assertEquals(0, Text.editDistance("A", "A"));
		assertEquals(1, Text.editDistance("A", "a"));

		//Test multi-char strings.
		assertEquals(0, Text.editDistance("ABC", "ABC"));
		assertEquals(3, Text.editDistance("ABC", "abc"));
		
		assertEquals(3, Text.editDistance("ABC", "ABC123"));
		assertEquals(3, Text.editDistance("ABC", "123ABC"));
		assertEquals(3, Text.editDistance("ABC", "1A2B3C"));
		
		assertEquals(1, Text.editDistance("ABC", "ABD"));
		assertEquals(2, Text.editDistance("ABC", "ACB"));
		
		//Test null or zero-length strings.
		assertEquals(3, Text.editDistance("", "ABC"));
		assertEquals(3, Text.editDistance("ABC", ""));
		assertEquals(0, Text.editDistance("", ""));
		
		assertEquals(3, Text.editDistance("ABC", null));
		assertEquals(3, Text.editDistance(null, "ABC"));
		assertEquals(0, Text.editDistance(null, null));

		assertEquals(0, Text.editDistance("", null));
		assertEquals(0, Text.editDistance(null, ""));
	}
}
