package jast.test.lexer;
import jast.parser.JavaTokenTypes;
import antlr.ANTLRException;

public class TestFloatingPointLiteral extends TestBase
{

    public TestFloatingPointLiteral(String name)
    {
        super(name);
    }

    public void testFloatingPointLiteral1() throws ANTLRException
    {
        createAndFillLexer("0f");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0f");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral10() throws ANTLRException
    {
        createAndFillLexer("0E3f");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0E3f");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral11() throws ANTLRException
    {
        createAndFillLexer("0e3F");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0e3F");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral12() throws ANTLRException
    {
        createAndFillLexer("0E3d");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0E3d");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral13() throws ANTLRException
    {
        createAndFillLexer("0e3D");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0e3D");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral14() throws ANTLRException
    {
        createAndFillLexer("009e-3");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "009e-3");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral15() throws ANTLRException
    {
        createAndFillLexer("009e-3f");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "009e-3f");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral16() throws ANTLRException
    {
        createAndFillLexer("009E-3F");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "009E-3F");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral17() throws ANTLRException
    {
        createAndFillLexer("009e-3d");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "009e-3d");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral18() throws ANTLRException
    {
        createAndFillLexer("009E-3D");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "009E-3D");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral19() throws ANTLRException
    {
        // This is meant to be a long numeral with an exponent which
        // is of course wrong.
        // The lexer, however, should see a floating-point numeral (0e1)
        // followed by an identifier (l)
        createAndFillLexer("0e1l");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0e1");
        checkNextToken(JavaTokenTypes.IDENTIFIER,
                       "l");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral2() throws ANTLRException
    {
        createAndFillLexer("0F");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0F");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral20() throws ANTLRException
    {
        // This is meant to be a long numeral with an exponent which
        // is of course wrong.
        // The lexer, however, should see a floating-point numeral (0E-1)
        // followed by an identifier (L)
        createAndFillLexer("0E-1L");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0E-1");
        checkNextToken(JavaTokenTypes.IDENTIFIER,
                       "L");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral21() throws ANTLRException
    {
        createAndFillLexer(".0");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       ".0");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral22() throws ANTLRException
    {
        createAndFillLexer(".0f");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       ".0f");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral23() throws ANTLRException
    {
        createAndFillLexer(".0F");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       ".0F");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral24() throws ANTLRException
    {
        createAndFillLexer(".0d");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       ".0d");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral25() throws ANTLRException
    {
        createAndFillLexer(".0D");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       ".0D");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral26() throws ANTLRException
    {
        createAndFillLexer(".0e1");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       ".0e1");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral27() throws ANTLRException
    {
        createAndFillLexer(".0e-1f");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       ".0e-1f");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral28() throws ANTLRException
    {
        createAndFillLexer(".0E1F");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       ".0E1F");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral29() throws ANTLRException
    {
        createAndFillLexer(".0e-1d");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       ".0e-1d");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral3() throws ANTLRException
    {
        createAndFillLexer("0d");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0d");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral30() throws ANTLRException
    {
        createAndFillLexer(".0e-1D");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       ".0e-1D");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral31() throws ANTLRException
    {
        createAndFillLexer(".091");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       ".091");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral32() throws ANTLRException
    {
        createAndFillLexer(".091f");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       ".091f");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral33() throws ANTLRException
    {
        createAndFillLexer(".091F");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       ".091F");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral34() throws ANTLRException
    {
        createAndFillLexer(".091d");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       ".091d");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral35() throws ANTLRException
    {
        createAndFillLexer(".091D");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       ".091D");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral36() throws ANTLRException
    {
        createAndFillLexer("0.");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0.");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral37() throws ANTLRException
    {
        createAndFillLexer("0.f");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0.f");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral38() throws ANTLRException
    {
        createAndFillLexer("0.F");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0.F");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral39() throws ANTLRException
    {
        createAndFillLexer("0.d");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0.d");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral4() throws ANTLRException
    {
        createAndFillLexer("0D");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0D");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral40() throws ANTLRException
    {
        createAndFillLexer("0.D");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0.D");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral41() throws ANTLRException
    {
        createAndFillLexer("1.091");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "1.091");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral42() throws ANTLRException
    {
        createAndFillLexer("1.091f");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "1.091f");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral43() throws ANTLRException
    {
        createAndFillLexer("001.091F");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "001.091F");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral44() throws ANTLRException
    {
        createAndFillLexer("001.091d");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "001.091d");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral45() throws ANTLRException
    {
        createAndFillLexer("1.091D");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "1.091D");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral46() throws ANTLRException
    {
        createAndFillLexer("0.e0");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0.e0");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral47() throws ANTLRException
    {
        createAndFillLexer("0.e-0f");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0.e-0f");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral48() throws ANTLRException
    {
        createAndFillLexer("0.e009F");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0.e009F");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral49() throws ANTLRException
    {
        createAndFillLexer("0.e-009d");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0.e-009d");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral5() throws ANTLRException
    {
        createAndFillLexer("009f");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "009f");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral50() throws ANTLRException
    {
        createAndFillLexer("0.e0D");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0.e0D");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral51() throws ANTLRException
    {
        // This is meant to be a numeral with a hex exponent which
        // is of course wrong.
        // The lexer, however, should see a floating-point numeral (0.1e4)
        // followed by an identifier (x3)
        createAndFillLexer("0.1e4x3");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0.1e4");
        checkNextToken(JavaTokenTypes.IDENTIFIER,
                       "x3");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral52() throws ANTLRException
    {
        // This is meant to be a numeral with a hex exponent which
        // is of course wrong.
        // The lexer, however, should see a floating-point numeral (0.1e0)
        // followed by an identifier (x3)
        createAndFillLexer("0.1e0x3");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0.1e0");
        checkNextToken(JavaTokenTypes.IDENTIFIER,
                       "x3");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral53() throws ANTLRException
    {
        // This is meant to be a numeral with a floating-point exponent
        // which is of course wrong.
        // The lexer, however, should see a floating-point numeral (0.1e4)
        // followed by another floating-point numeral (.3)
        createAndFillLexer("0.1e4.3");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0.1e4");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       ".3");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral54() throws ANTLRException
    {
        // This is meant to be a hex floating-point numeral which
        // is of course wrong.
        // The lexer, however, should see a floating-point numeral (0.9)
        // followed by an identifier (x3)
        createAndFillLexer("0.9x3");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0.9");
        checkNextToken(JavaTokenTypes.IDENTIFIER,
                       "x3");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral55() throws ANTLRException
    {
        // This is meant to be a hex floating-point numeral which
        // is of course wrong.
        // The lexer, however, should see a floating-point numeral (0.0)
        // followed by an identifier (x3)
        createAndFillLexer("0.0x3");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0.0");
        checkNextToken(JavaTokenTypes.IDENTIFIER,
                       "x3");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral56()
    {
        createAndFillLexer("0.0ef");
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

    public void testFloatingPointLiteral6() throws ANTLRException
    {
        createAndFillLexer("009F");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "009F");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral7() throws ANTLRException
    {
        createAndFillLexer("009d");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "009d");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral8() throws ANTLRException
    {
        createAndFillLexer("009D");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "009D");
        assertTrue(!getNextToken());
    }

    public void testFloatingPointLiteral9() throws ANTLRException
    {
        createAndFillLexer("0e3");
        checkNextToken(JavaTokenTypes.FLOATING_POINT_LITERAL,
                       "0e3");
        assertTrue(!getNextToken());
    }
}
