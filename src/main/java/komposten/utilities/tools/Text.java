/*
 * Copyright (c) 2017 Jakob Hjelm 
 */
package komposten.utilities.tools;

/**
 * A class to perform different operations concerning text.
 * 
 * @version <b>1.2.1</b> <br />
 *          <ul>
 *          <li><code>editDistance(String, String, boolean)</code> now properly creates the matrix if <code>saveMatrix == true</code> and either string is null/empty.</li>
 *          <li>Renamed <code>getEditDistanceChangeType()</code> to <code>getEditDistanceChangeSummary()</code>.
 *          <li>Renamed <code>updateChange()</code> to <code>addToChangeSummary()</code>.
 *          </ul>
 *          <b>Older</b> <br />
 *          1.2.0 <br />
 *          <ul>
 *          <li>Added <code>getEditDistanceChangeType()</code>.</li>
 *          <li>Fixed <code>getEditDistanceMatrix()</code> missing a column.</li>
 *          <li>Transposed the edit distance matrix so it correctly represents changes.</li>
 *          <li>Added and updated javadoc for most methods.</li>
 *          </ul>
 *          1.1.0 <br />
 *          <ul>
 *          <li>Added <code>editDistance(String, String, boolean)</code>.</li>
 *          <li>Added <code>getEditDistanceMatrix()</code>.</li>
 *          </ul>
 *          1.0.0 <br />
 *          <ul>
 *          <li>Added <code>editDistance(String, String)</code>.</li>
 *          </ul>
 * @author Jakob Hjelm
 */
public class Text
{
	private static int[][] editDistanceMatrix;

	
	/**
	 * Calculates the
	 * <a href="https://en.wikipedia.org/wiki/Levenshtein_distance">Levenshtein
	 * distance</a> between two strings. <br />
	 * The code is a more optimised version of an implementation by <a href=
	 * "http://www.programcreek.com/2013/12/edit-distance-in-java/">Program
	 * Creek</a>. <br />
	 * <br />
	 * This method calls {@link #editDistance(String, String, boolean)
	 * editDistance(string1, string2, false)} and will thus not create an
	 * {@link #getEditDistanceMatrix() edit distance matrix} or allow retrieval of
	 * {@link #getEditDistanceChangeSummary() change type}.
	 * 
	 * @return The minimum amount of insertions, deletions and/or substitutions
	 *         required to change <code>string1</code> into <code>string2</code>.
	 * @see #editDistance(String, String, boolean)
	 */
	public static int editDistance(String string1, String string2)
	{
		return editDistance(string1, string2, false);
	}
	
	
	
	/**
	 * Calculates the
	 * <a href="https://en.wikipedia.org/wiki/Levenshtein_distance">Levenshtein
	 * distance</a> between two strings. <br />
	 * The code is a more optimised version of an implementation by <a href=
	 * "http://www.programcreek.com/2013/12/edit-distance-in-java/">Program
	 * Creek</a>.
	 * 
	 * @param saveMatrix <code>true</code> if the edit distance matrix should be saved. It can later be retrieved using {@link #getEditDistanceMatrix()}.
	 * @return The minimum amount of insertions, deletions and/or substitutions
	 *         required to change <code>string1</code> into <code>string2</code>.
	 */
	public static int editDistance(String string1, String string2, boolean saveMatrix)
	{
		int length1 = (string1 == null ? 0 : string1.length());
		int length2 = (string2 == null ? 0 : string2.length());

		editDistanceMatrix = (saveMatrix ? new int[length2 + 1][length1 + 1] : null);
		
		if (string1 == null && string2 == null)
		{
			return 0;
		}
		else if ((string1 == null || string1.isEmpty()) && string2 != null)
		{
			if (saveMatrix)
			{
				for (int i = 0; i <= length2; i++)
					editDistanceMatrix[i][0] = i;
			}
			return string2.length();
		}
		else if ((string2 == null || string2.isEmpty()) && string1 != null)
		{
			if (saveMatrix)
			{
				for (int i = 0; i <= length1; i++)
					editDistanceMatrix[0][i] = i;
			}
			return string1.length();
		}
				
		int[] distance = new int[length1 + 1]; // We only need to keep track of 1 column, and then update values in it as we go.
		int previous = 0; //We also need to store the previously calculated value separately so we don't write to the column too early.
		int current = 0;

		for (int j = 0; j <= length1; j++)
		{
			distance[j] = j;
			if (saveMatrix) editDistanceMatrix[0][j] = j;
		}

		for (int i = 0; i < length2; i++)
		{
			previous = i + 1;
			
			char char1 = string2.charAt(i);

			for (int j = 0; j < length1; j++)
			{
				char char2 = string1.charAt(j);

				if (char1 == char2)
				{
					current = distance[j];
				}
				else
				{
					int insert = previous + 1;
					int delete = distance[j + 1] + 1;
					int replace = distance[j] + 1;

					int min = Math.min(insert, Math.min(delete, replace));

					current = min;
				}
				
				distance[j] = previous;
				
				if (saveMatrix)
					editDistanceMatrix[i + 1][j] = distance[j];
				
				previous = current;
			}
			
			distance[length1] = current;
			
			if (saveMatrix)
				editDistanceMatrix[i + 1][length1] = distance[length1];
		}

		return distance[length1];
	}
	
	
	/**
	 * @return The edit distance matrix created by {@link #editDistance(String, String, boolean)} if <code>saveMatrix</code> was set to <code>true</code>.
	 * @throws IllegalStateException If the last call to {@link #editDistance(String, String, boolean)} had <code>saveMatrix</code> set to <code>false</code>.
	 */
	public static int[][] getEditDistanceMatrix()
	{
		if (editDistanceMatrix == null)
		{
			throw new IllegalStateException(
					"getEditDistanceMatrix() can only be called after a call to "
					+ "editDistance(String, String, boolean) with saveMatrix == true!");
		}
		
		return editDistanceMatrix;
	}
	
	
	/**
	 * These enum values represent a change or a combination of changes that are
	 * required to change one string into another.
	 */
	public enum Change
	{
		None,
		Insertion,
		Deletion,
		Substitution,
		InDel,
		InSub,
		SubDel,
		InDelSub
	}


