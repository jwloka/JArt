package jast.test.parser;
import jast.ast.nodes.ArgumentList;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.BooleanLiteral;
import jast.ast.nodes.ConditionalExpression;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.VariableAccess;
import antlr.ANTLRException;

public class TestArgumentList extends TestBase
{

    public TestArgumentList(String name)
    {
        super(name);
    }

    public void testArgumentList1() throws ANTLRException
    {
        setupParser("true");

        ArgumentList result = _parser.argumentList();

        assertNotNull(result);
        assertEquals(1,
                     result.getArguments().getCount());

        BooleanLiteral boolLit = (BooleanLiteral)result.getArguments().get(0);

        assertNotNull(boolLit);
        assertTrue(boolLit.getValue());
        assertEquals(result,
                     boolLit.getContainer());
    }

    public void testArgumentList10() throws ANTLRException
    {
        setupParser("a & b, var ^ 2, 255 | getValue()");

        ArgumentList result = _parser.argumentList();

        assertNotNull(result);
        assertEquals(3,
                     result.getArguments().getCount());

        BinaryExpression binExpr = (BinaryExpression)result.getArguments().get(0);

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_AND_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("a",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        fieldAccess = (FieldAccess)binExpr.getRightOperand();

        assertNotNull(fieldAccess);
        assertEquals("b",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        binExpr = (BinaryExpression)result.getArguments().get(1);

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_XOR_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        binExpr = (BinaryExpression)result.getArguments().get(2);

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_OR_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        intLit = (IntegerLiteral)binExpr.getLeftOperand();

        assertNotNull(intLit);
        assertEquals("255",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        MethodInvocation methodInvoc = (MethodInvocation)binExpr.getRightOperand();

        assertNotNull(methodInvoc);
        assertEquals("getValue",
                     methodInvoc.getMethodName());
        assertEquals(binExpr,
                     methodInvoc.getContainer());
    }

    public void testArgumentList11() throws ANTLRException
    {
        setupParser("a && b, var || getValue()");
        defineVariable("var", false);

        ArgumentList result = _parser.argumentList();

        assertNotNull(result);
        assertEquals(2,
                     result.getArguments().getCount());

        BinaryExpression binExpr = (BinaryExpression)result.getArguments().get(0);

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.AND_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("a",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        fieldAccess = (FieldAccess)binExpr.getRightOperand();

        assertNotNull(fieldAccess);
        assertEquals("b",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        binExpr = (BinaryExpression)result.getArguments().get(1);

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.OR_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        VariableAccess varAccess = (VariableAccess)binExpr.getLeftOperand();

        assertNotNull(varAccess);
        assertEquals("var",
                     varAccess.getVariableName());
        assertEquals(binExpr,
                     varAccess.getContainer());

        MethodInvocation methodInvoc = (MethodInvocation)binExpr.getRightOperand();

        assertNotNull(methodInvoc);
        assertEquals("getValue",
                     methodInvoc.getMethodName());
        assertEquals(binExpr,
                     methodInvoc.getContainer());
    }

    public void testArgumentList12() throws ANTLRException
    {
        setupParser("0, isCorrect() ? 1 : 2, var += 2");

        ArgumentList result = _parser.argumentList();

        assertNotNull(result);
        assertEquals(3,
                     result.getArguments().getCount());

        IntegerLiteral intLit = (IntegerLiteral)result.getArguments().get(0);

        assertNotNull(intLit);
        assertEquals("0",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());

        ConditionalExpression condExpr = (ConditionalExpression)result.getArguments().get(1);

        assertNotNull(condExpr);
        assertEquals(result,
                     condExpr.getContainer());

        MethodInvocation methodInvoc = (MethodInvocation)condExpr.getCondition();

        assertNotNull(methodInvoc);
        assertEquals("isCorrect",
                     methodInvoc.getMethodName());
        assertEquals(condExpr,
                     methodInvoc.getContainer());

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

        AssignmentExpression assignExpr = (AssignmentExpression)result.getArguments().get(2);

        assertNotNull(assignExpr);
        assertEquals(AssignmentExpression.PLUS_ASSIGN_OP,
                     assignExpr.getOperator());
        assertEquals(result,
                     assignExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(assignExpr,
                     fieldAccess.getContainer());

        intLit = (IntegerLiteral)assignExpr.getValueExpression();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(assignExpr,
                     intLit.getContainer());
    }

    public void testArgumentList2() throws ANTLRException
    {
        setupParser("1, new String()");

        ArgumentList result = _parser.argumentList();

        assertNotNull(result);
        assertEquals(2,
                     result.getArguments().getCount());

        IntegerLiteral intLit = (IntegerLiteral)result.getArguments().get(0);

        assertNotNull(intLit);
        assertEquals("1",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());

        Instantiation instantiation = (Instantiation)result.getArguments().get(1);

        assertNotNull(instantiation);
        assertEquals("String",
                     instantiation.getInstantiatedType().getBaseName());
        assertEquals(result,
                     instantiation.getContainer());
    }

    public void testArgumentList3() throws ANTLRException
    {
        setupParser("void");
        try
        {
            _parser.argumentList();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testArgumentList4() throws ANTLRException
    {
        setupParser("0, void, \"text\"");
        try
        {
            _parser.argumentList();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testArgumentList5() throws ANTLRException
    {
        setupParser("idx++");
        defineVariable("idx", false);

        ArgumentList result = _parser.argumentList();

        assertNotNull(result);
        assertEquals(1,
                     result.getArguments().getCount());

        PostfixExpression postfixExpr = (PostfixExpression)result.getArguments().get(0);

        assertNotNull(postfixExpr);
        assertTrue(postfixExpr.isIncrement());
        assertEquals(result,
                     postfixExpr.getContainer());

        VariableAccess varAccess = (VariableAccess)postfixExpr.getInnerExpression();

        assertNotNull(varAccess);
        assertEquals("idx",
                     varAccess.getVariableName());
        assertEquals(postfixExpr,
                     varAccess.getContainer());
    }

    public void testArgumentList6() throws ANTLRException
    {
        setupParser("(int)var, new String()");

        ArgumentList result = _parser.argumentList();

        assertNotNull(result);
        assertEquals(2,
                     result.getArguments().getCount());

        UnaryExpression unaryExpr = (UnaryExpression)result.getArguments().get(0);

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.CAST_OP,
                     unaryExpr.getOperator());
        assertEquals("int",
                     unaryExpr.getCastType().toString());
        assertEquals(result,
                     unaryExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)unaryExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(unaryExpr,
                     fieldAccess.getContainer());

        Instantiation instantiation = (Instantiation)result.getArguments().get(1);

        assertNotNull(instantiation);
        assertEquals("String",
                     instantiation.getInstantiatedType().getBaseName());
        assertEquals(result,
                     instantiation.getContainer());
    }

    public void testArgumentList7() throws ANTLRException
    {
        setupParser("2/3, var");

        ArgumentList result = _parser.argumentList();

        assertNotNull(result);
        assertEquals(2,
                     result.getArguments().getCount());

        BinaryExpression binExpr = (BinaryExpression)result.getArguments().get(0);

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.DIVIDE_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getLeftOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("3",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        FieldAccess fieldAccess = (FieldAccess)result.getArguments().get(1);

        assertNotNull(fieldAccess);
        assertEquals("var",
                    fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());
    }

    public void testArgumentList8() throws ANTLRException
    {
        setupParser("2+3, var << 2");

        ArgumentList result = _parser.argumentList();

        assertNotNull(result);
        assertEquals(2,
                     result.getArguments().getCount());

        BinaryExpression binExpr = (BinaryExpression)result.getArguments().get(0);

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.PLUS_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getLeftOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("3",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        binExpr = (BinaryExpression)result.getArguments().get(1);

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.SHIFT_LEFT_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("var",
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

    public void testArgumentList9() throws ANTLRException
    {
        setupParser("2 >= 3, var == 2");

        ArgumentList result = _parser.argumentList();

        assertNotNull(result);
        assertEquals(2,
                     result.getArguments().getCount());

        BinaryExpression binExpr = (BinaryExpression)result.getArguments().get(0);

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.GREATER_OR_EQUAL_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getLeftOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("3",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());

        binExpr = (BinaryExpression)result.getArguments().get(1);

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.EQUAL_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("var",
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
}
