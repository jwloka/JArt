package jast.test.parser;
import jast.ast.nodes.ArrayInitializer;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.BooleanLiteral;
import jast.ast.nodes.ConditionalExpression;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FloatingPointLiteral;
import jast.ast.nodes.InstanceofExpression;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.SingleInitializer;
import jast.ast.nodes.StringLiteral;
import jast.ast.nodes.Type;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.VariableAccess;
import antlr.ANTLRException;

public class TestVariableInitializer extends TestBase
{

    public TestVariableInitializer(String name)
    {
        super(name);
    }

    public void testArrayInitializer1() throws ANTLRException
    {
        setupParser("{}");

        ArrayInitializer result = (ArrayInitializer)_parser.variableInitializer();

        assertNotNull(result);
        assertEquals(0,
                     result.getInitializers().getCount());
    }

    public void testArrayInitializer10() throws ANTLRException
    {
        setupParser("{ 2 >= 3, var == 2 }");

        ArrayInitializer result = (ArrayInitializer)_parser.variableInitializer();

        assertNotNull(result);
        assertEquals(2,
                     result.getInitializers().getCount());

        SingleInitializer innerInit = (SingleInitializer)result.getInitializers().get(0);

        assertNotNull(innerInit);
        assertEquals(result,
                     innerInit.getContainer());

        BinaryExpression binExpr = (BinaryExpression)innerInit.getInitEpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.GREATER_OR_EQUAL_OP,
                     binExpr.getOperator());
        assertEquals(innerInit,
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

        innerInit = (SingleInitializer)result.getInitializers().get(1);

        assertNotNull(innerInit);
        assertEquals(result,
                     innerInit.getContainer());

        binExpr = (BinaryExpression)innerInit.getInitEpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.EQUAL_OP,
                     binExpr.getOperator());
        assertEquals(innerInit,
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

    public void testArrayInitializer11() throws ANTLRException
    {
        setupParser("{a & b, var ^ 2, 255 | getValue()}");

        ArrayInitializer result = (ArrayInitializer)_parser.variableInitializer();

        assertNotNull(result);
        assertEquals(3,
                     result.getInitializers().getCount());

        SingleInitializer innerInit = (SingleInitializer)result.getInitializers().get(0);

        assertNotNull(innerInit);
        assertEquals(result,
                     innerInit.getContainer());

        BinaryExpression binExpr = (BinaryExpression)innerInit.getInitEpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_AND_OP,
                     binExpr.getOperator());
        assertEquals(innerInit,
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

        innerInit = (SingleInitializer)result.getInitializers().get(1);

        assertNotNull(innerInit);
        assertEquals(result,
                     innerInit.getContainer());

        binExpr = (BinaryExpression)innerInit.getInitEpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_XOR_OP,
                     binExpr.getOperator());
        assertEquals(innerInit,
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

        innerInit = (SingleInitializer)result.getInitializers().get(2);

        assertNotNull(innerInit);
        assertEquals(result,
                     innerInit.getContainer());

        binExpr = (BinaryExpression)innerInit.getInitEpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_OR_OP,
                     binExpr.getOperator());
        assertEquals(innerInit,
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

    public void testArrayInitializer12() throws ANTLRException
    {
        setupParser("{a && b, var || getValue()}");
        defineVariable("var", false);

        ArrayInitializer result = (ArrayInitializer)_parser.variableInitializer();

        assertNotNull(result);
        assertEquals(2,
                     result.getInitializers().getCount());

        SingleInitializer innerInit = (SingleInitializer)result.getInitializers().get(0);

        assertNotNull(innerInit);
        assertEquals(result,
                     innerInit.getContainer());

        BinaryExpression binExpr = (BinaryExpression)innerInit.getInitEpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.AND_OP,
                     binExpr.getOperator());
        assertEquals(innerInit,
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

        innerInit = (SingleInitializer)result.getInitializers().get(1);

        assertNotNull(innerInit);
        assertEquals(result,
                     innerInit.getContainer());

        binExpr = (BinaryExpression)innerInit.getInitEpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.OR_OP,
                     binExpr.getOperator());
        assertEquals(innerInit,
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

    public void testArrayInitializer13() throws ANTLRException
    {
        setupParser("{ idx, isCorrect() ? 1 : 2, count = 2 }");
        defineVariable("idx", false);

        ArrayInitializer result = (ArrayInitializer)_parser.variableInitializer();

        assertNotNull(result);
        assertEquals(3,
                     result.getInitializers().getCount());

        SingleInitializer innerInit = (SingleInitializer)result.getInitializers().get(0);

        assertNotNull(innerInit);
        assertEquals(result,
                     innerInit.getContainer());

        VariableAccess varAccess = (VariableAccess)innerInit.getInitEpression();

        assertNotNull(varAccess);
        assertEquals("idx",
                     varAccess.getVariableName());
        assertEquals(innerInit,
                     varAccess.getContainer());

        innerInit = (SingleInitializer)result.getInitializers().get(1);

        assertNotNull(innerInit);
        assertEquals(result,
                     innerInit.getContainer());

        ConditionalExpression condExpr = (ConditionalExpression)innerInit.getInitEpression();

        assertNotNull(condExpr);
        assertEquals(innerInit,
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

        innerInit = (SingleInitializer)result.getInitializers().get(2);

        assertNotNull(innerInit);
        assertEquals(result,
                     innerInit.getContainer());

        AssignmentExpression assignExpr = (AssignmentExpression)innerInit.getInitEpression();

        assertNotNull(assignExpr);
        assertEquals(AssignmentExpression.ASSIGN_OP,
                     assignExpr.getOperator());
        assertEquals(innerInit,
                     assignExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertNotNull(fieldAccess);
        assertEquals("count",
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

    public void testArrayInitializer2() throws ANTLRException
    {
        setupParser("{true}");

        ArrayInitializer result = (ArrayInitializer)_parser.variableInitializer();

        assertNotNull(result);
        assertEquals(1,
                     result.getInitializers().getCount());

        SingleInitializer innerInit = (SingleInitializer)result.getInitializers().get(0);

        assertNotNull(innerInit);
        assertEquals(result,
                     innerInit.getContainer());

        BooleanLiteral boolLit = (BooleanLiteral)innerInit.getInitEpression();

        assertNotNull(boolLit);
        assertTrue(boolLit.getValue());
        assertEquals(innerInit,
                     boolLit.getContainer());
    }

    public void testArrayInitializer3() throws ANTLRException
    {
        setupParser("{idx, 1.0f }");
        defineVariable("idx", false);

        ArrayInitializer result = (ArrayInitializer)_parser.variableInitializer();

        assertNotNull(result);
        assertEquals(2,
                     result.getInitializers().getCount());

        SingleInitializer innerInit = (SingleInitializer)result.getInitializers().get(0);

        assertNotNull(innerInit);
        assertEquals(result,
                     innerInit.getContainer());

        VariableAccess varAccess = (VariableAccess)innerInit.getInitEpression();

        assertNotNull(varAccess);
        assertEquals("idx",
                     varAccess.getVariableName());
        assertEquals(innerInit,
                     varAccess.getContainer());

        innerInit = (SingleInitializer)result.getInitializers().get(1);

        assertNotNull(innerInit);
        assertEquals(result,
                     innerInit.getContainer());

        FloatingPointLiteral floatLit = (FloatingPointLiteral)innerInit.getInitEpression();

        assertNotNull(floatLit);
        assertEquals("1.0f",
                     floatLit.asString());
        assertEquals(innerInit,
                     floatLit.getContainer());
    }

    public void testArrayInitializer4() throws ANTLRException
    {
        setupParser("{ {}, \"text\" }");

        ArrayInitializer result = (ArrayInitializer)_parser.variableInitializer();

        assertNotNull(result);
        assertEquals(2,
                     result.getInitializers().getCount());

        ArrayInitializer innerInit1 = (ArrayInitializer)result.getInitializers().get(0);

        assertNotNull(innerInit1);
        assertEquals(0,
                     innerInit1.getInitializers().getCount());
        assertEquals(result,
                     innerInit1.getContainer());

        SingleInitializer innerInit2 = (SingleInitializer)result.getInitializers().get(1);

        assertNotNull(innerInit2);
        assertEquals(result,
                     innerInit2.getContainer());

        StringLiteral stringLit = (StringLiteral)innerInit2.getInitEpression();

        assertNotNull(stringLit);
        assertEquals("text",
                     stringLit.getValue());
    }

    public void testArrayInitializer5() throws ANTLRException
    {
        setupParser("{ void }");
        try
        {
            _parser.variableInitializer();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testArrayInitializer6() throws ANTLRException
    {
        setupParser("{var--}");

        ArrayInitializer result = (ArrayInitializer)_parser.variableInitializer();

        assertNotNull(result);
        assertEquals(1,
                     result.getInitializers().getCount());

        SingleInitializer innerInit = (SingleInitializer)result.getInitializers().get(0);

        assertNotNull(innerInit);
        assertEquals(result,
                     innerInit.getContainer());

        PostfixExpression postfixExpr = (PostfixExpression)innerInit.getInitEpression();

        assertNotNull(postfixExpr);
        assertTrue(!postfixExpr.isIncrement());
        assertEquals(innerInit,
                     postfixExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)postfixExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(postfixExpr,
                     fieldAccess.getContainer());
    }

    public void testArrayInitializer7() throws ANTLRException
    {
        setupParser("{ idx++ }");
        defineVariable("idx", false);

        ArrayInitializer result = (ArrayInitializer)_parser.variableInitializer();

        assertNotNull(result);
        assertEquals(1,
                     result.getInitializers().getCount());

        SingleInitializer innerInit = (SingleInitializer)result.getInitializers().get(0);

        assertNotNull(innerInit);
        assertEquals(result,
                     innerInit.getContainer());

        PostfixExpression postfixExpr = (PostfixExpression)innerInit.getInitEpression();

        assertNotNull(postfixExpr);
        assertTrue(postfixExpr.isIncrement());
        assertEquals(innerInit,
                     postfixExpr.getContainer());

        VariableAccess varAccess = (VariableAccess)postfixExpr.getInnerExpression();

        assertNotNull(varAccess);
        assertEquals("idx",
                     varAccess.getVariableName());
        assertEquals(postfixExpr,
                     varAccess.getContainer());
    }

    public void testArrayInitializer8() throws ANTLRException
    {
        setupParser("{ idx, -1 }");
        defineVariable("idx", false);

        ArrayInitializer result = (ArrayInitializer)_parser.variableInitializer();

        assertNotNull(result);
        assertEquals(2,
                     result.getInitializers().getCount());

        SingleInitializer innerInit = (SingleInitializer)result.getInitializers().get(0);

        assertNotNull(innerInit);
        assertEquals(result,
                     innerInit.getContainer());

        VariableAccess varAccess = (VariableAccess)innerInit.getInitEpression();

        assertNotNull(varAccess);
        assertEquals("idx",
                     varAccess.getVariableName());
        assertEquals(innerInit,
                     varAccess.getContainer());

        innerInit = (SingleInitializer)result.getInitializers().get(1);

        assertNotNull(innerInit);
        assertEquals(result,
                     innerInit.getContainer());

        UnaryExpression unaryExpr = (UnaryExpression)innerInit.getInitEpression();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.MINUS_OP,
                     unaryExpr.getOperator());
        assertEquals(innerInit,
                     unaryExpr.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)unaryExpr.getInnerExpression();

        assertNotNull(intLit);
        assertEquals("1",
                     intLit.asString());
        assertEquals(unaryExpr,
                     intLit.getContainer());
    }

    public void testArrayInitializer9() throws ANTLRException
    {
        setupParser("{ 2/3, var + 2}");

        ArrayInitializer result = (ArrayInitializer)_parser.variableInitializer();

        assertNotNull(result);
        assertEquals(2,
                    result.getInitializers().getCount());

        SingleInitializer innerInit = (SingleInitializer)result.getInitializers().get(0);

        assertNotNull(innerInit);
        assertEquals(result,
                     innerInit.getContainer());

        BinaryExpression binExpr = (BinaryExpression)innerInit.getInitEpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.DIVIDE_OP,
                     binExpr.getOperator());
        assertEquals(innerInit,
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

        innerInit = (SingleInitializer)result.getInitializers().get(1);

        assertNotNull(innerInit);
        assertEquals(result,
                     innerInit.getContainer());

        binExpr = (BinaryExpression)innerInit.getInitEpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.PLUS_OP,
                     binExpr.getOperator());
        assertEquals(innerInit,
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

    public void testSingleInitializer1() throws ANTLRException
    {
        setupParser("1");

        SingleInitializer result = (SingleInitializer)_parser.variableInitializer();

        assertNotNull(result);

        IntegerLiteral intLit = (IntegerLiteral)result.getInitEpression();

        assertNotNull(intLit);
        assertEquals("1",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());
    }

    public void testSingleInitializer10() throws ANTLRException
    {
        setupParser("var instanceof String");

        SingleInitializer result = (SingleInitializer)_parser.variableInitializer();

        assertNotNull(result);

        InstanceofExpression instanceofExpr = (InstanceofExpression)result.getInitEpression();

        assertNotNull(instanceofExpr);
        assertEquals(result,
                     instanceofExpr.getContainer());

        Type type = instanceofExpr.getReferencedType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertEquals("String",
                     type.getBaseName());

        FieldAccess fieldAccess = (FieldAccess)instanceofExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(instanceofExpr,
                     fieldAccess.getContainer());
    }

    public void testSingleInitializer11() throws ANTLRException
    {
        setupParser("var != 2");

        SingleInitializer result = (SingleInitializer)_parser.variableInitializer();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getInitEpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.NOT_EQUAL_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

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
    }

    public void testSingleInitializer12() throws ANTLRException
    {
        setupParser("var & 2");

        SingleInitializer result = (SingleInitializer)_parser.variableInitializer();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getInitEpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_AND_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

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
    }

    public void testSingleInitializer13() throws ANTLRException
    {
        setupParser("var ^ 2");

        SingleInitializer result = (SingleInitializer)_parser.variableInitializer();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getInitEpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_XOR_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

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
    }

    public void testSingleInitializer14() throws ANTLRException
    {
        setupParser("var | 2");

        SingleInitializer result = (SingleInitializer)_parser.variableInitializer();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getInitEpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_OR_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

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
    }

    public void testSingleInitializer15() throws ANTLRException
    {
        setupParser("var && getValue()");

        SingleInitializer result = (SingleInitializer)_parser.variableInitializer();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getInitEpression();

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
        assertEquals("getValue",
                     methodInvoc.getMethodName());
        assertEquals(binExpr,
                     methodInvoc.getContainer());
    }

    public void testSingleInitializer16() throws ANTLRException
    {
        setupParser("var || getValue()");

        SingleInitializer result = (SingleInitializer)_parser.variableInitializer();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getInitEpression();

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
        assertEquals("getValue",
                     methodInvoc.getMethodName());
        assertEquals(binExpr,
                     methodInvoc.getContainer());
    }

    public void testSingleInitializer17() throws ANTLRException
    {
        setupParser("isCorrect() ? 1 : 2");

        SingleInitializer result = (SingleInitializer)_parser.variableInitializer();

        assertNotNull(result);

        ConditionalExpression condExpr = (ConditionalExpression)result.getInitEpression();

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

    public void testSingleInitializer18() throws ANTLRException
    {
        setupParser("count = 2");

        SingleInitializer result = (SingleInitializer)_parser.variableInitializer();

        assertNotNull(result);

        AssignmentExpression assignExpr = (AssignmentExpression)result.getInitEpression();

        assertNotNull(assignExpr);
        assertEquals(AssignmentExpression.ASSIGN_OP,
                     assignExpr.getOperator());
        assertEquals(result,
                     assignExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertNotNull(fieldAccess);
        assertEquals("count",
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

    public void testSingleInitializer2() throws ANTLRException
    {
        setupParser("getValue()");

        SingleInitializer result = (SingleInitializer)_parser.variableInitializer();

        assertNotNull(result);

        MethodInvocation methodInvoc = (MethodInvocation)result.getInitEpression();

        assertNotNull(methodInvoc);
        assertEquals("getValue",
                     methodInvoc.getMethodName());
        assertEquals(result,
                     methodInvoc.getContainer());
    }

    public void testSingleInitializer3()
    {
        setupParser("void");
        try
        {
            _parser.variableInitializer();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testSingleInitializer4() throws ANTLRException
    {
        setupParser("var++");

        SingleInitializer result = (SingleInitializer)_parser.variableInitializer();

        assertNotNull(result);

        PostfixExpression postfixExpr = (PostfixExpression)result.getInitEpression();

        assertNotNull(postfixExpr);
        assertTrue(postfixExpr.isIncrement());
        assertEquals(result,
                     postfixExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)postfixExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(postfixExpr,
                     fieldAccess.getContainer());
    }

    public void testSingleInitializer5() throws ANTLRException
    {
        setupParser("++var");

        SingleInitializer result = (SingleInitializer)_parser.variableInitializer();

        assertNotNull(result);

        UnaryExpression unaryExpr = (UnaryExpression)result.getInitEpression();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.INCREMENT_OP,
                     unaryExpr.getOperator());
        assertEquals(result,
                     unaryExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)unaryExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(unaryExpr,
                     fieldAccess.getContainer());
    }

    public void testSingleInitializer6() throws ANTLRException
    {
        setupParser("var/2.0");

        SingleInitializer result = (SingleInitializer)_parser.variableInitializer();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getInitEpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.DIVIDE_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        FloatingPointLiteral floatLit = (FloatingPointLiteral)binExpr.getRightOperand();

        assertNotNull(floatLit);
        assertEquals("2.0",
                     floatLit.asString());
        assertEquals(binExpr,
                     floatLit.getContainer());
    }

    public void testSingleInitializer7() throws ANTLRException
    {
        setupParser("var-2.0");

        SingleInitializer result = (SingleInitializer)_parser.variableInitializer();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getInitEpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.MINUS_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());

        FloatingPointLiteral floatLit = (FloatingPointLiteral)binExpr.getRightOperand();

        assertNotNull(floatLit);
        assertEquals("2.0",
                     floatLit.asString());
        assertEquals(binExpr,
                     floatLit.getContainer());
    }

    public void testSingleInitializer8() throws ANTLRException
    {
        setupParser("var>>2");

        SingleInitializer result = (SingleInitializer)_parser.variableInitializer();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getInitEpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.SHIFT_RIGHT_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

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
    }

    public void testSingleInitializer9() throws ANTLRException
    {
        setupParser("var <= 2");

        SingleInitializer result = (SingleInitializer)_parser.variableInitializer();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getInitEpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.LOWER_OR_EQUAL_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

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
    }
}
