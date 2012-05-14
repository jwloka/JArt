package jast.test.analysis;
import jast.ParseException;
import jast.analysis.TypeUsages;
import jast.analysis.UsageVisitor;
import jast.analysis.Usages;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.TypeDeclaration;



/**
 * Special cases for usages relations
 * - inner class uses features of its outer class
 * - inner class uses features which are inherited by its outer class
 * - outer class uses private features of the inner class
 * - class B extends class A, and B::innerClass extends A::innerClass
 *   using inherited and local features
 */
public class TestUsageVisitor extends TestBase
{

    private UsageVisitor _testObject;

    public TestUsageVisitor(String name)
    {
        super(name);
    }

    public void setUp() throws ParseException
    {
        super.setUp();

        _testObject = new UsageVisitor();
    }

    public void tearDown() throws ParseException
    {
        super.tearDown();

        _testObject = null;
    }

    public void testAddingRightUsages1() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  int method1() { return 0; }\n"+
                "}\n",
                true);
        addType("Class2",
                "public class Class2 extends Class1 {\n"+
                "  class InnerClass {\n"+
                "    int field = method1();\n"+
                "  };\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        assertEquals("Class1",
                     getTypeUsages(_project.getType("Class1")).getLocalType().getQualifiedName());
        assertEquals("Class2",
                     getTypeUsages(_project.getType("Class2")).getLocalType().getQualifiedName());
        assertEquals("Class2.InnerClass",
                     getTypeUsages(_project.getType("Class2.InnerClass")).getLocalType().getQualifiedName());

    }

    public void testConstructorUsage1() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  Class1() {}\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        ClassDeclaration       classDecl = (ClassDeclaration)_project.getType("Class1");
        ConstructorDeclaration constr    = classDecl.getConstructors().get(0);
        TypeUsages             usages    = getTypeUsages(classDecl);

        assertTrue(!usages.isUsed(classDecl));
        assertTrue(!usages.isUsed(constr));
    }

    public void testConstructorUsage2() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  Class1() {}\n"+
                "  Class1(Object arg) { this(); }\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        ClassDeclaration       classDecl = (ClassDeclaration)_project.getType("Class1");
        ConstructorDeclaration constr1   = classDecl.getConstructors().get(0);
        ConstructorDeclaration constr2   = classDecl.getConstructors().get(1);
        TypeUsages             usages    = getTypeUsages(classDecl);

        assertTrue(usages.isUsed(classDecl));
        assertTrue(usages.isUsed(constr1));
        assertTrue(usages.getTypeUsage(classDecl).isUsed(constr1));
        assertTrue(!usages.isUsed(constr2));
    }

    public void testConstructorUsage3() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  Class1() {}\n"+
                "  Class1(int idx) { this(); }\n"+
                "}\n",
                true);
        addType("Class2",
                "public class Class2 {\n"+
                "  void method(int idx) {\n"+
                "    Class1 a = new Class1();\n"+
                "    Class1 b = new Class1(idx);\n"+
                "  }\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        ClassDeclaration       classDecl = (ClassDeclaration)_project.getType("Class1");
        ConstructorDeclaration constr1   = classDecl.getConstructors().get(0);
        ConstructorDeclaration constr2   = classDecl.getConstructors().get(1);
        TypeUsages             usages    = getTypeUsages(_project.getType("Class2"));

        assertTrue(usages.isUsed(classDecl));
        assertTrue(usages.isUsed(constr1));
        assertTrue(usages.isUsed(constr2));
        assertTrue(usages.getTypeUsage(classDecl).isUsed(constr1));
        assertTrue(usages.getTypeUsage(classDecl).isUsed(constr2));
    }

    public void testConstructorUsage4() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  Class1() {}\n"+
                "  Class1(Object arg) { this(); }\n"+
                "}\n",
                true);
        addType("Class2",
                "public class Class2 extends Class1 {\n"+
                "  Class2() { super(); }\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        ClassDeclaration       classDecl1 = (ClassDeclaration)_project.getType("Class1");
        ClassDeclaration       classDecl2 = (ClassDeclaration)_project.getType("Class2");
        ConstructorDeclaration constr1    = classDecl1.getConstructors().get(0);
        TypeUsages             usages1    = getTypeUsages(classDecl1);
        TypeUsages             usages2    = getTypeUsages(classDecl2);

        assertTrue(usages1.isUsed(classDecl1));
        assertTrue(usages1.isUsed(constr1));
        assertTrue(usages1.getTypeUsage(classDecl1).isUsed(constr1));

        assertTrue(usages2.isUsed(classDecl1));
        assertTrue(usages2.isUsed(constr1));
        assertTrue(usages2.getTypeUsage(classDecl2).isUsed(constr1));
    }

    public void testFieldUsage1() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  int field;\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration typeDecl = _project.getType("Class1");
        TypeUsages      usages   = getTypeUsages(typeDecl);

        assertTrue(!usages.isUsed(typeDecl));
        assertTrue(!usages.isUsed(typeDecl.getFields().get(0)));
    }

    public void testFieldUsage2() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  int field1;\n"+
                "}\n",
                true);
        addType("Class2",
                "public class Class2 {\n"+
                "  int field2 = new Class1().field1;\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration  typeDecl  = _project.getType("Class1");
        FieldDeclaration fieldDecl = typeDecl.getFields().get(0);
        TypeUsages       usages    = getTypeUsages(_project.getType("Class2"));

        assertTrue(usages.isUsed(typeDecl));
        assertTrue(usages.isUsed(fieldDecl));
        assertTrue(usages.getTypeUsage(typeDecl).isUsed(fieldDecl));
    }

    public void testFieldUsage3() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  int field;\n"+
                "}\n",
                true);
        addType("Class2",
                "public class Class2 {\n"+
                "  void method(Class1 cls) {\n"+
                "    int var = cls.field;\n"+
                "  }\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration  typeDecl  = _project.getType("Class1");
        FieldDeclaration fieldDecl = typeDecl.getFields().get(0);
        TypeUsages       usages    = getTypeUsages(_project.getType("Class2"));

        assertTrue(usages.isUsed(typeDecl));
        assertTrue(usages.isUsed(fieldDecl));
        assertTrue(usages.getTypeUsage(typeDecl).isUsed(fieldDecl));
    }

    public void testFieldUsage4() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  static int field;\n"+
                "}\n",
                true);
        addType("Class2",
                "public class Class2 {\n"+
                "  void method() {\n"+
                "    int var = Class1.field;\n"+
                "  }\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration  typeDecl  = _project.getType("Class1");
        FieldDeclaration fieldDecl = typeDecl.getFields().get(0);
        TypeUsages       usages    = getTypeUsages(_project.getType("Class2"));

        assertTrue(usages.isUsed(typeDecl));
        assertTrue(usages.isUsed(fieldDecl));
        assertTrue(usages.getTypeUsage(typeDecl).isUsed(fieldDecl));
    }

    public void testFieldUsage5() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  static int field;\n"+
                "  void method() {\n"+
                "    int var = field;\n"+
                "  }\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration  typeDecl  = _project.getType("Class1");
        FieldDeclaration fieldDecl = typeDecl.getFields().get(0);
        TypeUsages       usages    = getTypeUsages(typeDecl);

        assertTrue(usages.isUsed(fieldDecl));
        assertTrue(usages.getTypeUsage(typeDecl).isUsed(fieldDecl));
    }

    public void testFieldUsage6() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  int field1;\n"+
                "  int field2 = field1;\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration typeDecl = _project.getType("Class1");
        TypeUsages      usages   = getTypeUsages(typeDecl);

        assertTrue(usages.isUsed(typeDecl));
        assertTrue(usages.isUsed(typeDecl.getFields().get(0)));
    }

    public void testFieldUsage7() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "int field1;\n"+
                "int field2 = field1;\n"+
                "int field3 = field2;\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration  typeDecl   = _project.getType("Class1");
        FieldDeclaration fieldDecl1 = typeDecl.getFields().get(0);
        FieldDeclaration fieldDecl2 = typeDecl.getFields().get(1);
        TypeUsages       usages     = getTypeUsages(typeDecl);

        assertTrue(usages.isUsed(typeDecl));
        assertTrue(usages.isUsed(fieldDecl1));
        assertTrue(usages.isUsed(fieldDecl2));
        assertTrue(usages.getTypeUsage(typeDecl).isUsed(fieldDecl1));
        assertTrue(usages.getTypeUsage(typeDecl).isUsed(fieldDecl2));
    }

    public void testFieldUsage8() throws ParseException
    {
        addType("Class1",
                "class Class1 {\n"+
                "  int field;\n"+
                "  public static void method(int p1, int p2) {\n"+
                "    field = p1;\n"+
                "  }\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration  typeDecl  = _project.getType("Class1");
        FieldDeclaration fieldDecl = typeDecl.getFields().get(0);
        TypeUsages       usages    = getTypeUsages(typeDecl);

        assertTrue(usages.isUsed(typeDecl));
        assertTrue(usages.isUsed(fieldDecl));
        assertTrue(usages.getTypeUsage(typeDecl).isUsed(fieldDecl));
    }

    public void testMethodUsage1() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  void method1() {}\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration typeDecl = _project.getType("Class1");
        TypeUsages      usages   = getTypeUsages(typeDecl);

        assertTrue(!usages.isUsed(typeDecl));
        assertTrue(!usages.isUsed(typeDecl.getMethods().get(0)));
    }

    public void testMethodUsage10() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "void method1() {}\n"+
                "void method2() { this.method1(); }\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration   typeDecl   = _project.getType("Class1");
        MethodDeclaration methodDecl = typeDecl.getMethods().get(0);
        TypeUsages        usages     = getTypeUsages(typeDecl);

        assertTrue(usages.isUsed(methodDecl));
        assertTrue(usages.getTypeUsage(typeDecl).isUsed(methodDecl));
    }

    public void testMethodUsage11() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  void method1() {}\n"+
                "}\n",
                true);
        addType("Class2",
                "public class Class2 extends Class1 {\n"+
                "  void method1() { super.method1(); }\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration   typeDecl1  = _project.getType("Class1");
        TypeDeclaration   typeDecl2  = _project.getType("Class2");
        MethodDeclaration methodDecl = typeDecl1.getMethods().get(0);
        TypeUsages        usages     = getTypeUsages(typeDecl2);

        assertTrue(usages.isUsed(typeDecl1));
        assertTrue(usages.isUsed(methodDecl));
        assertTrue(usages.getTypeUsage(typeDecl1).isUsed(methodDecl));
        assertTrue(!usages.isUsed(typeDecl2));
    }

    public void testMethodUsage12() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  class InnerClass {\n"+
                "    void method2() { Class1.this.method1(); }\n"+
                "  };\n"+
                "  void method1() {}\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration   typeDecl   = _project.getType("Class1");
        MethodDeclaration methodDecl = typeDecl.getMethods().get(0);
        TypeUsages        usages1    = getTypeUsages(typeDecl);
        TypeUsages        usages2    = getTypeUsages(typeDecl.getInnerTypes().get("InnerClass"));

        assertTrue(usages1.isUsed(typeDecl));
        assertTrue(usages1.isUsed(methodDecl));
        assertTrue(usages1.getTypeUsage(typeDecl).isUsed(methodDecl));

        assertTrue(usages2.isUsed(typeDecl));
        assertTrue(usages2.isUsed(methodDecl));
        assertTrue(usages2.getTypeUsage(typeDecl).isUsed(methodDecl));

    }

    public void testMethodUsage13() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  class InnerClass {\n"+
                "    int field = Class1.this.method();\n"+
                "  };\n"+
                "  int method() { return 0; }\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration   typeDecl   = _project.getType("Class1");
        MethodDeclaration methodDecl = typeDecl.getMethods().get(0);
        TypeUsages        usages1    = getTypeUsages(typeDecl);
        TypeUsages        usages2    = getTypeUsages(typeDecl.getInnerTypes().get("InnerClass"));

        assertTrue(usages1.isUsed(typeDecl));
        assertTrue(usages1.isUsed(methodDecl));
        assertTrue(usages1.getTypeUsage(typeDecl).isUsed(methodDecl));

        assertTrue(usages2.isUsed(typeDecl));
        assertTrue(usages2.isUsed(methodDecl));
        assertTrue(usages2.getTypeUsage(typeDecl).isUsed(methodDecl));
    }

    public void testMethodUsage14() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  int method() { return 0; }\n"+
                "}\n",
                true);
        addType("Class2",
                "public class Class2 extends Class1 {\n"+
                "  class InnerClass {\n"+
                "    int field = Class2.this.method();\n"+
                "  };\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration   typeDecl1  = _project.getType("Class1");
        TypeDeclaration   typeDecl2  = _project.getType("Class2");
        MethodDeclaration methodDecl = typeDecl1.getMethods().get(0);
        TypeUsages        usages1    = getTypeUsages(typeDecl2);
        TypeUsages        usages2    = getTypeUsages(typeDecl2.getInnerTypes().get("InnerClass"));

        assertTrue(usages1.isUsed(typeDecl1));
        assertTrue(usages1.isUsed(typeDecl2));
        assertTrue(usages1.isUsed(methodDecl));
        assertTrue(usages1.getTypeUsage(typeDecl2).isUsed(methodDecl));

        assertTrue(usages2.isUsed(typeDecl2));
        assertTrue(usages2.isUsed(methodDecl));
        assertTrue(usages2.getTypeUsage(typeDecl2).isUsed(methodDecl));
    }

    public void testMethodUsage15() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  int method() { return 0; }\n"+
                "}\n",
                true);
        addType("Class2",
                "public class Class2 extends Class1 {\n"+
                "  class InnerClass {\n"+
                "    int field = Class2.this.method();\n"+
                "  };\n"+
                "  int method() { return 0; }\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration   typeDecl1   = _project.getType("Class1");
        TypeDeclaration   typeDecl2   = _project.getType("Class2");
        MethodDeclaration methodDecl1 = typeDecl1.getMethods().get(0);
        MethodDeclaration methodDecl2 = typeDecl2.getMethods().get(0);
        TypeUsages        usages1     = getTypeUsages(typeDecl2);
        TypeUsages        usages2     = getTypeUsages(typeDecl2.getInnerTypes().get("InnerClass"));

        assertTrue(usages1.isUsed(typeDecl1));
        assertTrue(usages1.isUsed(typeDecl2));
        assertTrue(usages1.isUsed(methodDecl2));
        assertTrue(usages1.getTypeUsage(typeDecl2).isUsed(methodDecl2));

        assertTrue(usages2.isUsed(typeDecl2));
        assertTrue(!usages2.isUsed(methodDecl1));
        assertTrue(usages2.isUsed(methodDecl2));
        assertTrue(usages2.getTypeUsage(typeDecl2).isUsed(methodDecl2));
    }

    public void testMethodUsage16() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  private class InnerClass {\n"+
                "    private int method2() { return 0; }\n"+
                "  };\n"+
                "  int method1() {\n"+
                "    return new InnerClass().method2();\n"+
                "  }\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration   typeDecl1  = _project.getType("Class1");
        TypeDeclaration   typeDecl2  = typeDecl1.getInnerTypes().get("InnerClass");
        MethodDeclaration methodDecl = typeDecl2.getMethods().get(0);
        TypeUsages        usages     = getTypeUsages(typeDecl1);

        assertTrue(usages.isUsed(typeDecl2));
        assertTrue(usages.isUsed(methodDecl));
        assertTrue(usages.getTypeUsage(typeDecl2).isUsed(methodDecl));
    }

    // Class2 uses Class2.InnerClass.method1()
    public void testMethodUsage17() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  class InnerClass {\n"+
                "    int method1() { return 0; }\n"+
                "  };\n"+
                "}\n",
                true);
        addType("Class2",
                "public class Class2 extends Class1 {\n"+
                "  private class InnerClass extends Class1.InnerClass {\n"+
                "    int method3() { return 0; }\n"+
                "  };\n"+
                "  int method2() {\n"+
                "   return new InnerClass().method1();\n"+
                "  }\n "+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration   typeDecl1  = _project.getType("Class1.InnerClass");
        TypeDeclaration   typeDecl2  = _project.getType("Class2");
        TypeDeclaration   typeDecl3  = typeDecl2.getInnerTypes().get("InnerClass");
        MethodDeclaration methodDecl = typeDecl1.getMethods().get(0);
        TypeUsages        usages1    = getTypeUsages(typeDecl2);
        TypeUsages        usages2    = getTypeUsages(typeDecl3);

        assertTrue(usages1.isUsed(methodDecl));
        assertTrue(usages1.isUsed(typeDecl1));
        assertTrue(usages1.isUsed(typeDecl3));
        assertTrue(usages1.getTypeUsage(typeDecl3).isUsed(methodDecl));

        assertTrue(usages2.isUsed(typeDecl1));
    }

    public void testMethodUsage18() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  class InnerClass {\n"+
                "   int method1() { return 0; }\n"+
                "  };\n"+
                "}\n",
                true);
        addType("Class2",
                "public class Class2 extends Class1 {\n"+
                "  private class InnerClass extends Class1.InnerClass {\n"+
                "    int method1() { return super.method1(); }\n"+
                "  };\n"+
                "  int method2() {\n"+
                "    return new InnerClass().method1();\n"+
                "  }\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration   typeDecl1   = _project.getType("Class1");
        TypeDeclaration   typeDecl2   = typeDecl1.getInnerTypes().get("InnerClass");
        TypeDeclaration   typeDecl3   = _project.getType("Class2");
        TypeDeclaration   typeDecl4   = typeDecl3.getInnerTypes().get("InnerClass");
        MethodDeclaration methodDecl1 = typeDecl2.getMethods().get(0);
        MethodDeclaration methodDecl2 = typeDecl4.getMethods().get(0);
        TypeUsages        usages1     = getTypeUsages(typeDecl1);
        TypeUsages        usages2     = getTypeUsages(typeDecl2);
        TypeUsages        usages3     = getTypeUsages(typeDecl3);
        TypeUsages        usages4     = getTypeUsages(typeDecl4);

        assertTrue(!usages1.isUsed(methodDecl1));

        assertTrue(!usages2.isUsed(methodDecl1));

        assertTrue(usages3.isUsed(typeDecl1));
        assertTrue(usages3.isUsed(typeDecl2));
        assertTrue(usages3.isUsed(typeDecl4));
        assertTrue(usages3.isUsed(methodDecl1));
        assertTrue(usages3.getTypeUsage(typeDecl2).isUsed(methodDecl1));
        assertTrue(usages3.isUsed(methodDecl2));
        assertTrue(usages3.getTypeUsage(typeDecl4).isUsed(methodDecl2));

        assertTrue(usages4.isUsed(typeDecl2));
        assertTrue(usages4.isUsed(methodDecl1));
        assertTrue(usages4.getTypeUsage(typeDecl2).isUsed(methodDecl1));
    }

    public void testMethodUsage19() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  void method1() {}\n"+
                "  void method2() {}\n"+
                "  private class InnerClass1 {\n"+
                "    private void method() { Class1.this.method1(); }\n"+
                "  };\n"+
                "  private class InnerClass2 {\n"+
                "    private void method() { Class1.this.method2(); }\n"+
                "  };\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration   typeDecl    = _project.getType("Class1");
        MethodDeclaration methodDecl1 = typeDecl.getMethods().get(0);
        MethodDeclaration methodDecl2 = typeDecl.getMethods().get(1);
        TypeUsages        usages1     = getTypeUsages(typeDecl);
        TypeUsages        usages2     = getTypeUsages(typeDecl.getInnerTypes().get("InnerClass1"));
        TypeUsages        usages3     = getTypeUsages(typeDecl.getInnerTypes().get("InnerClass2"));

        assertTrue(usages1.isUsed(typeDecl));
        assertTrue(usages1.isUsed(methodDecl1));
        assertTrue(usages1.getTypeUsage(typeDecl).isUsed(methodDecl1));
        assertTrue(usages1.isUsed(methodDecl2));
        assertTrue(usages1.getTypeUsage(typeDecl).isUsed(methodDecl2));

        assertTrue(usages2.isUsed(typeDecl));
        assertTrue(usages2.isUsed(methodDecl1));
        assertTrue(usages2.getTypeUsage(typeDecl).isUsed(methodDecl1));

        assertTrue(usages3.isUsed(typeDecl));
        assertTrue(usages3.isUsed(methodDecl2));
        assertTrue(usages3.getTypeUsage(typeDecl).isUsed(methodDecl2));
    }

    public void testMethodUsage2() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  void method1() {}\n"+
                "}\n",
                true);
        addType("Class2",
                "public class Class2 {\n"+
                "  void method2(Class1 cls) {\n"+
                "    cls.method1();\n"+
                "  }\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration   typeDecl   = _project.getType("Class1");
        MethodDeclaration methodDecl = typeDecl.getMethods().get(0);
        TypeUsages        usages     = getTypeUsages(_project.getType("Class2"));

        assertTrue(usages.isUsed(typeDecl));
        assertTrue(usages.isUsed(methodDecl));
        assertTrue(usages.getTypeUsage(typeDecl).isUsed(methodDecl));
    }

    public void testMethodUsage3() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  void method1() {}\n"+
                "}\n",
                true);
        addType("Class2",
                "public class Class2 {\n"+
                "  void method2(Class1 cls) {\n"+
                "    cls.method1();\n"+
                "  }\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration   typeDecl1   = _project.getType("Class1");
        TypeDeclaration   typeDecl2   = _project.getType("Class2");
        MethodDeclaration methodDecl1 = typeDecl1.getMethods().get(0);
        MethodDeclaration methodDecl2 = typeDecl2.getMethods().get(0);
        TypeUsages        usages      = getTypeUsages(typeDecl2);

        assertTrue(usages.isUsed(typeDecl1));
        assertTrue(usages.isUsed(methodDecl1));
        assertTrue(usages.getTypeUsage(typeDecl1).isUsed(methodDecl1));
    }

    public void testMethodUsage4() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  static int method1() { return 0; }\n"+
                "}\n",
                true);
        addType("Class2",
                "public class Class2 {\n"+
                "  static int idx = Class1.method1();\n"+
                "  void method2() {}\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration   typeDecl   = _project.getType("Class1");
        MethodDeclaration methodDecl = typeDecl.getMethods().get(0);
        TypeUsages        usages     = getTypeUsages(_project.getType("Class2"));

        assertTrue(usages.isUsed(typeDecl));
        assertTrue(usages.isUsed(methodDecl));
        assertTrue(usages.getTypeUsage(typeDecl).isUsed(methodDecl));
    }

    public void testMethodUsage5() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  void method1() {}\n"+
                "}\n",
                true);
        addType("Class2",
                "public class Class2 extends Class1 {\n"+
                "  void method1() {}\n"+
                "}\n",
                true);
        addType("Class3",
                "public class Class3 {\n"+
                "  void method2(Class1 cls) {"+
                "    cls.method1()\n;"+
                "  }\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration   typeDecl   = _project.getType("Class1");
        MethodDeclaration methodDecl = typeDecl.getMethods().get(0);
        TypeUsages        usages1    = getTypeUsages(_project.getType("Class2"));
        TypeUsages        usages2    = getTypeUsages(_project.getType("Class3"));

        assertTrue(usages1.isUsed(typeDecl));

        assertTrue(usages2.isUsed(typeDecl));
        assertTrue(usages2.isUsed(methodDecl));
        assertTrue(usages2.getTypeUsage(typeDecl).isUsed(methodDecl));
    }

    public void testMethodUsage6() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  void method1() {}\n"+
                "}\n",
                true);
        addType("Class2",
                "public class Class2 extends Class1 {}\n",
                true);
        addType("Class3",
                "public class Class3 {\n"+
                "  void method2(Class2 cls) {"+
                "    cls.method1()\n;"+
                "  }\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration   typeDecl1  = _project.getType("Class1");
        TypeDeclaration   typeDecl2  = _project.getType("Class2");
        MethodDeclaration methodDecl = typeDecl1.getMethods().get(0);
        TypeUsages        usages1    = getTypeUsages(typeDecl2);
        TypeUsages        usages2    = getTypeUsages(_project.getType("Class3"));

        assertTrue(usages1.isUsed(typeDecl1));

        assertTrue(usages2.isUsed(typeDecl2));
        assertTrue(usages2.isUsed(methodDecl));
        assertTrue(usages2.getTypeUsage(typeDecl2).isUsed(methodDecl));
    }

    public void testMethodUsage7() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  void method1() {}\n"+
                "}\n",
                true);
        addType("Class2",
                "public class Class2 extends Class1 {\n"+
                "  void method1() {}\n"+
                "}\n",
                true);
        addType("Class3",
                "public class Class3 {\n"+
                "  void method2(Class2 cls) {"+
                "    cls.method1()\n;"+
                "  }\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration   typeDecl   = _project.getType("Class2");
        MethodDeclaration methodDecl = typeDecl.getMethods().get(0);
        TypeUsages        usages1    = getTypeUsages(typeDecl);
        TypeUsages        usages2    = getTypeUsages(_project.getType("Class3"));

        assertTrue(usages1.isUsed(_project.getType("Class1")));

        assertTrue(usages2.isUsed(typeDecl));
        assertTrue(usages2.getTypeUsage(typeDecl).isUsed(methodDecl));
    }

    public void testMethodUsage8() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  void method1() {}\n"+
                "}\n",
                true);
        addType("Class2",
                "public class Class2 extends Class1 {\n"+
                "  void method1() {}\n"+
                "}\n",
                true);
        addType("Class3",
                "public class Class3 {\n"+
                "  void method2(Class1 cls) {"+
                "    cls.method1()\n;"+
                "  }\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration   typeDecl   = _project.getType("Class1");
        MethodDeclaration methodDecl = typeDecl.getMethods().get(0);
        TypeUsages        usages1    = getTypeUsages(_project.getType("Class2"));
        TypeUsages        usages2    = getTypeUsages(_project.getType("Class3"));

        assertTrue(usages1.isUsed(typeDecl));

        assertTrue(usages2.isUsed(typeDecl));
        assertTrue(usages2.getTypeUsage(typeDecl).isUsed(methodDecl));
    }

    public void testMethodUsage9() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  void method1() {}\n"+
                "  void method2() { method1(); }\n"+
                "}\n",
                true);

        resolve();
        _testObject.visitProject(_project);

        TypeDeclaration   typeDecl   = _project.getType("Class1");
        MethodDeclaration methodDecl = typeDecl.getMethods().get(0);
        TypeUsages        usages     = getTypeUsages(typeDecl);

        assertTrue(usages.isUsed(methodDecl));
        assertTrue(usages.getTypeUsage(typeDecl).isUsed(methodDecl));
    }


    public TypeUsages getTypeUsages(TypeDeclaration typeDecl)
    {
        return (TypeUsages)typeDecl.getProperty(Usages.PROPERTY_LABEL);
    }
}
