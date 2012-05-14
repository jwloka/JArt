package jart.test.restructuring;
import jart.restructuring.fieldlevel.EncapsulateField;
import jast.ParseException;
import jast.ast.Node;
import antlr.ANTLRException;

public class TestEncapsulateField extends TestBase
{

    public TestEncapsulateField(String name)
    {
        super(name);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();
        setRestructuring(new EncapsulateField());
    }

    public void testAnalyze1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze12() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  public void test() {\n"+
                "    int var = _attr;\n"+
                "  }\n"+
                "  public int attr() {\n"+
                "    return _attr;\n"+
                "  }\n"+
                "  public void getAttr() {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
        assertTrue(hasNotes());
    }

    public void testAnalyze13() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  public void test() {\n"+
                "    _attr = 0;\n"+
                "  }\n"+
                "  public void getAttr() {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze14() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  public void test() {\n"+
                "    _attr = 0;\n"+
                "  }\n"+
                "  public void setAttr(int val) {\n"+
                "    _attr = val;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
        assertTrue(hasNotes());
    }

    public void testAnalyze15() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  public void test() {\n"+
                "    _attr = 0;\n"+
                "  }\n"+
                "  public void initAttr(short val) {\n"+
                "    _attr = val;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyze16() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  public void test() {\n"+
                "    _attr = 0;\n"+
                "  }\n"+
                "  public void setAttr(int val) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze17() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  public void test() {\n"+
                "    _attr = 0;\n"+
                "  }\n"+
                "  public void initAttr(int val) {\n"+
                "    _attr = val;\n"+
                "  }\n"+
                "  public void setAttr(int val) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
        assertTrue(hasNotes());
    }

    public void testAnalyze18() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  public void test() {\n"+
                "    int val = _attr;\n"+
                "  }\n"+
                "  public void setAttr(int val) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze19() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  private A doSomething() {\n"+
                "    _attr++;\n"+
                "  }\n"+
                "  public class B {\n"+
                "    public void test() {\n"+
                "      doSomething()._attr += 1;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  public void test() {\n"+
                "    int var = _attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected int _attr;\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B extends A {\n"+
                "  public void test() {\n"+
                "    _attr += 5;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  int _attr;\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  public void test(A obj) {\n"+
                "    int val = obj._attr = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze7() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  private void test() {\n"+
                "    class Local {\n"+
                "      public void test(A obj) {\n"+
                "        _attr--;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze8() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test;\n"+
                "public class B {\n"+
                "  public void test(test.A obj) {\n"+
                "    int var = obj._attr--;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze9() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  public void test() {\n"+
                "    int var = _attr;\n"+
                "  }\n"+
                "  public int getAttr() {\n"+
                "    return _attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
        assertTrue(hasNotes());
    }

    public void testAnalyze10() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  public void test() {\n"+
                "    int var = _attr;\n"+
                "  }\n"+
                "  public long attr() {\n"+
                "    return _attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyze11() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  public void test() {\n"+
                "    int var = _attr;\n"+
                "  }\n"+
                "  public void getAttr() {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  public class B {\n"+
                "    public void test(A obj) {\n"+
                "      ++this._attr;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }



    public void testAnalyze6() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  public class B {\n"+
                "    public void test(A obj) {\n"+
                "      int var = _attr++;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }



    public void testPerformEncapsulate1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected int _attr;\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B extends test.A {\n"+
                "  public void test() {\n"+
                "    int var = _attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(false) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(2,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().contains(toUnitName("test1.B")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    private int _attr;\n\n\n"+
                     "    protected int getAttr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test1;\n\n"+
                     "public class B extends test.A\n"+
                     "{\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        int var = getAttr();\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test1.B"));
    }



