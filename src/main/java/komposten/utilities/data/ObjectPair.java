/*
 * Copyright (c) 2017 Jakob Hjelm 
 */
package komposten.utilities.data;

/**
 * This class represents a pair of objects, similar to {@link java.util.Map.Entry}.
 * @author Komposten
 * @version 1.0.0
 */
public final class ObjectPair<F, S>
{
	private F first_;
	private S second_;


	public ObjectPair(F first, S second)
	{
		first_ = first;
		second_ = second;
	}


	public void setFirst(F f)
	{
		first_ = f;
	}


	public void setSecond(S s)
	{
		second_ = s;
	}


	public F getFirst()
	{
		return first_;
	}


	public S getSecond()
	{
		return second_;
	}


	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}

		if (obj.getClass() == getClass())
		{
			ObjectPair<?, ?> object = (ObjectPair<?, ?>) obj;

			if (object.getFirst() == getFirst() && object.getSecond() == getSecond())
			{
				return true;
			}

			return false;
		}
		else
		{
			return super.equals(obj);
		}
	}


	@Override
	public String toString()
	{
		return "(" + first_ + "; " + second_ + ")";
	}
}