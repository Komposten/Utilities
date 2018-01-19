package komposten.utilities.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import komposten.utilities.tools.FileOperations;


/**
 * This class represents a key-value table for storing program settings (similar
 * to Java's {@link Properties}).<br />
 * It is possible to listen to changes to either all or specific settings using
 * {@link SettingChangeListener SettingChangeListeners}. <br />
 * <br />
 * 
 * @version <b>1.2.0</b> <br />
 *          <ul>
 *          <li>Added {@link #getInt(String, int)}.</li>
 *          <li>Added {@link #getFloat(String, float)}.</li>
 *          <li>Added {@link #getDouble(String, double)}.</li>
 *          </ul>
 *          <b>Older</b> <br />
 *          1.1.0 <br />
 *          <ul>
 *          <li>Added {@link #get(String, String)}.</li>
 *          <li>Added {@link #getBoolean(String, boolean)}.</li>
 *          <li>Added {@link #Settings(String, char)}.</li>
 *          <li>Added {@link #Settings(File, char)}.</li>
 *          </ul>
 *          1.0.0 <br />
 *          <ul>
 *          <li>Added the class to the utilities package. It already contained
 *          {@link #get(String)}, {@link #getArray(String)},
 *          {@link #getBoolean(String)}, {@link #set(String, String)},
 *          {@link #set(String, String[])} and
 *          {@link #addListener(SettingChangeListener, String...)}.</li>
 *          </ul>
 * @author Jakob Hjelm
 *
 */
public class Settings
{
	private File file;
	private Map<String, String> data;
	private HashMap<String, ArrayList<SettingChangeListener>> listeners;
	private ArrayList<SettingChangeListener> globalListeners;
	
	private char arrayElementSeparator = ',';

	
	/**
	 * @param settingsFilePath The path to the settings file to use. If the file does not exist, an empty Settings object will be created.
	 * @param arrayElementSeparator The character that is used to separate array elements in the Settings file (',' is default).
	 */
	public Settings(String settingsFilePath, char arrayElementSeparator)
	{
		this(new File(settingsFilePath), arrayElementSeparator);
	}

	
	public Settings(String settingsFilePath)
	{
		this(new File(settingsFilePath));
	}

	
	/**
	 * @param settingsFile The settings file to use. If the file does not exist, an empty Settings object will be created.
	 * @param arrayElementSeparator The character that is used to separate array elements in the Settings file (',' is default).
	 */
	public Settings(File settingsFile, char arrayElementSeparator)
	{
		this(settingsFile);
		this.arrayElementSeparator = arrayElementSeparator;
	}

	
	public Settings(File file)
	{
		this.file = file;
		try
		{
			data = FileOperations.loadConfigFile(file, false);
		}
		catch (FileNotFoundException e)
		{
			data = new HashMap<String, String>();
		}
		listeners = new HashMap<String, ArrayList<SettingChangeListener>>();
		globalListeners = new ArrayList<SettingChangeListener>();
	}
	

	/**
	 * Adds a {@link SettingChangeListener} that is called whenever one of the
	 * settings in <code>settingsToListenTo</code> is changed.<br />
	 * <b>Note:</b> If <code>settingsToListenTo</code> is empty, the listener will
	 * be called for all settings!
	 * 
	 * @param listener
	 * @param settingsToListenTo The settings that trigger the listener when
	 *          changed. If no settings are provided, the listener is triggered
	 *          for all settings.
	 */
	public void addListener(SettingChangeListener listener, String... settingsToListenTo)
	{
		if (settingsToListenTo.length == 0)
		{
			globalListeners.add(listener);
		}
		else
		{
			for (String string : settingsToListenTo)
				addListener(listener, string);
		}
	}
	
	
	private void addListener(SettingChangeListener listener, String setting)
	{
		ArrayList<SettingChangeListener> list = listeners.get(setting);
		
		if (list != null)
		{
			list.add(listener);
		}
		else
		{
			list = new ArrayList<SettingChangeListener>();
			list.add(listener);
			listeners.put(setting, list);
		}
	}
	

