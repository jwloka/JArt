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

public class TestDoWhileStatement extends TestBase
{

    public TestDoWhileStatement(String name)
    {
        super(name);
    }

    public void testDoWhileStatement1() throws ANTLRException
    {
        setupParser("do ; while (true);");

        DoWhileStatement result = (DoWhileStatement)_parser.statement();

        assertNotNull(result);

        EmptyStatement emptyStmt = (EmptyStatement)result.getLoopStatement();

        assertNotNull(emptyStmt);
        assertEquals(result,
                     emptyStmt.getContainer());
    }

    public void testDoWhileStatement10() throws ANTLRException
    {
        setupParser("do if (val > 0) return; else val -= 1; while (true);");

        DoWhileStatement result = (DoWhileStatement)_parser.statement();

        assertNotNull(result);

        IfThenElseStatement ifStmt = (IfThenElseStatement)result.getLoopStatement();

        assertNotNull(ifStmt);
        assertEquals(result,
                     ifStmt.getContainer());
    }

    public void testDoWhileStatement11() throws ANTLRException
    {
        setupParser("do while (val > 1) val -= 1; while (true);");

        DoWhileStatement result = (DoWhileStatement)_parser.statement();

        assertNotNull(result);

        WhileStatement whileStmt = (WhileStatement)result.getLoopStatement();

        assertNotNull(whileStmt);
        assertEquals(result,
                     whileStmt.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)whileStmt.getLoopStatement();

        assertNotNull(exprStmt);
        assertEquals(whileStmt,
                     exprStmt.getContainer());
    }

    public void testDoWhileStatement12() throws ANTLRException
    {
        setupParser("do do val -= 1; while (val > 1); while (true);");

        DoWhileStatement result = (DoWhileStatement)_parser.statement();

        assertNotNull(result);

        DoWhileStatement doStmt = (DoWhileStatement)result.getLoopStatement();

        assertNotNull(doStmt);
        assertEquals(result,
                     doStmt.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)doStmt.getLoopStatement();

        assertNotNull(exprStmt);
        assertEquals(doStmt,
                     exprStmt.getContainer());
    }

    public void testDoWhileStatement14()
    {
        setupParser("do return; while a > 0;");
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

    public void testDoWhileStatement15()
    {
        setupParser("do return; while (a > 0)");
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

    public void testDoWhileStatement16()
    {
        setupParser("do while (a > 0);");
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

    public void testDoWhileStatement17()
    {
        setupParser("do ; while ();");
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

    public void testDoWhileStatement18() throws ANTLRException
    {
        setupParser("do for(idx = 0; idx < 3; idx++) doSomething(); while (true);");

        DoWhileStatement result = (DoWhileStatement)_parser.statement();

        assertNotNull(result);

        ForStatement forStmt = (ForStatement)result.getLoopStatement();

        assertNotNull(forStmt);
        assertNotNull(forStmt.getInitList());
        assertNotNull(forStmt.getUpdateList());
        assertEquals(result,
                     forStmt.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)forStmt.getLoopStatement();

        assertNotNull(exprStmt);
        assertEquals(forStmt,
                     exprStmt.getContainer());
    }

    public void testDoWhileStatement19() throws ANTLRException
    {
        setupParser("do try {doSomething();} catch (Exception ex) { return; } while (true);");

        DoWhileStatement result = (DoWhileStatement)_parser.statement();

        assertNotNull(result);

        TryStatement tryStmt = (TryStatement)result.getLoopStatement();

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
    }

    public void testDoWhileStatement2() throws ANTLRException
    {
        setupParser("do {} while (true);");

        DoWhileStatement result = (DoWhileStatement)_parser.statement();

        assertNotNull(result);

        Block block = (Block)result.getLoopStatement();

        assertNotNull(block);
        assertEquals(result,
                     block.getContainer());
    }

    public void testDoWhileStatement20() throws ANTLRException
    {
        setupParser("do switch (var) { default: return; } while (true);");

        DoWhileStatement result = (DoWhileStatement)_parser.statement();

        assertNotNull(result);

        SwitchStatement switchStmt = (SwitchStatement)result.getLoopStatement();

        assertNotNull(switchStmt);
        assertTrue(switchStmt.hasDefault());
        assertEquals(1,
                     switchStmt.getCaseBlocks().getCount());
        assertEquals(result,
                     switchStmt.getContainer());
    }

    public void testDoWhileStatement21()
    {
        setupParser("do int value = 0; while (a > 0);");
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

    public void testDoWhileStatement3() throws ANTLRException
    {
        setupParser("do test:; while (true);");

        DoWhileStatement result = (DoWhileStatement)_parser.statement();

        assertNotNull(result);

        LabeledStatement labeledStmt = (LabeledStatement)result.getLoopStatement();

        assertNotNull(labeledStmt);
        assertEquals(result,
                     labeledStmt.getContainer());
    }

    public void testDoWhileStatement4() throws ANTLRException
    {
        setupParser("do doSomething(); while (true);");

        DoWhileStatement result = (DoWhileStatement)_parser.statement();

        assertNotNull(result);

        ExpressionStatement exprStmt = (ExpressionStatement)result.getLoopStatement();

        assertNotNull(exprStmt);
        assertEquals(result,
                     exprStmt.getContainer());
    }

    public void testDoWhileStatement5() throws ANTLRException
    {
        setupParser("do return value; while (true);");

        DoWhileStatement result = (DoWhileStatement)_parser.statement();

        assertNotNull(result);

        ReturnStatement returnStmt = (ReturnStatement)result.getLoopStatement();

        assertNotNull(returnStmt);
        assertEquals(result,
                     returnStmt.getContainer());
    }

    public void testDoWhileStatement6() throws ANTLRException
    {
        setupParser("do break; while (true);");

        DoWhileStatement result = (DoWhileStatement)_parser.statement();

        assertNotNull(result);

        BreakStatement breakStmt = (BreakStatement)result.getLoopStatement();

        assertNotNull(breakStmt);
        assertEquals(result,
                     breakStmt.getContainer());
    }

    public void testDoWhileStatement7() throws ANTLRException
    {
        setupParser("do continue; while (true);");

        DoWhileStatement result = (DoWhileStatement)_parser.statement();

        assertNotNull(result);

        ContinueStatement continueStmt = (ContinueStatement)result.getLoopStatement();

        assertNotNull(continueStmt);
        assertEquals(result,
                     continueStmt.getContainer());
    }

    public void testDoWhileStatement8() throws ANTLRException
    {
        setupParser("do throw ex; while (true);");

        DoWhileStatement result = (DoWhileStatement)_parser.statement();

        assertNotNull(result);

        ThrowStatement throwStmt = (ThrowStatement)result.getLoopStatement();

        assertNotNull(throwStmt);
        assertEquals(result,
                     throwStmt.getContainer());
    }

    public void testDoWhileStatement9() throws ANTLRException
    {
        setupParser("do synchronized (val) { val += 2; } while (true);");

        DoWhileStatement result = (DoWhileStatement)_parser.statement();

        assertNotNull(result);

        SynchronizedStatement syncStmt = (SynchronizedStatement)result.getLoopStatement();

        assertNotNull(syncStmt);
        assertEquals(result,
                     syncStmt.getContainer());
    }
}
