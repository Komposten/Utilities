/*
 * Copyright 2017 Jakob Hjelm
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
package komposten.utilities.exceptions;

/**
 * 
 * @version 1.0.0
 * @author Jakob Hjelm
 */
public class InvalidStateException extends RuntimeException
{
  private static final long serialVersionUID = 2357289L;
  
  /**
   * Constructs a new invalid state exception with the specified detail message.
   * @param message - The detail message.
   */
  public InvalidStateException(String message)
  {
    super(message);
  }



  /**
   * Constructs a new invalid state exception with the specified cause and a detail
   * message of <code>(cause==null ? null : cause.toString())</code>.
   * 
   * @param cause - The cause of the exception. May be <code>null</code>.
   */
  public InvalidStateException(Throwable cause)
  {
    super(cause);
  }


  /**
   * Constructs a new invalid state exception with the specified detail message and cause. 
   * @param message - The detail message.
   * @param cause - The cause of the exception. May be <code>null</code>.
   */
  public InvalidStateException(String message, Throwable cause)
  {
    super(message, cause);
  }



  protected InvalidStateException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace)
  {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
