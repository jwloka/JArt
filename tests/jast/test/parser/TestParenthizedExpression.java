package jast.test.parser;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.ConditionalExpression;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.InstanceofExpression;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.ParenthesizedExpression;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.SelfAccess;
import jast.ast.nodes.Type;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.UnresolvedAccess;
import jast.ast.nodes.VariableAccess;
import antlr.ANTLRException;

public class TestParenthizedExpression extends TestBase
{

    public TestParenthizedExpression(String name)
    {
        super(name);
    }

    public void testParenthizedExpression1() throws ANTLRException
    {
        setupParser("(0)");

        ParenthesizedExpression result = (ParenthesizedExpression)_parser.primary();

        assertNotNull(result);

        IntegerLiteral intLit = (IntegerLiteral)result.getInnerExpression();

        assertNotNull(intLit);
        assertEquals("0",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());
    }

    public void testParenthizedExpression10() throws ANTLRException
    {
        setupParser("(arr--)");

        ParenthesizedExpression result = (ParenthesizedExpression)_parser.primary();

        assertNotNull(result);

        PostfixExpression postfixExpr = (PostfixExpression)result.getInnerExpression();

        assertNotNull(postfixExpr);
        assertTrue(!postfixExpr.isIncrement());
        assertEquals(result,
                     postfixExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)postfixExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("arr",
                     fieldAccess.getFieldName());
        assertEquals(postfixExpr,
                     fieldAccess.getContainer());
    }

