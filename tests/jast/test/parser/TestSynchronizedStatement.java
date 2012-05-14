package jast.test.parser;
import jast.ast.nodes.ArrayAccess;
import jast.ast.nodes.ArrayCreation;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.Block;
import jast.ast.nodes.ClassAccess;
import jast.ast.nodes.ConditionalExpression;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.ParenthesizedExpression;
import jast.ast.nodes.SelfAccess;
import jast.ast.nodes.SynchronizedStatement;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.VariableAccess;
import antlr.ANTLRException;

public class TestSynchronizedStatement extends TestBase
{

    public TestSynchronizedStatement(String name)
    {
        super(name);
    }

    public void testSynchronizedStatement1() throws ANTLRException
    {
        setupParser("synchronized ((var)) {}");

        SynchronizedStatement result = (SynchronizedStatement)_parser.statement();

        assertNotNull(result);

        ParenthesizedExpression parenExpr = (ParenthesizedExpression)result.getLockExpression();

        assertNotNull(parenExpr);
        assertEquals(result,
                     parenExpr.getContainer());
    }

    public void testSynchronizedStatement10() throws ANTLRException
    {
        setupParser("synchronized (var = getValue()) {}");

        SynchronizedStatement result = (SynchronizedStatement)_parser.statement();

        assertNotNull(result);

        AssignmentExpression assignExpr = (AssignmentExpression)result.getLockExpression();

        assertNotNull(assignExpr);
        assertEquals(AssignmentExpression.ASSIGN_OP,
                     assignExpr.getOperator());
        assertEquals(result,
                     assignExpr.getContainer());
    }

    public void testSynchronizedStatement11() throws ANTLRException
    {
        setupParser("synchronized (new Object[2]) {}");

        SynchronizedStatement result = (SynchronizedStatement)_parser.statement();

        assertNotNull(result);

        ArrayCreation arrayCreation = (ArrayCreation)result.getLockExpression();

        assertNotNull(arrayCreation);
        assertEquals(result,
                     arrayCreation.getContainer());
    }

    public void testSynchronizedStatement12() throws ANTLRException
    {
        setupParser("synchronized (Exception.class) {}");

        SynchronizedStatement result = (SynchronizedStatement)_parser.statement();

        assertNotNull(result);

        ClassAccess classAccess = (ClassAccess)result.getLockExpression();

        assertNotNull(classAccess);
        assertEquals(result,
                     classAccess.getContainer());
    }

    public void testSynchronizedStatement13()
    {
        setupParser("synchronized");
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

    public void testSynchronizedStatement14()
    {
        setupParser("synchronized (var)");
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

    public void testSynchronizedStatement15()
    {
        setupParser("synchronized {}");
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

    public void testSynchronizedStatement16() throws ANTLRException
    {
        setupParser("synchronized(var){a += b;}");

        SynchronizedStatement result = (SynchronizedStatement)_parser.statement();

        assertNotNull(result);

        FieldAccess fieldAccess = (FieldAccess)result.getLockExpression();

        assertNotNull(fieldAccess);
        assertEquals(result,
                     fieldAccess.getContainer());

        Block block = result.getBlock();

        assertNotNull(block);
        assertEquals(1,
                     block.getBlockStatements().getCount());
        assertEquals(result,
                     block.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)block.getBlockStatements().get(0);

        assertNotNull(exprStmt);
        assertEquals(block,
                     exprStmt.getContainer());
    }

    public void testSynchronizedStatement2() throws ANTLRException
    {
        setupParser("synchronized (new Exception()) {}");

        SynchronizedStatement result = (SynchronizedStatement)_parser.statement();

        assertNotNull(result);

        Instantiation instantiation = (Instantiation)result.getLockExpression();

        assertNotNull(instantiation);
        assertEquals("Exception",
                     instantiation.getInstantiatedType().getBaseName());
        assertEquals(result,
                     instantiation.getContainer());
    }

    public void testSynchronizedStatement3() throws ANTLRException
    {
        setupParser("synchronized (this) {}");

        SynchronizedStatement result = (SynchronizedStatement)_parser.statement();

        assertNotNull(result);

        SelfAccess selfAccess = (SelfAccess)result.getLockExpression();

        assertNotNull(selfAccess);
        assertEquals(result,
                     selfAccess.getContainer());
    }

    public void testSynchronizedStatement4() throws ANTLRException
    {
        setupParser("synchronized (var) {}");

        SynchronizedStatement result = (SynchronizedStatement)_parser.statement();

        assertNotNull(result);

        FieldAccess fieldAccess = (FieldAccess)result.getLockExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(result,
                     fieldAccess.getContainer());
    }

    public void testSynchronizedStatement5() throws ANTLRException
    {
        setupParser("synchronized (var) {}");
        defineVariable("var", false);

        SynchronizedStatement result = (SynchronizedStatement)_parser.statement();

        assertNotNull(result);

        VariableAccess varAccess = (VariableAccess)result.getLockExpression();

        assertNotNull(varAccess);
        assertEquals("var",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());
    }

    public void testSynchronizedStatement6() throws ANTLRException
    {
        setupParser("synchronized (getObject()) {}");

        SynchronizedStatement result = (SynchronizedStatement)_parser.statement();

        assertNotNull(result);

        MethodInvocation methodInvoc = (MethodInvocation)result.getLockExpression();

        assertNotNull(methodInvoc);
        assertEquals("getObject",
                     methodInvoc.getMethodName());
        assertEquals(result,
                     methodInvoc.getContainer());
    }

    public void testSynchronizedStatement7() throws ANTLRException
    {
        setupParser("synchronized (ex[0]) {}");

        SynchronizedStatement result = (SynchronizedStatement)_parser.statement();

        assertNotNull(result);

        ArrayAccess arrayAccess = (ArrayAccess)result.getLockExpression();

        assertNotNull(arrayAccess);
        assertEquals(result,
                     arrayAccess.getContainer());
    }

    public void testSynchronizedStatement8() throws ANTLRException
    {
        setupParser("synchronized ((String)var) {}");

        SynchronizedStatement result = (SynchronizedStatement)_parser.statement();

        assertNotNull(result);

        UnaryExpression unaryExpr = (UnaryExpression)result.getLockExpression();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.CAST_OP,
                     unaryExpr.getOperator());
        assertTrue(!unaryExpr.getCastType().isPrimitive());
        assertEquals(result,
                     unaryExpr.getContainer());
    }

    public void testSynchronizedStatement9() throws ANTLRException
    {
        setupParser("synchronized (idx > 0 ? var1 : var2) {}");

        SynchronizedStatement result = (SynchronizedStatement)_parser.statement();

        assertNotNull(result);

        ConditionalExpression condExpr = (ConditionalExpression)result.getLockExpression();

        assertNotNull(condExpr);
        assertEquals(result,
                     condExpr.getContainer());
    }
}
