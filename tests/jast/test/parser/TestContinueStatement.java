package jast.test.parser;
import jast.ast.nodes.Block;
import jast.ast.nodes.ContinueStatement;
import jast.ast.nodes.DoWhileStatement;
import jast.ast.nodes.ForStatement;
import jast.ast.nodes.IfThenElseStatement;
import jast.ast.nodes.LabeledStatement;
import jast.ast.nodes.SwitchStatement;
import jast.ast.nodes.SynchronizedStatement;
import jast.ast.nodes.TryStatement;
import jast.ast.nodes.WhileStatement;
import antlr.ANTLRException;

public class TestContinueStatement extends TestBase
{

    public TestContinueStatement(String name)
    {
        super(name);
    }

    public void testContinueStatement1() throws ANTLRException
    {
        setupParser("continue ;");

        ContinueStatement result = (ContinueStatement)_parser.statement();

        assertNotNull(result);
        assertNull(result.getTarget());
    }

    public void testContinueStatement10() throws ANTLRException
    {
        setupParser("test:{continue test;}");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        Block block = (Block)result.getStatement();

        assertNotNull(block);
        assertEquals(1,
                     block.getBlockStatements().getCount());
        assertEquals(result,
                     block.getContainer());

        ContinueStatement continueStmt = (ContinueStatement)block.getBlockStatements().get(0);

        assertNotNull(continueStmt);
        assertEquals("test",
                     continueStmt.getTarget().getName());
        assertEquals(block,
                     continueStmt.getContainer());
    }

    public void testContinueStatement11()
    {
        setupParser("{ test:{;} continue test;");
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

    public void testContinueStatement12() throws ANTLRException
    {
        setupParser("test:synchronized (var) {continue test;}");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        SynchronizedStatement syncStmt = (SynchronizedStatement)result.getStatement();

        assertNotNull(syncStmt);
        assertEquals(result,
                     syncStmt.getContainer());

        Block block = syncStmt.getBlock();

        assertNotNull(block);
        assertEquals(1,
                     block.getBlockStatements().getCount());
        assertEquals(syncStmt,
                     block.getContainer());

        ContinueStatement continueStmt = (ContinueStatement)block.getBlockStatements().get(0);

        assertNotNull(continueStmt);
        assertEquals("test",
                     continueStmt.getTarget().getName());
        assertEquals(block,
                     continueStmt.getContainer());
    }

    public void testContinueStatement13() throws ANTLRException
    {
        setupParser("test:if (isCorrect()) continue test;");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        IfThenElseStatement ifStmt = (IfThenElseStatement)result.getStatement();

        assertNotNull(ifStmt);
        assertEquals(result,
                     ifStmt.getContainer());

        ContinueStatement continueStmt = (ContinueStatement)ifStmt.getTrueStatement();

        assertNotNull(continueStmt);
        assertEquals("test",
                     continueStmt.getTarget().getName());
        assertEquals(ifStmt,
                     continueStmt.getContainer());
    }

    public void testContinueStatement14() throws ANTLRException
    {
        setupParser("test:while (isCorrect()) continue test;");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        WhileStatement whileStmt = (WhileStatement)result.getStatement();

        assertNotNull(whileStmt);
        assertEquals(result,
                     whileStmt.getContainer());

        ContinueStatement continueStmt = (ContinueStatement)whileStmt.getLoopStatement();

        assertNotNull(continueStmt);
        assertEquals("test",
                     continueStmt.getTarget().getName());
        assertEquals(whileStmt,
                     continueStmt.getContainer());
    }

    public void testContinueStatement15() throws ANTLRException
    {
        setupParser("test:do continue test; while (isCorrect());");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        DoWhileStatement doStmt = (DoWhileStatement)result.getStatement();

        assertNotNull(doStmt);
        assertEquals(result,
                     doStmt.getContainer());

        ContinueStatement continueStmt = (ContinueStatement)doStmt.getLoopStatement();

        assertNotNull(continueStmt);
        assertEquals("test",
                     continueStmt.getTarget().getName());
        assertEquals(doStmt,
                     continueStmt.getContainer());
    }

    public void testContinueStatement16() throws ANTLRException
    {
        setupParser("test:for (idx = 0; idx < 3; idx++) continue test;");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        ForStatement forStmt = (ForStatement)result.getStatement();

        assertNotNull(forStmt);
        assertNotNull(forStmt.getInitList());
        assertNotNull(forStmt.getCondition());
        assertNotNull(forStmt.getUpdateList());
        assertEquals(result,
                     forStmt.getContainer());

        ContinueStatement continueStmt = (ContinueStatement)forStmt.getLoopStatement();

        assertNotNull(continueStmt);
        assertEquals("test",
                     continueStmt.getTarget().getName());
        assertEquals(forStmt,
                     continueStmt.getContainer());
    }

    public void testContinueStatement17() throws ANTLRException
    {
        setupParser("test:try { doSomething(); continue; } catch (Exception ex) { continue test; }");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        TryStatement tryStmt = (TryStatement)result.getStatement();

        assertNotNull(tryStmt);
        assertEquals(2,
                     tryStmt.getTryBlock().getBlockStatements().getCount());
        assertNull(tryStmt.getFinallyClause());
        assertEquals(1,
                     tryStmt.getCatchClauses().getCount());
        assertEquals(1,
                     tryStmt.getCatchClauses().get(0).getCatchBlock().getBlockStatements().getCount());
        assertEquals(result,
                     tryStmt.getContainer());
    }

    public void testContinueStatement18() throws ANTLRException
    {
        setupParser("test:switch (var) { default: continue test; } }");

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

    public void testContinueStatement2() throws ANTLRException
    {
        setupParser("continue outer;");
        defineLabel("outer");

        ContinueStatement result = (ContinueStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("outer",
                     result.getTarget().getName());
    }

    public void testContinueStatement3()
    {
        setupParser("continue outer.inner;");
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

    public void testContinueStatement4()
    {
        setupParser("continue this;");
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

    public void testContinueStatement5()
    {
        setupParser("continue void;");
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

    public void testContinueStatement6()
    {
        setupParser("continue int;");
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

    public void testContinueStatement7() throws ANTLRException
    {
        setupParser("continue outer;");
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

    public void testContinueStatement8() throws ANTLRException
    {
        setupParser("{continue outer;}");
        defineLabel("outer");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(1,
                     result.getBlockStatements().getCount());

        ContinueStatement continueStmt = (ContinueStatement)result.getBlockStatements().get(0);

        assertNotNull(continueStmt);
        assertEquals("outer",
                     continueStmt.getTarget().getName());
        assertEquals(result,
                     continueStmt.getContainer());
    }

    public void testContinueStatement9() throws ANTLRException
    {
        setupParser("test1:; continue test1;");
        _parser.statement();
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
