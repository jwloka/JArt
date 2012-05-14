package jart.test;
import jast.ParseException;
import jast.analysis.UsageVisitor;

// Proxy class which is used to reduce the amount of
// dependencies to jast.test (to only this class)
public abstract class TestBase extends jast.test.resolver.TestBase
{

    public TestBase(String name)
    {
        super(name);
    }

    protected void addUsages()
    {
        new UsageVisitor().visitProject(_project);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();
    }

    protected void tearDown() throws ParseException
    {
        super.tearDown();
    }
}
