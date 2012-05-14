package jart.test.restructuring;
import jart.restructuring.fieldlevel.CreateField;
import jast.Global;
import jast.ParseException;
import jast.ast.Node;
import jast.ast.nodes.Block;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.PrimitiveType;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.UnaryExpression;
import antlr.ANTLRException;

public class TestCreateField extends TestBase
{

    public TestCreateField(String name)
    {
        super(name);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();
        setRestructuring(new CreateField());
    }

    public void testAnalyze1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType("String", false),
                           "_attr" };

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze10() throws ANTLRException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  protected int _attr = 0;\n"+
                "}\n",
                true);
        addType("test1.A",
                "package test1;\n"+
                "public class A extends test2.B {\n"+
                "  private void test() {\n"+
                "    int val = _attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test1.A") };
        Object[] args  = { Global.getFactory().createType("double", false),
                           "_attr" };

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze11() throws ANTLRException
    {
        addType("test2.B",
                "package test2;\n"+
                "public interface B {\n"+
                "  public int _attr = 0;\n"+
                "}\n",
                true);
        addType("test1.A",
                "package test1;\n"+
                "public interface A extends test2.B {\n"+
                "  public int _val = _attr;\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test1.A") };
        Object[] args  = { Global.getFactory().createType("double", false),
                           "_attr" };

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze12() throws ANTLRException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  protected int _attr = 0;\n"+
                "}\n",
                true);
        addType("test1.A",
                "package test1;\n"+
                "public class A extends test2.B {\n"+
                "}\n",
                true);
        addType("test3.C",
                "package test3;\n"+
                "public class C extends test1.A {\n"+
                "  private void test() {\n"+
                "    int val = _attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test1.A") };
        Object[] args  = { Global.getFactory().createType("double", false),
                           "_attr" };

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze13() throws ANTLRException
    {
        addType("test2.B",
                "package test2;\n"+
                "public interface B {\n"+
                "  public int _attr = 0;\n"+
                "}\n",
                true);
        addType("test1.A",
                "package test1;\n"+
                "public interface A extends test2.B {\n"+
                "}\n",
                true);
        addType("test3.C",
                "package test3;\n"+
                "public class C implements test1.A {\n"+
                "  private int _val = _attr;\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test1.A") };
        Object[] args  = { Global.getFactory().createType("double", false),
                           "_attr" };

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze14() throws ANTLRException
    {
        addType("test2.B",
                "package test2;\n"+
                "public interface B {\n"+
                "  public int _attr = 0;\n"+
                "}\n",
                true);
        addType("test1.A",
                "package test1;\n"+
                "public class A implements test2.B {\n"+
                "}\n",
                true);
        addType("test3.C",
                "package test3;\n"+
                "public class C extends test1.A {\n"+
                "  private void test() {\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test1.A") };
        Object[] args  = { Global.getFactory().createType("double", false),
                           "_attr" };

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyze15() throws ANTLRException
    {
        addType("test2.B",
                "package test2;\n"+
                "public interface B {\n"+
                "  public int _attr = 0;\n"+
                "}\n",
                true);
        addType("test1.A",
                "package test1;\n"+
                "public interface A extends test2.B {\n"+
                "}\n",
                true);
        addType("test3.C",
                "package test3;\n"+
                "public class C implements test1.A {\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test1.A") };
        Object[] args  = { Global.getFactory().createType("double", false),
                           "_attr" };

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyze16() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private String _attr;\n"+
                "  private class B {\n"+
                "    private String _val = _attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.BOOLEAN_TYPE, 1),
                           "_attr" };

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze17() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public String _attr;\n"+
                "  public interface B {\n"+
                "    public String _val = _attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createType("String", false),
                           "_attr" };

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze18() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private String _attr;\n"+
                "  private class B {\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.SHORT_TYPE, 0),
                           "_attr" };

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyze19() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public String _attr;\n"+
                "  public interface B {\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createType("Object", false),
                           "_attr" };

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyze2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType("String", false),
                           "_attr" };

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze20() throws ANTLRException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  protected String _attr;\n"+
                "}\n",
                true);
        addType("test1.A",
                "package test1;\n"+
                "public class A extends test2.B {\n"+
                "  private class C {\n"+
                "    private String _val = _attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test1.A.C") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.BOOLEAN_TYPE, 0),
                           "_attr" };

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze21() throws ANTLRException
    {
        addType("test2.B",
                "package test2;\n"+
                "public interface B {\n"+
                "  public int _attr = 0;\n"+
                "}\n",
                true);
        addType("test1.A",
                "package test1;\n"+
                "public class A implements test2.B {\n"+
                "  private class C {\n"+
                "    private long _val = _attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test1.A.C") };
        Object[] args  = { Global.getFactory().createType("String", false),
                           "_attr" };

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze22() throws ANTLRException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  protected String _attr;\n"+
                "}\n",
                true);
        addType("test1.A",
                "package test1;\n"+
                "public class A extends test2.B {\n"+
                "  private class C {\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test1.A.C") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.BOOLEAN_TYPE, 0),
                           "_attr" };

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyze23() throws ANTLRException
    {
        addType("test2.B",
                "package test2;\n"+
                "public interface B {\n"+
                "  public int _attr = 0;\n"+
                "}\n",
                true);
        addType("test1.A",
                "package test1;\n"+
                "public class A implements test2.B {\n"+
                "  private class C {\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test1.A.C") };
        Object[] args  = { Global.getFactory().createType("String", false),
                           "_attr" };

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyze24() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void test() {\n"+
                "    class Local {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Block           block    = typeDecl.getMethods().get(0).getBody();
        Node[]          nodes    = { (ClassDeclaration)block.getBlockStatements().get(0) };
        Object[]        args     = { Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0),
                                     "_attr" };

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze25() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void test() {\n"+
                "    class Local {\n"+
                "      private String _attr;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Block           block    = typeDecl.getMethods().get(0).getBody();
        Node[]          nodes    = { (ClassDeclaration)block.getBlockStatements().get(0) };
        Object[]        args     = { Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0),
                                     "_attr" };

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze26() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void test(int attr) {\n"+
                "    class Local {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Block           block    = typeDecl.getMethods().get(0).getBody();
        Node[]          nodes    = { (ClassDeclaration)block.getBlockStatements().get(0) };
        Object[]        args     = { Global.getFactory().createType("String", false),
                                     "attr" };

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyze27() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void test(final int attr) {\n"+
                "    class Local {\n"+
                "      private int _val = attr;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Block           block    = typeDecl.getMethods().get(0).getBody();
        Node[]          nodes    = { (ClassDeclaration)block.getBlockStatements().get(0) };
        Object[]        args     = { Global.getFactory().createType("String", false),
                                     "attr" };

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze28() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void test() {\n"+
                "    int attr = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0),
                           "attr" };

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyze3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType("String", false),
                           "_attr" };

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public int _attr = 0;\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType("String", false),
                           "_attr" };

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze5() throws ANTLRException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "}\n",
                true);
        addType("test2.B",
                "package test2;\n"+
                "public class B extends test1.A {\n"+
                "  private int _attr;\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test1.A") };
        Object[] args  = { Global.getFactory().createType("double", false),
                           "_attr" };

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyze6() throws ANTLRException
    {
        addType("test1.A",
                "package test1;\n"+
                "public interface A {\n"+
                "}\n",
                true);
        addType("test2.B",
                "package test2;\n"+
                "public class B implements test1.A {\n"+
                "  private int _attr;\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test1.A") };
        Object[] args  = { Global.getFactory().createType("double", false),
                           "_attr" };

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyze7() throws ANTLRException
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

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.BOOLEAN_TYPE, 1),
                           "_attr" };

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyze8() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public interface B {\n"+
                "    public String _attr = \"\";\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0),
                           "_attr" };

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyze9() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void test() {\n"+
                "    class Local {\n"+
                "      private String _attr;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.CHAR_TYPE, 0),
                           "_attr" };

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testPerform1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType("String", false),
                           "_attr" };

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
                     "    private String _attr = null;\n\n\n"+
                     "    public void doSomething(int arg)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform10() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {}\n"+
                "  private static int getValue() {\n"+
                "    return 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Node[]          nodes    = { typeDecl.getInnerTypes().get("B") };
        Object[]        args     = { Global.getFactory().createType(PrimitiveType.LONG_TYPE, 0),
                                     "_attr",
                                     Global.getFactory().createMethodInvocation(
                                         typeDecl.getMethods().get(0),
                                         null) };

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
                     "        private long _attr = getValue();\n"+
                     "    }\n\n\n"+
                     "    private static int getValue()\n"+
                     "    {\n"+
                     "        return 0;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform11() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  private interface B {\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.BOOLEAN_TYPE, 0),
                           "CONSTANT" };

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
                     "public interface A\n"+
                     "{\n"+
                     "    private interface B\n"+
                     "    {\n"+
                     "        boolean CONSTANT = false;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform12() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  private interface B {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.SHORT_TYPE, 0),
                           "CONSTANT",
                           Global.getFactory().createIntegerLiteral("-2") };

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
                     "public interface A\n"+
                     "{\n"+
                     "    private interface B\n"+
                     "    {\n"+
                     "        short CONSTANT = -2;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform13() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected class B {\n"+
                "    private boolean _isCorrect = false;\n"+
                "    private Object _val = null;\n"+
                "  }\n"+
                "  public void doSomething(int arg) {\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createType("String", false),
                           "_attr" };

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
                     "    protected class B\n"+
                     "    {\n"+
                     "        private boolean _isCorrect = false;\n\n"+
                     "        private Object _val = null;\n\n"+
                     "        private String _attr = null;\n"+
                     "    }\n\n\n"+
                     "    public void doSomething(int arg)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform14() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    private boolean _isCorrect = false;\n"+
                "    private short _val = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A.B");
        Node[]          nodes    = { typeDecl };
        Object[]        args     = { Global.getFactory().createType(PrimitiveType.LONG_TYPE, 0),
                                     "_attr",
                                     Global.getFactory().createFieldAccess(typeDecl.getFields().get(1)) };

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
                     "    public class B\n"+
                     "    {\n"+
                     "        private boolean _isCorrect = false;\n\n"+
                     "        private short _val = 0;\n\n"+
                     "        private long _attr = _val;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform15() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public class B {\n"+
                "    public int INT_CONSTANT = -1;\n"+
                "    public double DBL_CONSTANT = 10.0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createType("Object", false),
                           "CONSTANT" };

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
                     "public interface A\n"+
                     "{\n"+
                     "    public class B\n"+
                     "    {\n"+
                     "        public int INT_CONSTANT = -1;\n\n"+
                     "        public double DBL_CONSTANT = 10.0;\n\n"+
                     "        private Object CONSTANT = null;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform16() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public interface B {\n"+
                "    public static final int INT_CONSTANT = -1;\n"+
                "    public static final double DBL_CONSTANT = 10.0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A.B");
        Node[]          nodes    = { typeDecl };
        Object[]        args     = { Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0),
                                     "CONSTANT",
                                     Global.getFactory().createFieldAccess(
                                         typeDecl.getFields().get(0)) };

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
                     "public interface A\n"+
                     "{\n"+
                     "    public interface B\n"+
                     "    {\n"+
                     "        int INT_CONSTANT = -1;\n\n"+
                     "        double DBL_CONSTANT = 10.0;\n\n"+
                     "        double CONSTANT = INT_CONSTANT;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform17() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething(int arg) {\n"+
                "    class Local {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration method = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes  = { (Node)method.getBody().getBlockStatements().get(0) };
        Object[]          args   = { Global.getFactory().createType(PrimitiveType.INT_TYPE, 1),
                                     "_attr" };

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
                     "    private void doSomething(int arg)\n"+
                     "    {\n"+
                     "        class Local\n"+
                     "        {\n"+
                     "            private int[] _attr = null;\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform18() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething(final int arg) {\n"+
                "    class Local {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration method = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes  = { (Node)method.getBody().getBlockStatements().get(0) };
        Object[]          args   = { Global.getFactory().createType(PrimitiveType.LONG_TYPE, 0),
                                     "_attr",
                                     Global.getFactory().createVariableAccess(
                                         method.getParameterList().getParameters().get(0))
                                     };

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
                     "    private void doSomething(final int arg)\n"+
                     "    {\n"+
                     "        class Local\n"+
                     "        {\n"+
                     "            private long _attr = arg;\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform19() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething(int arg) {\n"+
                "    class Local {\n"+
                "      private boolean _isCorrect = false;\n"+
                "      private Object _val = null;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration method = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes  = { (Node)method.getBody().getBlockStatements().get(0) };
        Object[]          args   = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 1),
                                     "_attr" };

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
                     "    private void doSomething(int arg)\n"+
                     "    {\n"+
                     "        class Local\n"+
                     "        {\n"+
                     "            private boolean _isCorrect = false;\n\n"+
                     "            private Object _val = null;\n\n"+
                     "            private float[] _attr = null;\n"+
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
                "  public static int getValue() {\n"+
                "    return 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Node[]          nodes    = { typeDecl };
        Object[]        args     = { Global.getFactory().createType(PrimitiveType.LONG_TYPE, 0),
                                     "_attr",
                                     Global.getFactory().createMethodInvocation(
                                         typeDecl.getMethods().get(0),
                                         null) };

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
                     "    private long _attr = getValue();\n\n\n"+
                     "    public static int getValue()\n"+
                     "    {\n"+
                     "        return 0;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform20() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething(final int arg) {\n"+
                "    class Local {\n"+
                "      private boolean _isCorrect = false;\n"+
                "      private Object _val = null;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration method = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes  = { (Node)method.getBody().getBlockStatements().get(0) };
        Object[]          args   = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                     "_attr",
                                     Global.getFactory().createVariableAccess(
                                         method.getParameterList().getParameters().get(0)) };

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
                     "    private void doSomething(final int arg)\n"+
                     "    {\n"+
                     "        class Local\n"+
                     "        {\n"+
                     "            private boolean _isCorrect = false;\n\n"+
                     "            private Object _val = null;\n\n"+
                     "            private float _attr = arg;\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType("Object", false),
                           "CONSTANT" };

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
                     "public interface A\n"+
                     "{\n"+
                     "    Object CONSTANT = null;\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0),
                           "CONSTANT",
                           Global.getFactory().createFloatingPointLiteral("-1.0d") };

        initialize(nodes, args);
        analyze();
        assertTrue(perform());
        assertEquals("package test;\n\n"+
                     "public interface A\n"+
                     "{\n"+
                     "    double CONSTANT = -1.0d;\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private boolean _isCorrect = false;\n"+
                "  private Object _val = null;\n"+
                "  public void doSomething(int arg) {\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType("String", false),
                           "_attr" };

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
                     "    private boolean _isCorrect = false;\n\n"+
                     "    private Object _val = null;\n\n"+
                     "    private String _attr = null;\n\n\n"+
                     "    public void doSomething(int arg)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform6() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private boolean _isCorrect = false;\n"+
                "  private short _val = 0;\n"+
                "  public static int getValue() {\n"+
                "    return 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Node[]          nodes    = { typeDecl };
        Object[]        args     = { Global.getFactory().createType(PrimitiveType.LONG_TYPE, 0),
                                     "_attr",
                                     Global.getFactory().createFieldAccess(typeDecl.getFields().get(1)) };

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
                     "    private boolean _isCorrect = false;\n\n"+
                     "    private short _val = 0;\n\n"+
                     "    private long _attr = _val;\n\n\n"+
                     "    public static int getValue()\n"+
                     "    {\n"+
                     "        return 0;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform7() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "    public static final int INT_CONSTANT = -1;\n"+
                "    public static final double DBL_CONSTANT = 10.0;\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType("Object", false),
                           "CONSTANT" };

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
                     "public interface A\n"+
                     "{\n"+
                     "    int INT_CONSTANT = -1;\n\n"+
                     "    double DBL_CONSTANT = 10.0;\n\n"+
                     "    Object CONSTANT = null;\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform8() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "    public static final int INT_CONSTANT = -1;\n"+
                "    public static final double DBL_CONSTANT = 10.0;\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Node[]          nodes    = { typeDecl };
        Object[]        args     = { Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0),
                                     "CONSTANT",
                                     Global.getFactory().createUnaryExpression(
                                         UnaryExpression.MINUS_OP,
                                         Global.getFactory().createFieldAccess(
                                             typeDecl.getFields().get(1))) };

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
                     "public interface A\n"+
                     "{\n"+
                     "    int INT_CONSTANT = -1;\n\n"+
                     "    double DBL_CONSTANT = 10.0;\n\n"+
                     "    double CONSTANT = -DBL_CONSTANT;\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform9() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createType("String", false),
                           "_attr" };

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
                     "        private String _attr = null;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }
}
