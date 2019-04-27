/*
 * Copyright 2018 Jakob Hjelm
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
