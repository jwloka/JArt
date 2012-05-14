package jart.test.smelling;
import jart.smelling.IsDataClass;
import jart.test.TestBase;
import jast.ParseException;
import jast.ast.nodes.TypeDeclaration;
import junitx.framework.TestAccessException;


public class TestIsDataClass extends TestBase
{
    private IsDataClass _testObject;

    public TestIsDataClass(String name)
    {
        super(name);
    }

    private int getLocalAccessorCount(TypeDeclaration type)
    {
        try
        {
            Object[] args = { type };

            return ((Integer)invokeWithKey(
                       _testObject,
                       "getLocalAccessorCount_jast.ast.nodes.ClassDeclaration",
                       args)).intValue();
        }
        catch (TestAccessException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(TestIsDataClass.class);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();

        _testObject = new IsDataClass();
    }

    protected void tearDown() throws ParseException
    {
        super.tearDown();

        _testObject = null;
    }

    public void testCheck1() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  int field;\n"
                + "  public void setField(int var) { field = var; }\n"
                + "  public int getField() { return field; }\n"
                + "}\n",
            true);

        assertTrue(_testObject.check(_project, _project.getType("Class1")));
    }

    public void testCheck2() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  int field;\n"
                + "  public void setField(int var) { field = var; }\n"
                + "  public int getField() { return field; }\n"
                + "  public void otherMethod() {}\n"
                + "}\n",
            true);

        assertTrue(!_testObject.check(_project, _project.getType("Class1")));

    }

    public void testCheck3() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  int field1;\n"
                + "  int field2;\n"
                + "  int field3;\n"
                + "  int field4;\n"
                + "  int field5;\n"
                + "  int field6;\n"
                + "  public void setField1(int var) { field1 = var; }\n"
                + "  public void setField2(int var) { field2 = var; }\n"
                + "  public void setField3(int var) { field3 = var; }\n"
                + "  public void setField4(int var) { field4 = var; }\n"
                + "  public void setField5(int var) { field5 = var; }\n"
                + "  public void setField6(int var) { field6 = var; }\n"
                + "  public int getField1() { return field1; }\n"
                + "  public int getField2() { return field2; }\n"
                + "  public int getField3() { return field3; }\n"
                + "  public int getField4() { return field4; }\n"
                + "  public int getField5() { return field5; }\n"
                + "  public int getField6() { return field6; }\n"
                + "  public void otherMethod() {}\n"
                + "}\n",
            true);

        assertTrue(_testObject.check(_project, _project.getType("Class1")));

    }

    public void testGetLocalAccessorCount1()
        throws ParseException, TestAccessException
    {
        addType("Class1", "class Class1 {\n" + "}\n", true);

        assertEquals(0,
                     getLocalAccessorCount(_project.getType("Class1")));
    }

    public void testGetLocalAccessorCount2()
        throws ParseException, TestAccessException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  int field;\n"
                + "  public void setField(int var) {\n"
                + "    field = var;\n"
                + "  }\n"
                + "  public int getField() {\n"
                + "    return field;\n"
                + "  }\n"
                + "}\n",
            true);

        assertEquals(2,
                     getLocalAccessorCount(_project.getType("Class1")));
    }
}
