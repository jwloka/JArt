package jast.test.resolver;

import jast.ParseException;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.SingleInitializer;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.VariableAccess;
import antlr.ANTLRException;

public class TestMethodAccessibility extends TestBase
{

    public TestMethodAccessibility(String name)
    {
        super(name);
    }

    public void testAnonClass_CurrentFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      void test() {}\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration          typeDecl      = _project.getType("test.A.B.C");
        MethodDeclaration        methodDecl1   = typeDecl.getMethods().get(0);
        MethodDeclaration        methodDecl2   = typeDecl.getMethods().get(1);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_CurrentPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void test() {}\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration          typeDecl      = _project.getType("test.A.B.C");
        MethodDeclaration        methodDecl1   = typeDecl.getMethods().get(0);
        MethodDeclaration        methodDecl2   = typeDecl.getMethods().get(1);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_CurrentProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      protected void test() {}\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration          typeDecl      = _project.getType("test.A.B.C");
        MethodDeclaration        methodDecl1   = typeDecl.getMethods().get(0);
        MethodDeclaration        methodDecl2   = typeDecl.getMethods().get(1);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_CurrentPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      public void test() {}\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration          typeDecl      = _project.getType("test.A.B.C");
        MethodDeclaration        methodDecl1   = typeDecl.getMethods().get(0);
        MethodDeclaration        methodDecl2   = typeDecl.getMethods().get(1);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_DirectBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  void test() {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test.D").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_DirectBaseProtected() throws ANTLRException, ParseException
    {
        addType("test2.D",
                "package test2;\n"+
                "public class D {\n"+
                "  protected void test() {}\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test2.D").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_DirectBasePublic() throws ANTLRException, ParseException
    {
        addType("test2.D",
                "package test2;\n"+
                "public class D {\n"+
                "  public void test() {}\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test2.D").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_EnclosingDirectBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  void test() {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B extends D {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test.D").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_EnclosingDirectBaseProtected() throws ANTLRException, ParseException
    {
        addType("test2.D",
                "package test2;\n"+
                "public class D {\n"+
                "  protected void test() {}\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B extends test2.D {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test2.D").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_EnclosingDirectBasePublic() throws ANTLRException, ParseException
    {
        addType("test2.D",
                "package test2;\n"+
                "public class D {\n"+
                "  public void test() {}\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B extends test2.D {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test2.D").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_EnclosingFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    void test() {}\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test.A.B").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_EnclosingPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    private void test() {}\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test.A.B").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_EnclosingProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    protected void test() {}\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test.A.B").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_EnclosingPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    public void test() {}\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test.A.B").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_Friendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          void test() {}\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(1);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl2,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_Private() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          private void test() {}\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(1);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl2,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_Protected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          protected void test() {}\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(1);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl2,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_Public() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void test() {}\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(1);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl2,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_RemoteBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  void test() {}\n"+
                "}\n",
                false);
        addType("test.D",
                "package test;\n"+
                "public class D extends E {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test.E").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_RemoteBaseProtected() throws ANTLRException, ParseException
    {
        addType("test3.E",
                "package test3;\n"+
                "public class E {\n"+
                "  protected void test() {}\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test3.E {}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test3.E").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_RemoteBasePublic() throws ANTLRException, ParseException
    {
        addType("test3.E",
                "package test3;\n"+
                "public class E {\n"+
                "  public void test() {}\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test3.E {}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test3.E").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_TopLevelDirectBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  void test() {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test.D").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_TopLevelDirectBaseProtected() throws ANTLRException, ParseException
    {
        addType("test2.D",
                "package test2;\n"+
                "public class D {\n"+
                "  protected void test() {}\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A extends test2.D {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test2.D").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_TopLevelDirectBasePublic() throws ANTLRException, ParseException
    {
        addType("test2.D",
                "package test2;\n"+
                "public class D {\n"+
                "  public void test() {}\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A extends test2.D {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test2.D").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_TopLevelFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  void test() {}\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test.A").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_TopLevelPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void test() {}\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test.A").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_TopLevelProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected void test() {}\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test.A").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClass_TopLevelPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test() {}\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test.A").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClassDirectBase_Friendly() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  void test() {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        D obj = new D() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test.D").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClassDirectBase_Protected() throws ANTLRException, ParseException
    {
        addType("test2.D",
                "package test2;\n"+
                "public class D {\n"+
                "  protected void test() {}\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        test2.D obj = new test2.D() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test2.D").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClassDirectBase_Public() throws ANTLRException, ParseException
    {
        addType("test2.D",
                "package test2;\n"+
                "public class D {\n"+
                "  public void test() {}\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        test2.D obj = new test2.D() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test2.D").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClassRemoteBase_Friendly() throws ANTLRException, ParseException
    {
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  void test() {}\n"+
                "}\n",
                false);
        addType("test.D",
                "package test;\n"+
                "public class D extends E {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        D obj = new D() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test.E").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClassRemoteBase_Protected() throws ANTLRException, ParseException
    {
        addType("test3.E",
                "package test3;\n"+
                "public class E {\n"+
                "  protected void test() {}\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test3.E {}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        test2.D obj = new test2.D() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test3.E").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testAnonClassRemoteBase_Public() throws ANTLRException, ParseException
    {
        addType("test3.E",
                "package test3;\n"+
                "public class E {\n"+
                "  public void test() {}\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test3.E {}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        test2.D obj = new test2.D() {\n"+
                "          public void doSomething() {\n"+
                "            test();\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test3.E").getMethods().get(0);
        MethodDeclaration        methodDecl2   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl3   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl3.getBody().getBlockStatements().get(0);
        MethodInvocation         methodInvoc   = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testExpression_CurrentFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      void test() {}\n"+
                "      private void doSomething() {\n"+
                "        C var = new C();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl1 = typeDecl.getMethods().get(0);
        MethodDeclaration   methodDecl2 = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
        assertEquals(typeDecl,
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_CurrentPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void test() {}\n"+
                "      private void doSomething() {\n"+
                "        C var = new C();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl1 = typeDecl.getMethods().get(0);
        MethodDeclaration   methodDecl2 = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
        assertEquals(typeDecl,
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_CurrentProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected class B {\n"+
                "    protected class C {\n"+
                "      protected void test() {}\n"+
                "      private void doSomething() {\n"+
                "        C var = new C();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl1 = typeDecl.getMethods().get(0);
        MethodDeclaration   methodDecl2 = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
        assertEquals(typeDecl,
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_CurrentPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      public void test() {}\n"+
                "      private void doSomething() {\n"+
                "        C var = new C();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl1 = typeDecl.getMethods().get(0);
        MethodDeclaration   methodDecl2 = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
        assertEquals(typeDecl,
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_DirectBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  void test() {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        C var = new C();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDeclD = _project.getType("test.D").getMethods().get(0);
        MethodDeclaration   methodDeclC = typeDecl.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
        assertEquals(typeDecl,
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_DirectBaseProtected() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected void test() {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.D {\n"+
                "      private void doSomething() {\n"+
                "        C var = new C();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test2.A.B.C");
        MethodDeclaration   methodDeclD = _project.getType("test1.D").getMethods().get(0);
        MethodDeclaration   methodDeclC = typeDecl.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
        assertEquals(typeDecl,
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_DirectBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public void test() {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.D {\n"+
                "      private void doSomething() {\n"+
                "        C var = new C();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test2.A.B.C");
        MethodDeclaration   methodDeclD = _project.getType("test1.D").getMethods().get(0);
        MethodDeclaration   methodDeclC = typeDecl.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
        assertEquals(typeDecl,
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_EnclosingDirectBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  void test() {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B extends D {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        B var = new B();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclD = _project.getType("test.D").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test.A.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_EnclosingDirectBaseProtected() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected void test() {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B extends test1.D {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        B var = new B();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclD = _project.getType("test1.D").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test2.A.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_EnclosingDirectBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public void test() {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B extends test1.D {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        B var = new B();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclD = _project.getType("test1.D").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test2.A.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_EnclosingFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    void test() {}\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        B var = new B();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclB = _project.getType("test.A.B").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclB,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test.A.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_EnclosingPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private void test() {}\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        B var = new B();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclB = _project.getType("test.A.B").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclB,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test.A.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_EnclosingProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected class B {\n"+
                "    private void test() {}\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        B var = new B();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclB = _project.getType("test.A.B").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclB,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test.A.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_EnclosingPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public void test() {}\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        B var = new B();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclB = _project.getType("test.A.B").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclB,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test.A.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_RemoteBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  void test() {}\n"+
                "}\n",
                false);
        addType("test.D",
                "package test;\n"+
                "public class D extends E {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        C var = new C();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclE = _project.getType("test.E").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclE,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test.A.B.C"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_RemoteBaseProtected() throws ANTLRException, ParseException
    {
        addType("test1.E",
                "package test1;\n"+
                "public class E {\n"+
                "  protected void test() {}\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.E {}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        C var = new C();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclE = _project.getType("test1.E").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test3.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclE,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test3.A.B.C"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_RemoteBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.E",
                "package test1;\n"+
                "public class E {\n"+
                "  public void test() {}\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.E {}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        C var = new C();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclE = _project.getType("test1.E").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test3.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclE,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test3.A.B.C"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_TopLevelDirectBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  void test() {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        A var = new A();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        MethodDeclaration   methodDeclD = _project.getType("test.D").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test.A"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_TopLevelDirectBaseProtected() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected void test() {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A extends test1.D {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        A var = new A();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclD = _project.getType("test1.D").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test2.A"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_TopLevelDirectBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public void test() {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A extends test1.D {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        A var = new A();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclD = _project.getType("test1.D").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test2.A"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_TopLevelFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  void test() {}\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        A var = new A();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclA = _project.getType("test.A").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test.A"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_TopLevelPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void test() {}\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        A var = new A();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclA = _project.getType("test.A").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test.A"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_TopLevelProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected void test() {}\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        A var = new A();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclA = _project.getType("test.A").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test.A"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_TopLevelPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test() {}\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        A var = new A();\n"+
                "        var.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclA = _project.getType("test.A").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test.A"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_UnrelatedFriendly1() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  void test() {}\n"+
                "}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    B var = new B();\n"+
                "    var.test();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclB = _project.getType("test.B").getMethods().get(0);
        MethodDeclaration   methodDeclA = _project.getType("test.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclA.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclB,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_UnrelatedFriendly2() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B extends C {}\n"+
                "class C {\n"+
                "  void test() {}\n"+
                "}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    B var = new B();\n"+
                "    var.test();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclC = _project.getType("test.C").getMethods().get(0);
        MethodDeclaration   methodDeclA = _project.getType("test.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclA.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclC,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_UnrelatedFriendly3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    void test() {}\n"+
                "  }\n"+
                "}\n",
                false);
        addType("test.C",
                "package test;\n"+
                "public class C {\n"+
                "  private void doSomething() {\n"+
                "    A.B var = new A().new B();\n"+
                "    var.test();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclB = _project.getType("test.A.B").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclB,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test.A.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_UnrelatedProtected1() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  protected void test() {}\n"+
                "}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    B var = new B();\n"+
                "    var.test();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclB = _project.getType("test.B").getMethods().get(0);
        MethodDeclaration   methodDeclA = _project.getType("test.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclA.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclB,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_UnrelatedProtected2() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "class B {\n"+
                "  protected void test() {}\n"+
                "}\n"+
                "public class C extends B {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    C var = new C();\n"+
                "    var.test();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclB = _project.getType("test.B").getMethods().get(0);
        MethodDeclaration   methodDeclA = _project.getType("test.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclA.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclB,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test.C"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_UnrelatedProtected3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected class B {\n"+
                "    protected void test() {}\n"+
                "  }\n"+
                "}\n",
                false);
        addType("test.C",
                "package test;\n"+
                "public class C {\n"+
                "  private void doSomething() {\n"+
                "    A.B var = new A().new B();\n"+
                "    var.test();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclB = _project.getType("test.A.B").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclB,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test.A.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_UnrelatedPublic1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test() {}\n"+
                "}\n",
                true);
        addType("test2.A",
                "package test2;\n"+
                "import test1.B;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    B var = new B();\n"+
                "    var.test();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclB = _project.getType("test1.B").getMethods().get(0);
        MethodDeclaration   methodDeclA = _project.getType("test2.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclA.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclB,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test1.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_UnrelatedPublic2() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test() {}\n"+
                "}\n",
                true);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {}\n",
                true);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    test2.C var = new test2.C();\n"+
                "    var.test();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclB = _project.getType("test1.B").getMethods().get(0);
        MethodDeclaration   methodDeclA = _project.getType("test3.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclA.getBody().getBlockStatements().get(1);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess      varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclB,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test2.C"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_UnrelatedPublic3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public void test() {}\n"+
                "  }\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C {\n"+
                "  private void doSomething() {\n"+
                "    test1.A.B var = new test1.A().new B();\n"+
                "    var.test();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDeclB = _project.getType("test1.A.B").getMethods().get(0);
        MethodDeclaration    methodDeclC = _project.getType("test2.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(1);
        MethodInvocation     methodInvoc = (MethodInvocation)exprStmt.getExpression();
        VariableAccess       varAccess   = (VariableAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclB,
                     methodInvoc.getMethodDeclaration());
        assertEquals(_project.getType("test1.A.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testLocal_CurrentFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      void test() {}\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl1 = typeDecl.getMethods().get(0);
        MethodDeclaration   methodDecl2 = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_CurrentPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void test() {}\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl1 = typeDecl.getMethods().get(0);
        MethodDeclaration   methodDecl2 = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_CurrentProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected class B {\n"+
                "    protected class C {\n"+
                "      protected void test() {}\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl1 = typeDecl.getMethods().get(0);
        MethodDeclaration   methodDecl2 = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_CurrentPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      public void test() {}\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl1 = typeDecl.getMethods().get(0);
        MethodDeclaration   methodDecl2 = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_DirectBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  void test() {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclD = _project.getType("test.D").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_DirectBaseProtected() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected void test() {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.D {\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclD = _project.getType("test1.D").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_DirectBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public void test() {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.D {\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclD = _project.getType("test1.D").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_EnclosingDirectBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  void test() {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B extends D {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclD = _project.getType("test.D").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_EnclosingDirectBaseProtected() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected void test() {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B extends test1.D {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclD = _project.getType("test1.D").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_EnclosingDirectBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public void test() {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B extends test1.D {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclD = _project.getType("test1.D").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_EnclosingFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    void test() {}\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclB = _project.getType("test.A.B").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclB,
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_EnclosingPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private void test() {}\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclB = _project.getType("test.A.B").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclB,
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_EnclosingProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected class B {\n"+
                "    protected void test() {}\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclB = _project.getType("test.A.B").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclB,
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_EnclosingPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public void test() {}\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclB = _project.getType("test.A.B").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclB,
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_RemoteBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  void test() {}\n"+
                "}\n",
                false);
        addType("test.D",
                "package test;\n"+
                "public class D extends E {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclE = _project.getType("test.E").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclE,
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_RemoteBaseProtected() throws ANTLRException, ParseException
    {
        addType("test1.E",
                "package test1;\n"+
                "public class E {\n"+
                "  protected void test() {}\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.E {}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclE = _project.getType("test1.E").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test3.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclE,
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_RemoteBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.E",
                "package test1;\n"+
                "public class E {\n"+
                "  public void test() {}\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.E {}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclE = _project.getType("test1.E").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test3.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclE,
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_TopLevelDirectBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  void test() {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclD = _project.getType("test.D").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_TopLevelDirectBaseProtected() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected void test() {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A extends test1.D {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclD = _project.getType("test1.D").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_TopLevelDirectBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public void test() {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A extends test1.D {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclD = _project.getType("test1.D").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_TopLevelFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  void test() {}\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclA = _project.getType("test.A").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_TopLevelPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void test() {}\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclA = _project.getType("test.A").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_TopLevelProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected void test() {}\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclA = _project.getType("test.A").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testLocal_TopLevelPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test() {}\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclA = _project.getType("test.A").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
    }

    public void testSuper_DirectBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  void test() {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        super.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclD = _project.getType("test.D").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
    }

    public void testSuper_DirectBaseProtected() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected void test() {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.D {\n"+
                "      private void doSomething() {\n"+
                "        super.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclD = _project.getType("test1.D").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
    }

    public void testSuper_DirectBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public void test() {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.D {\n"+
                "      private void doSomething() {\n"+
                "        super.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclD = _project.getType("test1.D").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
    }

    public void testSuper_RemoteBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  void test() {}\n"+
                "}\n",
                false);
        addType("test.D",
                "package test;\n"+
                "public class D extends E {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        super.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclE = _project.getType("test.E").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclE,
                     methodInvoc.getMethodDeclaration());
    }

    public void testSuper_RemoteBaseProtected() throws ANTLRException, ParseException
    {
        addType("test1.E",
                "package test1;\n"+
                "public class E {\n"+
                "  protected void test() {}\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.E {}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        super.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclE = _project.getType("test1.E").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test3.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclE,
                     methodInvoc.getMethodDeclaration());
    }

    public void testSuper_RemoteBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.E",
                "package test1;\n"+
                "public class E {\n"+
                "  public void test() {}\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.E {}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        super.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclE = _project.getType("test1.E").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test3.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclE,
                     methodInvoc.getMethodDeclaration());
    }

    public void testThis_CurrentFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      void test() {}\n"+
                "      private void doSomething() {\n"+
                "        this.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl1 = typeDecl.getMethods().get(0);
        MethodDeclaration   methodDecl2 = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testThis_CurrentPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void test() {}\n"+
                "      private void doSomething() {\n"+
                "        this.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl1 = typeDecl.getMethods().get(0);
        MethodDeclaration   methodDecl2 = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testThis_CurrentProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected class B {\n"+
                "    protected class C {\n"+
                "      protected void test() {}\n"+
                "      private void doSomething() {\n"+
                "        this.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl1 = typeDecl.getMethods().get(0);
        MethodDeclaration   methodDecl2 = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testThis_CurrentPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      public void test() {}\n"+
                "      private void doSomething() {\n"+
                "        this.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration   methodDecl1 = typeDecl.getMethods().get(0);
        MethodDeclaration   methodDecl2 = typeDecl.getMethods().get(1);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDecl1,
                     methodInvoc.getMethodDeclaration());
    }

    public void testThis_DirectBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  void test() {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        this.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclD = _project.getType("test.D").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
    }

    public void testThis_DirectBaseProtected() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected void test() {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.D {\n"+
                "      private void doSomething() {\n"+
                "        this.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclD = _project.getType("test1.D").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
    }

    public void testThis_DirectBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public void test() {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.D {\n"+
                "      private void doSomething() {\n"+
                "        this.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclD = _project.getType("test1.D").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclD,
                     methodInvoc.getMethodDeclaration());
    }

    public void testThis_RemoteBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  void test() {}\n"+
                "}\n",
                false);
        addType("test.D",
                "package test;\n"+
                "public class D extends E {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        this.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclE = _project.getType("test.E").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclE,
                     methodInvoc.getMethodDeclaration());
    }

    public void testThis_RemoteBaseProtected() throws ANTLRException, ParseException
    {
        addType("test1.E",
                "package test1;\n"+
                "public class E {\n"+
                "  protected void test() {}\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.E {}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        this.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclE = _project.getType("test1.E").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test3.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclE,
                     methodInvoc.getMethodDeclaration());
    }

    public void testThis_RemoteBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.E",
                "package test1;\n"+
                "public class E {\n"+
                "  public void test() {}\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.E {}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        this.test();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration   methodDeclE = _project.getType("test1.E").getMethods().get(0);
        MethodDeclaration   methodDeclC = _project.getType("test3.A.B.C").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclC.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();

        assertEquals(methodDeclE,
                     methodInvoc.getMethodDeclaration());
    }
}
