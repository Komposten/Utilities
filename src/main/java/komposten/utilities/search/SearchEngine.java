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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import komposten.utilities.search.InvertedIndex.Indexable;
import komposten.utilities.tools.MathOps;
import komposten.utilities.tools.Text;


/**
 * A class used to query an {@link InvertedIndex} and sort the resulting list of
 * {@link Indexable}s based on
 * <a href="https://en.wikipedia.org/wiki/Tf–idf">term frequency and inverse
 * document frequency</a>.
 * <br />
 * Queries are split into terms, and each term is matched against the index.
 * Matches can either be <i>exact</i> or <i>broad</i> (which uses Levenshtein
 * distance to find similar terms in the index).
 * <br />
 * <br />
 * TF-IDF ranking is based on text and (python) code from <a href=
 * "http://www.ardendertat.com/2012/01/11/implementing-search-engines/">Arden
 * Dertat</a>.
 * 
 * @see InvertedIndex
 * @version <b>1.2.0</b> <br />
 *          <ul>
 *          <li><code>SearchEngine</code> is now generic.</li>
 *          </ul>
 *          <b>Older</b> <br />
 *          1.1.0 <br />
 *          <ul>
 *          <li>Added <code>returnAllIfEmptyQuery</code> to query().</li>
 *          </ul>
 *          1.0.0 <br />
 *          <ul>
 *          <li>Created the class.</li>
 *          <li>Added query(), with support for multi-word exact and "broad"
 *          queries.</li>
 *          <li>Added rankIndexables().</li>
 *          <li>Added RankedIndexable.</li>
 *          </ul>
 * @author Jakob Hjelm
 */
public class SearchEngine<T extends InvertedIndex.Indexable>
{
	private InvertedIndex<T> index;
	
	
	public SearchEngine(InvertedIndex<T> index)
	{
		this.index = index;
	}


	/**
	 * Finds {@link Indexable Indexables} in the index that contain the specified
	 * query.
	 * 
	 * @param query The query to look for.
	 * @param exact <code>true</code> if only exact matches should be returned,
	 *          <code>false</code> if {@link Text#editDistance(String, String)
	 *          Levenshtein distance} should be used to find approximate matches.
	 * @param returnAllIfEmptyQuery If set to <code>true</code>, all indexables in
	 *          the index will be returned if <code>query</code> is an empty
	 *          string. If set to <code>false</code>, an empty list would be
	 *          returned.
	 * @return A list of the <code>Indexable</code>s that match the query, sorted
	 *         in descending order.
	 */
	public List<T> query(String query, boolean exact, boolean returnAllIfEmptyQuery)
	{
		String[] terms = index.splitText(query);
		
		if (terms.length == 0)
		{
			if (returnAllIfEmptyQuery)
				return new ArrayList<T>(Arrays.asList(index.getIndexables()));
			else
				return new ArrayList<T>();
		}
		
		if (exact)
		{
			return exactQuery(terms);
		}
		else
		{
			return broadQuery(terms);
		}
	}


	private ArrayList<T> exactQuery(String[] terms)
	{
		HashSet<T> matchingIndexables = new HashSet<T>();
		
		for (String term : terms)
		{
			if (index.getIndex().containsKey(term))
			{
				ArrayList<IndexEntry<T>> indexEntries = index.getIndex().get(term);
				
				for (IndexEntry<T> entry : indexEntries)
				{
					matchingIndexables.add(entry.getIndexable());
				}
			}
		}
		
		return rankIndexables(terms, matchingIndexables, null);
	}


