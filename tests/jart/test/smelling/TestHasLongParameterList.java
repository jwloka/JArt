package jart.test.smelling;
import jart.smelling.HasLongParameterList;
import jart.test.TestBase;
import jast.ParseException;
import jast.ast.nodes.InvocableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import junitx.framework.TestAccessException;

public class TestHasLongParameterList extends TestBase
{
    private HasLongParameterList _testObject;

    public TestHasLongParameterList(String name)
    {
        super(name);
    }

    private int getFormalParameterCount(InvocableDeclaration decl)
    {
        try
        {
            Object[] args = { decl };

            return ((Integer)invokeWithKey(
                        _testObject,
                        "getFormalParameterCount_jast.ast.nodes.InvocableDeclaration",
                        args)).intValue();
        }
        catch (TestAccessException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(TestHasLongParameterList.class);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();

        _testObject = new HasLongParameterList();
    }

    protected void tearDown() throws ParseException
    {
        super.tearDown();

        _testObject = null;
    }

    public void testCheck1() throws ParseException
    {
        addType("Class1", "class Class1 {\n" + "  void method();\n" + "}\n", true);

        resolve();

        MethodDeclaration method = _project.getType("Class1").getMethods().get(0);

        assertTrue(!_testObject.check(_project, method));
    }

    public void testCheck2() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n" + "  void method(int p1, int p2, int p3);\n" + "}\n",
            true);

        resolve();

        MethodDeclaration method = _project.getType("Class1").getMethods().get(0);

        assertTrue(!_testObject.check(_project, method));
    }

    public void testCheck3() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n" + "  void method(int p1, int p2, int p3, int p4);\n" + "}\n",
            true);

        resolve();

        MethodDeclaration method = _project.getType("Class1").getMethods().get(0);

        assertTrue(_testObject.check(_project, method));
    }

    public void testGetFormalParameterCount1()
        throws ParseException, TestAccessException
    {
        addType("Class1", "class Class1 {\n" + "  void method();\n" + "}\n", true);

        resolve();

        assertEquals(0,
                     getFormalParameterCount(
                         _project.getType("Class1").getMethods().get(0)));
    }

    public void testGetFormalParameterCount2()
        throws ParseException, TestAccessException
    {
        addType(
            "Class1",
            "class Class1 {\n" + "  void method(int p1, int p2);\n" + "}\n",
            true);

        resolve();

        assertEquals(2,
                     getFormalParameterCount(
                          _project.getType("Class1").getMethods().get(0)));
    }
}
