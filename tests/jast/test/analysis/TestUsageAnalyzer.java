package jast.test.analysis;
import jast.ParseException;
import jast.analysis.UsageAnalyzer;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.collections.TypeDeclarationArray;


public class TestUsageAnalyzer extends TestBase
{
    private UsageAnalyzer _testObject;

    public TestUsageAnalyzer(String name)
    {
        super(name);
    }

    private void analyzeUsages()
    {
        new jast.analysis.UsageVisitor().visitProject(_project);
    }

    public void tearDown() throws ParseException
    {
        super.tearDown();

        _testObject = null;
    }

    public void testLocateUsageField1() throws ParseException
    {
        addType("Class1",
                "public class Class1 { int field1; }\n",
                true);
        addType("Class2",
                "public class Class2 { int field2 = new Class1().field1; }",
                true);
        addType("Class3",
                "public class Class3 extends Class1 {\n"+
                "  int field3 = super.field1;\n"+
                "}\n",
                true);

        resolve();
        analyzeUsages();

        _testObject = new UsageAnalyzer(_project);

        FieldDeclaration     field     = _project.getType("Class1").getFields().get("field1");
        TypeDeclarationArray locations = _testObject.getTypeDeclarationsUsing(field);

        assertTrue(!locations.contains("Class1"));
        assertTrue(locations.contains("Class2"));
        assertTrue(locations.contains("Class3"));
    }

    public void testLocateUsageField2() throws ParseException
    {
        addType("Class1",
                "public class Class1 { static int field1; }\n",
                true);
        addType("Class2",
                "public class Class2 { int field2 = Class1.field1; }",
                true);
        addType("Class3",
                "public class Class3 extends Class1 {\n"+
                "  int field3 = super.field1;\n"+
                "}\n",
                true);

        resolve();
        analyzeUsages();

        _testObject = new UsageAnalyzer(_project);

        FieldDeclaration     field     = _project.getType("Class1").getFields().get("field1");
        TypeDeclarationArray locations = _testObject.getTypeDeclarationsUsing(field);

        assertTrue(!locations.contains("Class1"));
        assertTrue(locations.contains("Class2"));
        assertTrue(locations.contains("Class3"));
    }

    public void testLocateUsageField3() throws ParseException
    {
        addType("Class1",
                "public class Class1 { int field1; }\n",
                true);
        addType("Class2",
                "public class Class2 {\n"+
                "  void method() { int var = new Class1().field1; }\n"+
                "}",
                true);
        addType("Class3",
                "public class Class3 extends Class1 {\n"+
                "  void method() { int var = super.field1; }\n"+
                "}\n",
                true);
        addType("Class4",
                "public class Class4 extends Class1 {\n"+
                "  int method() { return super.field1; }\n"+
                "}\n",
                true);
        addType("Class5",
                "public class Class5 {\n"+
                "  int method(Class1 cls) { return cls.field1; }\n"+
                "}\n",
                true);
        addType("Class6",
                "public class Class6 {\n"+
                "  Class1 method1() { return new Class1(); }\n"+
                "  int method() { return method1().field1; }\n"+
                "}\n",
                true);
        addType("Class7",
                "public class Class7 {\n"+
                "  int field8;\n"+
                "  public Class8(Class1 cls) { field8 = cls.field1; }\n"+
                "}\n",
                true);

        resolve();
        analyzeUsages();

        _testObject = new UsageAnalyzer(_project);

        FieldDeclaration     field     = _project.getType("Class1").getFields().get("field1");
        TypeDeclarationArray locations = _testObject.getTypeDeclarationsUsing(field);

        assertTrue(!locations.contains("Class1"));
        assertTrue(locations.contains("Class2"));
        assertTrue(locations.contains("Class3"));
        assertTrue(locations.contains("Class4"));
        assertTrue(locations.contains("Class5"));
        assertTrue(locations.contains("Class6"));
        assertTrue(locations.contains("Class7"));

    }

