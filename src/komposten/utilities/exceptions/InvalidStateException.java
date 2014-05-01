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
