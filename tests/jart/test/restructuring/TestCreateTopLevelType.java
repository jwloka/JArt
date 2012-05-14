package jart.test.restructuring;
import jart.restructuring.typelevel.CreateTopLevelType;
import jast.ParseException;
import jast.ast.Node;
import antlr.ANTLRException;

public class TestCreateTopLevelType extends TestBase
{

    public TestCreateTopLevelType(String name)
    {
        super(name);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();
        setRestructuring(new CreateTopLevelType());
    }

    public void testAnalyze1() throws ANTLRException
    {
        Node[]   nodes = { _project };
        Object[] args  = { new Boolean(true),
                           "A" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze2() throws ANTLRException
    {
        addType("A",
                "public class A {}\n",
                true);
        resolve();

        Node[]   nodes = { _project };
        Object[] args  = { new Boolean(false),
                           "",
                           "A" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {}\n",
                true);
        resolve();

        Node[]   nodes = { _project };
        Object[] args  = { new Boolean(true),
                           _project.getPackage("test"),
                           "B" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {}\n",
                true);
        resolve();

        Node[]   nodes = { _project };
        Object[] args  = { new Boolean(false),
                           "test.A" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze5() throws ANTLRException
    {
        addType("test.sub.A",
                "package test.sub;\n"+
                "public class A {}\n",
                true);
        resolve();

        Node[]   nodes = { _project };
        Object[] args  = { new Boolean(true),
                           "test",
                           "sub" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze6() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {}\n",
                true);
        resolve();

        Node[]   nodes = { _project };
        Object[] args  = { new Boolean(false),
                           "test.A.B" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze7() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {}\n",
                true);
        resolve();

        Node[]   nodes = { _project };
        Object[] args  = { new Boolean(true),
                           _project.getPackage("test"),
                           "Object" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyze8() throws ANTLRException
    {
        addType("A",
                "public class A {}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B {}\n",
                true);
        resolve();

        Node[]   nodes = { _project };
        Object[] args  = { new Boolean(false),
                           "test",
                           "A" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testPerform1() throws ANTLRException
    {
        Node[]   nodes = { _project };
        Object[] args  = { new Boolean(true),
                           "A" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(perform());

        // default package has been created
        assertEquals(1,
                     getNewPackages().getCount());
        assertTrue(getNewPackages().contains(""));
        assertTrue(getRemovedPackages().isEmpty());
        assertEquals(1,
                     getNewUnits().getCount());
        assertTrue(getNewUnits().contains(toUnitName("A")));
        assertTrue(getChangedUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
    }

    public void testPerform2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {}\n",
                true);
        addType("B",
                "import test.A;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project };
        Object[] args  = { new Boolean(true),
                           "A" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertEquals(1,
                     getNewUnits().getCount());
        assertTrue(getNewUnits().contains(toUnitName("A")));
        assertEquals(1,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("B")));
        assertTrue(getRemovedUnits().isEmpty());
    }

    public void testPerform3() throws ANTLRException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {}\n",
                true);
        resolve();

        Node[]   nodes = { _project };
        Object[] args  = { new Boolean(true),
                           "test.A" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertEquals(1,
                     getNewUnits().getCount());
        assertTrue(getNewUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
    }

    public void testPerform4() throws ANTLRException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "import test1.A;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project };
        Object[] args  = { new Boolean(true),
                           "test.A" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertEquals(1,
                     getNewUnits().getCount());
        assertTrue(getNewUnits().contains(toUnitName("test.A")));
        assertEquals(1,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.B")));
        assertTrue(getRemovedUnits().isEmpty());
    }

    public void testPerform5() throws ANTLRException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "import test.*;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "}\n",
                true);
        addType("test.C",
                "package test;\n"+
                "public class C {}\n",
                true);
        resolve();

        Node[]   nodes = { _project };
        Object[] args  = { new Boolean(true),
                           "test.A" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertEquals(1,
                     getNewUnits().getCount());
        assertTrue(getNewUnits().contains(toUnitName("test.A")));
        assertEquals(1,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test1.B")));
        assertTrue(getRemovedUnits().isEmpty());
    }

    public void testPerform6() throws ANTLRException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "import test.C;\n"+
                "public class B {\n"+
                "  private A _attr;\n"+
                "}\n",
                true);
        addType("test.C",
                "package test;\n"+
                "public class C {}\n",
                true);
        resolve();

        Node[]   nodes = { _project };
        Object[] args  = { new Boolean(true),
                           "test.A" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertEquals(1,
                     getNewUnits().getCount());
        assertTrue(getNewUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
    }

    public void testPerform7() throws ANTLRException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  private A      _attr1;\n"+
                "  private test.C _attr2;\n"+
                "}\n",
                true);
        addType("test.C",
                "package test;\n"+
                "public class C {}\n",
                true);
        resolve();

        Node[]   nodes = { _project };
        Object[] args  = { new Boolean(true),
                           "test.A" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertEquals(1,
                     getNewUnits().getCount());
        assertTrue(getNewUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
    }

    public void testPerform8() throws ANTLRException
    {
        Node[]   nodes = { _project };
        Object[] args  = { new Boolean(true),
                           "test.sub.A" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(perform());

        assertEquals(1,
                     getNewPackages().getCount());
        assertTrue(getNewPackages().contains("test.sub"));
        assertTrue(getRemovedPackages().isEmpty());
        assertEquals(1,
                     getNewUnits().getCount());
        assertTrue(getNewUnits().contains(toUnitName("test.sub.A")));
        assertTrue(getChangedUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
    }
}
