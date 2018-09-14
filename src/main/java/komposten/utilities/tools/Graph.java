/*
 * Copyright (c) 2014 Jakob Hjelm 
 */
package komposten.utilities.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * This class holds methods for operations on mathematical graphs.
 * @version
 * <b>1.2.0</b> <br />
 * <ul>
 * <li>Added <code>CircuitListener</code>.</li>
 * <li>Added a <code>CircuitListener</code> parameter to <code>findElementaryCircuits()</code>.
 * <li>Added JavaDoc to <code>circuit()</code>.
 * </ul>
 * <b>Older</b> <br />
 * 1.1.0> <br />
 * <ul>
 * <li>Added <code>abortCurrentOperations()</code>.</li>
 * <li>Added <code>addOperation()</code> and <code>removeOperation()</code>.</li>
 * </ul>
 * 1.0.0 <br />
 * <ul>
 * <li>Added <code>findElementaryCircuits(), circuit()</code> and <code>unblock()</code>.</li>
 * </ul>
 * @author Jakob Hjelm
 */
public class Graph
{
	private static int executingOperations = 0;
	private static boolean abortCurrentOperations = false;
	
	/**
	 * Finds all distinct (but see limitation 2) elementary circuits in a graph. This code is based on
	 * <a href="https://doi.org/10.1137/0204007">Donald B. Johnson's algorithm</a>.
	 * <br />
	 * <br />
	 * <b>Limitations:</b>
	 * <ol>
	 * <li>Loops (an edge between a vertex and itself) are counted as circuits.</li>
	 * <li>Duplicate circuits <i>will</i> occur if there are multiple edges between two vertices!</li>
	 * </ol>
	 * Operation can be aborted using {@link #abortCurrentOperations()}.
	 * 
	 * @param adjancencyLists Adjancency list that describes all edges from all
	 *                        vertices in in the graph.
	 * @param useCopyOfArray If <code>adjacencyList</code> should be copied before being
	 * used to ensure that the original array remains unaltered.
	 * @return An array containing all distinct elementary circuits in the provided
	 *         graph, or <code>null</code> if and only if execution was {@link #abortCurrentOperations() aborted}.
	 */
	public static int[][] findElementaryCircuits(int[][] adjacencyLists, boolean useCopyOfArray, CircuitListener listener)
	{
		addOperation();
		//TODO Graph; Check if first dividing the graph into Strongly Connected Components is a viable optimisation!
		List<int[]> circuits = new ArrayList<>();
		Stack<Integer> stack = new Stack<>();
		Map<Integer, List<Integer>> B = new HashMap<>(); //Stores information about parts of the graph with no elementary cycles.
		int[] lastCircuitCount = { 0 };
		
		if (useCopyOfArray)
		{
			int[][] newMatrix = new int[adjacencyLists.length][];
			for (int i = 0; i < adjacencyLists.length; i++)
			{
				int[] originalArray = adjacencyLists[i];
				int[] newArray = new int[originalArray.length];
				newMatrix[i] = newArray;
				System.arraycopy(originalArray, 0, newArray, 0, originalArray.length);
			}
			adjacencyLists = newMatrix;
		}
		
		int vertexCount = adjacencyLists.length;
		boolean[] blocked = new boolean[vertexCount];

		int root = 0;
		while (root < vertexCount && !abortCurrentOperations)
		{
			if (adjacencyLists.length > 0)
			{
				for (int i = root; i < adjacencyLists.length; i++)
				{
					if (abortCurrentOperations) break;
					blocked[i] = false;
					
					if (B.containsKey(i))
						B.get(i).clear();
				}

				circuit(root, root, stack, adjacencyLists, B, blocked, circuits, lastCircuitCount, listener);
				
				B.remove(root);
				for (int i = 0; i < adjacencyLists.length; i++)
				{
					if (abortCurrentOperations) break;
					for (int j = 0; j < adjacencyLists[i].length; j++)
					{
						if (abortCurrentOperations) break;
						if (adjacencyLists[i][j] == root)
						{
							adjacencyLists[i][j] = -1;
						}
					}
				}
				
				root++;
			}
			else
			{
				root = vertexCount;
			}
		}
		
		if (!abortCurrentOperations)
		{
			int[][] circuitArray = new int[circuits.size()][];
			
			for (int i = 0; i < circuits.size(); i++)
				circuitArray[i] = circuits.get(i);
			
			removeOperation();
			return circuitArray;
		}
		else
		{
			removeOperation();
			return null;
		}
	}

	
	/**
	 * 
	 * @param v The vertex to look at.
	 * @param s The root vertex of the current stack.
	 * @param stack The current stack of vertices.
	 * @param A_G The adjacency list.
	 * @param B Used to prevent duplicates of cycles. See Johnson's paper for more information.
	 * @param blocked An array with all "blocked" vertices. This prevents nodes from occurring more than once in a cycle.
	 * @param circuits A list containing all circuits that have been found so far.
	 * @param lastCircuitCount The circuit count the last time the {@link CircuitListener listener} was notified.
	 * @param listener A {@link CircuitListener} to notify when the circuit count updates.
	 * @return <code>true</code> if a circuit was found.
	 */
	private static boolean circuit(int v, int s, Stack<Integer> stack, int[][] A_G, Map<Integer, List<Integer>> B, boolean[] blocked, List<int[]> circuits, int[] lastCircuitCount, CircuitListener listener)
	{
		if (abortCurrentOperations) return false;
		boolean f = false;
		stack.push(v);
		blocked[v] = true;

		for (int w : A_G[v])
		{
			if (abortCurrentOperations) break;
			if (w == -1) continue;
			
			if (w == s)
			{
				int[] circuit = new int[stack.size()+1];
				for (int i = 0; i < stack.size(); i++)
					circuit[i] = stack.get(i);
				circuit[stack.size()] = s;
				circuits.add(circuit);
				f = true;
				
				if (listener != null && circuits.size() - lastCircuitCount[0] > circuits.size() * 0.1)
				{
					lastCircuitCount[0] = circuits.size();
					listener.onNewCircuitCount(lastCircuitCount[0]);
				}
			}
			else if (!blocked[w])
			{
				if (circuit(w, s, stack, A_G, B, blocked, circuits, lastCircuitCount, listener))
					f = true;
			}
		}

		if (f)
		{
			unblock(v, blocked, B);
		}
		else
		{
			for (int w : A_G[v])
			{
				if (abortCurrentOperations) break;
				if (w == -1) continue;
				
				if (!B.containsKey(w))
				{
					LinkedList<Integer> list = new LinkedList<>();
					list.add(v);
					B.put(w, list);
				}
				else if (!B.get(w).contains(v))
				{
					B.get(w).add(v);
				}
			}
		}

		stack.pop();

		if (abortCurrentOperations)
			return false;
		return f;
	}

	
	private static void unblock(int u, boolean[] blocked, Map<Integer, List<Integer>> B)
	{
		blocked[u] = false;
		
		List<Integer> BList = B.get(u);
		
		if (BList != null)
		{
			Iterator<Integer> iterator = B.get(u).iterator();
			while (iterator.hasNext())
			{
				if (abortCurrentOperations) break;
				int w = iterator.next();
				iterator.remove();
				if (blocked[w])
					unblock(w, blocked, B);
			}
		}
	}
	
	
	/**
	 * Aborts all current operations mid-execution.
	 * The general use-case is e.g. if a method takes too long to execute
	 * so the calling code wants to abort it (maybe due to user input).
	 */
	public static void abortCurrentOperations()
	{
		if (executingOperations > 0)
			abortCurrentOperations = true;
	}
	
	
	private static void addOperation()
	{
		executingOperations++;
	}
	
	
	private static void removeOperation()
	{
		executingOperations--;
		
		if (executingOperations <= 0)
		{
			executingOperations = 0;
			abortCurrentOperations = false;
		}
	}
	
	
	public static interface CircuitListener
	{
		/**
		 * Called by {@link Graph#findElementaryCircuits(int[][], boolean)} when the
		 * circuit count has been updated.
		 * 
		 * @param newCount The new total amount of circuits that has been found.
		 */
		public void onNewCircuitCount(int newCount);
	}
}
