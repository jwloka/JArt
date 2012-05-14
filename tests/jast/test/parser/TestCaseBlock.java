package jast.test.parser;
import jast.ast.nodes.Block;
import jast.ast.nodes.BreakStatement;
import jast.ast.nodes.CaseBlock;
import jast.ast.nodes.CharacterLiteral;
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
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.SwitchStatement;
import jast.ast.nodes.SynchronizedStatement;
import jast.ast.nodes.ThrowStatement;
import jast.ast.nodes.TryStatement;
import jast.ast.nodes.WhileStatement;
import antlr.ANTLRException;

public class TestCaseBlock extends TestBase
{

    public TestCaseBlock(String name)
    {
        super(name);
    }

    public void testCaseBlock1() throws ANTLRException
    {
        setupParser("case 0:");

        CaseBlock caseBlock = _parser.caseBlock();

        assertNotNull(caseBlock);
        assertTrue(!caseBlock.hasDefault());
        assertEquals(1,
                     caseBlock.getCases().getCount());
        assertEquals(0,
                     caseBlock.getBlockStatements().getCount());

        IntegerLiteral intLit = (IntegerLiteral)caseBlock.getCases().get(0);

        assertNotNull(intLit);
        assertEquals("0",
                     intLit.asString());
        assertEquals(caseBlock,
                     intLit.getContainer());
    }

    public void testCaseBlock10() throws ANTLRException
    {
        setupParser("case 0:continue outer;");
        defineLabel("outer");

        CaseBlock caseBlock = _parser.caseBlock();

        assertNotNull(caseBlock);
        assertTrue(!caseBlock.hasDefault());
        assertEquals(1,
                     caseBlock.getCases().getCount());
        assertEquals(1,
                     caseBlock.getBlockStatements().getCount());

        ContinueStatement continueStmt = (ContinueStatement)caseBlock.getBlockStatements().get(0);

        assertNotNull(continueStmt);
        assertEquals("outer",
                     continueStmt.getTarget().getName());
        assertEquals(caseBlock,
                     continueStmt.getContainer());
    }

    public void testCaseBlock11() throws ANTLRException
    {
        setupParser("default:synchronized (var) {} throw new NullPointerException();");

        CaseBlock caseBlock = _parser.caseBlock();

        assertNotNull(caseBlock);
        assertTrue(caseBlock.hasDefault());
        assertEquals(0,
                     caseBlock.getCases().getCount());
        assertEquals(2,
                     caseBlock.getBlockStatements().getCount());

        SynchronizedStatement syncStmt = (SynchronizedStatement)caseBlock.getBlockStatements().get(0);

        assertNotNull(syncStmt);
        assertEquals(caseBlock,
                     syncStmt.getContainer());

        ThrowStatement throwStmt = (ThrowStatement)caseBlock.getBlockStatements().get(1);

        assertNotNull(throwStmt);
        assertEquals(caseBlock,
                     throwStmt.getContainer());
    }

    public void testCaseBlock12() throws ANTLRException
    {
        setupParser("case 0:while (idx > 0) idx--; try { doSomething(); } catch (Exception ex) {}");

        CaseBlock caseBlock = _parser.caseBlock();

        assertNotNull(caseBlock);
        assertTrue(!caseBlock.hasDefault());
        assertEquals(1,
                     caseBlock.getCases().getCount());
        assertEquals(2,
                     caseBlock.getBlockStatements().getCount());

        WhileStatement whileStmt = (WhileStatement)caseBlock.getBlockStatements().get(0);

        assertNotNull(whileStmt);
        assertEquals(caseBlock,
                     whileStmt.getContainer());

        TryStatement tryStmt = (TryStatement)caseBlock.getBlockStatements().get(1);

        assertNotNull(tryStmt);
        assertEquals(caseBlock,
                     tryStmt.getContainer());
    }

    public void testCaseBlock13() throws ANTLRException
    {
        setupParser("case 0:for (;test();); do doSomething(); while (test());");

        CaseBlock caseBlock = _parser.caseBlock();

        assertNotNull(caseBlock);
        assertTrue(!caseBlock.hasDefault());
        assertEquals(1,
                     caseBlock.getCases().getCount());
        assertEquals(2,
                     caseBlock.getBlockStatements().getCount());

        ForStatement forStmt = (ForStatement)caseBlock.getBlockStatements().get(0);

        assertNotNull(forStmt);
        assertEquals(caseBlock,
                     forStmt.getContainer());

        DoWhileStatement doStmt = (DoWhileStatement)caseBlock.getBlockStatements().get(1);

        assertNotNull(doStmt);
        assertEquals(caseBlock,
                     doStmt.getContainer());
    }

