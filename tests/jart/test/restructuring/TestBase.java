package jart.test.restructuring;
import jart.restructuring.Restructuring;
import jart.restructuring.RestructuringResults;
import jast.ParseException;
import jast.ast.ASTHelper;
import jast.ast.Node;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.CompilationUnitArray;
import jast.ast.nodes.collections.PackageArray;
import jast.prettyprinter.PrettyPrinter;
import jast.prettyprinter.SimpleStyleWriter;

import java.io.StringWriter;

// we wrap all accesses to the restructuring and its results
// in order to make the tests more readable and changeable
public abstract class TestBase extends jart.test.TestBase
{
    private Restructuring        _restructuring;
    private RestructuringResults _results;

    public TestBase(String name)
    {
        super(name);
    }

    protected boolean analyze()
    {
        return _restructuring.analyze();
    }

    public CompilationUnitArray getChangedUnits()
    {
        return _results.getChangedUnits();
    }

    public PackageArray getNewPackages()
    {
        return _results.getNewPackages();
    }

    public CompilationUnitArray getNewUnits()
    {
        return _results.getNewUnits();
    }

    public PackageArray getRemovedPackages()
    {
        return _results.getRemovedPackages();
    }

    public CompilationUnitArray getRemovedUnits()
    {
        return _results.getRemovedUnits();
    }

    protected boolean hasErrors()
    {
        return _results.hasErrors();
    }

    protected boolean hasFatalErrors()
    {
        return _results.hasFatalErrors();
    }

    protected boolean hasNotes()
    {
        return _results.hasNotes();
    }

    protected boolean hasWarnings()
    {
        return _results.hasWarnings();
    }

    protected boolean initialize(Node[] nodes, Object[] processingData)
    {
        return _restructuring.initialize(nodes, processingData);
    }

    protected boolean perform()
    {
        return _restructuring.perform();
    }

    protected String prettyPrint(String typeName)
    {
        TypeDeclaration typeDecl = _project.getType(typeName);

        if (typeDecl == null)
            {
            return "";
        }

        StringWriter writer = new StringWriter();
        PrettyPrinter prettyPrinter = new PrettyPrinter(new SimpleStyleWriter(writer));

        ASTHelper.getCompilationUnitOf(typeDecl).accept(prettyPrinter);

        return writer.getBuffer().toString();
    }

    protected void setRestructuring(Restructuring restructuring)
    {
        _results       = new RestructuringResults();
        _restructuring = restructuring;
        _restructuring.setResultContainer(_results);
    }

    protected void tearDown() throws ParseException
    {
        _restructuring = null;
        super.tearDown();
    }

    protected String toUnitName(String typeName)
    {
        return java.io.File.separator +
               typeName.replace('.', java.io.File.separatorChar) +
               ".java";
    }
}
