package komposten.utilities.tools;

import java.io.File;
import java.io.FileFilter;

public class RegexFileFilter implements FileFilter
{
  private String[] regexes_;
  
  
  public RegexFileFilter()
  {
    this(new String[0]);
  }
  
  public RegexFileFilter(String... regexes)
  {
    regexes_ = regexes;
  }
  
  
  
  public void setAcceptedRegexes(String... regexes)
  {
    regexes_ = regexes;
  }
  
  
  
  @Override
  public boolean accept(File file)
  {
    if (regexes_.length == 0)
    {
      return true;
    }
    else
    {
      for (String regex : regexes_)
        if (file.getName().matches(regex))
          return true;
    }
    
    return false;
  }
}
