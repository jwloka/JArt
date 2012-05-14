package jast.test.resolver;
import jast.ParseException;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.TypeAccess;
import jast.ast.nodes.TypeDeclaration;
import antlr.ANTLRException;

public class TestTypesOcclusion extends TestBase
{

    public TestTypesOcclusion(String name)
    {
        super(name);
    }

    public void testEnclosing1() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  protected class X {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends test1.Y {\n"+
                "  private class X {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.X.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inner type X not the inherited inner type of A
        assertEquals(_project.getType("test.A.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testEnclosing2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class X {}\n"+
                "public class A {\n"+
                "  private class X {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.X.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inner type X not the top-level type
        assertEquals(_project.getType("test.A.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testEnclosing3() throws ANTLRException, ParseException
    {
        addType("test1.X",
                "package test1;\n"+
                "public class X {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "import test1.X;\n"+
                "public class A {\n"+
                "  private class X {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.X.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inner type X not the imported type
        assertEquals(_project.getType("test.A.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testEnclosing4() throws ANTLRException, ParseException
    {
        addType("test.X",
                "package test;\n"+
                "public class X {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class X {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.X.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inner type X not the type in the same package
        assertEquals(_project.getType("test.A.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testEnclosing5() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class Object {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        Object test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.Object.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inner type Object not the type in java.lang
        assertEquals(_project.getType("test.A.Object"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testEnclosing6() throws ANTLRException, ParseException
    {
        addType("test1.X",
                "package test1;\n"+
                "public class X {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class X {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.X.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inner type X not the imported type
        assertEquals(_project.getType("test.A.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testImport1() throws ANTLRException, ParseException
    {
        addType("test1.X",
                "package test1;\n"+
                "public class X {}\n",
                false);
        addType("test.X",
                "package test;\n"+
                "public class X {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "import test1.X;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses type imported from package test1
        assertEquals(_project.getType("test1.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testImport2() throws ANTLRException, ParseException
    {
        addType("test1.X",
                "package test1;\n"+
                "class X {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // no accessible type found
        assertEquals(null,
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testImport3() throws ANTLRException, ParseException
    {
        // It is possible to import nested types directly via the import
        // statement (it would also be possibly for inner types but they
        // cannot be instantiated without their enclosing type)
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  public static class C {\n"+
                "    public static void test() {}\n"+
                "  }\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "import test2.B.C;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    C.test();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl    = _project.getType("test2.B.C");
        MethodDeclaration   methodDecl  = _project.getType("test1.A").getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        TypeAccess          typeAccess  = (TypeAccess)methodInvoc.getBaseExpression();

        assertEquals(typeDecl,
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc.getMethodDeclaration());
    }

    public void testImport4() throws ANTLRException, ParseException
    {
        addType("test2.Object",
                "package test2;\n"+
                "public class Object {}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "import test2.Object;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    Object var = new Object();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test1.A").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(_project.getType("test2.Object"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testImport5() throws ANTLRException, ParseException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  public static class C {\n"+
                "    public static void test() {}\n"+
                "  }\n"+
                "}\n",
                false);
        addType("test3.D",
                "package test3;\n"+
                "import test2.B.C;\n"+
                "public class D {\n"+
                "  public static C getC() {\n"+
                "    return null;\n"+
                "  }\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    test3.D.getC().test();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl     = _project.getType("test3.D");
        MethodDeclaration   methodDecl   = _project.getType("test1.A").getMethods().get(0);
        ExpressionStatement exprStmt     = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc1 = (MethodInvocation)exprStmt.getExpression();
        MethodInvocation    methodInvoc2 = (MethodInvocation)methodInvoc1.getBaseExpression();
        TypeAccess          typeAccess   = (TypeAccess)methodInvoc2.getBaseExpression();

        assertEquals(typeDecl,
                     typeAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl.getMethods().get(0),
                     methodInvoc2.getMethodDeclaration());
        assertEquals(_project.getType("test2.B.C").getMethods().get(0),
                     methodInvoc1.getMethodDeclaration());
    }

    public void testInheritedInner1() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  protected class X {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class X {}\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inherited inner type of C not the inner type of B
        assertEquals(_project.getType("test1.Y.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner10() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  public class X {}\n"+
                "}\n",
                false);
        addType("test2.Y",
                "package test2;\n"+
                "public class X {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "import test2.X;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inherited inner type of C not the imported type
        assertEquals(_project.getType("test1.Y.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner11() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  class X {}\n"+
                "}\n",
                false);
        addType("test2.X",
                "package test2;\n"+
                "public class X {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "import test2.X;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the imported type
        assertEquals(_project.getType("test2.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner12() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  private class X {}\n"+
                "}\n",
                false);
        addType("test2.X",
                "package test2;\n"+
                "public class X {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "import test2.X;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the imported type
        assertEquals(_project.getType("test2.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner13() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  protected class X {}\n"+
                "}\n",
                false);
        addType("test.Y",
                "package test;\n"+
                "public class X {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inherited inner type of C not the type in the same package
        assertEquals(_project.getType("test1.Y.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner14() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  public class X {}\n"+
                "}\n",
                false);
        addType("test.Y",
                "package test;\n"+
                "public class X {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inherited inner type of C not the type in the same package
        assertEquals(_project.getType("test1.Y.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner15() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  class X {}\n"+
                "}\n",
                false);
        addType("test.Y",
                "package test;\n"+
                "public class X {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the type in the same package
        assertEquals(_project.getType("test.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner16() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  private class X {}\n"+
                "}\n",
                false);
        addType("test.Y",
                "package test;\n"+
                "public class X {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the imported type
        assertEquals(_project.getType("test.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner17() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  protected class Object {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        Object test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inherited inner type of C not the type in java.lang
        assertEquals(_project.getType("test1.Y.Object"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner18() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  public class Object {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        Object test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inherited inner type of C not the type in java.lang
        assertEquals(_project.getType("test1.Y.Object"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner19() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  class Object {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        Object test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the type in java.lang
        assertEquals(_project.getType("java.lang.Object"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner2() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  public class X {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class X {}\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inner type of Y not the inner type of B
        assertEquals(_project.getType("test1.Y.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner20() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  private class Object {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        Object test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the type in java.lang
        assertEquals(_project.getType("java.lang.Object"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner21() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  protected class X {}\n"+
                "}\n",
                false);
        addType("test2.Y",
                "package test2;\n"+
                "public class X {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "import test2.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inherited inner type of C not the imported type
        assertEquals(_project.getType("test1.Y.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner22() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  public class X {}\n"+
                "}\n",
                false);
        addType("test2.Y",
                "package test2;\n"+
                "public class X {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "import test2.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inherited inner type of C not the imported type
        assertEquals(_project.getType("test1.Y.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner23() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  class X {}\n"+
                "}\n",
                false);
        addType("test2.X",
                "package test2;\n"+
                "public class X {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "import test2.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the imported type
        assertEquals(_project.getType("test2.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner24() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  private class X {}\n"+
                "}\n",
                false);
        addType("test2.X",
                "package test2;\n"+
                "public class X {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "import test2.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the imported type
        assertEquals(_project.getType("test2.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner3() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  class X {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class X {}\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inner type X
        assertEquals(_project.getType("test.A.B.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner4() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  private class X {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class X {}\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inner type X
        assertEquals(_project.getType("test.A.B.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner5() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  protected class X {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "class X {}\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inherited inner type of C not the top-level type
        assertEquals(_project.getType("test1.Y.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner6() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  public class X {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "class X {}\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inner type of Y not the top-level type
        assertEquals(_project.getType("test1.Y.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner7() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  class X {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "class X {}\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the top-level type
        assertEquals(_project.getType("test.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner8() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  private class X {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "class X {}\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the top-level type
        assertEquals(_project.getType("test.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner9() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  protected class X {}\n"+
                "}\n",
                false);
        addType("test2.Y",
                "package test2;\n"+
                "public class X {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "import test2.X;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inherited inner type of C not the imported type
        assertEquals(_project.getType("test1.Y.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInner1() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  protected class X {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private class X {}\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration          typeDecl   = _project.getType("test.A.B.C");
        MethodDeclaration        methodDecl = typeDecl.getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inner type of C not the inherited inner type
        assertEquals(typeDecl.getInnerTypes().get("X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInner2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class X {}\n"+
                "    private class C extends test1.Y {\n"+
                "      private class X {}\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration          typeDecl   = _project.getType("test.A.B.C");
        MethodDeclaration        methodDecl = typeDecl.getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inner type of C not the inner type of B
        assertEquals(typeDecl.getInnerTypes().get("X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInner3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class X {}\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private class X {}\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration          typeDecl   = _project.getType("test.A.B.C");
        MethodDeclaration        methodDecl = typeDecl.getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inner type of C not the top-level type
        assertEquals(typeDecl.getInnerTypes().get("X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInner4() throws ANTLRException, ParseException
    {
        addType("test1.X",
                "package test1;\n"+
                "public class X { }\n",
                false);
        addType("test.A",
                "package test;\n"+
                "import test1.X;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private class X {}\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration          typeDecl   = _project.getType("test.A.B.C");
        MethodDeclaration        methodDecl = typeDecl.getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inner type of C not the directly imported type
        assertEquals(typeDecl.getInnerTypes().get("X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInner5() throws ANTLRException, ParseException
    {
        addType("test.X",
                "package test;\n"+
                "public class X { }\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private class X {}\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration          typeDecl   = _project.getType("test.A.B.C");
        MethodDeclaration        methodDecl = typeDecl.getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inner type of C not the type in the same package
        assertEquals(typeDecl.getInnerTypes().get("X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInner6() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private class Object {}\n"+
                "      private void doSomething() {\n"+
                "        Object test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration          typeDecl   = _project.getType("test.A.B.C");
        MethodDeclaration        methodDecl = typeDecl.getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inner type of C not the type in java.lang
        assertEquals(typeDecl.getInnerTypes().get("Object"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testInner7() throws ANTLRException, ParseException
    {
        addType("test1.X",
                "package test1;\n"+
                "public class X { }\n",
                false);
        addType("test.A",
                "package test;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private class X {}\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration          typeDecl   = _project.getType("test.A.B.C");
        MethodDeclaration        methodDecl = typeDecl.getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the inner type of C not the importable type
        assertEquals(typeDecl.getInnerTypes().get("X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testLocal1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private class X {}\n"+
                "      private void doSomething() {\n"+
                "        class X {}\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration         classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(1);

        // the variable declaration uses the local type not the inner type
        assertEquals(classDeclX,
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testLocal2() throws ANTLRException, ParseException
    {
        addType("test1.Y",
                "package test1;\n"+
                "public class Y {\n"+
                "  protected class X {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.Y {\n"+
                "      private void doSomething() {\n"+
                "        class X {}\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration         classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(1);

        // the variable declaration uses the local type not the inherited inner type
        assertEquals(classDeclX,
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testLocal3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class X {}\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {}\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration         classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(1);

        // the variable declaration uses the local type not the inner type of A
        assertEquals(classDeclX,
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testLocal4() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class X {}\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {}\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration         classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(1);

        // the variable declaration uses the local type not the other top-level type
        assertEquals(classDeclX,
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testLocal5() throws ANTLRException, ParseException
    {
        addType("test1.X",
                "package test1;\n"+
                "public class X { }\n",
                false);
        addType("test.A",
                "package test;\n"+
                "import test1.X;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {}\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration         classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(1);

        // the variable declaration uses the local type not the directly imported type
        assertEquals(classDeclX,
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testLocal6() throws ANTLRException, ParseException
    {
        addType("test.X",
                "package test;\n"+
                "public class X { }\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {}\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration         classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(1);

        // the variable declaration uses the local type not the type in the same package
        assertEquals(classDeclX,
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testLocal7() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class Object {}\n"+
                "        Object test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl      = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration         classDeclObject = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        LocalVariableDeclaration varDecl         = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(1);

        // the variable declaration uses the local type not the type in java.lang
        assertEquals(classDeclObject,
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testLocal8() throws ANTLRException, ParseException
    {
        addType("test1.X",
                "package test1;\n"+
                "public class X { }\n",
                false);
        addType("test.A",
                "package test;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        class X {}\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        ClassDeclaration         classDeclX = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(1);

        // the variable declaration uses the local type not the importable type
        assertEquals(classDeclX,
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testSamePackage1() throws ANTLRException, ParseException
    {
        addType("test.Object",
                "package test;\n"+
                "public class Object {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        Object test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the type in the same package
        assertEquals(_project.getType("test.Object"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testSamePackage2() throws ANTLRException, ParseException
    {
        addType("test1.X",
                "package test1;\n"+
                "public class X {}\n",
                false);
        addType("test.X",
                "package test;\n"+
                "public class X {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses type in the same package
        assertEquals(_project.getType("test.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testTopLevel1() throws ANTLRException, ParseException
    {
        addType("test1.X",
                "package test1;\n"+
                "public class X {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "import test1.X;\n"+
                "class X {}\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the top-level type of C not the imported type
        assertEquals(_project.getType("test.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testTopLevel2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class Object {}\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        Object test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the top-level type of C not the imported type
        assertEquals(_project.getType("test.Object"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testTopLevel3() throws ANTLRException, ParseException
    {
        addType("test1.X",
                "package test1;\n"+
                "public class X {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "import test1.*;\n"+
                "class X {}\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        X test;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        // the variable declaration uses the top-level type of C not the imported type
        assertEquals(_project.getType("test.X"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }


    public void testImport6() throws ANTLRException, ParseException
    {
        addType("test2.Object",
                "package test2;\n"+
                "public class Object {}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "import test2.Object;\n"+
                "import java.lang.*;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    Object var = new Object();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl = _project.getType("test1.A").getMethods().get(0);
        LocalVariableDeclaration varDecl    = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(_project.getType("test2.Object"),
                     varDecl.getType().getReferenceBaseTypeDecl());
    }
}
