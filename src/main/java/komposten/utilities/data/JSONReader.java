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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import komposten.utilities.tools.MathOps;


//CURRENT JSONReader; Split JSONReader into JSONReader and JSONParser, and update the unit tests. 
//FIXME JSONReader; Throw exceptions if the code JSON is broken (e.g. an object or array is never closed).
//FIXME JSONReader; Keep an int that keeps track of the current cursor position so that it can be known where errors occur.
/**
 * JSONReader is a tool that reads files with a JSON-format ("JavaScript Object
 * Notation") and creates {@link JSONObject}s from them.
 * 
 * @version <b>1.2.0</b> <br />
 *          <ul>
 *          <li>Added readString(String)</li>
 *          </ul>
 *          <b>Older</b> <br />
 *          1.1.0 <br />
 *          <ul>
 *          <li>Re-factored <code>parseObject()</code> and
 *          <code>parseArray()</code>. The old version was overly complex, and
 *          couldn't handle all scenarios.</li>
 *          </ul>
 *          1.0.0 <br />
 *          <ul>
 *          <li>Initial version, reading of JSON files and retrieval of
 *          members.</li>
 *          </ul>
 * 
 * @author Jakob Hjelm
 *
 */
public class JSONReader
{
	/**
	 * Reads the provided JSON-formatted file and stores its data in a
	 * {@link JSONObject}.
   * The system's default charset is assumed when reading the file.
	 * 
	 * @return A <code>JSONObject</code> representing the JSON data in the file,
	 *         or <code>null</code> if the file at <code>jsonFilePath</code> does
	 *         not exist or is a folder.
	 */
	public JSONObject readFile(String jsonFilePath)
	{
		return readFile(new File(jsonFilePath));
	}
	
	
	public JSONObject readFile(String jsonFilePath, Charset charset)
	{
		return readFile(new File(jsonFilePath), charset);
	}
	

  /**
   * Reads the provided JSON-formatted file and stores its data in a
   * {@link JSONObject}.
   * The system's default charset is assumed when reading the file.
   * @return A <code>JSONObject</code> representing the JSON data in the file, or
   *         <code>null</code> if the file does not exist, is a directory or
   *         is <code>null</code>.
   */
	public JSONObject readFile(File jsonFile) {
		return readFile(jsonFile, Charset.defaultCharset());
	}

  
  /**
   * Reads the provided JSON-formatted file and stores its data in a {@link JSONObject}.
   * @return A <code>JSONObject</code> representing the JSON data in the file, or
   *         <code>null</code> if the file does not exist, is a directory or
   *         is <code>null</code>.
   */
  public JSONObject readFile(File jsonFile, Charset charset)
  {
    if (jsonFile == null || !jsonFile.exists() || jsonFile.isDirectory())
      return null;
    
    JSONObject jsonObject = null;
    Scanner       scanner = null;
    StringBuilder builder = new StringBuilder();
    
    try
    {
      scanner = new Scanner(new BufferedReader(new FileReader(jsonFile, charset)));
      
      while (scanner.hasNextLine())
        builder.append(scanner.nextLine().trim());
      
      jsonObject = readString(builder.toString());
    }
    catch (IOException e)
    {
    	System.err.println("Could not find or read the file \"" + jsonFile + "\", returning an empty object!");
    	jsonObject = new JSONObject();
    }
    finally
    {
      if (scanner != null)
        scanner.close();
    }
    
    return jsonObject;
  }
  
  
  /**
   * Reads the provided JSON-formatted string and stores its data in a {@link JSONObject}.
   * @param jsonString A string containing a JSON object.
   * @return A <code>JSONObject</code> representing the JSON data in the string, or
   *         <code>null</code> if the string is <code>null</code>.
   */
  public JSONObject readString(String jsonString)
  {
  	if (jsonString == null)
  		return null;
  	
  	return parseObject(jsonString);
  }
  
  
  
  private JSONObject parseObject(String jsonObject)
  {
    jsonObject = jsonObject.replaceAll("\\n+", " ").trim(); //Remove all new-lines.
    
    if (jsonObject.startsWith("{"))
    {
    	jsonObject = jsonObject.replaceAll("^\\{", "").replaceAll("\\}$", "").trim(); //Remove leading and trailing curly braces.
    }

    JSONObject object = new JSONObject();
    
    int i = 0;
    while (i < jsonObject.length())
    {
    	int end = findPairEnd(jsonObject, i);
    	
    	if (end < 0)
    	{
    		//Couldn't find a comma, so we should be at the end of the string.
    		String substring = jsonObject.substring(i);
    		
    		if (!substring.matches("\\s*"))
    		{
    			Object[] pair = parsePair(substring);
          
          object.members.put((String)pair[0], pair[1]);
    		}
    		break;
    	}
    	else
    	{
    		//Found a comma (i.e. the end of a key:value-pair!
        Object[] pair = parsePair(jsonObject.substring(i, end));
        
        object.members.put((String)pair[0], pair[1]);
        
        i = end;
    	}
    }
    
    return object;
  }
  
  
  
