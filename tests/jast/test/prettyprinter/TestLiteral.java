package jast.test.prettyprinter;
import jast.ParseException;
import jast.ast.ParsePosition;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.BooleanLiteral;
import jast.ast.nodes.CharacterLiteral;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.FloatingPointLiteral;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.NullLiteral;
import jast.ast.nodes.SingleInitializer;
import jast.ast.nodes.StringLiteral;
import jast.ast.nodes.TypeDeclaration;
import jast.prettyprinter.Options;

public class TestLiteral extends TestPrettyPrinterBase
{

    public TestLiteral(String name)
    {
        super(name);
    }

    public void testBooleanLiteral_Comment1() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  boolean attr = true // bla\n"+
                "                 && false;\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        BinaryExpression  binExpr     = (BinaryExpression)initializer.getInitEpression();
        BooleanLiteral    literal     = (BooleanLiteral)binExpr.getLeftOperand();

        _prettyPrinter.incIndent(1);
        // we set the eol column somewhat nearer
        _prettyPrinter.getOptions().setEolCommentColumn(20);
        _prettyPrinter.visitBooleanLiteral(literal);

        assertEquals("    true           // bla\n",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     literal.getStartPosition());
        assertEquals(new ParsePosition(1, 8, 7),
                     literal.getFinishPosition());
        assertEquals(new ParsePosition(1, 20, 19),
                     literal.getComments().get(0).getStartPosition());
        assertEquals(new ParsePosition(1, 25, 24),
                     literal.getComments().get(0).getFinishPosition());
    }

    public void testBooleanLiteral_Comment2() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  boolean attr = true /* bla*/\n"+
                "                 && false;\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        BinaryExpression  binExpr     = (BinaryExpression)initializer.getInitEpression();
        BooleanLiteral    literal     = (BooleanLiteral)binExpr.getLeftOperand();

        _prettyPrinter.incIndent(1);
        // we set the eol column somewhat nearer
        _prettyPrinter.getOptions().setEolCommentColumn(20);
        _prettyPrinter.visitBooleanLiteral(literal);

        // the trailing space comes from the comment
        assertEquals("    true /* bla */ ",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     literal.getStartPosition());
        assertEquals(new ParsePosition(1, 8, 7),
                     literal.getFinishPosition());
        assertEquals(new ParsePosition(1, 10, 9),
                     literal.getComments().get(0).getStartPosition());
        assertEquals(new ParsePosition(1, 18, 17),
                     literal.getComments().get(0).getFinishPosition());
    }

    public void testBooleanLiteral_Comment3() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  boolean attr = true/*bla*///bla\n"+
                "                 && false;\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        BinaryExpression  binExpr     = (BinaryExpression)initializer.getInitEpression();
        BooleanLiteral    literal     = (BooleanLiteral)binExpr.getLeftOperand();

        _prettyPrinter.incIndent(1);
        // we set the eol column somewhat nearer
        _prettyPrinter.getOptions().setEolCommentColumn(20);
        _prettyPrinter.visitBooleanLiteral(literal);

        assertEquals("    true /* bla */ // bla\n",
                     getText());
        assertEquals(new ParsePosition(1, 5, 4),
                     literal.getStartPosition());
        assertEquals(new ParsePosition(1, 8, 7),
                     literal.getFinishPosition());
        assertEquals(new ParsePosition(1, 10, 9),
                     literal.getComments().get(0).getStartPosition());
        assertEquals(new ParsePosition(1, 18, 17),
                     literal.getComments().get(0).getFinishPosition());
        assertEquals(new ParsePosition(1, 20, 19),
                     literal.getComments().get(1).getStartPosition());
        assertEquals(new ParsePosition(1, 25, 24),
                     literal.getComments().get(1).getFinishPosition());
    }

    public void testBooleanLiteral1() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  boolean attr = true;\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        BooleanLiteral    literal     = (BooleanLiteral)initializer.getInitEpression();

        _prettyPrinter.visitBooleanLiteral(literal);

        assertEquals("true",
                     getText());
    }

    public void testBooleanLiteral2() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  boolean attr = false;\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        BooleanLiteral    literal     = (BooleanLiteral)initializer.getInitEpression();

        _prettyPrinter.visitBooleanLiteral(literal);

        assertEquals("false",
                     getText());
    }

    public void testCharacterLiteral_Comment1() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  int attr = 'a'// bla\n"+
                "             + 1;\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        BinaryExpression  binExpr     = (BinaryExpression)initializer.getInitEpression();
        CharacterLiteral  literal     = (CharacterLiteral)binExpr.getLeftOperand();

        // we set the eol column somewhat nearer
        _prettyPrinter.getOptions().setEolCommentColumn(10);
        _prettyPrinter.visitCharacterLiteral(literal);

        assertEquals("'a'      // bla\n",
                     getText());
        assertEquals(new ParsePosition(1, 1, 0),
                     literal.getStartPosition());
        assertEquals(new ParsePosition(1, 3, 2),
                     literal.getFinishPosition());
        assertEquals(new ParsePosition(1, 10, 9),
                     literal.getComments().get(0).getStartPosition());
        assertEquals(new ParsePosition(1, 15, 14),
                     literal.getComments().get(0).getFinishPosition());
    }

    public void testCharacterLiteral1() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  char attr = 'a';\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        CharacterLiteral  literal     = (CharacterLiteral)initializer.getInitEpression();

        _prettyPrinter.visitCharacterLiteral(literal);

        assertEquals("'a'",
                     getText());
    }

    public void testCharacterLiteral2() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  char attr = '\\uFFFF';\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        CharacterLiteral  literal     = (CharacterLiteral)initializer.getInitEpression();

        _prettyPrinter.visitCharacterLiteral(literal);

        assertEquals("'\\uFFFF'",
                     getText());
    }

    public void testCharacterLiteral3() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  char attr = '\u0123';\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        CharacterLiteral  literal     = (CharacterLiteral)initializer.getInitEpression();

        _prettyPrinter.visitCharacterLiteral(literal);

        assertEquals("'\u0123'",
                     getText());
    }

    public void testCharacterLiteral4() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  char attr = '\\uuu0123';\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        CharacterLiteral  literal     = (CharacterLiteral)initializer.getInitEpression();

        _prettyPrinter.visitCharacterLiteral(literal);

        assertEquals("'\\u0123'",
                     getText());
    }

    public void testCharacterLiteral5() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  char attr = '\\u0123';\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        CharacterLiteral  literal     = (CharacterLiteral)initializer.getInitEpression();

        _prettyPrinter.getOptions().doLitSlimUnicode(false);
        _prettyPrinter.visitCharacterLiteral(literal);

        assertEquals("'\\u0123'",
                     getText());
    }

    public void testCharacterLiteral6() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  char attr = '\\uuu0123';\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        CharacterLiteral  literal     = (CharacterLiteral)initializer.getInitEpression();

        _prettyPrinter.getOptions().doLitSlimUnicode(false);
        _prettyPrinter.visitCharacterLiteral(literal);

        assertEquals("'\\uuu0123'",
                     getText());
    }

    public void testFloatLiteral_Case1() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  float attr = 1.0F;\n"+
                "}\n",
                true);

        TypeDeclaration      typeDecl    = _project.getType("A");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer    initializer = (SingleInitializer)fieldDecl.getInitializer();
        FloatingPointLiteral literal     = (FloatingPointLiteral)initializer.getInitEpression();

        _prettyPrinter.visitFloatingPointLiteral(literal);

        // default is: force lowercase
        assertEquals("1.0f",
                     getText());
    }

    public void testFloatLiteral_Case2() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  double attr = 1.0d;\n"+
                "}\n",
                true);

        TypeDeclaration      typeDecl    = _project.getType("A");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer    initializer = (SingleInitializer)fieldDecl.getInitializer();
        FloatingPointLiteral literal     = (FloatingPointLiteral)initializer.getInitEpression();

        _prettyPrinter.getOptions().setNumLitCaseMode(Options.NUMLIT_FORCE_UPPERCASE);
        _prettyPrinter.visitFloatingPointLiteral(literal);

        assertEquals("1.0D",
                     getText());
    }

    public void testFloatLiteral_Case3() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  float attr = 1.0f;\n"+
                "}\n",
                true);

        TypeDeclaration      typeDecl    = _project.getType("A");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer    initializer = (SingleInitializer)fieldDecl.getInitializer();
        FloatingPointLiteral literal     = (FloatingPointLiteral)initializer.getInitEpression();

        _prettyPrinter.getOptions().setNumLitCaseMode(Options.NUMLIT_NOCHANGE);
        _prettyPrinter.visitFloatingPointLiteral(literal);

        assertEquals("1.0f",
                     getText());
    }

    public void testFloatLiteral_Case4() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  double attr = 1.0D;\n"+
                "}\n",
                true);

        TypeDeclaration      typeDecl    = _project.getType("A");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer    initializer = (SingleInitializer)fieldDecl.getInitializer();
        FloatingPointLiteral literal     = (FloatingPointLiteral)initializer.getInitEpression();

        _prettyPrinter.getOptions().setNumLitCaseMode(Options.NUMLIT_NOCHANGE);
        _prettyPrinter.visitFloatingPointLiteral(literal);

        assertEquals("1.0D",
                     getText());
    }

    public void testFloatLiteral_Case5() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  double attr = 1.0E3;\n"+
                "}\n",
                true);

        TypeDeclaration      typeDecl    = _project.getType("A");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer    initializer = (SingleInitializer)fieldDecl.getInitializer();
        FloatingPointLiteral literal     = (FloatingPointLiteral)initializer.getInitEpression();

        _prettyPrinter.visitFloatingPointLiteral(literal);

        // default is: force lowercase, don't force suffix
        assertEquals("1.0e3",
                     getText());
    }

    public void testFloatLiteral_Case6() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  double attr = 1.0e3;\n"+
                "}\n",
                true);

        TypeDeclaration      typeDecl    = _project.getType("A");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer    initializer = (SingleInitializer)fieldDecl.getInitializer();
        FloatingPointLiteral literal     = (FloatingPointLiteral)initializer.getInitEpression();

        _prettyPrinter.getOptions().setNumLitCaseMode(Options.NUMLIT_FORCE_UPPERCASE);
        _prettyPrinter.visitFloatingPointLiteral(literal);

        assertEquals("1.0E3",
                     getText());
    }

    public void testFloatLiteral_Case7() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  double attr = 1.0E3;\n"+
                "}\n",
                true);

        TypeDeclaration      typeDecl    = _project.getType("A");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer    initializer = (SingleInitializer)fieldDecl.getInitializer();
        FloatingPointLiteral literal     = (FloatingPointLiteral)initializer.getInitEpression();

        _prettyPrinter.getOptions().setNumLitCaseMode(Options.NUMLIT_NOCHANGE);
        _prettyPrinter.visitFloatingPointLiteral(literal);

        assertEquals("1.0E3",
                     getText());
    }

    public void testFloatLiteral_Case8() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  double attr = 1.0e3;\n"+
                "}\n",
                true);

        TypeDeclaration      typeDecl    = _project.getType("A");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer    initializer = (SingleInitializer)fieldDecl.getInitializer();
        FloatingPointLiteral literal     = (FloatingPointLiteral)initializer.getInitEpression();

        _prettyPrinter.getOptions().setNumLitCaseMode(Options.NUMLIT_NOCHANGE);
        _prettyPrinter.visitFloatingPointLiteral(literal);

        assertEquals("1.0e3",
                     getText());
    }

    public void testFloatLiteral_Comment1() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  double attr = 1.0e10// bla\n"+
                "                + 1;\n"+
                "}\n",
                true);

        TypeDeclaration      typeDecl    = _project.getType("A");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer    initializer = (SingleInitializer)fieldDecl.getInitializer();
        BinaryExpression     binExpr     = (BinaryExpression)initializer.getInitEpression();
        FloatingPointLiteral literal     = (FloatingPointLiteral)binExpr.getLeftOperand();

        // we set the eol column somewhat nearer
        _prettyPrinter.getOptions().setEolCommentColumn(10);
        _prettyPrinter.visitFloatingPointLiteral(literal);

        assertEquals("1.0e10   // bla\n",
                     getText());
        assertEquals(new ParsePosition(1, 1, 0),
                     literal.getStartPosition());
        assertEquals(new ParsePosition(1, 6, 5),
                     literal.getFinishPosition());
        assertEquals(new ParsePosition(1, 10, 9),
                     literal.getComments().get(0).getStartPosition());
        assertEquals(new ParsePosition(1, 15, 14),
                     literal.getComments().get(0).getFinishPosition());
    }

    public void testFloatLiteral_ForceDot1() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  float attr = 1.0f;\n"+
                "}\n",
                true);

        TypeDeclaration      typeDecl    = _project.getType("A");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer    initializer = (SingleInitializer)fieldDecl.getInitializer();
        FloatingPointLiteral literal     = (FloatingPointLiteral)initializer.getInitEpression();

        _prettyPrinter.visitFloatingPointLiteral(literal);

        // default is: force dot
        assertEquals("1.0f",
                     getText());
    }

    public void testFloatLiteral_ForceDot2() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  double attr = 1d;\n"+
                "}\n",
                true);

        TypeDeclaration      typeDecl    = _project.getType("A");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer    initializer = (SingleInitializer)fieldDecl.getInitializer();
        FloatingPointLiteral literal     = (FloatingPointLiteral)initializer.getInitEpression();

        _prettyPrinter.visitFloatingPointLiteral(literal);

        // default is: force dot
        assertEquals("1.0d",
                     getText());
    }

    public void testFloatLiteral_ForceDot3() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  double attr = 1.0e10;\n"+
                "}\n",
                true);

        TypeDeclaration      typeDecl    = _project.getType("A");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer    initializer = (SingleInitializer)fieldDecl.getInitializer();
        FloatingPointLiteral literal     = (FloatingPointLiteral)initializer.getInitEpression();

        _prettyPrinter.visitFloatingPointLiteral(literal);

        // default is: force dot
        assertEquals("1.0e10",
                     getText());
    }

    public void testFloatLiteral_ForceDot4() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  double attr = 1e10;\n"+
                "}\n",
                true);

        TypeDeclaration      typeDecl    = _project.getType("A");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer    initializer = (SingleInitializer)fieldDecl.getInitializer();
        FloatingPointLiteral literal     = (FloatingPointLiteral)initializer.getInitEpression();

        _prettyPrinter.visitFloatingPointLiteral(literal);

        // default is: force dot
        assertEquals("1.0e10",
                     getText());
    }

    public void testFloatLiteral_ForceDot5() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  float attr = 1.0f;\n"+
                "}\n",
                true);

        TypeDeclaration      typeDecl    = _project.getType("A");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer    initializer = (SingleInitializer)fieldDecl.getInitializer();
        FloatingPointLiteral literal     = (FloatingPointLiteral)initializer.getInitEpression();

        _prettyPrinter.getOptions().doFloatLitForceDot(false);
        _prettyPrinter.visitFloatingPointLiteral(literal);

        assertEquals("1.0f",
                     getText());
    }

    public void testFloatLiteral_ForceDot6() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  double attr = 1d;\n"+
                "}\n",
                true);

        TypeDeclaration      typeDecl    = _project.getType("A");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer    initializer = (SingleInitializer)fieldDecl.getInitializer();
        FloatingPointLiteral literal     = (FloatingPointLiteral)initializer.getInitEpression();

        _prettyPrinter.getOptions().doFloatLitForceDot(false);
        _prettyPrinter.visitFloatingPointLiteral(literal);

        assertEquals("1d",
                     getText());
    }

    public void testFloatLiteral_ForceDot7() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  float attr = 1.0e10f;\n"+
                "}\n",
                true);

        TypeDeclaration      typeDecl    = _project.getType("A");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer    initializer = (SingleInitializer)fieldDecl.getInitializer();
        FloatingPointLiteral literal     = (FloatingPointLiteral)initializer.getInitEpression();

        _prettyPrinter.getOptions().doFloatLitForceDot(false);
        _prettyPrinter.visitFloatingPointLiteral(literal);

        assertEquals("1.0e10f",
                     getText());
    }

    public void testFloatLiteral_ForceDot8() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  float attr = 1e10f;\n"+
                "}\n",
                true);

        TypeDeclaration      typeDecl    = _project.getType("A");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer    initializer = (SingleInitializer)fieldDecl.getInitializer();
        FloatingPointLiteral literal     = (FloatingPointLiteral)initializer.getInitEpression();

        _prettyPrinter.getOptions().doFloatLitForceDot(false);
        _prettyPrinter.visitFloatingPointLiteral(literal);

        assertEquals("1e10f",
                     getText());
    }

    public void testFloatLiteral_ForceSuffix1() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  double attr = 1.0;\n"+
                "}\n",
                true);

        TypeDeclaration      typeDecl    = _project.getType("A");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer    initializer = (SingleInitializer)fieldDecl.getInitializer();
        FloatingPointLiteral literal     = (FloatingPointLiteral)initializer.getInitEpression();

        _prettyPrinter.visitFloatingPointLiteral(literal);

        // default is: don't force suffix
        assertEquals("1.0",
                     getText());
    }

    public void testFloatLiteral_ForceSuffix2() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  double attr = 1.0e3d;\n"+
                "}\n",
                true);

        TypeDeclaration      typeDecl    = _project.getType("A");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer    initializer = (SingleInitializer)fieldDecl.getInitializer();
        FloatingPointLiteral literal     = (FloatingPointLiteral)initializer.getInitEpression();

        _prettyPrinter.visitFloatingPointLiteral(literal);

        // default is: don't force suffix
        assertEquals("1.0e3d",
                     getText());
    }

    public void testFloatLiteral_ForceSuffix3() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  double attr = 1.0e3;\n"+
                "}\n",
                true);

        TypeDeclaration      typeDecl    = _project.getType("A");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer    initializer = (SingleInitializer)fieldDecl.getInitializer();
        FloatingPointLiteral literal     = (FloatingPointLiteral)initializer.getInitEpression();

        _prettyPrinter.getOptions().doFloatLitForceSuffix(true);
        _prettyPrinter.visitFloatingPointLiteral(literal);

        assertEquals("1.0e3d",
                     getText());
    }

    public void testFloatLiteral_ForceSuffix4() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  double attr = 1.0d;\n"+
                "}\n",
                true);

        TypeDeclaration      typeDecl    = _project.getType("A");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer    initializer = (SingleInitializer)fieldDecl.getInitializer();
        FloatingPointLiteral literal     = (FloatingPointLiteral)initializer.getInitEpression();

        _prettyPrinter.getOptions().doFloatLitForceSuffix(true);
        _prettyPrinter.visitFloatingPointLiteral(literal);

        assertEquals("1.0d",
                     getText());
    }

    public void testFloatLiteral_Indent() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  float attr = 1.0f;\n"+
                "}\n",
                true);

        TypeDeclaration      typeDecl    = _project.getType("A");
        FieldDeclaration     fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer    initializer = (SingleInitializer)fieldDecl.getInitializer();
        FloatingPointLiteral literal     = (FloatingPointLiteral)initializer.getInitEpression();

        _prettyPrinter.incIndent(2);
        _prettyPrinter.visitFloatingPointLiteral(literal);

        assertEquals("        1.0f",
                     getText());
    }

    public void testIntegerLiteral_Case1() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  long attr = 1L;\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        IntegerLiteral    literal     = (IntegerLiteral)initializer.getInitEpression();

        _prettyPrinter.visitIntegerLiteral(literal);

        // default is: force lowercase
        assertEquals("1l",
                     getText());
    }

    public void testIntegerLiteral_Case2() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  long attr = 1l;\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        IntegerLiteral    literal     = (IntegerLiteral)initializer.getInitEpression();

        _prettyPrinter.visitIntegerLiteral(literal);

        // default is: force lowercase
        assertEquals("1l",
                     getText());
    }

    public void testIntegerLiteral_Case3() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  int attr = 0XaBfE;\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        IntegerLiteral    literal     = (IntegerLiteral)initializer.getInitEpression();

        _prettyPrinter.visitIntegerLiteral(literal);

        // default is: force lowercase
        assertEquals("0xabfe",
                     getText());
    }

    public void testIntegerLiteral_Case4() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  long attr = 1l;\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        IntegerLiteral    literal     = (IntegerLiteral)initializer.getInitEpression();

        _prettyPrinter.getOptions().setNumLitCaseMode(Options.NUMLIT_FORCE_UPPERCASE);
        _prettyPrinter.visitIntegerLiteral(literal);

        assertEquals("1L",
                     getText());
    }

    public void testIntegerLiteral_Case5() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  long attr = 1L;\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        IntegerLiteral    literal     = (IntegerLiteral)initializer.getInitEpression();

        _prettyPrinter.getOptions().setNumLitCaseMode(Options.NUMLIT_FORCE_UPPERCASE);
        _prettyPrinter.visitIntegerLiteral(literal);

        assertEquals("1L",
                     getText());
    }

    public void testIntegerLiteral_Case6() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  int attr = 0XaBfE;\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        IntegerLiteral    literal     = (IntegerLiteral)initializer.getInitEpression();

        _prettyPrinter.getOptions().setNumLitCaseMode(Options.NUMLIT_FORCE_UPPERCASE);
        _prettyPrinter.visitIntegerLiteral(literal);

        // default is: force lowercase
        assertEquals("0XABFE",
                     getText());
    }

    public void testIntegerLiteral_Case7() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  long attr = 1l;\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        IntegerLiteral    literal     = (IntegerLiteral)initializer.getInitEpression();

        _prettyPrinter.getOptions().setNumLitCaseMode(Options.NUMLIT_NOCHANGE);
        _prettyPrinter.visitIntegerLiteral(literal);

        assertEquals("1l",
                     getText());
    }

    public void testIntegerLiteral_Case8() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  long attr = 1L;\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        IntegerLiteral    literal     = (IntegerLiteral)initializer.getInitEpression();

        _prettyPrinter.getOptions().setNumLitCaseMode(Options.NUMLIT_NOCHANGE);
        _prettyPrinter.visitIntegerLiteral(literal);

        assertEquals("1L",
                     getText());
    }

    public void testIntegerLiteral_Case9() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  long attr = 0XaBFe;\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        IntegerLiteral    literal     = (IntegerLiteral)initializer.getInitEpression();

        _prettyPrinter.getOptions().setNumLitCaseMode(Options.NUMLIT_NOCHANGE);
        _prettyPrinter.visitIntegerLiteral(literal);

        assertEquals("0XaBFe",
                     getText());
    }

    public void testIntegerLiteral_Comment1() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  long attr = 10// bla\n"+
                "              + 1;\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        BinaryExpression  binExpr     = (BinaryExpression)initializer.getInitEpression();
        IntegerLiteral    literal     = (IntegerLiteral)binExpr.getLeftOperand();

        // we set the eol column somewhat nearer
        _prettyPrinter.getOptions().setEolCommentColumn(10);
        _prettyPrinter.visitIntegerLiteral(literal);

        assertEquals("10       // bla\n",
                     getText());
        assertEquals(new ParsePosition(1, 1, 0),
                     literal.getStartPosition());
        assertEquals(new ParsePosition(1, 2, 1),
                     literal.getFinishPosition());
        assertEquals(new ParsePosition(1, 10, 9),
                     literal.getComments().get(0).getStartPosition());
        assertEquals(new ParsePosition(1, 15, 14),
                     literal.getComments().get(0).getFinishPosition());
    }

    public void testNullLiteral() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  Object attr = null;\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        NullLiteral       literal     = (NullLiteral)initializer.getInitEpression();

        _prettyPrinter.visitNullLiteral(literal);

        assertEquals("null",
                     getText());
    }

    public void testNullLiteral_Comment1() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  String attr = null // bla\n"+
                "                + \"\";\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        BinaryExpression  binExpr     = (BinaryExpression)initializer.getInitEpression();
        NullLiteral       literal     = (NullLiteral)binExpr.getLeftOperand();

        // we set the eol column somewhat nearer
        _prettyPrinter.getOptions().setEolCommentColumn(10);
        _prettyPrinter.visitNullLiteral(literal);

        assertEquals("null     // bla\n",
                     getText());
        assertEquals(new ParsePosition(1, 1, 0),
                     literal.getStartPosition());
        assertEquals(new ParsePosition(1, 4, 3),
                     literal.getFinishPosition());
        assertEquals(new ParsePosition(1, 10, 9),
                     literal.getComments().get(0).getStartPosition());
        assertEquals(new ParsePosition(1, 15, 14),
                     literal.getComments().get(0).getFinishPosition());
    }

    public void testStringLiteral_Comment1() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  String attr = \"bla\"// bla\n"+
                "                + \"bla\";\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        BinaryExpression  binExpr     = (BinaryExpression)initializer.getInitEpression();
        StringLiteral     literal     = (StringLiteral)binExpr.getLeftOperand();

        // we set the eol column somewhat nearer
        _prettyPrinter.getOptions().setEolCommentColumn(10);
        _prettyPrinter.visitStringLiteral(literal);

        assertEquals("\"bla\"    // bla\n",
                     getText());
        assertEquals(new ParsePosition(1, 1, 0),
                     literal.getStartPosition());
        assertEquals(new ParsePosition(1, 5, 4),
                     literal.getFinishPosition());
        assertEquals(new ParsePosition(1, 10, 9),
                     literal.getComments().get(0).getStartPosition());
        assertEquals(new ParsePosition(1, 15, 14),
                     literal.getComments().get(0).getFinishPosition());
    }

    public void testStringLiteral1() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  String attr = \"abc\";\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        StringLiteral     literal     = (StringLiteral)initializer.getInitEpression();

        _prettyPrinter.visitStringLiteral(literal);

        assertEquals("\"abc\"",
                     getText());
    }

    public void testStringLiteral2() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  String attr = \"a\\uFFFFbc\";\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        StringLiteral     literal     = (StringLiteral)initializer.getInitEpression();

        _prettyPrinter.visitStringLiteral(literal);

        assertEquals("\"a\\uFFFFbc\"",
                     getText());
    }

    public void testStringLiteral3() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  String attr = \"a\u0123bc\";\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        StringLiteral     literal     = (StringLiteral)initializer.getInitEpression();

        _prettyPrinter.visitStringLiteral(literal);

        assertEquals("\"a\u0123bc\"",
                     getText());
    }

    public void testStringLiteral4() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  String attr = \"a\\uuu0123b\\uu2345c\";\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        StringLiteral     literal     = (StringLiteral)initializer.getInitEpression();

        _prettyPrinter.visitStringLiteral(literal);

        assertEquals("\"a\\u0123b\\u2345c\"",
                     getText());
    }

    public void testStringLiteral5() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  String attr = \"a\\u0123bc\";\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        StringLiteral     literal     = (StringLiteral)initializer.getInitEpression();

        _prettyPrinter.getOptions().doLitSlimUnicode(false);
        _prettyPrinter.visitStringLiteral(literal);

        assertEquals("\"a\\u0123bc\"",
                     getText());
    }

    public void testStringLiteral6() throws ParseException
    {
        addType("A",
                "class A {\n"+
                "  String attr = \"a\\uuu0123b\\uu2345c\";\n"+
                "}\n",
                true);

        TypeDeclaration   typeDecl    = _project.getType("A");
        FieldDeclaration  fieldDecl   = typeDecl.getFields().get(0);
        SingleInitializer initializer = (SingleInitializer)fieldDecl.getInitializer();
        StringLiteral     literal     = (StringLiteral)initializer.getInitEpression();

        _prettyPrinter.getOptions().doLitSlimUnicode(false);
        _prettyPrinter.visitStringLiteral(literal);

        assertEquals("\"a\\uuu0123b\\uu2345c\"",
                     getText());
    }
}
