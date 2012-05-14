package jast.test.analysis;
import jast.ParseException;
import jast.analysis.FeatureUsages;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.TypeDeclaration;

public class TestFeatureUsages extends TestBase
{

    private FeatureUsages _testObject;

    public TestFeatureUsages(String name)
    {
        super(name);
    }

    public void testAddUsage_Method1() throws ParseException
    {
        addType("Class1",
                "class Class1 {\n"+
                "  public void method() {}\n"+
                "}\n",
                true);

        TypeDeclaration typeDecl = _project.getType("Class1");

        _testObject = new FeatureUsages(typeDecl);
        _testObject.addUsage(typeDecl, typeDecl.getMethods().get(0));

        // check used types
        assertNotNull(_testObject.getUsedTypes());
        assertEquals(1,
                     _testObject.getUsedTypes().getCount());
        assertTrue(_testObject.isUsed(typeDecl));
        assertNotNull(_testObject.getTypeUsage(typeDecl));
        assertEquals(1,
                     _testObject.getTypeUsage(typeDecl).getUsedFeatureCount());

        // how many features are used?
        assertEquals(1,
                     _testObject.getUsedFeatureCount());
        assertEquals(0,
                     _testObject.getUsedRemoteFeatureCount());
        assertEquals(0,
                     _testObject.getUsedInheritedFeatureCount());
        assertEquals(1,
                     _testObject.getUsedLocalFeatureCount());

        // how many usages are counted?
        assertEquals(1,
                     _testObject.getUsageCount());
        assertEquals(0,
                     _testObject.getRemoteUsageCount());
        assertEquals(0,
                     _testObject.getInheritedUsageCount());
        assertEquals(1,
                     _testObject.getLocalUsageCount());

        // how many remote types are used?
        assertEquals(0,
                     _testObject.getUsedRemoteTypeCount());
        assertEquals(0,
                     _testObject.getUsedFeatureCountOfMaxUsedRemoteType());
    }

    public void testAddUsage_Method2() throws ParseException
    {
        addType("Class1",
                "class Class1 {}\n",
                true);
        addType("Class2",
                "class Class2 {\n"+
                " public void method() {}\n"+
                "}\n",
                true);

        resolve();

        TypeDeclaration   typeDecl   = _project.getType("Class2");
        MethodDeclaration methodDecl = typeDecl.getMethods().get(0);

        _testObject = new FeatureUsages(_project.getType("Class1"));
        _testObject.addUsage(typeDecl, methodDecl);
        _testObject.addUsage(typeDecl, methodDecl);

        // check used types
        assertNotNull(_testObject.getUsedTypes());
        assertEquals(1,
                     _testObject.getUsedTypes().getCount());
        assertTrue(_testObject.isUsed(typeDecl));
        assertNotNull(_testObject.getTypeUsage(typeDecl));
        assertEquals(1,
                     _testObject.getTypeUsage(typeDecl).getUsedFeatureCount());

        // how many features are used?
        assertEquals(1,
                     _testObject.getUsedFeatureCount());
        assertEquals(1,
                     _testObject.getUsedRemoteFeatureCount());
        assertEquals(0,
                     _testObject.getUsedInheritedFeatureCount());
        assertEquals(0,
                     _testObject.getUsedLocalFeatureCount());

        // how many usages are counted?
        assertEquals(2,
                     _testObject.getUsageCount());
        assertEquals(2,
                     _testObject.getRemoteUsageCount());
        assertEquals(0,
                     _testObject.getInheritedUsageCount());
        assertEquals(0,
                     _testObject.getLocalUsageCount());

        // how many remote types are used?
        assertEquals(1,
                     _testObject.getUsedRemoteTypeCount());
        assertEquals(1,
                     _testObject.getUsedFeatureCountOfMaxUsedRemoteType());

    }

