package jast.test.parser;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.FormalParameterList;
import jast.ast.nodes.Type;
import antlr.ANTLRException;

public class TestFormalParameter extends TestBase
{

    public TestFormalParameter(String name)
    {
        super(name);
    }

    public void testFormalParameter1() throws ANTLRException
    {
        setupParser("int var");

        FormalParameter result = _parser.formalParameter(true);

        assertTrue(result != null);
        assertTrue(!result.getModifiers().isFinal());

        Type type = result.getType();

        assertTrue(type != null);
        assertTrue(type.isPrimitive());
        assertEquals(0,
                     type.getDimensions());
        assertEquals("int",
                     type.getBaseName());
        assertEquals(0,
                     type.getDimensions());
        assertEquals("var",
                     result.getName());
    }

    public void testFormalParameter2() throws ANTLRException
    {
        setupParser("final String[] arr");

        FormalParameter result = _parser.formalParameter(true);

        assertTrue(result != null);
        assertTrue(result.getModifiers().isFinal());

        Type type = result.getType();

        assertTrue(type != null);
        assertTrue(!type.isPrimitive());
        assertEquals(1,
                     type.getDimensions());
        assertEquals("String",
                     type.getBaseName());
        assertEquals("arr",
                     result.getName());
    }

    public void testFormalParameter3() throws ANTLRException
    {
        setupParser("String arr[][]");

        FormalParameter result = _parser.formalParameter(true);

        assertTrue(result != null);
        assertTrue(!result.getModifiers().isFinal());

        Type type = result.getType();

        assertTrue(type != null);
        assertTrue(!type.isPrimitive());
        assertEquals(2,
                     type.getDimensions());
        assertEquals("String",
                     type.getBaseName());
        assertEquals("arr",
                     result.getName());
    }

    public void testFormalParameter4() throws ANTLRException
    {
        setupParser("final boolean[] arr[]");

        FormalParameter result = _parser.formalParameter(true);

        assertTrue(result != null);
        assertTrue(result.getModifiers().isFinal());

        Type type = result.getType();

        assertTrue(type != null);
        assertTrue(type.isPrimitive());
        assertEquals(2,
                     type.getDimensions());
        assertEquals("boolean",
                     type.getBaseName());
        assertEquals("arr",
                     result.getName());
    }

    public void testFormalParameter5()
    {
        setupParser("final boolean");
        try
        {
            _parser.formalParameter(true);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testFormalParameter6()
    {
        setupParser("final var");
        try
        {
            _parser.formalParameter(true);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testFormalParameter7()
    {
        setupParser("int[3] var");
        try
        {
            _parser.formalParameter(true);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testFormalParameter8()
    {
        setupParser("int var[4]");
        try
        {
            _parser.formalParameter(true);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testFormalParameterList1() throws ANTLRException
    {
        setupParser("int var, String[] name, java.lang.Object other[]");

        FormalParameterList result = _parser.formalParameterList(true);

        assertTrue(result != null);
        assertEquals(3,
                     result.getParameters().getCount());

        FormalParameter param = result.getParameters().get(0);

        assertTrue(param != null);
        assertTrue(!param.getModifiers().isFinal());
        assertEquals("int",
                     param.getType().getBaseName());
        assertEquals(0,
                     param.getType().getDimensions());
        assertEquals("var",
                     param.getName());
        assertEquals(result,
                     param.getContainer());

        param = result.getParameters().get(1);

        assertTrue(param != null);
        assertTrue(!param.getModifiers().isFinal());
        assertEquals("String",
                     param.getType().getBaseName());
        assertEquals(1,
                     param.getType().getDimensions());
        assertEquals("name",
                     param.getName());
        assertEquals(result,
                     param.getContainer());

        param = result.getParameters().get(2);

        assertTrue(param != null);
        assertTrue(!param.getModifiers().isFinal());
        assertEquals("java.lang.Object",
                     param.getType().getBaseName());
        assertEquals(1,
                     param.getType().getDimensions());
        assertEquals("other",
                     param.getName());
        assertEquals(result,
                     param.getContainer());
    }

    public void testFormalParameterList2()
    {
        setupParser("int var, value");
        try
        {
            _parser.formalParameterList(true);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testFormalParameterList3()
    {
        setupParser("int var, int");
        try
        {
            _parser.formalParameterList(true);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }
}
