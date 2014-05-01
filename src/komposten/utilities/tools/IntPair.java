package komposten.utilities.tools;

/**
 * @author Komposten
 * @version 1.1.1
 */
public final class IntPair
{
  private int first_;
  private int second_;
  
  public IntPair(int first, int second)
  {
    first_  = first;
    second_ = second;
  }

  public void setFirst (int f) { first_  = f; }
  public void setSecond(int s) { second_ = s; }
  
  public int  getFirst ()      { return first_;  }
  public int  getSecond()      { return second_; }
  
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
    int hashCode;
    
    if (Math.abs(first_) < Short.MAX_VALUE &&
        Math.abs(second_) < Short.MAX_VALUE)
    {
      hashCode = first_ >= second_ ? first_*first_ + first_ + second_ : first_ + second_*second_;
    }
    else
    {
      int a = first_  / 2;
      int b = second_ / 2;
      
      hashCode = a >= b ? a*a + a + b : a + b*b;
    }
    
    return hashCode;
  }
  
  @Override
  public String toString()
  {
    return "(" + first_ + "; " + second_ + ")";
  }
}