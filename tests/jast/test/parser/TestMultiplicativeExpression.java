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

public class TestMultiplicativeExpression extends TestBase
{

    public TestMultiplicativeExpression(String name)
    {
        super(name);
    }

    public void testMultiplicativeExpression1() throws ANTLRException
    {
        setupParser("1*2");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.MULTIPLY_OP,
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

    public void testMultiplicativeExpression10() throws ANTLRException
    {
        setupParser("(float)1*(double)getValue()");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.MULTIPLY_OP,
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

    public void testMultiplicativeExpression2() throws ANTLRException
    {
        setupParser("1.0f/var");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.DIVIDE_OP,
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

    public void testMultiplicativeExpression3() throws ANTLRException
    {
        setupParser("arr[0]%getValue()");
        defineVariable("arr", false);

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.MOD_OP,
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

    public void testMultiplicativeExpression4() throws ANTLRException
    {
        setupParser("1.0 * 2.0 * 3.0");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.MULTIPLY_OP,
                     result.getOperator());

        FloatingPointLiteral floatLit = (FloatingPointLiteral)result.getRightOperand();

        assertNotNull(floatLit);
        assertEquals("3.0",
                     floatLit.asString());
        assertEquals(result,
                     floatLit.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.MULTIPLY_OP,
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

    public void testMultiplicativeExpression5() throws ANTLRException
    {
        setupParser("3 / 1 / 2");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.DIVIDE_OP,
                     result.getOperator());

        IntegerLiteral intLit = (IntegerLiteral)result.getRightOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.DIVIDE_OP,
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

    public void testMultiplicativeExpression6() throws ANTLRException
    {
        setupParser("var % idx % 3");
        defineVariable("idx", false);

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.MOD_OP,
                     result.getOperator());

        IntegerLiteral intLit = (IntegerLiteral)result.getRightOperand();

        assertNotNull(intLit);
        assertEquals("3",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.MOD_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        VariableAccess varAccess = (VariableAccess)binExpr.getRightOperand();

        assertNotNull(varAccess);
        assertEquals("idx",
                     varAccess.getVariableName());
        assertEquals(binExpr,
                     varAccess.getContainer());
    }

    public void testMultiplicativeExpression7() throws ANTLRException
    {
        setupParser("1.0 * 2.0 / 3.0 % 4.0");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.MOD_OP,
                     result.getOperator());

        FloatingPointLiteral floatLit = (FloatingPointLiteral)result.getRightOperand();

        assertNotNull(floatLit);
        assertEquals("4.0",
                     floatLit.asString());
        assertEquals(result,
                     floatLit.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.DIVIDE_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        floatLit = (FloatingPointLiteral)binExpr.getRightOperand();

        assertNotNull(floatLit);
        assertEquals("3.0",
                     floatLit.asString());
        assertEquals(binExpr,
                     floatLit.getContainer());

        BinaryExpression innerBinExpr = (BinaryExpression)binExpr.getLeftOperand();

        assertNotNull(innerBinExpr);
        assertEquals(BinaryExpression.MULTIPLY_OP,
                     innerBinExpr.getOperator());
        assertEquals(binExpr,
                     innerBinExpr.getContainer());

        floatLit = (FloatingPointLiteral)innerBinExpr.getLeftOperand();

        assertNotNull(floatLit);
        assertEquals("1.0",
                     floatLit.asString());
        assertEquals(innerBinExpr,
                     floatLit.getContainer());

        floatLit = (FloatingPointLiteral)innerBinExpr.getRightOperand();

        assertNotNull(floatLit);
        assertEquals("2.0",
                     floatLit.asString());
        assertEquals(innerBinExpr,
                     floatLit.getContainer());
    }

    public void testMultiplicativeExpression8() throws ANTLRException
    {
        setupParser("1.0 % 2.0 * 3.0 / 4.0");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.DIVIDE_OP,
                     result.getOperator());

        FloatingPointLiteral floatLit = (FloatingPointLiteral)result.getRightOperand();

        assertNotNull(floatLit);
        assertEquals("4.0",
                     floatLit.asString());
        assertEquals(result,
                     floatLit.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.MULTIPLY_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        floatLit = (FloatingPointLiteral)binExpr.getRightOperand();

        assertNotNull(floatLit);
        assertEquals("3.0",
                     floatLit.asString());
        assertEquals(binExpr,
                     floatLit.getContainer());

        BinaryExpression innerBinExpr = (BinaryExpression)binExpr.getLeftOperand();

        assertNotNull(innerBinExpr);
        assertEquals(BinaryExpression.MOD_OP,
                     innerBinExpr.getOperator());
        assertEquals(binExpr,
                     innerBinExpr.getContainer());

        floatLit = (FloatingPointLiteral)innerBinExpr.getLeftOperand();

        assertNotNull(floatLit);
        assertEquals("1.0",
                     floatLit.asString());
        assertEquals(innerBinExpr,
                     floatLit.getContainer());

        floatLit = (FloatingPointLiteral)innerBinExpr.getRightOperand();

        assertNotNull(floatLit);
        assertEquals("2.0",
                     floatLit.asString());
        assertEquals(innerBinExpr,
                     floatLit.getContainer());
    }

    public void testMultiplicativeExpression9() throws ANTLRException
    {
        setupParser("-1 * (idx++) / ~getValue()");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.DIVIDE_OP,
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
        assertEquals(BinaryExpression.MULTIPLY_OP,
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
}
