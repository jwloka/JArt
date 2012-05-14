package jart.test.smelling;
import jart.smelling.HasAwfullyLongName;
import jart.test.TestBase;
import jast.ParseException;
import jast.ast.nodes.MethodDeclaration;
import junitx.framework.TestAccessException;


public class TestHasAwfullyLongName extends TestBase
{
    private HasAwfullyLongName _testObject;

    public TestHasAwfullyLongName(String name)
    {
        super(name);
    }

    private boolean hasAwfulName(MethodDeclaration method)
    {
        try
        {
            Object[] args = { method };

            return ((Boolean)invokeWithKey(
                       _testObject,
                       "hasAwfulName_jast.ast.nodes.MethodDeclaration",
                       args)).booleanValue();
        }
        catch (TestAccessException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private boolean hasLongName(MethodDeclaration method)
    {
        try
        {
            Object[] args = { method };

            return ((Boolean)invokeWithKey(
                       _testObject,
                       "hasLongName_jast.ast.nodes.MethodDeclaration",
                       args)).booleanValue();
        }
        catch (TestAccessException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(TestHasAwfullyLongName.class);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();

        _testObject = new HasAwfullyLongName();
    }

    protected void tearDown() throws ParseException
    {
        super.tearDown();

        _testObject = null;
    }

    public void testCheck1() throws ParseException
    {
        addType("Class",
                "class Class {\n"+
                "  void addItemToElement(Item item) {}\n"+
                "}",
                true);

        assertTrue(
            _testObject.check(
                _project,
                _project.getType("Class").getMethods().get(0)));
    }

    public void testCheck2() throws ParseException
    {
        addType("Class",
                "class Class {\n"+
                "  void getItemFromElement(String name) {}\n"+
                "}",
                true);

        assertTrue(
            _testObject.check(
                _project,
                _project.getType("Class").getMethods().get(0)));
    }

    public void testCheck3() throws ParseException
    {
        addType("Class",
                "class Class {\n"+
                "  void setElementWithString(String str) {}\n"+
                "}",
                true);

        assertTrue(
            _testObject.check(
                _project,
                _project.getType("Class").getMethods().get(0)));
    }

    public void testCheck4() throws ParseException
    {
        addType("Class",
                "class Class {\n"+
                "  void copyStringFromFile(String fname) {}\n"+
                "}",
                true);

        assertTrue(
            _testObject.check(
                _project,
                _project.getType("Class").getMethods().get(0)));
    }

    public void testCheck5() throws ParseException
    {
        addType("Class",
                "class Class {\n"+
                "  void elementToStr(String name) {}\n"+
                "}",
                true);

        // awfull but too short
        assertTrue(
            !_testObject.check(
                _project,
                _project.getType("Class").getMethods().get(0)));
    }

    public void testHasAwfulName1() throws ParseException
    {
        addType("Class",
                "class Class {\n"+
                "  void itemToElement() {}\n"+
                "}",
                true);

        assertTrue(
            hasAwfulName(
                _project.getType("Class").getMethods().get(0)));
    }

    public void testHasAwfulName2() throws ParseException
    {
        addType("Class",
                "class Class {\n"+
                "  void toElement() {}\n"+
                "}",
                true);

        assertTrue(
            !hasAwfulName(
                _project.getType("Class").getMethods().get(0)));
    }

    public void testHasAwfulName3() throws ParseException
    {
        addType("Class",
                "class Class {\n"+
                "  void itemFrom() {}\n"+
                "}",
                true);

        assertTrue(
            !hasAwfulName(
                _project.getType("Class").getMethods().get(0)));
    }

    public void testHasAwfulName4() throws ParseException
    {
        addType("Class",
                "class Class {\n"+
                "  void elementFromObject() {}\n"+
                "}",
                true);

        assertTrue(
            hasAwfulName(
                _project.getType("Class").getMethods().get(0)));
    }

    public void testHasAwfulName5() throws ParseException
    {
        addType("Class",
                "class Class {\n"+
                "  void copyWithObject() {}\n"+
                "}",
                true);

        assertTrue(
            hasAwfulName(
                _project.getType("Class").getMethods().get(0)));
    }

    public void testHasLongName1() throws ParseException
    {
        addType("Class",
                "class Class {\n"+
                "  void thisIsTooooLong() {}\n"+
                "}",
                true);

        assertTrue(
            hasLongName(
                _project.getType("Class").getMethods().get(0)));
    }

    public void testHasLongName2() throws ParseException
    {
        addType("Class",
                "class Class {\n"+
                "  void thisIsNotLong() {}\n"+
                "}",
                true);

        assertTrue(
            !hasLongName(
                _project.getType("Class").getMethods().get(0)));
    }

    public void testHasLongName3() throws ParseException
    {
        addType("Class",
                "class Class {\n"+
                "  void shorter() {}\n"+
                "}",
                true);

        assertTrue(
            !hasLongName(
                _project.getType("Class").getMethods().get(0)));
    }
}
