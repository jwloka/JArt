package jart.test.restructuring;
import jart.restructuring.variablelevel.IntroduceParameterObject;
import jast.ParseException;
import jast.ast.Node;
import jast.ast.nodes.ClassDeclaration;
import antlr.ANTLRException;

public class TestIntroduceParameterObject extends TestBase
{

    public TestIntroduceParameterObject(String name)
    {
        super(name);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();
        setRestructuring(new IntroduceParameterObject());
    }

    public void testAnalyze1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething() {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getMethods().get(0) };
        Object[] obj   = { "test.Parameters",
                           "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
        assertTrue(hasNotes());
    }

    public void testAnalyze10() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {}\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B extends A {\n"+
                "  public void doSomething(int arg) {}\n"+
                "}\n",
                true);
        addType("test1.C",
                "package test1;\n"+
                "public class C {\n"+
                "  public void test(test.A obj) {\n"+
                "    obj.doSomething(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.B").getMethods().get(0) };
        Object[] obj   = { "test.Parameters",
                           "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyze11() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {}\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B extends A {\n"+
                "  public void doSomething(int arg) {}\n"+
                "}\n",
                true);
        addType("test.C",
                "package test;\n"+
                "public class C extends B {\n"+
                "  public void doSomething(int arg) {}\n"+
                "}\n",
                true);
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public void test(test.C obj) {\n"+
                "    obj.doSomething(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.B").getMethods().get(0) };
        Object[] obj   = { "test.Parameters",
                           "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyze12() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public static void doSomething(int arg) {}\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B extends A {\n"+
                "  public static void doSomething(int arg) {}\n"+
                "}\n",
                true);
        addType("test1.C",
                "package test1;\n"+
                "public class C {\n"+
                "  public void test() {\n"+
                "    test.B.doSomething(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getMethods().get(0) };
        Object[] obj   = { "test.Parameters",
                           "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze13() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {}\n"+
                "}\n",
                true);
        addType("test.Parameters",
                "package test;\n"+
                "public class Parameters {}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getMethods().get(0) };
        Object[] obj   = { "test.Parameters",
                           "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze14() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {}\n"+
                "}\n",
                true);
        addType("test.Parameters",
                "package test;\n"+
                "public class Parameters {}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getMethods().get(0) };
        Object[] obj   = { "test.Parameters",
                           "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
        assertTrue(hasNotes());
    }

    public void testAnalyze15() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private A() {}\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration classDecl = (ClassDeclaration)_project.getType("test.A");
        Node[]           nodes     = { classDecl.getConstructors().get(0) };
        Object[]         obj       = { "test.Parameters",
                                       "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
        assertTrue(hasNotes());
    }

    public void testAnalyze16() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected A(int arg) {}\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration classDecl = (ClassDeclaration)_project.getType("test.A");
        Node[]           nodes     = { classDecl.getConstructors().get(0) };
        Object[]         obj       = { "test.Parameters",
                                       "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze17() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private A(int arg1, String arg2) {}\n"+
                "  public A() {\n"+
                "    this(0, null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration classDecl = (ClassDeclaration)_project.getType("test.A");
        Node[]           nodes     = { classDecl.getConstructors().get(0) };
        Object[]         obj       = { "test.Parameters",
                                       "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze18() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected A(int arg1, String arg2) {}\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B extends A {\n"+
                "  public B(int arg1, String arg2) {\n"+
                "    super(arg1, arg2);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration classDecl = (ClassDeclaration)_project.getType("test.A");
        Node[]           nodes     = { classDecl.getConstructors().get(0) };
        Object[]         obj       = { "test.Parameters",
                                       "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething(int arg) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getMethods().get(0) };
        Object[] obj   = { "test.Parameters",
                           "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething(int arg) {}\n"+
                "  public void test() {\n"+
                "    doSomething(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getMethods().get(0) };
        Object[] obj   = { "test.Parameters",
                           "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {}\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  public void test() {\n"+
                "    new A().doSomething(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getMethods().get(0) };
        Object[] obj   = { "test.Parameters",
                           "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething(int arg1, String arg2, Object[] arg3) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getMethods().get(0) };
        Object[] obj   = { "test.Parameters",
                           "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze6() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething(int arg1, String arg2, Object[] arg3) {}\n"+
                "  public void test(String arg) {\n"+
                "    doSomething(0, arg, null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getMethods().get(0) };
        Object[] obj   = { "test.Parameters",
                           "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze7() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg1, String arg2, Object[] arg3) {}\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(test.A obj, String arg) {\n"+
                "    obj.doSomething(0, arg, null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getMethods().get(0) };
        Object[] obj   = { "test.Parameters",
                           "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze8() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public static void doSomething(int arg) {}\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test() {\n"+
                "    test.A.doSomething(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getMethods().get(0) };
        Object[] obj   = { "test.Parameters",
                           "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze9() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {}\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B extends A {\n"+
                "  public void doSomething(int arg) {}\n"+
                "}\n",
                true);
        addType("test1.C",
                "package test1;\n"+
                "public class C {\n"+
                "  public void test(test.A obj) {\n"+
                "    obj.doSomething(0);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getMethods().get(0) };
        Object[] obj   = { "test.Parameters",
                           "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testPerformConstructor1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public A() {}\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test() {\n"+
                "    test.A obj = new test.A();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration classDecl = (ClassDeclaration)_project.getType("test.A");
        Node[]           nodes     = { classDecl.getConstructors().get(0) };
        Object[]         obj       = { "test.Parameters",
                                       "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertTrue(getChangedUnits().isEmpty());

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    public A()\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test1;\n\n"+
                     "public class B\n"+
                     "{\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        test.A obj = new test.A();\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test1.B"));
    }

    public void testPerformConstructor2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public A(float[] args) {}\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B extends test.A {\n"+
                "  public B(float[] values) {\n"+
                "    super(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration classDecl = (ClassDeclaration)_project.getType("test.A");
        Node[]           nodes     = { classDecl.getConstructors().get(0) };
        Object[]         obj       = { "test.Parameters",
                                       "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertEquals(1,
                     getNewUnits().getCount());
        assertTrue(getNewUnits().contains(toUnitName("test.Parameters")));
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(2,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().contains(toUnitName("test1.B")));

        assertEquals("package test;\n\n"+
                     "public class Parameters\n"+
                     "{\n"+
                     "    private float[] args = null;\n\n\n"+
                     "    public Parameters(float[] args)\n"+
                     "    {\n"+
                     "        this.args = args;\n"+
                     "    }\n\n\n"+
                     "    public float[] getArgs()\n"+
                     "    {\n"+
                     "        return args;\n"+
                     "    }\n\n"+
                     "    public void setArgs(float[] args)\n"+
                     "    {\n"+
                     "        this.args = args;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.Parameters"));
        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    public A(Parameters param)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test1;\n\n"+
                     "public class B extends test.A\n"+
                     "{\n"+
                     "    public B(float[] values)\n"+
                     "    {\n"+
                     "        super(new test.Parameters(values));\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test1.B"));
    }

    public void testPerformConstructor3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public A(final float[] args) {}\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(float[] values) {\n"+
                "    test.A obj = new test.A(values);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration classDecl = (ClassDeclaration)_project.getType("test.A");
        Node[]           nodes     = { classDecl.getConstructors().get(0) };
        Object[]         obj       = { "test.Parameters",
                                       "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertEquals(1,
                     getNewUnits().getCount());
        assertTrue(getNewUnits().contains(toUnitName("test.Parameters")));
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(2,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().contains(toUnitName("test1.B")));

        assertEquals("package test;\n\n"+
                     "public class Parameters\n"+
                     "{\n"+
                     "    private float[] args = null;\n\n\n"+
                     "    public Parameters(float[] args)\n"+
                     "    {\n"+
                     "        this.args = args;\n"+
                     "    }\n\n\n"+
                     "    public float[] getArgs()\n"+
                     "    {\n"+
                     "        return args;\n"+
                     "    }\n\n"+
                     "    public void setArgs(float[] args)\n"+
                     "    {\n"+
                     "        this.args = args;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.Parameters"));
        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    public A(Parameters param)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test1;\n\n"+
                     "public class B\n"+
                     "{\n"+
                     "    public void test(float[] values)\n"+
                     "    {\n"+
                     "        test.A obj = new test.A(new test.Parameters(values));\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test1.B"));
    }

    public void testPerformConstructor4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public A(int arg1, String arg2, Object[] arg3) {}\n"+
                "  public A() {\n"+
                "    this(0, null, null);\n"+
                "  }\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test() {\n"+
                "    test.A obj = new test.A(0, \"\", null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration classDecl = (ClassDeclaration)_project.getType("test.A");
        Node[]           nodes     = { classDecl.getConstructors().get(0) };
        Object[]         obj       = { "test.Parameters",
                                       "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertEquals(1,
                     getNewUnits().getCount());
        assertTrue(getNewUnits().contains(toUnitName("test.Parameters")));
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(2,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().contains(toUnitName("test1.B")));

        assertEquals("package test;\n\n"+
                     "public class Parameters\n"+
                     "{\n"+
                     "    private int arg1 = 0;\n\n"+
                     "    private String arg2 = null;\n\n"+
                     "    private Object[] arg3 = null;\n\n\n"+
                     "    public Parameters(int arg1, String arg2, Object[] arg3)\n"+
                     "    {\n"+
                     "        this.arg1 = arg1;\n"+
                     "        this.arg2 = arg2;\n"+
                     "        this.arg3 = arg3;\n"+
                     "    }\n\n\n"+
                     "    public int getArg1()\n"+
                     "    {\n"+
                     "        return arg1;\n"+
                     "    }\n\n"+
                     "    public void setArg1(int arg1)\n"+
                     "    {\n"+
                     "        this.arg1 = arg1;\n"+
                     "    }\n\n"+
                     "    public String getArg2()\n"+
                     "    {\n"+
                     "        return arg2;\n"+
                     "    }\n\n"+
                     "    public void setArg2(String arg2)\n"+
                     "    {\n"+
                     "        this.arg2 = arg2;\n"+
                     "    }\n\n"+
                     "    public Object[] getArg3()\n"+
                     "    {\n"+
                     "        return arg3;\n"+
                     "    }\n\n"+
                     "    public void setArg3(Object[] arg3)\n"+
                     "    {\n"+
                     "        this.arg3 = arg3;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.Parameters"));
        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    public A(Parameters param)\n"+
                     "    {}\n\n"+
                     "    public A()\n"+
                     "    {\n"+
                     "        this(new Parameters(0, null, null));\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test1;\n\n"+
                     "public class B\n"+
                     "{\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        test.A obj = new test.A(new test.Parameters(0, \"\", null));\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test1.B"));
    }

    public void testPerformMethod1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {}\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(test.A obj) {\n"+
                "    obj.doSomething();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getMethods().get(0) };
        Object[] obj   = { "test.Parameters",
                           "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertTrue(getChangedUnits().isEmpty());

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    public void doSomething()\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test1;\n\n"+
                     "public class B\n"+
                     "{\n"+
                     "    public void test(test.A obj)\n"+
                     "    {\n"+
                     "        obj.doSomething();\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test1.B"));
    }

    public void testPerformMethod2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(String arg) {\n"+
                "    String val = arg;\n"+
                "  }\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(test.A obj) {\n"+
                "    obj.doSomething(\"\");\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getMethods().get(0) };
        Object[] obj   = { "test.Parameters",
                           "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertEquals(1,
                     getNewUnits().getCount());
        assertTrue(getNewUnits().contains(toUnitName("test.Parameters")));
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(2,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().contains(toUnitName("test1.B")));

        assertEquals("package test;\n\n"+
                     "public class Parameters\n"+
                     "{\n"+
                     "    private String arg = null;\n\n\n"+
                     "    public Parameters(String arg)\n"+
                     "    {\n"+
                     "        this.arg = arg;\n"+
                     "    }\n\n\n"+
                     "    public String getArg()\n"+
                     "    {\n"+
                     "        return arg;\n"+
                     "    }\n\n"+
                     "    public void setArg(String arg)\n"+
                     "    {\n"+
                     "        this.arg = arg;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.Parameters"));
        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    public void doSomething(Parameters param)\n"+
                     "    {\n"+
                     "        String val = param.getArg();\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test1;\n\n"+
                     "public class B\n"+
                     "{\n"+
                     "    public void test(test.A obj)\n"+
                     "    {\n"+
                     "        obj.doSomething(new test.Parameters(\"\"));\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test1.B"));
    }

    public void testPerformMethod3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(final String arg) {\n"+
                "    String val = arg;\n"+
                "  }\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(test.A obj) {\n"+
                "    obj.doSomething(\"\");\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getMethods().get(0) };
        Object[] obj   = { "test.Parameters",
                           "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertEquals(1,
                     getNewUnits().getCount());
        assertTrue(getNewUnits().contains(toUnitName("test.Parameters")));
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(2,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().contains(toUnitName("test1.B")));

        assertEquals("package test;\n\n"+
                     "public class Parameters\n"+
                     "{\n"+
                     "    private String arg = null;\n\n\n"+
                     "    public Parameters(String arg)\n"+
                     "    {\n"+
                     "        this.arg = arg;\n"+
                     "    }\n\n\n"+
                     "    public String getArg()\n"+
                     "    {\n"+
                     "        return arg;\n"+
                     "    }\n\n"+
                     "    public void setArg(String arg)\n"+
                     "    {\n"+
                     "        this.arg = arg;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.Parameters"));
        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    public void doSomething(Parameters param)\n"+
                     "    {\n"+
                     "        String val = param.getArg();\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test1;\n\n"+
                     "public class B\n"+
                     "{\n"+
                     "    public void test(test.A obj)\n"+
                     "    {\n"+
                     "        obj.doSomething(new test.Parameters(\"\"));\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test1.B"));
    }

    public void testPerformMethod4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg1, String arg2, Object[] arg3) {\n"+
                "    String val = arg2;\n"+
                "    arg1 = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(test.A obj) {\n"+
                "    obj.doSomething(0, \"\", null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getMethods().get(0) };
        Object[] obj   = { "test.Parameters",
                           "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertEquals(1,
                     getNewUnits().getCount());
        assertTrue(getNewUnits().contains(toUnitName("test.Parameters")));
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(2,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().contains(toUnitName("test1.B")));

        assertEquals("package test;\n\n"+
                     "public class Parameters\n"+
                     "{\n"+
                     "    private int arg1 = 0;\n\n"+
                     "    private String arg2 = null;\n\n"+
                     "    private Object[] arg3 = null;\n\n\n"+
                     "    public Parameters(int arg1, String arg2, Object[] arg3)\n"+
                     "    {\n"+
                     "        this.arg1 = arg1;\n"+
                     "        this.arg2 = arg2;\n"+
                     "        this.arg3 = arg3;\n"+
                     "    }\n\n\n"+
                     "    public int getArg1()\n"+
                     "    {\n"+
                     "        return arg1;\n"+
                     "    }\n\n"+
                     "    public void setArg1(int arg1)\n"+
                     "    {\n"+
                     "        this.arg1 = arg1;\n"+
                     "    }\n\n"+
                     "    public String getArg2()\n"+
                     "    {\n"+
                     "        return arg2;\n"+
                     "    }\n\n"+
                     "    public void setArg2(String arg2)\n"+
                     "    {\n"+
                     "        this.arg2 = arg2;\n"+
                     "    }\n\n"+
                     "    public Object[] getArg3()\n"+
                     "    {\n"+
                     "        return arg3;\n"+
                     "    }\n\n"+
                     "    public void setArg3(Object[] arg3)\n"+
                     "    {\n"+
                     "        this.arg3 = arg3;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.Parameters"));
        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    public void doSomething(Parameters param)\n"+
                     "    {\n"+
                     "        int    varArg1 = param.getArg1();\n"+
                     "        String val = param.getArg2();\n\n"+
                     "        varArg1 = 1;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test1;\n\n"+
                     "public class B\n"+
                     "{\n"+
                     "    public void test(test.A obj)\n"+
                     "    {\n"+
                     "        obj.doSomething(new test.Parameters(0, \"\", null));\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test1.B"));
    }

    public void testPerformMethod5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  void doSomething(int arg) {\n"+
                "    int val = arg;\n"+
                "  }\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B extends A {\n"+
                "  void doSomething(int arg) {\n"+
                "    int val = arg;\n"+
                "  }\n"+
                "}\n",
                true);
        addType("test.C",
                "package test;\n"+
                "public class C {\n"+
                "  public void testA(A obj) {\n"+
                "    obj.doSomething(0);\n"+
                "  }\n"+
                "  public void testB(B obj) {\n"+
                "    obj.doSomething(1);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.B").getMethods().get(0) };
        Object[] obj   = { "test.Parameters",
                           "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertEquals(1,
                     getNewUnits().getCount());
        assertTrue(getNewUnits().contains(toUnitName("test.Parameters")));
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(3,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().contains(toUnitName("test.B")));
        assertTrue(getChangedUnits().contains(toUnitName("test.C")));

        assertEquals("package test;\n\n"+
                     "public class Parameters\n"+
                     "{\n"+
                     "    private int arg = 0;\n\n\n"+
                     "    public Parameters(int arg)\n"+
                     "    {\n"+
                     "        this.arg = arg;\n"+
                     "    }\n\n\n"+
                     "    public int getArg()\n"+
                     "    {\n"+
                     "        return arg;\n"+
                     "    }\n\n"+
                     "    public void setArg(int arg)\n"+
                     "    {\n"+
                     "        this.arg = arg;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.Parameters"));
        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    void doSomething(Parameters param)\n"+
                     "    {\n"+
                     "        int val = param.getArg();\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test;\n\n"+
                     "public class B extends A\n"+
                     "{\n"+
                     "    void doSomething(Parameters param)\n"+
                     "    {\n"+
                     "        int val = param.getArg();\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.B"));
        assertEquals("package test;\n\n"+
                     "public class C\n"+
                     "{\n"+
                     "    public void testA(A obj)\n"+
                     "    {\n"+
                     "        obj.doSomething(new Parameters(0));\n"+
                     "    }\n\n"+
                     "    public void testB(B obj)\n"+
                     "    {\n"+
                     "        obj.doSomething(new Parameters(1));\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.C"));
    }

    public void testPerformMethod6() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public abstract class A {\n"+
                "  protected abstract void doSomething(int arg1, String arg2, Object[] arg3);\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B extends test.A {\n"+
                "  protected void doSomething(int arg1, String arg2, Object[] arg3) {\n"+
                "    String val = arg2;\n"+
                "    arg1 = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {\n"+
                "  public void testA() {\n"+
                "    test.A obj = (test.A)this;\n"+
                "    obj.doSomething(0, \"\", null);\n"+
                "  }\n"+
                "  public void testB() {\n"+
                "    doSomething(0, \"\", null);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getMethods().get(0) };
        Object[] obj   = { "test.Parameters",
                           "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertEquals(1,
                     getNewUnits().getCount());
        assertTrue(getNewUnits().contains(toUnitName("test.Parameters")));
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(3,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().contains(toUnitName("test1.B")));
        assertTrue(getChangedUnits().contains(toUnitName("test2.C")));

        assertEquals("package test;\n\n"+
                     "public class Parameters\n"+
                     "{\n"+
                     "    private int arg1 = 0;\n\n"+
                     "    private String arg2 = null;\n\n"+
                     "    private Object[] arg3 = null;\n\n\n"+
                     "    public Parameters(int arg1, String arg2, Object[] arg3)\n"+
                     "    {\n"+
                     "        this.arg1 = arg1;\n"+
                     "        this.arg2 = arg2;\n"+
                     "        this.arg3 = arg3;\n"+
                     "    }\n\n\n"+
                     "    public int getArg1()\n"+
                     "    {\n"+
                     "        return arg1;\n"+
                     "    }\n\n"+
                     "    public void setArg1(int arg1)\n"+
                     "    {\n"+
                     "        this.arg1 = arg1;\n"+
                     "    }\n\n"+
                     "    public String getArg2()\n"+
                     "    {\n"+
                     "        return arg2;\n"+
                     "    }\n\n"+
                     "    public void setArg2(String arg2)\n"+
                     "    {\n"+
                     "        this.arg2 = arg2;\n"+
                     "    }\n\n"+
                     "    public Object[] getArg3()\n"+
                     "    {\n"+
                     "        return arg3;\n"+
                     "    }\n\n"+
                     "    public void setArg3(Object[] arg3)\n"+
                     "    {\n"+
                     "        this.arg3 = arg3;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.Parameters"));
        assertEquals("package test;\n\n"+
                     "public abstract class A\n"+
                     "{\n"+
                     "    protected abstract void doSomething(Parameters param);\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test1;\n\n"+
                     "public class B extends test.A\n"+
                     "{\n"+
                     "    protected void doSomething(test.Parameters param)\n"+
                     "    {\n"+
                     "        int    varArg1 = param.getArg1();\n"+
                     "        String val = param.getArg2();\n\n"+
                     "        varArg1 = 1;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test1.B"));
        assertEquals("package test2;\n\n"+
                     "public class C extends test1.B\n"+
                     "{\n"+
                     "    public void testA()\n"+
                     "    {\n"+
                     "        test.A obj = (test.A)this;\n\n"+
                     "        obj.doSomething(new test.Parameters(0, \"\", null));\n"+
                     "    }\n\n"+
                     "    public void testB()\n"+
                     "    {\n"+
                     "        doSomething(new test.Parameters(0, \"\", null));\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test2.C"));
    }

    public void testPerformMethod7() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public void doSomething(int arg);\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B implements test.A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    arg = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        addType("test2.C",
                "package test2;\n"+
                "public interface C extends test.A {\n"+
                "  public void doSomething(int arg);\n"+
                "}\n",
                true);
        addType("test3.D",
                "package test3;\n"+
                "public class D implements test2.C {\n"+
                "  public void doSomething(int arg) {}\n"+
                "}\n",
                true);
        addType("test4.E",
                "package test4;\n"+
                "public class E {\n"+
                "  public void testA(test.A obj) {\n"+
                "    obj.doSomething(0);\n"+
                "  }\n"+
                "  public void testB(test1.B obj) {\n"+
                "    obj.doSomething(1);\n"+
                "  }\n"+
                "  public void testC(test2.C obj) {\n"+
                "    obj.doSomething(2);\n"+
                "  }\n"+
                "  public void testD(test3.D obj) {\n"+
                "    obj.doSomething(3);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test2.C").getMethods().get(0) };
        Object[] obj   = { "test4.Parameters",
                           "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertEquals(1,
                     getNewUnits().getCount());
        assertTrue(getNewUnits().contains(toUnitName("test4.Parameters")));
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(5,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().contains(toUnitName("test1.B")));
        assertTrue(getChangedUnits().contains(toUnitName("test2.C")));
        assertTrue(getChangedUnits().contains(toUnitName("test3.D")));
        assertTrue(getChangedUnits().contains(toUnitName("test4.E")));

        assertEquals("package test4;\n\n"+
                     "public class Parameters\n"+
                     "{\n"+
                     "    private int arg = 0;\n\n\n"+
                     "    public Parameters(int arg)\n"+
                     "    {\n"+
                     "        this.arg = arg;\n"+
                     "    }\n\n\n"+
                     "    public int getArg()\n"+
                     "    {\n"+
                     "        return arg;\n"+
                     "    }\n\n"+
                     "    public void setArg(int arg)\n"+
                     "    {\n"+
                     "        this.arg = arg;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test4.Parameters"));
        assertEquals("package test;\n\n"+
                     "public interface A\n"+
                     "{\n"+
                     "    void doSomething(test4.Parameters param);\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test1;\n\n"+
                     "public class B implements test.A\n"+
                     "{\n"+
                     "    public void doSomething(test4.Parameters param)\n"+
                     "    {\n"+
                     "        int varArg = param.getArg();\n\n"+
                     "        varArg = 0;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test1.B"));
        assertEquals("package test2;\n\n"+
                     "public interface C extends test.A\n"+
                     "{\n"+
                     "    void doSomething(test4.Parameters param);\n"+
                     "}\n",
                     prettyPrint("test2.C"));
        assertEquals("package test3;\n\n"+
                     "public class D implements test2.C\n"+
                     "{\n"+
                     "    public void doSomething(test4.Parameters param)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test3.D"));
        assertEquals("package test4;\n\n"+
                     "public class E\n"+
                     "{\n"+
                     "    public void testA(test.A obj)\n"+
                     "    {\n"+
                     "        obj.doSomething(new Parameters(0));\n"+
                     "    }\n\n"+
                     "    public void testB(test1.B obj)\n"+
                     "    {\n"+
                     "        obj.doSomething(new Parameters(1));\n"+
                     "    }\n\n"+
                     "    public void testC(test2.C obj)\n"+
                     "    {\n"+
                     "        obj.doSomething(new Parameters(2));\n"+
                     "    }\n\n"+
                     "    public void testD(test3.D obj)\n"+
                     "    {\n"+
                     "        obj.doSomething(new Parameters(3));\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test4.E"));
    }

    public void testPerformMethod8() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public static void doSomething(int arg) {}\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B extends A {\n"+
                "  public static void doSomething(int arg) {}\n"+
                "}\n",
                true);
        addType("test.C",
                "package test;\n"+
                "public class C {\n"+
                "  public void testA(A obj) {\n"+
                "    obj.doSomething(0);\n"+
                "  }\n"+
                "  public void testB(B obj) {\n"+
                "    obj.doSomething(1);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.B").getMethods().get(0) };
        Object[] obj   = { "test.Parameters",
                           "param" };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertEquals(1,
                     getNewUnits().getCount());
        assertTrue(getNewUnits().contains(toUnitName("test.Parameters")));
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(2,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.B")));
        assertTrue(getChangedUnits().contains(toUnitName("test.C")));

        assertEquals("package test;\n\n"+
                     "public class Parameters\n"+
                     "{\n"+
                     "    private int arg = 0;\n\n\n"+
                     "    public Parameters(int arg)\n"+
                     "    {\n"+
                     "        this.arg = arg;\n"+
                     "    }\n\n\n"+
                     "    public int getArg()\n"+
                     "    {\n"+
                     "        return arg;\n"+
                     "    }\n\n"+
                     "    public void setArg(int arg)\n"+
                     "    {\n"+
                     "        this.arg = arg;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.Parameters"));
        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    public static void doSomething(int arg)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test;\n\n"+
                     "public class B extends A\n"+
                     "{\n"+
                     "    public static void doSomething(Parameters param)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.B"));
        assertEquals("package test;\n\n"+
                     "public class C\n"+
                     "{\n"+
                     "    public void testA(A obj)\n"+
                     "    {\n"+
                     "        obj.doSomething(0);\n"+
                     "    }\n\n"+
                     "    public void testB(B obj)\n"+
                     "    {\n"+
                     "        obj.doSomething(new Parameters(1));\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.C"));
    }
}
