package jast.test.parser;
import jast.ast.nodes.ArrayAccess;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.CaseBlock;
import jast.ast.nodes.CharacterLiteral;
import jast.ast.nodes.ConditionalExpression;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.ParenthesizedExpression;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.SwitchStatement;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.VariableAccess;
import antlr.ANTLRException;

public class TestSwitchStatement extends TestBase
{

    public TestSwitchStatement(String name)
    {
        super(name);
    }

    public void testSwitchStatement1() throws ANTLRException
    {
        setupParser("switch (var) {}");

        SwitchStatement result = (SwitchStatement)_parser.statement();

        assertNotNull(result);
        assertTrue(!result.hasDefault());
        assertEquals(0,
                     result.getCaseBlocks().getCount());
    }

    public void testSwitchStatement10() throws ANTLRException
    {
        setupParser("switch (~var) {}");

        SwitchStatement result = (SwitchStatement)_parser.statement();

        assertNotNull(result);

        UnaryExpression unaryExpr = (UnaryExpression)result.getSwitchExpression();

        assertNotNull(unaryExpr);
        assertEquals(result,
                     unaryExpr.getContainer());
    }

    public void testSwitchStatement11() throws ANTLRException
    {
        setupParser("switch ((int)1l) {}");

        SwitchStatement result = (SwitchStatement)_parser.statement();

        assertNotNull(result);

        UnaryExpression unaryExpr = (UnaryExpression)result.getSwitchExpression();

        assertNotNull(unaryExpr);
        assertEquals("int",
                     unaryExpr.getCastType().toString());
        assertEquals(result,
                     unaryExpr.getContainer());
    }

