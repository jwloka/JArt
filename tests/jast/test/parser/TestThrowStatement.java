package jast.test.parser;
import jast.ast.nodes.ArrayAccess;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.ConditionalExpression;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.ParenthesizedExpression;
import jast.ast.nodes.SelfAccess;
import jast.ast.nodes.ThrowStatement;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.VariableAccess;
import antlr.ANTLRException;

public class TestThrowStatement extends TestBase
{

    public TestThrowStatement(String name)
    {
        super(name);
    }

    public void testThrowStatement1() throws ANTLRException
    {
        setupParser("throw (var);");

        ThrowStatement result = (ThrowStatement)_parser.statement();

        assertNotNull(result);

        ParenthesizedExpression parenExpr = (ParenthesizedExpression)result.getThrowExpression();

        assertNotNull(parenExpr);
        assertEquals(result,
                     parenExpr.getContainer());
    }

    public void testThrowStatement10() throws ANTLRException
    {
        setupParser("throw var = getValue();");

        ThrowStatement result = (ThrowStatement)_parser.statement();

        assertNotNull(result);

        AssignmentExpression assignExpr = (AssignmentExpression)result.getThrowExpression();

        assertNotNull(assignExpr);
        assertEquals(AssignmentExpression.ASSIGN_OP,
                     assignExpr.getOperator());
        assertEquals(result,
                     assignExpr.getContainer());
    }

    public void testThrowStatement11()
    {
        setupParser("throw 'a'");
        try
        {
            _parser.statement();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testThrowStatement12()
    {
        setupParser("throw;");
        try
        {
            _parser.statement();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testThrowStatement13()
    {
        setupParser("throw");
        try
        {
            _parser.statement();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testThrowStatement2() throws ANTLRException
    {
        setupParser("throw new Exception();");

        ThrowStatement result = (ThrowStatement)_parser.statement();

        assertNotNull(result);

        Instantiation instantiation = (Instantiation)result.getThrowExpression();

        assertNotNull(instantiation);
        assertEquals("Exception",
                     instantiation.getInstantiatedType().getBaseName());
        assertEquals(result,
                     instantiation.getContainer());
    }

    public void testThrowStatement3() throws ANTLRException
    {
        setupParser("throw this;");

        ThrowStatement result = (ThrowStatement)_parser.statement();

        assertNotNull(result);

        SelfAccess selfAccess = (SelfAccess)result.getThrowExpression();

        assertNotNull(selfAccess);
        assertEquals(result,
                     selfAccess.getContainer());
    }

    public void testThrowStatement4() throws ANTLRException
    {
        setupParser("throw var;");

        ThrowStatement result = (ThrowStatement)_parser.statement();

        assertNotNull(result);

        FieldAccess fieldAccess = (FieldAccess)result.getThrowExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());
    }

    public void testThrowStatement5() throws ANTLRException
    {
        setupParser("throw ex;");
        defineVariable("ex", false);

        ThrowStatement result = (ThrowStatement)_parser.statement();

        assertNotNull(result);

        VariableAccess varAccess = (VariableAccess)result.getThrowExpression();

        assertNotNull(varAccess);
        assertEquals("ex",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());
    }

    public void testThrowStatement6() throws ANTLRException
    {
        setupParser("throw getException();");

        ThrowStatement result = (ThrowStatement)_parser.statement();

        assertNotNull(result);

        MethodInvocation methodInvoc = (MethodInvocation)result.getThrowExpression();

        assertNotNull(methodInvoc);
        assertEquals("getException",
                     methodInvoc.getMethodName());
        assertEquals(result,
                     methodInvoc.getContainer());
    }

    public void testThrowStatement7() throws ANTLRException
    {
        setupParser("throw ex[0];");

        ThrowStatement result = (ThrowStatement)_parser.statement();

        assertNotNull(result);

        ArrayAccess arrayAccess = (ArrayAccess)result.getThrowExpression();

        assertNotNull(arrayAccess);
        assertEquals(result,
                     arrayAccess.getContainer());
    }

    public void testThrowStatement8() throws ANTLRException
    {
        setupParser("throw (Exception)var;");

        ThrowStatement result = (ThrowStatement)_parser.statement();

        assertNotNull(result);

        UnaryExpression unaryExpr = (UnaryExpression)result.getThrowExpression();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.CAST_OP,
                     unaryExpr.getOperator());
        assertTrue(!unaryExpr.getCastType().isPrimitive());
        assertEquals(result,
                     unaryExpr.getContainer());
    }

    public void testThrowStatement9() throws ANTLRException
    {
        setupParser("throw idx > 0 ? var1 : var2;");

        ThrowStatement result = (ThrowStatement)_parser.statement();

        assertNotNull(result);

        ConditionalExpression condExpr = (ConditionalExpression)result.getThrowExpression();

        assertNotNull(condExpr);
        assertEquals(result,
                     condExpr.getContainer());
    }
}
