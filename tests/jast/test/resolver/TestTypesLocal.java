package jast.test.resolver;
import jast.ParseException;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.MethodDeclaration;
import antlr.ANTLRException;

public class TestTypesLocal extends TestBase
{

    public TestTypesLocal(String name)
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
                "        class X extends D {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(_project.getType("test1.D"),
                     classDeclX.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testEnclosing() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X extends C {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(_project.getType("test.A.B.C"),
                     classDeclX.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B extends test1.D {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X implements E {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(_project.getType("test1.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInnerOfDirectImport() throws ANTLRException, ParseException
    {
        addType("test1.F",
                "package test1;\n"+
                "interface D {\n"+
                "  public interface E {}\n"+
                "}\n"+
                "public class F implements D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.F;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X implements F.E {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(_project.getType("test1.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInnerOfOnDemandImport() throws ANTLRException, ParseException
    {
        addType("test1.F",
                "package test1;\n"+
                "interface D {\n"+
                "  public interface E {}\n"+
                "}\n"+
                "public class F implements D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X implements F.E {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(_project.getType("test1.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInnerOfOtherTopLevel() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X implements F.E {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n"+
                "class F extends D {}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(_project.getType("test1.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInnerOfQualified() throws ANTLRException, ParseException
    {
        addType("test1.F",
                "package test1;\n"+
                "interface D {\n"+
                "  public interface E {}\n"+
                "}\n"+
                "public class F implements D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X implements test1.F.E {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(_project.getType("test1.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInnerOfSamePackage() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D extends E {}\n"+
                "class E {\n"+
                "  class F {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X extends D.F {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(_project.getType("test.E.F"),
                     classDeclX.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_DirectImport() throws ANTLRException, ParseException
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
                "        class X {\n"+
                "          class Y extends D {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(_project.getType("test1.D"),
                     classDeclY.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_Enclosing1() throws ANTLRException, ParseException
    {
        addType("test2.A",
                "package test2;\n"+
                "import test1.D;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {\n"+
                "          class Y extends X {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(classDeclX,
                     classDeclY.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_Enclosing2() throws ANTLRException, ParseException
    {
        addType("test2.A",
                "package test2;\n"+
                "import test1.D;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {\n"+
                "          class Y extends C {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(_project.getType("test2.A.B.C"),
                     classDeclY.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_InheritedInner1() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.D;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X extends test1.D {\n"+
                "          class Y implements E {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclY.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InheritedInner2() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.D;\n"+
                "public class A {\n"+
                "  private class B extends test1.D {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {\n"+
                "          class Y implements E {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclY.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InheritedInnerOfDirectImport() throws ANTLRException, ParseException
    {
        addType("test1.F",
                "package test1;\n"+
                "interface D {\n"+
                "  public interface E {}\n"+
                "}\n"+
                "public class F implements D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.F;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {\n"+
                "          class Y implements F.E {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclY.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InheritedInnerOfOnDemandImport() throws ANTLRException, ParseException
    {
        addType("test1.F",
                "package test1;\n"+
                "interface D {\n"+
                "  public interface E {}\n"+
                "}\n"+
                "public class F implements D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {\n"+
                "          class Y implements F.E {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclY.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InheritedInnerOfOtherTopLevel() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {\n"+
                "          class Y implements F.E {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n"+
                "class F extends D {}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclY.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InheritedInnerOfQualified() throws ANTLRException, ParseException
    {
        addType("test1.F",
                "package test1;\n"+
                "interface D {\n"+
                "  public interface E {}\n"+
                "}\n"+
                "public class F implements D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {\n"+
                "          class Y implements test1.F.E {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclY.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InheritedInnerOfSamePackage() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D extends E {}\n"+
                "class E {\n"+
                "  class F {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {\n"+
                "          class Y extends D.F {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(_project.getType("test.E.F"),
                     classDeclY.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_InnerOfDirectImport() throws ANTLRException, ParseException
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
                "        class X {\n"+
                "          class Y extends D.E {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclY.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_InnerOfOnDemandImport() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public interface D {\n"+
                "  public interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {\n"+
                "          class Y implements D.E {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclY.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InnerOfOtherTopLevel() throws ANTLRException, ParseException
    {
        // qualified
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {\n"+
                "          class Y extends test.D.E {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n"+
                "class D {\n"+
                "  protected class E {}\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(_project.getType("test.D.E"),
                     classDeclY.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_InnerOfQualified() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public interface D {\n"+
                "  public interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {\n"+
                "          class Y implements test1.D.E {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclY.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InnerOfSamePackage() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "class D {\n"+
                "  class E {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {\n"+
                "          class Y extends D.E {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(_project.getType("test.D.E"),
                     classDeclY.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_OnDemandImport() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public interface D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {\n"+
                "          class Y implements D {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(_project.getType("test1.D"),
                     classDeclY.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_OtherInner1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {\n"+
                "          class Y extends Z {}\n"+
                "          class Z {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");
        ClassDeclaration  classDeclZ = (ClassDeclaration)classDeclX.getInnerTypes().get("Z");

        assertEquals(classDeclZ,
                     classDeclY.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_OtherInner2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private class D {}\n"+
                "      private void doSomething() {\n"+
                "        class X {\n"+
                "          class Y extends D {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(_project.getType("test.A.B.C.D"),
                     classDeclY.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_OtherLocal() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class Z {}\n"+
                "        class X {\n"+
                "          class Y extends Z {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclZ = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(1);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(classDeclZ,
                     classDeclY.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_OtherTopLevel() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {\n"+
                "          class Y extends D {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n"+
                "class D {}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(_project.getType("test.D"),
                     classDeclY.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_Outer() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {\n"+
                "          class Y extends B {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(_project.getType("test.A.B"),
                     classDeclY.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_OuterLocal1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class Z {}\n"+
                "        class X {\n"+
                "          private void doSomething() {\n"+
                "            class Y extends Z {}\n"+
                "          }\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclZ  = (ClassDeclaration)methodDeclC.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclX  = (ClassDeclaration)methodDeclC.getBody().getBlockStatements().get(1);
        MethodDeclaration methodDeclX = classDeclX.getMethods().get(0);
        ClassDeclaration  classDeclY  = (ClassDeclaration)methodDeclX.getBody().getBlockStatements().get(0);

        assertEquals(classDeclZ,
                     classDeclY.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_OuterLocal2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class Z {}\n"+
                "        class X {\n"+
                "          private void doSomething() {\n"+
                "            class Z {}\n"+
                "            class Y extends Z {}\n"+
                "          }\n"+
                "        }\n"+
                "        class Y extends Z {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDeclC = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclZ1 = (ClassDeclaration)methodDeclC.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclX  = (ClassDeclaration)methodDeclC.getBody().getBlockStatements().get(1);
        ClassDeclaration  classDeclY1 = (ClassDeclaration)methodDeclC.getBody().getBlockStatements().get(2);
        MethodDeclaration methodDeclX = classDeclX.getMethods().get(0);
        ClassDeclaration  classDeclZ2 = (ClassDeclaration)methodDeclX.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY2 = (ClassDeclaration)methodDeclX.getBody().getBlockStatements().get(1);

        assertEquals(classDeclZ1,
                     classDeclY1.getBaseClass().getReferenceBaseTypeDecl());
        assertEquals(classDeclZ2,
                     classDeclY2.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_Qualified() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public interface D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {\n"+
                "          class Y implements test1.D {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(_project.getType("test1.D"),
                     classDeclY.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_QualifiedOtherInner1() throws ANTLRException, ParseException
    {
        // qualified access
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private class D {}\n"+
                "      private void doSomething() {\n"+
                "        class X {\n"+
                "          class Y extends A.B.C.D {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(_project.getType("test.A.B.C.D"),
                     classDeclY.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_QualifiedOtherInner2() throws ANTLRException, ParseException
    {
        // qualified access
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {\n"+
                "          class Z {}\n"+
                "          class Y extends X.Z {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclZ = (ClassDeclaration)classDeclX.getInnerTypes().get("Z");
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(classDeclZ,
                     classDeclY.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_QualifiedOuter() throws ANTLRException, ParseException
    {
        // qualified access
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {\n"+
                "          class Y extends test.A.B.C {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(_project.getType("test.A.B.C"),
                     classDeclY.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_RemoteInheritedInner() throws ANTLRException, ParseException
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
                "        class X {\n"+
                "          class Y extends D.F {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(_project.getType("test1.E.F"),
                     classDeclY.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_SamePackage() throws ANTLRException, ParseException
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
                "        class X {\n"+
                "          class Y extends D {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclY = (ClassDeclaration)classDeclX.getInnerTypes().get("Y");

        assertEquals(_project.getType("test.D"),
                     classDeclY.getBaseClass().getReferenceBaseTypeDecl());
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
                "        class X extends D.E {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(_project.getType("test1.D.E"),
                     classDeclX.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInnerOfOnDemandImport() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public interface D {\n"+
                "  public interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X implements D.E {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(_project.getType("test1.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInnerOfOtherTopLevel() throws ANTLRException, ParseException
    {
        // qualified
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X extends test.D.E {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n"+
                "class D {\n"+
                "  protected class E {}\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(_project.getType("test.D.E"),
                     classDeclX.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInnerOfQualified() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public interface D {\n"+
                "  public interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X implements test1.D.E {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(_project.getType("test1.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInnerOfSamePackage() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "class D {\n"+
                "  class E {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X extends D.E {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(_project.getType("test.D.E"),
                     classDeclX.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testOnDemandImport() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public interface D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X implements D {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(_project.getType("test1.D"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testOtherInner() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private class D {}\n"+
                "      private void doSomething() {\n"+
                "        class X extends D {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(_project.getType("test.A.B.C.D"),
                     classDeclX.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testOtherLocal() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class Z {}\n"+
                "        class X extends Z {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n"+
                "class D {}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclZ = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(1);

        assertEquals(classDeclZ,
                     classDeclX.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testOtherTopLevel() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X extends D {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n"+
                "class D {}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(_project.getType("test.D"),
                     classDeclX.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testOuter() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X extends B {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(_project.getType("test.A.B"),
                     classDeclX.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testQualified() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public interface D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X implements test1.D {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(_project.getType("test1.D"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testQualifiedOtherInner() throws ANTLRException, ParseException
    {
        // qualified access
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private class D {}\n"+
                "      private void doSomething() {\n"+
                "        class X extends A.B.C.D {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(_project.getType("test.A.B.C.D"),
                     classDeclX.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testQualifiedOuter() throws ANTLRException, ParseException
    {
        // qualified access
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X extends test.A.B.C {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(_project.getType("test.A.B.C"),
                     classDeclX.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testRemoteInheritedInner() throws ANTLRException, ParseException
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
                "        class X extends D.F {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test2.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(_project.getType("test1.E.F"),
                     classDeclX.getBaseClass().getReferenceBaseTypeDecl());
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
                "        class X extends D {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration  classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        ClassDeclaration  classDeclD = (ClassDeclaration)_project.getType("test.D");

        assertEquals(_project.getType("test.D"),
                     classDeclX.getBaseClass().getReferenceBaseTypeDecl());
    }
}
