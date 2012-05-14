package jast.test.lexer;
import jast.parser.JavaTokenTypes;
import antlr.ANTLRException;

public class TestCharacters extends TestBase
{

    public TestCharacters(String name)
    {
        super(name);
    }

    public void testCharacterLiteral1() throws ANTLRException
    {
        createAndFillLexer("' '");
        checkNextToken(JavaTokenTypes.CHARACTER_LITERAL,
                       "' '");
        assertTrue(!getNextToken());
    }

    public void testCharacterLiteral10() throws ANTLRException
    {
        createAndFillLexer("'\\uFEAD'");
        checkNextToken(JavaTokenTypes.CHARACTER_LITERAL,
                       "'\\uFEAD'");
        assertTrue(!getNextToken());
    }

    public void testCharacterLiteral11() throws ANTLRException
    {
        createAndFillLexer("'\\uufead'");
        checkNextToken(JavaTokenTypes.CHARACTER_LITERAL,
                       "'\\uufead'");
        assertTrue(!getNextToken());
    }

    public void testCharacterLiteral12() throws ANTLRException
    {
        createAndFillLexer("'\\0'");
        checkNextToken(JavaTokenTypes.CHARACTER_LITERAL,
                       "'\\0'");
        assertTrue(!getNextToken());
    }

    public void testCharacterLiteral13() throws ANTLRException
    {
        createAndFillLexer("'\\37'");
        checkNextToken(JavaTokenTypes.CHARACTER_LITERAL,
                       "'\\37'");
        assertTrue(!getNextToken());
    }

    public void testCharacterLiteral14() throws ANTLRException
    {
        createAndFillLexer("'\\377'");
        checkNextToken(JavaTokenTypes.CHARACTER_LITERAL,
                       "'\\377'");
        assertTrue(!getNextToken());
    }

    public void testCharacterLiteral15() throws ANTLRException
    {
        createAndFillLexer("'\\7'");
        checkNextToken(JavaTokenTypes.CHARACTER_LITERAL,
                       "'\\7'");
        assertTrue(!getNextToken());
    }

    public void testCharacterLiteral16() throws ANTLRException
    {
        createAndFillLexer("'\\77'");
        checkNextToken(JavaTokenTypes.CHARACTER_LITERAL,
                       "'\\77'");
        assertTrue(!getNextToken());
    }

    public void testCharacterLiteral17() throws ANTLRException
    {
        createAndFillLexer("'\\uFFFF'");
        checkNextToken(JavaTokenTypes.CHARACTER_LITERAL,
                       "'\\uFFFF'");
        assertTrue(!getNextToken());
    }

    public void testCharacterLiteral2() throws ANTLRException
    {
        createAndFillLexer("'\"'");
        checkNextToken(JavaTokenTypes.CHARACTER_LITERAL,
                       "'\"'");
        assertTrue(!getNextToken());
    }

    public void testCharacterLiteral3() throws ANTLRException
    {
        createAndFillLexer("'\\''");
        checkNextToken(JavaTokenTypes.CHARACTER_LITERAL,
                       "'\\''");
        assertTrue(!getNextToken());
    }

    public void testCharacterLiteral4() throws ANTLRException
    {
        createAndFillLexer("'\\\\'");
        checkNextToken(JavaTokenTypes.CHARACTER_LITERAL,
                       "'\\\\'");
        assertTrue(!getNextToken());
    }

    public void testCharacterLiteral5() throws ANTLRException
    {
        createAndFillLexer("'\\b'");
        checkNextToken(JavaTokenTypes.CHARACTER_LITERAL,
                       "'\\b'");
        assertTrue(!getNextToken());
    }

    public void testCharacterLiteral6() throws ANTLRException
    {
        createAndFillLexer("'\\f'");
        checkNextToken(JavaTokenTypes.CHARACTER_LITERAL,
                       "'\\f'");
        assertTrue(!getNextToken());
    }

    public void testCharacterLiteral7() throws ANTLRException
    {
        createAndFillLexer("'\\n'");
        checkNextToken(JavaTokenTypes.CHARACTER_LITERAL,
                       "'\\n'");
        assertTrue(!getNextToken());
    }

    public void testCharacterLiteral8() throws ANTLRException
    {
        createAndFillLexer("'\\r'");
        checkNextToken(JavaTokenTypes.CHARACTER_LITERAL,
                       "'\\r'");
        assertTrue(!getNextToken());
    }

