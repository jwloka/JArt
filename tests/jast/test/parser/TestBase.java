package jast.test.parser;
import jast.Global;
import jast.SyntaxException;
import jast.ast.NodeFactory;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.PrimitiveType;
import jast.ast.nodes.Type;
import jast.parser.CommentFilter;
import jast.parser.JavaLexer;
import jast.parser.JavaParser;
import jast.parser.ParserHelper;
import jast.parser.UnicodeReader;

import java.io.StringReader;

public abstract class TestBase extends jast.test.TestBase
{
    protected ParserHelper _helper;
    protected JavaParser   _parser;
    private   JavaLexer    _lexer;

    public TestBase(String name)
    {
        super(name);
    }

    protected void defineField(String name) throws SyntaxException
    {
        defineField(name, Global.getFactory().createType(PrimitiveType.INT_TYPE, 0));
    }

    protected void defineField(String name, Type type) throws SyntaxException
    {
        NodeFactory      factory   = Global.getFactory();
        FieldDeclaration fieldDecl = factory.createFieldDeclaration(factory.createModifiers(),
                                                                    type,
                                                                    name,
                                                                    null);
        _helper.defineVariable(fieldDecl);
    }

    protected void defineVariable(String name, Type type, boolean isFinal) throws SyntaxException
    {
        LocalVariableDeclaration localVar = Global.getFactory().createLocalVariableDeclaration(isFinal, type, name, null);

        _helper.defineVariable(localVar);
    }

    protected void defineVariable(String name, boolean isFinal) throws SyntaxException
    {
        defineVariable(name,
                       Global.getFactory().createType("java.lang.String", 0),
                       isFinal);
    }

    protected void setUp()
    {
        _helper = new ParserHelper();
        _helper.setProject(Global.getFactory().createProject("Test"));
        _helper.setUnitName("SomeClass.java");
        _helper.pushTypeScope(null);
        _helper.pushBlockScope();
    }

    protected void setupParser(String text)
    {
        _lexer  = new JavaLexer(new UnicodeReader(new StringReader(text)));
        _parser = new JavaParser(new CommentFilter(_lexer, _helper));
        _parser.setHelper(_helper);
    }

    protected void tearDown()
    {
        if (_helper != null)
        {
            _helper = null;
        }
        _parser = null;
        _lexer = null;
    }


    protected void defineLabel(String name) throws SyntaxException
    {
        _helper.defineLabel(Global.getFactory().createLabeledStatement(name));
    }
}
