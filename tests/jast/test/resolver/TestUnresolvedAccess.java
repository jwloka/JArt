package jast.test.resolver;
import jast.ast.nodes.ArgumentList;
import jast.ast.nodes.ArrayAccess;
import jast.ast.nodes.ArrayCreation;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ConditionalExpression;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.InstanceofExpression;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.ParenthesizedExpression;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.SelfAccess;
import jast.ast.nodes.SwitchStatement;
import jast.ast.nodes.SynchronizedStatement;
import jast.ast.nodes.ThrowStatement;
import jast.ast.nodes.TypeAccess;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.UnresolvedAccess;
import antlr.ANTLRException;

public class TestUnresolvedAccess extends TestBase
{

    public TestUnresolvedAccess(String name)
    {
        super(name);
    }

    public void test_ArgumentList() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test() {\n"+
                "    doSomething(0, test.test, \"\");\n"+
                "  }\n"+
                "  public void doSomething(int idx, Object arg, String text) {}\n"+
                "  private A test;\n"+
                "}",
                true);

        MethodDeclaration   methodDecl    = _project.getType("test.A").getMethods().get(0);
        ExpressionStatement exprStmt      = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc   = (MethodInvocation)exprStmt.getExpression();
        ArgumentList        args          = methodInvoc.getArgumentList();
        UnresolvedAccess    unrsvldAccess = (UnresolvedAccess)args.getArguments().get(1);

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("test",
                     unrsvldAccess.getParts().get(1));
        assertEquals(args,
                     unrsvldAccess.getContainer());

        resolve();

