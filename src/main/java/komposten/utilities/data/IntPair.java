/*
 * Copyright (c) 2014 Jakob Hjelm 
 */
package komposten.utilities.data;

/**
 * This is an implementation identical to {@link ObjectPair}, but based on primitive ints.
 * @author Komposten
 * @version 1.3.1
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

  public void setFirst (int f) { first  = f; }
  public void setSecond(int s) { second = s; }
  
  public int getFirst () { return first;  }
  public int getSecond() { return second; }
  
  @Override
  public boolean equals(Object obj)
  {
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