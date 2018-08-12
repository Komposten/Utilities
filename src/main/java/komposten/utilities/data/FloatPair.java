/*
 * Copyright (c) 2014 Jakob Hjelm 
 */
package komposten.utilities.data;

/**
 * This is an implementation identical to {@link ObjectPair}, but based on primitive floats.
 * @author Komposten
 * @version 1.0.0
 */
public final class FloatPair
{
  private float first;
  private float second;
  
  public FloatPair(float first, float second)
  {
  	set(first, second);
  }
  
  public void set(float first, float second)
  {
  	this.first = first;
  	this.second = second;
  }

  public void setFirst (float f) { first  = f; }
  public void setSecond(float s) { second = s; }
  
  public float getFirst () { return first;  }
  public float getSecond() { return second; }
  
  @Override
  public boolean equals(Object obj)
  {
    if (obj == null)
      return false;
    
    if (obj.getClass() == FloatPair.class)
    {
      FloatPair object = (FloatPair)obj;
      
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

    int firstInt = Float.floatToIntBits(first);
    int secondInt = Float.floatToIntBits(second);

		hashCode = prime * hashCode + firstInt;
    hashCode = prime * hashCode + secondInt;
    
    return hashCode;
  }
  
  @Override
  public String toString()
  {
    return "(" + first + "; " + second + ")";
  }
}