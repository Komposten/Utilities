/*
 * Copyright (c) 2017 Jakob Hjelm 
 */
package komposten.utilities.search;

import komposten.utilities.search.InvertedIndex.Indexable;


public class IndexEntry
{
	private final Indexable indexable;
	/**
	 * Integers describing the positions (in the indexable's string) of the term
	 * this is an entry for.<br />
	 * E.g. <code>world</code> has index <code>1</code> in the string
	 * <code>Hello world!</code>.
	 */
	private int[] termPositions;


	/**
	 * @param indexable
	 * @param termPosition See {@link #termPositions}
	 */
	public IndexEntry(Indexable indexable, int termPosition)
	{
		this.indexable = indexable;
		this.termPositions = new int[] { termPosition };
	}
	
	
	public void addPosition(int position)
	{
		int[] newArray = new int[termPositions.length + 1];
		
		for (int i = 0; i < termPositions.length; i++)
		{
			newArray[i] = termPositions[i];
		}
		
		newArray[termPositions.length] = position;
		termPositions = newArray;
	}


	public Indexable getIndexable()
	{
		return indexable;
	}

	/**
	 * @see #termPositions
	 */
	public int[] getTermPositions()
	{
		return termPositions;
	}
}