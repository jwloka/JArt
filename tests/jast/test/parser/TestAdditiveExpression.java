package jast.test.parser;
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

public class TestAdditiveExpression extends TestBase
{

    public TestAdditiveExpression(String name)
    {
        super(name);
    }

    public void testAdditiveExpression1() throws ANTLRException
    {
        setupParser("1+2");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.PLUS_OP,
                     result.getOperator());

        IntegerLiteral intLit = (IntegerLiteral)result.getLeftOperand();

        assertNotNull(intLit);
        assertEquals("1",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());

        intLit = (IntegerLiteral)result.getRightOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());
    }

    public void testAdditiveExpression10() throws ANTLRException
    {
        setupParser("(0) - x");
        defineVariable("x", false);

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.MINUS_OP,
                     result.getOperator());

        ParenthesizedExpression parenExpr = (ParenthesizedExpression)result.getLeftOperand();

        assertNotNull(parenExpr);
        assertEquals(result,
                     parenExpr.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)parenExpr.getInnerExpression();

        assertNotNull(intLit);
        assertEquals("0",
                     intLit.asString());
        assertEquals(parenExpr,
                     intLit.getContainer());

        VariableAccess varAccess = (VariableAccess)result.getRightOperand();

        assertNotNull(varAccess);
        assertEquals("x",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());
    }

    public void testAdditiveExpression11() throws ANTLRException
    {
        setupParser("(PI) - x");
        defineVariable("x", false);

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.MINUS_OP,
                     result.getOperator());

        ParenthesizedExpression parenExpr = (ParenthesizedExpression)result.getLeftOperand();

        assertNotNull(parenExpr);
        assertEquals(result,
                     parenExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)parenExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertTrue(!fieldAccess.isTrailing());
        assertEquals("PI",
                     fieldAccess.getFieldName());
        assertEquals(parenExpr,
                     fieldAccess.getContainer());

        VariableAccess varAccess = (VariableAccess)result.getRightOperand();

        assertNotNull(varAccess);
        assertEquals("x",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());
    }

    public void testAdditiveExpression2() throws ANTLRException
    {
        setupParser("1.0f-var");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.MINUS_OP,
                     result.getOperator());

        FloatingPointLiteral floatLit = (FloatingPointLiteral)result.getLeftOperand();

        assertNotNull(floatLit);
        assertEquals("1.0f",
                     floatLit.asString());
        assertEquals(result,
                     floatLit.getContainer());

        FieldAccess fieldAccess = (FieldAccess)result.getRightOperand();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());
    }

    public void testAdditiveExpression3() throws ANTLRException
    {
        setupParser("1.0 + 2.0 + 3.0");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.PLUS_OP,
                     result.getOperator());

        FloatingPointLiteral floatLit = (FloatingPointLiteral)result.getRightOperand();

        assertNotNull(floatLit);
        assertEquals("3.0",
                     floatLit.asString());
        assertEquals(result,
                     floatLit.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.PLUS_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        floatLit = (FloatingPointLiteral)binExpr.getLeftOperand();

        assertNotNull(floatLit);
        assertEquals("1.0",
                     floatLit.asString());
        assertEquals(binExpr,
                     floatLit.getContainer());

        floatLit = (FloatingPointLiteral)binExpr.getRightOperand();

        assertNotNull(floatLit);
        assertEquals("2.0",
                     floatLit.asString());
        assertEquals(binExpr,
                     floatLit.getContainer());
    }

    public void testAdditiveExpression4() throws ANTLRException
    {
        setupParser("3 - 1 - 2");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.MINUS_OP,
                     result.getOperator());

        IntegerLiteral intLit = (IntegerLiteral)result.getRightOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.MINUS_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        intLit = (IntegerLiteral)binExpr.getLeftOperand();

        assertNotNull(intLit);
        assertEquals("3",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("1",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());
    }

    public void testAdditiveExpression5() throws ANTLRException
    {
        setupParser("1.0 + 2.0 - 3.0");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.MINUS_OP,
                     result.getOperator());

        FloatingPointLiteral floatLit = (FloatingPointLiteral)result.getRightOperand();

        assertNotNull(floatLit);
        assertEquals("3.0",
                     floatLit.asString());
        assertEquals(result,
                     floatLit.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.PLUS_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        floatLit = (FloatingPointLiteral)binExpr.getLeftOperand();

        assertNotNull(floatLit);
        assertEquals("1.0",
                     floatLit.asString());
        assertEquals(binExpr,
                     floatLit.getContainer());

        floatLit = (FloatingPointLiteral)binExpr.getRightOperand();

        assertNotNull(floatLit);
        assertEquals("2.0",
                     floatLit.asString());
        assertEquals(binExpr,
                     floatLit.getContainer());
    }

    public void testAdditiveExpression6() throws ANTLRException
    {
        setupParser("1.0 - 2.0 + 3.0");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.PLUS_OP,
                     result.getOperator());

        FloatingPointLiteral floatLit = (FloatingPointLiteral)result.getRightOperand();

        assertNotNull(floatLit);
        assertEquals("3.0",
                     floatLit.asString());
        assertEquals(result,
                     floatLit.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.MINUS_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        floatLit = (FloatingPointLiteral)binExpr.getLeftOperand();

        assertNotNull(floatLit);
        assertEquals("1.0",
                     floatLit.asString());
        assertEquals(binExpr,
                     floatLit.getContainer());

        floatLit = (FloatingPointLiteral)binExpr.getRightOperand();

        assertNotNull(floatLit);
        assertEquals("2.0",
                     floatLit.asString());
        assertEquals(binExpr,
                     floatLit.getContainer());
    }

    public void testAdditiveExpression7() throws ANTLRException
    {
        setupParser("-1 + (idx++) - ~getValue()");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.MINUS_OP,
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

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.PLUS_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        unaryExpr = (UnaryExpression)binExpr.getLeftOperand();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.MINUS_OP,
                     unaryExpr.getOperator());
        assertEquals(binExpr,
                     unaryExpr.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)unaryExpr.getInnerExpression();

        assertNotNull(intLit);
        assertEquals("1",
                     intLit.asString());
        assertEquals(unaryExpr,
                     intLit.getContainer());

        ParenthesizedExpression parenExpr = (ParenthesizedExpression)binExpr.getRightOperand();

        assertNotNull(parenExpr);
        assertEquals(binExpr,
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

    public void testAdditiveExpression8() throws ANTLRException
    {
        setupParser("(float)1-(double)getValue()");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.MINUS_OP,
                     result.getOperator());

        UnaryExpression unaryExpr = (UnaryExpression)result.getLeftOperand();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.CAST_OP,
                     unaryExpr.getOperator());
        assertEquals("float",
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
        assertEquals("double",
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

    public void testAdditiveExpression9() throws ANTLRException
    {
        setupParser("3*4+2.0/var");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.PLUS_OP,
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
        assertEquals(BinaryExpression.DIVIDE_OP,
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
}
