package komposten.utilities.logging;

import java.util.Calendar;


/** An interface that describes a formatter used to format log output from a {@link Logger}. */
public interface LogFormatter
{
	public String format(Level logLevel, Calendar date, String location, String message, Throwable throwable, boolean includeStackTrace);
}