    public void testCharacterLiteral9() throws ANTLRException
    {
        createAndFillLexer("'\\t'");
        checkNextToken(JavaTokenTypes.CHARACTER_LITERAL,
                       "'\\t'");
        assertTrue(!getNextToken());
    }

    public void testStringLiteral1() throws ANTLRException
    {
        createAndFillLexer("\"\"");
        checkNextToken(JavaTokenTypes.STRING_LITERAL,
                       "\"\"");
        assertTrue(!getNextToken());
    }

    public void testStringLiteral10() throws ANTLRException
    {
        createAndFillLexer("\"\\u9BC0\"");
        checkNextToken(JavaTokenTypes.STRING_LITERAL,
                       "\"\\u9BC0\"");
        assertTrue(!getNextToken());
    }

    public void testStringLiteral11() throws ANTLRException
    {
        createAndFillLexer("\"\\uubc00\"");
        checkNextToken(JavaTokenTypes.STRING_LITERAL,
                       "\"\\uubc00\"");
        assertTrue(!getNextToken());
    }

    public void testStringLiteral12() throws ANTLRException
    {
        createAndFillLexer("\"\\0\"");
        checkNextToken(JavaTokenTypes.STRING_LITERAL,
                       "\"\\0\"");
        assertTrue(!getNextToken());
    }

    public void testStringLiteral13() throws ANTLRException
    {
        createAndFillLexer("\"\\37\"");
        checkNextToken(JavaTokenTypes.STRING_LITERAL,
                       "\"\\37\"");
        assertTrue(!getNextToken());
    }

    public void testStringLiteral14() throws ANTLRException
    {
        createAndFillLexer("\"\\377\"");
        checkNextToken(JavaTokenTypes.STRING_LITERAL,
                       "\"\\377\"");
        assertTrue(!getNextToken());
    }

    public void testStringLiteral15() throws ANTLRException
    {
        createAndFillLexer("\"\\7\"");
        checkNextToken(JavaTokenTypes.STRING_LITERAL,
                       "\"\\7\"");
        assertTrue(!getNextToken());
    }

    public void testStringLiteral16() throws ANTLRException
    {
        createAndFillLexer("\"\\77\"");
        checkNextToken(JavaTokenTypes.STRING_LITERAL,
                       "\"\\77\"");
        assertTrue(!getNextToken());
    }

    public void testStringLiteral2() throws ANTLRException
    {
        createAndFillLexer("\" A simple 'test' ! \"");
        checkNextToken(JavaTokenTypes.STRING_LITERAL,
                       "\" A simple 'test' ! \"");
        assertTrue(!getNextToken());
    }

    public void testStringLiteral3() throws ANTLRException
    {
        createAndFillLexer("\"\\\"\"");
        checkNextToken(JavaTokenTypes.STRING_LITERAL,
                       "\"\\\"\"");
        assertTrue(!getNextToken());
    }

    public void testStringLiteral4() throws ANTLRException
    {
        createAndFillLexer("\"\\'\"");
        checkNextToken(JavaTokenTypes.STRING_LITERAL,
                       "\"\\'\"");
        assertTrue(!getNextToken());
    }

    public void testStringLiteral5() throws ANTLRException
    {
        createAndFillLexer("\"\\\\\"");
        checkNextToken(JavaTokenTypes.STRING_LITERAL,
                       "\"\\\\\"");
        assertTrue(!getNextToken());
    }

    public void testStringLiteral6() throws ANTLRException
    {
        createAndFillLexer("\"\\b\"");
        checkNextToken(JavaTokenTypes.STRING_LITERAL,
                       "\"\\b\"");
        assertTrue(!getNextToken());
    }

    public void testStringLiteral7() throws ANTLRException
    {
        createAndFillLexer("\"\\f\"");
        checkNextToken(JavaTokenTypes.STRING_LITERAL,
                       "\"\\f\"");
        assertTrue(!getNextToken());
    }

    public void testStringLiteral8() throws ANTLRException
    {
        createAndFillLexer("\"\\n\"");
        checkNextToken(JavaTokenTypes.STRING_LITERAL,
                       "\"\\n\"");
        assertTrue(!getNextToken());
    }

    public void testStringLiteral9() throws ANTLRException
    {
        createAndFillLexer("\"\\t\"");
        checkNextToken(JavaTokenTypes.STRING_LITERAL,
                       "\"\\t\"");
        assertTrue(!getNextToken());
    }
}