        FieldAccess fieldAccess1 = (FieldAccess)args.getArguments().get(1);
        FieldAccess fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(args,
                     fieldAccess1.getContainer());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
    }

    public void test_ArrayAccess1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test() {\n"+
                "    test.arr[0] = 0;\n"+
                "  }\n"+
                "  private int arr[];\n"+
                "  private A   test;\n"+
                "}",
                true);

        MethodDeclaration    methodDecl    = _project.getType("test.A").getMethods().get(0);
        ExpressionStatement  exprStmt      = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        ArrayAccess          arrayAccess   = (ArrayAccess)assignExpr.getLeftHandSide();
        UnresolvedAccess     unrsvldAccess = (UnresolvedAccess)arrayAccess.getBaseExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("arr",
                     unrsvldAccess.getParts().get(1));
        assertEquals(arrayAccess,
                     unrsvldAccess.getContainer());

        resolve();

        FieldAccess fieldAccess1 = (FieldAccess)arrayAccess.getBaseExpression();
        FieldAccess fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(arrayAccess,
                     fieldAccess1.getContainer());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
    }

    public void test_ArrayAccess2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test() {\n"+
                "    arr[test.val] = 0;\n"+
                "  }\n"+
                "  private int arr[];\n"+
                "  private int val;\n"+
                "  private A   test;\n"+
                "}",
                true);

        MethodDeclaration    methodDecl    = _project.getType("test.A").getMethods().get(0);
        ExpressionStatement  exprStmt      = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        ArrayAccess          arrayAccess   = (ArrayAccess)assignExpr.getLeftHandSide();
        UnresolvedAccess     unrsvldAccess = (UnresolvedAccess)arrayAccess.getIndexExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("val",
                     unrsvldAccess.getParts().get(1));
        assertEquals(arrayAccess,
                     unrsvldAccess.getContainer());

        resolve();

        FieldAccess fieldAccess1 = (FieldAccess)arrayAccess.getIndexExpression();
        FieldAccess fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(arrayAccess,
                     fieldAccess1.getContainer());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
    }

    public void test_ArrayCreation() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public Object test() {\n"+
                "    return new int[1][test.val][1];\n"+
                "  }\n"+
                "  private int val;\n"+
                "  private A   test;\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        ArrayCreation     arrayCreation = (ArrayCreation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)arrayCreation.getDimExpressions().get(1);

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("val",
                     unrsvldAccess.getParts().get(1));
        assertEquals(arrayCreation,
                     unrsvldAccess.getContainer());

        resolve();

        FieldAccess fieldAccess1 = (FieldAccess)arrayCreation.getDimExpressions().get(1);
        FieldAccess fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(arrayCreation,
                     fieldAccess1.getContainer());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
    }

    public void test_AssignmentExpression1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test() {\n"+
                "    test.val = 0;\n"+
                "  }\n"+
                "  private int val;\n"+
                "  private A   test;\n"+
                "}",
                true);

        MethodDeclaration    methodDecl    = _project.getType("test.A").getMethods().get(0);
        ExpressionStatement  exprStmt      = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        UnresolvedAccess     unrsvldAccess = (UnresolvedAccess)assignExpr.getLeftHandSide();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("val",
                     unrsvldAccess.getParts().get(1));
        assertEquals(assignExpr,
                     unrsvldAccess.getContainer());

        resolve();

        FieldAccess fieldAccess1 = (FieldAccess)assignExpr.getLeftHandSide();
        FieldAccess fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(assignExpr,
                     fieldAccess1.getContainer());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
    }

    public void test_AssignmentExpression2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test() {\n"+
                "    val = test.val;\n"+
                "  }\n"+
                "  private int val;\n"+
                "  private A   test;\n"+
                "}",
                true);

        MethodDeclaration    methodDecl    = _project.getType("test.A").getMethods().get(0);
        ExpressionStatement  exprStmt      = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        UnresolvedAccess     unrsvldAccess = (UnresolvedAccess)assignExpr.getValueExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("val",
                     unrsvldAccess.getParts().get(1));
        assertEquals(assignExpr,
                     unrsvldAccess.getContainer());

        resolve();

        FieldAccess fieldAccess1 = (FieldAccess)assignExpr.getValueExpression();
        FieldAccess fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(assignExpr,
                     fieldAccess1.getContainer());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
    }

    public void test_BinaryExpression1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int test() {\n"+
                "    return test.val + 1;\n"+
                "  }\n"+
                "  private int val;\n"+
                "  private A   test;\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        BinaryExpression  binExpr       = (BinaryExpression)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)binExpr.getLeftOperand();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("val",
                     unrsvldAccess.getParts().get(1));
        assertEquals(binExpr,
                     unrsvldAccess.getContainer());

        resolve();

        FieldAccess fieldAccess1 = (FieldAccess)binExpr.getLeftOperand();
        FieldAccess fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(binExpr,
                     fieldAccess1.getContainer());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
    }

    public void test_BinaryExpression2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int test() {\n"+
                "    return 1 + test.val;\n"+
                "  }\n"+
                "  private int val;\n"+
                "  private A   test;\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        BinaryExpression  binExpr       = (BinaryExpression)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)binExpr.getRightOperand();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("val",
                     unrsvldAccess.getParts().get(1));
        assertEquals(binExpr,
                     unrsvldAccess.getContainer());

        resolve();

        FieldAccess fieldAccess1 = (FieldAccess)binExpr.getRightOperand();
        FieldAccess fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(binExpr,
                     fieldAccess1.getContainer());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
    }

    public void test_ConditionalExpression1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int test() {\n"+
                "    return test.val ? 1 : 0;\n"+
                "  }\n"+
                "  private boolean val;\n"+
                "  private A       test;\n"+
                "}",
                true);

        MethodDeclaration     methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement       returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        ConditionalExpression condExpr      = (ConditionalExpression)returnStmt.getReturnValue();
        UnresolvedAccess      unrsvldAccess = (UnresolvedAccess)condExpr.getCondition();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("val",
                     unrsvldAccess.getParts().get(1));
        assertEquals(condExpr,
                     unrsvldAccess.getContainer());

        resolve();

        FieldAccess fieldAccess1 = (FieldAccess)condExpr.getCondition();
        FieldAccess fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(condExpr,
                     fieldAccess1.getContainer());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
    }

    public void test_ConditionalExpression2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int test() {\n"+
                "    return val > 0 ? test.val : 0;\n"+
                "  }\n"+
                "  private int val;\n"+
                "  private A   test;\n"+
                "}",
                true);

        MethodDeclaration     methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement       returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        ConditionalExpression condExpr      = (ConditionalExpression)returnStmt.getReturnValue();
        UnresolvedAccess      unrsvldAccess = (UnresolvedAccess)condExpr.getTrueExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("val",
                     unrsvldAccess.getParts().get(1));
        assertEquals(condExpr,
                     unrsvldAccess.getContainer());

        resolve();

        FieldAccess fieldAccess1 = (FieldAccess)condExpr.getTrueExpression();
        FieldAccess fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(condExpr,
                     fieldAccess1.getContainer());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
    }

    public void test_ConditionalExpression3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int test() {\n"+
                "    return val > 0 ? 1 : test.val;\n"+
                "  }\n"+
                "  private int val;\n"+
                "  private A   test;\n"+
                "}",
                true);

        MethodDeclaration     methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement       returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        ConditionalExpression condExpr      = (ConditionalExpression)returnStmt.getReturnValue();
        UnresolvedAccess      unrsvldAccess = (UnresolvedAccess)condExpr.getFalseExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("val",
                     unrsvldAccess.getParts().get(1));
        assertEquals(condExpr,
                     unrsvldAccess.getContainer());

        resolve();

        FieldAccess fieldAccess1 = (FieldAccess)condExpr.getFalseExpression();
        FieldAccess fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(condExpr,
                     fieldAccess1.getContainer());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
    }

    public void test_InstanceOfExpression() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public boolean test() {\n"+
                "    return test.obj instanceof String;\n"+
                "  }\n"+
                "  private Object obj;\n"+
                "  private A      test;\n"+
                "}",
                true);

        MethodDeclaration    methodDecl     = _project.getType("test.A").getMethods().get(0);
        ReturnStatement      returnStmt     = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        InstanceofExpression instanceOfExpr = (InstanceofExpression)returnStmt.getReturnValue();
        UnresolvedAccess     unrsvldAccess  = (UnresolvedAccess)instanceOfExpr.getInnerExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("obj",
                     unrsvldAccess.getParts().get(1));
        assertEquals(instanceOfExpr,
                     unrsvldAccess.getContainer());

        resolve();

        FieldAccess fieldAccess1 = (FieldAccess)instanceOfExpr.getInnerExpression();
        FieldAccess fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(instanceOfExpr,
                     fieldAccess1.getContainer());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
    }

    public void test_ParenthesizedExpression() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public Object test() {\n"+
                "    return (test.obj);\n"+
                "  }\n"+
                "  private Object obj;\n"+
                "  private A      test;\n"+
                "}",
                true);

        MethodDeclaration       methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement         returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        ParenthesizedExpression parenExpr     = (ParenthesizedExpression)returnStmt.getReturnValue();
        UnresolvedAccess        unrsvldAccess = (UnresolvedAccess)parenExpr.getInnerExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("obj",
                     unrsvldAccess.getParts().get(1));
        assertEquals(parenExpr,
                     unrsvldAccess.getContainer());

        resolve();

        FieldAccess fieldAccess1 = (FieldAccess)parenExpr.getInnerExpression();
        FieldAccess fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(parenExpr,
                     fieldAccess1.getContainer());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
    }

    public void test_PostfixExpression() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int test() {\n"+
                "    return test.val++;\n"+
                "  }\n"+
                "  private int val;\n"+
                "  private A   test;\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        PostfixExpression postfixExpr   = (PostfixExpression)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)postfixExpr.getInnerExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("val",
                     unrsvldAccess.getParts().get(1));
        assertEquals(postfixExpr,
                     unrsvldAccess.getContainer());

        resolve();

        FieldAccess fieldAccess1 = (FieldAccess)postfixExpr.getInnerExpression();
        FieldAccess fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(postfixExpr,
                     fieldAccess1.getContainer());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
    }

    public void test_ReturnStatement() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public A test() {\n"+
                "    return test.test;\n"+
                "  }\n"+
                "  private A test;\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)returnStmt.getReturnValue();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("test",
                     unrsvldAccess.getParts().get(1));
        assertEquals(returnStmt,
                     unrsvldAccess.getContainer());

        resolve();

        FieldAccess fieldAccess1 = (FieldAccess)returnStmt.getReturnValue();
        FieldAccess fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(returnStmt,
                     fieldAccess1.getContainer());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
    }

    public void test_SwitchStatement() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test() {\n"+
                "    switch (test.val) {};\n"+
                "  }\n"+
                "  private int val;\n"+
                "  private A   test;\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test.A").getMethods().get(0);
        SwitchStatement   switchStmt    = (SwitchStatement)methodDecl.getBody().getBlockStatements().get(0);
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)switchStmt.getSwitchExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("val",
                     unrsvldAccess.getParts().get(1));
        assertEquals(switchStmt,
                     unrsvldAccess.getContainer());

        resolve();

        FieldAccess fieldAccess1 = (FieldAccess)switchStmt.getSwitchExpression();
        FieldAccess fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(switchStmt,
                     fieldAccess1.getContainer());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
    }

    public void test_SynchronizedStatement() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test() {\n"+
                "    synchronized (test.obj) {};\n"+
                "  }\n"+
                "  private Object obj;\n"+
                "  private A      test;\n"+
                "}",
                true);

        MethodDeclaration     methodDecl    = _project.getType("test.A").getMethods().get(0);
        SynchronizedStatement syncStmt      = (SynchronizedStatement)methodDecl.getBody().getBlockStatements().get(0);
        UnresolvedAccess      unrsvldAccess = (UnresolvedAccess)syncStmt.getLockExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("obj",
                     unrsvldAccess.getParts().get(1));
        assertEquals(syncStmt,
                     unrsvldAccess.getContainer());

        resolve();

        FieldAccess fieldAccess1 = (FieldAccess)syncStmt.getLockExpression();
        FieldAccess fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(syncStmt,
                     fieldAccess1.getContainer());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
    }

    public void test_ThrowStatement() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test() {\n"+
                "    throw test.ex;\n"+
                "  }\n"+
                "  private Exception ex;\n"+
                "  private A         test;\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test.A").getMethods().get(0);
        ThrowStatement    throwStmt     = (ThrowStatement)methodDecl.getBody().getBlockStatements().get(0);
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)throwStmt.getThrowExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("ex",
                     unrsvldAccess.getParts().get(1));
        assertEquals(throwStmt,
                     unrsvldAccess.getContainer());

        resolve();

        FieldAccess fieldAccess1 = (FieldAccess)throwStmt.getThrowExpression();
        FieldAccess fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(throwStmt,
                     fieldAccess1.getContainer());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
    }

    public void test_UnaryExpression() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public byte test() {\n"+
                "    return (byte)test.val;\n"+
                "  }\n"+
                "  private int val;\n"+
                "  private A   test;\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        UnaryExpression   unaryExpr     = (UnaryExpression)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)unaryExpr.getInnerExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("val",
                     unrsvldAccess.getParts().get(1));
        assertEquals(unaryExpr,
                     unrsvldAccess.getContainer());

        resolve();

        FieldAccess fieldAccess1 = (FieldAccess)unaryExpr.getInnerExpression();
        FieldAccess fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(unaryExpr,
                     fieldAccess1.getContainer());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
    }

    public void testPair_FieldField1() throws ANTLRException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  Object test;\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return test.test.toString();\n"+
                "  }\n"+
                "  private B test;\n"+
                "}",
                true);

        TypeDeclaration   typeDecl      = _project.getType("test.A");
        MethodDeclaration methodDecl    = typeDecl.getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("test",
                     unrsvldAccess.getParts().get(1));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        ClassDeclaration classDeclB   = (ClassDeclaration)_project.getType("test.B");
        FieldAccess      fieldAccess1 = (FieldAccess)methodInvoc.getBaseExpression();
        FieldAccess      fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(classDeclB.getFields().get(0),
                     fieldAccess1.getFieldDeclaration());
        assertEquals(methodInvoc,
                     fieldAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     fieldAccess1.getFinishPosition());

        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess2.getFieldDeclaration());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     fieldAccess2.getFinishPosition());

        assertEquals(_project.getType("java.lang.Object").getMethods().get("toString", null),
                     methodInvoc.getMethodDeclaration());
    }

    public void testPair_FieldField2() throws ANTLRException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  class C {}\n"+
                "  C test;\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return test.test.toString();\n"+
                "  }\n"+
                "  private B test;\n"+
                "}",
                true);

        TypeDeclaration   typeDecl      = _project.getType("test.A");
        MethodDeclaration methodDecl    = typeDecl.getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("test",
                     unrsvldAccess.getParts().get(1));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        FieldAccess fieldAccess1 = (FieldAccess)methodInvoc.getBaseExpression();
        FieldAccess fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(_project.getType("test.B").getFields().get(0),
                     fieldAccess1.getFieldDeclaration());
        assertEquals(methodInvoc,
                     fieldAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     fieldAccess1.getFinishPosition());

        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess2.getFieldDeclaration());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     fieldAccess2.getFinishPosition());

        // C inherits from Object
        assertEquals(_project.getType("java.lang.Object").getMethods().get("toString", null),
                     methodInvoc.getMethodDeclaration());
    }

    public void testPair_FieldField3() throws ANTLRException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  Object test;\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return this.test.test.toString();\n"+
                "  }\n"+
                "  private B test;\n"+
                "}",
                true);

        TypeDeclaration   typeDecl      = _project.getType("test.A");
        MethodDeclaration methodDecl    = typeDecl.getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();
        SelfAccess        selfAccess    = (SelfAccess)unrsvldAccess.getBaseExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("test",
                     unrsvldAccess.getParts().get(1));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());
        assertEquals(unrsvldAccess,
                     selfAccess.getContainer());

        resolve();

        FieldAccess fieldAccess1 = (FieldAccess)methodInvoc.getBaseExpression();
        FieldAccess fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(_project.getType("test.B").getFields().get(0),
                     fieldAccess1.getFieldDeclaration());
        assertEquals(methodInvoc,
                     fieldAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     fieldAccess1.getFinishPosition());

        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess2.getFieldDeclaration());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     fieldAccess2.getFinishPosition());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());

        assertEquals(typeDecl,
                     selfAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldAccess2,
                     selfAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     selfAccess.getStartPosition());

        assertEquals(_project.getType("java.lang.Object").getMethods().get("toString", null),
                     methodInvoc.getMethodDeclaration());
    }

    public void testPair_FieldType1() throws ANTLRException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  static class C {\n"+
                "    static String test() {\n"+
                "      return \"\";\n"+
                "    }\n"+
                "  }\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return test.C.test();\n"+
                "  }\n"+
                "  private B test;\n"+
                "}",
                true);

        TypeDeclaration   typeDeclA     = _project.getType("test.A");
        MethodDeclaration methodDecl    = typeDeclA.getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("C",
                     unrsvldAccess.getParts().get(1));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclC   = _project.getType("test.B.C");
        TypeAccess      typeAccess  = (TypeAccess)methodInvoc.getBaseExpression();
        FieldAccess     fieldAccess = (FieldAccess)typeAccess.getBaseExpression();

        assertEquals(typeDeclC,
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     typeAccess.getFinishPosition());

        assertEquals(typeDeclA.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
        assertEquals(typeAccess,
                     fieldAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     fieldAccess.getFinishPosition());

        assertEquals(typeDeclC.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testPair_FieldType2() throws ANTLRException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  static class C {\n"+
                "    static String test() {\n"+
                "      return \"\";\n"+
                "    }\n"+
                "  }\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return this.test.C.test();\n"+
                "  }\n"+
                "  private B test;\n"+
                "}",
                true);

        TypeDeclaration   typeDeclA     = _project.getType("test.A");
        MethodDeclaration methodDecl    = typeDeclA.getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();
        SelfAccess        selfAccess    = (SelfAccess)unrsvldAccess.getBaseExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("C",
                     unrsvldAccess.getParts().get(1));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());
        assertEquals(unrsvldAccess,
                     selfAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclC   = _project.getType("test.B.C");
        TypeAccess      typeAccess  = (TypeAccess)methodInvoc.getBaseExpression();
        FieldAccess     fieldAccess = (FieldAccess)typeAccess.getBaseExpression();

        assertEquals(typeDeclC,
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     typeAccess.getFinishPosition());

        assertEquals(typeDeclA.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
        assertEquals(typeAccess,
                     fieldAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     fieldAccess.getFinishPosition());

        assertEquals(typeDeclA,
                     selfAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldAccess,
                     selfAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     selfAccess.getStartPosition());

        assertEquals(typeDeclC.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testPair_QualifiedType1() throws ANTLRException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {"+
                "  public String test() {\n"+
                "    return \"\";\n"+
                "  }\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return test2.B.test();\n"+
                "  }\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test1.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test2",
                     unrsvldAccess.getParts().get(0));
        assertEquals("B",
                     unrsvldAccess.getParts().get(1));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDecl   = _project.getType("test2.B");
        TypeAccess      typeAccess = (TypeAccess)methodInvoc.getBaseExpression();

        assertEquals(typeDecl,
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(),
                     typeAccess.getFinishPosition());

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testPair_QualifiedType2() throws ANTLRException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {}",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return test2.B.toString();\n"+
                "  }\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test1.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test2",
                     unrsvldAccess.getParts().get(0));
        assertEquals("B",
                     unrsvldAccess.getParts().get(1));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeAccess typeAccess = (TypeAccess)methodInvoc.getBaseExpression();

        assertEquals(_project.getType("test2.B"),
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(),
                     typeAccess.getFinishPosition());

        assertEquals(_project.getType("java.lang.Object").getMethods().get("toString", null),
                     methodInvoc.getMethodDeclaration());
    }

    public void testPair_TypeField1() throws ANTLRException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  static Object test;\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return B.test.toString();\n"+
                "  }\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals("test",
                     unrsvldAccess.getParts().get(1));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDecl    = _project.getType("test.B");
        FieldAccess     fieldAccess = (FieldAccess)methodInvoc.getBaseExpression();
        TypeAccess      typeAccess  = (TypeAccess)fieldAccess.getBaseExpression();

        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
        assertEquals(methodInvoc,
                     fieldAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     fieldAccess.getFinishPosition());

        assertEquals(typeDecl,
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldAccess,
                     typeAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     typeAccess.getFinishPosition());

        assertEquals(_project.getType("java.lang.Object").getMethods().get("toString", null),
                     methodInvoc.getMethodDeclaration());
    }

    public void testPair_TypeField2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return B.test.toString();\n"+
                "  }\n"+
                "  private static class B {\n"+
                "    static Object test;\n"+
                "  }\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals("test",
                     unrsvldAccess.getParts().get(1));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDecl    = _project.getType("test.A.B");
        FieldAccess     fieldAccess = (FieldAccess)methodInvoc.getBaseExpression();
        TypeAccess      typeAccess  = (TypeAccess)fieldAccess.getBaseExpression();

        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
        assertEquals(methodInvoc,
                     fieldAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     fieldAccess.getFinishPosition());

        assertEquals(typeDecl,
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldAccess,
                     typeAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     typeAccess.getFinishPosition());

        assertEquals(_project.getType("java.lang.Object").getMethods().get("toString", null),
                     methodInvoc.getMethodDeclaration());
    }

    public void testPair_TypeField3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return this.B.test.toString();\n"+
                "  }\n"+
                "  private static class B {\n"+
                "    static Object test;\n"+
                "  }\n"+
                "}",
                true);

        TypeDeclaration   typeDeclA     = _project.getType("test.A");
        MethodDeclaration methodDecl    = typeDeclA.getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();
        SelfAccess        selfAccess    = (SelfAccess)unrsvldAccess.getBaseExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals("test",
                     unrsvldAccess.getParts().get(1));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());
        assertEquals(unrsvldAccess,
                     selfAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclB   = typeDeclA.getInnerTypes().get("B");
        FieldAccess     fieldAccess = (FieldAccess)methodInvoc.getBaseExpression();
        TypeAccess      typeAccess  = (TypeAccess)fieldAccess.getBaseExpression();

        assertEquals(typeDeclB.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
        assertEquals(methodInvoc,
                     fieldAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     fieldAccess.getFinishPosition());

        assertEquals(typeDeclB,
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldAccess,
                     typeAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     typeAccess.getFinishPosition());

        assertEquals(typeDeclA,
                     selfAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(typeAccess,
                     selfAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     selfAccess.getStartPosition());

        assertEquals(_project.getType("java.lang.Object").getMethods().get("toString", null),
                     methodInvoc.getMethodDeclaration());
    }

    public void testPair_TypeType1() throws ANTLRException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  static class C {\n"+
                "    static String test() {\n"+
                "      return \"\";\n"+
                "    }\n"+
                "  }\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return B.C.test();\n"+
                "  }\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals("C",
                     unrsvldAccess.getParts().get(1));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclB   = _project.getType("test.B");
        TypeDeclaration typeDeclC   = typeDeclB.getInnerTypes().get("C");
        TypeAccess      typeAccess1 = (TypeAccess)methodInvoc.getBaseExpression();
        TypeAccess      typeAccess2 = (TypeAccess)typeAccess1.getBaseExpression();

        assertEquals(typeDeclC,
                     typeAccess1.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     typeAccess1.getFinishPosition());

        assertEquals(typeDeclB,
                     typeAccess2.getType().getReferenceBaseTypeDecl());
        assertEquals(typeAccess1,
                     typeAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     typeAccess2.getFinishPosition());

        assertEquals(typeDeclC.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testPair_TypeType2() throws ANTLRException
    {
        addType("test.C",
                "package test;\n"+
                "public class C {\n"+
                "  static class D {\n"+
                "    static String test() {\n"+
                "      return \"\";\n"+
                "    }\n"+
                "  }\n"+
                "}",
                false);
        addType("test.B",
                "package test;\n"+
                "public class B extends C {}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return B.D.test();\n"+
                "  }\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals("D",
                     unrsvldAccess.getParts().get(1));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDecl    = _project.getType("test.C.D");
        TypeAccess      typeAccess1 = (TypeAccess)methodInvoc.getBaseExpression();
        TypeAccess      typeAccess2 = (TypeAccess)typeAccess1.getBaseExpression();

        assertEquals(typeDecl,
                     typeAccess1.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     typeAccess1.getFinishPosition());

        assertEquals(_project.getType("test.B"),
                     typeAccess2.getType().getReferenceBaseTypeDecl());
        assertEquals(typeAccess1,
                     typeAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     typeAccess2.getFinishPosition());

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testPair_TypeType3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return B.C.test();\n"+
                "  }\n"+
                "  static class B {\n"+
                "    static class C {\n"+
                "      static String test() {\n"+
                "        return \"\";\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}",
                true);

        TypeDeclaration   typeDeclA     = _project.getType("test.A");
        MethodDeclaration methodDecl    = typeDeclA.getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals("C",
                     unrsvldAccess.getParts().get(1));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclB   = typeDeclA.getInnerTypes().get("B");
        TypeDeclaration typeDeclC   = typeDeclB.getInnerTypes().get("C");
        TypeAccess      typeAccess1 = (TypeAccess)methodInvoc.getBaseExpression();
        TypeAccess      typeAccess2 = (TypeAccess)typeAccess1.getBaseExpression();

        assertEquals(typeDeclC,
                     typeAccess1.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     typeAccess1.getFinishPosition());

        assertEquals(typeDeclB,
                     typeAccess2.getType().getReferenceBaseTypeDecl());
        assertEquals(typeAccess1,
                     typeAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     typeAccess2.getFinishPosition());

        assertEquals(typeDeclC.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testPair_TypeType4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return this.B.C.test();\n"+
                "  }\n"+
                "  static class B {\n"+
                "    static class C {\n"+
                "      static String test() {\n"+
                "        return \"\";\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}",
                true);

        TypeDeclaration   typeDeclA     = _project.getType("test.A");
        MethodDeclaration methodDecl    = typeDeclA.getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();
        SelfAccess        selfAccess    = (SelfAccess)unrsvldAccess.getBaseExpression();

        assertEquals(2,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals("C",
                     unrsvldAccess.getParts().get(1));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());
        assertEquals(unrsvldAccess,
                     selfAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclB   = typeDeclA.getInnerTypes().get("B");
        TypeDeclaration typeDeclC   = typeDeclB.getInnerTypes().get("C");
        TypeAccess      typeAccess1 = (TypeAccess)methodInvoc.getBaseExpression();
        TypeAccess      typeAccess2 = (TypeAccess)typeAccess1.getBaseExpression();

        assertEquals(typeDeclC,
                     typeAccess1.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     typeAccess1.getFinishPosition());

        assertEquals(typeDeclB,
                     typeAccess2.getType().getReferenceBaseTypeDecl());
        assertEquals(typeAccess1,
                     typeAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     typeAccess2.getFinishPosition());

        assertEquals(typeDeclA,
                     selfAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(typeAccess2,
                     selfAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     selfAccess.getStartPosition());

        assertEquals(typeDeclC.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testSingle_Field1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return test.toString();\n"+
                "  }\n"+
                "  private Object test;\n"+
                "}",
                true);

        TypeDeclaration   typeDecl      = _project.getType("test.A");
        MethodDeclaration methodDecl    = typeDecl.getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(1,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        FieldAccess fieldAccess = (FieldAccess)methodInvoc.getBaseExpression();

        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
        assertEquals(methodInvoc,
                     fieldAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(),
                     fieldAccess.getFinishPosition());

        assertEquals(_project.getType("java.lang.Object").getMethods().get("toString", null),
                     methodInvoc.getMethodDeclaration());
    }

    public void testSingle_Field2() throws ANTLRException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  private class C {\n"+
                "    public String toString() {\n"+
                "      return \"\";\n"+
                "    }\n"+
                "  }\n"+
                "  C test;\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends B{\n"+
                "  public String test() {\n"+
                "    return test.toString();\n"+
                "  }\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(1,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclB   = _project.getType("test.B");
        TypeDeclaration typeDeclC   = typeDeclB.getInnerTypes().get("C");
        FieldAccess     fieldAccess = (FieldAccess)methodInvoc.getBaseExpression();

        assertEquals(typeDeclB.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
        assertEquals(methodInvoc,
                     fieldAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(),
                     fieldAccess.getFinishPosition());

        assertEquals(typeDeclC,
                     fieldAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(typeDeclC.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testSingle_Field3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return this.test.toString();\n"+
                "  }\n"+
                "  private Object test;\n"+
                "}",
                true);

        TypeDeclaration   typeDecl      = _project.getType("test.A");
        MethodDeclaration methodDecl    = typeDecl.getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();
        SelfAccess        selfAccess    = (SelfAccess)unrsvldAccess.getBaseExpression();

        assertEquals(1,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());
        assertEquals(unrsvldAccess,
                     selfAccess.getContainer());

        resolve();

        FieldAccess fieldAccess = (FieldAccess)methodInvoc.getBaseExpression();

        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
        assertEquals(methodInvoc,
                     fieldAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(),
                     fieldAccess.getFinishPosition());

        assertEquals(typeDecl,
                     selfAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldAccess,
                     selfAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     selfAccess.getStartPosition());

        assertEquals(_project.getType("java.lang.Object").getMethods().get("toString", null),
                     methodInvoc.getMethodDeclaration());
    }

    public void testSingle_TypeDirectImport1() throws ANTLRException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  public static String test() {\n"+
                "    return \"\";\n"+
                "  }\n"+
                "}",
                false);
        addType("test1.A",
                "package test1;\n"+
                "import test2.B;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return B.test();\n"+
                "  }\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test1.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(1,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDecl   = _project.getType("test2.B");
        TypeAccess      typeAccess = (TypeAccess)methodInvoc.getBaseExpression();

        assertEquals(typeDecl,
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(),
                     typeAccess.getFinishPosition());

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testSingle_TypeDirectImport2() throws ANTLRException
    {
        addType("B",
                "public class B {\n"+
                "  public static String test() {\n"+
                "    return \"\";\n"+
                "  }\n"+
                "}",
                false);
        addType("test1.A",
                "package test1;\n"+
                "import B;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return B.test();\n"+
                "  }\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test1.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(1,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDecl   = _project.getType("B");
        TypeAccess      typeAccess = (TypeAccess)methodInvoc.getBaseExpression();

        assertEquals(typeDecl,
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(),
                     typeAccess.getFinishPosition());

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testSingle_TypeInner1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return B.test();\n"+
                "  }\n"+
                "  static class B {\n"+
                "    public static String test() {\n"+
                "      return \"\";\n"+
                "    }\n"+
                "  }\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(1,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDecl   = _project.getType("test.A.B");
        TypeAccess      typeAccess = (TypeAccess)methodInvoc.getBaseExpression();

        assertEquals(typeDecl,
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(),
                     typeAccess.getFinishPosition());

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testSingle_TypeInner2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return this.B.test();\n"+
                "  }\n"+
                "  static class B {\n"+
                "    public static String test() {\n"+
                "      return \"\";\n"+
                "    }\n"+
                "  }\n"+
                "}",
                true);

        TypeDeclaration   typeDeclA    = _project.getType("test.A");
        MethodDeclaration methodDecl    = typeDeclA.getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();
        SelfAccess        selfAccess    = (SelfAccess)unrsvldAccess.getBaseExpression();

        assertEquals(1,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());
        assertEquals(unrsvldAccess,
                     selfAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclB  = typeDeclA.getInnerTypes().get("B");
        TypeAccess      typeAccess = (TypeAccess)methodInvoc.getBaseExpression();

        assertEquals(typeDeclB,
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(),
                     typeAccess.getFinishPosition());

        assertEquals(typeDeclA,
                     selfAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(typeAccess,
                     selfAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     selfAccess.getStartPosition());

        assertEquals(typeDeclB.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testSingle_TypeOnDemandImport() throws ANTLRException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  public static String test() {\n"+
                "    return \"\";\n"+
                "  }\n"+
                "}",
                false);
        addType("test1.A",
                "package test1;\n"+
                "import test2.*;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return B.test();\n"+
                "  }\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test1.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(1,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDecl   = _project.getType("test2.B");
        TypeAccess      typeAccess = (TypeAccess)methodInvoc.getBaseExpression();

        assertEquals(typeDecl,
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(),
                     typeAccess.getFinishPosition());

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testSingle_TypeSamePackage() throws ANTLRException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  public static String test() {\n"+
                "    return \"\";\n"+
                "  }\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return B.test();\n"+
                "  }\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(1,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDecl   = _project.getType("test.B");
        TypeAccess      typeAccess = (TypeAccess)methodInvoc.getBaseExpression();

        assertEquals(typeDecl,
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(),
                     typeAccess.getFinishPosition());

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTrio_FieldFieldField1() throws ANTLRException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  C test;\n"+
                "}",
                false);
        addType("test.C",
                "package test;\n"+
                "public class C {\n"+
                "  Object test;\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return test.test.test.toString();\n"+
                "  }\n"+
                "  private B test;\n"+
                "}",
                true);

        TypeDeclaration   typeDecl      = _project.getType("test.A");
        MethodDeclaration methodDecl    = typeDecl.getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(3,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("test",
                     unrsvldAccess.getParts().get(1));
        assertEquals("test",
                     unrsvldAccess.getParts().get(2));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        FieldAccess fieldAccess1 = (FieldAccess)methodInvoc.getBaseExpression();
        FieldAccess fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();
        FieldAccess fieldAccess3 = (FieldAccess)fieldAccess2.getBaseExpression();

        assertEquals(_project.getType("test.C").getFields().get(0),
                     fieldAccess1.getFieldDeclaration());
        assertEquals(methodInvoc,
                     fieldAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(2),
                     fieldAccess1.getFinishPosition());

        assertEquals(_project.getType("test.B").getFields().get(0),
                     fieldAccess2.getFieldDeclaration());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     fieldAccess2.getFinishPosition());

        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess3.getFieldDeclaration());
        assertEquals(fieldAccess2,
                     fieldAccess3.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess3.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     fieldAccess3.getFinishPosition());

        assertEquals(_project.getType("java.lang.Object").getMethods().get("toString", null),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTrio_FieldFieldField2() throws ANTLRException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  C test;\n"+
                "}",
                false);
        addType("test.C",
                "package test;\n"+
                "public class C {\n"+
                "  Object test;\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return this.test.test.test.toString();\n"+
                "  }\n"+
                "  private B test;\n"+
                "}",
                true);

        TypeDeclaration   typeDecl      = _project.getType("test.A");
        MethodDeclaration methodDecl    = typeDecl.getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();
        SelfAccess        selfAccess    = (SelfAccess)unrsvldAccess.getBaseExpression();

        assertEquals(3,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("test",
                     unrsvldAccess.getParts().get(1));
        assertEquals("test",
                     unrsvldAccess.getParts().get(2));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());
        assertEquals(unrsvldAccess,
                     selfAccess.getContainer());

        resolve();

        FieldAccess fieldAccess1 = (FieldAccess)methodInvoc.getBaseExpression();
        FieldAccess fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();
        FieldAccess fieldAccess3 = (FieldAccess)fieldAccess2.getBaseExpression();

        assertEquals(_project.getType("test.C").getFields().get(0),
                     fieldAccess1.getFieldDeclaration());
        assertEquals(methodInvoc,
                     fieldAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(2),
                     fieldAccess1.getFinishPosition());

        assertEquals(_project.getType("test.B").getFields().get(0),
                     fieldAccess2.getFieldDeclaration());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     fieldAccess2.getFinishPosition());

        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess3.getFieldDeclaration());
        assertEquals(fieldAccess2,
                     fieldAccess3.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess3.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     fieldAccess3.getFinishPosition());

        assertEquals(typeDecl,
                     selfAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldAccess3,
                     selfAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     selfAccess.getStartPosition());

        assertEquals(_project.getType("java.lang.Object").getMethods().get("toString", null),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTrio_FieldFieldType1() throws ANTLRException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  C test;\n"+
                "}",
                false);
        addType("test.C",
                "package test;\n"+
                "public class C {\n"+
                "  static class D {\n"+
                "    public static String test() {\n"+
                "      return \"\";\n"+
                "    }\n"+
                "  }\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return test.test.D.test();\n"+
                "  }\n"+
                "  private B test;\n"+
                "}",
                true);

        TypeDeclaration   typeDeclA     = _project.getType("test.A");
        MethodDeclaration methodDecl    = typeDeclA.getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(3,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("test",
                     unrsvldAccess.getParts().get(1));
        assertEquals("D",
                     unrsvldAccess.getParts().get(2));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclD    = _project.getType("test.C.D");
        TypeAccess      typeAccess   = (TypeAccess)methodInvoc.getBaseExpression();
        FieldAccess     fieldAccess1 = (FieldAccess)typeAccess.getBaseExpression();
        FieldAccess     fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(typeDeclD,
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(2),
                     typeAccess.getFinishPosition());

        assertEquals(_project.getType("test.B").getFields().get(0),
                     fieldAccess1.getFieldDeclaration());
        assertEquals(typeAccess,
                     fieldAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     fieldAccess1.getFinishPosition());

        assertEquals(typeDeclA.getFields().get(0),
                     fieldAccess2.getFieldDeclaration());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     fieldAccess2.getFinishPosition());

        assertEquals(typeDeclD.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTrio_FieldFieldType2() throws ANTLRException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  C test;\n"+
                "}",
                false);
        addType("test.C",
                "package test;\n"+
                "public class C {\n"+
                "  static class D {\n"+
                "    public static String test() {\n"+
                "      return \"\";\n"+
                "    }\n"+
                "  }\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return this.test.test.D.test();\n"+
                "  }\n"+
                "  private B test;\n"+
                "}",
                true);

        TypeDeclaration   typeDeclA     = _project.getType("test.A");
        MethodDeclaration methodDecl    = typeDeclA.getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();
        SelfAccess        selfAccess    = (SelfAccess)unrsvldAccess.getBaseExpression();

        assertEquals(3,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("test",
                     unrsvldAccess.getParts().get(1));
        assertEquals("D",
                     unrsvldAccess.getParts().get(2));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());
        assertEquals(unrsvldAccess,
                     selfAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclD    = _project.getType("test.C.D");
        TypeAccess      typeAccess   = (TypeAccess)methodInvoc.getBaseExpression();
        FieldAccess     fieldAccess1 = (FieldAccess)typeAccess.getBaseExpression();
        FieldAccess     fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();

        assertEquals(typeDeclD,
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(2),
                     typeAccess.getFinishPosition());

        assertEquals(_project.getType("test.B").getFields().get(0),
                     fieldAccess1.getFieldDeclaration());
        assertEquals(typeAccess,
                     fieldAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     fieldAccess1.getFinishPosition());

        assertEquals(typeDeclA.getFields().get(0),
                     fieldAccess2.getFieldDeclaration());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     fieldAccess2.getFinishPosition());

        assertEquals(typeDeclA,
                     selfAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldAccess2,
                     selfAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     selfAccess.getStartPosition());

        assertEquals(typeDeclD.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTrio_FieldTypeField1() throws ANTLRException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  static class C {\n"+
                "    static D test;\n"+
                "  }\n"+
                "}",
                false);
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  String test() {\n"+
                "    return \"\";\n"+
                "  }\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return test.C.test.test();\n"+
                "  }\n"+
                "  private B test;\n"+
                "}",
                true);
        // note that the field is declared after its used; therefore
        // it will not be found at parse time resulting in a unresolved
        // access with two parts instead of only one

        TypeDeclaration   typeDeclA     = _project.getType("test.A");
        MethodDeclaration methodDecl    = typeDeclA.getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(3,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("C",
                     unrsvldAccess.getParts().get(1));
        assertEquals("test",
                     unrsvldAccess.getParts().get(2));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclC    = _project.getType("test.B.C");
        FieldAccess     fieldAccess1 = (FieldAccess)methodInvoc.getBaseExpression();
        TypeAccess      typeAccess   = (TypeAccess)fieldAccess1.getBaseExpression();
        FieldAccess     fieldAccess2 = (FieldAccess)typeAccess.getBaseExpression();

        assertEquals(typeDeclC.getFields().get(0),
                     fieldAccess1.getFieldDeclaration());
        assertEquals(methodInvoc,
                     fieldAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(2),
                     fieldAccess1.getFinishPosition());

        assertEquals(typeDeclC,
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldAccess1,
                     typeAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     typeAccess.getFinishPosition());

        assertEquals(typeDeclA.getFields().get(0),
                     fieldAccess2.getFieldDeclaration());
        assertEquals(typeAccess,
                     fieldAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     fieldAccess2.getFinishPosition());

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTrio_FieldTypeField2() throws ANTLRException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  static class C {\n"+
                "    static D test;\n"+
                "  }\n"+
                "}",
                false);
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  String test() {\n"+
                "    return \"\";\n"+
                "  }\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return this.test.C.test.test();\n"+
                "  }\n"+
                "  private B test;\n"+
                "}",
                true);
        // note that the field is declared after its used; therefore
        // it will not be found at parse time resulting in a unresolved
        // access with two parts instead of only one

        TypeDeclaration   typeDeclA     = _project.getType("test.A");
        MethodDeclaration methodDecl    = typeDeclA.getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();
        SelfAccess        selfAccess    = (SelfAccess)unrsvldAccess.getBaseExpression();

        assertEquals(3,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("C",
                     unrsvldAccess.getParts().get(1));
        assertEquals("test",
                     unrsvldAccess.getParts().get(2));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());
        assertEquals(unrsvldAccess,
                     selfAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclC    = _project.getType("test.B.C");
        FieldAccess     fieldAccess1 = (FieldAccess)methodInvoc.getBaseExpression();
        TypeAccess      typeAccess   = (TypeAccess)fieldAccess1.getBaseExpression();
        FieldAccess     fieldAccess2 = (FieldAccess)typeAccess.getBaseExpression();

        assertEquals(typeDeclC.getFields().get(0),
                     fieldAccess1.getFieldDeclaration());
        assertEquals(methodInvoc,
                     fieldAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(2),
                     fieldAccess1.getFinishPosition());

        assertEquals(typeDeclC,
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldAccess1,
                     typeAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     typeAccess.getFinishPosition());

        assertEquals(typeDeclA.getFields().get(0),
                     fieldAccess2.getFieldDeclaration());
        assertEquals(typeAccess,
                     fieldAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     fieldAccess2.getFinishPosition());

        assertEquals(typeDeclA,
                     selfAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldAccess2,
                     selfAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     selfAccess.getStartPosition());

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTrio_FieldTypeType1() throws ANTLRException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  static class C {\n"+
                "    static class D {\n"+
                "      static String test() {\n"+
                "        return \"\";\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return test.C.D.test();\n"+
                "  }\n"+
                "  private B test;\n"+
                "}",
                true);

        TypeDeclaration   typeDeclA     = _project.getType("test.A");
        MethodDeclaration methodDecl    = typeDeclA.getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(3,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("C",
                     unrsvldAccess.getParts().get(1));
        assertEquals("D",
                     unrsvldAccess.getParts().get(2));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclC   = _project.getType("test.B.C");
        TypeDeclaration typeDeclD   = typeDeclC.getInnerTypes().get("D");
        TypeAccess      typeAccess1 = (TypeAccess)methodInvoc.getBaseExpression();
        TypeAccess      typeAccess2 = (TypeAccess)typeAccess1.getBaseExpression();
        FieldAccess     fieldAccess = (FieldAccess)typeAccess2.getBaseExpression();

        assertEquals(typeDeclD,
                     typeAccess1.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(2),
                     typeAccess1.getFinishPosition());

        assertEquals(typeDeclC,
                     typeAccess2.getType().getReferenceBaseTypeDecl());
        assertEquals(typeAccess1,
                     typeAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     typeAccess2.getFinishPosition());

        assertEquals(typeDeclA.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
        assertEquals(typeAccess2,
                     fieldAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     fieldAccess.getFinishPosition());

        assertEquals(typeDeclD.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTrio_FieldTypeType2() throws ANTLRException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  static class C {\n"+
                "    static class D {\n"+
                "      static String test() {\n"+
                "        return \"\";\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return this.test.C.D.test();\n"+
                "  }\n"+
                "  private B test;\n"+
                "}",
                true);

        TypeDeclaration   typeDeclA     = _project.getType("test.A");
        MethodDeclaration methodDecl    = typeDeclA.getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();
        SelfAccess        selfAccess    = (SelfAccess)unrsvldAccess.getBaseExpression();

        assertEquals(3,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test",
                     unrsvldAccess.getParts().get(0));
        assertEquals("C",
                     unrsvldAccess.getParts().get(1));
        assertEquals("D",
                     unrsvldAccess.getParts().get(2));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());
        assertEquals(unrsvldAccess,
                     selfAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclC   = _project.getType("test.B.C");
        TypeDeclaration typeDeclD   = typeDeclC.getInnerTypes().get("D");
        TypeAccess      typeAccess1 = (TypeAccess)methodInvoc.getBaseExpression();
        TypeAccess      typeAccess2 = (TypeAccess)typeAccess1.getBaseExpression();
        FieldAccess     fieldAccess = (FieldAccess)typeAccess2.getBaseExpression();

        assertEquals(typeDeclD,
                     typeAccess1.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(2),
                     typeAccess1.getFinishPosition());

        assertEquals(typeDeclC,
                     typeAccess2.getType().getReferenceBaseTypeDecl());
        assertEquals(typeAccess1,
                     typeAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     typeAccess2.getFinishPosition());

        assertEquals(typeDeclA.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
        assertEquals(typeAccess2,
                     fieldAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     fieldAccess.getFinishPosition());

        assertEquals(typeDeclA,
                     selfAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldAccess,
                     selfAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     selfAccess.getStartPosition());

        assertEquals(typeDeclD.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTrio_QualifiedType() throws ANTLRException
    {
        addType("test2.test.B",
                "package test2.test;\n"+
                "public class B {"+
                "  public String test() {\n"+
                "    return \"\";\n"+
                "  }\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return test2.test.B.test();\n"+
                "  }\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test1.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(3,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test2",
                     unrsvldAccess.getParts().get(0));
        assertEquals("test",
                     unrsvldAccess.getParts().get(1));
        assertEquals("B",
                     unrsvldAccess.getParts().get(2));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclB  = _project.getType("test2.test.B");
        TypeAccess      typeAccess = (TypeAccess)methodInvoc.getBaseExpression();

        assertEquals(typeDeclB,
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(),
                     typeAccess.getFinishPosition());

        assertEquals(typeDeclB.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTrio_QualifiedTypeField() throws ANTLRException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  public static Object test;\n"+
                "}",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return test2.B.test.toString();\n"+
                "  }\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test1.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(3,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test2",
                     unrsvldAccess.getParts().get(0));
        assertEquals("B",
                     unrsvldAccess.getParts().get(1));
        assertEquals("test",
                     unrsvldAccess.getParts().get(2));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclB   = _project.getType("test2.B");
        FieldAccess     fieldAccess = (FieldAccess)methodInvoc.getBaseExpression();
        TypeAccess      typeAccess  = (TypeAccess)fieldAccess.getBaseExpression();

        assertEquals(typeDeclB.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
        assertEquals(methodInvoc,
                     fieldAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(2),
                     fieldAccess.getFinishPosition());

        assertEquals(typeDeclB,
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldAccess,
                     typeAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     typeAccess.getFinishPosition());

        assertEquals(_project.getType("java.lang.Object").getMethods().get("toString", null),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTrio_QualifiedTypeType() throws ANTLRException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  public static class C {\n"+
                "    public static String test() {\n"+
                "      return \"\";\n"+
                "    }\n"+
                "  }\n"+
                "}",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return test2.B.C.test();\n"+
                "  }\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test1.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(3,
                     unrsvldAccess.getParts().getCount());
        assertEquals("test2",
                     unrsvldAccess.getParts().get(0));
        assertEquals("B",
                     unrsvldAccess.getParts().get(1));
        assertEquals("C",
                     unrsvldAccess.getParts().get(2));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclB   = _project.getType("test2.B");
        TypeDeclaration typeDeclC   = typeDeclB.getInnerTypes().get("C");
        TypeAccess      typeAccess1 = (TypeAccess)methodInvoc.getBaseExpression();
        TypeAccess      typeAccess2 = (TypeAccess)typeAccess1.getBaseExpression();

        assertEquals(typeDeclC,
                     typeAccess1.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(2),
                     typeAccess1.getFinishPosition());

        assertEquals(typeDeclB,
                     typeAccess2.getType().getReferenceBaseTypeDecl());
        assertEquals(typeAccess1,
                     typeAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     typeAccess2.getFinishPosition());

        assertEquals(typeDeclC.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTrio_TypeFieldField1() throws ANTLRException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  static C test;\n"+
                "}",
                false);
        addType("test.C",
                "package test;\n"+
                "public class C {\n"+
                "  Object test;\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return B.test.test.toString();\n"+
                "  }\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(3,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals("test",
                     unrsvldAccess.getParts().get(1));
        assertEquals("test",
                     unrsvldAccess.getParts().get(2));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDecl     = _project.getType("test.B");
        FieldAccess     fieldAccess1 = (FieldAccess)methodInvoc.getBaseExpression();
        FieldAccess     fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();
        TypeAccess      typeAccess   = (TypeAccess)fieldAccess2.getBaseExpression();

        assertEquals(_project.getType("test.C").getFields().get(0),
                     fieldAccess1.getFieldDeclaration());
        assertEquals(methodInvoc,
                     fieldAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(2),
                     fieldAccess1.getFinishPosition());

        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess2.getFieldDeclaration());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     fieldAccess2.getFinishPosition());

        assertEquals(typeDecl,
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldAccess2,
                     typeAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     typeAccess.getFinishPosition());

        assertEquals(_project.getType("java.lang.Object").getMethods().get("toString", null),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTrio_TypeFieldField2() throws ANTLRException
    {
        addType("test.C",
                "package test;\n"+
                "public class C {\n"+
                "  Object test;\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return B.test.test.toString();\n"+
                "  }\n"+
                "  static class B {\n"+
                "    static C test;\n"+
                "  }\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(3,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals("test",
                     unrsvldAccess.getParts().get(1));
        assertEquals("test",
                     unrsvldAccess.getParts().get(2));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDecl     = _project.getType("test.A.B");
        FieldAccess     fieldAccess1 = (FieldAccess)methodInvoc.getBaseExpression();
        FieldAccess     fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();
        TypeAccess      typeAccess   = (TypeAccess)fieldAccess2.getBaseExpression();

        assertEquals(_project.getType("test.C").getFields().get(0),
                     fieldAccess1.getFieldDeclaration());
        assertEquals(methodInvoc,
                     fieldAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(2),
                     fieldAccess1.getFinishPosition());

        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess2.getFieldDeclaration());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     fieldAccess2.getFinishPosition());

        assertEquals(typeDecl,
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldAccess2,
                     typeAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     typeAccess.getFinishPosition());

        assertEquals(_project.getType("java.lang.Object").getMethods().get("toString", null),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTrio_TypeFieldField3() throws ANTLRException
    {
        addType("test.C",
                "package test;\n"+
                "public class C {\n"+
                "  Object test;\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return this.B.test.test.toString();\n"+
                "  }\n"+
                "  static class B {\n"+
                "    static C test;\n"+
                "  }\n"+
                "}",
                true);

        TypeDeclaration   typeDeclA     = _project.getType("test.A");
        MethodDeclaration methodDecl    = typeDeclA.getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();
        SelfAccess        selfAccess    = (SelfAccess)unrsvldAccess.getBaseExpression();

        assertEquals(3,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals("test",
                     unrsvldAccess.getParts().get(1));
        assertEquals("test",
                     unrsvldAccess.getParts().get(2));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());
        assertEquals(unrsvldAccess,
                     selfAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclB    = typeDeclA.getInnerTypes().get("B");
        FieldAccess     fieldAccess1 = (FieldAccess)methodInvoc.getBaseExpression();
        FieldAccess     fieldAccess2 = (FieldAccess)fieldAccess1.getBaseExpression();
        TypeAccess      typeAccess   = (TypeAccess)fieldAccess2.getBaseExpression();

        assertEquals(_project.getType("test.C").getFields().get(0),
                     fieldAccess1.getFieldDeclaration());
        assertEquals(methodInvoc,
                     fieldAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(2),
                     fieldAccess1.getFinishPosition());

        assertEquals(typeDeclB.getFields().get(0),
                     fieldAccess2.getFieldDeclaration());
        assertEquals(fieldAccess1,
                     fieldAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     fieldAccess2.getFinishPosition());

        assertEquals(typeDeclB,
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldAccess2,
                     typeAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     typeAccess.getFinishPosition());

        assertEquals(typeDeclA,
                     selfAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(typeAccess,
                     selfAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     selfAccess.getStartPosition());

        assertEquals(_project.getType("java.lang.Object").getMethods().get("toString", null),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTrio_TypeFieldType1() throws ANTLRException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  static C test;\n"+
                "}",
                false);
        addType("test.C",
                "package test;\n"+
                "public class C {\n"+
                "  static class D {\n"+
                "    public static String test() {\n"+
                "      return \"\";\n"+
                "    }\n"+
                "  }\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return B.test.D.test();\n"+
                "  }\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(3,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals("test",
                     unrsvldAccess.getParts().get(1));
        assertEquals("D",
                     unrsvldAccess.getParts().get(2));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclB   = _project.getType("test.B");
        TypeDeclaration typeDeclD   = _project.getType("test.C.D");
        TypeAccess      typeAccess1 = (TypeAccess)methodInvoc.getBaseExpression();
        FieldAccess     fieldAccess = (FieldAccess)typeAccess1.getBaseExpression();
        TypeAccess      typeAccess2 = (TypeAccess)fieldAccess.getBaseExpression();

        assertEquals(typeDeclD,
                     typeAccess1.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(2),
                     typeAccess1.getFinishPosition());

        assertEquals(typeDeclB.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
        assertEquals(typeAccess1,
                     fieldAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     fieldAccess.getFinishPosition());

        assertEquals(typeDeclB,
                     typeAccess2.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldAccess,
                     typeAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     typeAccess2.getFinishPosition());

        assertEquals(typeDeclD.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTrio_TypeFieldType2() throws ANTLRException
    {
        addType("test.C",
                "package test;\n"+
                "public class C {\n"+
                "  static class D {\n"+
                "    public static String test() {\n"+
                "      return \"\";\n"+
                "    }\n"+
                "  }\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return B.test.D.test();\n"+
                "  }\n"+
                "  static class B {\n"+
                "    static C test;\n"+
                "  }\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(3,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals("test",
                     unrsvldAccess.getParts().get(1));
        assertEquals("D",
                     unrsvldAccess.getParts().get(2));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclB   = _project.getType("test.A.B");
        TypeDeclaration typeDeclD   = _project.getType("test.C.D");
        TypeAccess      typeAccess1 = (TypeAccess)methodInvoc.getBaseExpression();
        FieldAccess     fieldAccess = (FieldAccess)typeAccess1.getBaseExpression();
        TypeAccess      typeAccess2 = (TypeAccess)fieldAccess.getBaseExpression();

        assertEquals(typeDeclD,
                     typeAccess1.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(2),
                     typeAccess1.getFinishPosition());

        assertEquals(typeDeclB.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
        assertEquals(typeAccess1,
                     fieldAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     fieldAccess.getFinishPosition());

        assertEquals(typeDeclB,
                     typeAccess2.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldAccess,
                     typeAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     typeAccess2.getFinishPosition());

        assertEquals(typeDeclD.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTrio_TypeFieldType3() throws ANTLRException
    {
        addType("test.C",
                "package test;\n"+
                "public class C {\n"+
                "  static class D {\n"+
                "    public static String test() {\n"+
                "      return \"\";\n"+
                "    }\n"+
                "  }\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return this.B.test.D.test();\n"+
                "  }\n"+
                "  static class B {\n"+
                "    static C test;\n"+
                "  }\n"+
                "}",
                true);

        TypeDeclaration   typeDeclA     = _project.getType("test.A");
        MethodDeclaration methodDecl    = typeDeclA.getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();
        SelfAccess        selfAccess    = (SelfAccess)unrsvldAccess.getBaseExpression();

        assertEquals(3,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals("test",
                     unrsvldAccess.getParts().get(1));
        assertEquals("D",
                     unrsvldAccess.getParts().get(2));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());
        assertEquals(unrsvldAccess,
                     selfAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclB   = typeDeclA.getInnerTypes().get("B");
        TypeDeclaration typeDeclD   = _project.getType("test.C.D");
        TypeAccess      typeAccess1 = (TypeAccess)methodInvoc.getBaseExpression();
        FieldAccess     fieldAccess = (FieldAccess)typeAccess1.getBaseExpression();
        TypeAccess      typeAccess2 = (TypeAccess)fieldAccess.getBaseExpression();

        assertEquals(typeDeclD,
                     typeAccess1.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(2),
                     typeAccess1.getFinishPosition());

        assertEquals(typeDeclB.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
        assertEquals(typeAccess1,
                     fieldAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     fieldAccess.getFinishPosition());

        assertEquals(typeDeclB,
                     typeAccess2.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldAccess,
                     typeAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     typeAccess2.getFinishPosition());

        assertEquals(typeDeclA,
                     selfAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(typeAccess2,
                     selfAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     selfAccess.getStartPosition());

        assertEquals(typeDeclD.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTrio_TypeTypeField1() throws ANTLRException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  static class C {\n"+
                "    static D test;\n"+
                "  }\n"+
                "}",
                false);
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public String test() {\n"+
                "    return \"\";\n"+
                "  }\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return B.C.test.test();\n"+
                "  }\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(3,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals("C",
                     unrsvldAccess.getParts().get(1));
        assertEquals("test",
                     unrsvldAccess.getParts().get(2));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclB   = _project.getType("test.B");
        TypeDeclaration typeDeclC   = typeDeclB.getInnerTypes().get("C");
        FieldAccess     fieldAccess = (FieldAccess)methodInvoc.getBaseExpression();
        TypeAccess      typeAccess1 = (TypeAccess)fieldAccess.getBaseExpression();
        TypeAccess      typeAccess2 = (TypeAccess)typeAccess1.getBaseExpression();

        assertEquals(typeDeclC.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
        assertEquals(methodInvoc,
                     fieldAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(2),
                     fieldAccess.getFinishPosition());

        assertEquals(typeDeclC,
                     typeAccess1.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldAccess,
                     typeAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     typeAccess1.getFinishPosition());

        assertEquals(typeDeclB,
                     typeAccess2.getType().getReferenceBaseTypeDecl());
        assertEquals(typeAccess1,
                     typeAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     typeAccess2.getFinishPosition());

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTrio_TypeTypeField2() throws ANTLRException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public String test() {\n"+
                "    return \"\";\n"+
                "  }\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return B.C.test.test();\n"+
                "  }\n"+
                "  static class B {\n"+
                "    static class C {\n"+
                "      static D test;\n"+
                "    }\n"+
                "  }\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(3,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals("C",
                     unrsvldAccess.getParts().get(1));
        assertEquals("test",
                     unrsvldAccess.getParts().get(2));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclB   = _project.getType("test.A.B");
        TypeDeclaration typeDeclC   = typeDeclB.getInnerTypes().get("C");
        FieldAccess     fieldAccess = (FieldAccess)methodInvoc.getBaseExpression();
        TypeAccess      typeAccess1 = (TypeAccess)fieldAccess.getBaseExpression();
        TypeAccess      typeAccess2 = (TypeAccess)typeAccess1.getBaseExpression();

        assertEquals(typeDeclC.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
        assertEquals(methodInvoc,
                     fieldAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(2),
                     fieldAccess.getFinishPosition());

        assertEquals(typeDeclC,
                     typeAccess1.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldAccess,
                     typeAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     typeAccess1.getFinishPosition());

        assertEquals(typeDeclB,
                     typeAccess2.getType().getReferenceBaseTypeDecl());
        assertEquals(typeAccess1,
                     typeAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     typeAccess2.getFinishPosition());

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTrio_TypeTypeField3() throws ANTLRException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public String test() {\n"+
                "    return \"\";\n"+
                "  }\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return this.B.C.test.test();\n"+
                "  }\n"+
                "  static class B {\n"+
                "    static class C {\n"+
                "      static D test;\n"+
                "    }\n"+
                "  }\n"+
                "}",
                true);

        TypeDeclaration   typeDeclA     = _project.getType("test.A");
        MethodDeclaration methodDecl    = typeDeclA.getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();
        SelfAccess        selfAccess    = (SelfAccess)unrsvldAccess.getBaseExpression();

        assertEquals(3,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals("C",
                     unrsvldAccess.getParts().get(1));
        assertEquals("test",
                     unrsvldAccess.getParts().get(2));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());
        assertEquals(unrsvldAccess,
                     selfAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclB   = typeDeclA.getInnerTypes().get("B");
        TypeDeclaration typeDeclC   = typeDeclB.getInnerTypes().get("C");
        FieldAccess     fieldAccess = (FieldAccess)methodInvoc.getBaseExpression();
        TypeAccess      typeAccess1 = (TypeAccess)fieldAccess.getBaseExpression();
        TypeAccess      typeAccess2 = (TypeAccess)typeAccess1.getBaseExpression();

        assertEquals(typeDeclC.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
        assertEquals(methodInvoc,
                     fieldAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     fieldAccess.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(2),
                     fieldAccess.getFinishPosition());

        assertEquals(typeDeclC,
                     typeAccess1.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldAccess,
                     typeAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     typeAccess1.getFinishPosition());

        assertEquals(typeDeclB,
                     typeAccess2.getType().getReferenceBaseTypeDecl());
        assertEquals(typeAccess1,
                     typeAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     typeAccess2.getFinishPosition());

        assertEquals(typeDeclA,
                     selfAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(typeAccess2,
                     selfAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     selfAccess.getStartPosition());

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTrio_TypeTypeType1() throws ANTLRException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  static class C {\n"+
                "    static class D {\n"+
                "      public static String test() {\n"+
                "        return \"\";\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return B.C.D.test();\n"+
                "  }\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(3,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals("C",
                     unrsvldAccess.getParts().get(1));
        assertEquals("D",
                     unrsvldAccess.getParts().get(2));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclB   = _project.getType("test.B");
        TypeDeclaration typeDeclC   = typeDeclB.getInnerTypes().get("C");
        TypeDeclaration typeDeclD   = typeDeclC.getInnerTypes().get("D");
        TypeAccess      typeAccess1 = (TypeAccess)methodInvoc.getBaseExpression();
        TypeAccess      typeAccess2 = (TypeAccess)typeAccess1.getBaseExpression();
        TypeAccess      typeAccess3 = (TypeAccess)typeAccess2.getBaseExpression();

        assertEquals(typeDeclD,
                     typeAccess1.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(2),
                     typeAccess1.getFinishPosition());

        assertEquals(typeDeclC,
                     typeAccess2.getType().getReferenceBaseTypeDecl());
        assertEquals(typeAccess1,
                     typeAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     typeAccess2.getFinishPosition());

        assertEquals(typeDeclB,
                     typeAccess3.getType().getReferenceBaseTypeDecl());
        assertEquals(typeAccess2,
                     typeAccess3.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess3.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     typeAccess3.getFinishPosition());

        assertEquals(typeDeclD.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTrio_TypeTypeType2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return B.C.D.test();\n"+
                "  }\n"+
                "  static class B {\n"+
                "    static class C {\n"+
                "      static class D {\n"+
                "        public static String test() {\n"+
                "          return \"\";\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}",
                true);

        MethodDeclaration methodDecl    = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(3,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals("C",
                     unrsvldAccess.getParts().get(1));
        assertEquals("D",
                     unrsvldAccess.getParts().get(2));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclB   = _project.getType("test.A.B");
        TypeDeclaration typeDeclC   = typeDeclB.getInnerTypes().get("C");
        TypeDeclaration typeDeclD   = typeDeclC.getInnerTypes().get("D");
        TypeAccess      typeAccess1 = (TypeAccess)methodInvoc.getBaseExpression();
        TypeAccess      typeAccess2 = (TypeAccess)typeAccess1.getBaseExpression();
        TypeAccess      typeAccess3 = (TypeAccess)typeAccess2.getBaseExpression();

        assertEquals(typeDeclD,
                     typeAccess1.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(2),
                     typeAccess1.getFinishPosition());

        assertEquals(typeDeclC,
                     typeAccess2.getType().getReferenceBaseTypeDecl());
        assertEquals(typeAccess1,
                     typeAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     typeAccess2.getFinishPosition());

        assertEquals(typeDeclB,
                     typeAccess3.getType().getReferenceBaseTypeDecl());
        assertEquals(typeAccess2,
                     typeAccess3.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess3.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     typeAccess3.getFinishPosition());

        assertEquals(typeDeclD.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTrio_TypeTypeType3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String test() {\n"+
                "    return this.B.C.D.test();\n"+
                "  }\n"+
                "  static class B {\n"+
                "    static class C {\n"+
                "      static class D {\n"+
                "        public static String test() {\n"+
                "          return \"\";\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}",
                true);

        TypeDeclaration   typeDeclA     = _project.getType("test.A");
        MethodDeclaration methodDecl    = typeDeclA.getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc   = (MethodInvocation)returnStmt.getReturnValue();
        UnresolvedAccess  unrsvldAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();
        SelfAccess        selfAccess    = (SelfAccess)unrsvldAccess.getBaseExpression();

        assertEquals(3,
                     unrsvldAccess.getParts().getCount());
        assertEquals("B",
                     unrsvldAccess.getParts().get(0));
        assertEquals("C",
                     unrsvldAccess.getParts().get(1));
        assertEquals("D",
                     unrsvldAccess.getParts().get(2));
        assertEquals(methodInvoc,
                     unrsvldAccess.getContainer());
        assertEquals(unrsvldAccess,
                     selfAccess.getContainer());

        resolve();

        TypeDeclaration typeDeclB   = typeDeclA.getInnerTypes().get("B");
        TypeDeclaration typeDeclC   = typeDeclB.getInnerTypes().get("C");
        TypeDeclaration typeDeclD   = typeDeclC.getInnerTypes().get("D");
        TypeAccess      typeAccess1 = (TypeAccess)methodInvoc.getBaseExpression();
        TypeAccess      typeAccess2 = (TypeAccess)typeAccess1.getBaseExpression();
        TypeAccess      typeAccess3 = (TypeAccess)typeAccess2.getBaseExpression();

        assertEquals(typeDeclD,
                     typeAccess1.getType().getReferenceBaseTypeDecl());
        assertEquals(methodInvoc,
                     typeAccess1.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess1.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(2),
                     typeAccess1.getFinishPosition());

        assertEquals(typeDeclC,
                     typeAccess2.getType().getReferenceBaseTypeDecl());
        assertEquals(typeAccess1,
                     typeAccess2.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess2.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(1),
                     typeAccess2.getFinishPosition());

        assertEquals(typeDeclB,
                     typeAccess3.getType().getReferenceBaseTypeDecl());
        assertEquals(typeAccess2,
                     typeAccess3.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     typeAccess3.getStartPosition());
        assertEquals(unrsvldAccess.getFinishPosition(0),
                     typeAccess3.getFinishPosition());

        assertEquals(typeDeclA,
                     selfAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(typeAccess3,
                     selfAccess.getContainer());
        assertEquals(unrsvldAccess.getStartPosition(),
                     selfAccess.getStartPosition());

        assertEquals(typeDeclD.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }
}
