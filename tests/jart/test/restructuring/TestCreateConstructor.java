package jart.test.restructuring;
import jart.restructuring.methodlevel.CreateConstructor;
import jast.Global;
import jast.ParseException;
import jast.ast.Node;
import jast.ast.nodes.Block;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.PrimitiveType;
import jast.ast.nodes.TypeDeclaration;
import antlr.ANTLRException;

public class TestCreateConstructor extends TestBase
{

    public TestCreateConstructor(String name)
    {
        super(name);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();
        setRestructuring(new CreateConstructor());
    }

    public void testAnalyze1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public A() {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0),
                           "arg" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze10() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private String _attr;\n"+
                "  public A(Object arg) {}\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Node[]          nodes    = { typeDecl };
        Object[]        args     = { typeDecl.getFields().get(0),
                                     "attr" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze11() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public A(String arg) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.SHORT_TYPE, 1),
                           "args" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze12() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private Object[][] _attrs;\n"+
                "  public A(Object arg) {}\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Node[]          nodes    = { typeDecl };
        Object[]        args     = { typeDecl.getFields().get(0),
                                     "attrs" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze13() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public A(int[] args) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.INT_TYPE, 2),
                           "args" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze14() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private Object[] _attrs;\n"+
                "  public A(long[] arg) {}\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Node[]          nodes    = { typeDecl };
        Object[]        args     = { typeDecl.getFields().get(0),
                                     "attrs" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze15() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public A(String[] args) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType("Object", 1),
                           "args" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public A(String arg) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType("String", false),
                           "arg1",
                           Global.getFactory().createType("Object", false),
                           "arg2" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private boolean _attr;\n"+
                "  public A(double arg) {}\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Node[]          nodes    = { typeDecl };
        Object[]        args     = { typeDecl.getFields().get(0),
                                     "attr" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private String _attr;\n"+
                "  public A(double arg) {}\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Node[]          nodes    = { typeDecl };
        Object[]        args     = { typeDecl.getFields().get(0),
                                     "attr" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public A(int arg) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.INT_TYPE, 1),
                           "args" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze6() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private String[] _attrs;\n"+
                "  public A(double arg) {}\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Node[]          nodes    = { typeDecl };
        Object[]        args     = { typeDecl.getFields().get(0),
                                     "attrs" };

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
                "  public A(int arg1, float arg2) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.INT_TYPE, 0),
                           "argA",
                           Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                           "argB" };

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
                "  private String _attrs;\n"+
                "  public A(String arg) {}\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Node[]          nodes    = { typeDecl };
        Object[]        args     = { typeDecl.getFields().get(0),
                                     "attrs" };

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
                "  public A(int arg1) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                           "argA" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testInitialize1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { };

        assertTrue(!initialize(nodes, args));
    }

    public void testInitialize2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                " private void test() {\n"+
                "   new Object() {};\n"+
                " }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration     typeDecl = _project.getType("test.A");
        Block               body     = typeDecl.getMethods().get(0).getBody();
        ExpressionStatement exprStmt = (ExpressionStatement)body.getBlockStatements().get(0);
        Instantiation       expr     = (Instantiation)exprStmt.getExpression();
        Node[]              nodes    = { expr.getAnonymousClass() };
        Object[]            args     = { };

        assertTrue(!initialize(nodes, args));
    }

    public void testPerform1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { };

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
                     "    public A()\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform10() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private int attr;\n"+
                "    private B(String arg) {}\n"+
                "    private B(int arg) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A.B");
        Node[]          nodes    = { typeDecl };
        Object[]        args     = { Global.getFactory().createType("Object", 0),
                                     "arg1",
                                     typeDecl.getFields().get(0),
                                     "attr" };

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
                     "        private int attr;\n\n\n"+
                     "        private B(String arg)\n"+
                     "        {}\n\n"+
                     "        private B(int arg)\n"+
                     "        {}\n\n"+
                     "        public B(Object arg1, int attr)\n"+
                     "        {\n"+
                     "            this.attr = attr;\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform11() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test() {\n"+
                "    class Local {\n"+
                "      private int value;\n"+
                "      private Object obj;\n"+
                "      Local(String arg) {}\n"+
                "      Local(int arg) {}\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl  = _project.getType("test.A");
        Block           body      = typeDecl.getMethods().get(0).getBody();
        TypeDeclaration localDecl = (ClassDeclaration)body.getBlockStatements().get(0);
        Node[]          nodes     = { localDecl };
        Object[]        args      = { localDecl.getFields().get(1),
                                      "obj",
                                      localDecl.getFields().get(0),
                                      "value" };

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
                     "    public void test()\n"+
                     "    {\n"+
                     "        class Local\n"+
                     "        {\n"+
                     "            private int value;\n\n"+
                     "            private Object obj;\n\n\n"+
                     "            Local(String arg)\n"+
                     "            {}\n\n"+
                     "            Local(int arg)\n"+
                     "            {}\n\n"+
                     "            public Local(Object obj, int value)\n"+
                     "            {\n"+
                     "                this.obj   = obj;\n"+
                     "                this.value = value;\n"+
                     "            }\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { };

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
                     "        public B()\n"+
                     "        {}\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test() {\n"+
                "    class Local {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Block           body     = typeDecl.getMethods().get(0).getBody();
        Node[]          nodes    = { (Node)body.getBlockStatements().get(0) };
        Object[]        args     = { };

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
                     "    public void test()\n"+
                     "    {\n"+
                     "        class Local\n"+
                     "        {\n"+
                     "            public Local()\n"+
                     "            {}\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0),
                           "arg" };

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
                     "    public A()\n"+
                     "    {}\n\n"+
                     "    public A(double arg)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private String _attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A.B");
        Node[]          nodes    = { typeDecl };
        Object[]        args     = { typeDecl.getFields().get(0),
                                     "attr" };

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
                     "        private String _attr;\n\n\n"+
                     "        public B()\n"+
                     "        {}\n\n"+
                     "        public B(String attr)\n"+
                     "        {\n"+
                     "            _attr = attr;\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform6() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private A(String arg) {}\n"+
                "  private A(int arg) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { };

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
                     "    private A(String arg)\n"+
                     "    {}\n\n"+
                     "    private A(int arg)\n"+
                     "    {}\n\n"+
                     "    public A()\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform7() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private B(String arg) {}\n"+
                "    private B(int arg) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { };

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
                     "        private B(String arg)\n"+
                     "        {}\n\n"+
                     "        private B(int arg)\n"+
                     "        {}\n\n"+
                     "        public B()\n"+
                     "        {}\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform8() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void test() {\n"+
                "    class Local {\n"+
                "      Local(String arg) {}\n"+
                "      Local(int arg) {}\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Block           body     = typeDecl.getMethods().get(0).getBody();
        Node[]          nodes    = { (Node)body.getBlockStatements().get(0) };
        Object[]        args     = { };

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
                     "    public void test()\n"+
                     "    {\n"+
                     "        class Local\n"+
                     "        {\n"+
                     "            Local(String arg)\n"+
                     "            {}\n\n"+
                     "            Local(int arg)\n"+
                     "            {}\n\n"+
                     "            public Local()\n"+
                     "            {}\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform9() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private A(String arg) {}\n"+
                "  private A(int arg) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0),
                           "arg1",
                           Global.getFactory().createType("String", 0),
                           "arg2" };

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
                     "    private A(String arg)\n"+
                     "    {}\n\n"+
                     "    private A(int arg)\n"+
                     "    {}\n\n"+
                     "    public A(double arg1, String arg2)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }
}
