/*
 * Copyright 2018 Jakob Hjelm
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