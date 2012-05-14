package jart.test.smelling;
import jart.smelling.HasSharedCollection;
import jart.test.TestBase;
import jast.ParseException;
import jast.ast.nodes.TypeDeclaration;
import junitx.framework.TestAccessException;



public class TestHasSharedCollection extends TestBase
{
    private HasSharedCollection _testObject;

    public TestHasSharedCollection(String name)
    {
        super(name);
    }

    private boolean hasPublicFieldWithCollection(TypeDeclaration type)
    {
        try
        {
            Object[] args = { _project, type };

            return ((Boolean)invokeWithKey(
                       _testObject,
                       "hasPublicFieldWithCollection_jast.ast.Project_jast.ast.nodes.TypeDeclaration",
                       args)).booleanValue();
        }
        catch (TestAccessException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private boolean hasPublicMethodSignatureWithCollection(TypeDeclaration type)
    {
        try
        {
            Object[] args = { _project, type };

            return ((Boolean)invokeWithKey(
                       _testObject,
                       "hasPublicMethodSignatureWithCollection_jast.ast.Project_jast.ast.nodes.TypeDeclaration",
                       args)).booleanValue();
        }
        catch (TestAccessException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(TestHasSharedCollection.class);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();

        _testObject = new HasSharedCollection();
    }

    protected void tearDown() throws ParseException
    {
        super.tearDown();

        _testObject = null;
    }

    public void testCheckField1() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n" + "  private java.util.Collection field;\n" + "}\n",
            true);

        addType(
            "java.util.Collection",
            "package java.util;\n" + "public interface Collection {}\n",
            true);

        resolve();

        assertTrue(!_testObject.check(_project, _project.getType("Class1")));
    }

    public void testCheckField2() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n" + "  java.util.Collection field;\n" + "}\n",
            true);

        addType(
            "java.util.Collection",
            "package java.util;\n" + "public interface Collection {}\n",
            true);

        resolve();

        assertTrue(_testObject.check(_project, _project.getType("Class1")));
    }

    public void testCheckField3() throws ParseException
    {
        addType(
            "Class1",
            "import java.util.*;\n"
                + "class Class1 {\n"
                + "  private Collection field;\n"
                + "  private Collection getField();\n"
                + "  private void setField(Collection field);\n"
                + "}\n",
            true);

        addType(
            "java.util.Collection",
            "package java.util;\n" + "public interface Collection {}\n",
            true);

        resolve();

        assertTrue(!_testObject.check(_project, _project.getType("Class1")));
    }

    public void testCheckField4() throws ParseException
    {
        addType(
            "Class1",
            "import java.util.*;\n"
                + "class Class1 {\n"
                + "  private Collection field;\n"
                + "  Collection getField();\n"
                + "  void setField(Collection field);\n"
                + "}\n",
            true);

        addType(
            "java.util.Collection",
            "package java.util;\n" + "public interface Collection {}\n",
            true);

        resolve();

        assertTrue(!_testObject.check(_project, _project.getType("Class1")));
    }

    public void testCheckField5() throws ParseException
    {
        addType(
            "Class1",
            "import java.util.*;\n"
                + "class Class1 {\n"
                + "  Collection field;\n"
                + "  private Collection getField();\n"
                + "  private void setField(Collection field);\n"
                + "}\n",
            true);

        addType(
            "java.util.Collection",
            "package java.util;\n" + "public interface Collection {}\n",
            true);

        resolve();

        assertTrue(_testObject.check(_project, _project.getType("Class1")));
    }

    public void testCheckMehtod1() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n" + "  void method(int param);\n" + "}\n",
            true);

        addType(
            "java.util.Collection",
            "package java.util;\n" + "public interface Collection {}\n",
            true);

        resolve();

        assertTrue(!_testObject.check(_project, _project.getType("Class1")));
    }

    public void testCheckMethod2() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n" + "  void method(java.util.Collection param);\n" + "}\n",
            true);

        addType(
            "java.util.Collection",
            "package java.util;\n" + "public interface Collection {}\n",
            true);

        resolve();

        assertTrue(!_testObject.check(_project, _project.getType("Class1")));
    }

    public void testCheckMethod3() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  public void method(java.util.Collection param);\n"
                + "}\n",
            true);

        addType(
            "java.util.Collection",
            "package java.util;\n" + "public interface Collection {}\n",
            true);

        resolve();

        assertTrue(_testObject.check(_project, _project.getType("Class1")));
    }

    public void testCheckMethod4() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n" + "  java.util.Collection method();\n" + "}\n",
            true);

        addType(
            "java.util.Collection",
            "package java.util;\n" + "public interface Collection {}\n",
            true);

        resolve();

        assertTrue(!_testObject.check(_project, _project.getType("Class1")));
    }

    public void testCheckMethod5() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n" + "  public java.util.Collection method();\n" + "}\n",
            true);

        addType(
            "java.util.Collection",
            "package java.util;\n" + "public interface Collection {}\n",
            true);

        resolve();

        assertTrue(_testObject.check(_project, _project.getType("Class1")));
    }

    public void testGetCollectionFields1()
        throws ParseException, TestAccessException
    {
        addType("Class1", "class Class1 {\n" + "  int field;\n" + "}\n", true);

        resolve();

        assertTrue(!hasPublicFieldWithCollection(_project.getType("Class1")));
    }

    public void testGetCollectionFields2()
        throws ParseException, TestAccessException
    {
        addType(
            "Class1",
            "class Class1 {\n" + "  java.util.Collection field;\n" + "}\n",
            true);
        addType(
            "java.util.Collection",
            "package java.util;\n" + "public interface Collection {}\n",
            true);

        resolve();

        assertTrue(hasPublicFieldWithCollection(_project.getType("Class1")));
    }

    public void testGetMethodsReturningCollection1()
        throws ParseException, TestAccessException
    {
        addType(
            "Class1",
            "class Class1 {\n" + "  int method() {\n" + "    return 0;\n" + "  }\n" + "}\n",
            true);

        resolve();

        assertTrue(!hasPublicMethodSignatureWithCollection(_project.getType("Class1")));
    }

    public void testGetMethodsReturningCollection2()
        throws ParseException, TestAccessException
    {
        addType(
            "Class1",
            "class Class1 {\n" + "  java.util.Collection method();\n" + "}\n",
            true);
        addType(
            "java.util.Collection",
            "package java.util;\n" + "public interface Collection {}\n",
            true);

        resolve();

        assertTrue(hasPublicMethodSignatureWithCollection(_project.getType("Class1")));
    }

    public void testGetMethodsWithCollectionParameter1()
        throws ParseException, TestAccessException
    {
        addType(
            "Class1",
            "class Class1 {\n" + "  void method(int param) ;\n" + "  }\n" + "}\n",
            true);

        resolve();

        assertTrue(!hasPublicMethodSignatureWithCollection(_project.getType("Class1")));
    }

    public void testGetMethodsWithCollectionParameter2()
        throws ParseException, TestAccessException
    {
        addType(
            "Class1",
            "class Class1 {\n" + "  void method(java.util.Collection param);\n" + "}\n",
            true);
        addType(
            "java.util.Collection",
            "package java.util;\n" + "public interface Collection {}\n",
            true);

        resolve();

        assertTrue(hasPublicMethodSignatureWithCollection(_project.getType("Class1")));
    }
}