	/**
	 * @return A summary of the types of changes that are required to go from the
	 *         first to the second string in the last call to
	 *         {@link #editDistance(String, String)}.
	 * @throws IllegalStateException If the last call to
	 *           {@link #editDistance(String, String, boolean)} had
	 *           <code>saveMatrix</code> set to <code>false</code>.
	 * @see Change
	 * @see #editDistance(String, String)
	 * @see #editDistance(String, String, boolean)
	 */
	public static Change getEditDistanceChangeSummary()
	{
		if (editDistanceMatrix == null)
		{
			throw new IllegalStateException(
					"getEditDistanceChangeType() can only be called after a call to "
					+ "editDistance(String, String, boolean) with saveMatrix == true!");
		}
		
		int x = editDistanceMatrix.length-1;
		int y = editDistanceMatrix[0].length-1;
		
		Change change = Change.None;
		
		while (x > 0 || y > 0)
		{
			boolean isLeftmost = (x == 0);
			boolean isTop = (y == 0);
			
			int current = editDistanceMatrix[x][y];
			int left = (!isLeftmost ? editDistanceMatrix[x-1][y] : Integer.MAX_VALUE);
			int above = (!isTop ? editDistanceMatrix[x][y-1] : Integer.MAX_VALUE);
			int diagonal = (!isTop ? (!isLeftmost ? editDistanceMatrix[x-1][y-1] : Integer.MAX_VALUE) : Integer.MAX_VALUE);
			
			if (diagonal <= left && diagonal <= above && diagonal <= current)
			{
				if (diagonal < current)
					change = addToChangeSummary(Change.Substitution, change);
				
				x = x-1;
				y = y-1;
			}
			else if (left <= above && left <= current)
			{
				change = addToChangeSummary(Change.Insertion, change);
				x = x-1;
			}
			else
			{
				change = addToChangeSummary(Change.Deletion, change);
				y = y-1;
			}
		}
		
		return change;
	}
	
	
	private static Change addToChangeSummary(Change newChange, Change summary)
	{
		if (summary == null || summary == Change.None)
			return newChange;
		if (newChange == summary)
			return summary;
		
		switch (summary)
		{
			case InDelSub :
				return summary;
			case Insertion :
				if (newChange == Change.Deletion)
					return Change.InDel;
				if (newChange == Change.Substitution)
					return Change.InSub;
				break;
			case Deletion :
				if (newChange == Change.Insertion)
					return Change.InDel;
				if (newChange == Change.Substitution)
					return Change.SubDel;
				break;
			case Substitution :
				if (newChange == Change.Insertion)
					return Change.InSub;
				if (newChange == Change.Deletion)
					return Change.SubDel;
				break;
			case InDel :
				if (newChange == Change.Insertion)
					return summary;
				if (newChange == Change.Deletion)
					return summary;
				if (newChange == Change.Substitution)
					return Change.InDelSub;
				break;
			case InSub :
				if (newChange == Change.Insertion)
					return summary;
				if (newChange == Change.Deletion)
					return Change.InDelSub;
				if (newChange == Change.Substitution)
					return summary;
				break;
			case SubDel :
				if (newChange == Change.Insertion)
					return Change.InDelSub;
				if (newChange == Change.Deletion)
					return summary;
				if (newChange == Change.Substitution)
					return summary;
				break;
			default :
				break;
		}
		
		return summary;
	}
}
