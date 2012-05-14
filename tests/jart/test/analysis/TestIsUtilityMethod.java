package jart.test.analysis;
import jart.analysis.IsUtilityMethod;
import jart.test.TestBase;
import jast.Global;
import jast.ParseException;
import jast.SyntaxException;
import jast.ast.nodes.FormalParameterList;
import jast.ast.nodes.MethodDeclaration;
import junitx.framework.TestAccessException;

public class TestIsUtilityMethod extends TestBase
{
    private IsUtilityMethod _testObject;

    public TestIsUtilityMethod(String name)
    {
        super(name);
    }

    private boolean call(String methodName, Object arg)
    {
        try
        {
            Object[] args = { arg };

            return ((Boolean)invoke(_testObject, methodName, args)).booleanValue();
        }
        catch (TestAccessException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(TestIsUtilityMethod.class);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();

        _testObject = new IsUtilityMethod();
    }

    protected void tearDown() throws ParseException
    {
        super.tearDown();

        _testObject = null;
    }

    public void testCheck1() throws ParseException
    {
        addType("Class1", "class Class1 {\n" + "  void method() {}\n" + "}\n", true);

        resolve();
        addUsages();

        MethodDeclaration method = _project.getType("Class1").getMethods().get(0);

        assertTrue(!_testObject.check(_project, method));
    }

    public void testCheck2() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n" + "  static void method() {}\n" + "}\n",
            true);

        resolve();
        addUsages();

        MethodDeclaration method = _project.getType("Class1").getMethods().get(0);

        assertTrue(!_testObject.check(_project, method));

    }

    public void testCheck3() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n" + "  final static void method() {}\n" + "}\n",
            true);

        resolve();
        addUsages();

        MethodDeclaration method = _project.getType("Class1").getMethods().get(0);

        assertTrue(!_testObject.check(_project, method));

    }

    public void testCheck4() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n" + "  public final static void method() {}\n" + "}\n",
            true);

        resolve();
        addUsages();

        MethodDeclaration method = _project.getType("Class1").getMethods().get(0);

        assertTrue(_testObject.check(_project, method));
    }

    public void testCheck5() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  public static void method(int p1, int p2, int p3, int p4) {}\n"
                + "}\n",
            true);

        resolve();
        addUsages();

        MethodDeclaration method = _project.getType("Class1").getMethods().get(0);

        assertTrue(_testObject.check(_project, method));
    }

    public void testCheck6() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  int field;\n"
                + "  public static void method(int p1, int p2) {\n"
                + "    field = p1;\n"
                + "  }\n"
                + "}\n",
            true);
        addType("Class2", "class Class2 {}\n" + "}\n", true);

        resolve();
        addUsages();

        MethodDeclaration method = _project.getType("Class1").getMethods().get(0);

        assertTrue(_testObject.check(_project, method));
    }

    public void testCheck7() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  int field;\n"
                + "  public static void method(int p1, int p2) {\n"
                + "    field = p1;\n"
                + "    Class2 var1 = new Class2(p1);\n"
                + "  }\n"
                + "}\n",
            true);
        addType("Class2", "class Class2 {}\n" + "}\n", true);

        resolve();
        addUsages();

        MethodDeclaration method = _project.getType("Class1").getMethods().get(0);

        assertTrue(_testObject.check(_project, method));
    }

    public void testCheck8() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  int field;\n"
                + "  public static void method(int p1, int p2) {\n"
                + "    field = p1;\n"
                + "    Class2 var1 = new Class2(p1);\n"
                + "    Class3 var2 = new Class3(p2);\n"
                + "    Class4 var3 = new Class4(p1);\n"
                + "    Class5 var4 = new Class5(p2);\n"
                + "    Class6 var5 = new Class6(p2);\n"
                + "  }\n"
                + "}\n",
            true);
        addType("Class2", "class Class2 {}\n" + "}\n", true);
        addType("Class3", "class Class3 {}\n" + "}\n", true);
        addType("Class4", "class Class4 {}\n" + "}\n", true);
        addType("Class5", "class Class5 {}\n" + "}\n", true);
        addType("Class6", "class Class6 {}\n" + "}\n", true);

        resolve();
        addUsages();

        MethodDeclaration method = _project.getType("Class1").getMethods().get(0);

        assertTrue(!_testObject.check(_project, method));
    }

    public void testUsesManyParameters1() throws SyntaxException, TestAccessException
    {
        FormalParameterList params = Global.getFactory().createFormalParameterList();

        MethodDeclaration method =
            Global.getFactory().createMethodDeclaration(
                Global.getFactory().createModifiers(),
                null,
                "method",
                params);

        assertTrue(!usesManyParameters(method));
    }

    public void testUsesManyParameters2() throws SyntaxException, TestAccessException
    {
        FormalParameterList params = Global.getFactory().createFormalParameterList();
        params.getParameters().add(
            Global.getFactory().createFormalParameter(
                false,
                Global.getFactory().createType("MyParameter", false),
                "param1"));
        params.getParameters().add(
            Global.getFactory().createFormalParameter(
                false,
                Global.getFactory().createType("MyParameter", false),
                "param2"));
        params.getParameters().add(
            Global.getFactory().createFormalParameter(
                false,
                Global.getFactory().createType("MyParameter", false),
                "param3"));
        params.getParameters().add(
            Global.getFactory().createFormalParameter(
                false,
                Global.getFactory().createType("MyParameter", false),
                "param4"));

        MethodDeclaration method =
            Global.getFactory().createMethodDeclaration(
                Global.getFactory().createModifiers(),
                null,
                "method",
                params);

        assertTrue(usesManyParameters(method));
    }

    public void testUsesManyParameters3() throws SyntaxException, TestAccessException
    {
        FormalParameterList params = Global.getFactory().createFormalParameterList();
        params.getParameters().add(
            Global.getFactory().createFormalParameter(
                false,
                Global.getFactory().createType("MyParameter", false),
                "param1"));
        params.getParameters().add(
            Global.getFactory().createFormalParameter(
                false,
                Global.getFactory().createType("MyParameter", false),
                "param2"));
        params.getParameters().add(
            Global.getFactory().createFormalParameter(
                false,
                Global.getFactory().createType("MyParameter", false),
                "param3"));
        params.getParameters().add(
            Global.getFactory().createFormalParameter(
                false,
                Global.getFactory().createType("MyParameter", false),
                "param4"));
        params.getParameters().add(
            Global.getFactory().createFormalParameter(
                false,
                Global.getFactory().createType("MyParameter", false),
                "param5"));

        MethodDeclaration method =
            Global.getFactory().createMethodDeclaration(
                Global.getFactory().createModifiers(),
                null,
                "method",
                params);

        assertTrue(usesManyParameters(method));
    }

    public void testUsesManyParameters4() throws SyntaxException, TestAccessException
    {
        FormalParameterList params = Global.getFactory().createFormalParameterList();
        params.getParameters().add(
            Global.getFactory().createFormalParameter(
                false,
                Global.getFactory().createType("int", true),
                "param1"));
        params.getParameters().add(
            Global.getFactory().createFormalParameter(
                false,
                Global.getFactory().createType("int", true),
                "param2"));
        params.getParameters().add(
            Global.getFactory().createFormalParameter(
                false,
                Global.getFactory().createType("int", true),
                "param3"));
        params.getParameters().add(
            Global.getFactory().createFormalParameter(
                false,
                Global.getFactory().createType("int", true),
                "param4"));

        MethodDeclaration method =
            Global.getFactory().createMethodDeclaration(
                Global.getFactory().createModifiers(),
                null,
                "method",
                params);

        assertTrue(usesManyParameters(method));
    }

    public void testUsesNoLocalFeature1() throws ParseException, TestAccessException
    {
        addType("Class1", "class Class1 {\n" + "  void method() {}\n" + "}\n", true);

        resolve();
        addUsages();

        // results false, caused by null usage. a size metric
        // characterized by 'few' makes only sense if something
        // is used at all
        assertTrue(!usesNoLocalFeature(_project.getType("Class1").getMethods().get(0)));
    }

    public void testUsesNoLocalFeature2() throws ParseException, TestAccessException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  void method() {\n"
                + "    Class2 var1 = new Class2();\n"
                + "  }\n"
                + "}\n",
            true);

        addType("Class2", "class Class2 {\n" + " Class2() {}\n" + "}\n", true);

        resolve();
        addUsages();

        assertTrue(usesNoLocalFeature(_project.getType("Class1").getMethods().get(0)));
    }

    public void testUsesNoLocalFeature3() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  InnerClass field;\n"
                + "  class InnerClass extends Class2 {}\n"
                + "  void method() {\n"
                + "    field = new Class2();\n"
                + "  }\n"
                + "}\n",
            true);

        addType("Class2", "class Class2 {}\n", true);

        resolve();
        addUsages();

        assertTrue(!usesNoLocalFeature(_project.getType("Class1").getMethods().get(0)));
    }

    public void testUsesNoLocalFeature4() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  Class2 field;\n"
                + "  void method() {\n"
                + "    field = new Class2();\n"
                + "  }\n"
                + "}\n",
            true);

        addType("Class2", "class Class2 {}\n", true);

        resolve();
        addUsages();

        assertTrue(!usesNoLocalFeature(_project.getType("Class1").getMethods().get(0)));
    }

    public void testUsesNoLocalFeature5() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  void method1() {}\n"
                + "  void method2() {\n"
                + "    method1();\n"
                + "  }\n"
                + "}\n",
            true);

        resolve();
        addUsages();

        assertTrue(!usesNoLocalFeature(_project.getType("Class1").getMethods().get(1)));
    }

    public void testUsesOnlyFewRemoteClasses1() throws ParseException
    {
        addType("Class1", "class Class1 {\n" + "  void method() {}\n" + "}\n", true);

        resolve();
        addUsages();

        // results false, caused by null usage. a size metric
        // characterized by 'few' makes only sense if something
        // is used at all
        assertTrue(!usesOnlyFewRemoteClasses(
                        _project.getType("Class1").getMethods().get(0)));
    }

    public void testUsesOnlyFewRemoteClasses2() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  void method() {\n"
                + "    Class2 var1;\n"
                + "    Class3 var2;\n"
                + "  }\n"
                + "}\n",
            true);

        addType("Class2", "class Class2 {}\n", true);

        addType("Class3", "class Class3 {}\n", true);

        resolve();
        addUsages();

        assertTrue(usesOnlyFewRemoteClasses(
                       _project.getType("Class1").getMethods().get(0)));
    }

    public void testUsesOnlyFewRemoteClasses3() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  void method() {\n"
                + "    Class2 var1;\n"
                + "    Class3 var2 = new Class4();\n"
                + "  }\n"
                + "}\n",
            true);

        addType("Class2", "class Class2 {}\n", true);

        addType("Class3", "class Class3 extends Class4 {}\n", true);

        addType("Class4", "class Class4 {}\n", true);

        resolve();
        addUsages();

        assertTrue(!usesOnlyFewRemoteClasses(
                        _project.getType("Class1").getMethods().get(0)));
    }

    public void testUsesOnlyFewRemoteClasses4() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 extends Class2 {\n"
                + "  void method() {\n"
                + "    Class2 var1;\n"
                + "    Class3 var2 = new Class4();\n"
                + "  }\n"
                + "}\n",
            true);

        addType("Class2", "class Class2 {}\n", true);

        addType("Class3", "class Class3 extends Class4 {}\n", true);

        addType("Class4", "class Class4 {}\n", true);

        resolve();
        addUsages();

        assertTrue(usesOnlyFewRemoteClasses(
                       _project.getType("Class1").getMethods().get(0)));
    }

    public void testUsesOnlyFewRemoteClasses5() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  class InnerClass extends Class2 {};\n"
                + "  void method() {\n"
                + "    InnerClass var1;\n"
                + "    Class3     var2 = new Class4();\n"
                + "  }\n"
                + "}\n",
            true);

        addType("Class2", "class Class2 {}\n", true);

        addType("Class3", "class Class3 extends Class4 {}\n", true);

        addType("Class4", "class Class4 {}\n", true);

        resolve();
        addUsages();

        assertTrue(usesOnlyFewRemoteClasses(
                       _project.getType("Class1").getMethods().get(0)));
    }

    public void testUsesOnlyFewRemoteClasses6() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  class InnerClass extends Class2 {};\n"
                + "  void method() {\n"
                + "    Class2 var1;\n"
                + "    Class3 var2 = new Class4();\n"
                + "  }\n"
                + "}\n",
            true);

        addType("Class2", "class Class2 {}\n", true);

        addType("Class3", "class Class3 extends Class4 {}\n", true);

        addType("Class4", "class Class4 {}\n", true);

        resolve();
        addUsages();

        assertTrue(usesOnlyFewRemoteClasses(
                       _project.getType("Class1").getMethods().get(0)));
    }


    private boolean usesManyParameters(MethodDeclaration method)
    {
        try
        {
            Object[] args = { method };

            return ((Boolean)invokeWithKey(
                        _testObject,
                        "usesManyParameters_jast.ast.nodes.MethodDeclaration",
                        args)).booleanValue();
        }
        catch (TestAccessException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }



    private boolean usesNoLocalFeature(MethodDeclaration method)
    {
        try
        {
            Object[] args = { method };

            return ((Boolean)invokeWithKey(
                        _testObject,
                        "usesNoLocalFeature_jast.ast.nodes.MethodDeclaration",
                        args)).booleanValue();
        }
        catch (TestAccessException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }



    private boolean usesOnlyFewRemoteClasses(MethodDeclaration method)
    {
        try
        {
            Object[] args = { method };

            return ((Boolean)invokeWithKey(
                        _testObject,
                        "usesOnlyFewRemoteClasses_jast.ast.nodes.MethodDeclaration",
                        args)).booleanValue();
        }
        catch (TestAccessException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
