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
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.SwitchStatement;
import jast.ast.nodes.SynchronizedStatement;
import jast.ast.nodes.ThrowStatement;
import jast.ast.nodes.TryStatement;
import jast.ast.nodes.WhileStatement;
import antlr.ANTLRException;

public class TestIfThenElseStatement extends TestBase
{

    public TestIfThenElseStatement(String name)
    {
        super(name);
    }

    public void testIfThenElseStatement1() throws ANTLRException
    {
        setupParser("if (true) ; else {}");

        IfThenElseStatement result = (IfThenElseStatement)_parser.statement();

        assertNotNull(result);

        EmptyStatement emptyStmt = (EmptyStatement)result.getTrueStatement();

        assertNotNull(emptyStmt);
        assertEquals(result,
                     emptyStmt.getContainer());

        Block block = (Block)result.getFalseStatement();

        assertNotNull(block);
        assertEquals(result,
                     block.getContainer());
    }

    public void testIfThenElseStatement10() throws ANTLRException
    {
        setupParser("if (true) do val--; while (val > 0); else ;");

        IfThenElseStatement result = (IfThenElseStatement)_parser.statement();

        assertNotNull(result);

        DoWhileStatement doStmt = (DoWhileStatement)result.getTrueStatement();

        assertNotNull(doStmt);
        assertEquals(result,
                     doStmt.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)doStmt.getLoopStatement();

        assertNotNull(exprStmt);
        assertEquals(doStmt,
                     exprStmt.getContainer());

        EmptyStatement emptyStmt = (EmptyStatement)result.getFalseStatement();

        assertNotNull(emptyStmt);
        assertEquals(result,
                     emptyStmt.getContainer());
    }

