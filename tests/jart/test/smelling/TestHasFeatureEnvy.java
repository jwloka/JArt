package jart.test.smelling;
import jart.smelling.HasFeatureEnvy;
import jart.test.TestBase;
import jast.ParseException;
import jast.ast.nodes.MethodDeclaration;
import junitx.framework.TestAccessException;

public class TestHasFeatureEnvy extends TestBase
{
    private HasFeatureEnvy _testObject;

    public TestHasFeatureEnvy(String name)
    {
        super(name);
    }

    private boolean usesEnoughRemoteFeatures(MethodDeclaration method)
    {
        try
        {
            Object[] args = { method };

            return ((Boolean)invokeWithKey(
                       _testObject,
                       "usesEnoughRemoteFeatures_jast.ast.Node",
                       args)).booleanValue();
        }
        catch (TestAccessException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(TestHasFeatureEnvy.class);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();

        _testObject = new HasFeatureEnvy();
    }

    protected void tearDown() throws ParseException
    {
        super.tearDown();

        _testObject = null;
    }

    public void testCheckMethod1() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n" + "  void addTimeStampToDate() {}\n" + "}\n",
            true);

        resolve();
        addUsages();

        MethodDeclaration method = _project.getType("Class1").getMethods().get(0);

        assertTrue(_testObject.check(_project, method));
    }

    public void testCheckMethod2() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  public static final void addTimeStampToDate() {}\n"
                + "}\n",
            true);

        resolve();
        addUsages();

        MethodDeclaration method = _project.getType("Class1").getMethods().get(0);

        // method appears as utility method
        assertTrue(!_testObject.check(_project, method));
    }

    public void testCheckMethod3() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  void method2(Class2 param) {\n"
                + "    method1();\n"
                + "    param.method1();\n"
                + "    param.method2();\n"
                + "    param.method3();\n"
                + "    param.method4();\n"
                + "    param.method5();\n"
                + "  }\n"
                + "}\n",
            true);

        addType(
            "Class2",
            "class Class2 {\n"
                + "  void method1() {}\n"
                + "  void method2() {}\n"
                + "  void method3() {}\n"
                + "  void method4() {}\n"
                + "  void method5() {}\n"
                + "}\n",
            true);

        resolve();
        addUsages();

        MethodDeclaration method = _project.getType("Class1").getMethods().get(0);

        assertTrue(_testObject.check(_project, method));
    }

    public void testCheckMethod4() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  public static final void utilMethod() {\n"
                + "    param.method1();\n"
                + "    param.method2();\n"
                + "    param.method3();\n"
                + "    param.method4();\n"
                + "    param.method5();\n"
                + "  }\n"
                + "}\n",
            true);

        addType(
            "Class2",
            "class Class2 {\n"
                + "  void method1() {}\n"
                + "  void method2() {}\n"
                + "  void method3() {}\n"
                + "  void method4() {}\n"
                + "  void method5() {}\n"
                + "}\n",
            true);

        resolve();
        addUsages();

        MethodDeclaration method = _project.getType("Class1").getMethods().get(0);
        assertTrue(!_testObject.check(_project, method));
    }

    public void testUsesEnoughRemoteFeatures1()
        throws ParseException, TestAccessException
    {
        addType(
            "Class1",
            "class Class1 {\n" + "  void method() {\n" + "  }\n" + "}\n",
            true);

        resolve();
        addUsages();

        assertTrue(!usesEnoughRemoteFeatures(
                        _project.getType("Class1").getMethods().get(0)));
    }

    public void testUsesEnoughRemoteFeatures2()
        throws ParseException, TestAccessException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  void method(Class2 param) {\n"
                + "    param.method();\n"
                + "  }\n"
                + "}\n",
            true);

        addType("Class2", "class Class2 {\n" + "  void method() {}\n" + "}\n", true);

        resolve();
        addUsages();

        assertTrue(usesEnoughRemoteFeatures(
                       _project.getType("Class1").getMethods().get(0)));
    }

    public void testUsesEnoughRemoteFeatures3()
        throws ParseException, TestAccessException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  void method1() {}\n"
                + "  void method2(Class2 param) {\n"
                + "    param.method1();\n"
                + "    param.method2();\n"
                + "  }\n"
                + "}\n",
            true);

        addType(
            "Class2",
            "class Class2 {\n"
                + "  void method1() {}\n"
                + "  void method2() {}\n"
                + "  void method3() {}\n"
                + "  void method4() {}\n"
                + "  void method5() {}\n"
                + "  void method6() {}\n"
                + "}\n",
            true);

        resolve();
        addUsages();

        assertTrue(usesEnoughRemoteFeatures(
                      _project.getType("Class1").getMethods().get(1)));
    }

    public void testUsesEnoughRemoteFeatures4()
        throws ParseException, TestAccessException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  void method1() {}\n"
                + "  void method2(Class2 param) {\n"
                + "    method1();\n"
                + "    param.method1();\n"
                + "    param.method2();\n"
                + "  }\n"
                + "}\n",
            true);

        addType(
            "Class2",
            "class Class2 {\n" + "  void method1() {}\n" + "  void method2() {}\n" + "}\n",
            true);

        resolve();
        addUsages();

        assertTrue(usesEnoughRemoteFeatures(
                      _project.getType("Class1").getMethods().get(1)));
    }
}
