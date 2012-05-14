package jast.test.misc;
import jast.helpers.HashArray;
import junit.framework.TestCase;

public class TestHelpersHashArray extends TestCase
{
    private HashArray _array;

    public TestHelpersHashArray(String name)
    {
        super(name);
    }

    protected void setUp()
    {
        _array = new HashArray();
    }

    protected void tearDown()
    {
        _array = null;
    }

    public void testAdd1()
    {
        _array.add("elem1", "value1");

        assertEquals(1,
                     _array.getCount());
        assertEquals("value1",
                     _array.get(0));
        assertEquals("value1",
                     _array.get("elem1"));
    }

    public void testAdd2()
    {
        _array.add("elem1", "value3");
        _array.add("elem3", "value2");
        _array.add("elem2", "value1");

        assertEquals(3,
                     _array.getCount());
        assertEquals("value3",
                     _array.get(0));
        assertEquals("value3",
                     _array.get("elem1"));
        assertEquals("value2",
                     _array.get(1));
        assertEquals("value2",
                     _array.get("elem3"));
        assertEquals("value1",
                     _array.get(2));
        assertEquals("value1",
                     _array.get("elem2"));
    }

    public void testAdd3()
    {
        _array.add("elem1", "value3");
        _array.add("elem3", "value2");
        _array.add("elem2", "value1");
        _array.add("elem3", "value4");

        assertEquals(3,
                     _array.getCount());
        assertEquals("value3",
                     _array.get(0));
        assertEquals("value3",
                     _array.get("elem1"));
        assertEquals("value1",
                     _array.get(1));
        assertEquals("value1",
                     _array.get("elem2"));
        assertEquals("value4",
                     _array.get(2));
        assertEquals("value4",
                     _array.get("elem3"));
    }

    public void testContains1()
    {
        _array.add("elem1", "value3");
        _array.add("elem3", "value2");
        _array.add("elem2", "value1");

        assertTrue(_array.contains("elem3"));
        assertTrue(!_array.contains("elem4"));

        _array.remove("elem3");

        assertTrue(!_array.contains("elem3"));
    }

    public void testContains2()
    {
        _array.add("elem1", "value3");
        _array.add("elem3", "value2");
        _array.add("elem2", "value1");

        assertTrue(_array.containsValue("value3"));
        assertTrue(!_array.containsValue("value4"));

        _array.remove("elem1");

        assertTrue(!_array.containsValue("value3"));
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
        {
            return;
        }
        fail();
    }

    public void testEmpty3()
    {
        assertNull(_array.get("elem1"));
    }

    public void testEmpty4()
    {
        _array.clear();
        assertEquals(0,
                     _array.getCount());
    }

    public void testEmpty5()
    {
        _array.remove("elem1");
        assertEquals(0,
                     _array.getCount());
    }

    public void testRemove1()
    {
        _array.add("elem1", "value1");
        _array.remove("elem1");

        assertEquals(0,
                     _array.getCount());
    }

    public void testRemove2()
    {
        _array.add("elem1", "value3");
        _array.add("elem3", "value2");
        _array.add("elem2", "value1");
        _array.remove(1);

        assertEquals(2,
                     _array.getCount());
        assertNull(_array.get("elem3"));
        assertEquals("value3",
                     _array.get(0));
        assertEquals("value1",
                     _array.get(1));
    }

    public void testRemove3()
    {
        _array.add("elem1", "value3");
        _array.add("elem3", "value2");
        _array.add("elem2", "value1");
        _array.remove("elem1");

        assertEquals(2,
                     _array.getCount());
        assertNull(_array.get("elem1"));
        assertEquals("value2",
                     _array.get("elem3"));
        assertEquals("value1",
                     _array.get("elem2"));
    }

    public void testRemove4()
    {
        _array.add("elem1", "value3");
        _array.add("elem3", "value2");
        _array.add("elem2", "value1");
        _array.remove("elem4");

        assertEquals(3,
                     _array.getCount());
        assertEquals("value1",
                     _array.get("elem2"));
        assertEquals("value2",
                     _array.get("elem3"));
        assertEquals("value3",
                     _array.get("elem1"));
    }

    public void testRemove5()
    {
        _array.add("elem1", "value3");
        _array.add("elem3", "value2");
        _array.add("elem2", "value1");
        _array.clear();

        assertEquals(0,
                     _array.getCount());
        assertNull(_array.get("elem2"));
        assertNull(_array.get("elem3"));
        assertNull(_array.get("elem1"));
    }
}
