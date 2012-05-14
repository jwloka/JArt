package jart.test.restructuring;
import jart.restructuring.variablelevel.CreateLocalVariable;
import jast.Global;
import jast.ParseException;
import jast.ast.Node;
import jast.ast.nodes.Block;
import jast.ast.nodes.CaseBlock;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ForStatement;
import jast.ast.nodes.IfThenElseStatement;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.PrimitiveType;
import jast.ast.nodes.SwitchStatement;
import jast.ast.nodes.TryStatement;
import antlr.ANTLRException;

public class TestCreateLocalVariable extends TestBase
{

    public TestCreateLocalVariable(String name)
    {
        super(name);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();
        setRestructuring(new CreateLocalVariable());
    }

    public void testAnalyzeForStmt1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block      = (Block)forStmt.getLoopStatement();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "txt",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeForStmt10() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "    }\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        int val = 0;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block      = (Block)forStmt.getLoopStatement();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeForStmt11() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        int val = 0;\n"+
                "    }\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(1);
        Block             block      = (Block)forStmt.getLoopStatement();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeForStmt12() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "    }\n"+
                "    {\n"+
                "      int val = 0;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block      = (Block)forStmt.getLoopStatement();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeForStmt13() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    {\n"+
                "      int val = 0;\n"+
                "    }\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(1);
        Block             block      = (Block)forStmt.getLoopStatement();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeForStmt14() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "      break;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block      = (Block)forStmt.getLoopStatement();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "txt",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeForStmt15() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "      break;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block      = (Block)forStmt.getLoopStatement();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "txt",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeForStmt2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(String val) {\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block      = (Block)forStmt.getLoopStatement();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeForStmt3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block      = (Block)forStmt.getLoopStatement();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.LONG_TYPE, 0),
                                         "idx",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeForStmt4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "    }\n"+
                "    String val = \"\";\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block      = (Block)forStmt.getLoopStatement();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeForStmt5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    String val = \"\";\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(1);
        Block             block      = (Block)forStmt.getLoopStatement();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeForStmt6() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "    }\n"+
                "    for (int val = 0; val < 5; val++) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block      = (Block)forStmt.getLoopStatement();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeForStmt7() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int val = 0; val < 5; val++) {}\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(1);
        Block             block      = (Block)forStmt.getLoopStatement();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeForStmt8() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "    }\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block      = (Block)forStmt.getLoopStatement();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "ex",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeForStmt9() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(1);
        Block             block      = (Block)forStmt.getLoopStatement();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "ex",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeInnerBlock1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "txt",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeInnerBlock10() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        int val = 0;\n"+
                "    }\n"+
                "    {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(1);
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeInnerBlock11() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    {\n"+
                "    }\n"+
                "    {\n"+
                "      int val = 0;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeInnerBlock12() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    {\n"+
                "      int val = 0;\n"+
                "    }\n"+
                "    {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(1);
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeInnerBlock2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(String val) {\n"+
                "    {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeInnerBlock3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    {\n"+
                "    }\n"+
                "    String val = \"\";\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeInnerBlock4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    String val = \"\";\n"+
                "    {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(1);
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeInnerBlock5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    {\n"+
                "    }\n"+
                "    for (int val = 0; val < 5; val++) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeInnerBlock6() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int val = 0; val < 5; val++) {}\n"+
                "    {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(1);
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeInnerBlock7() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    {\n"+
                "    }\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "ex",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeInnerBlock8() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "    {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(1);
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "ex",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeInnerBlock9() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    {\n"+
                "    }\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        int val = 0;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClass1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    class Local {\n"+
                "      private void test() {\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        ClassDeclaration  localDecl  = (ClassDeclaration)body.getBlockStatements().get(0);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "txt",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeLocalClass10() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {}\n"+
                "    class Local {\n"+
                "      private void test() {\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        ClassDeclaration  localDecl  = (ClassDeclaration)body.getBlockStatements().get(1);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.LONG_TYPE, 0),
                                         "idx",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClass11() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "      class Local {\n"+
                "        private void test() {\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        ForStatement      forStmt    = (ForStatement)body.getBlockStatements().get(0);
        Block             block      = (Block)forStmt.getLoopStatement();
        ClassDeclaration  localDecl  = (ClassDeclaration)block.getBlockStatements().get(0);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.LONG_TYPE, 0),
                                         "idx",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClass12() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    class Local {\n"+
                "      private void test() {\n"+
                "      }\n"+
                "    }\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        ClassDeclaration  localDecl  = (ClassDeclaration)body.getBlockStatements().get(0);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "ex",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClass13() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "    class Local {\n"+
                "      private void test() {\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        ClassDeclaration  localDecl  = (ClassDeclaration)body.getBlockStatements().get(1);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "ex",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClass14() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {\n"+
                "      class Local {\n"+
                "        private void test() {\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        TryStatement      tryStmt    = (TryStatement)body.getBlockStatements().get(0);
        Block             block      = tryStmt.getCatchClauses().get(0).getCatchBlock();
        ClassDeclaration  localDecl  = (ClassDeclaration)block.getBlockStatements().get(0);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "ex",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClass15() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (final Exception ex) {\n"+
                "      class Local {\n"+
                "        private void test() {\n"+
                "          String msg = ex.toString();\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        TryStatement      tryStmt    = (TryStatement)body.getBlockStatements().get(0);
        Block             block      = tryStmt.getCatchClauses().get(0).getCatchBlock();
        ClassDeclaration  localDecl  = (ClassDeclaration)block.getBlockStatements().get(0);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "ex",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClass16() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (final Exception ex) {\n"+
                "      class Local {\n"+
                "        private void test() {\n"+
                "          String msg = ex.toString();\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        TryStatement      tryStmt    = (TryStatement)body.getBlockStatements().get(0);
        Block             block      = tryStmt.getCatchClauses().get(0).getCatchBlock();
        ClassDeclaration  localDecl  = (ClassDeclaration)block.getBlockStatements().get(0);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "ex",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeLocalClass17() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    class Local {\n"+
                "      private void test() {\n"+
                "      }\n"+
                "    }\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        int val = 0;\n"+
                "        break;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        ClassDeclaration  localDecl  = (ClassDeclaration)body.getBlockStatements().get(0);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClass18() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        int val = 0;\n"+
                "        break;\n"+
                "    }\n"+
                "    class Local {\n"+
                "      private void test() {\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        ClassDeclaration  localDecl  = (ClassDeclaration)body.getBlockStatements().get(1);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClass19() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        int val = 0;\n"+
                "        break;\n"+
                "      default:\n"+
                "        class Local {\n"+
                "          private void test() {\n"+
                "          }\n"+
                "        }\n"+
                "        break;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        SwitchStatement   switchStmt = (SwitchStatement)body.getBlockStatements().get(0);
        CaseBlock         caseBlock  = switchStmt.getCaseBlocks().get(1);
        ClassDeclaration  localDecl  = (ClassDeclaration)caseBlock.getBlockStatements().get(0);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClass2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(String txt) {\n"+
                "    class Local {\n"+
                "      private void test() {\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        ClassDeclaration  localDecl  = (ClassDeclaration)body.getBlockStatements().get(0);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "txt",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClass20() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        final int val = 0;\n"+
                "        break;\n"+
                "      default:\n"+
                "        class Local {\n"+
                "          private void test() {\n"+
                "            int arg = val;\n"+
                "          }\n"+
                "        }\n"+
                "        break;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        SwitchStatement   switchStmt = (SwitchStatement)body.getBlockStatements().get(0);
        CaseBlock         caseBlock  = switchStmt.getCaseBlocks().get(1);
        ClassDeclaration  localDecl  = (ClassDeclaration)caseBlock.getBlockStatements().get(0);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0),
                                         "val",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClass21() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        final int val = 0;\n"+
                "        break;\n"+
                "      default:\n"+
                "        class Local {\n"+
                "          private void test() {\n"+
                "            int arg = val;\n"+
                "          }\n"+
                "        }\n"+
                "        break;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        SwitchStatement   switchStmt = (SwitchStatement)body.getBlockStatements().get(0);
        CaseBlock         caseBlock  = switchStmt.getCaseBlocks().get(1);
        ClassDeclaration  localDecl  = (ClassDeclaration)caseBlock.getBlockStatements().get(0);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeLocalClass22() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    class Local {\n"+
                "      private void test() {\n"+
                "      }\n"+
                "    }\n"+
                "    {\n"+
                "      double val = 0.0;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        ClassDeclaration  localDecl  = (ClassDeclaration)body.getBlockStatements().get(0);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.INT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClass23() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    {\n"+
                "      double val = 0.0;\n"+
                "    }\n"+
                "    class Local {\n"+
                "      private void test() {\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        ClassDeclaration  localDecl  = (ClassDeclaration)body.getBlockStatements().get(1);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.INT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClass24() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    {\n"+
                "      double val = 0.0;\n"+
                "      class Local {\n"+
                "        private void test() {\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        Block             block      = (Block)body.getBlockStatements().get(0);
        ClassDeclaration  localDecl  = (ClassDeclaration)block.getBlockStatements().get(1);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.INT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClass25() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    {\n"+
                "      final double val = 0.0;\n"+
                "      class Local {\n"+
                "        private void test() {\n"+
                "          double var = val;\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        Block             block      = (Block)body.getBlockStatements().get(0);
        ClassDeclaration  localDecl  = (ClassDeclaration)block.getBlockStatements().get(1);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.INT_TYPE, 0),
                                         "val",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClass26() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    {\n"+
                "      final double val = 0.0;\n"+
                "      class Local {\n"+
                "        private void test() {\n"+
                "          double var = val;\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        Block             block      = (Block)body.getBlockStatements().get(0);
        ClassDeclaration  localDecl  = (ClassDeclaration)block.getBlockStatements().get(1);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.INT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeLocalClass3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(String txt) {\n"+
                "    class Local {\n"+
                "      private void test() {\n"+
                "        String msg = txt;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        ClassDeclaration  localDecl  = (ClassDeclaration)body.getBlockStatements().get(0);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "txt",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClass4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(String txt) {\n"+
                "    class Local {\n"+
                "      private void test() {\n"+
                "        String msg = txt;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        ClassDeclaration  localDecl  = (ClassDeclaration)body.getBlockStatements().get(0);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "txt",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeLocalClass5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    class Local {\n"+
                "      private void test() {\n"+
                "      }\n"+
                "    }\n"+
                "    String txt;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        ClassDeclaration  localDecl  = (ClassDeclaration)body.getBlockStatements().get(0);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "txt",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClass6() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    String txt;\n"+
                "    class Local {\n"+
                "      private void test() {\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        ClassDeclaration  localDecl  = (ClassDeclaration)body.getBlockStatements().get(1);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "txt",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClass7() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    final String txt;\n"+
                "    class Local {\n"+
                "      private void test() {\n"+
                "        String msg = txt;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        ClassDeclaration  localDecl  = (ClassDeclaration)body.getBlockStatements().get(1);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "txt",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClass8() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    final String txt;\n"+
                "    class Local {\n"+
                "      private void test() {\n"+
                "        String msg = txt;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        ClassDeclaration  localDecl  = (ClassDeclaration)body.getBlockStatements().get(1);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "txt",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeLocalClass9() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    class Local {\n"+
                "      private void test() {\n"+
                "      }\n"+
                "    }\n"+
                "    for (int idx = 0; idx < 5; idx++) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block             body       = classDecl.getMethods().get(0).getBody();
        ClassDeclaration  localDecl  = (ClassDeclaration)body.getBlockStatements().get(0);
        MethodDeclaration methodDecl = localDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.LONG_TYPE, 0),
                                         "idx",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClassField1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    class Local {\n"+
                "      private int val = 0;\n"+
                "      private void test() {\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClassField2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    class Local {\n"+
                "      private int val = 0;\n"+
                "      private void test() {\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Node[]            nodes      = { methodDecl.getBody() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClassField3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    class Local {\n"+
                "      private int val = 0;\n"+
                "      private void test() {\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ClassDeclaration  localDecl  = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { localDecl.getMethods().get(0).getBody() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClassField4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    class Local {\n"+
                "      private int val = 0;\n"+
                "      private void test() {\n"+
                "        val = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ClassDeclaration  localDecl  = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { localDecl.getMethods().get(0).getBody() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocalClassField5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    class Local {\n"+
                "      private int val = 0;\n"+
                "      private void test() {\n"+
                "        val = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ClassDeclaration  localDecl  = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { localDecl.getMethods().get(0).getBody() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeMethodLevel1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = methodDecl.getBody();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "var",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeMethodLevel10() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = methodDecl.getBody();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.LONG_TYPE, 0),
                                         "ex",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeMethodLevel11() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = methodDecl.getBody();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.LONG_TYPE, 0),
                                         "ex",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeMethodLevel12() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        float val = 0.0f;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = methodDecl.getBody();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.LONG_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeMethodLevel13() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        float val = 0.0f;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = methodDecl.getBody();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.LONG_TYPE, 0),
                                         "val",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeMethodLevel14() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    {\n"+
                "      float val = 0.0f;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = methodDecl.getBody();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.LONG_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeMethodLevel15() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    {\n"+
                "      float val = 0.0f;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = methodDecl.getBody();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.LONG_TYPE, 0),
                                         "val",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeMethodLevel16() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    class Local {\n"+
                "      float val = 0.0f;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = methodDecl.getBody();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.LONG_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeMethodLevel17() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    class Local {\n"+
                "      float val = 0.0f;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = methodDecl.getBody();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.LONG_TYPE, 0),
                                         "val",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeMethodLevel18() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    return;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = methodDecl.getBody();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "var",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeMethodLevel2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int val) {\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = methodDecl.getBody();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeMethodLevel3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    int var = arg;\n"+
                "    var--;\n"+
                "    if (var > 0) {\n"+
                "      doSomething(var);\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = methodDecl.getBody();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(-1)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeMethodLevel4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    int var = arg;\n"+
                "    var--;\n"+
                "    if (var > 0) {\n"+
                "      doSomething(var);\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = methodDecl.getBody();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(2)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeMethodLevel5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    int var = arg;\n"+
                "    var--;\n"+
                "    if (var > 0) {\n"+
                "      doSomething(var);\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = methodDecl.getBody();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(10)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeMethodLevel6() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    int val = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = methodDecl.getBody();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeMethodLevel7() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    int val = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = methodDecl.getBody();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeMethodLevel8() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = methodDecl.getBody();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.LONG_TYPE, 0),
                                         "idx",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeMethodLevel9() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = methodDecl.getBody();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.LONG_TYPE, 0),
                                         "idx",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSwitchStmt1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        break;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        SwitchStatement   switchStmt = (SwitchStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { switchStmt.getCaseBlocks().get(0) };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "txt",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeSwitchStmt10() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int val = 0; val < 5; val++) {}\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        break;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        SwitchStatement   switchStmt = (SwitchStatement)methodDecl.getBody().getBlockStatements().get(1);
        Node[]            nodes      = { switchStmt.getCaseBlocks().get(0) };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSwitchStmt11() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        break;\n"+
                "    }\n"+
                "    try {}\n"+
                "    catch (Exception otherEx) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        SwitchStatement   switchStmt = (SwitchStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { switchStmt.getCaseBlocks().get(0) };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "otherEx",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSwitchStmt12() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception otherEx) {}\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        break;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        SwitchStatement   switchStmt = (SwitchStatement)methodDecl.getBody().getBlockStatements().get(1);
        Node[]            nodes      = { switchStmt.getCaseBlocks().get(0) };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "otherEx",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSwitchStmt13() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        break;\n"+
                "    }\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        int val = 0;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        SwitchStatement   switchStmt = (SwitchStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { switchStmt.getCaseBlocks().get(0) };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSwitchStmt14() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        int val = 0;\n"+
                "    }\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        break;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        SwitchStatement   switchStmt = (SwitchStatement)methodDecl.getBody().getBlockStatements().get(1);
        Node[]            nodes      = { switchStmt.getCaseBlocks().get(0) };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSwitchStmt15() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        break;\n"+
                "    }\n"+
                "    {\n"+
                "      int val = 0;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        SwitchStatement   switchStmt = (SwitchStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { switchStmt.getCaseBlocks().get(0) };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSwitchStmt16() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    {\n"+
                "      int val = 0;\n"+
                "    }\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        break;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        SwitchStatement   switchStmt = (SwitchStatement)methodDecl.getBody().getBlockStatements().get(1);
        Node[]            nodes      = { switchStmt.getCaseBlocks().get(0) };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSwitchStmt2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg, String txt) {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        break;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        SwitchStatement   switchStmt = (SwitchStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { switchStmt.getCaseBlocks().get(0) };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "txt",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeSwitchStmt3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        int val = 0;\n"+
                "        break;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        SwitchStatement   switchStmt = (SwitchStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { switchStmt.getCaseBlocks().get(0) };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeSwitchStmt4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        int val = 0;\n"+
                "        break;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        SwitchStatement   switchStmt = (SwitchStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { switchStmt.getCaseBlocks().get(0) };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                         "val",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeSwitchStmt5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        break;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        SwitchStatement   switchStmt = (SwitchStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { switchStmt.getCaseBlocks().get(0) };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                         "val",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeSwitchStmt6() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        int val = 0;\n"+
                "        break;\n"+
                "      default:\n"+
                "        break;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        SwitchStatement   switchStmt = (SwitchStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { switchStmt.getCaseBlocks().get(1) };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                         "val",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeSwitchStmt7() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        break;\n"+
                "    }\n"+
                "    String val = \"\";\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        SwitchStatement   switchStmt = (SwitchStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { switchStmt.getCaseBlocks().get(0) };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeSwitchStmt8() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    String val = \"\";\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        break;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        SwitchStatement   switchStmt = (SwitchStatement)methodDecl.getBody().getBlockStatements().get(1);
        Node[]            nodes      = { switchStmt.getCaseBlocks().get(0) };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeSwitchStmt9() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        break;\n"+
                "    }\n"+
                "    for (int val = 0; val < 5; val++) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        SwitchStatement   switchStmt = (SwitchStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { switchStmt.getCaseBlocks().get(0) };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTopLevelField1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val = 0;\n"+
                "  public void doSomething() {\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = methodDecl.getBody();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTopLevelField10() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val = 0;\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {\n"+
                "      val = 1;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getCatchBlock() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTopLevelField11() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val = 0;\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "    val = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getCatchBlock() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTopLevelField12() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val = 0;\n"+
                "  public void doSomething(int arg) {\n"+
                "    val = 1;\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        break;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        SwitchStatement   switchStmt = (SwitchStatement)methodDecl.getBody().getBlockStatements().get(1);
        Node[]            nodes      = { switchStmt.getCaseBlocks().get(0) };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTopLevelField13() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val = 0;\n"+
                "  public void doSomething() {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        val = 1;\n"+
                "        break;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        SwitchStatement   switchStmt = (SwitchStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { switchStmt.getCaseBlocks().get(0) };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTopLevelField14() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val = 0;\n"+
                "  public void doSomething() {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        val = 1;\n"+
                "        break;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        SwitchStatement   switchStmt = (SwitchStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { switchStmt.getCaseBlocks().get(0) };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTopLevelField15() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val = 0;\n"+
                "  public void doSomething(int arg) {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        break;\n"+
                "    }\n"+
                "    val = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        SwitchStatement   switchStmt = (SwitchStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { switchStmt.getCaseBlocks().get(0) };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTopLevelField16() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val = 0;\n"+
                "  public void doSomething(int arg) {\n"+
                "    val = 1;\n"+
                "    {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(1);
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTopLevelField17() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val = 0;\n"+
                "  public void doSomething(int arg) {\n"+
                "    {\n"+
                "      val = 1;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTopLevelField18() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val = 0;\n"+
                "  public void doSomething(int arg) {\n"+
                "    {\n"+
                "      val = 1;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTopLevelField19() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val = 0;\n"+
                "  public void doSomething(int arg) {\n"+
                "    {\n"+
                "    }\n"+
                "    val = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTopLevelField2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val = 0;\n"+
                "  public void doSomething() {\n"+
                "    val = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = methodDecl.getBody();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTopLevelField20() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val = 0;\n"+
                "  public void doSomething(int arg) {\n"+
                "    val = 1;\n"+
                "    class Local {\n"+
                "      private void test() {\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ClassDeclaration  localDecl  = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(1);
        Node[]            nodes      = { localDecl.getMethods().get(0).getBody() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTopLevelField21() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val = 0;\n"+
                "  public void doSomething(int arg) {\n"+
                "    class Local {\n"+
                "      private void test() {\n"+
                "        val = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ClassDeclaration  localDecl  = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { localDecl.getMethods().get(0).getBody() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTopLevelField22() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val = 0;\n"+
                "  public void doSomething(int arg) {\n"+
                "    class Local {\n"+
                "      private void test() {\n"+
                "        val = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ClassDeclaration  localDecl  = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { localDecl.getMethods().get(0).getBody() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTopLevelField23() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val = 0;\n"+
                "  public void doSomething(int arg) {\n"+
                "    class Local {\n"+
                "      private void test() {\n"+
                "      }\n"+
                "    }\n"+
                "    val = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ClassDeclaration  localDecl  = (ClassDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { localDecl.getMethods().get(0).getBody() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTopLevelField3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val = 0;\n"+
                "  public void doSomething() {\n"+
                "    val = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        Block             block      = methodDecl.getBody();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTopLevelField4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val = 0;\n"+
                "  public void doSomething() {\n"+
                "    val = 1;\n"+
                "    for (int idx = 0; idx < 5; idx++) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(1);
        Node[]            nodes      = { (Block)forStmt.getLoopStatement() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTopLevelField5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val = 0;\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "      val = 1;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { (Block)forStmt.getLoopStatement() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTopLevelField6() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val = 0;\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "      val = 1;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { (Block)forStmt.getLoopStatement() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTopLevelField7() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val = 0;\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {}\n"+
                "    val = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { (Block)forStmt.getLoopStatement() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTopLevelField8() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val = 0;\n"+
                "  public void doSomething() {\n"+
                "    val = 1;\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(1);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getCatchBlock() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTopLevelField9() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val = 0;\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {\n"+
                "      val = 1;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getCatchBlock() };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "val",
                                         new Integer(1)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTryStmt1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block      = tryStmt.getTryBlock();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "txt",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeTryStmt10() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int val = 0; val < 5; val++) {}\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(1);
        Block             block      = tryStmt.getCatchClauses().get(0).getCatchBlock();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTryStmt11() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "    try {}\n"+
                "    catch (Exception otherEx) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block      = tryStmt.getCatchClauses().get(0).getCatchBlock();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "otherEx",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTryStmt12() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception otherEx) {}\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(1);
        Block             block      = tryStmt.getCatchClauses().get(0).getCatchBlock();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "otherEx",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTryStmt13() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        int val = 0;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block      = tryStmt.getCatchClauses().get(0).getCatchBlock();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTryStmt14() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        int val = 0;\n"+
                "    }\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(1);
        Block             block      = tryStmt.getCatchClauses().get(0).getCatchBlock();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTryStmt15() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "    {\n"+
                "      int val = 0;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block      = tryStmt.getCatchClauses().get(0).getCatchBlock();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTryStmt16() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    {\n"+
                "      int val = 0;\n"+
                "    }\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(1);
        Block             block      = tryStmt.getCatchClauses().get(0).getCatchBlock();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("Object", false),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTryStmt2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block      = tryStmt.getCatchClauses().get(0).getCatchBlock();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "txt",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeTryStmt3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "    finally {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block      = tryStmt.getFinallyClause();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "txt",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeTryStmt4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(String val) {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block      = tryStmt.getCatchClauses().get(0).getCatchBlock();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeTryStmt5() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block      = tryStmt.getCatchClauses().get(0).getCatchBlock();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "ex",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeTryStmt6() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block      = tryStmt.getTryBlock();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType("String", false),
                                         "ex",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTryStmt7() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "    String val = \"\";\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block      = tryStmt.getCatchClauses().get(0).getCatchBlock();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeTryStmt8() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    String val = \"\";\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(1);
        Block             block      = tryStmt.getCatchClauses().get(0).getCatchBlock();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

        initialize(nodes, args);
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeTryStmt9() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "    for (int val = 0; val < 5; val++) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration  classDecl  = (ClassDeclaration) _project.getType("test.A");
        MethodDeclaration methodDecl = classDecl.getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block      = tryStmt.getCatchClauses().get(0).getCatchBlock();
        Node[]            nodes      = { block };
        Object[]          args       = { Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0),
                                         "val",
                                         new Integer(0)};

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
                "    int var = arg;\n"+
                "    var--;\n"+
                "    if (var > 0) {\n"+
                "      doSomething(var);\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration classDecl = (ClassDeclaration) _project.getType("test.A");
        Node[]           nodes     = { classDecl.getMethods().get(0).getBody() };
        Object[]         args      = { Global.getFactory().createType("String", false),
                                       "val",
                                       new Integer(0)};

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
                     "        String val = null;\n"+
                     "        int    var = arg;\n\n"+
                     "        var--;\n"+
                     "        if (var > 0)\n"+
                     "        {\n"+
                     "            doSomething(var);\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform10() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(final int arg) {\n"+
                "    class Local {\n"+
                "      private void test() {\n"+
                "        int var = arg;\n"+
                "        var--;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration classDecl = (ClassDeclaration) _project.getType("test.A");
        Block            block     = classDecl.getMethods().get(0).getBody();
        ClassDeclaration localDecl = (ClassDeclaration)block.getBlockStatements().get(0);
        Node[]           nodes     = { localDecl.getMethods().get(0).getBody() };
        Object[]         args      = { Global.getFactory().createType(PrimitiveType.CHAR_TYPE, 0),
                                       "val",
                                       new Integer(0)};

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
                     "    public void doSomething(final int arg)\n"+
                     "    {\n"+
                     "        class Local\n"+
                     "        {\n"+
                     "            private void test()\n"+
                     "            {\n"+
                     "                char val = '\\0';\n"+
                     "                int  var = arg;\n\n"+
                     "                var--;\n"+
                     "            }\n"+
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
                "  public void doSomething(final int arg) {\n"+
                "    class Local {\n"+
                "      private void test() {\n"+
                "        int var = arg;\n"+
                "        var--;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration classDecl = (ClassDeclaration) _project.getType("test.A");
        Block            block     = classDecl.getMethods().get(0).getBody();
        ClassDeclaration localDecl = (ClassDeclaration)block.getBlockStatements().get(0);
        Node[]           nodes     = { localDecl.getMethods().get(0).getBody() };
        Object[]         args      = { Global.getFactory().createType(PrimitiveType.CHAR_TYPE, 0),
                                       "val",
                                       new Integer(1)};

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
                     "    public void doSomething(final int arg)\n"+
                     "    {\n"+
                     "        class Local\n"+
                     "        {\n"+
                     "            private void test()\n"+
                     "            {\n"+
                     "                int  var = arg;\n"+
                     "                char val = '\\0';\n\n"+
                     "                var--;\n"+
                     "            }\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform12() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(final int arg) {\n"+
                "    class Local {\n"+
                "      private void test() {\n"+
                "        int var = arg;\n"+
                "        var--;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration classDecl = (ClassDeclaration) _project.getType("test.A");
        Block            block     = classDecl.getMethods().get(0).getBody();
        ClassDeclaration localDecl = (ClassDeclaration)block.getBlockStatements().get(0);
        Node[]           nodes     = { localDecl.getMethods().get(0).getBody() };
        Object[]         args      = { Global.getFactory().createType(PrimitiveType.CHAR_TYPE, 0),
                                       "val",
                                       new Integer(2)};

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
                     "    public void doSomething(final int arg)\n"+
                     "    {\n"+
                     "        class Local\n"+
                     "        {\n"+
                     "            private void test()\n"+
                     "            {\n"+
                     "                int var = arg;\n\n"+
                     "                var--;\n\n"+
                     "                char val = '\\0';\n"+
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
                "  public void doSomething(int arg) {\n"+
                "    int var = arg;\n"+
                "    var--;\n"+
                "    if (var > 0) {\n"+
                "      doSomething(var);\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration classDecl = (ClassDeclaration) _project.getType("test.A");
        Node[]           nodes     = { classDecl.getMethods().get(0).getBody() };
        Object[]         args      = { Global.getFactory().createType("String", false),
                                       "val",
                                       new Integer(2)};

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
                     "        int var = arg;\n\n"+
                     "        var--;\n\n"+
                     "        String val = null;\n\n"+
                     "        if (var > 0)\n"+
                     "        {\n"+
                     "            doSomething(var);\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform3() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    int var = arg;\n"+
                "    var--;\n"+
                "    if (var > 0) {\n"+
                "      doSomething(var);\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration classDecl = (ClassDeclaration) _project.getType("test.A");
        Node[]           nodes     = { classDecl.getMethods().get(0).getBody() };
        Object[]         args      = { Global.getFactory().createType("String", false),
                                       "val",
                                       new Integer(3)};

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
                     "        int var = arg;\n\n"+
                     "        var--;\n"+
                     "        if (var > 0)\n"+
                     "        {\n"+
                     "            doSomething(var);\n"+
                     "        }\n\n"+
                     "        String val = null;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform4() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    int var = arg;\n"+
                "    if (var > 0) {\n"+
                "      var--;\n"+
                "      doSomething(var);\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDecl = (ClassDeclaration) _project.getType("test.A");
        Block               block     = classDecl.getMethods().get(0).getBody();
        IfThenElseStatement ifStmt    = (IfThenElseStatement)block.getBlockStatements().get(1);
        Node[]              nodes     = { (Block)ifStmt.getTrueStatement() };
        Object[]            args      = { Global.getFactory().createType(PrimitiveType.BOOLEAN_TYPE, 2),
                                          "val",
                                          new Integer(-10)};

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
                     "        int var = arg;\n\n"+
                     "        if (var > 0)\n"+
                     "        {\n"+
                     "            boolean[][] val = null;\n\n"+
                     "            var--;\n"+
                     "            doSomething(var);\n"+
                     "        }\n"+
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
                "    int var = arg;\n"+
                "    if (var > 0) {\n"+
                "      var--;\n"+
                "      doSomething(var);\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDecl = (ClassDeclaration) _project.getType("test.A");
        Block               block     = classDecl.getMethods().get(0).getBody();
        IfThenElseStatement ifStmt    = (IfThenElseStatement)block.getBlockStatements().get(1);
        Node[]              nodes     = { (Block)ifStmt.getTrueStatement() };
        Object[]            args      = { Global.getFactory().createType(PrimitiveType.BOOLEAN_TYPE, 2),
                                          "val",
                                          new Integer(1)};

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
                     "        int var = arg;\n\n"+
                     "        if (var > 0)\n"+
                     "        {\n"+
                     "            var--;\n\n"+
                     "            boolean[][] val = null;\n\n"+
                     "            doSomething(var);\n"+
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
                "  public void doSomething(int arg) {\n"+
                "    int var = arg;\n"+
                "    if (var > 0) {\n"+
                "      var--;\n"+
                "      doSomething(var);\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDecl = (ClassDeclaration) _project.getType("test.A");
        Block               block     = classDecl.getMethods().get(0).getBody();
        IfThenElseStatement ifStmt    = (IfThenElseStatement)block.getBlockStatements().get(1);
        Node[]              nodes     = { (Block)ifStmt.getTrueStatement() };
        Object[]            args      = { Global.getFactory().createType(PrimitiveType.BOOLEAN_TYPE, 2),
                                          "val",
                                          new Integer(10)};

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
                     "        int var = arg;\n\n"+
                     "        if (var > 0)\n"+
                     "        {\n"+
                     "            var--;\n"+
                     "            doSomething(var);\n\n"+
                     "            boolean[][] val = null;\n"+
                     "        }\n"+
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
                "    int var = 1-arg;\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "           var--;\n"+
                "        doSomething(var);\n"+
                "           break;\n"+
                "      default:\n"+
                "        doSomething(arg-1);\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block            block      = classDecl.getMethods().get(0).getBody();
        SwitchStatement  switchStmt = (SwitchStatement)block.getBlockStatements().get(1);
        Node[]           nodes      = { switchStmt.getCaseBlocks().get(0) };
        Object[]         args       = { Global.getFactory().createType(PrimitiveType.INT_TYPE, 0),
                                        "val",
                                        new Integer(0)};

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
                     "        int var = 1 - arg;\n\n"+
                     "        switch (arg)\n"+
                     "        {\n"+
                     "            case 0 :\n"+
                     "                int val = 0;\n\n"+
                     "                var--;\n"+
                     "                doSomething(var);\n"+
                     "                break;\n"+
                     "            default :\n"+
                     "                doSomething(arg - 1);\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerform8() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    int var = 1-arg;\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "           var--;\n"+
                "        doSomething(var);\n"+
                "           break;\n"+
                "      default:\n"+
                "        doSomething(arg-1);\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block            block      = classDecl.getMethods().get(0).getBody();
        SwitchStatement  switchStmt = (SwitchStatement)block.getBlockStatements().get(1);
        Node[]           nodes      = { switchStmt.getCaseBlocks().get(0) };
        Object[]         args       = { Global.getFactory().createType(PrimitiveType.INT_TYPE, 0),
                                        "val",
                                        new Integer(1)};

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
                     "        int var = 1 - arg;\n\n"+
                     "        switch (arg)\n"+
                     "        {\n"+
                     "            case 0 :\n"+
                     "                var--;\n\n"+
                     "                int val = 0;\n\n"+
                     "                doSomething(var);\n"+
                     "                break;\n"+
                     "            default :\n"+
                     "                doSomething(arg - 1);\n"+
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
                "  public void doSomething(int arg) {\n"+
                "    int var = 1-arg;\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "           var--;\n"+
                "        doSomething(var);\n"+
                "           break;\n"+
                "      default:\n"+
                "        doSomething(arg-1);\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration classDecl  = (ClassDeclaration) _project.getType("test.A");
        Block            block      = classDecl.getMethods().get(0).getBody();
        SwitchStatement  switchStmt = (SwitchStatement)block.getBlockStatements().get(1);
        Node[]           nodes      = { switchStmt.getCaseBlocks().get(1) };
        Object[]         args       = { Global.getFactory().createType(PrimitiveType.INT_TYPE, 0),
                                        "val",
                                        new Integer(1)};

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
                     "        int var = 1 - arg;\n\n"+
                     "        switch (arg)\n"+
                     "        {\n"+
                     "            case 0 :\n"+
                     "                var--;\n"+
                     "                doSomething(var);\n"+
                     "                break;\n"+
                     "            default :\n"+
                     "                doSomething(arg - 1);\n\n"+
                     "                int val = 0;\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }
}
