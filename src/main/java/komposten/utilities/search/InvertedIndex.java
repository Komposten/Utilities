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
import java.util.HashMap;
import java.util.Map.Entry;


/**
 * A class that creates an inverted index from an array of {@link Indexable}
 * objects ("documents").<br />
 * It also calculates the frequency of each term for each document
 * (<a href="https://en.wikipedia.org/wiki/Tf–idf#Term_frequency_2">"term frequencies"</a>), as well as the
 * <a href="https://en.wikipedia.org/wiki/Tf–idf#Inverse_document_frequency_2">"inverse document frequencies"</a>
 * (defined as <code>ln(total amount of documents / the amount of documents that contain a specific
 * term)</code>).
 * <br />
 * <br />
 * The code is based on text and (python) code from <a href=
 * "http://www.ardendertat.com/2012/01/11/implementing-search-engines/">Arden
 * Dertat</a>.
 * 
 * @version <b>1.1.1</b> <br />
 *          <ul>
 *          <li><code>InvertedIndex</code> is now generic.</li>
 *          </ul>
 *          <b>Older</b> <br />
 *          1.1.1 <br />
 *          <ul>
 *          <li>Added getIndexables().</li>
 *          <li>splitText() now makes sure to return an empty array if <code>text</code> is empty or only contains white space.
 *          </ul>
 *          1.1.0 <br />
 *          <ul>
 *          <li>Migrated query(), exactQuery(), broadQuery() and rankIndexables() to {@link SearchEngine}.
 *          <li>Moved RankedIndexable to {@link SearchEngine}.</li>
 *          </ul>
 *          1.0.0 <br />
 *          <ul>
 *          <li>Created the class.</li>
 *          <li>Added createIndex().</li>
 *          <li>Added addObjectDataToIndex().</li>
 *          <li>Added calculateTermFrequencies().</li>
 *          <li>Added calculateInverseDocumentFrequencies().</li>
 *          <li>Added query(), with support for multi-word exact and "broad"
 *          queries.</li>
 *          <li>Added rankIndexables().</li>
 *          <li>Added splitText().</li>
 *          <li>Added dotProduct().</li>
 *          <li>Added getters.</li>
 *          <li>Added RankedIndexable.</li>
 *          </ul>
 * @author Jakob Hjelm
 */
public class InvertedIndex<T extends InvertedIndex.Indexable>
{
	public interface Indexable
	{
		public String getText();
	}

	private T[] documents;
	private HashMap<String, ArrayList<IndexEntry<T>>> index;
	private HashMap<String, double[]> termFrequencies; //Stored as "term; frequency per document", with frequencies ordered in the same order as the entries in index.get(term).
	private HashMap<String, Double> inverseDocumentFrequencies;

	public InvertedIndex(T[] documents)
	{
		this.documents = documents;
		createIndex(documents);
	}


	private void createIndex(T[] documents)
	{
		index = new HashMap<String, ArrayList<IndexEntry<T>>>();
		termFrequencies = new HashMap<String, double[]>();

		for (int objectIndex = 0; objectIndex < documents.length; objectIndex++)
		{
			T indexable = documents[objectIndex];
			String[] terms = splitText(indexable.getText());
			HashMap<String, IndexEntry<T>> objectData = new HashMap<String, IndexEntry<T>>();
			
			for (int i = 0; i < terms.length; i++)
			{
				String term = terms[i];

				if (objectData.containsKey(term))
				{
					objectData.get(term).addPosition(i);
				}
				else
				{
					objectData.put(term, new IndexEntry<T>(indexable, i));
				}
			}
			
			addObjectDataToIndex(objectData);
			calculateTermFrequencies(objectData);
		}
		
		calculateInverseDocumentFrequencies();
	}


	private void addObjectDataToIndex(HashMap<String, IndexEntry<T>> objectData)
	{
		for (String term : objectData.keySet())
		{
			if (index.containsKey(term))
			{
				index.get(term).add(objectData.get(term));
			}
			else
			{
				ArrayList<IndexEntry<T>> entries = new ArrayList<IndexEntry<T>>();
				entries.add(objectData.get(term));
				index.put(term, entries);
			}
		}
	}
	

	private void calculateTermFrequencies(HashMap<String, IndexEntry<T>> objectData)
	{
		//Calculate the Euclidian norm.
		double norm = 0;
		for (IndexEntry<T> entry : objectData.values())
		{
			norm += Math.pow(entry.getTermPositions().length, 2);
		}
		norm = Math.sqrt(norm);
		
		//Calculate the normalised term frequencies.
		for (Entry<String, IndexEntry<T>> entry : objectData.entrySet())
		{
			String term = entry.getKey();
			IndexEntry<T> indexEntry = entry.getValue();
			double termFrequency = indexEntry.getTermPositions().length / norm;
			
			if (termFrequencies.containsKey(term))
			{
				double[] oldArray = termFrequencies.get(term);
				double[] newArray = new double[oldArray.length + 1];
				
				for (int i = 0; i < oldArray.length; i++)
				{
					newArray[i] = oldArray[i];
				}
				
				newArray[oldArray.length] = termFrequency;
				termFrequencies.put(term, newArray);
			}
			else
			{
				termFrequencies.put(term, new double[] { termFrequency });
			}
		}
	}
	
	
	private void calculateInverseDocumentFrequencies()
	{
		inverseDocumentFrequencies = new HashMap<String, Double>();
		
		for (String term : index.keySet())
		{
			double inverseDocumentFrequency = documents.length / (float)index.get(term).size();
			inverseDocumentFrequency = Math.log(inverseDocumentFrequency); //Reduces the weight of idf compared to tf.
			
			inverseDocumentFrequencies.put(term, inverseDocumentFrequency);
		}
	}


	/**
	 * Splits a line of text around ' ' and '-'.
	 * @return
	 */
	public String[] splitText(String text)
	{
		if (text.isEmpty() || text.equals("\\s*"))
			return new String[0];
		
		return text.toLowerCase().split("(\\s+|-+)");
	}
	
	
	public HashMap<String, ArrayList<IndexEntry<T>>> getIndex()
	{
		return index;
	}
	
	
	public T[] getIndexables()
	{
		return documents;
	}
	
	
	public int getIndexableCount()
	{
		return documents.length;
	}
	
	
	public int getIndexSize()
	{
		return index.size();
	}
	
	
	public HashMap<String, double[]> getTermFrequencies()
	{
		return termFrequencies;
	}
	
	
	public HashMap<String, Double> getInverseDocumentFrequencies()
	{
		return inverseDocumentFrequencies;
	}
}

