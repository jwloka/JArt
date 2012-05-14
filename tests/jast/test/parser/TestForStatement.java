package jast.test.parser;
import jast.ast.nodes.ArrayAccess;
import jast.ast.nodes.ArrayInitializer;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.Block;
import jast.ast.nodes.BreakStatement;
import jast.ast.nodes.ContinueStatement;
import jast.ast.nodes.DoWhileStatement;
import jast.ast.nodes.EmptyStatement;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.ForStatement;
import jast.ast.nodes.IfThenElseStatement;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.LabeledStatement;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.SingleInitializer;
import jast.ast.nodes.StatementExpressionList;
import jast.ast.nodes.SwitchStatement;
import jast.ast.nodes.SynchronizedStatement;
import jast.ast.nodes.ThrowStatement;
import jast.ast.nodes.TryStatement;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.VariableAccess;
import jast.ast.nodes.WhileStatement;
import jast.parser.MultipleVariableDeclaration;
import antlr.ANTLRException;

public class TestForStatement extends TestBase
{

    public TestForStatement(String name)
    {
        super(name);
    }

    public void testForStatement1() throws ANTLRException
    {
        setupParser("for (;;) ;");

        ForStatement result = (ForStatement)_parser.statement();

        assertNotNull(result);
        assertNull(result.getInitList());
        assertNull(result.getCondition());
        assertNull(result.getUpdateList());

        EmptyStatement emptyStmt = (EmptyStatement)result.getLoopStatement();

        assertNotNull(emptyStmt);
        assertEquals(result,
                     emptyStmt.getContainer());
    }

    public void testForStatement10() throws ANTLRException
    {
        setupParser("for (;;) if (val > 0) return; else val -= 1;");

        ForStatement result = (ForStatement)_parser.statement();

        assertNotNull(result);

        IfThenElseStatement ifStmt = (IfThenElseStatement)result.getLoopStatement();

        assertNotNull(ifStmt);
        assertEquals(result,
                     ifStmt.getContainer());
    }

