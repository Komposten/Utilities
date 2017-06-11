/*
 * Copyright (c) 2017 Jakob Hjelm 
 */
package komposten.utilities.tools;


/**
 * A class to perform different operations concerning text.
 * 
 * @version <b>1.0.0</b> <br />
 *          <ul>
 *          <li>Added editDistance().</li>
 *          </ul>
 *          <b>Older</b> <br />
 * @author Jakob Hjelm
 */
public class Text
{
	/**
	 * Calculates the
	 * <a href="https://en.wikipedia.org/wiki/Levenshtein_distance">Levenshtein
	 * distance</a> between two strings. <br />
	 * The code is a more optimised version of an implementation by <a href=
	 * "http://www.programcreek.com/2013/12/edit-distance-in-java/">Program
	 * Creek</a>.
	 * 
	 * @return The minimum amount of insertions, deletions and/or substitutions
	 *         required to change <code>string1</code> into <code>string2</code>.
	 */
	public static int editDistance(String string1, String string2)
	{
		if (string1 == null && string2 == null)
			return 0;
		if ((string1 == null || string1.isEmpty()) && string2 != null)
			return string2.length();
		if ((string2 == null || string2.isEmpty()) && string1 != null)
			return string1.length();

		int length1 = string1.length();
		int length2 = string2.length();

		int[] distance = new int[length2 + 1]; // We only need to keep track of 1 column, and then update values in it as we go.
		int previous = 0; //We also need to store the previously calculated value separately so we don't write to the column too early.
		int current = 0;

		for (int j = 0; j <= length2; j++)
			distance[j] = j;

		for (int i = 0; i < length1; i++)
		{
			previous = i+1;
			
			char char1 = string1.charAt(i);

			for (int j = 0; j < length2; j++)
			{
				char char2 = string2.charAt(j);

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
				previous = current;
			}
			
			distance[length2] = current;
		}

		return distance[length2];
	}
}
