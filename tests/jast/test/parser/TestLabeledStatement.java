package jast.test.parser;
import jast.ast.nodes.Block;
import jast.ast.nodes.BreakStatement;
import jast.ast.nodes.ContinueStatement;
import jast.ast.nodes.DoWhileStatement;
import jast.ast.nodes.EmptyStatement;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.ForStatement;
import jast.ast.nodes.IfThenElseStatement;
import jast.ast.nodes.LabeledStatement;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.SwitchStatement;
import jast.ast.nodes.SynchronizedStatement;
import jast.ast.nodes.ThrowStatement;
import jast.ast.nodes.WhileStatement;
import antlr.ANTLRException;

public class TestLabeledStatement extends TestBase
{

    public TestLabeledStatement(String name)
    {
        super(name);
    }

    public void testLabeledStatement1() throws ANTLRException
    {
        setupParser("test:;");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        EmptyStatement emptyStmt = (EmptyStatement)result.getStatement();

        assertNotNull(emptyStmt);
        assertEquals(result,
                     emptyStmt.getContainer());
    }

    public void testLabeledStatement10() throws ANTLRException
    {
        setupParser("test:{}");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        Block block = (Block)result.getStatement();

        assertNotNull(block);
        assertEquals(0,
                     block.getBlockStatements().getCount());
        assertEquals(result,
                     block.getContainer());
    }

    public void testLabeledStatement11() throws ANTLRException
    {
        setupParser("test:return true;");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        ReturnStatement returnStmt = (ReturnStatement)result.getStatement();

        assertNotNull(returnStmt);
        assertEquals(result,
                     returnStmt.getContainer());
    }

    public void testLabeledStatement12() throws ANTLRException
    {
        setupParser("test:break;");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        BreakStatement breakStmt = (BreakStatement)result.getStatement();

        assertNotNull(breakStmt);
        assertEquals(result,
                     breakStmt.getContainer());
    }

    public void testLabeledStatement13() throws ANTLRException
    {
        setupParser("test:continue;");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        ContinueStatement continueStmt = (ContinueStatement)result.getStatement();

        assertNotNull(continueStmt);
        assertEquals(result,
                     continueStmt.getContainer());
    }

    public void testLabeledStatement14() throws ANTLRException
    {
        setupParser("test:throw getException();");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        ThrowStatement throwStmt = (ThrowStatement)result.getStatement();

        assertNotNull(throwStmt);
        assertEquals(result,
                     throwStmt.getContainer());
    }

    public void testLabeledStatement15() throws ANTLRException
    {
        setupParser("test:synchronized (getLock()) { a += 2; }");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        SynchronizedStatement syncStmt = (SynchronizedStatement)result.getStatement();

        assertNotNull(syncStmt);
        assertEquals(result,
                     syncStmt.getContainer());
    }

    public void testLabeledStatement16() throws ANTLRException
    {
        setupParser("test:if (isCorrect()) {} else { return; }");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        IfThenElseStatement ifStmt = (IfThenElseStatement)result.getStatement();

        assertNotNull(ifStmt);
        assertEquals(result,
                     ifStmt.getContainer());
    }

    public void testLabeledStatement17() throws ANTLRException
    {
        setupParser("test:while (isCorrect()) { val++; }");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        WhileStatement whileStmt = (WhileStatement)result.getStatement();

        assertNotNull(whileStmt);
        assertEquals(result,
                     whileStmt.getContainer());
    }

    public void testLabeledStatement18() throws ANTLRException
    {
        setupParser("test:do { val++; } while (isCorrect());");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        DoWhileStatement doStmt = (DoWhileStatement)result.getStatement();

        assertNotNull(doStmt);
        assertEquals(result,
                     doStmt.getContainer());
    }

    public void testLabeledStatement19() throws ANTLRException
    {
        setupParser("test:for (idx = 0; idx < 3; idx++) val+=1;");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        ForStatement forStmt = (ForStatement)result.getStatement();

        assertNotNull(forStmt);
        assertNotNull(forStmt.getInitList());
        assertNotNull(forStmt.getCondition());
        assertNotNull(forStmt.getUpdateList());
        assertNotNull(forStmt.getLoopStatement());
        assertEquals(result,
                     forStmt.getContainer());
    }

    public void testLabeledStatement2() throws ANTLRException
    {
        setupParser("test1:test2:;");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test1",
                     result.getName());

        LabeledStatement lbldStmt = (LabeledStatement)result.getStatement();

        assertNotNull(lbldStmt);
        assertEquals("test2",
                     lbldStmt.getName());
        assertEquals(result,
                     lbldStmt.getContainer());

        EmptyStatement emptyStmt = (EmptyStatement)lbldStmt.getStatement();

        assertNotNull(emptyStmt);
        assertEquals(lbldStmt,
                     emptyStmt.getContainer());
    }

    public void testLabeledStatement20() throws ANTLRException
    {
        setupParser("test:switch (var) { default: return; }");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        SwitchStatement switchStmt = (SwitchStatement)result.getStatement();

        assertNotNull(switchStmt);
        assertTrue(switchStmt.hasDefault());
        assertEquals(1,
                     switchStmt.getCaseBlocks().getCount());
        assertEquals(result,
                     switchStmt.getContainer());
    }

    public void testLabeledStatement21()
    {
        setupParser("default:;");
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

    public void testLabeledStatement22()
    {
        setupParser("case:;");
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

    public void testLabeledStatement23()
    {
        setupParser("test:int var = 0;");
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

    public void testLabeledStatement3() throws ANTLRException
    {
        setupParser("test1:doSomething();");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test1",
                     result.getName());

        ExpressionStatement exprStmt = (ExpressionStatement)result.getStatement();

        assertNotNull(exprStmt);
        assertEquals(result,
                     exprStmt.getContainer());

        MethodInvocation methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertNotNull(methodInvoc);
        assertEquals("doSomething",
                     methodInvoc.getMethodName());
        assertEquals(exprStmt,
                     methodInvoc.getContainer());
    }

    public void testLabeledStatement4()
    {
        setupParser("test:");
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

    public void testLabeledStatement5()
    {
        setupParser("a.b:");
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

    public void testLabeledStatement7()
    {
        setupParser("this:;");
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

    public void testLabeledStatement8()
    {
        setupParser("void:;");
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

    public void testLabeledStatement9()
    {
        setupParser("boolean:;");
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
}
