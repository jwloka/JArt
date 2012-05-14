package jart.test.smelling;
import jart.smelling.IsInappropriatelyIntimate;
import jart.test.TestBase;
import jast.ParseException;
import jast.ast.nodes.TypeDeclaration;
import junitx.framework.TestAccessException;

public class TestIsInappropriatelyIntimate extends TestBase
{
    private IsInappropriatelyIntimate _testObject;

    public TestIsInappropriatelyIntimate(String name)
    {
        super(name);
    }

    private int getUsageCountOf(TypeDeclaration type1, TypeDeclaration type2)
    {
        try
        {
            Object[] args = { type1, type2 };

            return ((Integer)invokeWithKey(
                       _testObject,
                       "getUsageCountOf_jast.ast.nodes.TypeDeclaration_jast.ast.nodes.TypeDeclaration",
                       args)).intValue();
        }
        catch (TestAccessException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(TestIsInappropriatelyIntimate.class);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();

        _testObject = new IsInappropriatelyIntimate();
    }

    protected void tearDown() throws ParseException
    {
        super.tearDown();

        _testObject = null;
    }

    public void testGetUsageCountBy1() throws ParseException, TestAccessException
    {
        addType("Class1", "class Class1 {\n" + "}\n", true);
        addType("Class2", "class Class2 {\n" + "}\n", true);

        resolve();
        addUsages();

        assertEquals(0,
                     getUsageCountOf(
                         _project.getType("Class2"),
                         _project.getType("Class1")));
    }

    public void testGetUsageCountBy2() throws ParseException, TestAccessException
    {
        addType(
            "Class1",
            "class Class1 {\n" + "  static int method() { return 0; }\n" + "}\n",
            true);
        addType(
            "Class2",
            "class Class2 {\n" + "  int field = Class1.method();\n" + "}\n",
            true);

        resolve();
        addUsages();

        assertEquals(1,
                     getUsageCountOf(
                         _project.getType("Class2"),
                         _project.getType("Class1")));
    }

    public void testGetUsageCountBy3() throws ParseException, TestAccessException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  static int method1() { return 0; }\n"
                + "  void method2() {}\n"
                + "}\n",
            true);
        addType(
            "Class2",
            "class Class2 {\n"
                + "  int field = Class1.method1();\n"
                + "  void method(Class1 param) { param.method2(); }\n"
                + "}\n",
            true);

        resolve();
        addUsages();

        assertEquals(2,
                     getUsageCountOf(
                         _project.getType("Class2"),
                         _project.getType("Class1")));
    }

    public void testGetUsageCountBy4() throws ParseException, TestAccessException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  static int method1() { return 0; }\n"
                + "  void method2() {}\n"
                + "}\n",
            true);
        addType(
            "Class2",
            "class Class2 {\n"
                + "  class InnerClass {\n"
                + "    int field = Class1.method1();\n"
                + "    void method(Class1 param) { param.method2(); }\n"
                + "  }\n"
                + "}\n",
            true);

        resolve();
        addUsages();

        assertEquals(0,
                     getUsageCountOf(
                         _project.getType("Class2"),
                         _project.getType("Class1")));
        assertEquals(2,
                     getUsageCountOf(
                         _project.getType("Class2.InnerClass"),
                         _project.getType("Class1")));
    }

    public void testGetUsageCountOf1() throws ParseException, TestAccessException
    {
        addType("Class1", "class Class1 {\n" + "}\n", true);
        addType("Class2", "class Class2 {\n" + "}\n", true);

        resolve();
        addUsages();

        assertEquals(0,
                     getUsageCountOf(
                         _project.getType("Class1"),
                         _project.getType("Class2")));
    }

    public void testGetUsageCountOf2() throws ParseException, TestAccessException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  Class2 field;\n"
                + "  void method (Class2 param) {\n"
                + "    field = param;\n"
                + "  }\n"
                + "}\n",
            true);
        addType("Class2", "class Class2 {\n" + "}\n", true);

        resolve();
        addUsages();

        assertEquals(0,
                     getUsageCountOf(
                         _project.getType("Class1"),
                         _project.getType("Class2")));
    }

    public void testGetUsageCountOf3() throws ParseException, TestAccessException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  Class2 field;\n"
                + "  void method (Class2 param) {\n"
                + "    param.method();\n"
                + "  }\n"
                + "}\n",
            true);
        addType(
            "Class2",
            "class Class2 {\n"
                + "  void method() {}\n"
                + "}\n",
            true);

        resolve();
        addUsages();

        assertEquals(1,
                     getUsageCountOf(
                         _project.getType("Class1"),
                         _project.getType("Class2")));
    }

    public void testGetUsageCountOf4() throws ParseException, TestAccessException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  int field;\n"
                + "  void method (Class2 param) {\n"
                + "    param.method();\n"
                + "    field = param.field;\n"
                + "  }\n"
                + "}\n",
            true);
        addType(
            "Class2",
            "class Class2 {\n" + "  int field;\n" + "  void method() {}\n" + "}\n",
            true);

        resolve();
        addUsages();

        assertEquals(2,
                     getUsageCountOf(
                         _project.getType("Class1"),
                         _project.getType("Class2")));
    }

    public void testGetUsageCountOf5() throws ParseException, TestAccessException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  int field;\n"
                + "  void method (Class2 param) {\n"
                + "    param.InnerClass.method();\n"
                + "    field = param.InnerClass.field;\n"
                + "  }\n"
                + "}\n",
            true);
        addType(
            "Class2",
            "class Class2 {\n"
                + "  class InnerClass {\n"
                + "    int field;\n"
                + "    void method() {}\n"
                + "  }\n"
                + "}\n",
            true);

        resolve();
        addUsages();

        assertEquals(0,
                     getUsageCountOf(
                         _project.getType("Class1"),
                         _project.getType("Class2")));
        assertEquals(2,
                     getUsageCountOf(
                         _project.getType("Class1"),
                         _project.getType("Class2.InnerClass")));
    }
}