    public void testAddUsage_Method3() throws ParseException
    {
        addType("Class1",
                "class Class1 extends Class2 {}\n",
                true);
        addType("Class2",
                "class Class2 {\n"+
                "  public void method1() {}\n"+
                "  public void method2() {}\n"+
                "}\n",
                true);

        resolve();

        TypeDeclaration   typeDecl    = _project.getType("Class2");
        MethodDeclaration methodDecl1 = typeDecl.getMethods().get(0);
        MethodDeclaration methodDecl2 = typeDecl.getMethods().get(1);

        _testObject = new FeatureUsages(_project.getType("Class1"));
        _testObject.addUsage(typeDecl, methodDecl1);
        _testObject.addUsage(typeDecl, methodDecl1);
        _testObject.addUsage(typeDecl, methodDecl2);

        // check used types
        assertNotNull(_testObject.getUsedTypes());
        assertEquals(1,
                     _testObject.getUsedTypes().getCount());
        assertTrue(_testObject.isUsed(typeDecl));
        assertNotNull(_testObject.getTypeUsage(typeDecl));
        assertEquals(2,
                     _testObject.getTypeUsage(typeDecl).getUsedFeatureCount());

        // how many features are used?
        assertEquals(2,
                     _testObject.getUsedFeatureCount());
        assertEquals(0,
                     _testObject.getUsedRemoteFeatureCount());
        assertEquals(2,
                     _testObject.getUsedInheritedFeatureCount());
        assertEquals(0,
                     _testObject.getUsedLocalFeatureCount());

        // how many usages are counted?
        assertEquals(3,
                     _testObject.getUsageCount());
        assertEquals(0,
                     _testObject.getRemoteUsageCount());
        assertEquals(3,
                     _testObject.getInheritedUsageCount());
        assertEquals(0,
                     _testObject.getLocalUsageCount());

        // how many remote types are used?
        assertEquals(0,
                     _testObject.getUsedRemoteTypeCount());
        assertEquals(0,
                     _testObject.getUsedFeatureCountOfMaxUsedRemoteType());
    }

    public void testAddUsage_Method4() throws ParseException
    {
        addType("Class1",
                "class Class1 {}\n",
                true);
        addType("Class2",
                "class Class2 {\n"+
                "  public void method() {}\n"+
                "}\n",
                true);
        addType("Class3",
                "class Class3 {\n"+
                "  public void method() {}\n"+
                "}\n",
                true);

        resolve();

        TypeDeclaration typeDecl2 = _project.getType("Class2");
        TypeDeclaration typeDecl3 = _project.getType("Class3");

        _testObject = new FeatureUsages(_project.getType("Class1"));
        _testObject.addUsage(typeDecl2, typeDecl2.getMethods().get(0));
        _testObject.addUsage(typeDecl3, typeDecl3.getMethods().get(0));

        // check used types
        assertNotNull(_testObject.getUsedTypes());
        assertEquals(2,
                     _testObject.getUsedTypes().getCount());
        assertTrue(_testObject.isUsed(typeDecl2));
        assertTrue(_testObject.isUsed(typeDecl3));
        assertNotNull(_testObject.getTypeUsage(typeDecl2));
        assertNotNull(_testObject.getTypeUsage(typeDecl3));
        assertEquals(1,
                     _testObject.getTypeUsage(typeDecl2).getUsedFeatureCount());
        assertEquals(1,
                     _testObject.getTypeUsage(typeDecl3).getUsedFeatureCount());

        // how many features are used?
        assertEquals(2,
                     _testObject.getUsedFeatureCount());
        assertEquals(2,
                     _testObject.getUsedRemoteFeatureCount());
        assertEquals(0,
                     _testObject.getUsedInheritedFeatureCount());
        assertEquals(0,
                     _testObject.getUsedLocalFeatureCount());

        // how many usages are counted?
        assertEquals(2,
                     _testObject.getUsageCount());
        assertEquals(2,
                     _testObject.getRemoteUsageCount());
        assertEquals(0,
                     _testObject.getInheritedUsageCount());
        assertEquals(0,
                     _testObject.getLocalUsageCount());

        // how many remote types are used?
        assertEquals(2,
                     _testObject.getUsedRemoteTypeCount());
        assertEquals(1,
                     _testObject.getUsedFeatureCountOfMaxUsedRemoteType());
    }

