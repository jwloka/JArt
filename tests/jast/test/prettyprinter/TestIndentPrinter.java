package jast.test.prettyprinter;
import jast.ParseException;
import jast.ast.ParsePosition;
import jast.prettyprinter.IndentPrinter;
import jast.prettyprinter.Options;
import jast.prettyprinter.Style;

public class TestIndentPrinter extends TestBase
{
    private IndentPrinter _printer;

    public TestIndentPrinter(String name)
    {
        super(name);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();
        _writer.doWriteStyles(true);
        _printer = new IndentPrinter(_writer, new Options());
    }

    protected void tearDown() throws ParseException
    {
        _printer = null;
        super.tearDown();
    }

    public void testIndentedPrint1()
    {
        _printer.getOptions().setIndentToken("  ");

        assertEquals(new ParsePosition(1, 1, 0),
                     _printer.getPosition());

        _printer.incIndent(2);

        assertEquals(new ParsePosition(1, 5, 4),
                     _printer.getPosition());

        _printer.write("abc");

        assertEquals(new ParsePosition(1, 8, 7),
                     _printer.getPosition());
        assertEquals("    abc",
                     getText());
    }

    public void testIndentedPrint2()
    {
        _printer.getOptions().setIndentToken("\t");

        assertEquals(new ParsePosition(1, 1, 0),
                     _printer.getPosition());

        _printer.incIndent(3);

        assertEquals(new ParsePosition(1, 4, 3),
                     _printer.getPosition());

        _printer.write("ab\n");

        // note that the position gives the position where the next
        // token would start (including the indent)
        assertEquals(new ParsePosition(2, 4, 9),
                     _printer.getPosition());
        assertEquals("\t\t\tab\n",
                     getText());
    }

    public void testIndentedPrint3()
    {
        _printer.getOptions().setIndentToken(" ");

        assertEquals(new ParsePosition(1, 1, 0),
                     _printer.getPosition());

        _printer.incIndent(1);

        assertEquals(new ParsePosition(1, 2, 1),
                     _printer.getPosition());

        _printer.write("ab\nc");

        assertEquals(new ParsePosition(2, 3, 6),
                     _printer.getPosition());
        assertEquals(" ab\n c",
                     getText());
    }

    public void testIndentedPrintLn1()
    {
        _printer.getOptions().setIndentToken("\t");

        assertEquals(new ParsePosition(1, 1, 0),
                     _printer.getPosition());

        _printer.incIndent(2);

        assertEquals(new ParsePosition(1, 3, 2),
                     _printer.getPosition());

        _printer.writeln("ab");

        // note that the position gives the position where the next
        // token would start (including the indent)
        assertEquals(new ParsePosition(2, 3, 7),
                     _printer.getPosition());
        assertEquals("\t\tab\n",
                     getText());
    }

    public void testIndentedPrintLn2()
    {
        _printer.getOptions().setIndentToken(" ");

        assertEquals(new ParsePosition(1, 1, 0),
                     _printer.getPosition());

        _printer.incIndent(1);

        assertEquals(new ParsePosition(1, 2, 1),
                     _printer.getPosition());

        _printer.writeln("ab\n");

        // note that the position gives the position where the next
        // token would start (including the indent)
        assertEquals(new ParsePosition(3, 2, 6),
                     _printer.getPosition());
        // no indent in empty lines
        assertEquals(" ab\n\n",
                     getText());
    }

    public void testIndentedPrintLn3()
    {
        _printer.getOptions().setIndentToken("  ");

        assertEquals(new ParsePosition(1, 1, 0),
                     _printer.getPosition());

        _printer.incIndent(3);

        assertEquals(new ParsePosition(1, 7, 6),
                     _printer.getPosition());

        _printer.writeln("ab\nc");

        // note that the position gives the position where the next
        // token would start (including the indent)
        assertEquals(new ParsePosition(3, 7, 23),
                     _printer.getPosition());
        assertEquals("      ab\n      c\n",
                     getText());
    }

    public void testIndentToggle1()
    {
        _printer.getOptions().setIndentToken("  ");

        assertEquals(new ParsePosition(1, 1, 0),
                     _printer.getPosition());

        _printer.incIndent(2);

        assertEquals(new ParsePosition(1, 5, 4),
                     _printer.getPosition());

        _printer.doIndent(false);

        assertEquals(new ParsePosition(1, 1, 0),
                     _printer.getPosition());

        _printer.write("abc");

        _printer.doIndent(true);

        assertEquals(new ParsePosition(1, 4, 3),
                     _printer.getPosition());

        assertEquals("abc",
                     getText());
    }

