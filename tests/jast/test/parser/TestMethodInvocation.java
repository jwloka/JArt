package jast.test.parser;
import jast.ast.nodes.ArgumentList;
import jast.ast.nodes.BooleanLiteral;
import jast.ast.nodes.CharacterLiteral;
import jast.ast.nodes.FloatingPointLiteral;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.SelfAccess;
import jast.ast.nodes.StringLiteral;
import jast.ast.nodes.TypeAccess;
import jast.ast.nodes.UnresolvedAccess;
import jast.ast.nodes.VariableAccess;
import antlr.ANTLRException;

public class TestMethodInvocation extends TestBase
{

    public TestMethodInvocation(String name)
    {
        super(name);
    }

    public void testMethodInvocation1() throws ANTLRException
    {
        setupParser("toString()");

        MethodInvocation result = (MethodInvocation)_parser.primary();

        assertNotNull(result);
        assertEquals("toString",
                     result.getMethodName());
        assertTrue(!result.isTrailing());
        assertNull(result.getArgumentList());
    }

    public void testMethodInvocation10() throws ANTLRException
    {
        setupParser("obj.toString()");
        defineVariable("obj", false);

        MethodInvocation result = (MethodInvocation)_parser.primary();

        assertNotNull(result);
        assertEquals("toString",
                     result.getMethodName());
        assertTrue(result.isTrailing());
        assertNull(result.getArgumentList());

        VariableAccess varAccess = (VariableAccess)result.getBaseExpression();

        assertNotNull(varAccess);
        assertEquals("obj",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());
    }

    public void testMethodInvocation11() throws ANTLRException
    {
        setupParser("obj1.obj2.toString()");

        MethodInvocation result = (MethodInvocation)_parser.primary();

        assertNotNull(result);
        assertEquals("toString",
                     result.getMethodName());
        assertTrue(result.isTrailing());
        assertNull(result.getArgumentList());

        UnresolvedAccess unresolvedAccess = (UnresolvedAccess)result.getBaseExpression();

        assertNotNull(unresolvedAccess);
        assertEquals(2,
                     unresolvedAccess.getParts().getCount());
        assertEquals("obj1",
                     unresolvedAccess.getParts().get(0));
        assertEquals("obj2",
                     unresolvedAccess.getParts().get(1));
        assertEquals(result,
                     unresolvedAccess.getContainer());
    }

    public void testMethodInvocation12() throws ANTLRException
    {
        setupParser("obj1.obj2.toString()");
        defineVariable("obj1", false);

        MethodInvocation result = (MethodInvocation)_parser.primary();

        assertNotNull(result);
        assertEquals("toString",
                     result.getMethodName());
        assertTrue(result.isTrailing());
        assertNull(result.getArgumentList());

        UnresolvedAccess unrslvdAccess = (UnresolvedAccess)result.getBaseExpression();

        assertNotNull(unrslvdAccess);
        assertEquals(1,
                     unrslvdAccess.getParts().getCount());
        assertEquals("obj2",
                     unrslvdAccess.getParts().get(0));
        assertTrue(unrslvdAccess.isTrailing());
        assertEquals(result,
                     unrslvdAccess.getContainer());

        VariableAccess varAccess = (VariableAccess)unrslvdAccess.getBaseExpression();

        assertNotNull(varAccess);
        assertEquals("obj1",
                     varAccess.getVariableName());
        assertEquals(unrslvdAccess,
                     varAccess.getContainer());
    }

