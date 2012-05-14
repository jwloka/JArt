package jast.test.prettyprinter;
import jast.ParseException;
import jast.prettyprinter.PrettyPrinter;

public abstract class TestPrettyPrinterBase extends TestBase
{
    protected PrettyPrinter _prettyPrinter;

    public TestPrettyPrinterBase(String name)
    {
        super(name);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();
        _prettyPrinter = new PrettyPrinter(_writer);
    }

    protected void tearDown() throws ParseException
    {
        _prettyPrinter = null;
        super.tearDown();
    }
}