    public void testIndentToggle2()
    {
        _printer.getOptions().setIndentToken("  ");

        assertEquals(new ParsePosition(1, 1, 0),
                     _printer.getPosition());

        _printer.incIndent(2);

        assertEquals(new ParsePosition(1, 5, 4),
                     _printer.getPosition());

        _printer.write("abc");

        _printer.doIndent(false);

        assertEquals(new ParsePosition(1, 8, 7),
                     _printer.getPosition());

        _printer.writeln();

        assertEquals(new ParsePosition(2, 1, 8),
                     _printer.getPosition());

        _printer.doIndent(true);

        assertEquals(new ParsePosition(2, 5, 12),
                     _printer.getPosition());

        assertEquals("    abc\n",
                     getText());
    }

    public void testIndentToggle3()
    {
        _printer.getOptions().setIndentToken("  ");

        assertEquals(new ParsePosition(1, 1, 0),
                     _printer.getPosition());

        _printer.incIndent(2);

        assertEquals(new ParsePosition(1, 5, 4),
                     _printer.getPosition());

        _printer.write("abc");

        _printer.doIndent(false);

        _printer.startTryMode();

        assertEquals(new ParsePosition(1, 8, 7),
                     _printer.getPosition());

        _printer.writeln();

        assertEquals(new ParsePosition(2, 1, 8),
                     _printer.getPosition());

        _printer.stopTryMode(false);

        assertEquals(new ParsePosition(1, 8, 7),
                     _printer.getPosition());

        _printer.doIndent(true);

        assertEquals(new ParsePosition(1, 8, 7),
                     _printer.getPosition());

        assertEquals("    abc",
                     getText());
    }

    public void testIndentToggle4()
    {
        _printer.getOptions().setIndentToken("  ");

        assertEquals(new ParsePosition(1, 1, 0),
                     _printer.getPosition());

        _printer.incIndent(2);

        assertEquals(new ParsePosition(1, 5, 4),
                     _printer.getPosition());

        _printer.writeln("abc");

        assertEquals(new ParsePosition(2, 5, 12),
                     _printer.getPosition());

        _printer.startTryMode();

        _printer.doIndent(false);

        assertEquals(new ParsePosition(2, 1, 8),
                     _printer.getPosition());

        _printer.writeln();

        assertEquals(new ParsePosition(3, 1, 9),
                     _printer.getPosition());

        _printer.doIndent(true);

        _printer.stopTryMode(true);

        assertEquals(new ParsePosition(3, 5, 13),
                     _printer.getPosition());

        assertEquals("    abc\n\n",
                     getText());
    }

    public void testInitial()
    {
        assertEquals(new ParsePosition(1, 1, 0),
                     _printer.getPosition());
        assertEquals("",
                     getText());
    }

    public void testMaxColumn1()
    {
        _printer.getOptions().setIndentToken("  ");

        assertEquals(new ParsePosition(1, 1, 0),
                     _printer.getPosition());

        _printer.incIndent(2);

        assertEquals(new ParsePosition(1, 5, 4),
                     _printer.getPosition());

        _printer.doGetMaxColumn();

        _printer.write("abc");
        _printer.writeln("def");

        assertEquals(new ParsePosition(2, 5, 15),
                     _printer.getPosition());
        assertEquals(11,
                     _printer.getMaxColumn());

        assertEquals("    abcdef\n",
                     getText());
    }

    public void testMaxColumn2()
    {
        _printer.getOptions().setIndentToken("  ");

        assertEquals(new ParsePosition(1, 1, 0),
                     _printer.getPosition());

        _printer.incIndent(2);

        assertEquals(new ParsePosition(1, 5, 4),
                     _printer.getPosition());

        _printer.doGetMaxColumn();

        _printer.writeln();

        assertEquals(new ParsePosition(2, 5, 5),
                     _printer.getPosition());
        assertEquals(1,
                     _printer.getMaxColumn());

        assertEquals("\n",
                     getText());
    }

    public void testPrint1()
    {
        _printer.write("abc");

        assertEquals(new ParsePosition(1, 4, 3),
                     _printer.getPosition());
        assertEquals("abc",
                     getText());
    }

    public void testPrint2()
    {
        _printer.write("ab\n");

        assertEquals(new ParsePosition(2, 1, 3),
                     _printer.getPosition());
        assertEquals("ab\n",
                     getText());
    }

