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
import jart.analysis.AnalysisFunction;
import jast.analysis.MethodAnalyzer;
import jast.ast.Node;
import jast.ast.Project;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.collections.MethodIterator;


public class IsDataClass implements AnalysisFunction
{
    private final static int PERCENTAGE_OF_ACCESSORS = 90;
    private final static int MIN_FIELD_COUNT         = 0;

    public IsDataClass()
    {
        super();
    }

    public boolean check(Project project, Node node)
    {
        if ((node == null) || !isApplicable(node))
            {
            return false;
        }

        ClassDeclaration clsDecl = (ClassDeclaration) node;

        if (clsDecl.getFields().getCount() <= MIN_FIELD_COUNT)
            {
            return false;
        }

        float numAccessors = getLocalAccessorCount(clsDecl);
        float numMethods = clsDecl.getMethods().getCount();

        if (numAccessors == numMethods)
            {
            return true;
        }

        return (100 * numAccessors >= PERCENTAGE_OF_ACCESSORS * numMethods);
    }

    private int getLocalAccessorCount(ClassDeclaration cls)
    {
        int result = 0;

        for (MethodIterator iter = cls.getMethods().getIterator(); iter.hasNext();)
            {
            if (MethodAnalyzer.isAccessor(iter.getNext()))
                {
                result++;
            }
        }

        return result;
    }

    private boolean isApplicable(Node node)
    {
        return (node instanceof ClassDeclaration);
    }

    public Class[] isApplicableTo()
    {
        return new Class[] { ClassDeclaration.class };
    }
}
