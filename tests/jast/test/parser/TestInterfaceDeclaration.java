package jast.test.parser;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.TypeDeclaration;
import antlr.ANTLRException;

public class TestInterfaceDeclaration extends TestBase
{

    public TestInterfaceDeclaration(String name)
    {
        super(name);
    }

    public void testInterfaceDeclaration1() throws ANTLRException
    {
        setupParser("interface Test {}");

        InterfaceDeclaration result = _parser.interfaceDeclaration();

        assertNotNull(result);
        assertEquals(0,
                     result.getBaseInterfaces().getCount());
        assertEquals("Test",
                     result.getName());
        assertTrue(!result.isInner());

        Modifiers mods = result.getModifiers();

        assertTrue(mods.isAbstract());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isStrictfp());

        assertEquals(0,
                     result.getFields().getCount());
        assertEquals(0,
                     result.getMethods().getCount());
        assertEquals(0,
                     result.getInnerTypes().getCount());
    }

    public void testInterfaceDeclaration10() throws ANTLRException
    {
        setupParser("interface TestInterface {"+
                    "  private abstract class TestClass {}"+
                    "  private static interface TestInterface {}"+
                    "}");

        InterfaceDeclaration result = _parser.interfaceDeclaration();

        assertNotNull(result);
        assertEquals(0,
                     result.getBaseInterfaces().getCount());
        assertEquals("TestInterface",
                     result.getName());
        assertTrue(!result.isInner());

        Modifiers mods = result.getModifiers();

        assertTrue(mods.isAbstract());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isStrictfp());

        assertEquals(0,
                     result.getFields().getCount());
        assertEquals(0,
                     result.getMethods().getCount());
        assertEquals(2,
                     result.getInnerTypes().getCount());

        TypeDeclaration decl = result.getInnerTypes().get(0);

        assertNotNull(decl);
        assertTrue(decl instanceof ClassDeclaration);
        assertEquals("TestClass",
                     decl.getName());
        assertTrue(decl.isInner());
        assertEquals(result,
                     decl.getEnclosingType());
        assertEquals(result,
                     decl.getContainer());

        decl = result.getInnerTypes().get(1);

        assertNotNull(decl);
        assertTrue(decl instanceof InterfaceDeclaration);
        assertEquals("TestInterface",
                     decl.getName());
        assertTrue(!decl.isInner());
        assertTrue(decl.isNested());
        assertEquals(result,
                     decl.getEnclosingType());
        assertEquals(result,
                     decl.getContainer());
    }

    public void testInterfaceDeclaration2() throws ANTLRException
    {
        setupParser("public interface SimpleInterface extends SomeInterface {"+
                    "  int attr;"+
                    "  void doSomething();"+
                    "}");

        InterfaceDeclaration result = _parser.interfaceDeclaration();

        assertNotNull(result);
        assertEquals("SimpleInterface",
                     result.getName());
        assertEquals(1,
                     result.getBaseInterfaces().getCount());
        assertEquals("SomeInterface",
                     result.getBaseInterfaces().get(0).getBaseName());
        assertTrue(!result.isInner());

        Modifiers mods = result.getModifiers();

        assertTrue(mods.isAbstract());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isStrictfp());

        assertEquals(1,
                     result.getFields().getCount());
        assertEquals(1,
                     result.getMethods().getCount());
        assertEquals(0,
                     result.getInnerTypes().getCount());

        FieldDeclaration fieldDecl = result.getFields().get(0);

        assertEquals("int",
                     fieldDecl.getType().getBaseName());
        assertEquals("attr",
                     fieldDecl.getName());
        assertEquals(result,
                     fieldDecl.getContainer());

        mods = fieldDecl.getModifiers();

        assertTrue(mods.isPublic());
        assertTrue(mods.isStatic());
        assertTrue(mods.isFinal());

        MethodDeclaration methodDecl = result.getMethods().get(0);

        assertNull(methodDecl.getReturnType());
        assertEquals("doSomething",
                     methodDecl.getName());
        assertEquals(result,
                     methodDecl.getContainer());

        mods = methodDecl.getModifiers();

        assertTrue(mods.isAbstract());
        assertTrue(mods.isPublic());
    }

    public void testInterfaceDeclaration3() throws ANTLRException
    {
        setupParser("private abstract strictfp interface SimpleInterface extends AnotherInterface {"+
                    "  public int attr = 0;"+
                    "  static String attr2 = String.valueOf(2);"+
                    "  final boolean[] arr;"+
                    "}");

        InterfaceDeclaration result = _parser.interfaceDeclaration();

        assertNotNull(result);
        assertEquals("SimpleInterface",
                     result.getName());
        assertEquals(1,
                     result.getBaseInterfaces().getCount());
        assertEquals("AnotherInterface",
                     result.getBaseInterfaces().get(0).getBaseName());
        assertTrue(!result.isInner());

        Modifiers mods = result.getModifiers();

        assertTrue(mods.isAbstract());
        assertTrue(mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(mods.isStrictfp());

        assertEquals(3,
                     result.getFields().getCount());
        assertEquals(0,
                     result.getMethods().getCount());
        assertEquals(0,
                     result.getInnerTypes().getCount());

        FieldDeclaration fieldDecl = result.getFields().get(0);

        assertEquals("int",
                     fieldDecl.getType().getBaseName());
        assertEquals("attr",
                     fieldDecl.getName());
        assertEquals(result,
                     fieldDecl.getContainer());

        mods = fieldDecl.getModifiers();

        assertTrue(mods.isPublic());
        assertTrue(mods.isStatic());
        assertTrue(mods.isFinal());

        fieldDecl = result.getFields().get(1);

        assertEquals("String",
                     fieldDecl.getType().getBaseName());
        assertEquals("attr2",
                     fieldDecl.getName());
        assertEquals(result,
                     fieldDecl.getContainer());

        mods = fieldDecl.getModifiers();

        assertTrue(mods.isPublic());
        assertTrue(mods.isStatic());
        assertTrue(mods.isFinal());

        fieldDecl = result.getFields().get(2);

        assertEquals("boolean",
                     fieldDecl.getType().getBaseName());
        assertEquals("arr",
                     fieldDecl.getName());
        assertEquals(result,
                     fieldDecl.getContainer());

        mods = fieldDecl.getModifiers();

        assertTrue(mods.isPublic());
        assertTrue(mods.isStatic());
        assertTrue(mods.isFinal());
    }

    public void testInterfaceDeclaration4() throws ANTLRException
    {
        setupParser("protected interface SimpleInterface extends InterfaceA, InterfaceB {"+
                    "  abstract void doSomething(String value);"+
                    "  public String toString();"+
                    "}");

        InterfaceDeclaration result = _parser.interfaceDeclaration();

        assertNotNull(result);
        assertEquals("SimpleInterface",
                     result.getName());
        assertEquals(2,
                     result.getBaseInterfaces().getCount());
        assertEquals("InterfaceA",
                     result.getBaseInterfaces().get(0).getBaseName());
        assertEquals("InterfaceB",
                     result.getBaseInterfaces().get(1).getBaseName());
        assertTrue(!result.isInner());

        Modifiers mods = result.getModifiers();

        assertTrue(mods.isAbstract());
        assertTrue(!mods.isPrivate());
        assertTrue(mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isStrictfp());

        assertEquals(0,
                     result.getFields().getCount());
        assertEquals(2,
                     result.getMethods().getCount());
        assertEquals(0,
                     result.getInnerTypes().getCount());

        MethodDeclaration methodDecl = result.getMethods().get(0);

        assertNull(methodDecl.getReturnType());
        assertEquals("doSomething",
                     methodDecl.getName());
        assertEquals(result,
                     methodDecl.getContainer());

        mods = methodDecl.getModifiers();

        assertTrue(mods.isAbstract());
        assertTrue(mods.isPublic());

        methodDecl = result.getMethods().get(1);

        assertEquals("String",
                     methodDecl.getReturnType().getBaseName());
        assertEquals("toString",
                     methodDecl.getName());
        assertEquals(result,
                     methodDecl.getContainer());

        mods = methodDecl.getModifiers();

        assertTrue(mods.isAbstract());
        assertTrue(mods.isPublic());
    }

    public void testInterfaceDeclaration5()
    {
        setupParser("interface Test;");
        try
        {
            _parser.interfaceDeclaration();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testInterfaceDeclaration6()
    {
        setupParser("interface {}");
        try
        {
            _parser.interfaceDeclaration();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testInterfaceDeclaration7()
    {
        setupParser("interface Test { int attr; static { attr = 1; } }");
        try
        {
            _parser.interfaceDeclaration();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testInterfaceDeclaration8()
    {
        setupParser("interface Test { Test(); }");
        try
        {
            _parser.interfaceDeclaration();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testInterfaceDeclaration9()
    {
        setupParser("interface Test { Test() {} }");
        try
        {
            _parser.interfaceDeclaration();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }
}
