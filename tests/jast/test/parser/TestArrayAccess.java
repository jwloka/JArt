package jast.test.parser;
import jast.ast.nodes.ArrayAccess;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.ConditionalExpression;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.UnresolvedAccess;
import jast.ast.nodes.VariableAccess;
import antlr.ANTLRException;

public class TestArrayAccess extends TestBase
{

    public TestArrayAccess(String name)
    {
        super(name);
    }

    public void testArrayAccess1() throws ANTLRException
    {
        setupParser("arr[0]");
        defineVariable("arr", false);

        ArrayAccess result = (ArrayAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isTrailing());

        IntegerLiteral intLit = (IntegerLiteral)result.getIndexExpression();

        assertNotNull(intLit);
        assertEquals("0",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());

        VariableAccess varAccess = (VariableAccess)result.getBaseExpression();

        assertNotNull(varAccess);
        assertEquals("arr",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());
    }

    public void testArrayAccess10() throws ANTLRException
    {
        setupParser("arr[num--]");
        defineVariable("arr", false);

        ArrayAccess result = (ArrayAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isTrailing());

        PostfixExpression postfixExpr = (PostfixExpression)result.getIndexExpression();

        assertNotNull(postfixExpr);
        assertTrue(!postfixExpr.isIncrement());
        assertEquals(result,
                     postfixExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)postfixExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("num",
                     fieldAccess.getFieldName());
        assertEquals(postfixExpr,
                     fieldAccess.getContainer());

        VariableAccess varAccess = (VariableAccess)result.getBaseExpression();

        assertNotNull(varAccess);
        assertEquals("arr",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());
    }

