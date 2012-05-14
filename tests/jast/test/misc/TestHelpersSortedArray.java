package jast.test.misc;
import jast.helpers.Comparable;
import jast.helpers.SortedArray;
import junit.framework.TestCase;

public class TestHelpersSortedArray extends TestCase
{
    private class TestComparable implements Comparable
    {
        private int _value;

        public TestComparable(int value)
        {
            _value = value;
        }

        public boolean isLowerOrEqual(Comparable other)
        {
            return (_value <= ((TestComparable)other)._value);
        }
    }

    private SortedArray _array;

    public TestHelpersSortedArray(String name)
    {
        super(name);
    }

    protected void setUp()
    {
        _array = new SortedArray();
    }

    protected void tearDown()
    {
        _array = null;
    }

    public void testAdd1()
    {
        _array.add(new TestComparable(1), "value1");

        assertEquals(1,
                     _array.getCount());
        assertEquals("value1",
                     _array.get(0));
    }

    public void testAdd2()
    {
        _array.add(new TestComparable(1), "value3");
        _array.add(new TestComparable(3), "value2");
        _array.add(new TestComparable(2), "value1");

        assertEquals(3,
                     _array.getCount());
        assertEquals("value3",
                     _array.get(0));
        assertEquals("value1",
                     _array.get(1));
        assertEquals("value2",
                     _array.get(2));
    }

    public void testAdd3()
    {
        _array.add(new TestComparable(1), "value3");
        _array.add(new TestComparable(3), "value2");
        _array.add(new TestComparable(2), "value1");
        _array.add(new TestComparable(1), "value4");

        assertEquals(4,
                     _array.getCount());
        assertEquals("value3",
                     _array.get(0));
        assertEquals("value4",
                     _array.get(1));
        assertEquals("value1",
                     _array.get(2));
        assertEquals("value2",
                     _array.get(3));
    }

    public void testEmpty1()
    {
        assertEquals(0,
                     _array.getCount());
    }

    public void testEmpty2()
    {
        try
        {
            _array.get(0);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {}
    }

    public void testEmpty3()
    {
        _array.clear();
        assertEquals(0,
                     _array.getCount());
    }

    public void testEmpty5()
    {
        try
        {
            _array.remove(0);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            return;
        }
        fail();
    }

    public void testRemove1()
    {
        _array.add(new TestComparable(1), "value1");
        _array.remove(0);

        assertEquals(0,
                     _array.getCount());
    }

    public void testRemove2()
    {
        _array.add(new TestComparable(1), "value3");
        _array.add(new TestComparable(3), "value2");
        _array.add(new TestComparable(2), "value1");
        _array.remove(1);

        assertEquals(2,
                     _array.getCount());
        assertEquals("value3",
                     _array.get(0));
        assertEquals("value2",
                     _array.get(1));
    }

    public void testRemove3()
    {
        _array.add(new TestComparable(1), "value3");
        _array.add(new TestComparable(3), "value2");
        _array.add(new TestComparable(2), "value1");
        _array.clear();

        assertEquals(0,
                     _array.getCount());
    }
}