    public void testPerformEncapsulate10() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public static int _attr;\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test() {\n"+
                "    class Local {\n"+
                "      private void doSomething() {\n"+
                "        test.A._attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(false) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(2,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().contains(toUnitName("test1.B")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    private static int _attr;\n\n\n"+
                     "    public static void setAttr(int attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test1;\n\n"+
                     "public class B\n"+
                     "{\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        class Local\n"+
                     "        {\n"+
                     "            private void doSomething()\n"+
                     "            {\n"+
                     "                test.A.setAttr(1);\n"+
                     "            }\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test1.B"));
    }



    public void testPerformEncapsulate11() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "  protected int attr() {\n"+
                "    return _attr;\n"+
                "  }\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(test.A obj) {\n"+
                "    int var = obj._attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(false) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(2,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().contains(toUnitName("test1.B")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    private int _attr;\n\n\n"+
                     "    public int attr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test1;\n\n"+
                     "public class B\n"+
                     "{\n"+
                     "    public void test(test.A obj)\n"+
                     "    {\n"+
                     "        int var = obj.attr();\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test1.B"));
    }



    public void testPerformEncapsulate12() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "  private void initAttr(int attr) {\n"+
                "    _attr = attr;\n"+
                "  }\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(test.A obj) {\n"+
                "    obj._attr = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(false) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(2,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().contains(toUnitName("test1.B")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    private int _attr;\n\n\n"+
                     "    public void initAttr(int attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test1;\n\n"+
                     "public class B\n"+
                     "{\n"+
                     "    public void test(test.A obj)\n"+
                     "    {\n"+
                     "        obj.initAttr(1);\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test1.B"));
    }



    public void testPerformEncapsulate13() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "  protected long attr() {\n"+
                "    return _attr;\n"+
                "  }\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(test.A obj) {\n"+
                "    int var = obj._attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(false) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(2,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().contains(toUnitName("test1.B")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    private int _attr;\n\n\n"+
                     "    protected long attr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n\n"+
                     "    public int getAttr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test1;\n\n"+
                     "public class B\n"+
                     "{\n"+
                     "    public void test(test.A obj)\n"+
                     "    {\n"+
                     "        int var = obj.getAttr();\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test1.B"));
    }



    public void testPerformEncapsulate14() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "  private void initAttr(short attr) {\n"+
                "    _attr = attr;\n"+
                "  }\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(test.A obj) {\n"+
                "    obj._attr = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(false) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(2,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().contains(toUnitName("test1.B")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    private int _attr;\n\n\n"+
                     "    private void initAttr(short attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n\n"+
                     "    public void setAttr(int attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test1;\n\n"+
                     "public class B\n"+
                     "{\n"+
                     "    public void test(test.A obj)\n"+
                     "    {\n"+
                     "        obj.setAttr(1);\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test1.B"));
    }



    public void testPerformEncapsulate2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected int _attr;\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B extends test.A {\n"+
                "  public void test(int val) {\n"+
                "    _attr = val;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(false) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(2,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().contains(toUnitName("test1.B")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    private int _attr;\n\n\n"+
                     "    protected void setAttr(int attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test1;\n\n"+
                     "public class B extends test.A\n"+
                     "{\n"+
                     "    public void test(int val)\n"+
                     "    {\n"+
                     "        setAttr(val);\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test1.B"));
    }



    public void testPerformEncapsulate3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  int _attr;\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  public void test(A obj) {\n"+
                "    int var = obj._attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(false) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(2,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().contains(toUnitName("test.B")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    private int _attr;\n\n\n"+
                     "    int getAttr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test;\n\n"+
                     "public class B\n"+
                     "{\n"+
                     "    public void test(A obj)\n"+
                     "    {\n"+
                     "        int var = obj.getAttr();\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.B"));
    }



    public void testPerformEncapsulate4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  int _attr;\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  public void test(A obj) {\n"+
                "    obj._attr = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(false) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(2,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().contains(toUnitName("test.B")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    private int _attr;\n\n\n"+
                     "    void setAttr(int attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test;\n\n"+
                     "public class B\n"+
                     "{\n"+
                     "    public void test(A obj)\n"+
                     "    {\n"+
                     "        obj.setAttr(1);\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.B"));
    }



    public void testPerformEncapsulate5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(test.A obj) {\n"+
                "    int var = obj._attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(false) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(2,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().contains(toUnitName("test1.B")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    private int _attr;\n\n\n"+
                     "    public int getAttr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test1;\n\n"+
                     "public class B\n"+
                     "{\n"+
                     "    public void test(test.A obj)\n"+
                     "    {\n"+
                     "        int var = obj.getAttr();\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test1.B"));
    }



    public void testPerformEncapsulate6() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(test.A obj) {\n"+
                "    obj._attr = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(false) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(2,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().contains(toUnitName("test1.B")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    private int _attr;\n\n\n"+
                     "    public void setAttr(int attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test1;\n\n"+
                     "public class B\n"+
                     "{\n"+
                     "    public void test(test.A obj)\n"+
                     "    {\n"+
                     "        obj.setAttr(1);\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test1.B"));
    }



    public void testPerformEncapsulate7() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  private class C {\n"+
                "    public void test(test.A obj) {\n"+
                "      int var = obj._attr;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(false) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(2,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().contains(toUnitName("test1.B")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    private int _attr;\n\n\n"+
                     "    public int getAttr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test1;\n\n"+
                     "public class B\n"+
                     "{\n"+
                     "    private class C\n"+
                     "    {\n"+
                     "        public void test(test.A obj)\n"+
                     "        {\n"+
                     "            int var = obj.getAttr();\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test1.B"));
    }



    public void testPerformEncapsulate8() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  private class C {\n"+
                "    public void test(test.A obj) {\n"+
                "      obj._attr = 1;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(false) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(2,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().contains(toUnitName("test1.B")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    private int _attr;\n\n\n"+
                     "    public void setAttr(int attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test1;\n\n"+
                     "public class B\n"+
                     "{\n"+
                     "    private class C\n"+
                     "    {\n"+
                     "        public void test(test.A obj)\n"+
                     "        {\n"+
                     "            obj.setAttr(1);\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test1.B"));
    }



    public void testPerformEncapsulate9() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public void test(final test.A obj) {\n"+
                "    class Local {\n"+
                "      int var = obj._attr;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(false) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertEquals(2,
                     getChangedUnits().getCount());
        assertTrue(getChangedUnits().contains(toUnitName("test.A")));
        assertTrue(getChangedUnits().contains(toUnitName("test1.B")));

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    private int _attr;\n\n\n"+
                     "    public int getAttr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
        assertEquals("package test1;\n\n"+
                     "public class B\n"+
                     "{\n"+
                     "    public void test(final test.A obj)\n"+
                     "    {\n"+
                     "        class Local\n"+
                     "        {\n"+
                     "            int var = obj.getAttr();\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test1.B"));
    }



    public void testPerformSelfEncapsulate1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
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
                     "    private int _attr;\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerformSelfEncapsulate10() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected int _attr;\n"+
                "  public void test() {\n"+
                "    ++_attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
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
                     "    private int _attr;\n\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        setAttr(getAttr() + 1);\n"+
                     "    }\n\n"+
                     "    protected int getAttr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n\n"+
                     "    protected void setAttr(int attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerformSelfEncapsulate11() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "  public void test() {\n"+
                "    ++this._attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
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
                     "    private int _attr;\n\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        this.setAttr(this.getAttr() + 1);\n"+
                     "    }\n\n"+
                     "    public int getAttr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n\n"+
                     "    public void setAttr(int attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerformSelfEncapsulate12() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "  public void test() {\n"+
                "    _attr++;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
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
                     "    private int _attr;\n\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        setAttr(getAttr() + 1);\n"+
                     "    }\n\n"+
                     "    public int getAttr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n\n"+
                     "    public void setAttr(int attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerformSelfEncapsulate13() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "  public void test() {\n"+
                "    this._attr++;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
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
                     "    private int _attr;\n\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        this.setAttr(this.getAttr() + 1);\n"+
                     "    }\n\n"+
                     "    public int getAttr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n\n"+
                     "    public void setAttr(int attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerformSelfEncapsulate14() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "  public void test() {\n"+
                "    --_attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
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
                     "    private int _attr;\n\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        setAttr(getAttr() - 1);\n"+
                     "    }\n\n"+
                     "    public int getAttr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n\n"+
                     "    public void setAttr(int attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerformSelfEncapsulate15() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "  public void test() {\n"+
                "    --this._attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
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
                     "    private int _attr;\n\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        this.setAttr(this.getAttr() - 1);\n"+
                     "    }\n\n"+
                     "    public int getAttr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n\n"+
                     "    public void setAttr(int attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerformSelfEncapsulate16() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "  public void test() {\n"+
                "    _attr--;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
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
                     "    private int _attr;\n\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        setAttr(getAttr() - 1);\n"+
                     "    }\n\n"+
                     "    public int getAttr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n\n"+
                     "    public void setAttr(int attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerformSelfEncapsulate17() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "  public void test() {\n"+
                "    this._attr--;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
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
                     "    private int _attr;\n\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        this.setAttr(this.getAttr() - 1);\n"+
                     "    }\n\n"+
                     "    public int getAttr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n\n"+
                     "    public void setAttr(int attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerformSelfEncapsulate18() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  private class B {\n"+
                "    public void test() {\n"+
                "      int val = _attr;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
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
                     "    private class B\n"+
                     "    {\n"+
                     "        public void test()\n"+
                     "        {\n"+
                     "            int val = getAttr();\n"+
                     "        }\n"+
                     "    }\n\n\n"+
                     "    private int _attr;\n\n\n"+
                     "    private int getAttr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerformSelfEncapsulate19() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  private class B {\n"+
                "    public void test() {\n"+
                "      _attr = 0;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
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
                     "    private class B\n"+
                     "    {\n"+
                     "        public void test()\n"+
                     "        {\n"+
                     "            setAttr(0);\n"+
                     "        }\n"+
                     "    }\n\n\n"+
                     "    private int _attr;\n\n\n"+
                     "    private void setAttr(int attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerformSelfEncapsulate2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  public int attr() {\n"+
                "    return _attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertTrue(getChangedUnits().isEmpty());

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    private int _attr;\n\n\n"+
                     "    public int attr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerformSelfEncapsulate20() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  private void test() {\n"+
                "    class Local {\n"+
                "      int val = _attr;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
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
                     "    private int _attr;\n\n\n"+
                     "    private void test()\n"+
                     "    {\n"+
                     "        class Local\n"+
                     "        {\n"+
                     "            int val = getAttr();\n"+
                     "        }\n"+
                     "    }\n\n"+
                     "    private int getAttr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerformSelfEncapsulate21() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  private void test() {\n"+
                "    class Local {\n"+
                "      private void doSomething() {\n"+
                "        _attr = 0;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
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
                     "    private int _attr;\n\n\n"+
                     "    private void test()\n"+
                     "    {\n"+
                     "        class Local\n"+
                     "        {\n"+
                     "            private void doSomething()\n"+
                     "            {\n"+
                     "                setAttr(0);\n"+
                     "            }\n"+
                     "        }\n"+
                     "    }\n\n"+
                     "    private void setAttr(int attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerformSelfEncapsulate22() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  private int attr() {\n"+
                "    return _attr;\n"+
                "  }\n"+
                "  public void test() {\n"+
                "    int val = _attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
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
                     "    private int _attr;\n\n\n"+
                     "    private int attr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        int val = attr();\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerformSelfEncapsulate23() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private static int _attr;\n"+
                "  public void initAttr(int val) {\n"+
                "    _attr = val;\n"+
                "  }\n"+
                "  public void test() {\n"+
                "    _attr = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
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
                     "    private static int _attr;\n\n\n"+
                     "    public static void initAttr(int val)\n"+
                     "    {\n"+
                     "        _attr = val;\n"+
                     "    }\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        initAttr(1);\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerformSelfEncapsulate3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  public void initAttr(long attr) {\n"+
                "    _attr = (int)(attr);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
        assertTrue(analyze());
        assertTrue(perform());

        assertTrue(getNewPackages().isEmpty());
        assertTrue(getRemovedPackages().isEmpty());
        assertTrue(getNewUnits().isEmpty());
        assertTrue(getRemovedUnits().isEmpty());
        assertTrue(getChangedUnits().isEmpty());

        assertEquals("package test;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    private int _attr;\n\n\n"+
                     "    public void initAttr(long attr)\n"+
                     "    {\n"+
                     "        _attr = (int)(attr);\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerformSelfEncapsulate4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "  public void test() {\n"+
                "    int var = _attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
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
                     "    private int _attr;\n\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        int var = getAttr();\n"+
                     "    }\n\n"+
                     "    public int getAttr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerformSelfEncapsulate5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "  public void test() {\n"+
                "    int var = this._attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
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
                     "    private int _attr;\n\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        int var = this.getAttr();\n"+
                     "    }\n\n"+
                     "    public int getAttr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerformSelfEncapsulate6() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "  public void test() {\n"+
                "    _attr = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
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
                     "    private int _attr;\n\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        setAttr(1);\n"+
                     "    }\n\n"+
                     "    public void setAttr(int attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerformSelfEncapsulate7() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "  public void test(A obj) {\n"+
                "    obj._attr = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
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
                     "    private int _attr;\n\n\n"+
                     "    public void test(A obj)\n"+
                     "    {\n"+
                     "        obj.setAttr(1);\n"+
                     "    }\n\n"+
                     "    public void setAttr(int attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerformSelfEncapsulate8() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "  public void test() {\n"+
                "    _attr += 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
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
                     "    private int _attr;\n\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        setAttr(getAttr() + 1);\n"+
                     "    }\n\n"+
                     "    public int getAttr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n\n"+
                     "    public void setAttr(int attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerformSelfEncapsulate9() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "  public void test() {\n"+
                "    this._attr *= 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A").getFields().get(0) };
        Object[] obj   = { new Boolean(true) };

        assertTrue(initialize(nodes, obj));
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
                     "    private int _attr;\n\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        this.setAttr(this.getAttr() * 1);\n"+
                     "    }\n\n"+
                     "    public int getAttr()\n"+
                     "    {\n"+
                     "        return _attr;\n"+
                     "    }\n\n"+
                     "    public void setAttr(int attr)\n"+
                     "    {\n"+
                     "        _attr = attr;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }
}
