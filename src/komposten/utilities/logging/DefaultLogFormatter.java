package komposten.utilities.logging;

import java.util.Calendar;


/**
 * A {@link LogFormatter} implementation that is used by {@link Logger} by default.
 * @see {@link Logger}
 * @author Jakob Hjelm
 * @version
 * <b>1.0.0</b> <br />
 * <ul>
 * <li>Replaced the different indents with (setable) <code>Strings</code>.</li>
 * <li>Split the <code>format()</code>-method into several sub-methods to format the different parts of the log.</li>
 * <li>Created the class from the old <code>Logger.log()</code>-method.</li>
 * </ul>
 */
public class DefaultLogFormatter implements LogFormatter
{
  protected static final String CAUSED_BY   = "Caused by: ";
  protected static final String[] MONTHS = { "Jan", "Feb", "Mar", "Apr", "May",
						"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
  protected static String newLine = System.getProperty("line.separator");
	
	private String firstLineIndent;
	private String messageIndent;
	private String messageMultiLineIndent;
	private String stackTraceIndent;

	public DefaultLogFormatter()
	{
		firstLineIndent = "/=|";
		messageIndent = "|-|";
		messageMultiLineIndent = "|----|";
		stackTraceIndent = "|---->";
	}
	
	
	public void setFirstLineIndent(String firstLineIndent)
	{
		this.firstLineIndent = firstLineIndent;
	}
	
	public void setMessageIndent(String messageIndent)
	{
		this.messageIndent = messageIndent;
	}
	
	public void setMessageMultiLineIndent(String messageMultiLineIndent)
	{
		this.messageMultiLineIndent = messageMultiLineIndent;
	}
	
	public void setStackTraceIndent(String stackTraceIndent)
	{
		this.stackTraceIndent = stackTraceIndent;
	}
	
	
	@Override
	public String format(Level logLevel, Calendar date, String location,
			String message, Throwable throwable, boolean includeStackTrace)
	{
		StringBuilder logMsg = new StringBuilder();

		String dateString = formatDate(date);
		String locationString = formatLocation(location);
		String logLevelString = formatLogLevel(logLevel);
		String messageString = formatMessage(message);
		String throwableString = formatThrowable(throwable, includeStackTrace);

		logMsg.append(newLine + firstLineIndent + dateString);
		
		if (location != null && location.length() > 0)
		{
			logMsg.append(" - " + locationString);
		}
		
		logMsg.append(newLine + messageIndent + logLevelString + ": " + messageString);
		
		if (throwable != null)
		{
			logMsg.append(newLine + throwableString);
		}
		
		formatSeparator(logMsg);
		
		return logMsg.toString();
	}


	protected String formatDate(Calendar date)
	{
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
		
		return month + " " + day + ", " + year + " " + hour + ":" + minute + ":" + second;
	}
	
	
	protected String formatLocation(String location)
	{
		return "Location: " + location;
	}


	private String formatLogLevel(Level logLevel)
	{
		return logLevel.toString().toUpperCase();
	}
	
	
	protected String formatMessage(String message)
	{
		return message.replaceAll("\n\r|\r\n|\r|\n|" + newLine + "", newLine + messageMultiLineIndent);
	}


	protected String formatThrowable(Throwable throwable, boolean includeStackTrace)
	{
		if (throwable == null)
			return "";
		
		StringBuilder string = new StringBuilder();
		string.append(messageIndent + throwable.toString());
		
		if (includeStackTrace)
		{
			for (StackTraceElement st : throwable.getStackTrace())
				string.append(newLine + stackTraceIndent + st.toString());
			
			while (throwable.getCause() != null)
			{
			  throwable = throwable.getCause();
			  
			  string.append(newLine + messageIndent + CAUSED_BY);
			  string.append(newLine + messageIndent + throwable.toString());
			  
			  for (StackTraceElement st : throwable.getStackTrace())
			    string.append(newLine + stackTraceIndent + st.toString());
			}
		}
		else
		{
		  string.append(newLine + stackTraceIndent + throwable.getStackTrace()[0]);
		  
		  while (throwable.getCause() != null)
		  {
		    throwable = throwable.getCause();
		    formatSeparator(string);
		    string.append(messageIndent + CAUSED_BY);
		    formatSeparator(string);
		    string.append(messageIndent + throwable.toString());
		    string.append(newLine + stackTraceIndent + throwable.getStackTrace()[0]);
		  }
		}
		
		return string.toString();
	}


	private void formatSeparator(StringBuilder logMsg)
	{
		logMsg.append(newLine);
	}
}
