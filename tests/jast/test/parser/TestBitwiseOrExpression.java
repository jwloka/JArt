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

public class TestBitwiseOrExpression extends TestBase
{

    public TestBitwiseOrExpression(String name)
    {
        super(name);
    }

    public void testBitwiseOrExpression1() throws ANTLRException
    {
        setupParser("1 | 2");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.BITWISE_OR_OP,
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

    public void testBitwiseOrExpression2() throws ANTLRException
    {
        setupParser("1.0 | 2.0 | var");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.BITWISE_OR_OP,
                     result.getOperator());

        FieldAccess fieldAccess = (FieldAccess)result.getRightOperand();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_OR_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FloatingPointLiteral floatLit = (FloatingPointLiteral)binExpr.getLeftOperand();

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

    public void testBitwiseOrExpression3() throws ANTLRException
    {
        setupParser("-1 | (idx++) | ~getValue()");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.BITWISE_OR_OP,
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
        assertEquals(BinaryExpression.BITWISE_OR_OP,
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

    public void testBitwiseOrExpression4() throws ANTLRException
    {
        setupParser("(int)1 | (long)getValue()");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.BITWISE_OR_OP,
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

    public void testBitwiseOrExpression5() throws ANTLRException
    {
        setupParser("i ^ 2 | 2.0 & var");
        defineVariable("var", false);

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.BITWISE_OR_OP,
                     result.getOperator());

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_XOR_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("i",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        binExpr = (BinaryExpression)result.getRightOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_AND_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FloatingPointLiteral floatLit = (FloatingPointLiteral)binExpr.getLeftOperand();

        assertNotNull(floatLit);
        assertEquals("2.0",
                     floatLit.asString());
        assertEquals(binExpr,
                     floatLit.getContainer());

        VariableAccess varAccess = (VariableAccess)binExpr.getRightOperand();

        assertNotNull(varAccess);
        assertEquals("var",
                     varAccess.getVariableName());
        assertEquals(binExpr,
                     varAccess.getContainer());
    }
}
