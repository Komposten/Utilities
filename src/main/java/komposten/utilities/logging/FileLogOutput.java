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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileLogOutput implements LogOutput
{
	private File file;
	

	public FileLogOutput(String filePath)
	{
		this(new File(filePath));
	}
	

	public FileLogOutput(File file)
	{
		this.file = file;
	}
	

	@Override
	public boolean write(String message, ExceptionHandler exceptionHandler)
	{
    try
    {
      if (!file.exists())
        file.createNewFile();
      
      FileWriter writer = new FileWriter(file, true);
      writer.write(message.toString());
      writer.flush();
      writer.close();
    }
    catch (IOException e)
    {
    	exceptionHandler.handleException("An unexpected exception occurred while logging:", e);
    	return false;
    }
    catch (SecurityException e)
    {
    	exceptionHandler.handleException("An unexpected exception occurred while logging:", new IOException("Access denied: " + file + "!", e));
    	return false;
    }
    
    return true;
	}


	@Override
	public boolean close(ExceptionHandler exceptionHandler)
	{
		return true;
	}
}
