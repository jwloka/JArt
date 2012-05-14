package jast.test.parser;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.Initializer;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.Modifiers;
import antlr.ANTLRException;

public class TestClassDeclaration extends TestBase
{

    public TestClassDeclaration(String name)
    {
        super(name);
    }

    public void testClassDeclaration1() throws ANTLRException
    {
        setupParser("class Test {}");

        ClassDeclaration result = _parser.classDeclaration(false);

        assertNotNull(result);
        assertNull(result.getBaseClass());
        assertEquals(0,
                     result.getBaseInterfaces().getCount());
        assertEquals("Test",
                     result.getName());
        assertTrue(!result.isInner());
        assertTrue(!result.isLocal());
        assertEquals(0,
                     result.getFields().getCount());
        assertEquals(0,
                     result.getInitializers().getCount());
        assertEquals(0,
                     result.getConstructors().getCount());
        assertEquals(0,
                     result.getMethods().getCount());
        assertEquals(0,
                     result.getInnerTypes().getCount());

        Modifiers mods = result.getModifiers();

        assertTrue(!mods.isAbstract());
        assertTrue(!mods.isFinal());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isStrictfp());
    }

    public void testClassDeclaration2() throws ANTLRException
    {
        setupParser("public final class SimpleClass implements SimpleInterface {" +
                    "  private int attr;" +
                    "  { attr = 0; }" +
                    "  SimpleClass() {}" +
                    "  public void doSomething() {}" +
                    "}");

        ClassDeclaration result = _parser.classDeclaration(false);

        assertNotNull(result);
        assertNull(result.getBaseClass());
        assertEquals("SimpleClass",
                     result.getName());
        assertEquals(1,
                     result.getBaseInterfaces().getCount());
        assertEquals("SimpleInterface",
                     result.getBaseInterfaces().get(0).getBaseName());
        assertTrue(!result.isInner());
        assertTrue(!result.isLocal());

        Modifiers mods = result.getModifiers();

        assertTrue(!mods.isAbstract());
        assertTrue(mods.isFinal());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isStrictfp());

        assertEquals(1,
                     result.getFields().getCount());
        assertEquals(1,
                     result.getInitializers().getCount());
        assertEquals(1,
                     result.getConstructors().getCount());
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

        Initializer initializer = result.getInitializers().get(0);

        assertTrue(!initializer.isStatic());
        assertEquals(1,
                     initializer.getBody().getBlockStatements().getCount());
        assertEquals(result,
                     initializer.getContainer());

        MethodDeclaration methodDecl = result.getMethods().get(0);

        assertNull(methodDecl.getReturnType());
        assertEquals("doSomething()",
                     methodDecl.getSignature());
        assertEquals(result,
                     methodDecl.getContainer());
    }

    public void testClassDeclaration3() throws ANTLRException
    {
        setupParser("private abstract class SimpleClass extends AnotherClass {" +
                    "  int attr = 0;" +
                    "  static String attr2 = String.valueOf(2);" +
                    "  private boolean[] arr;" +
                    "  public abstract int getValue();\n"+
                    "}");

        ClassDeclaration result = _parser.classDeclaration(false);

        assertNotNull(result);
        assertEquals("SimpleClass",
                     result.getName());
        assertEquals(0,
                     result.getBaseInterfaces().getCount());
        assertEquals("AnotherClass",
                     result.getBaseClass().getBaseName());
        assertTrue(!result.isInner());
        assertTrue(!result.isLocal());
        assertEquals(3,
                     result.getFields().getCount());
        assertEquals(0,
                     result.getInitializers().getCount());
        assertEquals(0,
                     result.getConstructors().getCount());
        assertEquals(1,
                     result.getMethods().getCount());
        assertEquals(0,
                     result.getInnerTypes().getCount());

        Modifiers mods = result.getModifiers();

        assertTrue(mods.isAbstract());
        assertTrue(!mods.isFinal());
        assertTrue(mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isStrictfp());

        FieldDeclaration fieldDecl = result.getFields().get(0);

        assertEquals("int",
                     fieldDecl.getType().getBaseName());
        assertEquals("attr",
                     fieldDecl.getName());
        assertEquals(result,
                     fieldDecl.getContainer());

        fieldDecl = result.getFields().get(1);

        assertEquals("String",
                     fieldDecl.getType().getBaseName());
        assertEquals("attr2",
                     fieldDecl.getName());
        assertEquals(result,
                     fieldDecl.getContainer());

        fieldDecl = result.getFields().get(2);

        assertEquals("boolean",
                     fieldDecl.getType().getBaseName());
        assertEquals("arr",
                     fieldDecl.getName());
        assertEquals(result,
                     fieldDecl.getContainer());

        MethodDeclaration methodDecl = result.getMethods().get(0);

        assertEquals("int",
                     methodDecl.getReturnType().getBaseName());
        assertEquals("getValue()",
                     methodDecl.getSignature());
        assertEquals(result,
                     methodDecl.getContainer());
    }

    public void testClassDeclaration4() throws ANTLRException
    {
        setupParser("static class SimpleClass extends Object implements InterfaceA, InterfaceB {" +
                    "  SimpleClass() {}" +
                    "  SimpleClass(int value) {}" +
                    "  private void doSomething(String value) {}" +
                    "  public String toString() { return \"\"; }" +
                    "}");

        ClassDeclaration result = _parser.classDeclaration(false);

        assertNotNull(result);
        assertEquals("SimpleClass",
                     result.getName());
        assertEquals(2,
                     result.getBaseInterfaces().getCount());
        assertEquals("InterfaceA",
                     result.getBaseInterfaces().get(0).getBaseName());
        assertEquals("InterfaceB",
                     result.getBaseInterfaces().get(1).getBaseName());
        assertEquals("Object",
                     result.getBaseClass().getBaseName());
        assertTrue(!result.isInner());
        assertTrue(!result.isLocal());
        assertEquals(0,
                     result.getFields().getCount());
        assertEquals(0,
                     result.getInitializers().getCount());
        assertEquals(2,
                     result.getConstructors().getCount());
        assertEquals(2,
                     result.getMethods().getCount());
        assertEquals(0,
                     result.getInnerTypes().getCount());

        Modifiers mods = result.getModifiers();

        assertTrue(!mods.isAbstract());
        assertTrue(!mods.isFinal());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(mods.isStatic());
        assertTrue(!mods.isStrictfp());

        MethodDeclaration methodDecl = result.getMethods().get(0);

        assertNull(methodDecl.getReturnType());
        assertEquals("doSomething(String)",
                     methodDecl.getSignature());
        assertEquals(result,
                     methodDecl.getContainer());

        methodDecl = result.getMethods().get(1);

        assertEquals("String",
                     methodDecl.getReturnType().getBaseName());
        assertEquals("toString()",
                     methodDecl.getSignature());
        assertEquals(result,
                     methodDecl.getContainer());
    }

    public void testClassDeclaration5()
    {
        setupParser("class Test;");
        try
        {
            _parser.classDeclaration(false);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testClassDeclaration6()
    {
        setupParser("class {}");
        try
        {
            _parser.classDeclaration(false);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testClassDeclaration7() throws ANTLRException
    {
        setupParser("class TestClass {" +
                    "  private class TestClass {}" +
                    "  private interface TestInterface {}" +
                    "}");

        ClassDeclaration result = _parser.classDeclaration(false);

        assertNotNull(result);
        assertNull(result.getBaseClass());
        assertEquals(0,
                     result.getBaseInterfaces().getCount());
        assertEquals("TestClass",
                     result.getName());
        assertTrue(!result.isInner());
        assertTrue(!result.isLocal());
        assertEquals(0,
                     result.getFields().getCount());
        assertEquals(0,
                     result.getInitializers().getCount());
        assertEquals(0,
                     result.getConstructors().getCount());
        assertEquals(0,
                     result.getMethods().getCount());
        assertEquals(2,
                     result.getInnerTypes().getCount());

        Modifiers mods = result.getModifiers();

        assertTrue(!mods.isAbstract());
        assertTrue(!mods.isFinal());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isStrictfp());

        ClassDeclaration classDecl = (ClassDeclaration)result.getInnerTypes().get(0);

        assertNotNull(classDecl);
        assertEquals("TestClass",
                     classDecl.getName());
        assertTrue(classDecl.isInner());
        assertEquals(result,
                     classDecl.getEnclosingType());
        assertEquals(result,
                     classDecl.getContainer());

        InterfaceDeclaration interfaceDecl = (InterfaceDeclaration)result.getInnerTypes().get(1);

        assertNotNull(interfaceDecl);
        assertEquals("TestInterface",
                     interfaceDecl.getName());
        assertTrue(interfaceDecl.isInner());
        assertEquals(result,
                     interfaceDecl.getEnclosingType());
        assertEquals(result,
                     interfaceDecl.getContainer());
    }

    public void testClassDeclaration8() throws ANTLRException
    {
        setupParser("public class SomeClass {\n" +
                    "  private Vector projects = new Vector();\n" +
                    "  private Vector employees = new Vector();\n" +
                    "  public SomeClass() {\n" +
                    "    super();\n" +
                    "  }\n" +
                    "  public void assignProject(Project prj, Person prs) {\n" +
                    "    prj.getPersons().add(prs);\n" +
                    "  }\n" +
                    "}\n");

        ClassDeclaration result = _parser.classDeclaration(false);

        assertNotNull(result);
        assertNull(result.getBaseClass());
        assertEquals(0,
                     result.getBaseInterfaces().getCount());
        assertEquals("SomeClass",
                     result.getName());
        assertTrue(!result.isInner());
        assertTrue(!result.isLocal());
        assertEquals(2,
                     result.getFields().getCount());
        assertEquals(0,
                     result.getInitializers().getCount());
        assertEquals(1,
                     result.getConstructors().getCount());
        assertEquals(1,
                     result.getMethods().getCount());
        assertEquals(0,
                     result.getInnerTypes().getCount());

        Modifiers mods = result.getModifiers();

        assertTrue(!mods.isAbstract());
        assertTrue(!mods.isFinal());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isStrictfp());

        FieldDeclaration fieldDecl = result.getFields().get(0);

        assertEquals("Vector",
                     fieldDecl.getType().getBaseName());
        assertEquals("projects",
                     fieldDecl.getName());
        assertEquals(result,
                     fieldDecl.getContainer());

        fieldDecl = result.getFields().get(1);

        assertEquals("Vector",
                     fieldDecl.getType().getBaseName());
        assertEquals("employees",
                     fieldDecl.getName());
        assertEquals(result,
                     fieldDecl.getContainer());

        MethodDeclaration methodDecl = result.getMethods().get(0);

        assertNull(methodDecl.getReturnType());
        assertEquals("assignProject(Project,Person)",
                     methodDecl.getSignature());
        assertEquals(result,
                     methodDecl.getContainer());

        ConstructorDeclaration constructorDecl = result.getConstructors().get(0);

        assertEquals("SomeClass()",
                     constructorDecl.getSignature());
        assertEquals(result,
                     constructorDecl.getContainer());
    }

    public void testClassDeclaration9() throws ANTLRException
    {
        setupParser("class Test {\n" +
                    "  private int attr;\n" +
                    "  private static class Inner implements SomeInterface {\n" +
                    "    private String attr = \"\";\n" +
                    "  }\n" +
                    "}");

        ClassDeclaration result = _parser.classDeclaration(false);

        assertNotNull(result);
        assertNull(result.getBaseClass());
        assertEquals(0,
                     result.getBaseInterfaces().getCount());
        assertEquals("Test",
                     result.getName());
        assertTrue(!result.isInner());
        assertTrue(!result.isLocal());
        assertEquals(1,
                     result.getFields().getCount());
        assertEquals(0,
                     result.getInitializers().getCount());
        assertEquals(0,
                     result.getConstructors().getCount());
        assertEquals(0,
                     result.getMethods().getCount());
        assertEquals(1,
                     result.getInnerTypes().getCount());

        Modifiers mods = result.getModifiers();

        assertTrue(!mods.isAbstract());
        assertTrue(!mods.isFinal());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isStrictfp());

        FieldDeclaration fieldDecl = result.getFields().get(0);

        assertEquals("int",
                     fieldDecl.getType().getBaseName());
        assertEquals("attr",
                     fieldDecl.getName());
        assertEquals(result,
                     fieldDecl.getContainer());

        ClassDeclaration classDecl = (ClassDeclaration) result.getInnerTypes().get(0);

        assertNotNull(classDecl);
        assertNull(classDecl.getBaseClass());
        assertEquals(1,
                     classDecl.getBaseInterfaces().getCount());
        assertEquals("Inner",
                     classDecl.getName());
        assertTrue(!classDecl.isInner());
        assertTrue(classDecl.isNested());
        assertTrue(!classDecl.isLocal());
        assertEquals(1,
                     classDecl.getFields().getCount());
        assertEquals(0,
                     classDecl.getInitializers().getCount());
        assertEquals(0,
                     classDecl.getConstructors().getCount());
        assertEquals(0,
                     classDecl.getMethods().getCount());
        assertEquals(0,
                     classDecl.getInnerTypes().getCount());

        mods = classDecl.getModifiers();

        assertTrue(!mods.isAbstract());
        assertTrue(!mods.isFinal());
        assertTrue(mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(mods.isStatic());
        assertTrue(!mods.isStrictfp());

        fieldDecl = classDecl.getFields().get(0);

        assertEquals("String",
                     fieldDecl.getType().getBaseName());
        assertEquals("attr",
                     fieldDecl.getName());
        assertEquals(classDecl,
                     fieldDecl.getContainer());
    }
}
