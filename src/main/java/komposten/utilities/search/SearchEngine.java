/*
 * Copyright (c) 2017 Jakob Hjelm 
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
 * @version <b>1.1.0</b> <br />
 *          <ul>
 *          <li>Added <code>returnAllIfEmptyQuery</code> to query().</li>
 *          </ul>
 *          <b>Older</b> <br />
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
public class SearchEngine //NEXT_TASK SearchEngine; Add a "return all on empty query"-option to query().
{
	private InvertedIndex index;
	
	
	public SearchEngine(InvertedIndex index)
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
	public List<Indexable> query(String query, boolean exact, boolean returnAllIfEmptyQuery)
	{
		String[] terms = index.splitText(query);
		
		if (terms.length == 0)
		{
			if (returnAllIfEmptyQuery)
				return new ArrayList<Indexable>(Arrays.asList(index.getIndexables()));
			else
				return new ArrayList<Indexable>();
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


	private ArrayList<Indexable> exactQuery(String[] terms)
	{
		HashSet<Indexable> matchingIndexables = new HashSet<Indexable>();
		
		for (String term : terms)
		{
			if (index.getIndex().containsKey(term))
			{
				ArrayList<IndexEntry> indexEntries = index.getIndex().get(term);
				
				for (IndexEntry entry : indexEntries)
				{
					matchingIndexables.add(entry.getIndexable());
				}
			}
		}
		
		return rankIndexables(terms, matchingIndexables, null);
	}


	private ArrayList<Indexable> broadQuery(String[] terms)
	{
		HashSet<Indexable> matchingIndexables = new HashSet<Indexable>();
		ArrayList<String> matchingTerms = new ArrayList<String>();
		ArrayList<Double> matchScores = new ArrayList<Double>();
		
		for (String term : terms)
		{
			for (Entry<String, ArrayList<IndexEntry>> entry : index.getIndex().entrySet())
			{
				String indexTerm = entry.getKey();
				int distance = Text.editDistance(term, indexTerm);
				
				double normalisedDistance = distance/(float)indexTerm.length();
				
				if (normalisedDistance < 0.8)
				{
					matchingTerms.add(indexTerm);
					matchScores.add(Math.pow(1-normalisedDistance, 2));
					
					for (IndexEntry indexEntry : entry.getValue())
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


	private ArrayList<Indexable> rankIndexables(String[] terms,
			Collection<Indexable> indexables, double[] scoresArray)
	{
		HashMap<Indexable, double[]> indexableVectors = new HashMap<Indexable, double[]>();
		double[] queryVector = new double[terms.length];
		
		for (int i = 0; i < terms.length; i++)
		{
			String term = terms[i];
			
			if (index.getIndex().containsKey(term))
			{
				double score = (scoresArray == null ? 1 : scoresArray[i]);
				queryVector[i] = index.getInverseDocumentFrequencies().get(term) * score;
				
				ArrayList<IndexEntry> entriesForTerm = index.getIndex().get(term);
				for (int j = 0; j < entriesForTerm.size(); j++)
				{
					Indexable indexable = entriesForTerm.get(j).getIndexable();
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
		
		
		ArrayList<RankedIndexable> rankedIndexables = new ArrayList<RankedIndexable>();
		for (Entry<Indexable, double[]> entry : indexableVectors.entrySet())
		{
			Indexable indexable = entry.getKey();
			double[] vector = entry.getValue();
			
			double dot = MathOps.dotProduct(vector, queryVector);
			
			rankedIndexables.add(new RankedIndexable(dot, indexable));
		}
		
		Collections.sort(rankedIndexables);
		
		ArrayList<Indexable> result = new ArrayList<Indexable>();
		for (RankedIndexable rankedIndexable : rankedIndexables)
		{
			result.add(rankedIndexable.indexable);
		}
		return result;
	}

	
	
	private static class RankedIndexable implements Comparable<RankedIndexable>
	{
		public double rank;
		public Indexable indexable;
		
		
		public RankedIndexable(double rank, Indexable indexable)
		{
			this.rank = rank;
			this.indexable = indexable;
		}
		

		@Override
		public int compareTo(RankedIndexable o)
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
