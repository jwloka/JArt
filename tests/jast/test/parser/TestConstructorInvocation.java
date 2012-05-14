package jast.test.parser;
import jast.ast.nodes.ArgumentList;
import jast.ast.nodes.ConstructorInvocation;
import antlr.ANTLRException;

public class TestConstructorInvocation extends TestBase
{

    public TestConstructorInvocation(String name)
    {
        super(name);
    }

    public void testConstructorInvocation1() throws ANTLRException
    {
        setupParser("this()");

        ConstructorInvocation result = (ConstructorInvocation)_parser.primary();

        assertNotNull(result);
        assertTrue(!result.ofBaseClass());
        assertTrue(!result.isTrailing());
        assertNull(result.getArgumentList());
    }

    public void testConstructorInvocation2() throws ANTLRException
    {
        setupParser("super()");

        ConstructorInvocation result = (ConstructorInvocation)_parser.primary();

        assertNotNull(result);
        assertTrue(result.ofBaseClass());
        assertTrue(!result.isTrailing());
        assertNull(result.getArgumentList());
    }

    public void testConstructorInvocation3() throws ANTLRException
    {
        setupParser("(new Outer()).super()");

        ConstructorInvocation result = (ConstructorInvocation)_parser.primary();

        assertNotNull(result);
        assertTrue(result.ofBaseClass());
        assertTrue(result.isTrailing());
        assertNull(result.getArgumentList());
    }

    public void testConstructorInvocation4() throws ANTLRException
    {
        setupParser("this(0, true)");

        ConstructorInvocation result = (ConstructorInvocation)_parser.primary();

        assertNotNull(result);
        assertTrue(!result.ofBaseClass());
        assertTrue(!result.isTrailing());

        ArgumentList args = result.getArgumentList();

        assertNotNull(args);
        assertEquals(2,
                     args.getArguments().getCount());
        assertEquals(result,
                     args.getContainer());
    }

    public void testConstructorInvocation5()
    {
        setupParser("(new Outer()).this()");
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
