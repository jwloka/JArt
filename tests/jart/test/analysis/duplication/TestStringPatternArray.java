package jart.test.analysis.duplication;
import jart.analysis.duplication.StringPattern;
import jart.analysis.duplication.collections.StringPatternArray;
import junitx.framework.PrivateTestCase;


public class TestStringPatternArray extends PrivateTestCase
{
    private StringPatternArray _testObject;

    public TestStringPatternArray(String name)
    {
        super(name);
    }

    protected void setUp()
    {
        _testObject = new StringPatternArray();
    }

    protected void tearDown()
    {
        _testObject = null;
    }

    public void testAdd1() throws Exception
    {
        _testObject.add(new StringPattern("aabb", 0, 3));

        assertEquals(0, getInt(_testObject, "_minStartPos"));

        assertEquals(3, getInt(_testObject, "_maxEndPos"));
    }

    public void testAdd2() throws Exception
    {
        _testObject.add(new StringPattern("aabb", 4, 7));

        assertEquals(4, getInt(_testObject, "_minStartPos"));

        assertEquals(7, getInt(_testObject, "_maxEndPos"));
    }

    public void testAdd3() throws Exception
    {
        _testObject.add(new StringPattern("aabb", 0, 3));
        _testObject.add(new StringPattern("aabbcc", 0, 5));

        assertEquals(0, getInt(_testObject, "_minStartPos"));

        assertEquals(5, getInt(_testObject, "_maxEndPos"));
    }

    public void testAdd4() throws Exception
    {
        _testObject.add(new StringPattern("bbcc", 2, 5));
        _testObject.add(new StringPattern("aabb", 0, 3));

        assertEquals(0, getInt(_testObject, "_minStartPos"));

        assertEquals(5, getInt(_testObject, "_maxEndPos"));
    }

    public void testAdd5() throws Exception
    {
        _testObject.add(new StringPattern("bbcc", 2, 5));
        _testObject.add(new StringPattern("aabbcc", 0, 5));

        assertEquals(0, getInt(_testObject, "_minStartPos"));

        assertEquals(5, getInt(_testObject, "_maxEndPos"));
    }

    public void testIsAlreadyFound1() throws Exception
    {
        _testObject.add(new StringPattern("aabbcc", 0, 5));

        Object[] args = { new StringPattern("aabb", 0, 3)};
        Boolean result = (Boolean) invoke(_testObject, "isAlreadyFound", args);

        assertTrue(result.booleanValue());
    }

    public void testIsAlreadyFound2() throws Exception
    {
        _testObject.add(new StringPattern("bbccdd", 2, 7));

        Object[] args = { new StringPattern("aabb", 0, 3)};
        Boolean result = (Boolean) invoke(_testObject, "isAlreadyFound", args);

        assertTrue(!result.booleanValue());
    }

    public void testIsAlreadyFound3() throws Exception
    {
        _testObject.add(new StringPattern("bbcc", 2, 5));

        Object[] args = { new StringPattern("ccdd", 3, 7)};
        Boolean result = (Boolean) invoke(_testObject, "isAlreadyFound", args);

        assertTrue(!result.booleanValue());
    }
}
