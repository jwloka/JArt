package jast.test.parser;
import jast.parser.VariableDeclaratorId;
import antlr.ANTLRException;

public class TestVariableDeclaratorId extends TestBase
{

    public TestVariableDeclaratorId(String name)
    {
        super(name);
    }

    public void testVarDeclId1() throws ANTLRException
    {
        setupParser("var");

        VariableDeclaratorId result = _parser.variableDeclaratorId();

        assertNotNull(result);
        assertEquals("var",
                     result.getName());
        assertEquals(0,
                     result.getDimensions());
    }

    public void testVarDeclId2() throws ANTLRException
    {
        setupParser("arr[]");

        VariableDeclaratorId result = _parser.variableDeclaratorId();

        assertNotNull(result);
        assertEquals("arr",
                     result.getName());
        assertEquals(1,
                     result.getDimensions());
    }

    public void testVarDeclId3() throws ANTLRException
    {
        setupParser("arr[][][]");

        VariableDeclaratorId result = _parser.variableDeclaratorId();

        assertNotNull(result);
        assertEquals("arr",
                     result.getName());
        assertEquals(3,
                     result.getDimensions());
    }

    public void testVarDeclId4()
    {
        setupParser("[]");
        try
        {
            _parser.variableDeclaratorId();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testVarDeclId5()
    {
        setupParser("var[][");
        try
        {
            _parser.variableDeclaratorId();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }
}
