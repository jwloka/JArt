package jast.test.prettyprinter;
import jast.ParseException;
import jast.ast.ParsePosition;
import jast.ast.nodes.ArrayAccess;
import jast.ast.nodes.ArrayCreation;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.ClassAccess;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.ConstructorInvocation;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.Initializer;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.ParenthesizedExpression;
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.SelfAccess;
import jast.ast.nodes.TypeAccess;
import jast.ast.nodes.VariableAccess;
import jast.prettyprinter.Options;

public class TestPrimary extends TestPrettyPrinterBase
{

    public TestPrimary(String name)
    {
        super(name);
    }

    public void testArgumentList1() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  {\n"+
                "    doSomething();\n"+
                "  }\n"+
                "}\n",
                true);

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        MethodInvocation     methodInvoc = (MethodInvocation)exprStmt.getExpression();

        _prettyPrinter.visitMethodInvocation(methodInvoc);

        assertEquals("doSomething()",
                     getText());
        assertEquals(new ParsePosition(1, 1, 0),
                     methodInvoc.getStartPosition());
        assertEquals(new ParsePosition(1, 13, 12),
                     methodInvoc.getFinishPosition());
    }

    public void testArgumentList2() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  {\n"+
                "    doSomething(someAttribute,anotherAttribute);\n"+
                "  }\n"+
                "}\n",
                true);

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        MethodInvocation     methodInvoc = (MethodInvocation)exprStmt.getExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.visitMethodInvocation(methodInvoc);

        assertEquals("    doSomething(someAttribute, anotherAttribute)",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     methodInvoc.getStartPosition());
        assertEquals(new ParsePosition(1, 48, 47),
                     methodInvoc.getFinishPosition());
        assertEquals(new ParsePosition(1, 17, 16),
                     methodInvoc.getArgumentList().getStartPosition());
        assertEquals(new ParsePosition(1, 47, 46),
                     methodInvoc.getArgumentList().getFinishPosition());
    }

    public void testArgumentList3() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  {\n"+
                "    doSomething(someAttribute,anotherAttribute);\n"+
                "  }\n"+
                "}\n",
                true);

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        MethodInvocation     methodInvoc = (MethodInvocation)exprStmt.getExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.getOptions().setExprListIndentationLevel(1);
        _prettyPrinter.getOptions().setExprListIndentationMode(Options.LIST_ALIGN_SUBSEQUENT);
        _prettyPrinter.getOptions().setExprListBreakLimit(1);    // break always
        _prettyPrinter.visitMethodInvocation(methodInvoc);

        assertEquals("    doSomething(someAttribute,\n"+
                     "                    anotherAttribute)",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     methodInvoc.getStartPosition());
        assertEquals(new ParsePosition(2, 37, 67),
                     methodInvoc.getFinishPosition());
        assertEquals(new ParsePosition(1, 17, 16),
                     methodInvoc.getArgumentList().getStartPosition());
        assertEquals(new ParsePosition(2, 36, 66),
                     methodInvoc.getArgumentList().getFinishPosition());
    }

    public void testArgumentList4() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  {\n"+
                "    doSomething(someAttribute,anotherAttribute);\n"+
                "  }\n"+
                "}\n",
                true);

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        MethodInvocation     methodInvoc = (MethodInvocation)exprStmt.getExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.getOptions().setMaxLineLength(20);
        _prettyPrinter.getOptions().doExprListSurroundBySpaces(true);
        _prettyPrinter.getOptions().setExprListIndentationMode(Options.LIST_RELATIVE_INDENTATION);
        _prettyPrinter.getOptions().setExprListIndentationLevel(1);
        _prettyPrinter.visitMethodInvocation(methodInvoc);

        assertEquals("    doSomething(\n"+
                     "        someAttribute,\n"+
                     "        anotherAttribute )",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     methodInvoc.getStartPosition());
        assertEquals(new ParsePosition(3, 26, 65),
                     methodInvoc.getFinishPosition());
        assertEquals(new ParsePosition(2, 9, 25),
                     methodInvoc.getArgumentList().getStartPosition());
        assertEquals(new ParsePosition(3, 24, 63),
                     methodInvoc.getArgumentList().getFinishPosition());
    }

    public void testArgumentList5() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  {\n"+
                "    doSomething(someAttribute, val1, val2, val3);\n"+
                "  }\n"+
                "}\n",
                true);

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        MethodInvocation     methodInvoc = (MethodInvocation)exprStmt.getExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.getOptions().setMaxLineLength(35);
        _prettyPrinter.getOptions().doExprListBreakAllIfTooLong(true);
        _prettyPrinter.getOptions().setExprListIndentationMode(Options.LIST_ALIGN_SUBSEQUENT);
        _prettyPrinter.visitMethodInvocation(methodInvoc);

        assertEquals("    doSomething(someAttribute,\n"+
                     "                val1,\n"+
                     "                val2,\n"+
                     "                val3)",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     methodInvoc.getStartPosition());
        assertEquals(new ParsePosition(4, 21, 95),
                     methodInvoc.getFinishPosition());
        assertEquals(new ParsePosition(1, 17, 16),
                     methodInvoc.getArgumentList().getStartPosition());
        assertEquals(new ParsePosition(4, 20, 94),
                     methodInvoc.getArgumentList().getFinishPosition());
    }

    public void testArrayAccess1() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private Object[] someObjectArray;\n"+
                "  private int      someIndexExpression;\n"+
                "  {\n"+
                "    someObjectArray[someIndexExpression] = null;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        ArrayAccess          arrayAccess = (ArrayAccess)assignExpr.getLeftHandSide();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.visitArrayAccess(arrayAccess);

        assertEquals("    someObjectArray[someIndexExpression]",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     arrayAccess.getStartPosition());
        assertEquals(new ParsePosition(1, 40, 39),
                     arrayAccess.getFinishPosition());
    }

    public void testArrayAccess2() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private Object[] someObjectArray;\n"+
                "  private int      someIndexExpression;\n"+
                "  {\n"+
                "    someObjectArray[someIndexExpression] = null;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        ArrayAccess          arrayAccess = (ArrayAccess)assignExpr.getLeftHandSide();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.getOptions().setMaxLineLength(15);
        _prettyPrinter.getOptions().doArrExprBreakBeforeBracket(false);
        _prettyPrinter.getOptions().doArrExprSpacesAroundIndex(true);
        _prettyPrinter.visitArrayAccess(arrayAccess);

        assertEquals("    someObjectArray[\n"+
                     "    someIndexExpression ]",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     arrayAccess.getStartPosition());
        assertEquals(new ParsePosition(2, 25, 45),
                     arrayAccess.getFinishPosition());
    }

    public void testArrayCreation1() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private Object obj;\n"+
                "  {\n"+
                "    obj = new java.lang.Object[1];\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl     = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer   = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt      = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        ArrayCreation        arrayCreation = (ArrayCreation)assignExpr.getValueExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.visitArrayCreation(arrayCreation);

        assertEquals("    new Object[1]",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     arrayCreation.getStartPosition());
        assertEquals(new ParsePosition(1, 17, 16),
                     arrayCreation.getFinishPosition());
    }

    public void testArrayCreation2() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private Object obj;\n"+
                "  {\n"+
                "    obj = new Object[]{null};\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl     = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer   = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt      = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        ArrayCreation        arrayCreation = (ArrayCreation)assignExpr.getValueExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.visitArrayCreation(arrayCreation);

        assertEquals("    new Object[] { null }",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     arrayCreation.getStartPosition());
        assertEquals(new ParsePosition(1, 25, 24),
                     arrayCreation.getFinishPosition());
        assertEquals(new ParsePosition(1, 18, 17),
                     arrayCreation.getInitializer().getStartPosition());
        assertEquals(new ParsePosition(1, 25, 24),
                     arrayCreation.getInitializer().getFinishPosition());
    }

    public void testArrayCreation3() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private Object obj;\n"+
                "  {\n"+
                "    obj = new String[]{\"some long string\",\"another long string\",\"and another long string\"};\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl     = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer   = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt      = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        ArrayCreation        arrayCreation = (ArrayCreation)assignExpr.getValueExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.getOptions().setMaxLineLength(40);
        _prettyPrinter.getOptions().setArrCreationInitBreakMode(Options.ARRCREATION_NO_BREAK);
        _prettyPrinter.getOptions().doArrInitBreakAllIfTooLong(true);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.visitArrayCreation(arrayCreation);

        assertEquals("    new String[] { \"some long string\",\n"+
                     "                   \"another long string\",\n"+
                     "                   \"and another long string\" }",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     arrayCreation.getStartPosition());
        assertEquals(new ParsePosition(3, 46, 126),
                     arrayCreation.getFinishPosition());
        assertEquals(new ParsePosition(1, 18, 17),
                     arrayCreation.getInitializer().getStartPosition());
        assertEquals(new ParsePosition(3, 46, 126),
                     arrayCreation.getInitializer().getFinishPosition());
    }

    public void testArrayCreation4() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private Object obj;\n"+
                "  {\n"+
                "    obj = new String[][]{{\"a\", \"b\", \"c\"},{\"d\", \"e\"}};\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl     = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer   = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt      = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr    = (AssignmentExpression)exprStmt.getExpression();
        ArrayCreation        arrayCreation = (ArrayCreation)assignExpr.getValueExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.getOptions().setMaxLineLength(40);
        _prettyPrinter.getOptions().doArrInitSpaceAfterComma(false);
        _prettyPrinter.getOptions().setArrCreationInitBreakMode(Options.ARRCREATION_CREATION_INDENTATION);
        _prettyPrinter.getOptions().setArrInitBreakLimit(3);
        _prettyPrinter.getOptions().setArrInitIndentationMode(Options.ARRINIT_INITIALIZER_INDENTATION);
        _prettyPrinter.getOptions().setArrInitIndentationLevel(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.visitArrayCreation(arrayCreation);

        // Note that only broken sub-initializers are indented (ie the second one)
        assertEquals("    new String[][]\n"+
                     "        { { \"a\",\n"+
                     "            \"b\",\n"+
                     "            \"c\" },\n"+
                     "            { \"d\",\"e\" } }",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     arrayCreation.getStartPosition());
        assertEquals(new ParsePosition(5, 25, 96),
                     arrayCreation.getFinishPosition());
        assertEquals(new ParsePosition(2, 9, 27),
                     arrayCreation.getInitializer().getStartPosition());
        assertEquals(new ParsePosition(5, 25, 96),
                     arrayCreation.getInitializer().getFinishPosition());
    }

    public void testClassAccess1() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private Object attr;\n"+
                "  {\n"+
                "    attr = test.A.class;\n"+
                "  }\n"+
                "}\n",
                true);

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        ClassAccess          classAccess = (ClassAccess)assignExpr.getValueExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.visitClassAccess(classAccess);

        assertEquals("    test.A.class",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     classAccess.getStartPosition());
        assertEquals(new ParsePosition(1, 16, 15),
                     classAccess.getFinishPosition());
        assertEquals(new ParsePosition(1, 5, 4),
                     classAccess.getReferencedType().getStartPosition());
        assertEquals(new ParsePosition(1, 10, 9),
                     classAccess.getReferencedType().getFinishPosition());
    }

    public void testClassAccess2() throws ParseException
    {
        addType("reallyLongPackageName.AReallyLongClassName",
                "package reallyLongPackageName;\n"+
                "class AReallyLongClassName {\n"+
                "  private Object attr;\n"+
                "  {\n"+
                "    attr = reallyLongPackageName.AReallyLongClassName.class;\n"+
                "  }\n"+
                "}\n",
                true);

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("reallyLongPackageName.AReallyLongClassName");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        ClassAccess          classAccess = (ClassAccess)assignExpr.getValueExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.getOptions().setMaxLineLength(20);
        _prettyPrinter.getOptions().setDotBreakIndentationLevel(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.visitClassAccess(classAccess);

        assertEquals("    reallyLongPackageName.AReallyLongClassName\n"+
                     "        .class",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     classAccess.getStartPosition());
        assertEquals(new ParsePosition(2, 14, 60),
                     classAccess.getFinishPosition());
        assertEquals(new ParsePosition(1, 5, 4),
                     classAccess.getReferencedType().getStartPosition());
        assertEquals(new ParsePosition(1, 46, 45),
                     classAccess.getReferencedType().getFinishPosition());
    }

    public void testConstructorInvocation1() throws ParseException
    {
        addType("test.C",
                "package test;\n"+
                "public class C {\n"+
                "  public C(int someValue) {}\n"+
                "}",
                true);
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  public C getSomeCObject() { return null; }\n"+
                "  class B extends C {\n"+
                "    public B() {\n"+
                "      getSomeCObject().super(0);\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration       classDecl = (ClassDeclaration)_project.getType("test.A.B");
        ConstructorDeclaration consDecl  = classDecl.getConstructors().get(0);
        ExpressionStatement    exprStmt  = (ExpressionStatement)consDecl.getBody().getBlockStatements().get(0);
        ConstructorInvocation  consInvoc = (ConstructorInvocation)exprStmt.getExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.getOptions().doExprListSurroundBySpaces(true);
        _prettyPrinter.visitConstructorInvocation(consInvoc);

        assertEquals("    getSomeCObject().super( 0 )",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     consInvoc.getStartPosition());
        assertEquals(new ParsePosition(1, 31, 30),
                     consInvoc.getFinishPosition());
        assertEquals(new ParsePosition(1, 29, 28),
                     consInvoc.getArgumentList().getStartPosition());
        assertEquals(new ParsePosition(1, 29, 28),
                     consInvoc.getArgumentList().getFinishPosition());
    }

    public void testFieldAccess1() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  private int attr;\n"+
                "  {\n"+
                "    attr = 0;\n"+
                "  }\n"+
                "}\n",
                true);

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.visitFieldAccess(fieldAccess);

        assertEquals("    attr",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(1, 8, 7),
                     fieldAccess.getFinishPosition());
    }

    public void testFieldAccess2() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  private int attr;\n"+
                "  {\n"+
                "    attr = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        _prettyPrinter.visitFieldAccess(fieldAccess);

        assertEquals("attr",
                     getText());
        assertEquals(new ParsePosition(1, 1, 0),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(1, 4, 3),
                     fieldAccess.getFinishPosition());
    }

    public void testInstantiation1() throws ParseException
    {
        addType("test.C",
                "package test;\n"+
                "public class C {\n"+
                "  public C(int someValue) {}\n"+
                "}",
                true);
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  public C getSomeCObject() { return null; }\n"+
                "  class B extends C {\n"+
                "    public B getValue() {\n"+
                "      return getSomeCObject().new B();\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration methodDecl    = _project.getType("test.A.B").getMethods().get(0);
        ReturnStatement   returnStmt    = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation     instantiation = (Instantiation)returnStmt.getReturnValue();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.getOptions().doExprListSurroundBySpaces(true);
        _prettyPrinter.visitInstantiation(instantiation);

        assertEquals("    getSomeCObject().new B()",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     instantiation.getStartPosition());
        assertEquals(new ParsePosition(1, 28, 27),
                     instantiation.getFinishPosition());
        assertEquals(new ParsePosition(1, 26, 25),
                     instantiation.getInstantiatedType().getStartPosition());
        assertEquals(new ParsePosition(1, 26, 25),
                     instantiation.getInstantiatedType().getFinishPosition());
    }

    public void testParenExpr1() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private int someAttribute;\n"+
                "  private int otherAttribute;\n"+
                "  {\n"+
                "    someAttribute = (otherAttribute);\n"+
                "  }\n"+
                "}\n",
                true);

        ClassDeclaration        classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer             initializer = classDecl.getInitializers().get(0);
        ExpressionStatement     exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression    assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        ParenthesizedExpression parenExpr   = (ParenthesizedExpression)assignExpr.getValueExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.getOptions().doParenExprSpacesAroundInner(false);
        _prettyPrinter.visitParenthesizedExpression(parenExpr);

        assertEquals("    (otherAttribute)",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     parenExpr.getStartPosition());
        assertEquals(new ParsePosition(1, 20, 19),
                     parenExpr.getFinishPosition());
        assertEquals(new ParsePosition(1, 6, 5),
                     parenExpr.getInnerExpression().getStartPosition());
        assertEquals(new ParsePosition(1, 19, 18),
                     parenExpr.getInnerExpression().getFinishPosition());
    }

    public void testParenExpr2() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private int someAttribute;\n"+
                "  private int otherAttribute;\n"+
                "  {\n"+
                "    someAttribute = (otherAttribute);\n"+
                "  }\n"+
                "}\n",
                true);

        ClassDeclaration        classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer             initializer = classDecl.getInitializers().get(0);
        ExpressionStatement     exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression    assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        ParenthesizedExpression parenExpr   = (ParenthesizedExpression)assignExpr.getValueExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.getOptions().doParenExprSpacesAroundInner(true);
        _prettyPrinter.visitParenthesizedExpression(parenExpr);

        assertEquals("    ( otherAttribute )",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     parenExpr.getStartPosition());
        assertEquals(new ParsePosition(1, 22, 21),
                     parenExpr.getFinishPosition());
        assertEquals(new ParsePosition(1, 7, 6),
                     parenExpr.getInnerExpression().getStartPosition());
        assertEquals(new ParsePosition(1, 20, 19),
                     parenExpr.getInnerExpression().getFinishPosition());
    }

    public void testSelfAccess1() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private Object self;\n"+
                "  {\n"+
                "    self = this;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        SelfAccess           selfAccess  = (SelfAccess)assignExpr.getValueExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.visitSelfAccess(selfAccess);

        assertEquals("    this",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     selfAccess.getStartPosition());
        assertEquals(new ParsePosition(1, 8, 7),
                     selfAccess.getFinishPosition());
    }

    public void testSelfAccess2() throws ParseException
    {
        addType("test.B",
                "package test;\n"+
                "class B {\n"+
                "  protected Object longAttributeName;\n"+
                "}\n",
                true);
        addType("test.A",
                "package test;\n"+
                "class A extends B {\n"+
                "  private Object self;\n"+
                "  {\n"+
                "    self = super.longAttributeName;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getValueExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.getOptions().setMaxLineLength(20);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.visitFieldAccess(fieldAccess);

        assertEquals("    super\n"+
                     "    .longAttributeName",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(2, 22, 31),
                     fieldAccess.getFinishPosition());
        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getBaseExpression().getStartPosition());
        assertEquals(new ParsePosition(1, 9, 8),
                     fieldAccess.getBaseExpression().getFinishPosition());
    }

    public void testSelfAccess3() throws ParseException
    {
        addType("test.ReallyTheOuterClass",
                "package test;\n"+
                "class ReallyTheOuterClass {\n"+
                "  class AndTheInnerClass {\n"+
                "    private Object longAttributeName;\n"+
                "    {\n"+
                "      ReallyTheOuterClass.this.longAttributeName = null;\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("test.ReallyTheOuterClass.AndTheInnerClass");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.getOptions().setMaxLineLength(20);
        _prettyPrinter.getOptions().doBreakEveryDotIfTooLong(true);
        _prettyPrinter.getOptions().setDotBreakIndentationLevel(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.visitFieldAccess(fieldAccess);

        assertEquals("    ReallyTheOuterClass\n"+
                     "        .this\n"+
                     "        .longAttributeName",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(3, 26, 63),
                     fieldAccess.getFinishPosition());
        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getBaseExpression().getStartPosition());
        assertEquals(new ParsePosition(2, 13, 36),
                     fieldAccess.getBaseExpression().getFinishPosition());
    }

    public void testTrailingPrimary1() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  private A   firstAttribute;\n"+
                "  private A   secondAttribute;\n"+
                "  private int thirdAttribute;\n"+
                "  {\n"+
                "    firstAttribute.secondAttribute.thirdAttribute = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        // default settings are :
        // * break before dot
        // * don't break every dot if too long
        // * indent level for dot break is 0
        _prettyPrinter.getOptions().setMaxLineLength(15);
        _prettyPrinter.incIndent(1);
        _prettyPrinter.visitFieldAccess(fieldAccess);

        assertEquals("    firstAttribute\n"+
                     "    .secondAttribute\n"+
                     "    .thirdAttribute",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(3, 19, 58),
                     fieldAccess.getFinishPosition());

        fieldAccess = (FieldAccess)fieldAccess.getBaseExpression();

        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(2, 20, 38),
                     fieldAccess.getFinishPosition());

        fieldAccess = (FieldAccess)fieldAccess.getBaseExpression();

        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(1, 18, 17),
                     fieldAccess.getFinishPosition());
    }

    public void testTrailingPrimary10() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  private A     firstAttribute;\n"+
                "  private A[]   secondAttribute;\n"+
                "  private int   thirdAttribute;\n"+
                "  {\n"+
                "    secondAttribute[firstAttribute.thirdAttribute].thirdAttribute = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        _prettyPrinter.getOptions().setMaxLineLength(35);
        _prettyPrinter.getOptions().doBreakBeforeDot(false);
        _prettyPrinter.getOptions().doBreakEveryDotIfTooLong(true);
        _prettyPrinter.getOptions().setDotBreakIndentationLevel(1);
        _prettyPrinter.getOptions().setArrExprIndentationLevel(1);
        _prettyPrinter.incIndent(1);
        _prettyPrinter.visitFieldAccess(fieldAccess);

        assertEquals("    secondAttribute\n"+
                     "        [firstAttribute.thirdAttribute].\n"+
                     "        thirdAttribute",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(3, 22, 82),
                     fieldAccess.getFinishPosition());

        ArrayAccess arrayAccess = (ArrayAccess)fieldAccess.getBaseExpression();

        assertEquals(new ParsePosition(1, 5, 4),
                     arrayAccess.getStartPosition());
        assertEquals(new ParsePosition(2, 39, 58),
                     arrayAccess.getFinishPosition());

        fieldAccess = (FieldAccess)arrayAccess.getIndexExpression();

        assertEquals(new ParsePosition(2, 10, 29),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(2, 38, 57),
                     fieldAccess.getFinishPosition());

        fieldAccess = (FieldAccess)fieldAccess.getBaseExpression();

        assertEquals(new ParsePosition(2, 10, 29),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(2, 23, 42),
                     fieldAccess.getFinishPosition());

        fieldAccess = (FieldAccess)arrayAccess.getBaseExpression();

        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(1, 19, 18),
                     fieldAccess.getFinishPosition());
    }

    public void testTrailingPrimary2() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  private A   firstAttribute;\n"+
                "  private A   secondAttribute;\n"+
                "  private int thirdAttribute;\n"+
                "  {\n"+
                "    firstAttribute.secondAttribute.thirdAttribute = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        _prettyPrinter.getOptions().setMaxLineLength(40);
        _prettyPrinter.getOptions().setDotBreakIndentationLevel(1);
        _prettyPrinter.incIndent(1);
        _prettyPrinter.visitFieldAccess(fieldAccess);

        assertEquals("    firstAttribute.secondAttribute\n"+
                     "        .thirdAttribute",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(2, 23, 57),
                     fieldAccess.getFinishPosition());

        fieldAccess = (FieldAccess)fieldAccess.getBaseExpression();

        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(1, 34, 33),
                     fieldAccess.getFinishPosition());

        fieldAccess = (FieldAccess)fieldAccess.getBaseExpression();

        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(1, 18, 17),
                     fieldAccess.getFinishPosition());
    }

    public void testTrailingPrimary3() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  private A   firstAttribute;\n"+
                "  private A   secondAttribute;\n"+
                "  private int thirdAttribute;\n"+
                "  {\n"+
                "    firstAttribute.secondAttribute.thirdAttribute = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        _prettyPrinter.getOptions().setMaxLineLength(40);
        _prettyPrinter.getOptions().doBreakBeforeDot(false);
        _prettyPrinter.incIndent(1);
        _prettyPrinter.visitFieldAccess(fieldAccess);

        assertEquals("    firstAttribute.secondAttribute.\n"+
                     "    thirdAttribute",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(2, 18, 53),
                     fieldAccess.getFinishPosition());

        fieldAccess = (FieldAccess)fieldAccess.getBaseExpression();

        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(1, 34, 33),
                     fieldAccess.getFinishPosition());

        fieldAccess = (FieldAccess)fieldAccess.getBaseExpression();

        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(1, 18, 17),
                     fieldAccess.getFinishPosition());
    }

    public void testTrailingPrimary4() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  private A   firstAttribute;\n"+
                "  private A   secondAttribute;\n"+
                "  private int thirdAttribute;\n"+
                "  {\n"+
                "    firstAttribute.secondAttribute.thirdAttribute = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        _prettyPrinter.getOptions().setMaxLineLength(40);
        _prettyPrinter.getOptions().doBreakEveryDotIfTooLong(true);
        _prettyPrinter.incIndent(1);
        _prettyPrinter.visitFieldAccess(fieldAccess);

        assertEquals("    firstAttribute\n"+
                     "    .secondAttribute\n"+
                     "    .thirdAttribute",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(3, 19, 58),
                     fieldAccess.getFinishPosition());

        fieldAccess = (FieldAccess)fieldAccess.getBaseExpression();

        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(2, 20, 38),
                     fieldAccess.getFinishPosition());

        fieldAccess = (FieldAccess)fieldAccess.getBaseExpression();

        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(1, 18, 17),
                     fieldAccess.getFinishPosition());
    }

    public void testTrailingPrimary5() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  private A firstAttribute;\n"+
                "  private A secondAttribute;\n"+
                "  {\n"+
                "    firstAttribute.getValue().secondAttribute = 0;\n"+
                "  }\n"+
                "  private A getValue() { return null; }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        _prettyPrinter.getOptions().setMaxLineLength(30);
        _prettyPrinter.getOptions().doBreakBeforeDot(false);
        _prettyPrinter.getOptions().doBreakEveryDotIfTooLong(true);
        _prettyPrinter.getOptions().setDotBreakIndentationLevel(2);
        _prettyPrinter.incIndent(1);
        _prettyPrinter.visitFieldAccess(fieldAccess);

        assertEquals("    firstAttribute.\n"+
                     "            getValue().\n"+
                     "            secondAttribute",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(3, 27, 70),
                     fieldAccess.getFinishPosition());

        MethodInvocation methodInvoc = (MethodInvocation)fieldAccess.getBaseExpression();

        assertEquals(new ParsePosition(1, 5, 4),
                     methodInvoc.getStartPosition());
        assertEquals(new ParsePosition(2, 22, 41),
                     methodInvoc.getFinishPosition());

        fieldAccess = (FieldAccess)methodInvoc.getBaseExpression();

        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(1, 18, 17),
                     fieldAccess.getFinishPosition());
    }

    public void testTrailingPrimary6() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  private A   firstAttribute;\n"+
                "  private A   secondAttribute;\n"+
                "  private int thirdAttribute;\n"+
                "  {\n"+
                "    firstAttribute/*bla*/.secondAttribute/*bla*/.thirdAttribute = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        _prettyPrinter.visitFieldAccess(fieldAccess);

        assertEquals("firstAttribute /* bla */ .secondAttribute /* bla */ .thirdAttribute",
                     getText());
        assertEquals(new ParsePosition(1, 1, 0),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(1, 67, 66),
                     fieldAccess.getFinishPosition());

        fieldAccess = (FieldAccess)fieldAccess.getBaseExpression();

        assertEquals(new ParsePosition(1, 1, 0),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(1, 41, 40),
                     fieldAccess.getFinishPosition());

        fieldAccess = (FieldAccess)fieldAccess.getBaseExpression();

        assertEquals(new ParsePosition(1, 1, 0),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(1, 14, 13),
                     fieldAccess.getFinishPosition());
    }

    public void testTrailingPrimary7() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  private A   firstAttribute;\n"+
                "  private A   secondAttribute;\n"+
                "  private int thirdAttribute;\n"+
                "  {\n"+
                "    firstAttribute/*bla*/.secondAttribute/*bla*/.thirdAttribute = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        _prettyPrinter.getOptions().setMaxLineLength(25);
        _prettyPrinter.getOptions().doBreakEveryDotIfTooLong(true);
        _prettyPrinter.getOptions().setDotBreakIndentationLevel(1);
        _prettyPrinter.incIndent(1);
        _prettyPrinter.visitFieldAccess(fieldAccess);

        // the trailing spaces come from the comments
        assertEquals("    firstAttribute /* bla */ \n"+
                     "        .secondAttribute /* bla */ \n"+
                     "        .thirdAttribute",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(3, 23, 88),
                     fieldAccess.getFinishPosition());

        fieldAccess = (FieldAccess)fieldAccess.getBaseExpression();

        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(2, 24, 53),
                     fieldAccess.getFinishPosition());

        fieldAccess = (FieldAccess)fieldAccess.getBaseExpression();

        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(1, 18, 17),
                     fieldAccess.getFinishPosition());
    }

    public void testTrailingPrimary8() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  static class Inner {\n"+
                "    private static int ATTRIBUTE;\n"+
                "  }\n"+
                "  private A attribute;\n"+
                "  {\n"+
                "    attribute.Inner.ATTRIBUTE = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        _prettyPrinter.incIndent(2);
        _prettyPrinter.getOptions().setMaxLineLength(25);
        _prettyPrinter.getOptions().doBreakEveryDotIfTooLong(false);
        _prettyPrinter.getOptions().setDotBreakIndentationLevel(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.visitFieldAccess(fieldAccess);

        assertEquals("        attribute.Inner\n"+
                     "            .ATTRIBUTE",
                     getText());
        assertEquals(new ParsePosition(1, 9, 8),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(2, 22, 45),
                     fieldAccess.getFinishPosition());

        TypeAccess typeAccess = (TypeAccess)fieldAccess.getBaseExpression();

        assertEquals(new ParsePosition(1, 9, 8),
                     typeAccess.getStartPosition());
        assertEquals(new ParsePosition(1, 23, 22),
                     typeAccess.getFinishPosition());

        fieldAccess = (FieldAccess)typeAccess.getBaseExpression();

        assertEquals(new ParsePosition(1, 9, 8),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(1, 17, 16),
                     fieldAccess.getFinishPosition());
    }

    public void testTrailingPrimary9() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  private A   firstAttribute;\n"+
                "  private A   secondAttribute;\n"+
                "  private int thirdAttribute;\n"+
                "  {\n"+
                "    firstAttribute//bla\n"+
                "    .secondAttribute/*bla*/.thirdAttribute = 0;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        _prettyPrinter.getOptions().setEolCommentColumn(20);
        _prettyPrinter.getOptions().setMaxLineLength(30);
        _prettyPrinter.getOptions().doBreakEveryDotIfTooLong(true);
        _prettyPrinter.getOptions().setDotBreakIndentationLevel(1);
        _prettyPrinter.incIndent(1);
        _prettyPrinter.visitFieldAccess(fieldAccess);

        // the trailing whitespace comes from the comment
        assertEquals("    firstAttribute // bla\n"+
                     "        .secondAttribute /* bla */ \n"+
                     "        .thirdAttribute",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(3, 23, 84),
                     fieldAccess.getFinishPosition());

        fieldAccess = (FieldAccess)fieldAccess.getBaseExpression();

        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(2, 24, 49),
                     fieldAccess.getFinishPosition());

        fieldAccess = (FieldAccess)fieldAccess.getBaseExpression();

        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(1, 18, 17),
                     fieldAccess.getFinishPosition());
    }

    public void testTypeAccess1() throws ParseException
    {
        addType("test.B",
                "package test;\n"+
                "class B {\n"+
                "  public static final int ATTR = 0;\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private int attr;\n"+
                "  {\n"+
                "    attr = test.B.ATTR;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("test.A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(0);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        FieldAccess          fieldAccess = (FieldAccess)assignExpr.getValueExpression();

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(classDecl);
        _prettyPrinter.visitFieldAccess(fieldAccess);

        assertEquals("    B.ATTR",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getStartPosition());
        assertEquals(new ParsePosition(1, 10, 9),
                     fieldAccess.getFinishPosition());
        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getBaseExpression().getStartPosition());
        assertEquals(new ParsePosition(1, 5, 4),
                     fieldAccess.getBaseExpression().getFinishPosition());
    }

    public void testVariableAccess() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  {\n"+
                "    Object var;\n"+
                "    var = null;\n"+
                "  }\n"+
                "}\n",
                true);

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        VariableAccess       varAccess   = (VariableAccess)assignExpr.getLeftHandSide();

        _prettyPrinter.visitVariableAccess(varAccess);

        assertEquals("var",
                     getText());
        assertEquals(new ParsePosition(1, 1, 0),
                     varAccess.getStartPosition());
        assertEquals(new ParsePosition(1, 3, 2),
                     varAccess.getFinishPosition());
    }

    public void testVariableAccess_Comment() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  {\n"+
                "    Object var;\n"+
                "    var/* bla */ = null;\n"+
                "  }\n"+
                "}\n",
                true);

        ClassDeclaration     classDecl   = (ClassDeclaration)_project.getType("A");
        Initializer          initializer = classDecl.getInitializers().get(0);
        ExpressionStatement  exprStmt    = (ExpressionStatement)initializer.getBody().getBlockStatements().get(1);
        AssignmentExpression assignExpr  = (AssignmentExpression)exprStmt.getExpression();
        VariableAccess       varAccess   = (VariableAccess)assignExpr.getLeftHandSide();

        _prettyPrinter.incIndent(1);
        // we set the eol column somewhat nearer
        _prettyPrinter.getOptions().setEolCommentColumn(20);
        _prettyPrinter.visitVariableAccess(varAccess);

        // the trailing whitespace comes from the comment
        assertEquals("    var /* bla */ ",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     varAccess.getStartPosition());
        assertEquals(new ParsePosition(1, 7, 6),
                     varAccess.getFinishPosition());
        assertEquals(new ParsePosition(1, 9, 8),
                     varAccess.getComments().get(0).getStartPosition());
        assertEquals(new ParsePosition(1, 17, 16),
                     varAccess.getComments().get(0).getFinishPosition());
    }
}
