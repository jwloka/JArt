package jast.test.prettyprinter;
import jast.prettyprinter.Style;
import jast.prettyprinter.StyleWriter;

import java.io.StringWriter;

public class StyleStringWriter extends    StringWriter
                               implements StyleWriter
{
    private boolean _writeStyles = false;

    public StyleStringWriter()
    {
        super();
    }

    public void doWriteStyles(boolean doIt)
    {
        _writeStyles = doIt;
    }

    public void startStyle(String name)
    {
        if (_writeStyles && !Style.NORMAL.equalsIgnoreCase(name))
        {
            write("<"+name.toLowerCase()+">");
        }
    }

    public void stopStyle(String name)
    {
        if (_writeStyles && !Style.NORMAL.equalsIgnoreCase(name))
        {
            write("</"+name.toLowerCase()+">");
        }
    }
}
