/*
 * Copyright 2018 Jakob Hjelm
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
package komposten.utilities.data;

import java.util.Arrays;
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
 * @version <b>1.3.0</b> <br />
 *          <ul>
 *          <li>Added hashCode() and equals().</li>
 *          <li>Added support for numbers, booleans and nulls.</li>
 *          <li>Added containsThis(), to prevent an object from being added to itself.</li>
 *          </ul>
 *          <b>Older</b> <br />
 *          1.2.2 <br />
 *          <ul>
 *          <li>Renamed hasElement() to hasMember() for consistency in naming.</li>
 *          <li>Renamed removeElement() to removeMember() for consistency in naming.</li>
 *          <li>Renamed the addXXXPair()-methods to a unified addMember().</li>
 *          </ul>
 *          1.2.1 <br />
 *          <ul>
 *          <li>Renamed toMultiLineString() to toString().</li>
 *          <li>Added a "minify" parameter to toString(), to allow creation of
 *          more compressed JSON data.</li>
 *          </ul>
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
	 * Adds a <code>key:String</code> pair to this object. If a previous value was
	 * associated with the specified identifier that value is overwritten.
	 * 
	 * @param identifier
	 * @param value
	 */
  public void addMember(String identifier, String value)
  {
    members.put(identifier, value);
  }
  
  
  /**
   * Adds a <code>key:long</code> pair to this object. If a previous value was
	 * associated with the specified identifier that value is overwritten.
   * @param identifier
   * @param value
   */
  public void addMember(String identifier, long value)
  {
  	members.put(identifier, value);
  }
  
  
  /**
   * Adds a <code>key:double</code> pair to this object. If a previous value was
	 * associated with the specified identifier that value is overwritten.
   * @param identifier
   * @param value
   */
  public void addMember(String identifier, double value)
  {
  	members.put(identifier, value);
  }
  
  
  /**
   * Adds a <code>key:boolean</code> pair to this object. If a previous value was
	 * associated with the specified identifier that value is overwritten.
   * @param identifier
   * @param value
   */
  public void addMember(String identifier, boolean value)
  {
  	members.put(identifier, value);
  }
  
  
  /**
   * Adds a <code>key:JSONObject</code> pair to this object. If a previous value was
	 * associated with the specified identifier that value is overwritten.
   * @param identifier
   * @param object
   */
  public void addMember(String identifier, JSONObject object)
  {
    if (object == this || (object != null && containsThis(object)))
      throw new IllegalArgumentException("A JSONObject cannot be added to itself!");
    
    members.put(identifier, object);
  }
  
  
  /**
	 * Adds a <code>key:Object[]</code> pair to this object. If a previous value was
	 * associated with the specified identifier that value is overwritten.
	 * 
	 * @param identifier
	 * @param array An array of objects. All elements in the array will be treated
	 *          as strings (using <code>toString()</code>), unless they are of
	 *          type <code>JSONObject</code>s or <code>Object[]</code>.
	 */
  public void addMember(String identifier, Object[] array)
  {
  	if (array != null && containsThis(array))
  		throw new IllegalArgumentException("A JSONObject cannot be added to itself!");
  	
    members.put(identifier, array);
  }
  
  
	/**
	 * Adds a <code>key:null</code> pair to this object. If a previous value was
	 * associated with the specified identifier that value is overwritten. This is
	 * effectively the same as calling <code>addMember(identifier, null)</code>.
	 * @param identifier 
	 */
  public void addNullMember(String identifier)
  {
  	members.put(identifier, null);
  }
  
  
  private boolean containsThis(Object[] array)
  {
  	for (Object object : array)
		{
  		if (object == this)
  			return true;
			if (containsThis(object))
				return true;
		}
  	
  	return false;
  }
  
  
  private boolean containsThis(JSONObject object)
  {
  	for (Object value : object.members.values())
  	{
  		if (value == this)
  			return true;
			if (containsThis(value))
				return true;
  	}
  	
  	return false;
  }


	private boolean containsThis(Object object)
	{
		if (object != null)
		{
			if (object.getClass().isArray())
			{
				if (containsThis((Object[])object))
					return true;
			}
			else if (object instanceof JSONObject)
			{
				if (containsThis((JSONObject)object))
					return true;
			}
		}
		
		return false;
	}
  
  
  
	/**
	 * Removes the member with the specified identifier. Since a member's value
	 * can be <code>null</code> it means that if <code>null</code> is returned, it
	 * can mean either that the member is <code>null</code> or does not exist. Use
	 * {@link #hasMember(String)} do distinguish between these two.
	 * 
	 * @param identifier The identifier for the member to remove.
	 * @return The value of the member that was removed, or <code>null</code> if
	 *         there was no such member.
	 */
  public Object removeMember(String identifier)
  {
  	return members.remove(identifier);
  }
  
  
  /**
   * Checks if the <code>JSONObject</code> contains a member with the specified identifier.
   * @param identifier The identifier for the member to remove.
   */
  public boolean hasMember(String identifier)
  {
  	return members.containsKey(identifier);
  }
  
  
  /**
	 * Checks if the specified member is <code>null</code> or not.
	 * 
	 * @param identifier
	 * @return <code>true</code> if and only if the specified member is
	 *         <code>null</code>. If the member has any other value or <i>does not
	 *         exist</i>, <code>false</code> is returned!
	 */
  public boolean isMemberNull(String identifier)
  {
  	if (!hasMember(identifier))
  		return false;
  	return members.get(identifier) == null;
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
      else if (pair.getValue() instanceof String)
      {
        if (index < object.members.size() - 1)
          stringBuilder.append(formatLine("\"" + pair.getKey() + "\":\"" + pair.getValue() + "\",", tabLevel + 2, minify));
        else
          stringBuilder.append(formatLine("\"" + pair.getKey() + "\":\"" + pair.getValue() + "\"", tabLevel + 2, minify));
      }
      else
      {
      	if (index < object.members.size() - 1)
      		stringBuilder.append(formatLine("\"" + pair.getKey() + "\":" + pair.getValue() + ",", tabLevel + 2, minify));
      	else
      		stringBuilder.append(formatLine("\"" + pair.getKey() + "\":" + pair.getValue(), tabLevel + 2, minify));
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
      else if (element instanceof String)
      {
        if (i < array.length - 1)
          stringBuilder.append(formatLine("\"" + element.toString() + "\",", tabLevel + 2, minify));
        else
          stringBuilder.append(formatLine("\"" + element.toString() + "\"", tabLevel + 2, minify));
      }
      else
      {
        if (i < array.length - 1)
          stringBuilder.append(formatLine("" + element + ",", tabLevel + 2, minify));
        else
          stringBuilder.append(formatLine("" + element + "", tabLevel + 2, minify));
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
  
  
  @Override
  public int hashCode()
  {
  	int prime = 31;
  	int hashCode = 1;
  	
  	for (Entry<String, Object> member : getMembers())
		{
  		Object value = member.getValue();
  		int valueHash;
  		
  		if (value == null)
  			valueHash = 0;
  		else if (value.getClass().isArray())
  			valueHash = Arrays.deepHashCode((Object[])value);
  		else
  			valueHash = value.hashCode();
  		
  		hashCode = prime * hashCode + valueHash;
		}
  	
  	return hashCode;
  }
  
  
  @Override
  public boolean equals(Object obj)
  {
  	if (obj == this)
  		return true;
  	if (obj == null)
  		return false;
  	if (getClass() != obj.getClass())
  		return false;
  	
  	JSONObject other = (JSONObject)obj;
  	
  	if (other.getMembers().size() != getMembers().size())
  		return false;
  	
  	for (Entry<String, Object> member : getMembers())
  	{
  		if (!other.hasMember(member.getKey()))
  			return false;
  		
  		Object value = member.getValue();
  		Object otherValue = other.getMemberByName(member.getKey());
  		
  		if (value == otherValue)
  			continue;
  		if (value == null || otherValue == null)
  			return false;
  		if (value.getClass() != otherValue.getClass())
  			return false;
  		
  		if (value.getClass().isArray())
  		{
  			if (!Arrays.deepEquals((Object[])value, (Object[])otherValue))
  				return false;
  		}
  		else if (!value.equals(otherValue))
  		{
  			return false;
  		}
  		
  	}
  	return true;
  }
}