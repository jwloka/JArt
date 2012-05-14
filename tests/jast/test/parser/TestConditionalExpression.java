package jast.test.parser;
import jast.ast.nodes.ArrayAccess;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.ConditionalExpression;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.InstanceofExpression;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.ParenthesizedExpression;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.VariableAccess;
import antlr.ANTLRException;

public class TestConditionalExpression extends TestBase
{

    public TestConditionalExpression(String name)
    {
        super(name);
    }

    public void testConditionalExpression1() throws ANTLRException
    {
        setupParser("var?1:2");

        ConditionalExpression result = (ConditionalExpression)_parser.expression();

        assertNotNull(result);

        FieldAccess fieldAccess = (FieldAccess)result.getCondition();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)result.getTrueExpression();

        assertNotNull(intLit);
        assertEquals("1",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());

        intLit = (IntegerLiteral)result.getFalseExpression();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());
    }

    public void testConditionalExpression10()
    {
        setupParser("var ? 1 ? 2");
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

    public void testConditionalExpression11()
    {
        setupParser("(var : 1 : 2)");
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

    public void testConditionalExpression2() throws ANTLRException
    {
        setupParser("getValue() ? a ? 0 : 1 : 2");
        defineVariable("a", false);

        ConditionalExpression result = (ConditionalExpression)_parser.expression();

        assertNotNull(result);

        MethodInvocation methodInvoc = (MethodInvocation)result.getCondition();

        assertNotNull(methodInvoc);
        assertEquals("getValue",
                     methodInvoc.getMethodName());
        assertEquals(result,
                     methodInvoc.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)result.getFalseExpression();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());

        ConditionalExpression condExpr = (ConditionalExpression)result.getTrueExpression();

        assertNotNull(condExpr);
        assertEquals(result,
                     condExpr.getContainer());

        VariableAccess varAccess = (VariableAccess)condExpr.getCondition();

        assertNotNull(varAccess);
        assertEquals("a",
                     varAccess.getVariableName());
        assertEquals(condExpr,
                     varAccess.getContainer());

        intLit = (IntegerLiteral)condExpr.getTrueExpression();

        assertNotNull(intLit);
        assertEquals("0",
                     intLit.asString());
        assertEquals(condExpr,
                     intLit.getContainer());

        intLit = (IntegerLiteral)condExpr.getFalseExpression();

        assertNotNull(intLit);
        assertEquals("1",
                     intLit.asString());
        assertEquals(condExpr,
                     intLit.getContainer());
    }

    public void testConditionalExpression3() throws ANTLRException
    {
        setupParser("getValue() ? 0 : a ? 1 : 2");
        defineVariable("a", false);

        ConditionalExpression result = (ConditionalExpression)_parser.expression();

        assertNotNull(result);

        MethodInvocation methodInvoc = (MethodInvocation)result.getCondition();

        assertNotNull(methodInvoc);
        assertEquals("getValue",
                     methodInvoc.getMethodName());
        assertEquals(result,
                     methodInvoc.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)result.getTrueExpression();

        assertNotNull(intLit);
        assertEquals("0",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());

        ConditionalExpression condExpr = (ConditionalExpression)result.getFalseExpression();

        assertNotNull(condExpr);
        assertEquals(result,
                     condExpr.getContainer());

        VariableAccess varAccess = (VariableAccess)condExpr.getCondition();

        assertNotNull(varAccess);
        assertEquals("a",
                     varAccess.getVariableName());
        assertEquals(condExpr,
                     varAccess.getContainer());

        intLit = (IntegerLiteral)condExpr.getTrueExpression();

        assertNotNull(intLit);
        assertEquals("1",
                     intLit.asString());
        assertEquals(condExpr,
                     intLit.getContainer());

        intLit = (IntegerLiteral)condExpr.getFalseExpression();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(condExpr,
                     intLit.getContainer());
    }

    public void testConditionalExpression4() throws ANTLRException
    {
        setupParser("!getValue() ? -1 : (var--)");

        ConditionalExpression result = (ConditionalExpression)_parser.expression();

        assertNotNull(result);

        UnaryExpression unaryExpr = (UnaryExpression)result.getCondition();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.NEGATION_OP,
                     unaryExpr.getOperator());
        assertEquals(result,
                     unaryExpr.getContainer());

        MethodInvocation methodInvoc = (MethodInvocation)unaryExpr.getInnerExpression();

        assertNotNull(methodInvoc);
        assertEquals("getValue",
                     methodInvoc.getMethodName());
        assertEquals(unaryExpr,
                     methodInvoc.getContainer());

        unaryExpr = (UnaryExpression)result.getTrueExpression();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.MINUS_OP,
                     unaryExpr.getOperator());
        assertEquals(result,
                     unaryExpr.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)unaryExpr.getInnerExpression();

        assertNotNull(intLit);
        assertEquals("1",
                     intLit.asString());
        assertEquals(unaryExpr,
                     intLit.getContainer());

        ParenthesizedExpression parenExpr = (ParenthesizedExpression)result.getFalseExpression();

        assertNotNull(parenExpr);
        assertEquals(result,
                     parenExpr.getContainer());

        PostfixExpression postfixExpr = (PostfixExpression)parenExpr.getInnerExpression();

        assertNotNull(postfixExpr);
        assertTrue(!postfixExpr.isIncrement());
        assertEquals(parenExpr,
                     postfixExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)postfixExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(postfixExpr,
                     fieldAccess.getContainer());
    }

    public void testConditionalExpression5() throws ANTLRException
    {
        setupParser("(boolean)var ? 1 + getValue() : idx << 2");

        ConditionalExpression result = (ConditionalExpression)_parser.expression();

        assertNotNull(result);

        UnaryExpression unaryExpr = (UnaryExpression)result.getCondition();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.CAST_OP,
                     unaryExpr.getOperator());
        assertEquals("boolean",
                     unaryExpr.getCastType().toString());
        assertEquals(result,
                     unaryExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)unaryExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(unaryExpr,
                     fieldAccess.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getTrueExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.PLUS_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getLeftOperand();

        assertNotNull(intLit);
        assertEquals("1",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        MethodInvocation methodInvoc = (MethodInvocation)binExpr.getRightOperand();

        assertNotNull(methodInvoc);
        assertEquals("getValue",
                     methodInvoc.getMethodName());
        assertEquals(binExpr,
                     methodInvoc.getContainer());

        binExpr = (BinaryExpression)result.getFalseExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.SHIFT_LEFT_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("idx",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());
    }

    public void testConditionalExpression6() throws ANTLRException
    {
        setupParser("var instanceof String ? value < 2 : value != 2");

        ConditionalExpression result = (ConditionalExpression)_parser.expression();

        assertNotNull(result);

        InstanceofExpression instanceofExpr = (InstanceofExpression)result.getCondition();

        assertNotNull(instanceofExpr);
        assertTrue(!instanceofExpr.getReferencedType().isPrimitive());
        assertEquals(0,
                     instanceofExpr.getReferencedType().getDimensions());
        assertEquals("String",
                     instanceofExpr.getReferencedType().getBaseName());
        assertEquals(result,
                     instanceofExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)instanceofExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(instanceofExpr,
                     fieldAccess.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getTrueExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.LOWER_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        binExpr = (BinaryExpression)result.getFalseExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.NOT_EQUAL_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());
    }

    public void testConditionalExpression7() throws ANTLRException
    {
        setupParser("var && isCorrect() ? value & 2 : value | 2");

        ConditionalExpression result = (ConditionalExpression)_parser.expression();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getCondition();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.AND_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        MethodInvocation methodInvoc = (MethodInvocation)binExpr.getRightOperand();

        assertNotNull(methodInvoc);
        assertEquals("isCorrect",
                     methodInvoc.getMethodName());
        assertEquals(binExpr,
                     methodInvoc.getContainer());

        binExpr = (BinaryExpression)result.getTrueExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_AND_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        binExpr = (BinaryExpression)result.getFalseExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_OR_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());
    }

    public void testConditionalExpression8() throws ANTLRException
    {
        setupParser("var || isCorrect() ? value ^ 2 : arr[2]");

        ConditionalExpression result = (ConditionalExpression)_parser.expression();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getCondition();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.OR_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        MethodInvocation methodInvoc = (MethodInvocation)binExpr.getRightOperand();

        assertNotNull(methodInvoc);
        assertEquals("isCorrect",
                     methodInvoc.getMethodName());
        assertEquals(binExpr,
                     methodInvoc.getContainer());

        binExpr = (BinaryExpression)result.getTrueExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_XOR_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        ArrayAccess arrayAccess = (ArrayAccess)result.getFalseExpression();

        assertNotNull(arrayAccess);
        assertEquals(result,
                     arrayAccess.getContainer());

        fieldAccess = (FieldAccess)arrayAccess.getBaseExpression();

        assertNotNull(fieldAccess);
        assertEquals("arr",
                     fieldAccess.getFieldName());
        assertEquals(arrayAccess,
                     fieldAccess.getContainer());

        intLit = (IntegerLiteral)arrayAccess.getIndexExpression();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(arrayAccess,
                     intLit.getContainer());
    }

    public void testConditionalExpression9()
    {
        setupParser("var ? value");
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