    public void testParenthizedExpression11() throws ANTLRException
    {
        setupParser("(~arr)");

        ParenthesizedExpression result = (ParenthesizedExpression)_parser.primary();

        assertNotNull(result);

        UnaryExpression unaryExpr = (UnaryExpression)result.getInnerExpression();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.COMPLEMENT_OP,
                     unaryExpr.getOperator());
        assertEquals(result,
                     unaryExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)unaryExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("arr",
                     fieldAccess.getFieldName());
        assertEquals(unaryExpr,
                     fieldAccess.getContainer());
    }

    public void testParenthizedExpression12() throws ANTLRException
    {
        setupParser("((String)obj)");

        ParenthesizedExpression result = (ParenthesizedExpression)_parser.primary();

        assertNotNull(result);

        UnaryExpression unaryExpr = (UnaryExpression)result.getInnerExpression();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.CAST_OP,
                     unaryExpr.getOperator());
        assertEquals("String",
                     unaryExpr.getCastType().getBaseName());
        assertEquals(result,
                     unaryExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)unaryExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("obj",
                     fieldAccess.getFieldName());
        assertEquals(unaryExpr,
                     fieldAccess.getContainer());
    }

    public void testParenthizedExpression13() throws ANTLRException
    {
        setupParser("(value % 6)");

        ParenthesizedExpression result = (ParenthesizedExpression)_parser.primary();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getInnerExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.MOD_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("6",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());
    }

    public void testParenthizedExpression14() throws ANTLRException
    {
        setupParser("(value - 6)");

        ParenthesizedExpression result = (ParenthesizedExpression)_parser.primary();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getInnerExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.MINUS_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("6",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());
    }

    public void testParenthizedExpression15() throws ANTLRException
    {
        setupParser("(value >>> 6)");

        ParenthesizedExpression result = (ParenthesizedExpression)_parser.primary();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getInnerExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.ZERO_FILL_SHIFT_RIGHT_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("6",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());
    }

    public void testParenthizedExpression16() throws ANTLRException
    {
        setupParser("(value <= 6)");

        ParenthesizedExpression result = (ParenthesizedExpression)_parser.primary();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getInnerExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.LOWER_OR_EQUAL_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("6",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());
    }

    public void testParenthizedExpression17() throws ANTLRException
    {
        setupParser("(arr instanceof String[])");

        ParenthesizedExpression result = (ParenthesizedExpression)_parser.primary();

        assertNotNull(result);

        InstanceofExpression instanceofExpr = (InstanceofExpression)result.getInnerExpression();

        assertNotNull(instanceofExpr);
        assertEquals(result,
                     instanceofExpr.getContainer());

        Type type = instanceofExpr.getReferencedType();

        assertNotNull(type);
        assertEquals(1,
                     type.getDimensions());
        assertTrue(!type.isPrimitive());
        assertEquals("String",
                     type.getBaseName());

        FieldAccess fieldAccess = (FieldAccess)instanceofExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("arr",
                     fieldAccess.getFieldName());
        assertEquals(instanceofExpr,
                     fieldAccess.getContainer());
    }

    public void testParenthizedExpression18() throws ANTLRException
    {
        setupParser("(value != 6)");

        ParenthesizedExpression result = (ParenthesizedExpression)_parser.primary();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getInnerExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.NOT_EQUAL_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("6",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());
    }

    public void testParenthizedExpression19() throws ANTLRException
    {
        setupParser("(value & 6)");

        ParenthesizedExpression result = (ParenthesizedExpression)_parser.primary();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getInnerExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_AND_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("6",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());
    }

    public void testParenthizedExpression2() throws ANTLRException
    {
        setupParser("(toString())");

        ParenthesizedExpression result = (ParenthesizedExpression)_parser.primary();

        assertNotNull(result);

        MethodInvocation methodInvoc = (MethodInvocation)result.getInnerExpression();

        assertNotNull(methodInvoc);
        assertEquals("toString",
                     methodInvoc.getMethodName());
        assertTrue(!methodInvoc.isTrailing());
        assertNull(methodInvoc.getArgumentList());
        assertEquals(result,
                     methodInvoc.getContainer());
    }

    public void testParenthizedExpression20() throws ANTLRException
    {
        setupParser("(value ^ 6)");

        ParenthesizedExpression result = (ParenthesizedExpression)_parser.primary();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getInnerExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_XOR_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("6",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());
    }

    public void testParenthizedExpression21() throws ANTLRException
    {
        setupParser("(value | 6)");

        ParenthesizedExpression result = (ParenthesizedExpression)_parser.primary();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getInnerExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_OR_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("6",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());
    }

    public void testParenthizedExpression22() throws ANTLRException
    {
        setupParser("(isValid && isCorrect())");

        ParenthesizedExpression result = (ParenthesizedExpression)_parser.primary();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getInnerExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.AND_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("isValid",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        MethodInvocation methodInvoc = (MethodInvocation)binExpr.getRightOperand();

        assertNotNull(methodInvoc);
        assertEquals("isCorrect",
                     methodInvoc.getMethodName());
        assertEquals(binExpr,
                     methodInvoc.getContainer());
    }

    public void testParenthizedExpression23() throws ANTLRException
    {
        setupParser("(isValid || isCorrect())");

        ParenthesizedExpression result = (ParenthesizedExpression)_parser.primary();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getInnerExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.OR_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("isValid",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        MethodInvocation methodInvoc = (MethodInvocation)binExpr.getRightOperand();

        assertNotNull(methodInvoc);
        assertEquals("isCorrect",
                     methodInvoc.getMethodName());
        assertEquals(binExpr,
                     methodInvoc.getContainer());
    }

    public void testParenthizedExpression24() throws ANTLRException
    {
        setupParser("(isCorrect() ? 1 : 2)");

        ParenthesizedExpression result = (ParenthesizedExpression)_parser.primary();

        assertNotNull(result);

        ConditionalExpression condExpr = (ConditionalExpression)result.getInnerExpression();

        assertNotNull(condExpr);
        assertEquals(result,
                     condExpr.getContainer());

        MethodInvocation methodInvoc = (MethodInvocation)condExpr.getCondition();

        assertNotNull(methodInvoc);
        assertEquals("isCorrect",
                     methodInvoc.getMethodName());
        assertEquals(condExpr,
                     methodInvoc.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)condExpr.getTrueExpression();

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

    public void testParenthizedExpression25() throws ANTLRException
    {
        setupParser("(isValid = isCorrect())");

        ParenthesizedExpression result = (ParenthesizedExpression)_parser.primary();

        assertNotNull(result);

        AssignmentExpression assignExpr = (AssignmentExpression)result.getInnerExpression();

        assertNotNull(assignExpr);
        assertEquals(AssignmentExpression.ASSIGN_OP,
                     assignExpr.getOperator());
        assertEquals(result,
                     assignExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertNotNull(fieldAccess);
        assertEquals("isValid",
                     fieldAccess.getFieldName());
        assertEquals(assignExpr,
                     fieldAccess.getContainer());

        MethodInvocation methodInvoc = (MethodInvocation)assignExpr.getValueExpression();

        assertNotNull(methodInvoc);
        assertEquals("isCorrect",
                     methodInvoc.getMethodName());
        assertEquals(assignExpr,
                     methodInvoc.getContainer());
    }

    public void testParenthizedExpression26() throws ANTLRException
    {
        setupParser("(Math.PI)");

        ParenthesizedExpression result = (ParenthesizedExpression)_parser.primary();

        assertNotNull(result);

        UnresolvedAccess unrslvdAccess = (UnresolvedAccess)result.getInnerExpression();

        assertNotNull(unrslvdAccess);
        assertTrue(!unrslvdAccess.isTrailing());
        assertEquals(2,
                     unrslvdAccess.getParts().getCount());
        assertEquals("Math",
                     unrslvdAccess.getParts().get(0));
        assertEquals("PI",
                     unrslvdAccess.getParts().get(1));
        assertEquals(result,
                     unrslvdAccess.getContainer());
    }

    public void testParenthizedExpression3() throws ANTLRException
    {
        setupParser("((0))");

        ParenthesizedExpression result = (ParenthesizedExpression)_parser.primary();

        assertNotNull(result);

        ParenthesizedExpression parenExpr = (ParenthesizedExpression)result.getInnerExpression();

        assertNotNull(parenExpr);
        assertEquals(result,
                     parenExpr.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)parenExpr.getInnerExpression();

        assertNotNull(intLit);
        assertEquals("0",
                     intLit.asString());
        assertEquals(parenExpr,
                     intLit.getContainer());
    }

    public void testParenthizedExpression4() throws ANTLRException
    {
        setupParser("(new String())");

        ParenthesizedExpression result = (ParenthesizedExpression)_parser.primary();

        assertNotNull(result);

        Instantiation instantiation = (Instantiation)result.getInnerExpression();

        assertNotNull(instantiation);
        assertTrue(!instantiation.isTrailing());
        assertEquals("String",
                     instantiation.getInstantiatedType().getBaseName());
        assertNull(instantiation.getArgumentList());
        assertEquals(result,
                     instantiation.getContainer());
    }

    public void testParenthizedExpression5() throws ANTLRException
    {
        setupParser("(this)");

        ParenthesizedExpression result = (ParenthesizedExpression)_parser.primary();

        assertNotNull(result);

        SelfAccess selfAccess = (SelfAccess)result.getInnerExpression();

        assertNotNull(selfAccess);
        assertTrue(!selfAccess.isQualified());
        assertNull(selfAccess.getTypeAccess());
        assertEquals(result,
                     selfAccess.getContainer());
    }

    public void testParenthizedExpression6()
    {
        setupParser("()");
        try
        {
            _parser.primary();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testParenthizedExpression7()
    {
        setupParser("(super)");
        try
        {
            _parser.primary();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testParenthizedExpression8()
    {
        setupParser("(void)");
        try
        {
            _parser.primary();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testParenthizedExpression9() throws ANTLRException
    {
        setupParser("(arr)");
        defineVariable("arr", false);

        ParenthesizedExpression result = (ParenthesizedExpression)_parser.primary();

        assertNotNull(result);

        VariableAccess varAccess = (VariableAccess)result.getInnerExpression();

        assertNotNull(varAccess);
        assertEquals("arr",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());
    }
}
