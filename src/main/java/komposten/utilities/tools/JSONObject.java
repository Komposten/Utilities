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
 * object"). The data can be converted to a readable string by calling
 * {@link #toMultiLineString()} for writing to files. To load existing JSON
 * files, see {@link JSONReader}.
 * 
 * @version <b>1.1.0</b> <br />
 *          <ul>
 *          <li>Added <code>removeElement()</code> and
 *          <code>hasElement()</code>.</li>
 *          </ul>
 *          <b>Older</b> <br />
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
	 * Converts the <code>JSONObject</code> to a readable, formatted string. This
	 * string can be saved to a file and a {@link JSONReader} can be used to
	 * re-create a <code>JSONObject</code> from it.
	 */
  public String toMultiLineString()
  {
    return toString(this, 0).replaceAll("\\s+,", ",\n").replaceAll("\\n+", "\n");
  }
  
  
  
  private String toString(JSONObject object, int tabLevel)
  {
    StringBuilder stringBuilder = new StringBuilder();
    
    stringBuilder.append(formatLine("{", tabLevel));
    
    int index = 0;
    
    for (Map.Entry<String, Object> pair : object.members.entrySet())
    {
      if (pair.getValue() instanceof Object[])
      {
        stringBuilder.append(formatLine("\"" + pair.getKey() + "\":", tabLevel + 2));
        stringBuilder.append(printArray((Object[])pair.getValue(), tabLevel + 2));

        if (index < object.members.size() - 1)
          stringBuilder.append(formatLine(",", tabLevel + 2));
      }
      else if (pair.getValue() instanceof JSONObject)
      {
        stringBuilder.append(formatLine("\"" + pair.getKey() + "\":", tabLevel + 2));
        stringBuilder.append(toString((JSONObject)pair.getValue(), tabLevel + 2));

        if (index < object.members.size() - 1)
          stringBuilder.append(formatLine(",", tabLevel + 2));
      }
      else
      {
        if (index < object.members.size() - 1)
          stringBuilder.append(formatLine("\"" + pair.getKey() + "\":\"" + pair.getValue() + "\",", tabLevel + 2));
        else
          stringBuilder.append(formatLine("\"" + pair.getKey() + "\":\"" + pair.getValue() + "\"", tabLevel + 2));
      }
      
      index++;
    }

    stringBuilder.append(formatLine("}", tabLevel));
    
    return stringBuilder.toString();
  }
  
  
  
  private String printArray(Object[] array, int tabLevel)
  {
    StringBuilder stringBuilder = new StringBuilder();
    
    stringBuilder.append(formatLine("[", tabLevel));
    
    for (int i = 0; i < array.length; i++)
    {
      Object element = array[i];
      
      if (element instanceof Object[])
      {
        stringBuilder.append(printArray((Object[])element, tabLevel + 2));
        
        if (i < array.length - 1)
          stringBuilder.append(formatLine(",", tabLevel + 2));
      }
      else if (element instanceof JSONObject)
      {
        stringBuilder.append(toString((JSONObject)element, tabLevel + 2));
        
        if (i < array.length - 1)
          stringBuilder.append(formatLine(",", tabLevel + 2));
      }
      else
      {
        if (i < array.length - 1)
          stringBuilder.append(formatLine("\"" + element.toString() + "\",", tabLevel + 2));
        else
          stringBuilder.append(formatLine("\"" + element.toString() + "\"", tabLevel + 2));
      }
    }

    stringBuilder.append(formatLine("]", tabLevel));
    
    return stringBuilder.toString();
  }
  
  
  
  private String formatLine(String line, int tabLevel)
  {
    if (tabLevel > 0)
      return String.format("%1$" + tabLevel + "s", "") + line + "\n";
    else
      return line + "\n";
  }
}