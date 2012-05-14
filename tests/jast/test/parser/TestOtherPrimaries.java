package jast.test.parser;
import jast.ast.nodes.BooleanLiteral;
import jast.ast.nodes.CharacterLiteral;
import jast.ast.nodes.FloatingPointLiteral;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.NullLiteral;
import jast.ast.nodes.StringLiteral;
import antlr.ANTLRException;

public class TestOtherPrimaries extends TestBase
{

    public TestOtherPrimaries(String name)
    {
        super(name);
    }

    public void testBooleanLiteral1() throws ANTLRException
    {
        setupParser("true");

        BooleanLiteral result = (BooleanLiteral)_parser.primary();

        assertNotNull(result);
        assertTrue(result.getValue());
    }

    public void testBooleanLiteral2() throws ANTLRException
    {
        setupParser("false");

        BooleanLiteral result = (BooleanLiteral)_parser.primary();

        assertNotNull(result);
        assertTrue(!result.getValue());
    }

    public void testCharacterLiteral() throws ANTLRException
    {
        setupParser("'a'");

        CharacterLiteral result = (CharacterLiteral)_parser.primary();

        assertNotNull(result);
        assertEquals("'a'",
                     result.asString());
        assertEquals('a',
                     result.getValue());
    }

    public void testFloatLiteral() throws ANTLRException
    {
        setupParser("0.0f");

        FloatingPointLiteral result = (FloatingPointLiteral)_parser.primary();

        assertNotNull(result);
        assertEquals("0.0f",
                     result.asString());
        assertEquals(0.0,
                     result.getValue(),
                     0.0);
        assertTrue(!result.isDouble());
    }

    public void testIntegerLiteral() throws ANTLRException
    {
        setupParser("0");

        IntegerLiteral result = (IntegerLiteral)_parser.primary();

        assertNotNull(result);
        assertEquals("0",
                     result.asString());
        assertEquals(0l,
                     result.getValue());
        assertTrue(!result.isLong());
    }

    public void testNullLiteral() throws ANTLRException
    {
        setupParser("null");

        NullLiteral result = (NullLiteral)_parser.primary();

        assertNotNull(result);
    }

    public void testStringLiteral() throws ANTLRException
    {
        setupParser("\"a\"");

        StringLiteral result = (StringLiteral)_parser.primary();

        assertNotNull(result);
        assertEquals("\"a\"",
                     result.asString());
        assertEquals("a",
                     result.getValue());
    }

}
