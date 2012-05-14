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
package jast.profiler;

import jast.BasicMessageContainer;
import jast.Global;
import jast.ProgressObserver;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.collections.CompilationUnitIterator;
import jast.parser.ParserApplicationHelper;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class Profiler implements ProgressObserver
{
    private ParserApplicationHelper _helper    = new ParserApplicationHelper("profiler");
    private Sizer                   _sizer     = new Sizer();
    private NumberFormat            _formatter = NumberFormat.getInstance();
    private Vector                  _unitNames;
    private Hashtable               _parseInfos;
    private long                    _time;
    private boolean                 _isPackage = false;

    public Profiler()
    {
        reset();
        if (_formatter instanceof DecimalFormat)
        {
            DecimalFormat deciFormat = (DecimalFormat)_formatter;

            deciFormat.applyPattern("0.0000");
            deciFormat.setGroupingUsed(false);
        }
    }

    public void addSearchDirectory(String path)
    {
        _helper.addSearchDirectory(path);
    }

    public void finishPackage(String qualifiedName)
    {
        System.out.println();
        System.out.flush();
    }

    public void finishType(String qualifiedName)
    {
        ParseInfo info = getParseInfo(qualifiedName);

        if (_helper.isParsing())
        {
            if (info.getParseTime() == 0l)
            {
                info.setParseTime(System.currentTimeMillis() - _time);
                _unitNames.addElement(qualifiedName);
            }
            if (_isPackage)
            {
                System.out.print(".");
                System.out.flush();
            }
        }
        else
        {
            if (info.getResolveTime() == 0l)
            {
                info.setResolveTime(System.currentTimeMillis() - _time);
            }
        }
    }

    private String format(double value, char dotChar)
    {
        return _formatter.format(value).replace(',', '.').replace('.', dotChar);
    }

    private ParseInfo getParseInfo(String unitName)
    {
        ParseInfo result = (ParseInfo)_parseInfos.get(unitName);

        if (result == null)
        {
            result = new ParseInfo(unitName);
            _parseInfos.put(unitName, result);
        }
        return result;
    }

    public static void main(String[] args) throws Exception
    {
        if (args.length == 0)
        {
            printUsage();
            return;
        }

        new Profiler().perform(args);
    }

    public void perform(String[] args) throws Exception
    {
        if (args.length == 0)
        {
            return;
        }

        boolean  genText     = true;
        boolean  doResolving = false;
        char     dotChar     = '.';

        for (int idx = 0; idx < args.length; idx++)
        {
            if ("-resolve".equalsIgnoreCase(args[idx]))
            {
                doResolving = true;
            }
            else if ("-textual".equalsIgnoreCase(args[idx]))
            {
                System.out.println("Generating textual output");
                genText = true;
            }
            else if ("-csv".equalsIgnoreCase(args[idx]))
            {
                System.out.println("Generating csv output");
                genText = false;
                if ((idx < args.length - 1) && "comma".equalsIgnoreCase(args[idx+1]))
                {
                    dotChar = ',';
                    idx++;
                }
                else
                {
                    dotChar = '.';
                }
            }
        }
        _helper.addObserver(this);
        _isPackage = false;
        if (_helper.perform(args))
        {
            CompilationUnit unit;
            File            file;
            ParseInfo       info;

            for (CompilationUnitIterator it = _helper.getProject().getCompilationUnits().getIterator(); it.hasNext();)
            {
                unit = it.getNext();
                file = new File(unit.getName());
                info = getParseInfo(unit.getTopLevelQualifiedName());

                info.setAstSize(_sizer.getSize(unit));
                info.setFileSize(file.length());
            }
            printResults(genText, doResolving, dotChar);
        }
    }

    private void printMessages()
    {
        ((BasicMessageContainer)Global.getOutput()).printAll();
    }

    public void printResults(boolean asText, boolean withResolving, char dotChar)
    {
        ParseInfo info;
        String    parseTime    = null;
        String    sizeFile     = null;
        String    sizeAST      = null;
        String    resolveTime  = null;
        String    sizeResolved = null;

        if (!asText)
        {
            System.out.print("compilation unit;parse time (s);file size (KB);ast size (KB)");
            if (withResolving)
            {
                System.out.print(";resolve time (s);resolved ast size (KB)");
            }
            System.out.println();
        }
        for (Enumeration en = _unitNames.elements(); en.hasMoreElements();)
        {
            info      = (ParseInfo)_parseInfos.get(en.nextElement());
            parseTime = format((double)info.getParseTime()/1000.0, dotChar);
            sizeFile  = format((double)info.getFileSize()/1024.0, dotChar);
            sizeAST   = format((double)info.getAstSize()/1024.0, dotChar);
            if (withResolving)
            {
                resolveTime  = format((double)info.getResolveTime()/1000.0, dotChar);
                sizeResolved = format((double)info.getResolvedAstSize()/1000.0, dotChar);
            }
            if (asText)
            {
                System.out.print(info.getUnitName()+": "+
                                 parseTime+"s for parsing "+
                                 sizeFile+"KB (file) -> "+
                                 sizeAST+"KB AST");
                if (withResolving)
                {
                    System.out.print(" | "+resolveTime+"s for resolving -> "+
                                     sizeResolved+"KB resolved AST");
                }
                System.out.println();
            }
            else
            {
                System.out.print(info.getUnitName()+";"+
                                 parseTime+";"+
                                 sizeFile+";"+
                                 sizeAST);
                if (withResolving)
                {
                    System.out.print(";"+resolveTime+";"+
                                     sizeResolved);
                }
                System.out.println();
            }
        }
    }

    private static void printUsage()
    {
        System.out.println("Usage:");
        System.out.println("  java "+Profiler.class.getName()+" [options]");
        System.out.println("with the following options:");
        System.out.println("  Input options");
        System.out.println("    -package <pckgName> Parses the given package");
        System.out.println("    -type    <typeName> Parses the given type");
        System.out.println("    -srcdir  <path>     Uses the given path as a search path\n");
        System.out.println("  Processing options");
        System.out.println("    -interface Parses only the interfaces of the types (no bodies)");
        System.out.println("    -resolve   Also resolves the parsed types\n");
        System.out.println("  Output options");
        System.out.println("    -textual     The output is in textual form (default)");
        System.out.println("    -csv [comma] A semi-colon separated list is generated (for sheets)\n");
        System.out.println("                 Per default dots are used for the numbers but commas \n");
        System.out.println("                 can be used if desired\n");
        System.out.println("Note that the package/class options are evaluated immediately which");
        System.out.println("means that only the source directories that have been specified before");
        System.out.println("are used.");
    }

    public void reset()
    {
        _unitNames  = new Vector();
        _parseInfos = new Hashtable();
        _time       = 0l;
    }

    public void setParsingInterface(boolean onlyInterfaces)
    {
        _helper.setParsingInterface(onlyInterfaces);
    }

    public void startPackage(String qualifiedName, int numOfTypes)
    {
        _isPackage = true;
        System.out.print("Parsing package "+qualifiedName+" ");
        System.out.flush();
    }

    public void startType(String qualifiedName)
    {
        if (_helper.isParsing())
        {
            System.out.println("Parsing type "+qualifiedName);
        }
        else
        {
            System.out.println("Resolving type "+qualifiedName);
        }
        _time = System.currentTimeMillis();
    }
}
