package jast.test.parser;
import jast.ast.Node;
import jast.ast.ParsePosition;
import jast.ast.nodes.ArgumentList;
import jast.ast.nodes.ArrayCreation;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.Block;
import jast.ast.nodes.CaseBlock;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.ConditionalExpression;
import jast.ast.nodes.ConstructorInvocation;
import jast.ast.nodes.DoWhileStatement;
import jast.ast.nodes.Expression;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.ForStatement;
import jast.ast.nodes.FormalParameterList;
import jast.ast.nodes.IfThenElseStatement;
import jast.ast.nodes.InstanceofExpression;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.LabeledStatement;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.ParenthesizedExpression;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.SelfAccess;
import jast.ast.nodes.SingleInitializer;
import jast.ast.nodes.Statement;
import jast.ast.nodes.SwitchStatement;
import jast.ast.nodes.SynchronizedStatement;
import jast.ast.nodes.TryStatement;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.WhileStatement;
import antlr.ANTLRException;

public class TestParsePositions extends TestBase
{

    public TestParsePositions(String name)
    {
        super(name);
    }

    public void assertEquals(int  startLine,  int startColumn,  int startAbsolute,
                             int  finishLine, int finishColumn, int finishAbsolute,
                             Node node)
    {
        assertNotNull(node);
        assertEquals(new ParsePosition(startLine, startColumn, startAbsolute),
                     node.getStartPosition());
        assertEquals(new ParsePosition(finishLine, finishColumn, finishAbsolute),
                     node.getFinishPosition());
    }

    public void assertEquals(int  startLine,  int startColumn,
                             int  finishLine, int finishColumn,
                             Node node)
    {
        assertNotNull(node);
        assertEquals(new ParsePosition(startLine, startColumn, node.getStartPosition().getAbsolute()),
                     node.getStartPosition());
        assertEquals(new ParsePosition(finishLine, finishColumn, node.getFinishPosition().getAbsolute()),
                     node.getFinishPosition());
    }

    public void testParsePosition1() throws ANTLRException
    {
        setupParser("package test.other;\n" +
                    "import java.io.*;\n" +
                    "import test.AnotherClass;\n" +
                    "public abstract class SomeClass {}\n");

        CompilationUnit result = _parser.compilationUnit(_helper.getProject());

        assertEquals(1, 1, 0, 4, 34, 97,
                     result);
        assertEquals(2, 1, 20, 2, 17, 36,
                     result.getImportDeclarations().get(0));
        assertEquals(3, 1, 38, 3, 25, 62,
                     result.getImportDeclarations().get(1));
        assertEquals(4, 1, 64, 4, 34, 97,
                     result.getTypeDeclarations().get(0));
        assertEquals(4, 1, 64, 4, 15, 78,
                     result.getTypeDeclarations().get(0).getModifiers());
    }

    public void testParsePosition10() throws ANTLRException
    {
        setupParser("public void someMethod(Object var) {\n"+
                    "  this.obj = null;\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 36, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 18,
                     stmt);

        AssignmentExpression assignExpr = (AssignmentExpression)stmt.getExpression();

        assertEquals(2, 3, 2, 17,
                     assignExpr);
        assertEquals(2, 14, 2, 17,
                     assignExpr.getValueExpression());

        FieldAccess access = (FieldAccess)assignExpr.getLeftHandSide();

        assertEquals(2, 3, 2, 10,
                     access);
        assertEquals(2, 3, 2, 6,
                     access.getBaseExpression());
    }

    public void testParsePosition11() throws ANTLRException
    {
        setupParser("public class SomeClass {\n"+
                    "  private boolean reentrantCall = false;\n"+
                    "}\n");

        ClassDeclaration decl = _parser.classDeclaration(false);

        assertEquals(1, 1, 3, 1,
                     decl);

        FieldDeclaration fieldDecl = decl.getFields().get(0);

        assertEquals(2, 3, 2, 39,
                     fieldDecl);
        assertEquals(2, 35, 2, 39,
                     fieldDecl.getInitializer());
        assertEquals(2, 35, 2, 39,
                     ((SingleInitializer)fieldDecl.getInitializer()).getInitEpression());
    }