    public void testPrint3()
    {
        _printer.write("ab\nc");

        assertEquals(new ParsePosition(2, 2, 4),
                     _printer.getPosition());
        assertEquals("ab\nc",
                     getText());
    }

    public void testPrintLn1()
    {
        _printer.writeln("ab");

        assertEquals(new ParsePosition(2, 1, 3),
                     _printer.getPosition());
        assertEquals("ab\n",
                     getText());
    }

    public void testPrintLn2()
    {
        _printer.writeln("ab\n");

        assertEquals(new ParsePosition(3, 1, 4),
                     _printer.getPosition());
        assertEquals("ab\n\n",
                     getText());
    }

    public void testPrintLn3()
    {
        _printer.writeln("ab\nc");

        assertEquals(new ParsePosition(3, 1, 5),
                     _printer.getPosition());
        assertEquals("ab\nc\n",
                     getText());
    }

    public void testStyle1()
    {
        _printer.write("ab");

        assertEquals(new ParsePosition(1, 3, 2),
                     _printer.getPosition());

        _printer.startTryMode();

        assertEquals(new ParsePosition(1, 3, 2),
                     _printer.getPosition());

        _printer.startStyle(Style.KEYWORD);
        _printer.write("c");

        assertEquals(new ParsePosition(1, 4, 3),
                     _printer.getPosition());

        _printer.stopStyle(Style.KEYWORD);
        _printer.stopTryMode(true);

        assertEquals(new ParsePosition(1, 4, 3),
                     _printer.getPosition());

        _printer.write("d");

        assertEquals(new ParsePosition(1, 5, 4),
                     _printer.getPosition());
        assertEquals("ab<keyword>c</keyword>d",
                     getText());
    }

    public void testStyle2()
    {
        _printer.write("ab");

        assertEquals(new ParsePosition(1, 3, 2),
                     _printer.getPosition());

        _printer.startTryMode();

        assertEquals(new ParsePosition(1, 3, 2),
                     _printer.getPosition());

        _printer.startStyle(Style.KEYWORD);
        _printer.write("c");

        assertEquals(new ParsePosition(1, 4, 3),
                     _printer.getPosition());

        _printer.stopStyle(Style.KEYWORD);
        _printer.stopTryMode(false);

        assertEquals(new ParsePosition(1, 3, 2),
                     _printer.getPosition());

        _printer.write("d");

        assertEquals(new ParsePosition(1, 4, 3),
                     _printer.getPosition());
        assertEquals("abd",
                     getText());
    }

    public void testStyle3()
    {
        _printer.write("ab");

        assertEquals(new ParsePosition(1, 3, 2),
                     _printer.getPosition());

        _printer.startStyle(Style.KEYWORD);
        _printer.startTryMode();

        assertEquals(new ParsePosition(1, 3, 2),
                     _printer.getPosition());

        _printer.write("c");

        assertEquals(new ParsePosition(1, 4, 3),
                     _printer.getPosition());

        _printer.stopTryMode(false);

        assertEquals(new ParsePosition(1, 3, 2),
                     _printer.getPosition());

        _printer.stopStyle(Style.KEYWORD);
        _printer.write("d");

        assertEquals(new ParsePosition(1, 4, 3),
                     _printer.getPosition());
        assertEquals("ab<keyword></keyword>d",
                     getText());
    }

    public void testStyle4()
    {
        // note that nested styles are not supported
        // however there is no need to stop styles before
        // starting new (this is done automatically
        _printer.startStyle(Style.NORMAL);
        _printer.write("ab");

        assertEquals(new ParsePosition(1, 3, 2),
                     _printer.getPosition());

        _printer.startTryMode();

        assertEquals(new ParsePosition(1, 3, 2),
                     _printer.getPosition());

        _printer.startStyle(Style.KEYWORD);
        _printer.write("c");

        assertEquals(new ParsePosition(1, 4, 3),
                     _printer.getPosition());

        _printer.startStyle(Style.NORMAL);
        _printer.stopTryMode(true);

        assertEquals(new ParsePosition(1, 4, 3),
                     _printer.getPosition());

        _printer.write("d");
        _printer.stopStyle(Style.NORMAL);

        assertEquals(new ParsePosition(1, 5, 4),
                     _printer.getPosition());
        assertEquals("ab<keyword>c</keyword>d",
                     getText());
    }