  private Object[] parsePair(String jsonPair)
  {
  	Object[] pairData = new Object[2];

  	jsonPair = jsonPair.replaceAll("^\\s*,?\\s*", "");
  	jsonPair = jsonPair.replaceAll("\\s*,?\\s*$", "");

  	if (!jsonPair.matches("\"[^:\"]+\"\\s*:\\s*.+"))
  		throw new IllegalArgumentException("'" + jsonPair + "' is not a valid key:value pair!");

  	String   identifier = jsonPair.substring(0, jsonPair.indexOf(':')).trim();
  	String   value      = jsonPair.substring(jsonPair.indexOf(':')+1).trim();

  	pairData[0] = identifier.replace("\"", "");

  	if (value.startsWith("["))
  	{
  		pairData[1] = parseArray(value);
  	}
  	else if (value.startsWith("{"))
  	{
  		pairData[1] = parseObject(value);
  	}
  	else if (value.startsWith("\""))
  	{
  		pairData[1] = value.replace("\"", "");
  	}
  	else
  	{
  		pairData[1] = parsePrimitiveValue(value);
  	}

  	return pairData;
  }


	private Object parsePrimitiveValue(String value)
	{
		if (isLong(value))
		{
			return Long.parseLong(value);
		}
		else if (isDouble(value))
		{
			return Double.parseDouble(value);
		}
		else if (isBoolean(value))
		{
			return Boolean.parseBoolean(value);
		}
		else if (isNull(value))
		{
			return null;
		}
		else
		{
			throw new IllegalArgumentException("The value '" + value + "' is not a Json object, array, string, number, boolean or null!");
		}
	}
  
  
  private boolean isLong(String value)
  {
  	return value.matches("[+-]?\\d+[lL]?");
  }
  
  
  private boolean isDouble(String value)
  {
  	return MathOps.isDouble(value);
  }
  
  
  private boolean isBoolean(String value)
  {
  	return value.matches("(true|false)");
  }
  
  
  private boolean isNull(String value)
  {
  	return value.toLowerCase().equals("null");
  }
  
  
  
  private Object[] parseArray(String jsonArray)
  {
    List<Object> arrayList = new ArrayList<Object>();
    
    jsonArray = jsonArray.trim();
    if (jsonArray.startsWith("["))
    {
    	jsonArray = jsonArray.replaceAll("^\\[", "").replaceAll("\\]$", "").trim(); //Remove leading and trailing brackets.
    }
    
    int i = 0;
    while (i < jsonArray.length())
    {
    	int end = findPairEnd(jsonArray, i);

    	if (end < 0)
    	{
    		//Couldn't find a comma, so we should be at the end of the string.
    		String substring = jsonArray.substring(i);
    		
    		if (!substring.matches("\\s*"))
    		{
    			Object element = parseArrayElement(substring);
          
          arrayList.add(element);
    		}
    		break;
    	}
    	else
    	{
    		//Found a comma (i.e. the end of a value)!
        Object element = parseArrayElement(jsonArray.substring(i, end));
        
        arrayList.add(element);
        
        i = end;
    	}
    }
    
    return arrayList.toArray();
  }
  
  
  
  private Object parseArrayElement(String jsonArrayElement)
  {
    Object element;

    jsonArrayElement = jsonArrayElement.replaceAll("^\\s*,\\s*", ""); //Remove leading comma and whitespace.
    jsonArrayElement = jsonArrayElement.replaceAll("\\s*,\\s*$", ""); //Remove trailing comma and whitespace.
    jsonArrayElement = jsonArrayElement.trim();
    
    if (jsonArrayElement.startsWith("["))
    {
      element = parseArray(jsonArrayElement);
    }
    else if (jsonArrayElement.startsWith("{"))
    {
      element = parseObject(jsonArrayElement);
    }
    else if (jsonArrayElement.startsWith("\""))
    {
      element = jsonArrayElement.replace("\"", "");
    }
    else
    {
    	element = parsePrimitiveValue(jsonArrayElement);
    }

    return element;
  }
  
  
  
  /**
   * Searches for the end of the element (e.g. a JSON array) that starts at <code>elementStart</code> and returns its index.
   * @param jsonObject The jsonObject containing the element.
   * @param elementStart The index of the element's opening character.
   * @return The index of the element's closing character, or <code>-1</code> if the element is never closed.
   */
  private int findElementEnd(String jsonObject, int elementStart, char openingCharacter, char closingCharacter)
  {
    int levels = 0;
    
    for (int i = elementStart; i < jsonObject.length(); i++)
    {
      char currentChar = jsonObject.charAt(i);
      
      if (currentChar == openingCharacter)
      {
        levels++;
      }
      else if (currentChar == closingCharacter)
      {
        levels--;
      }
      
      if (levels == 0)
        return i;
    }
    
    return -1;
  }
  
  
  
  /**
   * Searches for the end of the element (e.g. a JSON array) that starts at <code>elementStart</code> and returns its index.
   * @param jsonObject The jsonObject containing the element.
   * @param elementStart The index of the element's opening character.
   * @return The index of the element's closing character, or <code>-1</code> if the element is never closed.
   */
  private int findPairEnd(String jsonObject, int elementStart)
  {
    int arrayLevels  = 0;
    int objectLevels = 0;
    boolean inString = false;
    
    if (jsonObject.charAt(elementStart) == ',')
    {
    	elementStart++;
    }
    
    for (int i = elementStart; i < jsonObject.length(); i++)
    {
      char currentChar = jsonObject.charAt(i);

      if (currentChar == '[')
      {
        arrayLevels++;
      }
      else if (currentChar == ']')
      {
        arrayLevels--;
      }
      else if (currentChar == '{')
      {
        objectLevels++;
      }
      else if (currentChar == '}')
      {
        objectLevels--;
      }
      else if (currentChar == '"')
      {
        inString = !inString;
      }
      else if (currentChar == ',' && arrayLevels == 0 && objectLevels == 0 && !inString)
        return i;
    }
    
    return -1;
  }
}