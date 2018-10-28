package komposten.utilities.tools;

import java.io.File;
import java.io.FileFilter;


public class ExtensionFileFilter implements FileFilter
{
	private String[] extensions;
	private boolean acceptFolders;
	private boolean acceptFilesWithNoExtension;


	/**
	 * Creates a new ExtensionFileFilter that accepts all files and folders.
	 */
	public ExtensionFileFilter()
	{
		this(true, new String[0]);
	}


	/**
	 * Creates a new ExtensionFileFilter that accepts files with the specified
	 * extensions. <br />
	 * 
	 * @param acceptFolders If the filter should accept folders (folders will
	 *          never be matched against the filtered extensions).
	 * @param extensions File extensions to accept, with or without a leading
	 *          period (.). Use an empty String to accept files without extension.
	 */
	public ExtensionFileFilter(boolean acceptFolders, String... extensions)
	{
		setAcceptFolders(acceptFolders);
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


	/**
	 * Sets whether or not this filter should accept folders.
	 * 
	 * @param accept
	 */
	public void setAcceptFolders(boolean accept)
	{
		acceptFolders = accept;
	}


	@Override
	public boolean accept(File file)
	{
		if (extensions.length == 0)
		{
			return true;
		}
		else if (file.isDirectory())
		{
			return acceptFolders;
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
