package jast.test.analysis;
import jast.Global;
import jast.ParseException;
import jast.SyntaxException;
import jast.analysis.FeatureUsages;
import jast.analysis.Usages;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.TypeDeclaration;
import junitx.framework.PrivateTestCase;
import junitx.framework.TestAccessException;


public class TestUsages extends PrivateTestCase
{

    private Usages _testObject;

    public TestUsages(String name)
    {
        super(name);
    }

    private ClassDeclaration createTypeDecl(String name)
    {
        return Global.getFactory().createClassDeclaration(
            Global.getFactory().createModifiers(),
            name,
            false);
    }

    protected void setUp()
    {
        ClassDeclaration localTypeDecl =
            Global.getFactory().createClassDeclaration(
                Global.getFactory().createModifiers(),
                "LocalType",
                false);

        CompilationUnit cu =
            Global.getFactory().createCompilationUnit("LocalType.java");
        cu.getTypeDeclarations().add(localTypeDecl);

        _testObject = new Usages(localTypeDecl)
        {
            public int getUsedFeatureCount()
            {
                return 0;
            }
            public int getUsedInheritedFeatureCount()
            {
                return 0;
            }
            public int getUsedLocalFeatureCount()
            {
                return 0;
            }
            public int getUsedRemoteFeatureCount()
            {
                return 0;
            }
        };
    }

    protected void tearDown() throws ParseException
    {
        _testObject = null;
    }

    public void testAddUsages_Type1() throws SyntaxException
    {
        TypeDeclaration type1 = createTypeDecl("Class1");

        _testObject.addUsage(type1);

        // check used types?
        assertNotNull(_testObject.getUsedTypes());
        assertEquals(1, _testObject.getUsedTypes().getCount());
        assertTrue(_testObject.isUsed(type1));
        assertNotNull(_testObject.getTypeUsage(type1));
        assertEquals(0, _testObject.getTypeUsage(type1).getUsedFeatureCount());

        //     remote type usage?
        assertEquals(1, _testObject.getUsedRemoteTypeCount());
        assertEquals(0, _testObject.getUsedFeatureCountOfMaxUsedRemoteType());
        assertEquals(1, _testObject.getUsageCountOfMaxUsedRemoteType());

        // which kind of usages are counted?
        assertEquals(1, _testObject.getUsageCount());
        assertEquals(1, _testObject.getRemoteUsageCount());
        assertEquals(0, _testObject.getInheritedUsageCount());
        assertEquals(0, _testObject.getLocalUsageCount());

    }

    public void testAddUsages_Type2() throws SyntaxException
    {
        TypeDeclaration type1 = createTypeDecl("Class1");

        _testObject.addUsage(type1);
        _testObject.addUsage(type1);

        // check used types?
        assertNotNull(_testObject.getUsedTypes());
        assertEquals(1, _testObject.getUsedTypes().getCount());
        assertTrue(_testObject.isUsed(type1));
        assertNotNull(_testObject.getTypeUsage(type1));
        assertEquals(0, _testObject.getTypeUsage(type1).getUsedFeatureCount());

        //     remote type usage?
        assertEquals(1, _testObject.getUsedRemoteTypeCount());
        assertEquals(0, _testObject.getUsedFeatureCountOfMaxUsedRemoteType());
        assertEquals(2, _testObject.getUsageCountOfMaxUsedRemoteType());

        // how many usages are counted?
        assertEquals(2, _testObject.getUsageCount());
        assertEquals(2, _testObject.getRemoteUsageCount());
        assertEquals(0, _testObject.getInheritedUsageCount());
        assertEquals(0, _testObject.getLocalUsageCount());
    }

