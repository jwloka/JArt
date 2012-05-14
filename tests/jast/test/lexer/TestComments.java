package jast.test.lexer;
import jast.parser.JavaTokenTypes;
import antlr.ANTLRException;

public class TestComments extends TestBase
{

    public TestComments(String name)
    {
        super(name);
    }

    public void testBlockComment1() throws ANTLRException
    {
        createAndFillLexer("/**/");
        checkNextToken(JavaTokenTypes.BLOCK_COMMENT,
                       "");
        assertTrue(!getNextToken());
    }

    public void testBlockComment10() throws ANTLRException
    {
        createAndFillLexer("/* EOF as unicode (\\uFFFF) is allowed in block comments */");
        checkNextToken(JavaTokenTypes.BLOCK_COMMENT,
                       " EOF as unicode (\uFFFF) is allowed in block comments ");
        assertTrue(!getNextToken());
    }

    public void testBlockComment11() throws ANTLRException
    {
        createAndFillLexer("/* Some unicode: \\uFFFF\\u000A in block comments */");
        checkNextToken(JavaTokenTypes.BLOCK_COMMENT,
                       " Some unicode: \uFFFF\n in block comments ");
        assertTrue(!getNextToken());
    }

    public void testBlockComment2() throws ANTLRException
    {
        createAndFillLexer("/*Simple test*/");
        checkNextToken(JavaTokenTypes.BLOCK_COMMENT,
                       "Simple test");
        assertTrue(!getNextToken());
    }

    public void testBlockComment3() throws ANTLRException
    {
        createAndFillLexer("  \t  /* \t \n */");
        checkNextToken(JavaTokenTypes.BLOCK_COMMENT,
                       " \t \n ");
        assertTrue(!getNextToken());
    }

    public void testBlockComment4() throws ANTLRException
    {
        createAndFillLexer("/* Another test /* still the same comment */");
        checkNextToken(JavaTokenTypes.BLOCK_COMMENT,
                       " Another test /* still the same comment ");
        assertTrue(!getNextToken());
    }

    public void testBlockComment5() throws ANTLRException
    {
        createAndFillLexer("/* Now some unicode : \u0401 */");
        checkNextToken(JavaTokenTypes.BLOCK_COMMENT,
                       " Now some unicode : \u0401 ");
        assertTrue(!getNextToken());
    }

    public void testBlockComment6() throws ANTLRException
    {
        // Note that we have to use two slashes in java string literals
        // in order to denote one slash in the resulting string
        createAndFillLexer("/* This is not unicode : \\\\uFFFF */");
        checkNextToken(JavaTokenTypes.BLOCK_COMMENT,
                       " This is not unicode : \\\\uFFFF ");
        assertTrue(!getNextToken());
    }

    public void testBlockComment7() throws ANTLRException
    {
        createAndFillLexer("/**/\t\n");
        checkNextToken(JavaTokenTypes.BLOCK_COMMENT,
                       "");
        assertTrue(!getNextToken());
    }

    public void testBlockComment8() throws ANTLRException
    {
        createAndFillLexer("/* Still a // block comment */");
        checkNextToken(JavaTokenTypes.BLOCK_COMMENT,
                       " Still a // block comment ");
        assertTrue(!getNextToken());
    }

    public void testBlockComment9() throws ANTLRException
    {
        createAndFillLexer("/* this comment /* // /** ends here: */");
        checkNextToken(JavaTokenTypes.BLOCK_COMMENT,
                       " this comment /* // /** ends here: ");
        assertTrue(!getNextToken());
    }

    public void testLineComment1() throws ANTLRException
    {
        createAndFillLexer("//");
        checkNextToken(JavaTokenTypes.LINE_COMMENT,
                       "");
        assertTrue(!getNextToken());
    }

    public void testLineComment2() throws ANTLRException
    {
        createAndFillLexer("// Simple test");
        checkNextToken(JavaTokenTypes.LINE_COMMENT,
                       " Simple test");
        assertTrue(!getNextToken());
    }

    public void testLineComment3() throws ANTLRException
    {
        createAndFillLexer("  \t  //\t");
        checkNextToken(JavaTokenTypes.LINE_COMMENT,
                       "\t");
        assertTrue(!getNextToken());
    }

    public void testLineComment4() throws ANTLRException
    {
        createAndFillLexer("// Another test // still the same comment");
        checkNextToken(JavaTokenTypes.LINE_COMMENT,
                       " Another test // still the same comment");
        assertTrue(!getNextToken());
    }

    public void testLineComment5() throws ANTLRException
    {
        createAndFillLexer("// Now some unicode : \u0401");
        checkNextToken(JavaTokenTypes.LINE_COMMENT,
                       " Now some unicode : \u0401");
        assertTrue(!getNextToken());
    }

    public void testLineComment6() throws ANTLRException
    {
        createAndFillLexer("// /* Still a line comment */");
        checkNextToken(JavaTokenTypes.LINE_COMMENT,
                       " /* Still a line comment */");
        assertTrue(!getNextToken());
    }

    public void testLineComment7() throws ANTLRException
    {
        createAndFillLexer("// Here comes a newline :\\u000a// Another comment");
        checkNextToken(JavaTokenTypes.LINE_COMMENT,
                       " Here comes a newline :");
        checkNextToken(JavaTokenTypes.LINE_COMMENT,
                       " Another comment");
        assertTrue(!getNextToken());
    }

    public void testLineComment8() throws ANTLRException
    {
        createAndFillLexer("// EOF as unicode (\\uFFFF) is allowed in line comments");
        checkNextToken(JavaTokenTypes.LINE_COMMENT,
                       " EOF as unicode (\uFFFF) is allowed in line comments");
        assertTrue(!getNextToken());
    }
}
