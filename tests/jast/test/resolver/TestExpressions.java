package jast.test.resolver;
import jast.Global;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.CatchClause;
import jast.ast.nodes.ClassAccess;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.ForStatement;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.ParenthesizedExpression;
import jast.ast.nodes.PrimitiveType;
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.StringLiteral;
import jast.ast.nodes.TryStatement;
import jast.ast.nodes.Type;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.VariableAccess;
import antlr.ANTLRException;

// Tests the resolving of expressions
// Note that several expressions need more thorough testing
// and are not or only sparsely present here:
// - ArrayCreation/ArrayAccess (in TestArrayAccess)
// - Instantiation (in TestInstantiationTypes)
// - FieldAccess (in TestField*/TestUnresolvedAccess)
// - MethodInvocation (in TestMethod*)
// - SelfAccess (in TestSelfAccess)
// - TypeAccess (in TestUnresolvedAccess)
public class TestExpressions extends TestBase
{

    public TestExpressions(String name)
    {
        super(name);
    }

    public void testAdditiveExpression1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return \"\" + 2;\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        BinaryExpression  binExpr    = (BinaryExpression)returnStmt.getReturnValue();

        assertEquals(_project.getType("java.lang.String"),
                     binExpr.getType().getReferenceBaseTypeDecl());
    }

    public void testAdditiveExpression2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return 0.0f + toString();\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        BinaryExpression  binExpr    = (BinaryExpression)returnStmt.getReturnValue();

        assertEquals(_project.getType("java.lang.String"),
                     binExpr.getType().getReferenceBaseTypeDecl());
    }

    public void testAdditiveExpression3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test(int value) {\n"+
                "    return value + 2 + \"\";\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        BinaryExpression  binExpr1   = (BinaryExpression)returnStmt.getReturnValue();
        BinaryExpression  binExpr2   = (BinaryExpression)binExpr1.getLeftOperand();

        assertEquals(_project.getType("java.lang.String"),
                     binExpr1.getType().getReferenceBaseTypeDecl());
        assertEquals(PrimitiveType.INT_TYPE,
                     binExpr2.getType().getPrimitiveBaseType());
    }

    public void testAssignmentExpression1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private String _attr;\n"+
                "  public String test() {\n"+
                "    return _attr += \"\";\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        MethodDeclaration    methodDecl = _project.getType("test.A").getMethods().get(0);
        ReturnStatement      returnStmt = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr = (AssignmentExpression)returnStmt.getReturnValue();

        assertEquals(_project.getType("java.lang.String"),
                     assignExpr.getType().getReferenceBaseTypeDecl());
    }

    public void testAssignmentExpression2() throws ANTLRException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {}",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private test2.B _attr;\n"+
                "  public test2.B test() {\n"+
                "    return _attr = new test2.B();\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        MethodDeclaration    methodDecl = _project.getType("test1.A").getMethods().get(0);
        ReturnStatement      returnStmt = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr = (AssignmentExpression)returnStmt.getReturnValue();

        assertEquals(_project.getType("test2.B"),
                     assignExpr.getType().getReferenceBaseTypeDecl());
    }

    public void testAssignmentExpression3() throws ANTLRException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {}",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private test2.B _attr;\n"+
                "  public test2.B test() {\n"+
                "    return _attr = null;\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        MethodDeclaration    methodDecl = _project.getType("test1.A").getMethods().get(0);
        ReturnStatement      returnStmt = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr = (AssignmentExpression)returnStmt.getReturnValue();

        assertEquals(_project.getType("test2.B"),
                     assignExpr.getType().getReferenceBaseTypeDecl());
    }

    public void testCast() throws ANTLRException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {}",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public test2.B test(Object obj) {\n"+
                "    return (test2.B)obj;\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        TypeDeclaration   typeDecl   = _project.getType("test2.B");
        MethodDeclaration methodDecl = _project.getType("test1.A").getMethods().get(0);
        ReturnStatement   returnStmt = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        UnaryExpression   castExpr   = (UnaryExpression)returnStmt.getReturnValue();
        VariableAccess    varAccess  = (VariableAccess)castExpr.getInnerExpression();

        assertEquals(typeDecl,
                     castExpr.getCastType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     castExpr.getType().getReferenceBaseTypeDecl());
        assertEquals(_project.getType("java.lang.Object"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testClassAccess1() throws ANTLRException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {}",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public Object test() {\n"+
                "    return test2.B.class.newInstance();\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        TypeDeclaration   typeDecl    = _project.getType("java.lang.Class");
        MethodDeclaration methodDecl  = _project.getType("test1.A").getMethods().get(0);
        ReturnStatement   returnStmt  = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc = (MethodInvocation)returnStmt.getReturnValue();
        ClassAccess       classAccess = (ClassAccess)methodInvoc.getBaseExpression();

        assertEquals(_project.getType("test2.B"),
                     classAccess.getReferencedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     classAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl.getMethods().get("newInstance", null),
                     methodInvoc.getMethodDeclaration());
    }

    public void testClassAccess2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public Object test() {\n"+
                "    return boolean.class.getName();\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        TypeDeclaration   typeDecl    = _project.getType("java.lang.Class");
        MethodDeclaration methodDecl  = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt  = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc = (MethodInvocation)returnStmt.getReturnValue();
        ClassAccess       classAccess = (ClassAccess)methodInvoc.getBaseExpression();

        assertEquals(PrimitiveType.BOOLEAN_TYPE,
                     classAccess.getReferencedType().getPrimitiveBaseType());
        assertEquals(typeDecl,
                     classAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl.getMethods().get("getName", null),
                     methodInvoc.getMethodDeclaration());
    }

    public void testInstantiation1() throws ANTLRException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  public void test() {}\n"+
                "}",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test() {\n"+
                "    return new test2.B().test();\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        TypeDeclaration   typeDecl      = _project.getType("test2.B");
        MethodDeclaration methodDecl    = _project.getType("test1.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        Instantiation     instantiation = (Instantiation)methodInvoc.getBaseExpression();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testInstantiation2() throws ANTLRException
    {
        addType("test3.D",
                "package test3;\n"+
                "public class D {\n"+
                "  public void test() {}\n"+
                "}",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test3.*;\n"+
                "public class B {\n"+
                "  public class C extends D {}\n"+
                "}",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test() {\n"+
                "    return new test2.B().new C().test();\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        MethodDeclaration methodDecl     = _project.getType("test1.A").getMethods().get(0);
        ReturnStatement   returnStmt     = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc    = (MethodInvocation)returnStmt.getReturnValue();
        Instantiation     instantiation1 = (Instantiation)methodInvoc.getBaseExpression();
        Instantiation     instantiation2 = (Instantiation)instantiation1.getBaseExpression();

        assertEquals(_project.getType("test2.B"),
                     instantiation2.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(_project.getType("test2.B.C"),
                     instantiation1.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(_project.getType("test3.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testMethodInvocation1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public Object getObject();\n"+
                "  public String test() {\n"+
                "    return getObject().toString();\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        TypeDeclaration   typeDeclObj  = _project.getType("java.lang.Object");
        TypeDeclaration   typeDeclA    = _project.getType("test.A");
        MethodDeclaration methodDecl   = typeDeclA.getMethods().get(1);
        ReturnStatement   returnStmt   = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc1 = (MethodInvocation)returnStmt.getReturnValue();
        MethodInvocation  methodInvoc2 = (MethodInvocation)methodInvoc1.getBaseExpression();

        assertEquals(typeDeclObj.getMethods().get("toString", null),
                     methodInvoc1.getMethodDeclaration());
        assertEquals(typeDeclObj,
                     methodInvoc2.getType().getReferenceBaseTypeDecl());
        assertEquals(typeDeclA.getMethods().get(0),
                     methodInvoc2.getMethodDeclaration());
    }

    public void testMethodInvocation2() throws ANTLRException
    {
        addType("test3.D",
                "package test3;\n"+
                "public class D {\n"+
                "  public void test() {}\n"+
                "}",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test3.*;\n"+
                "public class B {\n"+
                "  public C getC();\n"+
                "  public class C extends D {}\n"+
                "}",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A extends test2.B {\n"+
                "  public String test() {\n"+
                "    return getC().test();\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        MethodDeclaration methodDecl   = _project.getType("test1.A").getMethods().get(0);
        ReturnStatement   returnStmt   = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc1 = (MethodInvocation)returnStmt.getReturnValue();
        MethodInvocation  methodInvoc2 = (MethodInvocation)methodInvoc1.getBaseExpression();

        assertEquals(_project.getType("test3.D").getMethods().get(0),
                     methodInvoc1.getMethodDeclaration());
        assertEquals(_project.getType("test2.B.C"),
                     methodInvoc2.getType().getReferenceBaseTypeDecl());
        assertEquals(_project.getType("test2.B").getMethods().get(0),
                     methodInvoc2.getMethodDeclaration());
    }

    public void testParenthesized() throws ANTLRException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  public void test() {}\n"+
                "}",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test() {\n"+
                "    return (new test2.B()).test();\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        TypeDeclaration         typeDecl      = _project.getType("test2.B");
        MethodDeclaration       methodDecl    = _project.getType("test1.A").getMethods().get(0);
        ReturnStatement         returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation        methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        ParenthesizedExpression parenExpr     = (ParenthesizedExpression)methodInvoc.getBaseExpression();;
        Instantiation           instantiation = (Instantiation)parenExpr.getInnerExpression();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     parenExpr.getType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testStringLiteral() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int test() {\n"+
                "    return \"abc\".length();\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        TypeDeclaration   typeDecl    = _project.getType("java.lang.String");
        MethodDeclaration methodDecl  = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt  = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc = (MethodInvocation)returnStmt.getReturnValue();
        StringLiteral     stringLit   = (StringLiteral)methodInvoc.getBaseExpression();

        assertEquals(typeDecl,
                     stringLit.getType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl.getMethods().get("length", null),
                     methodInvoc.getMethodDeclaration());
    }

    public void testVariableAccess1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test(Object obj) {\n"+
                "    return obj.toString();\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        TypeDeclaration   typeDecl    = _project.getType("java.lang.Object");
        MethodDeclaration methodDecl  = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt  = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc = (MethodInvocation)returnStmt.getReturnValue();
        VariableAccess    varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(typeDecl.getMethods().get("toString", null),
                     methodInvoc.getMethodDeclaration());
        assertEquals(typeDecl,
                     varAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(methodDecl.getParameterList().getParameters().get(0),
                     varAccess.getVariableDeclaration());
    }

    public void testVariableAccess2() throws ANTLRException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B extends Exception {\n"+
                "  public String text;\n"+
                "}",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    try {\n"+
                "      return doSomething();\n"+
                "    } catch (test2.B ex) {\n"+
                "      return ex.text;\n"+
                "    }\n"+
                "  }\n"+
                "  private String doSomething() throws B {\n"+
                "    throw new B();\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        TypeDeclaration   typeDecl    = _project.getType("test2.B");
        MethodDeclaration methodDecl  = _project.getType("test1.A").getMethods().get(0);
        TryStatement      tryStmt     = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        CatchClause       catchClause = tryStmt.getCatchClauses().get(0);
        ReturnStatement   returnStmt  = (ReturnStatement)catchClause.getCatchBlock().getBlockStatements().get(0);
        FieldAccess       fieldAccess = (FieldAccess)returnStmt.getReturnValue();
        VariableAccess    varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(typeDecl,
                     varAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
        assertEquals(catchClause.getFormalParameter(),
                     varAccess.getVariableDeclaration());
    }

    public void testVariableAccess3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test(String text) {\n"+
                "    for (String value = text; value.length() > 0; value = value.substring(1)) {}\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        TypeDeclaration          typeDecl     = _project.getType("java.lang.String");
        MethodDeclaration        methodDecl   = _project.getType("test.A").getMethods().get(0);
        ForStatement             forStmt      = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        LocalVariableDeclaration localVarDecl = forStmt.getInitDeclarations().get(0);
        BinaryExpression         binExpr      = (BinaryExpression)forStmt.getCondition();
        MethodInvocation         methodInvoc1 = (MethodInvocation)binExpr.getLeftOperand();
        VariableAccess           varAccess1   = (VariableAccess)methodInvoc1.getBaseExpression();
        AssignmentExpression     assignExpr   = (AssignmentExpression)forStmt.getUpdateList().getExpressions().get(0);
        MethodInvocation         methodInvoc2 = (MethodInvocation)assignExpr.getValueExpression();
        VariableAccess           varAccess2   = (VariableAccess)methodInvoc2.getBaseExpression();
        Type[]                   argTypes     = { Global.getFactory().createType(PrimitiveType.INT_TYPE, 0) };

        assertEquals(typeDecl,
                     varAccess1.getType().getReferenceBaseTypeDecl());
        assertEquals(localVarDecl,
                     varAccess1.getVariableDeclaration());
        assertEquals(typeDecl.getMethods().get("length", null),
                     methodInvoc1.getMethodDeclaration());
        assertEquals(typeDecl,
                     varAccess2.getType().getReferenceBaseTypeDecl());
        assertEquals(localVarDecl,
                     varAccess2.getVariableDeclaration());
        assertEquals(typeDecl.getMethods().get("length", argTypes),
                     methodInvoc2.getMethodDeclaration());
    }
}