    public void testIfThenElseStatement11()
    {
        setupParser("if () ;");
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

    public void testIfThenElseStatement12() throws ANTLRException
    {
        setupParser("if (true) for (idx = 0; idx < 3; idx++) doSomething(); else ;");

        IfThenElseStatement result = (IfThenElseStatement)_parser.statement();

        assertNotNull(result);

        ForStatement forStmt = (ForStatement)result.getTrueStatement();

        assertNotNull(forStmt);
        assertEquals(result,
                     forStmt.getContainer());
        assertNotNull(forStmt.getInitList());
        assertNotNull(forStmt.getUpdateList());

        ExpressionStatement exprStmt = (ExpressionStatement)forStmt.getLoopStatement();

        assertNotNull(exprStmt);
        assertEquals(forStmt,
                     exprStmt.getContainer());

        EmptyStatement emptyStmt = (EmptyStatement)result.getFalseStatement();

        assertNotNull(emptyStmt);
        assertEquals(result,
                     emptyStmt.getContainer());
    }

    public void testIfThenElseStatement13() throws ANTLRException
    {
        setupParser("if (isCorrect) try {doSomething();} catch (Exception ex) { return; } else ;");

        IfThenElseStatement result = (IfThenElseStatement)_parser.statement();

        assertNotNull(result);

        TryStatement tryStmt = (TryStatement)result.getTrueStatement();

        assertNotNull(tryStmt);
        assertEquals(1,
                     tryStmt.getTryBlock().getBlockStatements().getCount());
        assertNull(tryStmt.getFinallyClause());
        assertEquals(1,
                     tryStmt.getCatchClauses().getCount());
        assertEquals(1,
                     tryStmt.getCatchClauses().get(0).getCatchBlock().getBlockStatements().getCount());
        assertEquals(result,
                     tryStmt.getContainer());

        EmptyStatement emptyStmt = (EmptyStatement)result.getFalseStatement();

        assertNotNull(emptyStmt);
        assertEquals(result,
                     emptyStmt.getContainer());
    }

    public void testIfThenElseStatement14() throws ANTLRException
    {
        setupParser("if (true) switch (var) { default: return; } else ;");

        IfThenElseStatement result = (IfThenElseStatement)_parser.statement();

        assertNotNull(result);

        SwitchStatement switchStmt = (SwitchStatement)result.getTrueStatement();

        assertNotNull(switchStmt);
        assertTrue(switchStmt.hasDefault());
        assertEquals(1,
                     switchStmt.getCaseBlocks().getCount());
        assertEquals(result,
                     switchStmt.getContainer());

        EmptyStatement emptyStmt = (EmptyStatement)result.getFalseStatement();

        assertNotNull(emptyStmt);
        assertEquals(result,
                     emptyStmt.getContainer());
    }

    public void testIfThenElseStatement15()
    {
        setupParser("if (var > 0) int value = 0;");
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

    public void testIfThenElseStatement16()
    {
        setupParser("if (var > 0) return; else int value = 0;");
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

    public void testIfThenElseStatement2() throws ANTLRException
    {
        setupParser("if (true) test:; else return;");

        IfThenElseStatement result = (IfThenElseStatement)_parser.statement();

        assertNotNull(result);

        LabeledStatement lbldStmt = (LabeledStatement)result.getTrueStatement();

        assertNotNull(lbldStmt);
        assertEquals(result,
                     lbldStmt.getContainer());

        ReturnStatement returnStmt = (ReturnStatement)result.getFalseStatement();

        assertNotNull(returnStmt);
        assertEquals(result,
                     returnStmt.getContainer());
    }

    public void testIfThenElseStatement3() throws ANTLRException
    {
        setupParser("if (true) a = 2; else break;");

        IfThenElseStatement result = (IfThenElseStatement)_parser.statement();

        assertNotNull(result);

        ExpressionStatement exprStmt = (ExpressionStatement)result.getTrueStatement();

        assertNotNull(exprStmt);
        assertEquals(result,
                     exprStmt.getContainer());

        BreakStatement breakStmt = (BreakStatement)result.getFalseStatement();

        assertNotNull(breakStmt);
        assertEquals(result,
                     breakStmt.getContainer());
    }

    public void testIfThenElseStatement4() throws ANTLRException
    {
        setupParser("if (true) continue test; else throw new NullPointerException();");
        defineLabel("test");

        IfThenElseStatement result = (IfThenElseStatement)_parser.statement();

        assertNotNull(result);

        ContinueStatement continueStmt = (ContinueStatement)result.getTrueStatement();

        assertNotNull(continueStmt);
        assertEquals(result,
                     continueStmt.getContainer());

        ThrowStatement throwStmt = (ThrowStatement)result.getFalseStatement();

        assertNotNull(throwStmt);
        assertEquals(result,
                     throwStmt.getContainer());
    }

    public void testIfThenElseStatement5() throws ANTLRException
    {
        setupParser("if (true) synchronized (var) {} else ;");

        IfThenElseStatement result = (IfThenElseStatement)_parser.statement();

        assertNotNull(result);

        SynchronizedStatement syncStmt = (SynchronizedStatement)result.getTrueStatement();

        assertNotNull(syncStmt);
        assertEquals(result,
                     syncStmt.getContainer());

        EmptyStatement emptyStmt = (EmptyStatement)result.getFalseStatement();

        assertNotNull(emptyStmt);
        assertEquals(result,
                     emptyStmt.getContainer());
    }

    public void testIfThenElseStatement6() throws ANTLRException
    {
        setupParser("if (true) if (var) return true; else continue;");

        IfThenElseStatement result = (IfThenElseStatement)_parser.statement();

        assertNotNull(result);
        assertNull(result.getFalseStatement());

        IfThenElseStatement ifStmt = (IfThenElseStatement)result.getTrueStatement();

        assertNotNull(ifStmt);
        assertEquals(result,
                     ifStmt.getContainer());

        ReturnStatement returnStmt = (ReturnStatement)ifStmt.getTrueStatement();

        assertNotNull(returnStmt);
        assertEquals(ifStmt,
                     returnStmt.getContainer());

        ContinueStatement continueStmt = (ContinueStatement)ifStmt.getFalseStatement();

        assertNotNull(continueStmt);
        assertEquals(ifStmt,
                     continueStmt.getContainer());
    }

    public void testIfThenElseStatement7() throws ANTLRException
    {
        setupParser("if (true) while (val > 0) val--; else ;");

        IfThenElseStatement result = (IfThenElseStatement)_parser.statement();

        assertNotNull(result);

        WhileStatement whileStmt = (WhileStatement)result.getTrueStatement();

        assertNotNull(whileStmt);
        assertEquals(result,
                     whileStmt.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)whileStmt.getLoopStatement();

        assertNotNull(exprStmt);
        assertEquals(whileStmt,
                     exprStmt.getContainer());

        EmptyStatement emptyStmt = (EmptyStatement)result.getFalseStatement();

        assertNotNull(emptyStmt);
        assertEquals(result,
                     emptyStmt.getContainer());
    }

    public void testIfThenElseStatement8()
    {
        setupParser("if a > 0 return;");
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

    public void testIfThenElseStatement9() throws ANTLRException
    {
        setupParser("if (a > 0) a++; return; else a = 0;");
        _parser.statement(); // if
        _parser.statement(); // return
        try
        {
            _parser.statement(); // dangling else
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }
}
