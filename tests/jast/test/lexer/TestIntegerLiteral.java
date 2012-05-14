package jast.test.lexer;
import jast.parser.JavaTokenTypes;
import antlr.ANTLRException;

public class TestIntegerLiteral extends TestBase
{

    public TestIntegerLiteral(String name)
    {
        super(name);
    }

    public void testDecimalIntegerLiteral1() throws ANTLRException
    {
        createAndFillLexer("0");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "0");
        assertTrue(!getNextToken());
    }

    public void testDecimalIntegerLiteral2() throws ANTLRException
    {
        createAndFillLexer("0l");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "0l");
        assertTrue(!getNextToken());
    }

    public void testDecimalIntegerLiteral3() throws ANTLRException
    {
        createAndFillLexer("0L");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "0L");
        assertTrue(!getNextToken());
    }

    public void testDecimalIntegerLiteral4() throws ANTLRException
    {
        createAndFillLexer("1");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "1");
        assertTrue(!getNextToken());
    }

    public void testDecimalIntegerLiteral5() throws ANTLRException
    {
        createAndFillLexer("7l");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "7l");
        assertTrue(!getNextToken());
    }

    public void testDecimalIntegerLiteral6() throws ANTLRException
    {
        createAndFillLexer("9L");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "9L");
        assertTrue(!getNextToken());
    }

    public void testDecimalIntegerLiteral7() throws ANTLRException
    {
        createAndFillLexer("123");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "123");
        assertTrue(!getNextToken());
    }

    public void testDecimalIntegerLiteral8() throws ANTLRException
    {
        createAndFillLexer("92035682l");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "92035682l");
        assertTrue(!getNextToken());
    }

    public void testDecimalIntegerLiteral9() throws ANTLRException
    {
        createAndFillLexer("102592792035682L");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "102592792035682L");
        assertTrue(!getNextToken());
    }

    public void testHexIntegerLiteral1() throws ANTLRException
    {
        createAndFillLexer("0x0");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "0x0");
        assertTrue(!getNextToken());
    }

    public void testHexIntegerLiteral10() throws ANTLRException
    {
        // This is meant to be a hex numeral which is of course wrong
        // The lexer, however, should see a hex numeral (0x0) followed by
        // a floating-point numeral (.4)
        createAndFillLexer("0x0.4");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "0x0");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       ".4");
        assertTrue(!getNextToken());
    }

    public void testHexIntegerLiteral2() throws ANTLRException
    {
        createAndFillLexer("0X0");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "0X0");
        assertTrue(!getNextToken());
    }

    public void testHexIntegerLiteral3() throws ANTLRException
    {
        createAndFillLexer("0x0234F");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "0x0234F");
        assertTrue(!getNextToken());
    }

    public void testHexIntegerLiteral4() throws ANTLRException
    {
        createAndFillLexer("0xaef");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "0xaef");
        assertTrue(!getNextToken());
    }

    public void testHexIntegerLiteral5() throws ANTLRException
    {
        createAndFillLexer("0X12l");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "0X12l");
        assertTrue(!getNextToken());
    }

    public void testHexIntegerLiteral6() throws ANTLRException
    {
        createAndFillLexer("0XaFL");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "0XaFL");
        assertTrue(!getNextToken());
    }

    public void testHexIntegerLiteral7() throws ANTLRException
    {
        // This is meant to be a float numeral with hex numbers which
        // is of course wrong. The lexer, however, should see a hex
        // numeral (0x0) followed by a float numeral (.2)
        createAndFillLexer("0x0.2");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "0x0");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       ".2");
        assertTrue(!getNextToken());
    }

    public void testHexIntegerLiteral8()
    {
        // This is meant to be a float numeral with hex numbers which
        // is of course wrong.
        // As opposed to a the previous test, this one should also
        // be an error for the lexer (no hex digits after the x)
        createAndFillLexer("0x.9e3f");
        try
        {
            getNextToken();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testHexIntegerLiteral9() throws ANTLRException
    {
        // This is meant to be a hex numeral which is of course wrong.
        // The lexer, however, should see an octal numeral (00) followed by
        // an identifier (x2a)
        createAndFillLexer("00x2a");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "00");
        checkNextToken(JavaTokenTypes.IDENTIFIER,
                       "x2a");
        assertTrue(!getNextToken());
    }

    public void testOctalIntegerLiteral1() throws ANTLRException
    {
        createAndFillLexer("000");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "000");
        assertTrue(!getNextToken());
    }

    public void testOctalIntegerLiteral2() throws ANTLRException
    {
        createAndFillLexer("000l");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "000l");
        assertTrue(!getNextToken());
    }

    public void testOctalIntegerLiteral3() throws ANTLRException
    {
        createAndFillLexer("000L");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "000L");
        assertTrue(!getNextToken());
    }

    public void testOctalIntegerLiteral4() throws ANTLRException
    {
        createAndFillLexer("0777");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "0777");
        assertTrue(!getNextToken());
    }

    public void testOctalIntegerLiteral5() throws ANTLRException
    {
        createAndFillLexer("0777l");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "0777l");
        assertTrue(!getNextToken());
    }

    public void testOctalIntegerLiteral6() throws ANTLRException
    {
        createAndFillLexer("0777L");
        checkNextToken(JavaTokenTypes.INTEGER_LITERAL,
                       "0777L");
        assertTrue(!getNextToken());
    }

    public void testOctalIntegerLiteral7()
    {
        createAndFillLexer("0778");
        try
        {
            getNextToken();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testOctalIntegerLiteral8()
    {
        createAndFillLexer("0779");
        try
        {
            getNextToken();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }
}
