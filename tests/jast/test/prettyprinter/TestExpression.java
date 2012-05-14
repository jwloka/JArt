package jast.test.prettyprinter;
import jast.ParseException;
import jast.ast.ParsePosition;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ConditionalExpression;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.Initializer;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.UnaryExpression;

public class TestExpression extends TestPrettyPrinterBase
{

    public TestExpression(String name)
    {
        super(name);
    }

    public void testAssignmentExpression1() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private int var;\n"+
                "  {\n"+
                "    var = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.getOptions().doAssignExprSpacesAroundOperator(false);
        _prettyPrinter.visitAssignmentExpression(assignExpr);

        assertEquals("    var=0",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     assignExpr.getStartPosition());
        assertEquals(new ParsePosition(1, 9, 8),
                     assignExpr.getFinishPosition());
        assertEquals(new ParsePosition(1, 5, 4),
                     assignExpr.getLeftHandSide().getStartPosition());
        assertEquals(new ParsePosition(1, 7, 6),
                     assignExpr.getLeftHandSide().getFinishPosition());
        assertEquals(new ParsePosition(1, 9, 8),
                     assignExpr.getValueExpression().getStartPosition());
        assertEquals(new ParsePosition(1, 9, 8),
                     assignExpr.getValueExpression().getFinishPosition());
    }

    public void testAssignmentExpression2() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private String someReallyLongVariableName;\n"+
                "  {\n"+
                "    someReallyLongVariableName = \"a really long string value\";\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.getOptions().setMaxLineLength(40);
        _prettyPrinter.getOptions().setAssignExprIndentationLevel(1);
        _prettyPrinter.visitAssignmentExpression(assignExpr);

        // note that per default broken value expressions are indentated relative
        // to the operator but the string literal is indented absolute because the
        // assignment expression is broken not the value expression
        assertEquals("    someReallyLongVariableName =\n"+
                     "        \"a really long string value\"",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     assignExpr.getStartPosition());
        assertEquals(new ParsePosition(2, 36, 68),
                     assignExpr.getFinishPosition());
        assertEquals(new ParsePosition(1, 5, 4),
                     assignExpr.getLeftHandSide().getStartPosition());
        assertEquals(new ParsePosition(1, 30, 29),
                     assignExpr.getLeftHandSide().getFinishPosition());
        assertEquals(new ParsePosition(2, 9, 41),
                     assignExpr.getValueExpression().getStartPosition());
        assertEquals(new ParsePosition(2, 36, 68),
                     assignExpr.getValueExpression().getFinishPosition());
    }

    public void testAssignmentExpression3() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private String someLongVariableName;\n"+
                "  private String anotherLongVariableName;\n"+
                "  {\n"+
                "    someReallyLongVariableName = anotherLongVariableName = \"a long string value\";\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.getOptions().setMaxLineLength(40);
        _prettyPrinter.getOptions().setAssignExprIndentationLevel(1);
        _prettyPrinter.visitAssignmentExpression(assignExpr);

        // note that per default broken value expressions are indentated relative
        // to the operator but the string literal is indented absolute because the
        // assignment expression is broken not the value expression
        assertEquals("    someReallyLongVariableName =\n"+
                     "        anotherLongVariableName =\n"+
                     "            \"a long string value\"",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     assignExpr.getStartPosition());
        assertEquals(new ParsePosition(3, 33, 99),
                     assignExpr.getFinishPosition());
        assertEquals(new ParsePosition(1, 5, 4),
                     assignExpr.getLeftHandSide().getStartPosition());
        assertEquals(new ParsePosition(1, 30, 29),
                     assignExpr.getLeftHandSide().getFinishPosition());
        assertEquals(new ParsePosition(2, 9, 41),
                     assignExpr.getValueExpression().getStartPosition());
        assertEquals(new ParsePosition(3, 33, 99),
                     assignExpr.getValueExpression().getFinishPosition());
    }

    public void testAssignmentExpression4() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "abstract class A {\n"+
                "  private String someLongVariableName;\n"+
                "  private String someOtherName;\n"+
                "  {\n"+
                "    someLongVariableName = someReallyLongMethod(someOtherName).toString();\n"+
                "  }\n"+
                "  private String someReallyLongMethod();\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.getOptions().setMaxLineLength(55);
        _prettyPrinter.getOptions().doAssignExprAllowOperatorBreak(false);
        _prettyPrinter.visitAssignmentExpression(assignExpr);

        assertEquals("    someLongVariableName = someReallyLongMethod(\n"+
                     "                           someOtherName).toString()",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     assignExpr.getStartPosition());
        assertEquals(new ParsePosition(2, 52, 100),
                     assignExpr.getFinishPosition());
        assertEquals(new ParsePosition(1, 5, 4),
                     assignExpr.getLeftHandSide().getStartPosition());
        assertEquals(new ParsePosition(1, 24, 23),
                     assignExpr.getLeftHandSide().getFinishPosition());
        assertEquals(new ParsePosition(1, 28, 27),
                     assignExpr.getValueExpression().getStartPosition());
        assertEquals(new ParsePosition(2, 52, 100),
                     assignExpr.getValueExpression().getFinishPosition());
    }

    public void testAssignmentExpression5() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "abstract class A {\n"+
                "  private String[][] someLongVariableName;\n"+
                "  {\n"+
                "    someLongVariableName = new String[][] {{\"a\",\"b\" }, {\"c\", \"d\",\"e\" }};\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.getOptions().setMaxLineLength(40);
        _prettyPrinter.getOptions().doAssignExprAllowOperatorBreak(false);
        _prettyPrinter.visitAssignmentExpression(assignExpr);

        assertEquals("    someLongVariableName = new String[][]\n"+
                     "                           { { \"a\", \"b\" },\n"+
                     "                             { \"c\", \"d\",\n"+
                     "                               \"e\" } }",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     assignExpr.getStartPosition());
        assertEquals(new ParsePosition(4, 38, 163),
                     assignExpr.getFinishPosition());
        assertEquals(new ParsePosition(1, 5, 4),
                     assignExpr.getLeftHandSide().getStartPosition());
        assertEquals(new ParsePosition(1, 24, 23),
                     assignExpr.getLeftHandSide().getFinishPosition());
        assertEquals(new ParsePosition(1, 28, 27),
                     assignExpr.getValueExpression().getStartPosition());
        assertEquals(new ParsePosition(4, 38, 163),
                     assignExpr.getValueExpression().getFinishPosition());
    }

    public void testAssignmentExpression6() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "abstract class A {\n"+
                "  private String someLongVariableName;\n"+
                "  private String someOtherName;\n"+
                "  {\n"+
                "    someLongVariableName = someReallyLongMethod(someOtherName).toString();\n"+
                "  }\n"+
                "  private String someReallyLongMethod();\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.getOptions().setMaxLineLength(55);
        _prettyPrinter.getOptions().doAssignExprAllowOperatorBreak(false);
        _prettyPrinter.getOptions().doAssignExprRelativeIndentation(false);
        _prettyPrinter.getOptions().setAssignExprIndentationLevel(1);
        _prettyPrinter.visitAssignmentExpression(assignExpr);

        assertEquals("    someLongVariableName = someReallyLongMethod(\n"+
                     "        someOtherName).toString()",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     assignExpr.getStartPosition());
        assertEquals(new ParsePosition(2, 33, 81),
                     assignExpr.getFinishPosition());
        assertEquals(new ParsePosition(1, 5, 4),
                     assignExpr.getLeftHandSide().getStartPosition());
        assertEquals(new ParsePosition(1, 24, 23),
                     assignExpr.getLeftHandSide().getFinishPosition());
        assertEquals(new ParsePosition(1, 28, 27),
                     assignExpr.getValueExpression().getStartPosition());
        assertEquals(new ParsePosition(2, 33, 81),
                     assignExpr.getValueExpression().getFinishPosition());
    }

    public void testAssignmentExpression7() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private int someLongVariableName;\n"+
                "  private int anotherLongVariableName;\n"+
                "  private int aReallyLongVariableName;\n"+
                "  {\n"+
                "    someLongVariableName = anotherLongVariableName>aReallyLongVariableName?someLongVariableName:aReallyLongVariableName;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration      classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer           initializer = classDecl.getInitializers().get(0);
        ExpressionStatement   exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression  assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        ConditionalExpression condExpr    = (ConditionalExpression)assignExpr.getValueExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.getOptions().doAssignExprAllowOperatorBreak(false);
        _prettyPrinter.getOptions().doCondExprBreakAllIfTooLong(true);
        _prettyPrinter.getOptions().setCondExprIndentationLevel(1);
        _prettyPrinter.visitAssignmentExpression(assignExpr);

        assertEquals("    someLongVariableName = anotherLongVariableName > aReallyLongVariableName ?\n"+
                     "                               someLongVariableName :\n"+
                     "                               aReallyLongVariableName",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     assignExpr.getStartPosition());
        assertEquals(new ParsePosition(3, 54, 186),
                     assignExpr.getFinishPosition());
        assertEquals(new ParsePosition(1, 28, 27),
                     condExpr.getStartPosition());
        assertEquals(new ParsePosition(3, 54, 186),
                     condExpr.getFinishPosition());
        assertEquals(new ParsePosition(1, 28, 27),
                     condExpr.getCondition().getStartPosition());
        assertEquals(new ParsePosition(1, 76, 75),
                     condExpr.getCondition().getFinishPosition());
        assertEquals(new ParsePosition(2, 32, 110),
                     condExpr.getTrueExpression().getStartPosition());
        assertEquals(new ParsePosition(2, 51, 129),
                     condExpr.getTrueExpression().getFinishPosition());
        assertEquals(new ParsePosition(3, 32, 164),
                     condExpr.getFalseExpression().getStartPosition());
        assertEquals(new ParsePosition(3, 54, 186),
                     condExpr.getFalseExpression().getFinishPosition());
    }

    public void testBinaryExpression1() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private int var;\n"+
                "  {\n"+
                "    var = 1+2-3*4;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        BinaryExpression     binExpr     = (BinaryExpression)assignExpr.getValueExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.visitBinaryExpression(binExpr);

        assertEquals("    1 + 2 - 3 * 4",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     binExpr.getStartPosition());
        assertEquals(new ParsePosition(1, 17, 16),
                     binExpr.getFinishPosition());
        assertEquals(new ParsePosition(1, 5, 4),
                     binExpr.getLeftOperand().getStartPosition());
        assertEquals(new ParsePosition(1, 9, 8),
                     binExpr.getLeftOperand().getFinishPosition());
        assertEquals(new ParsePosition(1, 13, 12),
                     binExpr.getRightOperand().getStartPosition());
        assertEquals(new ParsePosition(1, 17, 16),
                     binExpr.getRightOperand().getFinishPosition());
    }

    public void testBinaryExpression2() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private String someLongVarName;\n"+
                "  private String someOtherLongVarName;\n"+
                "  private String anotherLongVarName;\n"+
                "  private String result;\n"+
                "  {\n"+
                "    result = someLongVarName + someOtherLongVarName + anotherLongVarName;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        BinaryExpression     binExpr     = (BinaryExpression)assignExpr.getValueExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.getOptions().setMaxLineLength(20);
        _prettyPrinter.getOptions().doBinExprSpacesAroundOperator(false);
        _prettyPrinter.getOptions().doBinExprBreakAfterOperator(false);
        _prettyPrinter.visitBinaryExpression(binExpr);

        assertEquals("    someLongVarName\n"+
                     "    +someOtherLongVarName\n"+
                     "    +anotherLongVarName",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     binExpr.getStartPosition());
        assertEquals(new ParsePosition(3, 23, 68),
                     binExpr.getFinishPosition());
        assertEquals(new ParsePosition(1, 5, 4),
                     binExpr.getLeftOperand().getStartPosition());
        assertEquals(new ParsePosition(2, 25, 44),
                     binExpr.getLeftOperand().getFinishPosition());
        assertEquals(new ParsePosition(3, 6, 51),
                     binExpr.getRightOperand().getStartPosition());
        assertEquals(new ParsePosition(3, 23, 68),
                     binExpr.getRightOperand().getFinishPosition());
    }

    public void testConditionalExpression1() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private int var;\n"+
                "  {\n"+
                "    var = var > 0 ? 0 : 1;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration      classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer           initializer = classDecl.getInitializers().get(0);
        ExpressionStatement   exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression  assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        ConditionalExpression condExpr    = (ConditionalExpression)assignExpr.getValueExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.getOptions().doCondExprSpacesAroundOperators(false);
        _prettyPrinter.visitConditionalExpression(condExpr);

        assertEquals("    var > 0?0:1",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     condExpr.getStartPosition());
        assertEquals(new ParsePosition(1, 15, 14),
                     condExpr.getFinishPosition());
        assertEquals(new ParsePosition(1, 5, 4),
                     condExpr.getCondition().getStartPosition());
        assertEquals(new ParsePosition(1, 11, 10),
                     condExpr.getCondition().getFinishPosition());
        assertEquals(new ParsePosition(1, 13, 12),
                     condExpr.getTrueExpression().getStartPosition());
        assertEquals(new ParsePosition(1, 13, 12),
                     condExpr.getTrueExpression().getFinishPosition());
        assertEquals(new ParsePosition(1, 15, 14),
                     condExpr.getFalseExpression().getStartPosition());
        assertEquals(new ParsePosition(1, 15, 14),
                     condExpr.getFalseExpression().getFinishPosition());
    }

    public void testConditionalExpression2() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private int someLongVariableName;\n"+
                "  private int anotherLongVariableName;\n"+
                "  private int aReallyLongVariableName;\n"+
                "  {\n"+
                "    someLongVariableName = anotherLongVariableName>aReallyLongVariableName?someLongVariableName:aReallyLongVariableName;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration      classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer           initializer = classDecl.getInitializers().get(0);
        ExpressionStatement   exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression  assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        ConditionalExpression condExpr    = (ConditionalExpression)assignExpr.getValueExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.getOptions().doCondExprBreakAllIfTooLong(false);
        _prettyPrinter.getOptions().setCondExprIndentationLevel(1);
        _prettyPrinter.getOptions().doCondExprBreakAfterOperator(false);
        _prettyPrinter.visitConditionalExpression(condExpr);

        assertEquals("    anotherLongVariableName > aReallyLongVariableName ? someLongVariableName\n"+
                     "        : aReallyLongVariableName",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     condExpr.getStartPosition());
        assertEquals(new ParsePosition(2, 33, 109),
                     condExpr.getFinishPosition());
        assertEquals(new ParsePosition(1, 5, 4),
                     condExpr.getCondition().getStartPosition());
        assertEquals(new ParsePosition(1, 53, 52),
                     condExpr.getCondition().getFinishPosition());
        assertEquals(new ParsePosition(1, 57, 56),
                     condExpr.getTrueExpression().getStartPosition());
        assertEquals(new ParsePosition(1, 76, 75),
                     condExpr.getTrueExpression().getFinishPosition());
        assertEquals(new ParsePosition(2, 11, 87),
                     condExpr.getFalseExpression().getStartPosition());
        assertEquals(new ParsePosition(2, 33, 109),
                     condExpr.getFalseExpression().getFinishPosition());
    }

    public void testPostfixExpression1() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private int var;\n"+
                "  {\n"+
                "    var++;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer         initializer = classDecl.getInitializers().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        PostfixExpression   postfixExpr = (PostfixExpression)exprStmt.getExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.visitPostfixExpression(postfixExpr);

        assertEquals("    var++",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     postfixExpr.getStartPosition());
        assertEquals(new ParsePosition(1, 9, 8),
                     postfixExpr.getFinishPosition());
        assertEquals(new ParsePosition(1, 5, 4),
                     postfixExpr.getInnerExpression().getStartPosition());
        assertEquals(new ParsePosition(1, 7, 6),
                     postfixExpr.getInnerExpression().getFinishPosition());
    }

    public void testPostfixExpression2() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private int var;\n"+
                "  {\n"+
                "    (var)/*bla*/--;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer         initializer = classDecl.getInitializers().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        PostfixExpression   postfixExpr = (PostfixExpression)exprStmt.getExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.visitPostfixExpression(postfixExpr);

        assertEquals("    (var) /* bla */ --",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     postfixExpr.getStartPosition());
        assertEquals(new ParsePosition(1, 22, 21),
                     postfixExpr.getFinishPosition());
        assertEquals(new ParsePosition(1, 5, 4),
                     postfixExpr.getInnerExpression().getStartPosition());
        assertEquals(new ParsePosition(1, 9, 8),
                     postfixExpr.getInnerExpression().getFinishPosition());
    }

    public void testUnaryExpression1() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private int var;\n"+
                "  {\n"+
                "    ++ /*bla*/ var;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration    classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer         initializer = classDecl.getInitializers().get(0);
        ExpressionStatement exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        UnaryExpression     unaryExpr   = (UnaryExpression)exprStmt.getExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.visitUnaryExpression(unaryExpr);

        assertEquals("    ++var /* bla */ ",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     unaryExpr.getStartPosition());
        assertEquals(new ParsePosition(1, 9, 8),
                     unaryExpr.getFinishPosition());
        assertEquals(new ParsePosition(1, 7, 6),
                     unaryExpr.getInnerExpression().getStartPosition());
        assertEquals(new ParsePosition(1, 9, 8),
                     unaryExpr.getInnerExpression().getFinishPosition());
    }

    public void testUnaryExpression2() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private Object obj;\n"+
                "  private String txt;\n"+
                "  {\n"+
                "    txt = (java.lang.Object)obj;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();;
        UnaryExpression      unaryExpr   = (UnaryExpression)assignExpr.getValueExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.getOptions().doCastSpaceAfterParen(true);
        _prettyPrinter.visitUnaryExpression(unaryExpr);

        assertEquals("    (Object) obj",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     unaryExpr.getStartPosition());
        assertEquals(new ParsePosition(1, 16, 15),
                     unaryExpr.getFinishPosition());
        assertEquals(new ParsePosition(1, 6, 5),
                     unaryExpr.getCastType().getStartPosition());
        assertEquals(new ParsePosition(1, 11, 10),
                     unaryExpr.getCastType().getFinishPosition());
        assertEquals(new ParsePosition(1, 14, 13),
                     unaryExpr.getInnerExpression().getStartPosition());
        assertEquals(new ParsePosition(1, 16, 15),
                     unaryExpr.getInnerExpression().getFinishPosition());
    }

    public void testUnaryExpression3() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private Object obj;\n"+
                "  private String txt;\n"+
                "  {\n"+
                "    txt = (java.lang.Object/*bla*/)obj;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();;
        UnaryExpression      unaryExpr   = (UnaryExpression)assignExpr.getValueExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.getOptions().doCastSpacesAroundType(true);
        _prettyPrinter.visitUnaryExpression(unaryExpr);

        assertEquals("    ( Object /* bla */ )obj",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     unaryExpr.getStartPosition());
        assertEquals(new ParsePosition(1, 27, 26),
                     unaryExpr.getFinishPosition());
        assertEquals(new ParsePosition(1, 7, 6),
                     unaryExpr.getCastType().getStartPosition());
        assertEquals(new ParsePosition(1, 12, 11),
                     unaryExpr.getCastType().getFinishPosition());
        assertEquals(new ParsePosition(1, 25, 24),
                     unaryExpr.getInnerExpression().getStartPosition());
        assertEquals(new ParsePosition(1, 27, 26),
                     unaryExpr.getInnerExpression().getFinishPosition());
    }
}
