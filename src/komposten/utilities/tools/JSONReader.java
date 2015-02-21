package komposten.utilities.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JSONReader //TODO Identifiers and values should be enclosed within quotation marks ("string":"value" instead of string:value).
{
  public JSONObject readFile(String jsonFilePath)
  {
    return readFile(new File(jsonFilePath));
  }
  
  
  
  /**
   * @param jsonFile
   * @return A {@link JSONObject} representing the JSON data in the file, or
   *         <code>null</code> if the file doesn't exist/is a directory/
   *         is <code>null</code>.
   */
  public JSONObject readFile(File jsonFile)
  {
    if (jsonFile == null || !jsonFile.exists() || jsonFile.isDirectory())
      return null;
    
    JSONObject jsonObject = null;
    
    Scanner       scanner = null;
    StringBuilder builder = new StringBuilder();
    
    try
    {
      scanner = new Scanner(new BufferedReader(new FileReader(jsonFile)));
      
      while (scanner.hasNextLine())
        builder.append(scanner.nextLine().trim() + "\n");
    }
    catch (FileNotFoundException e)
    {
      //FIXME JSONReader; Exception handling
    }
    finally
    {
      if (scanner != null)
        scanner.close();
    }
    
    jsonObject = parseObject(builder.toString());
    
    return jsonObject;
  }
  
  
  
  private JSONObject parseObject(String jsonObject)
  {
    jsonObject = jsonObject.replaceAll("\\n+", " ").trim();
    
    if (jsonObject.startsWith("{"))
      jsonObject = jsonObject.replaceAll("^\\{", "").replaceAll("\\{$", "");

    JSONObject object = new JSONObject();
    
    boolean inString = false;

    for (int i = 0; i < jsonObject.length(); i++)
    {
      char currentChar = jsonObject.charAt(i);
      
      if (currentChar == '"')
        inString = !inString;
      if ((currentChar == ',' && !inString) || (i < jsonObject.indexOf(',') && currentChar != '{' && currentChar != '['))
      {
        int endIndex = findPairEnd(jsonObject, i);
        
        if (endIndex == -1)
          endIndex = jsonObject.length() - 1;

        Object[] pair = parsePair(jsonObject.substring(i, endIndex));
        
        object.members.put((String)pair[0], pair[1]);
        
        i = endIndex-1;
      }
    }
    
    return object;
  }
  
  
  
  private Object[] parsePair(String jsonPair)
  {
    try
    {
      Object[] pairData = new Object[2];
  
      jsonPair = jsonPair.replaceAll("^\\s*,\\s*", "");
      jsonPair = jsonPair.replaceAll("\\s*,\\s*$", "");
      
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
      else
      {
        pairData[1] = value.replace("\"", "");
      }
      
      return pairData;
    }
    catch (IndexOutOfBoundsException e)
    {
      System.err.println("Invalid JSON member: " + jsonPair);
      
      return new Object[2];
    }
  }
  
  
  
  private Object parseElement(String jsonArrayElement)
  {
    Object element;

    jsonArrayElement = jsonArrayElement.replaceAll("^\\s*,\\s*", "");
    jsonArrayElement = jsonArrayElement.replaceAll("\\s*,\\s*$", "");
    
    if (jsonArrayElement.startsWith("["))
    {
      element = parseArray(jsonArrayElement);
    }
    else if (jsonArrayElement.startsWith("{"))
    {
      element = parseObject(jsonArrayElement);
    }
    else
    {
      element = jsonArrayElement;
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
    
    for (int i = elementStart+1; i < jsonObject.length(); i++)
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
  
  
  
  private Object[] parseArray(String jsonArray)
  {
    List<Object> arrayList = new ArrayList<Object>();

    for (int i = 0; i < jsonArray.length(); i++)
    {
      char currentChar = jsonArray.charAt(i);
      
      if (currentChar == ',' || (i < jsonArray.indexOf(',') && currentChar != '{' && currentChar != '['))
      {
        int endIndex = findPairEnd(jsonArray, i);
        
        if (endIndex == -1)
          endIndex = jsonArray.length() - 1;

        Object element = parseElement(jsonArray.substring(i, endIndex));
        
        arrayList.add(element);
        
        i = endIndex-1;
      }
    }
    
    return arrayList.toArray();
  }
  
  
  
  public static void main(String[] args)
  {
    JSONReader reader = new JSONReader();
    
    JSONObject object = reader.readFile("resources/json/schedule.json");
    
    System.out.println("----------------------------");
    System.out.println(object.toMultiLineString());
    
    object = new JSONObject();
    object.addObjectPair("me", object);
    System.out.println(object.toMultiLineString());
  }
}