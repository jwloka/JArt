package jast.test.parser;
import jast.ast.nodes.ArrayAccess;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FloatingPointLiteral;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.ParenthesizedExpression;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.VariableAccess;
import antlr.ANTLRException;

public class TestUnaryExpression extends TestBase
{

    public TestUnaryExpression(String name)
    {
        super(name);
    }

    public void testUnaryExpression1() throws ANTLRException
    {
        setupParser("+1");

        UnaryExpression result = (UnaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(UnaryExpression.PLUS_OP,
                     result.getOperator());

        IntegerLiteral intLit = (IntegerLiteral)result.getInnerExpression();

        assertNotNull(intLit);
        assertEquals("1",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());
    }

    public void testUnaryExpression10() throws ANTLRException
    {
        setupParser("-+var");

        UnaryExpression result = (UnaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(UnaryExpression.MINUS_OP,
                     result.getOperator());

        UnaryExpression unaryExpr = (UnaryExpression)result.getInnerExpression();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.PLUS_OP,
                     unaryExpr.getOperator());
        assertEquals(result,
                     unaryExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)unaryExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(unaryExpr,
                     fieldAccess.getContainer());
    }

    public void testUnaryExpression11() throws ANTLRException
    {
        setupParser("+~var");

        UnaryExpression result = (UnaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(UnaryExpression.PLUS_OP,
                     result.getOperator());

        UnaryExpression unaryExpr = (UnaryExpression)result.getInnerExpression();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.COMPLEMENT_OP,
                     unaryExpr.getOperator());
        assertEquals(result,
                     unaryExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)unaryExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(unaryExpr,
                     fieldAccess.getContainer());
    }

    public void testUnaryExpression12() throws ANTLRException
    {
        setupParser("~++var");

        UnaryExpression result = (UnaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(UnaryExpression.COMPLEMENT_OP,
                     result.getOperator());

        UnaryExpression unaryExpr = (UnaryExpression)result.getInnerExpression();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.INCREMENT_OP,
                     unaryExpr.getOperator());
        assertEquals(result,
                     unaryExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)unaryExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(unaryExpr,
                     fieldAccess.getContainer());
    }

    public void testUnaryExpression13() throws ANTLRException
    {
        setupParser("~+var");

        UnaryExpression result = (UnaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(UnaryExpression.COMPLEMENT_OP,
                     result.getOperator());

        UnaryExpression unaryExpr = (UnaryExpression)result.getInnerExpression();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.PLUS_OP,
                     unaryExpr.getOperator());
        assertEquals(result,
                     unaryExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)unaryExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(unaryExpr,
                     fieldAccess.getContainer());
    }

    public void testUnaryExpression14() throws ANTLRException
    {
        setupParser("(double)~var");

        UnaryExpression result = (UnaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(UnaryExpression.CAST_OP,
                     result.getOperator());
        assertEquals("double",
                     result.getCastType().toString());

        UnaryExpression unaryExpr = (UnaryExpression)result.getInnerExpression();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.COMPLEMENT_OP,
                     unaryExpr.getOperator());
        assertEquals(result,
                     unaryExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)unaryExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(unaryExpr,
                     fieldAccess.getContainer());
    }

    public void testUnaryExpression15() throws ANTLRException
    {
        setupParser("(float)-var");

        UnaryExpression result = (UnaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(UnaryExpression.CAST_OP,
                     result.getOperator());
        assertEquals("float",
                     result.getCastType().toString());

        UnaryExpression unaryExpr = (UnaryExpression)result.getInnerExpression();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.MINUS_OP,
                     unaryExpr.getOperator());
        assertEquals(result,
                     unaryExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)unaryExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(unaryExpr,
                     fieldAccess.getContainer());
    }

    public void testUnaryExpression16() throws ANTLRException
    {
        setupParser("(long)++var");

        UnaryExpression result = (UnaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(UnaryExpression.CAST_OP,
                     result.getOperator());
        assertEquals("long",
                     result.getCastType().toString());

        UnaryExpression unaryExpr = (UnaryExpression)result.getInnerExpression();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.INCREMENT_OP,
                     unaryExpr.getOperator());
        assertEquals(result,
                     unaryExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)unaryExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(unaryExpr,
                     fieldAccess.getContainer());
    }

    public void testUnaryExpression17() throws ANTLRException
    {
        setupParser("(long)(double)var");

        UnaryExpression result = (UnaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(UnaryExpression.CAST_OP,
                     result.getOperator());
        assertEquals("long",
                     result.getCastType().toString());

        UnaryExpression unaryExpr = (UnaryExpression)result.getInnerExpression();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.CAST_OP,
                     unaryExpr.getOperator());
        assertEquals("double",
                     unaryExpr.getCastType().toString());
        assertEquals(result,
                     unaryExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)unaryExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(unaryExpr,
                     fieldAccess.getContainer());
    }

    public void testUnaryExpression18()
    {
        setupParser("++~var");
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

    public void testUnaryExpression19()
    {
        setupParser("--++var");
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

    public void testUnaryExpression2() throws ANTLRException
    {
        setupParser("-2.0f");

        UnaryExpression result = (UnaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(UnaryExpression.MINUS_OP,
                     result.getOperator());

        FloatingPointLiteral floatLit = (FloatingPointLiteral)result.getInnerExpression();

        assertNotNull(floatLit);
        assertEquals("2.0f",
                     floatLit.asString());
        assertEquals(result,
                     floatLit.getContainer());
    }

    public void testUnaryExpression20()
    {
        setupParser("(java.lang.Object)--var");
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

    public void testUnaryExpression3() throws ANTLRException
    {
        setupParser("++arr[0]");

        UnaryExpression result = (UnaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(UnaryExpression.INCREMENT_OP,
                     result.getOperator());

        ArrayAccess arrayAccess = (ArrayAccess)result.getInnerExpression();

        assertNotNull(arrayAccess);
        assertTrue(arrayAccess.getIndexExpression() instanceof IntegerLiteral);
        assertTrue(arrayAccess.getBaseExpression() instanceof FieldAccess);
        assertEquals(result,
                     arrayAccess.getContainer());
    }

    public void testUnaryExpression4() throws ANTLRException
    {
        setupParser("--var");

        UnaryExpression result = (UnaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(UnaryExpression.DECREMENT_OP,
                     result.getOperator());

        FieldAccess fieldAccess = (FieldAccess)result.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());
    }

    public void testUnaryExpression5() throws ANTLRException
    {
        setupParser("!isValid()");

        UnaryExpression result = (UnaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(UnaryExpression.NEGATION_OP,
                     result.getOperator());

        MethodInvocation methodInvoc = (MethodInvocation)result.getInnerExpression();

        assertNotNull(methodInvoc);
        assertEquals("isValid",
                     methodInvoc.getMethodName());
        assertEquals(result,
                     methodInvoc.getContainer());
    }

    public void testUnaryExpression6() throws ANTLRException
    {
        setupParser("~getBitMask(0)");

        UnaryExpression result = (UnaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(UnaryExpression.COMPLEMENT_OP,
                     result.getOperator());

        MethodInvocation methodInvoc = (MethodInvocation)result.getInnerExpression();

        assertNotNull(methodInvoc);
        assertEquals("getBitMask",
                     methodInvoc.getMethodName());
        assertEquals(result,
                     methodInvoc.getContainer());
    }

    public void testUnaryExpression7() throws ANTLRException
    {
        setupParser("(double)1");

        UnaryExpression result = (UnaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(UnaryExpression.CAST_OP,
                     result.getOperator());
        assertEquals("double",
                     result.getCastType().toString());

        IntegerLiteral intLit = (IntegerLiteral)result.getInnerExpression();

        assertNotNull(intLit);
        assertEquals("1",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());
    }

    public void testUnaryExpression8() throws ANTLRException
    {
        setupParser("(java.lang.String)obj");

        UnaryExpression result = (UnaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(UnaryExpression.CAST_OP,
                     result.getOperator());
        assertEquals("java.lang.String",
                     result.getCastType().toString());

        FieldAccess fieldAccess = (FieldAccess)result.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("obj",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());
    }

    public void testUnaryExpression9() throws ANTLRException
    {
        setupParser("(int)(value)");
        defineVariable("value", false);

        UnaryExpression result = (UnaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(UnaryExpression.CAST_OP,
                     result.getOperator());
        assertEquals("int",
                     result.getCastType().toString());

        ParenthesizedExpression parenExpr = (ParenthesizedExpression)result.getInnerExpression();

        assertNotNull(parenExpr);
        assertEquals(result,
                     parenExpr.getContainer());

        VariableAccess varAccess = (VariableAccess)parenExpr.getInnerExpression();

        assertNotNull(varAccess);
        assertEquals("value",
                     varAccess.getVariableName());
        assertEquals(parenExpr,
                     varAccess.getContainer());
    }
}
