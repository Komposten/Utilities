package test.komposten.utilities.tools;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import komposten.utilities.tools.Graph;

public class GraphTest
{

	@Test
	public void testFindElementaryCircuits()
	{
		/* The following graph is based on Fig. 1 in Johnson's paper (https://doi.org/10.1137/0204007)
		 * and represents a worst-case scenario for Tarjan's algorithm.
		 */
		int k = 10;
		int[][] adjacencyLists = new int[3*k+3][];
		
		adjacencyLists[0] = new int[k];
		
		for (int i = 0; i < k; i++)
		{
			adjacencyLists[0][i] = i+1;
			adjacencyLists[i+1] = new int[] { k+1 };
		}
		
		adjacencyLists[k+1] = new int[] { k+2, 2*k+1 };
		
		for (int i = k+2; i < 2*k; i++)
		{
			adjacencyLists[i] = new int[] { i+1, 2*k+1 };
		}
		
		adjacencyLists[2*k] = new int[] { 0, 2*k+1 };
		adjacencyLists[2*k+1] = new int[k];
		
		adjacencyLists[2*k+1][0] = 2*k+2;
		adjacencyLists[2*k+2] = new int[] { k+1, 3*k+2 };
		for (int i = 2*k+3, j = 1; i <= 3*k+1; i++, j++)
		{
			adjacencyLists[2*k+1][j] = i;
			adjacencyLists[i] = new int[] { 3*k+2 };
		}
		
		adjacencyLists[3*k+2] = new int[] { 2*k+1 };
		
		int[][] circuits = Graph.findElementaryCircuits(adjacencyLists, false, null);
		assertEquals(3*k, circuits.length);
	}
	
	
	@Test
	public void testFindElementaryCircuitsWithLoop()
	{
		int[][] adjacencyLists = new int[3][];
		adjacencyLists[0] = new int[] { 1 };
		adjacencyLists[1] = new int[] { 2, 1 };
		adjacencyLists[2] = new int[] { };
		
		int[][] circuits = Graph.findElementaryCircuits(adjacencyLists, false, null);
		assertEquals(1, circuits.length);
	}
	
	
	@Test
	public void testFindElementaryCircuitsWithDuplicateEdges()
	{
		int[][] adjacencyLists = new int[3][];
		adjacencyLists[0] = new int[] { 1 };
		adjacencyLists[1] = new int[] { 2, 2 };
		adjacencyLists[2] = new int[] { 0 };
		
		int[][] circuits = Graph.findElementaryCircuits(adjacencyLists, false, null);
		assertEquals(2, circuits.length);
	}
	
	
	@Test
	public void testFindElementaryCircuitsTwoStrongComponents()
	{
		int[][] adjacencyLists = new int[6][];
		adjacencyLists[0] = new int[] { 1 };
		adjacencyLists[1] = new int[] { 2 };
		adjacencyLists[2] = new int[] { 0, 3 };
		adjacencyLists[3] = new int[] { 4 };
		adjacencyLists[4] = new int[] { 5 };
		adjacencyLists[5] = new int[] { 3 };
		
		int[][] circuits = Graph.findElementaryCircuits(adjacencyLists, false, null);
		assertEquals(2, circuits.length);
	}
	
	
	@Test
	public void testFindElementaryCircuitsTwoSeparateComponents()
	{
		int[][] adjacencyLists = new int[6][];
		adjacencyLists[0] = new int[] { 1 };
		adjacencyLists[1] = new int[] { 2 };
		adjacencyLists[2] = new int[] { 0 };
		adjacencyLists[3] = new int[] { 4 };
		adjacencyLists[4] = new int[] { 5 };
		adjacencyLists[5] = new int[] { 3 };
		
		int[][] circuits = Graph.findElementaryCircuits(adjacencyLists, false, null);
		assertEquals(2, circuits.length);
	}
	
	
	@Test
	public void testFindElementaryCircuitsCopyArray()
	{
		int[][] adjacencyLists = new int[6][];
		adjacencyLists[0] = new int[] { 1 };
		adjacencyLists[1] = new int[] { 2 };
		adjacencyLists[2] = new int[] { 0, 3 };
		adjacencyLists[3] = new int[] { 4 };
		adjacencyLists[4] = new int[] { 5 };
		adjacencyLists[5] = new int[] { 3 };
		
		int[][] copy = new int[6][];
		copy[0] = new int[] { 1 };
		copy[1] = new int[] { 2 };
		copy[2] = new int[] { 0, 3 };
		copy[3] = new int[] { 4 };
		copy[4] = new int[] { 5 };
		copy[5] = new int[] { 3 };
		
		Graph.findElementaryCircuits(adjacencyLists, true, null);
		boolean equal = true;
		for (int i = 0; i < adjacencyLists.length; i++)
		{
			if (!Arrays.equals(copy[i], adjacencyLists[i]))
			{
				equal = false;
				break;
			}
		}
		assertTrue("The arrays should be equal since findElementaryCircuits() should use a copy!", equal);
		
		Graph.findElementaryCircuits(adjacencyLists, false, null);
		equal = true;
		for (int i = 0; i < adjacencyLists.length; i++)
		{
			if (!Arrays.equals(copy[i], adjacencyLists[i]))
			{
				equal = false;
				break;
			}
		}
		assertFalse("The arrays should not be equal since findElementaryCircuits() should use the original!", equal);
	}
}
