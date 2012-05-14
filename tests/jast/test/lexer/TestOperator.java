package jast.test.lexer;
import jast.parser.JavaTokenTypes;
import antlr.ANTLRException;

public class TestOperator extends TestBase
{

    public TestOperator(String name)
    {
        super(name);
    }

    public void testOperator1() throws ANTLRException
    {
        createAndFillLexer("/");
        checkNextToken(JavaTokenTypes.OP_DIVIDE,
                       "/");
        assertTrue(!getNextToken());
    }

    public void testOperator10() throws ANTLRException
    {
        createAndFillLexer("<");
        checkNextToken(JavaTokenTypes.OP_LOWER,
                       "<");
        assertTrue(!getNextToken());
    }

    public void testOperator11() throws ANTLRException
    {
        createAndFillLexer("<<");
        checkNextToken(JavaTokenTypes.OP_SHIFT_LEFT,
                       "<<");
        assertTrue(!getNextToken());
    }

    public void testOperator12() throws ANTLRException
    {
        createAndFillLexer("<<=");
        checkNextToken(JavaTokenTypes.OP_SHIFT_LEFT_ASSIGN,
                       "<<=");
        assertTrue(!getNextToken());
    }

    public void testOperator13() throws ANTLRException
    {
        createAndFillLexer("<=");
        checkNextToken(JavaTokenTypes.OP_LOWER_OR_EQUAL,
                       "<=");
        assertTrue(!getNextToken());
    }

    public void testOperator14() throws ANTLRException
    {
        createAndFillLexer("!");
        checkNextToken(JavaTokenTypes.OP_NOT,
                       "!");
        assertTrue(!getNextToken());
    }

    public void testOperator15() throws ANTLRException
    {
        createAndFillLexer("!=");
        checkNextToken(JavaTokenTypes.OP_NOT_EQUAL,
                       "!=");
        assertTrue(!getNextToken());
    }

    public void testOperator16() throws ANTLRException
    {
        createAndFillLexer("~");
        checkNextToken(JavaTokenTypes.OP_BITWISE_COMPLEMENT,
                       "~");
        assertTrue(!getNextToken());
    }

    public void testOperator17() throws ANTLRException
    {
        createAndFillLexer("?");
        checkNextToken(JavaTokenTypes.OP_QUESTION,
                       "?");
        assertTrue(!getNextToken());
    }

    public void testOperator18() throws ANTLRException
    {
        createAndFillLexer(":");
        checkNextToken(JavaTokenTypes.OP_COLON,
                       ":");
        assertTrue(!getNextToken());
    }

    public void testOperator19() throws ANTLRException
    {
        createAndFillLexer("&");
        checkNextToken(JavaTokenTypes.OP_BITWISE_AND,
                       "&");
        assertTrue(!getNextToken());
    }

    public void testOperator2() throws ANTLRException
    {
        createAndFillLexer("/=");
        checkNextToken(JavaTokenTypes.OP_DIVIDE_ASSIGN,
                       "/=");
        assertTrue(!getNextToken());
    }

    public void testOperator20() throws ANTLRException
    {
        createAndFillLexer("&&");
        checkNextToken(JavaTokenTypes.OP_AND,
                       "&&");
        assertTrue(!getNextToken());
    }

    public void testOperator21() throws ANTLRException
    {
        createAndFillLexer("&=");
        checkNextToken(JavaTokenTypes.OP_BITWISE_AND_ASSIGN,
                       "&=");
        assertTrue(!getNextToken());
    }

    public void testOperator22() throws ANTLRException
    {
        createAndFillLexer("|");
        checkNextToken(JavaTokenTypes.OP_BITWISE_OR,
                       "|");
        assertTrue(!getNextToken());
    }

    public void testOperator23() throws ANTLRException
    {
        createAndFillLexer("||");
        checkNextToken(JavaTokenTypes.OP_OR,
                       "||");
        assertTrue(!getNextToken());
    }

    public void testOperator24() throws ANTLRException
    {
        createAndFillLexer("|=");
        checkNextToken(JavaTokenTypes.OP_BITWISE_OR_ASSIGN,
                       "|=");
        assertTrue(!getNextToken());
    }

    public void testOperator25() throws ANTLRException
    {
        createAndFillLexer("+");
        checkNextToken(JavaTokenTypes.OP_PLUS,
                       "+");
        assertTrue(!getNextToken());
    }

