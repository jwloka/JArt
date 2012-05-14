package jast.test;
import jast.ast.ParsePosition;
import junitx.framework.PrivateTestCase;

public abstract class TestBase extends PrivateTestCase
{

    public TestBase(String name)
    {
        super(name);
    }

    protected void assertEquals(ParsePosition firstPos,
                                ParsePosition secondPos)
    {
        // we using string equality because of the output
        // in case of an error
        assertEquals(firstPos.getLine()+","+firstPos.getColumn()+","+firstPos.getAbsolute(),
                     secondPos.getLine()+","+secondPos.getColumn()+","+secondPos.getAbsolute());
    }
}
