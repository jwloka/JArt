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
package jast.prettyprinter;

import jast.BasicMessageContainer;
import jast.FileParser;
import jast.Global;
import jast.ast.Project;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.collections.CompilationUnitIterator;
import jast.helpers.StringArray;
import jast.helpers.StringIterator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main
{
    private static final int PARSEMODE_TYPE      = 0;
    private static final int PARSEMODE_FILE      = 1;
    private static final int PARSEMODE_PACKAGE   = 2;
    private static final int PARSEMODE_DIRECTORY = 3;

    private Project           _project       = new Project("prettyprint");
    private FileParser        _parser        = new FileParser();
    private StringArray       _names         = new StringArray();
    private PrettyPrinter     _printer       = new PrettyPrinter();
    private SimpleStyleWriter _writer;
    private String            _targetPath    = null;
    private int               _parseMode     = PARSEMODE_TYPE;
    private boolean           _recursive     = false;
    private boolean           _withResolving = false;
    private boolean           _asHTML        = false;

    public Main()
    {
        Global.setParser(_parser);
    }

    public void addName(String name)
    {
        _names.add(name);
    }

    public void addSearchDirectory(String path)
    {
        try
        {
            _parser.addSearchDirectory(path);
        }
        catch (IOException ex)
        {
            Global.getOutput().addError("Path "+path+" does not denote a valid directory", null);
        }
    }

    public void doGenerateHTML(boolean doIt)
    {
        _asHTML = doIt;
    }

    public void doParseDirectories()
    {
        _parseMode = PARSEMODE_DIRECTORY;
    }

    public void doParseFiles()
    {
        _parseMode = PARSEMODE_FILE;
    }

    public void doParsePackages()
    {
        _parseMode = PARSEMODE_PACKAGE;
    }

    public void doParseRecursive(boolean doIt)
    {
        _recursive = doIt;
    }

    public void doParseTypes()
    {
        _parseMode = PARSEMODE_TYPE;
    }

    public void doResolve(boolean doIt)
    {
        _withResolving = doIt;
    }

    private File ensureDirForPackage(String pckgName)
    {
        StringArray parts = StringArray.fromString(pckgName, ".");
        File        dir   = new File(_targetPath);

        for (StringIterator it = parts.getIterator(); it.hasNext();)
        {
            dir = new File(dir, it.getNext());
            if ((dir.exists() && (!dir.canWrite() || !dir.isDirectory())) ||
                (!dir.exists() && !dir.mkdir()))
            {
                Global.getOutput().addError("Cannot create directory "+dir.getAbsolutePath(), null);
                return null;
            }
        }
        return dir;
    }

    private int getDepth(String pckgName)
    {
        return StringArray.fromString(pckgName, ".").getCount();
    }

    private String getRelativePathToTargetDir(String pckgName)
    {
        StringBuffer result = new StringBuffer(".");
        int          depth  = StringArray.fromString(pckgName, ".").getCount();

        for (; depth > 0; depth--)
        {
            result.append(File.separator);
            result.append("..");
        }
        result.append(File.separator);

        return result.toString();
    }

    public static void main(String[] args) throws Exception
    {
        if (args.length == 0)
        {
            printHelp();
            return;
        }

        Main   app = new Main();
        String option;

        for (int idx = 0; idx < args.length; idx++)
        {
            option = args[idx].toLowerCase();
            if (option.startsWith("-?"))
            {
                printHelp();
                return;
            }
            if (option.startsWith("-c"))
            {
                app.doResolve(true);
            }
            else if (option.startsWith("-h"))
            {
                app.doGenerateHTML(true);
            }
            else if (option.startsWith("-i") && (idx < args.length-1))
            {
                app.addSearchDirectory(args[++idx]);
            }
            else if (option.startsWith("-o") && (idx < args.length-1))
            {
                app.setTarget(args[++idx]);
            }
            else if (option.startsWith("-f"))
            {
                app.doParseFiles();
            }
            else if (option.startsWith("-d"))
            {
                app.doParseDirectories();
            }
            else if (option.startsWith("-t"))
            {
                app.doParseTypes();
            }
            else if (option.startsWith("-p"))
            {
                app.doParsePackages();
            }
            else if (option.startsWith("-r"))
            {
                app.doParseRecursive(true);
            }
            else
            {
                app.addName(args[idx]);
            }
        }
        app.print(app.parse());
        ((BasicMessageContainer)Global.getOutput()).printAll(new PrintWriter(System.err));
    }

    private StringArray parse()
    {
        _parser.setParsingInterface(false);

        StringArray parsedFiles = new StringArray();
        String      name;

        for (StringIterator it = _names.getIterator(); it.hasNext();)
        {
            name = it.getNext();
            try
            {
                switch (_parseMode)
                {
                    case PARSEMODE_TYPE :
                        _parser.parseType(_project, name);
                        break;
                    case PARSEMODE_FILE :
                        _parser.parseFile(_project, name);
                        break;
                    case PARSEMODE_PACKAGE :
                        _parser.parsePackage(_project, name, _recursive);
                        break;
                    case PARSEMODE_DIRECTORY :
                        _parser.parseDirectory(_project, name, _recursive);
                        break;
                }
            }
            catch (Exception ex)
            {
                Global.getOutput().addError(ex.toString(), null);
            }
        }

        // we must get the names of all parsed files prior to resolving
        // as this step naturally adds more types to the project but we
        // should not print them
        for (CompilationUnitIterator it = _project.getCompilationUnits().getIterator(); it.hasNext();)
        {
            parsedFiles.add(it.getNext().getName());
        }

        // now we can resolve if desired
        if (_withResolving)
        {
            Global.getResolver().resolve(_project);
        }

        return parsedFiles;
    }

    private void print(StringArray unitNames)
    {
        if (_asHTML)
        {
            _writer = new HtmlStyleWriter();
        }
        else
        {
            _writer = new SimpleStyleWriter();
        }
        _printer.setStyleWriter(_writer);

        // stdout ?
        if (_targetPath == null)
        {
            printToStdout(unitNames);
        }
        else
        {
            printToFiles(unitNames);
        }
    }

    private static void printHelp()
    {
        System.out.println("Usage:");
        System.out.println("  java jast.prettyprinter.Main [options] <name1> <name2> ...");
        System.out.println();
        System.out.println("Input options:");
        System.out.println("  -f        All names denote files");
        System.out.println("  -d        All names denote directories");
        System.out.println("  -t        All names denote types (use -i for search directories)");
        System.out.println("            Note that this is the default");
        System.out.println("  -p        All names denote packages (use -i for search directories)");
        System.out.println("  -r        Enables recursive parsing for directories/packages");
        System.out.println("Output options:");
        System.out.println("  -h        Directs the prettyprinter to generate HTML instead of text");
        System.out.println("  -o <path> Specified the output directory. If none is set, then the");
        System.out.println("            output is printed to stdout. Note that the output dir must");
        System.out.println("            exist and must be writable");
        System.out.println("Misc options:");
        System.out.println("  -?        Prints this help");
        System.out.println("  -i <path> Specifies an include directory which is searched for types");
        System.out.println("  -c        Forces the prettyprinter to resolve the parsed types prior");
        System.out.println("            to printing");
    }

    private void printHTMLFoot(PrintWriter output)
    {
        output.println("</pre></body></html>");
    }

    private void printHTMLHead(PrintWriter output, String toTargetDir)
    {
        output.println("<html><head>");
        output.print("<link rel='stylesheet' type='text/css' href='");
        if (toTargetDir != null)
        {
            output.print(toTargetDir);
        }
        output.println("javasourcecode.css'>");
        output.println("</head><body><pre>");
    }

    private void printStylesheet()
    {
        File file = new File(_targetPath + File.separator + "javasourcecode.css");

        try
        {
            PrintWriter output = new PrintWriter(new FileWriter(file));

            output.println("span.normal                 { color: black }");
            output.println("span.keyword                { color: blue }");
            output.println("span.modifier               { color: darkblue }");
            output.println("span.label                  { color: black }");
            output.println("span.typeidentifier         { color: red }");
            output.println("span.fieldidentifier        { color: darkred }");
            output.println("span.constructoridentifier  { color: darkred }");
            output.println("span.constructoridentifier  { color: darkred }");
            output.println("span.variableidentifier     { color: darkred }");
            output.println("span.linecomment            { color: green }");
            output.println("span.blockcomment           { color: green }");
            output.println("span.javadoccomment         { color: green }");
            output.println("span.characterliteral       { color: gray }");
            output.println("span.stringliteral          { color: gray }");
            output.println("span.numberliteral          { color: gray }");
            output.println("span.booleanliteral         { color: gray }");
            output.println("span.nullliteral            { color: gray }");
            output.println("span.operator               { color: black }");
            output.println("span.unresolvedaccess       { color: orange }");
            output.close();
        }
        catch (IOException ex)
        {
            Global.getOutput().addError("Could not write stylesheet to file "+file.getAbsolutePath(), null);
        }
    }

    private void printToFiles(StringArray unitNames)
    {
        PrintWriter     output = null;
        String          name;
        CompilationUnit unit;
        File            dir;
        File            file;

        if (_asHTML)
        {
            printStylesheet();
        }
        for (StringIterator it = unitNames.getIterator(); it.hasNext();)
        {
            name = it.getNext();

            // we must avoid the array type
            if ("[]".equals(name))
            {
                continue;
            }

            unit = _project.getCompilationUnits().get(name);
            dir  = ensureDirForPackage(unit.getPackage().getQualifiedName());
            if (dir == null)
            {
                continue;
            }
            name = unit.getTopLevelName();
            if (name == null)
            {
                // no top-level type in the unit
                continue;
            }
            if (_asHTML)
            {
                name += ".html";
            }
            else
            {
                name += ".java";
            }
            file = new File(dir, name);
            try
            {

                output = new PrintWriter(new FileWriter(file));
            }
            catch (IOException ex)
            {
                Global.getOutput().addError("Cannot open file "+file.getAbsolutePath(), null);
                continue;
            }
            _writer.setOutput(output);
            if (_asHTML)
            {
                printHTMLHead(output, getRelativePathToTargetDir(unit.getPackage().getQualifiedName()));
            }
            unit.accept(_printer);
            if (_asHTML)
            {
                printHTMLFoot(output);
            }
            output.close();
        }
    }

    private void printToStdout(StringArray unitNames)
    {
        PrintWriter output = new PrintWriter(System.out, true);
        String      name;

        _writer.setOutput(output);
        if (_asHTML)
        {
            printHTMLHead(output, null);
        }
        for (StringIterator it = unitNames.getIterator(); it.hasNext();)
        {
            name = it.getNext();

            // we must avoid the array type
            if ("[]".equals(name))
            {
                continue;
            }

            _project.getCompilationUnits().get(name).accept(_printer);
            output.flush();
            if (it.hasNext())
            {
                output.println();
            }
        }
        if (_asHTML)
        {
            printHTMLFoot(output);
        }
    }

    public void setTarget(String path)
    {
        _targetPath = path;
    }
}
