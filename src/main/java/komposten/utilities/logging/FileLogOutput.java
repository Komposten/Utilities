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
