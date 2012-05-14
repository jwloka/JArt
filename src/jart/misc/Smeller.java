/*
 * Copyright (C) 2002 Thomas Dudziak, Jan Wloka
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 * documentation files (the "Software"), to deal in the Software without restriction, including without 
 * limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the 
 * Software, and to permit persons to whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions 
 * of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 */
package jart.misc;
import jart.analysis.IsCollectionClass;
import jart.analysis.IsTestClass;
import jart.analysis.IsUtilityClass;
import jart.analysis.IsUtilityMethod;
import jart.smelling.HasAwfullyLongName;
import jart.smelling.HasFeatureEnvy;
import jart.smelling.HasLongParameterList;
import jart.smelling.HasSharedCollection;
import jart.smelling.IsDataClass;
import jart.smelling.IsSpeculativeGeneral;
import jart.smelling.IsUnnecessaryOpen;
import jart.smelling.MultipleSmellLocator;
import jart.smelling.SmellOccurrence;
import jart.smelling.SmellOccurrenceIterator;
import jast.FileParser;
import jast.ProgressObserver;
import jast.analysis.UsageVisitor;
import jast.analysis.Usages;
import jast.ast.ASTHelper;
import jast.ast.ContainedNode;
import jast.ast.Project;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.collections.CompilationUnitIterator;
import jast.resolver.Resolver;

public class Smeller implements ProgressObserver
{
    private Project    _project   = new Project("smeller");
    private FileParser _parser    = new FileParser();
    private Resolver   _resolver  = new Resolver(_parser);
    private boolean    _isParsing = true;

    public Smeller()
    {
        _parser.setParsingInterface(false);
        _resolver.setProject(_project);
    }

    public void addSearchDirectory(String path)
    {
        try
            {
            _parser.addSearchDirectory(path);
        }
        catch (java.io.IOException ex)
            {
        }
    }

    public void finishPackage(String qualifiedName)
    {
    }

    public void finishType(String qualifiedName)
    {
        System.out.println((_isParsing ? "Parsed " : "Resolved ") + qualifiedName);
    }

    public static void main(String[] args)
    {
        if (args.length == 0)
            {
            printUsage();
            return;
        }

        Smeller smeller = new Smeller();

        for (int idx = 0; idx < args.length; idx++)
            {
            if ("-package".equalsIgnoreCase(args[idx]))
                {
                smeller.parsePackage(args[++idx]);
            }
            else if ("-type".equalsIgnoreCase(args[idx]))
                {
                smeller.parseType(args[++idx]);
            }
            else if ("-srcdir".equalsIgnoreCase(args[idx]))
                {
                smeller.addSearchDirectory(args[++idx]);
                System.out.println("Search directory '" + args[idx] + "' added");
            }
            else
                {
                System.out.println("Unkown option " + args[idx]);
            }
        }
        smeller.prepare();
        smeller.smell();
    }

    private void parsePackage(String qualifiedName)
    {
        _parser.addObserver(this);
        _isParsing = true;
        try
            {
            _parser.parsePackage(_project, qualifiedName, true);
        }
        catch (Exception ex)
            {
            ex.printStackTrace();
        }
        _parser.remObserver(this);
    }

    private void parseType(String qualifiedName)
    {
        _parser.addObserver(this);
        _isParsing = true;
        try
            {
            _parser.parseType(_project, qualifiedName);
        }
        catch (Exception ex)
            {
            ex.printStackTrace();
        }
        _parser.remObserver(this);
    }

    private void prepare()
    {
        _parser.setParsingInterface(true);
        _isParsing = false;

        _resolver.addObserver(this);
        _resolver.resolve(_project);
        _resolver.remObserver(this);

        CompilationUnit unit;

        for (CompilationUnitIterator it = _project.getCompilationUnits().getIterator();
            it.hasNext();
            )
            {
            unit = it.getNext();

            if (unit.isComplete())
                {
                if (!unit.hasProperty(Usages.PROPERTY_LABEL))
                    {
                    unit.accept(new UsageVisitor());
                }

                _parser.uncomplete(unit);
                System.out.println("Uncompleted " + unit.getTopLevelQualifiedName());
            }
        }
    }

    private static void printUsage()
    {
        System.out.println("Usage:");
        System.out.println("  java " + Smeller.class.getName() + " [options]");
        System.out.println("with the following options:");
        System.out.println("  Input options");
        System.out.println("    -package <pckgName> Parses the given package");
        System.out.println("    -type    <typeName> Parses the given type");
        System.out.println(
            "    -srcdir  <path>     Uses the given path as a search path\n");
        System.out.println(
            "Note that the package/class options are evaluated immediately which");
        System.out.println(
            "means that only the source directories that have been specified before");
        System.out.println("are used.");
    }

    private void smell()
    {
        MultipleSmellLocator locator = new MultipleSmellLocator();

        locator.addAnalysisFunction(new HasAwfullyLongName());
        locator.addAnalysisFunction(new HasFeatureEnvy());
        locator.addAnalysisFunction(new HasLongParameterList());
        locator.addAnalysisFunction(new HasSharedCollection());
        locator.addAnalysisFunction(new IsCollectionClass());
        locator.addAnalysisFunction(new IsDataClass());
        locator.addAnalysisFunction(new IsSpeculativeGeneral());
        locator.addAnalysisFunction(new IsTestClass());
        locator.addAnalysisFunction(new IsUnnecessaryOpen());
        locator.addAnalysisFunction(new IsUtilityClass());
        locator.addAnalysisFunction(new IsUtilityMethod());

        locator.applyTo(_project);

        SmellOccurrence occurrence;
        CompilationUnit unit;

        for (SmellOccurrenceIterator it = locator.getOccurrences(); it.hasNext();)
            {
            occurrence = it.getNext();
            unit = ASTHelper.getCompilationUnitOf((ContainedNode) occurrence.getNode());

            System.out.print(occurrence.getSmellName());
            System.out.print(" in " + unit.getTopLevelQualifiedName());
            System.out.println(" at " + occurrence.getPosition());
        }
    }

    public void startPackage(String qualifiedName, int numOfTypes)
    {
    }

    public void startType(String qualifiedName)
    {
    }
}
