package jart.test.restructuring;
import jart.restructuring.variablelevel.RemoveAssignmentsToParameters;
import jast.ParseException;
import jast.ast.Node;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.TryStatement;
import antlr.ANTLRException;

public class TestRemoveAssignmentsToParameters extends TestBase
{

    public TestRemoveAssignmentsToParameters(String name)
    {
        super(name);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();
        setRestructuring(new RemoveAssignmentsToParameters());
    }

    public void testAnalyze1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "var" };

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
                "  public void doSomething(int arg) {\n"+
                "    int val = arg;\n"+
                "    arg = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "var" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze11() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int len = 0;\n"+
                "  public A(String[] args) {\n"+
                "    Object val = args;\n"+
                "    args = null;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDecl       = (ClassDeclaration) _project.getType("test.A");
        ConstructorDeclaration constructorDecl = classDecl.getConstructors().get(0);
        Node[]                 nodes           = { constructorDecl.getParameterList().getParameters().get(0) };
        Object[]               args            = { "var" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze12() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public abstract class A {\n"+
                "  public abstract void test() throws RuntimeException;\n"+
                "  public void doSomething() {\n"+
                "    try {\n"+
                "      test();\n"+
                "    } catch (RuntimeException ex) {\n"+
                "      ex.printStackTrace();\n"+
                "      ex = new RuntimeException();\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(1);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "var" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze13() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    arg += 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "var" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze14() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    arg = 1;\n"+
                "    String varArg = null;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "var" };

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
                "  public void doSomething(int arg) {\n"+
                "    int varArg = arg;\n"+
                "    arg += 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "var" };

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
                "  public A(String[] args) {\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDecl       = (ClassDeclaration) _project.getType("test.A");
        ConstructorDeclaration constructorDecl = classDecl.getConstructors().get(0);
        Node[]                 nodes           = { constructorDecl.getParameterList().getParameters().get(0) };
        Object[]               args            = { "var" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public abstract class A {\n"+
                "  public abstract void test() throws RuntimeException;\n"+
                "  public void doSomething() {\n"+
                "    try {\n"+
                "      test();\n"+
                "    } catch (RuntimeException ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(1);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "var" };

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
                "  public void doSomething(int arg) {\n"+
                "    int val = arg;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "var" };

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
                "  private int len = 0;\n"+
                "  public A(String[] args) {\n"+
                "    len = args.length;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDecl       = (ClassDeclaration) _project.getType("test.A");
        ConstructorDeclaration constructorDecl = classDecl.getConstructors().get(0);
        Node[]                 nodes           = { constructorDecl.getParameterList().getParameters().get(0) };
        Object[]               args            = { "var" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze6() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public abstract class A {\n"+
                "  public abstract void test() throws RuntimeException;\n"+
                "  public void doSomething() {\n"+
                "    try {\n"+
                "      test();\n"+
                "    } catch (RuntimeException ex) {\n"+
                "      ex.printStackTrace();\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(1);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "var" };

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
                "  public void doSomething(int arg) {\n"+
                "    arg = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "var" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze8() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int len = 0;\n"+
                "  public A(String[] args) {\n"+
                "    args = null;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDecl       = (ClassDeclaration) _project.getType("test.A");
        ConstructorDeclaration constructorDecl = classDecl.getConstructors().get(0);
        Node[]                 nodes           = { constructorDecl.getParameterList().getParameters().get(0) };
        Object[]               args            = { "var" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyze9() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public abstract class A {\n"+
                "  public abstract void test() throws RuntimeException;\n"+
                "  public void doSomething() {\n"+
                "    try {\n"+
                "      test();\n"+
                "    } catch (RuntimeException ex) {\n"+
                "      ex = new RuntimeException();\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(1);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "var" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
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

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "var" };

        assertTrue(initialize(nodes, args));
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
                "  public void doSomething(int arg) {\n"+
                "    for (arg *= 2; arg < 5; arg--) {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "var" };

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
                     "    public void doSomething(int arg)\n"+
                     "    {\n"+
                     "        int varArg = arg;\n\n"+
                     "        for (varArg *= 2; varArg < 5; varArg--)\n"+
                     "        {}\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    int var = arg;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "var" };

        assertTrue(initialize(nodes, args));
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
                     "    public void doSomething(int arg)\n"+
                     "    {\n"+
                     "        int var = arg;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int len = 0;\n"+
                "  public void doSomething(String arg) {\n"+
                "    len = arg.getLength();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "var" };

        assertTrue(initialize(nodes, args));
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
                     "    private int len = 0;\n\n\n"+
                     "    public void doSomething(String arg)\n"+
                     "    {\n"+
                     "        len = arg.getLength();\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(String arg) {\n"+
                "    arg = null;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "var" };

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
                     "    public void doSomething(String arg)\n"+
                     "    {\n"+
                     "        String varArg = arg;\n\n"+
                     "        varArg = null;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    int[] args = { arg = 0 };\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "var" };

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
                     "    public void doSomething(int arg)\n"+
                     "    {\n"+
                     "        int   varArg = arg;\n"+
                     "        int[] args = { varArg = 0 };\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform6() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    for (int idx = 0; idx < 5; arg++) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "var" };

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
                     "    public void doSomething(int arg)\n"+
                     "    {\n"+
                     "        int varArg = arg;\n\n"+
                     "        for (int idx = 0; idx < 5; varArg++)\n"+
                     "        {}\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform7() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    int[] vals = { --arg };\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "var" };

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
                     "    public void doSomething(int arg)\n"+
                     "    {\n"+
                     "        int   varArg = arg;\n"+
                     "        int[] vals = { --varArg };\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform8() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(String arg) {\n"+
                "    String val = arg += \" \";\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "var" };

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
                     "    public void doSomething(String arg)\n"+
                     "    {\n"+
                     "        String varArg = arg;\n"+
                     "        String val = varArg += \" \";\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform9() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(float arg) {\n"+
                "    int val = 0;\n"+
                "    if (arg != 0.0f) {\n"+
                "      val = (int)(arg += 1.0f);\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "var" };

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
                     "    public void doSomething(float arg)\n"+
                     "    {\n"+
                     "        float varArg = arg;\n"+
                     "        int   val = 0;\n\n"+
                     "        if (varArg != 0.0f)\n"+
                     "        {\n"+
                     "            val = (int)(varArg += 1.0f);\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }


    public void testAnalyze16() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg1, String arg2) {\n"+
                "    int var = arg1;\n"+
                "    arg2 += \"\";\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList() };
        Object[]          args       = { "var" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }



    public void testAnalyze17() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg1, String arg2) {\n"+
                "    int varArg2 = arg1;\n"+
                "    arg2 += \"\";\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList() };
        Object[]          args       = { "var" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }



    public void testPerform11() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg1, Object arg2) {\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList() };
        Object[]          args       = { "var" };

        assertTrue(initialize(nodes, args));
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
                     "    public void doSomething(int arg1, Object arg2)\n"+
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerform12() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(String arg1, Object arg2) {\n"+
                "    arg1 = arg2.toString();\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList() };
        Object[]          args       = { "var" };

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
                     "    public void doSomething(String arg1, Object arg2)\n"+
                     "    {\n"+
                     "        String varArg1 = arg1;\n\n"+
                     "        varArg1 = arg2.toString();\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }



    public void testPerform13() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(String arg1, Object arg2) {\n"+
                "    arg2 = arg1 = null;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList() };
        Object[]          args       = { "var" };

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
                     "    public void doSomething(String arg1, Object arg2)\n"+
                     "    {\n"+
                     "        String varArg1 = arg1;\n"+
                     "        Object varArg2 = arg2;\n\n"+
                     "        varArg2 = varArg1 = null;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }
}
