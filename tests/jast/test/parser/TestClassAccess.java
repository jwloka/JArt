package jast.test.parser;
import jast.ast.nodes.ClassAccess;
import jast.ast.nodes.Type;
import antlr.ANTLRException;

public class TestClassAccess extends TestBase
{

    public TestClassAccess(String name)
    {
        super(name);
    }

    public void testClassAccess1() throws ANTLRException
    {
        setupParser("void.class");

        ClassAccess result = (ClassAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(!result.isQualified());
        assertNull(result.getReferencedType());
    }

    public void testClassAccess2() throws ANTLRException
    {
        setupParser("boolean.class");

        ClassAccess result = (ClassAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isQualified());

        Type type = result.getReferencedType();

        assertTrue(type.isPrimitive());
        assertEquals("boolean",
                     type.getBaseName());
        assertEquals(0,
                     type.getDimensions());
    }

    public void testClassAccess3() throws ANTLRException
    {
        setupParser("Object.class");

        ClassAccess result = (ClassAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isQualified());

        Type type = result.getReferencedType();

        assertTrue(!type.isPrimitive());
        assertEquals("Object",
                     type.getBaseName());
        assertEquals(0,
                     type.getDimensions());
    }

    public void testClassAccess4() throws ANTLRException
    {
        setupParser("int[][].class");

        ClassAccess result = (ClassAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isQualified());

        Type type = result.getReferencedType();

        assertTrue(type.isPrimitive());
        assertEquals("int",
                     type.getBaseName());
        assertEquals(2,
                     type.getDimensions());
    }

    public void testClassAccess5() throws ANTLRException
    {
        setupParser("java.lang.String[].class");

        ClassAccess result = (ClassAccess)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isQualified());

        Type type = result.getReferencedType();

        assertTrue(!type.isPrimitive());
        assertEquals("java.lang.String",
                     type.getBaseName());
        assertEquals(1,
                     type.getDimensions());
    }

    public void testClassAccess6()
    {
        setupParser("void[].class");
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
