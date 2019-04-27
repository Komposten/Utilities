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
