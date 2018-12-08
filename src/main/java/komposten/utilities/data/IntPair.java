/*
 * Copyright (c) 2014 Jakob Hjelm 
 */
package komposten.utilities.data;

/**
 * This is an implementation more or less identical to {@link ObjectPair}, but based on primitive ints. <br />
 * @author Komposten
 * @version 1.4.0
 */
public final class IntPair
{
  private int first;
  private int second;
  
  public IntPair(int first, int second)
  {
  	set(first, second);
  }
  
  public void set(int first, int second)
  {
  	this.first = first;
  	this.second = second;
  }

  public void setFirst (int f) { first = f; }
  public void setSecond(int s) { second = s; }
  public int getFirst () { return first; }
  public int getSecond() { return second; }
  
  /** This is a wrapper for {@link #setFirst(int)} */
  public void setX(int x) { first = x; }
  /** This is a wrapper for {@link #setSecond(int)} */
  public void setY(int y) { second = y; }
  /** This is a wrapper for {@link #getFirst()}. */
  public int getX() { return getFirst(); }
  /** This is a wrapper for {@link #getSecond()}. */
  public int getY() { return getSecond(); }
  
  @Override
  public boolean equals(Object obj)
  {
  	if (obj == this)
  		return true;
  	
    if (obj == null)
      return false;
    
    if (obj.getClass() == IntPair.class)
    {
      IntPair object = (IntPair)obj;
      
      if (object.getFirst() == getFirst() && object.getSecond() == getSecond())
        return true;
      
      return false;
    }
    else
      return super.equals(obj);
  }
  
  @Override
  public int hashCode()
  {
    int hashCode = 1;
    int prime = 31;

		hashCode = prime * hashCode + first;
    hashCode = prime * hashCode + second;
    
    return hashCode;
  }
  
  @Override
  public String toString()
  {
    return "(" + first + "; " + second + ")";
  }
}