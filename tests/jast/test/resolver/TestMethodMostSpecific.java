package jast.test.resolver;
import jast.ParseException;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.MethodInvocation;
import antlr.ANTLRException;
public class TestMethodMostSpecific extends TestBase
{

    public TestMethodMostSpecific(String name)
    {
        super(name);
    }

    public void testOneArgClonablePrimArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(Clonable value) {}\n"+
                "  public void test(int[] values) {}\n"+
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
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgClonablePrimArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(Clonable value) {}\n"+
                "  public void test(int[] values) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(int[] values) {\n"+
                "    _attr.test(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgClonablePrimArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(Clonable value) {}\n"+
                "  void test(int[] values) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(int[] values) {\n"+
                "    _attr.test(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgClonablePrimArray4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(Clonable value) {}\n"+
                "  public void test(int[] values) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(short[] values) {\n"+
                "    _attr.test(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgClonableRefArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(Clonable obj) {}\n"+
                "  public void test(C[][] arr) {}\n"+
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
                "  private void doSomething(Clonable obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgClonableRefArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(Clonable obj) {}\n"+
                "  public void test(C[][] arr) {}\n"+
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
                "  private void doSomething(test3.C[][] arr) {\n"+
                "    _attr.test(arr);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgClonableRefArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(Clonable obj) {}\n"+
                "  void test(C[][] arr) {}\n"+
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
                "  private void doSomething(test3.C[][] arr) {\n"+
                "    _attr.test(arr);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgClonableRefArray4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(Clonable obj) {}\n"+
                "  public void test(C[][] arr) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.D[][] arr) {\n"+
                "    _attr.test(arr);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgClonableRefArray5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(Clonable obj) {}\n"+
                "  void test(C[][] arr) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.D[][] arr) {\n"+
                "    _attr.test(arr);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgClonableRefArray6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  void test(Clonable obj) {}\n"+
                "  public void test(D[][] arr) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C[][] arr) {\n"+
                "    _attr.test(arr);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No accessible methods are applicable (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testOneArgObjectPrimArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(Object value) {}\n"+
                "  public void test(int[] values) {}\n"+
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
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgObjectPrimArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(Object value) {}\n"+
                "  public void test(int[] values) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(int[] values) {\n"+
                "    _attr.test(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgObjectPrimArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(Object value) {}\n"+
                "  void test(int[] values) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(int[] values) {\n"+
                "    _attr.test(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgObjectPrimArray4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(Object value) {}\n"+
                "  public void test(int[] values) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(short[] values) {\n"+
                "    _attr.test(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgObjectRefArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(Object obj) {}\n"+
                "  public void test(C[][] arr) {}\n"+
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
                "  private void doSomething(Object obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgObjectRefArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(Object obj) {}\n"+
                "  public void test(C[][] arr) {}\n"+
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
                "  private void doSomething(test3.C[][] arr) {\n"+
                "    _attr.test(arr);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgObjectRefArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(Object obj) {}\n"+
                "  void test(C[][] arr) {}\n"+
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
                "  private void doSomething(test3.C[][] arr) {\n"+
                "    _attr.test(arr);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgObjectRefArray4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(Object obj) {}\n"+
                "  public void test(C[][] arr) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.D[][] arr) {\n"+
                "    _attr.test(arr);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgObjectRefArray5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(Object obj) {}\n"+
                "  void test(C[][] arr) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.D[][] arr) {\n"+
                "    _attr.test(arr);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgObjectRefArray6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(Object obj) {}\n"+
                "  public void test(D[][] arr) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C[][] arr) {\n"+
                "    _attr.test(arr);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgPrimArrayPrimArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(int[] value) {}\n"+
                "  public void test(float[] value) {}\n"+
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

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgPrimArrayPrimArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(int[] value) {}\n"+
                "  public void test(float[] value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(float[] value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgPrimArrayPrimArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(int[] value) {}\n"+
                "  public void test(float[] value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(long[] value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No method is applicable (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testOneArgPrimArrayPrimArray4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(int[] value) {}\n"+
                "  void test(float[] value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(float[] value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No accessible method is applicable (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testOneArgPrimPrim1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
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
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgPrimPrim2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
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

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgPrimPrim3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
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
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgPrimPrim4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(long value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgPrimPrim5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
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
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgPrimPrim6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
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
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgPrimPrim7() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(double value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No method is applicable (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testOneArgPrimPrim8() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "  void test(float value) {}\n"+
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

        // No accessible method is applicable (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRefArrayRefArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C[][] obj) {}\n"+
                "  public void test(D[][] obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C[][] obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRefArrayRefArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C[][] obj) {}\n"+
                "  public void test(D[][] obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.D[][] obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRefArrayRefArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C[][] obj) {}\n"+
                "  void test(D[][] obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.D[][] obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRefArrayRefArray4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C[][] obj) {}\n"+
                "  public void test(E[][] obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test3.E",
                "package test3;\n"+
                "public class E extends D {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.D[][] obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRefArrayRefArray5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C[][] obj) {}\n"+
                "  public void test(D[][] obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test3.E",
                "package test3;\n"+
                "public class E extends D {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.E[][] obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRefArrayRefArray6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C[][] obj) {}\n"+
                "  void test(D[][] obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test3.E",
                "package test3;\n"+
                "public class E extends D {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.E[][] obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRefArrayRefArray7() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(D[][] obj) {}\n"+
                "  public void test(E[][] obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test3.E",
                "package test3;\n"+
                "public class E extends D {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C[][] obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No method is applicable (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRefArrayRefArray8() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  void test(C[][] obj) {}\n"+
                "  public void test(D[][] obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C[][] obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No accessible method is applicable (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRefRef1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C obj) {}\n"+
                "  public void test(D obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRefRef2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C obj) {}\n"+
                "  public void test(D obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.D obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRefRef3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C obj) {}\n"+
                "  void test(D obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.D obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRefRef4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C obj) {}\n"+
                "  public void test(E obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test3.E",
                "package test3;\n"+
                "public class E extends D {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.D obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRefRef5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C obj) {}\n"+
                "  public void test(D obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test3.E",
                "package test3;\n"+
                "public class E extends D {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.E obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRefRef6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C obj) {}\n"+
                "  void test(D obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test3.E",
                "package test3;\n"+
                "public class E extends D {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.E obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRefRef7() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C obj) {}\n"+
                "  public void test(D obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(Object obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No method is applicable (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testOneArgRefRef8() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  void test(C obj) {}\n"+
                "  public void test(D obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No accessible method is applicable (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testOneArgSerializablePrimArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(java.io.Serializable value) {}\n"+
                "  public void test(int[] value) {}\n"+
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
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgSerializablePrimArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(java.io.Serializable value) {}\n"+
                "  public void test(int[] value) {}\n"+
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

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgSerializablePrimArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(java.io.Serializable value) {}\n"+
                "  void test(int[] value) {}\n"+
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

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgSerializablePrimArray4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(java.io.Serializable value) {}\n"+
                "  public void test(int[] value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(short[] value) {\n"+
                "    _attr.test(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgSerializableRefArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(java.io.Serializable obj) {}\n"+
                "  public void test(C[][] obj) {}\n"+
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
                "  private void doSomething(java.io.Serializable obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgSerializableRefArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(java.io.Serializable obj) {}\n"+
                "  public void test(C[][] obj) {}\n"+
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
                "  private void doSomething(test3.C[][] obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgSerializableRefArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(java.io.Serializable obj) {}\n"+
                "  void test(C[][] obj) {}\n"+
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
                "  private void doSomething(test3.C[][] obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgSerializableRefArray4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(java.io.Serializable obj) {}\n"+
                "  public void test(C[][] obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.D[][] obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgSerializableRefArray5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(java.io.Serializable obj) {}\n"+
                "  void test(C[][] obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.D[][] obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testOneArgSerializableRefArray6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  void test(java.io.Serializable obj) {}\n"+
                "  public void test(D[][] obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C[][] obj) {\n"+
                "    _attr.test(obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No accessible method is applicable (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgObjectClonable1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(Object obj1, int[] obj2) {}\n"+
                "  public void test(C[] obj1, Clonable obj2) {}\n"+
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
                "  private void doSomething(Object obj1, int[] obj2) {\n"+
                "    _attr.test(obj1, obj2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgObjectClonable2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(Object obj1, int[] obj2) {}\n"+
                "  public void test(C[] obj1, Clonable obj2) {}\n"+
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
                "  private void doSomething(test3.C[] obj1, Clonable obj2) {\n"+
                "    _attr.test(obj1, obj2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgObjectClonable3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(Object obj1, int[] obj2) {}\n"+
                "  public void test(C[] obj1, Clonable obj2) {}\n"+
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
                "  private void doSomething(Object obj1, Clonable obj2) {\n"+
                "    _attr.test(obj1, obj2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No method is applicable (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgObjectClonable4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(Object obj1, int[] obj2) {}\n"+
                "  public void test(C[] obj1, Clonable obj2) {}\n"+
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
                "  private void doSomething(test3.C[] obj1, int[] obj2) {\n"+
                "    _attr.test(obj1, obj2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Ambiguous (would not compile); we find the nearest most specific definition
        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgPrimRef1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(int value, C obj) {}\n"+
                "  public void test(float value, D obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(int value, test3.C obj) {\n"+
                "    _attr.test(value, obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgPrimRef2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(int value, C obj) {}\n"+
                "  public void test(float value, D obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(float value, test3.D obj) {\n"+
                "    _attr.test(value, obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgPrimRef3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(int value, C obj) {}\n"+
                "  public void test(float value, D obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(int value, test3.D obj) {\n"+
                "    _attr.test(value, obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgPrimRef4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(int value, C obj) {}\n"+
                "  public void test(float value, D obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(float value, test3.C obj) {\n"+
                "    _attr.test(value, obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No method is applicable (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgPrimRef5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  void test(int value, C obj) {}\n"+
                "  public void test(float value, D obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(int value, test3.C obj) {\n"+
                "    _attr.test(value, obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No accessible method is applicable (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgPrimRef6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(int value, C obj) {}\n"+
                "  public void test(float value, D obj) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test3.E",
                "package test3;\n"+
                "public class E extends D {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(short value, test3.E obj) {\n"+
                "    _attr.test(value, obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Ambiguous (would not compile); we find the nearest most specific definition
        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgRefArrayPrimArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C[][] obj1, Clonable obj2) {}\n"+
                "  public void test(D[][] obj1, int[] obj2) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C[][] obj1, Clonable obj2) {\n"+
                "    _attr.test(obj1, obj2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgRefArrayPrimArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C[][] obj1, Clonable obj2) {}\n"+
                "  public void test(D[][] obj1, int[] obj2) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.D[][] obj1, int[] obj2) {\n"+
                "    _attr.test(obj1, obj2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgRefArrayPrimArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C[][] obj1, Clonable obj2) {}\n"+
                "  void test(D[][] obj1, int[] obj2) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.D[][] obj1, int[] obj2) {\n"+
                "    _attr.test(obj1, obj2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgRefArrayPrimArray4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C[][] obj1, Clonable obj2) {}\n"+
                "  public void test(E[][] obj1, int[] obj2) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test3.E",
                "package test3;\n"+
                "public class E extends D {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.D[][] obj1, int[] obj2) {\n"+
                "    _attr.test(obj1, obj2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgRefArrayPrimArray5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C[][] obj1, Clonable obj2) {}\n"+
                "  public void test(D[][] obj1, int[] obj2) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test3.E",
                "package test3;\n"+
                "public class E extends D {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.E[][] obj1, int[] obj2) {\n"+
                "    _attr.test(obj1, obj2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgRefArrayPrimArray6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C[][] obj1, Clonable obj2) {}\n"+
                "  void test(D[][] obj1, int[] obj2) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test3.E",
                "package test3;\n"+
                "public class E extends D {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.E[][] obj1, int[] obj2) {\n"+
                "    _attr.test(obj1, obj2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgRefArrayPrimArray7() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(D[][] obj1, Clonable obj2) {}\n"+
                "  public void test(E[][] obj1, int[] obj2) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test3.E",
                "package test3;\n"+
                "public class E extends D {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C[][] obj1, int[] obj2) {\n"+
                "    _attr.test(obj1, obj2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No method is applicable (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgRefArrayPrimArray8() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  void test(C[][] obj1, Clonable obj2) {}\n"+
                "  public void test(E[][] obj1, int[] obj2) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test3.E",
                "package test3;\n"+
                "public class E extends D {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.D[][] obj1, int[] obj2) {\n"+
                "    _attr.test(obj1, obj2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No accessible method is applicable (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgRefRef1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C obj1, E obj2) {}\n"+
                "  public void test(D obj1, F obj2) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test3.E",
                "package test3;\n"+
                "public class E {}\n",
                false);
        addType("test3.F",
                "package test3;\n"+
                "public class F extends E {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C obj1, test3.E obj2) {\n"+
                "    _attr.test(obj1, obj2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgRefRef2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C obj1, E obj2) {}\n"+
                "  public void test(D obj1, F obj2) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test3.E",
                "package test3;\n"+
                "public class E {}\n",
                false);
        addType("test3.F",
                "package test3;\n"+
                "public class F extends E {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.D obj1, test3.F obj2) {\n"+
                "    _attr.test(obj1, obj2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgRefRef3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C obj1, E obj2) {}\n"+
                "  void test(D obj1, F obj2) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test3.E",
                "package test3;\n"+
                "public class E {}\n",
                false);
        addType("test3.F",
                "package test3;\n"+
                "public class F extends E {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.D obj1, test3.F obj2) {\n"+
                "    _attr.test(obj1, obj2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgRefRef4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C obj1, F obj2) {}\n"+
                "  public void test(E obj1, H obj2) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test3.E",
                "package test3;\n"+
                "public class E extends D {}\n",
                false);
        addType("test3.F",
                "package test3;\n"+
                "public class F {}\n",
                false);
        addType("test3.G",
                "package test3;\n"+
                "public class G extends F {}\n",
                false);
        addType("test3.H",
                "package test3;\n"+
                "public class H extends G {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.D obj1, test3.G obj2) {\n"+
                "    _attr.test(obj1, obj2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgRefRef5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C obj1, F obj2) {}\n"+
                "  public void test(D obj1, G obj2) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test3.E",
                "package test3;\n"+
                "public class E extends D {}\n",
                false);
        addType("test3.F",
                "package test3;\n"+
                "public class F {}\n",
                false);
        addType("test3.G",
                "package test3;\n"+
                "public class G extends F {}\n",
                false);
        addType("test3.H",
                "package test3;\n"+
                "public class H extends G {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.E obj1, test3.H obj2) {\n"+
                "    _attr.test(obj1, obj2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgRefRef6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(C obj1, F obj2) {}\n"+
                "  void test(D obj1, G obj2) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test3.E",
                "package test3;\n"+
                "public class E extends D {}\n",
                false);
        addType("test3.F",
                "package test3;\n"+
                "public class F {}\n",
                false);
        addType("test3.G",
                "package test3;\n"+
                "public class G extends F {}\n",
                false);
        addType("test3.H",
                "package test3;\n"+
                "public class H extends G {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.E obj1, test3.H obj2) {\n"+
                "    _attr.test(obj1, obj2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgRefRef7() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public void test(D obj1, G obj2) {}\n"+
                "  public void test(E obj1, H obj2) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test3.E",
                "package test3;\n"+
                "public class E extends D {}\n",
                false);
        addType("test3.F",
                "package test3;\n"+
                "public class F {}\n",
                false);
        addType("test3.G",
                "package test3;\n"+
                "public class G extends F {}\n",
                false);
        addType("test3.H",
                "package test3;\n"+
                "public class H extends G {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C obj1, test3.F obj2) {\n"+
                "    _attr.test(obj1, obj2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No method is applicable (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgRefRef8() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  void test(C obj1, E obj2) {}\n"+
                "  public void test(D obj1, F obj2) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public class C {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends C {}\n",
                false);
        addType("test3.E",
                "package test3;\n"+
                "public class E {}\n",
                false);
        addType("test3.F",
                "package test3;\n"+
                "public class F extends E {}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(test3.C obj1, test3.E obj2) {\n"+
                "    _attr.test(obj1, obj2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No accessible method is applicable (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimPrim1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(short value1, long value2) {}\n"+
                "  public void test(int value1, float value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(short value1, long value2) {\n"+
                "    _attr.test(value1, value2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimPrim2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(short value1, long value2) {}\n"+
                "  public void test(int value1, float value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(int value1, float value2) {\n"+
                "    _attr.test(value1, value2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimPrim3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  void test(short value1, long value2) {}\n"+
                "  public void test(int value1, float value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(short value1, long value2) {\n"+
                "    _attr.test(value1, value2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimPrim4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(byte value1, int value2) {}\n"+
                "  public void test(int value1, float value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(short value1, long value2) {\n"+
                "    _attr.test(value1, value2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimPrim5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(short value1, long value2) {}\n"+
                "  public void test(int value1, float value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(byte value1, int value2) {\n"+
                "    _attr.test(value1, value2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimPrim6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  void test(short value1, long value2) {}\n"+
                "  public void test(int value1, float value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(byte value1, int value2) {\n"+
                "    _attr.test(value1, value2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(classDeclA.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimPrim7() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(short value1, long value2) {}\n"+
                "  public void test(int value1, float value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(long value1, double value2) {\n"+
                "    _attr.test(value1, value2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No method is applicable (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }

    public void testTwoArgsPrimPrim8() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public void test(byte value1, int value2) {}\n"+
                "  void test(int value1, float value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "  private void doSomething(short value1, long value2) {\n"+
                "    _attr.test(value1, value2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // No accessible method is applicable (would not compile) !
        assertNull(methodInvoc.getMethodDeclaration());
    }
}
