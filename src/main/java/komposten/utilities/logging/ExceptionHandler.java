package komposten.utilities.logging;

/**
 * Exceptions that occur while a {@link Logger} is writing a log message are
 * passed to an <code>ErrorHandler</code>. That way the code that uses a
 * <code>Logger</code> can implement a single handler for these exceptions,
 * rather than having to wrap every single log-call in a try-catch.
 * <br />
 * <br />
 * The default implementation simply prints the exception to the standard error stream.
 * 
 * @author Jakob Hjelm
 *
 */
public class ExceptionHandler
{
	public void handleException(String msg, Throwable throwable)
	{
		System.err.println(msg);
		throwable.printStackTrace();
	}
}
