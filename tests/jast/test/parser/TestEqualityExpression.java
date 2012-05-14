package jast.test.parser;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.BooleanLiteral;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FloatingPointLiteral;
import jast.ast.nodes.InstanceofExpression;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.ParenthesizedExpression;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.UnaryExpression;
import antlr.ANTLRException;

public class TestEqualityExpression extends TestBase
{

    public TestEqualityExpression(String name)
    {
        super(name);
    }

    public void testEqualityExpression1() throws ANTLRException
    {
        setupParser("1==2");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.EQUAL_OP,
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

    public void testEqualityExpression2() throws ANTLRException
    {
        setupParser("1.0f!=var");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.NOT_EQUAL_OP,
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

    public void testEqualityExpression3() throws ANTLRException
    {
        setupParser("1.0 == 2.0 == var");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.EQUAL_OP,
                     result.getOperator());

        FieldAccess fieldAccess = (FieldAccess)result.getRightOperand();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.EQUAL_OP,
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

    public void testEqualityExpression4() throws ANTLRException
    {
        setupParser("3 != 1 != false");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.NOT_EQUAL_OP,
                     result.getOperator());

        BooleanLiteral boolLit = (BooleanLiteral)result.getRightOperand();

        assertNotNull(boolLit);
        assertTrue(!boolLit.getValue());
        assertEquals(result,
                     boolLit.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.NOT_EQUAL_OP,
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
        assertEquals("1",
                     intLit.asString());
        assertEquals(binExpr,
                     intLit.getContainer());
    }

    public void testEqualityExpression5() throws ANTLRException
    {
        setupParser("1.0 != 2.0 == true");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.EQUAL_OP,
                     result.getOperator());

        BooleanLiteral boolLit = (BooleanLiteral)result.getRightOperand();

        assertNotNull(boolLit);
        assertTrue(boolLit.getValue());
        assertEquals(result,
                     boolLit.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.NOT_EQUAL_OP,
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

    public void testEqualityExpression6() throws ANTLRException
    {
        setupParser("1.0 == 2.0 != var");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.NOT_EQUAL_OP,
                     result.getOperator());

        FieldAccess fieldAccess = (FieldAccess)result.getRightOperand();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.EQUAL_OP,
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

    public void testEqualityExpression7() throws ANTLRException
    {
        setupParser("-1 != (idx++) == !getValue()");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.EQUAL_OP,
                     result.getOperator());

        UnaryExpression unaryExpr = (UnaryExpression)result.getRightOperand();

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

        BinaryExpression binExpr = (BinaryExpression)result.getLeftOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.NOT_EQUAL_OP,
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

    public void testEqualityExpression8() throws ANTLRException
    {
        setupParser("(float)1==(double)getValue()");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.EQUAL_OP,
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

    public void testEqualityExpression9() throws ANTLRException
    {
        setupParser("obj instanceof Object != 2.0 < var");

        BinaryExpression result = (BinaryExpression)_parser.expression();

        assertNotNull(result);
        assertEquals(BinaryExpression.NOT_EQUAL_OP,
                     result.getOperator());

        InstanceofExpression instanceofExpr = (InstanceofExpression)result.getLeftOperand();

        assertNotNull(instanceofExpr);
        assertTrue(!instanceofExpr.getReferencedType().isPrimitive());
        assertEquals(0,
                     instanceofExpr.getReferencedType().getDimensions());
        assertEquals("Object",
                     instanceofExpr.getReferencedType().getBaseName());
        assertEquals(result,
                     instanceofExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)instanceofExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("obj",
                     fieldAccess.getFieldName());
        assertEquals(instanceofExpr,
                     fieldAccess.getContainer());

        BinaryExpression binExpr = (BinaryExpression)result.getRightOperand();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.LOWER_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        FloatingPointLiteral floatLit = (FloatingPointLiteral)binExpr.getLeftOperand();

        assertNotNull(floatLit);
        assertEquals("2.0",
                     floatLit.asString());
        assertEquals(binExpr,
                     floatLit.getContainer());

        fieldAccess = (FieldAccess)binExpr.getRightOperand();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(binExpr,
                     fieldAccess.getContainer());
    }
}
