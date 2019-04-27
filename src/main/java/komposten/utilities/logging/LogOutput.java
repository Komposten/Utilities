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
