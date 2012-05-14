package jast.test.parser;
import jast.ast.nodes.Block;
import jast.ast.nodes.BreakStatement;
import jast.ast.nodes.DoWhileStatement;
import jast.ast.nodes.ForStatement;
import jast.ast.nodes.IfThenElseStatement;
import jast.ast.nodes.LabeledStatement;
import jast.ast.nodes.SwitchStatement;
import jast.ast.nodes.SynchronizedStatement;
import jast.ast.nodes.TryStatement;
import jast.ast.nodes.WhileStatement;
import antlr.ANTLRException;

public class TestBreakStatement extends TestBase
{

    public TestBreakStatement(String name)
    {
        super(name);
    }

    public void testBreakStatement1() throws ANTLRException
    {
        setupParser("break ;");

        BreakStatement result = (BreakStatement)_parser.statement();

        assertNotNull(result);
        assertNull(result.getTarget());
    }

    public void testBreakStatement10() throws ANTLRException
    {
        setupParser("test:{break test;}");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        Block block = (Block)result.getStatement();

        assertNotNull(result);
        assertEquals(1,
                     block.getBlockStatements().getCount());
        assertEquals(result,
                     block.getContainer());

        BreakStatement breakStmt = (BreakStatement)block.getBlockStatements().get(0);

        assertNotNull(breakStmt);
        assertEquals("test",
                     breakStmt.getTarget().getName());
        assertEquals(block,
                     breakStmt.getContainer());
    }

    public void testBreakStatement11()
    {
        setupParser("{ test:{;} break test;");
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

    public void testBreakStatement12() throws ANTLRException
    {
        setupParser("test:synchronized (var) {break test;}");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        SynchronizedStatement syncStmt = (SynchronizedStatement)result.getStatement();

        assertNotNull(syncStmt);
        assertEquals(result,
                     syncStmt.getContainer());

        Block block = syncStmt.getBlock();

        assertEquals(1,
                     block.getBlockStatements().getCount());
        assertEquals(syncStmt,
                     block.getContainer());

        BreakStatement breakStmt = (BreakStatement)block.getBlockStatements().get(0);

        assertNotNull(breakStmt);
        assertEquals("test",
                     breakStmt.getTarget().getName());
        assertEquals(block,
                     breakStmt.getContainer());
    }

    public void testBreakStatement13() throws ANTLRException
    {
        setupParser("test:if (isCorrect()) break test;");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        IfThenElseStatement ifStmt = (IfThenElseStatement)result.getStatement();

        assertNotNull(ifStmt);
        assertEquals(result,
                     ifStmt.getContainer());

        BreakStatement breakStmt = (BreakStatement)ifStmt.getTrueStatement();

        assertNotNull(breakStmt);
        assertEquals("test",
                     breakStmt.getTarget().getName());
        assertEquals(ifStmt,
                     breakStmt.getContainer());
    }

    public void testBreakStatement14() throws ANTLRException
    {
        setupParser("test:while (isCorrect()) break test;");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        WhileStatement whileStmt = (WhileStatement)result.getStatement();

        assertNotNull(whileStmt);
        assertEquals(result,
                     whileStmt.getContainer());

        BreakStatement breakStmt = (BreakStatement)whileStmt.getLoopStatement();

        assertNotNull(breakStmt);
        assertEquals("test",
                     breakStmt.getTarget().getName());
        assertEquals(whileStmt,
                     breakStmt.getContainer());
    }

    public void testBreakStatement15() throws ANTLRException
    {
        setupParser("test:do break test; while (isCorrect());");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        DoWhileStatement doStmt = (DoWhileStatement)result.getStatement();

        assertNotNull(doStmt);
        assertEquals(result,
                     doStmt.getContainer());

        BreakStatement breakStmt = (BreakStatement)doStmt.getLoopStatement();

        assertNotNull(breakStmt);
        assertEquals("test",
                     breakStmt.getTarget().getName());
        assertEquals(doStmt,
                     breakStmt.getContainer());
    }

    public void testBreakStatement16() throws ANTLRException
    {
        setupParser("test:for (idx = 0; idx < 3; idx++) break test;");

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

        BreakStatement breakStmt = (BreakStatement)forStmt.getLoopStatement();

        assertNotNull(breakStmt);
        assertEquals("test",
                     breakStmt.getTarget().getName());
        assertEquals(forStmt,
                     breakStmt.getContainer());
    }

    public void testBreakStatement17() throws ANTLRException
    {
        setupParser("test:try { doSomething(); break; } catch (Exception ex) { break test; }");

        LabeledStatement result = (LabeledStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("test",
                     result.getName());

        TryStatement tryStmt = (TryStatement)result.getStatement();

        assertNotNull(tryStmt);
        assertNull(tryStmt.getFinallyClause());
        assertEquals(1,
                     tryStmt.getCatchClauses().getCount());
        assertEquals(result,
                     tryStmt.getContainer());

        Block block = tryStmt.getTryBlock();

        assertEquals(2,
                     block.getBlockStatements().getCount());
        assertEquals(tryStmt,
                     block.getContainer());

        block = tryStmt.getCatchClauses().get(0).getCatchBlock();

        assertEquals(1,
                     block.getBlockStatements().getCount());
        assertEquals(tryStmt.getCatchClauses().get(0),
                     block.getContainer());
    }

    public void testBreakStatement18() throws ANTLRException
    {
        setupParser("test:switch (var) { default: break test; } }");

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

    public void testBreakStatement2() throws ANTLRException
    {
        setupParser("break outer;");
        defineLabel("outer");

        BreakStatement result = (BreakStatement)_parser.statement();

        assertNotNull(result);
        assertEquals("outer",
                     result.getTarget().getName());
    }

    public void testBreakStatement3()
    {
        setupParser("break outer.inner;");
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

    public void testBreakStatement4()
    {
        setupParser("break this;");
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

    public void testBreakStatement5()
    {
        setupParser("break void;");
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

    public void testBreakStatement6()
    {
        setupParser("break int;");
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

    public void testBreakStatement7() throws ANTLRException
    {
        setupParser("break outer;");
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

    public void testBreakStatement8() throws ANTLRException
    {
        setupParser("{break outer;}");
        defineLabel("outer");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(1,
                     result.getBlockStatements().getCount());

        BreakStatement breakStmt = (BreakStatement)result.getBlockStatements().get(0);

        assertNotNull(breakStmt);
        assertEquals("outer",
                     breakStmt.getTarget().getName());
        assertEquals(result,
                     breakStmt.getContainer());
    }

    public void testBreakStatement9() throws ANTLRException
    {
        setupParser("test1:; break test1;");
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
