package jart.test.smelling;
import jart.smelling.IsUnnecessaryOpen;
import jart.test.TestBase;
import jast.ParseException;
import jast.ast.nodes.ClassDeclaration;

public class TestIsUnnecessaryOpen extends TestBase
{

    private IsUnnecessaryOpen _testObject;

    public TestIsUnnecessaryOpen(String name)
    {
        super(name);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(TestIsUnnecessaryOpen.class);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();

        _testObject = new IsUnnecessaryOpen();
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
                + "  public void accessor(int var) {\n"
                + "    field = var;\n"
                + "  }\n"
                + "}\n",
            true);

        resolve();
        addUsages();

        ClassDeclaration type = (ClassDeclaration) _project.getType("Class1");

        assertTrue(_testObject.check(_project, type));
    }

    public void testCheck2() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  public int field;\n"
                + "  void accessor(int var) {\n"
                + "    field = var;\n"
                + "  }\n"
                + "}\n",
            true);

        resolve();
        addUsages();

        ClassDeclaration type = (ClassDeclaration) _project.getType("Class1");

        assertTrue(_testObject.check(_project, type));
    }

    public void testCheck3() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  public int field;\n"
                + "  public void accessor(int var) {\n"
                + "    field = var;\n"
                + "  }\n"
                + "}\n",
            true);

        resolve();
        addUsages();

        ClassDeclaration type = (ClassDeclaration) _project.getType("Class1");

        assertTrue(_testObject.check(_project, type));
    }

    public void testCheck4() throws ParseException
    {
        addType("Class1", "class Class1 {\n" + "  int field;\n" + "}\n", true);

        resolve();
        addUsages();

        ClassDeclaration type = (ClassDeclaration) _project.getType("Class1");

        assertTrue(_testObject.check(_project, type));
    }

    public void testCheck5() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  public int field;\n"
                + "  void accessor(int var) {\n"
                + "    field = var;\n"
                + "  }\n"
                + "}\n",
            true);

        addType(
            "Class2",
            "class Class2 {\n"
                + "  void method(int param)  {\n"
                + "    new Class1().accessor(param);\n"
                + "  }\n"
                + "}\n",
            true);

        resolve();
        addUsages();

        ClassDeclaration type = (ClassDeclaration) _project.getType("Class1");

        assertTrue(_testObject.check(_project, type));
    }

    public void testCheck6() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  public int field;\n"
                + "  public void accessor(int var) {\n"
                + "    field = var;\n"
                + "  }\n"
                + "}\n",
            true);

        addType(
            "Class2",
            "class Class2 {\n"
                + "  void method(int param)  {\n"
                + "    new Class1().accessor(param);\n"
                + "  }\n"
                + "}\n",
            true);

        resolve();
        addUsages();

        ClassDeclaration type = (ClassDeclaration) _project.getType("Class1");

        assertTrue(_testObject.check(_project, type));
    }

    public void testGetUnnecessaryVisibleAccessors1() throws ParseException
    {
        addType("Class1", "class Class1 {\n" + "}\n", true);

        resolve();
        addUsages();

        ClassDeclaration type = (ClassDeclaration) _project.getType("Class1");

        assertTrue(
            _testObject.getUnnecessaryVisibleAccessors(_project, type).isEmpty());
    }

    public void testGetUnnecessaryVisibleAccessors2() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  int field;\n"
                + "  void accessor(int var) {\n"
                + "    field = var;\n"
                + "  }\n"
                + "}\n",
            true);

        resolve();
        addUsages();

        ClassDeclaration type = (ClassDeclaration) _project.getType("Class1");

        assertTrue(
            _testObject.getUnnecessaryVisibleAccessors(_project, type).isEmpty());
    }

    public void testGetUnnecessaryVisibleAccessors3() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  int field;\n"
                + "  public void accessor(int var) {\n"
                + "    field = var;\n"
                + "  }\n"
                + "}\n",
            true);

        resolve();
        addUsages();

        ClassDeclaration type = (ClassDeclaration) _project.getType("Class1");

        assertTrue(
            !_testObject.getUnnecessaryVisibleAccessors(_project, type).isEmpty());
    }

    public void testGetUnnecessaryVisibleAccessors4() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  int field;\n"
                + "  public void accessor(int var) {\n"
                + "    field = var;\n"
                + "  }\n"
                + "}\n",
            true);

        addType(
            "Class2",
            "class Class2 extends Class1 {\n"
                + "  void method(int param)  {\n"
                + "    super.accessor(param);\n"
                + "  }\n"
                + "}\n",
            true);

        resolve();
        addUsages();

        ClassDeclaration type = (ClassDeclaration) _project.getType("Class1");

        assertTrue(
            !_testObject.getUnnecessaryVisibleAccessors(_project, type).isEmpty());
    }

    public void testGetUnnecessaryVisibleAccessors5() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  int field;\n"
                + "  public void accessor(int var) {\n"
                + "    field = var;\n"
                + "  }\n"
                + "}\n",
            true);

        addType(
            "Class2",
            "class Class2 {\n"
                + "  void method(int param)  {\n"
                + "    new Class1().accessor(param);\n"
                + "  }\n"
                + "}\n",
            true);

        resolve();
        addUsages();

        ClassDeclaration type = (ClassDeclaration) _project.getType("Class1");

        assertTrue(
            _testObject.getUnnecessaryVisibleAccessors(_project, type).isEmpty());
    }

    public void testGetUnnecessaryVisibleFields1() throws ParseException
    {
        addType("Class1", "class Class1 {\n" + "}\n", true);

        resolve();
        addUsages();

        ClassDeclaration type = (ClassDeclaration) _project.getType("Class1");

        assertTrue(_testObject.getUnnecessaryVisibleFields(_project, type).isEmpty());
    }

    public void testGetUnnecessaryVisibleFields2() throws ParseException
    {
        addType("Class1", "class Class1 {\n" + "  int field;\n" + "}\n", true);

        resolve();
        addUsages();

        ClassDeclaration type = (ClassDeclaration) _project.getType("Class1");

        assertTrue(!_testObject.getUnnecessaryVisibleFields(_project, type).isEmpty());
    }

    public void testGetUnnecessaryVisibleFields3() throws ParseException
    {
        addType("Class1", "class Class1 {\n" + "  public int field;\n" + "}\n", true);

        resolve();
        addUsages();

        ClassDeclaration type = (ClassDeclaration) _project.getType("Class1");

        assertTrue(!_testObject.getUnnecessaryVisibleFields(_project, type).isEmpty());
    }
}
