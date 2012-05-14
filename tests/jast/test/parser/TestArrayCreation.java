package jast.test.parser;
import jast.ast.nodes.ArrayCreation;
import jast.ast.nodes.ArrayInitializer;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.BooleanLiteral;
import jast.ast.nodes.ConditionalExpression;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.SingleInitializer;
import jast.ast.nodes.StringLiteral;
import jast.ast.nodes.Type;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.VariableAccess;
import antlr.ANTLRException;

public class TestArrayCreation extends TestBase
{

    public TestArrayCreation(String name)
    {
        super(name);
    }

    public void testArrayCreation1() throws ANTLRException
    {
        setupParser("new boolean[0]");

        ArrayCreation result = (ArrayCreation)_parser.primary();

        assertNotNull(result);

        Type type = result.getCreatedType();

        assertNotNull(type);
        assertEquals(1,
                     type.getDimensions());
        assertTrue(type.isPrimitive());
        assertEquals("boolean",
                     type.getBaseName());

        assertTrue(!result.isInitialized());
        assertEquals(1,
                     result.getDimExpressions().getCount());

        IntegerLiteral intLit = (IntegerLiteral)result.getDimExpressions().get(0);

        assertNotNull(intLit);
        assertEquals("0",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());
    }

    public void testArrayCreation10() throws ANTLRException
    {
        setupParser("new int[(long)len]");
        defineVariable("len", false);

        ArrayCreation result = (ArrayCreation)_parser.primary();

        assertNotNull(result);

        Type type = result.getCreatedType();

        assertNotNull(type);
        assertEquals(1,
                     type.getDimensions());
        assertTrue(type.isPrimitive());
        assertEquals("int",
                     type.getBaseName());

        assertTrue(!result.isInitialized());
        assertEquals(1,
                     result.getDimExpressions().getCount());

        UnaryExpression unaryExpr = (UnaryExpression)result.getDimExpressions().get(0);

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.CAST_OP,
                     unaryExpr.getOperator());
        assertEquals("long",
                     unaryExpr.getCastType().toString());
        assertEquals(result,
                     unaryExpr.getContainer());

        VariableAccess varAccess = (VariableAccess)unaryExpr.getInnerExpression();