    public void testOperator26() throws ANTLRException
    {
        createAndFillLexer("++");
        checkNextToken(JavaTokenTypes.OP_INCREMENT,
                       "++");
        assertTrue(!getNextToken());
    }

    public void testOperator27() throws ANTLRException
    {
        createAndFillLexer("+=");
        checkNextToken(JavaTokenTypes.OP_PLUS_ASSIGN,
                       "+=");
        assertTrue(!getNextToken());
    }

    public void testOperator28() throws ANTLRException
    {
        createAndFillLexer("-");
        checkNextToken(JavaTokenTypes.OP_MINUS,
                       "-");
        assertTrue(!getNextToken());
    }

    public void testOperator29() throws ANTLRException
    {
        createAndFillLexer("--");
        checkNextToken(JavaTokenTypes.OP_DECREMENT,
                       "--");
        assertTrue(!getNextToken());
    }

    public void testOperator3() throws ANTLRException
    {
        createAndFillLexer("=");
        checkNextToken(JavaTokenTypes.OP_ASSIGN,
                       "=");
        assertTrue(!getNextToken());
    }

    public void testOperator30() throws ANTLRException
    {
        createAndFillLexer("-=");
        checkNextToken(JavaTokenTypes.OP_MINUS_ASSIGN,
                       "-=");
        assertTrue(!getNextToken());
    }

    public void testOperator31() throws ANTLRException
    {
        createAndFillLexer("*");
        checkNextToken(JavaTokenTypes.OP_MULTIPLY,
                       "*");
        assertTrue(!getNextToken());
    }

    public void testOperator32() throws ANTLRException
    {
        createAndFillLexer("*=");
        checkNextToken(JavaTokenTypes.OP_MULTIPLY_ASSIGN,
                       "*=");
        assertTrue(!getNextToken());
    }

    public void testOperator33() throws ANTLRException
    {
        createAndFillLexer("%");
        checkNextToken(JavaTokenTypes.OP_MOD,
                       "%");
        assertTrue(!getNextToken());
    }

    public void testOperator34() throws ANTLRException
    {
        createAndFillLexer("%=");
        checkNextToken(JavaTokenTypes.OP_MOD_ASSIGN,
                       "%=");
        assertTrue(!getNextToken());
    }

    public void testOperator35() throws ANTLRException
    {
        createAndFillLexer("^");
        checkNextToken(JavaTokenTypes.OP_BITWISE_XOR,
                       "^");
        assertTrue(!getNextToken());
    }

    public void testOperator36() throws ANTLRException
    {
        createAndFillLexer("^=");
        checkNextToken(JavaTokenTypes.OP_BITWISE_XOR_ASSIGN,
                       "^=");
        assertTrue(!getNextToken());
    }

    public void testOperator4() throws ANTLRException
    {
        createAndFillLexer(">");
        checkNextToken(JavaTokenTypes.OP_GREATER,
                       ">");
        assertTrue(!getNextToken());
    }

    public void testOperator5() throws ANTLRException
    {
        createAndFillLexer(">>");
        checkNextToken(JavaTokenTypes.OP_SHIFT_RIGHT,
                       ">>");
        assertTrue(!getNextToken());
    }

    public void testOperator6() throws ANTLRException
    {
        createAndFillLexer(">>>");
        checkNextToken(JavaTokenTypes.OP_ZERO_FILL_SHIFT_RIGHT,
                       ">>>");
        assertTrue(!getNextToken());
    }

    public void testOperator7() throws ANTLRException
    {
        createAndFillLexer(">>>=");
        checkNextToken(JavaTokenTypes.OP_ZERO_FILL_SHIFT_RIGHT_ASSIGN,
                       ">>>=");
        assertTrue(!getNextToken());
    }

    public void testOperator8() throws ANTLRException
    {
        createAndFillLexer(">>=");
        checkNextToken(JavaTokenTypes.OP_SHIFT_RIGHT_ASSIGN,
                       ">>=");
        assertTrue(!getNextToken());
    }

    public void testOperator9() throws ANTLRException
    {
        createAndFillLexer(">=");
        checkNextToken(JavaTokenTypes.OP_GREATER_OR_EQUAL,
                       ">=");
        assertTrue(!getNextToken());
    }
}
