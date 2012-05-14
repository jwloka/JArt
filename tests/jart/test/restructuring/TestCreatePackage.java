package jart.test.restructuring;
import jart.restructuring.packagelevel.CreatePackage;
import jast.ParseException;
import jast.ast.Node;
import antlr.ANTLRException;

public class TestCreatePackage extends TestBase
{

    public TestCreatePackage(String name)
    {
        super(name);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();
        setRestructuring(new CreatePackage());
    }

    public void testAnalyze1() throws ANTLRException
    {
        Node[]   nodes = { _project };
        Object[] args  = { "test" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze2() throws ANTLRException
    {
        Node[]   nodes = { _project };
        Object[] args  = { "test.sub" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze3() throws ANTLRException
    {
        // we add a type to ensure that the package test exists
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public A() {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project };
        Object[] args  = { "test.sub" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze4() throws ANTLRException
    {
        // we add a type to ensure that the package test.sub exists
        addType("test.sub.A",
                "package test.sub;\n"+
                "public class A {\n"+
                "  public A() {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project };
        Object[] args  = { "test" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze5() throws ANTLRException
    {
        // we add a type to ensure that the package test exists
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public A() {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project };
        Object[] args  = { "test" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze6() throws ANTLRException
    {
        Node[]   nodes = { _project };
        Object[] args  = { "" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze7() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public A() {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project };
        Object[] args  = { "test.A" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze8() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public A() {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project };
        Object[] args  = { "test.A.B" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze9() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project };
        Object[] args  = { "test.A.B" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testPerform1() throws ANTLRException
    {
        Node[]   nodes = { _project };
        Object[] args  = { "test" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(perform());

        assertEquals(1,
                     getNewPackages().getCount());
        assertTrue(getNewPackages().contains("test"));
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getChangedUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());

        assertNotNull(_project.getPackage("test"));
    }

    public void testPerform2() throws ANTLRException
    {
        Node[]   nodes = { _project };
        Object[] args  = { "test.sub" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(perform());

        assertEquals(1,
                     getNewPackages().getCount());
        assertTrue(getNewPackages().contains("test.sub"));
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getChangedUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());

        assertNull(_project.getPackage("test"));
        assertNotNull(_project.getPackage("test.sub"));
    }

    public void testPerform3() throws ANTLRException
    {
        // we add a type to ensure that the package test exists
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public A() {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project };
        Object[] args  = { "test.sub" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(perform());

        assertEquals(1,
                     getNewPackages().getCount());
        assertTrue(getNewPackages().contains("test.sub"));
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getChangedUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());

        assertNotNull(_project.getPackage("test"));
        assertNotNull(_project.getPackage("test.sub"));
    }

    public void testPerform4() throws ANTLRException
    {
        // we add a type to ensure that the package test exists
        addType("test.sub.A",
                "package test.sub;\n"+
                "public class A {\n"+
                "  public A() {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project };
        Object[] args  = { "test" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(perform());

        assertEquals(1,
                     getNewPackages().getCount());
        assertTrue(getNewPackages().contains("test"));
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getChangedUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());

        assertNotNull(_project.getPackage("test"));
        assertNotNull(_project.getPackage("test.sub"));
    }
}
