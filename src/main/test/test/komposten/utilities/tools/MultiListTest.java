package test.komposten.utilities.tools;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import komposten.utilities.data.MultiList;

public class MultiListTest
{
	private MultiList<Integer> listEmpty;
	private List<List<Integer>> listEmptyContent;
	
	private MultiList<Integer> list;
	private List<List<Integer>> listContent;
	
	@Before
	public void setup()
	{
		listEmptyContent = new ArrayList<List<Integer>>();
		listEmpty = new MultiList<Integer>(listEmptyContent);
		
		List<Integer> list1 = new ArrayList<Integer>();
		List<Integer> list2 = new LinkedList<Integer>();
		List<Integer> list3 = new LinkedList<Integer>();
		List<Integer> list4 = new ArrayList<Integer>();

		for (int i = 1; i <= 10; i++)
			list1.add(i);
		for (int i = 11; i <= 30; i++)
			list2.add(i);
		for (int i = 31; i <= 35; i++)
			list4.add(i);
		
		listContent = new ArrayList<List<Integer>>();
		listContent.add(list1);
		listContent.add(list2);
		listContent.add(list3);
		listContent.add(list4);
		
		list = new MultiList<Integer>(listContent);
	}
	

	@Test
	public void testSize()
	{
		assertEquals(35, list.size());
		listContent.get(0).add(0, 55);
		assertEquals(36, list.size());
	}
	

	@Test
	public void testIsEmpty()
	{
		assertTrue(listEmpty.isEmpty());
		
		listEmptyContent.add(new ArrayList<Integer>());
		assertTrue(listEmpty.isEmpty());
		
		listEmptyContent.add(new LinkedList<Integer>());
		assertTrue(listEmpty.isEmpty());
		
		ArrayList<Integer> listWithContent = new ArrayList<Integer>();
		listWithContent.add(1);
		listEmptyContent.add(listWithContent);
		assertFalse(listEmpty.isEmpty());
	}
	
	@Test
	public void testContains()
	{
		assertFalse(list.contains(100));
		assertTrue(list.contains(1));
		assertTrue(list.contains(5));
		assertTrue(list.contains(10));
		assertTrue(list.contains(11));
		assertTrue(list.contains(30));
		assertTrue(list.contains(31));
		assertTrue(list.contains(35));
	}
	
	@Test
	public void testContainsAll()
	{
		List<Integer> list2 = Arrays.asList(1, 5, 10, 11, 30, 31, 35);
		List<Integer> list3 = Arrays.asList(1, 5, 10, 11, 30, 31, 35, 100);
		
		assertTrue(list.containsAll(list2));
		assertFalse(list.containsAll(list3));
	}
	
	@Test
	public void testToArray()
	{
		//toArray()
		Object[] objectArray = new Object[list.size()];
		for (int i = 0; i < objectArray.length; i++)
			objectArray[i] = i + 1;
		
		assertArrayEquals(objectArray, list.toArray());
		
		//toArray(T[]) with 0 length or length == list.size().
		Integer[] sameLengthArray = new Integer[list.size()];
		for (int i = 0; i < sameLengthArray.length; i++)
			sameLengthArray[i] = i + 1;
		
		assertArrayEquals(sameLengthArray, list.toArray(new Integer[0]));
		assertArrayEquals(sameLengthArray, list.toArray(new Integer[list.size()]));
		
		//toArray(T[]) with length > list.size().
		Integer[] longerArray = new Integer[list.size() + 2];
		for (int i = 0; i < longerArray.length-2; i++)
			longerArray[i] = i + 1;
		longerArray[longerArray.length-2] = null;
		longerArray[longerArray.length-1] = 100;
		
		Integer[] longerArray2 = new Integer[longerArray.length];
		Arrays.fill(longerArray2, 100);
		
		assertArrayEquals(longerArray, list.toArray(longerArray2));
	}
	
	
	@Test
	public void testGet()
	{
		assertEquals(1, (int)list.get(0));
		assertEquals(6, (int)list.get(5));
		assertEquals(10, (int)list.get(9));
		assertEquals(11, (int)list.get(10));
		assertEquals(30, (int)list.get(29));
		assertEquals(31, (int)list.get(30));
		assertEquals(35, (int)list.get(34));
	}
	
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetOutOfBounds()
	{
		list.get(list.size());
	}
	
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetNegative()
	{
		list.get(-1);
	}
	
	
	@Test
	public void testIndexOf()
	{
		assertEquals(-1, list.indexOf(100));
		assertEquals(0, list.indexOf(1));
		assertEquals(5, list.indexOf(6));
		assertEquals(9, list.indexOf(10));
		assertEquals(10, list.indexOf(11));
		assertEquals(29, list.indexOf(30));
		assertEquals(30, list.indexOf(31));
		assertEquals(34, list.indexOf(35));
	}
	
	
	@Test
	public void testLastIndexOf()
	{
		assertEquals(9, list.lastIndexOf(10));
		listContent.get(1).add(10);
		assertEquals(30, list.lastIndexOf(10));
	}
	
	
	//List iterator
	@Test
	public void testListIterator()
	{
		ListIterator<Integer> iterator = null;
		
		//Test ascending order.
		Integer[] expected = list.toArray(new Integer[list.size()]);
		List<Integer> actual = new LinkedList<Integer>();
		
		iterator = list.listIterator();
		while (iterator.hasNext())
			actual.add(iterator.next());
		
		assertArrayEquals(expected, actual.toArray(new Integer[actual.size()]));
		
		//Test descending order.
		expected = list.toArray(new Integer[list.size()]);
		actual = new LinkedList<Integer>();
		
		iterator = list.listIterator(list.size());
		while (iterator.hasPrevious())
			actual.add(0, iterator.previous());
		
		assertArrayEquals(expected, actual.toArray(new Integer[actual.size()]));
		
		//Test random access point.
		iterator = list.listIterator(30);
		assertEquals(31, (int)iterator.next());
		iterator = list.listIterator(30);
		assertEquals(30, (int)iterator.previous());
	}
	
	
	@Test
	public void testListIteratorIndex()
	{
		ListIterator<Integer> iterator = list.listIterator(25);
		assertEquals(25, iterator.nextIndex());
		assertEquals(24, iterator.previousIndex());
	}
	

