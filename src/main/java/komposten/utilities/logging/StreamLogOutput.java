package komposten.utilities.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class StreamLogOutput implements LogOutput
{
	private OutputStream stream;
	

	public StreamLogOutput(OutputStream stream)
	{
		this.stream = stream;
	}
	
	
	@Override
	public boolean write(String message, ExceptionHandler exceptionHandler)
	{
    PrintStream print = new PrintStream(stream);
    
    print.println(message);
    print.flush();
    
    return true;
	}


	@Override
	public boolean close(ExceptionHandler exceptionHandler)
	{
	  try
	  {
  	  if (stream != null)
  	    stream.close();
  	  return true;
	  }
	  catch (IOException e)
	  {
	  	exceptionHandler.handleException("StreamLogOutput encountered an exception while closing its stream: ", e);
	  	return false;
	  }
	}
}
