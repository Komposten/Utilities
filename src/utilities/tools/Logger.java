package utilities.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Calendar;

import javax.swing.JOptionPane;



/**
 * This class holds a static method for logging exceptions in the file "log.txt"
 * (which is created at the top of the classpath).
 * 
 */
public final class Logger
{
  /**
   * The path to the log file utilised by this class.
   */
	public static final String FILEPATH = getProgramDir() + File.separator + "log.txt";

  public  static final String LOADERROR   = "LOAD ERROR";
  public  static final String READERROR   = "READ ERROR";
  public  static final String WRITEERROR  = "WRITE ERROR";
  public  static final String CREATEERROR = "CREATION ERROR";
  private static final String CAUSED_BY   = "Caused by: ";
	
	private static final String[] MONTHS = { "Jan", "Feb", "Mar", "Apr", "May",
						"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	
	private static String newLine = System.getProperty("line.separator");
	
	
	
	private Logger() {}
	
	
	
	private static String getProgramDir()
	{
    String path = Logger.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    
    try
    {
      path = URLDecoder.decode(path, "UTF-8");
    
//      if (path.endsWith(".jar"))
//      {
        path  = new File(path).getParent();
//      }
    }
    catch (IOException e)
    {
      String msg = "An unexpected exception ocurred while reading the game's .jar-archive's path.";
      
      Logger.log(Logger.LOADERROR, msg, e, true); //FIXME Can't use Logger here, the error ocurred while creating its file path!
      JOptionPane.showMessageDialog(null, msg + "\nThe game cannot start without this information!" +  //FIXME Maybe it shouldn't shut down the entire system...
      "\nConsult log.txt for further information!", "Load Error", JOptionPane.ERROR_MESSAGE);
      
      System.exit(0);
    }
    
    return path;
	}
	
	
	
	/**
	 * Logs the given <code>Exception</code> (which can be <code>null</code>) together with the time and message in 
	 * the file specified by <code>Logger.FILEPATH</code>.
	 * @param errorType - A <code>String</code> saying what kind of error occurred (e.g. "WRITE ERROR"). A set 
	 * of standard messages can be found as constants in this class.
	 * @param errorMsg  - The message to be displayed after the error type.
	 * @param e         - An <code>Exception</code> (can be <code>null</code>), from which additional information of the error 
	 * will be taken.
	 * @param exceptionMsgOnly - If only the <code>Exception</code>'s message should be written, and not the 
	 * entire stack trace.
	 * @return True if the message was successfully logged, false otherwise.
	 */
	public static boolean log(String errorType, String errorMsg, Exception e, boolean exceptionMsgOnly)
	{
		try
		{
			File log = new File(FILEPATH);
			
			if (!log.exists())
				if (!log.createNewFile())
				  JOptionPane.showMessageDialog(null, "Could not create the log file!", "File Creation Error", JOptionPane.ERROR_MESSAGE);
			
			StringBuilder logMsg = new StringBuilder();
			Calendar      date   = Calendar.getInstance();

			String month  = MONTHS[date.get(Calendar.MONTH)];
			String day    = Integer.toString(date.get(Calendar.DAY_OF_MONTH));
			String year   = Integer.toString(date.get(Calendar.YEAR));
			String hour   = Integer.toString(date.get(Calendar.HOUR_OF_DAY));
			String minute = Integer.toString(date.get(Calendar.MINUTE));
			String second = Integer.toString(date.get(Calendar.SECOND));

			if (hour.length() == 1)
				hour = "0" + hour;
			if (minute.length() == 1)
				minute = "0" + minute;
			if (second.length() == 1)
				second = "0" + second;
			
			logMsg.append(newLine + "/=|" + month + " " + day +
					", " + year + " " + hour + ":" + minute + ":" + second + "│");
			logMsg.append(newLine + "|-|" + errorType.toUpperCase() + ":");
			logMsg.append(" " + errorMsg);
			
			if (e != null)
			{
				logMsg.append(newLine + "|-|" + e.toString());
				
				if (!exceptionMsgOnly)
				{
					for (StackTraceElement st : e.getStackTrace())
						logMsg.append(newLine + "|---->" + st.toString());
					
					while (e.getCause() != null)
					{
					  if ((e.getCause() instanceof Exception))
					  {
  					  e = (Exception) e.getCause();
  					  
  					  logMsg.append(newLine + "|-|" + CAUSED_BY);
  					  logMsg.append(newLine + "|-|" + e.toString());
  					  
  					  for (StackTraceElement st : e.getStackTrace())
  					    logMsg.append(newLine + "|---->" + st.toString());
					  }
					  else
					  {
              logMsg.append(newLine + "|-|" + CAUSED_BY);
              logMsg.append(newLine + "|-|" + e.getCause().toString());
              
              for (StackTraceElement st : e.getCause().getStackTrace())
                logMsg.append(newLine + "|----->" + st.toString());
              
              break;
					  }
					}
				}
				else
				{
				  while (e.getCause() != null)
				  {
				    e = (Exception) e.getCause();
				    logMsg.append(newLine);
  	        logMsg.append("|-|" + CAUSED_BY);
  	        logMsg.append(newLine);
            logMsg.append("|-|" + e.toString());
            logMsg.append("|");
				  }
				  
				  logMsg.append(newLine + "---->At: " + e.getStackTrace()[0]);
				}
			}
			
			logMsg.append(newLine);
			
			FileWriter writer = new FileWriter(log, true);
			writer.write(logMsg.toString());
			writer.flush();
			writer.close();
		}
		catch (IOException ioe)
		{
			return false;
		}
		
		return true;
	}
}