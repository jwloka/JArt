package jast.test.parser;
import jast.ast.nodes.Block;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.Comment;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.ImportDeclaration;
import jast.ast.nodes.Initializer;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.TypeDeclaration;
import antlr.ANTLRException;

public class TestComments extends TestBase
{

    public TestComments(String name)
    {
        super(name);
    }

    public void testComments1() throws ANTLRException
    {
        setupParser("// A compilation unit\n" +
                    "package test;\n"+
                    "import java.io.*; // An import\n" +
                    "import java.net.*;\n" +
                    "/**A class\n" +
                    " * (really)\n"+
                    " */\n\n" +
                    "// not BClass !\n"+
                    "class AClass {}\n" +
                    "/* end of compilation unit */");

        CompilationUnit compilationUnit = _parser.compilationUnit(_helper.getProject());

        _helper.comment(compilationUnit);

        assertEquals(2,
                     compilationUnit.getImportDeclarations().getCount());
        assertEquals(1,
                     compilationUnit.getTypeDeclarations().getCount());
        assertEquals(2,
                     compilationUnit.getComments().getCount());

        Comment comment = compilationUnit.getComments().get(0);

        assertTrue(!comment.isBlockComment());
        assertTrue(!comment.isJavaDocComment());
        assertEquals("A compilation unit",
                     comment.getText());

        comment = compilationUnit.getComments().get(1);

        assertTrue(comment.isBlockComment());
        assertTrue(!comment.isJavaDocComment());
        assertEquals("end of compilation unit",
                     comment.getText());

        ImportDeclaration importDecl = compilationUnit.getImportDeclarations().get(0);

        assertEquals(1,
                     importDecl.getComments().getCount());

        comment = importDecl.getComments().get(0);

        assertTrue(!comment.isBlockComment());
        assertTrue(!comment.isJavaDocComment());
        assertEquals("An import",
                     comment.getText());

        importDecl = compilationUnit.getImportDeclarations().get(1);

        assertEquals(0,
                     importDecl.getComments().getCount());

        TypeDeclaration typeDecl = compilationUnit.getTypeDeclarations().get(0);

        assertEquals(2,
                     typeDecl.getComments().getCount());

        comment = typeDecl.getComments().get(0);

        assertTrue(comment.isBlockComment());
        assertTrue(comment.isJavaDocComment());
        assertEquals("A class\n(really)",
                     comment.getText());
        assertEquals(0,
                     comment.getTags().getCount());

        comment = typeDecl.getComments().get(1);

        assertTrue(!comment.isBlockComment());
        assertTrue(!comment.isJavaDocComment());
        assertEquals("not BClass !",
                     comment.getText());
    }

    public void testComments2() throws ANTLRException
    {
        setupParser("/**\n" +
                    "  * An interface\n" +
                    "  *\n"+
                    "  * @author  John Doe\n"+
                    "  * @version 1.0\n"+
                    "  */\n" +
                    "public interface AnInterface\n" +
                    "{\n" +
                    "// Some attribute\n" +
                    "int CONSTANT = 0; // is public static final\n" +
                    "/* A method */\n" +
                    "void doSomething();\n" +
                    "}");

        InterfaceDeclaration decl = _parser.interfaceDeclaration();

        _helper.comment(decl);

        assertEquals(1,
                     decl.getComments().getCount());

        Comment comment = decl.getComments().get(0);

        assertTrue(comment.isBlockComment());
        assertTrue(comment.isJavaDocComment());
        assertEquals("An interface",
                     comment.getText());
        assertEquals(2,
                     comment.getTags().getCount());
        assertEquals("author  John Doe",
                     comment.getTags().get(0));
        assertEquals("version 1.0",
                     comment.getTags().get(1));

        FieldDeclaration varDecl = decl.getFields().get(0);

        assertEquals(2,
                     varDecl.getComments().getCount());

        comment = varDecl.getComments().get(0);

        assertTrue(!comment.isBlockComment());
        assertTrue(!comment.isJavaDocComment());
        assertEquals("Some attribute",
                     comment.getText());

        comment = varDecl.getComments().get(1);

        assertTrue(!comment.isBlockComment());
        assertTrue(!comment.isJavaDocComment());
        assertEquals("is public static final",
                     comment.getText());

        MethodDeclaration methodDecl = decl.getMethods().get(0);

        assertEquals(1,
                     methodDecl.getComments().getCount());

        comment = methodDecl.getComments().get(0);

        assertTrue(comment.isBlockComment());
        assertTrue(!comment.isJavaDocComment());
        assertEquals("A method",
                     comment.getText());
    }

