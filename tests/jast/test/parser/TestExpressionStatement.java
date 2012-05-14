package jast.test.parser;
import jast.ast.nodes.ArgumentList;
import jast.ast.nodes.ArrayAccess;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.ConstructorInvocation;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.ParenthesizedExpression;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.StringLiteral;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.VariableAccess;
import antlr.ANTLRException;

public class TestExpressionStatement extends TestBase
{

    public TestExpressionStatement(String name)
    {
        super(name);
    }

    public void testExpressionStatement1() throws ANTLRException
    {
        setupParser("doSomething();");

        ExpressionStatement result = (ExpressionStatement)_parser.statement();

        assertNotNull(result);

        MethodInvocation methodInvoc = (MethodInvocation)result.getExpression();

        assertNotNull(methodInvoc);
        assertEquals("doSomething",
                     methodInvoc.getMethodName());
        assertEquals(result,
                     methodInvoc.getContainer());
    }

    public void testExpressionStatement10()
    {
        setupParser("getValue()");
        try
        {
            _parser.statement();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testExpressionStatement11() throws ANTLRException
    {
        setupParser("this();");

        ExpressionStatement result = (ExpressionStatement)_parser.statement();

        assertNotNull(result);

        ConstructorInvocation constructorInvoc = (ConstructorInvocation)result.getExpression();

        assertNotNull(constructorInvoc);
        assertTrue(!constructorInvoc.ofBaseClass());
        assertTrue(!constructorInvoc.isTrailing());
        assertEquals(result,
                     constructorInvoc.getContainer());
    }

    public void testExpressionStatement12() throws ANTLRException
    {
        setupParser("super();");

        ExpressionStatement result = (ExpressionStatement)_parser.statement();

        assertNotNull(result);

        ConstructorInvocation constructorInvoc = (ConstructorInvocation)result.getExpression();

        assertNotNull(constructorInvoc);
        assertTrue(constructorInvoc.ofBaseClass());
        assertTrue(!constructorInvoc.isTrailing());
        assertEquals(result,
                     constructorInvoc.getContainer());
    }

    public void testExpressionStatement13() throws ANTLRException
    {
        setupParser("(new Outer()).super();");

        ExpressionStatement result = (ExpressionStatement)_parser.statement();

        assertNotNull(result);

        ConstructorInvocation constructorInvoc = (ConstructorInvocation)result.getExpression();

        assertNotNull(constructorInvoc);
        assertTrue(constructorInvoc.ofBaseClass());
        assertTrue(constructorInvoc.isTrailing());
        assertEquals(result,
                     constructorInvoc.getContainer());
    }

    public void testExpressionStatement14() throws ANTLRException
    {
        setupParser("prj.getPersons().add(prs);");
        defineVariable("prj", false);

        ExpressionStatement result = (ExpressionStatement)_parser.statement();

        assertNotNull(result);

        MethodInvocation methodInvoc1 = (MethodInvocation)result.getExpression();

        assertNotNull(methodInvoc1);
        assertEquals("add",
                     methodInvoc1.getMethodName());
        assertTrue(methodInvoc1.isTrailing());
        assertNotNull(methodInvoc1.getArgumentList());
        assertEquals(result,
                     methodInvoc1.getContainer());

        MethodInvocation methodInvoc2 = (MethodInvocation)methodInvoc1.getBaseExpression();

        assertNotNull(methodInvoc2);
        assertEquals("getPersons",
                     methodInvoc2.getMethodName());
        assertNull(methodInvoc2.getArgumentList());
        assertTrue(methodInvoc2.isTrailing());
        assertEquals(methodInvoc1,
                     methodInvoc2.getContainer());

        VariableAccess varAccess = (VariableAccess)methodInvoc2.getBaseExpression();

        assertNotNull(varAccess);
        assertEquals("prj",
                     varAccess.getVariableName());
        assertEquals(methodInvoc2,
                     varAccess.getContainer());
    }

    public void testExpressionStatement2() throws ANTLRException
    {
        setupParser("other.doSomething();");

        ExpressionStatement result = (ExpressionStatement)_parser.statement();

        assertNotNull(result);

        MethodInvocation methodInvoc = (MethodInvocation)result.getExpression();

        assertNotNull(methodInvoc);
        assertTrue(methodInvoc.isTrailing());
        assertEquals("doSomething",
                     methodInvoc.getMethodName());
        assertEquals(result,
                     methodInvoc.getContainer());
    }

    public void testExpressionStatement3() throws ANTLRException
    {
        setupParser("new String(\"text\");");

        ExpressionStatement result = (ExpressionStatement)_parser.statement();

        assertNotNull(result);

        Instantiation instantiation = (Instantiation)result.getExpression();

        assertNotNull(instantiation);
        assertTrue(!instantiation.isTrailing());
        assertEquals("String",
                     instantiation.getInstantiatedType().getBaseName());
        assertEquals(result,
                     instantiation.getContainer());

        ArgumentList args = instantiation.getArgumentList();

        assertNotNull(args);
        assertEquals(1,
                     args.getArguments().getCount());
        assertEquals(instantiation,
                     args.getContainer());

        StringLiteral stringLit = (StringLiteral)args.getArguments().get(0);

        assertNotNull(stringLit);
        assertEquals("text",
                     stringLit.getValue());
        assertEquals(args,
                     stringLit.getContainer());
    }

    public void testExpressionStatement4() throws ANTLRException
    {
        setupParser("str.new java.lang.String(\"text\");");
        defineVariable("str", false);

        ExpressionStatement result = (ExpressionStatement)_parser.statement();

        assertNotNull(result);

        Instantiation instantiation = (Instantiation)result.getExpression();

        assertNotNull(instantiation);
        assertTrue(instantiation.isTrailing());
        assertEquals("java.lang.String",
                     instantiation.getInstantiatedType().getBaseName());
        assertEquals(result,
                     instantiation.getContainer());

        ArgumentList args = instantiation.getArgumentList();

        assertNotNull(args);
        assertEquals(1,
                     args.getArguments().getCount());
        assertEquals(instantiation,
                     args.getContainer());

        StringLiteral stringLit = (StringLiteral)args.getArguments().get(0);

        assertNotNull(stringLit);
        assertEquals("text",
                     stringLit.getValue());
        assertEquals(args,
                     stringLit.getContainer());

        VariableAccess varAccess = (VariableAccess)instantiation.getBaseExpression();

        assertNotNull(varAccess);
        assertEquals("str",
                     varAccess.getVariableName());
        assertEquals(instantiation,
                     varAccess.getContainer());
    }

    public void testExpressionStatement5() throws ANTLRException
    {
        setupParser("idx++;");
        defineVariable("idx", false);

        ExpressionStatement result = (ExpressionStatement)_parser.statement();

        assertNotNull(result);

        PostfixExpression postfixExpr = (PostfixExpression)result.getExpression();

        assertNotNull(postfixExpr);
        assertTrue(postfixExpr.isIncrement());
        assertEquals(result,
                     postfixExpr.getContainer());

        VariableAccess varAccess = (VariableAccess)postfixExpr.getInnerExpression();

        assertNotNull(varAccess);
        assertEquals("idx",
                     varAccess.getVariableName());
        assertEquals(postfixExpr,
                     varAccess.getContainer());
    }

    public void testExpressionStatement6() throws ANTLRException
    {
        setupParser("(var)--;");
        defineVariable("var", false);

        ExpressionStatement result = (ExpressionStatement)_parser.statement();

        assertNotNull(result);

        PostfixExpression postfixExpr = (PostfixExpression)result.getExpression();

        assertNotNull(postfixExpr);
        assertTrue(!postfixExpr.isIncrement());
        assertEquals(result,
                     postfixExpr.getContainer());

        ParenthesizedExpression parenExpr = (ParenthesizedExpression)postfixExpr.getInnerExpression();

        assertNotNull(parenExpr);
        assertEquals(postfixExpr,
                     parenExpr.getContainer());

        VariableAccess varAccess = (VariableAccess)parenExpr.getInnerExpression();

        assertNotNull(varAccess);
        assertEquals("var",
                     varAccess.getVariableName());
        assertEquals(parenExpr,
                     varAccess.getContainer());
    }

    public void testExpressionStatement7() throws ANTLRException
    {
        setupParser("++arr[0];");

        ExpressionStatement result = (ExpressionStatement)_parser.statement();

        assertNotNull(result);

        UnaryExpression unaryExpr = (UnaryExpression)result.getExpression();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.INCREMENT_OP,
                     unaryExpr.getOperator());
        assertEquals(result,
                     unaryExpr.getContainer());

        ArrayAccess arrayAccess = (ArrayAccess)unaryExpr.getInnerExpression();

        assertNotNull(arrayAccess);
        assertEquals(unaryExpr,
                     arrayAccess.getContainer());

        IntegerLiteral intLit = (IntegerLiteral)arrayAccess.getIndexExpression();

        assertNotNull(intLit);
        assertEquals("0",
                     intLit.asString());
        assertEquals(arrayAccess,
                     intLit.getContainer());

        FieldAccess fieldAccess = (FieldAccess)arrayAccess.getBaseExpression();

        assertNotNull(fieldAccess);
        assertEquals("arr",
                     fieldAccess.getFieldName());
        assertEquals(arrayAccess,
                     fieldAccess.getContainer());
    }

    public void testExpressionStatement8() throws ANTLRException
    {
        setupParser("--var;");

        ExpressionStatement result = (ExpressionStatement)_parser.statement();

        assertNotNull(result);

        UnaryExpression unaryExpr = (UnaryExpression)result.getExpression();

        assertNotNull(unaryExpr);
        assertEquals(UnaryExpression.DECREMENT_OP,
                     unaryExpr.getOperator());
        assertEquals(result,
                     unaryExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)unaryExpr.getInnerExpression();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(unaryExpr,
                     fieldAccess.getContainer());
    }

    public void testExpressionStatement9() throws ANTLRException
    {
        setupParser("arr[2] -= 2 / value;");

        ExpressionStatement result = (ExpressionStatement)_parser.statement();

        assertNotNull(result);

        AssignmentExpression assignExpr = (AssignmentExpression)result.getExpression();

        assertNotNull(assignExpr);
        assertEquals(AssignmentExpression.MINUS_ASSIGN_OP,
                     assignExpr.getOperator());
        assertEquals(result,
                     assignExpr.getContainer());

        ArrayAccess arrayAccess = (ArrayAccess)assignExpr.getLeftHandSide();

        assertNotNull(arrayAccess);
        assertEquals(assignExpr,
                     arrayAccess.getContainer());

        BinaryExpression binExpr = (BinaryExpression)assignExpr.getValueExpression();

        assertNotNull(binExpr);
        assertEquals(BinaryExpression.DIVIDE_OP,
                     binExpr.getOperator());
        assertEquals(assignExpr,
                     binExpr.getContainer());
    }
}
