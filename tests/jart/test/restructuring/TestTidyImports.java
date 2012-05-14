package jart.test.restructuring;
import jart.restructuring.tidyimports.TidyImports;
import jast.ParseException;
import jast.ast.ASTHelper;
import jast.ast.Node;

public class TestTidyImports extends TestBase
{

    public TestTidyImports(String name)
    {
        super(name);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();
        setRestructuring(new TidyImports());
    }

    public void testDefaultPackage1() throws ParseException
    {
        addType("B",
                "public class B {}\n",
                true);
        addType("C",
                "public class C {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "import B;\n"+
                "import C;\n"+
                "public class A extends B {\n"+
                "  private C attr;\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(1) };

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
                     "import B;\n"+
                     "import C;\n\n"+
                     "public class A extends B\n"+
                     "{\n"+
                     "    private C attr;\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testDefaultPackage2() throws ParseException
    {
        addType("B",
                "public class B {}\n",
                true);
        addType("test1.B",
                "package test1;\n"+
                "public class B {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "import B;\n"+
                "public class A extends B {\n"+
                "  private test1.B attr;\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(1) };

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
                     "import B;\n\n"+
                     "public class A extends B\n"+
                     "{\n"+
                     "    private test1.B attr;\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testInner1() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    test.A.B obj = null;\n"+
                "  }\n"+
                "  private class B {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "    private class B\n"+
                     "    {\n"+
                     "    }\n\n\n"+
                     "    private void doSomething()\n"+
                     "    {\n"+
                     "        B obj = null;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testInner2() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    class B {}\n"+
                "    test.A.B obj = null;\n"+
                "  }\n"+
                "  private class B {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "    private class B\n"+
                     "    {\n"+
                     "    }\n\n\n"+
                     "    private void doSomething()\n"+
                     "    {\n"+
                     "        class B\n"+
                     "        {\n"+
                     "        }\n\n\n"+
                     "        A.B obj = null;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testInner3() throws ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  class C {}\n"+
                "}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A extends B {\n"+
                "  private void doSomething() {\n"+
                "    A.C obj = null;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "public class A extends B\n"+
                     "{\n"+
                     "    private void doSomething()\n"+
                     "    {\n"+
                     "        C obj = null;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testInner4() throws ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  class C {}\n"+
                "}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A extends B {\n"+
                "  private void doSomething() {\n"+
                "    class C {}\n"+
                "    test.B.C obj = null;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "public class A extends B\n"+
                     "{\n"+
                     "    private void doSomething()\n"+
                     "    {\n"+
                     "        class C\n"+
                     "        {\n"+
                     "        }\n\n\n"+
                     "        B.C obj = null;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testInner5() throws ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  class C {}\n"+
                "}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A extends B {\n"+
                "  private class C {}\n"+
                "  private void doSomething() {\n"+
                "    test.B.C obj = null;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "public class A extends B\n"+
                     "{\n"+
                     "    private class C\n"+
                     "    {\n"+
                     "    }\n\n\n"+
                     "    private void doSomething()\n"+
                     "    {\n"+
                     "        B.C obj = null;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testJavaLang1() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private String attr;\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "    private String attr;\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testJavaLang2() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A extends java.lang.Object {\n"+
                "  private java.lang.String attr;\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(1) };

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
                     "    private String attr;\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testJavaLang3() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    class Object {}\n"+
                "    java.lang.Object obj = null;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "    private void doSomething()\n"+
                     "    {\n"+
                     "        class Object\n"+
                     "        {\n"+
                     "        }\n\n\n"+
                     "        java.lang.Object obj = null;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testJavaLang4() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class Object {}\n"+
                "  private void doSomething(java.lang.Object obj) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "    private class Object\n"+
                     "    {\n"+
                     "    }\n\n\n"+
                     "    private void doSomething(java.lang.Object obj)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testJavaLang5() throws ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  protected class Object {}\n"+
                "  private class String {}\n"+
                "}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A extends test1.B {\n"+
                "  private String attr;\n"+
                "  private void doSomething(java.lang.Object obj) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "import test1.B;\n\n"+
                     "public class A extends B\n"+
                     "{\n"+
                     "    private String attr;\n\n\n"+
                     "    private void doSomething(java.lang.Object obj)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testJavaLang6() throws ParseException
    {
        addType("test1.Object",
                "package test1;\n"+
                "public class Object {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A extends test1.Object {\n"+
                "  private void doSomething(Object obj) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "import java.lang.Object;\n\n"+
                     "public class A extends test1.Object\n"+
                     "{\n"+
                     "    private void doSomething(Object obj)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testJavaLang7() throws ParseException
    {
        addType("Object",
                "public class Object {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "import Object;\n"+
                "public class A extends Object {\n"+
                "  private void doSomething(java.lang.Object obj) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "import Object;\n\n"+
                     "public class A extends Object\n"+
                     "{\n"+
                     "    private void doSomething(java.lang.Object obj)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testJavaLang8() throws ParseException
    {
        addType("test.Object",
                "package test;\n"+
                "public class Object {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A extends test.Object {\n"+
                "  private void doSomething(java.lang.Object obj) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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

        // Note that the type from the same package has precedence over the
        // type from java.lang
        assertEquals("package test;\n\n"+
                     "public class A extends Object\n"+
                     "{\n"+
                     "    private void doSomething(java.lang.Object obj)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testJavaLang9() throws ParseException
    {
        addType("test.other.Object",
                "package test.other;\n"+
                "public class Object {}\n",
                true);
        addType("test.other.B",
                "package test.other;\n"+
                "public class B {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A extends test.other.Object {\n"+
                "  private test.other.B attr;\n"+
                "  private void doSomething(java.lang.Object obj) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(1) };

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
                     "import test.other.*;\n"+
                     "import test.other.Object;\n\n"+
                     "public class A extends Object\n"+
                     "{\n"+
                     "    private B attr;\n\n\n"+
                     "    private void doSomething(java.lang.Object obj)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testNested1() throws ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public static class C {}\n"+
                "}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private test1.B.C attr;\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "import test1.B;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    private B.C attr;\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testNested2() throws ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  public static class C {}\n"+
                "}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private test.B.C attr;\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "    private B.C attr;\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testOtherPackage1() throws ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A extends test1.B {}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "import test1.B;\n\n"+
                     "public class A extends B\n"+
                     "{\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testOtherPackage10() throws ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private test1.A attr;\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "    private test1.A attr;\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testOtherPackage11() throws ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A extends test1.B {}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "public class A extends test1.B\n"+
                     "{\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testOtherPackage2() throws ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {}\n",
                true);
        addType("test1.C",
                "package test1;\n"+
                "public class C {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A extends test1.B {\n"+
                "  private test1.C attr;\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(1) };

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
                     "import test1.*;\n\n"+
                     "public class A extends B\n"+
                     "{\n"+
                     "    private C attr;\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testOtherPackage3() throws ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {}\n",
                true);
        addType("test2.C",
                "package test2;\n"+
                "public class C {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A extends test1.B {\n"+
                "  private void doSomething(test2.C obj) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "import test1.B;\n"+
                     "import test2.C;\n\n"+
                     "public class A extends B\n"+
                     "{\n"+
                     "    private void doSomething(C obj)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testOtherPackage4() throws ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {}\n",
                true);
        addType("test12.B",
                "package test12;\n"+
                "public class B {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A extends test1.B {\n"+
                "  private void doSomething(test12.B obj) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "import test12.B;\n\n"+
                     "public class A extends test1.B\n"+
                     "{\n"+
                     "    private void doSomething(B obj)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testOtherPackage6() throws ParseException
    {
        addType("test12.B",
                "package test12;\n"+
                "public class B {}\n",
                true);
        addType("test12.C",
                "package test12;\n"+
                "public class C {}\n",
                true);
        addType("test2.C",
                "package test2;\n"+
                "public class C {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A extends test12.B {\n"+
                "  private test12.C attr;\n"+
                "  private void doSomething(test2.C obj) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(1) };

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
                     "import test12.*;\n"+
                     "import test12.C;\n\n"+
                     "public class A extends B\n"+
                     "{\n"+
                     "    private C attr;\n\n\n"+
                     "    private void doSomething(test2.C obj)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testOtherPackage7() throws ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    class B {}\n"+
                "    test1.B obj = null;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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

        // note that an import is generated even though the type is
        // only used qualified
        // this is because the restructuring does not check against
        // local types (the qualified usage is created by the
        // prettyprinter) as it is assumed that the used type is used
        // more than once (iow, also outside of the block scope
        // containing the local class)
        assertEquals("package test;\n\n"+
                     "import test1.B;\n\n"+
                     "public class A\n"+
                     "{\n"+
                     "    private void doSomething()\n"+
                     "    {\n"+
                     "        class B\n"+
                     "        {\n"+
                     "        }\n\n\n"+
                     "        test1.B obj = null;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testOtherPackage8() throws ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething(test1.B obj) {}\n"+
                "  private class B {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "    private class B\n"+
                     "    {\n"+
                     "    }\n\n\n"+
                     "    private void doSomething(test1.B obj)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testOtherPackage9() throws ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  protected class C {}\n"+
                "}\n",
                true);
        addType("test2.C",
                "package test2;\n"+
                "public class C {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A extends test1.B {\n"+
                "  private void doSomething(test2.C obj) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "import test1.B;\n\n"+
                     "public class A extends B\n"+
                     "{\n"+
                     "    private void doSomething(test2.C obj)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testSamePackage1() throws ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {}\n",
                true);
        addType("test.C",
                "package test;\n"+
                "public class C {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A extends B {\n"+
                "  private test.C attr;\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(1) };

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
                     "public class A extends B\n"+
                     "{\n"+
                     "    private C attr;\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testSamePackage2() throws ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    class B {}\n"+
                "    test.B obj = null;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "    private void doSomething()\n"+
                     "    {\n"+
                     "        class B\n"+
                     "        {\n"+
                     "        }\n\n\n"+
                     "        test.B obj = null;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testSamePackage3() throws ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething(test.B obj) {}\n"+
                "  private class B {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "    private class B\n"+
                     "    {\n"+
                     "    }\n\n\n"+
                     "    private void doSomething(test.B obj)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testSamePackage4() throws ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  protected class C {}\n"+
                "}\n",
                true);
        addType("test.C",
                "package test;\n"+
                "public class C {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A extends test1.B {\n"+
                "  private void doSomething(test.C obj) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "import test1.B;\n\n"+
                     "public class A extends B\n"+
                     "{\n"+
                     "    private void doSomething(test.C obj)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testSamePackage5() throws ParseException
    {
        addType("B",
                "public class B {}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "import B;\n"+
                "public class A extends B {\n"+
                "  private test.B attr;\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(1) };

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
                     "import B;\n\n"+
                     "public class A extends B\n"+
                     "{\n"+
                     "    private test.B attr;\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testSimple1() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {}\n",
                true);
        resolve();

        Node[]   nodes = { ASTHelper.getCompilationUnitOf(_project.getType("test.A")) };
        Object[] args  = { new Integer(5) };

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
                     "}\n",
                     prettyPrint("test.A"));
    }
}
