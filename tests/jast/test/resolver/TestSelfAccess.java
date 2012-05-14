package jast.test.resolver;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.SelfAccess;
import jast.ast.nodes.TypeDeclaration;
import antlr.ANTLRException;

public class TestSelfAccess extends TestBase
{

    public TestSelfAccess(String name)
    {
        super(name);
    }

    public void testSuperCurrent1() throws ANTLRException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends D{\n"+
                "      public String test() {\n"+
                "        return super.toString();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        MethodDeclaration methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ReturnStatement   returnStmt  = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc = (MethodInvocation)returnStmt.getReturnValue();
        SelfAccess        selfAccess  = (SelfAccess)methodInvoc.getBaseExpression();

        assertTrue(!selfAccess.isQualified());
        assertTrue(selfAccess.isSuper());
        assertEquals(_project.getType("test.D"),
                     selfAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testSuperCurrent2() throws ANTLRException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends D{\n"+
                "      public String test() {\n"+
                "        return C.super.toString();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        TypeDeclaration   typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration methodDecl  = typeDecl.getMethods().get(0);
        ReturnStatement   returnStmt  = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc = (MethodInvocation)returnStmt.getReturnValue();
        SelfAccess        selfAccess  = (SelfAccess)methodInvoc.getBaseExpression();

        assertTrue(selfAccess.isQualified());
        assertTrue(selfAccess.isSuper());
        assertEquals(typeDecl,
                     selfAccess.getTypeAccess().getType().getReferenceBaseTypeDecl());
        assertEquals(_project.getType("test.D"),
                     selfAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testSuperEnclosing() throws ANTLRException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B extends D {\n"+
                "    private class C {\n"+
                "      public String test() {\n"+
                "        return B.super.toString();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        TypeDeclaration   typeDecl    = _project.getType("test.A.B");
        MethodDeclaration methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ReturnStatement   returnStmt  = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc = (MethodInvocation)returnStmt.getReturnValue();
        SelfAccess        selfAccess  = (SelfAccess)methodInvoc.getBaseExpression();

        assertTrue(selfAccess.isQualified());
        assertTrue(selfAccess.isSuper());
        assertEquals(typeDecl,
                     selfAccess.getTypeAccess().getType().getReferenceBaseTypeDecl());
        assertEquals(_project.getType("test.D"),
                     selfAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testSuperTopLevel() throws ANTLRException
    {
        addType("test.D",
                "package test;\n"+
                "public class D {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends D {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      public String test() {\n"+
                "        return A.super.toString();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        MethodDeclaration methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ReturnStatement   returnStmt  = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc = (MethodInvocation)returnStmt.getReturnValue();
        SelfAccess        selfAccess  = (SelfAccess)methodInvoc.getBaseExpression();

        assertTrue(selfAccess.isQualified());
        assertTrue(selfAccess.isSuper());
        assertEquals(_project.getType("test.A"),
                     selfAccess.getTypeAccess().getType().getReferenceBaseTypeDecl());
        assertEquals(_project.getType("test.D"),
                     selfAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testThisCurrent1() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      public String test() {\n"+
                "        return this.toString();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        TypeDeclaration   typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration methodDecl  = typeDecl.getMethods().get(0);
        ReturnStatement   returnStmt  = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc = (MethodInvocation)returnStmt.getReturnValue();
        SelfAccess        selfAccess  = (SelfAccess)methodInvoc.getBaseExpression();

        assertTrue(!selfAccess.isQualified());
        assertTrue(!selfAccess.isSuper());
        assertEquals(typeDecl,
                     selfAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testThisCurrent2() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      public String test() {\n"+
                "        return C.this.toString();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        TypeDeclaration   typeDecl    = _project.getType("test.A.B.C");
        MethodDeclaration methodDecl  = typeDecl.getMethods().get(0);
        ReturnStatement   returnStmt  = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc = (MethodInvocation)returnStmt.getReturnValue();
        SelfAccess        selfAccess  = (SelfAccess)methodInvoc.getBaseExpression();

        assertTrue(selfAccess.isQualified());
        assertTrue(!selfAccess.isSuper());
        assertEquals(typeDecl,
                     selfAccess.getTypeAccess().getType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     selfAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testThisEnclosing() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      public String test() {\n"+
                "        return B.this.toString();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        TypeDeclaration   typeDecl    = _project.getType("test.A.B");
        MethodDeclaration methodDecl  = typeDecl.getInnerTypes().get("C").getMethods().get(0);
        ReturnStatement   returnStmt  = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc = (MethodInvocation)returnStmt.getReturnValue();
        SelfAccess        selfAccess  = (SelfAccess)methodInvoc.getBaseExpression();

        assertTrue(selfAccess.isQualified());
        assertTrue(!selfAccess.isSuper());
        assertEquals(typeDecl,
                     selfAccess.getTypeAccess().getType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     selfAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testThisTopLevel() throws ANTLRException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      public String test() {\n"+
                "        return A.this.toString();\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        TypeDeclaration   typeDecl    = _project.getType("test.A");
        MethodDeclaration methodDecl  = _project.getType("test.A.B.C").getMethods().get(0);
        ReturnStatement   returnStmt  = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        MethodInvocation  methodInvoc = (MethodInvocation)returnStmt.getReturnValue();
        SelfAccess        selfAccess  = (SelfAccess)methodInvoc.getBaseExpression();

        assertTrue(selfAccess.isQualified());
        assertTrue(!selfAccess.isSuper());
        assertEquals(typeDecl,
                     selfAccess.getTypeAccess().getType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     selfAccess.getType().getReferenceBaseTypeDecl());
    }
}
