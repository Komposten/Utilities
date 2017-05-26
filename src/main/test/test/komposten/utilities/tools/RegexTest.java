package test.komposten.utilities.tools;

import static org.junit.Assert.assertEquals;

import java.util.regex.Matcher;

import org.junit.Test;

import komposten.utilities.tools.Regex;

public class RegexTest
{
	
	@Test
	public void testGetMatcher()
	{
		Matcher matcher = Regex.getMatcher("\\d+", "A123B44C5D123E9999");
		
		assertEquals("\\d+", matcher.pattern().toString());
		assertEquals(18, matcher.regionEnd());
	}

	@Test
	public void testGetMatches()
	{
		//Test match single occurrence.
		String[] matches = Regex.getMatches("\\d+", "A123B");
		
		assertEquals(1, matches.length);
		assertEquals("123", matches[0]);
		
		
		//Test match multiple occurrences.
		matches = Regex.getMatches("\\d+", "A123B44C5D123E9999");
		
		assertEquals("123", matches[0]);
		assertEquals("44", matches[1]);
		assertEquals("5", matches[2]);
		assertEquals("123", matches[3]);
		assertEquals("9999", matches[4]);
	}
	
	
	@Test
	public void testGetMatchCount()
	{
		assertEquals(1, Regex.getMatchCount("\\d+", "A123B"));
		assertEquals(5, Regex.getMatchCount("\\d+", "A123B44C5D123E9999"));
	}
}
