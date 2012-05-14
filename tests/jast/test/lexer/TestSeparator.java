package jast.test.lexer;
import jast.parser.JavaTokenTypes;
import antlr.ANTLRException;

public class TestSeparator extends TestBase
{

    public TestSeparator(String name)
    {
        super(name);
    }

    public void testSeparator1() throws ANTLRException
    {
        createAndFillLexer("(");
        checkNextToken(JavaTokenTypes.SEP_OPENING_PARENTHESIS,
                       "(");
        assertTrue(!getNextToken());
    }

    public void testSeparator2() throws ANTLRException
    {
        createAndFillLexer(")");
        checkNextToken(JavaTokenTypes.SEP_CLOSING_PARENTHESIS,
                       ")");
        assertTrue(!getNextToken());
    }

    public void testSeparator3() throws ANTLRException
    {
        createAndFillLexer("[");
        checkNextToken(JavaTokenTypes.SEP_OPENING_BRACKET,
                       "[");
        assertTrue(!getNextToken());
    }

    public void testSeparator4() throws ANTLRException
    {
        createAndFillLexer("]");
        checkNextToken(JavaTokenTypes.SEP_CLOSING_BRACKET,
                       "]");
        assertTrue(!getNextToken());
    }

    public void testSeparator5() throws ANTLRException
    {
        createAndFillLexer("{");
        checkNextToken(JavaTokenTypes.SEP_OPENING_BRACE,
                       "{");
        assertTrue(!getNextToken());
    }

    public void testSeparator6() throws ANTLRException
    {
        createAndFillLexer("}");
        checkNextToken(JavaTokenTypes.SEP_CLOSING_BRACE,
                       "}");
        assertTrue(!getNextToken());
    }

    public void testSeparator7() throws ANTLRException
    {
        createAndFillLexer(";");
        checkNextToken(JavaTokenTypes.SEP_SEMICOLON,
                       ";");
        assertTrue(!getNextToken());
    }

    public void testSeparator8() throws ANTLRException
    {
        createAndFillLexer(",");
        checkNextToken(JavaTokenTypes.SEP_COMMA,
                       ",");
        assertTrue(!getNextToken());
    }
}