    public void testSwitchStatement12() throws ANTLRException
    {
        setupParser("switch (var + 2) {}");

        SwitchStatement result = (SwitchStatement)_parser.statement();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getSwitchExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.PLUS_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());
    }

    public void testSwitchStatement13() throws ANTLRException
    {
        setupParser("switch (var % 2) {}");

        SwitchStatement result = (SwitchStatement)_parser.statement();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getSwitchExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.MOD_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());
    }

    public void testSwitchStatement14() throws ANTLRException
    {
        setupParser("switch (var << 2) {}");

        SwitchStatement result = (SwitchStatement)_parser.statement();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getSwitchExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.SHIFT_LEFT_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());
    }

    public void testSwitchStatement15() throws ANTLRException
    {
        setupParser("switch (var ^ 2) {}");

        SwitchStatement result = (SwitchStatement)_parser.statement();

        assertNotNull(result);

        BinaryExpression binExpr = (BinaryExpression)result.getSwitchExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.BITWISE_XOR_OP,
                     binExpr.getOperator());
        assertEquals(result,
                     binExpr.getContainer());
    }

    public void testSwitchStatement16() throws ANTLRException
    {
        setupParser("switch (var > 2 ? 0 : var) {}");

        SwitchStatement result = (SwitchStatement)_parser.statement();

        assertNotNull(result);

        ConditionalExpression condExpr = (ConditionalExpression)result.getSwitchExpression();

        assertNotNull(condExpr);
        assertEquals(result,
                     condExpr.getContainer());
    }

    public void testSwitchStatement17() throws ANTLRException
    {
        setupParser("switch (true ? 0 : var) {}");

        SwitchStatement result = (SwitchStatement)_parser.statement();

        assertNotNull(result);

        ConditionalExpression condExpr = (ConditionalExpression)result.getSwitchExpression();

        assertNotNull(condExpr);
        assertEquals(result,
                     condExpr.getContainer());
    }

    public void testSwitchStatement18() throws ANTLRException
    {
        setupParser("switch (var += 2) {}");

        SwitchStatement result = (SwitchStatement)_parser.statement();

        assertNotNull(result);

        AssignmentExpression assignExpr = (AssignmentExpression)result.getSwitchExpression();

        assertNotNull(assignExpr);
        assertEquals(result,
                     assignExpr.getContainer());
    }

    public void testSwitchStatement19() throws ANTLRException
    {
        setupParser("switch (var) {}");
        defineVariable("var", false);

        SwitchStatement result = (SwitchStatement)_parser.statement();

        assertNotNull(result);

        VariableAccess varAccess = (VariableAccess)result.getSwitchExpression();

        assertNotNull(varAccess);
        assertEquals(result,
                     varAccess.getContainer());
    }

    public void testSwitchStatement2() throws ANTLRException
    {
        setupParser("switch (1) {}");

        SwitchStatement result = (SwitchStatement)_parser.statement();

        assertNotNull(result);

        IntegerLiteral intLit = (IntegerLiteral)result.getSwitchExpression();

        assertNotNull(intLit);
        assertEquals("1",
                     intLit.asString());
        assertEquals(result,
                     intLit.getContainer());
    }

    public void testSwitchStatement20() throws ANTLRException
    {
        setupParser("switch (var) { default:; }");

        SwitchStatement result = (SwitchStatement)_parser.statement();

        assertNotNull(result);
        assertTrue(result.hasDefault());
        assertEquals(1,
                     result.getCaseBlocks().getCount());

        CaseBlock caseBlock = result.getCaseBlocks().get(0);

        assertNotNull(caseBlock);
        assertTrue(caseBlock.hasDefault());
        assertEquals(0,
                     caseBlock.getCases().getCount());
        assertEquals(1,
                     caseBlock.getBlockStatements().getCount());
        assertEquals(result,
                     caseBlock.getContainer());
    }

    public void testSwitchStatement21() throws ANTLRException
    {
        setupParser("switch (var) { case 0: case 1:; case 2: default:; }");

        SwitchStatement result = (SwitchStatement)_parser.statement();

        assertNotNull(result);
        assertTrue(result.hasDefault());
        assertEquals(2,
                     result.getCaseBlocks().getCount());

        CaseBlock caseBlock = result.getCaseBlocks().get(0);

        assertNotNull(caseBlock);
        assertTrue(!caseBlock.hasDefault());
        assertEquals(2,
                     caseBlock.getCases().getCount());
        assertEquals(1,
                     caseBlock.getBlockStatements().getCount());
        assertEquals(result,
                     caseBlock.getContainer());

        caseBlock = result.getCaseBlocks().get(1);

        assertNotNull(caseBlock);
        assertTrue(caseBlock.hasDefault());
        assertEquals(1,
                     caseBlock.getCases().getCount());
        assertEquals(1,
                         caseBlock.getBlockStatements().getCount());
        assertEquals(result,
                     caseBlock.getContainer());
    }

    public void testSwitchStatement22()
    {
        setupParser("switch { default:; }");
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

    public void testSwitchStatement23()
    {
        setupParser("switch (var)");
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

    public void testSwitchStatement24()
    {
        setupParser("switch (var);");
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

    public void testSwitchStatement25() throws ANTLRException
    {
        setupParser("switch (var) { case 0: int value = 0; break; default:value = 1;  }");

        SwitchStatement result = (SwitchStatement)_parser.statement();

        assertNotNull(result);
        assertTrue(result.hasDefault());
        assertEquals(2,
                     result.getCaseBlocks().getCount());

        CaseBlock caseBlock = result.getCaseBlocks().get(0);

        assertNotNull(caseBlock);
        assertTrue(!caseBlock.hasDefault());
        assertEquals(1,
                     caseBlock.getCases().getCount());
        assertEquals(2,
                     caseBlock.getBlockStatements().getCount());
        assertEquals(result,
                     caseBlock.getContainer());

        LocalVariableDeclaration localVarDecl = (LocalVariableDeclaration)caseBlock.getBlockStatements().get(0);

        assertNotNull(localVarDecl);
        assertEquals("value",
                     localVarDecl.getName());
        assertEquals(caseBlock,
                     localVarDecl.getContainer());

        caseBlock = result.getCaseBlocks().get(1);

        assertNotNull(caseBlock);
        assertTrue(caseBlock.hasDefault());
        assertEquals(0,
                     caseBlock.getCases().getCount());
        assertEquals(1,
                     caseBlock.getBlockStatements().getCount());
        assertEquals(result,
                     caseBlock.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)caseBlock.getBlockStatements().get(0);

        assertNotNull(exprStmt);
        assertEquals(caseBlock,
                     exprStmt.getContainer());

        AssignmentExpression assignExpr = (AssignmentExpression)exprStmt.getExpression();

        assertNotNull(assignExpr);
        assertEquals(exprStmt,
                     assignExpr.getContainer());

        VariableAccess varAccess = (VariableAccess)assignExpr.getLeftHandSide();

        assertNotNull(varAccess);
        assertEquals(localVarDecl,
                     varAccess.getVariableDeclaration());
        assertEquals(assignExpr,
                     varAccess.getContainer());
    }

    public void testSwitchStatement3() throws ANTLRException
    {
        setupParser("switch ('a') {}");

        SwitchStatement result = (SwitchStatement)_parser.statement();

        assertNotNull(result);

        CharacterLiteral charLit = (CharacterLiteral)result.getSwitchExpression();

        assertNotNull(charLit);
        assertEquals("'a'",
                     charLit.asString());
        assertEquals(result,
                     charLit.getContainer());
    }

    public void testSwitchStatement4() throws ANTLRException
    {
        setupParser("switch ((var)) {}");

        SwitchStatement result = (SwitchStatement)_parser.statement();

        assertNotNull(result);

        ParenthesizedExpression parenExpr = (ParenthesizedExpression)result.getSwitchExpression();

        assertNotNull(parenExpr);
        assertEquals(result,
                     parenExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)parenExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals(parenExpr,
                     fieldAccess.getContainer());
    }

    public void testSwitchStatement5() throws ANTLRException
    {
        setupParser("switch (var) {}");

        SwitchStatement result = (SwitchStatement)_parser.statement();

        assertNotNull(result);

        FieldAccess fieldAccess = (FieldAccess)result.getSwitchExpression();

        assertNotNull(fieldAccess);
        assertEquals(result,
                     fieldAccess.getContainer());
    }

    public void testSwitchStatement6() throws ANTLRException
    {
        setupParser("switch (getValue()) {}");

        SwitchStatement result = (SwitchStatement)_parser.statement();

        assertNotNull(result);

        MethodInvocation methodInvoc = (MethodInvocation)result.getSwitchExpression();

        assertNotNull(methodInvoc);
        assertEquals("getValue",
                     methodInvoc.getMethodName());
        assertEquals(result,
                     methodInvoc.getContainer());
    }

    public void testSwitchStatement7() throws ANTLRException
    {
        setupParser("switch (arr[2]) {}");

        SwitchStatement result = (SwitchStatement)_parser.statement();

        assertNotNull(result);

        ArrayAccess arrayAccess = (ArrayAccess)result.getSwitchExpression();

        assertNotNull(arrayAccess);
        assertEquals(result,
                     arrayAccess.getContainer());
    }

    public void testSwitchStatement8() throws ANTLRException
    {
        setupParser("switch (var--) {}");

        SwitchStatement result = (SwitchStatement)_parser.statement();

        assertNotNull(result);

        PostfixExpression postfixExpr = (PostfixExpression)result.getSwitchExpression();

        assertNotNull(postfixExpr);
        assertEquals(result,
                     postfixExpr.getContainer());
    }

    public void testSwitchStatement9() throws ANTLRException
    {
        setupParser("switch (++var) {}");

        SwitchStatement result = (SwitchStatement)_parser.statement();

        assertNotNull(result);

        UnaryExpression unaryExpr = (UnaryExpression)result.getSwitchExpression();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.INCREMENT_OP,
                     unaryExpr.getOperator());
        assertEquals(result,
                     unaryExpr.getContainer());
    }
}