	/**
	 * Finds a setting value mapped to <code>key</code>.
	 * 
	 * @param key
	 * @param defaultValue
	 * @return The value mapped to <code>key</code>, or <code>defaultValue</code>
	 *         if no value is found (or the value is an empty string).
	 */
	public String get(String key, String defaultValue)
	{
		String value = get(key);
		
		if (value == null || value.equals(""))
			return defaultValue;
		return value;
	}

	
	public String get(String key)
	{
		return data.get(key);
	}
	
	
	/**
	 * Finds a boolean setting value mapped to <code>key</code>.
	 * 
	 * @param key
	 * @param defaultValue
	 * @return The boolean equivalent of the value mapped to <code>key</code>, or <code>defaultValue</code>
	 *         if no value is found (or the value is an empty string).
	 */
	public boolean getBoolean(String key, boolean defaultValue)
	{
		String value = get(key);
		
		if (value == null || value.equals(""))
			return defaultValue;
		return value.equals(Boolean.TRUE.toString());
	}
	
	
	/**
	 * @return <code>true</code> if the value stored under <code>key</code> is
	 *         equal to <code>true</code>, false otherwise.
	 */
	public boolean getBoolean(String key)
	{
		return getBoolean(key, false);
	}
	
	
	/**
	 * Finds an integer setting value mapped to <code>key</code>.
	 * 
	 * @return The <code>int</code> equivalent of the value mapped to <code>key</code>, or <code>defaultValue</code>
	 *         if no valid value is found.
	 */
	public int getInt(String key, int defaultValue)
	{
		String value = get(key);
		
		if (value == null || !value.matches("\\d+"))
			return defaultValue;
		return Integer.parseInt(value);
	}
	
	
	/**
	 * Finds a float setting value mapped to <code>key</code>.
	 * 
	 * @return The <code>float</code> equivalent of the value mapped to <code>key</code>, or <code>defaultValue</code>
	 *         if no valid value is found.
	 */
	public float getFloat(String key, float defaultValue)
	{
		String value = get(key);
		
		if (value == null)
			return defaultValue;
		
		try
		{
			float floatValue = Float.parseFloat(value);
			
			if (Float.isFinite(floatValue))
				return floatValue;
		}
		catch (NumberFormatException e) { }
		
		return defaultValue;
	}
	
	
	/**
	 * Finds a float setting value mapped to <code>key</code>.
	 * 
	 * @return The <code>float</code> equivalent of the value mapped to <code>key</code>, or <code>defaultValue</code>
	 *         if no valid value is found.
	 */
	public double getDouble(String key, double defaultValue)
	{
		String value = get(key);
		
		if (value == null)
			return defaultValue;
		
		try
		{
			double doubleValue = Double.parseDouble(value);
			
			if (Double.isFinite(doubleValue))
				return doubleValue;
		}
		catch (NumberFormatException e) { }
		
		return defaultValue;
	}
	
	
	
	/**
	 * @return the value mapped to <code>key</code> as an array by
	 *         {@link String#split(String) splitting} at <code>\\s*[arrayElementSeparator]\\s*</code>, or
	 *         <code>null</code> if no value was found.
	 */
	public String[] getArray(String key)
	{
		String value = data.get(key);
		
		if (value != null)
			return value.split("\\s*" + arrayElementSeparator + "\\s*");
		return null;
	}
	
	
	public void set(String key, String[] values)
	{
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < values.length; i++)
		{
			String value = values[i];
			
			if (i == 0)
				builder.append(value);
			else
				builder.append(arrayElementSeparator + " " + value);
		}
		
		data.put(key, builder.toString());
		notifyListeners(key, values);
	}


	public void set(String key, String value)
	{
		data.put(key, value);
		notifyListeners(key, value);
	}
	
	
	/**
	 * @throws IOException If an exception occurred while creating or writing to the file.
	 */
	public void saveToFile() throws IOException
	{
		FileOperations.createFileOrFolder(file, false);
		
		StringBuilder builder = new StringBuilder();
		
		for (Entry<String, String> entry : data.entrySet())
		{
			String key = entry.getKey();
			String value = entry.getValue();
			
			builder.append(key + "=" + value + "\n");
		}
		
		FileOperations fileOperations = new FileOperations();
		fileOperations.printData(file, builder.toString(), false, false);
	}
	
	
	private void notifyListeners(String key, Object value)
	{
		ArrayList<SettingChangeListener> list = listeners.get(key);
		
		if (list != null)
		{
			for (SettingChangeListener listener : list)
				listener.settingChanged(key, value, this);
		}
		
		for (SettingChangeListener listener : globalListeners)
			listener.settingChanged(key, value, this);
	}
	
	
	public static interface SettingChangeListener
	{
		/**
		 * Called when a setting this listener subscribes to (see {@link Settings#addListener(SettingChangeListener, String...)}) is changed.
		 * @param settingKey The key of the setting that changed.
		 * @param value The new value for the setting. The value type depends on which <code>Settings.set</code> method was used.
		 * @param settings A reference to the {@link Settings} object that sent the event.
		 */
		public void settingChanged(String settingKey, Object value, Settings settings);
	}
}
