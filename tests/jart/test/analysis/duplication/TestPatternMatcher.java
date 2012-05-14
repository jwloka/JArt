package jart.test.analysis.duplication;
import jart.analysis.duplication.PatternMatcher;
import junitx.framework.PrivateTestCase;


public class TestPatternMatcher extends PrivateTestCase
{
    private PatternMatcher _testObject;

    public TestPatternMatcher(String name)
    {
        super(name);
    }

    protected void setUp()
    {
        _testObject = new PatternMatcher();
        _testObject.setMatchByteCount(2);
    }

    protected void tearDown()
    {
        _testObject = null;
    }

    public void testAddFollowingChar1() throws Exception
    {
        _testObject.setPattern("aabbcc");

        Object[] args = { "aabb", new Integer(0)};

        String result =
            (String) invokeWithKey(_testObject, "addFollowingChar_java.lang.String_int", args);

        assertEquals("aabbcc", result);
    }

    public void testAddFollowingChar2() throws Exception
    {
        _testObject.setPattern("aabbccdd");

        Object[] args = { "bbcc", new Integer(2)};

        String result =
            (String) invokeWithKey(_testObject, "addFollowingChar_java.lang.String_int", args);

        assertEquals("bbccdd", result);
    }

    public void testAddFollowingChar3()
    {
        _testObject.setPattern("aabbcc");

        Object[] args = { "bbcc", new Integer(2)};

        try
            {
            invokeWithKey(_testObject, "addFollowingChar_java.lang.String_int", args);
        }
        catch (Exception ex)
            {
            // succeeded!
            return;
        }

        fail();
    }

    public void testCompleteMatch1() throws Exception
    {
        _testObject.setPattern("aabbcc");
        invoke(_testObject, "setText", new Object[] { "aabbaabbcc" });

        Object[] args = { "aa", new Integer(0)};

        invokeWithKey(_testObject, "getCompleteMatch_java.lang.String_int", args);

        String result = (String) get(_testObject, "_completeMatch");

        assertEquals("aabbcc", result);
    }

    public void testCompleteMatch2() throws Exception
    {
        _testObject.setPattern("aabbccdd");
        invoke(_testObject, "setText", new Object[] { "aabbaabbcc" });

        Object[] args = { "cc", new Integer(4)};

        invokeWithKey(_testObject, "getCompleteMatch_java.lang.String_int", args);

        String result = (String) get(_testObject, "_completeMatch");

        assertEquals("cc", result);
    }

    public void testGetIndexOfPattern1() throws Exception
    {
        _testObject.setPattern("aabbccdd");

        assertEquals(2, _testObject.getIndexOfPattern("bbccdd"));
    }

    public void testGetIndexOfPattern2() throws Exception
    {
        _testObject.setPattern("aabbccdd");

        assertEquals(0, _testObject.getIndexOfPattern("aa"));
    }

    public void testGetIndexOfPattern3() throws Exception
    {
        _testObject.setPattern("aabbccdd");

        assertEquals(-1, _testObject.getIndexOfPattern(""));
    }

    public void testGetIndexOfPattern4() throws Exception
    {
        _testObject.setPattern("aabbccdd");

        assertEquals(-1, _testObject.getIndexOfPattern("ee"));
    }

    public void testGetIndexOfPattern5() throws Exception
    {
        _testObject.setPattern("aabbccdd");

        assertEquals(-1, _testObject.getIndexOfPattern(null));
    }

    public void testSimpleMatch1()
    {
        _testObject.setPattern("aabbcc");
        _testObject.findMatches("aabbccdd", 6);

        assertEquals(1, _testObject.getMatches().getCount());
        assertEquals("aabbcc", _testObject.getMatches().get(0).getPattern());
    }

    public void testSimpleMatch2()
    {
        _testObject.setPattern("aabbcc");
        _testObject.findMatches("aabbccdd", 2);

        assertEquals(1, _testObject.getMatches().getCount());
        assertEquals("aabbcc", _testObject.getMatches().get(0).getPattern());
    }

    public void testSimpleMatch3()
    {
        _testObject.setPattern("aabbcc");
        _testObject.findMatches("aabbccddbbcc", 4);

        assertEquals(1, _testObject.getMatches().getCount());
        assertEquals("aabbcc", _testObject.getMatches().get(0).getPattern());
    }

    public void testSimpleMatch4()
    {
        _testObject.setPattern("aabb");
        _testObject.findMatches("ccddeeaabb", 2);

        assertEquals(1, _testObject.getMatches().getCount());
        assertEquals("aabb", _testObject.getMatches().get(0).getPattern());
    }

    public void testSimpleMatch5()
    {
        _testObject.setPattern("eeaabb");
        _testObject.findMatches("ccddeeaabb", 4);

        assertEquals(1, _testObject.getMatches().getCount());
        assertEquals("eeaabb", _testObject.getMatches().get(0).getPattern());
    }

    public void testSimpleMatch6()
    {
        _testObject.setPattern("aabbcc");
        _testObject.findMatches("aabbddeeaabbcc", 4);

        assertEquals(1, _testObject.getMatches().getCount());
        assertEquals("aabbcc", _testObject.getMatches().get(0).getPattern());
    }
}
