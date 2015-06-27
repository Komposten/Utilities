/*
 * Copyright (c) 2015 Jakob Hjelm 
 */
package komposten.utilities.tools;



/**
 * A class that represents an integer range.
 * 
 * @version 1.0
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
    lower_ = lower;
    upper_ = upper;
  }
  
  
  
  public int getLower()
  {
    return lower_;
  }
  
  
  
  public int getUpper()
  {
    return upper_;
  }
  
  
  
  public boolean contains(int value, boolean inclusive)
  {
    if (inclusive)
      return value >= lower_ && value <= upper_;
    else
      return value >  lower_ && value <  upper_;
  }
  
  
  
  public void setRange(int lower, int upper)
  {
    lower_ = lower;
    upper_ = upper;
  }
}
