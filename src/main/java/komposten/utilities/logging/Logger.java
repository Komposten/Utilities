/*
 * Copyright (c) 2014 Jakob Hjelm 
 */
package komposten.utilities.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URLDecoder;
import java.security.ProtectionDomain;
import java.util.Calendar;



/**
 * A utility class for logging messages and exceptions to either a file or a stream.
 * @see {@link LogUtils}
 * @author Jakob Hjelm
 * @version
 * <b>1.5.2</b> <br />
 * <ul>
 * <li>Removed the exception throwing introduced in the previous version.</li>
 * <li>All internal exceptions are now passed to an <code>ExceptionHandler</code>.</li>
 * <li>Added <code>setExceptionHandler(ExceptionHandler)</code>.</li>
 * <li><code>getProgramDir()</code> now uses default instances of ExceptionHandler for error messages rather than <code>JOptionPane</code>.</li>
 * </ul>
 * <b>Older</b> <br />
 * 1.5.1 <br />
 * <ul>
 * <li><code>write(String, String)</code> now throws exceptions instead of catching them.</li>
 * <li>All <code>log()</code>-methods now throw exceptions thrown by <code>write(String, String)</code>.</li>
 * </ul>
 * 1.5.0 <br />
 * <ul>
 * <li>Split <code>write(String, String, OutputStream)</code> into two methods, one for the file and one for the stream.</li>
 * <li>Added a LogFormatter to format the output. Also added setFormatter().</li>
 * <li>Removed the default error messages.</li>
 * <li>Moved to <code>komposten.utilities.logging</code>.</li>
 * <li>Added <code>log(Level, String, String)</code>. </li>
 * <li>Removed <code>logMsg(String)</code> and renamed <code>logMsg(Level, String)</code> to <code>log(Level, String)</code>.</li>
 * <li>API change: Replaced the "error type" strings with a log level.</li>
 * <li>Added log levels (as a separate class, {@link Level}).</li>
 * <li>API change: Removed the deprecated log()-method.</li>
 * <li>Renamed <code>className</code> to <code>location</code>.</li>
 * </ul>
 * 1.4.0 <br />
 * <ul>
 * <li>API change: <code>exceptionMessageOnly</code> has been replaced by <code>includeStackTrace</code>.</li>
 * <li>Added <code>@deprecated</code> tag to {@link #log(String, String, Throwable, boolean)}.</li>
 * </ul>
 * 1.3.2 <br />
 * <ul>
 * <li>Updated <code>Logger.getProgramDir()</code> to check if the <code>ProtectionDomain</code> is null to prevent <code>NullPointerException</code>s when loading the class.</li>
 * <li>Added null checks to <code>Logger.Logger(String)</code> and <code>Logger.writeToFile(String)</code>.</li>
 * </ul>
 * 1.3.1 <br />
 * <ul>
 * <li>Replaced <code>Exception</code> with <code>Throwable</code>.</li>
 * <li>Added support for newlines in the exception message.</li>
 * <li>The first line of each cause is now always written.</li>
 * </ul> <br />
 * 1.3.0 <br />
 * <ul>
 * <li>Removed <code>static</code> from all log methods.</li>
 * <li>Added support for changing log file, or to use a stream.</li>
 * <li>Added <code>write(String, String, OutputStream)</code>.</li>
 * <li>Added <code>writeToFile(String)</code> and <code>writeToStream(OutputStream)</code>.</li>
 * <li>Added <code>dispose()</code>.</li>
 * <li>Updated JavaDoc to match the new system.</li>
 * </ul>
 * 1.2.0 <br />
 * <ul>
 * <li>Added <code>logMsg(String)</code> and <code>logMsg(String, String)</code>.</li>
 * </ul>
 * 1.1.0 <br />
 * <ul>
 * <li>Added a <code>log()</code> method with a <code>className</code> parameter.</li>
 * <li>Marked old<code>log()</code> as deprecated.</li>
 * </ul>
 * 1.0.2 <br />
 * <ul>
 * <li>Improved exception handling.</li>
 * </ul>
 * 1.0.1 <br />
 * <ul>
 * <li>Removed a call to <code>log()</code> from the catch clause in <code>getProgramDir()</code>.</li>
 * </ul>
 * 1.0.0  <br />
 * <ul>
 * <li>Updated the look of the logged data.</li>
 * </ul>
 * 0.5.0  <br />
 * <ul>
 * <li>Created the class, the <code>log()</code> method and the error constants.</li>
 * <li>Added support for nested throwables.</li>
 * </ul>
 */
