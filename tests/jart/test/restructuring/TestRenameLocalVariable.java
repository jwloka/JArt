package jart.test.restructuring;
import jart.restructuring.variablelevel.RenameLocalVariable;
import jast.Global;
import jast.ParseException;
import jast.ast.Node;
import jast.ast.nodes.Block;
import jast.ast.nodes.CaseBlock;
import jast.ast.nodes.CatchClause;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.ForStatement;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.SwitchStatement;
import jast.ast.nodes.TryStatement;

public class TestRenameLocalVariable extends TestBase
{

    public TestRenameLocalVariable(String name)
    {
        super(name);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();
        setRestructuring(new RenameLocalVariable());
    }

    public void testAnalyzeCatchParam1() throws ParseException
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

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCatchParam10() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "    try {}\n"+
                "    catch (Exception obj) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeCatchParam11() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {}\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(1);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "idx" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeCatchParam12() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "    for (int idx = 0; idx < 5; idx++) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "idx" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeCatchParam13() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {\n"+
                "      {\n"+
                "        ex.printStackTrace();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCatchParam14() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private Object obj;\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement) methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeCatchParam15() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private Object obj;\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {\n"+
                "      obj.toString();\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeCatchParam16() throws ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  protected Object obj;\n"+
                "}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A extends B {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {\n"+
                "      obj.toString();\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeCatchParam17() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {\n"+
                "      Object obj = null;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCatchParam18() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {\n"+
                "      try {}\n"+
                "      catch (Exception obj) {}\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCatchParam19() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {\n"+
                "      for (int idx = 0; idx < 5; idx++) {}\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "idx" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCatchParam2() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (RuntimeException ex) {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(1).getFormalParameter() };
        Object[]          args       = { "obj" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCatchParam20() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {\n"+
                "      try {}\n"+
                "      catch (Exception ex) {}\n"+
                "    }\n"+
                "    catch (Exception obj) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt1   = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        TryStatement      tryStmt2   = (TryStatement)tryStmt1.getTryBlock().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt2.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeCatchParam21() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception obj) {\n"+
                "      try {}\n"+
                "      catch (Exception ex) {}\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl  = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt1    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        CatchClause       catchClause = tryStmt1.getCatchClauses().get(0);
        TryStatement      tryStmt2    = (TryStatement)catchClause.getCatchBlock().getBlockStatements().get(0);
        Node[]            nodes       = { tryStmt2.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args        = { "obj" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCatchParam22() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "      try {}\n"+
                "      catch (Exception ex) {}\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block      = (Block)forStmt.getLoopStatement();
        TryStatement      tryStmt    = (TryStatement)block.getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "idx" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCatchParam3() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (RuntimeException obj) {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(1).getFormalParameter() };
        Object[]          args       = { "obj" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeCatchParam4() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (RuntimeException ex) {}\n"+
                "    catch (Exception obj) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeCatchParam5() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(Object obj) {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCatchParam6() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {\n"+
                "      ex.printStackTrace();\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCatchParam7() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    Object obj = null;\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement) methodDecl.getBody().getBlockStatements().get(1);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeCatchParam8() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "    Object obj = null;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeCatchParam9() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception obj) {}\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(1);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeForDecl1() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0;;) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { forStmt.getInitDeclarations().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeForDecl10() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {}\n"+
                "    try {} catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { forStmt.getInitDeclarations().get(0) };
        Object[]          args       = { "ex" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeForDecl11() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int num = 0; num < 5; num++) {}\n"+
                "    for (int idx = 0; idx < 5; idx++) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(1);
        Node[]            nodes      = { forStmt.getInitDeclarations().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeForDecl12() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {}\n"+
                "    for (int num = 0; num < 5; num++) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { forStmt.getInitDeclarations().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeForDecl13() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "      {\n"+
                "        idx += 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { forStmt.getInitDeclarations().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeForDecl14() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int num;\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { forStmt.getInitDeclarations().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeForDecl15() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int num;\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < num; idx++) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { forStmt.getInitDeclarations().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeForDecl16() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int num;\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx += num) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { forStmt.getInitDeclarations().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeForDecl17() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int num;\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5;) {\n"+
                "      idx += num;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { forStmt.getInitDeclarations().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeForDecl18() throws ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  protected int num;\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends B {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5;) {\n"+
                "      idx += num;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { forStmt.getInitDeclarations().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeForDecl19() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "      int num = 0;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { forStmt.getInitDeclarations().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeForDecl2() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0, num = 1;;) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { forStmt.getInitDeclarations().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeForDecl20() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "      try {} catch (Exception ex) {}\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { forStmt.getInitDeclarations().get(0) };
        Object[]          args       = { "ex" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeForDecl21() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "      for (int num = 0; num < 5; num++) {}\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { forStmt.getInitDeclarations().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeForDecl22() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {\n"+
                "      for (int idx = 0; idx < 5; idx++) {}\n"+
                "    }\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        ForStatement      forStmt    = (ForStatement)tryStmt.getTryBlock().getBlockStatements().get(0);
        Node[]            nodes      = { forStmt.getInitDeclarations().get(0) };
        Object[]          args       = { "ex" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeForDecl23() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {\n"+
                "      for (int idx = 0; idx < 5; idx++) {}\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl  = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt     = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        CatchClause       catchClause = tryStmt.getCatchClauses().get(0);
        ForStatement      forStmt     = (ForStatement)catchClause.getCatchBlock().getBlockStatements().get(0);
        Node[]            nodes       = { forStmt.getInitDeclarations().get(0) };
        Object[]          args        = { "ex" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeForDecl24() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int num = 0; num < 5; num++)\n"+
                "      for (int idx = 0; idx < 5; idx++) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt1   = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        ForStatement      forStmt2   = (ForStatement)forStmt1.getLoopStatement();
        Node[]            nodes      = { forStmt2.getInitDeclarations().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeForDecl3() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int num = 0, idx = 1;;) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { forStmt.getInitDeclarations().get(1) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeForDecl4() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int num) {\n"+
                "    for (int idx = 0;;) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { forStmt.getInitDeclarations().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeForDecl5() throws ParseException
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

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { forStmt.getInitDeclarations().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeForDecl6() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0;;) {\n"+
                "      idx++;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { forStmt.getInitDeclarations().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeForDecl7() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    int num = 0;\n"+
                "    for (int idx = 0; idx < 5; idx++) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(1);
        Node[]            nodes      = { forStmt.getInitDeclarations().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeForDecl8() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {}\n"+
                "    int num = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { forStmt.getInitDeclarations().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeForDecl9() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {} catch (Exception ex) {}\n"+
                "    for (int idx = 0; idx < 5; idx++) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(1);
        Node[]            nodes      = { forStmt.getInitDeclarations().get(0) };
        Object[]          args       = { "ex" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeInner1() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(final int arg) {\n"+
                "    class Local {\n"+
                "      private class Inner {\n"+
                "        private int val;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "val" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeInner10() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(final Object obj) {\n"+
                "    class Local {\n"+
                "      private class Inner {\n"+
                "        public void test() {\n"+
                "          try {}\n"+
                "          catch (Exception ex) {\n"+
                "            ex = obj;\n"+
                "          }\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "ex" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeInner11() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(final int arg) {\n"+
                "    class Local {\n"+
                "      private class Inner {\n"+
                "        public void test() {\n"+
                "          for (int idx = 0; idx < 5; idx++) {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "idx" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeInner12() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(final int arg) {\n"+
                "    class Local {\n"+
                "      private class Inner {\n"+
                "        public int test() {\n"+
                "          for (int idx = 0; idx < 5; idx++) {\n"+
                "            return arg;\n"+
                "          }\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "idx" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeInner13() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "      class Local {\n"+
                "         class Inner {\n"+
                "           void test(int arg) {}\n"+
                "         }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl1 = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt     = (ForStatement)methodDecl1.getBody().getBlockStatements().get(0);
        Block             block       = (Block)forStmt.getLoopStatement();
        ClassDeclaration  localDecl   = (ClassDeclaration)block.getBlockStatements().get(0);
        ClassDeclaration  innerDecl   = (ClassDeclaration)localDecl.getInnerTypes().get(0);
        MethodDeclaration methodDecl2 = innerDecl.getMethods().get(0);
        Node[]            nodes       = { methodDecl2.getParameterList().getParameters().get(0) };
        Object[]          args        = { "idx" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeInner14() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    final int idx = 0;\n"+
                "    class Local {\n"+
                "      class Inner {\n"+
                "        void test(int arg) {\n"+
                "          arg = idx;\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl1 = _project.getType("test.A").getMethods().get(0);
        ClassDeclaration  localDecl   = (ClassDeclaration)methodDecl1.getBody().getBlockStatements().get(1);
        ClassDeclaration  innerDecl   = (ClassDeclaration)localDecl.getInnerTypes().get(0);
        MethodDeclaration methodDecl2 = innerDecl.getMethods().get(0);
        Node[]            nodes       = { methodDecl2.getParameterList().getParameters().get(0) };
        Object[]          args        = { "idx" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeInner15() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    final int idx = 0;\n"+
                "    class Local {\n"+
                "      class Inner {\n"+
                "        void test() {\n"+
                "          int var = idx;\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl1 = _project.getType("test.A").getMethods().get(0);
        ClassDeclaration  localDecl   = (ClassDeclaration)methodDecl1.getBody().getBlockStatements().get(1);
        ClassDeclaration  innerDecl   = (ClassDeclaration)localDecl.getInnerTypes().get(0);
        MethodDeclaration methodDecl2 = innerDecl.getMethods().get(0);
        Node[]            nodes       = { (Node)methodDecl2.getBody().getBlockStatements().get(0) };
        Object[]          args        = { "idx" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeInner2() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(final int arg) {\n"+
                "    class Local {\n"+
                "      private class Inner {\n"+
                "        private int val = arg;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "val" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        // we get a warning because of the field definition
        assertTrue(hasWarnings());
    }

    public void testAnalyzeInner3() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(final int arg) {\n"+
                "    class Local {\n"+
                "      private class Inner {\n"+
                "        public Inner(int val) {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "val" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeInner4() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(final int arg) {\n"+
                "    class Local {\n"+
                "      private class Inner {\n"+
                "        public void test(int val) {\n"+
                "          val = arg;\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "val" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeInner5() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(final int arg) {\n"+
                "    class Local {\n"+
                "      private class Inner {\n"+
                "        private int attr;\n"+
                "        public Inner() {\n"+
                "          attr = arg;\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "attr" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeInner6() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(final int arg) {\n"+
                "    class Local {\n"+
                "      private class Inner {\n"+
                "        public void test() {\n"+
                "          int val = 0;\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "val" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeInner7() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(final int arg) {\n"+
                "    class Local {\n"+
                "      private class Inner {\n"+
                "        public void test() {\n"+
                "          int val = arg;\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "val" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeInner8() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(final int arg) {\n"+
                "    class Local {\n"+
                "      private class Inner {\n"+
                "        public void test() {\n"+
                "          int nr  = arg;\n"+
                "          int val = nr;\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "val" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeInner9() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(final Object obj) {\n"+
                "    class Local {\n"+
                "      private class Inner {\n"+
                "        public void test() {\n"+
                "          try {}\n"+
                "          catch (Exception ex) {}\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "ex" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocal1() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(int arg) {\n"+
                "    class Local {\n"+
                "       private Object attr;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "attr" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocal10() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(final int arg) {\n"+
                "    class Local {\n"+
                "       int test() {\n"+
                "         for (int idx = 0; idx < 5; idx++) {\n"+
                "           return arg;\n"+
                "         }\n"+
                "       }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "idx" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeLocal11() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(final int arg) {\n"+
                "    class Local {\n"+
                "       private int attr;\n"+
                "       Local() {\n"+
                "       }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "attr" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocal12() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "      class Local {\n"+
                "         void test(int arg) {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl1 = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt     = (ForStatement)methodDecl1.getBody().getBlockStatements().get(0);
        Block             block       = (Block)forStmt.getLoopStatement();
        ClassDeclaration  localDecl   = (ClassDeclaration)block.getBlockStatements().get(0);
        MethodDeclaration methodDecl2 = localDecl.getMethods().get(0);
        Node[]            nodes       = { methodDecl2.getParameterList().getParameters().get(0) };
        Object[]          args        = { "idx" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocal13() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    final int idx = 0;\n"+
                "    class Local {\n"+
                "      void test(int arg) {\n"+
                "        arg = idx;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl1 = _project.getType("test.A").getMethods().get(0);
        ClassDeclaration  localDecl   = (ClassDeclaration)methodDecl1.getBody().getBlockStatements().get(1);
        MethodDeclaration methodDecl2 = localDecl.getMethods().get(0);
        Node[]            nodes       = { methodDecl2.getParameterList().getParameters().get(0) };
        Object[]          args        = { "idx" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeLocal14() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    final int idx = 0;\n"+
                "    class Local {\n"+
                "      void test() {\n"+
                "        int var = idx;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl1 = _project.getType("test.A").getMethods().get(0);
        ClassDeclaration  localDecl   = (ClassDeclaration)methodDecl1.getBody().getBlockStatements().get(1);
        MethodDeclaration methodDecl2 = localDecl.getMethods().get(0);
        Node[]            nodes       = { (Node)methodDecl2.getBody().getBlockStatements().get(0) };
        Object[]          args        = { "idx" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeLocal15() throws ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  protected int attr;\n"+
                "}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(final int arg) {\n"+
                "    class Local extends B {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "attr" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeLocal16() throws ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  protected int attr;\n"+
                "}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(final int arg) {\n"+
                "    class Local extends B {\n"+
                "      private void doSomething() {\n"+
                "        int value = attr;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "attr" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeLocal2() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    final int var = 0;\n"+
                "    class Local {\n"+
                "       private Object attr = var;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(0) };
        Object[]          args       = { "attr" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        // we get a warning because of the field definition
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocal3() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(int arg) {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {\n"+
                "      class Local {\n"+
                "         void test(Object obj) {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocal4() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(final int arg) {\n"+
                "    class Local {\n"+
                "       Local() {\n"+
                "         int var = arg;\n"+
                "       }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "var" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeLocal5() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(final int arg) {\n"+
                "    class Local {\n"+
                "       {\n"+
                "         int val = 0;\n"+
                "       }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "val" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocal6() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(final int arg) {\n"+
                "    class Local {\n"+
                "       {\n"+
                "         int nr  = arg;\n"+
                "         int val = nr;\n"+
                "       }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "val" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocal7() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(int arg) {\n"+
                "    class Local {\n"+
                "       void test() {\n"+
                "         try {}\n"+
                "         catch (Exception ex) {}\n"+
                "       }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "ex" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeLocal8() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(final Object arg) {\n"+
                "    class Local {\n"+
                "       Object test() {\n"+
                "         try {}\n"+
                "         catch (Exception ex) {\n"+
                "           return arg;\n"+
                "         }\n"+
                "       }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "ex" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeLocal9() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(int arg) {\n"+
                "    class Local {\n"+
                "       void test() {\n"+
                "         for (int idx = 0; idx < 5; idx++) {}\n"+
                "       }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "idx" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeParam1() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {}\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeParam10() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private Object attr;\n"+
                "  public int doSomething(int arg) {}\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "attr" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeParam11() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int attr;\n"+
                "  public int doSomething(int arg) {\n"+
                "    attr = arg;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "attr" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        // we get a warning because of the field definition
        assertTrue(hasWarnings());
    }

    public void testAnalyzeParam12() throws ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  protected int attr;\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends B {\n"+
                "  public A(int arg) {}\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDecl  = (ClassDeclaration)_project.getType("test.A");
        ConstructorDeclaration constrDecl = classDecl.getConstructors().get(0);
        Node[]                 nodes      = { constrDecl.getParameterList().getParameters().get(0) };
        Object[]               args       = { "attr" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        // we get a warning because of the field definition in B
        assertTrue(hasWarnings());
    }

    public void testAnalyzeParam13() throws ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  protected int attr;\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends B {\n"+
                "  public A(int arg) {\n"+
                "    attr = arg;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDecl  = (ClassDeclaration)_project.getType("test.A");
        ConstructorDeclaration constrDecl = classDecl.getConstructors().get(0);
        Node[]                 nodes      = { constrDecl.getParameterList().getParameters().get(0) };
        Object[]               args       = { "attr" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        // we get a warning because of the field definition in B
        assertTrue(hasWarnings());
    }

    public void testAnalyzeParam14() throws ParseException
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

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "val" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeParam2() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public A(int arg, int num) {}\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDecl  = (ClassDeclaration)_project.getType("test.A");
        ConstructorDeclaration constrDecl = classDecl.getConstructors().get(0);
        Node[]                 nodes      = { constrDecl.getParameterList().getParameters().get(0) };
        Object[]               args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeParam3() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int num, int arg) {}\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(1) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeParam4() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public A(int arg) {\n"+
                "    int var = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDecl  = (ClassDeclaration)_project.getType("test.A");
        ConstructorDeclaration constrDecl = classDecl.getConstructors().get(0);
        Node[]                 nodes      = { constrDecl.getParameterList().getParameters().get(0) };
        Object[]               args       = { "var" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeParam5() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    try\n"+
                "    {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "ex" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeParam6() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public A(int arg) {\n"+
                "    for (int idx = 0; idx < 5; idx++) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDecl  = (ClassDeclaration)_project.getType("test.A");
        ConstructorDeclaration constrDecl = classDecl.getConstructors().get(0);
        Node[]                 nodes      = { constrDecl.getParameterList().getParameters().get(0) };
        Object[]               args       = { "idx" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeParam7() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(int arg) {\n"+
                "    return arg;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeParam8() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public A(int arg) {\n"+
                "    if (arg > 0) {\n"+
                "      int num = 0;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDecl  = (ClassDeclaration)_project.getType("test.A");
        ConstructorDeclaration constrDecl = classDecl.getConstructors().get(0);
        Node[]                 nodes      = { constrDecl.getParameterList().getParameters().get(0) };
        Object[]               args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeParam9() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    {\n"+
                "      float num = 0.0;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeParamAnonymous1() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public Object doSomething(int arg) {\n"+
                "    return new Object() {\n"+
                "       private int attr;\n"+
                "    };\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "attr" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeParamAnonymous2() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public Object doSomething(int arg) {\n"+
                "    return new Object() {\n"+
                "       private int attr = arg;\n"+
                "    };\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "val" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeVarDecl1() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    int var = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeVarDecl10() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    int var = 0;\n"+
                "    {\n"+
                "      var = 1;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeVarDecl11() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int attr;\n"+
                "  public void doSomething() {\n"+
                "    int var = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(0) };
        Object[]          args       = { "attr" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeVarDecl12() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int attr;\n"+
                "  public void doSomething() {\n"+
                "    int var = attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(0) };
        Object[]          args       = { "attr" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        // we get a warning because of the field declaration
        assertTrue(hasWarnings());
    }

    public void testAnalyzeVarDecl13() throws ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  protected int attr;\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends B {\n"+
                "  public void doSomething() {\n"+
                "    int var = attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(0) };
        Object[]          args       = { "attr" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        // we get a warning because of the field declaration
        assertTrue(hasWarnings());
    }

    public void testAnalyzeVarDecl14() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {\n"+
                "      Object obj = null;\n"+
                "    }\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { (Node)tryStmt.getTryBlock().getBlockStatements().get(0) };
        Object[]          args       = { "ex" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeVarDecl15() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {\n"+
                "      Object obj = null;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl  = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt     = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block       = tryStmt.getCatchClauses().get(0).getCatchBlock();
        Node[]            nodes       = { (Node)block.getBlockStatements().get(0) };
        Object[]          args        = { "ex" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeVarDecl16() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {\n"+
                "      int var = 0;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Block             block      = (Block)forStmt.getLoopStatement();
        Node[]            nodes      = { (Node)block.getBlockStatements().get(0) };
        Object[]          args       = { "idx" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeVarDecl17() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    {\n"+
                "      int idx = 0;\n"+
                "    }\n"+
                "    int var = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(1) };
        Object[]          args       = { "idx" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        // we get a warning because of the other variable declaration
        assertTrue(hasWarnings());
    }

    public void testAnalyzeVarDecl18() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    int var = 0;\n"+
                "    {\n"+
                "      int idx = 0;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(0) };
        Object[]          args       = { "idx" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeVarDecl19() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    {\n"+
                "      Object obj = null;\n"+
                "    }\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { (Node)block.getBlockStatements().get(0) };
        Object[]          args       = { "ex" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeVarDecl2() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    int var = 0, num = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeVarDecl20() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    {\n"+
                "      int var = 0;\n"+
                "    }\n"+
                "    for (int idx = 0; idx < 5; idx++) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { (Node)block.getBlockStatements().get(0) };
        Object[]          args       = { "idx" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeVarDecl21() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    int var = 0;\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        int val = 0;\n"+
                "        break;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(0) };
        Object[]          args       = { "val" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeVarDecl22() throws ParseException
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
                "    int var = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(1) };
        Object[]          args       = { "val" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeVarDecl23() throws ParseException
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

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        SwitchStatement   switchStmt = (SwitchStatement)methodDecl.getBody().getBlockStatements().get(0);
        CaseBlock         caseBlock  = switchStmt.getCaseBlocks().get(0);
        Node[]            nodes      = { (Node)caseBlock.getBlockStatements().get(0) };
        Object[]          args       = { "arg" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeVarDecl24() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {\n"+
                "    switch (arg) {\n"+
                "      case 0:\n"+
                "        int var = 0;\n"+
                "        break;\n"+
                "      case 0:\n"+
                "        int val = 0;\n"+
                "        break;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        SwitchStatement   switchStmt = (SwitchStatement)methodDecl.getBody().getBlockStatements().get(0);
        CaseBlock         caseBlock  = switchStmt.getCaseBlocks().get(0);
        Node[]            nodes      = { (Node)caseBlock.getBlockStatements().get(0) };
        Object[]          args       = { "val" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeVarDecl25() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    int var = 0;\n"+
                "    {\n"+
                "    }\n"+
                "    int val = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(2) };
        Object[]          args       = { "var" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeVarDecl3() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    int num = 0, var = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(1) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeVarDecl4() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int num) {\n"+
                "    int var = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeVarDecl5() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething() {\n"+
                "    int var = 0;\n"+
                "    return var;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(0) };
        Object[]          args       = { "num" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeVarDecl6() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    Object obj = null;\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(0) };
        Object[]          args       = { "ex" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeVarDecl7() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try {}\n"+
                "    catch (Exception ex) {}\n"+
                "    Object obj = null;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(1) };
        Object[]          args       = { "ex" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testAnalyzeVarDecl8() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    int var = 0;\n"+
                "    for (int idx = 0; idx < 5; idx++) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(0) };
        Object[]          args       = { "idx" };

        assertTrue(initialize(nodes, args));
        assertTrue(!analyze());
        assertTrue(hasErrors());
        assertTrue(!hasWarnings());
    }

    public void testAnalyzeVarDecl9() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    for (int idx = 0; idx < 5; idx++) {}\n"+
                "    int var = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(1) };
        Object[]          args       = { "idx" };

        assertTrue(initialize(nodes, args));
        assertTrue(analyze());
        assertTrue(!hasErrors());
        assertTrue(hasWarnings());
    }

    public void testInitial()
    {
        assertTrue(!hasFatalErrors());
        assertTrue(!hasErrors());
        assertTrue(!hasWarnings());
        assertTrue(!hasNotes());
    }

    public void testInitialize1()
    {
        assertTrue(!initialize(null, null));
        assertTrue(hasFatalErrors());
    }

    public void testInitialize2()
    {
        Node[]   nodes = new Node[0];
        Object[] args  = { "test" };

        assertTrue(!initialize(nodes, args));
        assertTrue(hasFatalErrors());
    }

    public void testInitialize3()
    {
        Node[]   nodes = { Global.getFactory().createEmptyStatement()};
        Object[] args  = new Object[0];

        assertTrue(!initialize(nodes, args));
        assertTrue(hasFatalErrors());
    }

    public void testInitialize4()
    {
        Node[]   nodes = { Global.getFactory().createEmptyStatement()};
        Object[] args  = { "test" };

        assertTrue(!initialize(nodes, args));
        assertTrue(hasFatalErrors());
    }

    public void testInitialize5() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    int var = 0;\n"+
                "  }\n"+
                "}\n",
                true);

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(0) };
        Object[]          args       = { "test" };

        assertTrue(initialize(nodes, args));
        assertTrue(!hasFatalErrors());
    }

    public void testInitialize6() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int arg) {}\n"+
                "}\n",
                true);

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "test" };

        assertTrue(initialize(nodes, args));
        assertTrue(!hasFatalErrors());
    }

    public void testInitialize7() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(int arg) {\n"+
                "    return arg;\n"+
                "  }\n"+
                "}\n",
                true);

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { returnStmt.getReturnValue() };
        Object[]          args       = { "test" };

        assertTrue(initialize(nodes, args));
        assertTrue(!hasFatalErrors());
    }

    public void testInitialize8() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    try\n"+
                "    {}\n"+
                "    catch (Exception ex) {}\n"+
                "  }\n"+
                "}\n",
                true);

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "test" };

        assertTrue(initialize(nodes, args));
        assertTrue(!hasFatalErrors());
    }

    public void testInitialize9() throws ParseException
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

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        ForStatement      forStmt    = (ForStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { forStmt.getInitDeclarations().get(0) };
        Object[]          args       = { "test" };

        assertTrue(initialize(nodes, args));
        assertTrue(!hasFatalErrors());
    }

    public void testPerformCatchParam1() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public abstract class A {\n"+
                "  private abstract void doSomething() throws java.io.IOException;\n"+
                "  public void test() {\n"+
                "    try {\n"+
                "      doSomething();\n"+
                "    } catch (java.io.IOException ex) {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(1);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

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
                     "    private abstract void doSomething() throws java.io.IOException;\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        try\n"+
                     "        {\n"+
                     "            doSomething();\n"+
                     "        }\n"+
                     "        catch (java.io.IOException obj)\n"+
                     "        {}\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformCatchParam2() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public abstract class A {\n"+
                "  private abstract void doSomething() throws java.io.IOException;\n"+
                "  public void test() {\n"+
                "    try {\n"+
                "      doSomething();\n"+
                "    } catch (java.io.IOException ex) {\n"+
                "      ex.printStackTrace();\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(1);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

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
                     "    private abstract void doSomething() throws java.io.IOException;\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        try\n"+
                     "        {\n"+
                     "            doSomething();\n"+
                     "        }\n"+
                     "        catch (java.io.IOException obj)\n"+
                     "        {\n"+
                     "            obj.printStackTrace();\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformCatchParam3() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public abstract class A {\n"+
                "  private abstract void doSomething() throws java.io.IOException;\n"+
                "  public Object test() {\n"+
                "    try {\n"+
                "      doSomething();\n"+
                "    } catch (final java.io.IOException ex) {\n"+
                "      return new Object() {\n"+
                "        public String toString() {\n"+
                "          return ex.printStackTrace();\n"+
                "        }\n"+
                "      };\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(1);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

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
                     "    private abstract void doSomething() throws java.io.IOException;\n\n"+
                     "    public Object test()\n"+
                     "    {\n"+
                     "        try\n"+
                     "        {\n"+
                     "            doSomething();\n"+
                     "        }\n"+
                     "        catch (final java.io.IOException obj)\n"+
                     "        {\n"+
                     "            return new Object()\n"+
                     "                   {\n"+
                     "                       public String toString()\n"+
                     "                       {\n"+
                     "                           return obj.printStackTrace();\n"+
                     "                       }\n"+
                     "                   };\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformCatchParam4() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public abstract class A {\n"+
                "  private abstract void doSomething() throws java.io.IOException;\n"+
                "  public void test() {\n"+
                "    try {\n"+
                "      doSomething();\n"+
                "    } catch (final java.io.IOException ex) {\n"+
                "      class Local {\n"+
                "        public String toString() {\n"+
                "          return ex.printStackTrace();\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(1);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

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
                     "    private abstract void doSomething() throws java.io.IOException;\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        try\n"+
                     "        {\n"+
                     "            doSomething();\n"+
                     "        }\n"+
                     "        catch (final java.io.IOException obj)\n"+
                     "        {\n"+
                     "            class Local\n"+
                     "            {\n"+
                     "                public String toString()\n"+
                     "                {\n"+
                     "                    return obj.printStackTrace();\n"+
                     "                }\n"+
                     "            }\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformCatchParam5() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public abstract class A {\n"+
                "  private Object ex;\n"+
                "  private abstract void doSomething() throws java.io.IOException;\n"+
                "  public void test() {\n"+
                "    try {\n"+
                "      doSomething();\n"+
                "    } catch (java.io.IOException ex) {\n"+
                "      this.ex = ex;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(1);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

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
                     "    private Object ex;\n\n\n"+
                     "    private abstract void doSomething() throws java.io.IOException;\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        try\n"+
                     "        {\n"+
                     "            doSomething();\n"+
                     "        }\n"+
                     "        catch (java.io.IOException obj)\n"+
                     "        {\n"+
                     "            this.ex = obj;\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformCatchParam6() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public abstract class A {\n"+
                "  private Object obj;\n"+
                "  private abstract void doSomething() throws java.io.IOException;\n"+
                "  public void test() {\n"+
                "    try {\n"+
                "      doSomething();\n"+
                "    } catch (java.io.IOException ex) {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(1);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

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
                     "    private Object obj;\n\n\n"+
                     "    private abstract void doSomething() throws java.io.IOException;\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        try\n"+
                     "        {\n"+
                     "            doSomething();\n"+
                     "        }\n"+
                     "        catch (java.io.IOException obj)\n"+
                     "        {}\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformCatchParam7() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public abstract class A {\n"+
                "  private abstract void doSomething() throws java.io.IOException;\n"+
                "  public void test() {\n"+
                "    try {\n"+
                "      doSomething();\n"+
                "    } catch (final java.io.IOException ex) {\n"+
                "      class Local {\n"+
                "        public String toString() {\n"+
                "          return ex.toString();\n"+
                "        }\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(1);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

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
                     "    private abstract void doSomething() throws java.io.IOException;\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        try\n"+
                     "        {\n"+
                     "            doSomething();\n"+
                     "        }\n"+
                     "        catch (final java.io.IOException obj)\n"+
                     "        {\n"+
                     "            class Local\n"+
                     "            {\n"+
                     "                public String toString()\n"+
                     "                {\n"+
                     "                    return obj.toString();\n"+
                     "                }\n"+
                     "            }\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformCatchParam8() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public abstract class A {\n"+
                "  private abstract void doSomething() throws java.io.IOException;\n"+
                "  public void test() {\n"+
                "    try {\n"+
                "      doSomething();\n"+
                "    } catch (java.io.IOException ex) {\n"+
                "      ex.printStackTrace();\n"+
                "    }\n"+
                "    Object ex;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(1);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

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
                     "    private abstract void doSomething() throws java.io.IOException;\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        try\n"+
                     "        {\n"+
                     "            doSomething();\n"+
                     "        }\n"+
                     "        catch (java.io.IOException obj)\n"+
                     "        {\n"+
                     "            obj.printStackTrace();\n"+
                     "        }\n\n"+
                     "        Object ex;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformCatchParam9() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public abstract class A {\n"+
                "  private abstract void doSomething() throws java.io.IOException;\n"+
                "  public void test() {\n"+
                "    try {\n"+
                "      doSomething();\n"+
                "    } catch (java.io.IOException ex) {\n"+
                "      ex.printStackTrace();\n"+
                "    }\n"+
                "    Object obj;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(1);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { tryStmt.getCatchClauses().get(0).getFormalParameter() };
        Object[]          args       = { "obj" };

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
                     "    private abstract void doSomething() throws java.io.IOException;\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        try\n"+
                     "        {\n"+
                     "            doSomething();\n"+
                     "        }\n"+
                     "        catch (java.io.IOException obj)\n"+
                     "        {\n"+
                     "            obj.printStackTrace();\n"+
                     "        }\n\n"+
                     "        Object obj;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformLocalVariable1() throws ParseException
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

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(0) };
        Object[]          args       = { "value" };

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
                     "    public void doSomething()\n"+
                     "    {\n"+
                     "        int value = 0;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformLocalVariable10() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public abstract class A {\n"+
                "  private abstract void doSomething() throws java.io.IOException;\n"+
                "  public void test() {\n"+
                "    try {\n"+
                "      int val = 0;\n"+
                "      doSomething();\n"+
                "    } catch (java.io.IOException val) {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(1);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { (Node)tryStmt.getTryBlock().getBlockStatements().get(0) };
        Object[]          args       = { "ex" };

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
                     "    private abstract void doSomething() throws java.io.IOException;\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        try\n"+
                     "        {\n"+
                     "            int ex = 0;\n\n"+
                     "            doSomething();\n"+
                     "        }\n"+
                     "        catch (java.io.IOException val)\n"+
                     "        {}\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformLocalVariable11() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int value;\n"+
                "  public int doSomething() {\n"+
                "    int val = 1;\n"+
                "    return val * val;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(0) };
        Object[]          args       = { "value" };

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
                     "    private int value;\n\n\n"+
                     "    public int doSomething()\n"+
                     "    {\n"+
                     "        int value = 1;\n\n"+
                     "        return value * value;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformLocalVariable12() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public Object doSomething() {\n"+
                "    final int val = 0;\n"+
                "    return new Object() {\n"+
                "        public String toString() {\n"+
                "          return String.valueOf(val);\n"+
                "        }\n"+
                "      };\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(0) };
        Object[]          args       = { "value" };

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
                     "    public Object doSomething()\n"+
                     "    {\n"+
                     "        final int value = 0;\n\n"+
                     "        return new Object()\n"+
                     "               {\n"+
                     "                   public String toString()\n"+
                     "                   {\n"+
                     "                       return String.valueOf(value);\n"+
                     "                   }\n"+
                     "               };\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformLocalVariable13() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public Object doSomething() {\n"+
                "    final int val = 0;\n"+
                "    return new Object() {\n"+
                "        public String toString() {\n"+
                "          String val = \"\";\n"+
                "          return val;\n"+
                "        }\n"+
                "      };\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl1   = _project.getType("test.A").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl1.getBody().getBlockStatements().get(1);
        Instantiation     instantiation = (Instantiation)returnStmt.getReturnValue();
        MethodDeclaration methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        Node[]            nodes         = { (Node)methodDecl2.getBody().getBlockStatements().get(0) };
        Object[]          args          = { "value" };

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
                     "    public Object doSomething()\n"+
                     "    {\n"+
                     "        final int val = 0;\n\n"+
                     "        return new Object()\n"+
                     "               {\n"+
                     "                   public String toString()\n"+
                     "                   {\n"+
                     "                       String value = \"\";\n\n"+
                     "                       return value;\n"+
                     "                   }\n"+
                     "               };\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformLocalVariable2() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    int val = 0;\n"+
                "    val = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { (Node)methodDecl.getBody().getBlockStatements().get(0) };
        Object[]          args       = { "value" };

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
                     "    public void doSomething()\n"+
                     "    {\n"+
                     "        int value = 0;\n\n"+
                     "        value = 1;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformLocalVariable3() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    {\n"+
                "      int val = 0;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { (Node)block.getBlockStatements().get(0) };
        Object[]          args       = { "value" };

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
                     "    public void doSomething()\n"+
                     "    {\n"+
                     "        {\n"+
                     "            int value = 0;\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformLocalVariable4() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    {\n"+
                "      int val = 0;\n"+
                "      val = 1;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { (Node)block.getBlockStatements().get(0) };
        Object[]          args       = { "value" };

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
                     "    public void doSomething()\n"+
                     "    {\n"+
                     "        {\n"+
                     "            int value = 0;\n\n"+
                     "            value = 1;\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformLocalVariable5() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    {\n"+
                "      int val = 0;\n"+
                "    }\n"+
                "    int val = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { (Node)block.getBlockStatements().get(0) };
        Object[]          args       = { "value" };

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
                     "    public void doSomething()\n"+
                     "    {\n"+
                     "        {\n"+
                     "            int value = 0;\n"+
                     "        }\n\n"+
                     "        int val = 1;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformLocalVariable6() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    {\n"+
                "      int val = 0;\n"+
                "    }\n"+
                "    int value = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { (Node)block.getBlockStatements().get(0) };
        Object[]          args       = { "value" };

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
                     "    public void doSomething()\n"+
                     "    {\n"+
                     "        {\n"+
                     "            int value = 0;\n"+
                     "        }\n\n"+
                     "        int value = 1;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformLocalVariable7() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    {\n"+
                "      int val = 0;\n"+
                "    }\n"+
                "    {\n"+
                "      int val = 1;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { (Node)block.getBlockStatements().get(0) };
        Object[]          args       = { "value" };

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
                     "    public void doSomething()\n"+
                     "    {\n"+
                     "        {\n"+
                     "            int value = 0;\n"+
                     "        }\n"+
                     "        {\n"+
                     "            int val = 1;\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformLocalVariable8() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    {\n"+
                "      int val = 0;\n"+
                "    }\n"+
                "    {\n"+
                "      int value = 1;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Block             block      = (Block)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { (Node)block.getBlockStatements().get(0) };
        Object[]          args       = { "value" };

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
                     "    public void doSomething()\n"+
                     "    {\n"+
                     "        {\n"+
                     "            int value = 0;\n"+
                     "        }\n"+
                     "        {\n"+
                     "            int value = 1;\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformLocalVariable9() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public abstract class A {\n"+
                "  private abstract void doSomething() throws java.io.IOException;\n"+
                "  public void test() {\n"+
                "    try {\n"+
                "      int val = 0;\n"+
                "      doSomething();\n"+
                "    } catch (java.io.IOException ex) {\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(1);
        TryStatement      tryStmt    = (TryStatement)methodDecl.getBody().getBlockStatements().get(0);
        Node[]            nodes      = { (Node)tryStmt.getTryBlock().getBlockStatements().get(0) };
        Object[]          args       = { "ex" };

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
                     "    private abstract void doSomething() throws java.io.IOException;\n\n"+
                     "    public void test()\n"+
                     "    {\n"+
                     "        try\n"+
                     "        {\n"+
                     "            int ex = 0;\n\n"+
                     "            doSomething();\n"+
                     "        }\n"+
                     "        catch (java.io.IOException ex)\n"+
                     "        {}\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformMethodParam1() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(int val) {\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "arg" };

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
                     "    {}\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformMethodParam2() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int doSomething(final int val) {\n"+
                "    int value = val;\n"+
                "    return val*value;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "arg" };

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
                     "    public int doSomething(final int arg)\n"+
                     "    {\n"+
                     "        int value = arg;\n\n"+
                     "        return arg * value;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformMethodParam3() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public Object doSomething(final int val) {\n"+
                "    return new Object() {\n"+
                "        private int attr = val;\n"+
                "      };\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "arg" };

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
                     "    public Object doSomething(final int arg)\n"+
                     "    {\n"+
                     "        return new Object()\n"+
                     "               {\n"+
                     "                   private int attr = arg;\n"+
                     "               };\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformMethodParam4() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething(final int val) {\n"+
                "    class Local {\n"+
                "        private int attr = val;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "arg" };

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
                     "            private int attr = arg;\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformMethodParam5() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int val;\n"+
                "  public int doSomething(int val) {\n"+
                "    return val*this.val;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "arg" };

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
                     "    private int val;\n\n\n"+
                     "    public int doSomething(int arg)\n"+
                     "    {\n"+
                     "        return arg * this.val;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformMethodParam6() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private Object arg;\n"+
                "  public int doSomething(int val) {\n"+
                "    return val*val;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "arg" };

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
                     "    private Object arg;\n\n\n"+
                     "    public int doSomething(int arg)\n"+
                     "    {\n"+
                     "        return arg * arg;\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformMethodParam7() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public Object doSomething(final int val) {\n"+
                "    return new Object() {\n"+
                "        public String toString() {\n"+
                "          return String.valueOf(val);\n"+
                "        }\n"+
                "      };\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl = _project.getType("test.A").getMethods().get(0);
        Node[]            nodes      = { methodDecl.getParameterList().getParameters().get(0) };
        Object[]          args       = { "arg" };

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
                     "    public Object doSomething(final int arg)\n"+
                     "    {\n"+
                     "        return new Object()\n"+
                     "               {\n"+
                     "                   public String toString()\n"+
                     "                   {\n"+
                     "                       return String.valueOf(arg);\n"+
                     "                   }\n"+
                     "               };\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }

    public void testPerformMethodParam8() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public void doSomething() {\n"+
                "    final int val = 0;\n"+
                "    class Local {\n"+
                "        public String doSomething(int val) {\n"+
                "          return String.valueOf(val);\n"+
                "        }\n"+
                "      }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl1    = _project.getType("test.A").getMethods().get(0);
        ClassDeclaration  localClassDecl = (ClassDeclaration)methodDecl1.getBody().getBlockStatements().get(1);
        MethodDeclaration methodDecl2    = localClassDecl.getMethods().get(0);
        Node[]            nodes          = { methodDecl2.getParameterList().getParameters().get(0) };
        Object[]          args           = { "value" };

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
                     "    public void doSomething()\n"+
                     "    {\n"+
                     "        final int val = 0;\n\n\n"+
                     "        class Local\n"+
                     "        {\n"+
                     "            public String doSomething(int value)\n"+
                     "            {\n"+
                     "                return String.valueOf(value);\n"+
                     "            }\n"+
                     "        }\n"+
                     "    }\n"+
                     "}\n",
                     prettyPrint("test.A"));
    }
}