    public void testCaseBlock14() throws ANTLRException
    {
        setupParser("default:switch (var) { case 0: return; default: break;}");

        CaseBlock caseBlock = _parser.caseBlock();

        assertNotNull(caseBlock);
        assertTrue(caseBlock.hasDefault());
        assertEquals(0,
                     caseBlock.getCases().getCount());
        assertEquals(1,
                     caseBlock.getBlockStatements().getCount());

        SwitchStatement switchStmt = (SwitchStatement)caseBlock.getBlockStatements().get(0);

        assertNotNull(switchStmt);
        assertEquals(caseBlock,
                     switchStmt.getContainer());
    }

    public void testCaseBlock15()
    {
        setupParser("default:default:;");
        try
        {
            _parser.caseBlock();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testCaseBlock16()
    {
        setupParser("case:");
        try
        {
            _parser.caseBlock();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testCaseBlock17()
    {
        setupParser("case 0 case 1:");
        try
        {
            _parser.caseBlock();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testCaseBlock18()
    {
        setupParser("case0:;");
        try
        {
            _parser.caseBlock();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testCaseBlock19() throws ANTLRException
    {
        setupParser("case 1:int value = 0;");

        CaseBlock caseBlock = _parser.caseBlock();

        assertNotNull(caseBlock);
        assertTrue(!caseBlock.hasDefault());
        assertEquals(1,
                     caseBlock.getCases().getCount());
        assertEquals(1,
                     caseBlock.getBlockStatements().getCount());

        LocalVariableDeclaration localVarDecl = (LocalVariableDeclaration)caseBlock.getBlockStatements().get(0);

        assertNotNull(localVarDecl);
        assertTrue(!localVarDecl.getModifiers().isFinal());
        assertEquals("int",
                     localVarDecl.getType().getBaseName());
        assertEquals("value",
                     localVarDecl.getName());
        assertEquals(caseBlock,
                     localVarDecl.getContainer());
    }

    public void testCaseBlock2() throws ANTLRException
    {
        setupParser("default :");

        CaseBlock caseBlock = _parser.caseBlock();

        assertNotNull(caseBlock);
        assertTrue(caseBlock.hasDefault());
        assertEquals(0,
                     caseBlock.getCases().getCount());
        assertEquals(0,
                     caseBlock.getBlockStatements().getCount());
    }

    public void testCaseBlock3() throws ANTLRException
    {
        setupParser("case 0:\ncase 1:\ncase 2:");

        CaseBlock caseBlock = _parser.caseBlock();

        assertNotNull(caseBlock);
        assertTrue(!caseBlock.hasDefault());
        assertEquals(3,
                     caseBlock.getCases().getCount());
        assertEquals(0,
                     caseBlock.getBlockStatements().getCount());

        IntegerLiteral intLit = (IntegerLiteral)caseBlock.getCases().get(0);

        assertNotNull(intLit);
        assertEquals("0",
                     intLit.asString());
        assertEquals(caseBlock,
                     intLit.getContainer());

        intLit = (IntegerLiteral)caseBlock.getCases().get(1);

        assertNotNull(intLit);
        assertEquals("1",
                     intLit.asString());
        assertEquals(caseBlock,
                     intLit.getContainer());

        intLit = (IntegerLiteral)caseBlock.getCases().get(2);

        assertNotNull(intLit);
        assertEquals("2",
                     intLit.asString());
        assertEquals(caseBlock,
                     intLit.getContainer());
    }

    public void testCaseBlock4() throws ANTLRException
    {
        setupParser("case 0: default:;");

        CaseBlock caseBlock = _parser.caseBlock();

        assertNotNull(caseBlock);
        assertTrue(caseBlock.hasDefault());
        assertEquals(1,
                     caseBlock.getCases().getCount());
        assertEquals(1,
                     caseBlock.getBlockStatements().getCount());

        IntegerLiteral intLit = (IntegerLiteral)caseBlock.getCases().get(0);

        assertNotNull(intLit);
        assertEquals("0",
                     intLit.asString());
        assertEquals(caseBlock,
                     intLit.getContainer());
    }

    public void testCaseBlock5() throws ANTLRException
    {
        setupParser("case 'a':\ndoSomething();");

        CaseBlock caseBlock = _parser.caseBlock();

        assertNotNull(caseBlock);
        assertTrue(!caseBlock.hasDefault());
        assertEquals(1,
                     caseBlock.getCases().getCount());
        assertEquals(1,
                     caseBlock.getBlockStatements().getCount());

        CharacterLiteral charLit = (CharacterLiteral)caseBlock.getCases().get(0);

        assertNotNull(charLit);
        assertEquals("'a'",
                     charLit.asString());
        assertEquals(caseBlock,
                     charLit.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)caseBlock.getBlockStatements().get(0);

        assertNotNull(exprStmt);
        assertEquals(caseBlock,
                     exprStmt.getContainer());

        MethodInvocation methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertNotNull(methodInvoc);
        assertEquals("doSomething",
                     methodInvoc.getMethodName());
        assertEquals(exprStmt,
                     methodInvoc.getContainer());
    }

    public void testCaseBlock6() throws ANTLRException
    {
        setupParser("default:\nvar++;break;");

        CaseBlock caseBlock = _parser.caseBlock();

        assertNotNull(caseBlock);
        assertTrue(caseBlock.hasDefault());
        assertEquals(0,
                     caseBlock.getCases().getCount());
        assertEquals(2,
                     caseBlock.getBlockStatements().getCount());

        ExpressionStatement exprStmt = (ExpressionStatement)caseBlock.getBlockStatements().get(0);

        assertNotNull(exprStmt);
        assertEquals(caseBlock,
                     exprStmt.getContainer());

        PostfixExpression postfixExp = (PostfixExpression)exprStmt.getExpression();

        assertNotNull(postfixExp);
        assertTrue(postfixExp.isIncrement());
        assertEquals(exprStmt,
                     postfixExp.getContainer());

        BreakStatement breakStmt = (BreakStatement)caseBlock.getBlockStatements().get(1);

        assertNotNull(breakStmt);
        assertEquals(caseBlock,
                     breakStmt.getContainer());
    }

    public void testCaseBlock7() throws ANTLRException
    {
        setupParser("default:test:; if (x > 0) return;");

        CaseBlock caseBlock = _parser.caseBlock();

        assertNotNull(caseBlock);
        assertTrue(caseBlock.hasDefault());
        assertEquals(0,
                     caseBlock.getCases().getCount());
        assertEquals(2,
                     caseBlock.getBlockStatements().getCount());

        LabeledStatement labeledStmt = (LabeledStatement)caseBlock.getBlockStatements().get(0);

        assertNotNull(labeledStmt);
        assertTrue(labeledStmt.getStatement() instanceof EmptyStatement);
        assertEquals(caseBlock,
                     labeledStmt.getContainer());

        IfThenElseStatement ifStmt = (IfThenElseStatement)caseBlock.getBlockStatements().get(1);

        assertNotNull(ifStmt);
        assertEquals(caseBlock,
                     ifStmt.getContainer());
    }

    public void testCaseBlock8() throws ANTLRException
    {
        setupParser("default:{}return;");

        CaseBlock caseBlock = _parser.caseBlock();

        assertNotNull(caseBlock);
        assertTrue(caseBlock.hasDefault());
        assertEquals(0,
                     caseBlock.getCases().getCount());
        assertEquals(2,
                     caseBlock.getBlockStatements().getCount());

        Block block = (Block)caseBlock.getBlockStatements().get(0);

        assertNotNull(block);
        assertEquals(0,
                     block.getBlockStatements().getCount());
        assertEquals(caseBlock,
                     block.getContainer());

        ReturnStatement returnStmt = (ReturnStatement)caseBlock.getBlockStatements().get(1);

        assertNotNull(returnStmt);
        assertEquals(caseBlock,
                     returnStmt.getContainer());
    }

    public void testCaseBlock9() throws ANTLRException
    {
        setupParser("default:break;");

        CaseBlock caseBlock = _parser.caseBlock();

        assertNotNull(caseBlock);
        assertTrue(caseBlock.hasDefault());
        assertEquals(0,
                     caseBlock.getCases().getCount());
        assertEquals(1,
                     caseBlock.getBlockStatements().getCount());

        BreakStatement breakStmt = (BreakStatement)caseBlock.getBlockStatements().get(0);

        assertNotNull(breakStmt);
        assertEquals(caseBlock,
                     breakStmt.getContainer());
    }
}
