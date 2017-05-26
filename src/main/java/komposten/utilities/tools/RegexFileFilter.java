package komposten.utilities.tools;

import java.io.File;
import java.io.FileFilter;

/**
 * A {@link FileFilter} that tries to match file names to regular expression patterns.
 * @version
 * <b>1.0.1</b> <br />
 * <ul>
 * <li>Renamed "setAcceptedRegexes" to "setAcceptedPatterns".</li>
 * <li>Added JavaDoc for the default constructor.
 * </ul>
 * <b>Older</b> <br />
 * 1.0.0 <br />
 * <ul>
 * <li>Initial implementation.</li>
 * </ul>
 * @author Jakob Hjelm
 */
public class RegexFileFilter implements FileFilter
{
  private String[] patterns_;
  
  
  /**
   * Creates a <code>RegexFileFilter</code> that accepts all files.
   * @see #setAcceptedPatterns(String...)
   */
  public RegexFileFilter()
  {
    this(new String[0]);
  }
  
  
  public RegexFileFilter(String... patterns)
  {
    patterns_ = patterns;
  }
  
  
  
  public void setAcceptedPatterns(String... regexes)
  {
    patterns_ = regexes;
  }
  
  
  
  @Override
  public boolean accept(File file)
  {
    if (patterns_.length == 0)
    {
      return true;
    }
    else
    {
      for (String regex : patterns_)
        if (file.getName().matches(regex))
          return true;
    }
    
    return false;
  }
}
