package komposten.utilities.tools;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import komposten.utilities.tools.ExtensionFileFilter;

public class ExtensionFileFilterTest
{

	@Test
	public void testAcceptOneExtension()
	{
		ExtensionFileFilter filter = new ExtensionFileFilter(false, ".txt");
		
		assertTrue(filter.accept(new File("folder/file.txt")));
		assertFalse(filter.accept(new File("folder/file.csv")));
	}
	
	
	@Test
	public void testAcceptMultipleExtensions()
	{
		ExtensionFileFilter filter = new ExtensionFileFilter(false, ".txt", ".csv", ".exe");
		
		assertTrue(filter.accept(new File("folder/file.txt")));
		assertTrue(filter.accept(new File("folder/file.csv")));
		assertTrue(filter.accept(new File("folder/file.exe")));
		assertFalse(filter.accept(new File("folder/file.ttf")));
	}
	
	
	@Test
	public void testAcceptNoPeriod()
	{
		ExtensionFileFilter filter = new ExtensionFileFilter(false, "txt");
		
		assertTrue(filter.accept(new File("folder/file.txt")));
	}
	
	
	@Test
	public void testAcceptAll()
	{
		ExtensionFileFilter filter = new ExtensionFileFilter();
		
		assertTrue(filter.accept(new File("folder/file.txt")));
	}
	
	
	@Test
	public void testAcceptFileWithoutExtensions()
	{
		ExtensionFileFilter filter = new ExtensionFileFilter(false, ".txt");
		
		File file = new File("folder/file");
		assertFalse(filter.accept(file));
		
		filter.setAcceptedExtensions("");
		assertTrue(filter.accept(file));
	}
	
	
	@Test
	public void testSetAcceptedExtensions()
	{
		ExtensionFileFilter filter = new ExtensionFileFilter(false, ".txt", ".csv");
		
		filter.setAcceptedExtensions(".exe", ".ttf");
		assertFalse(filter.accept(new File("folder/file.txt")));
		assertFalse(filter.accept(new File("folder/file.csv")));
		assertTrue(filter.accept(new File("folder/file.exe")));
		assertTrue(filter.accept(new File("folder/file.ttf")));
	}
	
	
	@Test
	public void testAcceptFolders()
	{
		ExtensionFileFilter filter = new ExtensionFileFilter(true, ".txt");
		
		assertTrue(filter.accept(new File("/")));
	}
	
	
	@Test
	public void testSetAcceptFolders()
	{
		ExtensionFileFilter filter = new ExtensionFileFilter(true, ".txt");
		
		filter.setAcceptFolders(false);
		assertFalse(filter.accept(new File("/")));
	}
}
