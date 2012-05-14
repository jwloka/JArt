package jast.test.parser;
import jast.ast.nodes.ArrayAccess;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.ParenthesizedExpression;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.VariableAccess;
import antlr.ANTLRException;

public class TestPostfixExpression extends TestBase
{

    public TestPostfixExpression(String name)
    {
        super(name);
    }

    public void testPostfixExpression1() throws ANTLRException
    {
        setupParser("idx++");
        defineVariable("idx", false);

        PostfixExpression result = (PostfixExpression)_parser.expression();

        assertNotNull(result);
        assertTrue(result.isIncrement());

        VariableAccess varAccess = (VariableAccess)result.getInnerExpression();

        assertNotNull(varAccess);
        assertEquals("idx",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());
    }

    public void testPostfixExpression2() throws ANTLRException
    {
        setupParser("this.var--");

        PostfixExpression result = (PostfixExpression)_parser.expression();

        assertNotNull(result);
        assertTrue(!result.isIncrement());

        FieldAccess fieldAccess = (FieldAccess)result.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());
    }

    public void testPostfixExpression3() throws ANTLRException
    {
        setupParser("arr[3]++");

        PostfixExpression result = (PostfixExpression)_parser.expression();

        assertNotNull(result);
        assertTrue(result.isIncrement());

        ArrayAccess arrayAccess = (ArrayAccess)result.getInnerExpression();

        assertNotNull(arrayAccess);
        assertEquals(result,
                     arrayAccess.getContainer());

        FieldAccess fieldAccess = (FieldAccess)arrayAccess.getBaseExpression();

        assertNotNull(fieldAccess);
        assertEquals("arr",
                     fieldAccess.getFieldName());
        assertEquals(arrayAccess,
                     fieldAccess.getContainer());
    }

    public void testPostfixExpression4() throws ANTLRException
    {
        setupParser("(var)--");
        defineVariable("var", false);

        PostfixExpression result = (PostfixExpression)_parser.expression();

        assertNotNull(result);
        assertTrue(!result.isIncrement());

        ParenthesizedExpression parenExpr = (ParenthesizedExpression)result.getInnerExpression();

        assertNotNull(parenExpr);
        assertEquals(result,
                     parenExpr.getContainer());

        VariableAccess varAccess = (VariableAccess)parenExpr.getInnerExpression();

        assertNotNull(varAccess);
        assertEquals("var",
                     varAccess.getVariableName());
        assertEquals(parenExpr,
                     varAccess.getContainer());
    }

    public void testPostfixExpression5()
    {
        setupParser("(idx--++)");
        try
        {
            _parser.expression();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }
}