    public void testArrayAccess11() throws ANTLRException
    {
        setupParser("arr[(int)value]");
        defineVariable("arr", false);

        ArrayAccess result = (ArrayAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isTrailing());

        VariableAccess varAccess = (VariableAccess)result.getBaseExpression();

        assertNotNull(varAccess);
        assertEquals("arr",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());

        UnaryExpression unaryExpr = (UnaryExpression)result.getIndexExpression();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.CAST_OP,
                     unaryExpr.getOperator());
        assertEquals("int",
                     unaryExpr.getCastType().toString());
        assertEquals(result,
                     unaryExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)unaryExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("value",
                     fieldAccess.getFieldName());
        assertEquals(unaryExpr,
                     fieldAccess.getContainer());
    }

    public void testArrayAccess12() throws ANTLRException
    {
        setupParser("arr[value / 2]");
        defineVariable("arr", false);

        ArrayAccess result = (ArrayAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isTrailing());

        VariableAccess varAccess = (VariableAccess)result.getBaseExpression();

        assertNotNull(varAccess);
        assertEquals("arr",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getIndexExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.DIVIDE_OP,
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
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());
    }

    public void testArrayAccess13() throws ANTLRException
    {
        setupParser("arr[value - 2]");
        defineVariable("arr", false);

        ArrayAccess result = (ArrayAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isTrailing());

        VariableAccess varAccess = (VariableAccess)result.getBaseExpression();

        assertNotNull(varAccess);
        assertEquals("arr",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getIndexExpression();

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
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());
    }

    public void testArrayAccess14() throws ANTLRException
    {
        setupParser("arr[value >>> 2]");
        defineVariable("arr", false);

        ArrayAccess result = (ArrayAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isTrailing());

        VariableAccess varAccess = (VariableAccess)result.getBaseExpression();

        assertNotNull(varAccess);
        assertEquals("arr",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getIndexExpression();

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
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());
    }

    public void testArrayAccess15() throws ANTLRException
    {
        setupParser("arr[value&2]");
        defineVariable("arr", false);

        ArrayAccess result = (ArrayAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isTrailing());

        VariableAccess varAccess = (VariableAccess)result.getBaseExpression();

        assertNotNull(varAccess);
        assertEquals("arr",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getIndexExpression();

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
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());
    }

    public void testArrayAccess16() throws ANTLRException
    {
        setupParser("arr[value^2]");
        defineVariable("arr", false);

        ArrayAccess result = (ArrayAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isTrailing());

        VariableAccess varAccess = (VariableAccess)result.getBaseExpression();

        assertNotNull(varAccess);
        assertEquals("arr",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getIndexExpression();

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
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());
    }

    public void testArrayAccess17() throws ANTLRException
    {
        setupParser("arr[value|2]");
        defineVariable("arr", false);

        ArrayAccess result = (ArrayAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isTrailing());

        VariableAccess varAccess = (VariableAccess)result.getBaseExpression();

        assertNotNull(varAccess);
        assertEquals("arr",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getIndexExpression();

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
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());
    }

    public void testArrayAccess18() throws ANTLRException
    {
        setupParser("arr[isCorrect() ? 1 : 2]");
        defineVariable("arr", false);

        ArrayAccess result = (ArrayAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isTrailing());

        VariableAccess varAccess = (VariableAccess)result.getBaseExpression();

        assertNotNull(varAccess);
        assertEquals("arr",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());

        ConditionalExpression condExpr = (ConditionalExpression)result.getIndexExpression();

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

    public void testArrayAccess19() throws ANTLRException
    {
        setupParser("arr[idx = 2]");
        defineVariable("arr", false);

        ArrayAccess result = (ArrayAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isTrailing());

        VariableAccess varAccess = (VariableAccess)result.getBaseExpression();

        assertNotNull(varAccess);
        assertEquals("arr",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());

        AssignmentExpression assignExpr = (AssignmentExpression)result.getIndexExpression();

        assertNotNull(assignExpr);
        assertEquals(AssignmentExpression.ASSIGN_OP,
                     assignExpr.getOperator());
        assertEquals(result,
                     assignExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertNotNull(fieldAccess);
        assertEquals("idx",
                     fieldAccess.getFieldName());
        assertEquals(assignExpr,
                     fieldAccess.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)assignExpr.getValueExpression();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(assignExpr,
                     intLit.getContainer());
    }

    public void testArrayAccess2() throws ANTLRException
    {
        setupParser("toArray()[1]");

        ArrayAccess result = (ArrayAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isTrailing());

        IntegerLiteral intLit = (IntegerLiteral)result.getIndexExpression();

        assertNotNull(intLit);
        assertEquals("1",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());

        MethodInvocation methodInvoc = (MethodInvocation)result.getBaseExpression();

        assertNotNull(methodInvoc);
        assertEquals("toArray",
                     methodInvoc.getMethodName());
        assertTrue(!methodInvoc.isTrailing());
        assertNull(methodInvoc.getArgumentList());
        assertEquals(result,
                     methodInvoc.getContainer());
    }

    public void testArrayAccess3() throws ANTLRException
    {
        setupParser("obj.arr[0]");

        ArrayAccess result = (ArrayAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isTrailing());

        IntegerLiteral intLit = (IntegerLiteral)result.getIndexExpression();

        assertNotNull(intLit);
        assertEquals("0",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());
        assertEquals(result,
                     intLit.getContainer());

        UnresolvedAccess unresolvedAccess = (UnresolvedAccess)result.getBaseExpression();

        assertNotNull(unresolvedAccess);
        assertEquals(2,
                     unresolvedAccess.getParts().getCount());
        assertEquals("obj",
                     unresolvedAccess.getParts().get(0));
        assertEquals("arr",
                     unresolvedAccess.getParts().get(1));
        assertEquals(result,
                     unresolvedAccess.getContainer());
        assertEquals(result,
                     unresolvedAccess.getContainer());
    }

    public void testArrayAccess4() throws ANTLRException
    {
        setupParser("obj.arr[0]");
        defineVariable("obj", false);

        ArrayAccess result = (ArrayAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isTrailing());

        IntegerLiteral intLit = (IntegerLiteral)result.getIndexExpression();

        assertNotNull(intLit);
        assertEquals("0",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());

        FieldAccess fieldAccess = (FieldAccess)result.getBaseExpression();

        assertNotNull(fieldAccess);
        assertEquals("arr",
                     fieldAccess.getFieldName());
        assertTrue(fieldAccess.isTrailing());
        assertEquals(result,
                     fieldAccess.getContainer());

        VariableAccess varAccess = (VariableAccess)fieldAccess.getBaseExpression();

        assertNotNull(varAccess);
        assertEquals("obj",
                     varAccess.getVariableName());
        assertEquals(fieldAccess,
                     varAccess.getContainer());
    }

    public void testArrayAccess5() throws ANTLRException
    {
        setupParser("obj1.obj2.arr[0]");
        defineVariable("obj1", false);

        ArrayAccess result = (ArrayAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isTrailing());

        IntegerLiteral intLit = (IntegerLiteral)result.getIndexExpression();

        assertNotNull(intLit);
        assertEquals("0",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());

        UnresolvedAccess unrslvdAccess = (UnresolvedAccess)result.getBaseExpression();

        assertNotNull(unrslvdAccess);
        assertEquals(2,
                     unrslvdAccess.getParts().getCount());
        assertEquals("obj2",
                     unrslvdAccess.getParts().get(0));
        assertEquals("arr",
                     unrslvdAccess.getParts().get(1));
        assertTrue(unrslvdAccess.isTrailing());
        assertEquals(result,
                     unrslvdAccess.getContainer());

        VariableAccess varAccess = (VariableAccess)unrslvdAccess.getBaseExpression();

        assertNotNull(varAccess);
        assertEquals("obj1",
                     varAccess.getVariableName());
        assertEquals(unrslvdAccess,
                     varAccess.getContainer());
    }

    public void testArrayAccess6() throws ANTLRException
    {
        setupParser("obj1.obj2.arr[0]");

        ArrayAccess result = (ArrayAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isTrailing());

        IntegerLiteral intLit = (IntegerLiteral)result.getIndexExpression();

        assertNotNull(intLit);
        assertEquals("0",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());

        UnresolvedAccess unresolvedAccess = (UnresolvedAccess)result.getBaseExpression();

        assertNotNull(unresolvedAccess);
        assertEquals(3,
                     unresolvedAccess.getParts().getCount());
        assertEquals("obj1",
                     unresolvedAccess.getParts().get(0));
        assertEquals("obj2",
                     unresolvedAccess.getParts().get(1));
        assertEquals("arr",
                     unresolvedAccess.getParts().get(2));
        assertEquals(result,
                     unresolvedAccess.getContainer());
    }

    public void testArrayAccess7()
    {
        setupParser("(new Object[0])[0]");
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

    public void testArrayAccess8()
    {
        setupParser("(new Object[0]).length[0]");
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

    public void testArrayAccess9() throws ANTLRException
    {
        setupParser("arr[num]");
        defineVariable("num", false);

        ArrayAccess result = (ArrayAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isTrailing());

        VariableAccess varAccess = (VariableAccess)result.getIndexExpression();

        assertNotNull(varAccess);
        assertEquals("num",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());

        FieldAccess fieldAccess = (FieldAccess)result.getBaseExpression();

        assertNotNull(fieldAccess);
        assertEquals("arr",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());
    }
}
