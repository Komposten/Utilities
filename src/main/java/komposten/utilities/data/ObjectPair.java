/*
 * Copyright (c) 2017 Jakob Hjelm 
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