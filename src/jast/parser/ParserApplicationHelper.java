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
package jast.parser;

import jast.FileParser;
import jast.ProgressObserver;
import jast.ast.Project;
import jast.resolver.Resolver;

import java.io.IOException;

// Provides convenience functionality common to all
// applications that work with the JAST ast and source files
public class ParserApplicationHelper
{
    private Project    _project                 = new Project("profiler");
    private FileParser _parser                  = new FileParser();
    private Resolver   _resolver                = new Resolver(_parser);
    private boolean    _resolvingWithInterfaces = true;
    private boolean    _isParsing               = false;
    private boolean    _isResolving             = false;

    public ParserApplicationHelper(String projectName)
    {
        _project = new Project(projectName);
    }

    public void addObserver(ProgressObserver observer)
    {
        _parser.addObserver(observer);
        _resolver.addObserver(observer);
    }

    public void addSearchDirectory(String path)
    {
        try
        {
            _parser.addSearchDirectory(path);
        }
        catch (IOException ex)
        {}
    }

    public Project getProject()
    {
        return _project;
    }

    public boolean isParsing()
    {
        return _isParsing;
    }

    public boolean isResolving()
    {
        return _isResolving;
    }

    public boolean parsePackage(String qualifiedName)
    {
        boolean result = false;

        _isParsing = true;
        try
        {
            result = _parser.parsePackage(_project, qualifiedName, true);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        _isParsing = false;

        return result;
    }

    public boolean parseType(String qualifiedName)
    {
        boolean result = false;

        _isParsing = true;
        try
        {
            result = _parser.parseType(_project, qualifiedName);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        _isParsing = false;

        return result;
    }

    public boolean perform(String[] args) throws Exception
    {
        boolean didWork     = false;
        boolean doResolving = false;

        for (int idx = 0; idx < args.length; idx++)
        {
            if ("-package".equalsIgnoreCase(args[idx]))
            {
                if (parsePackage(args[++idx]))
                {
                    didWork = true;
                }
            }
            else if ("-type".equalsIgnoreCase(args[idx]))
            {
                if (parseType(args[++idx]))
                {
                    didWork = true;
                }
            }
            else if ("-srcdir".equalsIgnoreCase(args[idx]))
            {
                addSearchDirectory(args[++idx]);
            }
            else if ("-interface".equalsIgnoreCase(args[idx]))
            {
                setParsingInterface(true);
            }
            else if ("-resolve".equalsIgnoreCase(args[idx]))
            {
                doResolving = true;
            }
        }
        if (doResolving)
        {
            resolve();
        }
        return didWork;
    }

    public void remObserver(ProgressObserver observer)
    {
        _parser.remObserver(observer);
        _resolver.remObserver(observer);
    }

    public void resolve()
    {
        boolean interfaceParsing = _parser.isParsingInterfaceOnly();

        _isResolving = true;
        _parser.setParsingInterface(_resolvingWithInterfaces);
        _resolver.resolve(_project);
        _parser.setParsingInterface(interfaceParsing);
        _isResolving = false;
    }

    public void setParsingInterface(boolean onlyInterfaces)
    {
        _parser.setParsingInterface(onlyInterfaces);
    }

    public void setResolvingWithInterfaces(boolean onlyInterfaces)
    {
        _resolvingWithInterfaces = onlyInterfaces;
    }
}
