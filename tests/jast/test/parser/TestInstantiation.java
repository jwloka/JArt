package jast.test.parser;
import jast.ast.nodes.AnonymousClassDeclaration;
import jast.ast.nodes.ArgumentList;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.StringLiteral;
import jast.ast.nodes.VariableAccess;
import antlr.ANTLRException;

public class TestInstantiation extends TestBase
{

    public TestInstantiation(String name)
    {
        super(name);
    }

    public void testAnonymousClass1() throws ANTLRException
    {
        setupParser("new Object(){" +
                    "private String name = \"\";" +
                    "public String toString() { return name; }}");

        Instantiation result = (Instantiation)_parser.primary();

        assertNotNull(result);
        assertTrue(!result.isTrailing());
        assertEquals("Object",
                     result.getInstantiatedType().getBaseName());
        assertNull(result.getArgumentList());
        assertTrue(result.withAnonymousClass());

        AnonymousClassDeclaration classDecl = result.getAnonymousClass();

        assertNotNull(classDecl);
        assertEquals("Object",
                     classDecl.getBaseType().getBaseName());
        assertNull(classDecl.getName());
        assertEquals(1,
                     classDecl.getFields().getCount());
        assertEquals(0,
                     classDecl.getInitializers().getCount());
        assertEquals(0,
                     classDecl.getConstructors().getCount());
        assertEquals(1,
                     classDecl.getMethods().getCount());
        assertEquals(result,
                     classDecl.getContainer());

        Modifiers mods = classDecl.getModifiers();

        assertTrue(!mods.isAbstract());
        assertTrue(mods.isFinal());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isStrictfp());
    }

    public void testAnonymousClass2() throws ANTLRException
    {
        setupParser("new java.lang.Object(obj, 0){}");
        defineVariable("obj", false);

        Instantiation result = (Instantiation)_parser.primary();

        assertNotNull(result);
        assertTrue(!result.isTrailing());
        assertEquals("java.lang.Object",
                     result.getInstantiatedType().getBaseName());
        assertTrue(result.withAnonymousClass());

        ArgumentList args = result.getArgumentList();

        assertNotNull(args);
        assertEquals(2,
                     args.getArguments().getCount());
        assertEquals(result,
                     args.getContainer());

        VariableAccess varAccess = (VariableAccess)args.getArguments().get(0);

        assertNotNull(varAccess);
        assertEquals("obj",
                     varAccess.getVariableName());
        assertEquals(args,
                     varAccess.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)args.getArguments().get(1);

        assertNotNull(intLit);
        assertEquals(0l,
                     intLit.getValue());
        assertEquals(args,
                     intLit.getContainer());

        AnonymousClassDeclaration classDecl = result.getAnonymousClass();

        assertNotNull(classDecl);
        assertEquals("java.lang.Object",
                     classDecl.getBaseType().getBaseName());
        assertEquals(0,
                     classDecl.getBaseInterfaces().getCount());
        assertNull(classDecl.getName());
        assertEquals(result,
                     classDecl.getContainer());

        Modifiers mods = classDecl.getModifiers();

        assertTrue(!mods.isAbstract());
        assertTrue(mods.isFinal());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isStrictfp());

        assertEquals(0,
                     classDecl.getFields().getCount());
        assertEquals(0,
                     classDecl.getInitializers().getCount());
        assertEquals(0,
                     classDecl.getConstructors().getCount());
        assertEquals(0,
                     classDecl.getMethods().getCount());
    }

    public void testAnonymousClass3() throws ANTLRException
    {
        setupParser("doSomething().new java.lang.Object() {}");

        Instantiation result = (Instantiation)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isTrailing());
        assertEquals("java.lang.Object",
                     result.getInstantiatedType().getBaseName());
        assertTrue(result.withAnonymousClass());
        assertNull(result.getArgumentList());

        MethodInvocation methodInvoc = (MethodInvocation)result.getBaseExpression();

        assertNotNull(methodInvoc);
        assertEquals("doSomething",
                     methodInvoc.getMethodName());
        assertTrue(!methodInvoc.isTrailing());
        assertNull(methodInvoc.getArgumentList());
        assertEquals(result,
                     methodInvoc.getContainer());
    }

    public void testAnonymousClass4()
    {
        setupParser("new Object(){ Object() {} }");
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

    public void testInstantiation1() throws ANTLRException
    {
        setupParser("new Object()");

        Instantiation result = (Instantiation)_parser.primary();

        assertNotNull(result);
        assertTrue(!result.isTrailing());
        assertEquals("Object",
                     result.getInstantiatedType().getBaseName());
        assertTrue(!result.withAnonymousClass());
        assertNull(result.getArgumentList());
    }

    public void testInstantiation10() throws ANTLRException
    {
        setupParser("new Inner().new Outer()");

        Instantiation result = (Instantiation)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isTrailing());
        assertEquals("Outer",
                     result.getInstantiatedType().getBaseName());
        assertTrue(!result.withAnonymousClass());
        assertNull(result.getArgumentList());

        Instantiation inner = (Instantiation)result.getBaseExpression();

        assertNotNull(inner);
        assertTrue(!inner.isTrailing());
        assertEquals("Inner",
                     inner.getInstantiatedType().getBaseName());
        assertEquals(result,
                     inner.getContainer());
    }

    public void testInstantiation2() throws ANTLRException
    {
        setupParser("new java.lang.String()");

        Instantiation result = (Instantiation)_parser.primary();

        assertNotNull(result);
        assertTrue(!result.isTrailing());
        assertEquals("java.lang.String",
                     result.getInstantiatedType().getBaseName());
        assertTrue(!result.withAnonymousClass());
        assertNull(result.getArgumentList());
    }

    public void testInstantiation3() throws ANTLRException
    {
        setupParser("new String(\"text\")");

        Instantiation result = (Instantiation)_parser.primary();

        assertNotNull(result);
        assertTrue(!result.isTrailing());
        assertEquals("String",
                     result.getInstantiatedType().getBaseName());
        assertTrue(!result.withAnonymousClass());

        ArgumentList args = result.getArgumentList();

        assertNotNull(args);
        assertEquals(1,
                     args.getArguments().getCount());
        assertEquals(result,
                     args.getContainer());

        StringLiteral stringLit = (StringLiteral)args.getArguments().get(0);

        assertNotNull(stringLit);
        assertEquals("text",
                     stringLit.getValue());
        assertEquals(args,
                     stringLit.getContainer());
    }

    public void testInstantiation4() throws ANTLRException
    {
        setupParser("\"abc\".new String()");

        Instantiation result = (Instantiation)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isTrailing());
        assertEquals("String",
                     result.getInstantiatedType().getBaseName());
        assertNull(result.getArgumentList());
        assertTrue(!result.withAnonymousClass());

        StringLiteral stringLit = (StringLiteral)result.getBaseExpression();

        assertNotNull(stringLit);
        assertEquals("\"abc\"",
                     stringLit.asString());
        assertEquals(result,
                     stringLit.getContainer());
    }

    public void testInstantiation5() throws ANTLRException
    {
        setupParser("var.new Object()");
        defineVariable("var", false);

        Instantiation result = (Instantiation)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isTrailing());
        assertEquals("Object",
                     result.getInstantiatedType().getBaseName());
        assertNull(result.getArgumentList());

        VariableAccess varAccess = (VariableAccess)result.getBaseExpression();

        assertNotNull(varAccess);
        assertEquals("var",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());
    }

    public void testInstantiation6() throws ANTLRException
    {
        setupParser("str.new java.lang.String(\"text\")");
        defineVariable("str", false);

        Instantiation result = (Instantiation)_parser.primary();

        assertNotNull(result);
        assertTrue(result.isTrailing());
        assertEquals("java.lang.String",
                     result.getInstantiatedType().getBaseName());
        assertTrue(!result.withAnonymousClass());

        ArgumentList args = result.getArgumentList();

        assertNotNull(args);
        assertEquals(1,
                     args.getArguments().getCount());
        assertEquals(result,
                     args.getContainer());

        StringLiteral stringLit = (StringLiteral)args.getArguments().get(0);

        assertNotNull(stringLit);
        assertEquals("text",
                     stringLit.getValue());
        assertEquals(args,
                     stringLit.getContainer());

        VariableAccess varAccess = (VariableAccess)result.getBaseExpression();

        assertNotNull(varAccess);
        assertEquals("str",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());
    }

    public void testInstantiation7()
    {
        setupParser("new boolean()");
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

    public void testInstantiation8()
    {
        setupParser("new Object ");
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

    public void testInstantiation9() throws ANTLRException
    {
        setupParser("new Integer(idx++)");

        Instantiation result = (Instantiation)_parser.primary();

        assertNotNull(result);
        assertTrue(!result.isTrailing());
        assertEquals("Integer",
                     result.getInstantiatedType().getBaseName());
        assertTrue(!result.withAnonymousClass());

        ArgumentList args = result.getArgumentList();

        assertNotNull(args);
        assertEquals(1,
                     args.getArguments().getCount());
        assertEquals(result,
                     args.getContainer());

        PostfixExpression postfixExpr = (PostfixExpression)args.getArguments().get(0);

        assertNotNull(postfixExpr);
        assertTrue(postfixExpr.isIncrement());
        assertEquals(args,
                     postfixExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)postfixExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("idx",
                     fieldAccess.getFieldName());
        assertEquals(postfixExpr,
                     fieldAccess.getContainer());
    }
}