    public void testParsePosition12() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr = boolean.class;\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 23,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 22,
                     expr);
        assertEquals(2, 3, 2, 6,
                     ((AssignmentExpression)expr).getLeftHandSide());
        assertEquals(2, 10, 2, 22,
                     ((AssignmentExpression)expr).getValueExpression());
    }

    public void testParsePosition13() throws ANTLRException
    {
        setupParser("public void someMethod(int var) {\n"+
                    "  var = super.attr;\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 33, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 19,
                     stmt);

        AssignmentExpression assignExpr = (AssignmentExpression)stmt.getExpression();

        assertEquals(2, 3, 2, 18,
                     assignExpr);
        assertEquals(2, 3, 2, 5,
                     assignExpr.getLeftHandSide());

        FieldAccess access = (FieldAccess)assignExpr.getValueExpression();

        assertEquals(2, 9, 2, 18,
                     access);
        assertEquals(2, 9, 2, 13,
                     access.getBaseExpression());
    }

    public void testParsePosition14() throws ANTLRException
    {
        setupParser("public SomeClass() {\n"+
                    "  this(\"ab\");\n"+
                    "}\n");

        Block result = _parser.constructorDeclaration().getBody();

        assertEquals(1, 20, 3, 1,
                     result);

        ExpressionStatement exprStmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 13,
                     exprStmt);

        ConstructorInvocation consInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertEquals(2, 3, 2, 12,
                     consInvoc);

        ArgumentList args = consInvoc.getArgumentList();

        assertEquals(2, 8, 2, 11,
                     args);
        assertEquals(2, 8, 2, 11,
                     args.getArguments().get(0));
    }

    public void testParsePosition15() throws ANTLRException
    {
        setupParser("public SomeClass() {\n"+
                    "  super('a');\n"+
                    "}\n");

        Block result = _parser.constructorDeclaration().getBody();

        assertEquals(1, 20, 3, 1,
                     result);

        ExpressionStatement exprStmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 13,
                     exprStmt);

        ConstructorInvocation consInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertEquals(2, 3, 2, 12,
                     consInvoc);

        ArgumentList args = consInvoc.getArgumentList();

        assertEquals(2, 9, 2, 11,
                     args);
        assertEquals(2, 9, 2, 11,
                     args.getArguments().get(0));
    }

    public void testParsePosition16() throws ANTLRException
    {
        setupParser("public void doSomething() {\n"+
                    "  somePackage.someClass.someAttr.doSomething();\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 27, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 47,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 46,
                     expr);

        expr = ((MethodInvocation)expr).getBaseExpression();

        assertEquals(2, 3, 2, 32,
                     expr);
    }

    public void testParsePosition17() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr = void.class;\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 20,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 19,
                     expr);
        assertEquals(2, 3, 2, 6,
                     ((AssignmentExpression)expr).getLeftHandSide());
        assertEquals(2, 10, 2, 19,
                     ((AssignmentExpression)expr).getValueExpression());
    }

    public void testParsePosition18() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr = new String(\"abc\");\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 3, 1,
                     result);

        ExpressionStatement exprStmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 27,
                     exprStmt);

        AssignmentExpression assignExpr = (AssignmentExpression)exprStmt.getExpression();

        assertEquals(2, 3, 2, 26,
                     assignExpr);
        assertEquals(2, 3, 2, 6,
                     assignExpr.getLeftHandSide());

        Instantiation instantiation = (Instantiation)assignExpr.getValueExpression();

        assertEquals(2, 10, 2, 26,
                     instantiation);

        ArgumentList args = instantiation.getArgumentList();

        assertEquals(2, 21, 2, 25,
                     args.getArguments().get(0));
    }

    public void testParsePosition19() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr = new String(\"abc\")\n"+
                    "  {\n"+
                    "    public String toString() { return null; }\n"+
                    "  };\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 6, 1,
                     result);

        ExpressionStatement exprStmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 5, 4,
                     exprStmt);

        AssignmentExpression assignExpr = (AssignmentExpression)exprStmt.getExpression();

        assertEquals(2, 3, 5, 3,
                     assignExpr);
        assertEquals(2, 3, 2, 6,
                     assignExpr.getLeftHandSide());

        Instantiation instantiation = (Instantiation)assignExpr.getValueExpression();

        assertEquals(2, 10, 5, 3,
                     instantiation);
        assertEquals(3, 3, 5, 3,
                     instantiation.getAnonymousClass());

        ArgumentList args = instantiation.getArgumentList();

        assertEquals(2, 21, 2, 25,
                     args.getArguments().get(0));
    }

    public void testParsePosition2() throws ANTLRException
    {
        setupParser("public interface SimpleInterface extends SomeInterface {\n" +
                    "  public int attr;\n" +
                    "  public abstract void doSomething();\n"+
                    "  public interface InnerInterface {\n"+
                    "  }\n"+
                    "  protected static class InnerClass {\n"+
                    "  }\n"+
                    "}\n");

        InterfaceDeclaration result = _parser.interfaceDeclaration();

        assertEquals(1, 1, 8, 1,
                     result);
        assertEquals(1, 1, 1, 6,
                     result.getModifiers());
        assertEquals(4, 3, 5, 3,
                     result.getInnerTypes().get(0));
        assertEquals(4, 3, 4, 8,
                     result.getInnerTypes().get(0).getModifiers());
        assertEquals(6, 3, 7, 3,
                     result.getInnerTypes().get(1));
        assertEquals(6, 3, 6, 18,
                     result.getInnerTypes().get(1).getModifiers());
        assertEquals(2, 3, 2, 17,
                     result.getFields().get(0));
        assertEquals(2, 3, 2, 8,
                     result.getFields().get(0).getModifiers());
        assertEquals(3, 3, 3, 37,
                     result.getMethods().get(0));
        assertEquals(3, 3, 3, 17,
                     result.getMethods().get(0).getModifiers());
    }

    public void testParsePosition20() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr = outer.new Inner();\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 27,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 26,
                     expr);
        assertEquals(2, 3, 2, 6,
                     ((AssignmentExpression)expr).getLeftHandSide());

        expr = ((AssignmentExpression)expr).getValueExpression();

        assertEquals(2, 10, 2, 26,
                     expr);
        assertEquals(2, 10, 2, 14,
                     ((Instantiation)expr).getBaseExpression());
    }

    public void testParsePosition21() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr = new int[0];\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 20,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 19,
                     expr);
        assertEquals(2, 3, 2, 6,
                     ((AssignmentExpression)expr).getLeftHandSide());
        assertEquals(2, 10, 2, 19,
                     ((AssignmentExpression)expr).getValueExpression());
    }

    public void testParsePosition22() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr = new java.lang.String[0][];\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 35,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 34,
                     expr);
        assertEquals(2, 3, 2, 6,
                     ((AssignmentExpression)expr).getLeftHandSide());
        assertEquals(2, 10, 2, 34,
                     ((AssignmentExpression)expr).getValueExpression());
    }

    public void testParsePosition23() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr = new SomeClass[]{ null, null };\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 39,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 38,
                     expr);
        assertEquals(2, 3, 2, 6,
                     ((AssignmentExpression)expr).getLeftHandSide());

        expr = ((AssignmentExpression)expr).getValueExpression();

        assertEquals(2, 10, 2, 38,
                     expr);
        assertEquals(2, 25, 2, 38,
                     ((ArrayCreation)expr).getInitializer());
    }

    public void testParsePosition24() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr = (4.0d);\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 16,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 15,
                     expr);
        assertEquals(2, 3, 2, 6,
                     ((AssignmentExpression)expr).getLeftHandSide());

        expr = ((AssignmentExpression)expr).getValueExpression();

        assertEquals(2, 10, 2, 15,
                     expr);
        assertEquals(2, 11, 2, 14,
                     ((ParenthesizedExpression)expr).getInnerExpression());
    }

    public void testParsePosition25() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr = Outer.super.getValue();\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 32,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 31,
                     expr);
        assertEquals(2, 3, 2, 6,
                     ((AssignmentExpression)expr).getLeftHandSide());

        expr = ((AssignmentExpression)expr).getValueExpression();

        assertEquals(2, 10, 2, 31,
                     expr);

        expr = ((MethodInvocation)expr).getBaseExpression();

        assertEquals(2, 10, 2, 20,
                     expr);
        assertEquals(2, 10, 2, 14,
                     ((SelfAccess)expr).getTypeAccess());
    }

    public void testParsePosition26() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr--;\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 9,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 8,
                     expr);
        assertEquals(2, 3, 2, 6,
                     ((PostfixExpression)expr).getInnerExpression());
    }

    public void testParsePosition27() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr = !convert(attr);\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 24,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 23,
                     expr);
        assertEquals(2, 3, 2, 6,
                     ((AssignmentExpression)expr).getLeftHandSide());

        expr = ((AssignmentExpression)expr).getValueExpression();

        assertEquals(2, 10, 2, 23,
                     expr);
        assertEquals(2, 11, 2, 23,
                     ((UnaryExpression)expr).getInnerExpression());
    }

    public void testParsePosition28() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr = (int)getValue();\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 25,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 24,
                     expr);
        assertEquals(2, 3, 2, 6,
                     ((AssignmentExpression)expr).getLeftHandSide());

        expr = ((AssignmentExpression)expr).getValueExpression();

        assertEquals(2, 10, 2, 24,
                     expr);
        assertEquals(2, 11, 2, 13,
                     ((UnaryExpression)expr).getCastType());
        assertEquals(2, 15, 2, 24,
                     ((UnaryExpression)expr).getInnerExpression());
    }

    public void testParsePosition29() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr = -getValue();\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 21,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 20,
                     expr);
        assertEquals(2, 3, 2, 6,
                     ((AssignmentExpression)expr).getLeftHandSide());

        expr = ((AssignmentExpression)expr).getValueExpression();

        assertEquals(2, 10, 2, 20,
                     expr);
        assertEquals(2, 11, 2, 20,
                     ((UnaryExpression)expr).getInnerExpression());
    }

    public void testParsePosition3() throws ANTLRException
    {
        setupParser("protected class SimpleClass {\n" +
                    "  static float test;\n"+
                    "  int attr, attr2 = 0;\n" +
                    "  {\n"+
                    "    attr = 0;\n"+
                    "  }\n"+
                    "  private static void doSomething(int value, String otherValue) {\n"+
                    "    attr2 = value;\n"+
                    "  }\n"+
                    "  static {\n"+
                    "    test = 0.0f;\n"+
                    "  }\n"+
                    "  public SimpleClass(int value) {}\n"+
                    "  private static class InnerClass {\n"+
                    "  }\n"+
                    "  protected interface InnerInterface {\n"+
                    "  }\n"+
                    "}\n");

        ClassDeclaration result = _parser.classDeclaration(false);

        assertEquals(1, 1, 18, 1,
                     result);
        assertEquals(1, 1, 1, 9,
                     result.getModifiers());
        assertEquals(14, 3, 15, 3,
                     result.getInnerTypes().get(0));
        assertEquals(14, 3, 14, 16,
                     result.getInnerTypes().get(0).getModifiers());
        assertEquals(16, 3, 17, 3,
                     result.getInnerTypes().get(1));
        assertEquals(16, 3, 16, 11,
                     result.getInnerTypes().get(1).getModifiers());
        assertEquals(4, 3, 6, 3,
                     result.getInitializers().get(0));
        assertEquals(4, 3, 6, 3,
                     result.getInitializers().get(0).getBody());
        assertEquals(10, 3, 12, 3,
                     result.getInitializers().get(1));
        assertEquals(10, 10, 12, 3,
                     result.getInitializers().get(1).getBody());
        assertEquals(2, 3, 2, 19,
                     result.getFields().get(0));
        assertEquals(2, 3, 2, 8,
                     result.getFields().get(0).getModifiers());
        assertEquals(3, 3, 3, 10,
                     result.getFields().get(1));
        assertEquals(3, 13, 3, 21,
                     result.getFields().get(2));
        assertEquals(13, 3, 13, 34,
                     result.getConstructors().get(0));
        assertEquals(13, 3, 13, 8,
                     result.getConstructors().get(0).getModifiers());
        assertEquals(13, 33, 13, 34,
                     result.getConstructors().get(0).getBody());
        assertEquals(7, 3, 9, 3,
                     result.getMethods().get(0));
        assertEquals(7, 3, 7, 16,
                     result.getMethods().get(0).getModifiers());
        assertEquals(7, 65, 9, 3,
                     result.getMethods().get(0).getBody());

        FormalParameterList params = result.getConstructors().get(0).getParameterList();

        assertEquals(13, 22, 13, 30,
                     params);
        assertEquals(13, 22, 13, 30,
                     params.getParameters().get(0));

        params = result.getMethods().get(0).getParameterList();

        assertEquals(7, 35, 7, 62,
                     params);
        assertEquals(7, 35, 7, 43,
                     params.getParameters().get(0));
        assertEquals(7, 46, 7, 62,
                     params.getParameters().get(1));
    }

    public void testParsePosition30() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr = getValue() / 2.0;\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 26,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 25,
                     expr);
        assertEquals(2, 3, 2, 6,
                     ((AssignmentExpression)expr).getLeftHandSide());

        expr = ((AssignmentExpression)expr).getValueExpression();

        assertEquals(2, 10, 2, 25,
                     expr);
        assertEquals(2, 10, 2, 19,
                     ((BinaryExpression)expr).getLeftOperand());
        assertEquals(2, 23, 2, 25,
                     ((BinaryExpression)expr).getRightOperand());
    }

    public void testParsePosition31() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr = getValue() + 2.0;\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 26,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 25,
                     expr);
        assertEquals(2, 3, 2, 6,
                     ((AssignmentExpression)expr).getLeftHandSide());

        expr = ((AssignmentExpression)expr).getValueExpression();

        assertEquals(2, 10, 2, 25,
                     expr);
        assertEquals(2, 10, 2, 19,
                     ((BinaryExpression)expr).getLeftOperand());
        assertEquals(2, 23, 2, 25,
                     ((BinaryExpression)expr).getRightOperand());
    }

    public void testParsePosition32() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr = getValue() >> 2;\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 25,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 24,
                     expr);
        assertEquals(2, 3, 2, 6,
                     ((AssignmentExpression)expr).getLeftHandSide());

        expr = ((AssignmentExpression)expr).getValueExpression();

        assertEquals(2, 10, 2, 24,
                     expr);
        assertEquals(2, 10, 2, 19,
                     ((BinaryExpression)expr).getLeftOperand());
        assertEquals(2, 24, 2, 24,
                     ((BinaryExpression)expr).getRightOperand());
    }

    public void testParsePosition33() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr = getValue() <= 2;\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 25,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 24,
                     expr);
        assertEquals(2, 3, 2, 6,
                     ((AssignmentExpression)expr).getLeftHandSide());

        expr = ((AssignmentExpression)expr).getValueExpression();

        assertEquals(2, 10, 2, 24,
                     expr);
        assertEquals(2, 10, 2, 19,
                     ((BinaryExpression)expr).getLeftOperand());
        assertEquals(2, 24, 2, 24,
                     ((BinaryExpression)expr).getRightOperand());
    }

    public void testParsePosition34() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr = getValue() != 2;\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 25,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 24,
                     expr);
        assertEquals(2, 3, 2, 6,
                     ((AssignmentExpression)expr).getLeftHandSide());

        expr = ((AssignmentExpression)expr).getValueExpression();

        assertEquals(2, 10, 2, 24,
                     expr);
        assertEquals(2, 10, 2, 19,
                     ((BinaryExpression)expr).getLeftOperand());
        assertEquals(2, 24, 2, 24,
                     ((BinaryExpression)expr).getRightOperand());
    }

    public void testParsePosition35() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr = getValue() | 2;\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 24,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 23,
                     expr);
        assertEquals(2, 3, 2, 6,
                     ((AssignmentExpression)expr).getLeftHandSide());

        expr = ((AssignmentExpression)expr).getValueExpression();

        assertEquals(2, 10, 2, 23,
                     expr);
        assertEquals(2, 10, 2, 19,
                     ((BinaryExpression)expr).getLeftOperand());
        assertEquals(2, 23, 2, 23,
                     ((BinaryExpression)expr).getRightOperand());
    }

    public void testParsePosition36() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr = getValue() & 2;\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 24,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 23,
                     expr);
        assertEquals(2, 3, 2, 6,
                     ((AssignmentExpression)expr).getLeftHandSide());

        expr = ((AssignmentExpression)expr).getValueExpression();

        assertEquals(2, 10, 2, 23,
                     expr);
        assertEquals(2, 10, 2, 19,
                     ((BinaryExpression)expr).getLeftOperand());
        assertEquals(2, 23, 2, 23,
                     ((BinaryExpression)expr).getRightOperand());
    }

    public void testParsePosition37() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr = getValue() && isCorrect;\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 33,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 32,
                     expr);
        assertEquals(2, 3, 2, 6,
                     ((AssignmentExpression)expr).getLeftHandSide());

        expr = ((AssignmentExpression)expr).getValueExpression();

        assertEquals(2, 10, 2, 32,
                     expr);
        assertEquals(2, 10, 2, 19,
                     ((BinaryExpression)expr).getLeftOperand());
        assertEquals(2, 24, 2, 32,
                     ((BinaryExpression)expr).getRightOperand());
    }

    public void testParsePosition38() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr = getValue() || isCorrect;\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 33,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 32,
                     expr);
        assertEquals(2, 3, 2, 6,
                     ((AssignmentExpression)expr).getLeftHandSide());

        expr = ((AssignmentExpression)expr).getValueExpression();

        assertEquals(2, 10, 2, 32,
                     expr);
        assertEquals(2, 10, 2, 19,
                     ((BinaryExpression)expr).getLeftOperand());
        assertEquals(2, 24, 2, 32,
                     ((BinaryExpression)expr).getRightOperand());
    }

    public void testParsePosition39() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  attr = getValue() > isCorrect ? 0 : getValue();\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 49,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 48,
                     expr);
        assertEquals(2, 3, 2, 6,
                     ((AssignmentExpression)expr).getLeftHandSide());

        expr = ((AssignmentExpression)expr).getValueExpression();

        assertEquals(2, 10, 2, 48,
                     expr);
        assertEquals(2, 35, 2, 35,
                     ((ConditionalExpression)expr).getTrueExpression());
        assertEquals(2, 39, 2, 48,
                     ((ConditionalExpression)expr).getFalseExpression());

        expr = ((ConditionalExpression)expr).getCondition();

        assertEquals(2, 10, 2, 31,
                     expr);
        assertEquals(2, 10, 2, 19,
                     ((BinaryExpression)expr).getLeftOperand());
        assertEquals(2, 23, 2, 31,
                     ((BinaryExpression)expr).getRightOperand());
    }

    public void testParsePosition4() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  int test = 0;\n"+
                    "label:\n"+
                    "  while (true) {\n"+
                    "    break label;\n"+
                    "  }\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 7, 1,
                     result);
        // local variable declarations do not include the semicolon !
        assertEquals(2, 3, 2, 14,
                     (LocalVariableDeclaration)result.getBlockStatements().get(0));

        Statement stmt = (Statement)result.getBlockStatements().get(1);

        assertEquals(3, 1, 6, 3,
                     stmt);

        stmt = ((LabeledStatement)stmt).getStatement();

        assertEquals(4, 3, 6, 3,
                     stmt);

        stmt = ((WhileStatement)stmt).getLoopStatement();

        assertEquals(4, 16, 6, 3,
                     stmt);

        stmt = (Statement)((Block)stmt).getBlockStatements().get(0);

        assertEquals(5, 5, 5, 16,
                     stmt);
    }

    public void testParsePosition5() throws ANTLRException
    {
        setupParser("public int someMethod(int value) {\n"+
                    "  class Local {\n"+
                    "  }\n"+
                    "  if (true) ;\n"+
                    "  else {\n"+
                    "    value += 1.0;\n"+
                    "  }\n"+
                    "  return value;\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 34, 9, 1,
                     result);
        assertEquals(2, 3, 3, 3,
                     (ClassDeclaration)result.getBlockStatements().get(0));

        Statement stmt = (Statement)result.getBlockStatements().get(1);

        assertEquals(4, 3, 7, 3,
                     stmt);
        assertEquals(4, 13, 4, 13,
                     ((IfThenElseStatement)stmt).getTrueStatement());

        stmt = ((IfThenElseStatement)stmt).getFalseStatement();

        assertEquals(5, 8, 7, 3,
                     stmt);
        assertEquals(6, 5, 6, 17,
                     (Statement)((Block)stmt).getBlockStatements().get(0));

        stmt = (Statement)result.getBlockStatements().get(2);

        assertEquals(8, 3, 8, 15,
                     stmt);
    }

    public void testParsePosition6() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  for (int idx = 0; idx < 5; idx++) {\n"+
                    "    try {\n"+
                    "      doSomething(idx);\n"+
                    "    }\n"+
                    "    catch (IOException ex) {}\n"+
                    "    finally {\n"+
                    "      continue;\n"+
                    "    }\n"+
                    "  }\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 11, 1,
                     result);

        ForStatement forStmt = (ForStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 10, 3,
                     forStmt);
        assertEquals(2, 8, 2, 18,
                     forStmt.getInitDeclarations().get(0));
        assertEquals(2, 21, 2, 27,
                     forStmt.getCondition());
        assertEquals(2, 30, 2, 34,
                     forStmt.getUpdateList());

        Block block = (Block)forStmt.getLoopStatement();

        assertEquals(2, 37, 10, 3,
                     block);

        TryStatement tryStmt = (TryStatement)block.getBlockStatements().get(0);

        assertEquals(3, 5, 9, 5,
                     tryStmt);
        assertEquals(6, 5, 6, 29,
                     tryStmt.getCatchClauses().get(0));
        assertEquals(6, 28, 6, 29,
                     tryStmt.getCatchClauses().get(0).getCatchBlock());
        // The finally clause is only stored as a block which
        // does not include the finally keyword
        assertEquals(7, 13, 9, 5,
                     tryStmt.getFinallyClause());
        assertEquals(8, 7, 8, 15,
                     (Node)tryStmt.getFinallyClause().getBlockStatements().get(0));

        block = tryStmt.getTryBlock();

        assertEquals(3, 9, 5, 5,
                     block);
        assertEquals(4, 7, 4, 23,
                     (Node)block.getBlockStatements().get(0));
    }

    public void testParsePosition7() throws ANTLRException
    {
        setupParser("public void someMethod() {\n"+
                    "  switch (getValue()) {\n"+
                    "    case 0:\n"+
                    "      synchronized (attr) {\n"+
                    "        do {\n"+
                    "          doSomething();\n"+
                    "        } while (getValue() > 0);\n"+
                    "      }\n"+
                    "      break;\n"+
                    "    default:\n"+
                    "      throw new TestException();\n"+
                    "  }\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 26, 13, 1,
                     result);

        SwitchStatement switchStmt = (SwitchStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 12, 3,
                     switchStmt);
        assertEquals(2, 11, 2, 20,
                     switchStmt.getSwitchExpression());

        CaseBlock caseBlock = switchStmt.getCaseBlocks().get(0);

        assertEquals(3, 5, 9, 12,
                     caseBlock);
        // The getCase method only returns the case expression
        // without the case keyword
        assertEquals(3, 10, 3, 10,
                     caseBlock.getCases().get(0));

        Statement stmt = (Statement)caseBlock.getBlockStatements().get(0);

        assertEquals(4, 7, 8, 7,
                     stmt);
        assertEquals(4, 21, 4, 24,
                     ((SynchronizedStatement)stmt).getLockExpression());

        stmt = ((SynchronizedStatement)stmt).getBlock();

        assertEquals(4, 27, 8, 7,
                     stmt);

        stmt = (Statement)((Block)stmt).getBlockStatements().get(0);

        assertEquals(5, 9, 7, 33,
                     stmt);
        assertEquals(7, 18, 7, 31,
                     ((DoWhileStatement)stmt).getCondition());

        stmt = ((DoWhileStatement)stmt).getLoopStatement();

        assertEquals(5, 12, 7, 9,
                     stmt);
        assertEquals(6, 11, 6, 24,
                     (Statement)((Block)stmt).getBlockStatements().get(0));

        assertEquals(9, 7, 9, 12,
                     (Statement)caseBlock.getBlockStatements().get(1));

        caseBlock = switchStmt.getCaseBlocks().get(1);

        assertEquals(10, 5, 11, 32,
                     caseBlock);
        assertEquals(11, 7, 11, 32,
                     (Statement)caseBlock.getBlockStatements().get(0));
    }

    public void testParsePosition8() throws ANTLRException
    {
        setupParser("public void someMethod(Object var) {\n"+
                    "  isSomeClass = var instanceof SomeClass;\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 36, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 41,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 40,
                     expr);
        assertEquals(2, 3, 2, 13,
                     ((AssignmentExpression)expr).getLeftHandSide());

        expr = ((AssignmentExpression)expr).getValueExpression();

        assertEquals(2, 17, 2, 40,
                     expr);
        assertEquals(2, 17, 2, 19,
                     ((InstanceofExpression)expr).getInnerExpression());
    }

    public void testParsePosition9() throws ANTLRException
    {
        setupParser("public void someMethod(Object var) {\n"+
                    "  arr[0] += getValue() | 2l;\n"+
                    "}\n");

        Block result = _parser.methodDeclaration().getBody();

        assertEquals(1, 36, 3, 1,
                     result);

        ExpressionStatement stmt = (ExpressionStatement)result.getBlockStatements().get(0);

        assertEquals(2, 3, 2, 28,
                     stmt);

        Expression expr = stmt.getExpression();

        assertEquals(2, 3, 2, 27,
                     expr);
        assertEquals(2, 3, 2, 8,
                     ((AssignmentExpression)expr).getLeftHandSide());

        expr = ((AssignmentExpression)expr).getValueExpression();

        assertEquals(2, 13, 2, 27,
                     expr);
        assertEquals(2, 13, 2, 22,
                     ((BinaryExpression)expr).getLeftOperand());
        assertEquals(2, 26, 2, 27,
                     ((BinaryExpression)expr).getRightOperand());
    }
}
