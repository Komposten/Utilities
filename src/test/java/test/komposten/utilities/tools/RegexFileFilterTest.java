package test.komposten.utilities.tools;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import komposten.utilities.tools.RegexFileFilter;


public class RegexFileFilterTest
{

	@Test
	public void testNoPatterns()
	{
		RegexFileFilter filter = new RegexFileFilter();

		assertTrue(filter.accept(new File("")));
		assertTrue(filter.accept(new File("ABC.txt")));
	}


	@Test
	public void testAcceptSinglePattern()
	{
		RegexFileFilter filter = new RegexFileFilter("[A-Za-z]{3,}_\\d{2}\\.txt");

		assertFalse(filter.accept(new File("A_01.txt")));
		assertFalse(filter.accept(new File("ABC_1.txt")));
		assertFalse(filter.accept(new File("ABC_01.exe")));
		assertTrue(filter.accept(new File("ABC_01.txt")));
	}


	@Test
	public void testAcceptMultiplePatterns()
	{
		RegexFileFilter filter = new RegexFileFilter("[A-Z]+.txt", "\\d+.dll");

		assertTrue(filter.accept(new File("ABC.txt")));
		assertTrue(filter.accept(new File("012.dll")));
		assertFalse(filter.accept(new File("ABC.dll")));
		assertFalse(filter.accept(new File("012.txt")));
	}


	@Test
	public void testSetAcceptedRegexes()
	{
		RegexFileFilter filter = new RegexFileFilter("[A-Z]+.txt", "\\d+.dll");

		assertTrue(filter.accept(new File("ABC.txt")));
		assertTrue(filter.accept(new File("012.dll")));

		filter.setAcceptedPatterns("[A-Z]+.dll", "\\d+.txt");

		assertFalse(filter.accept(new File("ABC.txt")));
		assertFalse(filter.accept(new File("012.dll")));

		assertTrue(filter.accept(new File("ABC.dll")));
		assertTrue(filter.accept(new File("012.txt")));
	}
}
