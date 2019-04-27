/*
 * Copyright 2017, 2018 Jakob Hjelm
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package komposten.utilities.search;


public class IndexEntry<T extends InvertedIndex.Indexable>
{
	private final T indexable;
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
	public IndexEntry(T indexable, int termPosition)
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


	public T getIndexable()
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