package jast.test.resolver;

import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.ReturnStatement;
import antlr.ANTLRException;

public class TestArrayAccess extends TestBase
{

    public TestArrayAccess(String name)
    {
        super(name);
    }

    public void testLocal1() throws ANTLRException
    {
        addType("Test.java",
                "class Test {\n"+
                "  private int[] arr;\n"+
                "  public int test() {\n"+
                "    return arr.length;\n"+
                "  }\n"+
                "}",
                true);
        resolve();

        ClassDeclaration  classDecl   = (ClassDeclaration)_project.getType("Test");
        MethodDeclaration methodDecl  = classDecl.getMethods().get(0);
        ReturnStatement   returnStmt  = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        FieldAccess       fieldAccess = (FieldAccess)returnStmt.getReturnValue();

        assertEquals(_project.getArrayType().getFields().get("length"),
                     fieldAccess.getFieldDeclaration());
    }
}