        assertNotNull(varAccess);
        assertEquals("len",
                     varAccess.getVariableName());
        assertEquals(unaryExpr,
                     varAccess.getContainer());
    }

    public void testArrayCreation11() throws ANTLRException
    {
        setupParser("new String[len * 2]");
        defineVariable("len", false);

        ArrayCreation result = (ArrayCreation)_parser.primary();

        assertNotNull(result);

        Type type = result.getCreatedType();

        assertNotNull(type);
        assertEquals(1,
                     type.getDimensions());
        assertTrue(!type.isPrimitive());
        assertEquals("String",
                     type.getBaseName());

        assertTrue(!result.isInitialized());
        assertEquals(1,
                     result.getDimExpressions().getCount());

        BinaryExpression binExpr = (BinaryExpression)result.getDimExpressions().get(0);

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.MULTIPLY_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        VariableAccess varAccess = (VariableAccess)binExpr.getLeftOperand();

        assertNotNull(varAccess);
        assertEquals("len",
                     varAccess.getVariableName());
        assertEquals(binExpr,
                     varAccess.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)binExpr.getRightOperand();

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());
    }

    public void testArrayCreation12() throws ANTLRException
    {
        setupParser("new int[len + 2]");

        ArrayCreation result = (ArrayCreation)_parser.primary();

        assertNotNull(result);

        Type type = result.getCreatedType();

        assertNotNull(type);
        assertEquals(1,
                     type.getDimensions());
        assertTrue(type.isPrimitive());
        assertEquals("int",
                     type.getBaseName());

        assertTrue(!result.isInitialized());
        assertEquals(1,
                     result.getDimExpressions().getCount());

        BinaryExpression binExpr = (BinaryExpression)result.getDimExpressions().get(0);

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.PLUS_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("len",
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

    public void testArrayCreation13() throws ANTLRException
    {
        setupParser("new int[len >> 2]");

        ArrayCreation result = (ArrayCreation)_parser.primary();

        assertNotNull(result);

        Type type = result.getCreatedType();

        assertNotNull(type);
        assertEquals(1,
                     type.getDimensions());
        assertTrue(type.isPrimitive());
        assertEquals("int",
                     type.getBaseName());

        assertTrue(!result.isInitialized());
        assertEquals(1,
                     result.getDimExpressions().getCount());

        BinaryExpression binExpr = (BinaryExpression)result.getDimExpressions().get(0);

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.SHIFT_RIGHT_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("len",
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

    public void testArrayCreation14() throws ANTLRException
    {
        setupParser("new int[len & 2]");

        ArrayCreation result = (ArrayCreation)_parser.primary();

        assertNotNull(result);

        Type type = result.getCreatedType();

        assertNotNull(type);
        assertEquals(1,
                     type.getDimensions());
        assertTrue(type.isPrimitive());
        assertEquals("int",
                     type.getBaseName());

        assertTrue(!result.isInitialized());
        assertEquals(1,
                     result.getDimExpressions().getCount());

        BinaryExpression binExpr = (BinaryExpression)result.getDimExpressions().get(0);

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_AND_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("len",
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

    public void testArrayCreation15() throws ANTLRException
    {
        setupParser("new int[len ^ 2]");

        ArrayCreation result = (ArrayCreation)_parser.primary();

        assertNotNull(result);

        Type type = result.getCreatedType();

        assertNotNull(type);
        assertEquals(1,
                     type.getDimensions());
        assertTrue(type.isPrimitive());
        assertEquals("int",
                     type.getBaseName());

        assertTrue(!result.isInitialized());
        assertEquals(1,
                     result.getDimExpressions().getCount());

        BinaryExpression binExpr = (BinaryExpression)result.getDimExpressions().get(0);

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_XOR_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("len",
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

    public void testArrayCreation16() throws ANTLRException
    {
        setupParser("new int[len | 2]");

        ArrayCreation result = (ArrayCreation)_parser.primary();

        assertNotNull(result);

        Type type = result.getCreatedType();

        assertNotNull(type);
        assertEquals(1,
                     type.getDimensions());
        assertTrue(type.isPrimitive());
        assertEquals("int",
                     type.getBaseName());

        assertTrue(!result.isInitialized());
        assertEquals(1,
                     result.getDimExpressions().getCount());

        BinaryExpression binExpr = (BinaryExpression)result.getDimExpressions().get(0);

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_OR_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)binExpr.getLeftOperand();

        assertNotNull(fieldAccess);
        assertEquals("len",
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

    public void testArrayCreation17() throws ANTLRException
    {
        setupParser("new int[isCorrect() ? 1 : 2]");

        ArrayCreation result = (ArrayCreation)_parser.primary();

        assertNotNull(result);

        Type type = result.getCreatedType();

        assertNotNull(type);
        assertEquals(1,
                     type.getDimensions());
        assertTrue(type.isPrimitive());
        assertEquals("int",
                     type.getBaseName());

        assertTrue(!result.isInitialized());
        assertEquals(1,
                     result.getDimExpressions().getCount());

        ConditionalExpression condExpr = (ConditionalExpression)result.getDimExpressions().get(0);

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

    public void testArrayCreation18() throws ANTLRException
    {
        setupParser("new int[count = 2]");

        ArrayCreation result = (ArrayCreation)_parser.primary();

        assertNotNull(result);

        Type type = result.getCreatedType();

        assertNotNull(type);
        assertEquals(1,
                     type.getDimensions());
        assertTrue(type.isPrimitive());
        assertEquals("int",
                     type.getBaseName());

        assertTrue(!result.isInitialized());
        assertEquals(1,
                     result.getDimExpressions().getCount());

        AssignmentExpression assignExpr = (AssignmentExpression)result.getDimExpressions().get(0);

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

    public void testArrayCreation2() throws ANTLRException
    {
        setupParser("new java.lang.Object[2][][]");

        ArrayCreation result = (ArrayCreation)_parser.primary();

        assertNotNull(result);

        Type type = result.getCreatedType();

        assertNotNull(type);
        assertEquals(3,
                     type.getDimensions());
        assertTrue(!type.isPrimitive());
        assertEquals("java.lang.Object",
                     type.getBaseName());

        assertTrue(!result.isInitialized());
        assertEquals(1,
                     result.getDimExpressions().getCount());

        IntegerLiteral intLit = (IntegerLiteral)result.getDimExpressions().get(0);

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());
    }

    public void testArrayCreation3() throws ANTLRException
    {
        setupParser("new int[getCount()][1]");

        ArrayCreation result = (ArrayCreation)_parser.primary();

        assertNotNull(result);

        Type type = result.getCreatedType();

        assertNotNull(type);
        assertEquals(2,
                     type.getDimensions());
        assertTrue(type.isPrimitive());
        assertEquals("int",
                     type.getBaseName());

        assertTrue(!result.isInitialized());
        assertEquals(2,
                     result.getDimExpressions().getCount());

        MethodInvocation methodInvoc = (MethodInvocation)result.getDimExpressions().get(0);

        assertNotNull(methodInvoc);
        assertEquals("getCount",
                     methodInvoc.getMethodName());
        assertTrue(!methodInvoc.isTrailing());
        assertNull(methodInvoc.getArgumentList());
        assertEquals(result,
                     methodInvoc.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)result.getDimExpressions().get(1);

        assertNotNull(intLit);
        assertEquals("1",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());
    }

    public void testArrayCreation4() throws ANTLRException
    {
        setupParser("new String[len][size][]");
        defineVariable("size", false);

        ArrayCreation result = (ArrayCreation)_parser.primary();

        assertNotNull(result);

        Type type = result.getCreatedType();

        assertNotNull(type);
        assertEquals(3,
                     type.getDimensions());
        assertTrue(!type.isPrimitive());
        assertEquals("String",
                     type.getBaseName());

        assertTrue(!result.isInitialized());
        assertEquals(2,
                     result.getDimExpressions().getCount());

        FieldAccess fieldAccess = (FieldAccess)result.getDimExpressions().get(0);

        assertNotNull(fieldAccess);
        assertEquals("len",
                     fieldAccess.getFieldName());
        assertTrue(!fieldAccess.isTrailing());
        assertEquals(result,
                     fieldAccess.getContainer());

        VariableAccess varAccess = (VariableAccess)result.getDimExpressions().get(1);

        assertNotNull(varAccess);
        assertEquals("size",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());
    }

    public void testArrayCreation5() throws ANTLRException
    {
        setupParser("new boolean[]{true,false}");

        ArrayCreation result = (ArrayCreation)_parser.primary();

        assertNotNull(result);

        Type type = result.getCreatedType();

        assertNotNull(type);
        assertEquals(1,
                     type.getDimensions());
        assertTrue(type.isPrimitive());
        assertEquals("boolean",
                     type.getBaseName());

        assertTrue(result.isInitialized());
        assertEquals(0,
                     result.getDimExpressions().getCount());

        ArrayInitializer initializer = result.getInitializer();

        assertNotNull(initializer);
        assertEquals(2,
                     initializer.getInitializers().getCount());
        assertEquals(result,
                     initializer.getContainer());

        SingleInitializer innerInit = (SingleInitializer)initializer.getInitializers().get(0);

        assertNotNull(innerInit);
        assertEquals(initializer,
                     innerInit.getContainer());

        BooleanLiteral boolLit = (BooleanLiteral)innerInit.getInitEpression();

        assertNotNull(boolLit);
        assertTrue(boolLit.getValue());
        assertEquals(innerInit,
                     boolLit.getContainer());

        innerInit = (SingleInitializer)initializer.getInitializers().get(1);

        assertNotNull(innerInit);
        assertEquals(initializer,
                     innerInit.getContainer());

        boolLit = (BooleanLiteral)innerInit.getInitEpression();

        assertNotNull(boolLit);
        assertTrue(!boolLit.getValue());
        assertEquals(innerInit,
                     boolLit.getContainer());
    }

    public void testArrayCreation6() throws ANTLRException
    {
        setupParser("new java.lang.String[][]{{\"text\"}, {\"other text\"}}");

        ArrayCreation result = (ArrayCreation)_parser.primary();

        assertNotNull(result);

        Type type = result.getCreatedType();

        assertNotNull(type);
        assertEquals(2,
                     type.getDimensions());
        assertTrue(!type.isPrimitive());
        assertEquals("java.lang.String",
                     type.getBaseName());

        assertTrue(result.isInitialized());
        assertEquals(0,
                     result.getDimExpressions().getCount());

        ArrayInitializer initializer = result.getInitializer();

        assertNotNull(initializer);
        assertEquals(2,
                     initializer.getInitializers().getCount());
        assertEquals(result,
                     initializer.getContainer());

        ArrayInitializer innerInit = (ArrayInitializer)initializer.getInitializers().get(0);

        assertNotNull(innerInit);
        assertEquals(1,
                     innerInit.getInitializers().getCount());
        assertEquals(initializer,
                     innerInit.getContainer());

        SingleInitializer deepInit = (SingleInitializer)innerInit.getInitializers().get(0);

        assertNotNull(deepInit);
        assertEquals(innerInit,
                     deepInit.getContainer());

        StringLiteral stringLit = (StringLiteral)deepInit.getInitEpression();

        assertNotNull(stringLit);
        assertEquals("text",
                     stringLit.getValue());
        assertEquals(deepInit,
                     stringLit.getContainer());

        innerInit = (ArrayInitializer)initializer.getInitializers().get(1);

        assertNotNull(innerInit);
        assertEquals(1,
                     innerInit.getInitializers().getCount());
        assertEquals(initializer,
                     innerInit.getContainer());

        deepInit = (SingleInitializer)innerInit.getInitializers().get(0);

        assertNotNull(deepInit);
        assertEquals(innerInit,
                     deepInit.getContainer());

        stringLit = (StringLiteral)deepInit.getInitEpression();

        assertNotNull(stringLit);
        assertEquals("other text",
                     stringLit.getValue());
        assertEquals(deepInit,
                     stringLit.getContainer());
    }

    public void testArrayCreation7()
    {
        setupParser("new Object[][0]");
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

    public void testArrayCreation8()
    {
        setupParser("new [0]");
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

    public void testArrayCreation9() throws ANTLRException
    {
        setupParser("new boolean[len++]");
        defineVariable("len", false);

        ArrayCreation result = (ArrayCreation)_parser.primary();

        assertNotNull(result);

        Type type = result.getCreatedType();

        assertNotNull(type);
        assertEquals(1,
                     type.getDimensions());
        assertTrue(type.isPrimitive());
        assertEquals("boolean",
                     type.getBaseName());

        assertTrue(!result.isInitialized());
        assertEquals(1,
                     result.getDimExpressions().getCount());

        PostfixExpression expr1 = (PostfixExpression)result.getDimExpressions().get(0);

        assertNotNull(expr1);
        assertTrue(expr1.isIncrement());
        assertEquals(result,
                     expr1.getContainer());

        VariableAccess expr2 = (VariableAccess)expr1.getInnerExpression();

        assertNotNull(expr2);
        assertEquals("len",
                     expr2.getVariableName());
        assertEquals(expr1,
                     expr2.getContainer());
    }
}
