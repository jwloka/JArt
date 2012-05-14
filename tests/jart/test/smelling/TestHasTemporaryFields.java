package jart.test.smelling;
import jart.smelling.HasTemporaryFields;
import jart.test.TestBase;
import jast.Global;

public class TestHasTemporaryFields extends TestBase
{
    private HasTemporaryFields _testObject;

    public TestHasTemporaryFields(String name)
    {
        super(name);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(TestHasTemporaryFields.class);
    }

    public void testInitialize1()
    {
        try
        {
            _testObject = new HasTemporaryFields(null);
            fail("An Exception should be thrown");
        }
        catch (IllegalArgumentException ex)
        {
            // test passed
        }

    }

    public void testInitialize2()
    {
        try
        {
            _testObject =
                new HasTemporaryFields(
                    Global.getFactory().createClassDeclaration(
                        Global.getFactory().createModifiers(),
                        "Class1",
                        false));
        }
        catch (IllegalArgumentException ex)
        {
            fail("No Exception should be thrown.");
        }

    }
}
