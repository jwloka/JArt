package jast.test.analysis;
import jast.Global;
import jast.ParseException;
import jast.analysis.MethodUsage;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.TypeDeclaration;
import junitx.framework.TestAccessException;

public class TestMethodUsage extends TestBase
{

    private MethodUsage _testObject;

    public TestMethodUsage(String name)
    {
        super(name);
    }

    public void testAddUsage1() throws ParseException
    {
        addType("Class1",
                "class Class1 {\n" +
                "}\n",
                true);

        TypeDeclaration localType = _project.getType("Class1");

        _testObject = new MethodUsage(localType);
        _testObject.addUsage(localType);

        assertEquals(
            1,
            _testObject.getUsedTypes().getCount()
        );
        assertEquals(
            localType.getQualifiedName(),
            _testObject.getUsedTypes().get(0)
        );

        assertEquals(
            1,
            _testObject.getUsedFeatureCount()
        );
        assertEquals(
            1,
            _testObject.getUsedLocalFeatureCount()
        );
        assertEquals(
            0,
            _testObject.getUsedInheritedFeatureCount()
        );
        assertEquals(
            0,
            _testObject.getUsedRemoteFeatureCount()
        );
        assertEquals(
            0,
            _testObject.getMaxUsedRemoteFeatureCount());
    }

    public void testAddUsage11() throws ParseException
    {
        addType("Class1",
                "class Class1 {\n" +
                "}\n",
                true);

        TypeDeclaration localType = _project.getType("Class1");

        _testObject = new MethodUsage(localType);
        _testObject.addUsage(localType);
        _testObject.addUsage(localType);
        _testObject.addUsage(localType);
        _testObject.addUsage(localType);
        _testObject.addUsage(localType);
        _testObject.addUsage(localType);

        assertEquals(
            1,
            _testObject.getUsedTypes().getCount()
        );
        assertEquals(
            localType.getQualifiedName(),
            _testObject.getUsedTypes().get(0)
        );

        assertEquals(
            6,
            _testObject.getUsedFeatureCount()
        );
        assertEquals(
            6,
            _testObject.getUsedLocalFeatureCount()
        );
        assertEquals(
            0,
            _testObject.getUsedInheritedFeatureCount()
        );
        assertEquals(
            0,
            _testObject.getUsedRemoteFeatureCount()
        );
        assertEquals(
            0,
            _testObject.getMaxUsedRemoteFeatureCount());
    }

    public void testAddUsage2() throws ParseException
    {
        addType("Class1",
                "class Class1 {\n" +
                "}\n",
                true);

        addType("Class2",
                "class Class2 {\n" +
                "}\n",
                true);

        resolve();

        TypeDeclaration type1 = _project.getType("Class1");
        TypeDeclaration type2 = _project.getType("Class2");


        _testObject = new MethodUsage(type1);
        _testObject.addUsage(type2);

        assertEquals(
            1,
            _testObject.getUsedTypes().getCount());

        assertEquals(
            type2.getQualifiedName(),
            _testObject.getUsedTypes().get(0));

        assertEquals(
            1,
            _testObject.getUsedFeatureCount());

        assertEquals(
            0,
            _testObject.getUsedLocalFeatureCount());

        assertEquals(
            0,
            _testObject.getUsedInheritedFeatureCount());

        assertEquals(
            1,
            _testObject.getUsedRemoteFeatureCount());

        assertEquals(
            1,
            _testObject.getMaxUsedRemoteFeatureCount());

    }

    public void testAddUsage21() throws ParseException
    {
        addType("Class1",
                "class Class1 {\n" +
                "}\n",
                true);

        addType("Class2",
                "class Class2 {\n" +
                "}\n",
                true);

        addType("Class3",
                "class Class3 {\n" +
                "}\n",
                true);


        addType("Class4",
                "class Class4 {\n" +
                "}\n",
                true);

        resolve();

        TypeDeclaration type1 = _project.getType("Class1");
        TypeDeclaration type2 = _project.getType("Class2");
        TypeDeclaration type3 = _project.getType("Class3");
        TypeDeclaration type4 = _project.getType("Class4");


        _testObject = new MethodUsage(type1);
        _testObject.addUsage(type2);
        _testObject.addUsage(type3);
        _testObject.addUsage(type4);
        _testObject.addUsage(type4);

        assertEquals(
            3,
            _testObject.getUsedTypes().getCount());

        assertEquals(
            type2.getQualifiedName(),
            _testObject.getUsedTypes().get(0));
        assertEquals(
            type3.getQualifiedName(),
            _testObject.getUsedTypes().get(1));
        assertEquals(
            type4.getQualifiedName(),
            _testObject.getUsedTypes().get(2));

        assertEquals(
            4,
            _testObject.getUsedFeatureCount());

        assertEquals(
            0,
            _testObject.getUsedLocalFeatureCount());

        assertEquals(
            0,
            _testObject.getUsedInheritedFeatureCount());

        assertEquals(
            4,
            _testObject.getUsedRemoteFeatureCount());

        assertEquals(
            2,
            _testObject.getMaxUsedRemoteFeatureCount());

    }

