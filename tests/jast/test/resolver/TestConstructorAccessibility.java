package jast.test.resolver;

import jast.ParseException;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.ConstructorInvocation;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.SingleInitializer;
import antlr.ANTLRException;

public class TestConstructorAccessibility extends TestBase
{

    public TestConstructorAccessibility(String name)
    {
        super(name);
    }

    public void testNew_CurrentFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  A() {}\n"+
                "  private void doSomething() {\n"+
                "    A var = new A();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test.A");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclA.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testNew_CurrentPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private A() {}\n"+
                "  private void doSomething() {\n"+
                "    A var = new A();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test.A");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclA.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testNew_CurrentProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected A() {}\n"+
                "  private void doSomething() {\n"+
                "    A var = new A();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test.A");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclA.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testNew_CurrentPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public A() {}\n"+
                "  private void doSomething() {\n"+
                "    A var = new A();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test.A");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclA.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testNew_Default1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    A var = new A();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test.A");
        MethodDeclaration        methodDecl      = classDeclA.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // default constructor
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testNew_Default2() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  public B() {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends B {\n"+
                "  private void doSomething() {\n"+
                "    A var = new A();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test.A");
        MethodDeclaration        methodDecl      = classDeclA.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // default constructor
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testNew_EnclosingFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    B() {}\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        B var = new B();"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test.A.B");
        ConstructorDeclaration   constructorDecl = classDeclB.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getInnerTypes().get("C").getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testNew_EnclosingPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private B() {}\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        B var = new B();"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test.A.B");
        ConstructorDeclaration   constructorDecl = classDeclB.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getInnerTypes().get("C").getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testNew_EnclosingProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    protected B() {}\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        B var = new B();"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test.A.B");
        ConstructorDeclaration   constructorDecl = classDeclB.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getInnerTypes().get("C").getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testNew_EnclosingPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    public B() {}\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        B var = new B();"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test.A.B");
        ConstructorDeclaration   constructorDecl = classDeclB.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getInnerTypes().get("C").getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testNew_InnerOfCurrentFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    B() {}\n"+
                "    private class C {\n"+
                "      C() {}\n"+
                "    }\n"+
                "  }\n"+
                "  private void doSomething() {\n"+
                "    B.C var = new B().new C();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA       = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration         classDeclB       = (ClassDeclaration)classDeclA.getInnerTypes().get("B");
        ClassDeclaration         classDeclC       = (ClassDeclaration)classDeclB.getInnerTypes().get("C");
        ConstructorDeclaration   constructorDeclB = classDeclB.getConstructors().get(0);
        ConstructorDeclaration   constructorDeclC = classDeclC.getConstructors().get(0);
        MethodDeclaration        methodDecl       = classDeclA.getMethods().get(0);
        LocalVariableDeclaration localVarDecl     = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer      = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation1   = (Instantiation)initializer.getInitEpression();
        Instantiation            instantiation2   = (Instantiation)instantiation1.getBaseExpression();

        assertEquals(constructorDeclC,
                     instantiation1.getInvokedConstructor());
        assertEquals(constructorDeclB,
                     instantiation2.getInvokedConstructor());
    }

    public void testNew_InnerOfCurrentPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private B() {}\n"+
                "    private class C {\n"+
                "      private C() {}\n"+
                "    }\n"+
                "  }\n"+
                "  private void doSomething() {\n"+
                "    B.C var = new B().new C();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA       = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration         classDeclB       = (ClassDeclaration)classDeclA.getInnerTypes().get("B");
        ClassDeclaration         classDeclC       = (ClassDeclaration)classDeclB.getInnerTypes().get("C");
        ConstructorDeclaration   constructorDeclB = classDeclB.getConstructors().get(0);
        ConstructorDeclaration   constructorDeclC = classDeclC.getConstructors().get(0);
        MethodDeclaration        methodDecl       = classDeclA.getMethods().get(0);
        LocalVariableDeclaration localVarDecl     = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer      = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation1   = (Instantiation)initializer.getInitEpression();
        Instantiation            instantiation2   = (Instantiation)instantiation1.getBaseExpression();

        assertEquals(constructorDeclC,
                     instantiation1.getInvokedConstructor());
        assertEquals(constructorDeclB,
                     instantiation2.getInvokedConstructor());
    }

    public void testNew_InnerOfCurrentProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    protected B() {}\n"+
                "    private class C {\n"+
                "      protected C() {}\n"+
                "    }\n"+
                "  }\n"+
                "  private void doSomething() {\n"+
                "    B.C var = new B().new C();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA       = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration         classDeclB       = (ClassDeclaration)classDeclA.getInnerTypes().get("B");
        ClassDeclaration         classDeclC       = (ClassDeclaration)classDeclB.getInnerTypes().get("C");
        ConstructorDeclaration   constructorDeclB = classDeclB.getConstructors().get(0);
        ConstructorDeclaration   constructorDeclC = classDeclC.getConstructors().get(0);
        MethodDeclaration        methodDecl       = classDeclA.getMethods().get(0);
        LocalVariableDeclaration localVarDecl     = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer      = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation1   = (Instantiation)initializer.getInitEpression();
        Instantiation            instantiation2   = (Instantiation)instantiation1.getBaseExpression();

        assertEquals(constructorDeclC,
                     instantiation1.getInvokedConstructor());
        assertEquals(constructorDeclB,
                     instantiation2.getInvokedConstructor());
    }

    public void testNew_InnerOfCurrentPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    public B() {}\n"+
                "    private class C {\n"+
                "      public C() {}\n"+
                "    }\n"+
                "  }\n"+
                "  private void doSomething() {\n"+
                "    B.C var = new B().new C();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA       = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration         classDeclB       = (ClassDeclaration)classDeclA.getInnerTypes().get("B");
        ClassDeclaration         classDeclC       = (ClassDeclaration)classDeclB.getInnerTypes().get("C");
        ConstructorDeclaration   constructorDeclB = classDeclB.getConstructors().get(0);
        ConstructorDeclaration   constructorDeclC = classDeclC.getConstructors().get(0);
        MethodDeclaration        methodDecl       = classDeclA.getMethods().get(0);
        LocalVariableDeclaration localVarDecl     = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer      = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation1   = (Instantiation)initializer.getInitEpression();
        Instantiation            instantiation2   = (Instantiation)instantiation1.getBaseExpression();

        assertEquals(constructorDeclC,
                     instantiation1.getInvokedConstructor());
        assertEquals(constructorDeclB,
                     instantiation2.getInvokedConstructor());
    }

    public void testNew_InnerOfEnclosingFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      C() {}\n"+
                "      private class D {\n"+
                "        D() {}\n"+
                "      }\n"+
                "    }\n"+
                "    private class E {\n"+
                "      private void doSomething() {\n"+
                "        C.D var = new C().new D();"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclB       = (ClassDeclaration)_project.getType("test.A.B");
        ClassDeclaration         classDeclC       = (ClassDeclaration)classDeclB.getInnerTypes().get("C");
        ClassDeclaration         classDeclD       = (ClassDeclaration)classDeclC.getInnerTypes().get("D");
        ClassDeclaration         classDeclE       = (ClassDeclaration)classDeclB.getInnerTypes().get("E");
        ConstructorDeclaration   constructorDeclC = classDeclC.getConstructors().get(0);
        ConstructorDeclaration   constructorDeclD = classDeclD.getConstructors().get(0);
        MethodDeclaration        methodDecl       = classDeclE.getMethods().get(0);
        LocalVariableDeclaration localVarDecl     = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer      = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation1   = (Instantiation)initializer.getInitEpression();
        Instantiation            instantiation2   = (Instantiation)instantiation1.getBaseExpression();

        assertEquals(constructorDeclD,
                     instantiation1.getInvokedConstructor());
        assertEquals(constructorDeclC,
                     instantiation2.getInvokedConstructor());
    }

    public void testNew_InnerOfEnclosingPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private C() {}\n"+
                "      private class D {\n"+
                "        private D() {}\n"+
                "      }\n"+
                "    }\n"+
                "    private class E {\n"+
                "      private void doSomething() {\n"+
                "        C.D var = new C().new D();"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclB       = (ClassDeclaration)_project.getType("test.A.B");
        ClassDeclaration         classDeclC       = (ClassDeclaration)classDeclB.getInnerTypes().get("C");
        ClassDeclaration         classDeclD       = (ClassDeclaration)classDeclC.getInnerTypes().get("D");
        ClassDeclaration         classDeclE       = (ClassDeclaration)classDeclB.getInnerTypes().get("E");
        ConstructorDeclaration   constructorDeclC = classDeclC.getConstructors().get(0);
        ConstructorDeclaration   constructorDeclD = classDeclD.getConstructors().get(0);
        MethodDeclaration        methodDecl       = classDeclE.getMethods().get(0);
        LocalVariableDeclaration localVarDecl     = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer      = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation1   = (Instantiation)initializer.getInitEpression();
        Instantiation            instantiation2   = (Instantiation)instantiation1.getBaseExpression();

        assertEquals(constructorDeclD,
                     instantiation1.getInvokedConstructor());
        assertEquals(constructorDeclC,
                     instantiation2.getInvokedConstructor());
    }

    public void testNew_InnerOfEnclosingProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      protected C() {}\n"+
                "      private class D {\n"+
                "        private D() {}\n"+
                "      }\n"+
                "    }\n"+
                "    private class E {\n"+
                "      private void doSomething() {\n"+
                "        C.D var = new C().new D();"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclB       = (ClassDeclaration)_project.getType("test.A.B");
        ClassDeclaration         classDeclC       = (ClassDeclaration)classDeclB.getInnerTypes().get("C");
        ClassDeclaration         classDeclD       = (ClassDeclaration)classDeclC.getInnerTypes().get("D");
        ClassDeclaration         classDeclE       = (ClassDeclaration)classDeclB.getInnerTypes().get("E");
        ConstructorDeclaration   constructorDeclC = classDeclC.getConstructors().get(0);
        ConstructorDeclaration   constructorDeclD = classDeclD.getConstructors().get(0);
        MethodDeclaration        methodDecl       = classDeclE.getMethods().get(0);
        LocalVariableDeclaration localVarDecl     = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer      = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation1   = (Instantiation)initializer.getInitEpression();
        Instantiation            instantiation2   = (Instantiation)instantiation1.getBaseExpression();

        assertEquals(constructorDeclD,
                     instantiation1.getInvokedConstructor());
        assertEquals(constructorDeclC,
                     instantiation2.getInvokedConstructor());
    }

    public void testNew_InnerOfEnclosingPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      public C() {}\n"+
                "      private class D {\n"+
                "        public D() {}\n"+
                "      }\n"+
                "    }\n"+
                "    private class E {\n"+
                "      private void doSomething() {\n"+
                "        C.D var = new C().new D();"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclB       = (ClassDeclaration)_project.getType("test.A.B");
        ClassDeclaration         classDeclC       = (ClassDeclaration)classDeclB.getInnerTypes().get("C");
        ClassDeclaration         classDeclD       = (ClassDeclaration)classDeclC.getInnerTypes().get("D");
        ClassDeclaration         classDeclE       = (ClassDeclaration)classDeclB.getInnerTypes().get("E");
        ConstructorDeclaration   constructorDeclC = classDeclC.getConstructors().get(0);
        ConstructorDeclaration   constructorDeclD = classDeclD.getConstructors().get(0);
        MethodDeclaration        methodDecl       = classDeclE.getMethods().get(0);
        LocalVariableDeclaration localVarDecl     = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer      = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation1   = (Instantiation)initializer.getInitEpression();
        Instantiation            instantiation2   = (Instantiation)instantiation1.getBaseExpression();

        assertEquals(constructorDeclD,
                     instantiation1.getInvokedConstructor());
        assertEquals(constructorDeclC,
                     instantiation2.getInvokedConstructor());
    }

    public void testNew_InnerOfOtherPackagePublic() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public B() {}\n"+
                "  public class C {\n"+
                "    public C() {}\n"+
                "  }\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    B.C var = new B().new C();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclB       = (ClassDeclaration)_project.getType("test1.B");
        ClassDeclaration         classDeclC       = (ClassDeclaration)classDeclB.getInnerTypes().get("C");
        ConstructorDeclaration   constructorDeclB = classDeclB.getConstructors().get(0);
        ConstructorDeclaration   constructorDeclC = classDeclC.getConstructors().get(0);
        MethodDeclaration        methodDecl       = _project.getType("test2.A").getMethods().get(0);
        LocalVariableDeclaration localVarDecl     = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer      = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation1   = (Instantiation)initializer.getInitEpression();
        Instantiation            instantiation2   = (Instantiation)instantiation1.getBaseExpression();

        assertEquals(constructorDeclC,
                     instantiation1.getInvokedConstructor());
        assertEquals(constructorDeclB,
                     instantiation2.getInvokedConstructor());
    }

    public void testNew_InnerOfSamePackageFriendly() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  B() {}\n"+
                "  public class C {\n"+
                "    C() {}\n"+
                "  }\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    B.C var = new B().new C();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclB       = (ClassDeclaration)_project.getType("test.B");
        ClassDeclaration         classDeclC       = (ClassDeclaration)classDeclB.getInnerTypes().get("C");
        ConstructorDeclaration   constructorDeclB = classDeclB.getConstructors().get(0);
        ConstructorDeclaration   constructorDeclC = classDeclC.getConstructors().get(0);
        MethodDeclaration        methodDecl       = _project.getType("test.A").getMethods().get(0);
        LocalVariableDeclaration localVarDecl     = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer      = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation1   = (Instantiation)initializer.getInitEpression();
        Instantiation            instantiation2   = (Instantiation)instantiation1.getBaseExpression();

        assertEquals(constructorDeclC,
                     instantiation1.getInvokedConstructor());
        assertEquals(constructorDeclB,
                     instantiation2.getInvokedConstructor());
    }

    public void testNew_InnerOfSamePackageProtected() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  protected B() {}\n"+
                "  public class C {\n"+
                "    protected C() {}\n"+
                "  }\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    B.C var = new B().new C();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclB       = (ClassDeclaration)_project.getType("test.B");
        ClassDeclaration         classDeclC       = (ClassDeclaration)classDeclB.getInnerTypes().get("C");
        ConstructorDeclaration   constructorDeclB = classDeclB.getConstructors().get(0);
        ConstructorDeclaration   constructorDeclC = classDeclC.getConstructors().get(0);
        MethodDeclaration        methodDecl       = _project.getType("test.A").getMethods().get(0);
        LocalVariableDeclaration localVarDecl     = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer      = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation1   = (Instantiation)initializer.getInitEpression();
        Instantiation            instantiation2   = (Instantiation)instantiation1.getBaseExpression();

        assertEquals(constructorDeclC,
                     instantiation1.getInvokedConstructor());
        assertEquals(constructorDeclB,
                     instantiation2.getInvokedConstructor());
    }

    public void testNew_InnerOfSamePackagePublic() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  public B() {}\n"+
                "  public class C {\n"+
                "    public C() {}\n"+
                "  }\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    B.C var = new B().new C();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclB       = (ClassDeclaration)_project.getType("test.B");
        ClassDeclaration         classDeclC       = (ClassDeclaration)classDeclB.getInnerTypes().get("C");
        ConstructorDeclaration   constructorDeclB = classDeclB.getConstructors().get(0);
        ConstructorDeclaration   constructorDeclC = classDeclC.getConstructors().get(0);
        MethodDeclaration        methodDecl       = _project.getType("test.A").getMethods().get(0);
        LocalVariableDeclaration localVarDecl     = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer      = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation1   = (Instantiation)initializer.getInitEpression();
        Instantiation            instantiation2   = (Instantiation)instantiation1.getBaseExpression();

        assertEquals(constructorDeclC,
                     instantiation1.getInvokedConstructor());
        assertEquals(constructorDeclB,
                     instantiation2.getInvokedConstructor());
    }

    public void testNew_InnerOfTopLevelFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    B() {}\n"+
                "    private class C {\n"+
                "      C() {}\n"+
                "    }\n"+
                "  }\n"+
                "  private class D {\n"+
                "    private class E {\n"+
                "      private void doSomething() {\n"+
                "        B.C var = new B().new C();"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA       = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration         classDeclB       = (ClassDeclaration)classDeclA.getInnerTypes().get("B");
        ClassDeclaration         classDeclC       = (ClassDeclaration)classDeclB.getInnerTypes().get("C");
        ClassDeclaration         classDeclD       = (ClassDeclaration)classDeclA.getInnerTypes().get("D");
        ClassDeclaration         classDeclE       = (ClassDeclaration)classDeclD.getInnerTypes().get("E");
        ConstructorDeclaration   constructorDeclB = classDeclB.getConstructors().get(0);
        ConstructorDeclaration   constructorDeclC = classDeclC.getConstructors().get(0);
        MethodDeclaration        methodDecl       = classDeclE.getMethods().get(0);
        LocalVariableDeclaration localVarDecl     = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer      = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation1   = (Instantiation)initializer.getInitEpression();
        Instantiation            instantiation2   = (Instantiation)instantiation1.getBaseExpression();

        assertEquals(constructorDeclC,
                     instantiation1.getInvokedConstructor());
        assertEquals(constructorDeclB,
                     instantiation2.getInvokedConstructor());
    }

    public void testNew_InnerOfTopLevelPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private B() {}\n"+
                "    private class C {\n"+
                "      private C() {}\n"+
                "    }\n"+
                "  }\n"+
                "  private class D {\n"+
                "    private class E {\n"+
                "      private void doSomething() {\n"+
                "        B.C var = new B().new C();"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA       = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration         classDeclB       = (ClassDeclaration)classDeclA.getInnerTypes().get("B");
        ClassDeclaration         classDeclC       = (ClassDeclaration)classDeclB.getInnerTypes().get("C");
        ClassDeclaration         classDeclD       = (ClassDeclaration)classDeclA.getInnerTypes().get("D");
        ClassDeclaration         classDeclE       = (ClassDeclaration)classDeclD.getInnerTypes().get("E");
        ConstructorDeclaration   constructorDeclB = classDeclB.getConstructors().get(0);
        ConstructorDeclaration   constructorDeclC = classDeclC.getConstructors().get(0);
        MethodDeclaration        methodDecl       = classDeclE.getMethods().get(0);
        LocalVariableDeclaration localVarDecl     = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer      = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation1   = (Instantiation)initializer.getInitEpression();
        Instantiation            instantiation2   = (Instantiation)instantiation1.getBaseExpression();

        assertEquals(constructorDeclC,
                     instantiation1.getInvokedConstructor());
        assertEquals(constructorDeclB,
                     instantiation2.getInvokedConstructor());
    }

    public void testNew_InnerOfTopLevelProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    protected B() {}\n"+
                "    private class C {\n"+
                "      protected C() {}\n"+
                "    }\n"+
                "  }\n"+
                "  private class D {\n"+
                "    private class E {\n"+
                "      private void doSomething() {\n"+
                "        B.C var = new B().new C();"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA       = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration         classDeclB       = (ClassDeclaration)classDeclA.getInnerTypes().get("B");
        ClassDeclaration         classDeclC       = (ClassDeclaration)classDeclB.getInnerTypes().get("C");
        ClassDeclaration         classDeclD       = (ClassDeclaration)classDeclA.getInnerTypes().get("D");
        ClassDeclaration         classDeclE       = (ClassDeclaration)classDeclD.getInnerTypes().get("E");
        ConstructorDeclaration   constructorDeclB = classDeclB.getConstructors().get(0);
        ConstructorDeclaration   constructorDeclC = classDeclC.getConstructors().get(0);
        MethodDeclaration        methodDecl       = classDeclE.getMethods().get(0);
        LocalVariableDeclaration localVarDecl     = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer      = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation1   = (Instantiation)initializer.getInitEpression();
        Instantiation            instantiation2   = (Instantiation)instantiation1.getBaseExpression();

        assertEquals(constructorDeclC,
                     instantiation1.getInvokedConstructor());
        assertEquals(constructorDeclB,
                     instantiation2.getInvokedConstructor());
    }

    public void testNew_InnerOfTopLevelPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    public B() {}\n"+
                "    private class C {\n"+
                "      public C() {}\n"+
                "    }\n"+
                "  }\n"+
                "  private class D {\n"+
                "    private class E {\n"+
                "      private void doSomething() {\n"+
                "        B.C var = new B().new C();"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA       = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration         classDeclB       = (ClassDeclaration)classDeclA.getInnerTypes().get("B");
        ClassDeclaration         classDeclC       = (ClassDeclaration)classDeclB.getInnerTypes().get("C");
        ClassDeclaration         classDeclD       = (ClassDeclaration)classDeclA.getInnerTypes().get("D");
        ClassDeclaration         classDeclE       = (ClassDeclaration)classDeclD.getInnerTypes().get("E");
        ConstructorDeclaration   constructorDeclB = classDeclB.getConstructors().get(0);
        ConstructorDeclaration   constructorDeclC = classDeclC.getConstructors().get(0);
        MethodDeclaration        methodDecl       = classDeclE.getMethods().get(0);
        LocalVariableDeclaration localVarDecl     = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer      = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation1   = (Instantiation)initializer.getInitEpression();
        Instantiation            instantiation2   = (Instantiation)instantiation1.getBaseExpression();

        assertEquals(constructorDeclC,
                     instantiation1.getInvokedConstructor());
        assertEquals(constructorDeclB,
                     instantiation2.getInvokedConstructor());
    }

    public void testNew_OtherPackagePublic() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public B() {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    B var = new B();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test1.B");
        ConstructorDeclaration   constructorDecl = classDeclB.getConstructors().get(0);
        MethodDeclaration        methodDecl      = _project.getType("test2.A").getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testNew_SamePackageFriendly() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  B() {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    B var = new B();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test.B");
        ConstructorDeclaration   constructorDecl = classDeclB.getConstructors().get(0);
        MethodDeclaration        methodDecl      = _project.getType("test.A").getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testNew_SamePackageProtected() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  protected B() {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    B var = new B();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test.B");
        ConstructorDeclaration   constructorDecl = classDeclB.getConstructors().get(0);
        MethodDeclaration        methodDecl      = _project.getType("test.A").getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testNew_SamePackagePublic() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  public B() {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    B var = new B();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test.B");
        ConstructorDeclaration   constructorDecl = classDeclB.getConstructors().get(0);
        MethodDeclaration        methodDecl      = _project.getType("test.A").getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testNew_TopLevelFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  A() {}\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        A var = new A();"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)classDeclA.getInnerTypes().get("B");
        ClassDeclaration         classDeclC      = (ClassDeclaration)classDeclB.getInnerTypes().get("C");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclC.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testNew_TopLevelPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private A() {}\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        A var = new A();"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)classDeclA.getInnerTypes().get("B");
        ClassDeclaration         classDeclC      = (ClassDeclaration)classDeclB.getInnerTypes().get("C");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclC.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testNew_TopLevelProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected A() {}\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        A var = new A();"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)classDeclA.getInnerTypes().get("B");
        ClassDeclaration         classDeclC      = (ClassDeclaration)classDeclB.getInnerTypes().get("C");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclC.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testNew_TopLevelPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public A() {}\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        A var = new A();"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)classDeclA.getInnerTypes().get("B");
        ClassDeclaration         classDeclC      = (ClassDeclaration)classDeclB.getInnerTypes().get("C");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclC.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testSuper_Default() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends B {\n"+
                "  private A(int val) {\n"+
                "    super();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDeclA       = (ClassDeclaration)_project.getType("test.A");
        ConstructorDeclaration constructorDeclA = classDeclA.getConstructors().get(0);
        ExpressionStatement    exprStmt         = (ExpressionStatement)constructorDeclA.getBody().getBlockStatements().get(0);
        ConstructorInvocation  constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        // default constructor
        assertNull(constructorInvoc.getConstructorDeclaration());
    }

    public void testSuper_InnerFriendly1() throws ANTLRException, ParseException
    {
        addType("test.C",
                "package test;\n"+
                "public class C {\n"+
                "  public class D {\n"+
                "    D () {}\n"+
                "  }\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends C {\n"+
                "  private class B extends D {\n"+
                "    private B(int val) {\n"+
                "      super();\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDeclB       = (ClassDeclaration)_project.getType("test.A.B");
        ClassDeclaration       classDeclD       = (ClassDeclaration)_project.getType("test.C.D");
        ConstructorDeclaration constructorDeclB = classDeclB.getConstructors().get(0);
        ConstructorDeclaration constructorDeclD = classDeclD.getConstructors().get(0);
        ExpressionStatement    exprStmt         = (ExpressionStatement)constructorDeclB.getBody().getBlockStatements().get(0);
        ConstructorInvocation  constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertEquals(constructorDeclD,
                     constructorInvoc.getConstructorDeclaration());
    }

    public void testSuper_InnerFriendly2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class C {\n"+
                "    C() {}\n"+
                "  }\n"+
                "  private class B extends C {\n"+
                "    private B(int val) {\n"+
                "      super();\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDeclA       = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration       classDeclB       = (ClassDeclaration)classDeclA.getInnerTypes().get("B");
        ClassDeclaration       classDeclC       = (ClassDeclaration)classDeclA.getInnerTypes().get("C");
        ConstructorDeclaration constructorDeclB = classDeclB.getConstructors().get(0);
        ConstructorDeclaration constructorDeclC = classDeclC.getConstructors().get(0);
        ExpressionStatement    exprStmt         = (ExpressionStatement)constructorDeclB.getBody().getBlockStatements().get(0);
        ConstructorInvocation  constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertEquals(constructorDeclC,
                     constructorInvoc.getConstructorDeclaration());
    }

    public void testSuper_InnerPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class C {\n"+
                "    private C() {}\n"+
                "  }\n"+
                "  private class B extends C {\n"+
                "    private B(int val) {\n"+
                "      super();\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDeclA       = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration       classDeclB       = (ClassDeclaration)classDeclA.getInnerTypes().get("B");
        ClassDeclaration       classDeclC       = (ClassDeclaration)classDeclA.getInnerTypes().get("C");
        ConstructorDeclaration constructorDeclB = classDeclB.getConstructors().get(0);
        ConstructorDeclaration constructorDeclC = classDeclC.getConstructors().get(0);
        ExpressionStatement    exprStmt         = (ExpressionStatement)constructorDeclB.getBody().getBlockStatements().get(0);
        ConstructorInvocation  constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertEquals(constructorDeclC,
                     constructorInvoc.getConstructorDeclaration());
    }

    public void testSuper_InnerProtected1() throws ANTLRException, ParseException
    {
        addType("test1.C",
                "package test1;\n"+
                "public class C {\n"+
                "  public class D {\n"+
                "    protected D () {}\n"+
                "  }\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A extends test1.C {\n"+
                "  private class B extends D {\n"+
                "    private B(int val) {\n"+
                "      super();\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDeclB       = (ClassDeclaration)_project.getType("test2.A.B");
        ClassDeclaration       classDeclD       = (ClassDeclaration)_project.getType("test1.C.D");
        ConstructorDeclaration constructorDeclB = classDeclB.getConstructors().get(0);
        ConstructorDeclaration constructorDeclD = classDeclD.getConstructors().get(0);
        ExpressionStatement    exprStmt         = (ExpressionStatement)constructorDeclB.getBody().getBlockStatements().get(0);
        ConstructorInvocation  constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertEquals(constructorDeclD,
                     constructorInvoc.getConstructorDeclaration());
    }

    public void testSuper_InnerProtected2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class C {\n"+
                "    protected C() {}\n"+
                "  }\n"+
                "  private class B extends C {\n"+
                "    private B(int val) {\n"+
                "      super();\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDeclA       = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration       classDeclB       = (ClassDeclaration)classDeclA.getInnerTypes().get("B");
        ClassDeclaration       classDeclC       = (ClassDeclaration)classDeclA.getInnerTypes().get("C");
        ConstructorDeclaration constructorDeclB = classDeclB.getConstructors().get(0);
        ConstructorDeclaration constructorDeclC = classDeclC.getConstructors().get(0);
        ExpressionStatement    exprStmt         = (ExpressionStatement)constructorDeclB.getBody().getBlockStatements().get(0);
        ConstructorInvocation  constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertEquals(constructorDeclC,
                     constructorInvoc.getConstructorDeclaration());
    }

    public void testSuper_InnerPublic1() throws ANTLRException, ParseException
    {
        addType("test1.C",
                "package test1;\n"+
                "public class C {\n"+
                "  public class D {\n"+
                "    public D () {}\n"+
                "  }\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A extends test1.C {\n"+
                "  private class B extends D {\n"+
                "    private B(int val) {\n"+
                "      super();\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDeclB       = (ClassDeclaration)_project.getType("test2.A.B");
        ClassDeclaration       classDeclD       = (ClassDeclaration)_project.getType("test1.C.D");
        ConstructorDeclaration constructorDeclB = classDeclB.getConstructors().get(0);
        ConstructorDeclaration constructorDeclD = classDeclD.getConstructors().get(0);
        ExpressionStatement    exprStmt         = (ExpressionStatement)constructorDeclB.getBody().getBlockStatements().get(0);
        ConstructorInvocation  constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertEquals(constructorDeclD,
                     constructorInvoc.getConstructorDeclaration());
    }

    public void testSuper_InnerPublic2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class C {\n"+
                "    public C() {}\n"+
                "  }\n"+
                "  private class B extends C {\n"+
                "    private B(int val) {\n"+
                "      super();\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDeclA       = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration       classDeclB       = (ClassDeclaration)classDeclA.getInnerTypes().get("B");
        ClassDeclaration       classDeclC       = (ClassDeclaration)classDeclA.getInnerTypes().get("C");
        ConstructorDeclaration constructorDeclB = classDeclB.getConstructors().get(0);
        ConstructorDeclaration constructorDeclC = classDeclC.getConstructors().get(0);
        ExpressionStatement    exprStmt         = (ExpressionStatement)constructorDeclB.getBody().getBlockStatements().get(0);
        ConstructorInvocation  constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertEquals(constructorDeclC,
                     constructorInvoc.getConstructorDeclaration());
    }

    public void testSuper_TopLevelFriendly1() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  B() {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends B {\n"+
                "  private A(int val) {\n"+
                "    super();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDeclA       = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration       classDeclB       = (ClassDeclaration)_project.getType("test.B");
        ConstructorDeclaration constructorDeclA = classDeclA.getConstructors().get(0);
        ConstructorDeclaration constructorDeclB = classDeclB.getConstructors().get(0);
        ExpressionStatement    exprStmt         = (ExpressionStatement)constructorDeclA.getBody().getBlockStatements().get(0);
        ConstructorInvocation  constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertEquals(constructorDeclB,
                     constructorInvoc.getConstructorDeclaration());
    }

    public void testSuper_TopLevelFriendly2() throws ANTLRException, ParseException
    {
        addType("test.C",
                "package test;\n"+
                "public class C {\n"+
                "  public class D {\n"+
                "    D () {}\n"+
                "  }\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends C.D {\n"+
                "  private A(int val) {\n"+
                "    new C().super();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDeclA       = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration       classDeclD       = (ClassDeclaration)_project.getType("test.C.D");
        ConstructorDeclaration constructorDeclA = classDeclA.getConstructors().get(0);
        ConstructorDeclaration constructorDeclD = classDeclD.getConstructors().get(0);
        ExpressionStatement    exprStmt         = (ExpressionStatement)constructorDeclA.getBody().getBlockStatements().get(0);
        ConstructorInvocation  constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertEquals(constructorDeclD,
                     constructorInvoc.getConstructorDeclaration());
    }

    public void testSuper_TopLevelProtected1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  protected B() {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A extends test1.B {\n"+
                "  private A(int val) {\n"+
                "    super();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDeclA       = (ClassDeclaration)_project.getType("test2.A");
        ClassDeclaration       classDeclB       = (ClassDeclaration)_project.getType("test1.B");
        ConstructorDeclaration constructorDeclA = classDeclA.getConstructors().get(0);
        ConstructorDeclaration constructorDeclB = classDeclB.getConstructors().get(0);
        ExpressionStatement    exprStmt         = (ExpressionStatement)constructorDeclA.getBody().getBlockStatements().get(0);
        ConstructorInvocation  constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertEquals(constructorDeclB,
                     constructorInvoc.getConstructorDeclaration());
    }

    public void testSuper_TopLevelProtected2() throws ANTLRException, ParseException
    {
        addType("test1.C",
                "package test1;\n"+
                "public class C {\n"+
                "  public class D {\n"+
                "    protected D () {}\n"+
                "  }\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A extends test1.C.D {\n"+
                "  private A(int val) {\n"+
                "    new test1.C().super();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDeclA       = (ClassDeclaration)_project.getType("test2.A");
        ClassDeclaration       classDeclD       = (ClassDeclaration)_project.getType("test1.C.D");
        ConstructorDeclaration constructorDeclA = classDeclA.getConstructors().get(0);
        ConstructorDeclaration constructorDeclD = classDeclD.getConstructors().get(0);
        ExpressionStatement    exprStmt         = (ExpressionStatement)constructorDeclA.getBody().getBlockStatements().get(0);
        ConstructorInvocation  constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertEquals(constructorDeclD,
                     constructorInvoc.getConstructorDeclaration());
    }

    public void testSuper_TopLevelPublic1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public B() {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A extends test1.B {\n"+
                "  private A(int val) {\n"+
                "    super();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDeclA       = (ClassDeclaration)_project.getType("test2.A");
        ClassDeclaration       classDeclB       = (ClassDeclaration)_project.getType("test1.B");
        ConstructorDeclaration constructorDeclA = classDeclA.getConstructors().get(0);
        ConstructorDeclaration constructorDeclB = classDeclB.getConstructors().get(0);
        ExpressionStatement    exprStmt         = (ExpressionStatement)constructorDeclA.getBody().getBlockStatements().get(0);
        ConstructorInvocation  constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertEquals(constructorDeclB,
                     constructorInvoc.getConstructorDeclaration());
    }

    public void testSuper_TopLevelPublic2() throws ANTLRException, ParseException
    {
        addType("test1.C",
                "package test1;\n"+
                "public class C {\n"+
                "  public class D {\n"+
                "    public D () {}\n"+
                "  }\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A extends test1.C.D {\n"+
                "  private A(int val) {\n"+
                "    new test1.C().super();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDeclA       = (ClassDeclaration)_project.getType("test2.A");
        ClassDeclaration       classDeclD       = (ClassDeclaration)_project.getType("test1.C.D");
        ConstructorDeclaration constructorDeclA = classDeclA.getConstructors().get(0);
        ConstructorDeclaration constructorDeclD = classDeclD.getConstructors().get(0);
        ExpressionStatement    exprStmt         = (ExpressionStatement)constructorDeclA.getBody().getBlockStatements().get(0);
        ConstructorInvocation  constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertEquals(constructorDeclD,
                     constructorInvoc.getConstructorDeclaration());
    }

    public void testThis_InnerFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      C() {}\n"+
                "      private C(int val) {\n"+
                "        this();"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDeclC       = (ClassDeclaration)_project.getType("test.A.B.C");
        ConstructorDeclaration constructorDecl1 = classDeclC.getConstructors().get(0);
        ConstructorDeclaration constructorDecl2 = classDeclC.getConstructors().get(1);
        ExpressionStatement    exprStmt         = (ExpressionStatement)constructorDecl2.getBody().getBlockStatements().get(0);
        ConstructorInvocation  constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertEquals(constructorDecl1,
                     constructorInvoc.getConstructorDeclaration());
    }

    public void testThis_InnerPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private C() {}\n"+
                "      private C(int val) {\n"+
                "        this();"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDeclC       = (ClassDeclaration)_project.getType("test.A.B.C");
        ConstructorDeclaration constructorDecl1 = classDeclC.getConstructors().get(0);
        ConstructorDeclaration constructorDecl2 = classDeclC.getConstructors().get(1);
        ExpressionStatement    exprStmt         = (ExpressionStatement)constructorDecl2.getBody().getBlockStatements().get(0);
        ConstructorInvocation  constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertEquals(constructorDecl1,
                     constructorInvoc.getConstructorDeclaration());
    }

    public void testThis_InnerProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      protected C() {}\n"+
                "      private C(int val) {\n"+
                "        this();"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDeclC       = (ClassDeclaration)_project.getType("test.A.B.C");
        ConstructorDeclaration constructorDecl1 = classDeclC.getConstructors().get(0);
        ConstructorDeclaration constructorDecl2 = classDeclC.getConstructors().get(1);
        ExpressionStatement    exprStmt         = (ExpressionStatement)constructorDecl2.getBody().getBlockStatements().get(0);
        ConstructorInvocation  constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertEquals(constructorDecl1,
                     constructorInvoc.getConstructorDeclaration());
    }

    public void testThis_InnerPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      protected C() {}\n"+
                "      private C(int val) {\n"+
                "        this();"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDeclC       = (ClassDeclaration)_project.getType("test.A.B.C");
        ConstructorDeclaration constructorDecl1 = classDeclC.getConstructors().get(0);
        ConstructorDeclaration constructorDecl2 = classDeclC.getConstructors().get(1);
        ExpressionStatement    exprStmt         = (ExpressionStatement)constructorDecl2.getBody().getBlockStatements().get(0);
        ConstructorInvocation  constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertEquals(constructorDecl1,
                     constructorInvoc.getConstructorDeclaration());
    }

    public void testThis_TopLevelFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  A() {}\n"+
                "  private A(int val) {\n"+
                "    this();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDeclA       = (ClassDeclaration)_project.getType("test.A");
        ConstructorDeclaration constructorDecl1 = classDeclA.getConstructors().get(0);
        ConstructorDeclaration constructorDecl2 = classDeclA.getConstructors().get(1);
        ExpressionStatement    exprStmt         = (ExpressionStatement)constructorDecl2.getBody().getBlockStatements().get(0);
        ConstructorInvocation  constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertEquals(constructorDecl1,
                     constructorInvoc.getConstructorDeclaration());
    }

    public void testThis_TopLevelPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private A() {}\n"+
                "  private A(int val) {\n"+
                "    this();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDeclA       = (ClassDeclaration)_project.getType("test.A");
        ConstructorDeclaration constructorDecl1 = classDeclA.getConstructors().get(0);
        ConstructorDeclaration constructorDecl2 = classDeclA.getConstructors().get(1);
        ExpressionStatement    exprStmt         = (ExpressionStatement)constructorDecl2.getBody().getBlockStatements().get(0);
        ConstructorInvocation  constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertEquals(constructorDecl1,
                     constructorInvoc.getConstructorDeclaration());
    }

    public void testThis_TopLevelProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected A() {}\n"+
                "  private A(int val) {\n"+
                "    this();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDeclA       = (ClassDeclaration)_project.getType("test.A");
        ConstructorDeclaration constructorDecl1 = classDeclA.getConstructors().get(0);
        ConstructorDeclaration constructorDecl2 = classDeclA.getConstructors().get(1);
        ExpressionStatement    exprStmt         = (ExpressionStatement)constructorDecl2.getBody().getBlockStatements().get(0);
        ConstructorInvocation  constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertEquals(constructorDecl1,
                     constructorInvoc.getConstructorDeclaration());
    }

    public void testThis_TopLevelPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public A() {}\n"+
                "  private A(int val) {\n"+
                "    this();"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDeclA       = (ClassDeclaration)_project.getType("test.A");
        ConstructorDeclaration constructorDecl1 = classDeclA.getConstructors().get(0);
        ConstructorDeclaration constructorDecl2 = classDeclA.getConstructors().get(1);
        ExpressionStatement    exprStmt         = (ExpressionStatement)constructorDecl2.getBody().getBlockStatements().get(0);
        ConstructorInvocation  constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertEquals(constructorDecl1,
                     constructorInvoc.getConstructorDeclaration());
    }
}