    public void testAddUsage_Method5() throws ParseException
    {
        addType("Class1",
                "class Class1 extends Class2 {}\n",
                true);
        addType("Class2",
                "class Class2 extends Class3 {}\n",
                true);
        addType("Class3",
                "class Class3 extends Class4 {\n"+
                " public void method() {}\n"+
                "}\n",
                true);
        addType("Class4",
                "class Class4 extends Class5 {\n"+
                " public void method() {}\n"+
                "}\n",
                true);
        addType("Class5",
                "class Class5 {\n"+
                " public void method() {}\n"+
                "}\n",
                true);

        resolve();

        TypeDeclaration   typeDecl1   = _project.getType("Class1");
        TypeDeclaration   typeDecl2   = _project.getType("Class2");
        TypeDeclaration   typeDecl3   = _project.getType("Class3");
        TypeDeclaration   typeDecl4   = _project.getType("Class4");
        TypeDeclaration   typeDecl5   = _project.getType("Class5");
        MethodDeclaration methodDecl1 = typeDecl3.getMethods().get(0);
        MethodDeclaration methodDecl2 = typeDecl4.getMethods().get(0);
        MethodDeclaration methodDecl3 = typeDecl5.getMethods().get(0);

        _testObject = new FeatureUsages(typeDecl1);
        _testObject.addUsage(typeDecl1, methodDecl1);
        _testObject.addUsage(typeDecl2, methodDecl2);
        _testObject.addUsage(typeDecl3, methodDecl2);
        _testObject.addUsage(typeDecl4, methodDecl2);
        _testObject.addUsage(typeDecl5, methodDecl3);

        // check used types
        assertNotNull(_testObject.getUsedTypes());
        assertEquals(5,
                     _testObject.getUsedTypes().getCount());
        assertTrue(_testObject.isUsed(typeDecl1));
        assertTrue(_testObject.isUsed(typeDecl2));
        assertTrue(_testObject.isUsed(typeDecl3));
        assertTrue(_testObject.isUsed(typeDecl4));
        assertTrue(_testObject.isUsed(typeDecl5));
        assertNotNull(_testObject.getTypeUsage(typeDecl1));
        assertNotNull(_testObject.getTypeUsage(typeDecl2));
        assertNotNull(_testObject.getTypeUsage(typeDecl3));
        assertNotNull(_testObject.getTypeUsage(typeDecl4));
        assertNotNull(_testObject.getTypeUsage(typeDecl5));
        assertEquals(1,
                     _testObject.getTypeUsage(typeDecl1).getUsedFeatureCount());
        assertEquals(1,
                     _testObject.getTypeUsage(typeDecl2).getUsedFeatureCount());
        assertEquals(1,
                     _testObject.getTypeUsage(typeDecl3).getUsedFeatureCount());
        assertEquals(1,
                     _testObject.getTypeUsage(typeDecl4).getUsedFeatureCount());
        assertEquals(1,
                     _testObject.getTypeUsage(typeDecl5).getUsedFeatureCount());

        // how many features are used?
        assertEquals(5,
                     _testObject.getUsedFeatureCount());
        // 5 features are used, since methodDecl2
        // is every time accessed on a different type
        assertEquals(0,
                     _testObject.getUsedRemoteFeatureCount());
        assertEquals(4,
                     _testObject.getUsedInheritedFeatureCount());
        assertEquals(1,
                     _testObject.getUsedLocalFeatureCount());

        // how many usages are counted?
        assertEquals(5,
                     _testObject.getUsageCount());
        assertEquals(0,
                     _testObject.getRemoteUsageCount());
        assertEquals(4,
                     _testObject.getInheritedUsageCount());
        assertEquals(1,
                     _testObject.getLocalUsageCount());

        // how many remote types are used?
        assertEquals(0,
                     _testObject.getUsedRemoteTypeCount());
        assertEquals(0,
                     _testObject.getUsedFeatureCountOfMaxUsedRemoteType());
    }

    public void testAddUsage_Method6() throws ParseException
    {
        addType("Class1",
                "class Class1 {\n"+
                "  class InnerClass {\n"+
                "     public void method() {}\n"+
                "  };\n"+
                "}\n",
                true);

        resolve();

        TypeDeclaration   typeDecl1  = _project.getType("Class1");
        TypeDeclaration   typeDecl2  = typeDecl1.getInnerTypes().get("InnerClass");
        MethodDeclaration methodDecl = typeDecl2.getMethods().get(0);

        _testObject = new FeatureUsages(typeDecl1);
        _testObject.addUsage(typeDecl1, methodDecl);
        _testObject.addUsage(typeDecl2, methodDecl);
        _testObject.addUsage(typeDecl2, methodDecl);

        // check used types
        assertNotNull(_testObject.getUsedTypes());
        assertEquals(2,
                     _testObject.getUsedTypes().getCount());
        assertTrue(_testObject.isUsed(typeDecl1));
        assertTrue(_testObject.isUsed(typeDecl2));
        assertNotNull(_testObject.getTypeUsage(typeDecl1));
        assertNotNull(_testObject.getTypeUsage(typeDecl2));
        assertEquals(1,
                     _testObject.getTypeUsage(typeDecl1).getUsedFeatureCount());
        assertEquals(1,
                     _testObject.getTypeUsage(typeDecl2).getUsedFeatureCount());

        // how many features are used?
        assertEquals(2,
                     _testObject.getUsedFeatureCount());
        assertEquals(0,
                     _testObject.getUsedRemoteFeatureCount());
        assertEquals(0,
                     _testObject.getUsedInheritedFeatureCount());
        assertEquals(2,
                     _testObject.getUsedLocalFeatureCount());

        // how many usages are counted?
        assertEquals(3,
                     _testObject.getUsageCount());
        assertEquals(0,
                     _testObject.getRemoteUsageCount());
        assertEquals(0,
                     _testObject.getInheritedUsageCount());
        assertEquals(3,
                     _testObject.getLocalUsageCount());

        // how many remote types are used?
        assertEquals(0,
                     _testObject.getUsedRemoteTypeCount());
        assertEquals(0,
                     _testObject.getUsedFeatureCountOfMaxUsedRemoteType());

    }
}
