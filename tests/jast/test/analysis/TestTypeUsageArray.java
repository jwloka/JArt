package jast.test.analysis;
import jast.Global;
import jast.SyntaxException;
import jast.analysis.TypeUsage;
import jast.analysis.TypeUsageArray;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.MethodDeclaration;
import junitx.framework.PrivateTestCase;
import junitx.framework.TestAccessException;


public class TestTypeUsageArray extends PrivateTestCase
{
    private TypeUsageArray _testObject;

    public TestTypeUsageArray(String name)
    {
        super(name);
    }

    private TypeUsage createEmptyTypeUsage() throws SyntaxException
    {
        ClassDeclaration clsDecl =
            Global.getFactory().createClassDeclaration(
                Global.getFactory().createModifiers(),
                "Class1",
                false);
        return new TypeUsage(clsDecl);
    }

    private TypeUsage createRealTypeUsage1() throws SyntaxException
    {
        ClassDeclaration clsDecl =
            Global.getFactory().createClassDeclaration(
                Global.getFactory().createModifiers(),
                "Class1",
                false);
        FieldDeclaration field1 =
            Global.getFactory().createFieldDeclaration(
                Global.getFactory().createModifiers(),
                Global.getFactory().createType("int", true),
                "field1",
                null);
        ConstructorDeclaration const1 =
            Global.getFactory().createConstructorDeclaration(
                Global.getFactory().createModifiers(),
                "Class1",
                null);
        MethodDeclaration meth1 =
            Global.getFactory().createMethodDeclaration(
                Global.getFactory().createModifiers(),
                null,
                "method1",
                null);

        TypeUsage result = new TypeUsage(clsDecl);
        result.addUsage(field1);
        result.addUsage(const1);
        result.addUsage(meth1);

        return result;
    }

    private TypeUsage createRealTypeUsage2() throws SyntaxException
    {
        ClassDeclaration clsDecl =
            Global.getFactory().createClassDeclaration(
                Global.getFactory().createModifiers(),
                "Class1",
                false);
        FieldDeclaration field2 =
            Global.getFactory().createFieldDeclaration(
                Global.getFactory().createModifiers(),
                Global.getFactory().createType("int", true),
                "field2",
                null);
        MethodDeclaration meth2 =
            Global.getFactory().createMethodDeclaration(
                Global.getFactory().createModifiers(),
                null,
                "method2",
                null);

        TypeUsage result = new TypeUsage(clsDecl);
        result.addUsage(field2);
        result.addUsage(meth2);

        return result;
    }

    protected void setUp()
    {
        _testObject = new TypeUsageArray();
    }

    protected void tearDown()
    {
        _testObject = null;
    }

    public void testMerge1() throws SyntaxException, TestAccessException
    {
        TypeUsage usageA = createEmptyTypeUsage();
        TypeUsage usageB = createRealTypeUsage1();

        Object[] args = new Object[] { usageB, usageA };
        invoke(_testObject, "merge", args);

        assertEquals(3, usageA.getUsedFeatureCount());
        assertEquals(1, usageA.getUsedFields().getCount());
        assertEquals(1, usageA.getUsedConstructors().getCount());
        assertEquals(1, usageA.getUsedMethods().getCount());
    }

    public void testMerge2() throws SyntaxException, TestAccessException
    {
        TypeUsage usageA = createEmptyTypeUsage();
        TypeUsage usageB = createRealTypeUsage1();

        Object[] args = new Object[] { usageA, usageB };
        invoke(_testObject, "merge", args);

        assertEquals(0, usageA.getUsedFeatureCount());
        assertEquals(0, usageA.getUsedFields().getCount());
        assertEquals(0, usageA.getUsedConstructors().getCount());
        assertEquals(0, usageA.getUsedMethods().getCount());
    }

    public void testMerge3() throws SyntaxException, TestAccessException
    {
        TypeUsage usageA = createRealTypeUsage1();
        TypeUsage usageB = createRealTypeUsage2();

        Object[] args = new Object[] { usageA, usageB };
        invoke(_testObject, "merge", args);

        assertEquals(5, usageB.getUsedFeatureCount());
        assertEquals(2, usageB.getUsedFields().getCount());
        assertEquals(1, usageB.getUsedConstructors().getCount());
        assertEquals(2, usageB.getUsedMethods().getCount());
    }

    public void testMerge4() throws SyntaxException, TestAccessException
    {
        TypeUsage usageA = createRealTypeUsage1();
        TypeUsage usageB = createRealTypeUsage2();

        Object[] args = new Object[] { usageB, usageA };
        invoke(_testObject, "merge", args);

        assertEquals(5, usageA.getUsedFeatureCount());
        assertEquals(2, usageA.getUsedFields().getCount());
        assertEquals(1, usageA.getUsedConstructors().getCount());
        assertEquals(2, usageA.getUsedMethods().getCount());
    }
}
