package jast.test.parser;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.TryStatement;
import antlr.ANTLRException;

public class TestTryStatement extends TestBase
{

    public TestTryStatement(String name)
    {
        super(name);
    }

    public void testTryStatement1() throws ANTLRException
    {
        setupParser("try { doSomething(); } catch (final Exception ex) { return; }");

        TryStatement result = (TryStatement)_parser.statement();

        assertNotNull(result);
        assertEquals(1,
                     result.getTryBlock().getBlockStatements().getCount());
        assertNull(result.getFinallyClause());
        assertEquals(1,
                     result.getCatchClauses().getCount());
        assertEquals(1,
                     result.getCatchClauses().get(0).getCatchBlock().getBlockStatements().getCount());

        FormalParameter param = result.getCatchClauses().get(0).getFormalParameter();

        assertNotNull(param);
        assertTrue(param.getModifiers().isFinal());
        assertEquals("Exception",
                     param.getType().getBaseName());
        assertEquals("ex",
                     param.getName());
        assertEquals(result.getCatchClauses().get(0),
                     param.getContainer());
    }

    public void testTryStatement10()
    {
        setupParser("try finally {} catch (Exception ex) {}");
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

    public void testTryStatement11()
    {
        setupParser("try finally {} finally {}");
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

    public void testTryStatement12()
    {
        setupParser("catch (Exception ex) {}");
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

    public void testTryStatement13()
    {
        setupParser("finally {}");
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

    public void testTryStatement14()
    {
        setupParser("try {} finally ;");
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

    public void testTryStatement2() throws ANTLRException
    {
        setupParser("try {} catch (NullPointerException nullEx) {} catch (final Exception ex) {}");

        TryStatement result = (TryStatement)_parser.statement();

        assertNotNull(result);
        assertEquals(0,
                     result.getTryBlock().getBlockStatements().getCount());
        assertNull(result.getFinallyClause());
        assertEquals(2,
                     result.getCatchClauses().getCount());
        assertEquals(0,
                     result.getCatchClauses().get(0).getCatchBlock().getBlockStatements().getCount());
        assertEquals(0,
                     result.getCatchClauses().get(1).getCatchBlock().getBlockStatements().getCount());

        FormalParameter param = result.getCatchClauses().get(0).getFormalParameter();

        assertNotNull(param);
        assertTrue(!param.getModifiers().isFinal());
        assertEquals("NullPointerException",
                     param.getType().getBaseName());
        assertEquals("nullEx",
                     param.getName());
        assertEquals(result.getCatchClauses().get(0),
                     param.getContainer());

        param = result.getCatchClauses().get(1).getFormalParameter();

        assertNotNull(param);
        assertTrue(param.getModifiers().isFinal());
        assertEquals("Exception",
                     param.getType().getBaseName());
        assertEquals("ex",
                     param.getName());
        assertEquals(result.getCatchClauses().get(1),
                     param.getContainer());
    }

    public void testTryStatement3() throws ANTLRException
    {
        setupParser("try {} finally { return; }");

        TryStatement result = (TryStatement)_parser.statement();

        assertNotNull(result);
        assertEquals(0,
                     result.getTryBlock().getBlockStatements().getCount());
        assertEquals(0,
                     result.getCatchClauses().getCount());
        assertNotNull(result.getFinallyClause());
        assertEquals(1,
                     result.getFinallyClause().getBlockStatements().getCount());
    }

    public void testTryStatement4() throws ANTLRException
    {
        setupParser("try {} catch (final NullPointerException nullEx) {}" +
                    "catch (IOException ioEx) {} finally { return; }");

        TryStatement result = (TryStatement)_parser.statement();

        assertNotNull(result);
        assertEquals(0,
                     result.getTryBlock().getBlockStatements().getCount());
        assertEquals(2,
                     result.getCatchClauses().getCount());
        assertNotNull(result.getFinallyClause());
        assertEquals(0,
                     result.getCatchClauses().get(0).getCatchBlock().getBlockStatements().getCount());
        assertEquals(0,
                     result.getCatchClauses().get(1).getCatchBlock().getBlockStatements().getCount());

        FormalParameter param = result.getCatchClauses().get(0).getFormalParameter();

        assertNotNull(param);
        assertTrue(param.getModifiers().isFinal());
        assertEquals("NullPointerException",
                     param.getType().getBaseName());
        assertEquals("nullEx",
                     param.getName());
        assertEquals(result.getCatchClauses().get(0),
                     param.getContainer());

        param = result.getCatchClauses().get(1).getFormalParameter();

        assertNotNull(param);
        assertTrue(!param.getModifiers().isFinal());
        assertEquals("IOException",
                     param.getType().getBaseName());
        assertEquals("ioEx",
                     param.getName());
        assertEquals(1,
                     result.getFinallyClause().getBlockStatements().getCount());
        assertEquals(result.getCatchClauses().get(1),
                     param.getContainer());
    }

    public void testTryStatement5()
    {
        setupParser("try catch (Exception ex) {}");
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

    public void testTryStatement6()
    {
        setupParser("try {}");
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

    public void testTryStatement7()
    {
        setupParser("try ; catch (Exception ex) {}");
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

    public void testTryStatement8()
    {
        setupParser("try finally {}");
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

    public void testTryStatement9()
    {
        setupParser("try {} finally (Exception ex) {}");
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
