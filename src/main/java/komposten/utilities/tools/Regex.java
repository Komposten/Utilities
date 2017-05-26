/*
 * Copyright (c) 2014 Jakob Hjelm 
 */
package komposten.utilities.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @version
 * <b>1.1.0</b><br />
 * <ul>
 * <li>Added getMatchCount(String, String).</li>
 * </ul>
 * <b>Older</b> <br />
 * 1.0.0
 * <ul>
 * <li>Added getMatcher(String, String).</li>
 * <li>Added getMatches(String, String).</li>
 * </ul>
 * @author Jakob Hjelm
 */
public class Regex
{
  
  
  /**
   * Returns a {@link Matcher} for the specified string using the provided regex.
   * @param regex The regex to use in the <code>Matcher</code>.
   * @param string The string to apply to regex on.
   * @return A {@link Matcher} for the specified string using the provided regex.
   */
  public static Matcher getMatcher(String regex, String string)
  {
    return Pattern.compile(regex).matcher(string);
  }
  
  
  
  /**
   * Returns an array containing all the matches of the specified regex in the
   * provided string.
   * @param regex
   * @param string
   * @return An array containing all the matches of the specified regex in the
   * provided string, or null if no matches were found.
   */
  public static String[] getMatches(String regex, String string)
  {
    Matcher      matcher = getMatcher(regex, string);
    List<String> matches = new ArrayList<String>();
    
    while (matcher.find())
      matches.add(matcher.group());
    
    return matches.toArray(new String[0]);
  }
  
  
  
  /**
   * Returns the amount of matches for the specified regex in the provided <code>String</code>.
   * @param regex The regex to match in the string.
   * @param input The input string.
   * @return The match count for <code>regex</code> in <code>input</code>.
   */
  public static int getMatchCount(String regex, String input)
  {
    Matcher matcher = Pattern.compile(regex).matcher(input);
    
    int count = 0;
    while (matcher.find())
      count++;
    
    return count;
  }
}