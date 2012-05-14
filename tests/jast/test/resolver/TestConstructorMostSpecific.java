package jast.test.resolver;
import jast.ParseException;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.SingleInitializer;
import antlr.ANTLRException;
public class TestConstructorMostSpecific extends TestBase
{

    public TestConstructorMostSpecific(String name)
    {
        super(name);
    }

    public void testOneArgClonablePrimArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(Clonable value) {}\n"+
                "  public A(int[] value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(Clonable value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgClonablePrimArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(Clonable value) {}\n"+
                "  public A(int[] value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(int[] value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgClonablePrimArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(Clonable value) {}\n"+
                "  A(int[] value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(int[] value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgClonablePrimArray4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(Clonable value) {}\n"+
                "  public A(int[] value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(short[] value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgClonableRefArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(Clonable obj) {}\n"+
                "  public A(C[][] obj) {}\n"+
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
                "  private void doSomething(Clonable obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgClonableRefArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(Clonable obj) {}\n"+
                "  public A(C[][] obj) {}\n"+
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
                "  private void doSomething(test3.C[][] obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgClonableRefArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(Clonable obj) {}\n"+
                "  A(C[][] obj) {}\n"+
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
                "  private void doSomething(test3.C[][] obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgClonableRefArray4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(Clonable obj) {}\n"+
                "  public A(C[][] obj) {}\n"+
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
                "  private void doSomething(test3.D[][] obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgClonableRefArray5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(Clonable obj) {}\n"+
                "  A(C[][] obj) {}\n"+
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
                "  private void doSomething(test3.D[][] obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgClonableRefArray6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  A(Clonable obj) {}\n"+
                "  public A(D[][] obj) {}\n"+
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
                "  private void doSomething(test3.C[][] obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No accessible constructor is applicable !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOneArgObjectPrimArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(Object value) {}\n"+
                "  public A(int[] value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(Object value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgObjectPrimArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(Object value) {}\n"+
                "  public A(int[] value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(int[] value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgObjectPrimArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(Object value) {}\n"+
                "  A(int[] value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(int[] value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgObjectPrimArray4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(Object value) {}\n"+
                "  public A(int[] value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(short[] value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgObjectRefArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(Object obj) {}\n"+
                "  public A(C[][] obj) {}\n"+
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
                "  private void doSomething(Object obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgObjectRefArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(Object obj) {}\n"+
                "  public A(C[][] obj) {}\n"+
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
                "  private void doSomething(test3.C[][] obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgObjectRefArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(Object obj) {}\n"+
                "  A(C[][] obj) {}\n"+
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
                "  private void doSomething(test3.C[][] obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgObjectRefArray4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(Object obj) {}\n"+
                "  public A(C[][] obj) {}\n"+
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
                "  private void doSomething(test3.D[][] obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgObjectRefArray5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(Object obj) {}\n"+
                "  A(C[][] obj) {}\n"+
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
                "  private void doSomething(test3.D[][] obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgObjectRefArray6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  A(Object obj) {}\n"+
                "  public A(D[][] obj) {}\n"+
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
                "  private void doSomething(test3.C[][] obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No accessible constructor is applicable !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOneArgPrimArrayPrimArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(int[] value) {}\n"+
                "  public A(float[] value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(int[] value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgPrimArrayPrimArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(int[] value) {}\n"+
                "  public A(float[] value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(float[] value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgPrimArrayPrimArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(int[] value) {}\n"+
                "  public A(float[] value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(long[] value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No constructor is applicable !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOneArgPrimArrayPrimArray4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(int[] value) {}\n"+
                "  A(float[] value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(float[] value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No accessible constructor is applicable !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOneArgPrimPrim1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(int value) {}\n"+
                "  public A(float value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(int value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgPrimPrim2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(int value) {}\n"+
                "  public A(float value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(float value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgPrimPrim3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  A(int value) {}\n"+
                "  public A(float value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(int value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgPrimPrim4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(int value) {}\n"+
                "  public A(float value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(long value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgPrimPrim5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(int value) {}\n"+
                "  public A(float value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(short value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgPrimPrim6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  A(int value) {}\n"+
                "  public A(float value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(short value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgPrimPrim7() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(int value) {}\n"+
                "  public A(float value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(double value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No constructor is applicable !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOneArgPrimPrim8() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(int value) {}\n"+
                "  A(float value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(float value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No accessible constructor is applicable !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOneArgRefArrayRefArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C[][] obj) {}\n"+
                "  public A(D[][] obj) {}\n"+
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
                "  private void doSomething(test3.C[][] obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgRefArrayRefArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C[][] obj) {}\n"+
                "  public A(D[][] obj) {}\n"+
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
                "  private void doSomething(test3.D[][] obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgRefArrayRefArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C[][] obj) {}\n"+
                "  A(D[][] obj) {}\n"+
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
                "  private void doSomething(test3.D[][] obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgRefArrayRefArray4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C[][] obj) {}\n"+
                "  public A(E[][] obj) {}\n"+
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
                "  private void doSomething(test3.D[][] obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgRefArrayRefArray5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C[][] obj) {}\n"+
                "  public A(D[][] obj) {}\n"+
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
                "  private void doSomething(test3.E[][] obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgRefArrayRefArray6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C[][] obj) {}\n"+
                "  A(D[][] obj) {}\n"+
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
                "  private void doSomething(test3.E[][] obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgRefArrayRefArray7() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(D[][] obj) {}\n"+
                "  public A(E[][] obj) {}\n"+
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
                "  private void doSomething(test3.C[][] obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No constructor is applicable !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOneArgRefArrayRefArray8() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  A(C[][] obj) {}\n"+
                "  public A(D[][] obj) {}\n"+
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
                "  private void doSomething(test3.C[][] obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No accessible constructor is applicable !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOneArgRefRef1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C obj) {}\n"+
                "  public A(D obj) {}\n"+
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
                "  private void doSomething(test3.C obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgRefRef2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C obj) {}\n"+
                "  public A(D obj) {}\n"+
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
                "  private void doSomething(test3.D obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgRefRef3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C obj) {}\n"+
                "  A(D obj) {}\n"+
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
                "  private void doSomething(test3.D obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgRefRef4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C obj) {}\n"+
                "  public A(E obj) {}\n"+
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
                "  private void doSomething(test3.D obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgRefRef5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C obj) {}\n"+
                "  public A(D obj) {}\n"+
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
                "  private void doSomething(test3.E obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgRefRef6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C obj) {}\n"+
                "  A(D obj) {}\n"+
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
                "  private void doSomething(test3.E obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgRefRef7() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C obj) {}\n"+
                "  public A(D obj) {}\n"+
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
                "  private void doSomething(Object obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No constructor is applicable !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOneArgRefRef8() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  A(C obj) {}\n"+
                "  public A(D obj) {}\n"+
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
                "  private void doSomething(test3.C obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No accessible constructor is applicable !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOneArgSerializablePrimArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(java.io.Serializable value) {}\n"+
                "  public A(int[] value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(java.io.Serializable value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgSerializablePrimArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(java.io.Serializable value) {}\n"+
                "  public A(int[] value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(int[] value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgSerializablePrimArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(java.io.Serializable value) {}\n"+
                "  A(int[] value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(int[] value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgSerializablePrimArray4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(java.io.Serializable value) {}\n"+
                "  public A(int[] value) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(short[] value) {\n"+
                "    A var = new A(value);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgSerializableRefArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(java.io.Serializable obj) {}\n"+
                "  public A(C[][] obj) {}\n"+
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
                "  private void doSomething(java.io.Serializable obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgSerializableRefArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(java.io.Serializable obj) {}\n"+
                "  public A(C[][] obj) {}\n"+
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
                "  private void doSomething(test3.C[][] obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgSerializableRefArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(java.io.Serializable obj) {}\n"+
                "  A(C[][] obj) {}\n"+
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
                "  private void doSomething(test3.C[][] obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgSerializableRefArray4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(java.io.Serializable obj) {}\n"+
                "  public A(C[][] obj) {}\n"+
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
                "  private void doSomething(test3.D[][] obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgSerializableRefArray5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(java.io.Serializable obj) {}\n"+
                "  A(C[][] obj) {}\n"+
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
                "  private void doSomething(test3.D[][] obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testOneArgSerializableRefArray6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  A(java.io.Serializable obj) {}\n"+
                "  public A(D[][] obj) {}\n"+
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
                "  private void doSomething(test3.C[][] obj) {\n"+
                "    A var = new A(obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No accessible constructor is applicable !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testTwoArgObjectClonable1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(Object obj1, int[] obj2) {}\n"+
                "  public A(C[] obj1, Clonable obj2) {}\n"+
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
                "  private void doSomething(Object obj1, int[] obj2) {\n"+
                "    A var = new A(obj1, obj2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgObjectClonable2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(Object obj1, int[] obj2) {}\n"+
                "  public A(C[] obj1, Clonable obj2) {}\n"+
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
                "  private void doSomething(test3.C[] obj1, Clonable obj2) {\n"+
                "    A var = new A(obj1, obj2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgObjectClonable3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(Object obj1, int[] obj2) {}\n"+
                "  public A(C[] obj1, Clonable obj2) {}\n"+
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
                "  private void doSomething(Object obj1, Clonable obj2) {\n"+
                "    A var = new A(obj1, obj2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No constructor is applicable !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testTwoArgObjectClonable4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(Object obj1, int[] obj2) {}\n"+
                "  public A(C[] obj1, Clonable obj2) {}\n"+
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
                "  private void doSomething(test3.C[] obj1, int[] obj2) {\n"+
                "    A var = new A(obj1, obj2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // Ambiguous invocation (would not compile), we find the first most specific
        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgPrimRef1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(int value, C obj) {}\n"+
                "  public A(float value, D obj) {}\n"+
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
                "  private void doSomething(int value, test3.C obj) {\n"+
                "    A var = new A(value, obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgPrimRef2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(int value, C obj) {}\n"+
                "  public A(float value, D obj) {}\n"+
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
                "  private void doSomething(float value, test3.D obj) {\n"+
                "    A var = new A(value, obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgPrimRef3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(int value, C obj) {}\n"+
                "  public A(float value, D obj) {}\n"+
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
                "  private void doSomething(int value, test3.D obj) {\n"+
                "    A var = new A(value, obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgPrimRef4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(int value, C obj) {}\n"+
                "  public A(float value, D obj) {}\n"+
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
                "  private void doSomething(float value, test3.C obj) {\n"+
                "    A var = new A(value, obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No constructor is applicable !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testTwoArgPrimRef5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  A(int value, C obj) {}\n"+
                "  public A(float value, D obj) {}\n"+
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
                "  private void doSomething(int value, test3.C obj) {\n"+
                "    A var = new A(value, obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No accessible constructor is applicable !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testTwoArgPrimRef6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(int value, C obj) {}\n"+
                "  public A(float value, D obj) {}\n"+
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
                "  private void doSomething(short value, test3.E obj) {\n"+
                "    A var = new A(value, obj);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // The code would not compile (ambiguous); our algorithm would find the
        // first most specific
        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgRefArrayPrimArray1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C[][] obj1, Clonable obj2) {}\n"+
                "  public A(D[][] obj1, int[] obj2) {}\n"+
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
                "  private void doSomething(test3.C[][] obj1, Clonable obj2) {\n"+
                "    A var = new A(obj1, obj2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgRefArrayPrimArray2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C[][] obj1, Clonable obj2) {}\n"+
                "  public A(D[][] obj1, int[] obj2) {}\n"+
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
                "  private void doSomething(test3.D[][] obj1, int[] obj2) {\n"+
                "    A var = new A(obj1, obj2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgRefArrayPrimArray3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C[][] obj1, Clonable obj2) {}\n"+
                "  A(D[][] obj1, int[] obj2) {}\n"+
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
                "  private void doSomething(test3.D[][] obj1, int[] obj2) {\n"+
                "    A var = new A(obj1, obj2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgRefArrayPrimArray4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C[][] obj1, Clonable obj2) {}\n"+
                "  public A(E[][] obj1, int[] obj2) {}\n"+
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
                "  private void doSomething(test3.D[][] obj1, int[] obj2) {\n"+
                "    A var = new A(obj1, obj2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgRefArrayPrimArray5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C[][] obj1, Clonable obj2) {}\n"+
                "  public A(D[][] obj1, int[] obj2) {}\n"+
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
                "  private void doSomething(test3.E[][] obj1, int[] obj2) {\n"+
                "    A var = new A(obj1, obj2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgRefArrayPrimArray6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C[][] obj1, Clonable obj2) {}\n"+
                "  A(D[][] obj1, int[] obj2) {}\n"+
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
                "  private void doSomething(test3.E[][] obj1, int[] obj2) {\n"+
                "    A var = new A(obj1, obj2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgRefArrayPrimArray7() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(D[][] obj1, Clonable obj2) {}\n"+
                "  public A(E[][] obj1, int[] obj2) {}\n"+
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
                "  private void doSomething(test3.C[][] obj1, int[] obj2) {\n"+
                "    A var = new A(obj1, obj2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No constructor is applicable !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testTwoArgRefArrayPrimArray8() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  A(C[][] obj1, Clonable obj2) {}\n"+
                "  public A(E[][] obj1, int[] obj2) {}\n"+
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
                "  private void doSomething(test3.D[][] obj1, int[] obj2) {\n"+
                "    A var = new A(obj1, obj2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No accessible constructor is applicable
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testTwoArgRefRef1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C obj1, E obj2) {}\n"+
                "  public A(D obj1, F obj2) {}\n"+
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
                "  private void doSomething(test3.C obj1, test3.E obj2) {\n"+
                "    A var = new A(obj1, obj2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgRefRef2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C obj1, E obj2) {}\n"+
                "  public A(D obj1, F obj2) {}\n"+
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
                "  private void doSomething(test3.D obj1, test3.F obj2) {\n"+
                "    A var = new A(obj1, obj2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgRefRef3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C obj1, E obj2) {}\n"+
                "  A(D obj1, F obj2) {}\n"+
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
                "  private void doSomething(test3.D obj1, test3.F obj2) {\n"+
                "    A var = new A(obj1, obj2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgRefRef4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C obj1, F obj2) {}\n"+
                "  public A(E obj1, H obj2) {}\n"+
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
                "  private void doSomething(test3.D obj1, test3.G obj2) {\n"+
                "    A var = new A(obj1, obj2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgRefRef5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C obj1, F obj2) {}\n"+
                "  public A(D obj1, G obj2) {}\n"+
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
                "  private void doSomething(test3.E obj1, test3.H obj2) {\n"+
                "    A var = new A(obj1, obj2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgRefRef6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(C obj1, F obj2) {}\n"+
                "  A(D obj1, G obj2) {}\n"+
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
                "  private void doSomething(test3.E obj1, test3.H obj2) {\n"+
                "    A var = new A(obj1, obj2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgRefRef7() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  public A(D obj1, G obj2) {}\n"+
                "  public A(E obj1, H obj2) {}\n"+
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
                "  private void doSomething(test3.C obj1, test3.H obj2) {\n"+
                "    A var = new A(obj1, obj2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No accessible constructor is applicable !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testTwoArgRefRef8() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "import test3.*;\n"+
                "public class A {\n"+
                "  A(C obj1, E obj2) {}\n"+
                "  public A(D obj1, F obj2) {}\n"+
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
                "  private void doSomething(test3.C obj1, test3.E obj2) {\n"+
                "    A var = new A(obj1, obj2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No accessible constructor is applicable
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimPrim1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(short value1, long value2) {}\n"+
                "  public A(int value1, float value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(short value1, long value2) {\n"+
                "    A var = new A(value1, value2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimPrim2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(short value1, long value2) {}\n"+
                "  public A(int value1, float value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(int value1, float value2) {\n"+
                "    A var = new A(value1, value2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimPrim3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  A(short value1, long value2) {}\n"+
                "  public A(int value1, float value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(short value1, long value2) {\n"+
                "    A var = new A(value1, value2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimPrim4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(byte value1, int value2) {}\n"+
                "  public A(int value1, float value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(short value1, long value2) {\n"+
                "    A var = new A(value1, value2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimPrim5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(short value1, long value2) {}\n"+
                "  public A(int value1, float value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(byte value1, int value2) {\n"+
                "    A var = new A(value1, value2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimPrim6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  A(short value1, long value2) {}\n"+
                "  public A(int value1, float value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(byte value1, int value2) {\n"+
                "    A var = new A(value1, value2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        assertEquals(classDeclA.getConstructors().get(1),
                     instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimPrim7() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(short value1, long value2) {}\n"+
                "  public A(int value1, float value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(long value1, double value2) {\n"+
                "    A var = new A(value1, value2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No constructor is applicable !
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testTwoArgsPrimPrim8() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public A(byte value1, int value2) {}\n"+
                "  A(int value1, float value2) {}\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class B {\n"+
                "  private void doSomething(short value1, long value2) {\n"+
                "    A var = new A(value1, value2);"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclA      = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration         classDeclB      = (ClassDeclaration)_project.getType("test2.B");
        MethodDeclaration        methodDecl      = classDeclB.getMethods().get(0);
        LocalVariableDeclaration localVarDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer     = (SingleInitializer)localVarDecl.getInitializer();
        Instantiation            instantiation   = (Instantiation)initializer.getInitEpression();

        // No accessible constructor is applicable !
        assertNull(instantiation.getInvokedConstructor());
    }
}