    public void testAddUsages_Type3() throws SyntaxException
    {
        // type1 is parent of local type
        TypeDeclaration type1 = createTypeDecl("Class1");
        ((ClassDeclaration) _testObject.getLocalType()).setBaseClass(
            type1.getCreatedType());

        _testObject.addUsage(type1);
        _testObject.addUsage(type1);

        // check used types?
        assertNotNull(_testObject.getUsedTypes());
        assertEquals(1, _testObject.getUsedTypes().getCount());
        assertTrue(_testObject.isUsed(type1));
        assertNotNull(_testObject.getTypeUsage(type1));
        assertEquals(0, _testObject.getTypeUsage(type1).getUsedFeatureCount());

        //     remote type usage?
        assertEquals(0, _testObject.getUsedRemoteTypeCount());
        assertEquals(0, _testObject.getUsedFeatureCountOfMaxUsedRemoteType());
        assertEquals(0, _testObject.getUsageCountOfMaxUsedRemoteType());

        // how many usages are counted?
        assertEquals(2, _testObject.getUsageCount());
        assertEquals(0, _testObject.getRemoteUsageCount());
        assertEquals(2, _testObject.getInheritedUsageCount());
        assertEquals(0, _testObject.getLocalUsageCount());
    }

    public void testAddUsages_Type4() throws SyntaxException
    {
        // type1 is child of local type
        ClassDeclaration type1 = createTypeDecl("Class1");
        type1.setBaseClass(_testObject.getLocalType().getCreatedType());

        _testObject.addUsage(type1);
        _testObject.addUsage(type1);

        // check used types?
        assertNotNull(_testObject.getUsedTypes());
        assertEquals(1, _testObject.getUsedTypes().getCount());
        assertTrue(_testObject.isUsed(type1));
        assertNotNull(_testObject.getTypeUsage(type1));
        assertEquals(0, _testObject.getTypeUsage(type1).getUsedFeatureCount());

        //     remote type usage?
        assertEquals(1, _testObject.getUsedRemoteTypeCount());
        assertEquals(0, _testObject.getUsedFeatureCountOfMaxUsedRemoteType());
        assertEquals(2, _testObject.getUsageCountOfMaxUsedRemoteType());

        // how many usages are counted?
        assertEquals(2, _testObject.getUsageCount());
        assertEquals(2, _testObject.getRemoteUsageCount());
        assertEquals(0, _testObject.getInheritedUsageCount());
        assertEquals(0, _testObject.getLocalUsageCount());
    }

    public void testAddUsages_Type5() throws SyntaxException
    {
        // type1 is local type
        TypeDeclaration type1 = _testObject.getLocalType();

        _testObject.addUsage(type1);
        _testObject.addUsage(type1);

        // check used types?
        assertNotNull(_testObject.getUsedTypes());
        assertEquals(1, _testObject.getUsedTypes().getCount());
        assertTrue(_testObject.isUsed(type1));
        assertNotNull(_testObject.getTypeUsage(type1));
        assertEquals(0, _testObject.getTypeUsage(type1).getUsedFeatureCount());

        //     remote type usage?
        assertEquals(0, _testObject.getUsedRemoteTypeCount());
        assertEquals(0, _testObject.getUsedFeatureCountOfMaxUsedRemoteType());
        assertEquals(0, _testObject.getUsageCountOfMaxUsedRemoteType());

        // how many usages are counted?
        assertEquals(2, _testObject.getUsageCount());
        assertEquals(0, _testObject.getRemoteUsageCount());
        assertEquals(0, _testObject.getInheritedUsageCount());
        assertEquals(2, _testObject.getLocalUsageCount());
    }

    public void testAddUsages_Type6() throws SyntaxException
    {
        // type1 and local type are owned by same compilation unit (still a remote type)
        TypeDeclaration type1 = createTypeDecl("Class1");
        CompilationUnit cu =
            Global.getFactory().createCompilationUnit("LocalType.java");
        cu.getTypeDeclarations().add(_testObject.getLocalType());
        cu.getTypeDeclarations().add(type1);

        _testObject.addUsage(type1);
        _testObject.addUsage(type1);

        // check used types?
        assertNotNull(_testObject.getUsedTypes());
        assertEquals(1, _testObject.getUsedTypes().getCount());
        assertTrue(_testObject.isUsed(type1));
        assertNotNull(_testObject.getTypeUsage(type1));
        assertEquals(0, _testObject.getTypeUsage(type1).getUsedFeatureCount());

        // remote type usage?
        assertEquals(1, _testObject.getUsedRemoteTypeCount());
        assertEquals(0, _testObject.getUsedFeatureCountOfMaxUsedRemoteType());
        assertEquals(2, _testObject.getUsageCountOfMaxUsedRemoteType());

        // how many usages are counted?
        assertEquals(2, _testObject.getUsageCount());
        assertEquals(2, _testObject.getRemoteUsageCount());
        assertEquals(0, _testObject.getInheritedUsageCount());
        assertEquals(0, _testObject.getLocalUsageCount());
    }

