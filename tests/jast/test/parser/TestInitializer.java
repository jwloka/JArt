package jast.test.parser;
import jast.ast.nodes.Block;
import jast.ast.nodes.Initializer;
import antlr.ANTLRException;

public class TestInitializer extends TestBase
{

    public TestInitializer(String name)
    {
        super(name);
    }

    public void testInitializer1() throws ANTLRException
    {
        setupParser("{ attr = 0; }");

        Initializer result = (Initializer)_parser.classBodyDeclaration();

        assertNotNull(result);
        assertTrue(!result.isStatic());

        Block block = result.getBody();

        assertNotNull(block);
        assertEquals(1,
                     block.getBlockStatements().getCount());
        assertEquals(result,
                     block.getContainer());
    }

    public void testInitializer2() throws ANTLRException
    {
        setupParser("static {}");

        Initializer result = (Initializer)_parser.classBodyDeclaration();

        assertNotNull(result);
        assertTrue(result.isStatic());

        Block block = result.getBody();

        assertNotNull(block);
        assertEquals(0,
                     block.getBlockStatements().getCount());
        assertEquals(result,
                     block.getContainer());
    }

    public void testInitializer3()
    {
        setupParser("static final {}");
        try
        {
            _parser.classBodyDeclaration();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testInitializer4()
    {
        setupParser("final {}");
        try
        {
            _parser.classBodyDeclaration();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

}
