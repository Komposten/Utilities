/*
 * Copyright 2017 Jakob Hjelm
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
	public static final Level INFO = new Level("Info");
	public static final Level WARNING = new Level("Warning");
	public static final Level ERROR = new Level("Error");
	public static final Level FATAL = new Level("Fatal");
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