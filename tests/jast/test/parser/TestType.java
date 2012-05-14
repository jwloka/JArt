package jast.test.parser;
import jast.ast.nodes.Type;
import antlr.ANTLRException;

public class TestType extends TestBase
{

    public TestType(String name)
    {
        super(name);
    }

    public void testPrimitiveType1() throws ANTLRException
    {
        setupParser("boolean");

        Type result = _parser.type();

        assertNotNull(result);
        assertTrue(result.isPrimitive());
        assertEquals("boolean",
                     result.getBaseName());
        assertEquals(0,
                     result.getDimensions());
    }

    public void testPrimitiveType2() throws ANTLRException
    {
        setupParser("int[]");

        Type result = _parser.type();

        assertNotNull(result);
        assertTrue(result.isPrimitive());
        assertEquals("int",
                     result.getBaseName());
        assertEquals(1,
                     result.getDimensions());
    }

    public void testPrimitiveType3() throws ANTLRException
    {
        setupParser("char[][][]");

        Type result = _parser.type();

        assertNotNull(result);
        assertTrue(result.isPrimitive());
        assertEquals("char",
                     result.getBaseName());
        assertEquals(3,
                     result.getDimensions());
    }

    public void testReferenceType1() throws ANTLRException
    {
        setupParser("Object");

        Type result = _parser.type();

        assertNotNull(result);
        assertTrue(!result.isPrimitive());
        assertEquals("Object",
                     result.getBaseName());
        assertEquals(0,
                     result.getDimensions());
    }

    public void testReferenceType2() throws ANTLRException
    {
        setupParser("java.lang.String[]");

        Type result = _parser.type();

        assertNotNull(result);
        assertTrue(!result.isPrimitive());
        assertEquals("java.lang.String",
                     result.getBaseName());
        assertEquals(1,
                     result.getDimensions());
    }

    public void testReferenceType3() throws ANTLRException
    {
        setupParser("Boolean[][][]");

        Type result = _parser.type();

        assertNotNull(result);
        assertTrue(!result.isPrimitive());
        assertEquals("Boolean",
                     result.getBaseName());
        assertEquals(3,
                     result.getDimensions());
    }

    public void testVoid1()
    {
        setupParser("void");
        try
        {
            _parser.type();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testVoid2()
    {
        setupParser("void[][]");
        try
        {
            _parser.type();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }
}
