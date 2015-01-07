package komposten.utilities.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class JSONObject
{
  Map<String, Object> members;
  
  
  public JSONObject()
  {
    members = new HashMap<String, Object>();
  }
  
  
  
  public Object getMemberByName(String name)
  {
    return members.get(name);
  }
  
  
  
  public Set<Entry<String, Object>> getMembers()
  {
    return members.entrySet();
  }
  
  
  
  public void addStringPair(String identifier, String value)
  {
    members.put(identifier, value);
  }
  
  
  
  public void addObjectPair(String identifier, JSONObject object)
  {
    members.put(identifier, object);
  }
  
  
  
  public void addArrayPair(String identifier, Object[] array)
  {
    members.put(identifier, array);
  }

  
  
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
          stringBuilder.append(formatLine((String)element + ",", tabLevel + 2));
        else
          stringBuilder.append(formatLine((String)element, tabLevel + 2));
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