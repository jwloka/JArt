package jast.test.parser;
import jast.Global;
import jast.ast.nodes.ArrayInitializer;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.SingleInitializer;
import jast.ast.nodes.VariableDeclaration;
import antlr.ANTLRException;

public class TestVariableDeclarator extends TestBase
{

    public TestVariableDeclarator(String name)
    {
        super(name);
    }

    public void testVariableDeclarator1() throws ANTLRException
    {
        setupParser("idx");

        VariableDeclaration result = _parser.variableDeclarator(Global.getFactory().createModifiers(),
                                                                Global.getFactory().createType("String", 0),
                                                                false);

        assertNotNull(result);
        assertNull(result.getInitializer());
        assertEquals("String",
                     result.getType().getBaseName());
        assertEquals(0,
                     result.getType().getDimensions());
        assertEquals("idx",
                     result.getName());

        Modifiers mods = result.getModifiers();

        assertTrue(!mods.isFinal());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isTransient());
        assertTrue(!mods.isVolatile());
    }

    public void testVariableDeclarator2() throws ANTLRException
    {
        setupParser("idx = 0");

        VariableDeclaration result = _parser.variableDeclarator(Global.getFactory().createModifiers(),
                                                                Global.getFactory().createType("int", 0),
                                                                false);

        assertNotNull(result);
        assertEquals("int",
                     result.getType().getBaseName());
        assertEquals(0,
                     result.getType().getDimensions());
        assertEquals("idx",
                     result.getName());

        SingleInitializer init = (SingleInitializer)result.getInitializer();

        assertNotNull(init);
        assertEquals(result,
                     init.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)init.getInitEpression();

        assertNotNull(intLit);
        assertEquals("0",
                     intLit.asString());
        assertEquals(init,
                     intLit.getContainer());
    }

    public void testVariableDeclarator3() throws ANTLRException
    {
        setupParser("arr[] = { 0, 1 }");

        VariableDeclaration result = _parser.variableDeclarator(Global.getFactory().createModifiers(),
                                                                Global.getFactory().createType("int", 0),
                                                                false);

        assertNotNull(result);
        assertEquals("int",
                     result.getType().getBaseName());
        assertEquals(1,
                     result.getType().getDimensions());
        assertEquals("arr",
                     result.getName());

        ArrayInitializer initializer = (ArrayInitializer)result.getInitializer();

        assertNotNull(initializer);
        assertEquals(2,
                     initializer.getInitializers().getCount());
        assertEquals(result,
                     initializer.getContainer());
    }

    public void testVariableDeclarator4()
    {
        setupParser("arr =");
        try
        {
            _parser.variableDeclarator(Global.getFactory().createModifiers(),
                                       Global.getFactory().createType("int", 0),
                                       false);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }
}
