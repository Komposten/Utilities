/*
 * Copyright (c) 2014 Jakob Hjelm 
 */
package komposten.utilities.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Map.Entry;

import komposten.utilities.tools.FileOperations;


/**
 * Reads a file structured as <code>key=value</code> and stores the info in a <code>HashMap</code>.
 */
public class KeyMap
{
  private Map<String, String> data_;
  

	/**
	 * @throws FileNotFoundException If the file does not exist.
	 */
	public KeyMap(String filePath) throws FileNotFoundException
	{
		this(new File(filePath));
	}


	/**
	 * @throws FileNotFoundException If the file does not exist.
	 */
	public KeyMap(File file) throws FileNotFoundException
	{
		data_ = FileOperations.loadConfigFile(file, false);
	}
  
  
  
  /**
   * @param key
   * @return The value related to the key, or null if no such key is found.
   */
  public String value(String key) { return data_.get(key); }
  /**
   * @param value
   * @return The key related to the value, or null if no such value is found.
   */
  public String key  (String value)
  {
    for (Entry<String, String> e : data_.entrySet())
    {
      if (e.getValue().equals(value))
        return e.getKey();
    }
    
    return null;
  }
}
