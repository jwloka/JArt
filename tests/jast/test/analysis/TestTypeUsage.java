package jast.test.analysis;
import jast.Global;
import jast.ParseException;
import jast.SyntaxException;
import jast.analysis.TypeUsage;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.FormalParameterList;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.TypeDeclaration;
import junitx.framework.PrivateTestCase;
import junitx.framework.TestAccessException;


public class TestTypeUsage extends PrivateTestCase
{
    private TypeUsage _testObject;

    public TestTypeUsage(String name)
    {
        super(name);
    }

    protected void setUp() throws ParseException
    {
        _testObject =
            new TypeUsage(
                Global.getFactory().createClassDeclaration(
                    Global.getFactory().createModifiers(),
                    "Class1",
                    false));
    }

    protected void tearDown() throws ParseException
    {
        _testObject = null;
    }

    public void testAddUsage_Constructor1()
    {
        ConstructorDeclaration constructor =
            Global.getFactory().createConstructorDeclaration(
                Global.getFactory().createModifiers(),
                "MyClass",
                null);

        _testObject.addUsage(constructor);

        assertEquals(1, _testObject.getUsedConstructors().getCount());
        assertEquals(1, _testObject.getUsedFeatureCount());
        assertTrue(_testObject.isUsed(constructor));
    }

    public void testAddUsage_Constructor2()
    {
        ConstructorDeclaration constructor =
            Global.getFactory().createConstructorDeclaration(
                Global.getFactory().createModifiers(),
                "MyClass",
                null);

        _testObject.addUsage(constructor);
        _testObject.addUsage(constructor);

        assertEquals(1, _testObject.getUsedConstructors().getCount());
        assertEquals(1, _testObject.getUsedFeatureCount());
        assertTrue(_testObject.isUsed(constructor));
    }

    public void testAddUsage_Constructor3() throws SyntaxException
    {
        ConstructorDeclaration constructor1 =
            Global.getFactory().createConstructorDeclaration(
                Global.getFactory().createModifiers(),
                "MyClass",
                null);

        FormalParameterList params = Global.getFactory().createFormalParameterList();
        params.getParameters().add(
            Global.getFactory().createFormalParameter(
                false,
                Global.getFactory().createType("int", true),
                "param"));
        ConstructorDeclaration constructor2 =
            Global.getFactory().createConstructorDeclaration(
                Global.getFactory().createModifiers(),
                "MyClass",
                params);

        _testObject.addUsage(constructor1);
        _testObject.addUsage(constructor2);

        assertEquals(2, _testObject.getUsedConstructors().getCount());
        assertEquals(2, _testObject.getUsedFeatureCount());
        assertTrue(_testObject.isUsed(constructor1));
        assertTrue(_testObject.isUsed(constructor2));
    }

    public void testAddUsage_Field1() throws SyntaxException
    {
        FieldDeclaration field =
            Global.getFactory().createFieldDeclaration(
                Global.getFactory().createModifiers(),
                Global.getFactory().createType("MyField", false),
                "field",
                null);

        _testObject.addUsage(field);

        assertEquals(1, _testObject.getUsedFields().getCount());
        assertEquals(1, _testObject.getUsedFeatureCount());
        assertTrue(_testObject.isUsed(field));
    }

    public void testAddUsage_Field2() throws SyntaxException
    {
        FieldDeclaration field =
            Global.getFactory().createFieldDeclaration(
                Global.getFactory().createModifiers(),
                Global.getFactory().createType("MyField", false),
                "field",
                null);

        _testObject.addUsage(field);
        _testObject.addUsage(field);

        assertEquals(1, _testObject.getUsedFields().getCount());
        assertEquals(1, _testObject.getUsedFeatureCount());
        assertTrue(_testObject.isUsed(field));
    }

    public void testAddUsage_Field3() throws SyntaxException
    {
        FieldDeclaration field1 =
            Global.getFactory().createFieldDeclaration(
                Global.getFactory().createModifiers(),
                Global.getFactory().createType("MyField", false),
                "field1",
                null);
        FieldDeclaration field2 =
            Global.getFactory().createFieldDeclaration(
                Global.getFactory().createModifiers(),
                Global.getFactory().createType("MyField", false),
                "field2",
                null);

        _testObject.addUsage(field1);
        _testObject.addUsage(field2);

        assertEquals(2, _testObject.getUsedFields().getCount());
        assertEquals(2, _testObject.getUsedFeatureCount());
        assertTrue(_testObject.isUsed(field1));
        assertTrue(_testObject.isUsed(field2));
    }

    public void testAddUsage_Method1()
    {
        MethodDeclaration method =
            Global.getFactory().createMethodDeclaration(
                Global.getFactory().createModifiers(),
                null,
                "method",
                null);

        _testObject.addUsage(method);

        assertEquals(1, _testObject.getUsedMethods().getCount());
        assertEquals(1, _testObject.getUsedFeatureCount());
        assertTrue(_testObject.isUsed(method));
    }

    public void testAddUsage_Method2()
    {
        MethodDeclaration method =
            Global.getFactory().createMethodDeclaration(
                Global.getFactory().createModifiers(),
                null,
                "method",
                null);

        _testObject.addUsage(method);
        _testObject.addUsage(method);

        assertEquals(1, _testObject.getUsedMethods().getCount());
        assertEquals(1, _testObject.getUsedFeatureCount());
        assertTrue(_testObject.isUsed(method));
    }

    public void testAddUsage_Method3()
    {
        MethodDeclaration method1 =
            Global.getFactory().createMethodDeclaration(
                Global.getFactory().createModifiers(),
                null,
                "method1",
                null);
        MethodDeclaration method2 =
            Global.getFactory().createMethodDeclaration(
                Global.getFactory().createModifiers(),
                null,
                "method2",
                null);

        _testObject.addUsage(method1);
        _testObject.addUsage(method2);

        assertEquals(2, _testObject.getUsedMethods().getCount());
        assertEquals(2, _testObject.getUsedFeatureCount());
        assertTrue(_testObject.isUsed(method1));
        assertTrue(_testObject.isUsed(method2));
    }

    public void testInitialize() throws TestAccessException
    {
        TypeUsage testObject =
            new TypeUsage(
                Global.getFactory().createClassDeclaration(
                    Global.getFactory().createModifiers(),
                    "Class1",
                    false));

        assertNotNull(testObject);
        assertEquals(
            "Class1",
            ((TypeDeclaration) get(testObject, "_type")).getQualifiedName());

        assertEquals(0, testObject.getUsedFeatureCount());
    }

    public void testIsMethodUsed1()
    {
        MethodDeclaration method =
            Global.getFactory().createMethodDeclaration(
                Global.getFactory().createModifiers(),
                null,
                "addMethod",
                null);

        _testObject.addUsage(method);

        assertTrue(_testObject.usesMethod("add"));
        assertTrue(_testObject.usesMethod("Add"));

        assertTrue(!_testObject.usesMethod("_add"));
    }
}
