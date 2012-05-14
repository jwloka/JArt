package jast.test.parser;
import jast.FileParser;
import jast.ast.Node;
import jast.ast.ParsePosition;
import jast.ast.Project;
import jast.ast.nodes.ArrayAccess;
import jast.ast.nodes.ArrayInitializer;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.Block;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.Initializer;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.SingleInitializer;
import jast.ast.nodes.VariableAccess;
import jast.parser.JavaParser;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import junit.framework.TestCase;
import antlr.ANTLRException;

public class TestCompletingParse extends TestCase
{
    private Project _project;
    private FileParser _parser;

    public TestCompletingParse(String name)
    {
        super(name);
    }

    protected void setUp()
    {
        _project = new Project("Test");
        _parser = new FileParser();
    }

    protected void tearDown()
    {
        _parser = null;
        _project = null;
    }

    public void testCompleting1() throws IOException, ANTLRException
    {
        // one unit is already present because of the array type
        assertEquals(1,
                     _project.getCompilationUnits().getCount());
        // java.lang is already present because of the array type
        assertEquals(1,
                     _project.getPackages().getCount());

        writeToFile("SomeClass.java",
                    "class SomeClass {\n"+
                    "  private int attr = 0;\n"+
                    "  int someMethod(int value) {\n"+
                    "    int var = value;\n"+
                    "    {\n"+
                    "      var += attr;\n"+
                    "    }\n"+
                    "    return var;\n"+
                    "  }\n"+
                    "}");
        _parser.setParsingInterface(true);
        _parser.parseFile(_project, "SomeClass.java");

        assertEquals(2,
                     _project.getCompilationUnits().getCount());

        // first unit is that of the array type
        CompilationUnit compilationUnit = _project.getCompilationUnits().get(1);

        assertEquals("SomeClass.java",
                     compilationUnit.getName());
        assertEquals(_project,
                     compilationUnit.getContainer());
        assertTrue(!compilationUnit.isComplete());

        ClassDeclaration classDecl = (ClassDeclaration)compilationUnit.getTypeDeclarations().get(0);

        assertEquals("SomeClass",
                     classDecl.getName());
        assertEquals("",
                     classDecl.getPackage().getQualifiedName());
        assertEquals(compilationUnit,
                     classDecl.getContainer());

        FieldDeclaration field = classDecl.getFields().get(0);

        assertEquals("attr",
                     field.getName());
        assertEquals(classDecl,
                     field.getContainer());

        SingleInitializer init = (SingleInitializer)field.getInitializer();

        assertNull(init.getInitEpression());
        assertEquals(field,
                     init.getContainer());

        MethodDeclaration method = classDecl.getMethods().get(0);

        assertEquals("someMethod",
                     method.getName());
        assertNull(method.getBody());
        assertEquals(classDecl,
                     method.getContainer());

        _parser.complete(compilationUnit);

        assertTrue(compilationUnit.isComplete());

        init = (SingleInitializer)field.getInitializer();

        assertTrue(init.getInitEpression() instanceof IntegerLiteral);
        assertEquals(field,
                     init.getContainer());

        Block block = method.getBody();

        assertEquals((ParsePosition)method.getProperty(JavaParser.PROPERTY_BODY_START),
                     block.getStartPosition());
        assertEquals(method,
                     block.getContainer());

        LocalVariableDeclaration localVarDecl = (LocalVariableDeclaration)block.getBlockStatements().get(0);

        assertEquals("var",
                     localVarDecl.getName());
        assertEquals(block,
                     localVarDecl.getContainer());

        SingleInitializer initializer = (SingleInitializer)localVarDecl.getInitializer();

        assertEquals(localVarDecl,
                     initializer.getContainer());

        VariableAccess varAccess = (VariableAccess)initializer.getInitEpression();

        assertEquals(method.getParameterList().getParameters().get(0),
                     varAccess.getVariableDeclaration());
        assertEquals(initializer,
                     varAccess.getContainer());

        Block subBlock = (Block)block.getBlockStatements().get(1);

        assertEquals(block,
                     subBlock.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)subBlock.getBlockStatements().get(0);

        assertEquals(subBlock,
                     exprStmt.getContainer());

        AssignmentExpression assignExpr = (AssignmentExpression)exprStmt.getExpression();

        assertEquals(localVarDecl,
                     ((VariableAccess)assignExpr.getLeftHandSide()).getVariableDeclaration());
        assertEquals(classDecl.getFields().get("attr"),
                     ((FieldAccess)assignExpr.getValueExpression()).getFieldDeclaration());
        assertEquals(exprStmt,
                     assignExpr.getContainer());
    }

