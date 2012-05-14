package jast.test.lexer;
import jast.parser.JavaTokenTypes;
import antlr.ANTLRException;

public class TestIdentifier extends TestBase
{

    public TestIdentifier(String name)
    {
        super(name);
    }

    public void testIdentifier1() throws ANTLRException
    {
        createAndFillLexer("abc");
        checkNextToken(JavaTokenTypes.IDENTIFIER,
                       "abc");
        assertTrue(!getNextToken());
    }

    public void testIdentifier2() throws ANTLRException
    {
        createAndFillLexer("_2");
        checkNextToken(JavaTokenTypes.IDENTIFIER,
                       "_2");
        assertTrue(!getNextToken());
    }

    public void testIdentifier3() throws ANTLRException
    {
        createAndFillLexer("$b");
        checkNextToken(JavaTokenTypes.IDENTIFIER,
                       "$b");
        assertTrue(!getNextToken());
    }

    public void testIdentifier4() throws ANTLRException
    {
        createAndFillLexer("\u0391"); // alpha
        checkNextToken(JavaTokenTypes.IDENTIFIER,
                       "\u0391");
        assertTrue(!getNextToken());
    }

    public void testIdentifier5() throws ANTLRException
    {
        createAndFillLexer("a\uFEFFb"); // zero width no-break space
        checkNextToken(JavaTokenTypes.IDENTIFIER,
                       "a\uFEFFb");
        assertTrue(!getNextToken());
    }

    public void testIdentifier6() throws ANTLRException
    {
        createAndFillLexer("//\\u000aabc");
        checkNextToken(JavaTokenTypes.LINE_COMMENT,
                       "");
        checkNextToken(JavaTokenTypes.IDENTIFIER,
                       "abc");
        assertTrue(!getNextToken());
    }
}
