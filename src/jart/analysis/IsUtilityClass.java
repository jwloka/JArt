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
package jart.analysis;
import jast.ast.Node;
import jast.ast.Project;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.collections.MethodIterator;



public class IsUtilityClass implements AnalysisFunction
{

    private final static float PERCENTAGE_OF_UTILITY_METHODS = 0.75f;

    public IsUtilityClass()
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

        float utilMethodCount = 0;
        float methodCount = clsDecl.getMethods().getCount();

        for (MethodIterator iter = clsDecl.getMethods().getIterator(); iter.hasNext();)
            {
            if (isUtilityMethod(project, iter.getNext()))
                {
                utilMethodCount++;
            }
        }

        return utilMethodCount >= methodCount * PERCENTAGE_OF_UTILITY_METHODS;
    }

    private boolean isApplicable(Node node)
    {
        Class[] applicableClasses = isApplicableTo();

        for (int idx = 0; idx < applicableClasses.length; idx++)
            {
            if (applicableClasses[idx].isInstance(node))
                {
                return true;
            }
        }

        return false;
    }

    public Class[] isApplicableTo()
    {
        return new Class[] { ClassDeclaration.class };
    }

    private boolean isUtilityMethod(Project prj, MethodDeclaration method)
    {
        return new IsUtilityMethod().check(prj, method);
    }
}
