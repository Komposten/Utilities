package komposten.utilities.logging;

import java.io.OutputStream;

import komposten.utilities.exceptions.InvalidStateException;
import komposten.utilities.logging.Logger.Level;


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
	 * @throws InvalidStateException If the output file or stream has not been set (see {@link #writeToFile(String)} and {@link #writeToStream(OutputStream)}).
	 */
	public static boolean log(Level logLevel, String message)
	{
	  if (logger_ == null)
	    throw new InvalidStateException("Must call LogUtils.writeToFile() or LogUtils.writeToStream() before logging!");
	  return logger_.log(logLevel, message);
	}
  
  

  /**
	 * See {@link Logger#log(Level, String, String)}.
	 * @throws InvalidStateException If the output file or stream has not been set (see {@link #writeToFile(String)} and {@link #writeToStream(OutputStream)}).
	 */
	public static boolean log(Level logLevel, String location, String message)
	{
	  if (logger_ == null)
	    throw new InvalidStateException("Must call LogUtils.writeToFile() or LogUtils.writeToStream() before logging!");
	  return logger_.log(logLevel, location, message);
	}



	/**
   * See {@link Logger#log(Level, String, String, Exception, boolean)}.
   * @throws InvalidStateException If the output file or stream has not been set (see {@link #writeToFile(String)} and {@link #writeToStream(OutputStream)}).
   */
  public static boolean log(Level logLevel, String className, String errorMsg, Exception e, boolean includeStackTrace)
  {
    if (logger_ == null)
      throw new InvalidStateException("Must call LogUtils.writeToFile() or LogUtils.writeToStream() before logging!");
    return logger_.log(logLevel, className, errorMsg, e, includeStackTrace);
  }
}