    public void testTry1()
    {
        _printer.write("ab");

        assertEquals(new ParsePosition(1, 3, 2),
                     _printer.getPosition());

        _printer.startTryMode();

        assertEquals(new ParsePosition(1, 3, 2),
                     _printer.getPosition());

        _printer.write("c");

        assertEquals(new ParsePosition(1, 4, 3),
                     _printer.getPosition());

        _printer.stopTryMode(false);

        assertEquals(new ParsePosition(1, 3, 2),
                     _printer.getPosition());

        _printer.write("d");

        assertEquals(new ParsePosition(1, 4, 3),
                     _printer.getPosition());
        assertEquals("abd",
                     getText());
    }

    public void testTry2()
    {
        _printer.getOptions().setIndentToken("\t");
        _printer.incIndent(2);

        assertEquals(new ParsePosition(1, 3, 2),
                     _printer.getPosition());

        _printer.writeln("ab");

        // includes indent in new line ("\t\tab\n\t\t")
        assertEquals(new ParsePosition(2, 3, 7),
                     _printer.getPosition());

        _printer.startTryMode();

        // same as above
        assertEquals(new ParsePosition(2, 3, 7),
                     _printer.getPosition());

        _printer.writeln();

        // includes indent in new line but not in the
        // previous one which is unneccessary in an empty line
        // ("\t\tab\n\n\t\t")
        assertEquals(new ParsePosition(3, 3, 8),
                     _printer.getPosition());

        _printer.stopTryMode(true);

        assertEquals(new ParsePosition(3, 3, 8),
                     _printer.getPosition());

        _printer.write("c");

        assertEquals(new ParsePosition(3, 4, 9),
                     _printer.getPosition());
        assertEquals("\t\tab\n\n\t\tc",
                     getText());
    }

    public void testTry3()
    {
        _printer.getOptions().setIndentToken(" ");
        _printer.incIndent(1);

        assertEquals(new ParsePosition(1, 2, 1),
                     _printer.getPosition());

        _printer.write("ab");

        assertEquals(new ParsePosition(1, 4, 3),
                     _printer.getPosition());

        _printer.startTryMode();

        assertEquals(new ParsePosition(1, 4, 3),
                     _printer.getPosition());

        _printer.writeln("c");

        // includes indent in new line
        assertEquals(new ParsePosition(2, 2, 6),
                     _printer.getPosition());

        _printer.startTryMode();

        assertEquals(new ParsePosition(2, 2, 6),
                     _printer.getPosition());

        _printer.write("d");

        assertEquals(new ParsePosition(2, 3, 7),
                     _printer.getPosition());

        _printer.stopTryMode(false);

        assertEquals(new ParsePosition(2, 2, 6),
                     _printer.getPosition());

        _printer.writeln("e");

        // includes indent in new line
        assertEquals(new ParsePosition(3, 2, 9),
                     _printer.getPosition());

        _printer.stopTryMode(true);

        assertEquals(new ParsePosition(3, 2, 9),
                     _printer.getPosition());

        _printer.write("f");

        assertEquals(new ParsePosition(3, 3, 10),
                     _printer.getPosition());
        assertEquals(" abc\n e\n f",
                     getText());
    }

    public void testTry4()
    {
        _printer.getOptions().setIndentToken(" ");
        _printer.incIndent(1);


        assertEquals(new ParsePosition(1, 2, 1),
                     _printer.getPosition());

        _printer.write("ab");

        assertEquals(new ParsePosition(1, 4, 3),
                     _printer.getPosition());

        _printer.startTryMode();

        assertEquals(new ParsePosition(1, 4, 3),
                     _printer.getPosition());

        _printer.writeln("c");

        // includes indent in new line
        assertEquals(new ParsePosition(2, 2, 6),
                     _printer.getPosition());

        _printer.startTryMode();

        assertEquals(new ParsePosition(2, 2, 6),
                     _printer.getPosition());

        _printer.write("d");

        assertEquals(new ParsePosition(2, 3, 7),
                     _printer.getPosition());

        _printer.stopTryMode(true);

        assertEquals(new ParsePosition(2, 3, 7),
                     _printer.getPosition());

        _printer.writeln("e");

        // includes indent in new line
        assertEquals(new ParsePosition(3, 2, 10),
                     _printer.getPosition());

        _printer.stopTryMode(false);

        assertEquals(new ParsePosition(1, 4, 3),
                     _printer.getPosition());

        _printer.write("f");

        assertEquals(new ParsePosition(1, 5, 4),
                     _printer.getPosition());
        assertEquals(" abf",
                     getText());
    }
}