    public void testCompleting2() throws IOException, ANTLRException
    {
        // one unit is already present because of the array type
        assertEquals(1,
                     _project.getCompilationUnits().getCount());
        // java.lang is already present because of the array type
        assertEquals(1,
                     _project.getPackages().getCount());

        writeToFile("SomeClass.java",
                    "class SomeClass {\n"+
                    "  private int attr = 0;\n"+
                    "  SomeClass(int value) {\n"+
                    "    int var = value;\n"+
                    "    {\n"+
                    "      var += attr;\n"+
                    "    }\n"+
                    "  }\n"+
                    "}");
        _parser.setParsingInterface(true);
        _parser.parseFile(_project, "SomeClass.java");

        assertEquals(2,
                     _project.getCompilationUnits().getCount());

        // first unit is that of the array type
        CompilationUnit compilationUnit = _project.getCompilationUnits().get(1);

        assertEquals("SomeClass.java",
                     compilationUnit.getName());
        assertEquals(_project,
                     compilationUnit.getContainer());
        assertTrue(!compilationUnit.isComplete());

        ClassDeclaration classDecl = (ClassDeclaration)compilationUnit.getTypeDeclarations().get(0);

        assertEquals("SomeClass",
                     classDecl.getName());
        assertEquals("",
                     classDecl.getPackage().getQualifiedName());
        assertEquals(compilationUnit,
                     classDecl.getContainer());

        ConstructorDeclaration constructor = classDecl.getConstructors().get(0);

        assertNull(constructor.getBody());
        assertEquals(classDecl,
                     constructor.getContainer());

        _parser.complete(compilationUnit);

        assertTrue(compilationUnit.isComplete());

        Block block = constructor.getBody();

        assertEquals((ParsePosition) constructor.getProperty(JavaParser.PROPERTY_BODY_START),
                     block.getStartPosition());
        assertEquals(constructor,
                     block.getContainer());

        LocalVariableDeclaration localVarDecl = (LocalVariableDeclaration) block.getBlockStatements().get(0);

        assertEquals("var",
                     localVarDecl.getName());
        assertEquals(block,
                     localVarDecl.getContainer());

        SingleInitializer initializer = (SingleInitializer)localVarDecl.getInitializer();

        assertEquals(localVarDecl,
                     initializer.getContainer());

        VariableAccess varAccess = (VariableAccess)initializer.getInitEpression();

        assertEquals(constructor.getParameterList().getParameters().get(0),
                     varAccess.getVariableDeclaration());
        assertEquals(initializer,
                     varAccess.getContainer());

        Block subBlock = (Block)block.getBlockStatements().get(1);

        assertEquals(block,
                     subBlock.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)subBlock.getBlockStatements().get(0);

        assertEquals(subBlock,
                     exprStmt.getContainer());

        AssignmentExpression assignExpr = (AssignmentExpression)exprStmt.getExpression();

        assertEquals(localVarDecl,
                     ((VariableAccess)assignExpr.getLeftHandSide()).getVariableDeclaration());
        assertEquals(classDecl.getFields().get("attr"),
                     ((FieldAccess)assignExpr.getValueExpression()).getFieldDeclaration());
        assertEquals(exprStmt,
                     assignExpr.getContainer());
    }

