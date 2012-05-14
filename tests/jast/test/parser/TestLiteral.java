package jast.test.parser;
import jast.ast.nodes.BooleanLiteral;
import jast.ast.nodes.CharacterLiteral;
import jast.ast.nodes.FloatingPointLiteral;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.NullLiteral;
import jast.ast.nodes.StringLiteral;
import antlr.ANTLRException;

public class TestLiteral extends TestBase
{

    public TestLiteral(String name)
    {
        super(name);
    }

    public void testBooleanLiteral1() throws ANTLRException
    {
        setupParser("true");

        BooleanLiteral result = (BooleanLiteral)_parser.literal();

        assertNotNull(result);
        assertTrue(result.getValue());
    }

    public void testBooleanLiteral2() throws ANTLRException
    {
        setupParser("false");

        BooleanLiteral result = (BooleanLiteral)_parser.literal();

        assertNotNull(result);
        assertTrue(!result.getValue());
    }

    public void testBooleanLiteral3()
    {
        setupParser("True");
        try
        {
            _parser.literal();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testBooleanLiteral4()
    {
        setupParser("falseValue");
        try
        {
            _parser.literal();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testCharacterLiteral1() throws ANTLRException
    {
        setupParser("'a'");

        CharacterLiteral result = (CharacterLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals("'a'",
                     result.asString());
        assertEquals('a',
                     result.getValue());
    }

    public void testCharacterLiteral10() throws ANTLRException
    {
        setupParser("'\\uuu0061'");        // u0061 = a

        CharacterLiteral result = (CharacterLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals("'\\uuu0061'",
                     result.asString());
        assertEquals('a',
                     result.getValue());
    }

    public void testCharacterLiteral2() throws ANTLRException
    {
        setupParser("'\\n'");

        CharacterLiteral result = (CharacterLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals("'\\n'",
                     result.asString());
        assertEquals('\n',
                     result.getValue());
    }

    public void testCharacterLiteral3() throws ANTLRException
    {
        setupParser("'\\141'");

        CharacterLiteral result = (CharacterLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals("'\\141'",
                     result.asString());
        assertEquals('a',
                     result.getValue());
    }

    public void testCharacterLiteral4() throws ANTLRException
    {
        setupParser("'\\uFFFE'");

        CharacterLiteral result = (CharacterLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals("'\\uFFFE'",
                     result.asString());
        assertEquals('\uFFFE',
                     result.getValue());
    }

    public void testCharacterLiteral5()
    {
        setupParser("a");
        try
        {
            _parser.literal();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testCharacterLiteral6()
    {
        setupParser("'ab'");
        try
        {
            _parser.literal();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testCharacterLiteral7() throws ANTLRException
    {
        setupParser("\\u0027a'");        // u0027 = '

        CharacterLiteral result = (CharacterLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals("'a'",
                     result.asString());
        assertEquals('a',
                     result.getValue());
    }

    public void testCharacterLiteral8()
    {
        setupParser("\\47a'");
        try
        {
            _parser.literal();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testCharacterLiteral9()
    {
        setupParser("'");
        try
        {
            _parser.literal();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testFloatLiteral1() throws ANTLRException
    {
        setupParser("0.0");

        FloatingPointLiteral result = (FloatingPointLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals("0.0",
                     result.asString());
        assertEquals(0.0,
                     result.getValue(),
                     0.0);
        assertTrue(result.isDouble());
    }

    public void testFloatLiteral10() throws ANTLRException
    {
        String number = Double.toString(Double.MIN_VALUE) + "0d";

        setupParser(number);

        FloatingPointLiteral result = (FloatingPointLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals(number,
                     result.asString());
        assertEquals(0.0,
                     result.getValue(),
                     0.0);
        assertTrue(result.isDouble());
    }

    public void testFloatLiteral11()
    {
        setupParser("1" + Double.toString(Double.MAX_VALUE) + "f");
        try
        {
            _parser.literal();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testFloatLiteral2() throws ANTLRException
    {
        setupParser("0.0f");

        FloatingPointLiteral result = (FloatingPointLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals("0.0f",
                     result.asString());
        assertEquals(0.0,
                     result.getValue(),
                     0.0);
        assertTrue(!result.isDouble());
    }

    public void testFloatLiteral3() throws ANTLRException
    {
        setupParser("0.0d");

        FloatingPointLiteral result = (FloatingPointLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals("0.0d",
                     result.asString());
        assertEquals(0.0,
                     result.getValue(),
                     0.0);
        assertTrue(result.isDouble());
    }

    public void testFloatLiteral4() throws ANTLRException
    {
        String number = Float.toString(Float.MIN_VALUE) + "f";

        setupParser(number);

        FloatingPointLiteral result = (FloatingPointLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals(number,
                     result.asString());
        assertEquals(Float.MIN_VALUE,
                     result.getValue(),
                     0.0);
        assertTrue(!result.isDouble());
    }

    public void testFloatLiteral5() throws ANTLRException
    {
        String number = Float.toString(Float.MAX_VALUE) + "f";

        setupParser(number);

        FloatingPointLiteral result = (FloatingPointLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals(number,
                     result.asString());
        assertEquals(Float.MAX_VALUE,
                     result.getValue(),
                     0.0);
        assertTrue(!result.isDouble());
    }

    public void testFloatLiteral6() throws ANTLRException
    {
        String number = Double.toString(Double.MIN_VALUE);

        setupParser(number);

        FloatingPointLiteral result = (FloatingPointLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals(number,
                     result.asString());
        assertEquals(Double.MIN_VALUE,
                     result.getValue(),
                     0.0);
        assertTrue(result.isDouble());
    }

    public void testFloatLiteral7() throws ANTLRException
    {
        String number = Double.toString(Double.MAX_VALUE);

        setupParser(number);

        FloatingPointLiteral result = (FloatingPointLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals(number,
                     result.asString());
        assertEquals(Double.MAX_VALUE,
                     result.getValue(),
                     0.0);
        assertTrue(result.isDouble());
    }

    public void testFloatLiteral8() throws ANTLRException
    {
        String number = Double.toString(Double.MIN_VALUE) + "f";

        setupParser(number);

        FloatingPointLiteral result = (FloatingPointLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals(number,
                     result.asString());
        assertEquals(0.0,
                     result.getValue(),
                     0.0);
        assertTrue(!result.isDouble());
    }

    public void testFloatLiteral9()
    {
        setupParser(Double.toString(Double.MAX_VALUE) + "f");
        try
        {
            _parser.literal();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testIntegerLiteral1() throws ANTLRException
    {
        setupParser("0");

        IntegerLiteral result = (IntegerLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals("0",
                     result.asString());
        assertEquals(0l,
                     result.getValue());
        assertTrue(!result.isLong());
    }

    public void testIntegerLiteral10() throws ANTLRException
    {
        String number = "0" + Integer.toOctalString(Integer.MAX_VALUE);

        setupParser(number);

        IntegerLiteral result = (IntegerLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals(number,
                     result.asString());
        assertEquals(Integer.MAX_VALUE,
                     result.getValue());
        assertTrue(!result.isLong());
    }

    public void testIntegerLiteral11() throws ANTLRException
    {
        String number = Integer.toString(Integer.MIN_VALUE).substring(1);

        setupParser(number);

        IntegerLiteral result = (IntegerLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals(number,
                     result.asString());
        assertEquals(-1,
                     result.getValue());
        assertTrue(!result.isLong());
    }

    public void testIntegerLiteral12() throws ANTLRException
    {
        String number = Integer.toString(Integer.MAX_VALUE);

        setupParser(number);

        IntegerLiteral result = (IntegerLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals(number,
                     result.asString());
        assertEquals(Integer.MAX_VALUE,
                     result.getValue());
        assertTrue(!result.isLong());
    }

    public void testIntegerLiteral13() throws ANTLRException
    {
        String number = "0x" + Long.toHexString(Long.MIN_VALUE) + "l";

        setupParser(number);

        IntegerLiteral result = (IntegerLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals(number,
                     result.asString());
        assertEquals(-1,
                     result.getValue());
        assertTrue(result.isLong());
    }

    public void testIntegerLiteral14() throws ANTLRException
    {
        String number = "0x" + Long.toHexString(Long.MAX_VALUE) + "l";

        setupParser(number);

        IntegerLiteral result = (IntegerLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals(number,
                     result.asString());
        assertEquals(Long.MAX_VALUE,
                     result.getValue());
        assertTrue(result.isLong());
    }

    public void testIntegerLiteral15() throws ANTLRException
    {
        String number = "0" + Long.toOctalString(Long.MIN_VALUE) + "l";

        setupParser(number);

        IntegerLiteral result = (IntegerLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals(number,
                     result.asString());
        assertEquals(-1,
                     result.getValue());
        assertTrue(result.isLong());
    }

    public void testIntegerLiteral16() throws ANTLRException
    {
        String number = "0" + Long.toOctalString(Long.MAX_VALUE) + "l";

        setupParser(number);

        IntegerLiteral result = (IntegerLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals(number,
                     result.asString());
        assertEquals(Long.MAX_VALUE,
                     result.getValue());
        assertTrue(result.isLong());
    }

    public void testIntegerLiteral17() throws ANTLRException
    {
        String number = Long.toString(Long.MIN_VALUE).substring(1) + "l";

        setupParser(number);

        IntegerLiteral result = (IntegerLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals(number,
                     result.asString());
        assertEquals(-1,
                     result.getValue());
        assertTrue(result.isLong());
    }

    public void testIntegerLiteral18() throws ANTLRException
    {
        String number = Long.toString(Long.MAX_VALUE) + "l";

        setupParser(number);

        IntegerLiteral result = (IntegerLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals(number,
                     result.asString());
        assertEquals(Long.MAX_VALUE,
                     result.getValue());
        assertTrue(result.isLong());
    }

    public void testIntegerLiteral2() throws ANTLRException
    {
        setupParser("12l");

        IntegerLiteral result = (IntegerLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals("12l",
                     result.asString());
        assertEquals(12l,
                     result.getValue());
        assertTrue(result.isLong());
    }

    public void testIntegerLiteral3() throws ANTLRException
    {
        setupParser("0x01");

        IntegerLiteral result = (IntegerLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals("0x01",
                     result.asString());
        assertEquals(1l,
                     result.getValue());
        assertTrue(!result.isLong());
    }

    public void testIntegerLiteral4() throws ANTLRException
    {
        setupParser("0x00FFL");

        IntegerLiteral result = (IntegerLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals("0x00FFL",
                     result.asString());
        assertEquals(255l,
                     result.getValue());
        assertTrue(result.isLong());
    }

    public void testIntegerLiteral5() throws ANTLRException
    {
        setupParser("011");

        IntegerLiteral result = (IntegerLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals("011",
                     result.asString());
        assertEquals(9,
                     result.getValue());
        assertTrue(!result.isLong());
    }

    public void testIntegerLiteral6() throws ANTLRException
    {
        setupParser("0377l");

        IntegerLiteral result = (IntegerLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals("0377l",
                     result.asString());
        assertEquals(255l,
                     result.getValue());
        assertTrue(result.isLong());
    }

    public void testIntegerLiteral7() throws ANTLRException
    {
        String number = "0x" + Integer.toHexString(Integer.MIN_VALUE);

        setupParser(number);

        IntegerLiteral result = (IntegerLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals(number,
                     result.asString());
        assertEquals(-1,
                     result.getValue());
        assertTrue(!result.isLong());
    }

    public void testIntegerLiteral8() throws ANTLRException
    {
        String number = "0x" + Integer.toHexString(Integer.MAX_VALUE);

        setupParser(number);

        IntegerLiteral result = (IntegerLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals(number,
                     result.asString());
        assertEquals(Integer.MAX_VALUE,
                     result.getValue());
        assertTrue(!result.isLong());
    }

    public void testIntegerLiteral9() throws ANTLRException
    {
        String number = "0" + Integer.toOctalString(Integer.MIN_VALUE);

        setupParser(number);

        IntegerLiteral result = (IntegerLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals(number,
                     result.asString());
        assertEquals(-1,
                     result.getValue());
        assertTrue(!result.isLong());
    }

    public void testNullLiteral1() throws ANTLRException
    {
        setupParser("null");

        NullLiteral result = (NullLiteral)_parser.literal();

        assertNotNull(result);
    }

    public void testNullLiteral2()
    {
        setupParser("Null");
        try
        {
            _parser.literal();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testNullLiteral3()
    {
        setupParser("nullValue");
        try
        {
            _parser.literal();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testStringLiteral1() throws ANTLRException
    {
        setupParser("\"\"");

        StringLiteral result = (StringLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals("\"\"",
                     result.asString());
        assertEquals("",
                     result.getValue());
    }

    public void testStringLiteral2() throws ANTLRException
    {
        setupParser("\"a\\142\\u0063\\n\"");

        StringLiteral result = (StringLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals("\"a\\142\\u0063\\n\"",
                     result.asString());
        assertEquals("abc\n",
                     result.getValue());
    }

    public void testStringLiteral3()
    {
        setupParser("abc");
        try
        {
            _parser.literal();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testStringLiteral4()
    {
        setupParser("\"\n\"");
        try
        {
            _parser.literal();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testStringLiteral5()
    {
        setupParser("\"");
        try
        {
            _parser.literal();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testStringLiteral6() throws ANTLRException
    {
        setupParser("\\u0022abc\"");        // u0022 = "

        StringLiteral result = (StringLiteral)_parser.literal();

        assertNotNull(result);
        assertEquals("\"abc\"",
                     result.asString());
        assertEquals("abc",
                     result.getValue());
    }

    public void testStringLiteral7()
    {
        setupParser("\\42abc\"");
        try
        {
            _parser.literal();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }
}
