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
package jast.resolver;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.ImportDeclaration;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.ImportIterator;
import jast.helpers.StringArray;
import jast.helpers.StringIterator;
import jast.parser.ScopeStack;

import java.util.Hashtable;

public class CompilationUnitScope extends ScopeStack
{
    private CompilationUnit      _curCompUnit       = null;
    private StringArray          _imports           = new StringArray();
    // The name map: short name -> qualified name
    private Hashtable            _directImportNames = new Hashtable();
    // The already retrieved direct imports indexed by the short name
    private Hashtable            _directImports     = new Hashtable();

    public CompilationUnitScope(CompilationUnit node)
    {
        _imports.add("java.lang");
        _curCompUnit = node;

        ImportDeclaration curImport;
        String            name;
        int               pos;

        for (ImportIterator it = node.getImportDeclarations().getIterator(); it.hasNext();)
        {
            curImport = it.getNext();
            name      = curImport.getImportedPackageOrType();
            if (curImport.isOnDemand())
            {
                _imports.add(name);
            }
            else
            {
                // The directly imported type can be accessed by the last
                // part only regardless of whether it is a top-level
                // or nested type
                pos = name.lastIndexOf(".");
                if (pos > 0)
                {
                    _directImportNames.put(name.substring(pos+1), curImport);
                }
                else
                {
                    _directImportNames.put(name, curImport);
                }
            }
        }
    }

    public CompilationUnit getCompilationUnit()
    {
        return _curCompUnit;
    }

    public TypeDeclaration getDirectImport(String unqualifiedName)
    {
        return (TypeDeclaration)_directImports.get(unqualifiedName);
    }

    public ImportDeclaration getDirectImportName(String unqualifiedName)
    {
        return (ImportDeclaration)_directImportNames.get(unqualifiedName);
    }

    public StringIterator getImports()
    {
        return _imports.getIterator();
    }

    public void setDirectImport(String unqualifiedName, TypeDeclaration typeDecl)
    {
        _directImports.put(unqualifiedName, typeDecl);
    }
}