    public void testLocateUsageField4() throws ParseException
    {
        addType("Class1",
                "public class Class1 { static int field1; }\n",
                true);
        addType("Class2",
                "public class Class2 {\n"+
                "  void method() { int var = Class1.field1; }\n"+
                "}",
                true);
        addType("Class3",
                "public class Class3 extends Class1 {\n"+
                "  void method() { int var = super.field1; }\n"+
                "}\n",
                true);
        addType("Class4",
                "public class Class4 extends Class1 {\n"+
                "  int method() { return super.field1; }\n"+
                "}\n",
                true);
        addType("Class5",
                "public class Class5 {\n"+
                "  int method(Class1 cls) { return cls.field1; }\n"+
                "}\n",
                true);
        addType("Class6",
                "public class Class6 {\n"+
                "  Class1 method1() { return new Class1(); }\n"+
                "  int method() { return method1().field1; }\n"+
                "}\n",
                true);

        resolve();
        analyzeUsages();

        _testObject = new UsageAnalyzer(_project);

        FieldDeclaration     field     = _project.getType("Class1").getFields().get("field1");
        TypeDeclarationArray locations = _testObject.getTypeDeclarationsUsing(field);

        assertTrue(!locations.contains("Class1"));
        assertTrue(locations.contains("Class2"));
        assertTrue(locations.contains("Class3"));
        assertTrue(locations.contains("Class4"));
        assertTrue(locations.contains("Class5"));
        assertTrue(locations.contains("Class6"));
    }

    public void testLocateUsageField5() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  int field1;\n"+
                "  void method() { int var = field1; }\n"+
                "}\n",
                true);

        resolve();
        analyzeUsages();

        _testObject = new UsageAnalyzer(_project);

        FieldDeclaration     field     = _project.getType("Class1").getFields().get("field1");
        TypeDeclarationArray locations = _testObject.getTypeDeclarationsUsing(field);

        assertTrue(locations.contains("Class1"));
    }

    public void testLocateUsageField6() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  int field1;\n"+
                "  void method() { int var = this.field1; }\n"+
                "}\n",
                true);

        resolve();
        analyzeUsages();

        _testObject = new UsageAnalyzer(_project);

        FieldDeclaration     field     = _project.getType("Class1").getFields().get("field1");
        TypeDeclarationArray locations = _testObject.getTypeDeclarationsUsing(field);

        assertTrue(locations.contains("Class1"));
    }

    public void testLocateUsageField7() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  int field1;\n"+
                "  int method(int field1) { return this.field1; }\n"+
                "}\n",
                true);

        resolve();
        analyzeUsages();

        _testObject = new UsageAnalyzer(_project);

        FieldDeclaration     field     = _project.getType("Class1").getFields().get("field1");
        TypeDeclarationArray locations = _testObject.getTypeDeclarationsUsing(field);

        assertTrue(locations.contains("Class1"));
    }

    public void testLocateUsageField8() throws ParseException
    {
        addType("Class1",
                "public class Class1 {\n"+
                "  int field1;\n"+
                "  int method(Class1 cls) { return cls.field1; }\n"+
                "}\n",
                true);

        resolve();
        analyzeUsages();

        _testObject = new UsageAnalyzer(_project);

        FieldDeclaration     field     = _project.getType("Class1").getFields().get("field1");
        TypeDeclarationArray locations = _testObject.getTypeDeclarationsUsing(field);

        assertTrue(locations.contains("Class1"));
    }

    public void testLocateUsageMethod1() throws ParseException
    {
        addType("Class1",
                "public class Class1 { int method() { return 0; } }\n",
                true);
        addType("Class2",
                "public class Class2 { int field = new Class1().method(); }",
                true);
        addType("Class3",
                "public class Class3 extends Class1 {\n"+
                "  int field = super.method();\n"+
                "}\n",
                true);

        resolve();
        analyzeUsages();

        _testObject = new UsageAnalyzer(_project);

        MethodDeclaration    method    = _project.getType("Class1").getMethods().get(0);
        TypeDeclarationArray locations = _testObject.getTypeDeclarationsUsing(method);

        assertTrue(!locations.contains("Class1"));
        assertTrue(locations.contains("Class2"));
        assertTrue(locations.contains("Class3"));
    }

    public void testLocateUsageMethod2() throws ParseException
    {
        addType("Class1",
                "public class Class1 { static int method() { return 0; } }\n",
                true);
        addType("Class2",
                "public class Class2 { int field = Class1.method(); }",
                true);
        addType("Class3",
                "public class Class3 extends Class1 {\n"+
                "  int field = super.method();\n"+
                "}\n",
                true);

        resolve();
        analyzeUsages();

        _testObject = new UsageAnalyzer(_project);

        MethodDeclaration    method    = _project.getType("Class1").getMethods().get(0);
        TypeDeclarationArray locations = _testObject.getTypeDeclarationsUsing(method);

        assertTrue(!locations.contains("Class1"));
        assertTrue(locations.contains("Class2"));
        assertTrue(locations.contains("Class3"));
    }
}