	@Test(expected = IndexOutOfBoundsException.class)
	public void testListIteratorNegativeIndex()
	{
		list.listIterator(-1);
	}
	

	@Test(expected = IndexOutOfBoundsException.class)
	public void testListIteratorOOBIndex()
	{
		list.listIterator(list.size()+1);
	}
	
	
	@Test(expected = NoSuchElementException.class)
	public void testIteratorPrevious()
	{
		ListIterator<Integer> iterator = list.listIterator(1);
		assertTrue(iterator.hasPrevious());
		iterator.previous();
		assertFalse(iterator.hasPrevious());
		iterator.previous();
	}
	
	
	@Test(expected = NoSuchElementException.class)
	public void testIteratorNext()
	{
		ListIterator<Integer> iterator = list.listIterator(list.size()-1);
		assertTrue(iterator.hasNext());
		iterator.next();
		assertFalse(iterator.hasNext());
		iterator.next();
	}
	
	
	@Test(expected = UnsupportedOperationException.class)
	public void testIteratorAdd()
	{
		list.listIterator().add(5);
	}
	
	
	@Test(expected = UnsupportedOperationException.class)
	public void testIteratorRemove()
	{
		list.listIterator().remove();
	}
	
	
	@Test(expected = UnsupportedOperationException.class)
	public void testIteratorSet()
	{
		list.listIterator().set(5);
	}

	
	//List modifications
	@Test(expected = UnsupportedOperationException.class)
	public void testAdd()
	{
		list.add(5);
	}

	
	@Test(expected = UnsupportedOperationException.class)
	public void testAddAtIndex()
	{
		list.add(5, 5);
	}

	
	@Test(expected = UnsupportedOperationException.class)
	public void testAddAll()
	{
		list.addAll(Arrays.asList(1, 2, 3));
	}

	
	@Test(expected = UnsupportedOperationException.class)
	public void testAddAllAtIndex()
	{
		list.addAll(5, Arrays.asList(1, 2, 3));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRemoveIndex()
	{
		list.remove(5);
	}

	
	@Test(expected = UnsupportedOperationException.class)
	public void testRemoveObject()
	{
		list.remove(new Integer(5));
	}

	
	@Test(expected = UnsupportedOperationException.class)
	public void testRemoveAll()
	{
		list.removeAll(Arrays.asList(1, 2, 3));
	}

	
	@Test(expected = UnsupportedOperationException.class)
	public void testRetainAll()
	{
		list.retainAll(Arrays.asList(1, 2, 3));
	}

	
	@Test(expected = UnsupportedOperationException.class)
	public void testSet()
	{
		list.set(5, 5);
	}

	
	@Test(expected = UnsupportedOperationException.class)
	public void testClear()
	{
		list.clear();
	}

	
	@Test(expected = UnsupportedOperationException.class)
	public void testSubList()
	{
		list.subList(5, 10);
	}
}
