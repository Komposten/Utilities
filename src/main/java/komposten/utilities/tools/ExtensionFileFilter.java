package komposten.utilities.tools;

import java.io.File;
import java.io.FileFilter;

public class ExtensionFileFilter implements FileFilter
{
  private String[] extensions_;
  
  
  public ExtensionFileFilter()
  {
    this(new String[0]);
  }
  
  public ExtensionFileFilter(String... extensions)
  {
    extensions_ = extensions;
    
    for (int i = 0; i < extensions_.length; i++)
      if (!extensions_[i].startsWith("."))
        extensions_[i] = "." + extensions_[i];
  }
  
  
  
  public void setAcceptedExtensions(String... extensions)
  {
    extensions_ = extensions;
  }
  
  
  
  @Override
  public boolean accept(File file)
  {
    if (extensions_.length == 0)
    {
      return true;
    }
    else
    {
      String fileExtension = getFileExtension(file);
      
      for (String extension : extensions_)
        if (fileExtension.equals(extension))
          return true;
    }
    
    return false;
  }

  private String getFileExtension(File file)
  {
    int dotIndex = file.getName().lastIndexOf('.');
    
    if (dotIndex >= 0)
      return file.getName().substring(dotIndex);
    return "";
  }
}
