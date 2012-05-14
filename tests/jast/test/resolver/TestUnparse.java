package jast.test.resolver;
import jast.ast.ASTHelper;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.TypeAccess;
import antlr.ANTLRException;

public class TestUnparse extends TestBase
{

    public TestUnparse(String name)
    {
        super(name);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(TestUnparse.class);
    }

    public void test1() throws ANTLRException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {}",
                true);
        addType("test2.B",
                "package test2;\n"+
                "public class B extends test1.A {}",
                true);
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test2.B");
        CompilationUnit  unitA      = ASTHelper.getCompilationUnitOf(classDeclA);

        assertNotNull(classDeclA);
        assertEquals(classDeclA,
                     classDeclB.getBaseClass().getReferenceBaseTypeDecl());

        _project.delCompilationUnit(unitA.getName());

        classDeclA = null;
        unitA      = null;

        assertNull(_project.getType("test1.A"));
        assertNull(classDeclB.getBaseClass().getReferenceBaseTypeDecl());

        addType("test1.A",
                "package test1;\n"+
                "public class A {}",
                true);
        resolve();

        classDeclA = (ClassDeclaration)_project.getType("test1.A");
        unitA      = ASTHelper.getCompilationUnitOf(classDeclA);

        assertNotNull(classDeclA);
        assertEquals(classDeclA,
                     classDeclB.getBaseClass().getReferenceBaseTypeDecl());
    }


    public void test2() throws ANTLRException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public static void test() {}\n"+
                "}",
                true);
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  public void doSomething() {\n"+
                "    test1.A.test();\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        ClassDeclaration    classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration    classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        CompilationUnit     unitA       = ASTHelper.getCompilationUnitOf(classDeclA);
        MethodDeclaration   methodDeclA = classDeclA.getMethods().get(0);
        MethodDeclaration   methodDeclB = classDeclB.getMethods().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)methodDeclB.getBody().getBlockStatements().get(0);
        MethodInvocation    methodInvoc = (MethodInvocation)exprStmt.getExpression();
        TypeAccess          typeAccess  = (TypeAccess)methodInvoc.getBaseExpression();

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
        assertEquals(classDeclA,
                     typeAccess.getType().getReferenceBaseTypeDecl());

        _project.delCompilationUnit(unitA.getName());

        classDeclA  = null;
        unitA       = null;
        methodDeclA = null;

        assertNull(_project.getType("test1.A"));
        assertNull(methodInvoc.getMethodDeclaration());
        assertNull(typeAccess.getType().getReferenceBaseTypeDecl());

        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public static void test() {}\n"+
                "}",
                true);
        resolve();

        classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        unitA       = ASTHelper.getCompilationUnitOf(classDeclA);
        methodDeclA = classDeclA.getMethods().get(0);

        assertEquals(methodDeclA,
                     methodInvoc.getMethodDeclaration());
        assertEquals(classDeclA,
                     typeAccess.getType().getReferenceBaseTypeDecl());
    }



    public void test3() throws ANTLRException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public int _attr = 0;\n"+
                "}",
                true);
        addType("test2.B",
                "package test2;\n"+
                "import test1.A;\n"+
                "public class B {\n"+
                "  public void doSomething(test1.A obj) {\n"+
                "    obj._attr = 0;\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        ClassDeclaration     classDeclA  = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration     classDeclB  = (ClassDeclaration)_project.getType("test2.B");
        CompilationUnit      unitA       = ASTHelper.getCompilationUnitOf(classDeclA);
        FieldDeclaration     fieldDedlA  = classDeclA.getFields().get(0);
        MethodDeclaration    methodDecl  = classDeclB.getMethods().get(0);
        FormalParameter      param       = methodDecl.getParameterList().getParameters().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)methodDecl.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(classDeclA,
                     param.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldDedlA,
                     fieldAccess.getFieldDeclaration());

        _project.delCompilationUnit(unitA.getName());

        classDeclA = null;
        unitA      = null;
        fieldDedlA = null;

        assertNull(_project.getType("test1.A"));
        assertNull(param.getType().getReferenceBaseTypeDecl());
        assertNull(fieldAccess.getFieldDeclaration());

        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public int _attr = 0;\n"+
                "}",
                true);
        resolve();

        classDeclA = (ClassDeclaration)_project.getType("test1.A");
        unitA      = ASTHelper.getCompilationUnitOf(classDeclA);
        fieldDedlA = classDeclA.getFields().get(0);

        assertEquals(classDeclA,
                     param.getType().getReferenceBaseTypeDecl());
        assertEquals(fieldDedlA,
                     fieldAccess.getFieldDeclaration());
    }
}
