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

public class TestShiftExpression extends TestBase
{

    public TestShiftExpression(String name)
    {
        super(name);
    }

    public void testShiftExpression1() throws ANTLRException
    {
        setupParser("1<<2");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.SHIFT_LEFT_OP,
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

    public void testShiftExpression10() throws ANTLRException
    {
        setupParser("(int)1>>>(long)getValue()");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.ZERO_FILL_SHIFT_RIGHT_OP,
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

    public void testShiftExpression11() throws ANTLRException
    {
        setupParser("3*4>>2.0+var");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.SHIFT_RIGHT_OP,
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

    public void testShiftExpression2() throws ANTLRException
    {
        setupParser("1l>>var");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.SHIFT_RIGHT_OP,
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

    public void testShiftExpression3() throws ANTLRException
    {
        setupParser("arr[0]>>>getValue()");
        defineVariable("arr", false);

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.ZERO_FILL_SHIFT_RIGHT_OP,
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

    public void testShiftExpression4() throws ANTLRException
    {
        setupParser("1 << 2 << 3");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.SHIFT_LEFT_OP,
                     result.getOperator());

        IntegerLiteral intLit = (IntegerLiteral)result.getRightOperand();

        assertNotNull(intLit);
        assertEquals("3",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.SHIFT_LEFT_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        intLit = (IntegerLiteral)binExpr.getLeftOperand();

        assertNotNull(intLit);
        assertEquals("1",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());
    }

    public void testShiftExpression5() throws ANTLRException
    {
        setupParser("3 >> 1 >> 2");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.SHIFT_RIGHT_OP,
                     result.getOperator());

        IntegerLiteral intLit = (IntegerLiteral)result.getRightOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.SHIFT_RIGHT_OP,
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

    public void testShiftExpression6() throws ANTLRException
    {
        setupParser("var >>> idx >>> 3");
        defineVariable("idx", false);

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.ZERO_FILL_SHIFT_RIGHT_OP,
                     result.getOperator());

        IntegerLiteral intLit = (IntegerLiteral)result.getRightOperand();

        assertNotNull(intLit);
        assertEquals("3",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.ZERO_FILL_SHIFT_RIGHT_OP,
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

    public void testShiftExpression7() throws ANTLRException
    {
        setupParser("1 >>> 2 << 3 >> 4");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.SHIFT_RIGHT_OP,
                     result.getOperator());

        IntegerLiteral intLit = (IntegerLiteral)result.getRightOperand();

        assertNotNull(intLit);
        assertEquals("4",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.SHIFT_LEFT_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("3",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        BinaryExpression innerBinExpr = (BinaryExpression)binExpr.getLeftOperand();

        assertNotNull(innerBinExpr);
        assertEquals(BinaryExpression.ZERO_FILL_SHIFT_RIGHT_OP,
                     innerBinExpr.getOperator());
        assertEquals(binExpr,
                     innerBinExpr.getContainer());

        intLit = (IntegerLiteral)innerBinExpr.getLeftOperand();

        assertNotNull(intLit);
        assertEquals("1",
                     intLit.asString());
        assertEquals(innerBinExpr,
                     intLit.getContainer());

        intLit = (IntegerLiteral)innerBinExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(innerBinExpr,
                     intLit.getContainer());
    }

    public void testShiftExpression8() throws ANTLRException
    {
        setupParser("1 << 2 >> 3 >>> 4");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.ZERO_FILL_SHIFT_RIGHT_OP,
                     result.getOperator());

        IntegerLiteral intLit = (IntegerLiteral)result.getRightOperand();

        assertNotNull(intLit);
        assertEquals("4",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.SHIFT_RIGHT_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("3",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        BinaryExpression innerBinExpr = (BinaryExpression)binExpr.getLeftOperand();

        assertNotNull(innerBinExpr);
        assertEquals(BinaryExpression.SHIFT_LEFT_OP,
                     innerBinExpr.getOperator());
        assertEquals(binExpr,
                     innerBinExpr.getContainer());

        intLit = (IntegerLiteral)innerBinExpr.getLeftOperand();

        assertNotNull(intLit);
        assertEquals("1",
                     intLit.asString());
        assertEquals(innerBinExpr,
                     intLit.getContainer());

        intLit = (IntegerLiteral)innerBinExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(innerBinExpr,
                     intLit.getContainer());
    }

    public void testShiftExpression9() throws ANTLRException
    {
        setupParser("-1 >> (idx++) << ~getValue()");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.SHIFT_LEFT_OP,
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
        assertEquals(BinaryExpression.SHIFT_RIGHT_OP,
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
