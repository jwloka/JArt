package jast.test.parser;
import jast.ast.nodes.SelfAccess;
import jast.ast.nodes.TypeAccess;
import antlr.ANTLRException;

public class TestSelfAccess extends TestBase
{

    public TestSelfAccess(String name)
    {
        super(name);
    }

    public void testSelfAccess1() throws ANTLRException
    {
        setupParser("this");

        SelfAccess result = (SelfAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(!result.isQualified());
        assertTrue(!result.isSuper());
        assertNull(result.getTypeAccess());
    }

    public void testSelfAccess10()
    {
        setupParser("super");
        try
        {
            _parser.primary();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testSelfAccess11()
    {
        setupParser("Object.super");
        try
        {
            _parser.primary();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testSelfAccess2() throws ANTLRException
    {
        setupParser("Object.this");

        SelfAccess result = (SelfAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(!result.isSuper());
        assertTrue(result.isQualified());

        TypeAccess typeAccess = result.getTypeAccess();

        assertNotNull(typeAccess);
        assertEquals("Object",
                     typeAccess.getType().getBaseName());
        assertEquals(result,
                     typeAccess.getContainer());
    }

    public void testSelfAccess3() throws ANTLRException
    {
        setupParser("java.lang.String.this");

        SelfAccess result = (SelfAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(!result.isSuper());
        assertTrue(result.isQualified());

        TypeAccess typeAccess = result.getTypeAccess();

        assertNotNull(typeAccess);
        assertEquals("java.lang.String",
                     typeAccess.getType().getBaseName());
        assertEquals(result,
                     typeAccess.getContainer());
    }

    public void testSelfAccess4()
    {
        setupParser("boolean.this");
        try
        {
            _parser.primary();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testSelfAccess5()
    {
        setupParser("Object[].this");
        try
        {
            _parser.primary();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testSelfAccess6()
    {
        setupParser("int[][].this");
        try
        {
            _parser.primary();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testSelfAccess7()
    {
        setupParser("void.this");
        try
        {
            _parser.primary();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testSelfAccess8()
    {
        setupParser("void[].this");
        try
        {
            _parser.primary();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testSelfAccess9()
    {
        setupParser("super");
        try
        {
            _parser.primary();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }
}
