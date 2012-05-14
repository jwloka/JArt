package jast.test.analysis;
import jast.ParseException;
import jast.analysis.MethodAnalyzer;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.MethodDeclaration;



public class TestMethodAnalyzer extends TestBase
{

    private MethodAnalyzer _testObject;

    public TestMethodAnalyzer(String name)
    {
        super(name);
    }

    public void setUp() throws ParseException
    {
        super.setUp();

        _testObject = new MethodAnalyzer();
    }

    public void tearDown() throws ParseException
    {
        super.tearDown();

        _testObject = null;
    }

    public void testGetFieldOfAccessor1() throws ParseException
    {
        addType("Class1", "class Class1 {\n" + "  void method() {}\n" + "}\n", true);

        ClassDeclaration clsDecl = (ClassDeclaration) _project.getType("Class1");
        MethodDeclaration method = clsDecl.getMethods().get(0);

        FieldDeclaration result = MethodAnalyzer.getFieldOfAccessor(method);

        assertNull(result);
    }

    public void testGetFieldOfAccessor2() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  int field;\n"
                + "  int getMethod() {\n"
                + "    return field;\n"
                + "  }\n"
                + "}\n",
            true);

        ClassDeclaration clsDecl = (ClassDeclaration) _project.getType("Class1");
        MethodDeclaration method = clsDecl.getMethods().get(0);

        FieldDeclaration result = MethodAnalyzer.getFieldOfAccessor(method);

        assertNotNull(result);
        assertEquals("field", result.getName());
    }

    public void testGetFieldOfAccessor3() throws ParseException
    {
        addType(
            "Class1",
            "class Class1 {\n"
                + "  int field;\n"
                + "  void setMethod(int var) {\n"
                + "    field = var;\n"
                + "  }\n"
                + "}\n",
            true);

        ClassDeclaration clsDecl = (ClassDeclaration) _project.getType("Class1");
        MethodDeclaration method = clsDecl.getMethods().get(0);

        FieldDeclaration result = MethodAnalyzer.getFieldOfAccessor(method);

        assertNotNull(result);
        assertEquals("field", result.getName());
    }

    public void testIsFormalParameter1() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n" + "void method() {}\n" + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        _testObject.setMethod(cls.getMethods().get(0));

        Object[] args = { "param" };
        Boolean result = (Boolean) invoke(_testObject, "isFormalParameter", args);

        assertTrue(!result.booleanValue());
    }

    public void testIsFormalParameter2() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n" + "void method(Object para) {}\n" + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        _testObject.setMethod(cls.getMethods().get(0));

        Object[] args = { "param" };
        Boolean result = (Boolean) invoke(_testObject, "isFormalParameter", args);

        assertTrue(!result.booleanValue());
    }

    public void testIsFormalParameter3() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n" + "void method(int idx, Object param) {}\n" + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        _testObject.setMethod(cls.getMethods().get(0));

        Object[] args = { "param" };
        Boolean result = (Boolean) invoke(_testObject, "isFormalParameter", args);

        assertTrue(result.booleanValue());
    }

    public void testIsFormalParameterUsed1() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "void method() {\n"
                + "tester.start();\n"
                + "}\n"
                + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        _testObject.setMethod(cls.getMethods().get(0));

        Object[] args = { "tester" };
        Boolean result = (Boolean) invoke(_testObject, "isFormalParameterUsed", args);

        assertTrue(!result.booleanValue());
    }

    public void testIsFormalParameterUsed2() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "void method(Object tester) {\n"
                + "tester.start();\n"
                + "}\n"
                + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        _testObject.setMethod(cls.getMethods().get(0));

        Object[] args = { "tester" };
        Boolean result = (Boolean) invoke(_testObject, "isFormalParameterUsed", args);

        assertTrue(result.booleanValue());
    }

    public void testIsFormalParameterUsed3() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "Object tester;\n"
                + "void method(Object tester) {\n"
                + "tester.start();\n"
                + "}\n"
                + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        _testObject.setMethod(cls.getMethods().get(0));

        Object[] args = { "tester" };
        Boolean result = (Boolean) invoke(_testObject, "isFormalParameterUsed", args);

        assertTrue(result.booleanValue());
    }

    public void testIsFormalParameterUsed4() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "Object tester;\n"
                + "void method(Object tester) {\n"
                + "this.tester.start();\n"
                + "}\n"
                + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        _testObject.setMethod(cls.getMethods().get(0));

        Object[] args = { "tester" };
        Boolean result = (Boolean) invoke(_testObject, "isFormalParameterUsed", args);

        assertTrue(!result.booleanValue());
    }

    public void testIsFormalParameterUsed5() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "void method(Object tester) {\n"
                + "start(tester);\n"
                + "}\n"
                + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        _testObject.setMethod(cls.getMethods().get(0));

        Object[] args = { "tester" };
        Boolean result = (Boolean) invoke(_testObject, "isFormalParameterUsed", args);

        assertTrue(result.booleanValue());
    }

    public void testIsGetMethod1() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "String str;\n"
                + "public String method() {\n"
                + "return str;\n"
                + "}\n"
                + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        assertTrue(MethodAnalyzer.isGetMethod(cls.getMethods().get(0)));
    }

    public void testIsGetMethod2() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "String str;\n"
                + "public String method() {\n"
                + "return str.toString();\n"
                + "}\n"
                + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        assertTrue(!MethodAnalyzer.isGetMethod(cls.getMethods().get(0)));
    }

    public void testIsGetMethod3() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "String str;\n"
                + "String method() {\n"
                + "return str;\n"
                + "}\n"
                + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        // not public
        assertTrue(!MethodAnalyzer.isGetMethod(cls.getMethods().get(0)));
    }

    public void testIsGetMethod4() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "String str;\n"
                + "public String method() {}\n"
                + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        // no block statements
        assertTrue(!MethodAnalyzer.isGetMethod(cls.getMethods().get(0)));
    }

    public void testIsGetMethod5() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "String str;\n"
                + "public abstract String method();\n"
                + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        // abstract method
        assertTrue(!MethodAnalyzer.isGetMethod(cls.getMethods().get(0)));
    }

    public void testIsGetMethod6() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "String str;\n"
                + "public void method() {\n"
                + "return str;\n"
                + "}\n"
                + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        // no return value
        assertTrue(!MethodAnalyzer.isGetMethod(cls.getMethods().get(0)));
    }

    public void testIsSetMethod1() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "String str;\n"
                + "public void method(String str) {\n"
                + "this.str = str;\n"
                + "}\n"
                + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        assertTrue(MethodAnalyzer.isSetMethod(cls.getMethods().get(0)));
    }

    public void testIsSetMethod10() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "String str;\n"
                + "public void method(String name) {}\n"
                + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        // no blockstatements
        assertTrue(!MethodAnalyzer.isSetMethod(cls.getMethods().get(0)));
    }

    public void testIsSetMethod11() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "String str;\n"
                + "public void method(String name);\n"
                + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        // no method body
        assertTrue(!MethodAnalyzer.isSetMethod(cls.getMethods().get(0)));
    }

    public void testIsSetMethod2() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "String str;\n"
                + "public void method(String name) {\n"
                + "str = name;\n"
                + "}\n"
                + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        assertTrue(MethodAnalyzer.isSetMethod(cls.getMethods().get(0)));
    }

    public void testIsSetMethod3() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "String str;\n"
                + "public void method(Object name) {\n"
                + "str = (String)name;\n"
                + "}\n"
                + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        assertTrue(MethodAnalyzer.isSetMethod(cls.getMethods().get(0)));
    }

    public void testIsSetMethod4() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "String str;\n"
                + "public void method(String name) {\n"
                + "str = name + lastname;\n"
                + "}\n"
                + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        assertTrue(MethodAnalyzer.isSetMethod(cls.getMethods().get(0)));
    }

    public void testIsSetMethod5() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "String str;\n"
                + "public void method(Object name) {\n"
                + "str = (String)name + (String)lastname;\n"
                + "}\n"
                + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        assertTrue(MethodAnalyzer.isSetMethod(cls.getMethods().get(0)));
    }

    public void testIsSetMethod6() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "String str;\n"
                + "public void method(String name) {\n"
                + "str = getLastname(name);\n"
                + "}\n"
                + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        assertTrue(MethodAnalyzer.isSetMethod(cls.getMethods().get(0)));
    }

    public void testIsSetMethod7() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "String str;\n"
                + "public void method(String name) {\n"
                + "str = getLastname(name) + getAge(name);\n"
                + "}\n"
                + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        assertTrue(MethodAnalyzer.isSetMethod(cls.getMethods().get(0)));
    }

    public void testIsSetMethod8() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "String str;\n"
                + "public void method(String name) {\n"
                + "String lastname = getLastname(name);\n"
                + "str = lastname + getAge(name);\n"
                + "}\n"
                + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        // no local variable declaration allowed
        assertTrue(!MethodAnalyzer.isSetMethod(cls.getMethods().get(0)));
    }

    public void testIsSetMethod9() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "String str;\n"
                + "void method(String name) {\n"
                + "str = name;\n"
                + "}\n"
                + "}\n",
            true);

        CompilationUnit cu = _project.getCompilationUnits().get(1);
        ClassDeclaration cls = (ClassDeclaration) cu.getTypeDeclarations().get(0);

        // not public
        assertTrue(!MethodAnalyzer.isSetMethod(cls.getMethods().get(0)));
    }
}
