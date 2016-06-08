package komposten.utilities.logging;

/**
 * A class that represent logging levels used by {@link Logger}.<br />
 * This class contains a set of default levels that can be used. If other levels
 * are needed, new levels can be created using the constructor (
 * {@link #Level(String)}).
 * 
 * @author Jakob Hjelm
 *
 */
public class Level
{
	private String name;
	
	
	/**
	 * Creates a new <code>Level</code> with the specified name.
	 * @param name The name of the <code>Level</code>.
	 */
	public Level(String name)
	{
		this.name = name;
	}
	
	
	public String getName()
	{
		return name;
	}
	

	public static final Level DEBUG = new Level("Debug");
	public static final Level INFO = new Level("Debug");
	public static final Level WARNING = new Level("Debug");
	public static final Level ERROR = new Level("Debug");
	public static final Level FATAL = new Level("Debug");
}

//public enum Level
//{
//	/** Messaged with this debug level are only printed if debug mode is enabled. */
//	Debug,
//	Info,
//	Warning,
//	Error,
//	Fatal,
//	/**
//	 * A <code>Level</code> that can be used if the 5 "named" levels are not
//	 * enough. Override <code>formatLogLevel()</code> in a custom
//	 * {@link LogFormatter} to replace "Custom1" with the preferred string.
//	 */
//	Custom1,
//	/**
//	 * A <code>Level</code> that can be used if the 5 "named" levels are not
//	 * enough. Override <code>formatLogLevel()</code> in a custom
//	 * {@link LogFormatter} to replace "Custom2" with the preferred string.
//	 */
//	Custom2,
//	/**
//	 * A <code>Level</code> that can be used if the 5 "named" levels are not
//	 * enough. Override <code>formatLogLevel()</code> in a custom
//	 * {@link LogFormatter} to replace "Custom3" with the preferred string.
//	 */
//	Custom3,
//}