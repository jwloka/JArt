package jast.test.resolver;

import jast.ParseException;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.SingleInitializer;
import jast.ast.nodes.TypeDeclaration;
import antlr.ANTLRException;

public class TestInstantiationTypes extends TestBase
{

    public TestInstantiationTypes(String name)
    {
        super(name);
    }

    public void testDirectImport() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.D;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        D var = new D();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation            instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();

        assertEquals(_project.getType("test1.D"),
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testEnclosing() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        C var = new C();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration          typeDecl      = _project.getType("test.A.B.C");
        MethodDeclaration        methodDecl    = typeDecl.getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation            instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testImplicitImport() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        String var = new String();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration         classDeclString = (ClassDeclaration)_project.getType("java.lang.String");
        MethodDeclaration        methodDecl      = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl         = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation            instantiation   = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();

        assertEquals(classDeclString,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(classDeclString.getConstructors().get(0),
                     instantiation.getInvokedConstructor());
    }

    public void testInheritedInnerOfDirectImport() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D extends E {}\n"+
                "class E {\n"+
                "  public class F {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.D;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        D.F var = new D().new F();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation            instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();

        assertEquals(_project.getType("test1.E.F"),
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertNull(instantiation.getInvokedConstructor());
        assertTrue(instantiation.isTrailing());

        Instantiation outerInstantiation = (Instantiation)instantiation.getBaseExpression();

        assertEquals(_project.getType("test1.D"),
                     outerInstantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertNull(outerInstantiation.getInvokedConstructor());
    }

    public void testInnerOfDirectImport() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public class E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.D;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        D.E var = new D().new E();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation            instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();

        assertEquals(_project.getType("test1.D.E"),
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertNull(instantiation.getInvokedConstructor());
        assertTrue(instantiation.isTrailing());

        Instantiation outerInstantiation = (Instantiation)instantiation.getBaseExpression();

        assertEquals(_project.getType("test1.D"),
                     outerInstantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertNull(outerInstantiation.getInvokedConstructor());
    }

    public void testOnDemandImport() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        D var = new D();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation            instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();

        assertEquals(_project.getType("test1.D"),
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOtherInner1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private class D {}\n"+
                "      private void doSomething() {\n"+
                "        D var = new D();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation            instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();

        assertEquals(_project.getType("test.A.B.C.D"),
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOtherInner2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        B var = new A().new B();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation            instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();

        assertEquals(_project.getType("test.A.B"),
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertNull(instantiation.getInvokedConstructor());
        assertTrue(instantiation.isTrailing());

        Instantiation outerInstantiation = (Instantiation)instantiation.getBaseExpression();

        assertEquals(_project.getType("test.A"),
                     outerInstantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertNull(outerInstantiation.getInvokedConstructor());
    }

    public void testOtherTopLevel() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        D var = new D();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n"+
                "class D {}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation            instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();

        assertEquals(_project.getType("test.D"),
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testOuter() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        A var = new A();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation            instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();

        assertEquals(_project.getType("test.A"),
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testQualified() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        test1.D var = new test1.D();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation            instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();

        assertEquals(_project.getType("test1.D"),
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testQualifiedOtherInner() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private class D {}\n"+
                "      private void doSomething() {\n"+
                "        test.A.B.C.D var = new test.A.B.C.D();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation            instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();

        assertEquals(_project.getType("test.A.B.C.D"),
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testQualifiedOuter() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        test.A var = new test.A();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation            instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();

        assertEquals(_project.getType("test.A"),
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertNull(instantiation.getInvokedConstructor());
    }

    public void testSamePackage() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "class D {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        D var = new D();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation            instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();

        assertEquals(_project.getType("test.D"),
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertNull(instantiation.getInvokedConstructor());
    }
}
