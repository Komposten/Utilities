/*
 * Copyright (c) 2015 Jakob Hjelm 
 */
package komposten.utilities.tools;



/**
 * A class that represents an integer range.
 * 
 * @version 1.0.1
 *
 * @author Jakob Hjelm
 */
public class Range
{
  private int lower_;
  private int upper_;
  
  
  
  public Range()
  {
    this(0, 0);
  }
  
  
  
  public Range(int lower, int upper)
  {
  	setRange(lower, upper);
  }
  
  
  
  public int getLower()
  {
    return lower_;
  }
  
  
  
  public int getUpper()
  {
    return upper_;
  }
  
  
  
  public void setRange(int lower, int upper)
  {
    lower_ = Math.min(lower, upper);
    upper_ = Math.max(lower, upper);
  }
  
  
  
  public boolean contains(int value, boolean inclusive)
  {
    if (inclusive)
      return value >= lower_ && value <= upper_;
    else
      return value >  lower_ && value <  upper_;
  }
}