public final class Logger //TODO Logger: Make it possible to print to different files/streams depending on log level. (Explicitly set the file/stream for the given (VarArg) levels, or use int values like java.util.Level?)
{
  /**
   * The path to the default log file utilised by this class. <br />
   * <b>Note:</b> This path is determined based on the program's installation
   * directory using {@link Class#getProtectionDomain()}, therefore the path may
   * be <code>null</code> on some systems (e.g. Android).
   */
	public static final String FILEPATH;
	
	private LogFormatter formatter;
	private ExceptionHandler handler;
	private String filePath_; //TODO Logger; Create a "LogOutput/Handler" class with two sub-classes, "FileLogOutput" and "StreamLogOutput". Then replace filePath and stream with a single LogOutput field (can be set via writeToFile, writeToStream, or writeTo).
	private OutputStream stream_;
	
	
	static
	{
	  String programDir = getProgramDir();
	  
	  if (programDir != null && programDir.length() > 0)
	    FILEPATH = programDir + File.separator + "log.txt";
	  else
	    FILEPATH = null;
	}
	
	
	
	private static String getProgramDir()
	{
		try
		{
		  ProtectionDomain domain = Logger.class.getProtectionDomain();
		  
		  if (domain == null)
		    return "";
		  
	    String path = domain.getCodeSource().getLocation().getPath();
    
      path = URLDecoder.decode(path, "UTF-8");
      path = new File(path).getParent();
      
      return path;
    }
    catch (IOException e)
    {
      String msg = "An unexpected exception ocurred while reading the program's path:";
      (new ExceptionHandler()).handleException(msg, e); 
    }
		catch (SecurityException e)
		{
      String msg = "An unexpected exception ocurred while reading the program's path:";
      (new ExceptionHandler()).handleException(msg, e); 
		}
		
		return null;
	}
  
  
	
	{
    formatter = new DefaultLogFormatter();
    handler = new ExceptionHandler();
	}

  /**
   * Creates a new Logger that writes to the specified file. Use {@link #FILEPATH} for the default file.
   * @param filePath The path to the file the Logger writes to.
   */
  public Logger(String filePath)
  {
    writeToFile(filePath);
  }
  
  /**
   * Creates a new Logger that writes to the specified stream.
   * <br /><b>Note:</b> The stream will not be closed until {@link #closeStream()} is invoked,
   * or the stream is closed manually.
   * @param stream The stream to write to.
   */
  public Logger(OutputStream stream)
  {
    writeToStream(stream);
  }
  
  
  
	/**
	 * Sets the {@link LogFormatter} used to format the output from this logger. A
	 * {@link DefaultLogFormatter} is used by default.
	 * 
	 * @param formatter The new <code>LogFormatter</code> to use.
	 */
  public void setFormatter(LogFormatter formatter)
  {
  	this.formatter = formatter;
  }
  
  
  public void setExceptionHandler(ExceptionHandler handler)
  {
  	this.handler = handler;
  }
  
  
  
  /**
   * Sets this Logger to write to the file with the specified path. Use {@link #FILEPATH} for the default file.
   * <br /><b>Note:</b> If this Logger previously has been assigned an <code>OutputStream</code>,
   * that stream will <i>not be closed</i>! In order to close it, invoke {@link #closeStream()} prior to
   * invoking this method.
   * @param path The new target file.
   */
  public void writeToFile(String path)
  {
  	filePath_ = path;
  	stream_   = null;
  }
  
  

