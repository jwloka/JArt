package jart.test.analysis;
import jart.analysis.IsUtilityClass;
import jart.analysis.IsUtilityMethod;
import jart.test.TestBase;
import jast.Global;
import jast.SyntaxException;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.FormalParameterList;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.TypeDeclaration;

public class TestIsUtilityClass extends TestBase
{
    private IsUtilityClass _testObject;

    public TestIsUtilityClass(String name)
    {
        super(name);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(TestIsUtilityClass.class);
    }

    private MethodDeclaration createNonUtilityMethod(String name)
        throws SyntaxException
    {
        TypeDeclaration owningType =
            Global.getFactory().createClassDeclaration(
                Global.getFactory().createModifiers(),
                "Class1",
                false);

        MethodDeclaration result =
            Global.getFactory().createMethodDeclaration(
                Global.getFactory().createModifiers(),
                null,
                name,
                null);

        owningType.addDeclaration(result);

        if (new IsUtilityMethod().check(null, result))
            {
            throw new RuntimeException("This should not be a UtilityMethod");
        }

        return result;
    }

    private MethodDeclaration createUtilityMethod(String name)
        throws SyntaxException
    {
        TypeDeclaration owningType =
            Global.getFactory().createClassDeclaration(
                Global.getFactory().createModifiers(),
                "Class1",
                false);

        Modifiers mods = Global.getFactory().createModifiers();
        mods.setPublic();
        mods.setStatic();

        FormalParameterList params = Global.getFactory().createFormalParameterList();
        params.getParameters().add(
            Global.getFactory().createFormalParameter(
                false,
                Global.getFactory().createType("int", true),
                "param1"));
        params.getParameters().add(
            Global.getFactory().createFormalParameter(
                false,
                Global.getFactory().createType("int", true),
                "param2"));
        params.getParameters().add(
            Global.getFactory().createFormalParameter(
                false,
                Global.getFactory().createType("int", true),
                "param3"));
        params.getParameters().add(
            Global.getFactory().createFormalParameter(
                false,
                Global.getFactory().createType("int", true),
                "param4"));

        MethodDeclaration result =
            Global.getFactory().createMethodDeclaration(mods, null, name, params);
        result.setBody(Global.getFactory().createBlock());

        owningType.addDeclaration(result);

        if (!(new IsUtilityMethod().check(null, result)))
            {
            throw new RuntimeException("This is not a UtilityMethod");
        }

        return result;
    }

    protected void setUp()
    {
        _testObject = new IsUtilityClass();
    }

    protected void tearDown()
    {
        _testObject = null;
    }

    public void testCheck1() throws SyntaxException
    {
        ClassDeclaration testClass =
            Global.getFactory().createClassDeclaration(
                Global.getFactory().createModifiers(),
                "MyClass",
                false);

        testClass.addDeclaration(createUtilityMethod("utilMethod1"));
        testClass.addDeclaration(createUtilityMethod("utilMethod2"));
        testClass.addDeclaration(createUtilityMethod("utilMethod3"));
        testClass.addDeclaration(createUtilityMethod("utilMethod4"));
        testClass.addDeclaration(createNonUtilityMethod("otherMethod1"));

        assertTrue(_testObject.check(null, testClass));
    }

    public void testCheck2() throws SyntaxException
    {
        ClassDeclaration testClass =
            Global.getFactory().createClassDeclaration(
                Global.getFactory().createModifiers(),
                "MyClass",
                false);

        testClass.addDeclaration(createUtilityMethod("utilMethod1"));
        testClass.addDeclaration(createUtilityMethod("utilMethod2"));
        testClass.addDeclaration(createUtilityMethod("utilMethod3"));
        testClass.addDeclaration(createUtilityMethod("utilMethod4"));
        testClass.addDeclaration(createNonUtilityMethod("otherMethod1"));
        testClass.addDeclaration(createNonUtilityMethod("otherMethod2"));

        assertTrue(!_testObject.check(null, testClass));
    }

    public void testCheck3() throws SyntaxException
    {
        ClassDeclaration testClass =
            Global.getFactory().createClassDeclaration(
                Global.getFactory().createModifiers(),
                "MyClass",
                false);

        testClass.addDeclaration(createUtilityMethod("utilMethod1"));
        testClass.addDeclaration(createUtilityMethod("utilMethod2"));
        testClass.addDeclaration(createUtilityMethod("utilMethod3"));
        testClass.addDeclaration(createUtilityMethod("utilMethod4"));
        testClass.addDeclaration(createUtilityMethod("utilMethod5"));
        testClass.addDeclaration(createUtilityMethod("utilMethod6"));
        testClass.addDeclaration(createNonUtilityMethod("otherMethod1"));
        testClass.addDeclaration(createNonUtilityMethod("otherMethod2"));

        assertTrue(_testObject.check(null, testClass));
    }
}
