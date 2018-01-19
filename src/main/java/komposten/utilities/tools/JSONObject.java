/*
 * Copyright (c) 2014 Jakob Hjelm 
 */
package komposten.utilities.tools;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * A data structure that describes a JSON object ("JavaScript Object Notation
 * object"). The data can be converted to a formatted or minified string by
 * calling {@link #toString(boolean)} (e.g. for writing to files). <br />
 * To load existing JSON files, see {@link JSONReader}.
 * 
 * @version <b>1.2.1</b> <br />
 *          <ul>
 *          <li>Renamed toMultiLineString() to toString().</li>
 *          <li>Added a "minify" parameter to toString(), to allow creation of
 *          more compressed JSON data.</li>
 *          </ul>
 *          <b>Older</b> <br />
 *          1.2.0 <br />
 *          <ul>
 *          <li>Added <code>cleanUpSpaces()</code> to improve performance of
 *          <code>toMultiLineString()</code>.</li>
 *          <li>Fixed a bug where <code>toMultiLineString()</code> would remove
 *          spaces inside quotation marks (i.e. elements or keys).</li>
 *          </ul>
 *          1.1.0 <br />
 *          <ul>
 *          <li>Added <code>removeElement()</code> and
 *          <code>hasElement()</code>.</li>
 *          </ul>
 *          1.0.0 <br />
 *          <ul>
 *          <li>Initial version, supporting addition of strings, arrays and
 *          other <code>JSONObjects</code>, <code>toMultiLineString()</code>,
 *          and retrieval of members.</li>
 *          </ul>
 * 
 * @author Jakob Hjelm
 *
 */
public class JSONObject
{
  Map<String, Object> members;
  
  
  /**
   * Creates an empty <code>JSONObject</code>.
   */
  public JSONObject()
  {
    members = new LinkedHashMap<String, Object>();
  }
  
  
  
  /**
   * Retrieves a member's value based on a name/key.
   * @param name The name of the member to retrieve.
   * @return The value stored under <code>name</code>, or <code>null</code> if no such member exists.
   */
  public Object getMemberByName(String name)
  {
    return members.get(name);
  }
  
  
  
  /**
	 * @return An entry set containing all members in this
	 *         <code>JSONObject</code>. The set is backed by the
	 *         <code>JSONObject</code>, so changes to the set are reflected in the
	 *         <code>JSONObject</code>, and vice-versa.
	 */
  public Set<Entry<String, Object>> getMembers()
  {
    return members.entrySet();
  }
  
  
  
  /**
   * Adds a <code>key:String</code> pair to this object.
   * @param identifier
   * @param value
   */
  public void addStringPair(String identifier, String value)
  {
    members.put(identifier, value);
  }
  
  
  
  /**
   * Adds a <code>key:JSONObject</code> pair to this object.
   * @param identifier
   * @param object
   */
  public void addObjectPair(String identifier, JSONObject object)
  {
    if (object == this)
      throw new IllegalArgumentException("A JSONObject cannot be added to itself!");
    
    members.put(identifier, object);
  }
  
  
  
  /**
	 * Adds a <code>key:Object[]</code> pair to this object.
	 * 
	 * @param identifier
	 * @param array An array of objects. All elements in the array will be treated
	 *          as strings (using <code>toString()</code>), unless they are of
	 *          type <code>JSONObject</code>s or <code>Object[]</code>.
	 */
  public void addArrayPair(String identifier, Object[] array)
  {
    members.put(identifier, array);
  }
  
  
  
  /**
   * Removes the element with the specified identifier.
   * @param identifier
   * @return The value of the element that was removed.
   */
  public Object removeElement(String identifier)
  {
  	return members.remove(identifier);
  }
  
  
  /**
   * Checks if the <code>JSONObject</code> contains an element with the specified identifier.
   */
  public boolean hasElement(String identifier)
  {
  	return members.containsKey(identifier);
  }

  
  
	/**
	 * Converts the <code>JSONObject</code> to a string. This string can be saved
	 * to a file and a {@link JSONReader} can be used to re-create a
	 * <code>JSONObject</code> from it.
	 * 
	 * @param minify If <code>true</code> the JSON will be minified to reduce the
	 *          amount of space required to store it. If <code>false</code> the
	 *          JSON will be formatted in a human-readable way.
	 */
  public String toString(boolean minify)
  {
  	//TODO JSONObject; Add a toStream() option that streams the data (maybe every 1000 chars or so) to a stream.
  	//MAYBE JSONObject; Make sure toString() puts spaces and new lines in the correct places so no replace-calls are needed.
    String string = toString(this, 0, minify);
		string = cleanUpSpaces(new StringBuilder(string)); //Using a custom method here instead of replaceAll() because it's 2-20 times faster.
		return string.replaceAll("\\n+", "\n"); //This part is really fast anyway, so no need for a custom method.
  }
  
  
  
  private String cleanUpSpaces(StringBuilder builder)
	{
  	StringBuilder result = new StringBuilder(builder.length());
  	boolean isInQuote = false;
  	int spaceStart = -1;
  	
  	for (int i = 0; i < builder.length(); i++)
  	{
  		char c = builder.charAt(i);
  		
  		if (isInQuote)
  		{
    		if (c == '"')
    		{
    			isInQuote = !isInQuote;
    			spaceStart = -1;
    		}
    		
  			result.append(c);
  		}
  		else
  		{
    		if (c == '"')
    		{
    			isInQuote = !isInQuote;
    		}
    		
	  		if (Character.isWhitespace(c) && spaceStart == -1)
	  		{
	  			spaceStart = i;
	  		}
	  		else if (c == ',' && spaceStart != -1)
	  		{
	  			result.append(",\n");
	  			spaceStart = -1;
	  		}
	  		else if ((!Character.isWhitespace(c) && c != ',') || (spaceStart == -1 && c == ','))
	  		{
	  			if (spaceStart != -1)
	  			{
	  				result.append(builder.substring(spaceStart, i+1));
	  			}
	  			else
	  			{
	  				result.append(c);
	  			}
	  			
	  			spaceStart = -1;
	  		}
  		}
  	}
  	
		return result.toString();
	}

  

	private String toString(JSONObject object, int tabLevel, boolean minify)
  {
    StringBuilder stringBuilder = new StringBuilder();
    
    stringBuilder.append(formatLine("{", tabLevel, minify));
    
    int index = 0;
    
    for (Map.Entry<String, Object> pair : object.members.entrySet())
    {
      if (pair.getValue() instanceof Object[])
      {
        stringBuilder.append(formatLine("\"" + pair.getKey() + "\":", tabLevel + 2, minify));
        stringBuilder.append(printArray((Object[])pair.getValue(), tabLevel + 2, minify));

        if (index < object.members.size() - 1)
          stringBuilder.append(formatLine(",", tabLevel + 2, minify));
      }
      else if (pair.getValue() instanceof JSONObject)
      {
        stringBuilder.append(formatLine("\"" + pair.getKey() + "\":", tabLevel + 2, minify));
        stringBuilder.append(toString((JSONObject)pair.getValue(), tabLevel + 2, minify));

        if (index < object.members.size() - 1)
          stringBuilder.append(formatLine(",", tabLevel + 2, minify));
      }
      else
      {
        if (index < object.members.size() - 1)
          stringBuilder.append(formatLine("\"" + pair.getKey() + "\":\"" + pair.getValue() + "\",", tabLevel + 2, minify));
        else
          stringBuilder.append(formatLine("\"" + pair.getKey() + "\":\"" + pair.getValue() + "\"", tabLevel + 2, minify));
      }
      
      index++;
    }

    stringBuilder.append(formatLine("}", tabLevel, minify));
    
    return stringBuilder.toString();
  }
  
  
  
  private String printArray(Object[] array, int tabLevel, boolean minify)
  {
    StringBuilder stringBuilder = new StringBuilder();
    
    stringBuilder.append(formatLine("[", tabLevel, minify));
    
    for (int i = 0; i < array.length; i++)
    {
      Object element = array[i];
      
      if (element instanceof Object[])
      {
        stringBuilder.append(printArray((Object[])element, tabLevel + 2, minify));
        
        if (i < array.length - 1)
          stringBuilder.append(formatLine(",", tabLevel + 2, minify));
      }
      else if (element instanceof JSONObject)
      {
        stringBuilder.append(toString((JSONObject)element, tabLevel + 2, minify));
        
        if (i < array.length - 1)
          stringBuilder.append(formatLine(",", tabLevel + 2, minify));
      }
      else
      {
        if (i < array.length - 1)
          stringBuilder.append(formatLine("\"" + element.toString() + "\",", tabLevel + 2, minify));
        else
          stringBuilder.append(formatLine("\"" + element.toString() + "\"", tabLevel + 2, minify));
      }
    }

    stringBuilder.append(formatLine("]", tabLevel, minify));
    
    return stringBuilder.toString();
  }
  
  
  
  private String formatLine(String line, int tabLevel, boolean minify)
  {
  	if (!minify)
  	{
	    if (tabLevel > 0)
	      return String.format("%1$" + tabLevel + "s", "") + line + "\n";
	    else
	      return line + "\n";
  	}
  	else
  	{
  		return line;
  	}
  }
}