    public void testMethodInvocation13() throws ANTLRException
    {
        setupParser("doSomething(void)");
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

    public void testMethodInvocation14() throws ANTLRException
    {
        setupParser("Object.super.doSomething(void)");
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

    public void testMethodInvocation15() throws ANTLRException
    {
        setupParser("super.convert(count--)");
        defineVariable("count", false);

        MethodInvocation result = (MethodInvocation)_parser.primary();

        assertNotNull(result);
        assertEquals("convert",
                     result.getMethodName());
        assertTrue(result.isTrailing());

        ArgumentList args = result.getArgumentList();

        assertNotNull(args);
        assertEquals(1,
                     args.getArguments().getCount());
        assertEquals(result,
                     args.getContainer());

        PostfixExpression postfixExpr = (PostfixExpression)args.getArguments().get(0);

        assertNotNull(postfixExpr);
        assertTrue(!postfixExpr.isIncrement());
        assertEquals(args,
                     postfixExpr.getContainer());

        VariableAccess varAccess = (VariableAccess)postfixExpr.getInnerExpression();

        assertNotNull(varAccess);
        assertEquals("count",
                     varAccess.getVariableName());
        assertEquals(postfixExpr,
                     varAccess.getContainer());

        SelfAccess selfAccess = (SelfAccess)result.getBaseExpression();

        assertNotNull(selfAccess);
        assertTrue(!selfAccess.isQualified());
        assertTrue(selfAccess.isSuper());
        assertEquals(result,
                     selfAccess.getContainer());
    }

    public void testMethodInvocation16() throws ANTLRException
    {
        setupParser("prj.getPersons().add(prs)");
        defineVariable("prj", false);

        MethodInvocation result = (MethodInvocation)_parser.primary();

        assertNotNull(result);
        assertEquals("add",
                     result.getMethodName());
        assertTrue(result.isTrailing());
        assertNotNull(result.getArgumentList());

        MethodInvocation methodInvoc = (MethodInvocation)result.getBaseExpression();

        assertNotNull(methodInvoc);
        assertEquals("getPersons",
                     methodInvoc.getMethodName());
        assertNull(methodInvoc.getArgumentList());
        assertTrue(methodInvoc.isTrailing());
        assertEquals(result,
                     methodInvoc.getContainer());

        VariableAccess varAccess = (VariableAccess)methodInvoc.getBaseExpression();

        assertNotNull(varAccess);
        assertEquals("prj",
                     varAccess.getVariableName());
        assertEquals(methodInvoc,
                     varAccess.getContainer());
    }

    public void testMethodInvocation17() throws ANTLRException
    {
        setupParser("Object.this.toString()");

        MethodInvocation result = (MethodInvocation)_parser.primary();

        assertNotNull(result);
        assertEquals("toString",
                     result.getMethodName());
        assertTrue(result.isTrailing());
        assertNull(result.getArgumentList());

        SelfAccess selfAccess = (SelfAccess)result.getBaseExpression();

        assertNotNull(selfAccess);
        assertTrue(selfAccess.isQualified());
        assertTrue(!selfAccess.isSuper());
        assertEquals(result,
                     selfAccess.getContainer());

        TypeAccess typeAccess = selfAccess.getTypeAccess();

        assertNotNull(typeAccess);
        assertEquals("Object",
                     typeAccess.getType().getBaseName());
        assertEquals(selfAccess,
                     typeAccess.getContainer());
    }

    public void testMethodInvocation18() throws ANTLRException
    {
        setupParser("this.toString()");

        MethodInvocation result = (MethodInvocation)_parser.primary();

        assertNotNull(result);
        assertEquals("toString",
                     result.getMethodName());
        assertTrue(result.isTrailing());
        assertNull(result.getArgumentList());

        SelfAccess selfAccess = (SelfAccess)result.getBaseExpression();

        assertNotNull(selfAccess);
        assertTrue(!selfAccess.isQualified());
        assertTrue(!selfAccess.isSuper());
        assertEquals(result,
                     selfAccess.getContainer());
    }

    public void testMethodInvocation2() throws ANTLRException
    {
        setupParser("doSomething(true, 'a')");

        MethodInvocation result = (MethodInvocation)_parser.primary();

        assertNotNull(result);
        assertEquals("doSomething",
                     result.getMethodName());
        assertTrue(!result.isTrailing());

        ArgumentList args = result.getArgumentList();

        assertNotNull(args);
        assertEquals(2,
                     args.getArguments().getCount());
        assertEquals(result,
                     args.getContainer());

        BooleanLiteral boolLit = (BooleanLiteral)args.getArguments().get(0);

        assertNotNull(boolLit);
        assertTrue(boolLit.getValue());
        assertEquals(args,
                     boolLit.getContainer());

        CharacterLiteral charLit = (CharacterLiteral)args.getArguments().get(1);

        assertNotNull(charLit);
        assertEquals('a',
                     charLit.getValue());
        assertEquals(args,
                     charLit.getContainer());
    }

    public void testMethodInvocation3() throws ANTLRException
    {
        setupParser("toString().length()");

        MethodInvocation result = (MethodInvocation)_parser.primary();

        assertNotNull(result);
        assertEquals("length",
                     result.getMethodName());
        assertTrue(result.isTrailing());
        assertNull(result.getArgumentList());

        MethodInvocation methodInvoc = (MethodInvocation)result.getBaseExpression();

        assertNotNull(methodInvoc);
        assertEquals("toString",
                     methodInvoc.getMethodName());
        assertTrue(!methodInvoc.isTrailing());
        assertEquals(result,
                     methodInvoc.getContainer());
    }

    public void testMethodInvocation4() throws ANTLRException
    {
        setupParser("getValues(idx).getPart(false)");
        defineVariable("idx", false);

        MethodInvocation result = (MethodInvocation)_parser.primary();

        assertNotNull(result);
        assertEquals("getPart",
                     result.getMethodName());
        assertTrue(result.isTrailing());

        ArgumentList args = result.getArgumentList();

        assertNotNull(args);
        assertEquals(1,
                     args.getArguments().getCount());
        assertEquals(result,
                     args.getContainer());

        BooleanLiteral boolLit = (BooleanLiteral)args.getArguments().get(0);

        assertNotNull(boolLit);
        assertTrue(!boolLit.getValue());
        assertEquals(args,
                     boolLit.getContainer());

        MethodInvocation methodInvoc = (MethodInvocation)result.getBaseExpression();

        assertNotNull(methodInvoc);
        assertEquals("getValues",
                     methodInvoc.getMethodName());
        assertTrue(!methodInvoc.isTrailing());
        assertEquals(result,
                     methodInvoc.getContainer());

        args = methodInvoc.getArgumentList();

        assertNotNull(args);
        assertEquals(1,
                     args.getArguments().getCount());
        assertEquals(methodInvoc,
                     args.getContainer());

        VariableAccess varAccess = (VariableAccess)args.getArguments().get(0);

        assertNotNull(varAccess);
        assertEquals("idx",
                     varAccess.getVariableName());
        assertEquals(args,
                     varAccess.getContainer());
    }

    public void testMethodInvocation5() throws ANTLRException
    {
        setupParser("\"abc\".length()");

        MethodInvocation result = (MethodInvocation)_parser.primary();

        assertNotNull(result);
        assertEquals("length",
                     result.getMethodName());
        assertTrue(result.isTrailing());
        assertNull(result.getArgumentList());

        StringLiteral stringLit = (StringLiteral)result.getBaseExpression();

        assertNotNull(stringLit);
        assertEquals("\"abc\"",
                     stringLit.asString());
        assertEquals(result,
                     stringLit.getContainer());
    }

    public void testMethodInvocation6() throws ANTLRException
    {
        setupParser("super.toString()");

        MethodInvocation result = (MethodInvocation)_parser.primary();

        assertNotNull(result);
        assertEquals("toString",
                     result.getMethodName());
        assertTrue(result.isTrailing());
        assertNull(result.getArgumentList());

        SelfAccess selfAccess = (SelfAccess)result.getBaseExpression();

        assertNotNull(selfAccess);
        assertTrue(!selfAccess.isQualified());
        assertTrue(selfAccess.isSuper());
        assertEquals(result,
                     selfAccess.getContainer());
    }

    public void testMethodInvocation7() throws ANTLRException
    {
        setupParser("super.convert(1.0d)");

        MethodInvocation result = (MethodInvocation)_parser.primary();

        assertNotNull(result);
        assertEquals("convert",
                     result.getMethodName());
        assertTrue(result.isTrailing());

        ArgumentList args = result.getArgumentList();

        assertNotNull(args);
        assertEquals(1,
                     args.getArguments().getCount());
        assertEquals(result,
                     args.getContainer());

        FloatingPointLiteral floatLit = (FloatingPointLiteral)args.getArguments().get(0);

        assertNotNull(floatLit);
        assertEquals(1.0,
                     floatLit.getValue(),
                     0.0);
        assertEquals(args,
                     floatLit.getContainer());

        SelfAccess selfAccess = (SelfAccess)result.getBaseExpression();

        assertNotNull(selfAccess);
        assertTrue(!selfAccess.isQualified());
        assertTrue(selfAccess.isSuper());
        assertEquals(result,
                     selfAccess.getContainer());
    }

    public void testMethodInvocation8() throws ANTLRException
    {
        setupParser("Object.super.toString()");

        MethodInvocation result = (MethodInvocation)_parser.primary();

        assertNotNull(result);
        assertEquals("toString",
                     result.getMethodName());
        assertTrue(result.isTrailing());
        assertNull(result.getArgumentList());

        SelfAccess selfAccess = (SelfAccess)result.getBaseExpression();

        assertNotNull(selfAccess);
        assertTrue(selfAccess.isQualified());
        assertTrue(selfAccess.isSuper());
        assertEquals(result,
                     selfAccess.getContainer());

        TypeAccess typeAccess = selfAccess.getTypeAccess();

        assertNotNull(typeAccess);
        assertEquals("Object",
                     typeAccess.getType().getBaseName());
        assertEquals(selfAccess,
                     typeAccess.getContainer());
    }

    public void testMethodInvocation9() throws ANTLRException
    {
        setupParser("obj.toString()");

        MethodInvocation result = (MethodInvocation)_parser.primary();

        assertNotNull(result);
        assertEquals("toString",
                     result.getMethodName());
        assertTrue(result.isTrailing());
        assertNull(result.getArgumentList());

        UnresolvedAccess unresolvedAccess = (UnresolvedAccess)result.getBaseExpression();

        assertNotNull(unresolvedAccess);
        assertEquals(1,
                     unresolvedAccess.getParts().getCount());
        assertEquals("obj",
                     unresolvedAccess.getParts().get(0));
        assertEquals(result,
                     unresolvedAccess.getContainer());
    }
}
