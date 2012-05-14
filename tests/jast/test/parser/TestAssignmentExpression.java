package jast.test.parser;
import jast.ast.nodes.ArrayAccess;
import jast.ast.nodes.ArrayCreation;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.ConditionalExpression;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.InstanceofExpression;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.ParenthesizedExpression;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.Type;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.UnresolvedAccess;
import jast.ast.nodes.VariableAccess;
import antlr.ANTLRException;

public class TestAssignmentExpression extends TestBase
{

    public TestAssignmentExpression(String name)
    {
        super(name);
    }

    public void testAssignmentExpression1() throws ANTLRException
    {
        setupParser("var *= 2");

        AssignmentExpression result = (AssignmentExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(AssignmentExpression.MULTIPLY_ASSIGN_OP,
                     result.getOperator());

        FieldAccess fieldAccess = (FieldAccess)result.getLeftHandSide();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)result.getValueExpression();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());
    }

    public void testAssignmentExpression10() throws ANTLRException
    {
        setupParser("isString = value instanceof String");

        AssignmentExpression result = (AssignmentExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(AssignmentExpression.ASSIGN_OP,
                     result.getOperator());

        FieldAccess fieldAccess = (FieldAccess)result.getLeftHandSide();

        assertNotNull(fieldAccess);
        assertEquals("isString",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());

        InstanceofExpression instanceofExpr = (InstanceofExpression)result.getValueExpression();

        assertNotNull(instanceofExpr);
        assertEquals(result,
                     instanceofExpr.getContainer());

        Type type = instanceofExpr.getReferencedType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertEquals("String",
                     type.getBaseName());

        fieldAccess = (FieldAccess)instanceofExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(instanceofExpr,
                     fieldAccess.getContainer());
    }

    public void testAssignmentExpression11() throws ANTLRException
    {
        setupParser("var = 2 > value");

        AssignmentExpression result = (AssignmentExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(AssignmentExpression.ASSIGN_OP,
                     result.getOperator());

        FieldAccess fieldAccess = (FieldAccess)result.getLeftHandSide();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getValueExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.GREATER_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getLeftOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        fieldAccess = (FieldAccess)binExpr.getRightOperand();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());
    }

    public void testAssignmentExpression12() throws ANTLRException
    {
        setupParser("var = 2 != value");

        AssignmentExpression result = (AssignmentExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(AssignmentExpression.ASSIGN_OP,
                     result.getOperator());

        FieldAccess fieldAccess = (FieldAccess)result.getLeftHandSide();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getValueExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.NOT_EQUAL_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getLeftOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        fieldAccess = (FieldAccess)binExpr.getRightOperand();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());
    }

    public void testAssignmentExpression13() throws ANTLRException
    {
        setupParser("var >>>= 2 & value");

        AssignmentExpression result = (AssignmentExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(AssignmentExpression.ZERO_FILL_SHIFT_RIGHT_ASSIGN_OP,
                     result.getOperator());

        FieldAccess fieldAccess = (FieldAccess)result.getLeftHandSide();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getValueExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_AND_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getLeftOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        fieldAccess = (FieldAccess)binExpr.getRightOperand();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());
    }

    public void testAssignmentExpression14() throws ANTLRException
    {
        setupParser("var &= 2 ^ value");

        AssignmentExpression result = (AssignmentExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(AssignmentExpression.BITWISE_AND_ASSIGN_OP,
                     result.getOperator());

        FieldAccess fieldAccess = (FieldAccess)result.getLeftHandSide();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getValueExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_XOR_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getLeftOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        fieldAccess = (FieldAccess)binExpr.getRightOperand();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());
    }

    public void testAssignmentExpression15() throws ANTLRException
    {
        setupParser("var ^= 2 | value");

        AssignmentExpression result = (AssignmentExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(AssignmentExpression.BITWISE_XOR_ASSIGN_OP,
                     result.getOperator());

        FieldAccess fieldAccess = (FieldAccess)result.getLeftHandSide();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getValueExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_OR_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getLeftOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        fieldAccess = (FieldAccess)binExpr.getRightOperand();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());
    }

    public void testAssignmentExpression16() throws ANTLRException
    {
        setupParser("var = value && isCorrect()");

        AssignmentExpression result = (AssignmentExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(AssignmentExpression.ASSIGN_OP,
                     result.getOperator());

        FieldAccess fieldAccess = (FieldAccess)result.getLeftHandSide();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getValueExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.AND_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("value",
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

    public void testAssignmentExpression17() throws ANTLRException
    {
        setupParser("var = value || isCorrect()");

        AssignmentExpression result = (AssignmentExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(AssignmentExpression.ASSIGN_OP,
                     result.getOperator());

        FieldAccess fieldAccess = (FieldAccess)result.getLeftHandSide();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getValueExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.OR_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("value",
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

    public void testAssignmentExpression18() throws ANTLRException
    {
        setupParser("var |= isCorrect() ? 1 : 2");

        AssignmentExpression result = (AssignmentExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(AssignmentExpression.BITWISE_OR_ASSIGN_OP,
                     result.getOperator());

        FieldAccess fieldAccess = (FieldAccess)result.getLeftHandSide();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());

        ConditionalExpression condExpr = (ConditionalExpression)result.getValueExpression();

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

    public void testAssignmentExpression19()
    {
        setupParser("2 = 1");
        try
        {
            _parser.expression();
        }
        catch (Exception ex)
        {
            return;
        }
        fail();
    }

    public void testAssignmentExpression2() throws ANTLRException
    {
        setupParser("var /= (2)");
        defineVariable("var", false);

        AssignmentExpression result = (AssignmentExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(AssignmentExpression.DIVIDE_ASSIGN_OP,
                     result.getOperator());

        VariableAccess varAccess = (VariableAccess)result.getLeftHandSide();

        assertNotNull(varAccess);
        assertEquals("var",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());

        ParenthesizedExpression parenExpr = (ParenthesizedExpression)result.getValueExpression();

        assertNotNull(parenExpr);
        assertEquals(result,
                     parenExpr.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)parenExpr.getInnerExpression();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(parenExpr,
                     intLit.getContainer());
    }

    public void testAssignmentExpression20()
    {
        setupParser("getValue() = 1");
        try
        {
            _parser.expression();
        }
        catch (Exception ex)
        {
            return;
        }
        fail();
    }

    public void testAssignmentExpression21()
    {
        setupParser("(var) = 1");
        try
        {
            _parser.expression();
        }
        catch (Exception ex)
        {
            return;
        }
        fail();
    }

    public void testAssignmentExpression3() throws ANTLRException
    {
        setupParser("obj.var = new int[0]");

        AssignmentExpression result = (AssignmentExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(AssignmentExpression.ASSIGN_OP,
                     result.getOperator());

        UnresolvedAccess unrslvdAccess = (UnresolvedAccess)result.getLeftHandSide();

        assertNotNull(unrslvdAccess);
        assertTrue(!unrslvdAccess.isTrailing());
        assertEquals(2,
                     unrslvdAccess.getParts().getCount());
        assertEquals("obj",
                     unrslvdAccess.getParts().get(0));
        assertEquals("var",
                     unrslvdAccess.getParts().get(1));
        assertEquals(result,
                     unrslvdAccess.getContainer());

        ArrayCreation arrayCreation = (ArrayCreation)result.getValueExpression();

        assertNotNull(arrayCreation);
        assertTrue(!arrayCreation.isInitialized());
        assertEquals(result,
                     arrayCreation.getContainer());

        Type type = arrayCreation.getCreatedType();

        assertTrue(type.isPrimitive());
        assertEquals(1,
                     type.getDimensions());
        assertEquals("int",
                     type.getBaseName());

        IntegerLiteral intLit = (IntegerLiteral)arrayCreation.getDimExpressions().get(0);

        assertNotNull(intLit);
        assertEquals("0",
                     intLit.asString());
        assertEquals(arrayCreation,
                     intLit.getContainer());
    }

    public void testAssignmentExpression4() throws ANTLRException
    {
        setupParser("var %= value++");

        AssignmentExpression result = (AssignmentExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(AssignmentExpression.MOD_ASSIGN_OP,
                     result.getOperator());

        FieldAccess fieldAccess = (FieldAccess)result.getLeftHandSide();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());

        PostfixExpression postfixExpr = (PostfixExpression)result.getValueExpression();

        assertNotNull(postfixExpr);
        assertTrue(postfixExpr.isIncrement());
        assertEquals(result,
                     postfixExpr.getContainer());

        fieldAccess = (FieldAccess)postfixExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(postfixExpr,
                     fieldAccess.getContainer());
    }

    public void testAssignmentExpression5() throws ANTLRException
    {
        setupParser("var = (String)obj");

        AssignmentExpression result = (AssignmentExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(AssignmentExpression.ASSIGN_OP,
                     result.getOperator());

        FieldAccess fieldAccess = (FieldAccess)result.getLeftHandSide();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());

        UnaryExpression unaryExpr = (UnaryExpression)result.getValueExpression();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.CAST_OP,
                     unaryExpr.getOperator());
        assertEquals(result,
                     unaryExpr.getContainer());

        Type type = unaryExpr.getCastType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertEquals("String",
                     type.getBaseName());

        fieldAccess = (FieldAccess)unaryExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("obj",
                     fieldAccess.getFieldName());
        assertEquals(unaryExpr,
                     fieldAccess.getContainer());
    }

    public void testAssignmentExpression6() throws ANTLRException
    {
        setupParser("var += ~1");

        AssignmentExpression result = (AssignmentExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(AssignmentExpression.PLUS_ASSIGN_OP,
                     result.getOperator());

        FieldAccess fieldAccess = (FieldAccess)result.getLeftHandSide();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());

        UnaryExpression unaryExpr = (UnaryExpression)result.getValueExpression();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.COMPLEMENT_OP,
                     unaryExpr.getOperator());
        assertEquals(result,
                     unaryExpr.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)unaryExpr.getInnerExpression();

        assertNotNull(intLit);
        assertEquals("1",
                     intLit.asString());
        assertEquals(unaryExpr,
                     intLit.getContainer());
    }

    public void testAssignmentExpression7() throws ANTLRException
    {
        setupParser("arr[2] -= 2 / value");

        AssignmentExpression result = (AssignmentExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(AssignmentExpression.MINUS_ASSIGN_OP,
                     result.getOperator());

        ArrayAccess arrayAccess = (ArrayAccess)result.getLeftHandSide();

        assertNotNull(arrayAccess);
        assertEquals(result,
                     arrayAccess.getContainer());

        FieldAccess fieldAccess = (FieldAccess)arrayAccess.getBaseExpression();

        assertNotNull(fieldAccess);
        assertEquals("arr",
                     fieldAccess.getFieldName());
        assertEquals(arrayAccess,
                     fieldAccess.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)arrayAccess.getIndexExpression();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(arrayAccess,
                     intLit.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getValueExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.DIVIDE_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        intLit = (IntegerLiteral)binExpr.getLeftOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        fieldAccess = (FieldAccess)binExpr.getRightOperand();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());
    }

    public void testAssignmentExpression8() throws ANTLRException
    {
        setupParser("var <<= 2 - value");

        AssignmentExpression result = (AssignmentExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(AssignmentExpression.SHIFT_LEFT_ASSIGN_OP,
                     result.getOperator());

        FieldAccess fieldAccess = (FieldAccess)result.getLeftHandSide();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getValueExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.MINUS_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getLeftOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        fieldAccess = (FieldAccess)binExpr.getRightOperand();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());
    }

    public void testAssignmentExpression9() throws ANTLRException
    {
        setupParser("var >>= 2 >> value");

        AssignmentExpression result = (AssignmentExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(AssignmentExpression.SHIFT_RIGHT_ASSIGN_OP,
                     result.getOperator());

        FieldAccess fieldAccess = (FieldAccess)result.getLeftHandSide();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getValueExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.SHIFT_RIGHT_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getLeftOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        fieldAccess = (FieldAccess)binExpr.getRightOperand();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());
    }
}
