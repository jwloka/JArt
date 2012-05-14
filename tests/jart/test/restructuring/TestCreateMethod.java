package jart.test.restructuring;
import jart.restructuring.methodlevel.CreateMethod;
import jast.Global;
import jast.ParseException;
import jast.ast.Node;
import jast.ast.nodes.Block;
import jast.ast.nodes.PrimitiveType;
import jast.ast.nodes.TypeDeclaration;
import antlr.ANTLRException;

public class TestCreateMethod extends TestBase
{

    public TestCreateMethod(String name)
    {
        super(name);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();
        setRestructuring(new CreateMethod());
    }

    public void testAnalyzeCompatibleSignature1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0),
                           "arg" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCompatibleSignature10() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public abstract class B extends A {\n"+
                "  public abstract void doSomething(int arg);\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType(PrimitiveType.SHORT_TYPE, 0),
                           "arg" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCompatibleSignature11() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B extends A {\n"+
                "  public void doSomething(int arg1, double arg2) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                           "arg1",
                           Global.getFactory().createType(PrimitiveType.LONG_TYPE, 0),
                           "arg2" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCompatibleSignature12() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public interface B {\n"+
                "    public void doSomething(String arg);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType("Object", false),
                           "arg" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCompatibleSignature13() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public class B {\n"+
                "    public void doSomething(String arg1, Object arg2) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType("Object", false),
                           "arg1",
                           Global.getFactory().createType("String", false),
                           "arg2" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCompatibleSignature14() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private interface B {\n"+
                "    public void doSomething(String arg);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 1),
                           "args" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCompatibleSignature15() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private void doSomething(String arg) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType("String", 1),
                           "args" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCompatibleSignature16() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public void doSomething(float[] args);\n"+
                "  public interface B {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 1),
                           "args" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCompatibleSignature17() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething(float[] args) {}\n"+
                "  public interface B {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType("String", 1),
                           "args" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCompatibleSignature18() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public void doSomething(Object[] args);\n"+
                "  public class B {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType("String", 2),
                           "args" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCompatibleSignature19() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething(short arg) {}\n"+
                "  private class B {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0),
                           "arg" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCompatibleSignature2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public void doSomething(int arg1, double arg2);\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0),
                           "arg1",
                           Global.getFactory().createType(PrimitiveType.INT_TYPE, 0),
                           "arg2" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCompatibleSignature20() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void test() {\n"+
                "    class Local {\n"+
                "     private void doSomething(short arg1, float arg2) {}\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0),
                           "arg1",
                           Global.getFactory().createType(PrimitiveType.INT_TYPE, 0),
                           "arg2" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCompatibleSignature21() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private void test() {\n"+
                "      class Local {\n"+
                "        private void doSomething(java.io.Serializable arg) {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType("String", false),
                           "arg" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCompatibleSignature22() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething(String arg1, String arg2) {\n"+
                "    class Local {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Block           body     = typeDecl.getMethods().get(0).getBody();
        Node[]          nodes    = { (Node)body.getBlockStatements().get(0) };
        Object[]        args     = { Global.getFactory().createNullType(),
                                     "doSomething",
                                     Global.getFactory().createType("Object", false),
                                     "arg1",
                                     Global.getFactory().createType("Object", false),
                                     "arg2" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCompatibleSignature23() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething(String arg) {}\n"+
                "  private void test() {\n"+
                "    class Local {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Block           body     = typeDecl.getMethods().get(1).getBody();
        Node[]          nodes    = { (Node)body.getBlockStatements().get(0) };
        Object[]        args     = { Global.getFactory().createNullType(),
                                     "doSomething",
                                     Global.getFactory().createType(PrimitiveType.BOOLEAN_TYPE, 1),
                                     "args" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCompatibleSignature3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public void doSomething(String arg);\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public interface B extends A {}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.B") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType("Object", false),
                           "arg" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCompatibleSignature4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public void doSomething(String arg1, Object arg2);\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public abstract class B implements A {}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.B") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType("Object", false),
                           "arg1",
                           Global.getFactory().createType("String", false),
                           "arg2" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCompatibleSignature5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public abstract void doSomething(Object arg);\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public abstract class B extends A {}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.B") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType(PrimitiveType.INT_TYPE, 1),
                           "arg" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCompatibleSignature6() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(Object arg) {}\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public abstract class B extends A {}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.B") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType("Object", 1),
                           "arg" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCompatibleSignature7() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public interface B extends A {\n"+
                "  public void doSomething(int[] arg);\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 2),
                           "arg" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCompatibleSignature8() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public abstract class B implements A {\n"+
                "  public abstract void doSomething(int[] arg);\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 1),
                           "arg" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCompatibleSignature9() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B implements A {\n"+
                "  public void doSomething(Object[] arg) {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType("String", 1),
                           "arg" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeSameSignature1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeSameSignature10() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected void doSomething() {}\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B extends A {\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.B") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature11() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected void doSomething() {}\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B extends A {\n"+
                "  private void test() {\n"+
                "    doSomething();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.B") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature12() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected void doSomething() {}\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B extends A {\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.B") };
        Object[] args  = { Global.getFactory().createType("Object", 1),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeSameSignature13() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public interface B extends A {\n"+
                "  public void doSomething();\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature14() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public interface B extends A {\n"+
                "  public int doSomething();\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeSameSignature15() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public abstract class B implements A {\n"+
                "  public abstract void doSomething();\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature16() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public abstract class B implements A {\n"+
                "  public abstract String doSomething();\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType("Object", false),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeSameSignature17() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B implements A {\n"+
                "  public void doSomething() {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature18() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B implements A {\n"+
                "  public Object doSomething() {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType("Object", false),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature19() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B implements A {\n"+
                "  public void doSomething() {}\n"+
                "}\n",
                true);
        addType("test.C",
                "package test;\n"+
                "public class C implements A {\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public void doSomething();\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeSameSignature20() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public interface B extends A {\n"+
                "}\n",
                true);
        addType("test.C",
                "package test;\n"+
                "public class C implements A {\n"+
                "  public void doSomething() {}\n"+
                "}\n",
                true);
        addType("test.D",
                "package test;\n"+
                "public class D implements B {\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature21() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public abstract class B extends A {\n"+
                "  public abstract int[] doSomething();\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.INT_TYPE, 1),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature22() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public abstract class B extends A {\n"+
                "  public abstract int[] doSomething();\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.BOOLEAN_TYPE, 1),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeSameSignature23() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B extends A {\n"+
                "  public String[][] doSomething() {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType("String", 2),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature24() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B extends A {\n"+
                "  public String[][] doSomething();\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType("Object", 2),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeSameSignature25() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public interface B {\n"+
                "    public void doSomething();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature26() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public class B {\n"+
                "    public void doSomething() {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature27() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private interface B {\n"+
                "    public void doSomething();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature28() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    public void doSomething() {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature29() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public void doSomething();\n"+
                "  public interface B {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.SHORT_TYPE, 2),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public void doSomething();\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public interface B extends A {\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.B") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature30() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int doSomething() {\n"+
                "    return 0;\n"+
                "  }\n"+
                "  public interface B {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createType("String", false),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature31() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private static int doSomething() {\n"+
                "    return 0;\n"+
                "  }\n"+
                "  public interface B {\n"+
                "    public int CONSTANT = doSomething();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.INT_TYPE, 0),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeSameSignature32() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public int doSomething();\n"+
                "  public class B {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createType("String", 1),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature33() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int doSomething() {\n"+
                "    return 0;\n"+
                "  }\n"+
                "  private class B {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.INT_TYPE, 1),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature34() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int doSomething() {\n"+
                "    return 0;\n"+
                "  }\n"+
                "  private class B {\n"+
                "    public void test() {\n"+
                "      int var = doSomething();\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.INT_TYPE, 0),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeSameSignature35() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void test() {\n"+
                "    class Local {\n"+
                "      private Object doSomething() {}\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType("Object", 1),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature36() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private void test() {\n"+
                "      class Local {\n"+
                "        private Object doSomething() {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 1),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature37() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    class Local {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Block           body     = typeDecl.getMethods().get(0).getBody();
        Node[]          nodes    = { (Node)body.getBlockStatements().get(0) };
        Object[]        args     = { Global.getFactory().createNullType(),
                                     "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature38() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething() {}\n"+
                "  private void test() {\n"+
                "    class Local {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Block           body     = typeDecl.getMethods().get(1).getBody();
        Node[]          nodes    = { (Node)body.getBlockStatements().get(0) };
        Object[]        args     = { Global.getFactory().createNullType(),
                                     "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature39() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int doSomething() {}\n"+
                "  private void test() {\n"+
                "    class Local {\n"+
                "      private int attr = doSomething();\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Block           body     = typeDecl.getMethods().get(1).getBody();
        Node[]          nodes    = { (Node)body.getBlockStatements().get(0) };
        Object[]        args     = { Global.getFactory().createType(PrimitiveType.INT_TYPE, 0),
                                     "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public void doSomething();\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public interface B extends A {\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.B") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.BOOLEAN_TYPE, 0),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeSameSignature5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public void doSomething();\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public abstract class B implements A {\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.B") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature6() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public void doSomething();\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public abstract class B implements A {\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.B") };
        Object[] args  = { Global.getFactory().createType("String", false),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeSameSignature7() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public abstract class A {\n"+
                "  protected abstract void doSomething();\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public abstract class B extends A {\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.B") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature8() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public abstract class A {\n"+
                "  protected abstract void doSomething();\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public abstract class B extends A {\n"+
                "  private void test() {\n"+
                "    doSomething();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.B") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSameSignature9() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public abstract class A {\n"+
                "  protected abstract void doSomething();\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public abstract class B extends A {\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.B") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 1),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeUnambiguousName1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected int getSomething() {\n"+
                "    return 0;\n"+
                "  }\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public interface B {\n"+
                "}\n",
                true);
        addType("test.C",
                "package test;\n"+
                "public interface C extends B {\n"+
                "}\n",
                true);
        addType("test.D",
                "package test;\n"+
                "public class D extends A implements B, C {\n"+
                "}\n",
                true);
        addType("test.E",
                "package test;\n"+
                "public class E extends D {\n"+
                "  protected String toString() {\n"+
                "    return null;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.D") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeUnambiguousName2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected int getSomething() {\n"+
                "    return 0;\n"+
                "  }\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public interface B {\n"+
                "}\n",
                true);
        addType("test.C",
                "package test;\n"+
                "public interface C extends B {\n"+
                "}\n",
                true);
        addType("test.D",
                "package test;\n"+
                "public abstract class D extends A implements B, C {\n"+
                "}\n",
                true);
        addType("test.E",
                "package test;\n"+
                "public abstract class E extends D {\n"+
                "  protected String toString() {\n"+
                "    return null;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.C") };
        Object[] args  = { Global.getFactory().createType("String", false),
                           "doSomething",
                           Global.getFactory().createType(PrimitiveType.BOOLEAN_TYPE, 0),
                           "arg" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeUnambiguousSignature1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.BOOLEAN_TYPE, 0),
                           "doSomething",
                           Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0),
                           "arg" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeUnambiguousSignature10() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    public String doSomething(int arg) {\n"+
                "      return null;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType("String", false),
                           "arg" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeUnambiguousSignature11() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public interface B {\n"+
                "    public int doSomething(double arg);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.INT_TYPE, 0),
                           "doSomething",
                           Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 1),
                           "args" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeUnambiguousSignature12() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public interface B {\n"+
                "    public String doSomething(double arg);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType("String", 1),
                           "args" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeUnambiguousSignature13() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(String arg);\n"+
                "  public interface B {\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeUnambiguousSignature14() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(Object arg) {}\n"+
                "  private class B {\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType(PrimitiveType.SHORT_TYPE, 0),
                           "arg" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeUnambiguousSignature15() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public void doSomething(int[] args);\n"+
                "  public interface B {\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeUnambiguousSignature16() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public void doSomething(Object[][] args);\n"+
                "  public class B {\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeUnambiguousSignature2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public int doSomething();\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType("String", false),
                           "arg" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeUnambiguousSignature3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected void doSomething(String arg) {}\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B extends A {\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.B") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType("String", false),
                           "arg1",
                           Global.getFactory().createType("Object", false),
                           "arg2" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeUnambiguousSignature4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public double doSomething(int arg);\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public interface B extends A {\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.B") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeUnambiguousSignature5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public double doSomething(int arg);\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public abstract class B implements A {\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.B") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType(PrimitiveType.BOOLEAN_TYPE, 0),
                           "arg1",
                           Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                           "arg2" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeUnambiguousSignature7() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public class B extends A {\n"+
                "  protected void doSomething() {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.CHAR_TYPE, 0),
                           "doSomething",
                           Global.getFactory().createType(PrimitiveType.INT_TYPE, 2),
                           "args" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeUnambiguousSignature8() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "}\n",
                true);
        addType("test.B",
                "package test;\n"+
                "public interface B extends A {\n"+
                "  public void doSomething(int[] args);\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeUnambiguousSignature9() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public abstract class B {\n"+
                "    public abstract void doSomething(int arg);\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType(PrimitiveType.BOOLEAN_TYPE, 0),
                           "arg" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testPerform1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public abstract class A {\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

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
                     "public abstract class A\n"+
                     "{\n"+
                     "    private void doSomething()\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform10() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void test() {\n"+
                "    class Local {\n"+
                "      private void test() {}\n"+
                "      private void doSomething(String arg) {}\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Block           body     = typeDecl.getMethods().get(0).getBody();
        Node[]          nodes    = { (Node)body.getBlockStatements().get(0) };
        Object[]        args     = { Global.getFactory().createNullType(),
                                     "doSomething" };

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
                     "    private void test()\n"+
                     "    {\n"+
                     "        class Local\n"+
                     "        {\n"+
                     "            private void test()\n"+
                     "            {}\n\n"+
                     "            private void doSomething(String arg)\n"+
                     "            {}\n\n"+
                     "            private void doSomething()\n"+
                     "            {}\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething" };

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
                     "    void doSomething();\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createType("A", false),
                           "doSomething",
                           Global.getFactory().createType(PrimitiveType.BOOLEAN_TYPE, 0),
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
                     "    private class B\n"+
                     "    {\n"+
                     "        private A doSomething(boolean arg)\n"+
                     "        {\n"+
                     "            return null;\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public interface B {}\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0),
                           "doSomething",
                           Global.getFactory().createType("test.A", false),
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
                     "public interface A\n"+
                     "{\n"+
                     "    public interface B\n"+
                     "    {\n"+
                     "        double doSomething(A arg);\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void test() {\n"+
                "     class Local {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration typeDecl = _project.getType("test.A");
        Block           body     = typeDecl.getMethods().get(0).getBody();
        Node[]          nodes    = { (Node)body.getBlockStatements().get(0) };
        Object[]        args     = { Global.getFactory().createNullType(),
                                     "doSomething",
                                     Global.getFactory().createType(PrimitiveType.INT_TYPE, 1),
                                     "args1",
                                     Global.getFactory().createType("String", 2),
                                     "args2" };

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
                     "    private void test()\n"+
                     "    {\n"+
                     "        class Local\n"+
                     "        {\n"+
                     "            private void doSomething(int[] args1, String[][] args2)\n"+
                     "            {}\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform6() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public abstract class A {\n"+
                "  private void test() {}\n"+
                "  private abstract int doSomething(int arg);\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType("String", 1),
                           "doSomething",
                           Global.getFactory().createType(PrimitiveType.CHAR_TYPE, 2),
                           "args" };

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
                     "public abstract class A\n"+
                     "{\n"+
                     "    private void test()\n"+
                     "    {}\n\n"+
                     "    private abstract int doSomething(int arg);\n\n"+
                     "    private String[] doSomething(char[][] args)\n"+
                     "    {\n"+
                     "        return null;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform7() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public void test();\n"+
                "  public int doSomething(int arg);\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A") };
        Object[] args  = { Global.getFactory().createType(PrimitiveType.INT_TYPE, 1),
                           "doSomething",
                           Global.getFactory().createType("Object", 2),
                           "args" };

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
                     "    void test();\n\n"+
                     "    int doSomething(int arg);\n\n"+
                     "    int[] doSomething(Object[][] args);\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform8() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private void test() {}\n"+
                "    private void doSomething(int arg) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType(PrimitiveType.BOOLEAN_TYPE, 0),
                           "arg1",
                           Global.getFactory().createType(PrimitiveType.INT_TYPE, 0),
                           "arg2",
                           Global.getFactory().createType(PrimitiveType.CHAR_TYPE, 0),
                           "arg3" };

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
                     "        private void test()\n"+
                     "        {}\n\n"+
                     "        private void doSomething(int arg)\n"+
                     "        {}\n\n"+
                     "        private void doSomething(boolean arg1, int arg2, char arg3)\n"+
                     "        {}\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform9() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A {\n"+
                "  public interface B {\n"+
                "    public void test();\n"+
                "    public double doSomething();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        Node[]   nodes = { _project.getType("test.A.B") };
        Object[] args  = { Global.getFactory().createNullType(),
                           "doSomething",
                           Global.getFactory().createType("Object", false),
                           "arg1",
                           Global.getFactory().createType("test.A", false),
                           "arg2",
                           Global.getFactory().createType("String", false),
                           "arg3" };

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
                     "        void test();\n\n"+
                     "        double doSomething();\n\n"+
                     "        void doSomething(Object arg1, A arg2, String arg3);\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }
}
