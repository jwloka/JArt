package jast.test.resolver;
import jast.ParseException;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.SingleInitializer;
import antlr.ANTLRException;
public class TestConstructorArguments extends TestBase
{

    public TestConstructorArguments(String name)
    {
        super(name);
    }

    public void testOneArgPrim1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(int value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(int value) {\n"+
                "    A var = new A(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgClonable1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(Clonable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(Clonable value) {\n"+
                "    A var = new A(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgClonable2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(Clonable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(double[] values) {\n"+
                "    A var = new A(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgObject1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(Object obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(Object value) {\n"+
                "    A var = new A(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgObject2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(Object obj) {}\n"+
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
                "  private void doSomething(test3.C value) {\n"+
                "    A var = new A(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgObject3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(Object obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(boolean[] values) {\n"+
                "    A var = new A(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgObject4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(Object obj) {}\n"+
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
                "  private void doSomething(test3.C[][] values) {\n"+
                "    A var = new A(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgObject5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(Object obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething() {\n"+
                "    A var = new A(null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgPrim2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(int value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(short value) {\n"+
                "    A var = new A(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgPrimArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(float[][] value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(float[][] value) {\n"+
                "    A var = new A(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgPrimArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(float[][] value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething() {\n"+
                "    A var = new A(null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgRef1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public A(C value) {}\n"+
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
                "  private void doSomething(test3.C value) {\n"+
                "    A var = new A(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgRef2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public A(C value) {}\n"+
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
                "  private void doSomething(test3.D value) {\n"+
                "    A var = new A(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgRef3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public A(C value) {}\n"+
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
                "  private void doSomething() {\n"+
                "    A var = new A(null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgRefArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public A(C[][] value) {}\n"+
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
                "  private void doSomething(test3.C[][] values) {\n"+
                "    A var = new A(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgRefArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public A(C[][] value) {}\n"+
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
                "  private void doSomething(test3.D[][] values) {\n"+
                "    A var = new A(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgRefArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public A(C[][] value) {}\n"+
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
                "  private void doSomething() {\n"+
                "    A var = new A(null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgSerializable2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(java.io.Serializable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(char[][] values) {\n"+
                "    A var = new A(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgSerializable3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(java.io.Serializable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething() {\n"+
                "    A var = new A(null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimPrim1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(long value1, double value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething() {\n"+
                "    A var = new A(1l, 0.0f);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimPrim2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(long value1, double value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething() {\n"+
                "    A var = new A('a', 0.0f);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimPrim3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(long value1, double value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething() {\n"+
                "    A var = new A(1l, 0l);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimPrim4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(long value1, double value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething() {\n"+
                "    A var = new A(0, 1);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsRefRef1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C value1, D value2) {}\n"+
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
                "  private void doSomething(test3.C value1, test3.D value2) {\n"+
                "    A var = new A(value1, value2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsRefRef2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C value1, D value2) {}\n"+
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
                "  private void doSomething(test4.E value1, test3.D value2) {\n"+
                "    A var = new A(value1, value2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsRefRef3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C value1, D value2) {}\n"+
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
                "  private void doSomething(test3.C value1, test4.E value2) {\n"+
                "    A var = new A(value1, value2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsRefRef4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C value1, D value2) {}\n"+
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
                "  private void doSomething(test4.E value) {\n"+
                "    A var = new A(value, value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsRefRef5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C value1, D value2) {}\n"+
                "}\n",
                false);
        addType("test3.C",
                "package test3;\n"+
                "public interface C {}\n",
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
                "  private void doSomething(test4.E value) {\n"+
                "    A var = new A(null, value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testInheritance1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(int value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "public class B extends test1.A {\n"+
                "  private void doSomething(int value) {\n"+
                "    B var = new B(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No inheritance for constructors -> no applicable constructor
        // (would not compile)
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testInheritance2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(int value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "public class B extends test1.A {\n"+
                "  public B(float value) {}\n"+
                "  private void doSomething(int value) {\n"+
                "    B var = new B(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclB.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No inheritance for constructors
        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testNumOfArgs1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(int value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(int value) {\n"+
                "    A var = new A(value, value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No applicable constructor (would not compile)
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testNumOfArgs2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(int value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething() {\n"+
                "    A var = new A();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // Default constructor only when no constructors at all
        // -> No applicable constructor (would not compile)
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOneArgClonable3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(Clonable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(String[] values) {\n"+
                "    A var = new A(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgClonable4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(Clonable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(Object value) {\n"+
                "    A var = new A(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No applicable constructor (would not compile) !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOneArgClonable5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(Clonable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(float value) {\n"+
                "    A var = new A(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No applicable constructor (would not compile) !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOneArgObject6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(Object obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(int value) {\n"+
                "    A var = new A(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No applicable constructor (would not compile) !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOneArgObject7() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(Object obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(Clonable value) {\n"+
                "    A var = new A(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // References point to actual objects and are therefore
        // method-invocation convertible to java.lang.Object
        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgPrim3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(int value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(float value) {\n"+
                "    A var = new A(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No applicable constructor (would not compile) !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOneArgPrim4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(int value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(Object value) {\n"+
                "    A var = new A(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No applicable constructor (would not compile) !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOneArgPrim5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(int value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(int[] value) {\n"+
                "    A var = new A(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No applicable constructor (would not compile) !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOneArgPrimArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(float[][] value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(int[][] value) {\n"+
                "    A var = new A(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No applicable constructor (would not compile) !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOneArgRef4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public A(C value) {}\n"+
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
                "  private void doSomething(test3.C[] value) {\n"+
                "    A var = new A(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No applicable constructor (would not compile) !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOneArgRef5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public A(C value) {}\n"+
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
                "  private void doSomething(Object value) {\n"+
                "    A var = new A(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No applicable constructor (would not compile) !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOneArgRefArray4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public A(D[][] value) {}\n"+
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
                "  private void doSomething(test3.C[][] values) {\n"+
                "    A var = new A(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No applicable constructor (would not compile) !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOneArgRefArray5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public A(C[][] value) {}\n"+
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
                "  private void doSomething(test3.C[] values) {\n"+
                "    A var = new A(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No applicable constructor (would not compile) !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOneArgSerializable4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(java.io.Serializable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(Object[] value) {\n"+
                "    A var = new A(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgSerializable5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(java.io.Serializable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(Object value) {\n"+
                "    A var = new A(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclB    = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl    = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl  = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();

        // No applicable constructor (would not compile) !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimArrayClonable1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(long[] values, Clonable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(long[] values, Clonable obj) {\n"+
                "    A var = new A(values, obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimArrayClonable2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(long[] values, Clonable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(Clonable obj) {\n"+
                "    A var = new A(null, obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimArrayClonable3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(long[] values, Clonable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(long[] values) {\n"+
                "    A var = new A(values, values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimArrayClonable4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(long[] values, Clonable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(long[] values) {\n"+
                "    A var = new A(values, new String[2]);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimArrayClonable5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(long[] values, Clonable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(long[] values) {\n"+
                "    A var = new A(values, null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimArrayClonable6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(long[] values, Clonable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething() {\n"+
                "    A var = new A(null, null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimObject1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(short value, Object obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(short value, Object obj) {\n"+
                "    A var = new A(value, obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimObject2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(short value, Object obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(byte value, Object obj) {\n"+
                "    A var = new A(value, obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimObject3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(short value, Object obj) {}\n"+
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
                "  private void doSomething(short value, test3.C obj) {\n"+
                "    A var = new A(value, obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimObject4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(short value, Object obj) {}\n"+
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
                "  private void doSomething(byte value, test3.C obj) {\n"+
                "    A var = new A(value, obj);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimObject5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(short value, Object obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(short value) {\n"+
                "    A var = new A(value, null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimObject6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(short value, Object obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(byte value) {\n"+
                "    A var = new A(value, null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsSerializableRefArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import java.io.Serializable;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public A(Serializable obj, C[] values) {}\n"+
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
                "  private void doSomething(Serializable obj, test3.C[] values) {\n"+
                "    A var = new A(obj, values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsSerializableRefArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import java.io.Serializable;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public A(Serializable obj, C[] values) {}\n"+
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
                "import java.io.Serializable;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(Serializable obj, test3.D[] values) {\n"+
                "    A var = new A(obj, values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsSerializableRefArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import java.io.Serializable;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public A(Serializable obj, C[] values) {}\n"+
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
                "  private void doSomething(Serializable obj) {\n"+
                "    A var = new A(obj, null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsSerializableRefArray4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import java.io.Serializable;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public A(Serializable obj, C[] values) {}\n"+
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
                "  private void doSomething(int[][] arr, test3.C[] values) {\n"+
                "    A var = new A(arr, values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsSerializableRefArray5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import java.io.Serializable;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public A(Serializable obj, C[] values) {}\n"+
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
                "  private void doSomething(test3.C[] values) {\n"+
                "    A var = new A(values, values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsSerializableRefArray6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import java.io.Serializable;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public A(Serializable obj, C[] values) {}\n"+
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
                "  private void doSomething(test3.C[] values) {\n"+
                "    A var = new A(null, values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsSerializableRefArray7() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import java.io.Serializable;\n"+
                "import test3.C;\n"+
                "public class A {\n"+
                "  public A(Serializable obj, C[] values) {}\n"+
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
                "  private void doSomething() {\n"+
                "    A var = new A(null, null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }


    public void testNullArgs() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(String arg1, Clonable arg2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething() {\n"+
                "    A var = new A(null, null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA    = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB    = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl    = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl  = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }



    public void testOneArgPrim6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(int value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething() {\n"+
                "    A var = new A(null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No applicable constructor (would not compile) !
        assertNull(instantiation.getInvokedConstructor());
    }



    public void testOneArgSerializable1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(java.io.Serializable obj) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(java.io.Serializable value) {\n"+
                "    A var = new A(value);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        ConstructorDeclaration   constructorDecl = classDeclA.getConstructors().get(0);
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(constructorDecl,
                     instantiation.getInvokedConstructor());
    }
}