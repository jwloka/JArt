package jast.test.resolver;
import jast.ParseException;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.TypeDeclaration;
import antlr.ANTLRException;
public class TestMethodDepth extends TestBase
{

    public TestMethodDepth(String name)
    {
        super(name);
    }

    public void testLocal_Current1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void test(int value) {}\n"+
                "  private void test(float value) {}\n"+
                "  private void doSomething() {\n"+
                "    test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(2);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_Current2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void test(int value) {}\n"+
                "  private void test(float value) {}\n"+
                "  private void doSomething() {\n"+
                "    test(0.0f);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(2);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_Current3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void test(int value) {}\n"+
                "  private void test(float value) {}\n"+
                "  private void doSomething() {\n"+
                "    test(0l);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(2);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_CurrentVsBase1() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_CurrentVsEnclosing1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public void test(float value) {}\n"+
                "    public class C {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_CurrentVsEnclosingBase1() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_CurrentVsTopLevel1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(float value) {}\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_CurrentVsTopLevelBase1() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_Base1() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends B {\n"+
                "  private void doSomething() {\n"+
                "    test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.B").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_Base2() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends B {\n"+
                "  private void doSomething() {\n"+
                "    test(0.0f);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.B").getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_Base3() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends B {\n"+
                "  private void doSomething() {\n"+
                "    test(0l);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.B").getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_Base4() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  private void test(int value) {}\n"+
                "  public  void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends B {\n"+
                "  private void doSomething() {\n"+
                "    test(0l);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.B").getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosing1() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public void test(float value) {}\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Would not compile - we find the nearest applicable
        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosing10() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public void test(int value) {}\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.A.B").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosing2() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public void test(float value) {}\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.A.B").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosing3() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public void test(byte value) {}\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Would not compile - we find the nearest applicable
        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosing4() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public void test(byte value) {}\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Not applicable
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosing5() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public void test(int value) {}\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Would not compile - we find the nearest applicable
        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosing6() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public void test(int value) {}\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.A.B").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosing7() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public void test(int value) {}\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Would not compile (D defines a method with the name which is not applicable)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosing8() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public void test(int value) {}\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.A.B").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosing9() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public void test(int value) {}\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Would not compile - we find the nearest applicable
        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosingBase1() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends E {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosingBase10() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends E {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // not applicable
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosingBase11() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends E {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Would not compile (method in D is not applicable) -> we find nearest applicable
        assertEquals(_project.getType("test.E").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosingBase12() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends E {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // not applicable
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosingBase13() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends E {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosingBase14() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends E {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.E").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosingBase15() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends E {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosingBase2() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends E {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.E").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosingBase3() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  private void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends E {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosingBase4() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends E {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosingBase5() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends E {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Not applicable (would not compile)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosingBase6() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  private void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends E {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosingBase7() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends E {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosingBase8() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends E {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.E").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsEnclosingBase9() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends E {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevel1() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(float value) {}\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Would not compile - we find the nearest applicable
        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevel10() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.A").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevel2() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(float value) {}\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.A").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevel3() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(byte value) {}\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Would not compile - we find the nearest applicable
        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevel4() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(byte value) {}\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Not applicable
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevel5() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Would not compile - we find the nearest applicable
        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevel6() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.A").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevel7() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Would not compile (D defines a method with the name which is not applicable)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevel8() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.A").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevel9() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Would not compile - we find the nearest applicable
        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevelBase1() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevelBase10() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // not applicable
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevelBase11() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Would not compile (method in D is not applicable) -> we find nearest applicable
        assertEquals(_project.getType("test.E").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevelBase12() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // not applicable
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevelBase13() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevelBase14() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.E").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevelBase15() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevelBase2() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.E").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevelBase3() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  private void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevelBase4() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevelBase5() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Not applicable (would not compile)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevelBase6() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  private void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevelBase7() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevelBase8() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.E").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_BaseVsTopLevelBase9() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsBase10() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      public void test(float value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsBase2() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsBase3() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsBase4() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsBase5() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsBase6() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsBase7() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      public void test(byte value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsBase8() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      public void test(byte value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // would not compile !
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsBase9() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public class C extends D {\n"+
                "      public void test(float value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Ambiguous (would not compile) - we find the nearest method
        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsEnclosing2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public void test(byte value) {}\n"+
                "    public class C {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsEnclosing3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public void test(int value) {}\n"+
                "    public class C {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsEnclosing4() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public void test(int value) {}\n"+
                "    public class C {\n"+
                "      public void test(byte value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Not applicable (would not compile)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsEnclosing5() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public void test(int value) {}\n"+
                "    public class C {\n"+
                "      public void test(float value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsEnclosingBase10() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      public void test(float value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsEnclosingBase2() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsEnclosingBase3() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsEnclosingBase4() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsEnclosingBase5() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsEnclosingBase6() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsEnclosingBase7() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      public void test(byte value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Not applicable (would not compile)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsEnclosingBase8() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      public void test(byte value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Not applicable (would not compile)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsEnclosingBase9() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      public void test(float value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsTopLevel2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(byte value) {}\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsTopLevel3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsTopLevel4() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      public void test(byte value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Not applicable (would not compile)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsTopLevel5() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      public void test(float value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsTopLevelBase10() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      public void test(float value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsTopLevelBase2() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsTopLevelBase3() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsTopLevelBase4() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsTopLevelBase5() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsTopLevelBase6() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      public void test(int value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsTopLevelBase7() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      public void test(byte value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Not applicable (would not compile)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsTopLevelBase8() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      public void test(byte value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Not applicable (would not compile)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_CurrentVsTopLevelBase9() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      public void test(float value) {}\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl  = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_Enclosing1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    private void test(int value) {}\n"+
                "    private void test(float value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B");
        MethodDeclaration   methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_Enclosing2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    private void test(int value) {}\n"+
                "    private void test(float value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0.0f);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B");
        MethodDeclaration   methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_Enclosing3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    private void test(int value) {}\n"+
                "    private void test(float value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0l);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B");
        MethodDeclaration   methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBase1() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBase2() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0.0f);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBase3() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0l);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBase4() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevel1() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(float value) {}\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Would not compile - we find the nearest applicable
        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevel10() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(float value) {}\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.A").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevel2() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(float value) {}\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.A").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevel3() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(byte value) {}\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Would not compile - we find the nearest applicable
        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevel4() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(byte value) {}\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Not applicable (would not compile)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevel5() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Would not compile - we find the nearest applicable
        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevel6() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.A").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevel7() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Would not compile (D defines a method with the name which is not applicable)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevel8() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Would not compile - we find the nearest applicable
        assertEquals(_project.getType("test.A").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevel9() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(float value) {}\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Would not compile - we find the nearest applicable
        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevelBase1() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevelBase10() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // not applicable
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevelBase11() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Would not compile (method in D is not applicable) -> we find nearest applicable
        assertEquals(_project.getType("test.E").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevelBase12() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // not applicable
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevelBase13() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevelBase14() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.E").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevelBase15() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevelBase2() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.E").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevelBase3() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  private void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevelBase4() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevelBase5() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Not applicable (would not compile)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevelBase6() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  private void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevelBase7() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevelBase8() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.E").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingBaseVsTopLevelBase9() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends E {\n"+
                "  public class B extends D {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsEnclosingBase1() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    private void test(int value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B");
        MethodDeclaration   methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsEnclosingBase10() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    private void test(float value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B");
        MethodDeclaration   methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsEnclosingBase2() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    private void test(int value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B");
        MethodDeclaration   methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsEnclosingBase3() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    private void test(int value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B");
        MethodDeclaration   methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsEnclosingBase4() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    private void test(int value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B");
        MethodDeclaration   methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsEnclosingBase5() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    private void test(int value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B");
        MethodDeclaration   methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsEnclosingBase6() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    private void test(int value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B");
        MethodDeclaration   methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsEnclosingBase7() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    private void test(byte value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsEnclosingBase8() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    private void test(byte value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Not applicable (would not compile)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsEnclosingBase9() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B extends D {\n"+
                "    private void test(float value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B");
        MethodDeclaration   methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Ambiguous (would not compile) - we find the nearest applicable
        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsTopLevel1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(byte value) {}\n"+
                "  public class B {\n"+
                "    public void test(int value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B");
        MethodDeclaration   methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsTopLevel2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(float value) {}\n"+
                "  public class B {\n"+
                "    public void test(int value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B");
        MethodDeclaration   methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsTopLevel3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "  public class B {\n"+
                "    public void test(int value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B");
        MethodDeclaration   methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsTopLevel4() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "  public class B {\n"+
                "    public void test(byte value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Not applicable (would not compile)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsTopLevel5() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test(int value) {}\n"+
                "  public class B {\n"+
                "    public void test(float value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B");
        MethodDeclaration   methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsTopLevelBase1() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  public class B {\n"+
                "    private void test(int value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B");
        MethodDeclaration   methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsTopLevelBase10() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  public class B {\n"+
                "    private void test(float value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B");
        MethodDeclaration   methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsTopLevelBase2() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  public class B {\n"+
                "    private void test(int value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B");
        MethodDeclaration   methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsTopLevelBase3() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  public class B {\n"+
                "    private void test(int value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B");
        MethodDeclaration   methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsTopLevelBase4() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  public class B {\n"+
                "    private void test(int value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B");
        MethodDeclaration   methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsTopLevelBase5() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  public class B {\n"+
                "    private void test(int value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B");
        MethodDeclaration   methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsTopLevelBase6() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  public class B {\n"+
                "    private void test(int value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B");
        MethodDeclaration   methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsTopLevelBase7() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  public class B {\n"+
                "    private void test(byte value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Not applicable (would not compile)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsTopLevelBase8() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  public class B {\n"+
                "    private void test(byte value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Not applicable (would not compile)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_EnclosingVsTopLevelBase9() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  public class B {\n"+
                "    private void test(float value) {}\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B");
        MethodDeclaration   methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Ambiguous (would not compile) - we find the nearest applicable
        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_TopLevel1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void test(int value) {}\n"+
                "  private void test(float value) {}\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.A").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_TopLevel2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void test(int value) {}\n"+
                "  private void test(float value) {}\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0.0f);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.A").getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_TopLevel3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void test(int value) {}\n"+
                "  private void test(float value) {}\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0l);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.A").getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_TopLevelVsTopLevelBase1() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  private void test(int value) {}\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.A").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_TopLevelVsTopLevelBase10() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  private void test(float value) {}\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.A").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_TopLevelVsTopLevelBase2() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(float value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  private void test(int value) {}\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.A").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_TopLevelVsTopLevelBase3() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  private void test(int value) {}\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.A").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_TopLevelVsTopLevelBase4() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  private void test(int value) {}\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.A").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_TopLevelVsTopLevelBase5() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  private void test(int value) {}\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.A").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_TopLevelVsTopLevelBase6() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  private void test(int value) {}\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.A").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_TopLevelVsTopLevelBase7() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  private void test(byte value) {}\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testLocal_TopLevelVsTopLevelBase8() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  private void test(byte value) {}\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Not applicable (would not compile)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testLocal_TopLevelVsTopLevelBase9() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  private void test(float value) {}\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      private void doSomething() {\n"+
                "        test(0);\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Ambiguous (would not compile) -> we find nearest applicable
        assertEquals(_project.getType("test.A").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_Accessed1() throws ANTLRException, ParseException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                true);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private test2.B _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test1.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.B").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_Accessed2() throws ANTLRException, ParseException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                true);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private test2.B _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0.0f);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test1.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.B").getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_Accessed3() throws ANTLRException, ParseException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                true);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private test2.B _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0l);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test1.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.B").getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_Accessed4() throws ANTLRException, ParseException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                true);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private test2.B _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test1.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.B").getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsBase1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private test2.C _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test3.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.C").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsBase10() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private test2.C _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test3.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test1.B").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsBase11() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  private void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private test2.C _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test3.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test1.B").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsBase12() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private test2.C _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test3.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Not applicable (would not compile)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsBase13() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private test2.C _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test3.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Ambiguous (would not compile) - we find the nearest applicable
        assertEquals(_project.getType("test2.C").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsBase14() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  private void test(float value) {}\n"+
                "}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private test2.C _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test3.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test1.B").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsBase15() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private test2.C _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test3.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.C").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsBase2() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private test2.C _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test3.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test1.B").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsBase3() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  private void test(float value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private test2.C _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test3.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.C").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsBase4() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private test2.C _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test3.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.C").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsBase5() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private test2.C _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test3.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Not applicable (would not compile)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsBase6() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  private void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private test2.C _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test3.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.C").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsBase7() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private test2.C _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test3.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.C").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsBase8() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private test2.C _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test3.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Weaker modifier in redefinition (would not compile) -
        // we find nearest accessible
        assertEquals(_project.getType("test1.B").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsBase9() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private test2.C _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test3.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.C").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsRemoteBase1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test3.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsRemoteBase10() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test1.B").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsRemoteBase11() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {\n"+
                "  private void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test1.B").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsRemoteBase12() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Not applicable (would not compile)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsRemoteBase13() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Ambiguous (would not compile) - we find the nearest applicable
        assertEquals(_project.getType("test3.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsRemoteBase14() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {\n"+
                "  private void test(float value) {}\n"+
                "}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test1.B").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsRemoteBase15() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test3.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsRemoteBase2() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test1.B").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsRemoteBase3() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  private void test(float value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test3.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsRemoteBase4() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test3.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsRemoteBase5() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Not applicable (would not compile)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsRemoteBase6() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  private void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test3.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsRemoteBase7() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test3.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsRemoteBase8() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Weaker modifier in redefinition (would not compile) -
        // we find nearest accessible
        assertEquals(_project.getType("test1.B").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_AccessedVsRemoteBase9() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test3.D").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_Base1() throws ANTLRException, ParseException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                true);
        addType("test3.C",
                "package test3;\n"+
                "public class C extends test2.B {}\n",
                true);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private test3.C _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test1.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.B").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_Base2() throws ANTLRException, ParseException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                true);
        addType("test3.C",
                "package test3;\n"+
                "public class C extends test2.B {}\n",
                true);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private test3.C _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0.0f);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test1.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.B").getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_Base3() throws ANTLRException, ParseException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                true);
        addType("test3.C",
                "package test3;\n"+
                "public class C extends test2.B {}\n",
                true);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private test3.C _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0l);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test1.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.B").getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_Base4() throws ANTLRException, ParseException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                true);
        addType("test3.C",
                "package test3;\n"+
                "public class C extends test2.B {}\n",
                true);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private test3.C _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test1.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.B").getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_BaseVsRemoteBase1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.C").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_BaseVsRemoteBase10() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test1.B").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_BaseVsRemoteBase11() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  private void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test1.B").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_BaseVsRemoteBase12() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Not applicable (would not compile)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testRemote_BaseVsRemoteBase13() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Ambiguous (would not compile) - we find the nearest applicable
        assertEquals(_project.getType("test2.C").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_BaseVsRemoteBase14() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  private void test(float value) {}\n"+
                "}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test1.B").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_BaseVsRemoteBase15() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.C").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_BaseVsRemoteBase2() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test1.B").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_BaseVsRemoteBase3() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  private void test(float value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.C").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_BaseVsRemoteBase4() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.C").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_BaseVsRemoteBase5() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Not applicable (would not compile)
        assertNull(methodInvoc.getMethodDeclaration());
    }



    public void testRemote_BaseVsRemoteBase6() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  private void test(byte value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.C").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_BaseVsRemoteBase7() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.C").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_BaseVsRemoteBase8() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        // Private modifier in redefinition (would not compile) -
        // we find nearest accessible
        assertEquals(_project.getType("test1.B").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_BaseVsRemoteBase9() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  private void test(int value) {}\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  public void test(int value) {}\n"+
                "}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {}\n",
                false);
        addType("test4.A",
                "package test4;\n"+
                "public class A {\n"+
                "  private test3.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test4.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.C").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_RemoteBase1() throws ANTLRException, ParseException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                true);
        addType("test3.C",
                "package test3;\n"+
                "public class C extends test2.B {}\n",
                true);
        addType("test4.D",
                "package test4;\n"+
                "public class D extends test3.C {}\n",
                true);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private test4.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test1.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.B").getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_RemoteBase2() throws ANTLRException, ParseException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                true);
        addType("test3.C",
                "package test3;\n"+
                "public class C extends test2.B {}\n",
                true);
        addType("test4.D",
                "package test4;\n"+
                "public class D extends test3.C {}\n",
                true);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private test4.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0.0f);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test1.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.B").getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_RemoteBase3() throws ANTLRException, ParseException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  public void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                true);
        addType("test3.C",
                "package test3;\n"+
                "public class C extends test2.B {}\n",
                true);
        addType("test4.D",
                "package test4;\n"+
                "public class D extends test3.C {}\n",
                true);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private test4.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0l);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test1.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.B").getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }



    public void testRemote_RemoteBase4() throws ANTLRException, ParseException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  private void test(int value) {}\n"+
                "  public void test(float value) {}\n"+
                "}\n",
                true);
        addType("test3.C",
                "package test3;\n"+
                "public class C extends test2.B {}\n",
                true);
        addType("test4.D",
                "package test4;\n"+
                "public class D extends test3.C {}\n",
                true);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private test4.D _attr;\n"+
                "  private void doSomething() {\n"+
                "    _attr.test(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDecl  = _project.getType("test1.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(_project.getType("test2.B").getMethods().get(1),
                     methodInvoc.getMethodDeclaration());
    }
}
