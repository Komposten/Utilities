package komposten.utilities.tools;

import java.io.File;
import java.io.FileFilter;


public class ExtensionFileFilter implements FileFilter
{
	private String[] extensions;
	private boolean acceptFilesWithNoExtension;


	/**
	 * Creates a new ExtensionFileFilter that accepts all files.
	 */
	public ExtensionFileFilter()
	{
		this(new String[0]);
	}


	/**
	 * Creates a new ExtensionFileFilter that accepts files with the specified
	 * extensions. <br />
	 * 
	 * @param extensions File extensions to accept, with or without a leading
	 *          period (.). Use an empty String to accept files without extension.
	 */
	public ExtensionFileFilter(String... extensions)
	{
		setAcceptedExtensions(extensions);
	}


	/**
	 * Sets the file extensions accepted by the filter.
	 * 
	 * @param extensions File extensions to accept, with or without a leading
	 *          period (.). Use an empty String to accept files without extension.
	 *          If no parameters are passed, all files will be accepted.
	 */
	public void setAcceptedExtensions(String... extensions)
	{
		this.extensions = extensions;

		acceptFilesWithNoExtension = false;

		for (int i = 0; i < extensions.length; i++)
		{
			String extension = extensions[i].trim();
			if (extension.isEmpty())
				acceptFilesWithNoExtension = true;
			else if (!extension.startsWith("."))
				extensions[i] = "." + extension;
		}
	}


	@Override
	public boolean accept(File file)
	{
		if (extensions.length == 0)
		{
			return true;
		}
		else
		{
			String fileName = file.getName();

			for (String extension : extensions)
			{
				if (!extension.isEmpty() && fileName.endsWith(extension))
					return true;
			}

			if (acceptFilesWithNoExtension && fileName.indexOf('.') == -1)
			{
				return true;
			}
		}

		return false;
	}
}
