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
import jast.ast.Node;
import jast.ast.Project;
import jast.ast.nodes.InvocableDeclaration;



public class HasLongParameterList implements AnalysisFunction
{
    private final static int MAX_PARAMETER_COUNT = 4;

    public HasLongParameterList()
    {
        super();
    }

    public boolean check(Project project, Node node)
    {
        if ((project == null) || (node == null) || !isApplicable(node))
            {
            return false;
        }

        return (
            getFormalParameterCount((InvocableDeclaration) node) >= MAX_PARAMETER_COUNT);
    }

    private int getFormalParameterCount(InvocableDeclaration invocable)
    {
        if ((invocable == null)
            || (invocable.getParameterList() == null)
            || (invocable.getParameterList().getParameters() == null))
            {
            return 0;
        }

        return invocable.getParameterList().getParameters().getCount();
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
        return new Class[] { InvocableDeclaration.class };
    }
}
