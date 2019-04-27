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