    public void testAddUsage22() throws ParseException
    {
        addType("Class1",
                "class Class1 {\n" +
                "}\n",
                true);

        addType("Class2",
                "class Class2 {\n" +
                "}\n",
                true);

        addType("Class3",
                "class Class3 {\n" +
                "}\n",
                true);


        addType("Class4",
                "class Class4 {\n" +
                "}\n",
                true);

        resolve();

        TypeDeclaration type1 = _project.getType("Class1");
        TypeDeclaration type2 = _project.getType("Class2");
        TypeDeclaration type3 = _project.getType("Class3");
        TypeDeclaration type4 = _project.getType("Class4");


        _testObject = new MethodUsage(type1);
        _testObject.addUsage(type2);
        _testObject.addUsage(type3);
        _testObject.addUsage(type4);
        _testObject.addUsage(type4);
        _testObject.addUsage(type4);
        _testObject.addUsage(type4);

        assertEquals(
            3,
            _testObject.getUsedTypes().getCount());

        assertEquals(
            type2.getQualifiedName(),
            _testObject.getUsedTypes().get(0));
        assertEquals(
            type3.getQualifiedName(),
            _testObject.getUsedTypes().get(1));
        assertEquals(
            type4.getQualifiedName(),
            _testObject.getUsedTypes().get(2));

        assertEquals(
            6,
            _testObject.getUsedFeatureCount());

        assertEquals(
            0,
            _testObject.getUsedLocalFeatureCount());

        assertEquals(
            0,
            _testObject.getUsedInheritedFeatureCount());

        assertEquals(
            6,
            _testObject.getUsedRemoteFeatureCount());

        assertEquals(
            4,
            _testObject.getMaxUsedRemoteFeatureCount());

    }

    public void testAddUsage3() throws ParseException
    {
        addType("Class1",
                "class Class1 extends Class2 {\n" +
                "}\n",
                true);

        addType("Class2",
                "class Class2 {\n" +
                "}\n",
                true);

        resolve();

        TypeDeclaration type1 = _project.getType("Class1");
        TypeDeclaration type2 = _project.getType("Class2");


        _testObject = new MethodUsage(type1);
        _testObject.addUsage(type2);

        assertEquals(
            1,
            _testObject.getUsedTypes().getCount()
        );
        assertEquals(
            type2.getQualifiedName(),
            _testObject.getUsedTypes().get(0)
        );
        assertEquals(
            1,
            _testObject.getUsedFeatureCount()
        );
        assertEquals(
            0,
            _testObject.getUsedLocalFeatureCount()
        );
        assertEquals(
            1,
            _testObject.getUsedInheritedFeatureCount()
        );
        assertEquals(
            0,
            _testObject.getUsedRemoteFeatureCount()
        );
        assertEquals(
            0,
            _testObject.getMaxUsedRemoteFeatureCount());
    }

    public void testAddUsage31() throws ParseException
    {
        addType("Class1",
                "class Class1 extends Class2 {\n" +
                "}\n",
                true);

        addType("Class2",
                "class Class2 extends Class3 {\n" +
                "}\n",
                true);

        addType("Class3",
                "class Class3 extends Class4 {\n" +
                "}\n",
                true);

        addType("Class4",
                "class Class4 extends Class5 {\n" +
                "}\n",
                true);

        addType("Class5",
                "class Class5 {\n" +
                "}\n",
                true);

        resolve();

        TypeDeclaration type1 = _project.getType("Class1");
        TypeDeclaration type2 = _project.getType("Class2");
        TypeDeclaration type3 = _project.getType("Class3");
        TypeDeclaration type4 = _project.getType("Class4");
        TypeDeclaration type5 = _project.getType("Class5");

        _testObject = new MethodUsage(type1);
        _testObject.addUsage(type2);
        _testObject.addUsage(type3);
        _testObject.addUsage(type4);
        _testObject.addUsage(type5);
        _testObject.addUsage(type5);

        assertEquals(
            4,
            _testObject.getUsedTypes().getCount()
        );
        assertEquals(
            type2.getQualifiedName(),
            _testObject.getUsedTypes().get(0)
        );
        assertEquals(
            type3.getQualifiedName(),
            _testObject.getUsedTypes().get(1)
        );
        assertEquals(
            type4.getQualifiedName(),
            _testObject.getUsedTypes().get(2)
        );
        assertEquals(
            type5.getQualifiedName(),
            _testObject.getUsedTypes().get(3)
        );
        assertEquals(
            5,
            _testObject.getUsedFeatureCount()
        );
        assertEquals(
            0,
            _testObject.getUsedLocalFeatureCount()
        );
        assertEquals(
            5,
            _testObject.getUsedInheritedFeatureCount()
        );
        assertEquals(
            0,
            _testObject.getUsedRemoteFeatureCount()
        );
        assertEquals(
            0,
            _testObject.getMaxUsedRemoteFeatureCount());
    }

    public void testInitialize1()
    {
        try
        {
            _testObject = new MethodUsage(null);
            fail("Should throw an exception");
        }
        catch(IllegalArgumentException ex)
        {
            // test passed
        }
    }

    public void testInitialize2() throws TestAccessException
    {
        ClassDeclaration clsDecl =
            Global.getFactory().createClassDeclaration(
                Global.getFactory().createModifiers(),
                "MyClass",
                false);

        try
        {
            _testObject = new MethodUsage(clsDecl);
        }
        catch(IllegalArgumentException ex)
        {
            fail("No exception should be thrown");
        }

        assertNotNull(_testObject);
        assertEquals(
            clsDecl,
            get(_testObject, "_localType")
        );
        assertNotNull(get(_testObject, "_usedTypeNames"));
        assertNotNull(get(_testObject, "_usedRemoteTypes"));
    }
}
