package jart.test.analysis;
import jart.analysis.IsTestClass;
import jart.test.TestBase;
import jast.ParseException;
import jast.ast.nodes.ClassDeclaration;
import jast.helpers.StringArray;
import junitx.framework.TestAccessException;

public class TestIsTestClass extends TestBase
{
    private IsTestClass     _testObject;

    public TestIsTestClass(String name)
    {
        super(name);
    }

    private int getLocalDefinedMethodCount(ClassDeclaration decl, String prefix)
    {
        try
        {
            Object[] args = { decl, prefix };

            return ((Integer)invokeWithKey(
                       _testObject,
                       "getLocalDefinedMethodCount_jast.ast.nodes.ClassDeclaration_java.lang.String",
                       args)).intValue();
        }
        catch (TestAccessException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(TestIsTestClass.class);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();

        _testObject = new IsTestClass();
    }

    protected void tearDown() throws ParseException
    {
        super.tearDown();

        _testObject = null;
    }

    public void testContainsMainlyTestMethods1() throws ParseException
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "void method() {}\n"
                + "void testMethod() {}\n"
                + "}\n",
            true);

        assertTrue(!_testObject.containsMainlyTestMethods(
                        (ClassDeclaration)_project.getType("Class1")));
    }

    public void testContainsMainlyTestMethods2() throws ParseException
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "void method() {}\n"
                + "void testMethod1() {}\n"
                + "void testMethod2() {}\n"
                + "void testMethod3() {}\n"
                + "}\n",
            true);

        assertTrue(_testObject.containsMainlyTestMethods(
                       (ClassDeclaration)_project.getType("Class1")));
    }

    public void testGetLocalDefinedMethodCount1() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n" + "void aTestMethod() {}\n" // not really
        +"}\n", true);

        assertEquals(0,
                     getLocalDefinedMethodCount(
                         (ClassDeclaration)_project.getType("Class1"),
                         "test"));
    }

    public void testGetLocalDefinedMethodCount2() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "void testMethod1() {}\n"
                + "void testMethod2() {}\n"
                + "void testMethod3() {}\n"
                + "}\n",
            true);

        assertEquals(3,
                     getLocalDefinedMethodCount(
                         (ClassDeclaration)_project.getType("Class1"),
                         "test"));
    }

    public void testGetLocalDefinedMethodCount3() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "void testMethod1() {}\n"
                + "void aTestMethod() {}\n"
                + "void testMethod2() {}\n"
                + "void aDifferentMethod() {}\n"
                + "void testMethod3() {}\n"
                + "}\n",
            true);

        assertEquals(3,
                     getLocalDefinedMethodCount(
                         (ClassDeclaration)_project.getType("Class1"),
                         "test"));
    }

    public void testGetMethodNamesStartWith1() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n" + "public int numOf() {}\n" + "}\n",
            true);

        StringArray result = getMethodNamesStartWith(
                                 (ClassDeclaration)_project.getType("Class1"),
                                 "num");

        assertEquals(1,
                     result.getCount());
        assertTrue(result.contains("numOf"));
    }

    public void testGetMethodNamesStartWith2() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "public int numOf() {}\n"
                + "abstract void startSearch();\n"
                + "}\n",
            true);

        StringArray result = getMethodNamesStartWith(
                                 (ClassDeclaration)_project.getType("Class1"),
                                 "start");

        assertEquals(1,
                     result.getCount());
        assertTrue(result.contains("startSearch"));
    }

    public void testGetMethodNamesStartWith3() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "int startIndex() {}\n"
                + "void startSearch() {}\n"
                + "}\n",
            true);

        StringArray result = getMethodNamesStartWith(
                                 (ClassDeclaration)_project.getType("Class1"),
                                 "start");

        assertEquals(2,
                     result.getCount());
        assertTrue(result.contains("startSearch"));
        assertTrue(result.contains("startIndex"));
    }

    public void testGetMethodNamesStartWith4() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "int startIndex() {}\n"
                + "void startSearch() {}\n"
                + "}\n",
            true);

        StringArray result = getMethodNamesStartWith(
                                 (ClassDeclaration)_project.getType("Class1"),
                                 "num");

        assertEquals(0,
                     result.getCount());
    }

    public void testGetMethodNamesStartWith5() throws Exception
    {
        addType("Class1", "public class Class1 {\n" + "}\n", true);

        StringArray result = getMethodNamesStartWith(
                                 (ClassDeclaration)_project.getType("Class1"),
                                 "num");

        assertEquals(0,
                     result.getCount());
    }

    public void testHasImportFor1() throws Exception
    {
        addType("Class1", "import test.*;\n" + "public class Class1 {}\n", true);

        assertTrue(hasImportFor(
                       (ClassDeclaration)_project.getType("Class1"),
                       "test"));
    }

    public void testHasImportFor2() throws Exception
    {
        addType("Class1", "import test.TestCase;\n" + "public class Class1 {}\n", true);

        assertTrue(hasImportFor(
                       (ClassDeclaration)_project.getType("Class1"),
                       "test.TestCase"));
    }

    public void testHasImportFor3() throws Exception
    {
        addType(
            "Class1",
            "import test.TestCase;\n"
                + "import test.tester.*;\n"
                + "public class Class1 {}\n",
            true);

        assertTrue(hasImportFor(
                       (ClassDeclaration)_project.getType("Class1"),
                       "test.tester"));
    }

    public void testImportsTestPackage1() throws ParseException
    {
        addType("Class1", "import jtest.util.*;\n" + "public class Class1 {}\n", true);

        assertTrue(_testObject.importsTestPackage(
                       (ClassDeclaration)_project.getType("Class1")));
    }

    public void testImportsTestPackage2() throws ParseException
    {
        addType(
            "Class1",
            "import junit.framework.TestCase;\n" + "public class Class1 {}\n",
            true);

        assertTrue(_testObject.importsTestPackage(
                       (ClassDeclaration)_project.getType("Class1")));
    }

    public void testImportsTestPackage3() throws ParseException
    {
        addType("Class1", "import jtest.*;\n" + "public class Class1 {}\n", true);

        assertTrue(_testObject.importsTestPackage(
                       (ClassDeclaration)_project.getType("Class1")));
    }

    public void testImportsTestPackage4() throws ParseException
    {
        addType(
            "Class1",
            "import java.util.*;\n"
                + "import jtest.*;\n"
                + "import javax.swing.*;\n"
                + "public class Class1 {}\n",
            true);

        assertTrue(_testObject.importsTestPackage(
                       (ClassDeclaration)_project.getType("Class1")));
    }

    public void testImportsTestPackage5() throws ParseException
    {
        addType("Class1", "import jtester.*;\n" + "public class Class1 {}\n", true);

        assertTrue(!_testObject.importsTestPackage(
                       (ClassDeclaration)_project.getType("Class1")));
    }

    public void testInheritsFromTestClass1() throws ParseException
    {
        addType("Class1", "public class Class1 {}\n", true);

        resolve();
        assertTrue(!_testObject.inheritsFromTestClass(
                       (ClassDeclaration)_project.getType("Class1")));
    }

    public void testInheritsFromTestClass2() throws ParseException
    {
        addType(
            "Class1",
            "public class Class1 extends junit.framework.TestCase {}\n",
            true);
        addType(
            "junit.framework.TestCase",
            "package junit.framework;\n" + "public class TestCase extends Assert {}\n",
            true);
        addType(
            "junit.framework.Assert",
            "package junit.framework;\n" + "public class Assert {}\n",
            true);

        resolve();

        assertTrue(_testObject.inheritsFromTestClass(
                       (ClassDeclaration)_project.getType("Class1")));
    }

    public void testInheritsFromTestClass3() throws ParseException
    {
        addType(
            "Class1",
            "public class Class1 extends junit.framework.TestSuite {}\n",
            true);
        addType(
            "junit.framework.TestSuite",
            "package junit.framework;\n" + "public class TestSuite {}\n",
            true);

        resolve();

        assertTrue(_testObject.inheritsFromTestClass(
                       (ClassDeclaration)_project.getType("Class1")));
    }

    public void testInheritsFromTestClass4() throws ParseException
    {
        addType(
            "Class1",
            "public class Class1 implements junit.framework.TestPackage {}\n",
            true);
        addType(
            "junit.framework.TestPackage",
            "package junit.framework;\n" + "public interface TestPackage {}\n",
            true);

        resolve();

        assertTrue(_testObject.inheritsFromTestClass(
                       (ClassDeclaration)_project.getType("Class1")));
    }

    public void testInheritsFromTestClass5() throws ParseException
    {
        addType("Class1", "public class Class1 extends Class2 {}\n", true);

        addType(
            "Class2",
            "public class Class2 extends junit.framework.TestCase {}\n",
            true);
        addType(
            "junit.framework.TestCase",
            "package junit.framework;\n" + "public class TestCase extends Assert {}\n",
            true);
        addType(
            "junit.framework.Assert",
            "package junit.framework;\n" + "public class Assert {}\n",
            true);

        resolve();

        assertTrue(_testObject.inheritsFromTestClass(
                      (ClassDeclaration)_project.getType("Class1")));
    }

    public void testUsesAssertMethod1() throws ParseException
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "void method1() {\n"
                + "  Class2.assertTrue(true);\n"
                + "}\n"
                + "}\n",
            true);
        addType(
            "Class2",
            "public class Class2 {\n" + "static void assertTrue(boolean bol) {}\n" + "}\n",
            true);

        resolve();
        addUsages();

        assertTrue(_testObject.usesAssertMethod(
                       (ClassDeclaration)_project.getType("Class1")));
    }

    public void testUsesAssertMethod2() throws ParseException
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "void method1(int var) {\n"
                + "  Class2.assertNotNull(var);\n"
                + "}\n"
                + "}\n",
            true);
        addType(
            "Class2",
            "public class Class2 {\n" + "static void assertNotNull(int var) {}\n" + "}\n",
            true);

        resolve();
        addUsages();

        assertTrue(_testObject.usesAssertMethod(
                       (ClassDeclaration)_project.getType("Class1")));
    }

    public void testUsesMethod1() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "void testMethod() {\n"
                + "  Class2.usage();\n"
                + "}\n"
                + "}\n",
            true);

        addType(
            "Class2",
            "public class Class2 {\n" + "static void usage() {\n" + "}\n" + "}\n",
            true);

        resolve();
        addUsages();

        assertTrue(usesMethod(
                       (ClassDeclaration)_project.getType("Class1"),
                       "usage"));
    }

    public void testUsesMethod2() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "void testMethod() {\n"
                + "  Class2.usageTest();\n"
                + "}\n"
                + "}\n",
            true);

        addType(
            "Class2",
            "public class Class2 {\n" + "static void usageTest() {\n" + "}\n" + "}\n",
            true);

        resolve();
        addUsages();

        assertTrue(usesMethod(
                       (ClassDeclaration)_project.getType("Class1"),
                       "usage"));
    }

    public void testUsesMethod3() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n" + "Object obj = Class2.usage1();\n" + "}\n",
            true);
        addType(
            "Class2",
            "public class Class2 {\n" + "static void usage1() {\n" + "}\n" + "}\n",
            true);
        resolve();

        addUsages();

        assertTrue(usesMethod(
                       (ClassDeclaration)_project.getType("Class1"),
                       "usage1"));
    }

    public void testUsesTestClass1() throws Exception
    {
        addType(
            "Class1",
            "import junit.framework.*;\n"
                + "public class Class1 {\n"
                + "private TestCase test = new TestCase();\n"
                + "void method() {}\n"
                + "}\n",
            true);

        addType(
            "junit.framework.TestCase",
            "package junit.framework;\n" + "public class TestCase extends Assert {}\n",
            true);

        addType(
            "junit.framework.Assert",
            "package junit.framework;\n" + "public class Assert {}\n",
            true);

        resolve();
        addUsages();

        assertTrue(_testObject.usesTestClass(
                       (ClassDeclaration)_project.getType("Class1")));
    }

    public void testUsesTestClass2() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "junit.framework.TestSuite method() {\n"
                + " return null;\n"
                + "}\n"
                + "}\n",
            true);
        addType(
            "junit.framework.TestSuite",
            "package junit.framework;\n" + "public class TestSuite {}\n",
            true);

        resolve();
        addUsages();

        assertTrue(_testObject.usesTestClass(
                       (ClassDeclaration)_project.getType("Class1")));
    }

    public void testUsesTestClass3() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "void method(junit.framework.TestPackage pkg) {}\n"
                + "}\n",
            true);
        addType(
            "junit.framework.TestPackage",
            "package junit.framework;\n" + "public class TestPackage {}\n",
            true);

        resolve();
        addUsages();

        assertTrue(_testObject.usesTestClass(
                       (ClassDeclaration)_project.getType("Class1")));
    }

    public void testUsesTestClass4() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "public Class1(junit.framework.TestCase test) {}\n"
                + "}\n",
            true);
        addType(
            "junit.framework.TestCase",
            "package junit.framework;\n" + "public class TestCase extends Assert {}\n",
            true);

        addType(
            "junit.framework.Assert",
            "package junit.framework;\n" + "public class Assert {}\n",
            true);

        resolve();
        addUsages();

        assertTrue(_testObject.usesTestClass(
                       (ClassDeclaration)_project.getType("Class1")));
    }

    public void testUsesTestClass5() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 extends junit.framework.TestCase {}\n",
            true);
        addType(
            "junit.framework.TestCase",
            "package junit.framework;\n" + "public class TestCase extends Assert {}\n",
            true);

        addType(
            "junit.framework.Assert",
            "package junit.framework;\n" + "public class Assert {}\n",
            true);

        resolve();
        addUsages();

        assertTrue(_testObject.usesTestClass(
                       (ClassDeclaration)_project.getType("Class1")));
    }

    public void testUsesTestClass6() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 implements junit.framework.TestPackage {\n"
                + "public Class1() {\n"
                + " return;\n"
                + "}\n"
                + "}\n",
            true);
        addType(
            "junit.framework.TestPackage",
            "package junit.framework;\n" + "public interface TestPackage {}\n",
            true);

        resolve();
        addUsages();

        assertTrue(_testObject.usesTestClass(
                       (ClassDeclaration)_project.getType("Class1")));
    }

    public void testUsesTestClass7() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "public Class1() {\n"
                + " Test test = new junit.framework.TestCase();\n"
                + "}\n"
                + "}\n",
            true);
        addType(
            "junit.framework.TestCase",
            "package junit.framework;\n" + "public class TestCase extends Assert {}\n",
            true);

        addType(
            "junit.framework.Assert",
            "package junit.framework;\n" + "public class Assert {}\n",
            true);

        resolve();
        addUsages();

        assertTrue(_testObject.usesTestClass(
                       (ClassDeclaration)_project.getType("Class1")));
    }


    private StringArray getMethodNamesStartWith(ClassDeclaration decl, String prefix)
    {
        try
        {
            Object[] args = { decl, prefix };

            return (StringArray)invokeWithKey(
                       _testObject,
                       "getMethodNamesStartWith_jast.ast.nodes.ClassDeclaration_java.lang.String",
                       args);
        }
        catch (TestAccessException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }



    private boolean hasImportFor(ClassDeclaration decl, String name)
    {
        try
        {
            Object[] args = { decl, name };

            return ((Boolean)invokeWithKey(
                       _testObject,
                       "hasImportFor_jast.ast.nodes.ClassDeclaration_java.lang.String",
                       args)).booleanValue();
        }
        catch (TestAccessException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }



    private boolean usesMethod(ClassDeclaration decl, String name)
    {
        try
        {
            Object[] args = { decl, name };

            return ((Boolean)invokeWithKey(
                       _testObject,
                       "usesMethod_jast.ast.nodes.ClassDeclaration_java.lang.String",
                       args)).booleanValue();
        }
        catch (TestAccessException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