	private ArrayList<T> broadQuery(String[] terms)
	{
		HashSet<T> matchingIndexables = new HashSet<T>();
		ArrayList<String> matchingTerms = new ArrayList<String>();
		ArrayList<Double> matchScores = new ArrayList<Double>();
		
		for (String term : terms)
		{
			for (Entry<String, ArrayList<IndexEntry<T>>> entry : index.getIndex().entrySet())
			{
				String indexTerm = entry.getKey();
				int distance = Text.editDistance(term, indexTerm);
				
				double normalisedDistance = distance/(float)indexTerm.length();
				
				if (normalisedDistance < 0.8)
				{
					matchingTerms.add(indexTerm);
					matchScores.add(Math.pow(1-normalisedDistance, 2));
					
					for (IndexEntry<T> indexEntry : entry.getValue())
					{
						matchingIndexables.add(indexEntry.getIndexable());
					}
				}
			}
		}
		
		String[] termsArray = matchingTerms.toArray(new String[matchingTerms.size()]);
		double[] scoresArray = new double[matchScores.size()];
		for (int i = 0; i < matchScores.size(); i++)
		{
			scoresArray[i] = matchScores.get(i);
		}
		
		return rankIndexables(termsArray, matchingIndexables, scoresArray);
	}


	private ArrayList<T> rankIndexables(String[] terms,
			Collection<T> indexables, double[] scoresArray)
	{
		HashMap<T, double[]> indexableVectors = new HashMap<T, double[]>();
		double[] queryVector = new double[terms.length];
		
		for (int i = 0; i < terms.length; i++)
		{
			String term = terms[i];
			
			if (index.getIndex().containsKey(term))
			{
				double score = (scoresArray == null ? 1 : scoresArray[i]);
				queryVector[i] = index.getInverseDocumentFrequencies().get(term) * score;
				
				ArrayList<IndexEntry<T>> entriesForTerm = index.getIndex().get(term);
				for (int j = 0; j < entriesForTerm.size(); j++)
				{
					T indexable = entriesForTerm.get(j).getIndexable();
					if (indexables.contains(indexable))
					{
						if (!indexableVectors.containsKey(indexable))
						{
							indexableVectors.put(indexable, new double[terms.length]);
						}
						
						indexableVectors.get(indexable)[i] = index.getTermFrequencies().get(term)[j];
					}
				}
			}
		}
		
//		//Normalise the vectors before comparing them. If this code gets used, refactor into a normalise(double[] vector) method.
//		double norm = 0;
//		for (double d : queryVector)
//		{
//			norm += d*d;
//		}
//		norm = Math.sqrt(norm);
//		for (int i = 0; i < queryVector.length; i++)
//		{
//			queryVector[i] = queryVector[i] / norm;
//		}
//		
//		for (Entry<Indexable, double[]> entry : indexableVectors.entrySet())
//		{
//			norm = 0;
//			double[] vector = entry.getValue();
//			
//			for (double d : vector)
//			{
//				norm += d*d;
//			}
//			norm = Math.sqrt(norm);
//			for (int i = 0; i < vector.length; i++)
//			{
//				vector[i] = vector[i] / norm;
//			}
//		}
		
		
		ArrayList<RankedIndexable<T>> rankedIndexables = new ArrayList<RankedIndexable<T>>();
		for (Entry<T, double[]> entry : indexableVectors.entrySet())
		{
			T indexable = entry.getKey();
			double[] vector = entry.getValue();
			
			double dot = MathOps.dotProduct(vector, queryVector);
			
			rankedIndexables.add(new RankedIndexable<T>(dot, indexable));
		}
		
		Collections.sort(rankedIndexables);
		
		ArrayList<T> result = new ArrayList<T>();
		for (RankedIndexable<T> rankedIndexable : rankedIndexables)
		{
			result.add(rankedIndexable.indexable);
		}
		return result;
	}

	
	
	private static class RankedIndexable<T extends InvertedIndex.Indexable> implements Comparable<RankedIndexable<T>>
	{
		public double rank;
		public T indexable;
		
		
		public RankedIndexable(double rank, T indexable)
		{
			this.rank = rank;
			this.indexable = indexable;
		}
		

		@Override
		public int compareTo(RankedIndexable<T> o)
		{
			if (rank > o.rank)
			{
				return -1;
			}
			else if (rank == o.rank)
			{
				return 0;
			}
			else
			{
				return 1;
			}
		}
	}
}
