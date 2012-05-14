package jast.test.parser;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.Block;
import jast.ast.nodes.BooleanLiteral;
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

public class TestWhileStatement extends TestBase
{

    public TestWhileStatement(String name)
    {
        super(name);
    }

    public void testWhileStatement1() throws ANTLRException
    {
        setupParser("while (true) ;");

        WhileStatement result = (WhileStatement)_parser.statement();

        assertNotNull(result);

        BooleanLiteral boolLit = (BooleanLiteral)result.getCondition();

        assertNotNull(boolLit);
        assertTrue(boolLit.getValue());
        assertEquals(result,
                     boolLit.getContainer());

        EmptyStatement emptyStmt = (EmptyStatement)result.getLoopStatement();

        assertNotNull(emptyStmt);
        assertEquals(result,
                     emptyStmt.getContainer());
    }

    public void testWhileStatement10() throws ANTLRException
    {
        setupParser("while (true) if (val > 0) return; else val -= 1;");

        WhileStatement result = (WhileStatement)_parser.statement();

        assertNotNull(result);

        IfThenElseStatement ifStmt = (IfThenElseStatement)result.getLoopStatement();

        assertNotNull(ifStmt);
        assertEquals(result,
                     ifStmt.getContainer());
    }

    public void testWhileStatement11() throws ANTLRException
    {
        setupParser("while (true) while (val > 1) val -= 1;");

        WhileStatement result = (WhileStatement)_parser.statement();

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

    public void testWhileStatement12() throws ANTLRException
    {
        setupParser("while (true) do val -= 1; while (val > 1);");

        WhileStatement result = (WhileStatement)_parser.statement();

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

    public void testWhileStatement14()
    {
        setupParser("while a > 0 return;");
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

    public void testWhileStatement15()
    {
        setupParser("while () ;");
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

    public void testWhileStatement16() throws ANTLRException
    {
        setupParser("while (true) for(idx = 0; idx < 3; idx++) doSomething();");

        WhileStatement result = (WhileStatement)_parser.statement();

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

    public void testWhileStatement17() throws ANTLRException
    {
        setupParser("while (true) try {doSomething();} catch (Exception ex) { return; }");

        WhileStatement result = (WhileStatement)_parser.statement();

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

    public void testWhileStatement18() throws ANTLRException
    {
        setupParser("while (true) switch (var) { default: return; }");

        WhileStatement result = (WhileStatement)_parser.statement();

        assertNotNull(result);

        SwitchStatement switchStmt = (SwitchStatement)result.getLoopStatement();

        assertNotNull(switchStmt);
        assertTrue(switchStmt.hasDefault());
        assertEquals(1,
                     switchStmt.getCaseBlocks().getCount());
        assertEquals(result,
                     switchStmt.getContainer());
    }

    public void testWhileStatement19()
    {
        setupParser("while (var > 0) int value = 0;");
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

    public void testWhileStatement2() throws ANTLRException
    {
        setupParser("while (idx > 0) {}");

        WhileStatement result = (WhileStatement)_parser.statement();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getCondition();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.GREATER_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());

        Block block = (Block)result.getLoopStatement();

        assertNotNull(block);
        assertEquals(result,
                     block.getContainer());
    }

    public void testWhileStatement3() throws ANTLRException
    {
        setupParser("while (true) test:;");

        WhileStatement result = (WhileStatement)_parser.statement();

        assertNotNull(result);

        LabeledStatement lbldStmt = (LabeledStatement)result.getLoopStatement();

        assertNotNull(lbldStmt);
        assertEquals(result,
                     lbldStmt.getContainer());
    }

    public void testWhileStatement4() throws ANTLRException
    {
        setupParser("while (true) doSomething();");

        WhileStatement result = (WhileStatement)_parser.statement();

        assertNotNull(result);

        ExpressionStatement exprStmt = (ExpressionStatement)result.getLoopStatement();

        assertNotNull(exprStmt);
        assertEquals(result,
                     exprStmt.getContainer());
    }

    public void testWhileStatement5() throws ANTLRException
    {
        setupParser("while (true) return value;");

        WhileStatement result = (WhileStatement)_parser.statement();

        assertNotNull(result);

        ReturnStatement returnStmt = (ReturnStatement)result.getLoopStatement();

        assertNotNull(returnStmt);
        assertEquals(result,
                     returnStmt.getContainer());
    }

    public void testWhileStatement6() throws ANTLRException
    {
        setupParser("while (true) break;");

        WhileStatement result = (WhileStatement)_parser.statement();

        assertNotNull(result);

        BreakStatement breakStmt = (BreakStatement)result.getLoopStatement();

        assertNotNull(breakStmt);
        assertEquals(result,
                     breakStmt.getContainer());
    }

    public void testWhileStatement7() throws ANTLRException
    {
        setupParser("while (true) continue;");

        WhileStatement result = (WhileStatement)_parser.statement();

        assertNotNull(result);

        ContinueStatement continueStmt = (ContinueStatement)result.getLoopStatement();

        assertNotNull(continueStmt);
        assertEquals(result,
                     continueStmt.getContainer());
    }

    public void testWhileStatement8() throws ANTLRException
    {
        setupParser("while (true) throw ex;");

        WhileStatement result = (WhileStatement)_parser.statement();

        assertNotNull(result);

        ThrowStatement throwStmt = (ThrowStatement)result.getLoopStatement();

        assertNotNull(throwStmt);
        assertEquals(result,
                     throwStmt.getContainer());
    }

    public void testWhileStatement9() throws ANTLRException
    {
        setupParser("while (true) synchronized (val) { val += 2; }");

        WhileStatement result = (WhileStatement)_parser.statement();

        assertNotNull(result);

        SynchronizedStatement syncStmt = (SynchronizedStatement)result.getLoopStatement();

        assertNotNull(syncStmt);
        assertEquals(result,
                     syncStmt.getContainer());
    }
}
