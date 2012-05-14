package jart.test.restructuring;
import jart.restructuring.fieldlevel.CreateAccessorMethod;
import jast.ParseException;
import jast.ast.Node;
import jast.ast.nodes.FieldDeclaration;
import antlr.ANTLRException;

public class TestCreateAccessorMethod extends TestBase
{

    public TestCreateAccessorMethod(String name)
    {
        super(name);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();
        setRestructuring(new CreateAccessorMethod());
    }

    public void testGetterAnalyze1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration fieldDecl = _project.getType("test.A").getFields().get(0);
        Node[]           nodes     = { fieldDecl };
        Object[]         args      = { new Boolean(true),
                                       CreateAccessorMethod.generateAccessorName(
                                           fieldDecl.getName(),
                                           "get") };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testGetterAnalyze2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private String _attr;\n"+
                "  public String getAttribute() {\n"+
                "    return _attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration fieldDecl = _project.getType("test.A").getFields().get(0);
        Node[]           nodes     = { fieldDecl };
        Object[]         args      = { new Boolean(true),
                                       CreateAccessorMethod.generateAccessorName(
                                           fieldDecl.getName(),
                                           "get") };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testGetterAnalyze3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private double[] _attr;\n"+
                "  public double[] getAttr() {\n"+
                "    return _attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration fieldDecl = _project.getType("test.A").getFields().get(0);
        Node[]           nodes     = { fieldDecl };
        Object[]         args      = { new Boolean(true),
                                       CreateAccessorMethod.generateAccessorName(
                                           fieldDecl.getName(),
                                           "get") };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testGetterAnalyze4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public String[] getAttr() {\n"+
                "    return null;\n"+
                "  }\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B extends A {\n"+
                "  private String[] _attr;\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration fieldDecl = _project.getType("test.B").getFields().get(0);
        Node[]           nodes     = { fieldDecl };
        Object[]         args      = { new Boolean(true),
                                       CreateAccessorMethod.generateAccessorName(
                                           fieldDecl.getName(),
                                           "get") };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testGetterAnalyze5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private boolean _attr;\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B extends A {\n"+
                "  private boolean isAttr() {\n"+
                "    return false;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration fieldDecl = _project.getType("test.A").getFields().get(0);
        Node[]           nodes     = { fieldDecl };
        Object[]         args      = { new Boolean(true),
                                       CreateAccessorMethod.generateAccessorName(
                                           fieldDecl.getName(),
                                           "is") };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testGetterAnalyze6() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public static final int ATTR;\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration fieldDecl = _project.getType("test.A").getFields().get(0);
        Node[]           nodes     = { fieldDecl };
        Object[]         args      = { new Boolean(true),
                                       CreateAccessorMethod.generateAccessorName(
                                           fieldDecl.getName(),
                                           "get") };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testGetterPerform1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration fieldDecl = _project.getType("test.A").getFields().get(0);
        Node[]           nodes     = { fieldDecl };
        Object[]         args      = { new Boolean(true),
                                       CreateAccessorMethod.generateAccessorName(
                                           fieldDecl.getName(),
                                           "get") };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(1,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    public int _attr;\n\n\n"+
                     "    public int getAttr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testGetterPerform2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public boolean _attr;\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration fieldDecl = _project.getType("test.A").getFields().get(0);
        Node[]           nodes     = { fieldDecl };
        Object[]         args      = { new Boolean(true),
                                       CreateAccessorMethod.generateAccessorName(
                                           fieldDecl.getName(),
                                           "is") };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(1,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    public boolean _attr;\n\n\n"+
                     "    public boolean isAttr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testGetterPerform3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public boolean _isValid;\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration fieldDecl = _project.getType("test.A").getFields().get(0);
        Node[]           nodes     = { fieldDecl };
        Object[]         args      = { new Boolean(true),
                                       CreateAccessorMethod.generateAccessorName(
                                           fieldDecl.getName(),
                                           "is") };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(1,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    public boolean _isValid;\n\n\n"+
                     "    public boolean isValid()\n"+
                     "    {\n"+
                     "        return _isValid;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testGetterPerform4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private String[] Attr;\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration fieldDecl = _project.getType("test.A").getFields().get(0);
        Node[]           nodes     = { fieldDecl };
        Object[]         args      = { new Boolean(true),
                                       CreateAccessorMethod.generateAccessorName(
                                           fieldDecl.getName(),
                                           "get") };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(1,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    private String[] Attr;\n\n\n"+
                     "    private String[] getAttr()\n"+
                     "    {\n"+
                     "        return Attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testGetterPerform5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public static final String ATTR = \"\";\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration fieldDecl = _project.getType("test.A").getFields().get(0);
        Node[]           nodes     = { fieldDecl };
        Object[]         args      = { new Boolean(true),
                                       CreateAccessorMethod.generateAccessorName(
                                           fieldDecl.getName(),
                                           "get") };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(1,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    public static final String ATTR = \"\";\n\n\n"+
                     "    public static String getATTR()\n"+
                     "    {\n"+
                     "        return ATTR;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testSetterAnalyze1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration fieldDecl = _project.getType("test.A").getFields().get(0);
        Node[]           nodes     = { fieldDecl };
        Object[]         args      = { new Boolean(false),
                                       CreateAccessorMethod.generateAccessorName(
                                           fieldDecl.getName(),
                                           "set") };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testSetterAnalyze2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private String _attr;\n"+
                "  public void setAttribute(String newAttr) {\n"+
                "    _attr = newAttr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration fieldDecl = _project.getType("test.A").getFields().get(0);
        Node[]           nodes     = { fieldDecl };
        Object[]         args      = { new Boolean(false),
                                       CreateAccessorMethod.generateAccessorName(
                                           fieldDecl.getName(),
                                           "set") };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testSetterAnalyze3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private double[] _attr;\n"+
                "  public void setAttribute(Object attr) {\n"+
                "    _attr = (double[])attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration fieldDecl = _project.getType("test.A").getFields().get(0);
        Node[]           nodes     = { fieldDecl };
        Object[]         args      = { new Boolean(false),
                                       CreateAccessorMethod.generateAccessorName(
                                           fieldDecl.getName(),
                                           "set") };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testSetterAnalyze4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private double[] _attr;\n"+
                "  public void setAttr(double[] attr) {\n"+
                "    _attr = attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration fieldDecl = _project.getType("test.A").getFields().get(0);
        Node[]           nodes     = { fieldDecl };
        Object[]         args      = { new Boolean(false),
                                       CreateAccessorMethod.generateAccessorName(
                                           fieldDecl.getName(),
                                           "set") };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testSetterAnalyze5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void setAttr(String[] attr) {}\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B extends A {\n"+
                "  private String[] _attr;\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration fieldDecl = _project.getType("test.B").getFields().get(0);
        Node[]           nodes     = { fieldDecl };
        Object[]         args      = { new Boolean(false),
                                       CreateAccessorMethod.generateAccessorName(
                                           fieldDecl.getName(),
                                           "set") };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testSetterAnalyze6() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private boolean _attr;\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B extends A {\n"+
                "  private void setAttr(boolean newAttr) {}\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration fieldDecl = _project.getType("test.A").getFields().get(0);
        Node[]           nodes     = { fieldDecl };
        Object[]         args      = { new Boolean(false),
                                       CreateAccessorMethod.generateAccessorName(
                                           fieldDecl.getName(),
                                           "set") };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testSetterAnalyze7() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public static final int ATTR;\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration fieldDecl = _project.getType("test.A").getFields().get(0);
        Node[]           nodes     = { fieldDecl };
        Object[]         args      = { new Boolean(false),
                                       CreateAccessorMethod.generateAccessorName(
                                           fieldDecl.getName(),
                                           "set") };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testSetterPerform1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration fieldDecl = _project.getType("test.A").getFields().get(0);
        Node[]           nodes     = { fieldDecl };
        Object[]         args      = { new Boolean(false),
                                       CreateAccessorMethod.generateAccessorName(
                                           fieldDecl.getName(),
                                           "set") };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(1,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    public int _attr;\n\n\n"+
                     "    public void setAttr(int attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testSetterPerform2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public boolean _attr;\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration fieldDecl = _project.getType("test.A").getFields().get(0);
        Node[]           nodes     = { fieldDecl };
        Object[]         args      = { new Boolean(false),
                                       CreateAccessorMethod.generateAccessorName(
                                           fieldDecl.getName(),
                                           "set") };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(1,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    public boolean _attr;\n\n\n"+
                     "    public void setAttr(boolean attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testSetterPerform3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public boolean _setValue;\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration fieldDecl = _project.getType("test.A").getFields().get(0);
        Node[]           nodes     = { fieldDecl };
        Object[]         args      = { new Boolean(false),
                                       CreateAccessorMethod.generateAccessorName(
                                           fieldDecl.getName(),
                                           "set") };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(1,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    public boolean _setValue;\n\n\n"+
                     "    public void setValue(boolean setValue)\n"+
                     "    {\n"+
                     "        _setValue = setValue;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testSetterPerform4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private String[] Attr;\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration fieldDecl = _project.getType("test.A").getFields().get(0);
        Node[]           nodes     = { fieldDecl };
        Object[]         args      = { new Boolean(false),
                                       CreateAccessorMethod.generateAccessorName(
                                           fieldDecl.getName(),
                                           "set") };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(1,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    private String[] Attr;\n\n\n"+
                     "    private void setAttr(String[] Attr)\n"+
                     "    {\n"+
                     "        this.Attr = Attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testSetterPerform5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public static String ATTR = \"\";\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration fieldDecl = _project.getType("test.A").getFields().get(0);
        Node[]           nodes     = { fieldDecl };
        Object[]         args      = { new Boolean(false),
                                       CreateAccessorMethod.generateAccessorName(
                                           fieldDecl.getName(),
                                           "set") };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(1,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    public static String ATTR = \"\";\n\n\n"+
                     "    public static void setATTR(String ATTR)\n"+
                     "    {\n"+
                     "        this.ATTR = ATTR;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }
}
