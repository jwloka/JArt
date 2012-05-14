package jast.test.misc;
import jast.helpers.StringArray;
import jast.helpers.StringIterator;
import junit.framework.TestCase;

public class TestHelpersStringArray extends TestCase
{
    private StringArray _array;

    public TestHelpersStringArray(String name)
    {
        super(name);
    }

    protected void setUp()
    {
        _array = new StringArray();
    }

    protected void tearDown()
    {
        _array = null;
    }

    public void testAdd1()
    {
        _array.add("elem1");

        assertTrue(!_array.isEmpty());
        assertEquals(1,
                     _array.getCount());
        assertEquals("elem1",
                     _array.get(0));
        assertEquals("elem1",
                     _array.getLast());
    }

    public void testAdd2()
    {
        _array.add("elem1");
        _array.add("elem3");
        _array.add("elem2");

        assertTrue(!_array.isEmpty());
        assertEquals(3,
                     _array.getCount());
        assertEquals("elem1",
                     _array.get(0));
        assertEquals("elem3",
                     _array.get(1));
        assertEquals("elem2",
                     _array.get(2));
        assertEquals("elem2",
                     _array.getLast());
    }

    public void testAsString1()
    {
        _array.add("elem1");
        _array.add("elem3");
        _array.add("elem2");

        assertEquals("elem1.elem3.elem2",
                     _array.asString("."));
    }

    public void testAsString2()
    {
        _array.add("a");
        _array.add("a");
        _array.add("a");

        assertEquals("aabaaabaa",
                     _array.asString("aba"));
    }

    public void testAsString3()
    {
        _array = StringArray.fromString(".a..b.", ".");

        assertEquals("xaxxbx",
                     _array.asString("x"));
    }

    public void testAsString4()
    {
        _array = StringArray.fromString("a.b.c.d.e", ".");

        assertEquals("a.b.c",
                     _array.asString(".", 3));
    }

    public void testAsString5()
    {
        _array = StringArray.fromString("a.b.c.d.e", ".");

        assertEquals("",
                     _array.asString(".", 0));
    }

    public void testAsString6()
    {
        _array = StringArray.fromString("a.b.c.d.e", ".");

        assertEquals("a.b.c.d.e",
                     _array.asString(".", 10));
    }

    public void testContains1()
    {
        _array.add("elem1");
        _array.add("elem3");
        _array.add("elem2");

        assertTrue(_array.contains("elem3"));
        assertTrue(!_array.contains("elem4"));

        _array.remove(1);

        assertTrue(!_array.contains("elem3"));
    }

    public void testEmpty1()
    {
        assertTrue(_array.isEmpty());
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
        try
        {
            _array.getLast();
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            return;
        }
        fail();
    }

    public void testEmpty4()
    {
        _array.clear();
        assertTrue(_array.isEmpty());
        assertEquals(0,
                     _array.getCount());
    }

    public void testEmpty5()
    {
        try
        {
            _array.removeLast();
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            return;
        }
        fail();
    }

    public void testEmpty6()
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

    public void testFromString1()
    {
        _array = StringArray.fromString(null, ".");

        assertTrue(_array.isEmpty());
    }

    public void testFromString2()
    {
        _array = StringArray.fromString("", null);

        assertTrue(_array.isEmpty());
    }

    public void testFromString3()
    {
        _array = StringArray.fromString("elem1.elem2.elem3", ".");

        assertEquals(3,
                     _array.getCount());

        StringIterator it = _array.getIterator();

        assertTrue(it.hasNext());
        assertEquals("elem1",
                     it.getNext());
        assertTrue(it.hasNext());
        assertEquals("elem2",
                     it.getNext());
        assertTrue(it.hasNext());
        assertEquals("elem3",
                     it.getNext());
        assertTrue(!it.hasNext());
    }

    public void testFromString4()
    {
        _array = StringArray.fromString("elem1..elem2", ".");

        assertEquals(3,
                     _array.getCount());

        StringIterator it = _array.getIterator();

        assertTrue(it.hasNext());
        assertEquals("elem1",
                     it.getNext());
        assertTrue(it.hasNext());
        assertEquals("",
                     it.getNext());
        assertTrue(it.hasNext());
        assertEquals("elem2",
                     it.getNext());
        assertTrue(!it.hasNext());
    }

    public void testFromString5()
    {
        _array = StringArray.fromString("elem1blaelem2blaelem3", "bla");

        assertEquals(3,
                     _array.getCount());

        StringIterator it = _array.getIterator();

        assertTrue(it.hasNext());
        assertEquals("elem1",
                     it.getNext());
        assertTrue(it.hasNext());
        assertEquals("elem2",
                     it.getNext());
        assertTrue(it.hasNext());
        assertEquals("elem3",
                     it.getNext());
        assertTrue(!it.hasNext());
    }

    public void testFromString6()
    {
        _array = StringArray.fromString(".elem1.elem2.", ".");

        assertEquals(4,
                     _array.getCount());

        StringIterator it = _array.getIterator();

        assertTrue(it.hasNext());
        assertEquals("",
                     it.getNext());
        assertEquals("elem1",
                     it.getNext());
        assertTrue(it.hasNext());
        assertEquals("elem2",
                     it.getNext());
        assertTrue(it.hasNext());
        assertEquals("",
                     it.getNext());
        assertTrue(!it.hasNext());
    }

    public void testIterator1()
    {
        StringIterator it = _array.getIterator();

        assertTrue(!it.hasNext());
    }

    public void testIterator2()
    {
        _array.add("elem1");
        _array.add("elem3");
        _array.add("elem2");

        StringIterator it = _array.getIterator();

        assertTrue(it.hasNext());
        assertEquals("elem1",
                     it.getNext());
        assertTrue(it.hasNext());
        assertEquals("elem3",
                     it.getNext());
        assertTrue(it.hasNext());
        assertEquals("elem2",
                     it.getNext());
        assertTrue(!it.hasNext());
    }

    public void testRemove1()
    {
        _array.add("elem1");
        _array.remove(0);

        assertTrue(_array.isEmpty());
        assertEquals(0,
                     _array.getCount());
    }

    public void testRemove2()
    {
        _array.add("elem1");
        _array.add("elem3");
        _array.add("elem2");
        _array.remove(1);

        assertTrue(!_array.isEmpty());
        assertEquals(2,
                     _array.getCount());
        assertEquals("elem1",
                     _array.get(0));
        assertEquals("elem2",
                     _array.get(1));
        assertEquals("elem2",
                     _array.getLast());
    }

    public void testRemove3()
    {
        _array.add("elem1");
        _array.add("elem3");
        _array.add("elem2");
        _array.removeLast();

        assertTrue(!_array.isEmpty());
        assertEquals(2,
                     _array.getCount());
        assertEquals("elem1",
                     _array.get(0));
        assertEquals("elem3",
                     _array.get(1));
        assertEquals("elem3",
                     _array.getLast());
    }
}
