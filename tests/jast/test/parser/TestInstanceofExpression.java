package jast.test.parser;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.InstanceofExpression;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.NullLiteral;
import jast.ast.nodes.ParenthesizedExpression;
import jast.ast.nodes.PrimitiveType;
import jast.ast.nodes.Type;
import jast.ast.nodes.UnaryExpression;
import antlr.ANTLRException;

public class TestInstanceofExpression extends TestBase
{

    public TestInstanceofExpression(String name)
    {
        super(name);
    }

    public void testInstanceofExpression1() throws ANTLRException
    {
        setupParser("var instanceof Object");

        InstanceofExpression result = (InstanceofExpression)_parser.expression();

        assertNotNull(result);
        assertTrue(!result.getReferencedType().isPrimitive());
        assertEquals(0,
                     result.getReferencedType().getDimensions());
        assertEquals("Object",
                     result.getReferencedType().getBaseName());

        FieldAccess fieldAccess = (FieldAccess)result.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());
    }

    public void testInstanceofExpression2() throws ANTLRException
    {
        setupParser("null instanceof Object[]");

        InstanceofExpression result = (InstanceofExpression)_parser.expression();

        assertNotNull(result);
        assertTrue(!result.getReferencedType().isPrimitive());
        assertEquals(1,
                     result.getReferencedType().getDimensions());
        assertEquals("Object",
                     result.getReferencedType().getBaseName());

        NullLiteral nullLit = (NullLiteral)result.getInnerExpression();

        assertNotNull(nullLit);
        assertEquals(result,
                     nullLit.getContainer());
    }

    public void testInstanceofExpression3() throws ANTLRException
    {
        setupParser("getValue() instanceof java.lang.String");

        InstanceofExpression result = (InstanceofExpression)_parser.expression();

        assertNotNull(result);
        assertTrue(!result.getReferencedType().isPrimitive());
        assertEquals(0,
                     result.getReferencedType().getDimensions());
        assertEquals("java.lang.String",
                     result.getReferencedType().getBaseName());

        MethodInvocation methodInvoc = (MethodInvocation)result.getInnerExpression();

        assertNotNull(methodInvoc);
        assertEquals("getValue",
                     methodInvoc.getMethodName());
        assertEquals(result,
                     methodInvoc.getContainer());
    }

    public void testInstanceofExpression4() throws ANTLRException
    {
        setupParser("(Object)var instanceof String");

        InstanceofExpression result = (InstanceofExpression)_parser.expression();

        assertNotNull(result);

        Type type = result.getReferencedType();

        assertTrue(!type.isPrimitive());
        assertEquals(0,
                     type.getDimensions());
        assertEquals("String",
                     type.getBaseName());

        UnaryExpression unaryExpr = (UnaryExpression)result.getInnerExpression();

        assertNotNull(unaryExpr);
        assertEquals(result,
                     unaryExpr.getContainer());

        type = unaryExpr.getCastType();

        assertTrue(!type.isPrimitive());
        assertEquals(0,
                     type.getDimensions());
        assertEquals("Object",
                     type.getBaseName());

        FieldAccess fieldAccess = (FieldAccess)unaryExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(unaryExpr,
                     fieldAccess.getContainer());
    }

    public void testInstanceofExpression5() throws ANTLRException
    {
        setupParser("(var) instanceof Object");

        InstanceofExpression result = (InstanceofExpression)_parser.expression();

        assertNotNull(result);
        assertTrue(!result.getReferencedType().isPrimitive());
        assertEquals(0,
                     result.getReferencedType().getDimensions());
        assertEquals("Object",
                     result.getReferencedType().getBaseName());

        ParenthesizedExpression parenExpr = (ParenthesizedExpression)result.getInnerExpression();

        assertNotNull(parenExpr);
        assertEquals(result,
                     parenExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)parenExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(parenExpr,
                     fieldAccess.getContainer());
    }

    public void testInstanceofExpression6() throws ANTLRException
    {
        setupParser("obj instanceof int[][]");

        InstanceofExpression result = (InstanceofExpression)_parser.expression();

        assertNotNull(result);
        assertTrue(result.getReferencedType().isPrimitive());
        assertEquals(2,
                     result.getReferencedType().getDimensions());
        assertEquals(PrimitiveType.INT_TYPE,
                     result.getReferencedType().getPrimitiveBaseType());

        FieldAccess fieldAccess = (FieldAccess)result.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("obj",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());
    }

    public void testInstanceofExpression7()
    {
        setupParser("var instanceof void");
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

    public void testInstanceofExpression8()
    {
        setupParser("isValid = var instanceof getValue()");
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
