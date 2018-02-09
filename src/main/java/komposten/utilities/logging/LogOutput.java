package komposten.utilities.logging;

public interface LogOutput
{
	/**
	 * Writes the specified message to this <code>LogOutput</code>'s destination.
	 * @param message The message to write.
	 * @param exceptionHandler An exception handler to be used if any exception occurs.
	 * @return <code>true</code> if writing was successful, <code>false</code> otherwise.
	 */
	public boolean write(String message, ExceptionHandler exceptionHandler);
	/**
	 * Closes open streams/files/etc. used by this <code>LogOutput</code>. 
	 * @param exceptionHandler An exception handler to be used if any exception occurs.
	 * @return <code>true</code> if closing was successful, <code>false</code> otherwise.
	 */
	public boolean close(ExceptionHandler exceptionHandler);
}
