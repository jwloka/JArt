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
package jart.smelling;
import jast.analysis.UsageAnalyzer;
import jast.ast.Project;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.ConstructorIterator;
import jast.ast.nodes.collections.FieldIterator;
import jast.ast.nodes.collections.MethodIterator;

public class IsLazyClass
{
    private static final int MIN_CLASS_SIZE                   = 100;
    private static final int MAX_UNUSED_FEATURE_PERCENTAGE = 50;

    private TypeDeclaration _typeDecl;
    private Project         _project;

    public IsLazyClass(TypeDeclaration typeDecl)
    {
        super();
        _typeDecl = typeDecl;
    }

    private int countLocalConstructors()
    {
        if (_typeDecl instanceof InterfaceDeclaration)
            {
            return 0;
        }

        ClassDeclaration classDecl = (ClassDeclaration) _typeDecl;

        return classDecl.getConstructors() == null
            ? 0
            : classDecl.getConstructors().getCount();
    }

    private int countLocalFeatures()
    {
        return countLocalFields() + countLocalConstructors() + countLocalMethods();
    }

    private int countLocalFields()
    {
        return _typeDecl.getFields() == null ? 0 : _typeDecl.getFields().getCount();
    }

    private int countLocalMethods()
    {
        return _typeDecl.getMethods() == null ? 0 : _typeDecl.getMethods().getCount();
    }

    private int countUnusedConstructors()
    {
        int result = 0;

        if ((_typeDecl instanceof InterfaceDeclaration)
            || (((ClassDeclaration) _typeDecl).getConstructors() == null))
            {
            return result;
        }

        ClassDeclaration clsDecl = (ClassDeclaration) _typeDecl;
        UsageAnalyzer locator = new UsageAnalyzer(_project);

        for (ConstructorIterator iter = clsDecl.getConstructors().getIterator();
            iter.hasNext();
            )
            {
            if (!locator.isUsed(iter.getNext()))
                {
                result++;
            }
        }

        return result;
    }

    private int countUnusedFeatures()
    {
        return countUnusedFields() + countUnusedConstructors() + countUnusedMethods();
    }

    private int countUnusedFields()
    {
        int result = 0;

        if (_typeDecl.getFields() == null)
            {
            return result;
        }

        UsageAnalyzer locator = new UsageAnalyzer(_project);

        for (FieldIterator iter = _typeDecl.getFields().getIterator(); iter.hasNext();)
            {
            if (!locator.isUsed(iter.getNext()))
                {
                result++;
            }
        }

        return result;
    }

    private int countUnusedMethods()
    {
        int result = 0;

        if (_typeDecl.getMethods() == null)
            {
            return result;
        }

        UsageAnalyzer locator = new UsageAnalyzer(_project);

        for (MethodIterator iter = _typeDecl.getMethods().getIterator();
            iter.hasNext();
            )
            {
            if (!locator.isUsed(iter.getNext()))
                {
                result++;
            }
        }

        return result;
    }
}