    public void testAddUsages_Type7() throws SyntaxException
    {
        // type1 is inner type of local type
        TypeDeclaration type1 = createTypeDecl("Class1");
        _testObject.getLocalType().getInnerTypes().add(type1);

        _testObject.addUsage(type1);
        _testObject.addUsage(type1);

        // check used types?
        assertNotNull(_testObject.getUsedTypes());
        assertEquals(1, _testObject.getUsedTypes().getCount());
        assertTrue(_testObject.isUsed(type1));
        assertNotNull(_testObject.getTypeUsage(type1));
        assertEquals(0, _testObject.getTypeUsage(type1).getUsedFeatureCount());

        //     remote type usage?
        assertEquals(0, _testObject.getUsedRemoteTypeCount());
        assertEquals(0, _testObject.getUsedFeatureCountOfMaxUsedRemoteType());
        assertEquals(0, _testObject.getUsageCountOfMaxUsedRemoteType());

        // how many usages are counted?
        assertEquals(2, _testObject.getUsageCount());
        assertEquals(0, _testObject.getRemoteUsageCount());
        assertEquals(0, _testObject.getInheritedUsageCount());
        assertEquals(2, _testObject.getLocalUsageCount());
    }

    public void testAddUsages_Type8() throws SyntaxException
    {
        // local type is inner type of type1
        TypeDeclaration type1 = createTypeDecl("Class1");
        type1.getInnerTypes().add(_testObject.getLocalType());

        _testObject.addUsage(type1);
        _testObject.addUsage(type1);

        // check used types?
        assertNotNull(_testObject.getUsedTypes());
        assertEquals(1, _testObject.getUsedTypes().getCount());
        assertTrue(_testObject.isUsed(type1));
        assertNotNull(_testObject.getTypeUsage(type1));
        assertEquals(0, _testObject.getTypeUsage(type1).getUsedFeatureCount());

        //     remote type usage?
        assertEquals(0, _testObject.getUsedRemoteTypeCount());
        assertEquals(0, _testObject.getUsedFeatureCountOfMaxUsedRemoteType());
        assertEquals(0, _testObject.getUsageCountOfMaxUsedRemoteType());

        // how many usages are counted?
        assertEquals(2, _testObject.getUsageCount());
        assertEquals(0, _testObject.getRemoteUsageCount());
        assertEquals(0, _testObject.getInheritedUsageCount());
        assertEquals(2, _testObject.getLocalUsageCount());
    }

    public void testInitialize1()
    {
        try
            {
            _testObject = new FeatureUsages(null);
            fail("Should throw an exception");
        }
        catch (IllegalArgumentException ex)
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
            _testObject = new FeatureUsages(clsDecl);
        }
        catch (IllegalArgumentException ex)
            {
            fail("No exception should be thrown");
        }

        assertNotNull(_testObject);
        assertNotNull(_testObject.getUsedTypes());

        assertEquals(clsDecl, _testObject.getLocalType());

        // check used types?
        assertNotNull(_testObject.getUsedTypes());
        assertEquals(0, _testObject.getUsedTypes().getCount());

        //     remote type usage?
        assertEquals(0, _testObject.getUsedRemoteTypeCount());
        assertEquals(0, _testObject.getUsedFeatureCountOfMaxUsedRemoteType());
        assertEquals(0, _testObject.getUsageCountOfMaxUsedRemoteType());

        // how many usages are counted?
        assertEquals(0, _testObject.getUsageCount());
        assertEquals(0, _testObject.getRemoteUsageCount());
        assertEquals(0, _testObject.getInheritedUsageCount());
        assertEquals(0, _testObject.getLocalUsageCount());
    }
}
