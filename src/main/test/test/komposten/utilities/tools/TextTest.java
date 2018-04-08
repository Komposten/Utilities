package test.komposten.utilities.tools;

import static org.junit.Assert.*;

import org.junit.Test;

import komposten.utilities.tools.Text;
import komposten.utilities.tools.Text.Change;

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
	
	
	@Test(expected = IllegalStateException.class)
	public void testGetEditDistanceMatrixIllegalState()
	{
		Text.editDistance("cat", "dog", false);
		Text.getEditDistanceMatrix();
	}
	
	
	@Test(expected = IllegalStateException.class)
	public void testGetEditDistanceChangeTypeIllegalState()
	{
		Text.editDistance("cat", "dog", false);
		Text.getEditDistanceChangeType();
	}
	
	
	@Test
	public void testGetEditDistanceChangeType()
	{
		//Test all types of changes.
		Text.editDistance("in", "ins", true);
		assertEquals(Change.Insertion, Text.getEditDistanceChangeType());
		Text.editDistance("del", "de", true);
		assertEquals(Change.Deletion, Text.getEditDistanceChangeType());
		Text.editDistance("sub", "sus", true);
		assertEquals(Change.Substitution, Text.getEditDistanceChangeType());
		Text.editDistance("indel", "insde", true);
		assertEquals(Change.InDel, Text.getEditDistanceChangeType());
		Text.editDistance("insub", "inssus", true);
		assertEquals(Change.InSub, Text.getEditDistanceChangeType());
		Text.editDistance("subdel", "susde", true);
		assertEquals(Change.SubDel, Text.getEditDistanceChangeType());
		Text.editDistance("none", "none", true);
		assertEquals(Change.None, Text.getEditDistanceChangeType());
		Text.editDistance("indelsub", "insdesus", true);
		assertEquals(Change.InDelSub, Text.getEditDistanceChangeType());
		
		//Test null and zero-length strings.
		Text.editDistance("", "ins", true);
		assertEquals(Change.Insertion, Text.getEditDistanceChangeType());
		Text.editDistance(null, "ins", true);
		assertEquals(Change.Insertion, Text.getEditDistanceChangeType());
		Text.editDistance("del", "", true);
		assertEquals(Change.Deletion, Text.getEditDistanceChangeType());
		Text.editDistance("del", null, true);
		assertEquals(Change.Deletion, Text.getEditDistanceChangeType());
		Text.editDistance("", "", true);
		assertEquals(Change.None, Text.getEditDistanceChangeType());
		Text.editDistance(null, null, true);
		assertEquals(Change.None, Text.getEditDistanceChangeType());
	}
}
