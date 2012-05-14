package jast.test.analysis;
import jast.Global;
import jast.SyntaxException;
import jast.analysis.TypeUsages;
import jast.analysis.Usages;
import jast.analysis.UsagesStack;

import java.util.Vector;

import junitx.framework.PrivateTestCase;
import junitx.framework.TestAccessException;



public class TestUsagesStack extends PrivateTestCase
{
    private UsagesStack _testObject;

    public TestUsagesStack(String name)
    {
        super(name);
    }

    private Usages createUsages() throws SyntaxException
    {
        return (Usages) new TypeUsages(
            Global.getFactory().createClassDeclaration(
                Global.getFactory().createModifiers(),
                "Class1",
                false));
    }

    public void setUp()
    {
        _testObject = new UsagesStack();
    }

    public void tearDown()
    {
        _testObject = null;
    }

    public void testInitialize()
    {
        assertNotNull(_testObject);
        assertEquals(0, _testObject.getCount());

        assertNull(_testObject.top());
        assertNull(_testObject.pop());
    }

    public void testPop1() throws SyntaxException
    {
        Usages item1 = createUsages();
        Usages item2 = createUsages();

        _testObject.push(item1);
        _testObject.push(item2);

        assertEquals(item2, _testObject.pop());
        assertEquals(item1, _testObject.pop());
        assertEquals(null, _testObject.pop());
    }

    public void testPush1() throws SyntaxException
    {
        _testObject.push(createUsages());
        assertEquals(1, _testObject.getCount());
    }

    public void testPush2() throws SyntaxException, TestAccessException
    {
        Usages item1 = createUsages();
        Usages item2 = createUsages();

        _testObject.push(item1);
        _testObject.push(item2);

        assertEquals(2, _testObject.getCount());

        Vector internalData = (Vector) get(_testObject, "_data");
        assertEquals(item2, internalData.get(0));
        assertEquals(item1, internalData.get(1));
    }

    public void testTop1() throws SyntaxException
    {
        Usages item1 = createUsages();
        Usages item2 = createUsages();

        _testObject.push(item1);
        _testObject.push(item2);

        assertEquals(item2, _testObject.top());
        assertEquals(item2, _testObject.top());

        _testObject.pop();

        assertEquals(item1, _testObject.top());

        _testObject.pop();

        assertEquals(null, _testObject.top());

    }
}
