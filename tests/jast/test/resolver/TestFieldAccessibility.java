package jast.test.resolver;
import jast.ParseException;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.PrimitiveType;
import jast.ast.nodes.SingleInitializer;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.VariableAccess;
import antlr.ANTLRException;

public class TestFieldAccessibility extends TestBase
{

    public TestFieldAccessibility(String name)
    {
        super(name);
    }

    public void testAnonClass_CurrentFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      int _attr;\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = 1;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration          typeDecl      = _project.getType("test.A.B.C");
        FieldDeclaration         fieldDecl     = typeDecl.getFields().get(0);
        MethodDeclaration        methodDecl1   = typeDecl.getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testAnonClass_CurrentPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private int _attr;\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = 1;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration          typeDecl      = _project.getType("test.A.B.C");
        FieldDeclaration         fieldDecl     = typeDecl.getFields().get(0);
        MethodDeclaration        methodDecl1   = typeDecl.getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testAnonClass_CurrentProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      protected int _attr;\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = 1;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration          typeDecl      = _project.getType("test.A.B.C");
        FieldDeclaration         fieldDecl     = typeDecl.getFields().get(0);
        MethodDeclaration        methodDecl1   = typeDecl.getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testAnonClass_CurrentPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      public int _attr;\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = 1;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration          typeDecl      = _project.getType("test.A.B.C");
        FieldDeclaration         fieldDecl     = typeDecl.getFields().get(0);
        MethodDeclaration        methodDecl1   = typeDecl.getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testAnonClass_DirectBaseFriendly() throws ANTLRException, ParseException
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
                "  class B {\n"+
                "    class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = null;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test.D").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testAnonClass_DirectBaseProtected() throws ANTLRException, ParseException
    {
        addType("test2.D",
                "package test2;\n"+
                "public class D {\n"+
                "  protected Object _attr;\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = null;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test2.D").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testAnonClass_DirectBasePublic() throws ANTLRException, ParseException
    {
        addType("test2.D",
                "package test2;\n"+
                "public class D {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = null;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test2.D").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testAnonClass_EnclosingDirectBaseFriendly() throws ANTLRException, ParseException
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
                "  class B extends D {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = null;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test.D").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testAnonClass_EnclosingDirectBaseProtected() throws ANTLRException, ParseException
    {
        addType("test2.D",
                "package test2;\n"+
                "public class D {\n"+
                "  protected Object _attr;\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B extends test2.D {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = null;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test2.D").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testAnonClass_EnclosingDirectBasePublic() throws ANTLRException, ParseException
    {
        addType("test2.D",
                "package test2;\n"+
                "public class D {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B extends test2.D {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = null;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test2.D").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testAnonClass_EnclosingFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    String _attr;\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = \"\";\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test.A.B").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.String"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testAnonClass_EnclosingPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    private String _attr;\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = \"\";\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test.A.B").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.String"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testAnonClass_EnclosingProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    protected String _attr;\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = \"\";\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test.A.B").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.String"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testAnonClass_EnclosingPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    public String _attr;\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = \"\";\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test.A.B").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.String"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testAnonClass_Friendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          int _attr;\n"+
                "          public void doSomething() {\n"+
                "            _attr = 1;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        FieldDeclaration         fieldDecl     = instantiation.getAnonymousClass().getFields().get(0);
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testAnonClass_Private() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          private int _attr;\n"+
                "          public void doSomething() {\n"+
                "            _attr = 1;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        FieldDeclaration         fieldDecl     = instantiation.getAnonymousClass().getFields().get(0);
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testAnonClass_Protected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          protected int _attr;\n"+
                "          public void doSomething() {\n"+
                "            _attr = 1;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        FieldDeclaration         fieldDecl     = instantiation.getAnonymousClass().getFields().get(0);
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testAnonClass_Public() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public int _attr;\n"+
                "          public void doSomething() {\n"+
                "            _attr = 1;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration        methodDecl1   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        FieldDeclaration         fieldDecl     = instantiation.getAnonymousClass().getFields().get(0);
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testAnonClass_RemoteBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  int _attr;\n"+
                "}\n",
                false);
        addType("test.D",
                "package test;\n"+
                "public class D extends E {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = 1;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test.E").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testAnonClass_RemoteBaseProtected() throws ANTLRException, ParseException
    {
        addType("test3.E",
                "package test3;\n"+
                "public class E {\n"+
                "  protected int _attr;\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test3.E {}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = 1;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test3.E").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testAnonClass_RemoteBasePublic() throws ANTLRException, ParseException
    {
        addType("test3.E",
                "package test3;\n"+
                "public class E {\n"+
                "  public int _attr;\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test3.E {}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = 1;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test3.E").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testAnonClass_TopLevelDirectBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  String _attr;\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = \"\";\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test.D").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.String"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testAnonClass_TopLevelDirectBaseProtected() throws ANTLRException, ParseException
    {
        addType("test2.D",
                "package test2;\n"+
                "public class D {\n"+
                "  protected String _attr;\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A extends test2.D {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = \"\";\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test2.D").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.String"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testAnonClass_TopLevelDirectBasePublic() throws ANTLRException, ParseException
    {
        addType("test2.D",
                "package test2;\n"+
                "public class D {\n"+
                "  public String _attr;\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A extends test2.D {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = \"\";\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test2.D").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.String"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testAnonClass_TopLevelFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  int _attr;\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = 1;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test.A").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testAnonClass_TopLevelPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = 1;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test.A").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testAnonClass_TopLevelProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected int _attr;\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = 1;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test.A").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testAnonClass_TopLevelPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        Object obj = new Object() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = 1;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test.A").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testAnonClassDirectBase_Friendly() throws ANTLRException, ParseException
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
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        D obj = new D() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = null;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test.D").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testAnonClassDirectBase_Protected() throws ANTLRException, ParseException
    {
        addType("test2.D",
                "package test2;\n"+
                "public class D {\n"+
                "  protected Object _attr;\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        test2.D obj = new test2.D() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = null;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test2.D").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testAnonClassDirectBase_Public() throws ANTLRException, ParseException
    {
        addType("test2.D",
                "package test2;\n"+
                "public class D {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        test2.D obj = new test2.D() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = null;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test2.D").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testAnonClassRemoteBase_Friendly() throws ANTLRException, ParseException
    {
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  int _attr;\n"+
                "}\n",
                false);
        addType("test.D",
                "package test;\n"+
                "public class D extends E {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        D obj = new D() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = 1;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test.E").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testAnonClassRemoteBase_Protected() throws ANTLRException, ParseException
    {
        addType("test3.E",
                "package test3;\n"+
                "public class E {\n"+
                "  protected int _attr;\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test3.E {}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        test2.D obj = new test2.D() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = 1;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test3.E").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testAnonClassRemoteBase_Public() throws ANTLRException, ParseException
    {
        addType("test3.E",
                "package test3;\n"+
                "public class E {\n"+
                "  public int _attr;\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test3.E {}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      private void doSomething() {\n"+
                "        test2.D obj = new test2.D() {\n"+
                "          public void doSomething() {\n"+
                "            _attr = 1;\n"+
                "          }\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration         fieldDecl     = _project.getType("test3.E").getFields().get(0);
        MethodDeclaration        methodDecl1   = _project.getType("test1.A.B.C").getMethods().get(0);
        LocalVariableDeclaration varDecl       = (LocalVariableDeclaration)methodDecl1.getBody().getBlockStatements().get(0);
        SingleInitializer        initializer   = (SingleInitializer)varDecl.getInitializer();
        Instantiation            instantiation = (Instantiation)initializer.getInitEpression();
        MethodDeclaration        methodDecl2   = instantiation.getAnonymousClass().getMethods().get(0);
        ExpressionStatement      exprStmt      = (ExpressionStatement)methodDecl2.getBody().getBlockStatements().get(0);
        AssignmentExpression     assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess              fieldAccess   = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testExpression_CurrentFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      int _attr;\n"+
                "      private void doSomething() {\n"+
                "        C var = new C();\n"+
                "        var._attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test.A.B.C");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        MethodDeclaration    methodDecl  = typeDecl.getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
        assertEquals(typeDecl,
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_CurrentPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private int _attr;\n"+
                "      private void doSomething() {\n"+
                "        C var = new C();\n"+
                "        var._attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test.A.B.C");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        MethodDeclaration    methodDecl  = typeDecl.getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
        assertEquals(typeDecl,
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_CurrentProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected class B {\n"+
                "    protected class C {\n"+
                "      protected int _attr;\n"+
                "      private void doSomething() {\n"+
                "        C var = new C();\n"+
                "        var._attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test.A.B.C");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        MethodDeclaration    methodDecl  = typeDecl.getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
        assertEquals(typeDecl,
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_CurrentPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      public int _attr;\n"+
                "      private void doSomething() {\n"+
                "        C var = new C();\n"+
                "        var._attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test.A.B.C");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        MethodDeclaration    methodDecl  = typeDecl.getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
        assertEquals(typeDecl,
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_DirectBaseFriendly() throws ANTLRException, ParseException
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
                "  private class B {\n"+
                "    private class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        C var = new C();\n"+
                "        var._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(_project.getType("test.A.B.C"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_DirectBaseProtected() throws ANTLRException, ParseException
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
                "  private class B {\n"+
                "    private class C extends test1.D {\n"+
                "      private void doSomething() {\n"+
                "        C var = new C();\n"+
                "        var._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test1.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(_project.getType("test2.A.B.C"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_DirectBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.D {\n"+
                "      private void doSomething() {\n"+
                "        C var = new C();\n"+
                "        var._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test1.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(_project.getType("test2.A.B.C"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_EnclosingDirectBaseFriendly() throws ANTLRException, ParseException
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
                "  private class B extends D {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        B var = new B();\n"+
                "        var._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(_project.getType("test.A.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_EnclosingDirectBaseProtected() throws ANTLRException, ParseException
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
                "  private class B extends test1.D {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        B var = new B();\n"+
                "        var._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test1.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(_project.getType("test2.A.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_EnclosingDirectBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B extends test1.D {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        B var = new B();\n"+
                "        var._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test1.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(_project.getType("test2.A.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_EnclosingFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    int _attr;\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        B var = new B();\n"+
                "        var._attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.A.B").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
        assertEquals(_project.getType("test.A.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_EnclosingPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private int _attr;\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        B var = new B();\n"+
                "        var._attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.A.B").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
        assertEquals(_project.getType("test.A.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_EnclosingProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected class B {\n"+
                "    protected int _attr;\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        B var = new B();\n"+
                "        var._attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.A.B").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
        assertEquals(_project.getType("test.A.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_EnclosingPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public int _attr;\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        B var = new B();\n"+
                "        var._attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.A.B").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
        assertEquals(_project.getType("test.A.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_RemoteBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  Object _attr;\n"+
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
                "      private void doSomething() {\n"+
                "        C var = new C();\n"+
                "        var._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.E").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(_project.getType("test.A.B.C"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_RemoteBaseProtected() throws ANTLRException, ParseException
    {
        addType("test1.E",
                "package test1;\n"+
                "public class E {\n"+
                "  protected Object _attr;\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.E {}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        C var = new C();\n"+
                "        var._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test1.E").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test3.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(_project.getType("test3.A.B.C"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_RemoteBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.E",
                "package test1;\n"+
                "public class E {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.E {}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        C var = new C();\n"+
                "        var._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test1.E").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test3.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(_project.getType("test3.A.B.C"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_TopLevelDirectBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  Object _attr;\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        A var = new A();\n"+
                "        var._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(_project.getType("test.A"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_TopLevelDirectBaseProtected() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected Object _attr;\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A extends test1.D {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        A var = new A();\n"+
                "        var._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test1.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(_project.getType("test2.A"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_TopLevelDirectBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A extends test1.D {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        A var = new A();\n"+
                "        var._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test1.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
        assertEquals(_project.getType("test2.A"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_TopLevelFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  int _attr;\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        A var = new A();\n"+
                "        var._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.A").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
        assertEquals(_project.getType("test.A"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_TopLevelPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        A var = new A();\n"+
                "        var._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.A").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
        assertEquals(_project.getType("test.A"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_TopLevelProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected int _attr;\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        A var = new A();\n"+
                "        var._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.A").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
        assertEquals(_project.getType("test.A"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_TopLevelPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        A var = new A();\n"+
                "        var._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.A").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
        assertEquals(_project.getType("test.A"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_UnrelatedFriendly1() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  int _attr;\n"+
                "}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    B var = new B();\n"+
                "    var._attr = null;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.B").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
        assertEquals(_project.getType("test.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_UnrelatedFriendly2() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B extends C {}\n"+
                "class C {\n"+
                "  int _attr;\n"+
                "}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    B var = new B();\n"+
                "    var._attr = null;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.C").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
        assertEquals(_project.getType("test.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_UnrelatedFriendly3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    int _attr;\n"+
                "  }\n"+
                "}\n",
                false);
        addType("test.C",
                "package test;\n"+
                "public class C {\n"+
                "  private void doSomething() {\n"+
                "    A.B var = new A().new B();\n"+
                "    var._attr = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.A.B").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
        assertEquals(_project.getType("test.A.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_UnrelatedProtected1() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  protected int _attr;\n"+
                "}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    B var = new B();\n"+
                "    var._attr = null;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.B").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
        assertEquals(_project.getType("test.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_UnrelatedProtected2() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "class B {\n"+
                "  protected int _attr;\n"+
                "}\n"+
                "public class C extends B {}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    C var = new C();\n"+
                "    var._attr = null;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.B").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
        assertEquals(_project.getType("test.C"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_UnrelatedProtected3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected class B {\n"+
                "    protected int _attr;\n"+
                "  }\n"+
                "}\n",
                false);
        addType("test.C",
                "package test;\n"+
                "public class C {\n"+
                "  private void doSomething() {\n"+
                "    A.B var = new A().new B();\n"+
                "    var._attr = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.A.B").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
        assertEquals(_project.getType("test.A.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_UnrelatedPublic1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public int _attr;\n"+
                "}\n",
                true);
        addType("test2.A",
                "package test2;\n"+
                "import test1.B;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    B var = new B();\n"+
                "    var._attr = null;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test1.B").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test2.A").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
        assertEquals(_project.getType("test1.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_UnrelatedPublic2() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public int _attr;\n"+
                "}\n",
                true);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B {}\n",
                true);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private void doSomething() {\n"+
                "    test2.C var = new test2.C();\n"+
                "    var._attr = null;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test1.B").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test3.A").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
        assertEquals(_project.getType("test2.C"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testExpression_UnrelatedPublic3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public int _attr;\n"+
                "  }\n"+
                "}\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C {\n"+
                "  private void doSomething() {\n"+
                "    test1.A.B var = new test1.A().new B();\n"+
                "    var._attr = 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test1.A.B").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test2.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();
        VariableAccess       varAccess   = (VariableAccess)fieldAccess.getBaseExpression();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
        assertEquals(_project.getType("test1.A.B"),
                     varAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testLocal_CurrentFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      int _attr;\n"+
                "      private void doSomething() {\n"+
                "        _attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test.A.B.C");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        MethodDeclaration    methodDecl  = typeDecl.getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testLocal_CurrentPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private int _attr;\n"+
                "      private void doSomething() {\n"+
                "        _attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test.A.B.C");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        MethodDeclaration    methodDecl  = typeDecl.getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testLocal_CurrentProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected class B {\n"+
                "    protected class C {\n"+
                "      protected int _attr;\n"+
                "      private void doSomething() {\n"+
                "        _attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test.A.B.C");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        MethodDeclaration    methodDecl  = typeDecl.getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testLocal_CurrentPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      public int _attr;\n"+
                "      private void doSomething() {\n"+
                "        _attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test.A.B.C");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        MethodDeclaration    methodDecl  = typeDecl.getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testLocal_DirectBaseFriendly() throws ANTLRException, ParseException
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

        FieldDeclaration     fieldDecl   = _project.getType("test.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testLocal_DirectBaseProtected() throws ANTLRException, ParseException
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

        FieldDeclaration     fieldDecl   = _project.getType("test1.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testLocal_DirectBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
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

        FieldDeclaration     fieldDecl   = _project.getType("test1.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testLocal_EnclosingDirectBaseFriendly() throws ANTLRException, ParseException
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

        FieldDeclaration     fieldDecl   = _project.getType("test.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testLocal_EnclosingDirectBaseProtected() throws ANTLRException, ParseException
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

        FieldDeclaration     fieldDecl   = _project.getType("test1.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testLocal_EnclosingDirectBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
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

        FieldDeclaration     fieldDecl   = _project.getType("test1.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testLocal_EnclosingFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    int _attr;\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        _attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.A.B").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testLocal_EnclosingPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private int _attr;\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        _attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.A.B").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testLocal_EnclosingProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected class B {\n"+
                "    protected int _attr;\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        _attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.A.B").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testLocal_EnclosingPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public int _attr;\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        _attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.A.B").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testLocal_RemoteBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  Object _attr;\n"+
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
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.E").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testLocal_RemoteBaseProtected() throws ANTLRException, ParseException
    {
        addType("test1.E",
                "package test1;\n"+
                "public class E {\n"+
                "  protected Object _attr;\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.E {}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test1.E").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test3.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testLocal_RemoteBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.E",
                "package test1;\n"+
                "public class E {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.E {}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        _attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test1.E").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test3.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testLocal_TopLevelDirectBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {\n"+
                "  Object _attr;\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
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

        FieldDeclaration     fieldDecl   = _project.getType("test.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testLocal_TopLevelDirectBaseProtected() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected Object _attr;\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A extends test1.D {\n"+
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

        FieldDeclaration     fieldDecl   = _project.getType("test1.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testLocal_TopLevelDirectBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A extends test1.D {\n"+
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

        FieldDeclaration     fieldDecl   = _project.getType("test1.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testLocal_TopLevelFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  int _attr;\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        _attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.A").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testLocal_TopLevelPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private int _attr;\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        _attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.A").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testLocal_TopLevelProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected int _attr;\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        _attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.A").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testLocal_TopLevelPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public int _attr;\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        _attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.A").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testSuper_DirectBaseFriendly() throws ANTLRException, ParseException
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
                "  private class B {\n"+
                "    private class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        super._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testSuper_DirectBaseProtected() throws ANTLRException, ParseException
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
                "  private class B {\n"+
                "    private class C extends test1.D {\n"+
                "      private void doSomething() {\n"+
                "        super._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test1.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testSuper_DirectBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.D {\n"+
                "      private void doSomething() {\n"+
                "        super._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test1.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testSuper_RemoteBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  Object _attr;\n"+
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
                "      private void doSomething() {\n"+
                "        super._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.E").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testSuper_RemoteBaseProtected() throws ANTLRException, ParseException
    {
        addType("test1.E",
                "package test1;\n"+
                "public class E {\n"+
                "  protected Object _attr;\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.E {}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        super._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test1.E").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test3.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testSuper_RemoteBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.E",
                "package test1;\n"+
                "public class E {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.E {}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        super._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test1.E").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test3.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testThis_CurrentFriendly() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {\n"+
                "    class C {\n"+
                "      int _attr;\n"+
                "      private void doSomething() {\n"+
                "        this._attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test.A.B.C");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        MethodDeclaration    methodDecl  = typeDecl.getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testThis_CurrentPrivate() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private int _attr;\n"+
                "      private void doSomething() {\n"+
                "        this._attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test.A.B.C");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        MethodDeclaration    methodDecl  = typeDecl.getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testThis_CurrentProtected() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected class B {\n"+
                "    protected class C {\n"+
                "      protected int _attr;\n"+
                "      private void doSomething() {\n"+
                "        this._attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test.A.B.C");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        MethodDeclaration    methodDecl  = typeDecl.getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testThis_CurrentPublic() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public class C {\n"+
                "      public int _attr;\n"+
                "      private void doSomething() {\n"+
                "        this._attr = 1;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration      typeDecl    = _project.getType("test.A.B.C");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        MethodDeclaration    methodDecl  = typeDecl.getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(PrimitiveType.INT_TYPE,
                     fieldAccess.getType().getPrimitiveBaseType());
    }

    public void testThis_DirectBaseFriendly() throws ANTLRException, ParseException
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
                "  private class B {\n"+
                "    private class C extends D {\n"+
                "      private void doSomething() {\n"+
                "        this._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testThis_DirectBaseProtected() throws ANTLRException, ParseException
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
                "  private class B {\n"+
                "    private class C extends test1.D {\n"+
                "      private void doSomething() {\n"+
                "        this._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test1.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testThis_DirectBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.D {\n"+
                "      private void doSomething() {\n"+
                "        this._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test1.D").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test2.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testThis_RemoteBaseFriendly() throws ANTLRException, ParseException
    {
        addType("test.E",
                "package test;\n"+
                "public class E {\n"+
                "  Object _attr;\n"+
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
                "      private void doSomething() {\n"+
                "        this._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test.E").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testThis_RemoteBaseProtected() throws ANTLRException, ParseException
    {
        addType("test1.E",
                "package test1;\n"+
                "public class E {\n"+
                "  protected Object _attr;\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.E {}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        this._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test1.E").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test3.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testThis_RemoteBasePublic() throws ANTLRException, ParseException
    {
        addType("test1.E",
                "package test1;\n"+
                "public class E {\n"+
                "  public Object _attr;\n"+
                "}\n",
                false);
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.E {}\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test2.D {\n"+
                "      private void doSomething() {\n"+
                "        this._attr = null;\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        FieldDeclaration     fieldDecl   = _project.getType("test1.E").getFields().get(0);
        MethodDeclaration    methodDecl  = _project.getType("test3.A.B.C").getMethods().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(fieldDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(_project.getType("java.lang.Object"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }
}
