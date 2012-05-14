package jast.test.misc;
import jast.helpers.StringIterator;
import jast.helpers.StringMap;
import junit.framework.TestCase;

public class TestHelpersStringMap extends TestCase
{
    private StringMap _map;

    public TestHelpersStringMap(String name)
    {
        super(name);
    }

    protected void setUp()
    {
        _map = new StringMap();
    }

    protected void tearDown()
    {
        _map = null;
    }

    public void testContains1()
    {
        _map.set("elem1", "value3");
        _map.set("elem3", "value2");
        _map.set("elem2", "value1");

        assertTrue(_map.contains("elem3"));
        assertTrue(!_map.contains("elem4"));

        _map.remove("elem3");

        assertTrue(!_map.contains("elem3"));
    }

    public void testContains2()
    {
        _map.set("elem1", "value3");
        _map.set("elem3", "value2");
        _map.set("elem2", "value1");

        assertTrue(_map.containsValue("value3"));
        assertTrue(!_map.containsValue("value4"));

        _map.remove("elem1");

        assertTrue(!_map.contains("value3"));
    }

    public void testEmpty1()
    {
        assertEquals(0,
                     _map.getCount());
    }

    public void testEmpty2()
    {
        assertNull(_map.get("elem1"));
    }

    public void testEmpty3()
    {
        _map.clear();
        assertEquals(0,
                     _map.getCount());
    }

    public void testEmpty4()
    {
        _map.remove("elem1");
        assertEquals(0,
                     _map.getCount());
    }

    public void testIterator1()
    {
        assertTrue(!_map.getIterator().hasNext());
        assertTrue(!_map.getKeyIterator().hasNext());
    }

    public void testIterator2()
    {
        _map.set("elem1", "value3");
        _map.set("elem3", "value2");
        _map.set("elem2", "value1");

        StringIterator it = _map.getIterator();

        assertTrue(it.hasNext());
        it.getNext();
        assertTrue(it.hasNext());
        it.getNext();
        assertTrue(it.hasNext());
        it.getNext();
        assertTrue(!it.hasNext());

        it = _map.getKeyIterator();

        assertTrue(it.hasNext());
        it.getNext();
        assertTrue(it.hasNext());
        it.getNext();
        assertTrue(it.hasNext());
        it.getNext();
        assertTrue(!it.hasNext());
    }

    public void testRemove1()
    {
        _map.set("elem1", "value1");
        _map.remove("elem1");

        assertEquals(0,
                     _map.getCount());
    }

    public void testRemove2()
    {
        _map.set("elem1", "value3");
        _map.set("elem3", "value2");
        _map.set("elem2", "value1");
        _map.remove("elem1");

        assertEquals(2,
                     _map.getCount());
        assertNull(_map.get("elem1"));
        assertEquals("value2",
                     _map.get("elem3"));
        assertEquals("value1",
                     _map.get("elem2"));
    }

    public void testRemove3()
    {
        _map.set("elem1", "value3");
        _map.set("elem3", "value2");
        _map.set("elem2", "value1");
        _map.remove("elem4");

        assertEquals(3,
                     _map.getCount());
        assertEquals("value1",
                     _map.get("elem2"));
        assertEquals("value2",
                     _map.get("elem3"));
        assertEquals("value3",
                     _map.get("elem1"));
    }

    public void testRemove4()
    {
        _map.set("elem1", "value3");
        _map.set("elem3", "value2");
        _map.set("elem2", "value1");
        _map.clear();

        assertEquals(0,
                     _map.getCount());
        assertNull(_map.get("elem2"));
        assertNull(_map.get("elem3"));
        assertNull(_map.get("elem1"));
    }

    public void testSet1()
    {
        _map.set("elem1", "value1");

        assertEquals(1,
                     _map.getCount());
        assertEquals("value1",
                     _map.get("elem1"));
    }

    public void testSet2()
    {
        _map.set("elem1", "value3");
        _map.set("elem3", "value2");
        _map.set("elem2", "value1");

        assertEquals(3,
                     _map.getCount());
        assertEquals("value3",
                     _map.get("elem1"));
        assertEquals("value1",
                     _map.get("elem2"));
        assertEquals("value2",
                     _map.get("elem3"));
    }

    public void testSet3()
    {
        _map.set("elem1", "value3");
        _map.set("elem3", "value2");
        _map.set("elem2", "value1");
        _map.set("elem3", "value4");

        assertEquals(3,
                     _map.getCount());
        assertEquals("value3",
                     _map.get("elem1"));
        assertEquals("value1",
                     _map.get("elem2"));
        assertEquals("value4",
                     _map.get("elem3"));
    }
}
