package jast.test.prettyprinter;
import jast.ParseException;

public abstract class TestBase extends jast.test.resolver.TestBase
{
    protected StyleStringWriter _writer;

    public TestBase(String name)
    {
        super(name);
    }

    protected String getText()
    {
        return _writer.getBuffer().toString();
    }

    protected void setUp() throws ParseException
    {
        super.setUp();
        _writer = new StyleStringWriter();
    }

    protected void tearDown() throws ParseException
    {
        _writer = null;
        super.tearDown();
    }
}
