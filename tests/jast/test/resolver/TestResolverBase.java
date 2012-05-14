package jast.test.resolver;
import jast.FileParser;
import jast.Global;
import jast.SyntaxException;
import jast.ast.Project;
import jast.ast.nodes.CompilationUnit;
import jast.resolver.Resolver;
import junitx.framework.PrivateTestCase;
import junitx.framework.TestAccessException;

public abstract class TestResolverBase extends PrivateTestCase
{
    protected Project              _project;
    protected Resolver             _resolver;
    protected CompilationUnit      _curUnit;

    public TestResolverBase(String name)
    {
        super(name);
    }

    protected void setUp() throws SyntaxException, TestAccessException
    {
        _project  = Global.getFactory().createProject("Test");
        _curUnit  = Global.getFactory().createCompilationUnit("Test.java");

        _curUnit.setPackage(_project.ensurePackage("test"));
        _resolver = new Resolver(new FileParser());
        _resolver.setProject(_project);

        // Resolver.startCompilationUnit is private, so we use
        // the private-access facility of JunitX to invoke it

        Object[] args = { _curUnit };

        this.invoke(_resolver, "startUnitScope", args);
    }

    protected void tearDown()
    {
        try
        {
            // Resolver.finishCompilationUnit is private, so we use
            // the private-access facility of JunitX to invoke it
            this.invoke(_resolver, "stopUnitScope", null);
        }
        catch (TestAccessException ex)
        {}

        _curUnit  = null;
        _project  = null;
        _resolver = null;
    }
}
