/*
 * Copyright 2015, 2017 Jakob Hjelm
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