  /**
   * Sets this Logger to write to the provided stream.
   * @param stream The new target stream.
   * <br /><b>Note:</b> If this Logger previously has been assigned an <code>OutputStream</code>,
   * that stream will not be closed! In order to close it, invoke {@link #closeStream()} prior to
   * invoking this method.
   */
  public void writeToStream(OutputStream stream)
  {
    filePath_ = null;
    stream_   = stream;
  }
	
	
  
	/**
	 * Prints the specified message to the log file or stream.
	 * @param level The {@link Level log level} for the message.
	 * @param message The message to log.
	 * @return True if the message was logged, false otherwise.
	 */
	public boolean log(Level level, String message)
	{
	  return log(level, "", message, null, false);
	}
	
	
	
	/**
	 * Prints the specified message to the log file or stream.
	 * @param level The {@link Level log level} for the message.
	 * @param location The location where the error occurred (can be null or zero-length).
	 * @param message The message to log.
	 * @return True if the message was logged, false otherwise.
	 */
	public boolean log(Level level, String location, String message)
	{
		return log(level, location, message, null, false);
	}



	/**
	 * Logs the given <code>Exception</code> (which can be <code>null</code>) together with the time and message in 
	 * the log file or stream.
	 * @param logLevel - A <code>String</code> saying what kind of error occurred (e.g. "WRITE ERROR"). A set 
	 * of standard messages can be found as constants in this class.
	 * @param location - The location where the error occurred (can be null or zero-length).
	 * @param errorMsg  - The message to be displayed after the error type (may contain new lines).
	 * @param t         - A <code>Throwable</code> from which additional information of the error 
	 * will be taken. (May be <code>null</code>.)
   * @param includeStackTrace - If the <code>Throwable</code>'s stack trace
   *          should be included. If <code>false</code> only the
   *          <code>Throwable</code>'s message will be logged.
	 * @return True if the message was successfully logged, false otherwise.
	 */
	public boolean log(Level logLevel, String location, String errorMsg, Throwable t, boolean includeStackTrace)
	{
		Calendar date = Calendar.getInstance();
		String formattedString = formatter.format(logLevel, date, location, errorMsg, t, includeStackTrace);
		
		if (filePath_ != null)
			return write(formattedString, filePath_);
		else
			return write(formattedString, stream_);
	}
	
	
	
	/**
	 * Writes a message to the file specified by <code>filePath</code>.
	 * @param filePath The path to a file.
	 * @return True if the data was successfully written to the file, false
	 * otherwise.
	 */
	private boolean write(String data, String filePath)
	{
	  if (filePath != null)
	  {
      try
      {
        File log = new File(filePath);
        
        if (!log.exists())
          if (!log.createNewFile())
            System.err.println("Logger - write(): Could not create the log file!");
        
        FileWriter writer = new FileWriter(log, true);
        writer.write(data.toString());
        writer.flush();
        writer.close();
      }
      catch (IOException e)
      {
      	handler.handleException("An unexpected exception occurred while logging:", e);
      }
      catch (SecurityException e)
      {
    		handler.handleException("An unexpected exception occurred while logging:", new IOException("Access denied: " + filePath + "!", e));
      }
      
      return true;
	  }
	  else
	    return false;
	}
	
	
	
	/**
	 * Writes a message to the stream specified by <code>stream</code>.
	 * @param stream The stream to write to.
	 * @return True if the data was successfully written to the stream, false
	 * otherwise.
	 */
	private boolean write(String data, OutputStream stream)
	{
		if (stream != null)
	  {
	    PrintStream print = new PrintStream(stream);
	    
	    print.println(data);
	    print.flush();
	    
	    return true;
	  }
	  else
	    return false;
	}
	
	
	
	/**
	 * Closes this Logger's <code>OutputStream</code> if either {@link #Logger(OutputStream)}
	 * or {@link #writeToStream(OutputStream)} has been used.
	 */
	public void closeStream()
	{
	  try
	  {
  	  if (stream_ != null)
  	    stream_.close();
	  }
	  catch (IOException e)
	  {
	  	handler.handleException("Logger encountered an exception while closing its stream: ", e);
	  }
	}
}