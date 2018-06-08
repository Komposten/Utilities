package komposten.utilities.data;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;


/**
 * This is an <i>unmodifiable</i> list that effectively "combines" multiple
 * lists. Unlike using e.g. {@link List#addAll(Collection)},
 * <code>MultiList</code> does not actually merge the lists. Instead it keeps
 * them separate, but provides all standard facilities to iterate over them
 * (e.g. {@link #get(int)}, {@link #listIterator()}, {@link #indexOf(Object)},
 * etc.) as if they were just a single list. This means that this implementation
 * can "merge" any number of lists instantly. <br />
 * Additionally, since the underlying lists are not merged, they all keep their
 * respective list types, and all changes to them are reflected by the
 * <code>MultiList</code>. <br />
 * <br />
 * <code>MultiList</code> is unmodifiable since adding or removing values is
 * nonintuitive and could lead to unintended side effects. The reason for this
 * is that it there is no way to know which of the backing lists would be
 * affected by such an operation, so weird changes could appear in different
 * parts of the program without apparent reason.
 * 
 * @author Jakob Hjelm
 */
public class MultiList<E> implements List<E>
{
	private final List<List<E>> lists;


	public MultiList(List<List<E>> lists)
	{
		this.lists = lists;
	}


	@Override
	public int size()
	{
		int sum = 0;

		for (List<E> list : lists)
			sum += list.size();

		return sum;
	}


	@Override
	public boolean isEmpty()
	{
		for (List<E> list : lists)
		{
			if (!list.isEmpty())
				return false;
		}
		return true;
	}


	@Override
	public boolean contains(Object o)
	{
		for (List<E> list : lists)
		{
			if (list.contains(o))
				return true;
		}
		return false;
	}


	@Override
	public boolean containsAll(Collection<?> c)
	{
		for (Object e : c)
		{
			if (!contains(e))
				return false;
		}
		return true;
	}


	@Override
	public Iterator<E> iterator()
	{
		return listIterator();
	}


	@Override
	public ListIterator<E> listIterator()
	{
		return listIterator(0);
	}


	@Override
	public ListIterator<E> listIterator(int index)
	{
		positionCheck(index);
		return new ListItr(index);
	}
	
	
	private void positionCheck(int index)
	{
		int size = size();
		if (index < 0 || index > size)
			throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
	}


	@Override
	public Object[] toArray()
	{
//		Stream<T> stream = lists[0].stream();
//		for (int i = 1; i < lists.length; i++)
//			stream = Stream.concat(stream, lists[i].stream());
//		
//		return stream.toArray();
		
		return toArray(new Object[size()]);
	}


	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a)
	{
    int size = size();
		if (a.length < size)
    {
      a = (T[])java.lang.reflect.Array.newInstance(
                          a.getClass().getComponentType(), size);
    }
    
    int index = 0;
    for (List<E> list : lists)
		{
			System.arraycopy(list.toArray(), 0, a, index, list.size());
			index += list.size();
		}
    
    if (a.length > size)
    {
    	a[size] = null;
    }
    
		return a;
	}


	@Override
	public boolean add(E e)
	{
		throw new UnsupportedOperationException();
	}


	@Override
	public boolean remove(Object o)
	{
		throw new UnsupportedOperationException();
	}


	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		throw new UnsupportedOperationException();
	}


	@Override
	public boolean addAll(int index, Collection<? extends E> c)
	{
		throw new UnsupportedOperationException();
	}


	@Override
	public boolean removeAll(Collection<?> c)
	{
		throw new UnsupportedOperationException();
	}


	@Override
	public boolean retainAll(Collection<?> c)
	{
		throw new UnsupportedOperationException();
	}


	@Override
	public void clear()
	{
		throw new UnsupportedOperationException();
	}


	@Override
	public E get(int index)
	{
		rangeCheck(index);
		
		int temp = 0;
		for (List<E> list : lists)
		{
			if (index < temp + list.size())
				return list.get(index - temp);
			temp += list.size();
		}
		
		return null;
	}
	
	
	private void rangeCheck(int index)
	{
		int size = size();
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
	}


	@Override
	public E set(int index, E element)
	{
		throw new UnsupportedOperationException();
	}


	@Override
	public void add(int index, E element)
	{
		throw new UnsupportedOperationException();
	}


	@Override
	public E remove(int index)
	{
		throw new UnsupportedOperationException();
	}


	@Override
	public int indexOf(Object o)
	{
		int offset = 0;
		for (List<E> list : lists)
		{
			int index = list.indexOf(o);
			if (index >= 0)
				return offset + index;
			else
				offset += list.size();
		}
		
		return -1;
	}


	@Override
	public int lastIndexOf(Object o)
	{
		int offset = size() - lists.get(lists.size()-1).size();
		
		for (int i = lists.size()-1; i > 0; i--)
		{
			int index = lists.get(i).lastIndexOf(o);
			if (index >= 0)
				return offset + index;
			else
				offset -= lists.get(i-1).size();
		}
		
		return lists.get(0).lastIndexOf(o);
	}


	@Override
	public List<E> subList(int fromIndex, int toIndex)
	{
		throw new UnsupportedOperationException();
	}
	
	
	private class ListItr implements ListIterator<E>
	{
		private int position;
		private int listIndex;
		private ListIterator<E> listIterator;


		public ListItr(int index)
		{
			position = index;
			
			int offset = 0;
			for (int i = 0; i < lists.size(); i++)
			{
				List<E> list = lists.get(i);
				
				if (index <= offset + list.size())
				{
					index -= offset;
					listIndex = i;
					break;
				}
				else
				{
					offset += list.size();
				}
			}
			
			listIterator = lists.get(listIndex).listIterator(index);
		}


		@Override
		public boolean hasNext()
		{
			return position < size();
		}


		@Override
		public E next()
		{
			if (!hasNext())
				throw new NoSuchElementException();
			
			if (listIterator.hasNext())
			{
				position++;
				return listIterator.next();
			}
			else if (listIndex < lists.size()-1)
			{
				listIndex++;
				listIterator = lists.get(listIndex).listIterator(0);
				return next();
			}
			else
			{
				throw new NoSuchElementException();
			}
		}


		@Override
		public boolean hasPrevious()
		{
			return position > 0;
		}


		@Override
		public E previous()
		{
			if (!hasPrevious())
				throw new NoSuchElementException();
			
			if (listIterator.hasPrevious())
			{
				position--;
				return listIterator.previous();
			}
			else if (listIndex > 0)
			{
				listIndex--;
				List<E> previousList = lists.get(listIndex);
				listIterator = previousList.listIterator(previousList.size());
				return previous();
			}
			else
			{
				throw new NoSuchElementException();
			}
		}


		@Override
		public int nextIndex()
		{
			return position;
		}


		@Override
		public int previousIndex()
		{
			return position - 1;
		}


		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}


		@Override
		public void set(E e)
		{
			throw new UnsupportedOperationException();
		}


		@Override
		public void add(E e)
		{
			throw new UnsupportedOperationException();
		}
	}
}
