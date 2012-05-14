package jast.test.resolver;

import jast.ParseException;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.MethodInvocation;
import antlr.ANTLRException;
public class TestMethodArguments extends TestBase
{

    public TestMethodArguments(String name)
    {
        super(name);
    }

    public void testNumOfArgs1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(int value) {\n"+
                "    _attr.test(value, value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDecl  = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No applicable method (would not compile)
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testNumOfArgs2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDecl  = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No applicable method (would not compile)
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testOneArgClonable1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(Clonable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(Clonable value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgClonable2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(Clonable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(double[] values) {\n"+
                "    _attr.test(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgClonable3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(Clonable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(String[] values) {\n"+
                "    _attr.test(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgClonable4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(Clonable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(String value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No applicable method (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testOneArgClonable5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(Clonable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(int value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No applicable method (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testOneArgObject1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(Object obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(Object value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgObject2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(Object obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgObject3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(Object obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(boolean[] value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgObject4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(Object obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C[][] value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgObject5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(Object obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgObject6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(Object obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(int value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No applicable method (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testOneArgObject7() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(Object obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(Clonable value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // References point to actual objects and are therefore
        // method-invocation convertible to java.lang.Object
        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgPrim1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(int value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgPrim2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(short value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgPrim3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(float value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No applicable method (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testOneArgPrim4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(Object value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No applicable method (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testOneArgPrim5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(int[] value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No applicable method (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testOneArgPrimArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(float[][] values) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(float[][] values) {\n"+
                "    _attr.test(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgPrimArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(float[][] values) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgPrimArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(float[][] values) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(double[][] values) {\n"+
                "    _attr.test(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No applicable method (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRef1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public void test(C value) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRef2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public void test(C value) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.D value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRef3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public void test(C value) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRef4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public void test(C value) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C[] value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No applicable method (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRef5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public void test(C value) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(Object value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No applicable method (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRefArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public void test(C[][] value) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C[][] values) {\n"+
                "    _attr.test(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRefArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public void test(C[][] value) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.D[][] values) {\n"+
                "    _attr.test(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRefArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public void test(C[][] value) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRefArray4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public void test(D[][] value) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C[][] values) {\n"+
                "    _attr.test(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No applicable method (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRefArray5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public void test(C[][] value) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C[] values) {\n"+
                "    _attr.test(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No applicable method (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testOneArgSerializable1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(java.io.Serializable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(java.io.Serializable value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgSerializable2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(java.io.Serializable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(char[][] value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgSerializable3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(java.io.Serializable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimArrayClonable1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(long[] values, Clonable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(long[] values, Clonable obj) {\n"+
                "    _attr.test(values, obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimArrayClonable2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(long[] values, Clonable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(Clonable obj) {\n"+
                "    _attr.test(null, obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimArrayClonable3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(long[] values, Clonable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(long[] values) {\n"+
                "    _attr.test(values, values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimArrayClonable4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(long[] values, Clonable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(long[] values) {\n"+
                "    _attr.test(values, new String[2]);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimArrayClonable5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(long[] values, Clonable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(long[] values) {\n"+
                "    _attr.test(values, null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimArrayClonable6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(long[] values, Clonable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(null, null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimObject1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(short value, Object obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(short value, Object obj) {\n"+
                "    _attr.test(value, obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimObject2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(short value, Object obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(byte value, Object obj) {\n"+
                "    _attr.test(value, obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimObject3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(short value, Object obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(short value, test3.C obj) {\n"+
                "    _attr.test(value, obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimObject4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(short value, Object obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(byte value, test3.C obj) {\n"+
                "    _attr.test(value, obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimObject5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(short value, Object obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(short value) {\n"+
                "    _attr.test(value, null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimObject6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(short value, Object obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(byte value) {\n"+
                "    _attr.test(value, null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimPrim1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(long value1, double value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(1l, 0.0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimPrim2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(long value1, double value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test('a', 0.0f);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimPrim3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(long value1, double value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(1l, 1l);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimPrim4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(long value1, double value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0, 1);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsRefRef1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C value1, D value2) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C value1, test3.D value2) {\n"+
                "    _attr.test(value1, value2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsRefRef2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C value1, D value2) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D {}\n",
                false);
        addType("test4.E",
                "package test4;\n"+
                "public class E extends test3.C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test4.E value1, test3.D value2) {\n"+
                "    _attr.test(value1, value2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsRefRef3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C value1, D value2) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D {}\n",
                false);
        addType("test4.E",
                "package test4;\n"+
                "public class E extends test3.D {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C value1, test4.E value2) {\n"+
                "    _attr.test(value1, value2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsRefRef4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C value1, D value2) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public interface C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public interface D {}\n",
                false);
        addType("test4.E",
                "package test4;\n"+
                "import test3.*;\n"+
                "public class E implements C, D {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test4.E value) {\n"+
                "    _attr.test(value, value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsRefRef5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C value1, D value2) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D {}\n",
                false);
        addType("test4.E",
                "package test4;\n"+
                "public class E extends test3.D {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test4.E value2) {\n"+
                "    _attr.test(null, value2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsSerializableRefArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import java.io.Serializable;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public void test(Serializable obj, C[] values) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "import java.io.Serializable;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(Serializable obj, test3.C[] values) {\n"+
                "    _attr.test(obj, values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsSerializableRefArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import java.io.Serializable;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public void test(Serializable obj, C[] values) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public interface C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D implements C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "import java.io.Serializable;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(Serializable obj, test3.D[] values) {\n"+
                "    _attr.test(obj, values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsSerializableRefArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import java.io.Serializable;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public void test(Serializable obj, C[] values) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "import java.io.Serializable;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(Serializable obj) {\n"+
                "    _attr.test(obj, null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsSerializableRefArray4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import java.io.Serializable;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public void test(Serializable obj, C[] values) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(int[][] arr, test3.C[] values) {\n"+
                "    _attr.test(arr, values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsSerializableRefArray5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import java.io.Serializable;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public void test(Serializable obj, C[] values) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C[] values) {\n"+
                "    _attr.test(values, values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsSerializableRefArray6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import java.io.Serializable;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public void test(Serializable obj, C[] values) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C[] values) {\n"+
                "    _attr.test(null, values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsSerializableRefArray7() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import java.io.Serializable;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public void test(Serializable obj, C[] values) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(null, null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }


    public void testOneArgObject8() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(Object obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(java.io.Serializable value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // References point to actual objects and are therefore
        // method-invocation convertible to java.lang.Object
        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }



    public void testOneArgPrim6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No applicable method (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testOneArgSerializable4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(java.io.Serializable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(Object[] values) {\n"+
                "    _attr.test(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }



    public void testOneArgSerializable5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(java.io.Serializable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(Object value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No applicable method (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }
}
