package jast.test.parser;
import jast.ast.nodes.Block;
import jast.ast.nodes.FormalParameterList;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.PrimitiveType;
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.Type;
import jast.ast.nodes.VariableAccess;
import antlr.ANTLRException;

public class TestMethodDeclaration extends TestBase
{

    public TestMethodDeclaration(String name)
    {
        super(name);
    }

    public void testAbstractMethodDeclaration1() throws ANTLRException
    {
        setupParser("void doSomething();");

        MethodDeclaration result = (MethodDeclaration)_parser.interfaceBodyDeclaration();

        assertNotNull(result);
        assertEquals("doSomething",
                     result.getName());
        assertEquals(0,
                     result.getThrownExceptions().getCount());
        assertNull(result.getParameterList());
        assertNull(result.getReturnType());
        assertNull(result.getBody());

        Modifiers mods = result.getModifiers();

        assertTrue(!mods.isAbstract());
        assertTrue(!mods.isFinal());
        assertTrue(!mods.isNative());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isStrictfp());
        assertTrue(!mods.isSynchronized());
    }

    public void testAbstractMethodDeclaration2() throws ANTLRException
    {
        setupParser("public abstract int getValue(String text) throws NullPointerException;");

        MethodDeclaration result = (MethodDeclaration)_parser.interfaceBodyDeclaration();

        assertNotNull(result);
        assertEquals("getValue",
                     result.getName());
        assertEquals("int",
                     result.getReturnType().getBaseName());
        assertEquals(1,
                     result.getThrownExceptions().getCount());
        assertEquals("NullPointerException",
                     result.getThrownExceptions().get(0).getBaseName());
        assertNull(result.getBody());

        FormalParameterList params = result.getParameterList();

        assertNotNull(params);
        assertEquals(1,
                     params.getParameters().getCount());
        assertEquals(result,
                     params.getContainer());

        Modifiers mods = result.getModifiers();

        assertTrue(mods.isAbstract());
        assertTrue(!mods.isFinal());
        assertTrue(!mods.isNative());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isStrictfp());
        assertTrue(!mods.isSynchronized());
    }

    public void testAbstractMethodDeclaration3() throws ANTLRException
    {
        setupParser("java.lang.String convert(String value) throws NullPointerException, FormatException;");

        MethodDeclaration result = (MethodDeclaration)_parser.interfaceBodyDeclaration();

        assertNotNull(result);
        assertEquals("convert",
                     result.getName());
        assertEquals("java.lang.String",
                     result.getReturnType().getBaseName());
        assertEquals(2,
                     result.getThrownExceptions().getCount());
        assertEquals("NullPointerException",
                     result.getThrownExceptions().get(0).getBaseName());
        assertEquals("FormatException",
                     result.getThrownExceptions().get(1).getBaseName());
        assertNull(result.getBody());

        FormalParameterList params = result.getParameterList();

        assertNotNull(params);
        assertEquals(1,
                     params.getParameters().getCount());
        assertEquals(result,
                     params.getContainer());
    }

    public void testAbstractMethodDeclaration4()
    {
        setupParser("doSomething();");
        try
        {
            _parser.interfaceBodyDeclaration();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testAbstractMethodDeclaration5()
    {
        setupParser("static void doSomething();");
        try
        {
            _parser.interfaceBodyDeclaration();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testAbstractMethodDeclaration6()
    {
        setupParser("void doSomething() {}");
        try
        {
            _parser.interfaceBodyDeclaration();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testAbstractMethodDeclaration7() throws ANTLRException
    {
        setupParser("int doSomething()[];");

        MethodDeclaration result = (MethodDeclaration)_parser.interfaceBodyDeclaration();

        assertNotNull(result);
        assertEquals("doSomething",
                     result.getName());
        assertEquals(0,
                     result.getThrownExceptions().getCount());
        assertNull(result.getParameterList());
        assertNull(result.getBody());

        Modifiers mods = result.getModifiers();

        assertTrue(!mods.isAbstract());
        assertTrue(!mods.isFinal());
        assertTrue(!mods.isNative());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isStrictfp());
        assertTrue(!mods.isSynchronized());

        Type returnType = result.getReturnType();

        assertNotNull(returnType);
        assertTrue(returnType.isPrimitive());
        assertTrue(!returnType.isReference());
        assertTrue(returnType.isArray());
        assertEquals(PrimitiveType.INT_TYPE,
                     returnType.getPrimitiveBaseType());
        assertEquals(1,
                     returnType.getDimensions());
    }

    public void testAbstractMethodDeclaration8()
    {
        setupParser("void doSomething()[];");
        try
        {
            _parser.interfaceBodyDeclaration();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testMethodDeclaration1() throws ANTLRException
    {
        setupParser("void doSomething();");

        MethodDeclaration result = (MethodDeclaration)_parser.classBodyDeclaration();

        assertNotNull(result);
        assertEquals("doSomething",
                     result.getName());
        assertEquals(0,
                     result.getThrownExceptions().getCount());
        assertNull(result.getParameterList());
        assertNull(result.getReturnType());
        assertNull(result.getBody());

        Modifiers mods = result.getModifiers();

        assertTrue(!mods.isAbstract());
        assertTrue(!mods.isFinal());
        assertTrue(!mods.isNative());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isStrictfp());
        assertTrue(!mods.isSynchronized());
    }

    public void testMethodDeclaration2() throws ANTLRException
    {
        setupParser("public static int getValue(String text) throws NullPointerException {}");

        MethodDeclaration result = (MethodDeclaration)_parser.classBodyDeclaration();

        assertNotNull(result);
        assertEquals("getValue",
                     result.getName());
        assertEquals("int",
                     result.getReturnType().getBaseName());
        assertEquals(1,
                     result.getThrownExceptions().getCount());
        assertEquals("NullPointerException",
                     result.getThrownExceptions().get(0).getBaseName());
        assertNotNull(result.getBody());

        FormalParameterList params = result.getParameterList();

        assertNotNull(params);
        assertEquals(1,
                     params.getParameters().getCount());
        assertEquals(result,
                     params.getContainer());

        Modifiers mods = result.getModifiers();

        assertTrue(!mods.isAbstract());
        assertTrue(!mods.isFinal());
        assertTrue(!mods.isNative());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(mods.isPublic());
        assertTrue(mods.isStatic());
        assertTrue(!mods.isStrictfp());
        assertTrue(!mods.isSynchronized());
    }

    public void testMethodDeclaration3() throws ANTLRException
    {
        setupParser("java.lang.String convert(String value) throws NullPointerException, FormatException " +
                    "{ return value; }");

        MethodDeclaration result = (MethodDeclaration)_parser.classBodyDeclaration();

        assertNotNull(result);
        assertEquals("convert",
                     result.getName());
        assertEquals("java.lang.String",
                     result.getReturnType().getBaseName());
        assertEquals(2,
                     result.getThrownExceptions().getCount());
        assertEquals("NullPointerException",
                     result.getThrownExceptions().get(0).getBaseName());
        assertEquals("FormatException",
                     result.getThrownExceptions().get(1).getBaseName());

        FormalParameterList params = result.getParameterList();

        assertNotNull(params);
        assertEquals(1,
                     params.getParameters().getCount());
        assertEquals(result,
                     params.getContainer());

        Block block = result.getBody();

        assertNotNull(block);
        assertEquals(1,
                     block.getBlockStatements().getCount());
        assertEquals(result,
                     block.getContainer());

        ReturnStatement returnStmt = (ReturnStatement)block.getBlockStatements().get(0);

        assertNotNull(returnStmt);
        assertEquals(block,
                     returnStmt.getContainer());

        VariableAccess varAccess = (VariableAccess)returnStmt.getReturnValue();

        assertNotNull(varAccess);
        assertEquals(params.getParameters().get(0),
                     varAccess.getVariableDeclaration());
        assertEquals(returnStmt,
                     varAccess.getContainer());
    }

    public void testMethodDeclaration4()
    {
        setupParser("doSomething();");
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

    public void testMethodDeclaration5()
    {
        setupParser("volatile void doSomething();");
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

    public void testMethodDeclaration6() throws ANTLRException
    {
        setupParser("String[] doSomething()[] {}");

        MethodDeclaration result = (MethodDeclaration)_parser.classBodyDeclaration();

        assertNotNull(result);
        assertEquals("doSomething",
                     result.getName());
        assertEquals(0,
                     result.getThrownExceptions().getCount());
        assertNull(result.getParameterList());
        assertNotNull(result.getBody());

        Modifiers mods = result.getModifiers();

        assertTrue(!mods.isAbstract());
        assertTrue(!mods.isFinal());
        assertTrue(!mods.isNative());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isStrictfp());
        assertTrue(!mods.isSynchronized());

        Type returnType = result.getReturnType();

        assertNotNull(returnType);
        assertTrue(!returnType.isPrimitive());
        assertTrue(returnType.isReference());
        assertTrue(returnType.isArray());
        assertEquals("String",
                     returnType.getBaseName());
        assertEquals(2,
                     returnType.getDimensions());
    }

    public void testMethodDeclaration7()
    {
        setupParser("void doSomething()[] {}");
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
