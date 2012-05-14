package jast.test.parser;
import jast.ast.nodes.ArrayAccess;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FloatingPointLiteral;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.ParenthesizedExpression;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.VariableAccess;
import antlr.ANTLRException;

public class TestRelationalExpression extends TestBase
{

    public TestRelationalExpression(String name)
    {
        super(name);
    }

    public void testRelationalExpression1() throws ANTLRException
    {
        setupParser("var<2");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.LOWER_OP,
                     result.getOperator());

        FieldAccess fieldAccess = (FieldAccess)result.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)result.getRightOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());
    }

    public void testRelationalExpression2() throws ANTLRException
    {
        setupParser("1l>var");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.GREATER_OP,
                     result.getOperator());

        IntegerLiteral intLit = (IntegerLiteral)result.getLeftOperand();

        assertNotNull(intLit);
        assertEquals("1l",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());

        FieldAccess fieldAccess = (FieldAccess)result.getRightOperand();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());
    }

    public void testRelationalExpression3() throws ANTLRException
    {
        setupParser("arr[0]<=getValue()");
        defineVariable("arr", false);

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.LOWER_OR_EQUAL_OP,
                     result.getOperator());

        ArrayAccess arrayAccess = (ArrayAccess)result.getLeftOperand();

        assertNotNull(arrayAccess);
        assertTrue(arrayAccess.getBaseExpression() instanceof VariableAccess);
        assertEquals(result,
                     arrayAccess.getContainer());

        MethodInvocation methodInvoc = (MethodInvocation)result.getRightOperand();

        assertNotNull(methodInvoc);
        assertEquals("getValue",
                     methodInvoc.getMethodName());
        assertEquals(result,
                     methodInvoc.getContainer());
    }

    public void testRelationalExpression4() throws ANTLRException
    {
        setupParser("getValue()>=var");
        defineVariable("var", false);

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.GREATER_OR_EQUAL_OP,
                     result.getOperator());

        MethodInvocation methodInvoc = (MethodInvocation)result.getLeftOperand();

        assertNotNull(methodInvoc);
        assertEquals("getValue",
                     methodInvoc.getMethodName());
        assertEquals(result,
                     methodInvoc.getContainer());

        VariableAccess varAccess = (VariableAccess)result.getRightOperand();

        assertNotNull(varAccess);
        assertEquals("var",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());
    }

    public void testRelationalExpression5() throws ANTLRException
    {
        setupParser("(idx++) >= ~getValue()");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.GREATER_OR_EQUAL_OP,
                     result.getOperator());

        UnaryExpression unaryExpr = (UnaryExpression)result.getRightOperand();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.COMPLEMENT_OP,
                     unaryExpr.getOperator());
        assertEquals(result,
                     unaryExpr.getContainer());

        MethodInvocation methodInvoc = (MethodInvocation)unaryExpr.getInnerExpression();

        assertNotNull(methodInvoc);
        assertEquals("getValue",
                     methodInvoc.getMethodName());
        assertEquals(unaryExpr,
                     methodInvoc.getContainer());

        ParenthesizedExpression parenExpr = (ParenthesizedExpression)result.getLeftOperand();

        assertNotNull(parenExpr);
        assertEquals(result,
                     parenExpr.getContainer());

        PostfixExpression postfixExpr = (PostfixExpression)parenExpr.getInnerExpression();

        assertNotNull(postfixExpr);
        assertTrue(postfixExpr.isIncrement());
        assertEquals(parenExpr,
                     postfixExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)postfixExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("idx",
                     fieldAccess.getFieldName());
        assertEquals(postfixExpr,
                     fieldAccess.getContainer());
    }

    public void testRelationalExpression6() throws ANTLRException
    {
        setupParser("(int)1<(long)getValue()");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.LOWER_OP,
                     result.getOperator());

        UnaryExpression unaryExpr = (UnaryExpression)result.getLeftOperand();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.CAST_OP,
                     unaryExpr.getOperator());
        assertEquals("int",
                     unaryExpr.getCastType().toString());
        assertEquals(result,
                     unaryExpr.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)unaryExpr.getInnerExpression();

        assertNotNull(intLit);
        assertEquals("1",
                     intLit.asString());
        assertEquals(unaryExpr,
                     intLit.getContainer());

        unaryExpr = (UnaryExpression)result.getRightOperand();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.CAST_OP,
                     unaryExpr.getOperator());
        assertEquals("long",
                     unaryExpr.getCastType().toString());
        assertEquals(result,
                     unaryExpr.getContainer());

        MethodInvocation methodInvoc = (MethodInvocation)unaryExpr.getInnerExpression();

        assertNotNull(methodInvoc);
        assertEquals("getValue",
                     methodInvoc.getMethodName());
        assertEquals(unaryExpr,
                     methodInvoc.getContainer());
    }

    public void testRelationalExpression7() throws ANTLRException
    {
        setupParser("3*4 <= 2.0+var");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.LOWER_OR_EQUAL_OP,
                     result.getOperator());

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.MULTIPLY_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getLeftOperand();

        assertNotNull(intLit);
        assertEquals("3",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("4",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        binExpr = (BinaryExpression)result.getRightOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.PLUS_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FloatingPointLiteral floatLit = (FloatingPointLiteral)binExpr.getLeftOperand();

        assertNotNull(floatLit);
        assertEquals("2.0",
                     floatLit.asString());
        assertEquals(binExpr,
                     floatLit.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getRightOperand();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());
    }

    public void testRelationalExpression8() throws ANTLRException
    {
        setupParser("3>>>4 <= 2.0+var");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.LOWER_OR_EQUAL_OP,
                     result.getOperator());

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.ZERO_FILL_SHIFT_RIGHT_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getLeftOperand();

        assertNotNull(intLit);
        assertEquals("3",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("4",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        binExpr = (BinaryExpression)result.getRightOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.PLUS_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FloatingPointLiteral floatLit = (FloatingPointLiteral)binExpr.getLeftOperand();

        assertNotNull(floatLit);
        assertEquals("2.0",
                     floatLit.asString());
        assertEquals(binExpr,
                     floatLit.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getRightOperand();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());
    }

    public void testRelationalExpression9()
    {
        // Composite relational expressions are not allowed
        // because one relational expression is of result type
        // boolean which cannot be part of another relational
        // expression

        setupParser("(var < 2 < 3)");
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
