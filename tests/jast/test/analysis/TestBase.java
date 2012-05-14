package jast.test.analysis;

import jast.Global;
import jast.ParseException;
import jast.analysis.UsageVisitor;
import jast.ast.Project;
import jast.resolver.Resolver;
import jast.test.resolver.StringParser;
import junitx.framework.PrivateTestCase;


public abstract class TestBase extends PrivateTestCase
{
    protected Project      _project;
    protected StringParser _parser;
    protected Resolver       _resolver;

    public TestBase(String name)
    {
        super(name);
    }

    protected void addType(String qualifiedName, String compUnit, boolean doParse)
        throws ParseException
    {
        _parser.addType(qualifiedName, compUnit);
        if (doParse)
            {
            _parser.parseType(_project, qualifiedName);
        }
    }

    public void addUsages()
    {
        new UsageVisitor().visitProject(_project);
    }

    public void resolve()
    {
        _resolver.resolve(_project);
    }

    protected void setUp() throws ParseException
    {
        _project = Global.getFactory().createProject("Test");
        _parser = new StringParser();
        _resolver = new Resolver(_parser);
    }

    protected void tearDown() throws ParseException
    {
        _parser = null;
        _project = null;
        _resolver = null;
    }
}
