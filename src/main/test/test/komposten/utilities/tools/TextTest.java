package test.komposten.utilities.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import komposten.utilities.tools.Text;
import komposten.utilities.tools.Text.OperationType;

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
	public void testGetEditDistanceOperationSummaryIllegalState()
	{
		Text.editDistance("cat", "dog", false);
		Text.getEditDistanceOperationSummary();
	}
	
	
	@Test
	public void testGetEditDistanceOperationSummary()
	{
		//Test all types of changes.
		Text.editDistance("in", "ins", true);
		assertEquals(OperationType.Insertion, Text.getEditDistanceOperationSummary());
		Text.editDistance("del", "de", true);
		assertEquals(OperationType.Deletion, Text.getEditDistanceOperationSummary());
		Text.editDistance("sub", "sus", true);
		assertEquals(OperationType.Substitution, Text.getEditDistanceOperationSummary());
		Text.editDistance("indel", "insde", true);
		assertEquals(OperationType.InDel, Text.getEditDistanceOperationSummary());
		Text.editDistance("insub", "inssus", true);
		assertEquals(OperationType.InSub, Text.getEditDistanceOperationSummary());
		Text.editDistance("subdel", "susde", true);
		assertEquals(OperationType.SubDel, Text.getEditDistanceOperationSummary());
		Text.editDistance("none", "none", true);
		assertEquals(OperationType.None, Text.getEditDistanceOperationSummary());
		Text.editDistance("indelsub", "insdesus", true);
		assertEquals(OperationType.InDelSub, Text.getEditDistanceOperationSummary());
		
		//Test null and zero-length strings.
		Text.editDistance("", "ins", true);
		assertEquals(OperationType.Insertion, Text.getEditDistanceOperationSummary());
		Text.editDistance(null, "ins", true);
		assertEquals(OperationType.Insertion, Text.getEditDistanceOperationSummary());
		Text.editDistance("del", "", true);
		assertEquals(OperationType.Deletion, Text.getEditDistanceOperationSummary());
		Text.editDistance("del", null, true);
		assertEquals(OperationType.Deletion, Text.getEditDistanceOperationSummary());
		Text.editDistance("", "", true);
		assertEquals(OperationType.None, Text.getEditDistanceOperationSummary());
		Text.editDistance(null, null, true);
		assertEquals(OperationType.None, Text.getEditDistanceOperationSummary());
	}
}
