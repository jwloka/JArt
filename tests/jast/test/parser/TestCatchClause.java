package jast.test.parser;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.Block;
import jast.ast.nodes.CatchClause;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.VariableAccess;
import jast.parser.MultipleVariableDeclaration;
import antlr.ANTLRException;

public class TestCatchClause extends TestBase
{

    public TestCatchClause(String name)
    {
        super(name);
    }

    public void testCatchClause1() throws ANTLRException
    {
        setupParser("catch (Exception ex) {}");

        CatchClause clause = _parser.catchClause();

        assertNotNull(clause);

        FormalParameter param = clause.getFormalParameter();

        assertNotNull(param);
        assertTrue(!param.getModifiers().isFinal());
        assertEquals(0,
                     param.getType().getDimensions());
        assertEquals("Exception",
                     param.getType().getBaseName());
        assertEquals("ex",
                     param.getName());
        assertEquals(clause,
                     param.getContainer());

        Block block = clause.getCatchBlock();

        assertNotNull(block);
        assertEquals(0,
                     block.getBlockStatements().getCount());
        assertEquals(clause,
                     block.getContainer());
    }

    public void testCatchClause10()
    {
        setupParser("catch {}");
        try
        {
            _parser.catchClause();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testCatchClause11()
    {
        setupParser("catch (ex) {}");
        try
        {
            _parser.catchClause();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testCatchClause12()
    {
        setupParser("catch Exception ex {}");
        try
        {
            _parser.catchClause();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testCatchClause13() throws ANTLRException
    {
        setupParser("catch (Exception ex) { storedEx = ex; }");

        CatchClause clause = _parser.catchClause();

        assertNotNull(clause);

        FormalParameter param = clause.getFormalParameter();

        assertNotNull(param);
        assertTrue(!param.getModifiers().isFinal());
        assertEquals("Exception",
                     param.getType().getBaseName());
        assertEquals("ex",
                     param.getName());
        assertEquals(clause,
                     param.getContainer());

        Block block = clause.getCatchBlock();

        assertNotNull(block);
        assertEquals(1,
                     block.getBlockStatements().getCount());
        assertEquals(clause,
                     block.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)block.getBlockStatements().get(0);

        assertNotNull(exprStmt);
        assertEquals(block,
                     exprStmt.getContainer());

        AssignmentExpression assignExpr = (AssignmentExpression)exprStmt.getExpression();

        assertNotNull(assignExpr);
        assertEquals(exprStmt,
                     assignExpr.getContainer());

        VariableAccess varAccess = (VariableAccess)assignExpr.getValueExpression();

        assertNotNull(varAccess);
        assertEquals(param,
                     varAccess.getVariableDeclaration());
        assertEquals(assignExpr,
                     varAccess.getContainer());
    }

    public void testCatchClause14() throws ANTLRException
    {
        setupParser("catch (Exception ex) { } Object ex;");

        _parser.catchClause();

        MultipleVariableDeclaration localVar = (MultipleVariableDeclaration)_parser.blockStatement();

        assertNotNull(localVar);
        assertEquals("Object",
                     localVar.getDeclarators().get(0).getType().getBaseName());
    }

    public void testCatchClause2() throws ANTLRException
    {
        setupParser("catch (final NullPointerException nullEx) { return; }");

        CatchClause clause = _parser.catchClause();

        assertNotNull(clause);

        FormalParameter param = clause.getFormalParameter();

        assertNotNull(param);
        assertTrue(param.getModifiers().isFinal());
        assertEquals(0,
                     param.getType().getDimensions());
        assertEquals("NullPointerException",
                     param.getType().getBaseName());
        assertEquals("nullEx",
                     param.getName());
        assertEquals(clause,
                     param.getContainer());

        Block block = clause.getCatchBlock();

        assertNotNull(block);
        assertEquals(1,
                     block.getBlockStatements().getCount());
        assertEquals(clause,
                     block.getContainer());
    }

    public void testCatchClause3()
    {
        setupParser("catch () {}");
        try
        {
            _parser.catchClause();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testCatchClause4()
    {
        setupParser("catch (Exception ex);");
        try
        {
            _parser.catchClause();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testCatchClause5()
    {
        setupParser("catch (Exception ex)");
        try
        {
            _parser.catchClause();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testCatchClause6()
    {
        setupParser("catch (int a) {}");
        try
        {
            _parser.catchClause();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testCatchClause7()
    {
        setupParser("catch (Exception[] ex) {}");
        try
        {
            _parser.catchClause();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testCatchClause8()
    {
        setupParser("catch (Exception ex[]) {}");
        try
        {
            _parser.catchClause();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testCatchClause9()
    {
        setupParser("catch (int[] ex) {}");
        try
        {
            _parser.catchClause();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }
}
