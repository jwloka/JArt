package jast.test.lexer;
import jast.SyntaxException;
import jast.parser.JavaLexer;
import jast.parser.UnicodeReader;

import java.io.StringReader;

import antlr.Token;
import antlr.TokenStreamException;

public abstract class TestBase extends jast.test.TestBase
{
    private JavaLexer _lexer;
    private Token     _nextToken;

    public TestBase(String name)
    {
        super(name);
    }

    protected void checkNextToken(int type, String value) throws SyntaxException
    {
        assertTrue(getNextToken());
        assertEquals(type,
                     _nextToken.getType());
        if (value == null)
        {
            assertNull(_nextToken.getText());
        }
        else
        {
            assertEquals(value,
                         _nextToken.getText());
        }
    }

    protected void createAndFillLexer(String text)
    {
        _lexer = new JavaLexer(new UnicodeReader(new StringReader(text)));
    }

    protected boolean getNextToken() throws SyntaxException
    {
        if (_lexer.isDone())
        {
            return false;
        }
        try
        {
            _nextToken = _lexer.nextToken();
            return (_nextToken != null) &&
                   (_nextToken.getType() != Token.EOF_TYPE);
        }
        catch (TokenStreamException ex)
        {
            throw new SyntaxException("Could not read another token");
        }
    }

    protected void tearDown()
    {
        _lexer     = null;
        _nextToken = null;
    }
}
