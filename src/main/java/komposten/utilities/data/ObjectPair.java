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

import java.util.Objects;

/**
 * This class represents a pair of objects, similar to {@link java.util.Map.Entry}.
 * @author Komposten
 * @version 1.2.1
 */
public final class ObjectPair<F, S>
{
	private F first;
	private S second;


	public ObjectPair(F first, S second)
	{
		set(first, second);
	}
	
	
	public void set(F first, S second)
	{
		this.first = first;
		this.second = second;
	}


	public void setFirst(F f)
	{
		first = f;
	}


	public void setSecond(S s)
	{
		second = s;
	}


	public F getFirst()
	{
		return first;
	}


	public S getSecond()
	{
		return second;
	}
	
	
	@Override
	public int hashCode()
	{
		return Objects.hash(first, second);
	}


	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		
		if (obj == null)
			return false;
		
		if (obj.getClass() != getClass())
			return false;

		ObjectPair<?, ?> object = (ObjectPair<?, ?>) obj;
		
		if (first == null)
		{
			if (object.first != null)
				return false;
		}
		else
		{
			if (!first.equals(object.first))
				return false;
		}

		if (second == null)
		{
			if (object.second != null)
				return false;
		}
		else
		{
			if (!second.equals(object.second))
				return false;
		}
		
		return true;
	}
	

	@Override
	public String toString()
	{
		return "(" + first + "; " + second + ")";
	}
}