package jart.test.analysis;
import jart.analysis.IsCollectionClass;
import jart.test.TestBase;
import jast.ParseException;
import jast.helpers.StringArray;

public class TestIsCollectionClass extends TestBase
{
    private IsCollectionClass _testObject;

    public TestIsCollectionClass(String name)
    {
        super(name);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(TestIsCollectionClass.class);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();

        _testObject = new IsCollectionClass();
    }

    protected void tearDown() throws ParseException
    {
        super.tearDown();

        _testObject = null;
    }

    public void testContainsCollection1() throws ParseException
    {
        addType(
            "Class1",
            "import java.util.*;\n"
                + "public class Class1 {\n"
                + "  Collection data;\n"
                + "}\n",
            true);

        addType(
            "java.util.Collection",
            "package java.util;\n" + "public interface Collection {}\n",
            true);
        resolve();

        assertTrue(_testObject.containsCollection(_project.getType("Class1")));
    }

    public void testContainsCollection2() throws ParseException
    {
        addType(
            "Class1",
            "import java.util.*;\n"
                + "public class Class1 {\n"
                + "  Vector data;\n"
                + "}\n",
            true);

        addType(
            "java.util.Collection",
            "package java.util;\n" + "public interface Collection {}\n",
            true);

        addType(
            "java.util.Vector",
            "package java.util;\n" + "public class Vector implements Collection {}\n",
            true);
        resolve();

        assertTrue(_testObject.containsCollection(_project.getType("Class1")));
    }

    public void testContainsCollection3() throws ParseException
    {
        addType(
            "Class1",
            "import java.util.*;\n"
                + "public class Class1 {\n"
                + "  MyCollection data;\n"
                + "}\n",
            true);

        addType(
            "java.util.Collection",
            "package java.util;\n" + "public interface Collection {}\n",
            true);

        addType(
            "java.util.Vector",
            "package java.util;\n" + "public class Vector implements Collection {}\n",
            true);

        addType(
            "MyCollection",
            "import java.util.*;\n" + "public class MyCollection extends Vector {}\n",
            true);
        resolve();

        assertTrue(_testObject.containsCollection(_project.getType("Class1")));
    }

    public void testGetMethodNamesStartWith1() throws ParseException
    {
        addType("Class1", "public class Class1 {\n" + "}\n", true);

        StringArray result = _testObject.getMethodNamesStartWith(
                                 _project.getType("Class1"),
                                 "test");

        assertEquals(0,
                     result.getCount());
    }

    public void testGetMethodNamesStartWith2() throws ParseException
    {
        addType(
            "Class1",
            "public class Class1 {\n" + " public void testMethod() {}\n" + "}\n",
            true);

        StringArray result = _testObject.getMethodNamesStartWith(
                                 _project.getType("Class1"),
                                 "test");
        assertEquals(1,
                     result.getCount());
    }

    public void testGetMethodNamesStartWith3() throws ParseException
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + " public void testMethod1() {}\n"
                + " public void testMethod2() {}\n"
                + " public void TestMethod() {}\n"
                + " public void testerMethod() {}\n"
                + " public void otherMethod() {}\n"
                + " public void otherNonTestMethod() {}\n"
                + "}\n",
            true);

        StringArray result = _testObject.getMethodNamesStartWith(
                                 _project.getType("Class1"),
                                 "test");

        assertEquals(4,
                     result.getCount());
    }

    public void testImplementsAddPattern1() throws ParseException
    {
        addType(
            "Class1",
            "public class Class1 {\n" + " public void removeSomething() {}\n" + "}\n",
            true);

        assertTrue(_testObject.implementsRemovePattern(_project.getType("Class1")));
    }

    public void testImplementsAddPattern2() throws ParseException
    {
        addType(
            "Class1",
            "public class Class1 {\n" + " public void deleteSomething() {}\n" + "}\n",
            true);

        assertTrue(_testObject.implementsRemovePattern(_project.getType("Class1")));
    }

    public void testImplementsAddPattern3() throws ParseException
    {
        addType(
            "Class1",
            "public class Class1 {\n" + " public void DeleteSomething() {}\n" + "}\n",
            true);

        assertTrue(_testObject.implementsRemovePattern(_project.getType("Class1")));
    }

    public void testImplementsAddPattern4() throws ParseException
    {
        addType(
            "Class1",
            "public class Class1 {\n" + " public void InsertSomething() {}\n" + "}\n",
            true);

        assertTrue(_testObject.implementsAddPattern(_project.getType("Class1")));
    }

    public void testImplementsAddPattern5() throws ParseException
    {
        addType(
            "Class1",
            "public class Class1 {\n" + " public void getFieldAdder() {}\n" + "}\n",
            true);

        assertTrue(!_testObject.implementsAddPattern(_project.getType("Class1")));
    }

    public void testImplementsStackPattern1() throws ParseException
    {
        addType(
            "Class1",
            "public class Class1 {\n" + " public void popSomething() {}\n" + "}\n",
            true);

        assertTrue(_testObject.implementsStackPattern(_project.getType("Class1")));
    }

    public void testImplementsStackPattern2() throws ParseException
    {
        addType(
            "Class1",
            "public class Class1 {\n" + " public void top() {}\n" + "}\n",
            true);

        assertTrue(_testObject.implementsStackPattern(_project.getType("Class1")));
    }

    public void testImplementsStackPattern3() throws ParseException
    {
        addType(
            "Class1",
            "public class Class1 {\n" + " public void pushSomething() {}\n" + "}\n",
            true);

        assertTrue(_testObject.implementsStackPattern(_project.getType("Class1")));
    }

    public void testInheritsFromCollection1() throws ParseException
    {
        addType(
            "Class1",
            "import java.util.*;\n" + "public class Class1 implements Collection {}\n",
            true);

        addType(
            "java.util.Collection",
            "package java.util;\n" + "public interface Collection {}\n",
            true);
        resolve();

        assertTrue(_testObject.inheritsFromCollection(_project.getType("Class1")));
    }

    public void testInheritsFromCollection2() throws ParseException
    {
        addType(
            "Class1",
            "import java.util.*;\n" + "public class Class1 extends Vector {}\n",
            true);

        addType(
            "java.util.Collection",
            "package java.util;\n" + "public interface Collection {}\n",
            true);

        addType(
            "java.util.Vector",
            "package java.util;\n" + "public class Vector implements Collection {}\n",
            true);

        resolve();

        assertTrue(_testObject.inheritsFromCollection(_project.getType("Class1")));
    }

    public void testInheritsFromCollection3() throws ParseException
    {
        addType(
            "Class1",
            "import java.util.*;\n" + "public class Class1 extends MyCollection {}\n",
            true);

        addType(
            "java.util.Collection",
            "package java.util;\n" + "public interface Collection {}\n",
            true);

        addType(
            "java.util.Vector",
            "package java.util;\n" + "public class Vector implements Collection {}\n",
            true);

        addType(
            "MyCollection",
            "import java.util.*;\n" + "public class MyCollection extends Vector {}\n",
            true);

        resolve();

        assertTrue(_testObject.inheritsFromCollection(_project.getType("Class1")));
    }

    public void testIsKnownCollection1() throws ParseException
    {
        addType(
            "java.util.Collection",
            "package java.util;\n" + "public class Collection {}\n",
            true);

        assertTrue(_testObject.isKnownCollection(_project.getType("java.util.Collection")));
    }
}
