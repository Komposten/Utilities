package komposten.utilities.logging;

import java.io.IOException;
import java.io.OutputStream;

import komposten.utilities.exceptions.InvalidStateException;


/**
 * A convenience class to statically log messages using a {@link Logger}.
 * @author Komposten (Jakob Hjelm)
 */
public class LogUtils
{
  private static Logger logger_;
  
  
  
  /**
   * @return Whether or not the <code>Logger</code> used by this class has been initialised.
   * @see #writeToFile(String)
   * @see #writeToStream(OutputStream)
   */
  public static boolean hasInitialised()
  {
    return logger_ != null;
  }
  
  
  
  /**
   * Sets <code>LogUtils</code> to write to the specified file. Also initialises the logger if needed.
   * @see Logger#writeToFile(String)
   * @see LogUtils#writeToStream(OutputStream)
   */
  public static void writeToFile(String path)
  {
    if (logger_ == null)
      logger_ = new Logger(path);
    else
      logger_.writeToFile(path);
  }
  
  

  /**
   * Sets <code>LogUtils</code> to write to the specified stream. Also initialises the logger if needed.
   * @see Logger#writeToStream(OutputStream)
   * @see LogUtils#writeToFile(String)
   */
  public static void writeToStream(OutputStream stream)
  {
    if (logger_ == null)
      logger_ = new Logger(stream);
    else
      logger_.writeToStream(stream);
  }
  
  

  /**
	 * See {@link Logger#log(Level, String)}.
	 * @throws IOException If writing to a file and the file could not be created or if access to the file was denied, or if an error occurred while writing.
	 * @throws InvalidStateException If the output file or stream has not been set (see {@link #writeToFile(String)} and {@link #writeToStream(OutputStream)}).
	 */
	public static boolean log(Level logLevel, String message) throws IOException
	{
	  if (logger_ == null)
	    throw new InvalidStateException("Must call LogUtils.writeToFile() or LogUtils.writeToStream() before logging!");
	  return logger_.log(logLevel, message);
	}
  
  

  /**
	 * See {@link Logger#log(Level, String, String)}.
	 * @throws IOException If writing to a file and the file could not be created or if access to the file was denied, or if an error occurred while writing.
	 * @throws InvalidStateException If the output file or stream has not been set (see {@link #writeToFile(String)} and {@link #writeToStream(OutputStream)}).
	 */
	public static boolean log(Level logLevel, String location, String message) throws IOException
	{
	  if (logger_ == null)
	    throw new InvalidStateException("Must call LogUtils.writeToFile() or LogUtils.writeToStream() before logging!");
	  return logger_.log(logLevel, location, message);
	}



	/**
   * See {@link Logger#log(Level, String, String, Throwable, boolean)}.
	 * @throws IOException If writing to a file and the file could not be created or if access to the file was denied, or if an error occurred while writing.
   * @throws InvalidStateException If the output file or stream has not been set (see {@link #writeToFile(String)} and {@link #writeToStream(OutputStream)}).
   */
  public static boolean log(Level logLevel, String className, String errorMsg, Throwable t, boolean includeStackTrace) throws IOException
  {
    if (logger_ == null)
      throw new InvalidStateException("Must call LogUtils.writeToFile() or LogUtils.writeToStream() before logging!");
    return logger_.log(logLevel, className, errorMsg, t, includeStackTrace);
  }
  
  

  /**
   * Same as {@link #log(Level, String)}, but prints <code>IOExceptions</code>, thrown while writing, to the standard error stream instead of throwing them.
	 * See {@link Logger#log(Level, String)}.
	 * @throws InvalidStateException If the output file or stream has not been set (see {@link #writeToFile(String)} and {@link #writeToStream(OutputStream)}).
	 */
	public static boolean logSilent(Level logLevel, String message)
	{
	  try
		{
			return log(logLevel, message);
		}
		catch (IOException e)
		{
			System.err.println("An unexpected exception occurred while logging:");
			e.printStackTrace();
			return false;
		}
	}
  
  

  /**
   * Same as {@link #log(Level, String, String)}, but prints <code>IOExceptions</code>, thrown while writing, to the standard error stream instead of throwing them.
	 * See {@link Logger#log(Level, String, String)}.
	 * @throws InvalidStateException If the output file or stream has not been set (see {@link #writeToFile(String)} and {@link #writeToStream(OutputStream)}).
	 */
	public static boolean logSilent(Level logLevel, String location, String message)
	{
	  try
		{
			return log(logLevel, location, message);
		}
		catch (IOException e)
		{
			System.err.println("An unexpected exception occurred while logging:");
			e.printStackTrace();
			return false;
		}
	}



	/**
   * Same as {@link #log(Level, String, String, Throwable, boolean)}, but prints <code>IOExceptions</code>, thrown while writing, to the standard error stream instead of throwing them.
   * See {@link Logger#log(Level, String, String, Throwable, boolean)}.
   * @throws InvalidStateException If the output file or stream has not been set (see {@link #writeToFile(String)} and {@link #writeToStream(OutputStream)}).
   */
  public static boolean logSilent(Level logLevel, String className, String errorMsg, Throwable t, boolean includeStackTrace)
  {
    try
		{
			return log(logLevel, className, errorMsg, t, includeStackTrace);
		}
		catch (IOException e)
		{
			System.err.println("An unexpected exception occurred while logging:");
			e.printStackTrace();
			return false;
		}
  }
}