    public void testComments3() throws ANTLRException
    {
        setupParser("/**\n" +
                    "  * A class\n" +
                    "  */\n" +
                    "public class AClass extends Object" +
                    "{\n" +
                    "  /* Now some attributes */\n"+
                    "\n"+
                    "  // Some attribute\n" +
                    "  int attr = 0;\n" +
                    "  static String VALUE; // is not initialized\n" +
                    "  /* Initializes static attributes */\n" +
                    "  static {\n" +
                    "    VALUE = \"\";\n" +
                    "  }\n" +
                    "  /**\n" +
                    "    * A constructor\n" +
                    "    */\n" +
                    "  public AClass() {}\n" +
                    "  /*A method */\n" +
                    "  abstract void doSomething();\n" +
                    "}");

        ClassDeclaration decl = _parser.classDeclaration(false);

        _helper.comment(decl);

        assertEquals(1,
                     decl.getComments().getCount());

        Comment comment = decl.getComments().get(0);

        assertTrue(comment.isBlockComment());
        assertTrue(comment.isJavaDocComment());
        assertEquals("A class",
                     comment.getText());
        assertEquals(0,
                     comment.getTags().getCount());

        FieldDeclaration varDecl = decl.getFields().get(0);

        assertEquals(2,
                     varDecl.getComments().getCount());

        comment = varDecl.getComments().get(0);

        assertTrue(comment.isBlockComment());
        assertTrue(!comment.isJavaDocComment());
        assertEquals("Now some attributes",
                     comment.getText());

        comment = varDecl.getComments().get(1);

        assertTrue(!comment.isBlockComment());
        assertTrue(!comment.isJavaDocComment());
        assertEquals("Some attribute",
                     comment.getText());

        varDecl = decl.getFields().get(1);

        assertEquals(1,
                     varDecl.getComments().getCount());

        comment = varDecl.getComments().get(0);

        assertTrue(!comment.isBlockComment());
        assertTrue(!comment.isJavaDocComment());
        assertEquals("is not initialized",
                     comment.getText());

        Initializer initializer = decl.getInitializers().get(0);

        assertEquals(1,
                     initializer.getComments().getCount());

        comment = initializer.getComments().get(0);

        assertTrue(comment.isBlockComment());
        assertTrue(!comment.isJavaDocComment());
        assertEquals("Initializes static attributes",
                     comment.getText());

        ConstructorDeclaration constructorDecl = decl.getConstructors().get(0);

        assertEquals(1,
                     constructorDecl.getComments().getCount());

        comment = constructorDecl.getComments().get(0);

        assertTrue(comment.isBlockComment());
        assertTrue(comment.isJavaDocComment());
        assertEquals("A constructor",
                     comment.getText());
        assertEquals(0,
                     comment.getTags().getCount());

        MethodDeclaration methodDecl = decl.getMethods().get(0);

        assertEquals(1,
                     methodDecl.getComments().getCount());

        comment = methodDecl.getComments().get(0);

        assertTrue(comment.isBlockComment());
        assertTrue(!comment.isJavaDocComment());
        assertEquals("A method",
                     comment.getText());
    }

    public void testComments4() throws ANTLRException
    {
        setupParser("// A method\n" +
                    "void doSomething(\n" +
                    "       int    value, // some value\n" +
                    "       String text   // some text\n" +
                    ") /* does something */\n" +
                    "{}\n");

        MethodDeclaration decl = _parser.methodDeclaration();

        _helper.comment(decl);

        assertEquals(1,
                     decl.getComments().getCount());

        Comment comment = decl.getComments().get(0);

        assertTrue(!comment.isBlockComment());
        assertTrue(!comment.isJavaDocComment());
        assertEquals("A method",
                     comment.getText());

        FormalParameter param = decl.getParameterList().getParameters().get(0);

        assertEquals(1,
                     param.getComments().getCount());

        comment = param.getComments().get(0);

        assertTrue(!comment.isBlockComment());
        assertTrue(!comment.isJavaDocComment());
        assertEquals("some value",
                     comment.getText());

        param = decl.getParameterList().getParameters().get(1);

        assertEquals(1,
                     param.getComments().getCount());

        comment = param.getComments().get(0);

        assertTrue(!comment.isBlockComment());
        assertTrue(!comment.isJavaDocComment());
        assertEquals("some text",
                     comment.getText());

        Block block = decl.getBody();

        assertEquals(1,
                     block.getComments().getCount());

        comment = block.getComments().get(0);

        assertTrue(comment.isBlockComment());
        assertTrue(!comment.isJavaDocComment());
        assertEquals("does something",
                     comment.getText());
    }
}