    public void testCompleting3() throws IOException, ANTLRException
    {
        // one unit is already present because of the array type
        assertEquals(1,
                     _project.getCompilationUnits().getCount());
        // java.lang is already present because of the array type
        assertEquals(1,
                     _project.getPackages().getCount());

        writeToFile("SomeClass.java",
                    "class SomeClass {\n"+
                    "  private static int attr;\n"+
                    "  static {\n"+
                    "    int var = 1;\n"+
                    "    {\n"+
                    "      attr = var;\n"+
                    "    }\n"+
                    "  }\n"+
                    "}");
        _parser.setParsingInterface(true);
        _parser.parseFile(_project, "SomeClass.java");

        assertEquals(2,
                     _project.getCompilationUnits().getCount());

        // first unit is that of the array type
        CompilationUnit compilationUnit = _project.getCompilationUnits().get(1);

        assertEquals("SomeClass.java",
                     compilationUnit.getName());
        assertEquals(_project,
                     compilationUnit.getContainer());
        assertTrue(!compilationUnit.isComplete());

        ClassDeclaration classDecl = (ClassDeclaration)compilationUnit.getTypeDeclarations().get(0);

        assertEquals("SomeClass",
                     classDecl.getName());
        assertEquals("",
                     classDecl.getPackage().getQualifiedName());
        assertEquals(compilationUnit,
                     classDecl.getContainer());

        Initializer initializer = classDecl.getInitializers().get(0);

        assertTrue(initializer.isStatic());
        assertNull(initializer.getBody());
        assertEquals(classDecl,
                     initializer.getContainer());

        _parser.complete(compilationUnit);

        assertTrue(compilationUnit.isComplete());

        Block block = initializer.getBody();

        assertEquals((ParsePosition) initializer.getProperty(JavaParser.PROPERTY_BODY_START),
                     block.getStartPosition());
        assertEquals(initializer,
                     block.getContainer());

        LocalVariableDeclaration localVarDecl = (LocalVariableDeclaration)block.getBlockStatements().get(0);

        assertEquals("var",
                     localVarDecl.getName());
        assertEquals(block,
                     localVarDecl.getContainer());

        Block subBlock = (Block)block.getBlockStatements().get(1);

        assertEquals(block,
                     subBlock.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)subBlock.getBlockStatements().get(0);

        assertEquals(subBlock,
                     exprStmt.getContainer());

        AssignmentExpression assignExpr = (AssignmentExpression)exprStmt.getExpression();

        assertEquals(classDecl.getFields().get("attr"),
                     ((FieldAccess)assignExpr.getLeftHandSide()).getFieldDeclaration());
        assertEquals(localVarDecl,
                     ((VariableAccess)assignExpr.getValueExpression()).getVariableDeclaration());
        assertEquals(exprStmt,
                     assignExpr.getContainer());
    }

    public void testCompleting4() throws IOException, ANTLRException
    {
        // one unit is already present because of the array type
        assertEquals(1,
                     _project.getCompilationUnits().getCount());
        // java.lang is already present because of the array type
        assertEquals(1,
                     _project.getPackages().getCount());

        writeToFile("SomeClass.java",
                    "class SomeClass {\n"+
                    "  private class Inner {\n"+
                    "    private int[] attr = { 0, 1, 2 };\n"+
                    "    int someMethod(int value) {\n"+
                    "      int var = value;\n"+
                    "      var += attr[1];\n"+
                    "      return var;\n"+
                    "    }\n"+
                    "  }\n"+
                    "}");
        _parser.setParsingInterface(true);
        _parser.parseFile(_project, "SomeClass.java");

        assertEquals(2, _project.getCompilationUnits().getCount());

        // first unit is that of the array type
        CompilationUnit compilationUnit = _project.getCompilationUnits().get(1);

        assertEquals("SomeClass.java",
                     compilationUnit.getName());
        assertEquals(_project,
                     compilationUnit.getContainer());
        assertTrue(!compilationUnit.isComplete());

        ClassDeclaration classDecl = (ClassDeclaration)compilationUnit.getTypeDeclarations().get(0);

        assertEquals("SomeClass",
                     classDecl.getName());
        assertEquals("",
                     classDecl.getPackage().getQualifiedName());
        assertEquals(compilationUnit,
                     classDecl.getContainer());

        ClassDeclaration innerClassDecl = (ClassDeclaration)classDecl.getInnerTypes().get(0);

        assertEquals("Inner",
                     innerClassDecl.getName());
        assertEquals(classDecl,
                     innerClassDecl.getContainer());

        FieldDeclaration field = innerClassDecl.getFields().get(0);

        assertEquals("attr",
                     field.getName());
        assertEquals(innerClassDecl,
                     field.getContainer());

        ArrayInitializer init = (ArrayInitializer)field.getInitializer();

        assertTrue(init.getInitializers().isEmpty());
        assertEquals(field,
                     init.getContainer());

        MethodDeclaration method = innerClassDecl.getMethods().get(0);

        assertEquals("someMethod",
                     method.getName());
        assertNull(method.getBody());
        assertEquals(innerClassDecl,
                     method.getContainer());

        _parser.complete(compilationUnit);

        assertTrue(compilationUnit.isComplete());

        init = (ArrayInitializer)field.getInitializer();

        assertEquals(3,
                     init.getInitializers().getCount());
        assertEquals(field,
                     init.getContainer());

        Block block = method.getBody();

        assertEquals((ParsePosition) method.getProperty(JavaParser.PROPERTY_BODY_START),
                     block.getStartPosition());
        assertEquals(method,
                     block.getContainer());

        LocalVariableDeclaration localVarDecl = (LocalVariableDeclaration)block.getBlockStatements().get(0);

        assertEquals("var",
                     localVarDecl.getName());
        assertEquals(block,
                     localVarDecl.getContainer());

        SingleInitializer initializer = (SingleInitializer)localVarDecl.getInitializer();

        assertEquals(localVarDecl,
                     initializer.getContainer());

        VariableAccess varAccess = (VariableAccess)initializer.getInitEpression();

        assertEquals(method.getParameterList().getParameters().get(0),
                     varAccess.getVariableDeclaration());
        assertEquals(initializer,
                     varAccess.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)block.getBlockStatements().get(1);

        assertEquals(block,
                     exprStmt.getContainer());

        AssignmentExpression assignExpr = (AssignmentExpression)exprStmt.getExpression();

        assertEquals(localVarDecl,
                     ((VariableAccess)assignExpr.getLeftHandSide()).getVariableDeclaration());

        ArrayAccess arrayAccess = (ArrayAccess)assignExpr.getValueExpression();

        assertEquals(innerClassDecl.getFields().get("attr"),
                     ((FieldAccess)arrayAccess.getBaseExpression()).getFieldDeclaration());
        assertEquals(exprStmt,
                     assignExpr.getContainer());
    }

