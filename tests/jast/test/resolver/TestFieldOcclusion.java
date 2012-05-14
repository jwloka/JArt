package jast.test.resolver;

import jast.ParseException;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.TypeDeclaration;
import antlr.ANTLRException;
public class TestFieldOcclusion extends TestBase
{

    public TestFieldOcclusion(String name)
    {
        super(name);
    }

    public void testLocal_CurrentVsBaseEnclosing() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private Object _attr;\n"+
                "    private class C extends B {\n"+
                "      private Object _attr;\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration    methodDecl  = typeDecl.getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of current type (C)
        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_CurrentVsDirectBase() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends D {\n"+
                "      private Object _attr;\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration    methodDecl  = typeDecl.getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of current type (C)
        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_CurrentVsEnclosing() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private Object _attr;\n"+
                "    private class C {\n"+
                "      private Object _attr;\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration    methodDecl  = typeDecl.getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of current type (C)
        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_CurrentVsEnclosingBase() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B extends D {\n"+
                "    private class C {\n"+
                "      private Object _attr;\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration    methodDecl  = typeDecl.getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of current type (C)
        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_CurrentVsRemoteBase() throws ANTLRException, ParseException
    {
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test.D",
                "package test;\n"+
                "public class D extends E {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends D {\n"+
                "      private Object _attr;\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration    methodDecl  = typeDecl.getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of current type (C)
        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_CurrentVsTopLevel() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private Object _attr;\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private Object _attr;\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration    methodDecl  = typeDecl.getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of current type (C)
        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_CurrentVsTopLevelBase() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private Object _attr;\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration    methodDecl  = typeDecl.getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of current type (C)
        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_DirectBaseVsEnclosing1() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected Object _attr;\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public Object _attr;\n"+
                "    private class C extends test1.D {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of base type (D)
        assertEquals(_project.getType("test1.D").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_DirectBaseVsEnclosing2() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  private Object _attr;\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private Object _attr;\n"+
                "    private class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test.A.B");
        MethodDeclaration    methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of enclosing type (B)
        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_DirectBaseVsEnclosingBase1() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected Object _attr;\n"+
                "}\n",
                false);
        addType("test2.E",
                "package test2;\n"+
                "public class E {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  public class B extends E {\n"+
                "    private class C extends test1.D {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of base type (D)
        assertEquals(_project.getType("test1.D").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_DirectBaseVsEnclosingBase2() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  private Object _attr;\n"+
                "}\n",
                false);
        addType("test2.E",
                "package test2;\n"+
                "public class E {\n"+
                "  protected Object _attr;\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public class B extends test2.E {\n"+
                "    private class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test1.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of enclosing base type (E)
        assertEquals(_project.getType("test2.E").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_DirectBaseVsTopLevel1() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected Object _attr;\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  public Object _attr;\n"+
                "  private class B {\n"+
                "    private class C extends test1.D {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of base type (D)
        assertEquals(_project.getType("test1.D").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_DirectBaseVsTopLevel2() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  Object _attr;\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private Object _attr;\n"+
                "  private class B {\n"+
                "    private class C extends test1.D {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of top-level type (A)
        assertEquals(_project.getType("test2.A").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_DirectBaseVsTopLevelBase1() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected Object _attr;\n"+
                "}\n",
                false);
        addType("test2.E",
                "package test2;\n"+
                "public class E {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A extends E {\n"+
                "  public class B {\n"+
                "    private class C extends test1.D {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of base type (D)
        assertEquals(_project.getType("test1.D").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_DirectBaseVsTopLevelBase2() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  Object _attr;\n"+
                "}\n",
                false);
        addType("test2.E",
                "package test2;\n"+
                "public class E {\n"+
                "  Object _attr;\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A extends E {\n"+
                "  public class B {\n"+
                "    private class C extends test1.D {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of top-level base type (E)
        assertEquals(_project.getType("test2.E").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_EnclosingBaseVsTopLevel1() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected Object _attr;\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private Object _attr;\n"+
                "  private class B extends test1.D {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of base type of enclosing type (D)
        assertEquals(_project.getType("test1.D").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_EnclosingBaseVsTopLevel2() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  Object _attr;\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private Object _attr;\n"+
                "  private class B extends D {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of base type of enclosing type (D)
        assertEquals(_project.getType("test.D").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_EnclosingBaseVsTopLevel3() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  Object _attr;\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private Object _attr;\n"+
                "  private class B extends test1.D {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of base type of top-level type (A)
        assertEquals(_project.getType("test2.A").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_EnclosingBaseVsTopLevelBase1() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected Object _attr;\n"+
                "}\n",
                false);
        addType("test2.E",
                "package test2;\n"+
                "public class E {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A extends E {\n"+
                "  private class B extends test1.D {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of base type of enclosing type (D)
        assertEquals(_project.getType("test1.D").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_EnclosingBaseVsTopLevelBase2() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  private Object _attr;\n"+
                "}\n",
                false);
        addType("test2.E",
                "package test2;\n"+
                "public class E {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A extends E {\n"+
                "  private class B extends test1.D {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of base type of top-level type (E)
        assertEquals(_project.getType("test2.E").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_EnclosingVsEnclosingBase() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B extends D {\n"+
                "    private Object _attr;\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test.A.B");
        MethodDeclaration    methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of direct enclosing type (B)
        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_EnclosingVsTopLevel() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private Object _attr;\n"+
                "  private class B {\n"+
                "    private Object _attr;\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test.A.B");
        MethodDeclaration    methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of direct enclosing type (B)
        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_EnclosingVsTopLevelBase() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  private class B {\n"+
                "    private Object _attr;\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test.A.B");
        MethodDeclaration    methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of direct enclosing type (B)
        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_RemoteBaseVsEnclosing1() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected Object _attr;\n"+
                "}\n",
                false);
        addType("test2.E",
                "package test2;\n"+
                "public class E extends test1.D {}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public Object _attr;\n"+
                "    private class C extends test2.E {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test3.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of base type (D)
        assertEquals(_project.getType("test1.D").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_RemoteBaseVsEnclosing2() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  private Object _attr;\n"+
                "}\n",
                false);
        addType("test2.E",
                "package test2;\n"+
                "public class E extends test1.D {}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private Object _attr;\n"+
                "    private class C extends test2.E {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test3.A.B");
        MethodDeclaration    methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of enclosing type (B)
        assertEquals(typeDecl.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_RemoteBaseVsEnclosingBase1() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected Object _attr;\n"+
                "}\n",
                false);
        addType("test2.E",
                "package test2;\n"+
                "public class E extends test1.D {}\n",
                false);
        addType("test3.F",
                "package test3;\n"+
                "public class F {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  public class B extends F {\n"+
                "    private class C extends test2.E {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test3.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of base type (D)
        assertEquals(_project.getType("test1.D").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_RemoteBaseVsEnclosingBase2() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  Object _attr;\n"+
                "}\n",
                false);
        addType("test2.E",
                "package test2;\n"+
                "public class E extends test1.D {}\n",
                false);
        addType("test2.F",
                "package test2;\n"+
                "public class F {\n"+
                "  protected Object _attr;\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public class B extends test2.F {\n"+
                "    private class C extends test2.E {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test1.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of enclosing base type (F)
        assertEquals(_project.getType("test2.F").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_RemoteBaseVsTopLevel1() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  Object _attr;\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E extends D {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public Object _attr;\n"+
                "  private class B {\n"+
                "    private class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of base type (D)
        assertEquals(_project.getType("test.D").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_RemoteBaseVsTopLevel2() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  Object _attr;\n"+
                "}\n",
                false);
        addType("test2.E",
                "package test2;\n"+
                "public class E extends test1.D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private Object _attr;\n"+
                "  private class B {\n"+
                "    private class C extends E {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of top-level type (A)
        assertEquals(_project.getType("test2.A").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_RemoteBaseVsTopLevelBase1() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected Object _attr;\n"+
                "}\n",
                false);
        addType("test2.E",
                "package test2;\n"+
                "public class E extends test1.D {}\n",
                false);
        addType("test3.F",
                "package test3;\n"+
                "public class F {\n"+
                "  Object _attr;\n"+
                "}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A extends F {\n"+
                "  public class B {\n"+
                "    private class C extends test2.E {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test3.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of base type (D)
        assertEquals(_project.getType("test1.D").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_RemoteBaseVsTopLevelBase2() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  Object _attr;\n"+
                "}\n",
                false);
        addType("test2.E",
                "package test2;\n"+
                "public class E extends test1.D {}\n",
                false);
        addType("test2.F",
                "package test2;\n"+
                "public class F {\n"+
                "  Object _attr;\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A extends F {\n"+
                "  public class B {\n"+
                "    private class C extends E {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of top-level base type (F)
        assertEquals(_project.getType("test2.F").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_RemoteEnclosingBaseVsTopLevel1() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected Object _attr;\n"+
                "}\n",
                false);
        addType("test2.E",
                "package test2;\n"+
                "public class E extends test1.D {}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private Object _attr;\n"+
                "  private class B extends test2.E {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test3.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of remote base type of enclosing type (D)
        assertEquals(_project.getType("test1.D").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_RemoteEnclosingBaseVsTopLevel2() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  Object _attr;\n"+
                "}\n",
                false);
        addType("test.E",
                "package test;\n"+
                "public class E extends D {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private Object _attr;\n"+
                "  private class B extends E {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of remote base type of enclosing type (D)
        assertEquals(_project.getType("test.D").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_RemoteEnclosingBaseVsTopLevel3() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  Object _attr;\n"+
                "}\n",
                false);
        addType("test2.E",
                "package test2;\n"+
                "public class E extends test1.D {}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private Object _attr;\n"+
                "  private class B extends test2.E {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test1.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of top-level type (A)
        assertEquals(_project.getType("test1.A").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_RemoteEnclosingBaseVsTopLevelBase1() throws ANTLRException, ParseException
    {
        addType("test2.E",
                "package test2;\n"+
                "class D {\n"+
                "  Object _attr;\n"+
                "}\n"+
                "public class E extends D {}\n",
                false);
        addType("test1.F",
                "package test1;\n"+
                "public class F {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A extends test1.F {\n"+
                "  private class B extends E {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of remote base type of enclosing type (D)
        assertEquals(_project.getType("test2.D").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_RemoteEnclosingBaseVsTopLevelBase2() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  Object _attr;\n"+
                "}\n",
                false);
        addType("test2.E",
                "package test2;\n"+
                "public class E extends test1.D {}\n",
                false);
        addType("test1.F",
                "package test1;\n"+
                "public class F {\n"+
                "  protected Object _attr;\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A extends test1.F {\n"+
                "  private class B extends E {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of base type of top-level type
        assertEquals(_project.getType("test1.F").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }

    public void testLocal_TopLevelVsTopLevelBase() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  private Object _attr;\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // found field decl of top-level type (A)
        assertEquals(_project.getType("test.A").getFields().get(0),
                     fieldAccess.getFieldDeclaration());
    }
}