    public void testForStatement11() throws ANTLRException
    {
        setupParser("for (;;) while (val > 1) val -= 1;");

        ForStatement result = (ForStatement)_parser.statement();

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

    public void testForStatement12() throws ANTLRException
    {
        setupParser("for (;;) do val -= 1; while (val > 1);");

        ForStatement result = (ForStatement)_parser.statement();

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

    public void testForStatement13() throws ANTLRException
    {
        setupParser("for (;;) for(idx=0; idx < 3; idx++) ;");

        ForStatement result = (ForStatement)_parser.statement();

        assertNotNull(result);

        assertNull(result.getInitList());
        assertNull(result.getCondition());
        assertNull(result.getUpdateList());

        ForStatement forStmt = (ForStatement)result.getLoopStatement();

        assertNotNull(forStmt);
        assertEquals(result,
                     forStmt.getContainer());
        assertNotNull(forStmt.getInitList());
        assertNotNull(forStmt.getCondition());
        assertNotNull(forStmt.getUpdateList());

        EmptyStatement emptyStmt = (EmptyStatement)forStmt.getLoopStatement();

        assertNotNull(emptyStmt);
        assertEquals(forStmt,
                     emptyStmt.getContainer());
    }

    public void testForStatement14()
    {
        setupParser("for ;; return;");
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

    public void testForStatement15()
    {
        setupParser("for () return;");
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

    public void testForStatement16()
    {
        setupParser("for (;) return;");
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

    public void testForStatement17()
    {
        setupParser("for (;;;) return;");
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

    public void testForStatement18()
    {
        setupParser("for (;;; return;");
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

    public void testForStatement19() throws ANTLRException
    {
        setupParser("for (;;) try {doSomething();} catch (Exception ex) { return; }");

        ForStatement result = (ForStatement)_parser.statement();

        assertNotNull(result);
        assertNull(result.getInitList());
        assertNull(result.getCondition());
        assertNull(result.getUpdateList());

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

    public void testForStatement2() throws ANTLRException
    {
        setupParser("for (idx = 0, init(); idx < 2; ++idx, check()) {}");

        ForStatement result = (ForStatement)_parser.statement();

        assertNotNull(result);
        assertTrue(result.getCondition() instanceof BinaryExpression);

        StatementExpressionList list = result.getInitList();

        assertNotNull(list);
        assertEquals(2,
                     list.getExpressions().getCount());
        assertEquals(result,
                     list.getContainer());
        assertTrue(list.getExpressions().get(0) instanceof AssignmentExpression);
        assertTrue(list.getExpressions().get(1) instanceof MethodInvocation);

        list = result.getUpdateList();

        assertNotNull(list);
        assertEquals(2,
                     list.getExpressions().getCount());
        assertEquals(result,
                     list.getContainer());
        assertTrue(list.getExpressions().get(0) instanceof UnaryExpression);
        assertTrue(list.getExpressions().get(1) instanceof MethodInvocation);

        Block block = (Block)result.getLoopStatement();

        assertNotNull(block);
        assertEquals(result,
                     block.getContainer());
    }

    public void testForStatement20() throws ANTLRException
    {
        setupParser("for (;;) switch (var) { default: return; }");

        ForStatement result = (ForStatement)_parser.statement();

        assertNotNull(result);
        assertNull(result.getInitList());
        assertNull(result.getCondition());
        assertNull(result.getUpdateList());

        SwitchStatement switchStmt = (SwitchStatement)result.getLoopStatement();

        assertNotNull(switchStmt);
        assertTrue(switchStmt.hasDefault());
        assertEquals(1,
                     switchStmt.getCaseBlocks().getCount());
        assertEquals(result,
                     switchStmt.getContainer());
    }

    public void testForStatement21()
    {
        setupParser("for (;;) int value = 1;");
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

    public void testForStatement22() throws ANTLRException
    {
        setupParser("for (int idx = 0;idx < 10;idx++) val += idx;");

        ForStatement result = (ForStatement)_parser.statement();

        assertNotNull(result);
        assertTrue(result.hasInitDeclarations());
        assertEquals(1,
                     result.getInitDeclarations().getCount());

        LocalVariableDeclaration decl = result.getInitDeclarations().get(0);

        assertTrue(!decl.getModifiers().isFinal());
        assertEquals("int",
                     decl.getType().getBaseName());
        assertEquals("idx",
                     decl.getName());
        assertEquals(result,
                     decl.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)result.getLoopStatement();

        assertNotNull(exprStmt);
        assertEquals(result,
                     exprStmt.getContainer());

        AssignmentExpression assignExpr = (AssignmentExpression)exprStmt.getExpression();

        assertNotNull(assignExpr);
        assertEquals(exprStmt,
                     assignExpr.getContainer());

        VariableAccess varAccess = (VariableAccess)assignExpr.getValueExpression();

        assertNotNull(varAccess);
        assertEquals(decl,
                     varAccess.getVariableDeclaration());
        assertEquals(assignExpr,
                     varAccess.getContainer());
    }

    public void testForStatement23() throws ANTLRException
    {
        setupParser("for (int idx = 0;idx < 10;idx++) ; int idx = 0;");

        ForStatement forStmt = (ForStatement)_parser.blockStatement();

        assertNotNull(forStmt);

        MultipleVariableDeclaration localVar = (MultipleVariableDeclaration)_parser.blockStatement();

        assertNotNull(localVar);
    }

    public void testForStatement24()
    {
        setupParser("for (int idx = 0, double value = 0.0;idx < 3; idx++);");
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

    public void testForStatement25() throws ANTLRException
    {
        setupParser("for (iter.next(); iter.current() != null; iter.next()) {\n"+
                    "  Element e = iter.current();\n"+
                    "  if (e.isLeaf()) {\n"+
                    "      break;\n"+
                    "  }\n"+
                    "}\n");

        ForStatement result = (ForStatement)_parser.statement();

        assertNotNull(result);
        assertTrue(!result.hasInitDeclarations());
        assertTrue(result.getCondition() instanceof BinaryExpression);

        StatementExpressionList exprs = result.getInitList();

        assertNotNull(exprs);
        assertEquals(1,
                     exprs.getExpressions().getCount());
        assertEquals(result,
                     exprs.getContainer());
        assertTrue(exprs.getExpressions().get(0) instanceof MethodInvocation);

        exprs = result.getUpdateList();

        assertNotNull(exprs);
        assertEquals(1,
                     exprs.getExpressions().getCount());
        assertEquals(result,
                     exprs.getContainer());
        assertTrue(exprs.getExpressions().get(0) instanceof MethodInvocation);

        Block block = (Block)result.getLoopStatement();

        assertNotNull(block);
        assertEquals(2,
                     block.getBlockStatements().getCount());
        assertTrue(block.getBlockStatements().get(0) instanceof LocalVariableDeclaration);
        assertTrue(block.getBlockStatements().get(1) instanceof IfThenElseStatement);
        assertEquals(result,
                     block.getContainer());
    }

    public void testForStatement26() throws ANTLRException
    {
        setupParser("for (int idx = 0, val = 10;idx < 10;idx++) val += idx;");

        ForStatement result = (ForStatement)_parser.statement();

        assertNotNull(result);
    }

    public void testForStatement27() throws ANTLRException
    {
        setupParser("for (int idx = 0, val[] = { idx }; idx < 10; idx++) val[0] += idx;");

        ForStatement result = (ForStatement)_parser.statement();

        assertNotNull(result);
        assertTrue(result.hasInitDeclarations());
        assertEquals(2,
                     result.getInitDeclarations().getCount());

        LocalVariableDeclaration decl0 = result.getInitDeclarations().get(0);

        assertTrue(!decl0.getModifiers().isFinal());
        assertEquals("int",
                     decl0.getType().getBaseName());
        assertEquals(0,
                     decl0.getType().getDimensions());
        assertEquals("idx",
                     decl0.getName());
        assertEquals(result,
                     decl0.getContainer());

        LocalVariableDeclaration decl1 = result.getInitDeclarations().get(1);

        assertTrue(!decl1.getModifiers().isFinal());
        assertEquals("int",
                     decl1.getType().getBaseName());
        assertEquals(1,
                     decl1.getType().getDimensions());
        assertEquals("val",
                     decl1.getName());
        assertEquals(result,
                     decl1.getContainer());

        SingleInitializer singleInit = (SingleInitializer)decl0.getInitializer();

        assertNotNull(singleInit);
        assertEquals(decl0,
                     singleInit.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)singleInit.getInitEpression();

        assertNotNull(intLit);
        assertEquals("0",
                     intLit.asString());
        assertEquals(singleInit,
                     intLit.getContainer());

        ArrayInitializer arrayInit = (ArrayInitializer)decl1.getInitializer();

        assertNotNull(arrayInit);
        assertEquals(1,
                     arrayInit.getInitializers().getCount());
        assertEquals(decl1,
                     arrayInit.getContainer());

        singleInit = (SingleInitializer)arrayInit.getInitializers().get(0);

        assertNotNull(singleInit);
        assertEquals(arrayInit,
                     singleInit.getContainer());

        VariableAccess varAccess = (VariableAccess)singleInit.getInitEpression();

        assertNotNull(varAccess);
        assertEquals(decl0,
                     varAccess.getVariableDeclaration());
        assertEquals(singleInit,
                     varAccess.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)result.getLoopStatement();

        assertNotNull(exprStmt);
        assertEquals(result,
                     exprStmt.getContainer());

        AssignmentExpression assignExpr = (AssignmentExpression)exprStmt.getExpression();

        assertNotNull(assignExpr);
        assertEquals(exprStmt,
                     assignExpr.getContainer());

        ArrayAccess arrayAccess = (ArrayAccess)assignExpr.getLeftHandSide();

        assertNotNull(arrayAccess);
        assertEquals(assignExpr,
                     arrayAccess.getContainer());

        varAccess = (VariableAccess)arrayAccess.getBaseExpression();

        assertNotNull(varAccess);
        assertEquals(decl1,
                     varAccess.getVariableDeclaration());
        assertEquals(arrayAccess,
                     varAccess.getContainer());
    }

    public void testForStatement3() throws ANTLRException
    {
        setupParser("for (;;) test:;");

        ForStatement result = (ForStatement)_parser.statement();

        assertNotNull(result);

        LabeledStatement lbldStmt = (LabeledStatement)result.getLoopStatement();

        assertNotNull(lbldStmt);
        assertEquals(result,
                     lbldStmt.getContainer());
    }

    public void testForStatement4() throws ANTLRException
    {
        setupParser("for (;;) doSomething();");

        ForStatement result = (ForStatement)_parser.statement();

        assertNotNull(result);

        ExpressionStatement exprStmt = (ExpressionStatement)result.getLoopStatement();

        assertNotNull(exprStmt);
        assertEquals(result,
                     exprStmt.getContainer());
    }

    public void testForStatement5() throws ANTLRException
    {
        setupParser("for (;;) return value;");

        ForStatement result = (ForStatement)_parser.statement();

        assertNotNull(result);

        ReturnStatement returnStmt = (ReturnStatement)result.getLoopStatement();

        assertNotNull(returnStmt);
        assertEquals(result,
                     returnStmt.getContainer());
    }

    public void testForStatement6() throws ANTLRException
    {
        setupParser("for (;;) break;");

        ForStatement result = (ForStatement)_parser.statement();

        assertNotNull(result);

        BreakStatement breakStmt = (BreakStatement)result.getLoopStatement();

        assertNotNull(breakStmt);
        assertEquals(result,
                     breakStmt.getContainer());
    }

    public void testForStatement7() throws ANTLRException
    {
        setupParser("for (;;) continue;");

        ForStatement result = (ForStatement)_parser.statement();

        assertNotNull(result);

        ContinueStatement continueStmt = (ContinueStatement)result.getLoopStatement();

        assertNotNull(continueStmt);
        assertEquals(result,
                     continueStmt.getContainer());
    }

    public void testForStatement8() throws ANTLRException
    {
        setupParser("for (;;) throw ex;");

        ForStatement result = (ForStatement)_parser.statement();

        assertNotNull(result);

        ThrowStatement throwStmt = (ThrowStatement)result.getLoopStatement();

        assertNotNull(throwStmt);
        assertEquals(result,
                     throwStmt.getContainer());
    }

    public void testForStatement9() throws ANTLRException
    {
        setupParser("for (;;) synchronized (val) { val += 2; }");

        ForStatement result = (ForStatement)_parser.statement();

        assertNotNull(result);

        SynchronizedStatement syncStmt = (SynchronizedStatement)result.getLoopStatement();

        assertNotNull(syncStmt);
        assertEquals(result,
                     syncStmt.getContainer());
    }
}