    public void testCompleting5() throws IOException, ANTLRException
    {
        // one unit is already present because of the array type
        assertEquals(1,
                     _project.getCompilationUnits().getCount());
        // java.lang is already present because of the array type
        assertEquals(1,
                     _project.getPackages().getCount());

        writeToFile("SomeClass.java",
                    "class SomeClass {\n"+
                    "  int someMethod(int value) { // a method\n"+
                    "    int var = value;\n"+
                    "    var += attr;\n"+
                    "    return var; /* return the result */\n"+
                    "  } // end of method\n"+
                    "  private int attr = 0;\n"+
                    "  void anotherMethod() {\n"+
                    "    attr = someMethod(0);\n"+
                    "  }\n"+
                    "}");
        _parser.setParsingInterface(true);
        _parser.parseFile(_project, "SomeClass.java");

        assertEquals(2,
                     _project.getCompilationUnits().getCount());

        // first unit is that of the array type
        CompilationUnit compilationUnit = _project.getCompilationUnits().get(1);

        assertEquals("SomeClass.java",
                     compilationUnit.getName());
        assertEquals(_project,
                     compilationUnit.getContainer());
        assertTrue(!compilationUnit.isComplete());

        ClassDeclaration classDecl = (ClassDeclaration)compilationUnit.getTypeDeclarations().get(0);

        assertEquals("SomeClass",
                     classDecl.getName());
        assertEquals("",
                     classDecl.getPackage().getQualifiedName());
        assertEquals(compilationUnit,
                     classDecl.getContainer());

        MethodDeclaration method1 = classDecl.getMethods().get(0);

        assertEquals("someMethod",
                     method1.getName());
        assertNull(method1.getBody());
        assertEquals("end of method",
                     method1.getComments().get(0).getText());
        assertEquals(classDecl,
                     method1.getContainer());

        MethodDeclaration method2 = classDecl.getMethods().get(1);

        assertEquals("anotherMethod",
                     method2.getName());
        assertNull(method2.getBody());
        assertEquals(classDecl,
                     method2.getContainer());

        _parser.complete(compilationUnit);

        assertTrue(compilationUnit.isComplete());

        Block block = method1.getBody();

        assertEquals((ParsePosition) method1.getProperty(JavaParser.PROPERTY_BODY_START),
                     block.getStartPosition());
        assertEquals(method1,
                     block.getContainer());

        assertEquals("a method",
                     ((Node)block.getBlockStatements().get(0)).getComments().get(0).getText());
        assertEquals("return the result",
                     ((Node)block.getBlockStatements().get(2)).getComments().get(0).getText());

        block = method2.getBody();

        assertEquals((ParsePosition) method2.getProperty(JavaParser.PROPERTY_BODY_START),
                     block.getStartPosition());
        assertEquals(method2,
                     block.getContainer());
    }

    private void writeToFile(String filename, String text) throws IOException
    {
        PrintWriter output = new PrintWriter(new FileWriter(filename));
        output.println(text);
        output.close();
    }
}
