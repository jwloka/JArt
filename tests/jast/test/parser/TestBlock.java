package jast.test.parser;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.Block;
import jast.ast.nodes.BreakStatement;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ContinueStatement;
import jast.ast.nodes.DoWhileStatement;
import jast.ast.nodes.EmptyStatement;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.ForStatement;
import jast.ast.nodes.IfThenElseStatement;
import jast.ast.nodes.LabeledStatement;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.SwitchStatement;
import jast.ast.nodes.SynchronizedStatement;
import jast.ast.nodes.ThrowStatement;
import jast.ast.nodes.TryStatement;
import jast.ast.nodes.WhileStatement;
import antlr.ANTLRException;

public class TestBlock extends TestBase
{

    public TestBlock(String name)
    {
        super(name);
    }

    public void testBlock1() throws ANTLRException
    {
        setupParser("{}");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(0,
                     result.getBlockStatements().getCount());
    }

    public void testBlock10()
    {
        setupParser("{{}");
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

    public void testBlock11() throws ANTLRException
    {
        setupParser("{a = 5;}");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(1,
                     result.getBlockStatements().getCount());

        ExpressionStatement exprStmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertNotNull(exprStmt);
        assertEquals(result,
                     exprStmt.getContainer());

        AssignmentExpression assignExpr = (AssignmentExpression)exprStmt.getExpression();

        assertNotNull(assignExpr);
        assertEquals(AssignmentExpression.ASSIGN_OP,
                     assignExpr.getOperator());
        assertEquals(exprStmt,
                     assignExpr.getContainer());
    }

    public void testBlock12() throws ANTLRException
    {
        setupParser("{ throw new Exception(); }");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(1,
                     result.getBlockStatements().getCount());

        ThrowStatement throwStmt = (ThrowStatement)result.getBlockStatements().get(0);

        assertNotNull(throwStmt);
        assertEquals(result,
                     throwStmt.getContainer());
    }

    public void testBlock13() throws ANTLRException
    {
        setupParser("{ synchronized (val) { val += 2; } }");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(1,
                     result.getBlockStatements().getCount());

        SynchronizedStatement syncStmt = (SynchronizedStatement)result.getBlockStatements().get(0);

        assertNotNull(syncStmt);
        assertEquals(result,
                     syncStmt.getContainer());
    }

    public void testBlock14() throws ANTLRException
    {
        setupParser("{ if (val > 2) { return true; } else return false; }");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(1,
                     result.getBlockStatements().getCount());

        IfThenElseStatement ifStmt = (IfThenElseStatement)result.getBlockStatements().get(0);

        assertNotNull(ifStmt);
        assertNotNull(ifStmt.getFalseStatement());
        assertEquals(result,
                     ifStmt.getContainer());
    }

    public void testBlock15() throws ANTLRException
    {
        setupParser("{ while (isCorrect()) { val += 2; } }");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(1,
                     result.getBlockStatements().getCount());

        WhileStatement whileStmt = (WhileStatement)result.getBlockStatements().get(0);

        assertNotNull(whileStmt);
        assertEquals(result,
                     whileStmt.getContainer());
    }

    public void testBlock16() throws ANTLRException
    {
        setupParser("{ do { val += 2; } while (isCorrect()); }");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(1,
                     result.getBlockStatements().getCount());

        DoWhileStatement doStmt = (DoWhileStatement)result.getBlockStatements().get(0);

        assertNotNull(doStmt);
        assertEquals(result,
                     doStmt.getContainer());
    }

    public void testBlock17() throws ANTLRException
    {
        setupParser("{ for (idx = 0; idx < 3; idx += 1) val -= 2; }");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(1,
                     result.getBlockStatements().getCount());

        ForStatement forStmt = (ForStatement)result.getBlockStatements().get(0);

        assertNotNull(forStmt);
        assertNotNull(forStmt.getInitList());
        assertNotNull(forStmt.getCondition());
        assertNotNull(forStmt.getUpdateList());
        assertNotNull(forStmt.getLoopStatement());
        assertEquals(result,
                     forStmt.getContainer());
    }

    public void testBlock18() throws ANTLRException
    {
        setupParser("{ try { doSomething(); } catch (Exception ex) { return; } }");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(1,
                     result.getBlockStatements().getCount());

        TryStatement tryStmt = (TryStatement)result.getBlockStatements().get(0);

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

    public void testBlock19() throws ANTLRException
    {
        setupParser("{ switch (var) { default: return; } }");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(1,
                     result.getBlockStatements().getCount());

        SwitchStatement switchStmt = (SwitchStatement)result.getBlockStatements().get(0);

        assertNotNull(switchStmt);
        assertTrue(switchStmt.hasDefault());
        assertEquals(1,
                     switchStmt.getCaseBlocks().getCount());
        assertEquals(result,
                     switchStmt.getContainer());
    }

    public void testBlock2() throws ANTLRException
    {
        setupParser("{;}");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(1,
                     result.getBlockStatements().getCount());

        EmptyStatement emptyStmt = (EmptyStatement)result.getBlockStatements().get(0);

        assertNotNull(emptyStmt);
        assertEquals(result,
                     emptyStmt.getContainer());
    }

    public void testBlock20() throws ANTLRException
    {
        setupParser("{ int value = 0; }");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(1,
                     result.getBlockStatements().getCount());

        LocalVariableDeclaration localVarDecl = (LocalVariableDeclaration)result.getBlockStatements().get(0);

        assertNotNull(localVarDecl);
        assertTrue(!localVarDecl.getModifiers().isFinal());
        assertEquals("int",
                     localVarDecl.getType().getBaseName());
        assertEquals("value",
                     localVarDecl.getName());
        assertEquals(result,
                     localVarDecl.getContainer());
    }

    public void testBlock21() throws ANTLRException
    {
        setupParser("{ class Test {} }");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(1,
                     result.getBlockStatements().getCount());

        ClassDeclaration classDecl = (ClassDeclaration)result.getBlockStatements().get(0);

        assertNotNull(classDecl);
        assertEquals("Test", classDecl.getName());
        assertTrue(!classDecl.isInner());
        assertTrue(classDecl.isLocal());
        assertEquals(result,
                     classDecl.getContainer());
    }

    public void testBlock22()
    {
        setupParser("{ interface Test {} }");
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

    public void testBlock23() throws ANTLRException
    {
        setupParser("{ prj.getPersons().add(prs); }");
        defineField("prj");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(1,
                     result.getBlockStatements().getCount());

        ExpressionStatement exprStmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertNotNull(exprStmt);
        assertEquals(result,
                     exprStmt.getContainer());

        MethodInvocation methodInvoc1 = (MethodInvocation)exprStmt.getExpression();

        assertNotNull(methodInvoc1);
        assertEquals("add",
                     methodInvoc1.getMethodName());
        assertTrue(methodInvoc1.isTrailing());
        assertNotNull(methodInvoc1.getArgumentList());
        assertEquals(exprStmt,
                     methodInvoc1.getContainer());

        MethodInvocation methodInvoc2 = (MethodInvocation)methodInvoc1.getBaseExpression();

        assertNotNull(methodInvoc2);
        assertEquals("getPersons",
                     methodInvoc2.getMethodName());
        assertNull(methodInvoc2.getArgumentList());
        assertTrue(methodInvoc2.isTrailing());
        assertEquals(methodInvoc1,
                     methodInvoc2.getContainer());

        FieldAccess fieldAccess = (FieldAccess)methodInvoc2.getBaseExpression();

        assertNotNull(fieldAccess);
        assertEquals("prj",
                     fieldAccess.getFieldName());
        assertEquals(methodInvoc2,
                     fieldAccess.getContainer());
    }

    public void testBlock3() throws ANTLRException
    {
        setupParser("{;;;}");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(3,
                     result.getBlockStatements().getCount());

        EmptyStatement emptyStmt = (EmptyStatement)result.getBlockStatements().get(0);

        assertNotNull(emptyStmt);
        assertEquals(result,
                     emptyStmt.getContainer());

        emptyStmt = (EmptyStatement)result.getBlockStatements().get(1);

        assertNotNull(emptyStmt);
        assertEquals(result,
                     emptyStmt.getContainer());

        emptyStmt = (EmptyStatement)result.getBlockStatements().get(2);

        assertNotNull(emptyStmt);
        assertEquals(result,
                     emptyStmt.getContainer());
    }

    public void testBlock4() throws ANTLRException
    {
        setupParser("{{{}}{}}");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(2,
                     result.getBlockStatements().getCount());

        Block innerBlock = (Block)result.getBlockStatements().get(0);

        assertNotNull(innerBlock);
        assertEquals(1,
                     innerBlock.getBlockStatements().getCount());
        assertEquals(result,
                     innerBlock.getContainer());

        Block deepBlock = (Block)innerBlock.getBlockStatements().get(0);

        assertNotNull(deepBlock);
        assertEquals(0,
                     deepBlock.getBlockStatements().getCount());
        assertEquals(innerBlock,
                     deepBlock.getContainer());

        innerBlock = (Block)result.getBlockStatements().get(1);

        assertNotNull(innerBlock);
        assertEquals(0,
                     innerBlock.getBlockStatements().getCount());
        assertEquals(result,
                     innerBlock.getContainer());
    }

    public void testBlock5() throws ANTLRException
    {
        setupParser("{test:;}");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(1,
                     result.getBlockStatements().getCount());

        LabeledStatement labelStmt = (LabeledStatement)result.getBlockStatements().get(0);

        assertNotNull(labelStmt);
        assertEquals("test",
                     labelStmt.getName());
        assertEquals(result,
                     labelStmt.getContainer());

        EmptyStatement emptyStmt = (EmptyStatement)labelStmt.getStatement();

        assertNotNull(emptyStmt);
        assertEquals(labelStmt,
                     emptyStmt.getContainer());
    }

    public void testBlock6() throws ANTLRException
    {
        setupParser("{return;}");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(1,
                     result.getBlockStatements().getCount());

        ReturnStatement returnStmt = (ReturnStatement)result.getBlockStatements().get(0);

        assertNotNull(returnStmt);
        assertNull(returnStmt.getReturnValue());
        assertEquals(result,
                     returnStmt.getContainer());
    }

    public void testBlock7() throws ANTLRException
    {
        setupParser("{break;}");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(1,
                     result.getBlockStatements().getCount());

        BreakStatement breakStmt = (BreakStatement)result.getBlockStatements().get(0);

        assertNotNull(breakStmt);
        assertNull(breakStmt.getTarget());
        assertEquals(result,
                     breakStmt.getContainer());
    }

    public void testBlock8() throws ANTLRException
    {
        setupParser("{continue;}");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(1,
                     result.getBlockStatements().getCount());

        ContinueStatement continueStmt = (ContinueStatement)result.getBlockStatements().get(0);

        assertNotNull(continueStmt);
        assertNull(continueStmt.getTarget());
        assertEquals(result,
                     continueStmt.getContainer());
    }

    public void testBlock9()
    {
        setupParser("{");
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
